package com.bcp.rest.retiro.response;

import com.bcp.rest.JsonUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class RspObtainDoc {

    private static final String PERSON = "person";
    private static final String DOCUMENT_TYPE = "documentType";
    private static final String SESION_ID = "sessionId";

    private String personData;
    private String documentType;
    private String sesionId;

    public String getPerson() {
        return personData;
    }

    public void setPerson(String person) {
        this.personData = person;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getSesionId() {
        return sesionId;
    }

    public void setSesionId(String sesionId) {
        this.sesionId = sesionId;
    }

    /**
     * realiza el parsin del objeto JSON recibido.
     * @param json
     * @return retorna el objeto con los datos JSON interpretados de la respuesta.
     */
    public boolean getRspObtainDoc(JSONObject json, Map<String,String> header) throws JSONException {

        if(JsonUtil.validatedNull(json, PERSON))
            setPerson(json.getString(PERSON));
        else
            return false;

        JSONObject json2 = new JSONObject(getPerson());

        if(JsonUtil.validatedNull(json2, DOCUMENT_TYPE))
            setDocumentType(json2.getString(DOCUMENT_TYPE));
        else
            return false;

        if(header != null && !header.isEmpty())
            setSesionId(header.get(SESION_ID));


        return true;
    }
}
