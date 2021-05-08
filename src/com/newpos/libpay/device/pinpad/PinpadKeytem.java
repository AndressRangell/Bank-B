package com.newpos.libpay.device.pinpad;

import com.pos.device.ped.KeySystem;

/**
 * Created by zhouqiang on 2017/7/19.
 * @author zhouqiang
 * 密码键盘密钥体系
 */

public class PinpadKeytem {
    public static final int MS_DES = 0 ;
    public static final int MS_SM4 = 1 ;
    public static final int FIXED_DES = 2 ;
    public static final int FIXED_SM4 = 3 ;
    public static final int DUKPT_DES = 4 ;
    public static final int ICC_PLAIN = 5 ;
    public static final int ICC_CIPHER = 6 ;
    public static final int TMS_DES = 7 ;
    public static final int KS_SM2 = 8 ;
    public static final int TMS_RSA = 9 ;
    public static final int TMS_ANS = 10 ;
    public static final int MS_AES = 11 ;
    public static final int FIXED_AES = 12 ;
    
    private PinpadKeytem() {
    }

    public static KeySystem getKS(int pk){
        KeySystem ks = KeySystem.MS_DES ;
        KeySystem[] kss = {KeySystem.MS_DES, KeySystem.MS_SM4, KeySystem.FIXED_DES, KeySystem.FIXED_SM4,
                KeySystem.DUKPT_DES, KeySystem.ICC_PLAIN, KeySystem.ICC_CIPHER, KeySystem.TMS_DES, KeySystem.KS_SM2,
                KeySystem.TMS_RSA, KeySystem.TMS_ANS, KeySystem.MS_AES, KeySystem.FIXED_AES} ;
        int[] pks  = {PinpadKeytem.MS_DES, PinpadKeytem.MS_SM4, PinpadKeytem.FIXED_DES, PinpadKeytem.FIXED_SM4,
                PinpadKeytem.DUKPT_DES, PinpadKeytem.ICC_PLAIN, PinpadKeytem.ICC_CIPHER, PinpadKeytem.TMS_DES, PinpadKeytem.KS_SM2,
                PinpadKeytem.TMS_RSA, PinpadKeytem.TMS_ANS, PinpadKeytem.MS_AES, PinpadKeytem.FIXED_AES} ;
        for (int i = 0 ; i < pks.length ; i++){
            if(pk == pks[i]){
                ks = kss[i] ;
                break;
            }
        }
        return ks ;
    }
}
