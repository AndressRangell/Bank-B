package com.bcp.rest.generic.response;

import com.bcp.rest.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static com.newpos.libpay.trans.Trans.CONSULTAS;
import static com.newpos.libpay.trans.Trans.DEPOSITO;
import static com.newpos.libpay.trans.Trans.RETIRO;

public class RspExecuteTransaction extends JsonUtil{

    private String transEname;

    private static final String TRANSACTIONDATE = "transactionDate";
    private static final String TRANSACTIONTIME = "transactionTime";
    private static final String TRANSACTIONNUMBER = "transactionNumber";
    private static final String LEADDESCRIPTION = "leadDescription";
    private static final String TOPRINT = "toPrint";

    //AMOUNT
    private static final String AMOUNT = "amount";
    private static final String AMNTEXCHANGERATE = "amountExchangeRate";
    private static final String EXCHANGERATE = "exchangeRate";

    //PERSON
    private static final String PERSON = "person";
    private static final String FULLNAME = "fullName";

    //CARD
    private static final String CARD = "card";
    private static final String CARDID = "cardId";
    private static final String ARPC = "ARPC";

    //ACCOUNT
    private static final String ACCOUNT = "account";
    private static final String ACCOUNTID = "accountId";
    private static final String FAMILYDESCRIPTION = "familyDescription";
    private static final String CURRENCYDESCRIPTION = "currencyDescription";
    private static final String CURRENCYSYMBOL = "currencySymbol";
    private static final String AVAILABLEBALANCE ="availableBalance";
    private static final String COUNTABLEBALANCE = "countableBalance";

    //TRANSACTIONS
    private static final String TRANSACTIONS = "transactions";
    private static final String DATE = "date";
    private static final String REFERENCE = "reference";

    private String transDate;
    private String transTime;
    private String transNumber;
    private String leadDescript;
    private String print;

    //AMOUNT
    private String amnt;
    private String amntExcRate;
    private String excRate;

    //PERSON
    private String objPerson;
    private String ctnFullName;

    //CARD
    private String objCard;
    private String ctnArpc;
    private String ctnCardId;

    //ACCOUNT
    private String objAccount;
    private String ctnAccountId;
    private String ctnFmlDescript;
    private String ctnCurrenDescript;
    private String ctnCurrencySymbol;
    private String availablelance;
    private String ctncountablebalance;

    private String transactionsPerson;
    private ProductsTransactions[] ctntransactions;
    private boolean withCard;

    public void setTransEname(String transEname) {
        this.transEname = transEname;
    }

    public String getTransDate() {
        return transDate;
    }

    private void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransTime() {
        return transTime;
    }

    private void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getTransNumber() {
        return transNumber;
    }

    private void setTransNumber(String transNumber) {
        this.transNumber = transNumber;
    }

    public String getLeadDescript() {
        return leadDescript;
    }

    private void setLeadDescript(String leadDescript) {
        this.leadDescript = leadDescript;
    }

    public String getPrint() {
        return print;
    }

    public void setPrint(String print) {
        this.print = print;
    }

    public String getAmnt() {
        return amnt;
    }

    public void setAmnt(String amnt) {
        this.amnt = amnt;
    }

    public String getAmntExcRate() {
        return amntExcRate;
    }

    private void setAmntExcRate(String amntExcRate) {
        this.amntExcRate = amntExcRate;
    }

    public String getExcRate() {
        return excRate;
    }

    private void setExcRate(String excRate) {
        this.excRate = excRate;
    }

    private String getObjPerson() {
        return objPerson;
    }

    private void setObjPerson(String objPerson) {
        this.objPerson = objPerson;
    }

    public String getCtnFullName() {
        return ctnFullName;
    }

    private void setCtnFullName(String ctnFullName) {
        this.ctnFullName = ctnFullName;
    }

    private String getObjCard() {
        return objCard;
    }

    private void setObjCard(String objCard) {
        this.objCard = objCard;
    }

    public String getCtnArpc() {
        return ctnArpc;
    }

    private void setCtnArpc(String ctnArpc) {
        this.ctnArpc = ctnArpc;
    }

    public String getCtnCardId() {
        return ctnCardId;
    }

    private void setCtnCardId(String ctnCardId) {
        this.ctnCardId = ctnCardId;
    }

    private String getObjAccount() {
        return objAccount;
    }

