package com.bcp.rest.retiro.request;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReqObtainDoc extends JsonUtil {
    private static final String QUERY_PARAMETERS = "queryParameters";

    private String queryParameters;

    private String track2;

    public String getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(String queryParameters) {
        this.queryParameters = queryParameters;
    }

    private String getTrack2() {
        return track2;
    }

    public void setTrack2(String track2) {
        this.track2 = track2;
    }

    public JSONObject buildsObjectJSON(){

        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();

        try {

            array.put(getTrack2());
            jsonObject.put(QUERY_PARAMETERS, array);

        } catch (JSONException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "buildsObjectJSON" + e.getMessage());
            return null;
        }

        Logger.debug("==JSON Request Obtain Document==");
        Logger.debug(String.valueOf(jsonObject));
        Logger.debug("====================");

        return jsonObject;
    }
}
