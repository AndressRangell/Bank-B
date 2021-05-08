package com.bcp.rest.giros.emision.request;
import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ReqObtainPersonEmision extends JsonUtil {

    private static final String PERSON = "person";
    private static final String ACTOR = "actor";
    private static final String DOCUMENT_TYPE = "documentType";
    private static final String DOCUMENTNUMBER = "documentNumber";


    private String reqactor;
    private String reqdocumentType;
    private String reqdocumentNumber;


    public String getReqactor() {
        return reqactor;
    }

    public void setReqactor(String reqactor) {
        this.reqactor = reqactor;
    }

    public String getReqdocumentType() {
        return reqdocumentType;
    }

    public void setReqdocumentType(String reqdocumentType) {
        this.reqdocumentType = reqdocumentType;
    }

    public String getReqdocumentNumber() {
        return reqdocumentNumber;
    }

    public void setReqdocumentNumber(String reqdocumentNumber) {
        this.reqdocumentNumber = reqdocumentNumber;
    }

    public JSONObject buildsObjectJSON(){

        JSONObject jsonRequest = new JSONObject();
        JSONObject jsonActor = new JSONObject();

        try {
            //PERSON
            jsonActor.put(ACTOR, getReqactor());
            jsonActor.put(DOCUMENT_TYPE, getReqdocumentType());
            jsonActor.put(DOCUMENTNUMBER, jweEncryptDecrypt(getReqdocumentNumber() + "",false,false) ? jweDataEncryptDecrypt.getDataEncrypt() : "");


            jsonRequest.put(PERSON, jsonActor);

        } catch (JSONException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            return null;
        }

        Logger.debug("==JSON Request Obtain Document==");
        Logger.debug(String.valueOf(jsonRequest));
        Logger.debug("====================");

        return jsonRequest;
    }
}
