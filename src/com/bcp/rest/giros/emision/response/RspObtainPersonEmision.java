package com.bcp.rest.giros.emision.response;

import com.bcp.rest.JsonUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class RspObtainPersonEmision extends JsonUtil {

    private static final String PERSON = "person";
    private static final String NAME = "name";
    private static final String SESSION_ID = "sessionId";

    private String rsp_person;
    private String rspname;
    private String rspsessionId;

    public String getRsp_person() {
        return rsp_person;
    }

    public void setRsp_person(String rsp_person) {
        this.rsp_person = rsp_person;
    }

    public String getRspname() {
        return rspname;
    }

    public void setRspname(String rspname) {
        this.rspname = rspname;
    }

    public String getRspsessionId() {
        return rspsessionId;
    }

    public void setRspsessionId(String rspsessionId) {
        this.rspsessionId = rspsessionId;
    }

    /**
     * realiza el parsin del objeto JSON recibido.
     * @param json
     * @return retorna el objeto con los datos JSON interpretados de la respuesta.
     */

    public boolean getRspobtainPerson(JSONObject json, Map<String,String> header) throws JSONException {


        if(JsonUtil.validatedNull(json, PERSON))
            setRsp_person(json.getString(PERSON));

        if (getRsp_person() != null && !getRsp_person().equals("")){
            JSONObject jsonPerson = new JSONObject(getRsp_person());

            if(JsonUtil.validatedNull(jsonPerson, NAME))
                setRspname(jweEncryptDecrypt(jsonPerson.getString(NAME) + "", false, true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
            else
                return false;
        }

        if(header != null && !header.isEmpty())
            setRspsessionId(header.get(SESSION_ID));
        else
            return false;

        return true;
    }
}
