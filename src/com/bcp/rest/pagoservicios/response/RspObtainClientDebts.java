package com.bcp.rest.pagoservicios.response;

import com.bcp.rest.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class RspObtainClientDebts {

    private static final String DEBTS = "debts";
    private static final String DEBTCODE = "debtCode";
    private static final String DUEDATE = "dueDate";
    private static final String AMOUNT = "amount";
    private static final String AMOUNTCURRENCYSYMBOL = "amountCurrencySymbol";
    private static final String MINIMUMAMOUNT = "minimumAmount";
    private static final String MINIMUMAMOUNTCURRENCYSYMBOL = "minimumAmountCurrencySymbol";
    private static final String TOTALAMOUNT = "totalAmount";
    private static final String TOTALAMOUNTCURRENCYSYMBOL = "totalAmountCurrencySymbol";


    private String debts;
    private String debtCode;
    private String dueDate;
    private String amount;
    private String amountCurrencySymbol;
    private String minimumAmount;
    private String minimumAmountCurrencySymbol;
    private String totalAmount;
    private String totalAmountCurrencySymbol;


    public String getDebts() {
        return debts;
    }

    public void setDebts(String debts) {
        this.debts = debts;
    }

    public String getDebtCode() {
        return debtCode;
    }

    public void setDebtCode(String debtCode) {
        this.debtCode = debtCode;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountCurrencySymbol() {
        return amountCurrencySymbol;
    }

    public void setAmountCurrencySymbol(String amountCurrencySymbol) {
        this.amountCurrencySymbol = amountCurrencySymbol;
    }

    public String getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(String minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public String getMinimumAmountCurrencySymbol() {
        return minimumAmountCurrencySymbol;
    }

    public void setMinimumAmountCurrencySymbol(String minimumAmountCurrencySymbol) {
        this.minimumAmountCurrencySymbol = minimumAmountCurrencySymbol;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalAmountCurrencySymbol() {
        return totalAmountCurrencySymbol;
    }

    public void setTotalAmountCurrencySymbol(String totalAmountCurrencySymbol) {
        this.totalAmountCurrencySymbol = totalAmountCurrencySymbol;
    }

    private boolean getObtainClientDebts(JSONObject json)throws JSONException{

        if (JsonUtil.validatedNull(json, DEBTS))
            setDebts(json.getString(DEBTS));
        else
            return false;

        JSONObject json2 = new JSONObject(getDebts());

        if (JsonUtil.validatedNull(json2, DEBTCODE))
            setDebtCode(json2.getString(DEBTCODE));
        else
            return false;

        if (JsonUtil.validatedNull(json2, DUEDATE))
            setDueDate(json2.getString(DUEDATE));
        else
            return false;

        if (JsonUtil.validatedNull(json2, AMOUNT))
            setAmount(json2.getString(AMOUNT));
        else
            return false;

        if (JsonUtil.validatedNull(json2, AMOUNTCURRENCYSYMBOL))
            setAmountCurrencySymbol(json2.getString(AMOUNTCURRENCYSYMBOL));
        else
            return false;

        if (JsonUtil.validatedNull(json2, MINIMUMAMOUNT))
            setMinimumAmount(json2.getString(MINIMUMAMOUNT));
        else
            return false;

        if (JsonUtil.validatedNull(json2, MINIMUMAMOUNTCURRENCYSYMBOL))
            setMinimumAmountCurrencySymbol(json2.getString(MINIMUMAMOUNTCURRENCYSYMBOL));
        else
            return false;

        if (JsonUtil.validatedNull(json2, TOTALAMOUNT))
            setTotalAmount(json2.getString(TOTALAMOUNT));
        else
            return false;

        if (JsonUtil.validatedNull(json2, TOTALAMOUNTCURRENCYSYMBOL))
            setTotalAmountCurrencySymbol(json2.getString(TOTALAMOUNTCURRENCYSYMBOL));
        else
            return false;

        return true;
    }
}
