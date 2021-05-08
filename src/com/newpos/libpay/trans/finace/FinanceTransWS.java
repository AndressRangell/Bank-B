package com.newpos.libpay.trans.finace;

import android.content.Context;

import com.android.newpos.libemv.PBOCCardInfo;
import com.android.newpos.libemv.PBOCTag9c;
import com.android.newpos.libemv.PBOCTransProperty;
import com.android.newpos.libemv.PBOCUtil;
import com.android.newpos.libemv.PBOCode;
import com.android.newpos.pay.R;
import com.bcp.menus.seleccion_cuenta.InputSelectAccount;
import com.bcp.menus.seleccion_cuenta.SelectedAccountItem;
import com.bcp.printer.MsgScreenPrinter;
import com.bcp.rest.generic.response.ProductsAccounts;
import com.bcp.rest.jwt.CipherDocument;
import com.bcp.rest.jwt.JWEString;
import com.bcp.tools.Estadisticas;
import com.bcp.transactions.common.CommonFunctionalities;
import com.bcp.transactions.common.GetAmount;
import com.newpos.bypay.EmvL2CVM;
import com.newpos.libpay.Logger;
import com.newpos.libpay.device.card.CardInfo;
import com.newpos.libpay.device.card.CardManager;
import com.newpos.libpay.device.contactless.EmvL2Process;
import com.newpos.libpay.device.pinpad.PinInfo;
import com.newpos.libpay.process.EmvTransaction;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.trans.Trans;
import com.newpos.libpay.trans.TransInputPara;
import com.newpos.libpay.trans.translog.TransLogDataWs;
import com.newpos.libpay.trans.translog.TransLogWs;
import com.newpos.libpay.utils.ISOUtil;
import com.newpos.libpay.utils.PAYUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import cn.desert.newpos.payui.UIUtils;

import static cn.desert.newpos.payui.master.MasterControl.incardTable;
import static com.android.newpos.pay.StartAppBCP.estadisticas;
import static com.android.newpos.pay.StartAppBCP.variables;
import static com.bcp.definesbcp.Definesbcp.GERCARD_MSG_FALLBACK;
import static com.bcp.definesbcp.Definesbcp.GERCARD_MSG_ICC1;
import static com.bcp.definesbcp.Definesbcp.GERCARD_MSG_ICC2;
import static com.bcp.definesbcp.Variables.FALLBACK;

public class FinanceTransWS extends Trans {

    /**
     * 外界输入类型
     */
    public static final int INMODE_HAND = 0x01;
    public static final int INMODE_MAG = 0x02;
    public static final int INMODE_IC = 0x08;
    public static final int INMODE_NFC = 0x10;

    /**
     * 联机交易还是脱机交易
     */
    public static final int AAC_ARQC = 1;

    public static final String LOCAL = "1";
    public static final String DOLAR = "2";
    public static final String EURO = "3";

    /**
     * 卡片模式
     */
    protected int inputMode = 1;// 刷卡模式 1 手输卡号；2刷卡；5 3插IC；7 4非接触卡

    /**
     * 是否有密码
     */
    protected boolean isPinExist = false;

    /**
     * 标记此次交易是否需要冲正
     */
    protected boolean isReversal;

    /**
     * 标记此次交易是否需要存记录
     */
    protected boolean isSaveLog;

    /**
     * 是否借记卡交易
     */
    protected boolean isDebit;

    /**
     * 标记此交易联机前是否进行冲正上送
     */
    protected boolean isProcPreTrans;

    /**
     * whether need GAC2
     */
    protected boolean isNeedGAC2;

    protected TransLogDataWs logData;

    protected String arqc;

    protected JWEString jweDataEncrypt;

    protected CipherDocument cipherDocument;

    /**
     * 金融交易类构造
     *
     * @param ctx
     * @param transEname
     */
    public FinanceTransWS(Context ctx, String transEname, TransInputPara p) {
        super(ctx, transEname, false, p);
        setTraceNoInc(true);
    }

    protected int saveTransFinance() {

        estadisticas.writeEstadisticas(Estadisticas.TRANSSUCCESS,para.getTransType());

        Logger.logLine(Logger.LOG_GENERAL, "Guardado de logs");

        try {
            if(transEName.equals(Trans.GIROS)){
                if (arguments.getTypetransaction() == 1)
                    logData = setTransLogWsEmision();
                else
                    logData = setTransLogWsCobros();
            }else if(transEName.equals(Trans.PAGOSERVICIOS)){
                logData = setTransLogWsPS();
            }else {
                logData = setTransLogWs();
            }
            if (isSaveLog) {
                if(!transLogWs.saveLogWs(logData)){
                    retVal = TcodeError.T_ERR_SAVE_BATCH;
                }else{
                    if (amount > 0){
                        UIUtils.validateMaxAmount(amount, transEName,false,context);
                    }

                    //Actualiza la fecha de siguiente cierre si la trans que se esta guardando es la primerta del nuevo lote
                    Date fechaHora;
                    try{
                        fechaHora = PAYUtils.strToDate(PAYUtils.dateToStr(new Date(),"MM/dd/yyyy")+ " " +logData.getTransactionTime(),"MM/dd/yyyy HH:mm:ss");
                    }catch (Exception e){
                        Logger.logLine(Logger.LOG_EXECPTION, " FinanceTransWs " +  e.getMessage());
                        //Logger.error(Logger.TAGERROR + " FinanceTransWs " +  e.getMessage());
                        fechaHora = null;
                    }
                    CommonFunctionalities.updateDateFirstTrans(hostId, context,fechaHora);

                    retVal = 0;
                }
            }
        }catch (Exception e){
            Logger.logLine(Logger.LOG_EXECPTION, "Error en guardado de logs");
            Logger.logLine(Logger.LOG_EXECPTION, e.getMessage());
            Logger.logLine(Logger.LOG_EXECPTION, Arrays.toString(e.getStackTrace()));
            Logger.logLine(Logger.LOG_EXECPTION, e.getLocalizedMessage());
            Logger.logLine(Logger.LOG_EXECPTION, "Final error en guardado de logs");
        }

        if(!TransLogWs.clearReveral())
            retVal = TcodeError.T_ERR_CLEAR_REV;
        else
            retVal = 0;

        return retVal;
    }


