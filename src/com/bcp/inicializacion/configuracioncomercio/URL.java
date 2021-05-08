package com.bcp.inicializacion.configuracioncomercio;

public class URL {
    private String idTransaction;
    private String method;
    private String order;

    private String[] fields = new String[]{
            "ID_TRANSACCION",
            "METODO",
            "ORDEN"
    };

    public void setURL(String column, String value) {
        switch (column) {
            case "ID_TRANSACCION":
                setIdTransaction(value);
                break;
            case "METODO":
                setMethod(value);
                break;
            case "ORDEN":
                setOrder(value);
                break;
            default:
                break;
        }
    }

    public void setUrlTest(String idTransaction, String method, String order){
        setIdTransaction(idTransaction);
        setMethod(method);
        setOrder(order);
    }

    public void clearURL(){
        for (String s: getFields()){
            setURL(s, "");
        }
    }

    public String getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(String idTransaction) {
        this.idTransaction = idTransaction;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String[] getFields() {
        return fields;
    }
}
