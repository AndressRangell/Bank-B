package com.bcp.rest.giros.cobro_nacional.request;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class ReqExecuteTransaction extends JsonUtil {
    
    //REQUEST
    private static final String PAYMENTMETHOD = "paymentMethod";
    private static final String OPERATION = "operation";
    private static final String BANKDRAFTPASSENCRYPTED = "bankDraftPassEncrypted";
    private static final String BANKDRAFTREFERENCE = "bankDraftReference";
    private static final String PARTNERCARD = "partnerCard";
    private static final String TRACK2 = "track2";
    private static final String BENEFICIARYACCOUNT = "beneficiaryAccount";
    private static final String FAMILYCODE = "familyCode";
    private static final String CURRENCYCODE = "currencyCode";

    private String reqPaymentMethod;
    private String reqBankDraftPassEncrypted;
    private String reqBankDraftReference;
    private String reqTrack2;
    private String reqFamilyCode;
    private String reqCurrencyCode;

    public String getReqPaymentMethod() {
        return reqPaymentMethod;
    }

    public void setReqPaymentMethod(String reqPaymentMethod) {
        this.reqPaymentMethod = reqPaymentMethod;
    }

    public String getReqBankDraftPassEncrypted() {
        return reqBankDraftPassEncrypted;
    }

    public void setReqBankDraftPassEncrypted(String reqBankDraftPassEncrypted) {
        this.reqBankDraftPassEncrypted = reqBankDraftPassEncrypted;
    }

    public String getReqBankDraftReference() {
        return reqBankDraftReference;
    }

    public void setReqBankDraftReference(String reqBankDraftReference) {
        this.reqBankDraftReference = reqBankDraftReference;
    }

    public String getReqTrack2() {
        return reqTrack2;
    }

    public void setReqTrack2(String reqTrack2) {
        this.reqTrack2 = reqTrack2;
    }

    public String getReqFamilyCode() {
        return reqFamilyCode;
    }

    public void setReqFamilyCode(String reqFamilyCode) {
        this.reqFamilyCode = reqFamilyCode;
    }

    public String getReqCurrencyCode() {
        return reqCurrencyCode;
    }

    public void setReqCurrencyCode(String reqCurrencyCode) {
        this.reqCurrencyCode = reqCurrencyCode;
    }

    public JSONObject buildsObjectJSON(){

        JSONObject request = new JSONObject();
        JSONObject jsonOperation = new JSONObject();
        JSONObject jsonPartnerCard = new JSONObject();
        JSONObject jsonBeneficiaryAccount = new JSONObject();

        try {

            funcCipherDocument(getReqBankDraftPassEncrypted()+"");

            jsonOperation.put(BANKDRAFTPASSENCRYPTED,cipherDocument.getDocument());
            jsonOperation.put(BANKDRAFTREFERENCE,getReqBankDraftReference());

            jsonPartnerCard.put(TRACK2,getReqTrack2());

            jsonBeneficiaryAccount.put(FAMILYCODE,getReqFamilyCode());
            jsonBeneficiaryAccount.put(CURRENCYCODE,getReqCurrencyCode());

            request.put(PAYMENTMETHOD,getReqPaymentMethod());
            request.put(OPERATION,jsonOperation);
            if (getReqPaymentMethod().equals("1")){
                request.put(PARTNERCARD,jsonPartnerCard);
            }else {
                request.put(BENEFICIARYACCOUNT,jsonBeneficiaryAccount);
            }

        } catch (JSONException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " ReqExecuteTrans " +  e.getMessage());
            return null;
        }

        Logger.debug("==JSON Request Verify Deposit==");
        Logger.debug(String.valueOf(request));
        Logger.debug("====================");

        return request;
    }
}
