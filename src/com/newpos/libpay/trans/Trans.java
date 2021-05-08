package com.newpos.libpay.trans;

import android.content.Context;
import android.media.ToneGenerator;
import com.android.newpos.libemv.PBOCManager;
import com.android.newpos.pay.R;
import com.bcp.document.ClassArguments;
import com.bcp.document.InputInfo;
import com.bcp.document.MsgScreenDocument;
import com.bcp.inicializacion.configuracioncomercio.CheckCredentials;
import com.bcp.inicializacion.configuracioncomercio.CheckURL;
import com.bcp.inicializacion.configuracioncomercio.Credentials;
import com.bcp.inicializacion.configuracioncomercio.URL;
import com.bcp.menus.seleccion_cuenta.SelectedAccountItem;
import com.bcp.printer.MsgScreenPrinter;
import com.bcp.rest.JSONInfo;
import com.bcp.rest.JsonUtil;
import com.bcp.rest.generic.response.RspExecuteTransaction;
import com.bcp.rest.generic.response.RspListProducts;
import com.bcp.rest.giros.cobro_nacional.response.RspExecuteTransactionGiros;
import com.bcp.rest.giros.cobro_nacional.response.RspListProductsGiros;
import com.bcp.rest.giros.emision.response.RspExecuteTransactionEmision;
import com.bcp.rest.jsonerror.JsonError;
import com.bcp.rest.jwt.JWEString;
import com.bcp.rest.pagoservicios.response.RspExecuteTransactionPS;
import com.bcp.rest.token.response.RspObtainTkn;
import com.bcp.tools.UtilNetwork;
import com.bcp.transactions.callbacks.WaitTransFragment;
import com.bcp.transactions.common.GetAmount;
import com.newpos.libpay.Logger;
import com.newpos.libpay.device.contactless.EmvL2Process;
import com.newpos.libpay.device.printer.PrintManager;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.global.TMConstants;
import com.newpos.libpay.helper.iso8583.ISO8583;
import com.newpos.libpay.helper.ssl.NetworkHelper;
import com.newpos.libpay.presenter.TransUI;
import com.newpos.libpay.process.EmvTransaction;
import com.newpos.libpay.process.QpbocTransaction;
import com.newpos.libpay.trans.translog.TransLog;
import com.newpos.libpay.trans.translog.TransLogDataWs;
import com.newpos.libpay.trans.translog.TransLogWs;
import com.newpos.libpay.utils.ISOUtil;
import com.newpos.libpay.utils.PAYUtils;
import com.pos.device.printer.Printer;
import org.json.JSONException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import cn.desert.newpos.payui.UIUtils;

import static com.android.newpos.pay.ProcessingCertificate.polarisUtil;
import static com.android.newpos.pay.StartAppBCP.agente;
import static com.android.newpos.pay.StartAppBCP.transaction;
import static com.android.newpos.pay.StartAppBCP.variables;
import static com.bcp.definesbcp.Variables.TOTAL_BATCH;
import static com.bcp.rest.JsonUtil.ROOT;


/**
 * 交易抽象类，定义所有交易类的父类
 *
 * @author zhouqiang
 */
public abstract class Trans {

    public static final String ID_LOTE = "01";

    public static final String SETTLE = "CIERRE TOTAL";
    public static final String SETTLE_AUTO = "CIERRE AUTO";
    public static final String ECHO_TEST = "ECHO_TEST";
    public static final String RETIRO = "RETIRO";
    public static final String DEPOSITO = "DEPÓSITO";
    public static final String CONSULTAS="CONSULTAS";
    public static final String INIT_T = "INIT_TOTAL";
    public static final String IT = "IT";
    public static final String INIT_P = "INIT_PARCIAL";
    public static final String IP = "IP";
    public static final String LOGIN = "LOGIN";
    public static final String GIROS = "GIROS";
    public static final String GIROS_COBROS = "GIROS_COBROS";
    public static final String GIROS_EMISION = "GIROS_EMISION";
    public static final String PAGOSERVICIOS = "PAGO_SERVICIOS";
    public static final String PAGO_PASEMOVISTAR = "PASE_MOVISTAR";
    public static final String PAGO_CONRANGO = "PAGOCON_RANGO";
    public static final String PAGO_SINRANGO = "PAGO_SINRANGO";
    public static final String PAGO_SINVALIDACION = "SIN_VALIDACION";
    public static final String PAGO_IMPORTE = "PAGO_IMPORTE";
    public static final String PAGO_PARCIAL = "PAGO_PARCIAL";
    public static final String ULT_OPERACIONES = "ULT_OPERACIONES";
    public static final String TOKEN = "TOKEN";
    public static final String CERTIFICADO = "CERTIFICADO";