    protected int printerDataClient(){
        if (para.isNeedPrint()){
            if (logData!=null)
                retVal = printData(logData, fillScreenPrinter());
            else
                retVal = TcodeError.T_ERR_PRINT_DATA;
        }

        return retVal;
    }

    protected int printerDataCommerce(){
        if (para.isNeedPrint()){
            if (logData!=null)
                retVal = printDataComer(logData, retValPrinter == 0);
            else
                retVal = TcodeError.T_ERR_PRINT_DATA;
        }

        return retVal;
    }

    public MsgScreenPrinter fillScreenPrinter(){
        MsgScreenPrinter msgScreenPrinter = new MsgScreenPrinter();
        String nameBanner="Comprobante para cliente";
        String nameBtnConfirm="Imprimir";
        String nameBtnCancel="Sin comprobante";

        Logger.logLine(Logger.LOG_GENERAL, "Llenado pantalla de impresion");

        String complement = "";
        try {
            if (transEName.equals(Trans.RETIRO) || transEName.equals(Trans.PAGOSERVICIOS)){
                complement = "de ";
            }else if (transEName.equals(Trans.DEPOSITO)){
                complement = "a ";
            }
            msgScreenPrinter.setTittle(getTittle(transEName));
            msgScreenPrinter.setMsgAmount(amount + "");
            if(!transEName.equals(Trans.GIROS) && !transEName.equals(Trans.PAGOSERVICIOS)){
                msgScreenPrinter.setSymbol(rspExecTrans.getCtnCurrencySymbol());
                msgScreenPrinter.setMsgAccount(complement + "cuenta " + rspExecTrans.getCtnFmlDescript() + " " + (rspExecTrans.getCtnCurrenDescript() != null ? rspExecTrans.getCtnCurrenDescript() : ""));
            }else if(!transEName.equals(PAGOSERVICIOS)){
                msgScreenPrinter.setTittle(context.getResources().getString(arguments.getTypetransaction() == 1 ? R.string.titleImpGiro : R.string.titleCobroGiro));
                msgScreenPrinter.setMsgAmount(String.valueOf(logData.getAmount()).replaceAll("[,.]",""));
                msgScreenPrinter.setSymbol(logData.getCurrencySymbol());
                msgScreenPrinter.setMsgAccount(depWithCard ? "con tarjeta" : "en efectivo");
            }else {
                msgScreenPrinter.setTittle("Monto pagado");
                msgScreenPrinter.setMsgAmount(String.valueOf(logData.getAmount()).replaceAll("[,.]",""));
                msgScreenPrinter.setSymbol(logData.getCurrencySymbol());
                if(rspExecuteTransactionPS.getCurrencyDescription() != null && rspExecuteTransactionPS.getFamilyDescription() != null)
                    msgScreenPrinter.setMsgAccount(complement + "cuenta " + rspExecuteTransactionPS.getFamilyDescription() + " " + rspExecuteTransactionPS.getCurrencyDescription());
            }
            msgScreenPrinter.setMsgBanner(nameBanner);
            msgScreenPrinter.setMsgButtonConfirm(nameBtnConfirm);
            msgScreenPrinter.setMsgButtonCancel(nameBtnCancel);
            msgScreenPrinter.setTypebill(typebill);
            msgScreenPrinter.setTransEname(transEName);
        }catch (Exception e){
            Logger.logLine(Logger.LOG_EXECPTION, "Error en llenado pantalla de impresion");
            Logger.logLine(Logger.LOG_EXECPTION, e.getMessage());
            Logger.logLine(Logger.LOG_EXECPTION, Arrays.toString(e.getStackTrace()));
            Logger.logLine(Logger.LOG_EXECPTION, e.getLocalizedMessage());
            Logger.logLine(Logger.LOG_EXECPTION, "Final error en llenado pantalla de impresion");
        }

        return msgScreenPrinter;
    }

    private String getTittle(String transName){
        String ret = "";
        switch (transName){
            case Trans.RETIRO:
                ret="Monto retirado";
                break;
            case Trans.DEPOSITO:
                ret="Monto depositado";
                break;
            case Trans.CONSULTAS:
                if(typebill.equals("1")){
                    ret="Consulta Saldo";
                }else {
                    ret="Consulta Movimiento";
                }
                break;
            default:
                ret="";
                break;
        }
        return ret;
    }



    /**
     * 联机前某些特殊值的处理
     *
     * @param inputMode
     */
    protected void setDatas(int inputMode) {

        Logger.debug("==FinanceTrans->setDatas==");
        this.inputMode = inputMode;

        if (isPinExist) {
            captureCode = "12";
        }

        entryMode = ISOUtil.padleft(inputMode + "", 2, '0');

        if (inputMode == ENTRY_MODE_MAG) {
            if (isFallBack) {
                entryMode = MODE1_FALLBACK + capPinPOS();
            } else {
                entryMode = MODE_MAG + capPinPOS();
            }
        } else if (inputMode == ENTRY_MODE_ICC) {
            entryMode = MODE_ICC + capPinPOS();
        } else if (inputMode == ENTRY_MODE_NFC) {
            entryMode = MODE_CTL + capPinPOS();
        } else if (inputMode == ENTRY_MODE_HAND) {
            entryMode = MODE_HANDLE + capPinPOS();
        } else {
            entryMode = "000";
        }

        if (isPinExist || track2 != null || track3 != null) {
            if (isPinExist) {
                securityInfo = "2";
            } else {
                securityInfo = "0";
            }
            if (cfg.isSingleKey()) {
                securityInfo += "0";
            } else {
                securityInfo += "6";
            }
            if (cfg.isTrackEncrypt()) {
                securityInfo += "10000000000000";
            } else {
                securityInfo += "00000000000000";
            }
        }
        appendField60("048");
    }

    private String capPinPOS() {
        String capPINPos = "1";
        if (pin == null) {
            capPINPos = "2";
        }
        return capPINPos;
    }

