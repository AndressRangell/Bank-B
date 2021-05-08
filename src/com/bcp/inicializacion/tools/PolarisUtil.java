package com.bcp.inicializacion.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import com.android.newpos.pay.R;
import com.bcp.inicializacion.configuracioncomercio.Category;
import com.bcp.inicializacion.configuracioncomercio.Transaction;
import com.bcp.inicializacion.sqlite.frompackage.DBHelper;
import com.bcp.transactions.callbacks.MakeInitCallback;
import com.bcp.transactions.callbacks.WaitOkPrintSettle;
import com.bcp.transactions.callbacks.WaitPrintReport;
import com.bcp.transactions.callbacks.WaitReadDB;
import com.bcp.transactions.callbacks.WaitSeatleReport;
import com.bcp.transactions.common.CommonFunctionalities;
import com.newpos.libpay.Logger;
import com.newpos.libpay.utils.ISOUtil;
import com.pos.device.SDKException;
import com.pos.device.ped.KeySystem;
import com.pos.device.ped.KeyType;
import com.pos.device.ped.Ped;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.android.newpos.pay.ProcessingCertificate.polarisUtil;
import static com.bcp.definesbcp.Definesbcp.NAME_FOLDER_DB;
import static com.bcp.definesbcp.Definesbcp.PATH_DATA;
import static com.bcp.definesbcp.Variables.MASTERKEYIDX;
import static com.bcp.definesbcp.Variables.WORKINGKEYIDX;

public class PolarisUtil {

    private boolean isInit;
    private boolean isCertificate;
    public static final String NAME_DB = "init.db";

    public static final String GENERAL = "GENERAL.db";
    public static final String AGENTE = "AGENTE.sql";
    public static final String CAPKS = "CAPKS.sql";
    public static final String CARDS = "CARDS.sql";
    public static final String CATEGORY = "CATEGORIA.sql";
    public static final String CREDENTIALS = "CREDENCIALS.sql";
    public static final String COMPANNY = "EMPRESA.sql";
    public static final String EMVAPPS = "EMVAPPS.sql";
    public static final String TRANSACCIONES = "TRANSACCIONES.sql";
    public static final String URL = "URL.sql";
    public static final int IGUALORDENANTESI = 2131361825;
    public static final int IGUALORDENANTENO = 2131361826;

    //KEYSTORES
    public  static final  String TRANSACCIONPUB = "TRANSACCION_PUBLICA.pfx";
    public  static final  String TRANSACCIONPRIV = "TRANSACCION_PRIVADA.pfx";
    public  static final  String TLS = "TLS.pfx";
    public  static final  String LLAVE = "LLAVE.pfx";
    public  static final  String NUMERO_TARJETA = "NUMERO_TARJETA.pfx";

    public static final String PATTERN_DATE_FORMAT = "dd/MM/yyyy";
    private int intentSettle = 0;

    private WaitPrintReport callbackPrint = null;
    private WaitSeatleReport callBackSeatle = null;
    private MakeInitCallback makeinitcallback = null;
    private WaitReadDB callbackReadDB = null;
    private WaitOkPrintSettle callbackIsPrinterSettle = null;
    private List<Transaction> listTrans = new ArrayList<>();
    private Transaction transCurrent = new Transaction();
    private List<Category> listCategory = new ArrayList<>();


    public PolarisUtil() {
        this.isInit = false;
    }

