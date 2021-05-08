package com.bcp.rest.pagoservicios.response;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.trans.Trans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RspExecuteTransactionPS extends JsonUtil {

    //RESPONSE
    private static final String TRANSACTIONDATE = "transactionDate";
    private static final String TRANSACTIONTIME = "transactionTime";
    private static final String TRANSACTIONNUMBER = "transactionNumber";
    private static final String CLIENTDEPOSITCODE = "clientDepositCode";

    // CLIENTDEPOSITNAME NO SE UTILIZA EN IMPORTE
    private static final String CLIENTDEPOSITNAME = "clientDepositName";

    //IMPORTNAME SE UTILIZA EN IMPORTE
    private static final String IMPORTNAME = "importName";

    //CON RANGON, IMPORTE TARJETA, PARCIAL, SIN RANGO, SIN VALIDACION
    private static final String CATEGORY = "category";
    private static final String COMPANY = "company";
    private static final String PAYMENTACCOUNT = "paymentAccount";


    private static final String SERVICE = "service";
    private static final String DESCRIPTION = "description";

    //Esto es solo con tarjeta
    //ACCOUNT
    private static final String ACCOUNT = "account" ;
    private static final String ACCOUNTID = "accountId" ;
    private static final String FAMILYDESCRIPTION = "familyDescription";
    private static final String CURRENCYDESCRIPTION = "currencyDescription";
    private static final String CURRENCYSYMBOL = "currencySymbol";

    //CARD
    private static final String CARD = "card";
    private static final String ARPC = "ARPC";
    private static final String CARDID = "cardId";
    //

    //OPERATION TODAS
    private static final String COMMISSIONCURRENCYSYMBOL = "commissionCurrencySymbol";
    private static final String COMMISSIONAMOUNT = "commissionAmount";

    //CON RANGO,PARCIAL,SIN RANGO,SIN RANGO,SIN VALIDACION
    //OPERATION
    private static final String OPERATION = "operation";
    private static final String AMOUNTCURRENCYSYMBOL = "amountCurrencySymbol";
    private static final String AMOUNT = "amount";
    private static final String FIXEDCHARGECURRENCYSYMBOL = "fixedChargeCurrencySymbol";
    private static final String FIXEDCHARGEAMOUNT = "fixedChargeAmount";
    private static final String DELAYCURRENCYSYMBOL = "delayCurrencySymbol";
    private static final String DELAYAMOUNT = "delayAmount";

    //EXCHANGE
    private static final String EXCHANGE = "exchange";
    private static final String TOTALCURRENCYSYMBOL = "totalCurrencySymbol";
    private static final String TOTALEXCHANGE = "total";
    private static final String EXCHANGERATE = "exchangeRate";

    private static final String TOTALAMOUNTCURRENCYSYMBOL = "totalAmountCurrencySymbol";
    private static final String TOTALAMOUNT = "totalAmount";
    private static final String PAYMENTMETHOD = "paymentMethod";

    //DEBTS
    // PARA PASE MOVISTAR
    private static final String DEBTS = "debts";
    private static final String DUEDATE = "dueDate";
    private static final String CONSUMPTION = "consumption";
    private static final String IMPORT = "import";
    private static final String TOTAL = "total";

    //TODOS MENOS MOVISTAR
    private static final String DEBT = "debt";

    private static final String LEADDESCRIPTION = "leadDescription";
    private static final String TOPRINT = "toPrint";

    //CON RANGO, IMPORTE
    private static final String ADDITIONALINFORMATION = "additionalInformation";
    private static final String ROWS = "rows";
    private static final String DETAIL = "detail";

    //IMPORTE CON TARJETA
    private static final String MESSAGE = "message";

    //TODAS MENOS PASE MOVISTAR
    private static final String TOTALDEBTAMOUNT = "totalDebtAmount";
    private static final String TOTALDEBTAMOUNTCURRENCYSYMBOL = "totalDebtCurrencySymbol";

    private String transactionDate;
    private String transactionTime;
    private String transactionNumber;
    private String clientDepositCode;
    private String clientDepositName;

    //IMPORTE
    private String Importname;

    //SERVICE
    private String service;
    private String description;

    //CON RANGO TARJETA
    private String category;
    private String company;
    private String paymentAccount;

    //AMOUNT
    private String account;
    private String accountId;
    private String familyDescription;
    private String currencyDescription;
    private String currencySymbol;
    private String card;
    private String arpc;
    private String cardId;

    //OPERATION
    private String operation;
    private String commissionCurrencySymbol;
    private String commissionAmount;

    //CON RANGO
    private String amountCurrencySymbol;
    private String amount;
    private String fixedChargeCurrencySymbol;
    private String fixedChargeAmount;
    private String delayCurrencySymbol;
    private String delayAmount;

    //EXCHANGE
    private String exchange;
    private String totalCurrencySymbol;
    private String totalexchange;
    private String exchangeRate;

    private String totalAmountCurrencySymbol;
    private String totalAmount;
    private String paymentMethod;

    //DEBTS
    //SOLO MOVISTAR
    private ListsDebt[] listsDebts;
    private String debts;
    private String dueDate;
    private String consumption;
    private String importt;
    private String total;

    //!MOVISTAR
    private String debt;

    private String leadDescription;
    private String toPrint;

    //CON RANGO, IMPORTE
    private String additionalInformation;
    private String rows;
    private String detail;

    private String message;

    //TODAS MENOS PASE MOVISTAR
    private String totalDebtAmount;
    private String totalDebtAmountCurrencySymbol;


    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getClientDepositCode() {
        return clientDepositCode;
    }

    public void setClientDepositCode(String clientDepositCode) {
        this.clientDepositCode = clientDepositCode;
    }

    public String getClientDepositName() {
        return clientDepositName;
    }

    public void setClientDepositName(String clientDepositName) {
        this.clientDepositName = clientDepositName;
    }

    public String getImportname() {
        return Importname;
    }

    public void setImportname(String importname) {
        Importname = importname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFamilyDescription() {
        return familyDescription;
    }

    public void setFamilyDescription(String familyDescription) {
        this.familyDescription = familyDescription;
    }

    public String getCurrencyDescription() {
        return currencyDescription;
    }

    public void setCurrencyDescription(String currencyDescription) {
        this.currencyDescription = currencyDescription;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getArpc() {
        return arpc;
    }

    public void setArpc(String arpc) {
        this.arpc = arpc;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCommissionCurrencySymbol() {
        return commissionCurrencySymbol;
    }

    public void setCommissionCurrencySymbol(String commissionCurrencySymbol) {
        this.commissionCurrencySymbol = commissionCurrencySymbol;
    }

    public String getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(String commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getAmountCurrencySymbol() {
        return amountCurrencySymbol;
    }

    public void setAmountCurrencySymbol(String amountCurrencySymbol) {
        this.amountCurrencySymbol = amountCurrencySymbol;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFixedChargeCurrencySymbol() {
        return fixedChargeCurrencySymbol;
    }

    public void setFixedChargeCurrencySymbol(String fixedChargeCurrencySymbol) {
        this.fixedChargeCurrencySymbol = fixedChargeCurrencySymbol;
    }

    public String getFixedChargeAmount() {
        return fixedChargeAmount;
    }

    public void setFixedChargeAmount(String fixedChargeAmount) {
        this.fixedChargeAmount = fixedChargeAmount;
    }

    public String getDelayCurrencySymbol() {
        return delayCurrencySymbol;
    }

    public void setDelayCurrencySymbol(String delayCurrencySymbol) {
        this.delayCurrencySymbol = delayCurrencySymbol;
    }

    public String getDelayAmount() {
        return delayAmount;
    }

    public void setDelayAmount(String delayAmount) {
        this.delayAmount = delayAmount;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getTotalCurrencySymbol() {
        return totalCurrencySymbol;
    }

    public void setTotalCurrencySymbol(String totalCurrencySymbol) {
        this.totalCurrencySymbol = totalCurrencySymbol;
    }

    public String getTotalexchange() {
        return totalexchange;
    }

    public void setTotalexchange(String totalexchange) {
        this.totalexchange = totalexchange;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getTotalAmountCurrencySymbol() {
        return totalAmountCurrencySymbol;
    }

    public void setTotalAmountCurrencySymbol(String totalAmountCurrencySymbol) {
        this.totalAmountCurrencySymbol = totalAmountCurrencySymbol;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getConsumption() {
        return consumption;
    }

    public void setConsumption(String consumption) {
        this.consumption = consumption;
    }

    public String getImportt() {
        return importt;
    }

    public void setImportt(String importt) {
        this.importt = importt;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getLeadDescription() {
        return leadDescription;
    }

    public void setLeadDescription(String leadDescription) {
        this.leadDescription = leadDescription;
    }

    public String getToPrint() {
        return toPrint;
    }

    public void setToPrint(String toPrint) {
        this.toPrint = toPrint;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public ListsDebt[] getListsDebts() {
        return listsDebts;
    }

    public void setListsDebts(ListsDebt[] listsDebts) {
        this.listsDebts = listsDebts;
    }

    public String getDebts() {
        return debts;
    }

    public void setDebts(String debts) {
        this.debts = debts;
    }

    public String getTotalDebtAmount() {
        return totalDebtAmount;
    }

    public void setTotalDebtAmount(String totalDebtAmount) {
        this.totalDebtAmount = totalDebtAmount;
    }

    public String getTotalDebtAmountCurrencySymbol() {
        return totalDebtAmountCurrencySymbol;
    }

    public void setTotalDebtAmountCurrencySymbol(String totalDebtAmountCurrencySymbol) {
        this.totalDebtAmountCurrencySymbol = totalDebtAmountCurrencySymbol;
    }

    public String getDebt() {
        return debt;
    }

    public void setDebt(String debt) {
        this.debt = debt;
    }

    public boolean getRspTransactionPS(JSONObject json , String typePaymetService, String typePayment) throws JSONException{

        if (JsonUtil.validatedNull(json, TRANSACTIONDATE))
            setTransactionDate(json.getString(TRANSACTIONDATE));
        else
            return false;

        if (JsonUtil.validatedNull(json, TRANSACTIONTIME))
            setTransactionTime(json.getString(TRANSACTIONTIME));
        else
            return false;

        if (JsonUtil.validatedNull(json, TRANSACTIONNUMBER))
            setTransactionNumber(json.getString(TRANSACTIONNUMBER));
        else
            return false;

        if (JsonUtil.validatedNull(json, CLIENTDEPOSITCODE))
            setClientDepositCode(jweEncryptDecrypt(json.getString(CLIENTDEPOSITCODE),false,true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
        else
            return false;

        //IMPORTE
        if(typePaymetService.equals(Trans.PAGO_IMPORTE)){
            if (JsonUtil.validatedNull(json, IMPORTNAME))
                setImportname(json.getString(IMPORTNAME));
            else
                return false;
        }else {
            if (!typePaymetService.equals(Trans.PAGO_SINVALIDACION) && !typePaymetService.equals(Trans.PAGO_PASEMOVISTAR)){
                if (JsonUtil.validatedNull(json, CLIENTDEPOSITNAME))
                    setClientDepositName(jweEncryptDecrypt(json.getString(CLIENTDEPOSITNAME),false,true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
                else
                    return false;
            }
        }

        if (!getRspDebts(json,typePaymetService)){
            return false;
        }

        if(!getRspInfoService(json,typePaymetService)){
            return false;
        }

        if(typePayment.equals("2") && !getRspAccount(json)){
            return false;
        }

        if (!getRspOperation(json,typePayment)){
            return false;
        }

        if (JsonUtil.validatedNull(json, LEADDESCRIPTION))
            setLeadDescription(json.getString(LEADDESCRIPTION));
        else
            return false;

        if (JsonUtil.validatedNull(json, TOPRINT))
            setToPrint(json.getString(TOPRINT));
        else
            return false;

        if(!typePaymetService.equals(Trans.PAGO_PASEMOVISTAR)){
            return getAdditiona(json);
        }

        return true;
    }

    private boolean getRspInfoService(JSONObject jsonService, String typePs) throws JSONException{

        if (JsonUtil.validatedNull(jsonService, SERVICE))
            setService(jsonService.getString(SERVICE));
        else
            return false;

        JSONObject jsonObjectService = new JSONObject(getService());

        //CON RANGON, IMPORTE , PARCIAL, SIN RANGO, SIN VALIDACION
        if(!typePs.equals(Trans.PAGO_PASEMOVISTAR)){

            if (JsonUtil.validatedNull(jsonObjectService,CATEGORY))
                setCategory(jsonObjectService.getString(CATEGORY));
            else
                return false;

            if (JsonUtil.validatedNull(jsonObjectService, COMPANY))
                setCompany(jsonObjectService.getString(COMPANY));
            else
                return false;

            if (JsonUtil.validatedNull(jsonObjectService, PAYMENTACCOUNT))
                setPaymentAccount(jweEncryptDecrypt(jsonObjectService.getString(PAYMENTACCOUNT),false,true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
            else
                return false;
        }

        if (JsonUtil.validatedNull(jsonObjectService, DESCRIPTION))
            setDescription(jsonObjectService.getString(DESCRIPTION));
        else
            return false;

        return true;
    }

    //Esto es solo para tarjeta
    private boolean getRspAccount(JSONObject jsonObject) throws JSONException{

        if (JsonUtil.validatedNull(jsonObject, ACCOUNT))
            setAccount(jsonObject.getString(ACCOUNT));
        else
            return false;

        JSONObject jsonObjetAccount = new JSONObject(getAccount());

        if (JsonUtil.validatedNull(jsonObjetAccount, ACCOUNTID))
            setAccountId(jweEncryptDecrypt(jsonObjetAccount.getString(ACCOUNTID),false,true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
        else
            return false;

        if (JsonUtil.validatedNull(jsonObjetAccount, FAMILYDESCRIPTION))
            setFamilyDescription(jsonObjetAccount.getString(FAMILYDESCRIPTION));
        else
            return false;

        if (JsonUtil.validatedNull(jsonObjetAccount, CURRENCYDESCRIPTION))
            setCurrencyDescription(jsonObjetAccount.getString(CURRENCYDESCRIPTION));
        else
            return false;

        if (JsonUtil.validatedNull(jsonObjetAccount, CURRENCYSYMBOL))
            setCurrencySymbol(jsonObjetAccount.getString(CURRENCYSYMBOL));
        else
            return false;

        return getRspCard(jsonObject);
    }

    private boolean getRspCard(JSONObject jsonCard) throws JSONException{

        if (JsonUtil.validatedNull(jsonCard, CARD))
            setCard(jsonCard.getString(CARD));
        else
            return false;

        JSONObject jsonObjectCard = new JSONObject(getCard());

        if (JsonUtil.validatedNull(jsonObjectCard, ARPC))
            setArpc(jsonObjectCard.getString(ARPC));
        else
            return false;

        if (JsonUtil.validatedNull(jsonObjectCard, CARDID))
            setCardId(jsonObjectCard.getString(CARDID));
        else
            return false;

        return true;
    }

    private boolean getRspOperation(JSONObject jsonOperation,String typePs) throws JSONException{

        if (JsonUtil.validatedNull(jsonOperation, OPERATION))
            setOperation(jsonOperation.getString(OPERATION));
        else
            return false;

        JSONObject jsonObjectOperation = new JSONObject(getOperation());

        if(!typePs.equals(Trans.PAGO_PASEMOVISTAR)){
            if (JsonUtil.validatedNull(jsonObjectOperation, AMOUNTCURRENCYSYMBOL))
                setAmountCurrencySymbol(jsonObjectOperation.getString(AMOUNTCURRENCYSYMBOL));
            else
                return false;

            if (JsonUtil.validatedNull(jsonObjectOperation, AMOUNT))
                setAmount(jsonObjectOperation.getString(AMOUNT));
            else
                return false;

            if (JsonUtil.validatedNull(jsonObjectOperation, FIXEDCHARGECURRENCYSYMBOL))
                setFixedChargeCurrencySymbol(jsonObjectOperation.getString(FIXEDCHARGECURRENCYSYMBOL));
            else
                return false;

            if (JsonUtil.validatedNull(jsonObjectOperation, FIXEDCHARGEAMOUNT))
                setFixedChargeAmount(jsonObjectOperation.getString(FIXEDCHARGEAMOUNT));
            else
                return false;

            if (JsonUtil.validatedNull(jsonObjectOperation, DELAYCURRENCYSYMBOL))
                setDelayCurrencySymbol(jsonObjectOperation.getString(DELAYCURRENCYSYMBOL));
            else
                return false;

            if (JsonUtil.validatedNull(jsonObjectOperation, DELAYAMOUNT))
                setDelayAmount(jsonObjectOperation.getString(DELAYAMOUNT));
            else
                return false;

            if (JsonUtil.validatedNull(jsonObjectOperation, TOTALDEBTAMOUNTCURRENCYSYMBOL))
                setTotalDebtAmountCurrencySymbol(jsonObjectOperation.getString(TOTALDEBTAMOUNTCURRENCYSYMBOL));
            else
                return false;

            if (JsonUtil.validatedNull(jsonObjectOperation, TOTALDEBTAMOUNT))
                setTotalDebtAmount(jsonObjectOperation.getString(TOTALDEBTAMOUNT));
            else
                return false;
        }else {

            if (JsonUtil.validatedNull(jsonObjectOperation,DEBTS))
                setDebts(jsonObjectOperation.getString(DEBTS));
            else
                return false;

            JSONArray jsonArray = new JSONArray(getDebts());

            if (jsonArray.length()<=0)
                return false;

            ListsDebt[] listsDebts = new ListsDebt[jsonArray.length()];

            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject ctn = jsonArray.getJSONObject(j);
                listsDebts[j] = new ListsDebt();

                if (JsonUtil.validatedNull(ctn, DUEDATE))
                    listsDebts[j].setDueDate(ctn.getString(DUEDATE));
                else
                    return false;

                if (JsonUtil.validatedNull(ctn, CONSUMPTION))
                    listsDebts[j].setConsumption(ctn.getString(CONSUMPTION));
                else
                    return false;

                if (JsonUtil.validatedNull(ctn, IMPORT))
                    listsDebts[j].setImportName(ctn.getString(IMPORT));
                else
                    return false;

                if (JsonUtil.validatedNull(ctn,TOTAL))
                    listsDebts[j].setAmount(ctn.getString(TOTAL));
                else
                    return false;
            }
            setListsDebts(listsDebts);

        }

        if (JsonUtil.validatedNull(jsonObjectOperation, COMMISSIONCURRENCYSYMBOL))
            setCommissionCurrencySymbol(jsonObjectOperation.getString(COMMISSIONCURRENCYSYMBOL));
        else
            return false;

        if (JsonUtil.validatedNull(jsonObjectOperation, COMMISSIONAMOUNT))
            setCommissionAmount(jsonObjectOperation.getString(COMMISSIONAMOUNT));
        else
            return false;

        if (JsonUtil.validatedNull(jsonObjectOperation, EXCHANGE))
            setExchange(jsonObjectOperation.getString(EXCHANGE));
        else
            return false;

        if (!getRspExchange()){
            return false;
        }

        if (JsonUtil.validatedNull(jsonObjectOperation, TOTALAMOUNTCURRENCYSYMBOL))
            setTotalAmountCurrencySymbol(jsonObjectOperation.getString(TOTALAMOUNTCURRENCYSYMBOL));
        else
            return false;

        if (JsonUtil.validatedNull(jsonObjectOperation, TOTALAMOUNT))
            setTotalAmount(jsonObjectOperation.getString(TOTALAMOUNT));
        else
            return false;

        if (JsonUtil.validatedNull(jsonObjectOperation, PAYMENTMETHOD))
            setPaymentMethod(jsonObjectOperation.getString(PAYMENTMETHOD));
        else
            return false;

        return true;
    }

    private boolean getRspExchange() throws JSONException{

        JSONObject jsonExchange = new JSONObject(getExchange());

        if (JsonUtil.validatedNull(jsonExchange, TOTALCURRENCYSYMBOL))
            setTotalCurrencySymbol(jsonExchange.getString(TOTALCURRENCYSYMBOL));
        else
            return false;

        if (JsonUtil.validatedNull(jsonExchange, TOTALEXCHANGE))
            setTotalexchange(jsonExchange.getString(TOTALEXCHANGE));
        else
            return false;

        if (JsonUtil.validatedNull(jsonExchange, EXCHANGERATE))
            setExchangeRate(jsonExchange.getString(EXCHANGERATE));
        else
            return false;

        return true;
    }

    private boolean getRspDebts(JSONObject json, String  typePaymetService) throws JSONException{

        if (typePaymetService.equals(Trans.PAGO_CONRANGO) || typePaymetService.equals(Trans.PAGO_SINRANGO)){
            if (JsonUtil.validatedNull(json,DEBT))
                setDebt(json.getString(DEBT));
            else
                return false;

            JSONObject jsonDebt = new JSONObject(getDebt());

            if (JsonUtil.validatedNull(jsonDebt, DUEDATE))
                setDueDate(jsonDebt.getString(DUEDATE));
            else
                return false;
        }
        return true;
    }

    //CON RANGO
    private boolean getAdditiona(JSONObject json)throws JSONException{

        if (JsonUtil.validatedNull(json, ADDITIONALINFORMATION))
            setAdditionalInformation(json.getString(ADDITIONALINFORMATION));
        else
            return false;

        JSONObject jsonAdditiona = new JSONObject(getAdditionalInformation());

        if (JsonUtil.validatedNull(jsonAdditiona, ROWS))
            setRows(jsonAdditiona.getString(ROWS));
        else
            return false;

        if (JsonUtil.validatedNull(jsonAdditiona, DETAIL))
            setDetail(jsonAdditiona.getString(DETAIL));
        else
            return false;

        return true;
    }

}
