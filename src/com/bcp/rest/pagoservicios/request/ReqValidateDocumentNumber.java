package com.bcp.rest.pagoservicios.request;

import com.newpos.libpay.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class ReqValidateDocumentNumber {

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
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            return null;
        }

        Logger.debug("==JSON Request ValidateDocument==");
        Logger.debug(String.valueOf(requestObject));
        Logger.debug("====================");

        return requestObject;
    }
}
