package com.bcp.transactions.ultimasoperaciones;

import android.content.Context;
import com.bcp.rest.JSONInfo;
import com.bcp.rest.generic.request.ReqConfirmVoucher;
import com.bcp.rest.retiro.response.RspObtainDoc;
import com.bcp.rest.ultimasoperaciones.response.RspViewLastOperations;
import com.bcp.settings.BCPConfig;
import com.newpos.libpay.Logger;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.trans.TransInputPara;
import com.newpos.libpay.trans.finace.FinanceTransWS;

import org.json.JSONObject;

import static com.bcp.rest.JsonUtil.ROOT;
import static com.bcp.rest.JsonUtil.TIMEOUT;

public class JsonConsumeLastOperations extends FinanceTransWS {

    protected RspViewLastOperations rspViewLastOperations;

    public JsonConsumeLastOperations(Context ctx, String transEname, TransInputPara p){
        super(ctx,transEname,p);
    }

    public boolean rspLastOperations(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en rspLastOperations ");
        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER , TcodeSucces.MSGINFORMATION, false);

        try {
            JSONInfo jsonInfo = transUI.processApiRestStringGet(TIMEOUT, setHeader(true), setCardId() ,ROOT + listUrl.get(0).getMethod());

            opnNumberPlus();

            if ((retVal=jsonInfo.getErr())!=0){
                validarError(jsonInfo);
                return false;
            }

            transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV, TcodeSucces.MSGINFORMATION, false);

            Logger.debug("==Parsing Json==\n");
            rspViewLastOperations = new RspViewLastOperations();

            retVal = 0;
            if (!rspViewLastOperations.getRspOperations(jsonInfo.getJsonObject(),jsonInfo.getHeader())){
                retVal = TcodeError.T_ERR_UNPACK_JSON;
                return false;
            }
            Logger.logLine(Logger.LOG_GENERAL, " Saliendo de rspLastOperations " + retVal);
        }catch (Exception e){
            retVal = TcodeError.T_ERR_UNPACK_JSON;
            controllerCatch(e);
            return false;
        }
        return true;
    }

    private String setCardId() {
        if(jweEncryptDecrypt( BCPConfig.getInstance(context).getPanAgente(BCPConfig.PANAGENTEKEY) + "",false,false)){
            return "cardId=" + jweDataEncryptDecrypt.getDataEncrypt();
        }
        return null;
    }

    public boolean reqConfirVoucher(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqConfirVoucher ");
        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER ,TcodeSucces.MSGINFORMATION,false);
        try {
            JSONInfo jsonInfo = transUI.processApiRest(TIMEOUT, setConfirmVoucher(), setHeader(true), ROOT + listUrl.get(1).getMethod());

            opnNumberPlus();

            if ((retVal = jsonInfo.getErr()) !=0){
                validarError(jsonInfo);
                return false;
            }

            transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV, TcodeSucces.MSGINFORMATION, true);
            Logger.logLine(Logger.LOG_GENERAL, " Saliendo de reqConfirVoucher " + retVal);
            return true;
        } catch (Exception e) {
            retVal = TcodeError.T_ERR_CONF_VOUCHER;
            controllerCatch(e);
            return false;
        }
    }

    private JSONObject setConfirmVoucher() {
        final ReqConfirmVoucher reqConfirmVoucher = new ReqConfirmVoucher();
        reqConfirmVoucher.setCtnPrintOption(retVal == 0 ? "1" : "2");
        reqConfirmVoucher.setCtnPrintDecision("1");
        reqConfirmVoucher.setCtnPrintStatus(retVal !=0 ? "0":"1");

        return reqConfirmVoucher.buildsObjectJSON(true);
    }

}
