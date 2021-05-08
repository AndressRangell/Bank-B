package com.bcp.transactions.settle;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.ToneGenerator;
import com.android.newpos.pay.R;
import com.bcp.definesbcp.Definesbcp;
import com.bcp.printer.PrintParameter;
import com.bcp.reportes.DetailReport;
import com.bcp.tools_bacth.ToolsBatch;
import com.bcp.transactions.common.CommonFunctionalities;
import com.newpos.libpay.Logger;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.helper.iso8583.ISO8583;
import com.newpos.libpay.presenter.TransPresenter;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.TransInputPara;
import com.newpos.libpay.trans.translog.TransLogWs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import cn.desert.newpos.payui.UIUtils;

import static android.content.Context.MODE_PRIVATE;
import static com.android.newpos.pay.ProcessingCertificate.polarisUtil;

public class Settle extends JsonConsumeSettle implements TransPresenter {


    public Settle(Context ctx, String transEn, TransInputPara p) {
        super(ctx, transEn, p);
        transUI = para.getTransUI();
        isReversal = false;
        isSaveLog = false;
        isDebit = false;
        isProcPreTrans = false;
    }

    @Override
    public ISO8583 getISO8583() {
        return iso8583;
    }

    @Override
    public void start() {

        Logger.logLine(Logger.LOG_GENERAL, "==== startSettle");

        if (ToolsBatch.statusTrans()) {
            transUI.showError(transEName, TcodeError.T_ERR_NO_TRANS);
            return;
        }

        if (!reqObtainToken()) {
            endTrans(TcodeSucces.LOGONOUT_SUCC,context.getString(R.string.auth));
            return;
        }

        if(!prepareOnline()) {
            endTrans(TcodeSucces.LOGONOUT_SUCC,context.getString(R.string.auth));
            return;
        }

        polarisUtil.setCallBackSeatle(null);
        polarisUtil.setCallBackSeatle(status -> {
            if (status == 0) {
                saveSettle();
                polarisUtil.setIntentSettle(0);
                transUI.trannSuccess(TcodeSucces.OK, validateTrans(), context.getResources().getString(R.string.msg_RetiroImpr));
            }else {
                if (polarisUtil.getIntentSettle() >= 3){
                    polarisUtil.setIntentSettle(0);
                    saveSettle();
                    TransLogWs.getInstance().clearAll();
                }
                transUI.showError(transEName,status);
            }
        });

        if (retVal== 0) {
            if(para.isForcePrint()){
                UIUtils.intentTrans(PrintParameter.class,"typeReport", Definesbcp.ITEM_REPORTE_DETALLADO,false,context);
            }else {
                UIUtils.intentTrans(DetailReport.class, "REPORTE", "REPORTE DE CIERRE TOTAL", false, context);
            }
            UIUtils.beep(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD);
        } else {
            transUI.showError(transEName, retVal);
        }
    }

    private boolean prepareOnline() {

        transUI.handlingBCP(TcodeSucces.CONNECTING_CENTER,TcodeSucces.MSGEMPTY,false);

        if(!reqResumenTrans()){
            return false;
        }

        Logger.debug("Logout>>OnlineTrans=" + retVal);

        if (retVal == 0) {
            msgAprob(TcodeSucces.LOGONOUT_SUCC,TcodeSucces.MSGAUTORIZADO);
        } else {
            return false;
        }
        return true;
    }

    private void guardarFechaDeUltimoCierre() {
        DateFormat hourdateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date fechaActual = new Date();
        SharedPreferences.Editor editor = context.getSharedPreferences("fecha-cierre", MODE_PRIVATE).edit();
        editor.putString("fechaUltimoCierre", hourdateFormat.format(fechaActual));
        editor.apply();
    }

    private void saveSettle(){
        int val = Integer.parseInt(TMConfig.getInstance().getBatchNo());
        TMConfig.getInstance().setBatchNo(val).save();

        CommonFunctionalities.saveDateSettle(context);

        if (CommonFunctionalities.getFirtsTrans(context))
            CommonFunctionalities.saveFirtsTrans(context, false);

        guardarFechaDeUltimoCierre();
    }
}
