package com.bcp.transactions.pagoservicios;

import android.content.Context;
import android.content.Intent;

import com.android.newpos.pay.R;
import com.bcp.definesbcp.Definesbcp;
import com.bcp.menus.MenuBCP;
import com.bcp.settings.BCPConfig;
import com.bcp.transactions.common.CommonFunctionalities;
import com.newpos.libpay.Logger;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.helper.iso8583.ISO8583;
import com.newpos.libpay.presenter.TransPresenter;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.trans.Trans;
import com.newpos.libpay.trans.TransInputPara;

import static com.android.newpos.pay.StartAppBCP.variables;

public class PagoServicios extends JsonConsumePagoServicios implements TransPresenter {

    /**
     * 金融交易类构造
     *
     * @param ctx
     * @param transEname
     * @param p
     */
    public PagoServicios(Context ctx, String transEname, TransInputPara p) {
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

        Logger.debug(context.getResources().getString(R.string.inicioTrans) + transEName);


        if (!reqObtainToken()) {
            endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.tipoefectPS));
            return;
        }

            if (( retVal = CommonFunctionalities.seleccionServicio(transUI, fillScreenDocument(true, 8, 8, context.getResources().getString(R.string.titleTypeService), context.getResources().getString(R.string.numDocu), false))) != 0){
                if (retVal != -1)
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
            if (!cardProcess(INMODE_IC)) {
                endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.tipoefectPS));
                return;
            }
            if(arguments.getTipoPagoServicio().equals(Trans.PAGO_PASEMOVISTAR)){
                if(!reqObtainDocument()){
                    endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.tipoefectPS));
                    return;
                }

                if(!setDocument(context.getResources().getString(R.string.numDNI), 8, 8, false)){
                    endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.tipoefectPS));
                    return;
                }
            }

            if(!reqListProducts()){
                endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.tipoefectPS));
                return;
            }

            if (arguments.getTipoPagoServicio().equals(Trans.PAGO_IMPORTE) || arguments.getTipoPagoServicio().equals(Trans.PAGO_CONRANGO) || arguments.getTipoPagoServicio().equals(Trans.PAGO_PARCIAL)||arguments.getTipoPagoServicio().equals(Trans.PAGO_SINRANGO) || arguments.getTipoPagoServicio().equals(Trans.PAGO_SINVALIDACION)){
                if (!selectAccountTarjeta()) {
                    if (retVal == -2)
                        validarError(CommonFunctionalities.getJsonInfo());
                    else
                        endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.tipoefectPS));
                    return;
                }
            }else {
                if (!selectAccount()){
                    endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.tipoefectPS));
                    return;
                }
            }
        }

        prepareOnline();

        endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.deposito1));

    }

    @Override
    public ISO8583 getISO8583() {
        return null;
    }

    private void prepareOnline() {
        //Para generar el pinblock del metodo verific deposit debe ser con el pan del establecimiento
        if (pan == null)
            pan = BCPConfig.getInstance(context).getPanAgente(BCPConfig.PANAGENTEKEY);

        if (!requestPin(context.getResources().getString(arguments.getTypepayment().equals("1") ? R.string.ingClaveAgente : R.string.ingClaveCliente))) {
            return;
        }

        if(arguments.getTipoPagoServicio().equals(Trans.PAGO_PASEMOVISTAR)){

            if(!reqListClientDebts()){
                retVal = CommonFunctionalities.alertView(transUI,Trans.PAGOSERVICIOS,"",  "Por ahora,no tienes deudas","pendientes de pago");
                return;
            }

            arguments.setListDebts(rspListClientDebts.getListsDebts());
            if(arguments.getTipoPagoServicio().equals(Trans.PAGO_PASEMOVISTAR)){
                retVal = CommonFunctionalities.detalleServicio(transUI, fillScreenDocument(true, 4, 8, context.getResources().getString(R.string.datosClaveGiro), context.getResources().getString(R.string.clave_secreta), false));
                if(retVal != 0){
                    return;
                }
            }

            //doble procesamiento de tarjeta para obtener el ARQC generado con monto diferente de 0
            if (arguments.getTypepayment().equals("2")){
                amount = Long.parseLong(arguments.getMonto());

                if (isICC(false))
                    return;
            }
        }

        if(!reqExecuteTrans()){
            return;
        }

         retVal = saveTransFinance();

        //printer voucher
        retVal = printerDataClient();

        if(!reqConfirmVoucher()){
            return;
        }

        retVal = printerDataCommerce();
    }

    private boolean setDocument(String typeDoc, int minLen, int maxLen,  boolean isAlfa){

        if(!arguments.getTipoPagoServicio().equals(Trans.PAGO_CONRANGO) || arguments.getTipoPagoServicio().equals(Trans.PAGO_SINVALIDACION)){
            retVal = CommonFunctionalities.setNumberDoc(transUI, fillScreenDocument(false, 4, 8, context.getResources().getString(R.string.titleDoc), context.getResources().getString(R.string.titleNumDoc), false));
        }

        setDocNumber(CommonFunctionalities.getNumDocument());
        return reqValidateDocument();
    }
    private boolean selectAccountTarjeta(){

        if ((retVal = CommonFunctionalities.detailPayment(transUI, fillScreenDocument(true, 4, 8, context.getResources().getString(R.string.titleDetPagoServ), context.getResources().getString(R.string.titleNumDoc), false))) != 0){
            return false;
        }

        arguments = CommonFunctionalities.getArguments();

        return true;
    }
    private boolean selectAccount(){
        if(drawMenuApiRest(context.getResources().getString(R.string.selecCuenta), getRspListProducts().getAccounts())==-1) {
            endTrans(TcodeSucces.RETIRO_SUCC, context.getString(R.string.auth));
            return false;
        }

        arguments.setArgument5(CommonFunctionalities.getNumDocument());

        return true;
    }

}
