package com.bcp.transactions.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.SystemClock;
import com.bcp.document.ClassArguments;
import com.bcp.document.InputData;
import com.bcp.document.InputInfo;
import com.bcp.document.MsgScreenDocument;
import com.bcp.rest.JSONInfo;
import com.newpos.libpay.Logger;
import com.newpos.libpay.device.pinpad.PinInfo;
import com.newpos.libpay.presenter.TransUI;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.finace.FinanceTrans;
import com.newpos.libpay.trans.translog.TransLogWs;
import com.newpos.libpay.utils.ISOUtil;
import com.pos.device.icc.IccReader;
import com.pos.device.icc.SlotType;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import cn.desert.newpos.payui.UIUtils;

import static android.content.Context.MODE_PRIVATE;
import static com.android.newpos.pay.StartAppBCP.agente;
import static java.lang.Thread.sleep;

public class CommonFunctionalities {

    private static String pan;
    private static String numDocument;
    private static String newPassword;
    private static StringBuilder expDate;
    private static String cvv2;
    private static String pin;
    private static String proCode;
    private static boolean isSumarTotales = false;
    private static ClassArguments arguments;
    private static JSONInfo jsonInfo;

    private static final String PATTERN_DATE_FORMAT = "dd/MM/yyyy";
    private static final String MSG_FECHA_CIERRE = "fecha-cierre";
    private static final String MSG_FECHA_SIG_CIERRE = "fechaSigCierre";
    private static final String MSG_FIRTS_TRANS = "firtsTrans";

    CommonFunctionalities()
    {
        super();
    }

    public static String getPan() {
        return pan;
    }

    public static String getNumDocument() {
        return numDocument;
    }

    public static String getExpDate() {
        return expDate.toString();
    }

    public static String getCvv2() {
        return cvv2;
    }

    public static String getPin() {
        return pin;
    }

    public static String getProCode() {
        return proCode;
    }

    public static boolean isSumarTotales() {
        return isSumarTotales;
    }

    public static String getNewPassword() {
        return newPassword;
    }

    public static String[] tipoMoneda() {
        String[] moneda = new String[2];
        moneda[0] = "$";
        moneda[1] = FinanceTrans.DOLAR;
        return moneda;
    }

    public static boolean mustSettle(Context context) {
        DateFormat hourdateFormat = new SimpleDateFormat(PATTERN_DATE_FORMAT);
        Date fechaActual = new Date();
        String fechaFormat = hourdateFormat.format(fechaActual);
        SharedPreferences prefs = context.getSharedPreferences(MSG_FECHA_CIERRE, MODE_PRIVATE);
        String fechaCierre = prefs.getString(MSG_FECHA_SIG_CIERRE, null);
        if (fechaCierre != null) {
            Date dateCierre;
            Date dateActual;
            try {
                dateCierre = hourdateFormat.parse(fechaCierre);
                dateActual = hourdateFormat.parse(fechaFormat);
                int rta = dateActual.compareTo(dateCierre);
                if (rta >= 0 && (TransLogWs.getInstance().getSize() > 0)) {
                    return true;
                } else if (rta >= 0 && (TransLogWs.getInstance().getSize() == 0)) {
                    saveDateSettle(context);
                }else{
                    return false;
                }
            } catch (ParseException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            }
        } else {
            saveDateSettle(context);
        }
        return false;
    }

    public static int setPanManual(int timeout, String transEName, TransUI transUI) {

        int ret = 1;

        while (true) {
            InputInfo inputInfo = transUI.showInputUser(timeout, transEName, "DIGITE TARJETA", 0,19);
            if (inputInfo.isResultFlag()) {
                pan = inputInfo.getResult();
                ret = 0;
            } else {
                ret = inputInfo.getErrno();
            }
            break;
        }

        return ret;
    }

    public static int setFechaExp(int timeout, String transEName, TransUI transUI, boolean mostrarPantalla) {

        int ret = 1;
        String tmp;

        if (!mostrarPantalla) {
            return 0;
        }

        while (true) {
            InputInfo inputInfo = transUI.showInputUser(timeout, transEName, "FECHA EXPIRACION MM/YY", 0,4);

            if (inputInfo.isResultFlag()) {
                tmp = inputInfo.getResult();
                expDate = new StringBuilder();
                try {
                    expDate.append(tmp.substring(2, 4));
                    expDate.append(tmp.substring(0, 2));
                    ret = 0;
                }catch (IndexOutOfBoundsException e){
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " CommonFunctionalities " +  e.getMessage());
                    ret = TcodeError.T_ERR_INVALID_LEN;
                }
            } else {
                ret = TcodeError.T_USER_CANCEL;
            }
            break;
        }

