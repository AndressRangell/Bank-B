package com.bcp.transactions.pagoservicios;

import android.content.Context;

import com.bcp.rest.JSONInfo;
import com.bcp.rest.generic.request.ReqConfirmVoucher;
import com.bcp.rest.generic.response.RspListProducts;
import com.bcp.rest.pagoservicios.request.ReqExecuteTransactionPS;
import com.bcp.rest.pagoservicios.request.ReqListClientDebts;
import com.bcp.rest.pagoservicios.response.RspExecuteTransactionPS;
import com.bcp.rest.pagoservicios.response.RspListClientDebts;
import com.bcp.rest.retiro.request.ReqValidateDocument;
import com.bcp.rest.retiro.response.RspObtainDoc;
import com.bcp.settings.BCPConfig;
import com.bcp.transactions.common.CommonFunctionalities;
import com.newpos.libpay.Logger;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.trans.Trans;
import com.newpos.libpay.trans.TransInputPara;
import com.newpos.libpay.trans.finace.FinanceTransWS;
import com.newpos.libpay.utils.PAYUtils;

import org.json.JSONObject;

import static com.bcp.rest.JsonUtil.ROOT;
import static com.bcp.rest.JsonUtil.TIMEOUT;

public class JsonConsumePagoServicios extends FinanceTransWS {

    protected RspListClientDebts rspListClientDebts;
    private RspObtainDoc rspObtainDocumentType;
    private String docNumber;


    public JsonConsumePagoServicios(Context ctx, String transEname, TransInputPara p) {
        super(ctx, transEname, p);
    }

    /**
     * Metodo 2 consumo API BCP Pago Servicio
     * @return
     */
    public boolean reqListClientDebts(){

        transUI.handlingBCP(TcodeSucces.VERIFICANDO ,TcodeSucces.MSGEMPTY,false);

        try {
            JSONInfo jsonInfo = transUI.processApiRest(TIMEOUT, setListClientDebts(), setHeader(true), ROOT + listUrl.get(1).getMethod());

            opnNumberPlus();

            if ((retVal=jsonInfo.getErr())!=0){
                validarError(jsonInfo);
                return false;
            }

            rspListClientDebts = new RspListClientDebts();

            retVal = 0;
            if(!rspListClientDebts.getRspObtain(jsonInfo.getJsonObject(),setHeader(false),arguments.getTipoPagoServicio(),arguments.getTypepayment())) {
                retVal = TcodeError.T_ERR_UNPACK_JSON;
                return false;
            }

            try {
                transUI.handlingBCP(TcodeSucces.VERIFICANDO ,TcodeSucces.MSGEMPTY,true);
                arguments.setListDebts(rspListClientDebts.getListsDebts());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                Thread.currentThread().interrupt();
            }
        } catch (Exception e) {
            retVal = TcodeError.T_ERR_EXECUTE_TRANS;
            controllerCatch(e);
            return false;
        }
        return true;
    }

    /**
     * Metodo 3 consumo API BCP Pago Servicio
     * @return
     */
    public boolean reqExecuteTrans(){
        transUI.handlingBCP(TcodeSucces.HANDLING ,TcodeSucces.PAGOSERVICIO,false);

        try {
            JSONInfo jsonInfo = transUI.processApiRest(TIMEOUT, setExecuteTrans(), setHeader(true), ROOT + listUrl.get(9).getMethod());

            opnNumberPlus();

            if ((retVal=jsonInfo.getErr())!=0){
                validarError(jsonInfo);
                return false;
            }

            rspExecuteTransactionPS = new RspExecuteTransactionPS();

            retVal = 0;
            if(!rspExecuteTransactionPS.getRspTransactionPS(jsonInfo.getJsonObject(),arguments.getTipoPagoServicio(),arguments.getTypepayment())) {
                retVal = TcodeError.T_ERR_UNPACK_JSON;
                return false;
            }
            try {
                transUI.handlingBCP(TcodeSucces.OK ,TcodeSucces.PAGO_AUTORIZADI,true);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                Thread.currentThread().interrupt();
            }
        } catch (Exception e) {
            retVal = TcodeError.T_ERR_EXECUTE_TRANS;
            controllerCatch(e);
            return false;
        }
        return true;
    }

