package com.newpos.libpay.trans.translog;

import com.bcp.rest.generic.response.ProductsTransactions;
import com.bcp.rest.pagoservicios.response.ListsDebt;

import java.io.Serializable;

public class TransLogDataWs implements Serializable {

    private String transactionDate;
    private String transactionTime;
    private String transactionNumber;
    private String leadDescription;
    private String amount;
    private String amountExchangeRate;
    private String exchangeRate;
    private String fullName;
    private String cardId;
    private String arpc;
    private String accountId;
    private String familyDescription;
    private String currencyDescription;
    private String currencySymbol;
    private String availableBalance;
    private String countableBalance;
    private String date;
    private String reference;
    private String toPrint;
    private String nameTrans;
    private String opnNumber;
    private ProductsTransactions[] productsTransactions;
    private String moneyOut = "";
    private boolean alreadyPrinted = false;
    private String typeQuery;
    private String entryMode;
    private String currencyCode;
    private String nameBenficiary;
    private String nameRemitter;
    private String docTypeBeneficiary;
    private String docNumBeneficiary;
    private String docTypeRemitter;
    private String docNumRemitter;
    private String commissionCurrencySymbol;
    private String commissionAmount;
    private String symbolExchangeRate;
    private String totalAmountCurrency;
    private String totalAmount;
    private String paymentMethod;
    private String nameSender;
    private String docTypeSender;
    private String docNumSender;
    private String additionalInformation;
    private String amountCurrencySymbol;
    private String card;
    private String category;
    private String depositCode;
    private String depositName;
    private String company;
    private String delayAmount;
    private String delayCurrencySymbol;
    private String description;
    private String detail;
    private String consumption;
    private String dueDate;
    private String fixedChargeAmount;
    private String fixedChargeCurrencySymbol;
    private String imporName;
    private String rows;
    private String paymentAccount;
    private String message;
    private String importt;
    private String total;
    private String totalDebtAmount;
    private String totalDebtCurrencySymbol;
    private ListsDebt[] listsDebts;

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getLeadDescription() {
        return leadDescription;
    }

