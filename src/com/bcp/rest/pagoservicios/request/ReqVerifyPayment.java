package com.bcp.rest.pagoservicios.request;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;
import com.newpos.libpay.trans.Trans;

import org.json.JSONObject;

public class ReqVerifyPayment extends JsonUtil {

    private static final String DEBTCODE = "debtCode";
    private static final String AMOUNT = "amount";
    private static final String AFFILIATIONCODE = "affiliationCode";
    private static final String CLIENTDEPOSITCODE = "clientDepositCode";
    private static final String ACCOUNT = "account";
    private static final String FAMILYCODE = "familyCode";
    private static final String CURRENCYCODE = "currencyCode";
    private static final String PAYMENTTYPE = "paymentType";
    private static final String IMPORTCODE = "importCode";


    private String ctnDebtcode;
    private String ctnAmount;
    private String ctnAffiliationCode;
    private String ctnClientDepositCode;
    private String ctnFamilyCode;
    private String ctnCurrencyCode;
    private String ctnPaymentType;
    private String ctnImportCode;

    public String getDebtcode() {
        return ctnDebtcode;
    }

    public void setDebtcode(String debtcode) {
        this.ctnDebtcode = debtcode;
    }

    public String getCtnAmount() {
        return ctnAmount;
    }

    public void setCtnAmount(String ctnAmount) {
        this.ctnAmount = ctnAmount;
    }

    public String getCtnAffiliationCode() {
        return ctnAffiliationCode;
    }

    public void setCtnAffiliationCode(String ctnAffiliationCode) {
        this.ctnAffiliationCode = ctnAffiliationCode;
    }

    public String getCtnClientDepositCode() {
        return ctnClientDepositCode;
    }

    public void setCtnClientDepositCode(String ctnClientDepositCode) {
        this.ctnClientDepositCode = ctnClientDepositCode;
    }

    public String getCtnFamilyCode() {
        return ctnFamilyCode;
    }

    public void setCtnFamilyCode(String ctnFamilyCode) {
        this.ctnFamilyCode = ctnFamilyCode;
    }

    public String getCtnCurrencyCode() {
        return ctnCurrencyCode;
    }

    public void setCtnCurrencyCode(String ctnCurrencyCode) {
        this.ctnCurrencyCode = ctnCurrencyCode;
    }

    public String getCtnPaymentType() {
        return ctnPaymentType;
    }

    public void setCtnPaymentType(String ctnPaymentType) {
        this.ctnPaymentType = ctnPaymentType;
    }

    public String getCtnImportCode() {
        return ctnImportCode;
    }

    public void setCtnImportCode(String ctnImportCode) {
        this.ctnImportCode = ctnImportCode;
    }

    public JSONObject buildsJsonObject(String typePaymetService){

        JSONObject jsonRequest = new JSONObject();
        JSONObject jsonAccount = new JSONObject();

        try {

            switch (typePaymetService){
                case Trans.PAGO_SINVALIDACION:
                    jsonRequest.put(AMOUNT,getCtnAmount());
                    jsonRequest.put(AFFILIATIONCODE,getCtnAffiliationCode());
                    jsonRequest.put(CLIENTDEPOSITCODE,jweEncryptDecrypt(getCtnClientDepositCode(),false,false) ? jweDataEncryptDecrypt.getDataEncrypt() : "");
                    break;
                case Trans.PAGO_CONRANGO:
                    jsonRequest.put(DEBTCODE,getDebtcode());
                    jsonRequest.put(PAYMENTTYPE,getCtnPaymentType());
                    if (getCtnAmount() != null)
                        jsonRequest.put(AMOUNT,getCtnAmount());
                    break;
                case Trans.PAGO_IMPORTE:
                    jsonRequest.put(IMPORTCODE,getCtnImportCode());
                    jsonRequest.put(CLIENTDEPOSITCODE,jweEncryptDecrypt(getCtnClientDepositCode(),false,false) ? jweDataEncryptDecrypt.getDataEncrypt() : "");
                    break;
                case Trans.PAGO_PARCIAL:
                    jsonRequest.put(AMOUNT,getCtnAmount());
                    break;
                case Trans.PAGO_PASEMOVISTAR:
                case Trans.PAGO_SINRANGO:
                    jsonRequest.put(DEBTCODE,getDebtcode());
                    break;
                default:
                    break;
            }

            if (getCtnFamilyCode() != null){
                jsonAccount.put(FAMILYCODE,getCtnFamilyCode());
                jsonAccount.put(CURRENCYCODE,getCtnCurrencyCode());
                jsonRequest.put(ACCOUNT,jsonAccount);
            }

        } catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }

        return jsonRequest;
    }
}