    private void setObjAccount(String objAccount) {
        this.objAccount = objAccount;
    }

    public String getCtnAccountId() {
        return ctnAccountId;
    }

    private void setCtnAccountId(String ctnAccountId) {
        this.ctnAccountId = ctnAccountId;
    }

    public String getCtnFmlDescript() {
        return ctnFmlDescript;
    }

    private void setCtnFmlDescript(String ctnFmlDescript) {
        this.ctnFmlDescript = ctnFmlDescript;
    }

    public String getCtnCurrenDescript() {
        return ctnCurrenDescript;
    }

    private void setCtnCurrenDescript(String ctnCurrenDescript) {
        this.ctnCurrenDescript = ctnCurrenDescript;
    }

    public String getCtnCurrencySymbol() {
        return ctnCurrencySymbol;
    }

    public void setCtnCurrencySymbol(String ctnCurrencySymbol) {
        this.ctnCurrencySymbol = ctnCurrencySymbol;
    }

    public String getAvailablelance() {
        return availablelance;
    }

    private void setAvailablelance(String availablelance) {
        this.availablelance = availablelance;
    }

    public String getCountablebalance() {
        return ctncountablebalance;
    }

    private void setCountablebalance(String countablebalance) {
        this.ctncountablebalance = countablebalance;
    }

    private String getTransactionsPerson() {
        return transactionsPerson;
    }

    private void setTransactionsPerson(String transactionsPerson) {
        this.transactionsPerson = transactionsPerson;
    }

    public ProductsTransactions[] getTransactions() {
        return ctntransactions;
    }

    public void setTransactions(ProductsTransactions[] transactions) {
        this.ctntransactions = transactions;
    }

    public void setWithCard(boolean withCard) {
        this.withCard = withCard;
    }

    /**
     * realiza el parsin del objeto JSON recibido.
     * @param json
     * @return retorna el objeto con los datos JSON interpretados de la respuesta.
     */
    public boolean getRspTransaction(JSONObject json) throws JSONException {

        if (!getRspObtainGeneralInfo(json)){
            return false;
        }

        if (!getRspObtainAmount(json)){
            return false;
        }

        if (transEname.equals(DEPOSITO)){
            if(JsonUtil.validatedNull(json, PERSON)){
                setObjPerson(json.getString(PERSON));
            }else
                return false;

            if (!getRspObtainPerson()){
                return false;
            }
        }

        if (withCard && !transEname.equals(DEPOSITO)){
            if(JsonUtil.validatedNull(json, CARD)){
                setObjCard(json.getString(CARD));
            }else
                return false;

            if (!getRspObtainCard()){
                return false;
            }
        }

        if(JsonUtil.validatedNull(json, ACCOUNT)){
            setObjAccount(json.getString(ACCOUNT));
        }else
            return false;

        if (transEname.equals(CONSULTAS)){
            if (JsonUtil.validatedNull(json, TRANSACTIONS)) {
                setTransactionsPerson(json.getString(TRANSACTIONS));
                if (!getRspObtainTransaction()){
                    return false;
                }
            }
        }

        return getRspObtainAccount();
    }

    private boolean getRspObtainGeneralInfo(JSONObject jsonResponse) throws JSONException {

        if(JsonUtil.validatedNull(jsonResponse, TRANSACTIONDATE)){
            setTransDate(jsonResponse.getString(TRANSACTIONDATE));
        }else
            return false;

        if(JsonUtil.validatedNull(jsonResponse, TRANSACTIONTIME)){
            setTransTime(jsonResponse.getString(TRANSACTIONTIME));
        }else
            return false;

        if(JsonUtil.validatedNull(jsonResponse, TRANSACTIONNUMBER)){
            setTransNumber(jsonResponse.getString(TRANSACTIONNUMBER));
        }else
            return false;

        if(JsonUtil.validatedNull(jsonResponse, LEADDESCRIPTION)){
            setLeadDescript(jsonResponse.getString(LEADDESCRIPTION));
        }

        if(JsonUtil.validatedNull(jsonResponse, TOPRINT)){
            setPrint(jsonResponse.getString(TOPRINT));
        }else
            return false;

        return true;
    }

