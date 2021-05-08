package com.bcp.inicializacion.configuracioncomercio;

public class Transaction {
    private String idTransaction;
    private String transEnable;
    private String amountMinimun;
    private String amountMaximun;
    private String amountMinUsd;
    private String amountMaxUsd;

    public String[] fields = new String[]{
            "ID_TRANSACCION",
            "HABILITAR",
            "MONTO_MAXIMO",
            "MONTO_MINIMO",
            "MONTO_MAXIMO_DOLARES",
            "MONTO_MINIMO_DOLARES"
    };

    public void setTransaction(String column, String value) {
        switch (column) {
            case "ID_TRANSACCION":
                setIdTransaction(value);
                break;
            case "HABILITAR":
                setTransEnable(value);
                break;
            case "MONTO_MAXIMO":
                setAmountMaximun(value);
                break;
            case "MONTO_MINIMO":
                setAmountMinimun(value);
                break;
            case "MONTO_MAXIMO_DOLARES":
                setAmountMaxUsd(value);
                break;
            case "MONTO_MINIMO_DOLARES":
                setAmountMinUsd(value);
                break;
            default:
                break;
        }
    }

    public void clearTransaction() {
        for (String s : fields) {
            setTransaction(s, "");
        }
    }

    public String getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(String idTransaction) {
        this.idTransaction = idTransaction;
    }

    public String getTransEnable() {
        return transEnable;
    }

    public void setTransEnable(String transEnable) {
        this.transEnable = transEnable;
    }

    public String getAmountMinimun() {
        return amountMinimun;
    }

    public void setAmountMinimun(String amountMinimun) {
        this.amountMinimun = amountMinimun;
    }

    public String getAmountMaximun() {
        return amountMaximun;
    }

    public void setAmountMaximun(String amountMaximun) {
        this.amountMaximun = amountMaximun;
    }

    public String getAmountMinUsd() {
        return amountMinUsd;
    }

    public void setAmountMinUsd(String amountMinUsd) {
        this.amountMinUsd = amountMinUsd;
    }

    public String getAmountMaxUsd() {
        return amountMaxUsd;
    }

    public void setAmountMaxUsd(String amountMaxUsd) {
        this.amountMaxUsd = amountMaxUsd;
    }

    public String[] getFields() {
        return fields;
    }
}