    /**
     * isInitPolaris check Stis Table
     *
     * @author Francisco Mahecha
     * @modified by JM
     * @version 2.0
     * @param context - Activity'S context
     * @return true or false
     */
    public boolean isInitPolaris(Context context) {
        int countRow;
        int counterTables = 1;

        boolean agentOk = false;
        boolean capksOk = false;
        boolean cardsOk = false;
        //boolean categoryOk = false;
        boolean credentialsOk = false;
        //boolean compannyOk = false;
        boolean emvappsOk = false;
        boolean transactionsOk = false;
        boolean urlOk = false;

        DBHelper databaseAccess = new DBHelper(context, NAME_DB, null, 1);
        databaseAccess.openDb(NAME_DB);

        StringBuilder sql = new StringBuilder();

        String msgUnionAll = "union all ";
        sql.append("select count (*) from AGENTE ");
        sql.append(msgUnionAll);
        sql.append("select  count (*) from CAPKS ");
        sql.append(msgUnionAll);
        sql.append("select  count (*) from CARDS ");
        sql.append(msgUnionAll);
        /*sql.append("select  count (*) from CATEGORIA ");
        sql.append(msgUnionAll);*/
        sql.append("select  count (*) from CREDENCIALS ");
        sql.append(msgUnionAll);
        /*sql.append("select  count (*) from EMPRESA ");
        sql.append(msgUnionAll);*/
        sql.append("select count (*) from EMVAPPS ");
        sql.append(msgUnionAll);
        sql.append("select  count (*) from TRANSACCIONES ");
        sql.append(msgUnionAll);
        sql.append("select  count (*) from URL ");

        try {

            Cursor cursor = databaseAccess.rawQuery(sql.toString());

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                countRow = cursor.getInt(0);

                if (countRow == 0) {
                    break;
                } else {
                    switch (counterTables) {
                        case 1:
                            agentOk = true;
                            break;
                        case 2:
                            capksOk = true;
                            break;
                        case 3:
                            cardsOk = true;
                            break;
                        // eliminar para la version con pago de servicios
                        case 4:
                            credentialsOk = true;
                            break;
                        case 5:
                            emvappsOk = true;
                            break;
                        case 6:
                            transactionsOk = true;
                            break;
                        case 7:
                            urlOk = true;
                            break;
                        //eliminar hasta aca
                        /*case 4:
                            categoryOk = true;
                            break;
                        case 5:
                            credentialsOk = true;
                            break;
                        case 6:
                            compannyOk = true;
                            break;
                        case 7:
                            emvappsOk = true;
                            break;
                        case 8:
                            transactionsOk = true;
                            break;
                        case 9:
                            urlOk = true;
                            break;*/
                        default:
                            break;
                    }
                }

                counterTables = counterTables + 1;
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }

        databaseAccess.closeDb();

        boolean ret =
                counterTables == 8 &&
                agentOk &&
                capksOk &&
                cardsOk &&
                //categoryOk &&
                credentialsOk &&
                //compannyOk &&
                emvappsOk &&
                transactionsOk &&
                urlOk;

        this.isInit = ret;

        if (polarisUtil.getCallbackReadDB() != null)
            polarisUtil.getCallbackReadDB().getResponseReadDB(ret);

        return ret;
    }

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }

    public void setInit(boolean init, String track2) {
        if (init && (track2.length()>0))
            isInit = true;
    }

    public WaitPrintReport getCallbackPrint() {
        return callbackPrint;
    }

    public void setCallbackPrint(WaitPrintReport callbackPrint) {
        this.callbackPrint = callbackPrint;
    }

    public WaitSeatleReport getCallBackSeatle() {
        return callBackSeatle;
    }

    public void setCallBackSeatle(WaitSeatleReport callBackSeatle) {
        this.callBackSeatle = callBackSeatle;
    }

    public WaitReadDB getCallbackReadDB() {
        return callbackReadDB;
    }

    public void setCallbackReadDB(WaitReadDB callbackReadDB) {
        this.callbackReadDB = callbackReadDB;
    }

    public WaitOkPrintSettle getCallbackIsPrinterSettle() {
        return callbackIsPrinterSettle;
    }

    public void setCallbackIsPrinterSettle(WaitOkPrintSettle callbackIsPrinterSettle) {
        this.callbackIsPrinterSettle = callbackIsPrinterSettle;
    }

    public MakeInitCallback getMakeinitcallback() {
        return makeinitcallback;
    }

    public void setMakeinitcallback(MakeInitCallback makeinitcallback) {
        this.makeinitcallback = makeinitcallback;
    }

    public List<Transaction> getListTrans() {
        return listTrans;
    }

    public void setListTrans(List<Transaction> listTrans) {
        this.listTrans = listTrans;
    }

    public Transaction getTransCurrent() {
        return transCurrent;
    }

    public void setTransCurrent(Transaction transCurrent) {
        this.transCurrent = transCurrent;
    }

    public int getIntentSettle() {
        return intentSettle;
    }

    public void setIntentSettle(int intentSettle) {
        this.intentSettle = intentSettle;
    }

    public List<Category> getListCategory() {
        return listCategory;
    }

    public void setListCategory(List<Category> listCategory) {
        this.listCategory = listCategory;
    }

    //========== Metodos de prueba para inyeccion de llaves ========
    /**
     *
     * @param masterKey
     * @return
     */
    public int injectMk(String masterKey) {
        byte[] masterKeyData = ISOUtil.str2bcd(masterKey, false);
        return Ped.getInstance().injectKey(KeySystem.MS_DES, KeyType.KEY_TYPE_MASTK, MASTERKEYIDX, masterKeyData);
    }

    /**
     *
     * @param workingKey
     * @return
     */
    public int injectWorkingKey(String workingKey) {
        byte[] workingKeyData = ISOUtil.str2bcd(workingKey, false);
        return Ped.getInstance().writeKey(KeySystem.MS_DES, KeyType.KEY_TYPE_PINK, MASTERKEYIDX, WORKINGKEYIDX, Ped.KEY_VERIFY_NONE, workingKeyData);
    }

    /**
     *
     * @param giros
     * @return
     */
    public int injectGirosKey(String giros) {
        byte[] girosKeyData = ISOUtil.str2bcd(giros, false);
        return Ped.getInstance().writeKey(KeySystem.MS_DES, KeyType.KEY_TYPE_EAK, MASTERKEYIDX, WORKINGKEYIDX, Ped.KEY_VERIFY_NONE, girosKeyData);
    }

    public void deleteMasterKey(){
        try {
            Ped.getInstance().deleteKey(KeySystem.MS_DES, KeyType.KEY_TYPE_MASTK, MASTERKEYIDX);
        } catch (SDKException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
    }

    public boolean isCertificate() {
        return isCertificate;
    }

    public void setCertificate(boolean certificate) {
        isCertificate = certificate;
    }

    public boolean mustInitialize(Context context) {
        DateFormat hourDateFormat = new SimpleDateFormat(PATTERN_DATE_FORMAT);
        Date currentDate = new Date();
        String dateActually = hourDateFormat.format(currentDate);
        SharedPreferences prefs = context.getSharedPreferences("CambioFecha", MODE_PRIVATE);
        String dateSave = prefs.getString("FechaGuardada", null);
        if (dateSave != null) {
            Date changeDate;
            Date dateCurrent;
            try {
                changeDate = hourDateFormat.parse(dateSave);
                dateCurrent = hourDateFormat.parse(dateActually);
                assert dateCurrent != null;
                int ret = dateCurrent.compareTo(changeDate);
                return ret >= 0;
            } catch (ParseException e) {
                Logger.debug("error" + e.getMessage());
            }
        }else {
            dateSave(context);
        }
        return false;
    }

    public static void dateSave(Context context) {
        DateFormat hourDateFormat = new SimpleDateFormat(PATTERN_DATE_FORMAT);
        Date fechaActual = new Date();
        String daysChangePwd = "1";
        Date dateChangePwd = CommonFunctionalities.sumarRestarDiasFecha(fechaActual, Integer.parseInt(daysChangePwd));
        SharedPreferences.Editor editor = context.getSharedPreferences("CambioFecha", MODE_PRIVATE).edit();
        editor.putString("FechaGuardada", hourDateFormat.format(dateChangePwd));
        editor.apply();
    }

    public void searchfile(Context context, String aFileName, String aFileWithOutExt) {

        String packageName = context.getPackageName();
        String directory = File.separator + PATH_DATA + File.separator +
                PATH_DATA + File.separator + packageName +
                File.separator + NAME_FOLDER_DB + File.separator;
        File file = new File(directory + File.separator + aFileWithOutExt);

        if (aFileName.endsWith(".db") || aFileName.endsWith(".DB")) {

            try (InputStream readBD = context.getResources().openRawResource(R.raw.init)) {

                byte[] inputBuffer = new byte[4096];
                int charRead;

                try (FileOutputStream outWrite = new FileOutputStream(file)) {
                    while ((charRead = readBD.read(inputBuffer)) > 0) {
                        outWrite.write(inputBuffer, 0, charRead);
                    }
                    outWrite.flush();
                }
            } catch (Exception e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            }
        }
    }
}
