package com.bcp.rest.token.request;

import com.newpos.libpay.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ReqObtainTkn {

    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String SCOPE = "scope";

    private String grantType;
    private String clientId;
    private String clientSecret;
    private String scopeC;

    private int lenData;

    public ReqObtainTkn(){
        this.lenData = 0;
    }

    public int getLenData() {
        try {
            lenData += grantType.length();
            lenData += clientId.length();
            lenData += clientSecret.length();
            lenData += scopeC.length();
        }
        catch (Exception e){
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " ReqObtainTkn " +  e.getMessage());
            return 0;
        }

        return lenData;
    }

    public String getGrantType(){
        return grantType;
    }

    public void setGrantType(String valGrantType){
        this.grantType = valGrantType;
    }

    public String getClientId(){
        return clientId;
    }

    public void setClientId(String valClientId){
        this.clientId = valClientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String valClientSecret){
        this.clientSecret = valClientSecret;
    }

    public String getScopeC(){
        return scopeC;
    }

    public void setScopeC(String valScope){
        this.scopeC = valScope;
    }

    public JSONObject buildsObjectJSON(){

        JSONObject jsonObject = new JSONObject();
        JSONObject header = new JSONObject();

        try {

            header.put(GRANT_TYPE, getGrantType());
            header.put(CLIENT_ID, getClientId());
            header.put(CLIENT_SECRET, getClientSecret());
            header.put(SCOPE, getScopeC());

            jsonObject.put("request", header);

        } catch (JSONException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " ReqObtainTkn 1 " +  e.getMessage());
            return null;
        }

        Logger.debug("==JSON Request Obtain Token==");
        Logger.debug(String.valueOf(jsonObject));
        Logger.debug("====================");

        return jsonObject;
    }
}
