package com.bcp.rest.giros.cobro_nacional.response;

public class PaymentOrders {

    private String rspTransactionReference;
    private String rspCurrencySymbol;
    private String rspAmount;
    private String rspBeneficiaryName;
    private String rspLock;

    public String getRspTransactionReference() {
        return rspTransactionReference;
    }

    public void setRspTransactionReference(String rspTransactionReference) {
        this.rspTransactionReference = rspTransactionReference;
    }

    public String getRspCurrencySymbol() {
        return rspCurrencySymbol;
    }

    public void setRspCurrencySymbol(String rspCurrencySymbol) {
        this.rspCurrencySymbol = rspCurrencySymbol;
    }

    public String getRspAmount() {
        return rspAmount;
    }

    public void setRspAmount(String rspAmount) {
        this.rspAmount = rspAmount;
    }

    public String getRspBeneficiaryName() {
        return rspBeneficiaryName;
    }

    public void setRspBeneficiaryName(String rspBeneficiaryName) {
        this.rspBeneficiaryName = rspBeneficiaryName;
    }

    public String getRspLock() {
        return rspLock;
    }

    public void setRspLock(String rspLock) {
        this.rspLock = rspLock;
    }
}
