package com.bcp.document;

import com.bcp.inicializacion.configuracioncomercio.URL;
import com.bcp.menus.seleccion_cuenta.SelectedAccountItem;
import com.bcp.rest.generic.response.ProductsAccounts;
import com.bcp.rest.giros.cobro_nacional.response.PaymentOrders;
import com.bcp.transactions.callbacks.WaitTransFragment;

import java.util.List;
import java.util.Map;

public class MsgScreenDocument {

    private String tittle;
    private String tittleDoc;
    private String transEname;
    private int timeOut;
    private int minLength;
    private int maxLength;
    private boolean isBanner;
    private boolean isAlfa;
    private SelectedAccountItem selectedAccountItem;
    private Map<String,String> header;
    private List<URL> listUrl;
    private WaitTransFragment callbackTransFragment;
    private PaymentOrders paymentOrders;
    private ClassArguments arguments;
    private ProductsAccounts[] productsAccounts;
    private String maxAmount;
    private String minAmount;
    private String maxAmountUsd;
    private String minAmountUsd;

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getTittleDoc() {
        return tittleDoc;
    }

    public void setTittleDoc(String tittleDoc) {
        this.tittleDoc = tittleDoc;
    }

    public String getTransEname() {
        return transEname;
    }

    public void setTransEname(String transEname) {
        this.transEname = transEname;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public boolean isBanner() {
        return isBanner;
    }

    public void setBanner(boolean banner) {
        isBanner = banner;
    }

    public boolean isAlfa() {
        return isAlfa;
    }

    public void setAlfa(boolean alfa) {
        isAlfa = alfa;
    }

    public SelectedAccountItem getSelectedAccountItem() {
        return selectedAccountItem;
    }

    public void setSelectedAccountItem(SelectedAccountItem selectedAccountItem) {
        this.selectedAccountItem = selectedAccountItem;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public List<URL> getListUrl() {
        return listUrl;
    }

    public void setListUrl(List<URL> listUrl) {
        this.listUrl = listUrl;
    }

    public void setCallbackTransFragment(WaitTransFragment callbackTransFragment) {
        this.callbackTransFragment = callbackTransFragment;
    }

    public WaitTransFragment getCallbackTransFragment() {
        return callbackTransFragment;
    }

    public PaymentOrders getPaymentOrders() {
        return paymentOrders;
    }

    public void setPaymentOrders(PaymentOrders paymentOrders) {
        this.paymentOrders = paymentOrders;
    }

    public ClassArguments getArguments() {
        return arguments;
    }

    public void setArguments(ClassArguments arguments) {
        this.arguments = arguments;
    }

    public ProductsAccounts[] getProductsAccounts() {
        return productsAccounts;
    }

    public void setProductsAccounts(ProductsAccounts[] productsAccounts) {
        this.productsAccounts = productsAccounts;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getMaxAmountUsd() {
        return maxAmountUsd;
    }

    public void setMaxAmountUsd(String maxAmountUsd) {
        this.maxAmountUsd = maxAmountUsd;
    }

    public String getMinAmountUsd() {
        return minAmountUsd;
    }

    public void setMinAmountUsd(String minAmountUsd) {
        this.minAmountUsd = minAmountUsd;
    }
}
