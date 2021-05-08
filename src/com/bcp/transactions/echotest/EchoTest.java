package com.bcp.transactions.echotest;

import android.content.Context;
import android.media.ToneGenerator;
import com.newpos.libpay.Logger;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.helper.iso8583.ISO8583;
import com.newpos.libpay.presenter.TransPresenter;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.TransInputPara;
import com.newpos.libpay.trans.finace.FinanceTrans;
import com.newpos.libpay.trans.translog.TransLog;
import cn.desert.newpos.payui.UIUtils;
import static com.android.newpos.pay.StartAppBCP.variables;

public class EchoTest extends FinanceTrans implements TransPresenter {

    /**
     * 金融交易类构造
     *
     * @param ctx
     * @param transEname
     */
    public EchoTest(Context ctx, String transEname, TransInputPara p) {
        super(ctx, transEname, p);
        transEName = transEname;
        if(para != null) {
            transUI = para.getTransUI();
        }
        isReversal = false;
        isSaveLog = false;
        isDebit = false;
        hostId = variables.getIdAcquirer();
    }

    @Override
    public void start() {

        transUI.handlingBCP(TcodeSucces.ECHO_TEST,TcodeSucces.MSGEMPTY,false);
        retVal = echoTest();
        if(retVal!=0){
            transUI.showError(transEName, retVal);
        }else{
            transUI.trannSuccess(TcodeSucces.ECHO_TEST_SUCCESS);
            UIUtils.beep(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD);
        }
        Logger.debug("InitTrans>>finish");
    }

    @Override
    public ISO8583 getISO8583() {
        return null;
    }

    private int echoTest(){
        setFixedDatas();
        setField();

        String rspCode;

        retVal = sendRcvdInit();

        if (retVal != 0) {
            return retVal ;
        }

        rspCode = iso8583.getfield(39);

        if (rspCode != null && (rspCode.equals(ISO8583.RSP_00) || rspCode.equals(ISO8583.RSP_89))) {
            Logger.debug("LogoutTrans>>Logout>>Init Exitosa!");
            return 0 ;

        } else {
            if (rspCode == null) {
                return TcodeError.T_RECEIVE_ERR;
            } else {
                return Integer.valueOf(rspCode);
            }
        }
    }

    private int sendRcvdInit() {

        byte[] respData;


        if(tryConnect()!=0)
            return retVal;
        
        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER,TcodeSucces.MSGINFORMATION,false);
        if (send() == -1) {
            return TcodeError.T_SEND_ERR;
        }
        transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV,TcodeSucces.MSGINFORMATION,false);
        respData = recive();

        netWork.close();

        if (respData == null || respData.length <= 0) {
            return TcodeError.T_RECEIVE_ERR;
        }

        int ret = iso8583.unPacketISO8583(respData);

        rspCode = iso8583.getfield(39);
        if (!"00".equals(rspCode)&& !"89".equals(rspCode)) {
            TransLog.clearReveral();
            //Trans reject
            ret = formatRsp(rspCode);
            return ret;
        }

        if (ret == 0 && isTraceNoInc) {
                cfg.incTraceNo().save();
                traceNo = cfg.getTraceNo();
        }
        return ret;
    }
}