    private boolean getRspObtainPerson() throws JSONException {
        //OBJETO PERSON
        JSONObject jsonPerson = new JSONObject(getObjPerson());

        if(JsonUtil.validatedNull(jsonPerson, FULLNAME)){
            setCtnFullName(jweEncryptDecrypt(jsonPerson.getString(FULLNAME)+"",false,true) ? jweDataEncryptDecrypt.getDataDecrypt(): "");
        }else
            return false;

        return true;
    }

    private boolean getRspObtainAmount(JSONObject jsonAmount) throws JSONException {

        if (transEname.equals(RETIRO)){
            if(JsonUtil.validatedNull(jsonAmount, AMNTEXCHANGERATE)){
                setAmntExcRate(jsonAmount.getString(AMNTEXCHANGERATE));
            }

            if(JsonUtil.validatedNull(jsonAmount, EXCHANGERATE)){
                setExcRate(jsonAmount.getString(EXCHANGERATE));
            }
        }
        if (!transEname.equals(CONSULTAS)) {
            if(JsonUtil.validatedNull(jsonAmount, AMOUNT)){
                setAmnt(jsonAmount.getString(AMOUNT));
            }else
                return false;
        }
        return true;
    }

    private boolean getRspObtainCard() throws JSONException {
        //OBJETO CARD
        JSONObject jsonCard = new JSONObject(getObjCard());

        if(JsonUtil.validatedNull(jsonCard, ARPC)){
            setCtnArpc(jweEncryptDecrypt(jsonCard.getString(ARPC)+"",false,true) ? jweDataEncryptDecrypt.getDataDecrypt(): "");
        }else
            return false;

        if(JsonUtil.validatedNull(jsonCard, CARDID)){
            setCtnCardId(jsonCard.getString(CARDID)+"");
        }else
            return false;

        return true;
    }

    private boolean getRspObtainAccount() throws JSONException {
        //OBJETO ACCOUNT
        JSONObject jsonAccount = new JSONObject(getObjAccount());

        if(JsonUtil.validatedNull(jsonAccount, ACCOUNTID)){
            setCtnAccountId(jweEncryptDecrypt(jsonAccount.getString(ACCOUNTID)+"",false,true) ? jweDataEncryptDecrypt.getDataDecrypt(): "");
        }else
            return false;

        if(JsonUtil.validatedNull(jsonAccount, FAMILYDESCRIPTION)){
            setCtnFmlDescript(jsonAccount.getString(FAMILYDESCRIPTION));
        }else
            return false;

        if(JsonUtil.validatedNull(jsonAccount, CURRENCYSYMBOL)){
            setCtnCurrencySymbol(jsonAccount.getString(CURRENCYSYMBOL));
        }else
            return false;

        if (transEname.equals(CONSULTAS)){
            if(JsonUtil.validatedNull(jsonAccount, AVAILABLEBALANCE)){
                setAvailablelance(jsonAccount.getString(AVAILABLEBALANCE));
            }else
                return false;

            if(JsonUtil.validatedNull(jsonAccount, COUNTABLEBALANCE)){
                setCountablebalance(jsonAccount.getString(COUNTABLEBALANCE));
            }else
                return false;
        }else {
            if(JsonUtil.validatedNull(jsonAccount, CURRENCYDESCRIPTION)){
                setCtnCurrenDescript(jsonAccount.getString(CURRENCYDESCRIPTION));
            }else
                return false;
        }

        return true;
    }

    private boolean getRspObtainTransaction() throws JSONException {
        //OBJETO TRANSACTION
        JSONArray jsonArray = new JSONArray(getTransactionsPerson());

        if(jsonArray.length()<=0)
            return true;

        ProductsTransactions[] productsTransactions = new ProductsTransactions[jsonArray.length()];

        for(int j = 0; j < jsonArray.length(); j++){
            JSONObject ctn= jsonArray.getJSONObject(j);
            productsTransactions[j] = new ProductsTransactions();

            if (JsonUtil.validatedNull(ctn, DATE))
                productsTransactions[j].setData(ctn.getString(DATE));
            else
                return false;
            if (JsonUtil.validatedNull(ctn, REFERENCE))
                productsTransactions[j].setReference(ctn.getString(REFERENCE));
            else
                return false;
            if (JsonUtil.validatedNull(ctn, AMOUNT))
                productsTransactions[j].setAmount(ctn.getString(AMOUNT));
            else
                return false;
        }

        setTransactions(productsTransactions);

        return true;
    }

}
