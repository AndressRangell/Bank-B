package com.newpos.libpay.global;

/**
 * Created by zhouqiang on 2017/4/29.
 * @author zhouqiang
 * 全局常量
 */

public class TMConstants {
    public static final String BANKNAME = "bankname/bankname.properties" ;
    public static final String DEFAULTCONFIG = "default/default_config.properties" ;
    public static final String ERRNO = "errno/errno.properties" ;
    public static final String ERRNO_EN = "errno/errno_en.properties" ;
    public static final String ISO8583 = "iso8583/iso8583attr.properties" ;
    public static final String STATUS = "status/success.properties" ;
    public static final String STATUS_EN = "status/success_en.properties" ;
    public static final String TRANS = "transcfg/transcfg.properties" ;

    protected TMConstants(){

    }

    static final String[] ASSETS = {
            "logo/huazirong.png",
            "logo/zhongguoyinlian.png",
            "logo/zhongguogongshang.png",
            "logo/zhongguojianshe.png",
            "logo/zhongguoyinhang.png",
            "logo/zhongguonongye.png",
            "logo/zhongguominsheng.png",
            "logo/zhongguoguangda.png",
            "logo/zhongguoyouzheng.png",
            "logo/zhongxinyinhang.png",
            "logo/zhaoshangyinhang.png",
            "logo/xingyeyinhang.png",
            "logo/pufayinhang.png",
            "logo/pipinganyinhang.png",
            "logo/jiaotongyinhang.png",
            "logo/huaxiayinhang.png",
            "logo/beijinyinhang.png"
    };

    public static String getBankName(int pos){
        return ASSETS[pos];
    }
}
