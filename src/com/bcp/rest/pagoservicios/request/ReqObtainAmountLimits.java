package com.bcp.rest.pagoservicios.request;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class ReqObtainAmountLimits extends JsonUtil {

    private static final String CLIENTDEPOSITCODE = "clientDepositCode";
    private static final String AFFILIATIONCODE = "affiliationCode";

    private String clientDepositCode;
    private String affiliationCode;

    public String getClientDepositCode() {
        return clientDepositCode;
    }

    public void setClientDepositCode(String clientDepositCode) {
        this.clientDepositCode = clientDepositCode;
    }

    public String getAffiliationCode() {
        return affiliationCode;
    }

    public void setAffiliationCode(String affiliationCode) {
        this.affiliationCode = affiliationCode;
    }


    public JSONObject buildsJsonObject() {

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put(CLIENTDEPOSITCODE, jweEncryptDecrypt(getClientDepositCode() + "",false,false) ? jweDataEncryptDecrypt.getDataEncrypt() : "");
            jsonObject.put(AFFILIATIONCODE, getAffiliationCode());

        } catch (JSONException e){
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
        Logger.debug("==JSON Request Obtain Client Code==");
        Logger.debug(String.valueOf(jsonObject));
        Logger.debug("====================");

        return jsonObject;
    }
}