    public void setLeadDescription(String leadDescription) {
        this.leadDescription = leadDescription;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountExchangeRate() {
        return amountExchangeRate;
    }

    public void setAmountExchangeRate(String amountExchangeRate) {
        this.amountExchangeRate = amountExchangeRate;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getArpc() {
        return arpc;
    }

    public void setArpc(String arpc) {
        this.arpc = arpc;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFamilyDescription() {
        return familyDescription;
    }

    public void setFamilyDescription(String familyDescription) {
        this.familyDescription = familyDescription;
    }

    public String getCurrencyDescription() {
        return currencyDescription;
    }

    public void setCurrencyDescription(String currencyDescription) {
        this.currencyDescription = currencyDescription;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getCountableBalance() {
        return countableBalance;
    }

    public void setCountableBalance(String countableBalance) {
        this.countableBalance = countableBalance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getToPrint() {
        return toPrint;
    }

    public void setToPrint(String toPrint) {
        this.toPrint = toPrint;
    }

    public String getNameTrans() {
        return nameTrans;
    }

    public void setNameTrans(String nameTrans) {
        this.nameTrans = nameTrans;
    }

    public String getOpnNumber() {
        return opnNumber;
    }

    public void setOpnNumber(String opnNumber) {
        this.opnNumber = opnNumber;
    }

    public ProductsTransactions[] getProductsTransactions() {
        return productsTransactions;
    }

    public void setProductsTransactions(ProductsTransactions[] productsTransactions) {
        this.productsTransactions = productsTransactions;
    }

    public String getMoneyOut() {
        return moneyOut;
    }

    public void setMoneyOut(String moneyOut) {
        this.moneyOut = moneyOut;
    }

    public boolean isAlreadyPrinted() {
        return alreadyPrinted;
    }

    public void setAlreadyPrinted(boolean alreadyPrinted) {
        this.alreadyPrinted = alreadyPrinted;
    }

    public String getTypeQuery() {
        return typeQuery;
    }

    public void setTypeQuery(String typeQuery) {
        this.typeQuery = typeQuery;
    }

    public String getEntryMode() {
        return entryMode;
    }

    public void setEntryMode(String entryMode) {
        this.entryMode = entryMode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getNameBenficiary() {
        return nameBenficiary;
    }

    public void setNameBenficiary(String nameBenficiary) {
        this.nameBenficiary = nameBenficiary;
    }

    public String getNameRemitter() {
        return nameRemitter;
    }

    public void setNameRemitter(String nameRemitter) {
        this.nameRemitter = nameRemitter;
    }

    public String getDocTypeBeneficiary() {
        return docTypeBeneficiary;
    }

    public void setDocTypeBeneficiary(String docTypeBeneficiary) {
        this.docTypeBeneficiary = docTypeBeneficiary;
    }

    public String getDocNumBeneficiary() {
        return docNumBeneficiary;
    }

    public void setDocNumBeneficiary(String docNumBeneficiary) {
        this.docNumBeneficiary = docNumBeneficiary;
    }

    public String getDocTypeRemitter() {
        return docTypeRemitter;
    }

    public void setDocTypeRemitter(String docTypeRemitter) {
        this.docTypeRemitter = docTypeRemitter;
    }

    public String getDocNumRemitter() {
        return docNumRemitter;
    }

    public void setDocNumRemitter(String docNumRemitter) {
        this.docNumRemitter = docNumRemitter;
    }

    public String getCommissionCurrencySymbol() {
        return commissionCurrencySymbol;
    }

    public void setCommissionCurrencySymbol(String commissionCurrencySymbol) {
        this.commissionCurrencySymbol = commissionCurrencySymbol;
    }

    public String getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(String commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getSymbolExchangeRate() {
        return symbolExchangeRate;
    }

    public void setSymbolExchangeRate(String symbolExchangeRate) {
        this.symbolExchangeRate = symbolExchangeRate;
    }

    public String getTotalAmountCurrency() {
        return totalAmountCurrency;
    }

    public void setTotalAmountCurrency(String totalAmountCurrency) {
        this.totalAmountCurrency = totalAmountCurrency;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNameSender() {
        return nameSender;
    }

    public void setNameSender(String nameSender) {
        this.nameSender = nameSender;
    }

    public String getDocTypeSender() {
        return docTypeSender;
    }

    public void setDocTypeSender(String docTypeSender) {
        this.docTypeSender = docTypeSender;
    }

    public String getDocNumSender() {
        return docNumSender;
    }

    public void setDocNumSender(String docNumSender) {
        this.docNumSender = docNumSender;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getAmountCurrencySymbol() {
        return amountCurrencySymbol;
    }

    public void setAmountCurrencySymbol(String amountCurrencySymbol) {
        this.amountCurrencySymbol = amountCurrencySymbol;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDepositCode() {
        return depositCode;
    }

    public void setDepositCode(String depositeCode) {
        this.depositCode = depositeCode;
    }

    public String getDepositName() {
        return depositName;
    }

    public void setDepositName(String depositName) {
        this.depositName = depositName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDelayAmount() {
        return delayAmount;
    }

    public void setDelayAmount(String delayAmount) {
        this.delayAmount = delayAmount;
    }

    public String getDelayCurrencySymbol() {
        return delayCurrencySymbol;
    }

    public void setDelayCurrencySymbol(String delayCurrencySymbol) {
        this.delayCurrencySymbol = delayCurrencySymbol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getConsumption() {
        return consumption;
    }

    public void setConsumption(String consumption) {
        this.consumption = consumption;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getFixedChargeAmount() {
        return fixedChargeAmount;
    }

    public void setFixedChargeAmount(String fixedChargeAmount) {
        this.fixedChargeAmount = fixedChargeAmount;
    }

    public String getFixedChargeCurrencySymbol() {
        return fixedChargeCurrencySymbol;
    }

    public void setFixedChargeCurrencySymbol(String fixedChargeCurrencySymbol) {
        this.fixedChargeCurrencySymbol = fixedChargeCurrencySymbol;
    }

    public String getImporName() {
        return imporName;
    }

    public void setImporName(String imporName) {
        this.imporName = imporName;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImportt() {
        return importt;
    }

    public void setImportt(String importt) {
        this.importt = importt;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotalDebtAmount() {
        return totalDebtAmount;
    }

    public void setTotalDebtAmount(String totalDebtAmount) {
        this.totalDebtAmount = totalDebtAmount;
    }

    public String getTotalDebtCurrencySymbol() {
        return totalDebtCurrencySymbol;
    }

    public void setTotalDebtCurrencySymbol(String totalDebtCurrencySymbol) {
        this.totalDebtCurrencySymbol = totalDebtCurrencySymbol;
    }

    public ListsDebt[] getListsDebts() {
        return listsDebts;
    }

    public void setListsDebts(ListsDebt[] listsDebts) {
        this.listsDebts = listsDebts;
    }
}
