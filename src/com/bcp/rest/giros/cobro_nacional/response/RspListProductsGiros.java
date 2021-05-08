package com.bcp.rest.giros.cobro_nacional.response;

import com.bcp.rest.JsonUtil;
import com.bcp.rest.generic.response.ProductsAccounts;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class RspListProductsGiros {

    private static final String REMITTERACCOUNTS = "remitterAccounts";
    private static final String BENEFICIARYACCOUNTS = "beneficiaryAccounts";
    private static final String FAMILYCODE = "familyCode";
    private static final String FAMILYDESCRIPTION = "familyDescription";
    private static final String CURRENCYCODE = "currencyCode";
    private static final String CURRENCYDESCRIPTION = "currencyDescription";
    private static final String CURRENCYSYMBOL = "currencySymbol";
    private static final String SESSION_ID = "sessionId";

    private String rspBeneficiaryAccounts;

    private ProductsAccounts[] listProducts;
    private String rspsessionId;

    public String getRspBeneficiaryAccounts() {
        return rspBeneficiaryAccounts;
    }

    public void setRspBeneficiaryAccounts(String rspBeneficiaryAccounts) {
        this.rspBeneficiaryAccounts = rspBeneficiaryAccounts;
    }

    public ProductsAccounts[] getListProducts() {
        return listProducts;
    }

    public void setListProducts(ProductsAccounts[] listProducts) {
        this.listProducts = listProducts;
    }

    public String getRspsessionId() {
        return rspsessionId;
    }

    public void setRspsessionId(String rspsessionId) {
        this.rspsessionId = rspsessionId;
    }

    /**
     * realiza el parsin del objeto JSON recibido.
     * @param json
     * @return retorna el objeto con los datos JSON interpretados de la respuesta.
     */

    public boolean getRspListProducts(JSONObject json, int typeTransaction, Map<String,String> header) throws JSONException {

        if(typeTransaction == 1){

            if(JsonUtil.validatedNull(json, REMITTERACCOUNTS))
                setRspBeneficiaryAccounts(json.getString(REMITTERACCOUNTS));
            else
                return false;
        }else {

            if(JsonUtil.validatedNull(json, BENEFICIARYACCOUNTS))
                setRspBeneficiaryAccounts(json.getString(BENEFICIARYACCOUNTS));
            else
                return false;
        }

        JSONArray jsonArray = new JSONArray(getRspBeneficiaryAccounts());

        if (jsonArray.length()<=0)
            return false;

        ProductsAccounts[] ArrayListProducts = new ProductsAccounts[jsonArray.length()];

        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject ctn = jsonArray.getJSONObject(j);
            ArrayListProducts[j] = new ProductsAccounts();

            if (JsonUtil.validatedNull(ctn, FAMILYCODE))
                ArrayListProducts[j].setFamilyCode(ctn.getString(FAMILYCODE));
            else
                return false;

            if (JsonUtil.validatedNull(ctn, FAMILYDESCRIPTION))
                ArrayListProducts[j].setProductDescrip(ctn.getString(FAMILYDESCRIPTION));
            else
                return false;

            if (JsonUtil.validatedNull(ctn, CURRENCYCODE))
                ArrayListProducts[j].setCurrencyCode(ctn.getString(CURRENCYCODE));
            else
                return false;

            if (JsonUtil.validatedNull(ctn, CURRENCYDESCRIPTION))
                ArrayListProducts[j].setCurrencyDescrip(ctn.getString(CURRENCYDESCRIPTION));
            else
                return false;

            if (JsonUtil.validatedNull(ctn, CURRENCYSYMBOL))
                ArrayListProducts[j].setCurrencySymbol(ctn.getString(CURRENCYSYMBOL));

        }

        setListProducts(ArrayListProducts);

        if(header != null && !header.isEmpty())
            setRspsessionId(header.get(SESSION_ID));
        else
            return false;

        return true;
    }
}
