package com.bcp.tools_bacth;

import com.newpos.libpay.trans.translog.TransLog;
import com.newpos.libpay.trans.translog.TransLogData;
import com.newpos.libpay.trans.translog.TransLogDataWs;
import com.newpos.libpay.trans.translog.TransLogWs;
import com.newpos.libpay.utils.ISOUtil;
import java.util.List;
import static com.newpos.libpay.trans.Trans.ID_LOTE;

public class ToolsBatch {

    protected ToolsBatch() {
    }

    /**
     * Recorre todos los acquirers y valida si alguno tiene trans.
     * @param
     * @return
     */
    public static boolean statusTrans() {
        boolean ret;
        List<TransLogDataWs> list = TransLogWs.getInstance().getData();
        ret = list.isEmpty();
        return ret;
    }

    /**
     *
     * @param value
     * @return
     */
    public static String incBatchNo(String value) {
        int val = Integer.parseInt(value);
        if (val == 999999) {
            val = 0;
        }
        val += 1;
        return ISOUtil.padleft(String.valueOf(val) + "", 6, '0');
    }

    public static boolean transActive(String idAcq) {
        boolean ret = false;

        List<TransLogData> list = TransLog.getInstance(idAcq).getData();
        if (list.size() == 1) {
            return true;
        } else {
            ret =  false;
        }

        return ret;
    }

    public static boolean isReverse(){
        boolean ret = false;
        TransLogData revesalData1 = TransLog.getReversal();
        if (revesalData1 != null) {
            ret = true;
        }
            return ret;
    }
}
