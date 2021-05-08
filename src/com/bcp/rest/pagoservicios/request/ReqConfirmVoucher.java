package com.bcp.rest.pagoservicios.request;

import com.newpos.libpay.Logger;

import org.json.JSONObject;

public class ReqConfirmVoucher {

    //SIN RANGO, SIN VALIDACION SOLO UTILIZA LOS DOS SIGUIENTES
    private static final String PRINTDECISION = "printDecision";
    private static final String PRINTSTATUS = "printStatus";

    //Esto es solo para tarjeta
    private static final String ARPCSTATUS = "arpcStatus";
    private static final String TC = "tc";
    private static final String AAC = "aac";


    private String printDecision;
    private String printStatus;

    //Esto es solo para tarjeta
    private String arpcstatus;
    private String tc;
    private String aac;

    public String getPrintDecision() {
        return printDecision;
    }

    public void setPrintDecision(String printDecision) {
        this.printDecision = printDecision;
    }

    public String getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(String printStatus) {
        this.printStatus = printStatus;
    }

    public String getArpcstatus() {
        return arpcstatus;
    }

    public void setArpcstatus(String arpcstatus) {
        this.arpcstatus = arpcstatus;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getAac() {
        return aac;
    }

    public void setAac(String aac) {
        this.aac = aac;
    }

    public JSONObject buildsJsonObject(boolean isCard){
        JSONObject jsonRequest = new JSONObject();

        try {

            //REQUEST
            jsonRequest.put(PRINTDECISION, getPrintDecision());
            jsonRequest.put(PRINTSTATUS, getPrintStatus());

            //Esto es solo para tarjeta
            jsonRequest.put(ARPCSTATUS, getArpcstatus());
            jsonRequest.put(TC, getTc());
            jsonRequest.put(AAC, getAac());

        } catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }

        Logger.debug("==JSON Request ConfirmVoucher==");
        Logger.debug(String.valueOf(jsonRequest));
        Logger.debug("====================");

        return jsonRequest;
    }
}
