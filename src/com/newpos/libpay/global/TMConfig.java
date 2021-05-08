package com.newpos.libpay.global;

import android.content.Context;
import android.util.Log;

import com.newpos.libpay.Logger;
import com.newpos.libpay.PaySdk;
import com.newpos.libpay.PaySdkException;
import com.newpos.libpay.utils.ISOUtil;
import com.newpos.libpay.utils.PAYUtils;
import com.pos.device.config.DevConfig;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by zhouqiang on 2017/4/29.
 *
 * @author zhouqiang
 * 全局参数管理
 */

public class TMConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    private static String configPath = "config.dat";
    private static TMConfig mInstance = null;

    /**
     * 本程序的所有文件的保存路径
     */
    private static String rootFilePath;

    public static String getRootFilePath() {
        return rootFilePath;
    }

    public static void setRootFilePath(String rootFilePath) {
        TMConfig.rootFilePath = rootFilePath;
    }

    /**
     * 是否开启sdk调试信息
     */


    /**
     * 是否开启sdk联机功能
     */
    private boolean isOnline = false;

    public boolean isOnline() {
        return isOnline;
    }

    public TMConfig setOnline(boolean online) {
        isOnline = online;
        return mInstance;
    }

    /**
     * 银行logo列表索引
     */
    private int bankid;

    public int getBankid() {
        return bankid;
    }

    private void setBankid(int bankid) {
        if ((bankid < TMConstants.ASSETS.length) && bankid >= 0) {
            this.bankid = bankid;
        } else {
            this.bankid = 0;
        }
    }

    /**
     * 标记当前sdk环境支持的支付规范，不同在于相关标准及交易算法
     * 1 ==== 应用交易规范基本按照银联规范
     * 2 ==== 应用交易规范基本按照中信规范
     */
    private int standard;

    public int getStandard() {
        return standard;
    }

    public TMConfig setStandard(int standard) {
        if (standard == 1 || standard == 2) {
            this.standard = standard;
        } else {
            this.standard = 1;
        }
        return mInstance;
    }

    /**
     * 联机后台台公网端口
     */
    private String ip;

    public String getIp() {
        return ip;
    }

    public TMConfig setIp(String ip) {
        this.ip = ip;
        return mInstance;
    }

    /**
     * 联机后台公网IP
     */
    private String ip2;

    public String getIP2() {
        return ip2;
    }

    public TMConfig setIp2(String s) {
        this.ip2 = s;
        return mInstance;
    }

    /**
     * 非接强制PBOC
     */
    private boolean forcePboc;

    public boolean isForcePboc() {
        return forcePboc;
    }

    public TMConfig setForcePboc(boolean forcePboc) {
        this.forcePboc = forcePboc;
        return mInstance;
    }

    /**
     * 联机后内网IP
     */
    private String port;

    public String getPort() {
        return port;
    }

    public TMConfig setPort(String port) {
        this.port = port;
        return mInstance;
    }

    /**
     * 联机后台内网端口
     */
    private String port2;

    public String getPort2() {
        return port2;
    }

    public TMConfig setPort2(String s) {
        this.port2 = s;
        return mInstance;
    }

    /**
     * 联机超时时间（单位:秒）
     */
    private int timeout;

    public int getTimeout() {
        return timeout;
    }

    public TMConfig setTimeout(int timeout) {
        this.timeout = timeout;
        return mInstance;
    }

    /**
     * 是否开启公网通讯
     */
    private boolean isPubCommun = false;

    public TMConfig setPubCommun(boolean is) {
        this.isPubCommun = is;
        return mInstance;
    }

    public boolean getPubCommun() {
        return isPubCommun;
    }

    /**
     * 等待用户交互超时时间(单位 :秒)
     */
    private int waitUserTime;

    public int getWaitUserTime() {
        return waitUserTime;
    }

    public TMConfig setWaitUserTime(int waitUserTime) {
        this.waitUserTime = waitUserTime;
        return mInstance;
    }

    /**
     * 扫码时开启闪光
     */
    private boolean scanTorchOn;

    public boolean isScanTorchOn() {
        return scanTorchOn;
    }

    public TMConfig setScanTorchOn(boolean scanTorchOn) {
        this.scanTorchOn = scanTorchOn;
        return mInstance;
    }

    /**
     * 扫码时扫到结果打开蜂鸣提示
     */
    private boolean scanBeeper;

    public boolean isScanBeeper() {
        return scanBeeper;
    }

    public TMConfig setScanBeeper(boolean scanBeeper) {
        this.scanBeeper = scanBeeper;
        return mInstance;
    }

    /**
     * 扫码时使用后置
     */
    private boolean scanBack;

    public boolean isScanBack() {
        return scanBack;
    }

    public TMConfig setScanBack(boolean scanFront) {
        this.scanBack = scanFront;
        return mInstance;
    }

    /**
     * 消费撤销密码开关
     */
    private boolean revocationPassWSwitch;

    public boolean getRevocationPassSwitch() {
        return revocationPassWSwitch;
    }

    public TMConfig setRevocationPassWSwitch(boolean is) {
        this.revocationPassWSwitch = is;
        return mInstance;
    }

    /**
     * 消费撤销用卡开关
     */
    private boolean revocationCardSwitch;

    public TMConfig setRevocationCardSwitch(boolean is) {
        this.revocationCardSwitch = is;
        return mInstance;
    }

    public boolean getRevocationCardSwitch() {
        return revocationCardSwitch;
    }

    /**
     * 预授权撤销密码开关
     */
    private boolean preauthVoidPassSwitch;

    public boolean isPreauthVoidPassSwitch() {
        return preauthVoidPassSwitch;
    }

    public TMConfig setPreauthVoidPassSwitch(boolean preauthVoidPassSwitch) {
        this.preauthVoidPassSwitch = preauthVoidPassSwitch;
        return mInstance;
    }

    /**
     * 预授权完成密码开关
     */
    private boolean preauthCompletePassSwitch;

    public boolean isPreauthCompletePassSwitch() {
        return preauthCompletePassSwitch;
    }

    public TMConfig setPreauthCompletePassSwitch(boolean preauthCompletePassSwitch) {
        this.preauthCompletePassSwitch = preauthCompletePassSwitch;
        return mInstance;
    }

    /**
     * 预授权完成撤销用卡开关
     */
    private boolean preauthCompleteVoidCardSwitch;

    public boolean isPreauthCompleteVoidCardSwitch() {
        return preauthCompleteVoidCardSwitch;
    }

    public TMConfig setPreauthCompleteVoidCardSwitch(boolean preauthCompleteVoidCardSwitch) {
        this.preauthCompleteVoidCardSwitch = preauthCompleteVoidCardSwitch;
        return mInstance;
    }

    /**
     * 主管密码
     */
    private String masterPass;

    public String getMasterPass() {
        return masterPass;
    }

    public TMConfig setMasterPass(String pass) {
        this.masterPass = pass;
        return mInstance;
    }

    /**
     * 维护密码
     */
    private String maintainPass;

    public String getMaintainPass() {
        return maintainPass;
    }

    public TMConfig setMaintainPass(String pass) {
        this.maintainPass = pass;
        return mInstance;
    }

    /**
     * 主密钥索引号
     */
    private int masterKeyIndex;

    public int getMasterKeyIndex() {
        return masterKeyIndex;
    }

    public TMConfig setMasterKeyIndex(int masterKeyIndex) {
        this.masterKeyIndex = masterKeyIndex;
        return mInstance;
    }

    /**
     * 是否校验IC卡刷卡
     */
    private boolean isCheckICC;

    public boolean isCheckICC() {
        return isCheckICC;
    }

    public TMConfig setCheckICC(boolean is) {
        this.isCheckICC = is;
        return mInstance;
    }

    /**
     * 报文TPDU
     */
    private String tpdu;

    public String getTpdu() {
        return tpdu;
    }

    public TMConfig setTpdu(String tpdu) {
        this.tpdu = tpdu;
        return mInstance;
    }

    /**
     * 报文头
     */
    private String header;

    public String getHeader() {
        return header;
    }

    public TMConfig setHeader(String header) {
        this.header = header;
        return mInstance;
    }

    /**
     * 终端ID
     */
    private String termID;

    public String getTermID() {
        return termID;
    }

    public TMConfig setTermID(String termID) {
        this.termID = termID;
        return mInstance;
    }

    /**
     * 商户ID
     */
    private String merchID;

    public String getMerchID() {
        return merchID;
    }

    public TMConfig setMerchID(String merchID) {
        this.merchID = merchID;
        return mInstance;
    }

    /**
     * 分支号
     */
    private int batchNo;

    public String getBatchNo() {
        return ISOUtil.padleft(batchNo + "", 6, '0');
    }

    public TMConfig setBatchNo(int batchNo) {
        this.batchNo = batchNo;
        if (this.batchNo == 999999) {
            this.batchNo = 0;
        }
        this.batchNo += 1;
        return mInstance ;
    }

    /**
     * 流水号
     */
    private int traceNo;

    public String getTraceNo() {
        return ISOUtil.padleft(traceNo + "", 6, '0');
    }

    public TMConfig setTraceNo(int traceNo) {
        this.traceNo = traceNo;
        return mInstance;
    }

    /**
     * 操作员编号
     */
    private int oprNo;

    public int getOprNo() {
        return oprNo;
    }

    public TMConfig setOprNo(int oprNo) {
        this.oprNo = oprNo;
        return mInstance;
    }

    /**
     * 1-3联 1 商户 2.持卡人(没签名) 3.银行
     */
    private int printerTickNumber;

    public int getPrinterTickNumber() {
        return printerTickNumber;
    }

    public TMConfig setPrinterTickNumber(int n) {
        this.printerTickNumber = n;
        return mInstance;
    }

    /**
     * 商户名称
     */
    private String merchName;

    public String getMerchName() {
        return merchName;
    }

    public TMConfig setMerchName(String merchName) {
        this.merchName = merchName;
        return mInstance;
    }

    /**
     * 1中文 2英文 3中英文
     */
    private int printEn;

    public int getPrintEn() {
        return printEn;
    }

    public TMConfig setPrintEn(int lang) {
        this.printEn = lang;
        return mInstance;
    }

    /**
     * 磁道是否加密
     */
    private boolean isTrackEncrypt;

    public boolean isTrackEncrypt() {
        return isTrackEncrypt;
    }

    public TMConfig setTrackEncrypt(boolean is) {
        this.isTrackEncrypt = is;
        return mInstance;
    }

    /**
     * 是否是单倍长密钥
     */
    private boolean isSingleKey;

    public boolean isSingleKey() {
        return isSingleKey;
    }

    public TMConfig setSingleKey(boolean is) {
        this.isSingleKey = is;
        return mInstance;
    }

    /**
     * 货币代码
     */
    private String currencyCode;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public TMConfig setCurrencyCode(String cur) {
        this.currencyCode = cur;
        return mInstance;
    }

    /**
     * 商行代码
     */
    private String firmCode;

    public String getFirmCode() {
        return firmCode;
    }

    public TMConfig setFirmCode(String firmCode) {
        this.firmCode = firmCode;
        return mInstance;
    }

    /**
     * 冲正重发次数
     */
    private int reversalCount;

    public int getReversalCount() {
        return reversalCount;
    }

    public TMConfig setReversalCount(int reversalCount) {
        this.reversalCount = reversalCount;
        return mInstance;
    }

    /**
     * banderaMessageTel
     */
    private boolean banderaMessageTel;

    public boolean isBanderaMessageTel() {
        return banderaMessageTel;
    }

    public TMConfig setbanderaMessageTel(boolean banderaMessageTel) {
        this.banderaMessageTel = banderaMessageTel;
        return mInstance;
    }

    /**
     * banderaMessageFirma
     */
    private boolean banderaMessageFirma;

    public boolean isBanderaMessageFirma() {
        return banderaMessageFirma;
    }

    public TMConfig setbanderaMessageFirma(boolean banderaMessageFirma) {
        this.banderaMessageFirma = banderaMessageFirma;
        return mInstance;
    }

    /**
     * banderaMessageDoc
     */
    private boolean banderaMessageDoc;

    public boolean isBanderaMessageDoc() {
        return banderaMessageDoc;
    }

    public TMConfig setbanderaMessageDoc(boolean banderaMessageDoc) {
        this.banderaMessageDoc = banderaMessageDoc;
        return mInstance;
    }

    /**
     * Intentos de conexion
     */
    private int intentosConex;

    public int getIntentosConex() {
        return intentosConex;
    }

    public TMConfig setIntentosConex(int intentosConex) {
        this.intentosConex = intentosConex;
        return mInstance;
    }

    private String nii;

    public String getNii() {
        return nii;
    }

    public TMConfig setNii(String nii) {
        this.nii = nii;
        return mInstance;
    }
    private  long amntMaxAgent;

    public Long getamntMaxAgent() {
        return amntMaxAgent;
    }

    public TMConfig setamntMaxAgent(Long amntMaxAgent) {
        this.amntMaxAgent = amntMaxAgent;
        return mInstance;
    }

    private String track2Agente;

    public String getTrack2Agente() {
        return track2Agente;
    }

    public TMConfig setTrack2Agente(String track2Agente) {
        this.track2Agente = track2Agente;
        return mInstance;
    }

    private String loginPassword;

    public String getLoginPassword() {
        return loginPassword;
    }

    public TMConfig setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
        return mInstance;
    }
    private boolean isError;

    public boolean isError() {
        return isError;
    }

    public TMConfig setError(boolean error) {
        isError = error;
        return mInstance;
    }

    private boolean isInfo;

    public boolean isInfo(){
        return isInfo;
    }

    public TMConfig setInfo(boolean info){
        isInfo = info;
        return mInstance;
    }

    private boolean isDebug;

    public boolean isDebug() {
        return isDebug;
    }

    public TMConfig setDebug(boolean debug) {
        isDebug = debug;
        return mInstance;
    }

    private String loginSupervisor;

    public String getLoginSupervisor() {
        return loginSupervisor;
    }

    public TMConfig  setLoginSupervisor(String loginSupervisor) {
        this.loginSupervisor = loginSupervisor;
        return  mInstance;
    }

    /**
     * 流水号
     */
    private int opnNumber;

    public String getOpnNumber() {
        return ISOUtil.padleft(opnNumber + "", 8, '0');
    }

    public TMConfig setOpnNumber(int opnNumber) {
        this.opnNumber = opnNumber;
        return mInstance;
    }
    private String pwdKeystoreTransPub;

    public String getPwdKeystoreTransPub() {
        return pwdKeystoreTransPub;
    }

    public TMConfig setPwdKeystoreTransPub(String pwdKeystoreTransPub) {
        this.pwdKeystoreTransPub = pwdKeystoreTransPub;
        return mInstance;
    }

    private String pwdKeystoreTransPriv;

    public String getPwdKeystoreTransPriv() {
        return pwdKeystoreTransPriv;
    }

    public TMConfig setPwdKeystoreTransPriv(String pwdKeystoreTransPriv) {
        this.pwdKeystoreTransPriv = pwdKeystoreTransPriv;
        return mInstance;
    }

    private String pwdKeystorePolaris;

    public String getPwdKeystorePolaris() {
        return pwdKeystorePolaris;
    }

    public TMConfig setPwdKeystorePolaris(String pwdKeystorePolaris) {
        this.pwdKeystorePolaris = pwdKeystorePolaris;
        return mInstance;
    }

    private String pwdKeystoreBcp;

    public String getPwdKeystoreBcp() {
        return pwdKeystoreBcp;
    }

    public TMConfig setPwdKeystoreBcp(String pwdKeystoreBcp) {
        this.pwdKeystoreBcp = pwdKeystoreBcp;
        return mInstance;
    }

    private String pwdKeystoreHttps;

    public String getPwdKeystoreHttps() {
        return pwdKeystoreHttps;
    }

    public TMConfig setPwdKeystoreHttps(String pwdKeystoreHttps) {
        this.pwdKeystoreHttps = pwdKeystoreHttps;
        return mInstance;
    }

    private String pwdKeystoreTls;

    public String getPwdKeystoreTls() {
        return pwdKeystoreTls;
    }

    public TMConfig setPwdKeystoreTls(String pwdKeystoreTls) {
        this.pwdKeystoreTls = pwdKeystoreTls;
        return mInstance;
    }

    private String panAgente;

    public String getPanAgente() {
        return panAgente;
    }

    public TMConfig setPanAgente(String panAgente) {
        this.panAgente = panAgente;
        return mInstance;
    }

    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }

    public TMConfig setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return mInstance;
    }

    private int intentLoginAgent;

    public int getIntentLoginAgent() {
        return intentLoginAgent;
    }

    public TMConfig setIntentLoginAgent(String intentLoginAgent) {
        this.intentLoginAgent = intentLoginAgent == null || intentLoginAgent.equals("")? 0 : Integer.parseInt(intentLoginAgent);
        return mInstance;
    }

    private Date dateUnlockAgent;

    public Date getDateUnlockAgent() {
        return dateUnlockAgent;
    }

    public TMConfig setDateUnlockAgent(String dateUnlockAgent) {
        this.dateUnlockAgent = dateUnlockAgent == null || dateUnlockAgent.equals("") ? PAYUtils.strToDate(PAYUtils.getSysTime()) : PAYUtils.strToDate(dateUnlockAgent);
        return mInstance;
    }

    private int intentLoginTecnico;

    public int getIntentLoginTecnico() {
        return intentLoginTecnico;
    }

    public TMConfig setIntentLoginTecnico(String intentLoginTecnico) {
        this.intentLoginTecnico = intentLoginTecnico == null || intentLoginTecnico.equals("")? 0 : Integer.parseInt(intentLoginTecnico);
        return mInstance;
    }

    private Date dateUnlockTecnico;

    public Date getDateUnlockTecnico() {
        return dateUnlockTecnico;
    }

    public TMConfig setDateUnlockTecnico(String dateUnlockTecnico) {
        this.dateUnlockTecnico = dateUnlockTecnico == null || dateUnlockTecnico.equals("") ? PAYUtils.strToDate(PAYUtils.getSysTime()) : PAYUtils.strToDate(dateUnlockTecnico);
        return mInstance;
    }

    private String reservado1;

    public String getReservado1() {
        return reservado1;
    }

    public TMConfig setReservado1(String reservado1) {
        this.reservado1 = reservado1;
        return mInstance;
    }

    private String reservado2;

    public String getReservado2() {
        return reservado2;
    }

    public TMConfig setReservado2(String reservado2) {
        this.reservado2 = reservado2;
        return mInstance;
    }

    private String reservado3;

    public String getReservado3() {
        return reservado3;
    }

    public TMConfig setReservado3(String reservado3) {
        this.reservado3 = reservado3;
        return mInstance;
    }

    private TMConfig() {
        try {
            loadFile(PaySdk.getInstance().getContext(),
                    PaySdk.getInstance().getParaFilepath());
        } catch (PaySdkException pse) {
            Log.e("ERROR TRY", "TMConfig->" + pse.toString());
        }
    }

    public static TMConfig getInstance() {
        if (mInstance == null) {
            String fullPath = getRootFilePath() + configPath;
            try {
                mInstance = (TMConfig) PAYUtils.file2Object(fullPath);
            } catch (ClassNotFoundException | IOException e) {
                Log.e("ERROR TRY", "getInstance->" + e.toString());
            }
            if (mInstance == null) {
                mInstance = new TMConfig();
            }
        }
        return mInstance;
    }

    private void loadFile(Context context, String path) {
        Log.i("Info: ", "loadFile->path:" + path);

        String t = "1";
        Properties properties = PAYUtils.lodeConfig(context, TMConstants.DEFAULTCONFIG);
        if (properties != null) {
            Enumeration<?> enumeration = properties.propertyNames();
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                Log.i("Info: ", "loadFile->name:" + name);
                if (!PAYUtils.isNullWithTrim(name)) {
                    int index = Integer.parseInt(name.substring(name.length() - 2));
                    String prop = properties.getProperty(name);
                    try {
                        switch (index - 1) {
                            case 0:
                                setIp(prop);
                                break;
                            case 1:
                                setPort(prop);
                                break;
                            case 2:
                                setIp2(prop);
                                break;
                            case 3:
                                setPort2(prop);
                                break;
                            case 4:
                                setTimeout(Integer.parseInt(prop) * 1000);
                                break;
                            case 5:
                                setPubCommun(prop.equals(t));
                                break;
                            case 6:
                                setWaitUserTime(Integer.parseInt(prop));
                                break;
                            case 7:
                                setRevocationPassWSwitch(prop.equals(t));
                                break;
                            case 8:
                                setRevocationCardSwitch(prop.equals(t));
                                break;
                            case 9:
                                setPreauthVoidPassSwitch(prop.equals(t));
                                break;
                            case 10:
                                setPreauthCompletePassSwitch(prop.equals(t));
                                break;
                            case 11:
                                setPreauthCompleteVoidCardSwitch(prop.equals(t));
                                break;
                            case 12:
                                setMasterPass(prop);
                                break;
                            case 13:
                                setMasterKeyIndex(Integer.parseInt(prop));
                                break;
                            case 14:
                                setCheckICC(prop.equals(t));
                                break;
                            case 15:
                                setTpdu(prop);
                                break;
                            case 16:
                                setHeader(prop);
                                break;
                            case 17:
                                String sn = DevConfig.getSN();
                                sn = sn.substring(2);
                                setTermID(sn);
                                break;
                            case 18:
                                setMerchID(prop);
                                break;
                            case 19:
                                setBatchNo(Integer.parseInt(prop));
                                break;
                            case 20:
                                setTraceNo(Integer.parseInt(prop));
                                break;
                            case 21:
                                setOprNo(Integer.parseInt(prop));
                                break;
                            case 22:
                                setPrinterTickNumber(Integer.parseInt(prop));
                                break;
                            case 23:
                                setMerchName(prop);
                                break;
                            case 24:
                                setPrintEn(Integer.parseInt(prop));
                                break;
                            case 25:
                                setTrackEncrypt(prop.equals(t));
                                break;
                            case 26:
                                setSingleKey(prop.equals(t));
                                break;
                            case 27:
                                setCurrencyCode(prop);
                                break;
                            case 28:
                                setFirmCode(prop);
                                break;
                            case 29:
                                setReversalCount(Integer.parseInt(prop));
                                break;
                            case 30:
                                setMaintainPass(prop);
                                break;
                            case 31:
                                setScanTorchOn(prop.equals(t));
                                break;
                            case 32:
                                setScanBeeper(prop.equals(t));
                                break;
                            case 33:
                                setScanBack(prop.equals(t));
                                break;
                            case 34:
                                setDebug(prop.equals(t));
                                break;
                            case 35:
                                setOnline(prop.equals(t));
                                break;
                            case 36:
                                setBankid(Integer.parseInt(prop));
                                break;
                            case 37:
                                setStandard(Integer.parseInt(prop));
                                break;
                            case 38:
                                setForcePboc(prop.equals(t));
                                break;
                            case 39:
                                setbanderaMessageTel(prop.equals(t));
                                break;
                            case 40:
                                setbanderaMessageFirma(prop.equals(t));
                                break;
                            case 41:
                                setbanderaMessageDoc(prop.equals(t));
                                break;
                            case 42:
                                setIntentosConex(Integer.parseInt(prop));
                                break;
                            case 43:
                                setNii(prop);
                                break;
                            case 44:
                                setTrack2Agente(prop);
                                break;
                            case 45:
                                setamntMaxAgent(Long.valueOf(prop));
                                break;
                            case 46:
                                setLoginPassword(prop);
                                break;
                            case 47:
                                setLoginSupervisor(prop);
                                break;
                            case 48:
                                setError(prop.equals(t));
                                break;
                            case 49:
                                setInfo(prop.equals(t));
                                break;
                            case 50:
                                setOpnNumber(Integer.parseInt(prop));
                                break;
                            case 51:
                                setPanAgente(prop);
                                break;
                            case 52:
                                setPwdKeystoreTransPub(prop);
                                break;
                            case 53:
                                setPwdKeystorePolaris(prop);
                                break;
                            case 54:
                                setPwdKeystoreBcp(prop);
                                break;
                            case 55:
                                setPwdKeystoreHttps(prop);
                                break;
                            case 56:
                                setPwdKeystoreTls(prop);
                                break;
                            case 57:
                                setPwdKeystoreTransPriv(prop);
                                break;
                            case 58:
                                setSecretKey(prop);
                                break;
                            case 59:
                                setIntentLoginAgent(prop);
                                break;
                            case 60:
                                setDateUnlockAgent(prop);
                                break;
                            case 61:
                                setIntentLoginTecnico(prop);
                                break;
                            case 62:
                                setDateUnlockTecnico(prop);
                                break;
                            case 63:
                                setReservado1(prop);
                                break;
                            case 64:
                                setReservado2(prop);
                                break;
                            case 65:
                                setReservado3(prop);
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        Log.e("Error: ", "loadFile->" + e.toString());
                    }
                }
            }
        }
        save();
    }

    /**
     * 流水号自增设置，用于交易过程中流水号控制
     *
     * @return
     */
    public TMConfig incTraceNo() {
        if (this.traceNo == 999999) {
            this.traceNo = 0;
        }
        this.traceNo += 1;
        this.save();
        return mInstance;
    }

    /**
     * 流水号自增设置，用于交易过程中流水号控制
     *
     * @return
     */
    public TMConfig incOpnNumber() {
        if (this.opnNumber == 99999999) {
            this.opnNumber = 0;
        }
        this.opnNumber += 1;
        this.save();
        return mInstance;
    }

    /**
     * 保存当前配置
     */
    public void save() {
        String fullName = getRootFilePath() + configPath;
        try {
            File file = new File(fullName);
            if (file.exists()) {
                Log.i("Archivo Eliminado" , " "+ file.delete());

            }
            PAYUtils.object2File(mInstance, fullName);
        } catch (IOException e) {
            Log.e("ERROR TRY", e.toString());
        }
    }
}