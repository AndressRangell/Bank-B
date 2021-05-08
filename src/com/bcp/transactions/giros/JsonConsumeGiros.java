package com.bcp.transactions.giros;


import android.content.Context;

import com.bcp.rest.JSONInfo;
import com.bcp.rest.generic.request.ReqConfirmVoucher;
import com.bcp.rest.giros.cobro_nacional.request.ReqExecuteTransaction;
import com.bcp.rest.giros.cobro_nacional.request.ReqListProducts;
import com.bcp.rest.giros.cobro_nacional.response.RspExecuteTransactionGiros;
import com.bcp.rest.giros.cobro_nacional.response.RspListProductsGiros;
import com.bcp.rest.giros.emision.request.ReqExecuteTransactionEmision;
import com.bcp.rest.giros.emision.response.RspExecuteTransactionEmision;
import com.bcp.settings.BCPConfig;
import com.newpos.libpay.Logger;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.trans.TransInputPara;
import com.newpos.libpay.trans.finace.FinanceTransWS;
import com.newpos.libpay.utils.PAYUtils;

import org.json.JSONObject;
import static com.bcp.rest.JsonUtil.ROOT;
import static com.bcp.rest.JsonUtil.TIMEOUT;

public class JsonConsumeGiros extends FinanceTransWS {


    public JsonConsumeGiros(Context ctx, String transEname, TransInputPara p) {
        super(ctx, transEname, p);
    }

    /**
     * Metodo 2 consumo API BCP Giros Cobros
     * @return
     */
    public boolean reqListProductsGiros(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqListProductsGiros ");
        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER ,TcodeSucces.MSGINFORMATION,false);

        try {

            JSONInfo jsonInfo = transUI.processApiRest( TIMEOUT, setCardId(),setHeader(arguments.getTypetransaction() == 2),ROOT + listUrl.get(arguments.getTypetransaction() == 2 ? 1 : 0).getMethod());

            opnNumberPlus();

            if ((retVal=jsonInfo.getErr())!=0){
                validarError(jsonInfo);
                return false;
            }

            transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV ,TcodeSucces.MSGINFORMATION,false);

            rspListProductsGiros = new RspListProductsGiros();

            retVal = 0;
            if(!rspListProductsGiros.getRspListProducts(jsonInfo.getJsonObject(),arguments.getTypetransaction(),jsonInfo.getHeader())) {
                retVal = TcodeError.T_ERR_UNPACK_JSON;
                return false;
            }

