package com.bcp.rest.giros.cobro_nacional.response;

import com.bcp.rest.JsonUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class RspVerifyBank extends JsonUtil{

    private static final String PAYMENTORDERS = "paymentOrders";
    private static final String TRANSACTIONREFERENCE = "transactionReference";
    private static final String CURRENCYSYMBOL = "currencySymbol";
    private static final String AMOUNT = "amount";
    private static final String BENEFICIARYNAME = "beneficiaryName";
    private static final String LOCK = "lock";
    private static final String SESION_ID = "sessionId";

    private String rspPaymentOrders;
    private String sesionId;

    private PaymentOrders[] rspOrders;

    public String getRspPaymentOrders() {
        return rspPaymentOrders;
    }

    public void setRspPaymentOrders(String rspPaymentOrders) {
        this.rspPaymentOrders = rspPaymentOrders;
    }

    public String getSesionId() {
        return sesionId;
    }

    public void setSesionId(String sesionId) {
        this.sesionId = sesionId;
    }

    public PaymentOrders[] getRspOrders() {
        return rspOrders;
    }

    public void setRspOrders(PaymentOrders[] rspOrders) {
        this.rspOrders = rspOrders;
    }

    /**
     * realiza el parsin del objeto JSON recibido.
     * @param json
     * @return retorna el objeto con los datos JSON interpretados de la respuesta.
     */

    public boolean getRspObtainOrders(JSONObject json, Map<String,String> header) throws JSONException {

        if(JsonUtil.validatedNull(json, PAYMENTORDERS))
            setRspPaymentOrders(json.getString(PAYMENTORDERS));
        else
            return false;

        JSONArray jsonArray = new JSONArray(getRspPaymentOrders());

        if (jsonArray.length()<=0)
            return false;

        PaymentOrders[] paymentOrders = new PaymentOrders[jsonArray.length()];

        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject ctn = jsonArray.getJSONObject(j);
            paymentOrders[j] = new PaymentOrders();

            if (JsonUtil.validatedNull(ctn, TRANSACTIONREFERENCE))
                paymentOrders[j].setRspTransactionReference(ctn.getString(TRANSACTIONREFERENCE));
            else
                return false;

            if (JsonUtil.validatedNull(ctn, CURRENCYSYMBOL))
                paymentOrders[j].setRspCurrencySymbol(ctn.getString(CURRENCYSYMBOL));
            else
                return false;

            if (JsonUtil.validatedNull(ctn, AMOUNT))
                paymentOrders[j].setRspAmount(ctn.getString(AMOUNT));
            else
                return false;

            if (JsonUtil.validatedNull(ctn, BENEFICIARYNAME))
                paymentOrders[j].setRspBeneficiaryName(jweEncryptDecrypt(ctn.getString(BENEFICIARYNAME)+"",false,true) ? jweDataEncryptDecrypt.getDataDecrypt(): "");
            else
                return false;

            if (JsonUtil.validatedNull(ctn, LOCK))
                paymentOrders[j].setRspLock(ctn.getString(LOCK));
            else
                return false;
        }

        setRspOrders(paymentOrders);

        if(header != null && !header.isEmpty())
            setSesionId(header.get(SESION_ID));
        else
            return false;

        return true;
    }
}
