package com.bcp.transactions.deposito;

import android.content.Context;

import com.bcp.rest.JSONInfo;
import com.bcp.rest.deposito.request.ReqValidateAccount;
import com.bcp.rest.deposito.request.ReqVerifyDeposit;
import com.bcp.rest.deposito.response.RspValidateAccount;
import com.bcp.rest.deposito.response.RspVerifyDeposit;
import com.bcp.rest.generic.request.ReqConfirmVoucher;
import com.bcp.rest.generic.response.RspExecuteTransaction;
import com.bcp.rest.generic.response.RspListProducts;
import com.bcp.settings.BCPConfig;
import com.newpos.libpay.Logger;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.trans.Trans;
import com.newpos.libpay.trans.TransInputPara;
import com.newpos.libpay.trans.finace.FinanceTransWS;

import org.json.JSONObject;

import cn.desert.newpos.payui.UIUtils;

import static com.bcp.rest.JsonUtil.ROOT;
import static com.bcp.rest.JsonUtil.TIMEOUT;

public class JsonConsumeDep extends FinanceTransWS {

    private RspListProducts rspListProducts;
    private RspValidateAccount rspValidateAccount;
    private RspVerifyDeposit rspVerifyDeposit;

    private String accountId;
    private String document;
    private String documentType;

    public JsonConsumeDep(Context ctx, String transEname, TransInputPara p) {
        super(ctx, transEname, p);
    }

    /**
     * Metodo 1 consumo API BCP Deposito
     * @return
     */
    public boolean reqListProducts(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqListProducts ");
        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER ,TcodeSucces.MSGINFORMATION,false);

        try {

            JSONInfo jsonInfo = transUI.processApiRestStringGet(TIMEOUT, setHeader(false), setCardId(),ROOT + listUrl.get(0).getMethod());

            opnNumberPlus();

            if ((retVal=jsonInfo.getErr())!=0){
                validarError(jsonInfo);
                return false;
            }

            transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV ,TcodeSucces.MSGINFORMATION,false);

            rspListProducts = new RspListProducts();

