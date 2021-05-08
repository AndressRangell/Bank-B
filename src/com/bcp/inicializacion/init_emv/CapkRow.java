package com.bcp.inicializacion.init_emv;
import android.content.Context;
import android.database.Cursor;
import com.bcp.inicializacion.sqlite.frompackage.DBHelper;
import com.bcp.inicializacion.tools.PolarisUtil;
import com.newpos.libpay.Logger;
import com.pos.device.emv.CAPublicKey;
import com.pos.device.emv.EMVHandler;
import com.pos.device.emv.IEMVHandler;
import static org.jpos.stis.Util.hex2byte;
import static org.jpos.stis.Util.hexString;


/**
 *
 * @author francisco
 */
public class CapkRow {

    private String subType;
    private String len;
    private String keyIdx;
    private String rId;
    private String exPonent;
    private String keySize;
    private String key;
    private String expiryDate;
    private String effectDate;
    private String chksum;
    private String sha1;
    private boolean showDebug = false;

    private static final String TAG_EMVINIT = "emvinit";

    private static String[] fields = new String[]{
            "KEY_SUBTYPE",
            "KEY_LEN",
            "KEY_ID",
            "KEY_RID",
            "KEY_EXPONENT",
            "KEY_SIZE",
            "KEY_MODULE",
            "KEY_EXPIRATION_DATE",
            "KEY_EFFECTUATION_DATE",
            "KEY_CHKSUM",
            "KEY_SHA1"
    };

    private static CapkRow capkRow;

    private void setCAPKROW(String column, String value) {
        switch (column) {
            case "KEY_SUBTYPE":
                setSubType(value);
                break;
            case "KEY_LEN":
                setLen(value);
                break;
            case "KEY_ID":
                setKeyIdx(value);
                break;
            case "KEY_RID":
                setRID(value);
                break;
            case "KEY_EXPONENT":
                setExponent(value);
                break;
            case "KEY_SIZE":
                setKeySize(value);
                break;
            case "KEY_MODULE":
                setKey(value);
                break;
            case "KEY_EXPIRATION_DATE":
                setExpiryDate(value);
                break;
            case "KEY_EFFECTUATION_DATE":
                setEffectDate(value);
                break;
            case "KEY_CHKSUM":
                setChksum(value);
                break;
            case "KEY_SHA1":
                setSha1(value);
                break;
            default:
                break;
        }

    }

    private boolean checkSigned() {
        StringBuilder textToVerify = new StringBuilder();
        textToVerify.append(this.subType);
        textToVerify.append(this.len);
        textToVerify.append(this.keyIdx);
        textToVerify.append(this.rId);
        textToVerify.append(this.exPonent);
        textToVerify.append(this.keySize);
        textToVerify.append(this.key);
        textToVerify.append(this.expiryDate);
        textToVerify.append(this.effectDate);
        textToVerify.append(this.chksum);
        textToVerify.append(this.sha1);
        return true;
    }

    private void clearCAPKROW() {
        for (String s : CapkRow.fields) {
            setCAPKROW(s, "");
        }
    }