    /**
     * Metodo 4 consumo API BCP Pago Servicio
     * @return
     */
    public boolean reqConfirmVoucher(){

        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER ,TcodeSucces.MSGINFORMATION,false);

        try {
            JSONInfo jsonInfo = transUI.processApiRest(TIMEOUT, setConfirmVoucher(), setHeader(true), ROOT + listUrl.get(10).getMethod());

            opnNumberPlus();

            if ((retVal = jsonInfo.getErr()) != 0) {
                validarError(jsonInfo);
                return false;
            }

            transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV, TcodeSucces.MSGINFORMATION, false);
            return true;
        }catch (Exception e){
            retVal = TcodeError.T_ERR_CONF_VOUCHER;
            controllerCatch(e);
            return false;
        }
    }

    /**
     * Metodo 5 consumo API BCP Pago Servicio
     * @return
     */
    public boolean reqObtainDocument(){
        try {
            String track2Cipher;
            if ((track2Cipher = setDataObtainDoc())==null)
                return false;

            JSONInfo jsonInfo = transUI.processApiRestStringGet(TIMEOUT, setHeader(false), track2Cipher,ROOT + listUrl.get(5).getMethod());

            opnNumberPlus();

            if ((retVal=jsonInfo.getErr())!=0){
                validarError(jsonInfo);
                return false;
            }

            transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV ,TcodeSucces.MSGINFORMATION,false);

            Logger.debug("==Parsing Json==\n");
            rspObtainDocumentType = new RspObtainDoc();

            retVal = 0;
            if(!rspObtainDocumentType.getRspObtainDoc(jsonInfo.getJsonObject(),jsonInfo.getHeader())) {
                retVal = TcodeError.T_ERR_UNPACK_JSON;
                return false;
            }
            Logger.debug(rspObtainDocumentType.getPerson());
            Logger.debug(rspObtainDocumentType.getDocumentType());
            Logger.debug(rspObtainDocumentType.getSesionId());
            Logger.debug("====================\n");
        } catch (Exception e) {
            retVal = TcodeError.T_ERR_OBTE_DOCUMENT;
            controllerCatch(e);
            return false;
        }
        return true;
    }

    /**
     * Metodo 6 consumo API BCP Pago Servicio
     * @return
     */
    public boolean reqValidateDocument(){
        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER ,TcodeSucces.MSGINFORMATION,false);

        try {
            JSONInfo jsonInfo = transUI.processApiRest(TIMEOUT, setValidateDocument(), setHeader(true), ROOT + listUrl.get(6).getMethod());

            opnNumberPlus();

            if ((retVal = jsonInfo.getErr()) != 0) {
                validarError(jsonInfo);
                return false;
            }

            transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV, TcodeSucces.MSGINFORMATION, false);
            return true;
        }catch (Exception e){
            retVal = TcodeError.T_ERR_VALIDATE_DOCUMENT;
            transUI.showError(transEName,retVal);
            controllerCatch(e);
            return false;
        }
    }

    private JSONObject setValidateDocument(){
        final ReqValidateDocument reqValidateDocument = new ReqValidateDocument();
        reqValidateDocument.setDocNumber(jweEncryptDecrypt(docNumber + "", false,false)? jweDataEncryptDecrypt.getDataEncrypt() : "");
        return reqValidateDocument.buildsObjectJSON();
    }

    /**
     * Metodo 8 consumo API BCP Pago Servicio
     * @return
     */
    public boolean reqListProducts(){

        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER ,TcodeSucces.MSGINFORMATION,false);

        try {
            JSONInfo jsonInfo = transUI.processApiRestStringGet(TIMEOUT, setHeader(false), setCardId(),ROOT + listUrl.get(7).getMethod());
            opnNumberPlus();

            if ((retVal=jsonInfo.getErr())!=0){
                validarError(jsonInfo);
                return false;
            }

            transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV ,TcodeSucces.MSGINFORMATION,false);

            rspListProductsPS = new RspListProducts();

            retVal = 0;
            if(!rspListProductsPS.getRspObtainDoc(jsonInfo.getJsonObject(),true,jsonInfo.getHeader(), Trans.PAGOSERVICIOS)) {
                retVal = TcodeError.T_ERR_UNPACK_JSON;
                return false;
            }
        } catch (Exception e) {
            retVal = TcodeError.T_ERR_LIST_PROD;
            controllerCatch(e);
            return false;
        }
        return true;
    }
    /**
     * envio setListClientDebts
     * @return
     */

    private JSONObject setListClientDebts(){

        arguments = CommonFunctionalities.getArguments();
        final ReqListClientDebts reqListClientDebts = new ReqListClientDebts();

        reqListClientDebts.setClientDepositCode(arguments.getDebcode());
        reqListClientDebts.setAffiliationCode(arguments.getAffiliationCode());

        if(arguments.getTypepayment().equals("1")){
            reqListClientDebts.setReqTrack2(BCPConfig.getInstance(context).getTrack2Agente(BCPConfig.TRACK2AGENTEKEY) +"");
        }else {
            reqListClientDebts.setReqField55(PAYUtils.packTags(PAYUtils.getOnliTags()) + "");
            reqListClientDebts.setReqArqc(arqc + "");
        }
        reqListClientDebts.setReqPinblock(pin + "");

        return reqListClientDebts.builJsonObject();
    }

    private JSONObject setExecuteTrans(){
        final ReqExecuteTransactionPS reqExecuteTransactionPs = new ReqExecuteTransactionPS();

        if (arguments.getTypepayment().equals("1")){
            if (arguments.getTipoPagoServicio().equals(Trans.PAGO_IMPORTE))
                reqExecuteTransactionPs.setDocumentCode(arguments.getDocumentCode());//validar si existe para enviar

            reqExecuteTransactionPs.setDocumentType("1");
            reqExecuteTransactionPs.setDocumentNumber(arguments.getClientDocumentNumber());

            if (!arguments.getTipoPagoServicio().equals(Trans.PAGO_PASEMOVISTAR))
                reqExecuteTransactionPs.setCtnTrack2(BCPConfig.getInstance(context).getTrack2Agente(BCPConfig.TRACK2AGENTEKEY) +"");

        }else {
            reqExecuteTransactionPs.setCtnField55(PAYUtils.packTags(PAYUtils.getOnliTags()) + "");
            reqExecuteTransactionPs.setCtnArqc(arqc + "");
        }

        if (!arguments.getTipoPagoServicio().equals(Trans.PAGO_PASEMOVISTAR))
            reqExecuteTransactionPs.setCtnPinblock(pin);

        reqExecuteTransactionPs.setPaymentMethod(arguments.getTypepayment());

        return reqExecuteTransactionPs.buildsObjectJSON();
    }

    private JSONObject setConfirmVoucher(){
        boolean isCard = false;
        final ReqConfirmVoucher reqConfirmVoucher = new ReqConfirmVoucher();

        reqConfirmVoucher.setCtnPrintDecision(printClient);
        reqConfirmVoucher.setCtnPrintStatus(retValPrinter != 0 ? "0" : "1");

        if (arguments.getTypepayment().equals("2")){
            isCard = true;
            reqConfirmVoucher.setCtnArpcStatus(emv.afterOnline("00","0",field55(rspExecuteTransactionPS.getArpc()),0) == 0 ? "0" : "1");
            reqConfirmVoucher.setCtnAac(getTC());
            reqConfirmVoucher.setCtnTc(arqc);
        }
        return reqConfirmVoucher.buildsObjectJSON(isCard);
    }

    /**
     * Envia el track2 del cliente
     * example: 4557885500076784=22022262195359000000
     * example: 4557885790258027D21112262365418800000 (dni 29360558)
     * @return
     */
    private String setDataObtainDoc(){

        if(jweEncryptDecrypt(track2+"", false,false)) {
            return "track2=" + jweDataEncryptDecrypt.getDataEncrypt();
        }

        return null;
    }

    public RspListProducts getRspListProducts() {
        return rspListProductsPS;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    /**
     * envio pan del cliente
     * example 4557885900617195
     * @return
     */
    private String setCardId(){
        if(jweEncryptDecrypt(track2+"", false,false)) {
            return "cardId=" + jweDataEncryptDecrypt.getDataEncrypt();
        }
        return null;
    }
}