    /**
     * 从内核获取
     * 卡号，
     * 有效期，
     * 2磁道，
     * 1磁道，
     * 卡序号
     * 55域数据
     */
    private void setICCData() {
        Logger.debug("==FinanceTrans->setIccdata==");
        byte[] temp = new byte[128];
        // 卡号
        int len = PAYUtils.getTlvDataKernal(0x5A, temp);
        pan = ISOUtil.trimf(ISOUtil.byte2hex(temp, 0, len));
        // 有效期
        len = PAYUtils.getTlvDataKernal(0x5F24, temp);
        if (len == 3) {
            expDate = ISOUtil.byte2hex(temp, 0, len - 1);
        }
        // 2磁道
        len = PAYUtils.getTlvDataKernal(0x57, temp);
        track2 = ISOUtil.trimf(ISOUtil.byte2hex(temp, 0, len));
        // 1磁道
        len = PAYUtils.getTlvDataKernal(0x9F1F, temp);
        track1 = new String(temp, 0, len);
        // 卡序号
        len = PAYUtils.getTlvDataKernal(0x5F34, temp);
        if (len!=0)
            panSeqNo = ISOUtil.padleft(ISOUtil.byte2hex(temp, 0, len) + "", 3, '0');
        //ARQC
        arqc = getTC();
    }

    /**
     * set some IC card data
     */
    private void setICCDataCTL() {
        Logger.debug("==FinanceTrans->setIccdata==");
        PBOCCardInfo info = PBOCUtil.getPBOCCardInfo();
        pan = info.getCardNO();
        expDate = info.getExpDate();
        track2 = info.getCardTrack2();
        track1 = info.getCardTrack1();
        track3 = info.getCardTrack3();
        panSeqNo = info.getCardSeqNo();
        iccdata = PBOCUtil.getF55Data(PBOCUtil.wOnlineTags);
    }

    protected boolean cardProcess(int mode) {

        CardInfo cardInfo = transUI.getCardUse(GERCARD_MSG_ICC1, GERCARD_MSG_ICC2, timeout, mode);

        if (cardInfo.isResultFalg()) {
            int type = cardInfo.getCardType();
            switch (type) {
                case CardManager.TYPE_MAG:
                    inputMode = ENTRY_MODE_MAG;
                    break;
                case CardManager.TYPE_ICC:
                    inputMode = ENTRY_MODE_ICC;
                    break;
                case CardManager.TYPE_NFC:
                    inputMode = ENTRY_MODE_NFC;
                    break;
                case CardManager.TYPE_HAND:
                    inputMode = ENTRY_MODE_HAND;
                    break;
                default:
                    retVal = TcodeError.T_NOT_ALLOW;
                    return false;
            }
            para.setInputMode(inputMode);
            if (inputMode == ENTRY_MODE_ICC && isICC(false)) {
                try {
                    transUI.verifyCard(context.getResources().getString(R.string.listo), true);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " FinanceTransWs 2 " +  e.getMessage());
                    Thread.currentThread().interrupt();
                }
                return true;
            }
            if (inputMode == ENTRY_MODE_MAG) {
                isDebit = false;
                if(isMag(cardInfo.getTrackNo())) {
                    return true;
                }
            }
            if (inputMode == ENTRY_MODE_NFC) {
                if (cfg.isForcePboc()) {
                    if(isICC(false))
                        return true;
                } else {
                    if(pbocTrans()) {
                        return true;
                    }
                }
            }
            if (inputMode == ENTRY_MODE_HAND) {
                isDebit = false;
                if(isHandle()) {
                    return true;
                }
            }
        } else {
            retVal = cardInfo.getErrno();
            if(retVal == 107) {
                if (!CommonFunctionalities.validateCard(timeout, transUI)) {
                    retVal = TcodeError.T_ERR_TIMEOUT;
                    return false;
                }
                variables.setContFallback(variables.getContFallback() + 1);
            }else{
                return false;
            }
        }