    public boolean selectCAPKROW(Context context) {
        boolean ok = false;
        DBHelper databaseAccess = new DBHelper(context, PolarisUtil.NAME_DB, null, 1);
        databaseAccess.openDb(PolarisUtil.NAME_DB);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        int counter = 1;
        for (String s : CapkRow.fields) {
            sql.append(s);
            if (counter++ < CapkRow.fields.length) {
                sql.append(",");
            }
        }
        sql.append(" from CAPKS");
        sql.append(";");

        try {

            Cursor cursor = databaseAccess.rawQuery(sql.toString());

            cursor.moveToFirst();
            int indexColumn;
            IEMVHandler emvHandler = EMVHandler.getInstance();
            while (!cursor.isAfterLast()) {
                clearCAPKROW();
                indexColumn = 0;
                for (String s : CapkRow.fields) {
                    setCAPKROW(s, cursor.getString(indexColumn++));
                }

                int rta = fillCapks(emvHandler);

                if (showDebug) {
                    Logger.debug(TAG_EMVINIT + "\n" + this.toString());
                    Logger.debug(TAG_EMVINIT + " CAPK_ROW checkSigned: " + (this.checkSigned() ? "true" : "false"));
                    Logger.debug("");
                    Logger.debug("emvinit " + "load capk index:  " + this.getKeyIdx() + " - Result: " + rta);
                }

                ok = true;
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
        databaseAccess.closeDb();
        return ok;
    }

    private int fillCapks(IEMVHandler emvHandler){

        CAPublicKey caPublicKey = new CAPublicKey();
        caPublicKey.setRID(hex2byte(this.getRID()));
        caPublicKey.setIndex(Integer.parseInt(this.getKeyIdx(), 16));

        int moduleLength = Integer.parseInt(this.getKeySize(), 16);
        caPublicKey.setLenOfModulus(moduleLength);
        byte[] varKey = hex2byte(this.getKey());
        byte[] module = new byte[moduleLength];
        System.arraycopy(varKey, 0, module, 0, moduleLength);
        caPublicKey.setModulus(module);

        byte[] exponent = getExp(hex2byte(this.getExponent()));
        caPublicKey.setLenOfExponent(exponent.length);
        caPublicKey.setExponent(exponent);
        byte[] expDate = new byte[3];
        byte[] date = hex2byte(this.getExpiryDate());
        byte[] lastDayMonth = lastDayOfMonth(date);
        System.arraycopy(date, 1, expDate, 0, 2);
        System.arraycopy(lastDayMonth, 0, expDate, 2, 1);
        caPublicKey.setExpirationDate(expDate);

        caPublicKey.setChecksum(hex2byte(this.getSha1()));

        return emvHandler.addCAPublicKey(caPublicKey);
    }

    private byte[] lastDayOfMonth(byte[] date) {
        byte[] ndays = new byte[]{(byte) 0x00, (byte) 0x31, (byte) 0x28, (byte) 0x31, (byte) 0x30, (byte) 0x31, (byte) 0x30, (byte) 0x31,
                (byte) 0x31, (byte) 0x30, (byte) 0x31, (byte) 0x30, (byte) 0x31};

        byte[] year = new byte[2];
        byte[] month = new byte[1];
        byte[] ret = new byte[1];
        System.arraycopy(date, 0, year, 0, 2);
        System.arraycopy(date, 2, month, 0, 1);

        int yearI = Integer.parseInt(hexString(year));
        int monthI = Integer.parseInt(hexString(month));

        ret[0] = ndays[monthI];
        if (monthI == 0x02 && (((yearI % 4 == 0) && (yearI % 100 != 0)) || (yearI % 400 == 0))) {
            ret[0]++; //leap year
        }
        return ret;
    }
    private byte[] getExp(byte[] source) {
        int lenModule = 4;
        int index = 0;
        if (source[0] != 0x00) {
            return new byte[0];
        }

        while (lenModule > 0) {
            if (source[index++] == 0x00) {
                lenModule--;
            } else {
                break;
            }
        }

        byte[] exponent = new byte[lenModule];
        if (lenModule > 0) {
            System.arraycopy(source, 4 - lenModule, exponent, 0, lenModule);
        }

        return exponent;
    }

    private CapkRow() {
    }

    public static CapkRow getSingletonInstance() {
        if (capkRow == null) {
            capkRow = new CapkRow();
        } else {
            Logger.debug("No se puede crear otro objeto, ya existe");
        }
        return capkRow;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CAPK_ROW: \n");

        sb.append("\t");
        sb.append("Subtype: ");
        sb.append(this.subType);
        sb.append("\n");

        sb.append("\t");
        sb.append("len: ");
        sb.append(this.len);
        sb.append("\n");

        sb.append("\t");
        sb.append("KeyIdx: ");
        sb.append(this.keyIdx);
        sb.append("\n");

        sb.append("\t");
        sb.append("RID: ");
        sb.append(this.rId);
        sb.append("\n");

        sb.append("\t");
        sb.append("Exponent: ");
        sb.append(this.exPonent);
        sb.append("\n");

        sb.append("\t");
        sb.append("keySize: ");
        sb.append(this.keySize);
        sb.append("\n");

        sb.append("\t");
        sb.append("key: ");
        sb.append(this.key);
        sb.append("\n");

        sb.append("\t");
        sb.append("expiryDate: ");
        sb.append(this.expiryDate);
        sb.append("\n");

        sb.append("\t");
        sb.append("effectDate: ");
        sb.append(this.effectDate);
        sb.append("\n");

        sb.append("\t");
        sb.append("chksum: ");
        sb.append(this.chksum);
        sb.append("\n");

        sb.append("\t");
        sb.append("sha1: ");
        sb.append(this.sha1);
        sb.append("\n");


        return sb.toString();

    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getLen() {
        return len;
    }

    public void setLen(String len) {
        this.len = len;
    }

    public String getKeyIdx() {
        return keyIdx;
    }

    public void setKeyIdx(String keyIdx) {
        this.keyIdx = keyIdx;
    }

    public String getRID() {
        return rId;
    }

    public void setRID(String rid) {
        this.rId = rid;
    }

    public String getExponent() {
        return exPonent;
    }

    public void setExponent(String exponent) {
        this.exPonent = exponent;
    }

    public String getKeySize() {
        return keySize;
    }

    public void setKeySize(String keySize) {
        this.keySize = keySize;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(String effectDate) {
        this.effectDate = effectDate;
    }

    public String getChksum() {
        return chksum;
    }

    public void setChksum(String chksum) {
        this.chksum = chksum;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

}
