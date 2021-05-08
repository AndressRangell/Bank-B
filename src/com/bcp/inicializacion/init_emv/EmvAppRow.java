package com.bcp.inicializacion.init_emv;

import android.content.Context;
import android.database.Cursor;
import com.bcp.inicializacion.sqlite.frompackage.DBHelper;
import com.bcp.inicializacion.tools.PolarisUtil;
import com.newpos.libpay.Logger;
import com.pos.device.emv.EMVHandler;
import com.pos.device.emv.IEMVHandler;
import com.pos.device.emv.TerminalAidInfo;
import org.jpos.stis.TLV_parsing;
import static org.jpos.stis.Util.hex2byte;



/**
 *
 * @author francisco
 */
public class EmvAppRow {

    private String subType;
    private String len;
    private String eType;
    private String eBitField;
    private String eRSBThresh;
    private String eRSTarget;
    private String eRSBMax;
    private String eTACDenial;
    private String eTACOnline;
    private String eTACDefault;
    private String eACFG;
    private boolean showDebug = false;
    private static final String TAG_EMVINIT = "emvinit";

    protected static String[] fields = new String[]{
            "subType",
            "len",
            "eType",
            "eBitField",
            "eRSBThresh",
            "eRSTarget",
            "eRSBMax",
            "eTACDenial",
            "eTACOnline",
            "eTACDefault",
            "eACFG"
    };

    private static EmvAppRow emvappRow;

    private void setEMVAPPROW(String column, String value) {
        switch (column) {
            case "subType":
                setSubType(value);
                break;
            case "len":
                setLen(value);
                break;
            case "eType":
                seteType(value);
                break;
            case "eBitField":
                seteBitField(value);
                break;
            case "eRSBThresh":
                seteRSBThresh(value);
                break;
            case "eRSTarget":
                seteRSTarget(value);
                break;
            case "eRSBMax":
                seteRSBMax(value);
                break;
            case "eTACDenial":
                seteTACDenial(value);
                break;
            case "eTACOnline":
                seteTACOnline(value);
                break;
            case "eTACDefault":
                seteTACDefault(value);
                break;
            case "eACFG":
                seteACFG(value);
                break;
            default:
                break;
        }

    }

    private boolean checkSigned() {
        StringBuilder textToVerify = new StringBuilder();
        textToVerify.append(this.subType);
        textToVerify.append(this.len);
        textToVerify.append(this.eType);
        textToVerify.append(this.eBitField);
        textToVerify.append(this.eRSBThresh);
        textToVerify.append(this.eRSTarget);
        textToVerify.append(this.eRSBMax);
        textToVerify.append(this.eTACDenial);
        textToVerify.append(this.eTACOnline);
        textToVerify.append(this.eTACDefault);
        textToVerify.append(this.eACFG);
        return true;
    }

    private void clearEMVAPPROW() {
        for (String s : EmvAppRow.fields) {
            setEMVAPPROW(s, "");
        }
    }

