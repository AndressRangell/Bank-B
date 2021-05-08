package com.newpos.libpay.process;

import android.os.SystemClock;

import com.bcp.document.InputInfo;
import com.newpos.libpay.Logger;
import com.newpos.libpay.device.pinpad.PinInfo;
import com.newpos.libpay.device.pinpad.PinpadManager;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.presenter.TransUI;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.Trans;
import com.newpos.libpay.trans.TransInputPara;
import com.newpos.libpay.utils.ISOUtil;
import com.newpos.libpay.utils.PAYUtils;
import com.pos.device.SDKException;
import com.pos.device.config.DevConfig;
import com.pos.device.emv.CandidateListApp;
import com.pos.device.emv.CoreParam;
import com.pos.device.emv.EMVHandler;
import com.pos.device.emv.IEMVCallback;
import com.pos.device.emv.IEMVHandler;
import com.pos.device.emv.IEMVHandler.EMVTransType;
import com.pos.device.emv.TerminalMckConfigure;
import com.pos.device.icc.ContactCard;
import com.pos.device.icc.IccReader;
import com.pos.device.icc.OperatorMode;
import com.pos.device.icc.SlotType;
import com.pos.device.icc.VCC;
import com.pos.device.ped.RsaPinKey;
import com.pos.device.picc.EmvContactlessCard;
import com.pos.device.picc.PiccReader;
import java.util.Arrays;
import cn.desert.newpos.payui.UIUtils;
import static cn.desert.newpos.payui.master.MasterControl.incardTable;
import static com.android.newpos.pay.StartAppBCP.agente;
import static com.pos.device.emv.IEMVHandler.KERNEL_TYPE_PBOC;


/**
 * EMV交易流程
 *
 * @author zhouqiang
 */
public class EmvTransaction {

    private IccReader icCard = null;
    private ContactCard contactCard = null;
    private IEMVHandler emvHandler = null;
    private String transEname;
    private EmvContactlessCard emvContactlessCard = null;
    private int timeout;

    private long amoUnt;
    private int inputMode;
    private long otherAmount;

    protected String cvv;

    private String traceNo;

    private String rspCode;// 39
    private String authCode;// 38

    private byte[] rspICCData;// 55
    private int onlineResult;// 成功和失败
    private String pinBlock = "";
    private String eCAmount = null;
    private int retExpApp;

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getTraceNo() {
        return this.traceNo;
    }

    final int [] wOnlineTags = {0x9F26, // AC (Application Cryptogram)
            0x9F27, // CID
            0x9F10, // IAD (Issuer Application Data)
            0x9F37, // Unpredicatable Number
            0x9F36, // ATC (Application Transaction Counter)
            0x95, // TVR
            0x9A, // Transaction date
            0x9C, // Transaction Type
            0x9F02, // Amount Authorised
            0x5F2A, // Transaction Currency Code
            0x82, // AIP
            0x9F1A, // Terminal Country Code
            0x9F03, // Amount Other
            0x9F33, // Terminal Capabilities
            // opt
            0x9F34, // CVM Result
            0x9F35, // Terminal Type
            0x9F1E, // IFD Serial Number
            0x84, // Dedicated File Name
            0x9F09, // Application Version #
            0x9F41, // Transaction Sequence Counter
            // 0x5F34, // PAN Sequence Number
            0};
    // 0X8E, //CVM

    final int [] wisrTags = {0x9F33, // Terminal Capabilities
            0x95, // TVR
            0x9F37, // Unpredicatable Number
            0x9F1E, // IFD Serial Number
            0x9F10, // Issuer Application Data
            0x9F26, // Application Cryptogram
            0x9F36, // Application Tranaction Counter
            0x82, // AIP
            0xDF31, // 发卡行脚本结果
            0x9F1A, // Terminal Country Code
            0x9A, // Transaction date
            0};