            retVal = 0;
            if(!rspListProducts.getRspObtainDoc(jsonInfo.getJsonObject(),false,jsonInfo.getHeader(), Trans.DEPOSITO)) {
                retVal = TcodeError.T_ERR_UNPACK_JSON;
                return false;
            }
            Logger.logLine(Logger.LOG_GENERAL, " Saliendo de reqListProducts " + retVal);
        } catch (Exception e) {
            retVal = TcodeError.T_ERR_LIST_PROD;
           controllerCatch(e);
           return false;
        }
        return true;
    }

    /**
     * Metodo 2 consumo API BCP Deposito
     * @return
     */
    public boolean reqValidateAccount(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqValidateAccount ");
        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER ,TcodeSucces.MSGINFORMATION,false);

        try{
            JSONInfo jsonInfo = transUI.processApiRest(TIMEOUT, setValidateAccount(), setHeader(false), ROOT + listUrl.get(1).getMethod());

            opnNumberPlus();

            if ((retVal=jsonInfo.getErr())!=0){
                validarError(jsonInfo);
                return false;
            }

            transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV ,TcodeSucces.MSGINFORMATION,false);

            rspValidateAccount = new RspValidateAccount();

            retVal = 0;
            if(!rspValidateAccount.getRspObtainDoc(jsonInfo.getJsonObject(),jsonInfo.getHeader())) {
                retVal = TcodeError.T_ERR_UNPACK_JSON;
                return false;
            }
            Logger.logLine(Logger.LOG_GENERAL, " Saliendo de reqValidateAccount " + retVal);
        } catch (Exception e) {
            retVal = TcodeError.T_ERR_VALITE_ACCOUNT;
            controllerCatch(e);
            return false;
        }
        return true;
    }

    /**
     * Metodo 3 consumo API BCP Deposito
     * @return
     */
    public boolean reqVerifyDeposit(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqVerifyDeposit ");
        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER ,TcodeSucces.MSGINFORMATION,false);

        try{
            JSONInfo jsonInfo = transUI.processApiRest(TIMEOUT, setVerifyDeposit(), setHeader(true), ROOT + listUrl.get(2).getMethod());

            opnNumberPlus();

            if ((retVal=jsonInfo.getErr())!=0){
                validarError(jsonInfo);
                return false;
            }

            transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV ,TcodeSucces.MSGINFORMATION,false);

            rspVerifyDeposit = new RspVerifyDeposit();
            rspVerifyDeposit.setWithCard(depWithCard);

            retVal = 0;
            if(!rspVerifyDeposit.getRspObtainDoc(jsonInfo.getJsonObject())) {
                retVal = TcodeError.T_ERR_UNPACK_JSON;
                return false;
            }
            Logger.logLine(Logger.LOG_GENERAL, " Saliendo de reqVerifyDeposit " + retVal);
        } catch (Exception e) {
            retVal = TcodeError.T_ERR_VERIF_DEPOSI;
            controllerCatch(e);
            return false;
        }
        return true;
    }

    /**
     * Metodo 4 consumo API BCP Deposito
     * @return
     */
    public boolean reqExecuteTransaction(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqExecuteTransaction ");
        transUI.handlingBCP(TcodeSucces.HANDLING ,TcodeSucces.DEPOSITO_SUCC,false);


        try{
            JSONInfo jsonInfo = transUI.httpRequetsStringPostWithOutBody(TIMEOUT, setHeader(true),ROOT + listUrl.get(3).getMethod());

            opnNumberPlus();

            if ((retVal=jsonInfo.getErr())!=0){
                validarError(jsonInfo);
                return false;
            }

            rspExecTrans = new RspExecuteTransaction();
            rspExecTrans.setTransEname(transEName);
            rspExecTrans.setWithCard(depWithCard);

            retVal = 0;
            if(!rspExecTrans.getRspTransaction(jsonInfo.getJsonObject())) {
                retVal = TcodeError.T_ERR_UNPACK_JSON;
                return false;
            }
            try {
                transUI.handlingBCP(TcodeSucces.DEPOSITO_SUCC,TcodeSucces.MSGAUTORIZADO,true);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                Thread.currentThread().interrupt();
            }
            Logger.logLine(Logger.LOG_GENERAL, " Saliendo de reqExecuteTransaction " + retVal);
        } catch (Exception e) {
            retVal = TcodeError.T_ERR_EXECUTE_TRANS;
            controllerCatch(e);
            return false;
        }
        return true;
    }

    /**
     * Metodo 5 consumo API BCP Deposito
     * @return
     */
    public boolean reqConfirmVoucher(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqConfirmVoucher ");
        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER ,TcodeSucces.MSGINFORMATION,false);

        try {
            JSONInfo jsonInfo = transUI.processApiRest(TIMEOUT, setConfirmVoucher(), setHeader(true), ROOT + listUrl.get(4).getMethod());

            opnNumberPlus();

            if ((retVal = jsonInfo.getErr()) != 0) {
                validarError(jsonInfo);
                return false;
            }

            transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV, TcodeSucces.MSGINFORMATION, false);
            Logger.logLine(Logger.LOG_GENERAL, " Saliendo de reqConfirmVoucher " + retVal);
            return true;
        }catch (Exception e){
            retVal = TcodeError.T_ERR_CONF_VOUCHER;
            controllerCatch(e);
            return false;
        }
    }

    /**
     * envio pan del cliente
     * example 4557885900617195
     * @return
     */
    private String setCardId(){
        if(jweEncryptDecrypt(pan+"", false,false)) {
            return "cardId=" + jweDataEncryptDecrypt.getDataEncrypt();
        }
        return null;
    }

    private JSONObject setValidateAccount(){
        final ReqValidateAccount reqValidateAccount = new ReqValidateAccount();
        reqValidateAccount.setCtnAccountId(jweEncryptDecrypt(accountId+"", false,false) ? jweDataEncryptDecrypt.getDataEncrypt() : "");

        return reqValidateAccount.buildsObjectJSON();
    }

    private JSONObject setVerifyDeposit(){
        final ReqVerifyDeposit reqVerifyDeposit = new ReqVerifyDeposit();

        if (documentType==null) {
            reqVerifyDeposit.setFamCode(selectedAccountItem.getFamilyCode());
            reqVerifyDeposit.setCurrCode(selectedAccountItem.getCurrencyCode());
        }else{
            reqVerifyDeposit.setCtnDocumentType(documentType);
            reqVerifyDeposit.setCtnDocumentNumber(jweEncryptDecrypt(document,false,false) ? jweDataEncryptDecrypt.getDataEncrypt() : "");
        }
        reqVerifyDeposit.setTrack2(jweEncryptDecrypt(BCPConfig.getInstance(context).getTrack2Agente(BCPConfig.TRACK2AGENTEKEY)+"", false, false) ? jweDataEncryptDecrypt.getDataEncrypt() : "");
        reqVerifyDeposit.setCtnPinblock(pin+"");
        reqVerifyDeposit.setCtnAmount(UIUtils.formatMiles(String.valueOf(amount)));

        return reqVerifyDeposit.buildsObjectJSON();
    }

    private JSONObject setConfirmVoucher(){
        int status = -1;
        final ReqConfirmVoucher reqConfirmVoucher = new ReqConfirmVoucher();
        if(emv!=null)
            status = emv.afterOnline("00","0",field55(rspExecTrans.getCtnArpc()),0);

        if (rspExecTrans.getCtnArpc() != null && !rspExecTrans.getCtnArpc().equals(""))
            reqConfirmVoucher.setCtnArpcStatus(status == 0 ? "0" : "1");
        
        reqConfirmVoucher.setCtnPrintDecision(printClient);
        reqConfirmVoucher.setCtnPrintStatus(retValPrinter != 0 ? "0" : "1");

        return reqConfirmVoucher.buildsObjectJSON(false);
    }

    public RspListProducts getRspListProducts() {
        return rspListProducts;
    }

    public RspValidateAccount getRspValidateAccount() {
        return rspValidateAccount;
    }

    public RspVerifyDeposit getRspVerifyDeposit() {
        return rspVerifyDeposit;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String[] getLabelVerifDeposit() {
        return new String[]{
                rspVerifyDeposit.getCtnFullName()+"",
                rspVerifyDeposit.getCtnFmlDescript() + " " + rspVerifyDeposit.getCtnCurrenDescript(),
                rspVerifyDeposit.getCtnAccountId()+""
        };
    }
}
