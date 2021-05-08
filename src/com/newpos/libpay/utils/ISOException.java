package com.newpos.libpay.utils;

import com.newpos.libpay.Logger;

public class ISOException extends Exception {

	public ISOException(String msg){
		Logger.logLine(Logger.LOG_GENERAL, msg);
	}
}
