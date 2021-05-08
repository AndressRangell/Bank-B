package com.newpos.libpay.trans.manager;

import android.content.Context;

import com.newpos.libpay.trans.Trans;
import com.newpos.libpay.trans.TransInputPara;
import com.newpos.libpay.trans.translog.TransLogData;
import com.newpos.libpay.utils.ISOUtil;

/**
 * 脚本处理实体类
 * @author zhouqiang
 */
public class ScriptTrans extends Trans {

	public ScriptTrans(Context ctx, String transEname, TransInputPara p) {
		super(ctx, transEname,true, p);
		iso8583.setHasMac(true);
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
		if (data.getAmount() >= 0) {
			String amoutData = "";
			amoutData = ISOUtil.padleft(data.getAmount() + "", 12, '0');
			iso8583.setField(4, amoutData);
		}
		if (traceNo != null){
			iso8583.setField(11, traceNo);
		}
		if (data.getEntryMode() != null) {
			iso8583.setField(22, data.getEntryMode());
		}
		if (data.getAcquirerID() != null) {
			iso8583.setField(32, data.getAcquirerID());
		}
		if (data.getRrn() != null){
			iso8583.setField(37, data.getRrn());
		}
		if (data.getAuthCode() != null) {
			iso8583.setField(38, data.getAuthCode());
		}
		if (termID != null) {
			iso8583.setField(41, termID);
		}
		if (merchID != null) {
			iso8583.setField(42, merchID);
		}
		if (data.getCurrencyCode() != null) {
			iso8583.setField(49, data.getCurrencyCode());
		}
		if (data.getIccdata() != null) {
			iso8583.setField(55, ISOUtil.byte2hex(data.getIccdata()));
		}
		if (field60 != null) {
			iso8583.setField(60, field60);
		}
		// 61.1 原交易批次号 61.2 原pos流水号 61.3 原交易日期
		field61 = data.getTraceNo() + data.getBatchNo() + data.getLocalDate();
		iso8583.setField(61, field61);
	}

	public int sendScriptResult(TransLogData data) {
		setFields(data);
		return onLineTrans();
	}
}
