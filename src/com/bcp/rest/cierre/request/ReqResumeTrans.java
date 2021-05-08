package com.bcp.rest.cierre.request;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class ReqResumeTrans extends JsonUtil{

    //Request
    private static final String FIRST_TRANS_DATE = "firstTransactionDate";
    private static final String FIRST_TRANS_TIME = "firstTransactionTime";
    private static final String LAST_TRANS_DATE = "lastTransactionDate";
    private static final String LAST_TRANS_TIME = "lastTransactionTime";

    private String firstTransDate;
    private String firstTransTime;
    private String lastTransDate;
    private String lastTransTime;

    public String getFirstTransDate() {
        return firstTransDate;
    }

    public void setFirstTransDate(String firstTransDate) {
        this.firstTransDate = firstTransDate;
    }

    public String getFirstTransTime() {
        return firstTransTime;
    }

    public void setFirstTransTime(String firstTransTime) {
        this.firstTransTime = firstTransTime;
    }

    public String getLastTransDate() {
        return lastTransDate;
    }

    public void setLastTransDate(String lastTransDate) {
        this.lastTransDate = lastTransDate;
    }

    public String getLastTransTime() {
        return lastTransTime;
    }

    public void setLastTransTime(String lastTransTime) {
        this.lastTransTime = lastTransTime;
    }

    public JSONObject buildsObjectJSON() {
        JSONObject request = new JSONObject();

        try {
            //Request
            request.put(FIRST_TRANS_DATE, getFirstTransDate());
            request.put(FIRST_TRANS_TIME, getFirstTransTime());
            request.put(LAST_TRANS_DATE, getLastTransDate());
            request.put(LAST_TRANS_TIME, getLastTransTime());

        } catch (JSONException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " reqResumeTrans " +  e.getMessage());
            return null;
        }

        Logger.debug("==JSON Request Resume Transactions==");
        Logger.debug(String.valueOf(request));
        Logger.debug("====================");

        return request;
    }
}
