package com.bcp.settings;

import android.content.Context;
import android.content.SharedPreferences;
import com.newpos.libpay.utils.ISOUtil;

public class BCPConfig {
    public static final String TRACK2AGENTEKEY = "TRACK2AGENTE";
    public static final String AMOUNTMAXAGENTKEY = "AMOUNTMAXAGENT";
    public static final String LOGINPASSWORDKEY = "LOGINPASSWORD";
    public static final String PANAGENTEKEY = "PANAGENTE";
    public static final String PWDKEYSTORETRANSPUBKEY = "PWDKEYSTORETRANSPUB";
    public static final String PWDKEYSTOREPOLARISKEY = "PWDKEYSTOREPOLARIS";
    public static final String PWDKEYSTOREBCPKEY = "PWDKEYSTOREBCP";
    public static final String PWDKEYSTOREHTTPSKEY = "PWDKEYSTOREHTTPS";
    public static final String PWDKEYSTORETLSKEY = "PWDKEYSTORETLS";
    public static final String PWDKEYSTORETRANSPRIVKEY = "PWDKEYSTORETRANSPRIV";
    public static final String PWDSECRETKEY = "PWDSECRETKEY";
    private SharedPreferences archivoConfig;

    public static BCPConfig instance = null;

    public static BCPConfig getInstance(Context context) {
        if (instance == null){
            instance = new BCPConfig();
            instance.archivoConfig = context.getSharedPreferences("ConfigFile", Context.MODE_PRIVATE);
        }
        return instance;
    }

    public void setTrack2Agente(String key, String track2Agente) {
        save(key,track2Agente);
    }

    public String getTrack2Agente(String key) {
        String track = archivoConfig.getString(key,"");
        if (!ISOUtil.checkNull( track ) ){
            track = "";
        }
        return track;
    }

    public void setAmntMaxAgent(String key, Long amntMaxAgent) {
        save(key,String.valueOf(amntMaxAgent));
    }

    public Long getAmntMaxAgent(String key) {
        String amount = archivoConfig.getString(key,"0");
        if (!ISOUtil.checkNull( amount ) ){
            amount = "0";
        }
        return Long.valueOf(amount);
    }

    public void setLoginPassword(String key, String loginPassword) {
        save(key,loginPassword);
    }

    public String getLoginPassword(String key) {
        String login = archivoConfig.getString(key,"123456");
        if (!ISOUtil.checkNull( login ) ){
            login = "123456";
        }
        return login;
    }

    public void setPanAgente(String key, String panAgente) {
        save(key,panAgente);
    }

    public String getPanAgente(String key) {
        String pamAgente = archivoConfig.getString(key,"");
        if (!ISOUtil.checkNull( pamAgente ) ){
            pamAgente = "";
        }
        return pamAgente;
    }

    public void setPwdKeystoreTransPub(String key, String pwdKeystoreTransPub) {
        save(key,pwdKeystoreTransPub);
    }

    public String getPwdKeystoreTransPub(String key) {
        String pwdKeystoreTransPub = archivoConfig.getString(key,"");
        if (!ISOUtil.checkNull( pwdKeystoreTransPub ) ){
            pwdKeystoreTransPub = "";
        }
        return pwdKeystoreTransPub;
    }

    public void setPwdKeyStorePolaris(String key,String pwdKeystorePolaris) {
        save(key,pwdKeystorePolaris);
    }

    public String getPwdKeyStorePolaris(String key) {
        String pwdKeyStorePolaris = archivoConfig.getString(key,"");
        if (!ISOUtil.checkNull( pwdKeyStorePolaris ) ){
            pwdKeyStorePolaris = "";
        }
        return pwdKeyStorePolaris;
    }

    public void setPwdKeystoreBcp(String key,String pwdKeystoreBcp) {
        save(key,pwdKeystoreBcp);
    }

    public String getPwdKeystoreBcp(String key) {
        String pwdKeystoreBcp = archivoConfig.getString(key,"");
        if (!ISOUtil.checkNull( pwdKeystoreBcp ) ){
            pwdKeystoreBcp = "";
        }
        return pwdKeystoreBcp;
    }

    public void setPwdKeystoreHttps(String key,String pwdKeystoreHttps) {
        save(key,pwdKeystoreHttps);
    }

    public String getPwdKeystoreHttps(String key) {
        String pwdKeystoreHttps = archivoConfig.getString(key,"");
        if (!ISOUtil.checkNull( pwdKeystoreHttps ) ){
            pwdKeystoreHttps = "";
        }
        return pwdKeystoreHttps;
    }

    public void setPwdKeystoreTls(String key,String pwdKeystoreTls) {
        save(key,pwdKeystoreTls);
    }

    public String getPwdKeystoreTls(String key) {
        String pwdKeystoreTls = archivoConfig.getString(key,"");
        if (!ISOUtil.checkNull( pwdKeystoreTls ) ){
            pwdKeystoreTls = "";
        }
        return pwdKeystoreTls;
    }

    public void setPwdKeystoreTransPriv(String key,String pwdKeystoreTransPriv) {
        save(key,pwdKeystoreTransPriv);
    }

    public String getPwdKeystoreTransPriv(String key) {
        String pwdKeystoreTransPriv = archivoConfig.getString(key,"");
        if (!ISOUtil.checkNull( pwdKeystoreTransPriv ) ){
            pwdKeystoreTransPriv = "";
        }
        return pwdKeystoreTransPriv;
    }

    public void setSecretKey(String key,String secretKey) {
        save(key,secretKey);
    }

    public String getSecretKey(String key) {
        String secretKey = archivoConfig.getString(key,"Zq4t7w9z$C&F)J@NcRfUjXn2r5u8x/A%");
        if (!ISOUtil.checkNull( secretKey ) ){
            secretKey = "Zq4t7w9z$C&F)J@NcRfUjXn2r5u8x/A%";
        }
        return secretKey;
    }

    private void save(String key, String value) {
        SharedPreferences.Editor editor = archivoConfig.edit();
        editor.putString(key, value);

        editor.apply();
    }
}
