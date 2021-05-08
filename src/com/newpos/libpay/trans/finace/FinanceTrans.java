package com.newpos.libpay.trans.finace;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.android.newpos.libemv.EMVISRCode;
import com.android.newpos.libemv.PBOCOnlineResult;
import com.android.newpos.libemv.PBOCode;
import com.android.newpos.pay.R;
import com.bcp.file_management.FilesManagement;
import com.bcp.settings.BCPConfig;
import com.bcp.transactions.certificado.ProcessField61;
import com.bcp.transactions.common.CommonFunctionalities;
import com.newpos.libpay.Logger;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.process.EmvTransaction;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.Trans;
import com.newpos.libpay.trans.TransInputPara;
import com.newpos.libpay.trans.manager.RevesalTrans;
import com.newpos.libpay.trans.manager.ScriptTrans;
import com.newpos.libpay.trans.translog.TransLog;
import com.newpos.libpay.trans.translog.TransLogData;
import com.newpos.libpay.utils.ISOUtil;
import com.newpos.libpay.utils.PAYUtils;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static com.android.newpos.pay.ProcessingCertificate.certificate;
import static com.android.newpos.pay.ProcessingCertificate.polarisUtil;
import static com.android.newpos.pay.StartAppBCP.agente;
import static com.android.newpos.pay.StartAppBCP.variables;

/**
 * 金融交易类
 *
 * @author zhouqiang
 */
public class FinanceTrans extends Trans {

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
    protected int inputMode = 0x02;// 刷卡模式 1 手输卡号；2刷卡；5 3插IC；7 4非接触卡

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
    private boolean isNeedGAC2;

    protected long offsetInit;

    protected String fileNameInit;

    protected FilesManagement filesManagement;

    /**
     * 金融交易类构造
     *
     * @param ctx
     * @param transEname
     */
    public FinanceTrans(Context ctx, String transEname, TransInputPara p) {
        super(ctx, transEname, true, p);
        iso8583.setHasMac(false);
        setTraceNoInc(true);
    }

    public FinanceTrans(Context ctx, String transEname, String fileNameLog) {
        super(ctx, transEname, fileNameLog);
        iso8583.setHasMac(false);
        setTraceNoInc(true);
    }

    private boolean setFieldTrans() {

        localTime = PAYUtils.getLocalTime(PAYUtils.TIMEWITHOUTPOINT);
        localDate = PAYUtils.getLocalDate();

        switch (transEName){
            case INIT_P:
            case INIT_T:
                setFieldInit();
                return true;
            case LOGIN:
                setField();
                return  true;
            case CERTIFICADO:
                setFieldCertificado();
                return true;
            default:
                break;
        }
        return false;
    }

    private void setFieldInit() {

        String sizeReqBytes = "9000";
        iso8583.clearData();

        if (msgID != null) {
            iso8583.setField(0, msgID);
        }

        if (procCode != null) {
            iso8583.setField(3, procCode);
        }

        if (traceNo != null) {
            iso8583.setField(11, traceNo);
        }

        if (BCPConfig.getInstance(context).getTrack2Agente(BCPConfig.TRACK2AGENTEKEY) != null) {
            iso8583.setField(35, BCPConfig.getInstance(context).getTrack2Agente(BCPConfig.TRACK2AGENTEKEY));
        }

        if (termID != null) {
            iso8583.setField(41, termID);
        }

        field60 = fileNameInit+","+offsetInit+","+sizeReqBytes;
        iso8583.setField(60, field60);

        if (jweDataEncryptDecrypt.getDataEncrypt() != null) {
            iso8583.setField(61, jweDataEncryptDecrypt.getDataEncrypt());
        }

        if (IT.equals(para.getTypeInit())){
            field62 = IT;
        }else if (IP.equals(para.getTypeInit()) && polarisUtil.isInit()){
            field62 = IP;
        }else
            field62 = IT;

        iso8583.setField(62, field62);

        //Descomentar para implementar una vez plataforma agregue la recepcion del campo
        iso8583.setField(63, getVersion());

    }
    