    /**
     * 上下文对象
     */
    protected Context context;

    /**
     * 8583组包解包
     */
    protected ISO8583 iso8583;

    /**
     * 网络操作对象
     */
    protected NetworkHelper netWork;

    /**
     * 交易记录的集合
     */
    protected TransLog transLog;

    protected TransLogWs transLogWs;

    /**
     * 配置文件操作实例
     */
    protected TMConfig cfg;

    /**
     * MODEL与VIEW层接口实例
     */
    protected TransUI transUI;

    /**
     * 等待页面超时时间
     */
    protected int timeout;

    /**
     * 返回值全局定义
     */
    protected int retVal = -1;

    protected int retValPrinter = -1;

    /**
     * EMV流程控制实例
     */
    protected EmvTransaction emv;

    /**
     * QPBOC流程控制实例
     */
    protected QpbocTransaction qpboc;

    /**
     * 交易相关参数集合
     */
    protected TransInputPara para;

    protected Trans() {
    }

    /** 报文域定义 */

    /**
     * 0 消息类型
     */
    protected String msgID;

    /**
     * 2* 卡号
     */
    protected String pan;

    /**
     * 3  预处理码
     */
    protected String procCode;

    /**
     * 4* 金额
     */
    protected long amount;

    protected String cvv;

    protected String codott;

    protected boolean isField55;

    /**
     * 11域交易流水号
     */
    protected String traceNo;
    protected String opnNumber;

    /**
     * 12 hhmmss*
     */
    protected String localTime;

    /**
     * 13 MMDD*
     */
    protected String localDate;

    /**
     * 14 YYMM*
     */
    protected String expDate;

    /**
     * 15 MMDD*
     */
    protected String settleDate;

    /**
     * 22*
     */
    protected String entryMode;

    /**
     * 23*
     */
    protected String panSeqNo;
    /**
     * 24*
     */
    protected String nii;

    /**
     * 25
     */
    protected String svrCode;

    /**
     * 26
     */
    protected String captureCode;

    /**
     * 32*
     */
    protected String acquirerID;

    /**
     * 1磁道数据
     */
    protected String track1;

    /**
     * 35
     */
    protected String track2;

    /**
     * 36
     */
    protected String track3;

    /**
     * 37*
     */
    protected String rrn;

    /**
     * 38*
     */
    protected String authCode;

    /**
     * 39
     */
    protected String rspCode;

    /**
     * 41
     */
    protected String termID;

    /**
     * 42
     */
    protected String merchID;

    /**
     * 44 *
     */
    protected String field44;

    /**
     * 48 *
     */
    protected String field48;

    /**
     * 49*
     */
    protected String currencyCode;

    /**
     * 52
     */
    protected String pin;

    /**
     * 53
     */
    protected String securityInfo;

    /**
     * 54
     */
    protected String extAmount;

    protected String field54;

    /**
     * 55*
     */
    protected byte[] iccdata;

    /**
     * 57
     */
    protected String field57;

    /**
     * 58
     */
    protected String field58;

    /**
     * 59
     */
    protected String field59;

    /**
     * 60
     */
    protected String field60;

    /**
     * 61
     */
    protected String field61;

    /**
     * 62
     */
    protected String field62;

    /**
     * 63
     */
    protected String field63;

    /**
     * 64
     */
    protected String field64;

    /**
     * 交易中文名
     */
    protected String transCName;
    /**
     * 交易英文名 主键 交易初始化设置
     */
    protected String transEName;

    protected String typeCoin;

    protected String hostId;

    protected String currencyName;

    protected int numCuotas;

    protected GetAmount amountGet;

    /**
     * 批次号 60_2
     */
    protected String batchNo;

    /**
     * 标记此次交易流水号是否自增
     */
    protected boolean isTraceNoInc = false;

    /**
     * 是否允许IC卡降级为磁卡
     */
    protected boolean isFallBack;

    /**
     * 使用原交易的第3域和 60.1域
     */
    protected boolean isUseOrgVal = false;


