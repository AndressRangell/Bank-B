package com.bcp.login;

import android.content.Context;
import android.content.Intent;
import com.android.newpos.pay.R;
import com.bcp.definesbcp.Definesbcp;
import com.bcp.document.InputInfo;
import com.bcp.settings.BCPConfig;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.helper.iso8583.ISO8583;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.trans.TransInputPara;
import com.newpos.libpay.trans.finace.FinanceTrans;
import com.newpos.libpay.presenter.TransPresenter;

public class LoginSupervisor extends FinanceTrans implements TransPresenter {

    public LoginSupervisor(Context ctx, String transEname, TransInputPara p) {
        super(ctx, transEname, p);
        init(transEname);
    }

    private void init(String transEname) {
        transUI = para.getTransUI();
        isReversal = false;
        isProcPreTrans = false;
        isSaveLog = false;
        isDebit = false;
        transEName = transEname;

    }

    @Override
    public void start() {
        InputInfo inputInfo = transUI.showMessageInfo(context.getString(R.string.saludosupervisor), context.getString(R.string.initSesion), "No", "Sí", timeout,true);
        if (!inputInfo.isResultFlag()) {
            transUI.showError(transEName, TcodeError.T_USER_CANCEL);
            return;
        }
        setFixedDatas();
        retVal =  onlineTransFinance(null);

        if (retVal == 0){
            if( iso8583.getfield(60) != null && iso8583.getfield(39).equals("00") ){

                transUI.handlingBCP(TcodeSucces.HANDLING ,TcodeSucces.RESP_CODIGO,false);
                TMConfig.getInstance().setLoginSupervisor(iso8583.getfield(60)).save();
                intentLogin();
            } else {
                transUI.showError(transCName, TcodeError.T_USER_CANCEL);
            }
        }else{
            transUI.showError(transCName,TcodeError.T_USER_CANCEL);
        }
    }

    @Override
    public ISO8583 getISO8583() {
        return null;
    }

    private void intentLogin(){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, Login.class);
        intent.putExtra(Definesbcp.LOGIN_SUPERVISOR, Definesbcp.LOGIN_SUPERVISOR);
        context.startActivity(intent);
    }
}
