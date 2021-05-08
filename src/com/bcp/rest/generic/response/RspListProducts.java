package com.bcp.rest.generic.response;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.trans.Trans;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;

public class RspListProducts {

    private static final String ACCOUNT = "accounts";
    private static final String FAMILYCODE = "familyCode";
    private static final String FAMILYDESCRIPTION = "familyDescription";
    private static final String PRODUCTDESCRIPTION = "productDescription";
    private static final String CURRENCYCODE = "currencyCode";
    private static final String CURRENCYDESCRIPTION = "currencyDescription";
    private static final String CURRENCYSYMBOL = "currencySymbol";
    private static final String SESSIONID = "sessionId";

    private String accountPerson;
    private String sesionId;

    private ProductsAccounts[] accounts;

    private String getAccountPerson() {
        return accountPerson;
    }

    private void setAccountPerson(String accountPerson) {
        this.accountPerson = accountPerson;
    }

    public ProductsAccounts[] getAccounts() {
        return accounts;
    }

    private void setAccounts(ProductsAccounts[] accounts) {
        this.accounts = accounts;
    }

    public String getSesionId() {
        return sesionId;
    }

    private void setSesionId(String sesionId) {
        this.sesionId = sesionId;
    }

    /**
     * realiza el parsin del objeto JSON recibido.
     * @param json
     * @return retorna el objeto con los datos JSON interpretados de la respuesta.
     */
    public boolean getRspObtainDoc(JSONObject json, boolean sesionActive, Map<String,String> header,String transEname) throws JSONException {

        if(JsonUtil.validatedNull(json, ACCOUNT))
            setAccountPerson(json.getString(ACCOUNT));
        else
            return false;

        JSONArray jsonArray = new JSONArray(getAccountPerson());

        if (jsonArray.length()<=0)
            return false;

        ProductsAccounts[] productsAccounts = new ProductsAccounts[jsonArray.length()];

        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject ctn = jsonArray.getJSONObject(j);
            productsAccounts[j] = new ProductsAccounts();

            if (JsonUtil.validatedNull(ctn, FAMILYCODE))
                productsAccounts[j].setFamilyCode(ctn.getString(FAMILYCODE));
            else
                return false;

            if(transEname.equals(Trans.PAGOSERVICIOS)){
                if (JsonUtil.validatedNull(ctn, PRODUCTDESCRIPTION))
                    productsAccounts[j].setProductDescrip(ctn.getString(PRODUCTDESCRIPTION));
                else
                    return false;
            }else {

                if (JsonUtil.validatedNull(ctn, FAMILYDESCRIPTION))
                    productsAccounts[j].setProductDescrip(ctn.getString(FAMILYDESCRIPTION));
                else
                    return false;
            }
            if (JsonUtil.validatedNull(ctn, CURRENCYCODE))
                productsAccounts[j].setCurrencyCode(ctn.getString(CURRENCYCODE));
            else
                return false;

            if (JsonUtil.validatedNull(ctn, CURRENCYDESCRIPTION))
                productsAccounts[j].setCurrencyDescrip(ctn.getString(CURRENCYDESCRIPTION));
            else
                return false;

            if (JsonUtil.validatedNull(ctn, CURRENCYSYMBOL))
                productsAccounts[j].setCurrencySymbol(ctn.getString(CURRENCYSYMBOL));

        }

        setAccounts(productsAccounts);

        if (!sesionActive){
            if(header != null && !header.isEmpty())
                setSesionId(header.get(SESSIONID));
            else
                return false;
        }

        return true;
    }
}