    public boolean selectEMVAPPROW(Context context) {
        boolean ok = false;
        DBHelper databaseAccess = new DBHelper(context, PolarisUtil.NAME_DB, null, 1);
        databaseAccess.openDb(PolarisUtil.NAME_DB);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        int counter = 1;
        for (String s : EmvAppRow.fields) {
            sql.append(s);
            if (counter++ < EmvAppRow.fields.length) {
                sql.append(",");
            }
        }
        sql.append(" from EMVAPPS");
        sql.append(";");

        try {

            Cursor cursor = databaseAccess.rawQuery(sql.toString());

            cursor.moveToFirst();
            int indexColumn;
            IEMVHandler emvHandler = EMVHandler.getInstance();
            while (!cursor.isAfterLast()){
                clearEMVAPPROW();
                indexColumn = 0;
                for (String s : EmvAppRow.fields) {
                    setEMVAPPROW(s, cursor.getString(indexColumn++));
                }

                if (showDebug) {
                    Logger.debug(TAG_EMVINIT + "\n" + this.toStringMet());
                    Logger.debug(TAG_EMVINIT + " EMVAPPROW checkSigned: " + (this.checkSigned() ? "true" : "false"));
                    Logger.debug("");
                }

                fillEmvApp(emvHandler);

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

    private void fillEmvApp(IEMVHandler emvHandler){
        TLV_parsing tlvParsing = new TLV_parsing(geteACFG());

        if (showDebug)
            Logger.debug("emvinit: \n" + tlvParsing.getAllTags());

        TerminalAidInfo terminalAidInfo = new TerminalAidInfo();
        terminalAidInfo.setAIDdLength(tlvParsing.getValueB(0x9f06).length);
        terminalAidInfo.setAId(tlvParsing.getValueB(0x9f06));


        byte[] tmp = hex2byte(this.geteBitField());
        byte tmp1 = tmp[0] &= 0x01;
        terminalAidInfo.setSupportPartialAIDSelect(tmp1 != 0x01);

        terminalAidInfo.setApplicationPriority(0);
        terminalAidInfo.setTargetPercentage(0);
        terminalAidInfo.setMaximumTargetPercentage(0);


        if (!tlvParsing.getValue(0x9f1b).equals("NA")) {
            terminalAidInfo.setTerminalFloorLimit(Integer.parseInt(tlvParsing.getValue(0x9f1b)));
        }

        terminalAidInfo.setThresholdValue(Integer.parseInt(this.geteRSBMax()));
        terminalAidInfo.setTerminalActionCodeDenial(hex2byte(this.geteTACDenial()));
        terminalAidInfo.setTerminalActionCodeOnline(hex2byte(this.geteTACOnline()));
        terminalAidInfo.setTerminalActionCodeDefault(hex2byte(this.geteTACDefault()));

        if (!tlvParsing.getValue(0x9f01).equals("NA")) {
            terminalAidInfo.setAcquirerIdentifier(tlvParsing.getValueB(0x9f01));
        }

        byte[] ddol = tlvParsing.getValueB(0x9f49);
        if (ddol != null) {
            terminalAidInfo.setLenOfDefaultDDOL(ddol.length);
            terminalAidInfo.setDefaultDDOL(ddol);
        }


        byte[] tdol = tlvParsing.getValueB(0x0097);
        if (tdol != null) {
            terminalAidInfo.setLenOfDefaultTDOL(tdol.length);
            terminalAidInfo.setDefaultTDOL(tdol);
        }


        byte[] applicationVersion = tlvParsing.getValueB(0x9F09);
        if (applicationVersion != null) {
            terminalAidInfo.setApplicationVersion(applicationVersion);
        }

        int rta = emvHandler.addAidInfo(terminalAidInfo);

        if (showDebug)
            Logger.debug(TAG_EMVINIT + " load aid, aid: " + tlvParsing.getValue(0x9f06) + " - Result: " + (rta));
    }

    private EmvAppRow() {
    }

    public static EmvAppRow getSingletonInstance() {
        if (emvappRow == null) {
            emvappRow = new EmvAppRow();
        } else {
            Logger.debug("No se puede crear otro objeto, ya existe");
        }
        return emvappRow;
    }

    public String toStringMet() {
        StringBuilder sb = new StringBuilder();
        sb.append("EMVAPPROW: \n");

        sb.append("\t");
        sb.append("Subtype: ");
        sb.append(this.subType);
        sb.append("\n");

        sb.append("\t");
        sb.append("len: ");
        sb.append(this.len);
        sb.append("\n");

        sb.append("\t");
        sb.append("eType: ");
        sb.append(this.eType);
        sb.append("\n");

        sb.append("\t");
        sb.append("eBitField: ");
        sb.append(this.eBitField);
        sb.append("\n");

        sb.append("\t");
        sb.append("eRSBThresh: ");
        sb.append(this.eRSBThresh);
        sb.append("\n");

        sb.append("\t");
        sb.append("eRSTarget: ");
        sb.append(this.eRSTarget);
        sb.append("\n");

        sb.append("\t");
        sb.append("eRSBMax: ");
        sb.append(this.eRSBMax);
        sb.append("\n");

        sb.append("\t");
        sb.append("eTACDenial: ");
        sb.append(this.eTACDenial);
        sb.append("\n");

        sb.append("\t");
        sb.append("eTACOnline: ");
        sb.append(this.eTACOnline);
        sb.append("\n");

        sb.append("\t");
        sb.append("eTACDefault: ");
        sb.append(this.eTACDefault);
        sb.append("\n");

        sb.append("\t");
        sb.append("eACFG: ");
        sb.append(this.eACFG);
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

    public String geteType() {
        return eType;
    }

    public void seteType(String eType) {
        this.eType = eType;
    }

    public String geteBitField() {
        return eBitField;
    }

    public void seteBitField(String eBitField) {
        this.eBitField = eBitField;
    }

    public String geteRSBThresh() {
        return eRSBThresh;
    }

    public void seteRSBThresh(String eRSBThresh) {
        this.eRSBThresh = eRSBThresh;
    }

    public String geteRSTarget() {
        return eRSTarget;
    }

    public void seteRSTarget(String eRSTarget) {
        this.eRSTarget = eRSTarget;
    }

    public String geteRSBMax() {
        return eRSBMax;
    }

    public void seteRSBMax(String eRSBMax) {
        this.eRSBMax = eRSBMax;
    }

    public String geteTACDenial() {
        return eTACDenial;
    }

    public void seteTACDenial(String eTACDenial) {
        this.eTACDenial = eTACDenial;
    }

    public String geteTACOnline() {
        return eTACOnline;
    }

    public void seteTACOnline(String eTACOnline) {
        this.eTACOnline = eTACOnline;
    }

    public String geteTACDefault() {
        return eTACDefault;
    }

    public void seteTACDefault(String eTACDefault) {
        this.eTACDefault = eTACDefault;
    }

    public String geteACFG() {
        return eACFG;
    }

    public void seteACFG(String eACFG) {
        this.eACFG = eACFG;
    }

}
