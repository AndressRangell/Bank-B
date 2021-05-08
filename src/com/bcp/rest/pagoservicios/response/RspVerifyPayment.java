package com.bcp.rest.pagoservicios.response;

import com.bcp.rest.JsonUtil;
import com.newpos.libpay.trans.Trans;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class RspVerifyPayment extends JsonUtil{

    //VARIABLES EN GENERAL
    private static final String COMPANYNAME = "companyName";
    private static final String SERVICEDESCRIPTION = "serviceDescription";

    //SOLO PARA PS IMPORTE EFECTIVO
    private static final String IMPORTNAME = "importName";

    //NO SE USA EN PS IMPORTE Y SIN VALIDACION
    private static final String CLIENTDEPOSITNAME = "clientDepositName";

    private static final String CLIENTDEPOSITCODE = "clientDepositCode";
    private static final String AMOUNTCURRENCYSYMBOL = "amountCurrencySymbol";
    private static final String AMOUNT = "amount";
    private static final String COMMISSIONCURRENCYSYMBOL = "commissionCurrencySymbol";
    private static final String COMMISSIONAMOUNT = "commissionAmount";
    private static final String EXTRACHARGEAMOUNTCURRENCYSYMBOL = "extraChargeAmountCurrencySymbol";
    private static final String EXTRACHARGEAMOUNT = "extraChargeAmount";
    private static final String TOTALAMOUNTCURRENCYSYMBOL = "totalAmountCurrencySymbol";
    private static final String TOTALAMOUNT = "totalAmount";
    private static final String EXCHANGESTATUS = "exchangeStatus";

    //SE ENVIAN CUANDO ES CON TARJETA ACCOUNT
    private static final String ACCOUNT = "account";
    private static final String FAMILYDESCRIPTION = "familyDescription";
    private static final String CURRENCYDESCRIPTION = "currencyDescription";
    private static final String CURRENCYSYMBOL = "currencySymbol";

    //SESSION_ID
    private static final String SESION_ID = "sessionId";

    private String rspcompanyName;
    private String rspserviceDescription;
    private String rspclientDepositCode;
    private String rspclientDepositName;
    private String rspamountCurrencySymbol;
    private String rspamount;
    private String rspcommissionCurrencySymbol;
    private String rspcommissionAmount;
    private String rspextraChargeAmountCurrencySymbol;
    private String rspextraChargeAmount;
    private String rsptotalAmountCurrencySymbol;
    private String rsptotalAmount;
    private String rspexchangeStatus;

    //SOLO PARA PS IMPORTE EFECTIVO
    private String rspimportName;

    //SE ENVIAN CUANDO ES CON TARJETA ACCOUNT
    private String rspaccount;
    private String rspfamilyDescription;
    private String rspcurrencyDescription;
    private String rspcurrencySymbol;

    //SESSION_ID
    private String sesionId;


    public String getRspcompanyName() {
        return rspcompanyName;
    }

    public void setRspcompanyName(String rspcompanyName) {
        this.rspcompanyName = rspcompanyName;
    }

    public String getRspserviceDescription() {
        return rspserviceDescription;
    }

    public void setRspserviceDescription(String rspserviceDescription) {
        this.rspserviceDescription = rspserviceDescription;
    }

    public String getRspclientDepositCode() {
        return rspclientDepositCode;
    }

    public void setRspclientDepositCode(String rspclientDepositCode) {
        this.rspclientDepositCode = rspclientDepositCode;
    }

    public String getRspclientDepositName() {
        return rspclientDepositName;
    }

    public void setRspclientDepositName(String rspclientDepositName) {
        this.rspclientDepositName = rspclientDepositName;
    }

    public String getRspamountCurrencySymbol() {
        return rspamountCurrencySymbol;
    }

    public void setRspamountCurrencySymbol(String rspamountCurrencySymbol) {
        this.rspamountCurrencySymbol = rspamountCurrencySymbol;
    }

    public String getRspamount() {
        return rspamount;
    }

    public void setRspamount(String rspamount) {
        this.rspamount = rspamount;
    }

    public String getRspcommissionCurrencySymbol() {
        return rspcommissionCurrencySymbol;
    }

    public void setRspcommissionCurrencySymbol(String rspcommissionCurrencySymbol) {
        this.rspcommissionCurrencySymbol = rspcommissionCurrencySymbol;
    }

    public String getRspcommissionAmount() {
        return rspcommissionAmount;
    }

    public void setRspcommissionAmount(String rspcommissionAmount) {
        this.rspcommissionAmount = rspcommissionAmount;
    }

    public String getRspextraChargeAmountCurrencySymbol() {
        return rspextraChargeAmountCurrencySymbol;
    }

    public void setRspextraChargeAmountCurrencySymbol(String rspextraChargeAmountCurrencySymbol) {
        this.rspextraChargeAmountCurrencySymbol = rspextraChargeAmountCurrencySymbol;
    }

    public String getRspextraChargeAmount() {
        return rspextraChargeAmount;
    }

    public void setRspextraChargeAmount(String rspextraChargeAmount) {
        this.rspextraChargeAmount = rspextraChargeAmount;
    }

    public String getRsptotalAmountCurrencySymbol() {
        return rsptotalAmountCurrencySymbol;
    }

    public void setRsptotalAmountCurrencySymbol(String rsptotalAmountCurrencySymbol) {
        this.rsptotalAmountCurrencySymbol = rsptotalAmountCurrencySymbol;
    }

    public String getRsptotalAmount() {
        return rsptotalAmount;
    }

    public void setRsptotalAmount(String rsptotalAmount) {
        this.rsptotalAmount = rsptotalAmount;
    }

    public String getRspexchangeStatus() {
        return rspexchangeStatus;
    }

    public void setRspexchangeStatus(String rspexchangeStatus) {
        this.rspexchangeStatus = rspexchangeStatus;
    }

    public String getRspimportName() {
        return rspimportName;
    }

    public void setRspimportName(String rspimportName) {
        this.rspimportName = rspimportName;
    }

    public String getRspaccount() {
        return rspaccount;
    }

    public void setRspaccount(String rspaccount) {
        this.rspaccount = rspaccount;
    }

    public String getRspfamilyDescription() {
        return rspfamilyDescription;
    }

    public void setRspfamilyDescription(String rspfamilyDescription) {
        this.rspfamilyDescription = rspfamilyDescription;
    }

    public String getRspcurrencyDescription() {
        return rspcurrencyDescription;
    }

    public void setRspcurrencyDescription(String rspcurrencyDescription) {
        this.rspcurrencyDescription = rspcurrencyDescription;
    }

    public String getRspcurrencySymbol() {
        return rspcurrencySymbol;
    }

    public void setRspcurrencySymbol(String rspcurrencySymbol) {
        this.rspcurrencySymbol = rspcurrencySymbol;
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

    public boolean getRspVeryfyPaymentPS(JSONObject json ,String typePayment , String TypePagoServicio,Map<String,String> header) throws JSONException{


        if (JsonUtil.validatedNull(json,COMPANYNAME))
            setRspcompanyName(json.getString(COMPANYNAME));
        else
            return false;

        if (JsonUtil.validatedNull(json,SERVICEDESCRIPTION))
            setRspserviceDescription(json.getString(SERVICEDESCRIPTION));
        else
            return false;

        if(TypePagoServicio.equals(Trans.PAGO_IMPORTE)){

            if (JsonUtil.validatedNull(json,IMPORTNAME))
                setRspimportName(json.getString(IMPORTNAME));
            else
                return false;
        }

        if (JsonUtil.validatedNull(json,CLIENTDEPOSITCODE))
                setRspclientDepositCode(jweEncryptDecrypt(json.getString(CLIENTDEPOSITCODE),false,true) ? jweDataEncryptDecrypt.getDataDecrypt() : "");
        else
            return false;

        if(!TypePagoServicio.equals(Trans.PAGO_IMPORTE)  && !TypePagoServicio.equals(Trans.PAGO_SINVALIDACION)){

            if (JsonUtil.validatedNull(json,CLIENTDEPOSITNAME))
                setRspclientDepositName(json.getString(CLIENTDEPOSITNAME));
            else
                return false;
        }

        if (JsonUtil.validatedNull(json,AMOUNTCURRENCYSYMBOL))
            setRspamountCurrencySymbol(json.getString(AMOUNTCURRENCYSYMBOL));
        else
            return false;

        if (JsonUtil.validatedNull(json,AMOUNT))
            setRspamount(json.getString(AMOUNT));
        else
            return false;

        if (JsonUtil.validatedNull(json,COMMISSIONCURRENCYSYMBOL))
            setRspcommissionCurrencySymbol(json.getString(COMMISSIONCURRENCYSYMBOL));
        else
            return false;

        if (JsonUtil.validatedNull(json,COMMISSIONAMOUNT))
            setRspcommissionAmount(json.getString(COMMISSIONAMOUNT));
        else
            return false;

        if (JsonUtil.validatedNull(json,EXTRACHARGEAMOUNTCURRENCYSYMBOL))
            setRspextraChargeAmountCurrencySymbol(json.getString(EXTRACHARGEAMOUNTCURRENCYSYMBOL));
        else
            return false;

        if (JsonUtil.validatedNull(json,EXTRACHARGEAMOUNT))
            setRspextraChargeAmount(json.getString(EXTRACHARGEAMOUNT));
        else
            return false;

        if (JsonUtil.validatedNull(json,TOTALAMOUNTCURRENCYSYMBOL))
            setRsptotalAmountCurrencySymbol(json.getString(TOTALAMOUNTCURRENCYSYMBOL));
        else
            return false;

        if (JsonUtil.validatedNull(json,TOTALAMOUNT))
            setRsptotalAmount(json.getString(TOTALAMOUNT));
        else
            return false;

        //SE RECIBE CUANDO ES CON TARJETA ACCOUNT
        if (typePayment.equals("2")){

            if (JsonUtil.validatedNull(json,ACCOUNT))
                setRspaccount(json.getString(ACCOUNT));
            else
                return false;

            if (!getRspAccountyCard()){
                return false;
            }

        }

        if (JsonUtil.validatedNull(json,EXCHANGESTATUS))
            setRspexchangeStatus(json.getString(EXCHANGESTATUS));
        else
            return false;

        if(header != null && !header.isEmpty())
            setSesionId(header.get(SESION_ID));

        return true;

    }

    private boolean getRspAccountyCard() throws JSONException {

        JSONObject jsonAccountCard = new JSONObject(getRspaccount());

        if(JsonUtil.validatedNull(jsonAccountCard, FAMILYDESCRIPTION)){
            setRspfamilyDescription(jsonAccountCard.getString(FAMILYDESCRIPTION));
        }else
            return false;

        if(JsonUtil.validatedNull(jsonAccountCard, CURRENCYDESCRIPTION)){
            setRspcurrencyDescription(jsonAccountCard.getString(CURRENCYDESCRIPTION));
        }else
            return false;

        if(JsonUtil.validatedNull(jsonAccountCard, CURRENCYSYMBOL)){
            setRspcurrencySymbol(jsonAccountCard.getString(CURRENCYSYMBOL));
        }else
            return false;

        return true;
    }
}
