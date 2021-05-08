package com.bcp.rest.consulta.request;

import com.newpos.libpay.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ReqListProductsCons {

    private static final String TRACK2= "track2";
    private static final String CARD= "card";

    private String deviceReference;
    private String opnNumber;
    private String deviceIp;
    private String deviceMac;
    private String trackDos;



    public String getDeviceReference() {
        return deviceReference;
    }

    public void setDeviceReference(String deviceReference) {
        this.deviceReference = deviceReference;
    }

    public String getOpnNumber() {
        return opnNumber;
    }

    public void setOpnNumber(String opnNumber) {
        this.opnNumber = opnNumber;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public String getTrackDos() {
        return trackDos;
    }

    public void setTrackDos(String trackDos) {
        this.trackDos = trackDos;
    }


    public JSONObject builJsonObject(){
        JSONObject jsonRequest= new JSONObject();
        JSONObject card= new JSONObject();

        try {

            card.put(TRACK2, getTrackDos());
            jsonRequest.put(CARD,card);

        } catch (JSONException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " ReqListProductsCons " +  e.getMessage());
            return null;
        }

        Logger.debug("==JSON Request List Products==");
        Logger.debug(String.valueOf(jsonRequest));
        Logger.debug("====================");

        return jsonRequest;
    }
}
