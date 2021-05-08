package com.bcp.rest.giros.cobro_nacional.request;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class ReqVerifyBank extends JsonUtil {
    //REQUEST
    private static final String BENEFICIARY = "beneficiary";
    private static final String DOCUMENTTYPE = "documentType";
    private static final String DOCUMENTNUMBER = "documentNumber";

    private String ctnDocumentType;
    private String ctnDocumentNumber;

    public String getCtnDocumentType() {
        return ctnDocumentType;
    }

    public void setCtnDocumentType(String ctnDocumentType) {
        this.ctnDocumentType = ctnDocumentType;
    }

    public String getCtnDocumentNumber() {
        return ctnDocumentNumber;
    }

    public void setCtnDocumentNumber(String ctnDocumentNumber) {
        this.ctnDocumentNumber = ctnDocumentNumber;
    }

    public JSONObject buildsObjectJSON(){

        String documentCipher;

        JSONObject request = new JSONObject();
        JSONObject jsonBeneficiary = new JSONObject();

        try {

            switch (getCtnDocumentType()){
                case "1":
                case "6":
                    funcCipherDocument(getCtnDocumentNumber()+"");
                    documentCipher = cipherDocument.getDocument();
                    break;
                default:
                    documentCipher = jweEncryptDecrypt(getCtnDocumentNumber()+"",false,false) ? jweDataEncryptDecrypt.getDataEncrypt(): "";
                    break;
            }

            jsonBeneficiary.put(DOCUMENTTYPE,getCtnDocumentType());
            jsonBeneficiary.put(DOCUMENTNUMBER,documentCipher);
            request.put(BENEFICIARY,jsonBeneficiary);

        }catch (JSONException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            return null;
        }
        return request;
    }
}
