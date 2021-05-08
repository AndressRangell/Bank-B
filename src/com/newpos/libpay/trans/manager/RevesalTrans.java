package com.newpos.libpay.trans.manager;

import android.content.Context;

import com.newpos.libpay.Logger;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.Trans;
import com.newpos.libpay.trans.TransInputPara;
import com.newpos.libpay.trans.translog.TransLog;
import com.newpos.libpay.trans.translog.TransLogData;
import com.newpos.libpay.utils.ISOUtil;


/**
 * 冲正交易实体类
 *
 * @author zhouqiang
 */
public class RevesalTrans extends Trans {

    public RevesalTrans(Context ctx, String transEname, TransInputPara p) {
        super(ctx, transEname,true, p);
        isUseOrgVal = true; // 使用原交易的60.1 60.3
        iso8583.setHasMac(false);
        isTraceNoInc = false; // 冲正不需要自增流水号
    }

    private void setFields(TransLogData data) {

        if (msgID != null) {
            iso8583.setField(0, msgID);
        }
        if (data.getPan() != null) {
            iso8583.setField(2, data.getPan());
        }
        if (data.getProcCode() != null) {
            iso8583.setField(3, data.getProcCode());
        }

        String amoutData;
        amoutData = ISOUtil.padleft(data.getAmount() + "", 12, '0');
        iso8583.setField(4, amoutData);

        if (data.getTraceNo() != null) {
            iso8583.setField(11, data.getTraceNo());
        }
        if (data.getLocalTime() != null) {
            iso8583.setField(12, data.getLocalTime());
        }
        if (data.getLocalDate() != null) {
            iso8583.setField(13, data.getLocalDate());
        }
        if (data.getExpDate() != null) {
            iso8583.setField(14, data.getExpDate());
        }
        if (data.getEntryMode() != null) {
            iso8583.setField(22, data.getEntryMode());
        }
        if (data.getPanSeqNo() != null) {
            iso8583.setField(23, data.getPanSeqNo());
        }
        if (data.getNii() != null) {
            iso8583.setField(24, data.getNii());
        }
        if (data.getSvrCode() != null) {
            iso8583.setField(25, data.getSvrCode());
        }
        if (data.getTrack2() != null) {
            iso8583.setField(35, data.getTrack2());
        }
        if (data.getTermID() != null) {
            iso8583.setField(41, data.getTermID());
        }
        if (data.getMerchID() != null) {
            iso8583.setField(42, data.getMerchID());
        }
        if (data.getField54() != null) {
            iso8583.setField(54, data.getField54());
        }
        if (data.getField55() != null){
            iso8583.setField(55, data.getField55());
        }
        if (data.getField57() != null) {
            iso8583.setField(57, data.getField57());
        }
        if (data.getField58() != null) {
            iso8583.setField(58, data.getField58());
        }
        if (data.getField59() != null) {
            iso8583.setField(59, data.getField59());
        }
        if (data.getField60() != null) {
            iso8583.setField(60, data.getField60());
        }
        if (data.getField61() != null) {
            iso8583.setField(61, data.getField61());

        }
    }

    public int sendRevesal() {
        TransLogData data = TransLog.getReversal();
        setFields(data);
        retVal = onLineTrans();
        if (retVal == 0) {
            rspCode = iso8583.getfield(39);
            if (rspCode.equals("00") || rspCode.equals("12") || rspCode.equals("25")) {
                return retVal;
            } else {
                data.setRspCode("06");
                TransLog.saveReversal(data);
                return TcodeError.T_RECEIVE_REFUSE;
            }
        } else if (retVal == TcodeError.T_PACKAGE_MAC_ERR) {
            data.setRspCode("A0");
            TransLog.saveReversal(data);
        } else if ((retVal == TcodeError.T_RECEIVE_ERR) && (retVal == TcodeError.T_PACKAGE_ILLEGAL)) {
            data.setRspCode("08");
            TransLog.saveReversal(data);
        } else {
            Logger.debug("Revesal result :" + retVal);
        }
        return retVal;
    }
}
