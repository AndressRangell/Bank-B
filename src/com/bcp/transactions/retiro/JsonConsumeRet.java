package com.bcp.transactions.retiro;

import android.content.Context;
import com.bcp.rest.JSONInfo;
import com.bcp.rest.generic.request.ReqConfirmVoucher;
import com.bcp.rest.generic.response.RspExecuteTransaction;
import com.bcp.rest.generic.response.RspListProducts;
import com.bcp.rest.retiro.request.ReqExecTransaction;
import com.bcp.rest.retiro.request.ReqValidateDocument;
import com.bcp.rest.retiro.response.RspObtainDoc;
import com.bcp.settings.BCPConfig;
import com.newpos.libpay.Logger;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.trans.Trans;
import com.newpos.libpay.trans.TransInputPara;
import com.newpos.libpay.trans.finace.FinanceTransWS;
import com.newpos.libpay.utils.PAYUtils;

import org.json.JSONObject;

import cn.desert.newpos.payui.UIUtils;
import static com.bcp.rest.JsonUtil.ROOT;
import static com.bcp.rest.JsonUtil.TIMEOUT;

public class JsonConsumeRet extends FinanceTransWS {

    private RspObtainDoc rspObtainDoc;
    private RspListProducts rspListProducts;
    private String docNumber;

    JsonConsumeRet(Context ctx, String transEname, TransInputPara p){
        super(ctx, transEname, p);
    }

    /**
     * Metodo 1 consumo API BCP Retiro
     * @return
     */
    public boolean reqObtainDocument(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqObtainDocument ");
        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER ,TcodeSucces.MSGINFORMATION,false);

        try {
            Logger.logLine(Logger.LOG_GENERAL, " =====reqObtainDocument ");
            String track2Cipher;
            if ((track2Cipher = setDataObtainDoc())==null){
                Logger.logLine(Logger.LOG_GENERAL, " track2Cipher == Null");
                return false;
            }


            JSONInfo jsonInfo = transUI.processApiRestStringGet(TIMEOUT, setHeader(false), track2Cipher,ROOT + listUrl.get(0).getMethod());

            opnNumberPlus();

            if ((retVal=jsonInfo.getErr())!=0){
                validarError(jsonInfo);
                return false;
            }

            transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV ,TcodeSucces.MSGINFORMATION,false);

            Logger.debug("==Parsing Json==\n");
            rspObtainDoc = new RspObtainDoc();

