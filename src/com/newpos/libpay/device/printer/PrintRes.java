package com.newpos.libpay.device.printer;

import java.util.Locale;

/**
 * Created by zhouqiang on 2017/4/4.
 * 打印常量类
 *
 * @author zhouqiang
 */

public class PrintRes {
    public interface CH {
        boolean ZH = Locale.getDefault().getLanguage().equals("ZH");
        String MERCHANT_ID = ZH ? "商户编号(MERCHANT NO):" : "MERCHANT NO:";
        String TERNIMAL_ID = ZH ? "终端编号(TERMINAL NO):" : "TERMINAL NO:";
        String CARD_NO = ZH ? "卡号(CARD NO):" : "CARD NO:";
        String SCANCODE = ZH ? "付款码(PayCode):" : "PayCode:";
        String TRANS_TYPE = ZH ? "交易类型(TXN. TYPE):" : "TXN. TYPE :";
        String VOUCHER_NO = ZH ? "凭证号(VOUCHER NO):" : "VOUCHER NO:";
        String AUTH_NO = ZH ? "授权码(AUTH NO):" : "AUTH NO:";
        String DATE_TIME = ZH ? "日期/时间(DATE/TIME):" : "DATE/TIME:";
        String REF_NO = ZH ? "交易参考号(REF. NO):" : "REF. NO:";
        String AMOUNT = ZH ? "金额(AMOUNT):" : "AMOUNT:";
        String RMB = ZH ? "RMB:" : "$:";
    }

    protected static final String[] TRANSEN = {
            "LOGIN",//0
            "CIERRE TOTAL",//1
            "ECHO_TEST",//2
            "RETIRO",//3
            "DEPÓSITO",//4
            "INIT_TOTAL",//5
            "INIT_PARCIAL",//6
            "CONSULTAS",//7
            "CERTIFICADO",//8
            "GIROS",//9
            "CIERRE AUTO",//10
            "PAGO_SERVICIOS",//11
            "ULT_OPERACIONES"//12

    };

    public static int lenRunnerTransEn(){
        return TRANSEN.length;
    }

    public static String runnerTransEn(int pos){
        return TRANSEN[pos];
    }
}
