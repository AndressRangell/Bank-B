package com.bcp.rest.pagoservicios.response;

import com.bcp.rest.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class RspListServiceDocuments {

    private static final String DOCUMENTS = "documents";
    private static final String DOCUMENTCODE = "documentCode";
    private static final String DOCUMENTDESCRIPTION = "documentDescription";

    private String documents;
    private String documentCode;
    private String documentDescription;

    ListsDebt[] listDocument;


    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public String getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    public ListsDebt[] getListDocument() {
        return listDocument;
    }

    public void setListDocument(ListsDebt[] listDocument) {
        this.listDocument = listDocument;
    }

    public boolean getRspListSerDocu(JSONObject json, Map<String,String> header)throws JSONException{

        if (JsonUtil.validatedNull(json, DOCUMENTS))
            setDocuments(json.getString(DOCUMENTS));
        else
            return false;
        JSONArray jsonDoc = new JSONArray(getDocuments());

        if (jsonDoc.length()<=0)
            return false;

        ListsDebt[] arrayDocument = new ListsDebt[jsonDoc.length()];

        for (int j = 0; j < jsonDoc.length(); j++) {
            JSONObject ctn = jsonDoc.getJSONObject(j);
            arrayDocument[j] = new ListsDebt();


            if (JsonUtil.validatedNull(ctn,DOCUMENTCODE))
                arrayDocument[j].setDocumentCode(ctn.getString(DOCUMENTCODE));
            else
                return false;

            if (JsonUtil.validatedNull(ctn,DOCUMENTDESCRIPTION))
                arrayDocument[j].setDocumentDescription(ctn.getString(DOCUMENTDESCRIPTION));
            else
                return false;

        }
        setListDocument(arrayDocument);

        return true;
    }

}