            retVal = 0;
            if(!rspObtainDoc.getRspObtainDoc(jsonInfo.getJsonObject(),jsonInfo.getHeader())) {
                retVal = TcodeError.T_ERR_UNPACK_JSON;
                return false;
            }
            Logger.logLine(Logger.LOG_GENERAL, " Saliendo de reqObtainDocument " + retVal);
            Logger.debug(rspObtainDoc.getPerson());
            Logger.debug(rspObtainDoc.getDocumentType());
            Logger.debug(rspObtainDoc.getSesionId());
            Logger.debug("====================\n");
        } catch (Exception e) {
            retVal = TcodeError.T_ERR_OBTE_DOCUMENT;
            controllerCatch(e);
            return false;
        }
        return true;
    }

    /**
     * Metodo 2 consumo API BCP Retiro
     * @return
     */
    public boolean reqValidateDocument(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqValidateDocument ");
        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER ,TcodeSucces.MSGINFORMATION,false);

        try {
            JSONInfo jsonInfo = transUI.processApiRest(TIMEOUT, setValidateDocument(), setHeader(true), ROOT + listUrl.get(1).getMethod());

            opnNumberPlus();

            if ((retVal = jsonInfo.getErr()) != 0) {
                validarError(jsonInfo);
                return false;
            }

            transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV, TcodeSucces.MSGINFORMATION, false);
            Logger.logLine(Logger.LOG_GENERAL, " Saliendo de reqValidateDocument " + retVal);
            return true;
        }catch (Exception e){
            retVal = TcodeError.T_ERR_VALIDATE_DOCUMENT;
            transUI.showError(transEName,retVal);
            controllerCatch(e);
            return false;
        }
    }

    /**
     * Metodo 3 consumo API BCP Retiro
     * @return
     */
    public boolean reqListProducts(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqListProducts ");
        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER ,TcodeSucces.MSGINFORMATION,false);

        try {
            JSONInfo jsonInfo = transUI.processApiRestStringGet(TIMEOUT, setHeader(true), null,ROOT + listUrl.get(2).getMethod());

            opnNumberPlus();

            if ((retVal=jsonInfo.getErr())!=0){
                validarError(jsonInfo);
                return false;
            }

            transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV ,TcodeSucces.MSGINFORMATION,false);

            rspListProducts = new RspListProducts();

            retVal = 0;
            if(!rspListProducts.getRspObtainDoc(jsonInfo.getJsonObject(),true,jsonInfo.getHeader(), Trans.RETIRO)) {
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
     * Metodo 4 consumo API BCP Retiro
     * @return
     */
    public boolean reqExecuteTrans(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqExecuteTrans ");
        transUI.handlingBCP(TcodeSucces.HANDLING ,TcodeSucces.RETIRO_SUCC,false);

        try {
            JSONInfo jsonInfo = transUI.processApiRest(TIMEOUT, setExecuteTrans(), setHeader(true), ROOT + listUrl.get(3).getMethod());

            opnNumberPlus();

            if ((retVal=jsonInfo.getErr())!=0){
                validarError(jsonInfo);
                return false;
            }

            rspExecTrans = new RspExecuteTransaction();
            rspExecTrans.setTransEname(transEName);
            rspExecTrans.setWithCard(true);

            retVal = 0;
            if(!rspExecTrans.getRspTransaction(jsonInfo.getJsonObject())) {
                retVal = TcodeError.T_ERR_UNPACK_JSON;
                return false;
            }
            try {
                transUI.handlingBCP(TcodeSucces.RETIRO_SUCC ,TcodeSucces.MSGAUTORIZADO,true);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                Thread.currentThread().interrupt();
            }
            Logger.logLine(Logger.LOG_GENERAL, " Saliendo de reqExecuteTrans " + retVal);
        } catch (Exception e) {
            retVal = TcodeError.T_ERR_EXECUTE_TRANS;
            controllerCatch(e);
            return false;
        }
        return true;
    }

    /**
     * Metodo 5 consumo API BCP Retiro
     * @return
     */
    public boolean reqConfirmVoucher(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqExecuteTrans ");
        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER ,TcodeSucces.MSGINFORMATION,false);

        try {
            JSONInfo jsonInfo = transUI.processApiRest(TIMEOUT, setConfirmVoucher(), setHeader(true), ROOT + listUrl.get(4).getMethod());

            opnNumberPlus();

            if ((retVal = jsonInfo.getErr()) != 0) {
                validarError(jsonInfo);
                return false;
            }

            transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV, TcodeSucces.MSGINFORMATION, false);
            Logger.logLine(Logger.LOG_GENERAL, " Saliendo de reqExecuteTrans " + retVal);
            return true;
        }catch (Exception e){
            retVal = TcodeError.T_ERR_CONF_VOUCHER;
            controllerCatch(e);
            return false;
        }
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

    private JSONObject setValidateDocument(){
        final ReqValidateDocument reqValidateDocument = new ReqValidateDocument();
        reqValidateDocument.setDocNumber(jweEncryptDecrypt(docNumber + "", false,false)? jweDataEncryptDecrypt.getDataEncrypt() : "");
        return reqValidateDocument.buildsObjectJSON();
    }

    private JSONObject setExecuteTrans(){
        final ReqExecTransaction reqExecTransaction = new ReqExecTransaction();
        reqExecTransaction.setFamCode(selectedAccountItem.getFamilyCode());
        reqExecTransaction.setCurrCode(selectedAccountItem.getCurrencyCode());

        reqExecTransaction.setCtnPinblock(pin+"");
        reqExecTransaction.setCtnField55(jweEncryptDecrypt(PAYUtils.packTags(PAYUtils.getOnliTags()) + "", false,false)? jweDataEncryptDecrypt.getDataEncrypt() : "");
        reqExecTransaction.setCtnArqc(jweEncryptDecrypt(arqc + "", false,false)? jweDataEncryptDecrypt.getDataEncrypt() : "");

        reqExecTransaction.setCtnCardId(jweEncryptDecrypt(BCPConfig.getInstance(context).getPanAgente(BCPConfig.PANAGENTEKEY) + "", false,false)? jweDataEncryptDecrypt.getDataEncrypt() : "");

        reqExecTransaction.setCtnAmount(UIUtils.formatMiles(String.valueOf(amount)));

        switch (rspObtainDoc.getDocumentType()){
            case "1":
            case "6":
                funcCipherDocument(docNumber);
                reqExecTransaction.setEncryptedIdc(cipherDocument.getDocument());
                break;
        }


        return reqExecTransaction.buildsObjectJSON();
    }

    private JSONObject setConfirmVoucher(){
        final ReqConfirmVoucher reqConfirmVoucher = new ReqConfirmVoucher();
        reqConfirmVoucher.setCtnArpcStatus(emv.afterOnline("00","0",field55(rspExecTrans.getCtnArpc()),0) == 0 ? "0" : "1");
        reqConfirmVoucher.setCtnPrintDecision(printClient);
        reqConfirmVoucher.setCtnPrintStatus(retValPrinter != 0 ? "0" : "1");
        reqConfirmVoucher.setCtnTc(arqc);
        reqConfirmVoucher.setCtnAac(getTC());

        return reqConfirmVoucher.buildsObjectJSON(true);
    }

    public RspObtainDoc getRspObtainDoc() {
        return rspObtainDoc;
    }

    public RspListProducts getRspListProducts() {
        return rspListProducts;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }
}
