package com.bcp.document;

import com.bcp.inicializacion.configuracioncomercio.Companny;
import com.bcp.menus.seleccion_cuenta.SelectedAccountItem;
import com.bcp.rest.giros.cobro_nacional.response.PaymentOrders;
import com.bcp.rest.pagoservicios.response.ListsDebt;

import static com.bcp.inicializacion.tools.PolarisUtil.IGUALORDENANTESI;

public class ClassArguments {

    private int error;
    private String argument1;
    private String argument2;
    private String argument3;
    private String typepayment;
    private String argument5;
    private int typetransaction;
    private String[] argument6;
    private PaymentOrders[] paymentOrders;
    private String sessionId;
    private String nameBeneficiary;
    private String nameRemitter;
    private String nameSender;
    private String dniBeneficiary;
    private String dniRemitter;
    private String dniSender;
    private String typeCoin;
    private String monto;
    private String tipoCambio;
    private String editBenef;
    private String editRemit;
    private String editSender;
    private String editEditable;
    private String verifyBank;
    private String total;
    private String totalPagar;
    private String comissionAmount;
    private String simbolTotal;
    private String simbcomision;
    private String simboltotalPagar;
    private String editableGiros;
    private boolean flag1 = false;
    private boolean flag2 = false;
    private boolean editar = false;
    private int typeclient = IGUALORDENANTESI;
    private String tipoPagoServicio;
    private String typeDocumentBeneficiary;
    private String typeDocumentSender;
    private String typeDocumentRemitter;
    private SelectedAccountItem selectedAccountItem;
    private ListsDebt[] listDebts;
    private String typeservice;
    private String codTypeService;
    private String maxAmount;
    private String minAmount;
    private String empresa;
    private String typeAccount;
    private String otroCargo;
    private String simOtroCargo;
    private String StatusTipoCambio;
    private String affiliationCode;
    private String debcode;
    private String clientDepositName;
    private String clientDocumentNumber;
    private boolean otherAmount = false;
    private String documentCode;
    private String importcode;
    private Companny objCompanny;


    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getArgument1() {
        return argument1;
    }

    public void setArgument1(String argument1) {
        this.argument1 = argument1;
    }

    public String getArgument2() {
        return argument2;
    }

    public void setArgument2(String argument2) {
        this.argument2 = argument2;
    }

    public String getArgument3() {
        return argument3;
    }

    public void setArgument3(String argument3) {
        this.argument3 = argument3;
    }

    public String getTypepayment() {
        return typepayment;
    }

    public void setTypepayment(String typepayment) {
        this.typepayment = typepayment;
    }

    public String getArgument5() {
        return argument5;
    }

    public void setArgument5(String argument5) {
        this.argument5 = argument5;
    }

    public String[] getArgument6() {
        return argument6;
    }

    public void setArgument6(String[] argument6) {
        this.argument6 = argument6;
    }

    public int getTypetransaction() {
        return typetransaction;
    }

    public void setTypetransaction(int typetransaction) {
        this.typetransaction = typetransaction;
    }

    public PaymentOrders[] getPaymentOrders() {
        return paymentOrders;
    }

