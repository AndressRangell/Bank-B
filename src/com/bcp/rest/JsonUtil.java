package com.bcp.rest;
import com.bcp.rest.jwt.CipherDocument;
import com.bcp.rest.jwt.JWEString;
import com.newpos.libpay.Logger;

import org.json.JSONObject;

public class JsonUtil {

    public static final String ROOT = "http://192.168.1.14:5000/"; // agente.getUrl();
    public static final int TIMEOUT = 30000;

    //HEADER
    private static final String DEV_REF = "orig-device-reference";
    private static final String NUMBER = "opn-number";
    private static final String DEVIP = "orig-device-ip";
    private static final String DEVMAC = "orig-device-mac";
    private static final String SESSION = "sessionId";
    private static final String APIKEY = "authorization";


    private String deviceReference;
    private String opnNumber;
    private String deviceIp;
    private String deviceMac;
    private String sessionId;
    private String appKey;

    protected JWEString jweDataEncryptDecrypt;
    protected CipherDocument cipherDocument;

    protected JsonUtil(){}

    public String getDeviceReference() {
        return deviceReference;
    }

    public void setDeviceReference(String deviceReference) {
        this.deviceReference = deviceReference;
    }

    public String getOpnNumber() {
        return opnNumber;
    }

    public void setOpnNumber(String opnNumber) {
        this.opnNumber = opnNumber;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public static String getDevRef() {
        return DEV_REF;
    }

    public static String getNUMBER() {
        return NUMBER;
    }

    public static String getDEVIP() {
        return DEVIP;
    }

    public static String getDEVMAC() {
        return DEVMAC;
    }

    public static String getSESSION() {
        return SESSION;
    }

    public static String getAPIKEY() {
        return APIKEY;
    }

    /**
     * Valida si el objeto tiene valor null
     * @param jsonObject
     * @param key
     */
    public static boolean validatedNull(JSONObject jsonObject, String key){
        if (jsonObject==null)
            return false;

        return (jsonObject.has(key) && !jsonObject.isNull(key));
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

    protected boolean funcCipherDocument(String doc){
        if (cipherDocument ==null)
            cipherDocument = new CipherDocument();
        cipherDocument.setDocument(doc);
        return cipherDocument.generatePinblock();
    }
}
