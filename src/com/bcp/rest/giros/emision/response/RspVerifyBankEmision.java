package com.bcp.rest.giros.emision.response;

import com.bcp.rest.JsonUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class RspVerifyBankEmision extends JsonUtil {

    private static final String BENEFICIARY = "beneficiary";
    private static final String NAME = "name";
    private static final String DOCUMENT_TYPEDESCRIPTION = "documentTypeDescription";
    private static final String DOCUMENTNUMBER = "documentNumber";
    private static final String EDITABLE = "editable";
    private static final String REMITTER = "remitter";
    private static final String SENDER = "sender";
    private static final String OPERATION = "operation";
    private static final String COMMISSIONCURRENCYSYMBOL = "commissionCurrencySymbol";
    private static final String COMISSION_AMOUNT = "commissionAmount";
    private static final String EXCHANGE = "exchange";
    private static final String TOTALCURRENCY_SYMBOL = "totalCurrencySymbol";
    private static final String TOTAL = "total";
    private static final String EXCHANGE_RATE = "exchangeRate";
    private static final String TOTALAMOUNTCURRENCYSYMBOL = "totalAmountCurrencySymbol";
    private static final String TOTALAMOUNT = "totalAmount";
    private static final String EDITABLE_OPERATION = "editable";

    private String rspBeneficiary;
    private String rspNameBenef;
    private String rspDocumentTypeDesBenef;
    private String rspDocumentNumberBenef;
    private String rspEditableBenef;
    private String rspRemitter;
    private String rspNameRemitter;
    private String rspDocumentTypeDesRemitter;
    private String rspDocumentNumber_Remitter;
    private String rspEditableRemitter;
    private String rspSend;
    private String rspNameSender;
    private String rspDocumentTypeDes_Sender;
    private String rspDocumentNumberSender;
    private String rspEditableSender;
    private String rspOperation;
    private String rspCommissionCurrencySymbol;
    private String rsCommissionAmount;
    private String rspExchange;
    private String rspTotalCurrencySymbol;
    private String rspTotal;
    private String rspExchangeRate;
    private String rspTotalAmountCurrencySymbol;
    private String rspTotalAmount;
    private String rspEditable;


    public String getRspBeneficiary() {
        return rspBeneficiary;
    }

    public void setRspBeneficiary(String rspBeneficiary) {
        this.rspBeneficiary = rspBeneficiary;
    }

    public String getRspNameBenef() {
        return rspNameBenef;
    }

    public void setRspNameBenef(String rspNameBenef) {
        this.rspNameBenef = rspNameBenef;
    }

    public String getRspDocumentTypeDesBenef() {
        return rspDocumentTypeDesBenef;
    }

    public void setRspDocumentTypeDesBenef(String rspDocumentTypeDesBenef) {
        this.rspDocumentTypeDesBenef = rspDocumentTypeDesBenef;
    }

    public String getRspDocumentNumberBenef() {
        return rspDocumentNumberBenef;
    }

    public void setRspDocumentNumberBenef(String rspDocumentNumberBenef) {
        this.rspDocumentNumberBenef = rspDocumentNumberBenef;
    }

    public String getRspEditableBenef() {
        return rspEditableBenef;
    }

    public void setRspEditableBenef(String rspEditableBenef) {
        this.rspEditableBenef = rspEditableBenef;
    }

    public String getRspRemitter() {
        return rspRemitter;
    }

    public void setRspRemitter(String rspRemitter) {
        this.rspRemitter = rspRemitter;
    }

    public String getRspNameRemitter() {
        return rspNameRemitter;
    }

    public void setRspNameRemitter(String rspNameRemitter) {
        this.rspNameRemitter = rspNameRemitter;
    }

    public String getRspDocumentTypeDesRemitter() {
        return rspDocumentTypeDesRemitter;
    }

    public void setRspDocumentTypeDesRemitter(String rspDocumentTypeDesRemitter) {
        this.rspDocumentTypeDesRemitter = rspDocumentTypeDesRemitter;
    }

    public String getRspDocumentNumber_Remitter() {
        return rspDocumentNumber_Remitter;
    }

    public void setRspDocumentNumber_Remitter(String rspDocumentNumber_Remitter) {
        this.rspDocumentNumber_Remitter = rspDocumentNumber_Remitter;
    }

    public String getRspEditableRemitter() {
        return rspEditableRemitter;
    }

    public void setRspEditableRemitter(String rspEditableRemitter) {
        this.rspEditableRemitter = rspEditableRemitter;
    }

    public String getRspSend() {
        return rspSend;
    }

    public void setRspSend(String rspSend) {
        this.rspSend = rspSend;
    }

    public String getRspNameSender() {
        return rspNameSender;
    }

    public void setRspNameSender(String rspNameSender) {
        this.rspNameSender = rspNameSender;
    }

    public String getRspDocumentTypeDes_Sender() {
        return rspDocumentTypeDes_Sender;
    }

    public void setRspDocumentTypeDes_Sender(String rspDocumentTypeDes_Sender) {
        this.rspDocumentTypeDes_Sender = rspDocumentTypeDes_Sender;
    }

    public String getRspDocumentNumberSender() {
        return rspDocumentNumberSender;
    }

    public void setRspDocumentNumberSender(String rspDocumentNumberSender) {
        this.rspDocumentNumberSender = rspDocumentNumberSender;
    }

    public String getRspEditableSender() {
        return rspEditableSender;
    }

    public void setRspEditableSender(String rspEditableSender) {
        this.rspEditableSender = rspEditableSender;
    }

    public String getRspOperation() {
        return rspOperation;
    }

    public void setRspOperation(String rspOperation) {
        this.rspOperation = rspOperation;
    }

    public String getRspCommissionCurrencySymbol() {
        return rspCommissionCurrencySymbol;
    }

    public void setRspCommissionCurrencySymbol(String rspCommissionCurrencySymbol) {
        this.rspCommissionCurrencySymbol = rspCommissionCurrencySymbol;
    }

    public String getRsCommissionAmount() {
        return rsCommissionAmount;
    }

    public void setRsCommissionAmount(String rsCommissionAmount) {
        this.rsCommissionAmount = rsCommissionAmount;
    }

    public String getRspExchange() {
        return rspExchange;
    }

    public void setRspExchange(String rspExchange) {
        this.rspExchange = rspExchange;
    }

    public String getRspTotalCurrencySymbol() {
        return rspTotalCurrencySymbol;
    }

    public void setRspTotalCurrencySymbol(String rspTotalCurrencySymbol) {
        this.rspTotalCurrencySymbol = rspTotalCurrencySymbol;
    }

    public String getRspTotal() {
        return rspTotal;
    }

    public void setRspTotal(String rspTotal) {
        this.rspTotal = rspTotal;
    }

    public String getRspExchangeRate() {
        return rspExchangeRate;
    }

    public void setRspExchangeRate(String rspExchangeRate) {
        this.rspExchangeRate = rspExchangeRate;
    }

    public String getRspTotalAmountCurrencySymbol() {
        return rspTotalAmountCurrencySymbol;
    }

    public void setRspTotalAmountCurrencySymbol(String rspTotalAmountCurrencySymbol) {
        this.rspTotalAmountCurrencySymbol = rspTotalAmountCurrencySymbol;
    }

    public String getRspTotalAmount() {
        return rspTotalAmount;
    }

    public void setRspTotalAmount(String rspTotalAmount) {
        this.rspTotalAmount = rspTotalAmount;
    }

    public String getRspEditable() {
        return rspEditable;
    }

    public void setRspEditable(String rspEditable) {
        this.rspEditable = rspEditable;
    }

    public boolean getRspVerifyBank(JSONObject json) throws JSONException {

        if(JsonUtil.validatedNull(json, BENEFICIARY))
            setRspBeneficiary(json.getString(BENEFICIARY));
        else
            return false;

        if(!getRspVerifyBeneficiary()){
            return false;
        }

        if(JsonUtil.validatedNull(json, REMITTER))
            setRspRemitter(json.getString(REMITTER));
        else
            return false;

        if(!getRspVerifyRemitter()){
            return false;
        }

        if(JsonUtil.validatedNull(json, SENDER))
            setRspSend(json.getString(SENDER));
        else
            return false;

        if(!getRspSender()){
            return false;
        }

        if(JsonUtil.validatedNull(json, OPERATION))
            setRspOperation(json.getString(OPERATION));
        else
            return false;

        return getRspOperat();
    }

    private boolean getRspVerifyBeneficiary() throws JSONException {
        //OBJETO BENEFICIARY
        JSONObject jsonBeneficiary = new JSONObject(getRspBeneficiary());

        if(JsonUtil.validatedNull(jsonBeneficiary, NAME)){
            setRspNameBenef(jweEncryptDecrypt(jsonBeneficiary.getString(NAME) + "",false, true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
        }else
            return false;

        if(JsonUtil.validatedNull(jsonBeneficiary, DOCUMENT_TYPEDESCRIPTION)){
            setRspDocumentTypeDesBenef(jsonBeneficiary.getString(DOCUMENT_TYPEDESCRIPTION));
        }else
            return false;

        if(JsonUtil.validatedNull(jsonBeneficiary, DOCUMENTNUMBER)){
            setRspDocumentNumberBenef(jweEncryptDecrypt(jsonBeneficiary.getString(DOCUMENTNUMBER) + "",false, true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
        }else
            return false;

        if(JsonUtil.validatedNull(jsonBeneficiary, EDITABLE)){
            setRspEditableBenef(jsonBeneficiary.getString(EDITABLE));
        }else
            return false;

        return true;
    }

    private boolean getRspVerifyRemitter() throws JSONException {
        //OBJETO REMITTER
        JSONObject jsonRemitter = new JSONObject(getRspRemitter());


        if(JsonUtil.validatedNull(jsonRemitter, NAME)){
            setRspNameRemitter(jweEncryptDecrypt(jsonRemitter.getString(NAME) + "",false, true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
        }else
            return false;

        if(JsonUtil.validatedNull(jsonRemitter, DOCUMENT_TYPEDESCRIPTION)){
            setRspDocumentTypeDesRemitter(jsonRemitter.getString(DOCUMENT_TYPEDESCRIPTION));
        }else
            return false;

        if(JsonUtil.validatedNull(jsonRemitter, DOCUMENTNUMBER)){
            setRspDocumentNumber_Remitter(jweEncryptDecrypt(jsonRemitter.getString(DOCUMENTNUMBER) + "",false, true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
        }else
            return false;

        if(JsonUtil.validatedNull(jsonRemitter, EDITABLE)){
            setRspEditableRemitter(jsonRemitter.getString(EDITABLE));
        }else
            return false;

        return true;
    }

    private boolean getRspSender() throws JSONException {

        //OBJETO SENDER
        JSONObject jsonRemitter = new JSONObject(getRspSend());


        if(JsonUtil.validatedNull(jsonRemitter, NAME)){
            setRspNameSender(jweEncryptDecrypt(jsonRemitter.getString(NAME) + "",false, true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
        }else
            return false;

        if(JsonUtil.validatedNull(jsonRemitter, DOCUMENT_TYPEDESCRIPTION)){
            setRspDocumentTypeDes_Sender(jsonRemitter.getString(DOCUMENT_TYPEDESCRIPTION));
        }else
            return false;

        if(JsonUtil.validatedNull(jsonRemitter, DOCUMENTNUMBER)){
            setRspDocumentNumberSender(jweEncryptDecrypt(jsonRemitter.getString(DOCUMENTNUMBER) + "",false, true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
        }else
            return false;

        if(JsonUtil.validatedNull(jsonRemitter, EDITABLE)){
            setRspEditableSender(jsonRemitter.getString(EDITABLE));
        }else
            return false;

        return true;
    }

    private boolean getRspOperat() throws JSONException {

        //OBJETO OPERATION
        JSONObject jsonOperation = new JSONObject(getRspOperation());


        if(JsonUtil.validatedNull(jsonOperation, COMMISSIONCURRENCYSYMBOL)){
            setRspCommissionCurrencySymbol(jsonOperation.getString(COMMISSIONCURRENCYSYMBOL));
        }else
            return false;

        if(JsonUtil.validatedNull(jsonOperation, COMISSION_AMOUNT)){
            setRsCommissionAmount(jsonOperation.getString(COMISSION_AMOUNT));
        }else
            return false;

        if(JsonUtil.validatedNull(jsonOperation, EXCHANGE))
            setRspExchange(jsonOperation.getString(EXCHANGE));

        if(!getRspExeChange()){
            return false;
        }

        if(JsonUtil.validatedNull(jsonOperation, TOTALAMOUNTCURRENCYSYMBOL)){
            setRspTotalAmountCurrencySymbol(jsonOperation.getString(TOTALAMOUNTCURRENCYSYMBOL));
        }else
            return false;

        if(JsonUtil.validatedNull(jsonOperation, TOTALAMOUNT)){
            setRspTotalAmount(jsonOperation.getString(TOTALAMOUNT));
        }else
            return false;

        if(JsonUtil.validatedNull(jsonOperation, EDITABLE_OPERATION)){
            setRspEditable(jsonOperation.getString(EDITABLE_OPERATION));
        }else
            return false;

        return true;
    }

    private boolean getRspExeChange() throws JSONException {

        if (getRspExchange() != null && !getRspExchange().equals("")){
            //OBJETO EXCHANGE
            JSONObject jsonExchange = new JSONObject(getRspExchange());

            if(JsonUtil.validatedNull(jsonExchange, TOTALCURRENCY_SYMBOL)){
                setRspTotalCurrencySymbol(jsonExchange.getString(TOTALCURRENCY_SYMBOL));
            }else
                return false;

            if(JsonUtil.validatedNull(jsonExchange, TOTAL)){
                setRspTotal(jsonExchange.getString(TOTAL));
            }else
                return false;

            if(JsonUtil.validatedNull(jsonExchange, EXCHANGE_RATE)){
                setRspExchangeRate(jsonExchange.getString(EXCHANGE_RATE));
            }else
                return false;
        }
        return true;
    }
}
