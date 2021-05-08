package com.bcp.transactions.giros;

import android.content.Context;
import android.content.Intent;
import com.android.newpos.pay.R;
import com.bcp.definesbcp.Definesbcp;
import com.bcp.menus.MenuBCP;
import com.bcp.settings.BCPConfig;
import com.bcp.tools.Estadisticas;
import com.bcp.transactions.common.CommonFunctionalities;
import com.newpos.libpay.Logger;
import com.newpos.libpay.helper.iso8583.ISO8583;
import com.newpos.libpay.presenter.TransPresenter;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.trans.Trans;
import com.newpos.libpay.trans.TransInputPara;

import static com.android.newpos.pay.StartAppBCP.estadisticas;
import static com.android.newpos.pay.StartAppBCP.variables;

public class Giros extends JsonConsumeGiros  implements TransPresenter {

    public Giros(Context ctx, String transEname, TransInputPara p) {
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
        Logger.logLine(Logger.LOG_GENERAL, "==== startGiros ====");

        Logger.logLine(Logger.LOG_GENERAL, context.getResources().getString(R.string.inicioTrans) + transEName);


        if (!checkBatchAndSettle(true))
            return;

        if (!reqObtainToken()) {
            endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.tipoefect));
            return;
        }

        if ((retVal = CommonFunctionalities.typeTrans(transUI, fillScreenDocument(true, 8, 8, context.getResources().getString(R.string.titleTypeTrnas), context.getResources().getString(R.string.numDocu), false))) != 0){
            if (retVal == -2)
                validarError(CommonFunctionalities.getJsonInfo());
            else if (retVal != -1)
                endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.giro1));
            else{
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(context, MenuBCP.class);
                intent.putExtra(Definesbcp.DATO_MENU, R.array.main1);
                context.startActivity(intent);
            }
            return;
        }

        arguments = CommonFunctionalities.getArguments();

        if (arguments.getTypepayment().equals("2")){
            depWithCard = true;
            Logger.debug(context.getResources().getString(R.string.tipoefect));

            if (!cardProcess(INMODE_IC)) {
                endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.tipoefect));
                return;
            }

            if (arguments.getTypetransaction() == 2 && ((retVal = CommonFunctionalities.setBeneficiaryCobros(transUI, fillScreenDocument(true, 8, 8, context.getResources().getString(R.string.titleTypeTrnas), context.getResources().getString(R.string.numDocu), false))) != 0)){
                if (retVal == -2)
                    validarError(CommonFunctionalities.getJsonInfo());
                else
                    endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.tipoefect));
                return;
            }

            if(!reqListProductsGiros()){
                endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.tipoefect));
                return;
            }

            if (arguments.getTypetransaction() == 1){
                if (!selectAccountEmision()){
                    if (retVal == -2)
                        validarError(CommonFunctionalities.getJsonInfo());
                    else
                        endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.tipoefect));
                    return;
                }
            }else {
                if (!selectAccount()){
                    endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.tipoefect));
                    return;
                }
            }
        }

        prepareOnline();
        transEName = arguments.getTypetransaction() == 1 ? Trans.GIROS_EMISION : Trans.GIROS_COBROS;
        endTrans(TcodeSucces.ENDOPERATION, context.getString(arguments.getTypetransaction() == 1 ? R.string.titleEmisGiroEnd :  R.string.msgCobroGiro));
        Logger.logLine(Logger.LOG_EXECPTION, context.getResources().getString(R.string.finTrans) + transEName);
    }

    @Override
    public ISO8583 getISO8583() {
        return null;
    }

    private boolean selectAccount(){
        if(drawMenuApiRest(context.getResources().getString(R.string.accountPrimary),  getRspListProductsGiros().getListProducts())==-1) {
            return false;
        }

        if ((retVal = CommonFunctionalities.setClaveSecreta(transUI, fillScreenDocument(false, 4, 8, context.getResources().getString(R.string.claveGiroSecreta), context.getResources().getString(R.string.clave_secreta), false))) != 0){
            if (retVal == -1) {
                if (!selectAccount()) {
                    return false;
                }
            }else
                return false;
        }

        arguments.setArgument5(CommonFunctionalities.getNumDocument());

        return true;
    }

    private boolean selectAccountEmision(){

        if ((retVal = CommonFunctionalities.datosoBeneficiary(transUI, fillScreenDocument(true, 4, 8, context.getResources().getString(R.string.datosClaveGiro), context.getResources().getString(R.string.clave_secreta), false))) != 0){
            return false;
        }

        arguments = CommonFunctionalities.getArguments();

        return true;
    }

   private void prepareOnline() {

        if (arguments.getTypetransaction() == 1){
            //Para generar el pinblock del metodo verific deposit debe ser con el pan del establecimiento
            if (pan == null)
                pan = BCPConfig.getInstance(context).getPanAgente(BCPConfig.PANAGENTEKEY);

            if (!requestPin(context.getResources().getString(arguments.getTypepayment().equals("1") ? R.string.ingClaveAgente : R.string.ingClaveCliente))) {
                return;
            }

            if(!reqExecuteTransactionEmision()) {
                if (retVal == TcodeError.T_ERR_NOT_INTERNET) {
                    estadisticas.writeEstadisticas(Estadisticas.TRANSTIMEOUT, para.getTransType());
                } else {
                    estadisticas.writeEstadisticas(Estadisticas.TRANSFAIL, para.getTransType());
                }
                return;
            }
        }else {
            if(!reqExecuteTransactionGiros()) {
                if(retVal == TcodeError.T_ERR_NOT_INTERNET){
                    estadisticas.writeEstadisticas(Estadisticas.TRANSTIMEOUT,para.getTransType());
                }else {
                    estadisticas.writeEstadisticas(Estadisticas.TRANSFAIL,para.getTransType());
                }
                return;
            }
        }

       //save batch
       retVal = saveTransFinance();

       //printer voucher
       retVal = printerDataClient();

       reqConfirmVoucher();

       retVal = printerDataCommerce();

   }
}
