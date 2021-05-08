package com.bcp.rest.pagoservicios.response;

import com.bcp.rest.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class RspObtainAmountLimits {

    private static final String MINIMUMAMOUNT = "minimumAmount";
    private static final String MAXIMUMAMOUNT = "maximumAmount";

    private String minimumAmount;
    private String maximumAmount;

    public String getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(String minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public String getMaximumAmount() {
        return maximumAmount;
    }

    public void setMaximumAmount(String maximumAmount) {
        this.maximumAmount = maximumAmount;
    }

    public boolean getObtainAmountLimits(JSONObject json, Map<String,String> header)throws JSONException{

        if (JsonUtil.validatedNull(json, MINIMUMAMOUNT))
            setMinimumAmount(json.getString(MINIMUMAMOUNT));
        else
            return false;

        if (JsonUtil.validatedNull(json, MAXIMUMAMOUNT))
            setMaximumAmount(json.getString(MAXIMUMAMOUNT));
        else
            return false;

        return true;
    }

}
