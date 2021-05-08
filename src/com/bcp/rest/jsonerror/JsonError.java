package com.bcp.rest.jsonerror;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonError {

    private static final String ERROR_TYPE = "errorType";
    private static final String CODE = "code";
    private static final String DESCRIPTION = "description";

    private String errorType;
    private String varCode = "xxx00";
    private String varDescription;

    private String getErrorType() {
        return errorType;
    }

    private void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getCode() {
        return varCode;
    }

    public void setCode(String code) {
        this.varCode = code;
    }

    public String getDescription() {
        return varDescription;
    }

    private void setDescription(String description) {
        this.varDescription = description;
    }

    public boolean getRspObtJson(JSONObject json) throws JSONException {

        if(JsonUtil.validatedNull(json, ERROR_TYPE))
            setErrorType(json.getString(ERROR_TYPE));
        else
            return false;

        switch (getErrorType().toUpperCase()){
            case "TECHNICAL":
            case "TECNICO":

                if(JsonUtil.validatedNull(json,DESCRIPTION))
                    setDescription(json.getString(DESCRIPTION));
                else
                    return false;

                if (JsonUtil.validatedNull(json,CODE))
                    setCode(json.getString(CODE));
                else
                    return false;
                break;
            case "FUNCTIONAL":
            case "FUNCIONAL":
                if(JsonUtil.validatedNull(json,DESCRIPTION)) {
                    String description = json.getString(DESCRIPTION);
                    setDescription(description.length() > 10 ? description.substring(9):description);
                } else
                    return false;
                break;
            default:
                if(JsonUtil.validatedNull(json,DESCRIPTION))
                    setDescription(json.getString(DESCRIPTION));

                if (JsonUtil.validatedNull(json,CODE))
                    setCode(json.getString(CODE));
                break;
        }

        Logger.debug("==JSON Response error==");
        Logger.debug(String.valueOf(json));
        Logger.debug("====================");

        return true;
    }
}
