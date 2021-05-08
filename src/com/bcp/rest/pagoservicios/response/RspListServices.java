package com.bcp.rest.pagoservicios.response;

import com.bcp.rest.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class RspListServices {

    private static final String SERVICES = "services";
    private static final String SERVICEDESCRIPTION = "serviceDescription";
    private static final String AFFILIATIONCODE = "affiliationCode";
    private static final String SERVICEINPUTDESCRIPTION = "serviceInputDescription";
    private static final String CURRENCYSYMBOL = "currencySymbol";
    private static final String SERVICETYPE = "serviceType";
    private static final String DOCUMENTSTATUS = "documentStatus";
    private static final String SESSIONID = "sessionId";

    private String accountServices;

    private ListsServices[] services;
    private String sesionId;

    public ListsServices[] getServices() {
        return services;
    }

    public void setServices(ListsServices[] services) {
        this.services = services;
    }

    public String getAccountServices() {
        return accountServices;
    }

    public void setAccountServices(String accountServices) {
        this.accountServices = accountServices;
    }

    public String getSesionId() {
        return sesionId;
    }

    private void setSesionId(String sesionId) {
        this.sesionId = sesionId;
    }

    public boolean getRspObtain(JSONObject json, Map<String,String> header) throws JSONException{

        if (JsonUtil.validatedNull(json, SERVICES))
            setAccountServices(json.getString(SERVICES));
        else
            return false;

        JSONArray jsonArray = new JSONArray(getAccountServices());

        if (jsonArray.length()<=0)
            return false;

        ListsServices[] listsServices = new ListsServices[jsonArray.length()];

        for (int j = 0; j < jsonArray.length(); j++){
            JSONObject ctn = jsonArray.getJSONObject(j);
            listsServices[j] = new ListsServices();

            if (JsonUtil.validatedNull(ctn, SERVICEDESCRIPTION))
                listsServices[j].setServiceDescription(ctn.getString(SERVICEDESCRIPTION));
            else
                return false;

            if (JsonUtil.validatedNull(ctn, AFFILIATIONCODE))
                listsServices[j].setAffiliationCode(ctn.getString(AFFILIATIONCODE));
            else
                return false;

            if (JsonUtil.validatedNull(ctn, SERVICEINPUTDESCRIPTION))
                listsServices[j].setServiceInputDescription(ctn.getString(SERVICEINPUTDESCRIPTION));
            else
                return false;

            if (JsonUtil.validatedNull(ctn, CURRENCYSYMBOL))
                listsServices[j].setCurrencySymbol(ctn.getString(CURRENCYSYMBOL));
            else
                return false;

            if (JsonUtil.validatedNull(ctn, SERVICETYPE))
                listsServices[j].setServiceType(ctn.getString(SERVICETYPE));
            else
                return false;

            if (JsonUtil.validatedNull(ctn, DOCUMENTSTATUS))
                listsServices[j].setDocumentStatus(ctn.getString(DOCUMENTSTATUS));
            else
                return false;
            
        }

        setServices(listsServices);

        if(header != null && !header.isEmpty())
            setSesionId(header.get(SESSIONID));
        else
            return false;

        return true;
    }
}
