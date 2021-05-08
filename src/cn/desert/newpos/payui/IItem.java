package cn.desert.newpos.payui;

import com.android.newpos.pay.R;

/**
 * @author zhouqiang
 * @email wy1376359644@163.com
 */

public class IItem {

    private IItem() {
    }

    protected static final int[] IMGS = {
            R.drawable.icon_setting_communication,
            R.drawable.icon_setting_transpara,
            R.drawable.icon_setting_keyspara,
            R.drawable.icon_setting_maintainpdw,
            R.drawable.icon_setting_errlogs,
            R.drawable.icon_setting_privacy,
            R.drawable.icon_setting_deviceinfo
    };

    protected static final int[] IMGS2 = {
            R.drawable.home2_setting_commun,
            R.drawable.home2_setting_trans,
            R.drawable.home2_setting_android
    } ;

    public static int runnerIMGS(int i){
        return IMGS[i];
    }

    public static int runnerIMGS2(int i){
        return IMGS2[i];
    }
}
