package com.bcp.transactions.deposito;

import android.content.Context;

import com.android.newpos.pay.R;
import com.bcp.menus.seleccion_cuenta.SelectedAccountItem;
import com.bcp.settings.BCPConfig;
import com.bcp.tools.Estadisticas;
import com.bcp.transactions.common.CommonFunctionalities;
import com.newpos.libpay.Logger;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.helper.iso8583.ISO8583;
import com.newpos.libpay.presenter.TransPresenter;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.trans.TransInputPara;

import static com.android.newpos.pay.StartAppBCP.estadisticas;
import static com.android.newpos.pay.StartAppBCP.variables;

public class Deposito extends JsonConsumeDep implements TransPresenter {

    /**
     * 金融交易类构造
     *
     * @param ctx        Context
     * @param transEname Nombre Transaccion
     * @param p          Parametros
     */
    public Deposito(Context ctx, String transEname, TransInputPara p) {
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

        Logger.logLine(Logger.LOG_GENERAL, "==== startDeposito");

        Logger.debug(context.getResources().getString(R.string.inicioTrans)+ transEName);
        int selectItem;

        if (!checkBatchAndSettle(true))
            return;

        if (!reqObtainToken()) {
            endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.deposito1));
            return;
        }

        if ((selectItem=drawMenuHardcode(context.getResources().getString(R.string.tipoDepos), new String[]{"Con tarjeta", "Con número\nde cuenta"})) == -1) {
            endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.deposito1));
            return;
        }

        depWithCard = selectItem == 0;

        if (selectItem == 1){
            Logger.debug(context.getResources().getString(R.string.deposNumCuenta));
            if (!noCard()) {
                endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.deposito1));
                return;
            }
        } else if(selectItem == 0){
            Logger.debug(context.getResources().getString(R.string.deposTrajeta));
            if (!withCard()) {
                endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.deposito1));
                return;
            }
        } else {
            return;
        }

        if (!setAmount()) {
            endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.deposito1));
            return;
        }

        prepareOnline();
        endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.deposito1));

        Logger.debug(context.getResources().getString(R.string.finTrans)+ transEName);
    }

    private boolean withCard(){
        if (!cardProcess(INMODE_IC)) {
            return false;
        }

        if(!reqListProducts()){
            return false;
        }

        setSessionId(getRspListProducts().getSesionId());

        return drawMenuApiRest(context.getResources().getString(R.string.selectCuentaDepo), getRspListProducts().getAccounts()) == 0;
    }

    private boolean noCard(){
        if ((retVal=CommonFunctionalities.setNumberDoc(transUI, fillScreenDocument(false, 13, 14, context.getResources().getString(R.string.cuentaDest), context.getResources().getString(R.string.numCuenta), false))) != 0)
            return false;

        setAccountId(CommonFunctionalities.getNumDocument());
        pan=CommonFunctionalities.getNumDocument();

        if(!reqValidateAccount()){
            return false;
        }

        setSessionId(getRspValidateAccount().getSesionId());

        //se llena el tipo de cuenta
        selectedAccountItem = new SelectedAccountItem();
        selectedAccountItem.setCurrencyDescription(getRspValidateAccount().getCurrencyDescription());
        selectedAccountItem.setProductDescription(getRspValidateAccount().getProductDescription());


        if ((retVal=CommonFunctionalities.setNumberDoc(transUI, fillScreenDocument(true, 8, 8, context.getResources().getString(R.string.datosDepositante), context.getResources().getString(R.string.numDocu), false))) != 0)
            return false;

        setDocumentType("1");
        setDocument(CommonFunctionalities.getNumDocument());

        return true;
    }

    @Override
    public ISO8583 getISO8583() {
        return null;
    }

    private void prepareOnline() {

        //Para generar el pinblock del metodo verific deposit debe ser con el pan del establecimiento
        pan = BCPConfig.getInstance(context).getPanAgente(BCPConfig.PANAGENTEKEY);

        if (!requestPin(context.getResources().getString(R.string.ingClaveAgente))) {
            return;
        }

        if(!reqVerifyDeposit()){
            return;
        }

        if ((retVal = CommonFunctionalities.confirmTrans(timeout, transUI, amountGet, transEName, getLabelVerifDeposit())) != 0) {
            clearPan();
            return;
        }

        if(!reqExecuteTransaction()) {
            if (retVal == TcodeError.T_ERR_NOT_INTERNET) {
                estadisticas.writeEstadisticas(Estadisticas.TRANSTIMEOUT, para.getTransType());
            } else {
                estadisticas.writeEstadisticas(Estadisticas.TRANSFAIL, para.getTransType());
            }
            return;
        }
        //save batch
        retVal = saveTransFinance();

        //printer voucher
        retVal = printerDataClient();


        reqConfirmVoucher();

        retVal = printerDataCommerce();

    }
}
