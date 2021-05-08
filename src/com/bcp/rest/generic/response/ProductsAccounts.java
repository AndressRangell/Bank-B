package com.bcp.rest.generic.response;

public class ProductsAccounts {

    private String familyCode;
    private String productDescrip;
    private String currencyCode;
    private String currencyDescrip;
    private String currencySymbol;

    public String getFamilyCode() {
        return familyCode;
    }

    public void setFamilyCode(String familyCode) {
        this.familyCode = familyCode;
    }

    public String getProductDescrip() {
        return productDescrip;
    }

    public void setProductDescrip(String productDescrip) {
        this.productDescrip = productDescrip;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyDescrip() {
        return currencyDescrip;
    }

    public void setCurrencyDescrip(String currencyDescrip) {
        this.currencyDescrip = currencyDescrip;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
}
