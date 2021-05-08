package com.bcp.rest.giros.cobro_nacional.response;

import com.bcp.rest.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class RspExecuteTransactionGiros extends JsonUtil{

    private static final String OPERATIONDATE = "operationDate";
    private static final String OPERATIONTIME = "operationTime";
    private static final String OPERATION_NUMBER = "operationNumber";
    private static final String BENEFICIARYCARD = "beneficiaryCard";
    private static final String CARDID = "cardId";
    private static final String BANKDRAFTREFERENCE = "bankDraftReference";
    private static final String BENEFICIARY = "beneficiary";
    private static final String NAME = "name";
    private static final String DOCUMENT_TYPEDESCRIPTION = "documentTypeDescription";
    private static final String DOCUMENTNUMBER = "documentNumber";
    private static final String REMITTER = "remitter";
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
    private static final String PAYMENT_METHOD = "paymentMethod";
    private static final String BENEFICIARYACCOUNT = "beneficiaryAccount";
    private static final String FAMILYDESCRIPTION = "familyDescription";
    private static final String CURRENCYSYMBOL = "currencySymbol";
    private static final String ACCOUNTID = "accountId";
    private static final String TOPRINT = "toPrint";
    private static final String LEAD_DESCRIPTION = "leadDescription";


    private String rspOperationDate;
    private String rspOperationTime;
    private String rspOperationNumber;
    private String rspBeneficiaryCard;
    private String rspCardId;
    private String rspBankDraftReference;
    private String rspBeneficiary;
    private String rspNameBenef;
    private String rspDocumentTypeDescriptionBenef;
    private String rspDocumentNumberBenef;
    private String rspRemitter;
    private String rspNameRemit;
    private String rspDocumentTypeDescriptionRemit;
    private String rspDocumentNumberRemit;
    private String rspAmountCurrencySymbol;
    private String rspAmount;
    private String rspCommissionCurrencySymbol;
    private String rspCommissionAmount;
    private String rspExchange;
    private String rspTotalCurrencySymbol;
    private String rspTotal;
    private String rspExchangeRate;
    private String rspTotalAmountCurrencySymbol;
    private String rspTotalAmount;
    private String rspPaymentMethod;
    private String rspBeneficiaryAccount;
    private String rspFamilyDescription;
    private String rspCurrencySymbol;
    private String rspAccountId;
    private String rspToPrint;
    private String rspLeadDescription;
    private String typePayment;

    public String getRspOperationDate() {
        return rspOperationDate;
    }

    public void setRspOperationDate(String rspOperationDate) {
        this.rspOperationDate = rspOperationDate;
    }

    public String getRspOperationTime() {
        return rspOperationTime;
    }

    public void setRspOperationTime(String rspOperationTime) {
        this.rspOperationTime = rspOperationTime;
    }

    public String getRspOperationNumber() {
        return rspOperationNumber;
    }

    public void setRspOperationNumber(String rspOperationNumber) {
        this.rspOperationNumber = rspOperationNumber;
    }

    public String getRspBeneficiaryCard() {
        return rspBeneficiaryCard;
    }

    public void setRspBeneficiaryCard(String rspBeneficiaryCard) {
        this.rspBeneficiaryCard = rspBeneficiaryCard;
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

    public String getRspRemitter() {
        return rspRemitter;
    }

    public void setRspRemitter(String rspRemitter) {
        this.rspRemitter = rspRemitter;
    }

    public String getRspNameRemit() {
        return rspNameRemit;
    }

    public void setRspNameRemit(String rspNameRemit) {
        this.rspNameRemit = rspNameRemit;
    }

    public String getRspDocumentTypeDescriptionRemit() {
        return rspDocumentTypeDescriptionRemit;
    }

    public void setRspDocumentTypeDescriptionRemit(String rspDocumentTypeDescriptionRemit) {
        this.rspDocumentTypeDescriptionRemit = rspDocumentTypeDescriptionRemit;
    }

    public String getRspDocumentNumberRemit() {
        return rspDocumentNumberRemit;
    }

    public void setRspDocumentNumberRemit(String rspDocumentNumberRemit) {
        this.rspDocumentNumberRemit = rspDocumentNumberRemit;
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

    public String getRspPaymentMethod() {
        return rspPaymentMethod;
    }

    public void setRspPaymentMethod(String rspPaymentMethod) {
        this.rspPaymentMethod = rspPaymentMethod;
    }

    public String getRspBeneficiaryAccount() {
        return rspBeneficiaryAccount;
    }

    public void setRspBeneficiaryAccount(String rspBeneficiaryAccount) {
        this.rspBeneficiaryAccount = rspBeneficiaryAccount;
    }

    public String getRspFamilyDescription() {
        return rspFamilyDescription;
    }

    public void setRspFamilyDescription(String rspFamilyDescription) {
        this.rspFamilyDescription = rspFamilyDescription;
    }

    public String getRspCurrencySymbol() {
        return rspCurrencySymbol;
    }

    public void setRspCurrencySymbol(String rspCurrencySymbol) {
        this.rspCurrencySymbol = rspCurrencySymbol;
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

    public String getTypePayment() {
        return typePayment;
    }

    public void setTypePayment(String typePayment) {
        this.typePayment = typePayment;
    }


    public boolean getRspExecuteTrans(JSONObject json) throws JSONException {

        if(JsonUtil.validatedNull(json, OPERATIONDATE))
            setRspOperationDate(json.getString(OPERATIONDATE));
        else
            return false;

        if(JsonUtil.validatedNull(json, OPERATIONTIME))
            setRspOperationTime(json.getString(OPERATIONTIME));
        else
            return false;

        if(JsonUtil.validatedNull(json, OPERATION_NUMBER))
            setRspOperationNumber(json.getString(OPERATION_NUMBER));
        else
            return false;

        if (getTypePayment().equals("2")){
            if(JsonUtil.validatedNull(json, BENEFICIARYCARD)){
                setRspBeneficiaryCard(json.getString(BENEFICIARYCARD));
            }else
                return false;

            if (!getRspObtainBeneficiaryCard()){
                return false;
            }
        }

        if(JsonUtil.validatedNull(json, BANKDRAFTREFERENCE)){
            setRspBankDraftReference(json.getString(BANKDRAFTREFERENCE));
        }else
            return false;

        if(JsonUtil.validatedNull(json, BENEFICIARY)){
            setRspBeneficiary(json.getString(BENEFICIARY));
        }else
            return false;

        if (!getRspObtainBeneficiary()){
            return false;
        }

        if(JsonUtil.validatedNull(json, REMITTER)){
            setRspRemitter(json.getString(REMITTER));
        }else
            return false;

        if (!getRspObtainRemitter()){
            return false;
        }

        if(JsonUtil.validatedNull(json, AMOUNTCURRENCYSYMBOL)){
            setRspAmountCurrencySymbol(json.getString(AMOUNTCURRENCYSYMBOL));
        }else
            return false;

        if(JsonUtil.validatedNull(json, AMOUNT)){
            setRspAmount(json.getString(AMOUNT));
        }else
            return false;

        if(JsonUtil.validatedNull(json, COMMISSIONCURRENCYSYMBOL)){
            setRspCommissionCurrencySymbol(json.getString(COMMISSIONCURRENCYSYMBOL));
        }else
            return false;

        if(JsonUtil.validatedNull(json, COMMISSIONAMOUNT)){
            setRspCommissionAmount(json.getString(COMMISSIONAMOUNT));
        }else
            return false;

        if(JsonUtil.validatedNull(json, EXCHANGE)){
            setRspExchange(json.getString(EXCHANGE));
        }

        if (getRspExchange() != null)
            getRspObtainExchange();

        if(JsonUtil.validatedNull(json, TOTALAMOUNTCURRENCYSYMBOL)){
            setRspTotalAmountCurrencySymbol(json.getString(TOTALAMOUNTCURRENCYSYMBOL));
        }else
            return false;

        if(JsonUtil.validatedNull(json, TOTALAMOUNT)){
            setRspTotalAmount(json.getString(TOTALAMOUNT));
        }else
            return false;

        if(JsonUtil.validatedNull(json, PAYMENT_METHOD)){
            setRspPaymentMethod(json.getString(PAYMENT_METHOD));
        }else
            return false;

        if (getTypePayment().equals("2")){
            if(JsonUtil.validatedNull(json, BENEFICIARYACCOUNT)){
                setRspBeneficiaryAccount(json.getString(BENEFICIARYACCOUNT));
            }else
                return false;

            if (!getRspObtainBeneficiaryAccount()){
                return false;
            }
        }

        if(JsonUtil.validatedNull(json, TOPRINT)){
            setRspToPrint(json.getString(TOPRINT));
        }else
            return false;

        if(JsonUtil.validatedNull(json, LEAD_DESCRIPTION))
            setRspLeadDescription(json.getString(LEAD_DESCRIPTION));

        return true;
    }

    private boolean getRspObtainBeneficiaryCard() throws JSONException {
        //OBJETO BENEFICIARYCARD
        JSONObject jsonBeneficiaryCard = new JSONObject(getRspBeneficiaryCard());


        if(JsonUtil.validatedNull(jsonBeneficiaryCard, CARDID)){
            setRspCardId(jsonBeneficiaryCard.getString(CARDID));
        }else {
            return false;
        }

        return true;
    }
    private boolean getRspObtainBeneficiary() throws JSONException {
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
            setRspDocumentNumberBenef(jweEncryptDecrypt(jsonBeneficiary.getString(DOCUMENTNUMBER) + "",false, true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
        }else
            return false;

        return true;
    }

    private boolean getRspObtainRemitter() throws JSONException {
        //OBJETO REMITTER
        JSONObject jsonRemitter = new JSONObject(getRspRemitter());


        if(JsonUtil.validatedNull(jsonRemitter, NAME)){
            setRspNameRemit(jweEncryptDecrypt(jsonRemitter.getString(NAME) + "",false, true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
        }else
            return false;

        if(JsonUtil.validatedNull(jsonRemitter, DOCUMENT_TYPEDESCRIPTION)){
            setRspDocumentTypeDescriptionRemit(jsonRemitter.getString(DOCUMENT_TYPEDESCRIPTION));
        }else
            return false;

        if(JsonUtil.validatedNull(jsonRemitter, DOCUMENTNUMBER)){
            setRspDocumentNumberRemit(jweEncryptDecrypt(jsonRemitter.getString(DOCUMENTNUMBER) + "",false, true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
        }else
            return false;

        return true;
    }

    private void getRspObtainExchange() throws JSONException {
        //OBJETO EXCHANGE
        JSONObject jsonExchange = new JSONObject(getRspExchange());

        if(JsonUtil.validatedNull(jsonExchange, TOTALCURRENCYSYMBOL))
            setRspTotalCurrencySymbol(jsonExchange.getString(TOTALCURRENCYSYMBOL));

        if(JsonUtil.validatedNull(jsonExchange, TOTAL))
            setRspTotal(jsonExchange.getString(TOTAL));

        if(JsonUtil.validatedNull(jsonExchange, EXCHANGERATE))
            setRspExchangeRate(jsonExchange.getString(EXCHANGERATE));
    }

    private boolean getRspObtainBeneficiaryAccount() throws JSONException {
        //OBJETO BENEFICIARYACCOUNT
        JSONObject jsonBeneficiaryAccount = new JSONObject(getRspBeneficiaryAccount());


        if(JsonUtil.validatedNull(jsonBeneficiaryAccount, FAMILYDESCRIPTION)){
            setRspFamilyDescription(jsonBeneficiaryAccount.getString(FAMILYDESCRIPTION));
        }else
            return false;

        if(JsonUtil.validatedNull(jsonBeneficiaryAccount, CURRENCYSYMBOL)){
            setRspCurrencySymbol(jsonBeneficiaryAccount.getString(CURRENCYSYMBOL));
        }else
            return false;

        if(JsonUtil.validatedNull(jsonBeneficiaryAccount, ACCOUNTID)){
            setRspAccountId(jweEncryptDecrypt(jsonBeneficiaryAccount.getString(ACCOUNTID) + "",false, true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
        }else
            return false;

        return true;
    }

}
