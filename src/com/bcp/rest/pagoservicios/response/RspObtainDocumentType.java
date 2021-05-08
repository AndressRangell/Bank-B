package com.bcp.rest.pagoservicios.response;

import com.bcp.rest.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class RspObtainDocumentType {

    private static final String PERSON = "person";
    private static final String DOCUMENTTYPE = "documentType";

    private String person;
    private String documenttype;

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getDocumenttype() {
        return documenttype;
    }

    public void setDocumenttype(String documenttype) {
        this.documenttype = documenttype;
    }

    private boolean getObtain(JSONObject json) throws JSONException{

        if (JsonUtil.validatedNull(json, PERSON))
            setPerson(json.getString(PERSON));
        else
            return false;

        JSONObject json2 = new JSONObject(getPerson());

        if (JsonUtil.validatedNull(json2, DOCUMENTTYPE))
            setDocumenttype(json2.getString(DOCUMENTTYPE));
        else
            return false;

        return true;
    }
}
