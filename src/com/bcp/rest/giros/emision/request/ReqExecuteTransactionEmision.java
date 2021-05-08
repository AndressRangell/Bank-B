package com.bcp.rest.giros.emision.request;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class ReqExecuteTransactionEmision extends JsonUtil {

    private static final String OPERATION = "operation";
    private static final String BANKDRAFTPASSENCRYPTED = "bankDraftPassEncrypted";
    private static final String CHARGECARD = "chargeCard";
    private static final String PINBLOCKENCRYPTED = "pinblockEncrypted";
    private static final String FIELD55 = "field55";
    private static final String ARQC = "ARQC";


    private String reqBankDraftPassEncrypted;
    private String reqPinblockEncrypted;
    private String reqField55;
    private String reqARQC;


    public String getReqBankDraftPassEncrypted() {
        return reqBankDraftPassEncrypted;
    }

    public void setReqBankDraftPassEncrypted(String reqBankDraftPassEncrypted) {
        this.reqBankDraftPassEncrypted = reqBankDraftPassEncrypted;
    }


    public String getReqPinblockEncrypted() {
        return reqPinblockEncrypted;
    }

    public void setReqPinblockEncrypted(String reqPinblockEncrypted) {
        this.reqPinblockEncrypted = reqPinblockEncrypted;
    }

    public String getReqField55() {
        return reqField55;
    }

    public void setReqField55(String reqField55) {
        this.reqField55 = reqField55;
    }

    public String getReqARQC() {
        return reqARQC;
    }

    public void setReqARQC(String reqARQC) {
        this.reqARQC = reqARQC;
    }

    public JSONObject buildsObjectJSON(){

        String field55;
        String Arqc;
        JSONObject jsonRequest = new JSONObject();
        JSONObject jsonOperation = new JSONObject();
        JSONObject jsoChargeCard = new JSONObject();

        try {

            field55 = jweEncryptDecrypt(getReqField55()+"",false,false) ? jweDataEncryptDecrypt.getDataEncrypt(): "";
            Arqc = jweEncryptDecrypt(getReqARQC()+"",false,false) ? jweDataEncryptDecrypt.getDataEncrypt(): "";
            funcCipherDocument(getReqBankDraftPassEncrypted()+"");

            //operation
            jsonOperation.put(BANKDRAFTPASSENCRYPTED, cipherDocument.getDocument());

            jsoChargeCard.put(PINBLOCKENCRYPTED, getReqPinblockEncrypted());
            if (getReqField55() != null && !getReqField55().equals("")){
                jsoChargeCard.put(FIELD55, field55);
                jsoChargeCard.put(ARQC, Arqc);
            }

            jsonRequest.put(OPERATION, jsonOperation);
            jsonRequest.put(CHARGECARD, jsoChargeCard);

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
