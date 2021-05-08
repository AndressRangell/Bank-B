package com.bcp.transactions.ultimasoperaciones;

import android.content.Context;
import android.content.Intent;
import com.android.newpos.pay.R;
import com.bcp.definesbcp.Definesbcp;
import com.bcp.login.Login;
import com.bcp.menus.MenuBCP;
import com.bcp.transactions.common.CommonFunctionalities;
import com.newpos.libpay.Logger;
import com.newpos.libpay.device.printer.PrintManager;
import com.newpos.libpay.helper.iso8583.ISO8583;
import com.newpos.libpay.presenter.TransPresenter;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.trans.Trans;
import com.newpos.libpay.trans.TransInputPara;
import static com.android.newpos.pay.StartAppBCP.variables;

public class LastOperations extends JsonConsumeLastOperations implements TransPresenter {

    public LastOperations(Context ctx, String transEname, TransInputPara p){
       super(ctx, transEname, p);
       init(transEname);
    }

    private void init(String transEname) {
        transUI = para.getTransUI();
        isReversal = false;
        isSaveLog = false;
        isDebit = false;
        isProcPreTrans = false;
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

        Logger.debug(context.getResources().getString(R.string.inicioTrans) + transEName);
        if (!checkBatchAndSettle(true))
            return;

        if (!reqObtainToken()) {
            endTrans(TcodeSucces.ULT_OPERACIONES, context.getString(R.string.auth));
            return;
        }

        //Realizar consumo tres ultimas operaciones.
        if (rspLastOperations()) {
            if ((retVal = CommonFunctionalities.alertView(transUI, Trans.ULT_OPERACIONES, "Consultarás las 3 últimas", "\noperaciones monetarias", "procesadas")) != 0) {
                endTrans(TcodeSucces.ULT_OPERACIONES, context.getString(R.string.auth));
                return;
            }
        }else if (!errOP.equals("OP0003")) {
            endTrans(TcodeSucces.ULT_OPERACIONES, context.getString(R.string.auth));
            return;
        }

        if (rspViewLastOperations == null){
            if ((retVal = CommonFunctionalities.alertView(transUI, Trans.ULT_OPERACIONES,"No realizaste operaciones","monetarias procesadas", "en el día")) != 0) {
                intentLogin(false, Login.class);
                return;
            }
        }

        setSessionId(rspViewLastOperations.getSesionId());
        retValPrinter =  CommonFunctionalities.lastOperations(transUI,rspViewLastOperations.getContent(),rspViewLastOperations.getContentTheeLast());

        if (retValPrinter == -1){
            intentLogin(true,MenuBCP.class);
            return;
        }else if (retValPrinter == TcodeError.T_ERR_TIMEOUT_IN_DATA){
            retVal = retValPrinter;
            endTrans(TcodeSucces.ULT_OPERACIONES, context.getString(R.string.auth));
            return;
        }

        if (( retVal = CommonFunctionalities.impresionUltimas(transUI,  retValPrinter == 0 ? " 3 últimas operaciones" : "última operación","Impresa" , 4000)) != 0){
            return;
        }

        printLast(retValPrinter == 0);

        endTrans(TcodeSucces.ENDOPERATION, context.getString(R.string.titUltiOperaciones));

    }

    protected void intentLogin(boolean main,Class<?> cls){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, cls);
        if (main)
            intent.putExtra(Definesbcp.DATO_MENU, R.array.main1);
        context.startActivity(intent);
    }

    private void printLast(boolean tipOperations) {
        PrintManager printManager = PrintManager.getmInstance(context, transUI);
        retVal = printManager.printLastOpe(rspViewLastOperations,tipOperations);
        reqConfirVoucher();
    }
}
