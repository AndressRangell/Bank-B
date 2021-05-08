package com.bcp.rest.pagoservicios.response;

public class ListsDebt {

    //CON RANGO
    private String debtCode;
    private String debtPaymentCode;
    private String minimumAmount;
    private String minimumAmountCurrencySymbol;
    private String totalAmount;
    private String totalAmountCurrencySymbol;

    private String dueDate;
    private String amount;
    private String amountCurrencySymbol;
    private String consumption;
    private String importDebts;

    //PARA PAGO IMPORTE
    private String importCode;
    private String importName;

    //IMPORTE DOCUEMNT
    private String documentCode;
    private String documentDescription;


    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountCurrencySymbol() {
        return amountCurrencySymbol;
    }

    public void setAmountCurrencySymbol(String amountCurrencySymbol) {
        this.amountCurrencySymbol = amountCurrencySymbol;
    }

    public String getDebtCode() {
        return debtCode;
    }

    public void setDebtCode(String debtCode) {
        this.debtCode = debtCode;
    }

    public String getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(String minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public String getMinimumAmountCurrencySymbol() {
        return minimumAmountCurrencySymbol;
    }

    public void setMinimumAmountCurrencySymbol(String minimumAmountCurrencySymbol) {
        this.minimumAmountCurrencySymbol = minimumAmountCurrencySymbol;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalAmountCurrencySymbol() {
        return totalAmountCurrencySymbol;
    }

    public void setTotalAmountCurrencySymbol(String totalAmountCurrencySymbol) {
        this.totalAmountCurrencySymbol = totalAmountCurrencySymbol;
    }

    public String getConsumption() {
        return consumption;
    }

    public void setConsumption(String consumption) {
        this.consumption = consumption;
    }

    public String getImportDebts() {
        return importDebts;
    }

    public void setImportDebts(String importDebts) {
        this.importDebts = importDebts;
    }

    public String getImportCode() {
        return importCode;
    }

    public void setImportCode(String importCode) {
        this.importCode = importCode;
    }

    public String getImportName() {
        return importName;
    }

    public void setImportName(String importName) {
        this.importName = importName;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public String getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    public String getDebtPaymentCode() {
        return debtPaymentCode;
    }

    public void setDebtPaymentCode(String debtPaymentCode) {
        this.debtPaymentCode = debtPaymentCode;
    }
}
