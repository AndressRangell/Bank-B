package com.bcp.rest.pagoservicios.response;

public class ListsServices {

    private String serviceDescription;
    private String affiliationCode;
    private String serviceInputDescription;
    private String currencySymbol;
    private String serviceType;
    private String documentStatus;


    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getAffiliationCode() {
        return affiliationCode;
    }

    public void setAffiliationCode(String affiliationCode) {
        this.affiliationCode = affiliationCode;
    }

    public String getServiceInputDescription() {
        return serviceInputDescription;
    }

    public void setServiceInputDescription(String serviceInputDescription) {
        this.serviceInputDescription = serviceInputDescription;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }
}
