package com.bcp.rest.pagoservicios.response;

import com.bcp.rest.JsonUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;

public class RspListClientDebts {

    //DEBTS
    //CON RANGO
    private static final String DEBTCODE = "debtCode";
    private static final String DEBTPAYMENTCODE ="debtPaymentCode";
    private static final String MINIMUMAMOUNT = "minimumAmount";
    private static final String MINIMUMAMOUNTCURRENCYSYMBOL = "minimumAmountCurrencySymbol";
    private static final String TOTALAMOUNT = "totalAmount";
    private static final String TOTALAMOUNTCURRENCYSYMBOL = "totalAmountCurrencySymbol";

    private static final String DEBTS = "debts";
    private static final String DUEDATE = "dueDate";
    private static final String AMOUNT = "amount";
    private static final String AMOUNTCURRENCYSYMBOL = "amountCurrencySymbol";

    //RESPONSE
    private static final String COMMISSIONCURRENCYSYMBOL = "commissionCurrencySymbol";
    private static final String COMMISSIONAMOUNT = "commissionAmount";

    //SESION_ID
    private static final String SESION_ID = "sessionId";

    private String commissioncurrencysymbol;
    private String commissionamount;

    private String accountDebt;
    private String sesionId;

    private ListsDebt[] listsDebts;

    public String getCommissioncurrencysymbol() {
        return commissioncurrencysymbol;
    }

    public void setCommissioncurrencysymbol(String commissioncurrencysymbol) {
        this.commissioncurrencysymbol = commissioncurrencysymbol;
    }

    public String getCommissionamount() {
        return commissionamount;
    }

    public void setCommissionamount(String commissionamount) {
        this.commissionamount = commissionamount;
    }

    public String getAccountDebt() {
        return accountDebt;
    }

    public void setAccountDebt(String accountDebt) {
        this.accountDebt = accountDebt;
    }

    public ListsDebt[] getListsDebts() {
        return listsDebts;
    }

    public void setListsDebts(ListsDebt[] listsDebts) {
        this.listsDebts = listsDebts;
    }

    public String getSesionId() {
        return sesionId;
    }

    public void setSesionId(String sesionId) {
        this.sesionId = sesionId;
    }

    public boolean getRspObtain(JSONObject json, Map<String,String> header, String TypePaymentServices, String typePayment) throws JSONException{

        if (JsonUtil.validatedNull(json,DEBTS))
            setAccountDebt(json.getString(DEBTS));
        else
            return false;

        JSONArray jsonArray = new JSONArray(getAccountDebt());

        if (jsonArray.length()<=0)
            return false;

        ListsDebt[] listsDebts = new ListsDebt[jsonArray.length()];

        for (int j = 0; j < jsonArray.length(); j++){
            JSONObject ctn = jsonArray.getJSONObject(j);
            listsDebts[j] = new ListsDebt();

            if (JsonUtil.validatedNull(ctn,DUEDATE))
                listsDebts[j].setDueDate(ctn.getString(DUEDATE));
            else
                return false;

            if(typePayment.equals("2")){
                if (JsonUtil.validatedNull(ctn,DEBTPAYMENTCODE))
                    listsDebts[j].setDebtPaymentCode(ctn.getString(DEBTPAYMENTCODE));

            }

            if (JsonUtil.validatedNull(ctn,DEBTCODE))
                listsDebts[j].setDebtCode(ctn.getString(DEBTCODE));


            if (JsonUtil.validatedNull(ctn,AMOUNT))
                listsDebts[j].setAmount(ctn.getString(AMOUNT));
            else
                return false;

            if (JsonUtil.validatedNull(ctn,AMOUNTCURRENCYSYMBOL))
                listsDebts[j].setAmountCurrencySymbol(ctn.getString(AMOUNTCURRENCYSYMBOL));
            else
                return false;

            //CON RANGO
            if (JsonUtil.validatedNull(ctn, MINIMUMAMOUNT))
                listsDebts[j].setMinimumAmount(ctn.getString(MINIMUMAMOUNT));


            if (JsonUtil.validatedNull(ctn,MINIMUMAMOUNTCURRENCYSYMBOL))
                listsDebts[j].setMinimumAmountCurrencySymbol(ctn.getString(MINIMUMAMOUNTCURRENCYSYMBOL));


            if (JsonUtil.validatedNull(ctn,TOTALAMOUNT))
                listsDebts[j].setTotalAmount(TOTALAMOUNT);


            if (JsonUtil.validatedNull(ctn,TOTALAMOUNTCURRENCYSYMBOL))
                listsDebts[j].setTotalAmountCurrencySymbol(ctn.getString(TOTALAMOUNTCURRENCYSYMBOL));
            //CON RANGO
        }

        //DIFERENTE A CON RANGO
        if (JsonUtil.validatedNull(json, COMMISSIONCURRENCYSYMBOL))
            setCommissioncurrencysymbol(json.getString(COMMISSIONCURRENCYSYMBOL));


        if (JsonUtil.validatedNull(json, COMMISSIONAMOUNT))
            setCommissionamount(json.getString(COMMISSIONAMOUNT));


        setListsDebts(listsDebts);

        if(header != null && !header.isEmpty())
            setSesionId(header.get(SESION_ID));
        else
            return false;

        return true;
    }


}
