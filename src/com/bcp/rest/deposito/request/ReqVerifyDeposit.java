package com.bcp.rest.deposito.request;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ReqVerifyDeposit extends JsonUtil {

    //REQUEST
    private static final String PERSON = "person";
    private static final String DOCUMENTTYPE = "documentType";
    private static final String DOCUMENTNUMBER = "documentNumber";
    private static final String ACCOUNT = "account";
    private static final String FAMILYCODE = "familyCode";
    private static final String CURRENCYCODE = "currencyCode";
    private static final String CARD = "card";
    private static final String TRACK2 = "track2";
    private static final String PINBLOCK = "pinblock";
    private static final String AMOUNT = "amount";

    private String ctnDocumentType;
    private String ctnDocumentNumber;
    private String famCode;
    private String currCode;
    private String ctnTrack2;
    private String ctnPinblock;
    private String ctnAmount;

    public String getCtnDocumentType() {
        return ctnDocumentType;
    }

    public void setCtnDocumentType(String ctnDocumentType) {
        this.ctnDocumentType = ctnDocumentType;
    }

    public String getCtnDocumentNumber() {
        return ctnDocumentNumber;
    }

    public void setCtnDocumentNumber(String ctnDocumentNumber) {
        this.ctnDocumentNumber = ctnDocumentNumber;
    }

    public String getFamCode() {
        return famCode;
    }

    public void setFamCode(String famCode) {
        this.famCode = famCode;
    }

    public String getCurrCode() {
        return currCode;
    }

    public void setCurrCode(String currCode) {
        this.currCode = currCode;
    }

    public String getTrack2() {
        return ctnTrack2;
    }

    public void setTrack2(String ctnTrack2) {
        this.ctnTrack2 = ctnTrack2;
    }

    public String getCtnPinblock() {
        return ctnPinblock;
    }

    public void setCtnPinblock(String ctnPinblock) {
        this.ctnPinblock = ctnPinblock;
    }

    private String getCtnAmount() {
        return ctnAmount;
    }

    public void setCtnAmount(String ctnAmount) {
        this.ctnAmount = ctnAmount;
    }

    public JSONObject buildsObjectJSON(){
        JSONObject request = new JSONObject();
        JSONObject jsonAccount = new JSONObject();
        JSONObject jsonPerson = new JSONObject();
        JSONObject jsonCard = new JSONObject();

        try {
            //PERSON

            if (getCtnDocumentNumber()!=null) {
                jsonPerson.put(DOCUMENTTYPE, getCtnDocumentType());
                jsonPerson.put(DOCUMENTNUMBER, getCtnDocumentNumber());
                request.put(PERSON, jsonPerson);
            }else{
                //ACOUNT
                jsonAccount.put(FAMILYCODE, getFamCode());
                jsonAccount.put(CURRENCYCODE, getCurrCode());
                request.put(ACCOUNT, jsonAccount);
            }

            //CARD ESTABLECIMIENTO
            jsonCard.put(TRACK2, getTrack2());

            jsonCard.put(PINBLOCK, getCtnPinblock());

            //REQUEST
            request.put(CARD, jsonCard);
            request.put(AMOUNT, getCtnAmount());

        } catch (JSONException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " ReqVerifyDeposit " +  e.getMessage());
            return null;
        }

        Logger.debug("==JSON Request Verify Deposit==");
        Logger.debug(String.valueOf(request));
        Logger.debug("====================");

        return request;
    }
}
