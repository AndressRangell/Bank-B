package com.bcp.rest.giros.emision.request;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ReqVerifyBankEmision extends JsonUtil {

    private static final String OPERATION = "operation";
    private static final String CURRENCYCODE = "currencyCode";
    private static final String AMOUNT = "amount";
    private static final String REMITTERACOUNT =  "remitterAccount";
    private static final String FAMILYCODE = "familyCode";
    private static final String PARTNERCARD = "partnerCard";
    private static final String TRACK2 = "track2";
    private static final String REMITTER = "remitter";
    private static final String NAME = "name";
    private static final String SENDER = "sender";
    private static final String BENEFICIARY = "beneficiary";


    private String reqCurrencyCode;
    private String reqCurrencyCodeAccount;
    private String reqAmount;
    private String reqFamilyCode;
    private String reqTrack2;
    private String reqName;
    private String reqNameSender;
    private String reqNameBeneficiary;
    private boolean isRemitter;
    private boolean isSender;
    private boolean isBeneficiary;


    public String getReqCurrencyCode() {
        return reqCurrencyCode;
    }

    public void setReqCurrencyCode(String reqCurrencyCode) {
        this.reqCurrencyCode = reqCurrencyCode;
    }

    public String getReqCurrencyCodeAccount() {
        return reqCurrencyCodeAccount;
    }

    public void setReqCurrencyCodeAccount(String reqCurrencyCodeAccount) {
        this.reqCurrencyCodeAccount = reqCurrencyCodeAccount;
    }

    public String getReqAmount() {
        return reqAmount;
    }

    public void setReqAmount(String reqAmount) {
        this.reqAmount = reqAmount;
    }

    public String getReqFamilyCode() {
        return reqFamilyCode;
    }

    public void setReqFamilyCode(String reqFamilyCode) {
        this.reqFamilyCode = reqFamilyCode;
    }

    public String getReqTrack2() {
        return reqTrack2;
    }

    public void setReqTrack2(String reqTrack2) {
        this.reqTrack2 = reqTrack2;
    }

    public String getReqName() {
        return reqName;
    }

    public void setReqName(String reqName) {
        this.reqName = reqName;
    }

    public String getReqNameSender() {
        return reqNameSender;
    }

    public void setReqNameSender(String reqNameSender) {
        this.reqNameSender = reqNameSender;
    }

    public String getReqNameBeneficiary() {
        return reqNameBeneficiary;
    }

    public void setReqNameBeneficiary(String reqNameBeneficiary) {
        this.reqNameBeneficiary = reqNameBeneficiary;
    }

    public void setRemitter(boolean remitter) {
        isRemitter = remitter;
    }

    public void setSender(boolean sender) {
        isSender = sender;
    }

    public void setBeneficiary(boolean beneficiary) {
        isBeneficiary = beneficiary;
    }

    public JSONObject buildsObjectJSON(){

        JSONObject jsonRequest = new JSONObject();
        JSONObject jsonOperation = new JSONObject();
        JSONObject jsonRemitterAccount = new JSONObject();
        JSONObject jsonPartnerCard = new JSONObject();
        JSONObject jsonRemitter = new JSONObject();
        JSONObject jsonSender = new JSONObject();
        JSONObject jsonBeneficiary = new JSONObject();

        try {

            //operation
            jsonOperation.put(CURRENCYCODE,getReqCurrencyCode());
            jsonOperation.put(AMOUNT ,getReqAmount());

            jsonRequest.put(OPERATION,jsonOperation);
            if (getReqTrack2() != null && !getReqTrack2().equals("")){
                //partnerCard
                jsonPartnerCard.put(TRACK2,jweEncryptDecrypt(getReqTrack2() + "",false,false) ? jweDataEncryptDecrypt.getDataEncrypt() : "");
                jsonRequest.put(PARTNERCARD,jsonPartnerCard);
                if ((getReqNameSender() != null && getReqName() != null) && !getReqNameSender().equals(getReqName())){
                    if (!isRemitter && !getReqName().equals("")){
                        //remitter
                        jsonRemitter.put(NAME,jweEncryptDecrypt(getReqName() + "",false,false) ? jweDataEncryptDecrypt.getDataEncrypt() : "");
                        jsonRequest.put(REMITTER,jsonRemitter);
                    }
                }

                if (!isSender && getReqNameSender() != null && !getReqNameSender().equals("")){
                    //sender
                    jsonSender.put(NAME,jweEncryptDecrypt(getReqNameSender() + "",false,false) ? jweDataEncryptDecrypt.getDataEncrypt() : "");
                    jsonRequest.put(SENDER,jsonSender);
                }
            } else {
                //remitterAccount
                jsonRemitterAccount.put(FAMILYCODE,getReqFamilyCode());
                jsonRemitterAccount.put(CURRENCYCODE,getReqCurrencyCodeAccount());

                jsonRequest.put(REMITTERACOUNT,jsonRemitterAccount);
            }

            if (!isBeneficiary && (getReqNameBeneficiary() != null && !getReqNameBeneficiary().equals(""))){
                //beneficiary
                jsonBeneficiary.put(NAME,jweEncryptDecrypt(getReqNameBeneficiary() + "",false,false)  ? jweDataEncryptDecrypt.getDataEncrypt() : "");

                jsonRequest.put(BENEFICIARY,jsonBeneficiary);
            }

        }catch (JSONException e){
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            return null;
        }

        Logger.debug("==JSON ExecuteTrans ==");
        Logger.debug(String.valueOf(jsonRequest));
        Logger.debug("====================");

        return jsonRequest;
    }
}