        if (variables.getContFallback() == FALLBACK){
            variables.setContFallback(0);
            return false;
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " FinanceTransWs 3 " +  e.getMessage());
            Thread.currentThread().interrupt();
        }

        if (retVal == 107 || retVal == 109){
            return cardProcess(mode);
        }

        return false;
    }

    private boolean fallback(){
        isFallBack = true;
        retVal = transUI.showCardConfirm(timeout, "Pase la tarjeta");
        if(0 == retVal){
            CardInfo cardInfo = transUI.getCardUse(transEName, GERCARD_MSG_FALLBACK, timeout,  INMODE_MAG);
            if (cardInfo.isResultFalg()) {
                int type = cardInfo.getCardType();
                if (type == CardManager.TYPE_MAG){
                    inputMode = ENTRY_MODE_MAG;
                }else {
                    retVal = TcodeError.T_NOT_ALLOW;
                }
                para.setInputMode(inputMode);
                if (inputMode == ENTRY_MODE_MAG) {
                    isDebit = false;
                    return isMag(cardInfo.getTrackNo());
                }
            } else {
                retVal = cardInfo.getErrno();
                if (retVal == 0) {
                    retVal = TcodeError.T_USER_CANCEL_INPUT;
                    return false;
                }else{
                    retVal = TcodeError.T_WAIT_TIMEOUT;
                    return false;
                }
            }
        }else {
            retVal = TcodeError.T_USER_CANCEL_INPUT;
            return false;
        }
        return false;
    }

    protected boolean isICC(boolean isVerify) {
        String creditCard = "SI";
        if (!isVerify)
            transUI.verifyCard(context.getResources().getString(R.string.verificando),false);

        emv = new EmvTransaction(para,transEName);
        emv.setTraceNo(traceNo);
        retVal = emv.start();
        pan = emv.getCardNo();

        if (retVal == 1 || retVal == 0) {
            //Credito
            if (PAYUtils.isNullWithTrim(emv.getPinBlock())) {
                isPinExist = true;
            } else if (emv.getPinBlock().equals("CANCEL")) {//Cancelo usuario
                isPinExist = false;
                retVal = TcodeError.T_USER_CANCEL_PIN_ERR;
                return false;
            } else if (emv.getPinBlock().equals("NULL")) {
                isPinExist = false;
                retVal = TcodeError.T_ERR_PIN_NULL;
                return false;
            } else {//debito
                creditCard = "NO";
                isPinExist = true;
            }

            if (creditCard.equals("NO")){
                pin = emv.getPinBlock();
            }
            setICCData();
            retVal = 0;
            return true;

        } else {
            return false;
        }
    }

    private boolean isMag(String[] tracks) {
        String data1 = null;
        String data2 = null;
        String data3 = null;
        int msgLen = 0;
        if (tracks[0].length() > 0 && tracks[0].length() <= 80) {
            data1 = tracks[0];
        }
        if (tracks[1].length() >= 13 && tracks[1].length() <= 37) {
            data2 = tracks[1];
            if (!data2.contains("=")) {
                retVal = TcodeError.T_SEARCH_CARD_ERR;
            } else {
                String judge = data2.substring(0, data2.indexOf('='));
                if (judge.length() < 13 || judge.length() > 19) {
                    retVal = TcodeError.T_SEARCH_CARD_ERR;
                } else {
                    if (data2.indexOf('=') != -1) {
                        msgLen++;
                    }
                }
            }
        }
        if (tracks[2].length() >= 15 && tracks[2].length() <= 107) {
            data3 = tracks[2];
        }
        if (retVal != 0) {
            return false;
        } else {
            if (msgLen == 0) {
                retVal =  TcodeError.T_SEARCH_CARD_ERR;
                return false;
            } else {

                try {
                    if (!incardTable(data2.substring(0, data2.indexOf('=')))) {
                        retVal = TcodeError.T_UNSUPPORT_CARD;
                        return false;
                    }
                }catch (IndexOutOfBoundsException e) {
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " FinanceTransWs 4 " +  e.getMessage());
                    retVal = TcodeError.T_READ_APP_DATA_ERR;
                    return false;
                }

                int splitIndex = data2.indexOf('=');


                char isDebitChar = data2.charAt(splitIndex + 7);
                if (isDebitChar == '0' || isDebitChar == '5' || isDebitChar == '6' || isDebitChar == '7') {
                    isDebit = true;
                }

                if (data2.length() - splitIndex >= 5) {
                    char iccChar = data2.charAt(splitIndex + 5);

                    if ((iccChar == '2' || iccChar == '6') && (!isFallBack)) {
                        retVal = TcodeError.T_IC_NOT_ALLOW_SWIPE;
                        return false;
                    } else {
                        if (afterMAGJudge(data1, data2, data3))
                            return true;
                    }
                } else {
                    retVal = TcodeError.T_SEARCH_CARD_ERR;
                    return false;
                }

                return afterMAGJudge(data1, data2, data3);

            }
        }
    }

    private boolean afterMAGJudge(String data1, String data2, String data3) {
        String cardNo = data2.substring(0, data2.indexOf('='));
        pan = cardNo;
        track1 = data1;
        track2 = data2;
        track3 = data3;

        if ((retVal = CommonFunctionalities.last4card(timeout, transEName, pan, transUI, false)) != 0) {
            return false;
        }

        if ((retVal = CommonFunctionalities.setCVV2(timeout, transEName, transUI, false)) != 0) {
            return false;
        }

        cvv = CommonFunctionalities.getCvv2();

        return true;
    }

    private boolean isHandle() {
        if ((retVal = CommonFunctionalities.setPanManual(timeout, transEName, transUI)) != 0) {
            return false;
        }

        pan = CommonFunctionalities.getPan();

        if (!incardTable(pan)) {
            retVal = TcodeError.T_UNSUPPORT_CARD;
            return false;
        }

        if ((retVal = CommonFunctionalities.setFechaExp(timeout, transEName, transUI, false)) != 0) {
            return false;
        }

        expDate = CommonFunctionalities.getExpDate();

        if ((retVal = CommonFunctionalities.setCVV2(timeout, transEName, transUI, false)) != 0) {
            return false;
        }

        cvv = CommonFunctionalities.getCvv2();

        return true;
    }


    private boolean pbocTrans() {

        int code = 0;

        PBOCTransProperty property = new PBOCTransProperty();
        property.setTag9c(PBOCTag9c.sale);
        property.setTraceNO(Integer.parseInt(traceNo));
        property.setFirstEC(false);
        property.setForceOnline(true);
        property.setAmounts(amount);
        property.setOtherAmounts(0);
        property.setIcCard(false);

        transUI.handlingBCP(TcodeSucces.PROCESS_TRANS,TcodeSucces.MSGTRANSACCION,false);

        emvl2 = new EmvL2Process(this.context, para);
        emvl2.setTraceNo(traceNo);//JM
        emvl2.setTypeTrans(transEName);

        if ((retVal = emvl2.emvl2ParamInit()) != 0) {
            switch (retVal) {
                case 1:
                    retVal = TcodeError.T_ERR_NOT_FILE_TERMINAL;
                    break;
                case 2:
                    retVal = TcodeError.T_ERR_NOT_FILE_PROCESSING;
                    break;
                case 3:
                    retVal = TcodeError.T_ERR_NOT_FILE_ENTRY_POINT;
                    break;
                default:
                    break;
            }
            return false;
        }

        emvl2.setAmount(amount, 0);
        emvl2.setTypeCoin(typeCoin);//JM
        code = emvl2.start();

        Logger.debug("EmvL2Process return = " + code);
        if (code != 0) {
            if (code==7){
                retVal= TcodeError.T_INSERT_CARD;
            }else if (code == 8){
                retVal= TcodeError.T_ERR_CONTACT_TRANS;
            } else {
                retVal= TcodeError.T_ERR_DETECT_CARD_FAILED;
            }
            return false;
        }

        pan = emvl2.getCardNo();
        panSeqNo = emvl2.getPanSeqNo();
        track2 = emvl2.getTrack2data();
        iccdata = emvl2.getEmvOnlineData();
        variables.setHolderName(emvl2.getHolderName());
        Logger.logLine(Logger.LOG_GENERAL, "PAN =" + pan);

        if (!incardTable(pan)) {
            retVal = TcodeError.T_UNSUPPORT_CARD;
            return false;
        }
        if (emvl2.getCVMType() == EmvL2CVM.L2_CVONLINE_PIN) {
            if (CommonFunctionalities.ctlPIN(pan, timeout, amount, transUI, transEName) != 0) {
                retVal = TcodeError.T_USER_CANCEL_INPUT;
                return false;
            }
            pin = CommonFunctionalities.getPin();
        }
        variables.setCtlSign(emvl2.getCVMType() == EmvL2CVM.L2_CVOBTAIN_SIGNATURE);
        return handlePBOCode(PBOCode.PBOC_REQUEST_ONLINE);
    }

    /**
     * handle PBOC transaction
     *
     * @param code
     */
    private boolean handlePBOCode(int code) {
        if (code != PBOCode.PBOC_REQUEST_ONLINE) {
            retVal = code;
            return false;
        }
        if (inputMode != ENTRY_MODE_NFC)
            setICCDataCTL();
        return true;
    }

    protected boolean setAmount() {

        amountGet = new GetAmount(transUI, timeout, transEName, context);

        if (amountGet.setAmount(selectedAccountItem)) {
            amount = amountGet.getmAmnt();
            retVal = amountGet.getmRetVal();
            para.setAmount(amount);
            para.setOtherAmount(0);
            para.setCurrencyName(currencyName);
            para.setTypeCoin(typeCoin);

            Logger.logLine(Logger.LOG_GENERAL, " setAmount");
            Logger.logLine(Logger.LOG_GENERAL, " amount " + amount);
            Logger.logLine(Logger.LOG_GENERAL, " retVal " + retVal);
            return true;
        } else {
            retVal = amountGet.getmRetVal();
            Logger.logLine(Logger.LOG_GENERAL,  " setAmount : retVal " + retVal);
            return false;
        }
    }

    protected boolean requestPin(String tittle) {

        if ( isDebit) {
            PinInfo info = transUI.getPinpadOnlinePin(timeout, String.valueOf(amount), pan, tittle);
            if (info.isResultFlag()) {
                if (!info.isNoPin()) {
                    isPinExist = info.getPinblock() != null;
                    pin = ISOUtil.hexString(Objects.requireNonNull(info.getPinblock()));
                    retVal = 0;
                }
                if (isPinExist) {
                    return true;
                } else {
                    retVal = info.getErrno();
                    return false;
                }
            } else {
                retVal = TcodeError.T_USER_CANCEL_PIN_ERR;
                return false;
            }
        }
        return true;
    }

    protected int drawMenuHardcode(String title, String[] args){
        int ret = -1;
        String[] labels = {title, "", ""};
        List<SelectedAccountItem> itemSol = new ArrayList<>();
        List<SelectedAccountItem> itemDolar = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            SelectedAccountItem selectedItem = new SelectedAccountItem();
            selectedItem.setCurrencyCode(String.valueOf(i));
            selectedItem.setCurrencyDescription("");
            selectedItem.setFamilyCode("");
            selectedItem.setProductDescription(args[i]);

            itemSol.add(selectedItem);
        }

        InputSelectAccount inputInfo = transUI.showSelectAccount(timeout,labels, itemSol, itemDolar);
        if (inputInfo.isResultFlag()) {
            ret = Integer.parseInt(inputInfo.getResult().getCurrencyCode());
        } else {
            retVal = inputInfo.getErrno();
        }
        return ret;

    }
    //Metodo para mostrar el menu seleccion cuenta y llenar los recycler's
    protected int drawMenuApiRest(String title, ProductsAccounts[] productsAccounts) {
        int ret = -1;
        String[] labels = {title, "", ""};
        List<SelectedAccountItem> itemSol = new ArrayList<>();
        List<SelectedAccountItem> itemDolar = new ArrayList<>();
        boolean sol = false;
        boolean dolar = false;

        for (ProductsAccounts productsAccount : productsAccounts) {
            SelectedAccountItem selectedItem = new SelectedAccountItem();
            selectedItem.setCurrencyCode(productsAccount.getCurrencyCode());
            selectedItem.setCurrencyDescription(productsAccount.getCurrencyDescrip());
            selectedItem.setFamilyCode(productsAccount.getFamilyCode());
            selectedItem.setProductDescription(productsAccount.getProductDescrip());
            if (productsAccount.getCurrencySymbol() != null && !productsAccount.getCurrencySymbol().equals(""))
                selectedItem.setCurrencySymbol(productsAccount.getCurrencySymbol());

            //Si solo es un tipo de cuenta no se muestra en pantalla se toma por defecto esta
            if (productsAccounts.length == 1) {
                if (transEName.equals(Trans.RETIRO) || transEName.equals(Trans.PAGOSERVICIOS)){
                    transUI.alertView( null, context.getResources().getString(transEName.equals(Trans.RETIRO) ? R.string.retAccount : R.string.msgAccountPS),selectedItem.getProductDescription() + " " + selectedItem.getCurrencyDescription() + " " + selectedItem.getCurrencySymbol(),"");
                }
                selectedAccountItem = selectedItem;
                variables.setMultiAccount(false);
                return 0;
            }

            if (context.getResources().getString(R.string.pen).equals(productsAccount.getCurrencyCode())) {
                if (!sol) {
                    sol = true;
                    labels[1] = context.getResources().getString(R.string.ctaSoles);
                }
                itemSol.add(selectedItem);
            }

            if (context.getResources().getString(R.string.usd).equals(productsAccount.getCurrencyCode())) {
                if (!dolar) {
                    dolar = true;
                    labels[2] = context.getResources().getString(R.string.ctaDolares);
                }
                itemDolar.add(selectedItem);
            }

        }

        InputSelectAccount inputInfo = transUI.showSelectAccount(timeout,labels, itemSol, itemDolar);
        if (inputInfo.isResultFlag()) {
            selectedAccountItem = inputInfo.getResult();
            ret = 0;
        } else {
            retVal = inputInfo.getErrno();
        }
        variables.setMultiAccount(true);
        return ret;
    }

    private TransLogDataWs setTransLogWs(){

        TransLogDataWs logDataWs = new TransLogDataWs();

        if (ISOUtil.checkNull(rspExecTrans.getAmnt())){
            logDataWs.setAmount(rspExecTrans.getAmnt());
        }

        if (ISOUtil.checkNull(rspExecTrans.getAmntExcRate())){
            logDataWs.setAmountExchangeRate(rspExecTrans.getAmntExcRate());
        }

        if (ISOUtil.checkNull(rspExecTrans.getExcRate())){
            logDataWs.setExchangeRate(rspExecTrans.getExcRate());
        }

        if (ISOUtil.checkNull(rspExecTrans.getTransDate())){
            logDataWs.setTransactionDate(rspExecTrans.getTransDate());
        }

        if (ISOUtil.checkNull(rspExecTrans.getTransTime())){
            logDataWs.setTransactionTime(rspExecTrans.getTransTime());
        }

        if (ISOUtil.checkNull(rspExecTrans.getTransNumber())){
            logDataWs.setTransactionNumber(rspExecTrans.getTransNumber());
        }

        if (ISOUtil.checkNull(rspExecTrans.getCtnAccountId())){
            logDataWs.setAccountId(rspExecTrans.getCtnAccountId());
        }

        if (ISOUtil.checkNull(rspExecTrans.getCtnArpc())){
            logDataWs.setArpc(rspExecTrans.getCtnArpc());
        }

        if (ISOUtil.checkNull(rspExecTrans.getCtnCardId())){
            logDataWs.setCardId(rspExecTrans.getCtnCardId());
        }

        if (ISOUtil.checkNull(rspExecTrans.getCtnCurrenDescript())){
            logDataWs.setCurrencyDescription(rspExecTrans.getCtnCurrenDescript());
        }

        if (ISOUtil.checkNull(rspExecTrans.getCtnFmlDescript())){
            logDataWs.setFamilyDescription(rspExecTrans.getCtnFmlDescript());
        }

        if (ISOUtil.checkNull(rspExecTrans.getLeadDescript())){
            logDataWs.setLeadDescription(rspExecTrans.getLeadDescript());
        }

        if (ISOUtil.checkNull(rspExecTrans.getPrint())){
            logDataWs.setToPrint(rspExecTrans.getPrint());
        }

        if (ISOUtil.checkNull(transEName)){
            logDataWs.setNameTrans(transEName);
        }

        if (ISOUtil.checkNull(opnNumber)) {
            logDataWs.setOpnNumber(opnNumber);
        }

        if (ISOUtil.checkNull(rspExecTrans.getAvailablelance())){
            logDataWs.setAvailableBalance(rspExecTrans.getAvailablelance());
        }

        if (ISOUtil.checkNull(rspExecTrans.getCountablebalance())){
            logDataWs.setCountableBalance(rspExecTrans.getAvailablelance());
        }

        if (rspExecTrans.getTransactions()!=null && rspExecTrans.getTransactions().length > 0) {
            logDataWs.setProductsTransactions(rspExecTrans.getTransactions());
        }

        logDataWs.setMoneyOut(entryOrExit(transEName));

        logDataWs.setEntryMode(String.valueOf(inputMode));

        if (typebill != null){
            logDataWs.setTypeQuery(typebill);
        }

        if (selectedAccountItem!=null) {
            logDataWs.setCurrencyCode(selectedAccountItem.getCurrencyCode());
        }

        if (ISOUtil.checkNull(rspExecTrans.getCtnCurrencySymbol())){
            logDataWs.setCurrencySymbol(rspExecTrans.getCtnCurrencySymbol());
        }

        if (ISOUtil.checkNull(rspExecTrans.getCtnFullName())){
            logDataWs.setFullName(rspExecTrans.getCtnFullName());
        }

        arqc = getTC();

        return logDataWs;
    }

    private TransLogDataWs setTransLogWsPS(){

        TransLogDataWs logDataWsPS = new TransLogDataWs();

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getAmountCurrencySymbol())){
            logDataWsPS.setAmountCurrencySymbol(rspExecuteTransactionPS.getAmountCurrencySymbol());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getAmount())) {
            logDataWsPS.setAmount(rspExecuteTransactionPS.getAmount());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getFixedChargeCurrencySymbol())){
            logDataWsPS.setFixedChargeCurrencySymbol(rspExecuteTransactionPS.getFixedChargeCurrencySymbol());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getFixedChargeAmount())){
            logDataWsPS.setFixedChargeAmount(rspExecuteTransactionPS.getFixedChargeAmount());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getDelayCurrencySymbol())){
            logDataWsPS.setDelayCurrencySymbol(rspExecuteTransactionPS.getDelayCurrencySymbol());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getDelayAmount())){
            logDataWsPS.setDelayAmount(rspExecuteTransactionPS.getDelayAmount());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getAdditionalInformation())){
            logDataWsPS.setAdditionalInformation(rspExecuteTransactionPS.getAdditionalInformation());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getRows())){
            logDataWsPS.setRows(rspExecuteTransactionPS.getRows());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getDetail())){
            logDataWsPS.setDetail(rspExecuteTransactionPS.getDetail());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getMessage())){
            logDataWsPS.setMessage(rspExecuteTransactionPS.getMessage());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getAccountId())){
            logDataWsPS.setAccountId(rspExecuteTransactionPS.getAccountId());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getTotalAmountCurrencySymbol())){
            logDataWsPS.setTotalAmountCurrency(rspExecuteTransactionPS.getTotalAmountCurrencySymbol());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getCommissionCurrencySymbol())){
            logDataWsPS.setCommissionCurrencySymbol(rspExecuteTransactionPS.getCommissionCurrencySymbol());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getCommissionAmount())){
            logDataWsPS.setCommissionAmount(rspExecuteTransactionPS.getCommissionAmount());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getArpc())){
            logDataWsPS.setArpc(rspExecuteTransactionPS.getArpc());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getCard())){
            logDataWsPS.setCard(rspExecuteTransactionPS.getCard());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getCardId())){
            logDataWsPS.setCardId(rspExecuteTransactionPS.getCardId());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getCategory())) {
            logDataWsPS.setCategory(rspExecuteTransactionPS.getCategory());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getCompany())) {
            logDataWsPS.setCompany(rspExecuteTransactionPS.getCompany());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getPaymentAccount())) {
            logDataWsPS.setPaymentAccount(rspExecuteTransactionPS.getPaymentAccount());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getDescription())) {
            logDataWsPS.setDescription(rspExecuteTransactionPS.getDescription());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getClientDepositCode())){
            logDataWsPS.setDepositCode(rspExecuteTransactionPS.getClientDepositCode());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getClientDepositName())){
            logDataWsPS.setDepositName(rspExecuteTransactionPS.getClientDepositName());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getCommissionCurrencySymbol())){
            logDataWsPS.setCommissionCurrencySymbol(rspExecuteTransactionPS.getCommissionCurrencySymbol());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getConsumption())){
            logDataWsPS.setConsumption(rspExecuteTransactionPS.getConsumption());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getDueDate())){
            logDataWsPS.setDueDate(rspExecuteTransactionPS.getDueDate());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getExchangeRate())){
            logDataWsPS.setExchangeRate(rspExecuteTransactionPS.getExchangeRate());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getCurrencyDescription())){
            logDataWsPS.setCurrencyDescription(rspExecuteTransactionPS.getCurrencyDescription());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getFamilyDescription())){
            logDataWsPS.setFamilyDescription(rspExecuteTransactionPS.getFamilyDescription());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getImportname())){
            logDataWsPS.setImporName(rspExecuteTransactionPS.getImportname());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getLeadDescription())){
            logDataWsPS.setLeadDescription(rspExecuteTransactionPS.getLeadDescription());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getImportt())){
            logDataWsPS.setImportt(rspExecuteTransactionPS.getImportt());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getTotal())){
            logDataWsPS.setTotal(rspExecuteTransactionPS.getTotal());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getTotalAmount())){
            logDataWsPS.setTotalAmount(rspExecuteTransactionPS.getTotalAmount());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getPaymentMethod())){
            logDataWsPS.setPaymentMethod(rspExecuteTransactionPS.getPaymentMethod());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getCurrencySymbol())){
            logDataWsPS.setCurrencySymbol(rspExecuteTransactionPS.getCurrencySymbol());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getToPrint())){
            logDataWsPS.setToPrint(rspExecuteTransactionPS.getToPrint());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getTotalCurrencySymbol())){
            logDataWsPS.setSymbolExchangeRate(rspExecuteTransactionPS.getTotalCurrencySymbol());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getTotalexchange())){
            logDataWsPS.setAmountExchangeRate(rspExecuteTransactionPS.getTotalexchange());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getTransactionDate()))
            logDataWsPS.setTransactionDate(rspExecuteTransactionPS.getTransactionDate());

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getTransactionNumber()))
            logDataWsPS.setTransactionNumber(rspExecuteTransactionPS.getTransactionNumber());

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getTransactionTime())){
            logDataWsPS.setTransactionTime(rspExecuteTransactionPS.getTransactionTime());
        }

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getTotalDebtAmount()))
            logDataWsPS.setTotalDebtAmount(rspExecuteTransactionPS.getTotalDebtAmount());

        if (ISOUtil.checkNull(rspExecuteTransactionPS.getTotalDebtAmountCurrencySymbol()))
            logDataWsPS.setTotalDebtCurrencySymbol(rspExecuteTransactionPS.getTotalDebtAmountCurrencySymbol());

        if (rspExecuteTransactionPS.getListsDebts() != null)
            logDataWsPS.setListsDebts(rspExecuteTransactionPS.getListsDebts());

        if (ISOUtil.checkNull(transEName)){
            logDataWsPS.setNameTrans(transEName);
        }

        if (ISOUtil.checkNull(opnNumber)) {
            logDataWsPS.setOpnNumber(opnNumber);
        }

        logDataWsPS.setMoneyOut(entryOrExit(transEName));

        logDataWsPS.setEntryMode(String.valueOf(inputMode));

        if (typebill != null){
            logDataWsPS.setTypeQuery(typebill);
        }

        if (selectedAccountItem!=null) {
            logDataWsPS.setCurrencyCode(selectedAccountItem.getCurrencyCode());
        }

        arqc = getTC();

        return logDataWsPS;
    }

    private TransLogDataWs setTransLogWsCobros(){

        TransLogDataWs logDataWs = new TransLogDataWs();

        if (ISOUtil.checkNull(rspExecTransGiros.getRspOperationDate())){
            logDataWs.setTransactionDate(rspExecTransGiros.getRspOperationDate());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspOperationTime())){
            logDataWs.setTransactionTime(rspExecTransGiros.getRspOperationTime());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspOperationNumber())){
            logDataWs.setTransactionNumber(rspExecTransGiros.getRspOperationNumber());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspCardId())){
            logDataWs.setCardId(rspExecTransGiros.getRspCardId());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspBankDraftReference())){
            logDataWs.setReference(rspExecTransGiros.getRspBankDraftReference());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspNameBenef())){
            logDataWs.setNameBenficiary(rspExecTransGiros.getRspNameBenef());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspDocumentTypeDescriptionBenef())){
            logDataWs.setDocTypeBeneficiary(rspExecTransGiros.getRspDocumentTypeDescriptionBenef());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspDocumentNumberBenef())){
            logDataWs.setDocNumBeneficiary(rspExecTransGiros.getRspDocumentNumberBenef());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspNameRemit())){
            logDataWs.setNameRemitter(rspExecTransGiros.getRspNameRemit());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspDocumentTypeDescriptionRemit())){
            logDataWs.setDocTypeRemitter(rspExecTransGiros.getRspDocumentTypeDescriptionRemit());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspDocumentNumberRemit())){
            logDataWs.setDocNumRemitter(rspExecTransGiros.getRspDocumentNumberRemit());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspAmountCurrencySymbol())){
            logDataWs.setCurrencySymbol(rspExecTransGiros.getRspAmountCurrencySymbol());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspAmount())){
            logDataWs.setAmount(rspExecTransGiros.getRspAmount());
        }

        if (ISOUtil.checkNull(transEName)){
            logDataWs.setNameTrans(transEName);
        }

        if (ISOUtil.checkNull(opnNumber)) {
            logDataWs.setOpnNumber(opnNumber);
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspCommissionCurrencySymbol())){
            logDataWs.setCommissionCurrencySymbol(rspExecTransGiros.getRspCommissionCurrencySymbol());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspCommissionAmount())){
            logDataWs.setCommissionAmount(rspExecTransGiros.getRspCommissionAmount());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspTotalCurrencySymbol())){
            logDataWs.setSymbolExchangeRate(rspExecTransGiros.getRspTotalCurrencySymbol());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspTotal())){
            logDataWs.setAmountExchangeRate(rspExecTransGiros.getRspTotal());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspExchangeRate())){
            logDataWs.setExchangeRate(rspExecTransGiros.getRspExchangeRate());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspTotalAmountCurrencySymbol())){
            logDataWs.setTotalAmountCurrency(rspExecTransGiros.getRspTotalAmountCurrencySymbol());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspTotalAmount())){
            logDataWs.setTotalAmount(rspExecTransGiros.getRspTotalAmount());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspPaymentMethod())){
            logDataWs.setPaymentMethod(rspExecTransGiros.getRspPaymentMethod());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspFamilyDescription())){
            logDataWs.setFamilyDescription(rspExecTransGiros.getRspFamilyDescription());
        }

        if (selectedAccountItem!=null) {
            logDataWs.setCurrencyCode(selectedAccountItem.getCurrencyCode());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspAccountId())){
            logDataWs.setAccountId(rspExecTransGiros.getRspAccountId());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspToPrint())){
            logDataWs.setToPrint(rspExecTransGiros.getRspToPrint());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspLeadDescription())){
            logDataWs.setLeadDescription(rspExecTransGiros.getRspLeadDescription());
        }

        if (ISOUtil.checkNull(rspExecTransGiros.getRspCurrencySymbol()))
            logDataWs.setCurrencyDescription(rspExecTransGiros.getRspCurrencySymbol());

        logDataWs.setMoneyOut(String.valueOf(arguments.getTypetransaction()));

        logDataWs.setEntryMode(String.valueOf(inputMode));

        arqc = getTC();

        return logDataWs;
    }

    private TransLogDataWs setTransLogWsEmision() {

        TransLogDataWs logDataWs = new TransLogDataWs();

        if (ISOUtil.checkNull(transEName)){
            logDataWs.setNameTrans(transEName);
        }

        if (ISOUtil.checkNull(opnNumber)) {
            logDataWs.setOpnNumber(opnNumber);
        }

        if (ISOUtil.checkNull(rspExecTransEmision.getRspTransactionDate()))
            logDataWs.setTransactionDate(rspExecTransEmision.getRspTransactionDate());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspTransactionNumber()))
            logDataWs.setTransactionNumber(rspExecTransEmision.getRspTransactionNumber());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspTransactionTime()))
            logDataWs.setTransactionTime(rspExecTransEmision.getRspTransactionTime());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspArpc()))
            logDataWs.setArpc(rspExecTransEmision.getRspArpc());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspCardId()))
            logDataWs.setCardId(rspExecTransEmision.getRspCardId());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspBankDraftReference()))
            logDataWs.setReference(rspExecTransEmision.getRspBankDraftReference());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspNameBenef()))
            logDataWs.setNameBenficiary(rspExecTransEmision.getRspNameBenef());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspDocumentTypeDescriptionBenef()))
            logDataWs.setDocTypeBeneficiary(rspExecTransEmision.getRspDocumentTypeDescriptionBenef());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspDocumentNumberBenef()))
            logDataWs.setDocNumBeneficiary(rspExecTransEmision.getRspDocumentNumberBenef());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspNameRemitter()))
            logDataWs.setNameRemitter(rspExecTransEmision.getRspNameRemitter());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspDocumentTypeDesRemitter()))
            logDataWs.setDocTypeRemitter(rspExecTransEmision.getRspDocumentTypeDesRemitter());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspDocumentNumberRemitter()))
            logDataWs.setDocNumRemitter(rspExecTransEmision.getRspDocumentNumberRemitter());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspNameSender()))
            logDataWs.setNameSender(rspExecTransEmision.getRspNameSender());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspDocumentTypeDeSender()))
            logDataWs.setDocTypeSender(rspExecTransEmision.getRspDocumentTypeDeSender());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspDocumentNumberSender()))
            logDataWs.setDocNumSender(rspExecTransEmision.getRspDocumentNumberSender());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspAmount()))
            logDataWs.setAmount(rspExecTransEmision.getRspAmount());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspAmountCurrencySymbol()))
            logDataWs.setCurrencySymbol(rspExecTransEmision.getRspAmountCurrencySymbol());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspCommissionAmount()))
            logDataWs.setCommissionAmount(rspExecTransEmision.getRspCommissionAmount());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspCommissionCurrencySymbol()))
            logDataWs.setCommissionCurrencySymbol(rspExecTransEmision.getRspCommissionCurrencySymbol());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspTotalCurrencySymbol()))
            logDataWs.setSymbolExchangeRate(rspExecTransEmision.getRspTotalCurrencySymbol());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspTotal()))
            logDataWs.setAmountExchangeRate(rspExecTransEmision.getRspTotal());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspExchangeRate()))
            logDataWs.setExchangeRate(rspExecTransEmision.getRspExchangeRate());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspTotalAmountCurrencySymbol()))
            logDataWs.setTotalAmountCurrency(rspExecTransEmision.getRspTotalAmountCurrencySymbol());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspTotalAmount()))
            logDataWs.setTotalAmount(rspExecTransEmision.getRspTotalAmount());

        if (ISOUtil.checkNull(rspExecTransEmision.getRsp_PaymentMethod()))
            logDataWs.setPaymentMethod(rspExecTransEmision.getRsp_PaymentMethod());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspFamilyDescription()))
            logDataWs.setFamilyDescription(rspExecTransEmision.getRspFamilyDescription());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspCurrencyDescription()))
            logDataWs.setCurrencyDescription(rspExecTransEmision.getRspCurrencyDescription());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspAccountId()))
            logDataWs.setAccountId(rspExecTransEmision.getRspAccountId());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspToPrint()))
            logDataWs.setToPrint(rspExecTransEmision.getRspToPrint());

        if (ISOUtil.checkNull(rspExecTransEmision.getRspLeadDescription()))
            logDataWs.setLeadDescription(rspExecTransEmision.getRspLeadDescription());

        logDataWs.setMoneyOut(String.valueOf(arguments.getTypetransaction()));

        logDataWs.setEntryMode(String.valueOf(inputMode));

        arqc = getTC();

        if (selectedAccountItem!=null) {
            logDataWs.setCurrencyCode(selectedAccountItem.getCurrencyCode());
        }

        return logDataWs;

    }

    private String entryOrExit(String nameTrans){
        String ret = "1";
        switch (nameTrans){
            case DEPOSITO:
                ret = "1";
                break;
            case RETIRO:
                ret = "2";
                break;
            default:
                break;
        }
        return ret;
    }

    protected String getTC() {
        byte[] temp = new byte[128];
        int len = PAYUtils.getTlvDataKernal(0x9F26, temp);
        String aux = ISOUtil.bcd2str(temp, 0, len);
        Logger.debug("ARQC: " + aux.trim());
        return aux.trim();
    }

    protected byte[] field55(String arpc){
        byte[] temp = new byte[256];
        int tempSize = 0;

        PAYUtils.packTlvData(temp,91,ISOUtil.str2bcd(arpc,false).length,ISOUtil.str2bcd(arpc,false),tempSize);

        return temp;
    }

    protected boolean funcCipherDocument(String doc){
        if (cipherDocument ==null)
            cipherDocument = new CipherDocument();
        cipherDocument.setDocument(doc);
        return cipherDocument.generatePinblock();
    }
}
