package com.newpos.libpay;

/**
 * Created by zhouqiang on 2017/4/25.
 * @author zhouqiang
 * 异常处理
 */

public class PaySdkException extends Exception {

    public static final String NOT_INIT = "presione X para continuar!" ;
    public static final String PARA_NULL = "init pay sdk paras is null!" ;

    public PaySdkException(String msg){
        Logger.logLine(Logger.LOG_GENERAL, msg);
    }
}
