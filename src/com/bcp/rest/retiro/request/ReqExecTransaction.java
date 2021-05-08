package com.bcp.rest.retiro.request;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ReqExecTransaction extends JsonUtil {

    //REQUEST
    private static final String ACCOUNT = "account";
    private static final String FAMILYCODE = "familyCode";
    private static final String CURRENCYCODE = "currencyCode";
    private static final String CUSTOMERCARD = "customerCard";
    private static final String PINBLOCK = "pinblock";
    private static final String FIELD55 = "field55";
    private static final String ARQC = "ARQC";
    private static final String PARTNERCARD = "partnerCard";
    private static final String CARDID = "cardId";
    private static final String AMOUNT = "amount";
    private static final String ENCRYPTEDIDC = "encryptedIdc";

    private String famCode;
    private String currCode;
    private String ctnPinblock;
    private String ctnField55;
    private String ctnArqc;
    private String ctnCardId;
    private String ctnAmount;
    private String encryptedIdc;

    private String getFamCode() {
        return famCode;
    }

    public void setFamCode(String famCode) {
        this.famCode = famCode;
    }

    private String getCurrCode() {
        return currCode;
    }

    public void setCurrCode(String currCode) {
        this.currCode = currCode;
    }

    private String getCtnPinblock() {
        return ctnPinblock;
    }

    public void setCtnPinblock(String ctnPinblock) {
        this.ctnPinblock = ctnPinblock;
    }

    private String getCtnField55() {
        return ctnField55;
    }

    public void setCtnField55(String ctnField55) {
        this.ctnField55 = ctnField55;
    }

    private String getCtnArqc() {
        return ctnArqc;
    }

    public void setCtnArqc(String ctnArqc) {
        this.ctnArqc = ctnArqc;
    }

    private String getCtnCardId() {
        return ctnCardId;
    }

    public void setCtnCardId(String ctnCardId) {
        this.ctnCardId = ctnCardId;
    }

    private String getCtnAmount() {
        return ctnAmount;
    }

    public void setCtnAmount(String ctnAmount) {
        this.ctnAmount = ctnAmount;
    }

    public void setEncryptedIdc(String encryptedIdc) {
        this.encryptedIdc = encryptedIdc;
    }

    public JSONObject buildsObjectJSON(){
        JSONObject jsonRequest = new JSONObject();
        JSONObject jsonAccount = new JSONObject();
        JSONObject jsonCustomerCard = new JSONObject();
        JSONObject jsonPartnerCard = new JSONObject();

        try {
            //ACOUNT
            jsonAccount.put(FAMILYCODE, getFamCode());
            jsonAccount.put(CURRENCYCODE, getCurrCode());

            //CUSTOMERCARD
            jsonCustomerCard.put(PINBLOCK, getCtnPinblock());
            jsonCustomerCard.put(FIELD55, getCtnField55());
            jsonCustomerCard.put(ARQC, getCtnArqc());

            //PARTNERCARD
            jsonPartnerCard.put(CARDID, getCtnCardId());

            //REQUEST
            jsonRequest.put(ACCOUNT, jsonAccount);
            jsonRequest.put(CUSTOMERCARD, jsonCustomerCard);

            if (encryptedIdc!=null)
                jsonRequest.put(ENCRYPTEDIDC, encryptedIdc);
            jsonRequest.put(PARTNERCARD, jsonPartnerCard);
            jsonRequest.put(AMOUNT, getCtnAmount());

        } catch (JSONException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " ReqExectrans 1 " +  e.getMessage());
            return null;
        }

        Logger.debug("==JSON Request List Products==");
        Logger.debug(String.valueOf(jsonRequest));
        Logger.debug("====================");

        return jsonRequest;
    }

}
