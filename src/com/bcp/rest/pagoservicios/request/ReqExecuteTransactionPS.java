package com.bcp.rest.pagoservicios.request;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;

import org.json.JSONObject;

public class ReqExecuteTransactionPS extends JsonUtil {

    //PERSON
    private static final String PERSON = "person";
    private static final String DOCUMENTTYPE = "documentType";
    private static final String DOCUMENTNUMBER = "documentNumber";
    private static final String DOCUMENTCODE = "documentCode";

    //CUSTOMERCARD
    private static final String CUSTOMERCARD = "customerCard";
    private static final String PINBLOCK = "pinblock";
    private static final String FIELD55 = "field55";
    private static final String ARQC = "ARQC";

    //PARTNERCARD
    private static final String PARTNERCARD = "partnerCard";
    private static final String TRACK2 = "track2";

    //PAYMENTMETHOD
    private static final String PAYMENTMETHOD = "paymentMethod";

    private String documentType;
    private String documentNumber;
    private String paymentMethod;
    private String documentCode;

    private String ctnPinblock;
    private String ctnField55;
    private String ctnArqc;

    private String ctnTrack2;




    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCtnPinblock() {
        return ctnPinblock;
    }

    public void setCtnPinblock(String ctnPinblock) {
        this.ctnPinblock = ctnPinblock;
    }

    public String getCtnField55() {
        return ctnField55;
    }

    public void setCtnField55(String ctnField55) {
        this.ctnField55 = ctnField55;
    }

    public String getCtnArqc() {
        return ctnArqc;
    }

    public void setCtnArqc(String ctnArqc) {
        this.ctnArqc = ctnArqc;
    }

    public String getCtnTrack2() {
        return ctnTrack2;
    }

    public void setCtnTrack2(String ctnTrack2) {
        this.ctnTrack2 = ctnTrack2;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public JSONObject buildsObjectJSON(){
        JSONObject request = new JSONObject();
        JSONObject jsonPerson = new JSONObject();
        JSONObject jsonPartnerCard = new JSONObject();
        JSONObject jsonCustomerCard = new JSONObject();

        try {

            if (getPaymentMethod().equals("1")){
                if (getDocumentCode() != null)
                    jsonPerson.put(DOCUMENTCODE,getDocumentCode());
                jsonPerson.put(DOCUMENTTYPE, getDocumentType());
                jsonPerson.put(DOCUMENTNUMBER, jweEncryptDecrypt(getDocumentNumber(),false,false) ? jweDataEncryptDecrypt.getDataEncrypt() : "");
                request.put(PERSON, jsonPerson);

                if (getCtnTrack2() != null){
                    jsonPartnerCard.put(TRACK2,jweEncryptDecrypt(getCtnTrack2(),false,false) ? jweDataEncryptDecrypt.getDataEncrypt() : "");
                    jsonPartnerCard.put(PINBLOCK,getCtnPinblock());
                    request.put(PARTNERCARD,jsonPartnerCard);
                }
            }else {
                if (getCtnPinblock() != null)
                    jsonCustomerCard.put(PINBLOCK,getCtnPinblock());
                jsonCustomerCard.put(FIELD55,jweEncryptDecrypt(getCtnField55(),false,false) ? jweDataEncryptDecrypt.getDataEncrypt() : "");
                jsonCustomerCard.put(ARQC,jweEncryptDecrypt(getCtnArqc(),false,false) ? jweDataEncryptDecrypt.getDataEncrypt() : "");
                request.put(CUSTOMERCARD,jsonCustomerCard);
            }

            request.put(PAYMENTMETHOD, getPaymentMethod());

        } catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            return null;
        }

        return request;
    }
}
