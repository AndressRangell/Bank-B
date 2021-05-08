package com.bcp.transactions.retiro;

import android.content.Context;

import com.android.newpos.pay.R;
import com.bcp.tools.Estadisticas;
import com.bcp.transactions.common.CommonFunctionalities;
import com.newpos.libpay.Logger;
import com.newpos.libpay.helper.iso8583.ISO8583;
import com.newpos.libpay.presenter.TransPresenter;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.trans.TransInputPara;

import static com.android.newpos.pay.StartAppBCP.estadisticas;
import static com.android.newpos.pay.StartAppBCP.variables;

public class Retiro extends JsonConsumeRet implements TransPresenter {

    /**
     * 金融交易类构造
     *
     * @param ctx        Context
     * @param transEname Nombre Transaccion
     * @param p          Parametros
     */
    public Retiro(Context ctx, String transEname, TransInputPara p) {
        super(ctx, transEname, p);
        init(transEname);
    }

    private void init(String transEname) {
        transUI = para.getTransUI();
        isReversal = true;
        isProcPreTrans = true;
        isSaveLog = true;
        isDebit = true;
        transEName = transEname;
        currencyName = CommonFunctionalities.tipoMoneda()[0];
        typeCoin = CommonFunctionalities.tipoMoneda()[1];
        hostId = variables.getIdAcquirer();
    }

    @Override
    public ISO8583 getISO8583() {
        return null;
    }

    @Override
    public void start() {
        Logger.logLine(Logger.LOG_GENERAL, "==== startRetiro");

        Logger.logLine(Logger.LOG_GENERAL, context.getResources().getString(R.string.inicioTrans) + transEName);

        if (!checkBatchAndSettle(true))
            return;

        if (!reqObtainToken()) {
            endTrans(TcodeSucces.RETIRO_SUCC, context.getString(R.string.auth));
            return;
        }

        if (!setAmount()) {
            endTrans(TcodeSucces.RETIRO_SUCC, context.getString(R.string.auth));
            return;
        }

        if (!cardProcess(INMODE_IC)) {
            endTrans(TcodeSucces.RETIRO_SUCC, context.getString(R.string.auth));
            return;
        }

        if(!reqObtainDocument()){
            endTrans(TcodeSucces.RETIRO_SUCC, context.getString(R.string.auth));
            return;
        }


        setSessionId(getRspObtainDoc().getSesionId());
        switch (getRspObtainDoc().getDocumentType()){
            case "1":
                if(!setDocument(context.getResources().getString(R.string.numDNI), 8, 8, false)) {
                    endTrans(TcodeSucces.RETIRO_SUCC, context.getString(R.string.auth));
                    return;
                }
                break;
            case "6":
                if(!setDocument(context.getResources().getString(R.string.RUC), 11, 11, false)){
                    endTrans(TcodeSucces.RETIRO_SUCC, context.getString(R.string.auth));
                    return;
                }
                break;
            case "3":
                if(!setDocument(context.getResources().getString(R.string.numTarjetaExtra), 5, 12, true)){
                    endTrans(TcodeSucces.RETIRO_SUCC, context.getString(R.string.auth));
                    return;
                }
                break;
            case "4":
                if(!setDocument(context.getResources().getString(R.string.passport), 5, 12, true)){
                    endTrans(TcodeSucces.RETIRO_SUCC, context.getString(R.string.auth));
                    return;
                }
                break;
            default:
                retVal = TcodeError.T_USER_CANCEL_INPUT;

                endTrans(TcodeSucces.RETIRO_SUCC, context.getString(R.string.auth));
                return;
        }

        if(!reqListProducts()){
            endTrans(TcodeSucces.RETIRO_SUCC, context.getString(R.string.auth));
            return;
        }

        if(drawMenuApiRest(context.getResources().getString(R.string.selecCuenta), getRspListProducts().getAccounts())==-1) {
            endTrans(TcodeSucces.RETIRO_SUCC, context.getString(R.string.auth));
            return;
        }

        prepareOnline();
        endTrans(TcodeSucces.RETIRO_SUCC, context.getString(R.string.auth));

        Logger.logLine(Logger.LOG_GENERAL, context.getResources().getString(R.string.finTrans)+ transEName);
    }

    /**
     * 准备联机
     */
    private void prepareOnline() {

        procCode = CommonFunctionalities.getProCode();

        Logger.logLine(Logger.LOG_GENERAL, "=====prepareOnline");

        Logger.logLine(Logger.LOG_GENERAL, "procCode" + procCode);

        if (!requestPin(context.getResources().getString(R.string.ingClaveCliente))) {
            return;
        }

        if (retVal == 0) {
            if(!reqExecuteTrans()){
                if(retVal == TcodeError.T_ERR_NOT_INTERNET){
                    estadisticas.writeEstadisticas(Estadisticas.TRANSTIMEOUT,para.getTransType());
                }else {
                    estadisticas.writeEstadisticas(Estadisticas.TRANSFAIL,para.getTransType());
                }
                return;
            }

            //save batch
            retVal = saveTransFinance();

            //printer voucher
            retVal = printerDataClient();

            reqConfirmVoucher();

            retVal = printerDataCommerce();

            Logger.logLine(Logger.LOG_GENERAL, "retVal " + retVal);
        } else {
            clearPan();
        }
    }

    private boolean setDocument(String typeDoc, int minLen, int maxLen,  boolean isAlfa){

        if ((retVal=CommonFunctionalities.setNumberDoc(transUI, fillScreenDocument(false, minLen, maxLen, context.getResources().getString(R.string.ingresoDoc), typeDoc, isAlfa) )) != 0) {
            return false;
        }

        setDocNumber(CommonFunctionalities.getNumDocument());

        Logger.logLine(Logger.LOG_GENERAL, "=====setDocument");

        return reqValidateDocument();
    }
}
