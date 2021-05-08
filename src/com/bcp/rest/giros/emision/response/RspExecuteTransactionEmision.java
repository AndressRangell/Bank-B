package com.bcp.rest.giros.emision.response;

import com.bcp.rest.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class RspExecuteTransactionEmision extends JsonUtil {

    private static final String TRANSACTIONDATE = "transactionDate";
    private static final String TRANSACTIONTIME = "transactionTime";
    private static final String TRANSACTION_NUMBER = "transactionNumber";
    private static final String REMITTERCARD = "remitterCard";
    private static final String ARPC = "ARPC";
    private static final String CARDID = "cardId";
    private static final String BANKDRAFTREFERENCE = "bankDraftReference";
    private static final String BENEFICIARY = "beneficiary";
    private static final String NAME = "name";
    private static final String DOCUMENT_TYPEDESCRIPTION = "documentTypeDescription";
    private static final String DOCUMENTNUMBER = "documentNumber";
    private static final String REMITTER = "remitter";
    private static final String SENDER = "sender";
    private static final String AMOUNTCURRENCYSYMBOL = "amountCurrencySymbol";
    private static final String AMOUNT = "amount";
    private static final String COMMISSIONCURRENCYSYMBOL = "commissionCurrencySymbol";
    private static final String COMMISSIONAMOUNT = "commissionAmount";
    private static final String EXCHANGE = "exchange";
    private static final String TOTALCURRENCYSYMBOL = "totalCurrencySymbol";
    private static final String TOTAL = "total";
    private static final String EXCHANGERATE = "exchangeRate";
    private static final String TOTALAMOUNTCURRENCYSYMBOL = "totalAmountCurrencySymbol";
    private static final String TOTALAMOUNT = "totalAmount";
    private static final String PAYMENTMETHOD = "paymentMethod";
    private static final String REMITTERACCOUNT = "remitterAccount";
    private static final String FAMILYDESCRYPTION = "familyDescription";
    private static final String CURRENCYSYMBOL = "currencySymbol";
    private static final String ACCOUNTID = "accountId";
    private static final String TOPRINT = "toPrint";
    private static final String LEAD_DESCRIPTION = "leadDescription";


    private String rspTransactionDate;
    private String rspTransactionTime;
    private String rspTransactionNumber;
    private String rspArpc;
    private String rspCardId;
    private String rspBankDraftReference;
    private String rspBeneficiary;
    private String rspNameBenef;
    private String rspDocumentTypeDescriptionBenef;
    private String rspDocumentNumberBenef;
    private String rspRemitterCard;
    private String rspNameRemitter;
    private String rspDocumentTypeDesRemitter;
    private String rspDocumentNumberRemitter;
    private String rspRemitter;
    private String rspSender;
    private String rspNameSender;
    private String rspDocumentTypeDeSender;
    private String rspDocumentNumberSender;
    private String rspAmountCurrencySymbol;
    private String rspRemitterAccount;
    private String rspAmount;
    private String rspCommissionCurrencySymbol;
    private String rspCommissionAmount;
    private String rspTotalCurrencySymbol;
    private String rspTotal;
    private String rspExchange;
    private String rspExchangeRate;
    private String rspTotalAmountCurrencySymbol;
    private String rspTotalAmount;
    private String rsp_PaymentMethod;
    private String rspFamilyDescription;
    private String rspCurrencyDescription;
    private String rspAccountId;
    private String rspToPrint;
    private String rspLeadDescription;

    public String getRspRemitterAccount() {
        return rspRemitterAccount;
    }

    public void setRspRemitterAccount(String rspRemitterAmount) {
        this.rspRemitterAccount = rspRemitterAmount;
    }

    public String getRspExchange() {
        return rspExchange;
    }

    public void setRspExchange(String rspExchange) {
        this.rspExchange = rspExchange;
    }

    public String getRspSender() {
        return rspSender;
    }

    public void setRspSender(String rspSender) {
        this.rspSender = rspSender;
    }

    public String getRspRemitter() {
        return rspRemitter;
    }

    public void setRspRemitter(String rspRemitter) {
        this.rspRemitter = rspRemitter;
    }

    public String getRspBeneficiary() {
        return rspBeneficiary;
    }

    public void setRspBeneficiary(String rspBeneficiary) {
        this.rspBeneficiary = rspBeneficiary;
    }

    public String getRspRemitterCard() {
        return rspRemitterCard;
    }

    public void setRspRemitterCard(String rspRemitterCard) {
        this.rspRemitterCard = rspRemitterCard;
    }

    public String getRspTransactionDate() {
        return rspTransactionDate;
    }

    public void setRspTransactionDate(String rspTransactionDate) {
        this.rspTransactionDate = rspTransactionDate;
    }

    public String getRspTransactionTime() {
        return rspTransactionTime;
    }

    public void setRspTransactionTime(String rspTransactionTime) {
        this.rspTransactionTime = rspTransactionTime;
    }

    public String getRspTransactionNumber() {
        return rspTransactionNumber;
    }

    public void setRspTransactionNumber(String rspTransactionNumber) {
        this.rspTransactionNumber = rspTransactionNumber;
    }

    public String getRspArpc() {
        return rspArpc;
    }

    public void setRspArpc(String rspArpc) {
        this.rspArpc = rspArpc;
    }

    public String getRspCardId() {
        return rspCardId;
    }

    public void setRspCardId(String rspCardId) {
        this.rspCardId = rspCardId;
    }

    public String getRspBankDraftReference() {
        return rspBankDraftReference;
    }

    public void setRspBankDraftReference(String rspBankDraftReference) {
        this.rspBankDraftReference = rspBankDraftReference;
    }

    public String getRspNameBenef() {
        return rspNameBenef;
    }

    public void setRspNameBenef(String rspNameBenef) {
        this.rspNameBenef = rspNameBenef;
    }

    public String getRspDocumentTypeDescriptionBenef() {
        return rspDocumentTypeDescriptionBenef;
    }

    public void setRspDocumentTypeDescriptionBenef(String rspDocumentTypeDescriptionBenef) {
        this.rspDocumentTypeDescriptionBenef = rspDocumentTypeDescriptionBenef;
    }

    public String getRspDocumentNumberBenef() {
        return rspDocumentNumberBenef;
    }

    public void setRspDocumentNumberBenef(String rspDocumentNumberBenef) {
        this.rspDocumentNumberBenef = rspDocumentNumberBenef;
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

    public String getRspDocumentNumberRemitter() {
        return rspDocumentNumberRemitter;
    }

    public void setRspDocumentNumberRemitter(String rspDocumentNumberRemitter) {
        this.rspDocumentNumberRemitter = rspDocumentNumberRemitter;
    }

    public String getRspNameSender() {
        return rspNameSender;
    }

    public void setRspNameSender(String rspNameSender) {
        this.rspNameSender = rspNameSender;
    }

    public String getRspDocumentTypeDeSender() {
        return rspDocumentTypeDeSender;
    }

    public void setRspDocumentTypeDeSender(String rspDocumentTypeDeSender) {
        this.rspDocumentTypeDeSender = rspDocumentTypeDeSender;
    }

    public String getRspDocumentNumberSender() {
        return rspDocumentNumberSender;
    }

    public void setRspDocumentNumberSender(String rspDocumentNumberSender) {
        this.rspDocumentNumberSender = rspDocumentNumberSender;
    }

    public String getRspAmountCurrencySymbol() {
        return rspAmountCurrencySymbol;
    }

    public void setRspAmountCurrencySymbol(String rspAmountCurrencySymbol) {
        this.rspAmountCurrencySymbol = rspAmountCurrencySymbol;
    }

    public String getRspAmount() {
        return rspAmount;
    }

    public void setRspAmount(String rspAmount) {
        this.rspAmount = rspAmount;
    }

    public String getRspCommissionCurrencySymbol() {
        return rspCommissionCurrencySymbol;
    }

    public void setRspCommissionCurrencySymbol(String rspCommissionCurrencySymbol) {
        this.rspCommissionCurrencySymbol = rspCommissionCurrencySymbol;
    }

    public String getRspCommissionAmount() {
        return rspCommissionAmount;
    }

    public void setRspCommissionAmount(String rspCommissionAmount) {
        this.rspCommissionAmount = rspCommissionAmount;
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

    public String getRsp_PaymentMethod() {
        return rsp_PaymentMethod;
    }

    public void setRsp_PaymentMethod(String rsp_PaymentMethod) {
        this.rsp_PaymentMethod = rsp_PaymentMethod;
    }

    public String getRspFamilyDescription() {
        return rspFamilyDescription;
    }

    public void setRspFamilyDescription(String rspFamilyDescription) {
        this.rspFamilyDescription = rspFamilyDescription;
    }

    public String getRspCurrencyDescription() {
        return rspCurrencyDescription;
    }

    public void setRspCurrencyDescription(String rspCurrencyDescription) {
        this.rspCurrencyDescription = rspCurrencyDescription;
    }

    public String getRspAccountId() {
        return rspAccountId;
    }

    public void setRspAccountId(String rspAccountId) {
        this.rspAccountId = rspAccountId;
    }

    public String getRspToPrint() {
        return rspToPrint;
    }

    public void setRspToPrint(String rspToPrint) {
        this.rspToPrint = rspToPrint;
    }

    public String getRspLeadDescription() {
        return rspLeadDescription;
    }

    public void setRspLeadDescription(String rspLeadDescription) {
        this.rspLeadDescription = rspLeadDescription;
    }

    public boolean getRspExecuteTrans(JSONObject json) throws JSONException {

        if(JsonUtil.validatedNull(json, TRANSACTIONDATE))
            setRspTransactionDate(json.getString(TRANSACTIONDATE));
        else
            return false;

        if(JsonUtil.validatedNull(json, TRANSACTIONTIME))
            setRspTransactionTime(json.getString(TRANSACTIONTIME));
        else
            return false;

        if(JsonUtil.validatedNull(json, TRANSACTION_NUMBER))
            setRspTransactionNumber(json.getString(TRANSACTION_NUMBER));
        else
            return false;

        if(JsonUtil.validatedNull(json, REMITTERCARD))
            setRspRemitterCard(json.getString(REMITTERCARD));

        if(!getRemitterCard()){
            return false;
        }

        if(JsonUtil.validatedNull(json, BANKDRAFTREFERENCE))
            setRspBankDraftReference(json.getString(BANKDRAFTREFERENCE));
        else
            return false;

        if(JsonUtil.validatedNull(json, BENEFICIARY))
            setRspBeneficiary(json.getString(BENEFICIARY));
        else
            return false;

        if(!getBeneficiary()){
            return false;
        }

        if(JsonUtil.validatedNull(json, REMITTER))
            setRspRemitter(json.getString(REMITTER));
        else
            return false;

        if(!getRemitterRsp()){
            return false;
        }

        if(JsonUtil.validatedNull(json, SENDER))
            setRspSender(json.getString(SENDER));

        if(!getSender()){
            return false;
        }

        if(JsonUtil.validatedNull(json, AMOUNTCURRENCYSYMBOL))
            setRspAmountCurrencySymbol(json.getString(AMOUNTCURRENCYSYMBOL));
        else
            return false;

        if(JsonUtil.validatedNull(json, AMOUNT))
            setRspAmount(json.getString(AMOUNT));
        else
            return false;

        if(JsonUtil.validatedNull(json, COMMISSIONCURRENCYSYMBOL))
            setRspCommissionCurrencySymbol(json.getString(COMMISSIONCURRENCYSYMBOL));
        else
            return false;

        if(JsonUtil.validatedNull(json, COMMISSIONAMOUNT))
            setRspCommissionAmount(json.getString(COMMISSIONAMOUNT));
        else
            return false;

        if(JsonUtil.validatedNull(json, EXCHANGE))
            setRspExchange(json.getString(EXCHANGE));

        if(!getExchange()){
            return false;
        }

        if(JsonUtil.validatedNull(json, TOTALAMOUNTCURRENCYSYMBOL))
            setRspTotalAmountCurrencySymbol(json.getString(TOTALAMOUNTCURRENCYSYMBOL));
        else
            return false;

        if(JsonUtil.validatedNull(json, TOTALAMOUNT))
            setRspTotalAmount(json.getString(TOTALAMOUNT));
        else
            return false;

        if(JsonUtil.validatedNull(json, PAYMENTMETHOD))
            setRsp_PaymentMethod(json.getString(PAYMENTMETHOD));
        else
            return false;

        if(JsonUtil.validatedNull(json, REMITTERACCOUNT))
            setRspRemitterAccount(json.getString(REMITTERACCOUNT));

        if(!getRemitterAccount()){
            return false;
        }

        if(JsonUtil.validatedNull(json, TOPRINT))
            setRspToPrint(json.getString(TOPRINT));
        else
            return false;

        if(JsonUtil.validatedNull(json, LEAD_DESCRIPTION))
            setRspLeadDescription(json.getString(LEAD_DESCRIPTION));

        return true;
    }

    private boolean getRemitterCard() throws JSONException {

        if (getRspRemitterCard() != null && !getRspRemitterCard().equals("")){
            //OBJETO REMITTERCARD
            JSONObject jsonRemitterCard = new JSONObject(getRspRemitterCard());


            if(JsonUtil.validatedNull(jsonRemitterCard, ARPC)){
                setRspArpc(jweEncryptDecrypt(jsonRemitterCard.getString(ARPC) + "",false, true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
            }else
                return false;

            if(JsonUtil.validatedNull(jsonRemitterCard, CARDID)){
                setRspCardId(jsonRemitterCard.getString(CARDID));
            }else
                return false;
        }

        return true;
    }

    private boolean getBeneficiary() throws JSONException {

        //OBJETO BENEFICIARY
        JSONObject jsonBeneficiary = new JSONObject(getRspBeneficiary());


        if(JsonUtil.validatedNull(jsonBeneficiary, NAME)){
            setRspNameBenef(jweEncryptDecrypt(jsonBeneficiary.getString(NAME) + "",false, true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
        }else
            return false;

        if(JsonUtil.validatedNull(jsonBeneficiary, DOCUMENT_TYPEDESCRIPTION)){
            setRspDocumentTypeDescriptionBenef(jsonBeneficiary.getString(DOCUMENT_TYPEDESCRIPTION));
        }else
            return false;

        if(JsonUtil.validatedNull(jsonBeneficiary, DOCUMENTNUMBER)){
            setRspDocumentNumberBenef(jweEncryptDecrypt(jsonBeneficiary.getString(DOCUMENTNUMBER)+"",false,true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
        }else
            return false;

        return true;
    }

    private boolean getRemitterRsp() throws JSONException {

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
            setRspDocumentNumberRemitter(jweEncryptDecrypt(jsonRemitter.getString(DOCUMENTNUMBER)+"",false,true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
        }else
            return false;

        return true;
    }

    private boolean getSender() throws JSONException {

        if (getRspSender() != null && !getRspSender().equals("")){
            //OBJETO SENDER
            JSONObject jsonSender = new JSONObject(getRspSender());

            if(JsonUtil.validatedNull(jsonSender, NAME)){
                setRspNameSender(jweEncryptDecrypt(jsonSender.getString(NAME) + "",false, true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
            }else
                return false;

            if(JsonUtil.validatedNull(jsonSender, DOCUMENT_TYPEDESCRIPTION)){
                setRspDocumentTypeDeSender(jsonSender.getString(DOCUMENT_TYPEDESCRIPTION));
            }else
                return false;

            if(JsonUtil.validatedNull(jsonSender, DOCUMENTNUMBER)){
                setRspDocumentNumberSender(jweEncryptDecrypt(jsonSender.getString(DOCUMENTNUMBER)+"",false,true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
            }else
                return false;
        }
        return true;
    }

    private boolean getExchange() throws JSONException {

        if (getRspExchange() != null && !getRspExchange().equals("")){
            //OBJETO EXCHANGE
            JSONObject jsonExchange = new JSONObject(getRspExchange());


            if(JsonUtil.validatedNull(jsonExchange, TOTALCURRENCYSYMBOL)){
                setRspTotalCurrencySymbol(jsonExchange.getString(TOTALCURRENCYSYMBOL));
            }else
                return false;

            if(JsonUtil.validatedNull(jsonExchange, TOTAL)){
                setRspTotal(jsonExchange.getString(TOTAL));
            }else
                return false;

            if(JsonUtil.validatedNull(jsonExchange, EXCHANGERATE)){
                setRspExchangeRate(jsonExchange.getString(EXCHANGERATE));
            }else
                return false;
        }
        return true;
    }

    private boolean getRemitterAccount() throws JSONException {

        if (getRspRemitterAccount() != null && !getRspRemitterAccount().equals("")){
            //OBJETO EXCHANGE
            JSONObject jsonRemitterAccount = new JSONObject(getRspRemitterAccount());

            if(JsonUtil.validatedNull(jsonRemitterAccount, FAMILYDESCRYPTION)){
                setRspFamilyDescription(jsonRemitterAccount.getString(FAMILYDESCRYPTION));
            }else
                return false;

            if(JsonUtil.validatedNull(jsonRemitterAccount, CURRENCYSYMBOL)){
                setRspCurrencyDescription(jsonRemitterAccount.getString(CURRENCYSYMBOL));
            }else
                return false;

            if(JsonUtil.validatedNull(jsonRemitterAccount, ACCOUNTID)){
                setRspAccountId(jweEncryptDecrypt(jsonRemitterAccount.getString(ACCOUNTID) + "",false, true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
            }else
                return false;
        }
        return true;
    }


}
