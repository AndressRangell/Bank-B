package com.bcp.transactions.settle;

import android.content.Context;
import com.bcp.rest.JSONInfo;
import com.bcp.rest.JsonUtil;
import com.bcp.rest.cierre.request.ReqResumeTrans;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.trans.TransInputPara;
import com.newpos.libpay.trans.finace.FinanceTransWS;
import com.newpos.libpay.trans.translog.TransLogDataWs;
import com.newpos.libpay.trans.translog.TransLogWs;
import org.json.JSONObject;
import java.util.List;
import static com.bcp.rest.JsonUtil.ROOT;
import static com.bcp.rest.JsonUtil.TIMEOUT;

public class JsonConsumeSettle extends FinanceTransWS {

    /**
     * 金融交易类构造
     *
     * @param ctx
     * @param transEname
     */
    public JsonConsumeSettle(Context ctx, String transEname, TransInputPara p) {
        super(ctx, transEname, p);
    }

    /**
     * Metodo 1 consumo API BCP Settle
     * @return
     */
    public boolean reqResumenTrans(){
        transUI.handlingBCP(TcodeSucces.SEND_DATA_2_SERVER ,TcodeSucces.MSGINFORMATION,false);

        try {
            JSONInfo jsonInfo = transUI.processApiRest(TIMEOUT, setResumeTrans(), setHeader(false), ROOT + listUrl.get(0).getMethod());

            opnNumberPlus();

            if ((retVal = jsonInfo.getErr()) != 0) {
                validarError(jsonInfo);
                return false;
            }

            if(jsonInfo.getHeader() != null && !jsonInfo.getHeader().isEmpty())
                setSessionId(jsonInfo.getHeader().get(JsonUtil.getSESSION()));

            transUI.handlingBCP(TcodeSucces.SEND_OVER_2_RECV, TcodeSucces.MSGINFORMATION, false);
            return true;
        }catch (Exception e){
            retVal = TcodeError.T_ERR_REUME_TRANS;
            controllerCatch(e);
            return false;
        }
    }

    private JSONObject setResumeTrans(){
        final ReqResumeTrans reqResumeTrans = new ReqResumeTrans();

        List<TransLogDataWs> list = TransLogWs.getInstance().getData();

        reqResumeTrans.setFirstTransDate(!list.isEmpty() ? list.get(0).getTransactionDate() : "");
        reqResumeTrans.setFirstTransTime(!list.isEmpty() ? list.get(0).getTransactionTime() : "");
        reqResumeTrans.setLastTransDate(!list.isEmpty() ? list.get(list.size()-1).getTransactionDate() : "");
        reqResumeTrans.setLastTransTime(!list.isEmpty() ? list.get(list.size()-1).getTransactionTime() : "");

        return reqResumeTrans.buildsObjectJSON();
    }
}
