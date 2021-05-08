package com.bcp.rest.generic.request;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class ReqConfirmVoucher extends JsonUtil {

    //REQUEST
    private static final String ARPCSTATUS = "arpcStatus";
    private static final String PRINTDECISION = "printDecision";
    private static final String PRINTSTATUS = "printStatus";
    private static final String PRINTOPTION = "printOption";
    private static final String TC = "tc";
    private static final String AAC = "aac";

    private String ctnArpcStatus;
    private String ctnPrintOption;
    private String ctnPrintDecision;
    private String ctnPrintStatus;
    private String ctnTc;
    private String ctnAac;

    private String getCtnArpcStatus() {
        return ctnArpcStatus;
    }

    public void setCtnArpcStatus(String ctnArpcStatus) {
        this.ctnArpcStatus = ctnArpcStatus;
    }

    public String getCtnPrintOption() {
        return ctnPrintOption;
    }

    public void setCtnPrintOption(String ctnPrintOption) {
        this.ctnPrintOption = ctnPrintOption;
    }

    private String getCtnPrintDecision() {
        return ctnPrintDecision;
    }

    public void setCtnPrintDecision(String ctnPrintDecision) {
        this.ctnPrintDecision = ctnPrintDecision;
    }

    private String getCtnPrintStatus() {
        return ctnPrintStatus;
    }

    public void setCtnPrintStatus(String ctnPrintStatus) {
        this.ctnPrintStatus = ctnPrintStatus;
    }

    private String getCtnTc() {
        return ctnTc;
    }

    public void setCtnTc(String ctnTc) {
        this.ctnTc = ctnTc;
    }

    private String getCtnAac() {
        return ctnAac;
    }

    public void setCtnAac(String ctnAac) {
        this.ctnAac = ctnAac;
    }

    public JSONObject buildsObjectJSON(boolean isCard){
        JSONObject jsonRequest = new JSONObject();

        try {

            //REQUEST
            jsonRequest.put(ARPCSTATUS, getCtnArpcStatus());
            if (getCtnPrintOption() != null)
                jsonRequest.put(PRINTOPTION, getCtnPrintOption());
            jsonRequest.put(PRINTDECISION, getCtnPrintDecision());
            jsonRequest.put(PRINTSTATUS, getCtnPrintStatus());
            if (isCard){
                jsonRequest.put(TC, getCtnTc());
                jsonRequest.put(AAC, getCtnAac());
            }


        } catch (JSONException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " ReqConfirmVoucher " +  e.getMessage());
            return null;
        }

        Logger.debug("==JSON Request ConfirmVoucher==");
        Logger.debug(String.valueOf(jsonRequest));
        Logger.debug("====================");

        return jsonRequest;
    }
}
