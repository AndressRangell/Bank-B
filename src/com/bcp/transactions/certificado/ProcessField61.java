package com.bcp.transactions.certificado;

public class ProcessField61 {

    private String keyStoreTransPub;
    private String keyStoreTransPriv;
    private String keyStorePolaris;
    private String keyStoreBcp;
    private String keyStoreHttps;
    private String keyStoretls;

    public void processFld(String dataFld61){
        String[] data = dataFld61.split("\\|");

        for (String datum : data) {
            String[] valor = datum.split("\\\\");
            String name = valor[0];
            switch (name) {
                case "LLAVE":
                    keyStoreBcp = valor[1];
                    break;
                case "TRANSACCION_PUBLICA":
                    keyStoreTransPub = valor[1];
                    break;
                case "TRANSACCION_PRIVADA":
                    keyStoreTransPriv = valor[1];
                    break;
                case "TLS":
                    keyStoreHttps = valor[1];
                    break;
                case "NUMERO_TARJETA":
                    keyStorePolaris = valor[1];
                    break;
                default:
                    break;
            }
        }
    }

    public String getKeyStoreTransPub() {
        return keyStoreTransPub;
    }

    public String getKeyStorePolaris() {
        return keyStorePolaris;
    }

    public String getKeyStoreBcp() {
        return keyStoreBcp;
    }

    public String getKeyStoreHttps() {
        return keyStoreHttps;
    }

    public String getKeyStoretls() {
        return keyStoretls;
    }

    public String getKeyStoreTransPriv() {
        return keyStoreTransPriv;
    }
}
