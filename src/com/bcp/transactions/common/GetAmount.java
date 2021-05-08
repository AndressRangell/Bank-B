package com.bcp.transactions.common;

import android.content.Context;
import com.android.newpos.pay.R;
import com.bcp.document.InputInfo;
import com.bcp.menus.seleccion_cuenta.SelectedAccountItem;
import com.bcp.settings.BCPConfig;
import com.newpos.libpay.Logger;
import com.newpos.libpay.presenter.TransUI;
import com.newpos.libpay.trans.TcodeError;

import cn.desert.newpos.payui.UIUtils;

import static com.android.newpos.pay.ProcessingCertificate.polarisUtil;
import static com.android.newpos.pay.StartAppBCP.agente;

public class GetAmount {

    private TransUI mtransUI;
    private int mtimeOut;
    private long mAmnt;
    private int mRetVal;
    private String titleTrans;
    private Context ctx;
    private InputInfo inputInfo = null;

    private String msgCancelar = "Cancelar";
    private String msgReintentar = "Reintentar";

    public GetAmount(TransUI transUI, int timeOut, String transEname, Context context) {
        this.mtransUI = transUI;
        this.mtimeOut = timeOut;
        this.titleTrans = transEname;
        this.ctx = context;
    }

    public int getmRetVal() {
        return mRetVal;
    }

    public long getmAmnt() {
        return mAmnt;
    }

    /**
     *
     *
     * @return
     */
    public boolean setAmount(SelectedAccountItem selectedAccountItem) {
        String account = selectedAccountItem !=null ? selectedAccountItem.getProductDescription() : "";
        String symbol = selectedAccountItem != null ? selectedAccountItem.getCurrencyDescription() : "S/";
        String msg = "";
        try {
            long amountLow = Long.parseLong(polarisUtil.getTransCurrent().getAmountMinimun());
            long amountHig = Long.parseLong(polarisUtil.getTransCurrent().getAmountMaximun());

            while (true) {

                if (!inputAmount(String.valueOf(amountHig),account, symbol,String.valueOf(amountLow))) {
                    return false;
                }

                if (!UIUtils.validateMaxAmount(mAmnt, titleTrans,true, ctx)){
                    long amountRest = Integer.parseInt(agente.getMaximumTransactionAmount()) - (BCPConfig.getInstance(ctx).getAmntMaxAgent(BCPConfig.AMOUNTMAXAGENTKEY) < 0 ? BCPConfig.getInstance(ctx).getAmntMaxAgent(BCPConfig.AMOUNTMAXAGENTKEY) * -1 : BCPConfig.getInstance(ctx).getAmntMaxAgent(BCPConfig.AMOUNTMAXAGENTKEY));
                    msg = ctx.getResources().getString(R.string.msgAmountMax) + UIUtils.formatMiles(String.valueOf(amountRest));
                }else {
                    break;
                }
                inputInfo = mtransUI.showMessageInfo(titleTrans, msg, msgCancelar, msgReintentar, mtimeOut,false);
                if (!inputInfo.isResultFlag()) {
                    mRetVal = inputInfo.getErrno();
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            Logger.logLine(Logger.LOG_EXECPTION, "setAmount " +   e.getMessage());
            mRetVal = TcodeError.T_ERR_AMNT;
            return false;
        }
        return true;
    }

    private boolean inputAmount(String maxAmount, String account, String symbol, String minAmount) {

        inputInfo = mtransUI.getOutsideInput(UIUtils.fillScreenAmount(titleTrans, maxAmount, account, mtimeOut, symbol, "Ingresa el monto a retirar", minAmount));
        if (inputInfo.isResultFlag()) {
            if (inputInfo.getResult()!=null)
                mAmnt = Long.parseLong(inputInfo.getResult().replaceAll("[,.]",""));
            else
                return false;
        } else {
            mRetVal = inputInfo.getErrno();
            return false;
        }

        return true;
    }
}
