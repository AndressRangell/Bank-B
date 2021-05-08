package com.bcp.rest.generic.response;

import java.io.Serializable;

public class ProductsTransactions implements Serializable {

    private String data;
    private String reference;
    private String amount;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
