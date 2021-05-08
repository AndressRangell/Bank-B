package com.bcp.rest.ultimasoperaciones.response;

import com.bcp.rest.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class RspViewLastOperations extends JsonUtil {

    private static final String TRANSACTIONDATE = "transactionDate";
    private static final String TRANSACTIONTIME = "transactionTime";
    private static final String TRANSACTIONNUMBER = "transactionNumber";
    private static final String THREELASTOPERATIONS = "threeLastOperations";
    private static final String ROWS = "rows";
    private static final String CONTENT = "content";
    private static final String LASTOPERATION = "lastOperation";
    private static final String SESION_ID = "sessionId";


    private String transactionDate;
    private String transactionTime;
    private String transactionNumber;

    private String threeLastOperations;
    private String rowsTheeLast;
    private String contentTheeLast;

    private String lastOperation;
    private String rows;
    private String content;
    private String sesionId;

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getThreeLastOperations() {
        return threeLastOperations;
    }

    public void setThreeLastOperations(String threeLastOperations) {
        this.threeLastOperations = threeLastOperations;
    }

    public String getRowsTheeLast() {
        return rowsTheeLast;
    }

    public void setRowsTheeLast(String rowsTheeLast) {
        this.rowsTheeLast = rowsTheeLast;
    }

    public String getContentTheeLast() {
        return contentTheeLast;
    }

    public void setContentTheeLast(String contentTheeLast) {
        this.contentTheeLast = contentTheeLast;
    }

    public String getLastOperation() {
        return lastOperation;
    }

    public void setLastOperation(String lastOperation) {
        this.lastOperation = lastOperation;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSesionId() {
        return sesionId;
    }

    public void setSesionId(String sesionId) {
        this.sesionId = sesionId;
    }

    public boolean getRspOperations(JSONObject json, Map<String, String> header) throws JSONException{

        if (JsonUtil.validatedNull(json, TRANSACTIONDATE))
            setTransactionDate(json.getString(TRANSACTIONDATE));
        else
            return false;

        if (JsonUtil.validatedNull(json,TRANSACTIONTIME))
            setTransactionTime(json.getString(TRANSACTIONTIME));
        else
            return false;

        if (JsonUtil.validatedNull(json,TRANSACTIONNUMBER))
            setTransactionNumber(json.getString(TRANSACTIONNUMBER));
        else
            return false;

        if (!getRspTheeLastOperations(json))
            return false;

        if (!getRspLastOperations(json))
            return false;

        if(header != null && !header.isEmpty())
            setSesionId(header.get(SESION_ID));

    return true;
    }

    private boolean getRspTheeLastOperations(JSONObject json) throws JSONException {

        if (JsonUtil.validatedNull(json, THREELASTOPERATIONS))
            setThreeLastOperations(json.getString(THREELASTOPERATIONS));
        else
            return false;

        JSONObject jsonObject = new JSONObject(getThreeLastOperations());

        if (JsonUtil.validatedNull(jsonObject,ROWS))
            setRowsTheeLast(jsonObject.getString(ROWS));
        else
            return false;

        if (JsonUtil.validatedNull(jsonObject,CONTENT))
            setContentTheeLast(jsonObject.getString(CONTENT));
        else
            return false;

        return true;
    }

    private boolean getRspLastOperations(JSONObject json) throws JSONException {

        if (JsonUtil.validatedNull(json, LASTOPERATION))
            setLastOperation(json.getString(LASTOPERATION));
        else
            return false;

        JSONObject jsonObject = new JSONObject(getLastOperation());

        if (JsonUtil.validatedNull(jsonObject,ROWS))
            setRows(jsonObject.getString(ROWS));
        else
            return false;

        if (JsonUtil.validatedNull(jsonObject,CONTENT))
            setContent(jsonObject.getString(CONTENT));
        else
            return false;

        return true;
    }


}