    /**
     * 22域服务点输入方式
     */
    public static final int ENTRY_MODE_HAND = 1;
    public static final int ENTRY_MODE_MAG = 2;
    public static final int ENTRY_MODE_ICC = 5;
    public static final int ENTRY_MODE_NFC = 7;
    public static final int ENTRY_MODE_QRC = 9;
    public static final int ENTRY_MODE_FALLBACK = 3;

    public static final String MODE_MAG = "90";
    public static final String MODE_ICC = "05";
    public static final String MODE1_FALLBACK = "80";
    public static final String MODE2_FALLBACK = "90";
    public static final String MODE_CTL = "07";
    public static final String MODE_HANDLE = "01";


    protected EmvL2Process emvl2;   //非接交易

    protected List<URL> listUrl;

    private List<Credentials> listCredentials;

    /**
     * PBOC library manager
     */
    protected PBOCManager pbocManager;

    /**
     * 返回值全局定义
     */
    protected String printClient = "0";

    protected boolean depWithCard = false;

    protected RspObtainTkn rspObtainTkn;

    protected JWEString jweDataEncryptDecrypt;

    protected RspExecuteTransaction rspExecTrans;

    protected RspExecuteTransactionGiros rspExecTransGiros;

    protected RspExecuteTransactionEmision rspExecTransEmision;

    protected String typebill;

    protected ClassArguments arguments;

    protected SelectedAccountItem selectedAccountItem;

    protected String sessionId;

    protected WaitTransFragment waitTransFragment;

    protected RspListProductsGiros rspListProductsGiros;

    protected RspExecuteTransactionPS rspExecuteTransactionPS;

    protected RspListProducts rspListProductsPS;

    protected String errOP = "";

    protected String ipTrans;
    protected String macTrans;

