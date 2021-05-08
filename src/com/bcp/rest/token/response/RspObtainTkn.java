package com.bcp.rest.token.response;

import com.bcp.rest.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class RspObtainTkn {

    private static final String ACCES_TOKEN = "access_token";
    private static final String TOKEN_TYPE = "token_type";
    private static final String EXPIRES_IN = "expires_in";
    private static final String SCOPE = "scope";

    private String accesToken;
    private String tokenType;
    private int expiresIn;
    private String scopeC;

    public String getAccesToken() {
        return accesToken;
    }

    public void setAccesToken(String accesToken) {
        this.accesToken = accesToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getScopeC() {
        return scopeC;
    }

    public void setScopeC(String scopeC) {
        this.scopeC = scopeC;
    }

    /**
     * realiza el parsin del objeto JSON recibido.
     * @param json
     * @return retorna el objeto con los datos JSON interpretados de la respuesta.
     */
    public boolean getRspObtainTkn(JSONObject json) throws JSONException {

        if(JsonUtil.validatedNull(json, ACCES_TOKEN))
            setAccesToken(json.getString(ACCES_TOKEN));
        else
            return false;

        if(JsonUtil.validatedNull(json, TOKEN_TYPE))
            setTokenType(json.getString(TOKEN_TYPE));
        else
            return false;

        if(JsonUtil.validatedNull(json, EXPIRES_IN))
            setExpiresIn(json.getInt(EXPIRES_IN));
        else
            return false;

        if(JsonUtil.validatedNull(json, SCOPE))
            setScopeC(json.getString(SCOPE));
        else
            return false;

        return true;
    }
}
