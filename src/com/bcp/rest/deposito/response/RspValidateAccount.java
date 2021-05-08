package com.bcp.rest.deposito.response;

import com.bcp.rest.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class RspValidateAccount {

    private static final String SESION_ID = "sessionId";
    private static final String FAMILYDESCRIPTION = "familyDescription";
    private static final String CURRENCYDESCRIPTION = "currencyDescription";

    private String sesionId;
    private String productDescription;
    private String currencyDescription;

    public String getSesionId() {
        return sesionId;
    }

    private void setSesionId(String sesionId) {
        this.sesionId = sesionId;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getCurrencyDescription() {
        return currencyDescription;
    }

    public void setCurrencyDescription(String currencyDescription) {
        this.currencyDescription = currencyDescription;
    }

    /**
     * realiza el parsin del objeto JSON recibido.
     * @param json
     * @return retorna el objeto con los datos JSON interpretados de la respuesta.
     */
    public boolean getRspObtainDoc(JSONObject json, Map<String,String> header) throws JSONException {

        if(header != null && !header.isEmpty())
            setSesionId(header.get(SESION_ID));
        else
            return false;

        if(JsonUtil.validatedNull(json, FAMILYDESCRIPTION))
            setProductDescription(json.getString(FAMILYDESCRIPTION));
        else
            return false;

        if(JsonUtil.validatedNull(json, CURRENCYDESCRIPTION))
            setCurrencyDescription(json.getString(CURRENCYDESCRIPTION));
        else
            return false;

        return true;
    }
}