    private String getVersion(){
        String version;
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionName;
        }catch (PackageManager.NameNotFoundException e){
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            version = "0.0.0";
        }
        return version;
    }

    protected void setField(){
        iso8583.setHasMac(false);
        iso8583.clearData();

        localTime = PAYUtils.getLocalTime(PAYUtils.TIMEWITHOUTPOINT);
        localDate = PAYUtils.getLocalDate();

        if (msgID != null) {
            iso8583.setField(0, msgID);
        }
        if (procCode != null) {
            iso8583.setField(3, procCode);
        }

        if (traceNo != null) {
            iso8583.setField(11, traceNo);
        }

        if (TMConfig.getInstance().getNii() != null) {
            nii = ISOUtil.padleft(TMConfig.getInstance().getNii()+ "", 4, '0');
            iso8583.setField(24, nii);
        }

        if (termID != null) {
            iso8583.setField(41, termID);
        }

        if (merchID != null){
            iso8583.setField(42, merchID);
        }

        iso8583.setField(60, field60);
    }

    private void setFieldsSettle() {

        iso8583.clearData();
        Logger.debug("==FinanceTrans->setFieldsLogout==");

        if (msgID != null) {
            iso8583.setField(0, msgID);
        }

        if (procCode != null) {
            iso8583.setField(3, procCode);
        }

        if (traceNo != null) {
            iso8583.setField(11, traceNo);
        }

        if (nii != null) {
            iso8583.setField(24, nii);
        }

        if (termID != null) {
            iso8583.setField(41, termID);
        }

        if (merchID != null) {
            iso8583.setField(42, merchID);
        }

        if (batchNo != null) {
            iso8583.setField(60, batchNo);
        }

        if (field63 != null)
            iso8583.setField(63, field63);

    }

    private void setFieldCertificado(){

        String sizeReqBytes = "9000";//tamaño de solicitud por transaccion
        iso8583.clearData();
        Logger.debug("==FinanceTrans->setFieldsLogout==");

        if (msgID != null) {
            iso8583.setField(0, msgID);
        }

        if (procCode != null) {
            iso8583.setField(3, procCode);
        }

        if (traceNo != null) {
            iso8583.setField(11, traceNo);
        }

        if (termID != null) {
            iso8583.setField(41, termID);
        }

        field60 = fileNameInit+","+offsetInit+","+sizeReqBytes;
        iso8583.setField(60, field60);

        iso8583.setField(61,context.getResources().getString(R.string.app_name));
    }

    private void setFieldsBatchNo(TransLogData data) {

        iso8583.clearData();
        Logger.debug("==FinanceTrans->setFields==");

        iso8583.setField(0, "0320");

        switch (data.getEntryMode()) {
            case "011"://Manual
            case "012"://Manual unionPay
                if (data.getPan() != null)
                    iso8583.setField(2, data.getPan());

                if (data.getExpDate() != null)
                    iso8583.setField(14, data.getExpDate());
                break;
            case "101"://PE
            case "102"://PE
                if (data.getPanPE() != null)
                    iso8583.setField(2, data.getPanPE());
                if (data.getExpDate() != null)
                    iso8583.setField(14, data.getExpDate());
                break;
            default:
                break;
        }

        if (data.getProcCode() != null)
            iso8583.setField(3, data.getProcCode());

        String amoutData;
        amoutData = ISOUtil.padleft(data.getAmount() + "", 12, '0');
        iso8583.setField(4, amoutData);

        if (data.getTraceNo() != null) {
            iso8583.setField(11, data.getTraceNo());
        }

        if (data.getLocalTime() != null) {
            iso8583.setField(12, data.getLocalTime());
        }

        if (data.getLocalDate() != null) {
            iso8583.setField(13, data.getLocalDate().substring(4));
        }


        if (data.getEntryMode() != null) {
            iso8583.setField(22, data.getEntryMode());
        }

        if (data.getNii() != null) {
            iso8583.setField(24, data.getNii());
        }

        if (data.getSvrCode() != null) {
            iso8583.setField(25, data.getSvrCode());
        }

        if (data.getTrack2() != null) {
            iso8583.setField(35, data.getTrack2());
        }

        if (data.getRrn() != null) {
            iso8583.setField(37, data.getRrn());
        }

        if (data.getAuthCode() != null) {
            iso8583.setField(38, data.getAuthCode());
        }

        if (data.getTermID() != null) {
            iso8583.setField(41, data.getTermID());
        }
        if (data.getMerchID() != null) {
            iso8583.setField(42, data.getMerchID());
        }

        if (data.getTrack1() != null) {
            iso8583.setField(45, data.getTrack1());
        }

        if (data.getCvv() != null) {
            iso8583.setField(48, data.getCvv());
        }

        if (data.getField54() != null) {
            iso8583.setField(54, data.getField54());
        }

        if (data.getField57() != null) {
            iso8583.setField(57, data.getField57());
        }

        if (data.getField59() != null) {
            iso8583.setField(59, data.getField59());
        }

        iso8583.setField(60, data.getMsgID() + data.getTraceNo() + data.getBatchNo());


    }

    private void setNII() {
        if (TMConfig.getInstance().getNii() != null)
            nii = ISOUtil.padleft(TMConfig.getInstance().getNii() + "", 4, '0');
        else
            nii = "0000";
    }

    protected void connecting(){
        transUI.handlingBCP(TcodeSucces.CONNECTING_CENTER ,TcodeSucces.MSGEMPTY,false);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " FinanceTrans " +  e.getMessage());
        }
    }

    protected int tryConnect(){
        int retries = 3;  //Intentos
        int startRetries = 1;
        int rta = 0;


        Logger.logLine(Logger.LOG_GENERAL, "=============== tryConnect");


        do { // Intentara N veces el connect
            transUI.handlingBCP(TcodeSucces.CONNECTING_CENTER ,TcodeSucces.MSGEMPTY,false);
            rta = connect();

            Logger.logLine(Logger.LOG_GENERAL, "tryConnect iteracion");
            Logger.logLine(Logger.LOG_GENERAL, "rta = " + rta);
            Logger.logLine(Logger.LOG_GENERAL, "startRetries = " + startRetries);


            if (rta == 0) {
                startRetries = retries;
            }
            startRetries ++;




            transUI.handlingBCP(TcodeSucces.MSG_RETRY ,TcodeSucces.MSGEMPTY,false);
            transUI.handlingBCP(TcodeSucces.CONNECTING_CENTER ,TcodeSucces.MSGEMPTY,false);
        }while (retries >= startRetries);

        if (rta == -1){



            retries = 3;
            startRetries = 1;
            cfg = TMConfig.getInstance();
            cfg.setPubCommun(false);
            loadConfigIP();

            Logger.logLine(Logger.LOG_GENERAL, "rta == -1");

            do {
                transUI.handlingBCP(TcodeSucces.CONNECTING_CENTER ,TcodeSucces.MSGEMPTY,false);
                rta = connect();

                Logger.logLine(Logger.LOG_GENERAL, "tryConnect iteracion");
                Logger.logLine(Logger.LOG_GENERAL, "rta = " + rta);
                Logger.logLine(Logger.LOG_GENERAL, "startRetries = " + startRetries);

                if (rta == 0) {
                    startRetries = retries;
                }
                startRetries ++;
            }while (retries >= startRetries);
        }

        Logger.logLine(Logger.LOG_GENERAL, "rta final = " + rta);

        retVal = rta == -1 ? TcodeError.T_SOCKET_ERR : rta;

        Logger.logLine(Logger.LOG_GENERAL, "retVal : " + retVal);

        return retVal;
    }

    protected int onlineTransInit(){

        Logger.logLine(Logger.LOG_GENERAL, "onlineTransInit");


        if(!setFieldTrans())
            return TcodeError.T_ERR_PACK_ISO;

        field60 = "";

        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER ,TcodeSucces.MSGEMPTY,false);
        retVal = send();

        Logger.logLine(Logger.LOG_GENERAL, "retVal send : " + retVal);

        if (retVal == 0 && isTraceNoInc) {
            cfg.incTraceNo();
            traceNo = ISOUtil.padleft("" + cfg.getTraceNo(), 6, '0');
        }

        transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV ,TcodeSucces.MSGINFORMATION,false);

        byte[] respData = recive();

        if (respData == null || respData.length <= 0) {
            return TcodeError.T_RECEIVE_ERR;
        }

        if ((retVal = iso8583.unPacketISO8583(respData)) != 0)
            return retVal;

        rspCode = iso8583.getfield(39);

        if (transEName.equals(Trans.CERTIFICADO) && iso8583.getfield(61) != null){
            try {
                ProcessField61 processField61 = new ProcessField61();
                processField61.processFld(certificate.decrypt(iso8583.getfield(61) != null ? iso8583.getfield(61) : "",BCPConfig.getInstance(context).getSecretKey(BCPConfig.PWDSECRETKEY).getBytes()));

                BCPConfig.getInstance(context).setPwdKeystoreTransPub(BCPConfig.PWDKEYSTORETRANSPUBKEY,processField61.getKeyStoreTransPub() != null ? processField61.getKeyStoreTransPub() : "");
                BCPConfig.getInstance(context).setPwdKeystoreTransPriv(BCPConfig.PWDKEYSTORETRANSPRIVKEY,processField61.getKeyStoreTransPriv() != null ? processField61.getKeyStoreTransPriv() : "");
                BCPConfig.getInstance(context).setPwdKeyStorePolaris(BCPConfig.PWDKEYSTOREPOLARISKEY,processField61.getKeyStorePolaris() != null ? processField61.getKeyStorePolaris() : "");
                BCPConfig.getInstance(context).setPwdKeystoreBcp(BCPConfig.PWDKEYSTOREBCPKEY,processField61.getKeyStoreBcp() != null ? processField61.getKeyStoreBcp() : "");
                BCPConfig.getInstance(context).setPwdKeystoreHttps(BCPConfig.PWDKEYSTOREHTTPSKEY,processField61.getKeyStoreHttps() != null ? processField61.getKeyStoreHttps() : "");
            } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                retVal = TcodeError.T_ERR_DECRYPT_DATA;
                return retVal;
            }
        }

        if (transEName.equals(Trans.INIT_T) || transEName.equals(Trans.INIT_P) && iso8583.getfield(61) != null && iso8583.getfield(62) != null){
            if (jweEncryptDecrypt(iso8583.getfield(61) + "", true, true))
                polarisUtil.injectGirosKey(jweDataEncryptDecrypt.getDataDecrypt());

            if (iso8583.getfield(62) != null && jweEncryptDecrypt(iso8583.getfield(62) + "", true, true))
                polarisUtil.injectWorkingKey(jweDataEncryptDecrypt.getDataDecrypt());
        }

        PAYUtils.dateTime(iso8583.getfield(13),iso8583.getfield(12));

        if (!"00".equals(rspCode)) {
            TransLog.clearReveral();
            field60 = ""+iso8583.getfield(60);
            return rspCode != null ? Integer.parseInt(rspCode) : TcodeError.T_PACKAGE_ILLEGAL;//Revisar como mostrar el mensaje que recibe del host
        }

        if ((field64=iso8583.getfield(64))!=null){
            if ((field63=iso8583.getfield(63))==null){
                return TcodeError.T_RECEIVE_ERR;
            }

            if (!filesManagement.writeFile(field63,iso8583.getField64(),iso8583.getLen64()))
                return TcodeError.T_RECEIVE_ERR;
        }

        if ("960101".equals(iso8583.getfield(3)) || ("970101".equals(iso8583.getfield(3)))){
            offsetInit += iso8583.getLen64();
            onlineTransInit();
        }
        procCode = iso8583.getfield(3);

        return retVal;
    }

    protected int onlineTransFinance(EmvTransaction emvTrans) {

        byte[] tag9f27 = new byte[1];
        byte[] tag9b = new byte[2];

        setNII();
        if(!setFieldTrans())
            return TcodeError.T_ERR_PACK_ISO;

        if (isProcPreTrans) {
            TransLogData revesalData = TransLog.getReversal();
            if (revesalData != null) {
                transUI.handlingBCP(TcodeSucces.TERMINAL_REVERSAL,TcodeSucces.MSGEMPTY,false);
                RevesalTrans revesal = new RevesalTrans(context, "REVERSAL", para);
                for (int i = 0; i < 1; i++) {
                    retVal = revesal.sendRevesal();
                    if (retVal == 0) {
                        TransLog.clearReveral();
                        transUI.toasTrans(TcodeSucces.REV_RECEIVE_OK, true, false);
                        break;
                    }
                }
                if (retVal == TcodeError.T_SOCKET_ERR || retVal == TcodeError.T_SEND_ERR) {
                    return retVal;
                } else {
                    if (retVal != 0) {
                        return TcodeError.T_REVERSAL_FAIL;
                    }
                }
            }
        }

        connecting();

        if((retVal = tryConnect())!=0)
            return retVal;

        if (isReversal) {
            Logger.debug("FinanceTrans->OnlineTrans->save Reversal");
            TransLogData reveral = setReveralData();
            TransLog.saveReversal(reveral);
        }

        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER, TcodeSucces.MSGINFORMATION,false);
        retVal = send();

        if (retVal == -1) {
            return TcodeError.T_SEND_ERR;
        }

        if (retVal == 0 && isTraceNoInc) {
            cfg.incTraceNo();
        }

        transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV, TcodeSucces.MSGINFORMATION, false);
        byte[] respData = recive();
        netWork.close();
        if (respData == null || respData.length <= 0) {
            return TcodeError.T_RECEIVE_ERR;
        }

        retVal = iso8583.unPacketISO8583(respData);

        if (retVal != 0) {
            if (retVal == TcodeError.T_PACKAGE_MAC_ERR && isReversal) {
                //Devuelva el mensaje de verificación de error de MAC, actualice la causa correcta A0
                TransLogData newR = TransLog.getReversal();
                newR.setRspCode("A0");
                TransLog.clearReveral();
                TransLog.saveReversal(newR);
            }
            return retVal;
        }

        rspCode = iso8583.getfield(39);

        PAYUtils.dateTime(iso8583.getfield(13),iso8583.getfield(12));

        String strICC = iso8583.getfield(55);

        authCode = iso8583.getfield(38);

        if (strICC != null && (!strICC.trim().equals(""))) {
            iccdata = ISOUtil.str2bcd(strICC, false);
        } else {
            iccdata = null;
        }

        if ("95".equals(rspCode) && (para.getTransType().equals(SETTLE))) {
            transUI.handlingBCP(TcodeSucces.SETTLE_ERROR,TcodeSucces.MSGEMPTY, false);
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " FinanceTrans 2 " +  e.getMessage());
                }
                break;
            }
            transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER,TcodeSucces.MSGINFORMATION,false);
            retVal = sendBatchUpload();
            if (retVal == 0) {
                procCode = "960000";
                setFieldsSettle();
                retVal = onLineTrans();
                if (retVal == 0)
                    rspCode = iso8583.getfield(39);
            }
        }
        if ("95".equals(rspCode)) {
            return 95;
        }

        if (!"00".equals(rspCode) && !rspCode.equals("05")) {
            TransLog.clearReveral();
            return formatRsp(rspCode);
        }


        if (inputMode == ENTRY_MODE_ICC) {
            boolean need2AC = transEName.equals(RETIRO);

            if (emvTrans != null && retVal == 0 && need2AC) {
                retVal = emvTrans.afterOnline(rspCode, authCode, iccdata, retVal);
                int lenOf9f27 = PAYUtils.getTlvDataKernal(0x9F27, tag9f27);
                if (lenOf9f27 != 1) {
                    // Procesamiento de falla de IC Si el campo 39 es 00, el archivo de actualización es correcto. 39 Campo 06
                    TransLogData revesalData = TransLog.getReversal();
                    if (revesalData != null) {
                        revesalData.setRspCode("06");
                        TransLog.saveReversal(revesalData);
                    }
                }
                if (tag9f27[0] != 0x40) {
                    // Aprobado en segundo plano, rechazado por la tarjeta, para mantener el golpe
                    return TcodeError.T_GEN_2_AC_FAIL;
                }
                //Resultado del script del emisor
                int len9b = PAYUtils.getTlvDataKernal(0x9b, tag9b);
                if (len9b == 2 && (tag9b[0] & 0x04) != 0) {
                    // Guarde los resultados del script de línea de tarjeta
                    byte[] temp = new byte[256];
                    int len = PAYUtils.packTags(PAYUtils.getTagsEMV(), temp);
                    if (len > 0) {
                        iccdata = new byte[len];
                        System.arraycopy(temp, 0, iccdata, 0, len);
                    } else {
                        iccdata = null;
                    }
                    TransLogData scriptResult = setScriptData();
                    TransLog.saveScriptResult(scriptResult);
                }
            }

        } else if (inputMode == ENTRY_MODE_NFC && isNeedGAC2) {
            retVal = genAC2Trans();
            if (retVal != PBOCode.PBOC_TRANS_SUCCESS) {
                return retVal;
            }
        }

        if (retVal != 0) {
            return retVal;
        }


        //脚本上送
        TransLogData data = TransLog.getScriptResult();
        if (data != null) {
            ScriptTrans script = new ScriptTrans(context, "SENDSCRIPT", para);
            int ret = script.sendScriptResult(data);
            if (ret == 0) {
                TransLog.clearScriptResult();
            }
        }


        TransLogData logData;

        if (isSaveLog) {
            logData = setLogData();
            transLog.saveLog(logData, hostId);

            //Actualiza la fecha de siguiente cierre si la trans que se esta guardando es la primerta del nuevo lote
            Date fechaHora;
            try{
                fechaHora = PAYUtils.strToDate(PAYUtils.dateToStr(new Date(),"MM/dd/yyyy")+ " " +PAYUtils.formato2Hora(localTime),"MM/dd/yyyy HH:mm");
            }catch (Exception e){
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " FinanceTrans 3 " +  e.getMessage());
                fechaHora = null;
            }
            CommonFunctionalities.updateDateFirstTrans(hostId, context,fechaHora);
        }
        TransLog.clearReveral();

        return retVal;
    }

    private int sendBatchUpload() {

        List<TransLogData> list = TransLog.getInstance(variables.getIdAcquirer()).getData();
        TransLogData data;

        for (int i = 0; i < list.size(); i++) {

            data = list.get(i);
            setFieldsBatchNo(data);

            retVal = onLineTrans();

            if (retVal == 0) {
                rspCode = iso8583.getfield(39);
                if (!rspCode.equals("00")) {
                    return TcodeError.T_RECEIVE_REFUSE;
                }
            } else {
                Logger.debug("Revesal result :" + retVal);
            }
        }
        return retVal;
    }

    private TransLogData setLogData() {

        TransLogData logData = new TransLogData();

        if (msgID != null) {
            logData.setMsgID(msgID);
        }

        if (procCode != null) {
            logData.setProcCode(procCode);
        }

        if (typeCoin != null) {
            logData.setTypeCoin(typeCoin);
        }

        if (iso8583.getfield(2)!=null){
            logData.setPanNormal(iso8583.getfield(2));
            logData.setPan(packageMaskedCard(iso8583.getfield(2)));
            logData.setPanPE(packageMaskedCard(iso8583.getfield(2)));
        }else {
            if (pan != null) {
                logData.setPan(pan);
                logData.setPanNormal(pan);
                logData.setPan(packageMaskedCard(pan));
            }
        }

        if (expDate != null) {
            logData.setExpDate(expDate);
        }

        logData.setOprNo(cfg.getOprNo());

        if (batchNo != null) {
            logData.setBatchNo(batchNo);
        }
        if (transEName != null) {
            logData.setEName(transEName);
        }

        if (amount >= 0) {
            logData.setAmount(amount);
        }

        if (extAmount != null) {
            logData.setField54(extAmount);
        }

        if (iso8583.getfield(11) != null) {
            logData.setTraceNo(iso8583.getfield(11));
        } else if (traceNo != null) {
            logData.setTraceNo(traceNo);
        }

        if (localTime != null) {
            logData.setLocalTime(localTime);
        }

        logData.setLocalDate(PAYUtils.getYear() + localDate);

        logData.setDatePrint(PAYUtils.getMonth() + " " + PAYUtils.getDay() + "," + PAYUtils.getYear());

        if (numCuotas > 0) {
            logData.setNumCuotas(numCuotas);
        }
        if (iso8583.getfield(14) != null) {
            logData.setExpDate(iso8583.getfield(14));
        }
        if (iso8583.getfield(15) != null) {
            logData.setSettleDate(iso8583.getfield(15));
        }
        if (entryMode != null) {
            logData.setEntryMode(entryMode);
        }
        if (iso8583.getfield(23) != null) {
            logData.setPanSeqNo(iso8583.getfield(23));
        }
        if (nii != null) {
            logData.setNii(nii);
        }
        if (svrCode != null) {
            logData.setSvrCode(svrCode);
        }
        if (iso8583.getfield(32) != null) {
            logData.setAcquirerID(iso8583.getfield(32));
        }
        if (track2 != null) {
            logData.setTrack2(track2);
        }
        if (iso8583.getfield(37) != null) {
            logData.setRrn(iso8583.getfield(37));
        }

        if (iso8583.getfield(38) != null) {
            logData.setAuthCode(iso8583.getfield(38));
        }

        if (iso8583.getfield(39) != null) {
            logData.setRspCode(iso8583.getfield(39));
        }
        if (termID != null) {
            logData.setTermID(termID);
        }
        if (merchID != null) {
            logData.setMerchID(merchID);
        }

        //Si se recibe el P42 se maneja como interoperabilidad en el Voucher
        if (iso8583.getfield(42) != null){
            logData.setMidInteroper(iso8583.getfield(42));
        }

        if (iso8583.getfield(49) != null) {
            logData.setCurrencyCode(iso8583.getfield(49));
        }

        if (field57 != null) {
            logData.setField57(field57);
        }
        if (iso8583.getfield(57) != null) {
            logData.setField57Print(iso8583.getfield(57));
        }

        if (iso8583.getfield(59) != null) {
            logData.setField59(iso8583.getfield(59));
        }else{
            if (field59 != null){
                logData.setField59(field59);
            }
        }

        if (currencyCode != null) {
            logData.setCurrencyCode(currencyCode);
        }
        if (pin != null) {
            logData.setPin(pin);
        }
        if (field62 != null) {
            logData.setField62(field62);
        }
        if (field63 != null) {
            logData.setField63(field63);
        }
        if (field61 != null) {
            logData.setField61(field61);
        }
        if (iccdata != null) {
            logData.setIccdata(iccdata);
            logData.setField55(ISOUtil.byte2hex(iccdata));
        }
        if (isField55){
            logData.setIsField55(isField55);
        }
        if (inputMode == ENTRY_MODE_NFC) {
            logData.setNFC(true);
        }
        if (inputMode == ENTRY_MODE_ICC) {
            logData.setICC(true);
        }

        if (isFallBack)
            logData.setFallback(isFallBack);

        if (codott != null) {
            logData.setOtt(codott);
        }

        if (agente.getAgentName() != null) {
            logData.setNameTrade(agente.getAgentName());
        }

        if (agente.getAddress() != null) {
            logData.setAddressTrade(agente.getAddress());
        }

        if (agente.getPhone() != null) {
            logData.setPhoneTrade(agente.getPhone());
        }

        logData.setAlreadyPrinted(false);

        return logData;
    }

    /**
     * 设置发卡行脚本数据
     *
     * @return
     */
    private TransLogData setScriptData() {
        TransLogData logData = new TransLogData();
        logData.setPan(PAYUtils.getSecurityNum(pan, 6, 3));
        logData.setIccdata(iccdata);
        logData.setBatchNo(batchNo);
        logData.setAmount(Long.parseLong(iso8583.getfield(4)));
        logData.setTraceNo(iso8583.getfield(11));
        logData.setLocalTime(iso8583.getfield(12));
        logData.setLocalDate(iso8583.getfield(13));
        logData.setEntryMode(iso8583.getfield(22));
        logData.setPanSeqNo(iso8583.getfield(23));
        logData.setAcquirerID(iso8583.getfield(32));
        logData.setRrn(iso8583.getfield(37));
        logData.setAuthCode(iso8583.getfield(38));
        logData.setCurrencyCode(iso8583.getfield(49));
        return logData;
    }

    /**
     * 设置冲正数据
     *
     * @return
     */
    private TransLogData setReveralData() {

        TransLogData logData = new TransLogData();

        if (transEName != null) {
            logData.setEName(transEName);
        }

        if (inputMode == ENTRY_MODE_HAND && pan != null) {
            logData.setPan(pan);
        }

        if (procCode != null) {
            logData.setProcCode(procCode);
        }

        logData.setAmount(amount);

        if (traceNo != null) {
            logData.setTraceNo(traceNo);
        }

        if (localTime != null) {
            logData.setLocalTime(localTime);
        }

        if (localDate != null) {
            logData.setLocalDate(localDate);
        }

        if (entryMode != null) {
            logData.setEntryMode(entryMode);
        }

        if (panSeqNo != null) {
            logData.setPanSeqNo(panSeqNo);
        }

        if (nii != null) {
            logData.setNii(nii);
        }

        if (svrCode != null) {
            logData.setSvrCode(svrCode);
        }

        if (track2 != null) {
            logData.setTrack2(track2);
        }

        if (termID != null) {
            logData.setTermID(termID);
        }

        if (merchID != null) {
            logData.setMerchID(merchID);
        }

        if (extAmount != null) {
            logData.setField54(extAmount);
        }

        if (iccdata != null) {
            logData.setIccdata(iccdata);
            logData.setField55(ISOUtil.byte2hex(iccdata));
        }

        if (field57 != null) {
            logData.setField57(field57);
        }

        if (field58 != null) {
            logData.setField58(field58);
        }

        if (field59 != null) {
            logData.setField59(field59);
        }

        if (field60 != null) {
            logData.setField60(field60);
        }

        if (field63 != null) {
            logData.setField63(field63);
        }

        logData.setAlreadyPrinted(false);

        return logData;
    }

    /**
     * 格式化处理响应码
     *
     * @param rsp
     * @return
     */
    public static int formatRsp(String rsp) {
        String[] standRsp = {"02", "03", "04", "05", "07", "12", "13", "14", "15", "10", "17", "19",
                "30", "39", "41", "43", "51", "52", "53", "54", "55", "57", "61", "62", "65", "74",
                "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "87", "88", "89", "91", "96", "59",
        };
        int start = 3000;
        boolean finded = false;
        for (int i = 0; i < standRsp.length; i++) {
            if (standRsp[i].equals(rsp)) {
                start += i;
                finded = true;
                break;
            }
        }
        if (finded) {
            return start;
        } else {
            return 4000;
        }
    }

    /**
     * deal with GAC2
     *
     * @return
     */
    private int genAC2Trans() {
        PBOCOnlineResult result = new PBOCOnlineResult();
        result.setField39(rspCode.getBytes());
        result.setFiled38(authCode.getBytes());
        result.setField55(iccdata);
        result.setResultCode(PBOCOnlineResult.ONLINECODE.SUCCESS);
        int retVal = pbocManager.afterOnlineProc(result);
        Logger.debug("genAC2Trans->afterOnlineProc:" + retVal);

        //Issue script deal result
        int isResult = pbocManager.getISResult();
        if (isResult != EMVISRCode.NO_ISR) {
            // save issue script result
            byte[] temp = new byte[256];
            int len = PAYUtils.packTags(PAYUtils.getTagsEMV(), temp);
            if (len > 0) {
                iccdata = new byte[len];
                System.arraycopy(temp, 0, iccdata, 0, len);
            } else {
                iccdata = null;
            }
            TransLogData scriptResult = setScriptData();
            TransLog.saveScriptResult(scriptResult);
        }

        if (retVal != PBOCode.PBOC_TRANS_SUCCESS) {
            //IC card transaction failed, if return "00" in field 39,
            //update the field 39 as "06" in reversal data
            TransLogData revesalData = TransLog.getReversal();
            if (revesalData != null) {
                revesalData.setRspCode("06");
                TransLog.saveReversal(revesalData);
            }
        }

        return retVal;
    }
}
