package com.bcp.rest.pagoservicios.response;

import com.bcp.rest.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class RspListServiceImports {

    private static final String IMPORTS = "imports";
    private static final String IMPORTCODE = "importCode";
    private static final String IMPORTNAME = "importName";
    private static final String AMOUNT = "amount";
    private static final String AMOUNTCURRENCYSYMBOL = "amountCurrencySymbol";

    //SESION_ID
    private static final String SESION_ID = "sessionId";

    ListsDebt[] listImport;

    private String imports;
    private String importCode;
    private String importName;
    private String amount;
    private String amountCurrencySymbol;
    private String sesionId;

    public String getImports() {
        return imports;
    }

    public void setImports(String imports) {
        this.imports = imports;
    }

    public String getImportCode() {
        return importCode;
    }

    public void setImportCode(String importCode) {
        this.importCode = importCode;
    }

    public String getImportName() {
        return importName;
    }

    public void setImportName(String importName) {
        this.importName = importName;
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

    public ListsDebt[] getListImport() {
        return listImport;
    }

    public void setListImport(ListsDebt[] listImport) {
        this.listImport = listImport;
    }

    public String getSesionId() {
        return sesionId;
    }

    public void setSesionId(String sesionId) {
        this.sesionId = sesionId;
    }

    public boolean getRspListImports(JSONObject json, Map<String,String> header)throws JSONException{

        if (JsonUtil.validatedNull(json, IMPORTS))
            setImports(json.getString(IMPORTS));
        else
            return false;

        JSONArray jsonlistDebst = new JSONArray(getImports());

        if (jsonlistDebst.length()<=0)
            return false;

        ListsDebt[] listImportArray = new ListsDebt[jsonlistDebst.length()];

        for (int j = 0; j < jsonlistDebst.length(); j++) {
            JSONObject ctn = jsonlistDebst.getJSONObject(j);
            listImportArray[j] = new ListsDebt();


            if (JsonUtil.validatedNull(ctn,IMPORTCODE))
                listImportArray[j].setImportCode(ctn.getString(IMPORTCODE));
            else
                return false;

            if (JsonUtil.validatedNull(ctn,IMPORTNAME))
                listImportArray[j].setImportName(ctn.getString(IMPORTNAME));
            else
                return false;

            if (JsonUtil.validatedNull(ctn,AMOUNT))
                listImportArray[j].setAmount(ctn.getString(AMOUNT));
            else
                return false;

            if (JsonUtil.validatedNull(ctn,AMOUNTCURRENCYSYMBOL))
                listImportArray[j].setAmountCurrencySymbol(ctn.getString(AMOUNTCURRENCYSYMBOL));
            else
                return false;

        }
        setListImport(listImportArray);

       /* if(header != null && !header.isEmpty())
            setSesionId(header.get(SESION_ID));
        else
            return false;*/

        return true;
    }
}