    public void setPaymentOrders(PaymentOrders[] paymentOrders) {
        this.paymentOrders = paymentOrders;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getNameBeneficiary() {
        return nameBeneficiary;
    }

    public void setNameBeneficiary(String nameBeneficiary) {
        this.nameBeneficiary = nameBeneficiary;
    }

    public String getNameRemitter() {
        return nameRemitter;
    }

    public void setNameRemitter(String nameRemitter) {
        this.nameRemitter = nameRemitter;
    }

    public String getNameSender() {
        return nameSender;
    }

    public void setNameSender(String nameSender) {
        this.nameSender = nameSender;
    }

    public String getDniBeneficiary() {
        return dniBeneficiary;
    }

    public void setDniBeneficiary(String dniBeneficiary) {
        this.dniBeneficiary = dniBeneficiary;
    }

    public String getDniRemitter() {
        return dniRemitter;
    }

    public void setDniRemitter(String dniRemitter) {
        this.dniRemitter = dniRemitter;
    }

    public String getDniSender() {
        return dniSender;
    }

    public void setDniSender(String dniSender) {
        this.dniSender = dniSender;
    }

    public String getTypeCoin() {
        return typeCoin;
    }

    public void setTypeCoin(String typeCoin) {
        this.typeCoin = typeCoin;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getEditBenef() {
        return editBenef;
    }

    public void setEditBenef(String editBenef) {
        this.editBenef = editBenef;
    }

    public String getEditRemit() {
        return editRemit;
    }

    public void setEditRemit(String editRemit) {
        this.editRemit = editRemit;
    }

    public String getEditSender() {
        return editSender;
    }

    public void setEditSender(String editSender) {
        this.editSender = editSender;
    }

    public String getEditEditable() {
        return editEditable;
    }

    public void setEditEditable(String editEditable) {
        this.editEditable = editEditable;
    }

    public String getVerifyBank() {
        return verifyBank;
    }

    public void setVerifyBank(String verifyBank) {
        this.verifyBank = verifyBank;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getComissionAmount() {
        return comissionAmount;
    }

    public void setComissionAmount(String comissionAmount) {
        this.comissionAmount = comissionAmount;
    }

    public String getSimbolTotal() {
        return simbolTotal;
    }

    public void setSimbolTotal(String simbolTotal) {
        this.simbolTotal = simbolTotal;
    }

    public String getSimbcomision() {
        return simbcomision;
    }

    public void setSimbcomision(String simbcomision) {
        this.simbcomision = simbcomision;
    }

    public String getSimboltotalPagar() {
        return simboltotalPagar;
    }

    public void setSimboltotalPagar(String simboltotalPagar) {
        this.simboltotalPagar = simboltotalPagar;
    }

    public String getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(String tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    public String getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(String totalPagar) {
        this.totalPagar = totalPagar;
    }

    public boolean isFlag1() {
        return flag1;
    }

    public void setFlag1(boolean flag1) {
        this.flag1 = flag1;
    }

    public boolean isFlag2() {
        return flag2;
    }

    public void setFlag2(boolean flag2) {
        this.flag2 = flag2;
    }

    public SelectedAccountItem getSelectedAccountItem() {
        return selectedAccountItem;
    }

    public void setSelectedAccountItem(SelectedAccountItem selectedAccountItem) {
        this.selectedAccountItem = selectedAccountItem;
    }

    public boolean isEditar() {
        return editar;
    }

    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    public int getTypeclient() {
        return typeclient;
    }

    public void setTypeclient(int typeclient) {
        this.typeclient = typeclient;
    }

    public String getTypeDocumentBeneficiary() {
        return typeDocumentBeneficiary;
    }

    public void setTypeDocumentBeneficiary(String typeDocumentBeneficiary) {
        this.typeDocumentBeneficiary = typeDocumentBeneficiary;
    }

    public String getTypeDocumentSender() {
        return typeDocumentSender;
    }

    public void setTypeDocumentSender(String typeDocumentSender) {
        this.typeDocumentSender = typeDocumentSender;
    }

    public String getTypeDocumentRemitter() {
        return typeDocumentRemitter;
    }

    public void setTypeDocumentRemitter(String typeDocumentRemitter) {
        this.typeDocumentRemitter = typeDocumentRemitter;
    }

    public String getTipoPagoServicio() {
        return tipoPagoServicio;
    }

    public void setTipoPagoServicio(String tipoPagoServicio) {
        this.tipoPagoServicio = tipoPagoServicio;
    }

    public String getEditableGiros() {
        return editableGiros;
    }

    public void setEditableGiros(String editableGiros) {
        this.editableGiros = editableGiros;
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

    public ListsDebt[] getListDebts() {
        return listDebts;
    }

    public void setListDebts(ListsDebt[] listDebts) {
        this.listDebts = listDebts;
    }

    public String getTypeservice() {
        return typeservice;
    }

    public void setTypeservice(String typeservice) {
        this.typeservice = typeservice;
    }

    public String getCodTypeService() {
        return codTypeService;
    }

    public void setCodTypeService(String codTypeService) {
        this.codTypeService = codTypeService;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getTypeAccount() {
        return typeAccount;
    }

    public void setTypeAccount(String typeAccount) {
        this.typeAccount = typeAccount;
    }

    public String getOtroCargo() {
        return otroCargo;
    }

    public void setOtroCargo(String otroCargo) {
        this.otroCargo = otroCargo;
    }

    public String getSimOtroCargo() {
        return simOtroCargo;
    }

    public void setSimOtroCargo(String simOtroCargo) {
        this.simOtroCargo = simOtroCargo;
    }

    public String getStatusTipoCambio() {
        return StatusTipoCambio;
    }

    public void setStatusTipoCambio(String statusTipoCambio) {
        StatusTipoCambio = statusTipoCambio;
    }

    public String getAffiliationCode() {
        return affiliationCode;
    }

    public void setAffiliationCode(String affiliationCode) {
        this.affiliationCode = affiliationCode;
    }

    public String getDebcode() {
        return debcode;
    }

    public void setDebcode(String debcode) {
        this.debcode = debcode;
    }

    public String getClientDepositName() {
        return clientDepositName;
    }

    public void setClientDepositName(String clientDepositName) {
        this.clientDepositName = clientDepositName;
    }

    public String getClientDocumentNumber() {
        return clientDocumentNumber;
    }

    public void setClientDocumentNumber(String clientDocumentNumber) {
        this.clientDocumentNumber = clientDocumentNumber;
    }

    public boolean isOtherAmount() {
        return otherAmount;
    }

    public void setOtherAmount(boolean otherAmount) {
        this.otherAmount = otherAmount;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public String getImportcode() {
        return importcode;
    }

    public void setImportcode(String importcode) {
        this.importcode = importcode;
    }

    public Companny getObjCompanny() {
        return objCompanny;
    }

    public void setObjCompanny(Companny objCompanny) {
        this.objCompanny = objCompanny;
    }

}