    /***
     * Trans 构造
     * @param ctx
     * @param transEname
     */
    public Trans(Context ctx, String transEname, boolean isISo, TransInputPara p) {
        try {
            this.context = ctx;
            this.transEName = transEname;
            this.timeout = 60 * 1000;
            if (isISo)
                this.transLog = TransLog.getInstance(variables.getIdAcquirer());
            else
                this.transLogWs = TransLogWs.getInstance();
            this.pbocManager = PBOCManager.getInstance();
            this.pbocManager.setDEBUG(true);
            this.amountGet = new GetAmount(transUI, timeout, transEName, context);
            para = p;
            loadConfig(isISo);
            executeSentenceSqlTrans(para.getTransType());
            if (!isISo)
                getCredentials();
        }catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " Trans 1 " +  e.getMessage());
        }
    }

    public Trans(Context ctx, String transEname, String fileNameLog) {
        try {
            this.context = ctx;
            this.transEName = transEname;
            this.timeout = 60 * 1000;
            loadConfig(true);
            transLog = TransLog.getInstance(variables.getIdAcquirer() + fileNameLog);
        }catch (NumberFormatException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " Trans 2 " +  e.getMessage());
        }
    }

    //OJO SOLO PRUEBAS LOCALES
    boolean dev = true; // IGUALAR A FALSE PARA PRODUCCION

    /**
     * Para inicializacion no requiere ejecutar estos querys
     */
    private void executeSentenceSqlTrans(String nameTrans){
        polarisUtil.setTransCurrent(transaction.selectTransaction(context,nameTrans));
        listUrl = new ArrayList<>();
        listUrl = CheckURL.getSingletonInstance().selectURL(context, nameTrans);
        //SOLO PRUEBAS LOCALES
        if (dev)
            listUrl = CheckURL.getSingletonInstance().selectUrlTest(nameTrans);
    }

    /**
     * query para obtener las credenciales usadas en el header del token
     */
    private void getCredentials(){
        listCredentials = new ArrayList<>();
        listCredentials = CheckCredentials.getSingletonInstance().selectCredentials(context);
    }

    /**
     * 加载初始设置
     */
    private void loadConfig(boolean isISO) {
        cfg = TMConfig.getInstance();

        termID = ISOUtil.padright(cfg.getTermID()+"", 8, '0');

        ipTrans = UtilNetwork.getIPAddress(true);
        macTrans = UtilNetwork.getMacAddressFromIp(context);

        if (isISO) {
            if (agente.getMerchantId().length() < 16)
                merchID = ISOUtil.padright("" + agente.getMerchantId(), 15, ' ');
            else
                merchID = agente.getMerchantId();
        }else{
            merchID = agente.getMerchantId().substring(1);
        }

        currencyCode = cfg.getCurrencyCode();
        batchNo = ISOUtil.padleft("" + cfg.getBatchNo(), 6, '0');
        traceNo = ISOUtil.padleft("" + cfg.getTraceNo(), 6, '0');
        opnNumber = ISOUtil.padleft("" + cfg.getOpnNumber(), 8, '0');

        if (isISO) {
            cfg.setPubCommun(true);  //Se pone en true para que siempre se intente primero por la IP 1
            loadConfigIP();
            String tpdu = getTpdu();
            String header = cfg.getHeader();
            setFixedDatas();
            iso8583 = new ISO8583(this.context, tpdu, header);
        }
    }

    protected void loadConfigIP(){
        String ip = null;
        int port = 0;
        int timeoutRsp = 0;
        int timeoutCon = 0;

        //JM
        try {
            ip = cfg.getIp();
            port = Integer.parseInt(cfg.getPort());
            timeoutRsp = cfg.getTimeout();
            timeoutCon = cfg.getTimeout();

        } catch (NumberFormatException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " Trans 3 " +  e.getMessage());
            timeoutRsp = cfg.getTimeout();
            timeoutCon = cfg.getTimeout();
        }
        netWork = new NetworkHelper(ip, port, timeoutRsp,timeoutCon, "1", context);
    }

    /**
     * 设置消息类型及60域3个子域数据
     */
    protected void setFixedDatas() {
        Logger.debug("==Trans->setFixedDatas==");
        if (null == transEName) {
            return;
        }
        Properties pro = PAYUtils.lodeConfig(context, TMConstants.TRANS);
        if (pro == null) {
            return;
        }
        String prop = pro.getProperty(transEName);
        String[] propGroup = prop.split(",");
        if (!PAYUtils.isNullWithTrim(propGroup[0])) {
            msgID = propGroup[0];
        } else {
            msgID = null;
        }
        if (!isUseOrgVal) {
            if (!PAYUtils.isNullWithTrim(propGroup[1])) {
                procCode = propGroup[1];
            } else {
                procCode = null;
            }
        }
        if (!PAYUtils.isNullWithTrim(propGroup[2])) {
            svrCode = propGroup[2];
        } else {
            svrCode = null;
        }

        transCName = new String(propGroup[5].getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    /**
     * 获取流水号是否自增
     *
     * @return
     */
    public boolean isTraceNoInc() {
        return isTraceNoInc;
    }

    /**
     * 设置流水号是否自增
     *
     * @param isTraceNoInc
     */
    public void setTraceNoInc(boolean isTraceNoInc) {
        this.isTraceNoInc = isTraceNoInc;
    }

    /**
     * 追加60域内容
     *
     * @param f60
     */
    protected void appendField60(String f60) {
        field60 = field60 + f60;
    }

    /**
     * 连接
     *
     * @return
     */
    protected int connect() {
        return netWork.connect();
    }

    /**
     * 发送
     *
     * @return
     */
    protected int send() {
        byte[] pack = iso8583.packetISO8583();
        if (pack == null) {
            return -1;
        }
        Logger.debug("交易:" + transEName + "\n发送报文:" + ISOUtil.hexString(pack));
        return netWork.send(pack);
    }

    /**
     * 接收
     *
     * @return
     */
    protected byte[] recive() {
        byte[] recive = null;
        try {
            recive = netWork.recive(2048);
        } catch (IOException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " Trans 4 " +  e.getMessage());
            return new byte[0];
        }
        if (recive != null) {
            Logger.debug("交易:" + transEName + "\n接收报文:" + ISOUtil.hexString(recive));
        }
        return recive;
    }

    /**
     * 联机处理
     *
     * @return
     */
    protected int onLineTrans() {

        int reintentos = 3;
        int rta;

        do {
            rta = connect();
            if (rta == 0) {
                reintentos = 0;
            }
            reintentos --;
        }while (reintentos >0);

        if (rta == -1){
            reintentos = 3;
            cfg = TMConfig.getInstance();
            cfg.setPubCommun(false);
            loadConfigIP();
            do {
                rta = connect();
                if (rta == 0) {
                    reintentos = 0;
                }
                reintentos --;
            }while (reintentos >0);
        }

        if (rta == -1) {
            return TcodeError.T_SOCKET_ERR;
        }

        if (send() == -1) {
            return TcodeError.T_SEND_ERR;
        }

        byte[] respData = recive();
        netWork.close();

        if (respData == null || respData.length <= 0) {
            return TcodeError.T_RECEIVE_ERR;
        }

        int ret = iso8583.unPacketISO8583(respData);

        if (ret == 0 && (isTraceNoInc)) {
                cfg.incTraceNo().save();
        }

        return ret;
    }

    /**
     * 清除关键信息
     */
    protected void clearPan() {
        pan = null;
        track2 = null;
        track3 = null;
        emvl2 = null;
        listUrl = null;
        listCredentials = null;
        pbocManager = null;
        printClient = null;
        rspObtainTkn = null;
        jweDataEncryptDecrypt = null;
        rspExecTrans = null;
        rspExecTransGiros = null;
        rspExecTransEmision = null;
        typebill = null;
        arguments = null;
        selectedAccountItem = null;
        sessionId = null;
        waitTransFragment = null;
        rspListProductsGiros = null;
        rspExecuteTransactionPS = null;
        rspListProductsPS = null;
        errOP = null;
        ipTrans = null;
        macTrans = null;
        System.gc();
    }

    protected String packageMaskedCard(String pan){
        String panTemp="";
        if (pan==null)
            return panTemp;
        panTemp = PAYUtils.getSecurityNum(pan, 6, 3);
        return panTemp;
    }

    public static String getTpdu() {
        StringBuilder tmpTPDU = new StringBuilder();
        String nii;
        String tpdu = "";

        nii = ISOUtil.padleft(TMConfig.getInstance().getNii() + "", 4, '0');

        //JM
        tmpTPDU.append("60");
        tmpTPDU.append(nii);
        tmpTPDU.append("0000");

        tpdu = tmpTPDU.toString();
        return tpdu;
    }

    protected void msgAprob(int codeTittle, int codeMsg){
        try {
            transUI.handlingBCP(codeTittle ,codeMsg,true);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " Trans 5 " +  e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    protected void msgAprob(int codeTittle,String msg){
        UIUtils.beep(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD);
        transUI.trannSuccess(codeTittle,validateTrans(),msg);
    }

    protected void msgError(){
        switch (retVal){
            case TcodeError.T_INTERNAL_SERVER_ERR:
            case TcodeError.T_ERR_BAD_REQUEST:
            case TcodeError.T_ERR_NO_FOUND:
                break;
            default:
                transUI.showError(transEName, retVal);
                break;
        }
        Logger.logLine(Logger.LOG_GENERAL, "msgError : transEName " + transEName);
        Logger.logLine(Logger.LOG_GENERAL, "msgError : retVal " + retVal);
    }

    protected String validateTrans(){
        String ret;
        switch (transEName){
            case GIROS_COBROS:
            case GIROS_EMISION:
            case Trans.DEPOSITO:
                ret = depWithCard ? "0" : "1";
                break;
            case INIT_P:
            case INIT_T:
            case CERTIFICADO:
            case SETTLE:
                ret = "2";
                break;
            default:
                ret = "0";
                break;
        }

        return ret;
    }

    protected boolean checkBatchAndSettle(boolean checkBatch){

        if (!ISOUtil.stringToBoolean(polarisUtil.getTransCurrent().getTransEnable()) ){
            transUI.showError(transEName, TcodeError.T_NOT_ALLOW);
            Logger.logLine(Logger.LOG_GENERAL, " checkBatchAndSettle : false1");
            return false;
        }

        if (checkBatch && transLogWs.getSize() >= TOTAL_BATCH) {
            transUI.showError(transEName, TcodeError.T_ERR_BATCH_FULL);
            Logger.logLine(Logger.LOG_GENERAL, "checkBatchAndSettle : false2");
            return false;
        }

        Logger.logLine(Logger.LOG_GENERAL, " checkBatchAndSettle : true");
        return true;
    }

    protected int printData(TransLogDataWs logData, MsgScreenPrinter msgScreenPrinter){

        Logger.logLine(Logger.LOG_GENERAL, "Impresion voucher cliente");

        try {
            PrintManager printManager = PrintManager.getmInstance(context, transUI);
            if (msgScreenPrinter.getTransEname().equals(CONSULTAS)) {
                retVal = 0;
                printManager.setTyQuery(msgScreenPrinter.getTittle());
                printClient(printManager, logData);
            }else {

                if(ISOUtil.stringToBoolean(logData.getToPrint())){
                    printClient(printManager, logData);
                }else {
                    InputInfo inputInfo = transUI.showMessageImpresion(msgScreenPrinter);
                    printManager.setTyQuery(msgScreenPrinter.getTittle());

                    if (inputInfo.isResultFlag()) {
                        printClient(printManager, logData);
                    } else {
                        return inputInfo.getErrno();
                    }
                }
            }
        }catch (Exception e){
            retVal = -1;
            Logger.logLine(Logger.LOG_EXECPTION, "Error en impresion voucher cliente");
            Logger.logLine(Logger.LOG_EXECPTION, e.getMessage());
            Logger.logLine(Logger.LOG_EXECPTION, Arrays.toString(e.getStackTrace()));
            Logger.logLine(Logger.LOG_EXECPTION, e.getLocalizedMessage());
            Logger.logLine(Logger.LOG_EXECPTION, "Final error en impresion voucher cliente");
        }
        if (retVal != Printer.PRINTER_OK)
            retVal = TcodeError.T_PRINTER_EXCEPTION;

        return retVal;
    }

    private int printClient(PrintManager printManager, TransLogDataWs logData) {
        printClient = "1";
        retValPrinter = printManager.print(logData, false, false);
        return 0;
    }

    protected int printDataComer(TransLogDataWs logData,boolean isComprobante){
        MsgScreenPrinter msgScreenPrinter = new MsgScreenPrinter();

        Logger.logLine(Logger.LOG_GENERAL, "Impresion voucher comercio");

        try {
            msgScreenPrinter.setTransEname(transEName);
            msgScreenPrinter.setTittle(context.getResources().getString(R.string.comprobanteimpre));
            msgScreenPrinter.setMsgAmount("");
            msgScreenPrinter.setSymbol("");
            if(msgScreenPrinter.getTransEname().equals(Trans.DEPOSITO) && isComprobante || msgScreenPrinter.getTransEname().equals(Trans.PAGOSERVICIOS)){
                msgScreenPrinter.setMsgAccount(context.getResources().getString(R.string.entregaComproCliente));
            }else if(msgScreenPrinter.getTransEname().equals(Trans.CONSULTAS)){
                msgScreenPrinter.setTittle(context.getResources().getString(R.string.impExitosa));
                msgScreenPrinter.setMsgAccount(context.getResources().getString(R.string.entregaComproCliente));
            }else if(msgScreenPrinter.getTransEname().equals(Trans.GIROS)) {
                msgScreenPrinter.setTittle(context.getResources().getString(arguments.getTypetransaction() == 1 ? R.string.titleImpGiro : R.string.titleCobroGiro));
                msgScreenPrinter.setMsgAmount(String.valueOf(logData.getAmount()).replaceAll("[,.]",""));
                msgScreenPrinter.setSymbol(logData.getCurrencySymbol());
                msgScreenPrinter.setMsgAccount(depWithCard ? "con tarjeta" : "en efectivo");
            }else if(msgScreenPrinter.getTransEname().equals(Trans.RETIRO) && isComprobante){
                msgScreenPrinter.setMsgAccount(context.getResources().getString(R.string.dineroCliente));
            }else{
                if(msgScreenPrinter.getTransEname().equals(Trans.RETIRO)) {
                    msgScreenPrinter.setTittle(context.getResources().getString(R.string.sinComprobante));
                    msgScreenPrinter.setMsgAccount(context.getResources().getString(R.string.entrDinerCliente));
                }else if(msgScreenPrinter.getTransEname().equals(Trans.DEPOSITO)){
                    msgScreenPrinter.setTittle(context.getResources().getString(R.string.sinComprobante));
                    msgScreenPrinter.setMsgAccount(context.getResources().getString(R.string.entregaCompro));
                }
            }
            msgScreenPrinter.setMsgButtonConfirm("Imprimir");
            msgScreenPrinter.setMsgBanner(context.getResources().getString(R.string.copiaEstabl));
            msgScreenPrinter.setMsgButtonCancel(context.getResources().getString(R.string.sinCopia));
            msgScreenPrinter.setTimeOut(timeout);
            msgScreenPrinter.setTypebill(typebill);

            InputInfo inputInfo = transUI.showMessageImpresion(msgScreenPrinter);
            retVal = 0;

            if (inputInfo.isResultFlag()){
                PrintManager printManager = PrintManager.getmInstance(context, transUI);
                retVal = printManager.print(logData, true, true);
            }else {
                transUI.handlingBCP("Procesando" ,"",false);
            }
        }catch (Exception e){
            retVal = -1;
            Logger.logLine(Logger.LOG_EXECPTION, "Error en impresion voucher comercio");
            Logger.logLine(Logger.LOG_EXECPTION, e.getMessage());
            Logger.logLine(Logger.LOG_EXECPTION, Arrays.toString(e.getStackTrace()));
            Logger.logLine(Logger.LOG_EXECPTION, e.getLocalizedMessage());
            Logger.logLine(Logger.LOG_EXECPTION, "Final error en impresion voucher comercio");
        }

        if (retVal != Printer.PRINTER_OK)
            retVal = TcodeError.T_PRINTER_EXCEPTION;

        return retVal;
    }

    protected void validarError(JSONInfo jsonInfo){
        Logger.logLine(Logger.LOG_GENERAL, "validarError " +  "=====validarError");
        Logger.logLine(Logger.LOG_GENERAL, "validarError " + "getErr " + jsonInfo.getErr());
        Logger.logLine(Logger.LOG_GENERAL, "ValidarError " + "getJsonObject " + jsonInfo.getJsonObject());
        try {
            if (jsonInfo.getHeader() != null)
                setSessionId(jsonInfo.getHeader().get(JsonUtil.getSESSION()));
        }catch (Exception ex){
            Logger.logLine(Logger.LOG_EXECPTION, ex.getStackTrace(), ex.getMessage());
        }

        switch (jsonInfo.getStatusCode()){
            case TcodeError.T_INTERNAL_SERVER_ERR:
            case TcodeError.T_ERR_BAD_REQUEST:
            case TcodeError.T_ERR_NO_FOUND:
                JsonError jsonError =  new JsonError();
                try {
                    if(!jsonError.getRspObtJson(jsonInfo.getJsonObject())) {
                        if (transEName.equals(ULT_OPERACIONES)){
                            if(jsonError.getCode().equals("OP0003")){
                                errOP = jsonError.getCode();
                               return;
                            }
                        }
                        transUI.showError(transEName,jsonInfo.getStatusCode());
                        return;
                    }
                } catch (JSONException e) {
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " Trans 6 " +  e.getMessage());
                }
                transUI.showError(transEName,jsonError.getCode(),jsonError.getDescription());
                break;
            default:
                break;
        }
    }

    protected boolean reqObtainToken(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqObtainToken ");

        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER ,TcodeSucces.MSGINFORMATION,false);

        JSONInfo jsonInfo = transUI.processApiRestStringPost(timeout, setParamToken(),ROOT + CheckURL.getSingletonInstance().getUrlToken());
        if ((retVal=jsonInfo.getErr())!=0){
            validarError(jsonInfo);
            return false;
        }

        transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV ,TcodeSucces.MSGINFORMATION,false);

        Logger.debug("==Parsing Json==\n");
        rspObtainTkn = new RspObtainTkn();
        try {
            retVal = 0;
            if(!rspObtainTkn.getRspObtainTkn(jsonInfo.getJsonObject())){
                retVal = TcodeError.T_ERR_UNPACK_JSON;
                return false;
            }

            Logger.logLine(Logger.LOG_GENERAL, " Saliendo de reqObtainToken " + retVal);

            Logger.debug(rspObtainTkn.getAccesToken());
            Logger.debug(rspObtainTkn.getTokenType());
            Logger.debug(String.valueOf(rspObtainTkn.getExpiresIn()));
            Logger.debug(rspObtainTkn.getScopeC());
            Logger.debug("====================\n");
        } catch (JSONException e) {
            retVal = TcodeError.T_ERR_OBTENER_TOKEN;
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " Trans 7 " +  e.getMessage());
            return false;
        }
        return true;
    }

    private HashMap<String, String> setParamToken(){
        HashMap<String, String> headers = new HashMap<>();
        for (int i = 0; i < listCredentials.size(); i++){
            headers.put(listCredentials.get(i).getClave(),listCredentials.get(i).getValor());
        }
        return headers;
    }

    protected void controllerCatch(Exception e){
        if (e.getMessage().contains("java.lang.Object java.util.ArrayList.get(int)"))
            retVal = TcodeError.T_ERR_GEN_TRAN;
        Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "CONTROLLERCATCH"  + e.getMessage());
    }

    protected void endTrans(int codmsg, String msg){
        if (sessionId != null)
            setSessionId(sessionId);

        if (retVal == 0) {
            msgAprob(codmsg,msg);
        }else {
            msgError();
        }
        clearPan();
    }

    protected boolean jweEncryptDecrypt(String data, boolean keyInit, boolean decrypt){
        if (jweDataEncryptDecrypt==null)
            jweDataEncryptDecrypt = new JWEString(keyInit);

        try {
            if (decrypt)
                jweDataEncryptDecrypt.decryptJWE(data);
            else
                jweDataEncryptDecrypt.encryptedJWE(data);
        } catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            return false;
        }

        return true;
    }

    protected Map<String, String> setHeader(boolean isSession){
        Map<String, String> headers = new HashMap<>();
        headers.put(JsonUtil.getDevRef(), merchID);
        headers.put(JsonUtil.getNUMBER(), opnNumber);
        headers.put(JsonUtil.getDEVIP(), ipTrans);
        headers.put(JsonUtil.getDEVMAC(), macTrans);
        if (isSession)
            headers.put(JsonUtil.getSESSION(), sessionId);
        headers.put(JsonUtil.getAPIKEY(), rspObtainTkn.getTokenType() + " " +rspObtainTkn.getAccesToken());
        return headers;
    }

    protected MsgScreenDocument fillScreenDocument(boolean isBanner, int minLen, int maxLen, String tittle, String typeDoc, boolean isAlfa){
        MsgScreenDocument msgScreenDocument= new MsgScreenDocument();
        msgScreenDocument.setBanner(isBanner);
        msgScreenDocument.setMaxLength(maxLen);
        msgScreenDocument.setMinLength(minLen);
        msgScreenDocument.setTimeOut(timeout);
        msgScreenDocument.setTittle(tittle);
        msgScreenDocument.setTittleDoc(typeDoc);
        msgScreenDocument.setTransEname(transEName);
        msgScreenDocument.setAlfa(isAlfa);
        msgScreenDocument.setSelectedAccountItem(selectedAccountItem);
        msgScreenDocument.setHeader(setHeader(false));
        msgScreenDocument.setListUrl(listUrl);
        msgScreenDocument.setCallbackTransFragment(initTransFragment());
        msgScreenDocument.setMaxAmount(polarisUtil.getTransCurrent().getAmountMaximun());
        msgScreenDocument.setMinAmount(polarisUtil.getTransCurrent().getAmountMinimun());
        msgScreenDocument.setMaxAmountUsd(polarisUtil.getTransCurrent().getAmountMaxUsd());
        msgScreenDocument.setMinAmountUsd(polarisUtil.getTransCurrent().getAmountMinUsd());
        if (arguments != null && arguments.getPaymentOrders() != null)
            msgScreenDocument.setPaymentOrders(arguments.getPaymentOrders()[Integer.parseInt(arguments.getArgument3())]);
        if(rspListProductsPS != null)
            msgScreenDocument.setProductsAccounts(rspListProductsPS.getAccounts());
        if(rspListProductsGiros != null)
            msgScreenDocument.setProductsAccounts(rspListProductsGiros.getListProducts());
        if(arguments != null)
            msgScreenDocument.setArguments(arguments);

        return msgScreenDocument;
    }

    protected WaitTransFragment initTransFragment(){
        waitTransFragment = new WaitTransFragment() {
            @Override
            public void getSessionId(String idSession) {
                setSessionId(idSession);
            }

            @Override
            public Map<String, String> headerTrans(boolean isSession) {
                return setHeader(isSession);
            }

            @Override
            public void setOpnNumber() {
                opnNumberPlus();
            }

            @Override
            public List<URL> getListUrl(String nameTrans) {
                executeSentenceSqlTrans(nameTrans);
                return listUrl;
            }

            @Override
            public String[] getAmount() {
                String[] amountTrans = new String[4];
                amountTrans[0] = polarisUtil.getTransCurrent().getAmountMaximun();
                amountTrans[1] = polarisUtil.getTransCurrent().getAmountMinimun();
                amountTrans[2] = polarisUtil.getTransCurrent().getAmountMaxUsd();
                amountTrans[3] = polarisUtil.getTransCurrent().getAmountMinUsd();
                return amountTrans;
            }
        };
        return waitTransFragment;
    }

    protected void setSessionId(String sessionId) {
        this.sessionId = sessionId;
        transUI.setHeader(setHeader(true), ROOT + listUrl.get(listUrl.size() - 1).getMethod());
    }

    protected void opnNumberPlus(){
        if (isTraceNoInc) {
            cfg.incOpnNumber();
            opnNumber = ISOUtil.padleft("" + cfg.getOpnNumber(), 8, '0');
        }
    }
}
