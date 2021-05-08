package com.bcp.inicializacion.configuracioncomercio;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.bcp.inicializacion.sqlite.frompackage.DBHelper;
import com.bcp.inicializacion.tools.PolarisUtil;
import com.newpos.libpay.Logger;

public class AGENTE {

    private String agentName;
    private String address;
    private String phone;
    private String ruc;
    private String merchantId;
    private String maximumTransactionAmount;
    private String signature;
    private String keyExpiration;
    private String settleDays;
    private String agentId;
    private String access;
    private String url;

    private static AGENTE instance = null;

    private static String[] fields = new String[]{
            "NOMBRE",
            "DIRECCION",
            "TELEFONO",
            "RUC",
            "MERCHANT_ID",
            "MONTO_MAXIMO_TRANSACCION",
            "HABILITA_FIRMA",
            "CADUCIDAD_CLAVE",
            "DIAS_CIERRE",
            "AGENTE_ID",
            "CLAVE_TECNICO",
            "BASE_URL"
    };

    public AGENTE(){

    }

    public static AGENTE getSingletonInstance(){
        if (instance == null){
            instance = new AGENTE();
        }else{
            Log.d("AGENTE", "No se puede crear otro objeto, ya existe");
        }
        return instance;
    }

    private void setTCONF(String column, String value){
        switch (column){
            case "NOMBRE":
                setAgentName(value);
                break;
            case "DIRECCION":
                setAddress(value);
                break;
            case "TELEFONO":
                setPhone(value);
                break;
            case "RUC":
                setRuc(value);
                break;
            case "MERCHANT_ID":
                setMerchantId(value);
                break;
            case "MONTO_MAXIMO_TRANSACCION":
                setMaximumTransactionAmount(value);
                break;
            case "HABILITA_FIRMA":
                setSignature(value);
                break;
            case "CADUCIDAD_CLAVE":
                setKeyExpiration(value);
                break;
            case "DIAS_CIERRE":
                setSettleDays(value);
                break;
            case "AGENTE_ID":
                setAgentId(value);
                break;
            case "CLAVE_TECNICO":
                setAccess(value);
                break;
            case "BASE_URL":
                setUrl(value);
                break;
            default:
                break;
        }
    }

    public void clearAGENTE() {
        for (String s : AGENTE.fields) {
            setTCONF(s, "");
        }
    }

    public boolean selectAgent(Context context){
        DBHelper databaseAccess = new DBHelper(context, PolarisUtil.NAME_DB, null, 1);
        databaseAccess.openDb(PolarisUtil.NAME_DB);

        StringBuilder sql = new StringBuilder();

        sql.append("select ");
        int counter = 1;
        for (String s : AGENTE.fields) {
            sql.append(s);
            if (counter++ < AGENTE.fields.length) {
                sql.append(",");
            }
        }
        sql.append(" from AGENTE");

        try {

            Cursor cursor = databaseAccess.rawQuery(sql.toString());

            cursor.moveToFirst();
            int indexColumn;
            while (!cursor.isAfterLast()) {
                clearAGENTE();
                indexColumn = 0;
                for (String s : AGENTE.fields) {
                    setTCONF(s, cursor.getString(indexColumn++).trim());
                }
                cursor.moveToNext();
            }
            cursor.close();

        } catch (Exception e) {
            databaseAccess.closeDb();
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " from AGENTE" + e.getMessage());
            return false;
        }
        databaseAccess.closeDb();
        return true;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMaximumTransactionAmount() {
        return maximumTransactionAmount;
    }

    public void setMaximumTransactionAmount(String maximumTransactionAmount) {
        this.maximumTransactionAmount = maximumTransactionAmount;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getKeyExpiration() {
        return keyExpiration;
    }

    public void setKeyExpiration(String keyExpiration) {
        this.keyExpiration = keyExpiration;
    }

    public String getSettleDays() {
        return settleDays;
    }

    public void setSettleDays(String settleDays) {
        this.settleDays = settleDays;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

}
