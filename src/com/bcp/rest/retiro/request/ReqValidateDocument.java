package com.bcp.rest.retiro.request;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class ReqValidateDocument extends JsonUtil {

    private static final String PERSON = "person";
    private static final String DOC_NUMBER = "documentNumber";

    private String docNumber;

    private String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public JSONObject buildsObjectJSON(){
        JSONObject requestObject = new JSONObject();
        JSONObject docNumberObject = new JSONObject();

        try{
            docNumberObject.put(DOC_NUMBER, getDocNumber());
            requestObject.put(PERSON, docNumberObject);

        } catch (JSONException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " ReqValidateDocument " +  e.getMessage());
            return null;
        }

        Logger.debug("==JSON Request ValidateDocument==");
        Logger.debug(String.valueOf(requestObject));
        Logger.debug("====================");

        return requestObject;
    }
}
