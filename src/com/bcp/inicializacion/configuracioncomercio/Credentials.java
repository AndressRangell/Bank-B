package com.bcp.inicializacion.configuracioncomercio;

public class Credentials {

    private String clave;
    private String valor;

    private static String[] fields = new String[]{
            "CLAVE",
            "VALOR"
    };

    public void setCredentials(String column, String value){
        switch (column){
            case "CLAVE":
                setClave(value);
                break;
            case "VALOR":
                setValor(value);
                break;
            default:
                break;
        }
    }

    public void clearCredentials(){
        for (String s : Credentials.fields){
            setCredentials(s,"");
        }
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String[] getFields() {
        return fields;
    }
}
