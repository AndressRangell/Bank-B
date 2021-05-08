package com.bcp.transactions.consulta;

import android.content.Context;

import com.android.newpos.pay.R;
import com.bcp.rest.JSONInfo;
import com.bcp.rest.consulta.request.ReqExcecuteTransacionsCons;
import com.bcp.rest.consulta.request.ReqListProductsCons;
import com.bcp.rest.generic.request.ReqConfirmVoucher;
import com.bcp.rest.generic.response.RspExecuteTransaction;
import com.bcp.rest.generic.response.RspListProducts;
import com.newpos.libpay.Logger;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.trans.Trans;
import com.newpos.libpay.trans.TransInputPara;
import com.newpos.libpay.trans.finace.FinanceTransWS;
import com.newpos.libpay.utils.PAYUtils;

import org.json.JSONObject;
import static com.bcp.rest.JsonUtil.ROOT;
import static com.bcp.rest.JsonUtil.TIMEOUT;

public class JsonConsumeConsulta extends FinanceTransWS {

    RspListProducts rspListProducts;

    private String typeQuery;


    public JsonConsumeConsulta(Context ctx, String transEname, TransInputPara p) {
        super(ctx, transEname, p);
    }

    /**
     * Metodo 1 consumo API BCP Consulta
     * @return
     */
    public boolean reqListProducts(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqListProducts ");
        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER ,TcodeSucces.MSGINFORMATION,false);

        try {
            JSONInfo jsonInfo = transUI.processApiRest(TIMEOUT, setListProducts(), setHeader(false), ROOT + listUrl.get(0).getMethod());

            opnNumberPlus();

            if ((retVal=jsonInfo.getErr())!=0){
                validarError(jsonInfo);
                return false;
            }

            transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV ,TcodeSucces.MSGINFORMATION,false);

            rspListProducts = new RspListProducts();

            retVal = 0;
            if(!rspListProducts.getRspObtainDoc(jsonInfo.getJsonObject(),false,jsonInfo.getHeader(), Trans.CONSULTAS)) {
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
     * Se envia el track2 del cliente
     * example: 4557885500076784=22022262195359000000
     * @return
     */
    private JSONObject setListProducts() {
        final  ReqListProductsCons reqListProductsCons = new ReqListProductsCons();
        reqListProductsCons.setTrackDos(jweEncryptDecrypt(track2, false,false)? jweDataEncryptDecrypt.getDataEncrypt():"");
        return reqListProductsCons.builJsonObject();
    }

    /**
     * Metodo 2 consumo API BCP Consulta
     * @return
     */
    public boolean reqExecuteTrans(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqExecuteTrans ");
        transUI.handlingBCP(TcodeSucces.HANDLING , typebill.equals("1") ? TcodeSucces.CONSULTAS_SALDOS : TcodeSucces.CONSULTAS_MOVIMIENTOS,false);

        try {
            JSONInfo jsonInfo = transUI.processApiRest(TIMEOUT, setExecuteTrans(), setHeader(true), ROOT + listUrl.get(1).getMethod());

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
                transUI.handlingBCP(context.getResources().getString(R.string.consulta) ,context.getResources().getString(R.string.autorizada),true);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace()," JsonConsumeConsulta " +  e.getMessage());
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
     * Metodo 3 consumo API BCP Consulta
     * @return
     */
    public void reqConfirmVoucher(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqConfirmVoucher ");
        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER ,TcodeSucces.MSGINFORMATION,false);

        try {
            JSONInfo jsonInfo = transUI.processApiRest(TIMEOUT, setConfirmVoucher(), setHeader(true), ROOT + listUrl.get(2).getMethod());

            opnNumberPlus();

            if ((retVal = jsonInfo.getErr()) != 0) {
                validarError(jsonInfo);
                return;
            }

            transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV, TcodeSucces.MSGINFORMATION, false);
            Logger.logLine(Logger.LOG_GENERAL, " Saliendo de reqConfirmVoucher " + retVal);
        }catch (Exception e){
            retVal = TcodeError.T_ERR_CONF_VOUCHER;
            controllerCatch(e);
        }
    }

    private JSONObject setExecuteTrans(){
        final ReqExcecuteTransacionsCons reqExcecuteTransacionsCons = new ReqExcecuteTransacionsCons();
        reqExcecuteTransacionsCons.setQuetype(typeQuery);
        reqExcecuteTransacionsCons.setFamCode(selectedAccountItem.getFamilyCode());
        reqExcecuteTransacionsCons.setCurrCode(selectedAccountItem.getCurrencyCode());

        reqExcecuteTransacionsCons.setCtnPinblock(pin+"");
        reqExcecuteTransacionsCons.setCtnField55(jweEncryptDecrypt(PAYUtils.packTags(PAYUtils.getOnliTags()) + "", false, false)? jweDataEncryptDecrypt.getDataEncrypt() : "");
        reqExcecuteTransacionsCons.setCtnArqc(jweEncryptDecrypt(arqc + "", false,false)? jweDataEncryptDecrypt.getDataEncrypt() : "");
        return reqExcecuteTransacionsCons.buildsObjectJSON();
    }

    private JSONObject setConfirmVoucher(){
        final ReqConfirmVoucher reqConfirmVoucher = new ReqConfirmVoucher();
        reqConfirmVoucher.setCtnArpcStatus("0");
        reqConfirmVoucher.setCtnPrintDecision(printClient);
        reqConfirmVoucher.setCtnPrintStatus(retValPrinter != 0 ? "0" : "1");
        reqConfirmVoucher.setCtnTc(arqc);
        reqConfirmVoucher.setCtnAac(getTC());

        return reqConfirmVoucher.buildsObjectJSON(true);
    }

    public RspListProducts getRspListProducts() {
        return rspListProducts;
    }

    public void setTypeQuery(String typeQuery) {
        typebill = typeQuery;
        this.typeQuery = typeQuery;
    }
}
