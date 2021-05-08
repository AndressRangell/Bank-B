package com.bcp.rest.consulta.request;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class ReqExcecuteTransacionsCons extends JsonUtil {

    private static final String QUERYTYPE= "queryType";
    private static final String FAMILYCODE = "familyCode";
    private static final String CURRENCYCODE = "currencyCode";
    private static final String CARD = "card";
    private static final String PINBLOCK = "pinblock";
    private static final String FIELD55 = "field55";
    private static final String ARQC = "ARQC";
    private static final String ACCOUNT = "account";

    private String quetype;
    private String famCode;
    private String currCode;
    private String ctnPinblock;
    private String ctnField55;
    private String ctnArqc;

    public String getQuetype() {
        return quetype;
    }

    public void setQuetype(String quetype) {
        this.quetype = quetype;
    }

    public String getFamCode() {
        return famCode;
    }

    public void setFamCode(String famCode) {
        this.famCode = famCode;
    }

    public String getCurrCode() {
        return currCode;
    }

    public void setCurrCode(String currCode) {
        this.currCode = currCode;
    }

    public String getCtnPinblock() {
        return ctnPinblock;
    }

    public void setCtnPinblock(String ctnPinblock) {
        this.ctnPinblock = ctnPinblock;
    }

    public String getCtnField55() {
        return ctnField55;
    }

    public void setCtnField55(String ctnField55) {
        this.ctnField55 = ctnField55;
    }

    public String getCtnArqc() {
        return ctnArqc;
    }

    public void setCtnArqc(String ctnArqc) {
        this.ctnArqc = ctnArqc;
    }

    public JSONObject buildsObjectJSON(){
        JSONObject jsonRequest = new JSONObject();
        JSONObject account = new JSONObject();
        JSONObject jsonCard = new JSONObject();

        try {

            //Request
            jsonRequest.put(QUERYTYPE, getQuetype());
            account.put(FAMILYCODE,getFamCode());
            account.put(CURRENCYCODE,getCurrCode());

            //Card
            jsonCard.put(PINBLOCK,getCtnPinblock());
            jsonCard.put(FIELD55,getCtnField55());
            jsonCard.put(ARQC,getCtnArqc());

            jsonRequest.put(ACCOUNT,account);
            jsonRequest.put(CARD,jsonCard);

        }catch (JSONException e){
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " ReqExecuteTransCons " +  e.getMessage());
            return null;
        }

        Logger.debug("==JSON ExecuteTrans ==");
        Logger.debug(String.valueOf(jsonRequest));
        Logger.debug("====================");

        return jsonRequest;
    }
}
