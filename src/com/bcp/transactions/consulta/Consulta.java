package com.bcp.transactions.consulta;

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

public class Consulta extends JsonConsumeConsulta implements TransPresenter {

    public Consulta(Context ctx, String transEname, TransInputPara p) {
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
    public void start() {

        Logger.logLine(Logger.LOG_GENERAL, "==== startConsulta");

        Logger.debug(context.getResources().getString(R.string.inicioTrans) + transEName);

        int selectItem;

        if(!checkBatchAndSettle(true))
            return;

        if (!reqObtainToken()) {
            endTrans(TcodeSucces.CONSULTAS, context.getString(R.string.autorizada));
            return;
        }

        if ((selectItem=drawMenuHardcode(context.getResources().getString(R.string.tipoConsulta), new String[]{context.getResources().getString(R.string.msgsandos),context.getResources().getString(R.string.msgMovimiento)}))==-1) {
            endTrans(TcodeSucces.CONSULTAS, context.getString(R.string.autorizada));
            return;
        }

        setTypeQuery(String.valueOf(selectItem+1));

        if(!cardProcess(INMODE_IC)){
            endTrans(TcodeSucces.CONSULTAS, context.getString(R.string.autorizada));
            return;
        }


        if(!reqListProducts()){
            endTrans(TcodeSucces.CONSULTAS, context.getString(R.string.autorizada));
            return;
        }

        setSessionId(rspListProducts.getSesionId());

        if(drawMenuApiRest(context.getResources().getString(R.string.msgSeleccionCuentaConsul), getRspListProducts().getAccounts())!=0) {
            endTrans(TcodeSucces.CONSULTAS, context.getString(R.string.autorizada));
            return;
        }

        prepareOnline();
        endTrans(TcodeSucces.CONSULTAS, context.getString(R.string.autorizada));

        Logger.debug(context.getResources().getString(R.string.finTrans)+ transEName);

    }

    @Override
    public ISO8583 getISO8583() {
        return null;
    }

    private void prepareOnline() {

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

            retVal = saveTransFinance();

            //printer voucher
            retVal = printerDataClient();

            reqConfirmVoucher();

            retVal = printerDataCommerce();

        } else {
            clearPan();
        }
    }

}
