package com.bcp.rest.deposito.response;

import android.content.Context;

import com.bcp.rest.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class RspVerifyDeposit extends JsonUtil{

    private static final String PERSON = "person";
    private static final String FULLNAME = "fullName";
    private static final String ACCOUNT = "account";
    private static final String ACCOUNTID = "accountId";
    private static final String FAMILYDESCRIPTION = "familyDescription";
    private static final String CURRENCYDESCRIPTION = "currencyDescription";

    private String ctnPerson;
    private String ctnFullName;
    private String objAccount;
    private String ctnAccountId;
    private String ctnFmlDescript;
    private String ctnCurrenDescript;
    private boolean withCard;

    private String getCtnPerson() {
        return ctnPerson;
    }

    private void setCtnPerson(String ctnPerson) {
        this.ctnPerson = ctnPerson;
    }

    public String getCtnFullName() {
        return ctnFullName;
    }

    private void setCtnFullName(String ctnFullName) {
        this.ctnFullName = ctnFullName;
    }

    public String getCtnAccountId() {
        return ctnAccountId;
    }

    private void setCtnAccountId(String ctnAccountId) {
        this.ctnAccountId = ctnAccountId;
    }

    public String getCtnFmlDescript() {
        return ctnFmlDescript;
    }

    private void setCtnFmlDescript(String ctnFmlDescript) {
        this.ctnFmlDescript = ctnFmlDescript;
    }

    public String getCtnCurrenDescript() {
        return ctnCurrenDescript;
    }

    private void setCtnCurrenDescript(String ctnCurrenDescript) {
        this.ctnCurrenDescript = ctnCurrenDescript;
    }

    public void setWithCard(boolean withCard) {
        this.withCard = withCard;
    }

    private String getObjAccount(){
        return objAccount;
    }

    private void setObjAccount(String objAccount){
        this.objAccount = objAccount;
    }

    /**
     * realiza el parsin del objeto JSON recibido.
     * @param json
     * @return retorna el objeto con los datos JSON interpretados de la respuesta.
     */
    public boolean getRspObtainDoc(JSONObject json) throws JSONException {

        if(JsonUtil.validatedNull(json, PERSON)){
            setCtnPerson(json.getString(PERSON));
        }else
            return false;

        JSONObject jsonPerson = new JSONObject(getCtnPerson());

        if(JsonUtil.validatedNull(jsonPerson, FULLNAME)){
            setCtnFullName(jsonPerson.getString(FULLNAME)+"");
        }else
            return false;

        if(JsonUtil.validatedNull(json, ACCOUNT)){
            setObjAccount(json.getString(ACCOUNT));
        }else
            return false;

        return getRspObtainAccount();
    }

    private boolean getRspObtainAccount() throws JSONException {
        //OBJETO ACCOUNT
        JSONObject jsonAccount = new JSONObject(getObjAccount());

        if(JsonUtil.validatedNull(jsonAccount, ACCOUNTID)){
            if (withCard)
                setCtnAccountId(jsonAccount.getString(ACCOUNTID)+"");
            else
                setCtnAccountId(jweEncryptDecrypt(jsonAccount.getString(ACCOUNTID)+"",false,true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
        }else
            return false;

        if(JsonUtil.validatedNull(jsonAccount, FAMILYDESCRIPTION)){
            setCtnFmlDescript(jsonAccount.getString(FAMILYDESCRIPTION));
        }else
            return false;

        if(JsonUtil.validatedNull(jsonAccount, CURRENCYDESCRIPTION)){
            setCtnCurrenDescript(jsonAccount.getString(CURRENCYDESCRIPTION));
        }else
            return false;

        return true;
    }
}