    final int [] reversalTag = {0x95, // TVR
            0x9F1E, // IFD Serial Number
            0x9F10, // Issuer Application Data
            0x9F36, // Application Transaction Counter
            0xDF31, // 发卡行脚本结果
            0};

    private TransUI transUI;
    private TransInputPara para;

    /**
     * 初始化内核专用构造器
     */
    public EmvTransaction() {
        emvHandler = EMVHandler.getInstance();
    }


    public String getAuthCode() {
        return authCode;
    }

    /**
     * EMV流程专用构造器
     */
    public EmvTransaction(TransInputPara p, String nameTrans) {
        this.emvHandler = EMVHandler.getInstance();
        this.para = p;
        this.transUI = para.getTransUI();
        this.retExpApp = -1;
        this.transEname = nameTrans;
        Logger.debug("amount = " + para.isNeedAmount());
        Logger.debug("online = " + para.isNeedOnline());
        Logger.debug("pass = " + para.isNeedPass());
        Logger.debug("eccash = " + para.isECTrans());
        Logger.debug("print = " + para.isNeedPrint());
        if (para.isNeedAmount()) {
            this.amoUnt = para.getAmount();
            this.otherAmount = para.getOtherAmount();
        }
        this.inputMode = para.getInputMode();
        if (inputMode == Trans.ENTRY_MODE_NFC) {
            try {
                PiccReader.getInstance();
                emvContactlessCard = EmvContactlessCard.connect();
            } catch (SDKException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "Exception" + e.toString());
            }
        }
        if (inputMode == Trans.ENTRY_MODE_ICC) {
            try {
                icCard = IccReader.getInstance(SlotType.USER_CARD);
                contactCard = icCard.connectCard(VCC.VOLT_5, OperatorMode.EMV_MODE);
            } catch (SDKException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "Exception" + e.toString());
            }
        }


    }

    /**
     * Inyectar monto y tipo de moneda al kernel emv
     */
    public void setAmountAndCurrencyCodeEMV() {
        String amountTmp1;

        //Amount
        amountTmp1 = ISOUtil.padleft(para.getAmount() + "", 12, '0');
        byte[] amountTmp2 = ISOUtil.str2bcd(amountTmp1, false);
        emvHandler.setDataElement(new byte[]{(byte) 0x9f, 0x02}, amountTmp2);

        emvHandler.setDataElement(new byte[]{(byte) 0x5f, 0x2a}, new byte[]{0x06, 0x04});
    }

    /**
     * Inyectar el tag 9F1E IFDSerialNo
     */
    private void getSerialNoEMV() {
        String sn = DevConfig.getSN();
        sn = sn.substring(2, sn.length());
        byte[] serialNo = sn.getBytes();
        emvHandler.setDataElement(new byte[]{(byte) 0x9f, 0x1e}, serialNo);
    }

    /**
     * EMV交易流程开始
     *
     * @return
     */
    public int start() {
        int ret;
        Logger.debug("EmvTransaction>>EMVTramsProcess");
        timeout = 60 * 1000;

        initEmvKernel();

        ret = emvReadData(true);
        if (ret != 0) {
            return ret;
        }

        if (para.isNeedConfirmCard()) {
            Logger.debug("EmvTransaction>>EMVTramsProcess>>提示确认卡号");
            String cn = getCardNo();
            Logger.debug("EmvTransaction>>EMVTramsProcess>>卡号=" + cn);
            ret = transUI.showCardConfirm(timeout, cn);
            if (ret != 0) {
                return TcodeError.T_USER_CANCEL_INPUT;
            }
        }

        if (!transEname.equals(Trans.INIT_T) && !incardTable(getCardNo())) {
            ret = TcodeError.T_UNSUPPORT_CARD;
            transUI.showError(transEname, TcodeError.T_UNSUPPORT_CARD);
            return ret;
        }

        if (retExpApp == TcodeError.T_ERR_EXP_DATE_APP) {
            ret =  TcodeError.T_ERR_EXP_DATE_APP;
            return ret;
        }
        setAmountAndCurrencyCodeEMV();
        getSerialNoEMV();

        if (!para.isEmvAll()) {
            return 0;
        }
        Logger.debug("EmvTransaction>>EMVTramsProcess>>processRestriction");
        emvHandler.processRestriction();
        Logger.debug("EmvTransaction>>EMVTramsProcess>>持卡人认证");
        try {
            ret = emvHandler.cardholderVerify();
        } catch (SDKException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.toString());
        }
        Logger.debug("EmvTransaction>>EMVTramsProcess>>cardholderVerify=" + ret);
        if (ret != 0) {
            Logger.debug("EmvTransaction>>EMVTramsProcess>>cardholderVerify fail");
            return TcodeError.T_USER_CANCEL_INPUT;
        }

        Logger.debug("EmvTransaction>>EMVTramsProcess>>终端风险分析");
        try {
            ret = emvHandler.terminalRiskManage();
        } catch (SDKException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
        Logger.debug("EmvTransaction>>EMVTramsProcess>>terminalRiskManage=" + ret);
        if (ret != 0) {
            Logger.debug("EmvTransaction>>EMVTramsProcess>>terminalRiskManage fail");
            return TcodeError.T_TERMINAL_ACTION_ANA_ERR;
        }

        Logger.debug("EmvTransaction>>EMVTramsProcess>>是否联机");
        boolean isNeedOnline = false;
        try {
            isNeedOnline = emvHandler.terminalActionAnalysis();
        } catch (SDKException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            if (e.toString().indexOf("2069")>=0){
                return TcodeError.T_DECLINE_OFFLINE;
            }
        }
        Logger.debug("EmvTransaction>>EMVTramsProcess>>terminalActionAnalysis=" + isNeedOnline);
        if (isNeedOnline) {
            Logger.debug("EmvTransaction>>EMVTramsProcess>>联机交易");
            Logger.debug("EMV完整流程结束");
            return 1;
        }
        Logger.debug("EmvTransaction>>EMVTramsProcess>>脱机批准");
        Logger.debug("EMV完整流程结束");
        return 0;
    }

    /**
     * EMV读数据
     *
     * @param ifOfflineDataAuth
     * @return
     */
    private int emvReadData(boolean ifOfflineDataAuth) {
        Logger.debug("EmvTransaction>>emvReadData>>start");
        if (para.isECTrans()) {
            emvHandler.pbocECenable(true);
            Logger.debug("set pboc EC enable true");
        } else {
            emvHandler.pbocECenable(false);
            Logger.debug("set pboc EC enable false");
        }
        emvHandler.setEMVInitCallback(emvInitListener);
        emvHandler.setApduExchangeCallback(apduExchangeListener);
        emvHandler.setDataElement(new byte[]{(byte) 0x9c}, new byte[]{0x00});

        Logger.debug("EmvTransaction>>emvReadData>>应用选择");
        int ret1 = emvHandler.selectApp(Integer.parseInt(traceNo));//JM
        Logger.debug("EmvTransaction>>emvReadData>>selectApp = " + ret1);
        if (ret1 != 0) {
            if (ret1 == 2064){
                return TcodeError.T_ERR_FALLBACK;
            }else {
                Logger.debug("EmvTransaction>>emvReadData>>selectApp fail");
                if (retExpApp == 2063) {
                    return TcodeError.T_BLOCKED_APLICATION;
                }else if (retExpApp == 116) {
                    return TcodeError.T_USER_CANCEL_INPUT;
                }
                return TcodeError.T_SELECT_APP_ERR;
            }
        }

        if (para.isECTrans()) {
            byte[] firstBal = emvHandler.pbocReadECBalance();
            //9F79 , DF71 , DF79 , DF71
            if (firstBal != null) {
                eCAmount = ISOUtil.byte2hex(firstBal);
            }
        }

        Logger.debug("EmvTransaction>>emvReadData>>读应用数据");
        try {
            ret1 = emvHandler.readAppData();
        } catch (SDKException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "Excepcion " + e.toString());
        }
        Logger.debug("EmvTransaction>>emvReadData>>readAppData = " + ret1);
        if (ret1 != 0) {
            Logger.debug("EmvTransaction>>emvReadData>>readAppData fail");
            switch (retExpApp) {
                case TcodeError.T_ERR_EXP_DATE_APP:
                    ret1 = TcodeError.T_ERR_EXP_DATE_APP;
                    break;
                case TcodeError.T_USER_CANCEL_INPUT:
                    ret1 = TcodeError.T_USER_CANCEL_INPUT;
                    break;
                default:
                    ret1 = TcodeError.T_READ_APP_DATA_ERR;
                    break;
            }
            return ret1;
        }

        Logger.debug("EmvTransaction>>emvReadData>>脱机数据认证");
        if (ifOfflineDataAuth) {
            try {
                ret1 = emvHandler.offlineDataAuthentication();
            } catch (SDKException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "Excepcion " + e.toString());
            }
            Logger.debug("EmvTransaction>>emvReadData>>offlineDataAuthentication=" + ret1);
            if (ret1 != 0) {
                Logger.debug("EmvTransaction>>emvReadData>>offlineDataAuthentication fail");
                return TcodeError.T_OFFLINE_DATAAUTH_ERR;
            }
        }
        Logger.debug("EmvTransaction>>emvReadData>>finish");
        return 0;
    }

    /**
     * EMV联机后处理，二次授权
     *
     * @param rspCode
     * @param authCode
     * @param rspICCData
     * @param onlineResult
     * @return
     */
    public int afterOnline(String rspCode, String authCode, byte[] rspICCData, int onlineResult) {
        Logger.debug("enter afterOnline");
        Logger.debug("rspCode = " + rspCode);
        Logger.debug("authCode = " + authCode);
        if (rspICCData != null) {
            Logger.debug("rspICCData = " + ISOUtil.byte2hex(rspICCData));
        }
        this.rspCode = rspCode;
        this.authCode = authCode;
        this.rspICCData = rspICCData;
        this.onlineResult = onlineResult;

        boolean onlineTransaction = false;
        try {
            onlineTransaction = emvHandler.onlineTransaction();
        } catch (SDKException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
        Logger.debug("onlineTransaction =" + onlineTransaction);
        if (onlineTransaction) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * 获取当前交易卡号
     *
     * @return
     */
    public String getCardNo() {
        byte[] temp = new byte[256];
        int len = PAYUtils.getTlvDataKernal(0x5A, temp);
        return ISOUtil.trimf(ISOUtil.byte2hex(temp, 0, len));
    }

    /**
     * 获取当前交易卡号
     *
     * @return
     */
    public String getTrack2() {
        byte[] temp = new byte[256];
        int len = PAYUtils.getTlvDataKernal(0x57, temp);
        return ISOUtil.trimf(ISOUtil.byte2hex(temp, 0, len));
    }

    /**
     * 获取当前交易密码加密
     *
     * @return
     */
    public String getPinBlock() {
        return pinBlock;
    }

    public void setPinBlock(String pinBlock) {
        this.pinBlock = pinBlock;
    }

    /**
     * 获取电子现金余额
     *
     * @return
     */
    public String getECAmount() {
        return eCAmount;
    }


    /**
     * 初始化Kernel
     *
     * @return
     */
    public boolean initEmvKernel() {
        emvHandler.initDataElement();
        emvHandler.setKernelType(KERNEL_TYPE_PBOC);

        // 配置MCK,支持项默认为支持，不支持的请设置为-1
        TerminalMckConfigure configure = new TerminalMckConfigure();
        configure.setTerminalType(0x22);
        configure.setTerminalCapabilities(new byte[]{(byte) 0xE0,(byte) 0xF8, (byte) 0xC8});
        configure.setAdditionalTerminalCapabilities(new byte[]{0x60, 0x00,(byte) 0xF0, (byte) 0xA0, 0x01});

        configure.setSupportCardInitiatedVoiceReferrals(false);
        configure.setSupportForcedAcceptanceCapability(false);
        if (para.isNeedOnline()) {
            configure.setSupportForcedOnlineCapability(true);
            Logger.debug("setSupportForcedOnlineCapability true");
        } else {
            configure.setSupportForcedOnlineCapability(false);
            Logger.debug("setSupportForcedOnlineCapability false");
        }
        configure.setPosEntryMode(0x05);

        int ret2 = emvHandler.setMckConfigure(configure);
        if (ret2 != 0) {
            Logger.debug("setMckConfigure failed");
            return false;
        }
        CoreParam coreParam = new CoreParam();
        coreParam.setTerminalId(TMConfig.getInstance().getTermID().getBytes());

        if (agente.getMerchantId()!=null)
            coreParam.setMerchantId(ISOUtil.padright("" + agente.getMerchantId(), 15, ' ').getBytes());
        else
            coreParam.setMerchantId(TMConfig.getInstance().getMerchID().getBytes());
        coreParam.setMerchantCateCode(new byte[]{0x00, 0x01});
        coreParam.setMerchantNameLocLen(17);
        coreParam.setMerchantNameLoc("BCP, Peru".getBytes());
        coreParam.setTerminalCountryCode(new byte[]{0x06, 0x04});
        coreParam.setTransactionCurrencyCode(new byte[]{0x06, 0x04});
        coreParam.setReferCurrencyCode(new byte[]{0x06, 0x04});
        coreParam.setTransactionCurrencyExponent(0x02);
        coreParam.setReferCurrencyExponent(0x02);
        coreParam.setReferCurrencyCoefficient(1000);
        coreParam.setTransactionType(EMVTransType.EMV_GOODS);

        ret2 = emvHandler.setCoreInitParameter(coreParam);
        if (ret2 != 0) {
            Logger.debug("setCoreInitParameter error");
            return false;
        }
        return true;
    }

    private IEMVCallback.EMVInitListener emvInitListener = new IEMVCallback.EMVInitListener() {
        @Override
        public int candidateAppsSelection() {
            Logger.debug("======candidateAppsSelection=====");
            int[] numData = new int[1];
            CandidateListApp[] listapp = new CandidateListApp[32];
            try {
                listapp = emvHandler.getCandidateList();
                if (listapp != null) {
                    numData[0] = listapp.length;
                } else {
                    return -1;
                }
            } catch (SDKException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            }
            int ret3 = 0;
            if (listapp.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < numData[0]; i++) {
                    Logger.debug("应用名称：" + listapp[i].toString());
                    if (i == 0) {
                        sb.append(Arrays.toString(listapp[i].gettCandAppName()));
                    } else {
                        sb.append(",").append(Arrays.toString(listapp[i].gettCandAppName()));
                    }
                }
                if (numData[0] > 1) {
                    Logger.debug("卡片多应用选择");
                    int select = transUI.showCardApplist(timeout, transEname,sb.toString().split(","));
                    if (select >= 0) {
                        ret3 = select;
                    } else {
                        retExpApp = TcodeError.T_USER_CANCEL_INPUT;
                        return -1;
                    }
                }
                Logger.debug("EMV>>多应用选择>>用户选择的应用ret=" + ret3);
                return ret3;
            } else {
                return -1;
            }
        }

        @Override
        public void multiLanguageSelection() {
            byte[] tag = new byte[]{0x5F, 0x2D};
            int ret4 = emvHandler.checkDataElement(tag);
            // 从内核读aid 0x9f06 设置是否支持联机PIN
            Logger.debug("===multiLanguageSelection==ret:" + ret4);
        }

        @Override
        public int getAmount(int[] transAmount, int[] cashBackAmount) {
            Logger.debug("====getAmount======" + amoUnt);

            if (para.isNeedAmount()) {
                if (amoUnt <= 0) {
                    // 调用输入金额
                    Logger.debug("EMV>>需要输入金额且金额参数未0>>EMV流程执行获取金额的回调");

                    InputInfo info = transUI.getOutsideInput(UIUtils.fillScreenAmount(transEname, "1200", "AHORROS", timeout, "S/.","INGRESE MONTO", "200"));
                    if (info.isResultFlag()) {
                        amoUnt = (int) (Double.parseDouble(info.getResult()) * 100);
                        Logger.debug("EMV>>用户输入的金额=" + amoUnt);
                    }
                    otherAmount = 0;
                } else {
                    transAmount[0] = Integer.valueOf(amoUnt + "");
                    cashBackAmount[0] = Integer.valueOf(otherAmount + "");
                }
            }
            return 0;
        }

        @Override
        public int getPin(int[] pinLen, byte[] cardPin) {
            Logger.debug("=====getOfflinePin======");
            // 读PED倒计时并为零 才继续执行
            // 请输入OFFLINE PIN
            return 0;
        }

        @Override
        public int pinVerifyResult(int tryCount) {
            Logger.debug("======pinVerifyResult=======" + tryCount);
            if (tryCount == 0) {
                Logger.debug("EMV>>pinVerifyResult>>脱机PIN校验成功");
            } else if (tryCount == 1) {
                Logger.debug("EMV>>pinVerifyResult>>脱机PIN输入只剩下最后一次机会");
            } else {
                Logger.debug("EMV>>pinVerifyResult>>脱机PIN输入还剩下" + tryCount + "次机会");
            }
            return 0;
        }

        @Override
        public int checkOnlinePIN() {
            Logger.debug("checkOnlinePIN pass=" + para.isNeedPass());
            if (para.isNeedPass()) {
                Logger.debug("=====checkOnlinePIN=======");
                byte[] val = new byte[16];
                String cardNum = "";
                try {
                    val = emvHandler.getDataElement(new byte[]{0x5A});
                } catch (SDKException e) {
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                }
                if (val != null) {
                    cardNum = ISOUtil.trimf(ISOUtil.byte2hex(val, 0, val.length));
                    Logger.debug("EMV>>获取联机PIN>>卡号=" + cardNum);
                }
                PinInfo info = transUI.getPinpadOnlinePin(timeout, String.valueOf(amoUnt), cardNum, "Ingresa clave de tarjeta");
                if (info.isResultFlag()) {
                    if (info.isNoPin()) {
                        pinBlock = "NULL";
                        return -1;
                    } else {
                        pinBlock = ISOUtil.hexString(info.getPinblock());
                        Logger.debug("EMV>>获取联机PIN>>pinBlock=" + pinBlock);
                    }
                } else {
                    pinBlock = "CANCEL";
                    return -1;
                }
                return 0;
            } else {
                Logger.debug("EMV>>checkOnlinePIN>>ret=0");
                return 0;
            }
        }

        /** 核对身份证证件 **/
        @Override
        public int checkCertificate() {
            Logger.debug("=====checkCertificate====");
            return 0;
        }

        @Override
        public int onlineTransactionProcess(byte[] brspCode, byte[] bauthCode,
                                            int[] authCodeLen, byte[] bauthData, int[] authDataLen,
                                            byte[] script, int[] scriptLen, byte[] bonlineResult) {
            Logger.debug("==onlineTransactionProcess========");
            brspCode[0] = 0;
            brspCode[1] = 0;
            authCodeLen[0] = 0;
            scriptLen[0] = 0;
            authDataLen[0] = 0;
            bonlineResult[0] = (byte) onlineResult;
            if (rspCode == null || rspCode.equals("") || onlineResult != 0) {
                return 0;
            } else {
                System.arraycopy(rspCode.getBytes(), 0, brspCode, 0, 2);
            }
            if (authCode == null || authCode.equals("")) {
                authCodeLen[0] = 0;
            } else {
                authCodeLen[0] = authCode.length();
                System.arraycopy(authCode.getBytes(), 0, bauthCode, 0, authCodeLen[0]);
            }
            if (rspICCData != null && rspICCData.length > 0) {
                authDataLen[0] = PAYUtils.getTlvData(rspICCData, rspICCData.length, 0x91, bauthData, false);
                byte[] scriptTemp = new byte[256];
                int scriptLen1 = PAYUtils.getTlvData(rspICCData, rspICCData.length, 0x71, scriptTemp, true);
                System.arraycopy(scriptTemp, 0, script, 0, scriptLen1);
                int scriptLen2 = PAYUtils.getTlvData(rspICCData, rspICCData.length, 0x72, scriptTemp, true);
                System.arraycopy(scriptTemp, 0, script, scriptLen1, scriptLen2);
                scriptLen[0] = scriptLen1 + scriptLen2;
            }
            bonlineResult[0] = (byte) onlineResult;
            Logger.debug("onlineTransactionProcess return_exit 0.");
            return 0;
        }

        @Override
        public int issuerReferralProcess() {
            Logger.debug("=====issuerReferralProcess======");
            return 0;
        }

        @Override
        public int adviceProcess(int firstFlg) {
            Logger.debug("=====adviceProcess======");
            return 0;
        }

        @Override
        public int checkRevocationCertificate(int caPublicKeyID, byte[] rid,
                                              byte[] destBuf) {
            Logger.debug("===checkRevocationCertificate==");
            return -1;
        }

        /**
         * 黑名单
         */
        @Override
        public int checkExceptionFile(int panLen, byte[] pan, int panSN) {
            Logger.debug("==checkExceptionFile=");
            return -1;
        }

        /**
         * 判断IC卡脱机的累计金额 超过就强制联机
         */
        @Override
        public int getTransactionLogAmount(int panLen, byte[] pan, int panSN) {
            Logger.debug("======getTransactionLogAmount===");
            return 0;
        }

        //增加脱机PIN回调接口
        @Override
        public int getOfflinePin(int i, RsaPinKey rsaPinKey, byte[] bytes, byte[] bytes1) {
            return PinpadManager.getInstance().getOfflinePin(i, rsaPinKey, bytes, bytes1);
        }
    };

    private IEMVCallback.ApduExchangeListener apduExchangeListener = new IEMVCallback.ApduExchangeListener() {
        @Override
        public int apduExchange(byte[] sendData, int[] recvLen, byte[] recvData) {
            Logger.debug("==apduExchangeListener===");
            Logger.debug("sendData:" + ISOUtil.byte2hex(sendData));
            if (inputMode == Trans.ENTRY_MODE_NFC) {
                int[] status = new int[1];
                long start = SystemClock.uptimeMillis();
                while (true) {
                    try {
                        status[0] = emvContactlessCard.getStatus();
                    } catch (SDKException e) {
                        Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                    }
                    if (SystemClock.uptimeMillis() - start > 3 * 1000 || (status[0] == EmvContactlessCard.STATUS_EXCHANGE_APDU)) {
                        break;
                    }
                    try {
                        Thread.sleep(6);
                    } catch (Exception e) {
                        Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                    }
                }
                int len = 0;
                try {
                    byte[] rawData = emvContactlessCard.transmit(sendData);
                    if (rawData != null) {
                        Logger.debug("rawData=" + ISOUtil.hexString(rawData));
                        len = rawData.length;
                    }
                    if (len <= 0) {
                        return -1;
                    }
                    System.arraycopy(rawData, 0, recvData, 0, rawData.length);
                } catch (SDKException e) {
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                }
                recvLen[0] = len;
                Logger.debug("Data received from card:" + ISOUtil.byte2hex(recvData, 0, recvLen[0]));
                return 0;
            }
            if (Trans.ENTRY_MODE_ICC == inputMode) {
                int len = 0;
                try {
                    if (contactCard == null || icCard == null) {
                        return -1;
                    } else {
                        byte[] rawData = icCard.transmit(contactCard, sendData);
                        if (rawData != null) {
                            Logger.debug("rawData = " + ISOUtil.hexString(rawData));
                            len = rawData.length;
                        }
                        if (len <= 0) {
                            return -1;
                        }
                        System.arraycopy(rawData, 0, recvData, 0, rawData.length);
                    }
                } catch (SDKException e) {
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                }
                if (len >= 0) {
                    recvLen[0] = len;
                    Logger.debug("Data received from card:" + ISOUtil.byte2hex(recvData, 0, recvLen[0]));
                    if (isAppExpDate(recvData)) {
                        retExpApp = TcodeError.T_ERR_EXP_DATE_APP;
                        return -1;
                    }

                    if (isAppBlocked(recvData, len)) {
                        retExpApp = TcodeError.T_BLOCKED_APLICATION;
                        return -1;
                    }
                    return 0;
                }
                return -1;
            }
            return -1;
        }

        /**
         * Application Expiration date
         * Tag emv 5F24
         * JM
         *
         *
         * @return
         */
        private boolean isAppExpDate(byte[] data) {
            String hex = "";
            int len = data.length;

            try{
                hex = ISOUtil.byte2hex(data,0,len);
            }catch (NumberFormatException e){
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                return false;
            }
            try {
                if (len > 2 && (hex.contains("5F24"))) {

                    int offset;
                    int yearCard;
                    int monCard;
                    int yearLocal;
                    int monLocal;
                    String dateCard;
                    String dateLocal;

                    offset = hex.indexOf("5F24");
                    offset += 4;

                    int l = Integer.parseInt(hex.substring(offset, offset + 2));
                    dateCard = hex.substring(offset + 2, offset + 2 + l * 2);

                    dateLocal = PAYUtils.getExpDate();
                    monLocal = Integer.parseInt(dateLocal.substring(2));
                    yearLocal = Integer.parseInt(dateLocal.substring(0, 2));
                    yearCard = Integer.parseInt(dateCard.substring(0, 2));
                    monCard = Integer.parseInt(dateCard.substring(2, 4));

                    if (yearCard > yearLocal) {
                        return false;
                    } else if (yearCard == yearLocal) {
                        if (monCard > monLocal) {
                            return false;
                        } else return monCard != monLocal;
                    } else {
                        return true;
                    }
                }
            }catch (NumberFormatException ex){
                Logger.logLine(Logger.LOG_EXECPTION, ex.getStackTrace(), ex.getMessage());
                return false;
            }
            return false;
        }

        /**
         * Se crea método para declinar el caso 11 ADVT para declinar transacción cuando la aplicación
         * está bloqueada
         * @param data
         * @param len
         * @return
         */
        private boolean isAppBlocked(byte[] data, int len) {
            String hex = "";
            try{
                hex = ISOUtil.byte2hex(data,0,len);
            }catch (NumberFormatException e){
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                return false;
            }
            try {
                if (len > 2 && hex.contains("6283")) {
                    String last4 = hex.substring(hex.length() - 4);
                    if (last4.equals("6283")) {
                        return true;
                    }
                }
            }catch (NumberFormatException ex){
                Logger.logLine(Logger.LOG_EXECPTION, ex.getStackTrace(), ex.getMessage());
                return false;
            }
            return false;
        }
    };

    /**
     * 与卡片进行APDU交互
     *
     * @param apdu
     * @return
     */
    public byte[] exeAPDU(byte[] apdu) {
        byte[] rawData = null;
        int recvlen = 0;
        try {
            rawData = icCard.transmit(contactCard, apdu);
            if (rawData != null) {
                Logger.debug("rawData = " + ISOUtil.hexString(rawData));
                recvlen = rawData.length;
            }
        } catch (SDKException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
        byte[] recv = new byte[recvlen];
        if (rawData != null) {
            System.arraycopy(rawData, 0, recv, 0, recvlen);
        } else {
            recv = null;
        }
        return recv;
    }

}