        return ret;
    }

    public static int setCVV2(int timeout, String transEName, TransUI transUI, boolean mostrarPantalla) {

        int ret = 1;

        if (!mostrarPantalla) {
            return 0;
        }

        while (true) {
            InputInfo inputInfo = transUI.showInputUser(timeout, transEName, "CODIGO SEGURIDAD CVV2", 0,3);
            if (inputInfo.isResultFlag()) {
                if (inputInfo.getResult().length()==3) {
                    cvv2 = inputInfo.getResult();
                    ret = 0;
                }else{
                    ret = TcodeError.T_ERR_INVALID_LEN;
                }
            } else {
                ret = TcodeError.T_USER_CANCEL;
            }
            break;
        }

        return ret;
    }

    public static int ctlPIN(String pan, int timeout, long amount, TransUI transUI, String transEName) {
        boolean isPinExist;
        int ret = 1;
        PinInfo info = transUI.getPinpadOnlinePin(timeout, String.valueOf(amount), pan, "Ingresa clave de tarjeta");
        if (info.isResultFlag()) {
            if (info.isNoPin()) {
                isPinExist = false;
            } else {
                if (null == info.getPinblock()) {
                    isPinExist = false;
                } else {
                    isPinExist = true;
                }
                pin = ISOUtil.hexString(Objects.requireNonNull(info.getPinblock()));
                ret = 0;
            }
            if (!isPinExist) {
                ret = TcodeError.T_USER_CANCEL_PIN_ERR;
                transUI.showError(transEName, ret);
                return ret;
            }
        } else {
            ret = TcodeError.T_USER_CANCEL_PIN_ERR;
            transUI.showError(transEName, ret);
            return ret;
        }
        return ret;
    }

    public static int last4card(int timeout, String transEName, String pan, TransUI transUI, boolean mostrarPantalla) {

        int ret = 1;

        if (!mostrarPantalla)
            return 0;

        while (true) {
            InputInfo inputInfo = transUI.showInputUser(timeout, transEName, "ULTIMOS 4 DIGITOS", 0,4);
            if (inputInfo.isResultFlag()) {
                String last4Pan = pan.substring((pan.length() - 4), pan.length());
                if (last4Pan.equals(inputInfo.getResult())) {
                    ret = 0;
                } else {
                    ret = TcodeError.T_ERR_LAST_4;
                }
            } else {
                ret = TcodeError.T_USER_CANCEL;
            }
            break;
        }

        return ret;
    }

    public static int confirmTrans(int timeout, TransUI transUI, GetAmount amnt, String transEName, String[] label) {

        int ret = 1;

        try {
            long amount = amnt.getmAmnt();

            InputInfo inputInfo = transUI.showConfirmAmount(timeout, "Detalle del depósito", label, UIUtils.formatMiles(String.valueOf(amount)) + "", true, 26);

            if (inputInfo.isResultFlag()) {
                ret = 0;
            } else {
                ret = TcodeError.T_USER_CANCEL;
            }
        }catch (Exception e){
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " CommonFunctionalities 1 " +  e.getMessage());
            ret = TcodeError.T_USER_CANCEL;
        }

        return ret;
    }

    public static int setNumberDoc(TransUI transUI, MsgScreenDocument msgScreenDocument) {
        int ret = 1;

        InputInfo inputInfo = transUI.showInputDoc(msgScreenDocument);

        if (inputInfo.isResultFlag()) {
            numDocument = inputInfo.getResult();
            ret = 0;
        } else {
            ret = inputInfo.getErrno();
        }

        return ret;
    }
    public static int typeTrans(TransUI transUI, MsgScreenDocument msgScreenDocument) {
        int ret;

        InputData inputInfo = transUI.showInputTypeTrans(msgScreenDocument);

        if (inputInfo.isResultFlag()) {
            arguments = inputInfo.getArguments();
            ret = 0;
        } else {
            if (inputInfo.getJsonInfo() != null) {
                jsonInfo = inputInfo.getJsonInfo();
                ret = -2;
            }else
                ret = inputInfo.isBack() ? -1 : inputInfo.getErrno();
        }

        return ret;
    }

    public static int seleccionServicio(TransUI transUI, MsgScreenDocument msgScreenDocument) {
        int ret;

        InputData inputInfo = transUI.showseleccionServicio(msgScreenDocument);

        if (inputInfo.isResultFlag()) {
            arguments = inputInfo.getArguments();
            ret = 0;
        } else {
            ret = inputInfo.isBack() ? -1 : inputInfo.getErrno();
        }

        return ret;
    }

    public static int setClaveSecreta(TransUI transUI, MsgScreenDocument msgScreenDocument) {
        int ret;

        InputInfo inputInfo = transUI.showInputclavSecre(msgScreenDocument);

        if (inputInfo.isResultFlag()) {
            numDocument = inputInfo.getResult();
            ret = 0;
        } else {
            ret = inputInfo.isBack() ? -1 : inputInfo.getErrno();
        }

        return ret;
    }

    public static int setBeneficiaryCobros(TransUI transUI, MsgScreenDocument msgScreenDocument) {
        int ret;

        InputData inputInfo = transUI.showInputbeneficiarycobros(msgScreenDocument);

        if (inputInfo.isResultFlag()) {
            arguments = inputInfo.getArguments();
            ret = 0;
        } else {
            if (inputInfo.getJsonInfo() != null) {
                jsonInfo = inputInfo.getJsonInfo();
                ret = -2;
            }else
                ret = inputInfo.isBack() ? -1 : inputInfo.getErrno();
        }

        return ret;
    }

    public static int alertView(TransUI transUI,String transEname, String msg1, String msg2,String msg3) {
        int ret;

        InputInfo inputInfo = transUI.alertView(transEname, msg1,msg2,msg3);

        if (inputInfo.isResultFlag()) {
            numDocument = inputInfo.getResult();
            ret = 0;
        } else {
            ret = inputInfo.isBack() ? -1 : inputInfo.getErrno();
        }

        return ret;
    }

    public static int impresionUltimas(TransUI transUI ,String title, String msg, int timeout) {
        int ret;

        InputData inputInfo = transUI.showInputImpresionUltimas(title,msg, timeout);

        if (inputInfo.isResultFlag()) {
            arguments = inputInfo.getArguments();
            ret = 0;
        } else {
            if (inputInfo.getJsonInfo() != null) {
                jsonInfo = inputInfo.getJsonInfo();
                ret = -2;
            }else
                ret = inputInfo.isBack() ? -1 : inputInfo.getErrno();
        }

        return ret;
    }

    public static int datosoBeneficiary(TransUI transUI, MsgScreenDocument msgScreenDocument) {
        int ret;

        InputData inputInfo = transUI.showInputdatosoBeneficiary(msgScreenDocument);

        if (inputInfo.isResultFlag()) {
            arguments = inputInfo.getArguments();
            ret = 0;
        } else {
            if (inputInfo.getJsonInfo() != null) {
                jsonInfo = inputInfo.getJsonInfo();
                ret = -2;
            }else
                ret = inputInfo.isBack() ? -1 : inputInfo.getErrno();
        }

        return ret;
    }

    public static int detailPayment(TransUI transUI, MsgScreenDocument msgScreenDocument) {
        int ret;

        InputData inputInfo = transUI.showInputdetailPayment(msgScreenDocument);

        if (inputInfo.isResultFlag()) {
            arguments = inputInfo.getArguments();
            ret = 0;
        } else {
            if (inputInfo.getJsonInfo() != null) {
                jsonInfo = inputInfo.getJsonInfo();
                ret = -2;
            }else
                ret = inputInfo.isBack() ? -1 : inputInfo.getErrno();
        }

        return ret;
    }

    public static int detalleServicio(TransUI transUI, MsgScreenDocument msgScreenDocument) {
        int ret;

        InputData inputInfo = transUI.showInputdetalleServicio(msgScreenDocument);

        if (inputInfo.isResultFlag()) {
            arguments = inputInfo.getArguments();
            ret = 0;
        } else {
            ret = inputInfo.isBack() ? -1 : inputInfo.getErrno();
        }

        return ret;
    }

    public static void updateDateFirstTrans(String idAcq, Context context,Date fecha) {
        if (TransLogWs.getInstance().getSize() == 1 && !getFirtsTrans(context)) {
            saveDateSettleAfterFirtsTrans(context,fecha);
            saveFirtsTrans(context, true);
        }
    }

    public static void saveFirtsTrans(Context context, boolean flag) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MSG_FIRTS_TRANS, MODE_PRIVATE).edit();
        editor.putBoolean(MSG_FIRTS_TRANS, flag);
        editor.apply();
    }

    public static boolean getFirtsTrans(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MSG_FIRTS_TRANS, MODE_PRIVATE);
        return prefs.getBoolean(MSG_FIRTS_TRANS, false);
    }

    public static void saveDateSettle(Context context) {
        DateFormat hourdateFormat = new SimpleDateFormat(PATTERN_DATE_FORMAT);
        Date fechaActual = new Date();
        String diasCierre = !agente.getSettleDays().equals("") ? agente.getSettleDays() : "1";
        Date fechaCierre = sumarRestarDiasFecha(fechaActual, Integer.parseInt(diasCierre));
        SharedPreferences.Editor editor = context.getSharedPreferences(MSG_FECHA_CIERRE, MODE_PRIVATE).edit();
        editor.putString(MSG_FECHA_SIG_CIERRE, hourdateFormat.format(fechaCierre));
        editor.putString("fechaUltAct", hourdateFormat.format(fechaActual));
        editor.apply();
    }

    public static void saveDateSettleAfterFirtsTrans(Context context,Date fechaActual) {
        DateFormat hourdateFormat = new SimpleDateFormat(PATTERN_DATE_FORMAT);
        if (fechaActual == null){
            fechaActual = new Date();
        }
        String diasCierre = agente.getSettleDays();
        Date fechaCierre = sumarRestarDiasFecha(fechaActual, Integer.parseInt(diasCierre));
        SharedPreferences.Editor editor = context.getSharedPreferences(MSG_FECHA_CIERRE, MODE_PRIVATE).edit();
        editor.putString(MSG_FECHA_SIG_CIERRE, hourdateFormat.format(fechaCierre));
        editor.apply();
    }

    public static Date sumarRestarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto date con los nuevos días añadidos
    }

    public static boolean validateCard(int timeout, TransUI transUI){
        boolean ret;
        final int TIMEOUT_REMOVE_CARD = timeout * 1000;

        IccReader iccReader0;
        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);

        iccReader0 = IccReader.getInstance(SlotType.USER_CARD);
        long start = SystemClock.uptimeMillis() ;

        while (true){
            try {
                if (iccReader0.isCardPresent()) {
                    transUI.handlingBCP("Retire la tarjeta","",true);
                    toneG.startTone(ToneGenerator.TONE_PROP_BEEP2, 2000);
                    sleep(2000);
                    if (SystemClock.uptimeMillis() - start > TIMEOUT_REMOVE_CARD) {
                        toneG.stopTone();
                        ret = false;
                        break;
                    }
                }else {
                    ret = true;
                    break;
                }
            }catch (Exception e){
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " CommonFunctionalities 2 " +  e.getMessage());
                ret = true;
                break;
            }
        }
        return ret;
    }

    public static int setChangePassword(int timeout, TransUI transUI, String tittle, int minLen, int maxLen, String transEName) {
        int ret = 1;

        InputInfo inputInfo = transUI.showChangePwd(timeout, tittle, minLen,maxLen);
        if (inputInfo.isResultFlag()) {
            newPassword = inputInfo.getResult();
            ret = 0;
        } else {
            ret = inputInfo.getErrno();
        }

        return ret;
    }

    public static ClassArguments getArguments() {
        return arguments;
    }

    public static JSONInfo getJsonInfo() {
        return jsonInfo;
    }

    public static int lastOperations(TransUI transUI, String contentLast, String contentThree) {
        int ret;

        InputInfo inputInfo = transUI.showLastOperations(contentLast, contentThree);

        if (inputInfo.isResultFlag()) {
            numDocument = inputInfo.getResult();
            ret = 0;
        } else {
            ret = inputInfo.isBack() ? -1 : inputInfo.getErrno();
        }

        return ret;
    }
}
