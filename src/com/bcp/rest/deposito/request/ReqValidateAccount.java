package com.bcp.rest.deposito.request;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ReqValidateAccount extends JsonUtil {

    private static final String ACCOUNT = "account";
    private static final String ACCOUNTID = "accountId";

    private String ctnAccountId;

    private String getCtnAccountId() {
        return ctnAccountId;
    }

    public void setCtnAccountId(String ctnAccountId) {
        this.ctnAccountId = ctnAccountId;
    }


    public JSONObject buildsObjectJSON(){

        JSONObject jsonRequest = new JSONObject();
        JSONObject jsonAccount = new JSONObject();

        try {

            jsonAccount.put(ACCOUNTID,getCtnAccountId());
            jsonRequest.put(ACCOUNT, jsonAccount);

        } catch (JSONException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " ReqValidateAccount " +  e.getMessage());
            return null;
        }

        Logger.debug("==JSON Request Validate Account==");
        Logger.debug(String.valueOf(jsonRequest));
        Logger.debug("====================");

        return jsonRequest;
    }
}
