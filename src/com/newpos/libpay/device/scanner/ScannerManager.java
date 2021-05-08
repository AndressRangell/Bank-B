package com.newpos.libpay.device.scanner;

import android.app.Activity;
import com.android.desert.keyboard.InputManager;
import com.newpos.libpay.Logger;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.TcodeSucces;

/**
 * Created by zhouqiang on 2017/7/7.
 * @author zhouqiang
 * 扫码管理
 */

public class ScannerManager {

    private static ScannerManager instance ;
    private static InnerScanner scanner ;

    private ScannerManager(){}

    public static ScannerManager getInstance(Activity activity , InputManager.Style m){
        Logger.logLine(Logger.LOG_GENERAL, "stilo " + m);
        scanner = new InnerScanner(activity);
        if(null == instance){
            instance = new ScannerManager();
        }
        return instance ;
    }

    private QRCListener listener ;

    public void getQRCode(int timeout , QRCListener l){
        Logger.debug("ScannerManager>>getQRCode>>timeout="+timeout);
        scanner.initScanner();
        final QRCInfo info = new QRCInfo();
        if(null == l){
            info.setResultFalg(false);
            info.setErrno(TcodeError.T_INVOKE_PARA_ERR);
            listener.callback(info);
        }else {
            this.listener = l ;
            scanner.startScan(timeout, (retCode, data) -> {
                if(TcodeSucces.SCANSUCCES == retCode){
                    info.setResultFalg(true);
                    info.setQrc(new String(data));
                }else {
                    info.setResultFalg(false);
                    info.setErrno(retCode);
                }
                listener.callback(info);
            });
        }
    }
}
