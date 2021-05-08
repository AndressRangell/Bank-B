package com.bcp.rest.giros.cobro_nacional.request;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class ReqEndSession extends JsonUtil {

    //REQUEST
    private static final String CATEGORY = "category";
    private static final String REASONDETAIL = "reasonDetail";

    private String reqCategory;
    private String reqReasonDetail;

    public String getReqCategory() {
        return reqCategory;
    }

    public void setReqCategory(String reqCategory) {
        this.reqCategory = reqCategory;
    }

    public String getReqReasonDetail() {
        return reqReasonDetail;
    }

    public void setReqReasonDetail(String reqReasonDetail) {
        this.reqReasonDetail = reqReasonDetail;
    }

    public JSONObject buildsObjectJSON() {

        JSONObject request = new JSONObject();

        try {
            if (!getReqCategory().equals("") && !getReqReasonDetail().equals("")){
                request.put(CATEGORY,getReqCategory());
                request.put(REASONDETAIL,getReqReasonDetail());
            }
        } catch (JSONException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " ReqEndSession " +  e.getMessage());
            return null;
        }
        Logger.debug("==JSON Request Verify Deposit==");
        Logger.debug(String.valueOf(request));
        Logger.debug("====================");

        return request;
    }
}
