package com.bcp.rest.giros.cobro_nacional.request;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ReqConfirmVoucher extends JsonUtil {

    //REQUEST
    private static final String ARPCSTATUS = "arpcStatus";
    private static final String PRINTDECISION = "printDecision";
    private static final String PRINTSTATUS = "printStatus";


    private String reqArpcStatusd;
    private String reqPrintDecision;
    private String reqPrintStatus;

    public String getReqArpcStatusd() {
        return reqArpcStatusd;
    }

    public void setReqArpcStatusd(String reqArpcStatusd) {
        this.reqArpcStatusd = reqArpcStatusd;
    }

    public String getReqPrintDecision() {
        return reqPrintDecision;
    }

    public void setReqPrintDecision(String reqPrintDecision) {
        this.reqPrintDecision = reqPrintDecision;
    }

    public String getReqPrintStatus() {
        return reqPrintStatus;
    }

    public void setReqPrintStatus(String reqPrintStatus) {
        this.reqPrintStatus = reqPrintStatus;
    }

    public JSONObject buildsObjectJSON() {

        JSONObject request = new JSONObject();
        JSONObject jsonArpcStatus = new JSONObject();
        JSONObject jsonPrintDecision = new JSONObject();
        JSONObject jsonPrintStatus = new JSONObject();

        try {

            //ARPCSTATUS
            jsonArpcStatus.put(ARPCSTATUS, getReqArpcStatusd());
            request.put(ARPCSTATUS,jsonArpcStatus);

            //PRINTDECISION
            jsonPrintDecision.put(PRINTDECISION, getReqPrintDecision());
            request.put(PRINTDECISION,jsonPrintDecision);

            //PRINTSTATUS
            jsonPrintStatus.put(PRINTSTATUS, getReqPrintStatus());
            request.put(PRINTSTATUS,jsonPrintStatus);

        } catch (JSONException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " ReqConfirmVoucher " +  e.getMessage());
            return null;
        }
        Logger.debug("==JSON Request Verify Deposit==");
        Logger.debug(String.valueOf(request));
        Logger.debug("====================");

        return request;
    }
}
