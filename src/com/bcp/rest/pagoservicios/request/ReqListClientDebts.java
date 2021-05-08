package com.bcp.rest.pagoservicios.request;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;

import org.json.JSONObject;

public class ReqListClientDebts extends JsonUtil {

    //REQUEST
    private static final String CLIENTDEPOSITCODE = "clientDepositCode";
    private static final String AFFILIATIONCODE = "affiliationCode";
    private static final String CUSTOMERCARD = "customerCard";
    private static final String PERSON = "person";

    //PARTNERCARD
    private static final String PARTNERCARD = "partnerCard";
    private static final String PINBLOCK = "pinblock";
    private static final String TRACK2 = "track2";
    private static final String FIELD55 = "field55";
    private static final String ARQC = "ARQC";
    private static final String ENCRYPTEDDOCUMENTNUMBER = "encryptedDocumentNumber";

    //REQUEST
    private String clientDepositCode;
    private String affiliationCode;

    //PARTNERCARD
    private String reqPinblock;
    private String reqTrack2;
    private String reqField55;
    private String reqArqc;
    private String reqEncryptedDocumentNumber;

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

    public String getReqPinblock() {
        return reqPinblock;
    }

    public void setReqPinblock(String reqPinblock) {
        this.reqPinblock = reqPinblock;
    }

    public String getReqTrack2() {
        return reqTrack2;
    }

    public void setReqTrack2(String reqTrack2) {
        this.reqTrack2 = reqTrack2;
    }

    public String getReqField55() {
        return reqField55;
    }

    public void setReqField55(String reqField55) {
        this.reqField55 = reqField55;
    }

    public String getReqArqc() {
        return reqArqc;
    }

    public void setReqArqc(String reqArqc) {
        this.reqArqc = reqArqc;
    }

    public String getReqEncryptedDocumentNumber() {
        return reqEncryptedDocumentNumber;
    }

    public void setReqEncryptedDocumentNumber(String reqEncryptedDocumentNumber) {
        this.reqEncryptedDocumentNumber = reqEncryptedDocumentNumber;
    }

    public JSONObject builJsonObject(){

        JSONObject jsonRequest = new JSONObject();
        JSONObject jsonPartnerCard = new JSONObject();
        JSONObject jsonCustomerCard = new JSONObject();
        JSONObject jsonPerson = new JSONObject();

        try {
            //REQUEST
            jsonRequest.put(CLIENTDEPOSITCODE, jweEncryptDecrypt(getClientDepositCode() + "",false,false) ? jweDataEncryptDecrypt.getDataEncrypt() : "");
            jsonRequest.put(AFFILIATIONCODE, getAffiliationCode());

            if (getReqTrack2() != null){
                jsonPartnerCard.put(TRACK2, jweEncryptDecrypt( getReqTrack2() + "",false,false) ? jweDataEncryptDecrypt.getDataEncrypt() : "");
                jsonPartnerCard.put(PINBLOCK,getReqPinblock());
                jsonRequest.put(PARTNERCARD,jsonPartnerCard);
            }

            if (getReqField55() != null){
                jsonCustomerCard.put(PINBLOCK,getReqPinblock());
                jsonCustomerCard.put(FIELD55,jweEncryptDecrypt(getReqField55() + "",false,false) ? jweDataEncryptDecrypt.getDataEncrypt() : "");
                jsonCustomerCard.put(ARQC,jweEncryptDecrypt(getReqArqc() + "",false,false) ? jweDataEncryptDecrypt.getDataEncrypt() : "");
                jsonRequest.put(CUSTOMERCARD,jsonCustomerCard);
            }

            if (getReqEncryptedDocumentNumber() != null){
                funcCipherDocument(getReqEncryptedDocumentNumber()+"");
                jsonPerson.put(ENCRYPTEDDOCUMENTNUMBER,cipherDocument.getDocument());
                jsonRequest.put(PERSON,jsonPerson);
            }

        } catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            return null;
        }

        return jsonRequest;
    }
}