            setSessionId(rspListProductsGiros.getRspsessionId());
            Logger.logLine(Logger.LOG_GENERAL, " Saliendo de reqListProductsGiros " + retVal);
        } catch (Exception e) {
            retVal = TcodeError.T_ERR_LIST_PROD;
            controllerCatch(e);
            return false;
        }
        return true;
    }
    /**
     * Metodo 3 consumo API BCP Giros Cobros
     * @return
     */
    public boolean reqExecuteTransactionGiros(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqExecuteTransactionGiros ");
        transUI.handlingBCP(TcodeSucces.HANDLING ,TcodeSucces.GIRO_SUCC,false);

        try {
            JSONInfo jsonInfo = transUI.processApiRest(TIMEOUT, setExecuteTrans(), setHeader(true), ROOT + listUrl.get(2).getMethod());

            opnNumberPlus();

            if ((retVal=jsonInfo.getErr())!=0){
                validarError(jsonInfo);
                return false;
            }

            rspExecTransGiros = new RspExecuteTransactionGiros();
            rspExecTransGiros.setTypePayment(arguments.getTypepayment());

            retVal = 0;
            if(!rspExecTransGiros.getRspExecuteTrans(jsonInfo.getJsonObject())) {
                retVal = TcodeError.T_ERR_UNPACK_JSON;
                return false;
            }
            try {
                transUI.handlingBCP(TcodeSucces.OK ,TcodeSucces.COBRO_AUT,true);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                Thread.currentThread().interrupt();
            }
            Logger.logLine(Logger.LOG_GENERAL, " Saliendo de reqExecuteTransactionGiros " + retVal);
        } catch (Exception e) {
            retVal = TcodeError.T_ERR_EXECUTE_TRANS;
            controllerCatch(e);
            return false;
        }
        return true;
    }
    /**
     * Metodo 4 consumo API BCP Giros
     * @return
     */
    public boolean reqConfirmVoucher(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqConfirmVoucher ");

        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER ,TcodeSucces.MSGINFORMATION,false);

        try {
            JSONInfo jsonInfo = transUI.processApiRest(TIMEOUT, setConfirmVoucher(), setHeader(true), ROOT + listUrl.get(arguments.getTypetransaction() == 2 ? 3 : 4).getMethod());

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
     * Metodo 5 consumo API BCP Giros Emision
     * @return
     */
    public boolean reqExecuteTransactionEmision(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqExecuteTransactionEmision ");
        transUI.handlingBCP(TcodeSucces.HANDLING ,TcodeSucces.PROCE_GIRO,false);

        try {
            JSONInfo jsonInfo = transUI.processApiRest(TIMEOUT, setExecuteTransEmision(), setHeader(true), ROOT + listUrl.get(3).getMethod());

            opnNumberPlus();

            if ((retVal=jsonInfo.getErr())!=0){
                validarError(jsonInfo);
                return false;
            }

            rspExecTransEmision = new RspExecuteTransactionEmision();

            retVal = 0;

            if(!rspExecTransEmision.getRspExecuteTrans(jsonInfo.getJsonObject())) {
                retVal = TcodeError.T_ERR_UNPACK_JSON;
                return false;
            }
            try {
                transUI.handlingBCP(TcodeSucces.OK ,TcodeSucces.GIRO_EMITIDO,true);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                Thread.currentThread().interrupt();
            }
            Logger.logLine(Logger.LOG_GENERAL, " Saliendo de reqExecuteTransactionEmision " + retVal);
        } catch (Exception e) {
            retVal = TcodeError.T_ERR_EXECUTE_TRANS;
            controllerCatch(e);
            return false;
        }
        return true;
    }

    public RspListProductsGiros getRspListProductsGiros() {
        return rspListProductsGiros;
    }

    private JSONObject setCardId(){
        final ReqListProducts reqlistproducts = new ReqListProducts();
        reqlistproducts.setReqTrack2(track2 + "");
        return reqlistproducts.buildsObjectJSON(arguments.getTypetransaction());
    }

    private JSONObject setExecuteTrans(){
        final ReqExecuteTransaction reqExecTransaction = new ReqExecuteTransaction();

        reqExecTransaction.setReqPaymentMethod(arguments.getTypepayment());
        reqExecTransaction.setReqBankDraftPassEncrypted(arguments.getArgument5());
        reqExecTransaction.setReqBankDraftReference(arguments.getPaymentOrders()[Integer.parseInt(arguments.getArgument3())].getRspTransactionReference());
        if (arguments.getTypepayment().equals("1")){
            reqExecTransaction.setReqTrack2(jweEncryptDecrypt(BCPConfig.getInstance(context).getTrack2Agente(BCPConfig.TRACK2AGENTEKEY) +"",false,false) ? jweDataEncryptDecrypt.getDataEncrypt() : "");
        }else {
            reqExecTransaction.setReqFamilyCode(selectedAccountItem.getFamilyCode());
            reqExecTransaction.setReqCurrencyCode(selectedAccountItem.getCurrencyCode());
        }

        return reqExecTransaction.buildsObjectJSON();
    }

    private JSONObject setConfirmVoucher(){
        boolean isCard = false;
        final ReqConfirmVoucher reqConfirmVoucher = new ReqConfirmVoucher();

        reqConfirmVoucher.setCtnPrintDecision(printClient);
        reqConfirmVoucher.setCtnPrintStatus(retValPrinter != 0 ? "0" : "1");

        if (arguments.getTypetransaction() == 1 && arguments.getTypepayment().equals("2")){
            isCard = true;
            reqConfirmVoucher.setCtnArpcStatus(emv.afterOnline("00","0",field55(rspExecTransEmision.getRspArpc()),0) == 0 ? "0" : "1");
            reqConfirmVoucher.setCtnAac(getTC());
            reqConfirmVoucher.setCtnTc(arqc);
        }else {
            reqConfirmVoucher.setCtnArpcStatus("0");
        }
        return reqConfirmVoucher.buildsObjectJSON(isCard);
    }

    private JSONObject setExecuteTransEmision(){
        final ReqExecuteTransactionEmision reqExecTransaction = new ReqExecuteTransactionEmision();

        reqExecTransaction.setReqBankDraftPassEncrypted(arguments.getArgument5());//clave
        reqExecTransaction.setReqPinblockEncrypted(pin + "");//Pin
        if (emv != null){
            reqExecTransaction.setReqARQC(arqc + "");
            reqExecTransaction.setReqField55(PAYUtils.packTags(PAYUtils.getOnliTags()) + "");
        }

        return reqExecTransaction.buildsObjectJSON();
    }

}
