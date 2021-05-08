package com.newpos.libpay.device.user;

import com.android.desert.keyboard.InputManager;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by zhouqiang on 2017/4/25.
 * 用户自定义view的统一回调接口
 * @author zhouqiang
 */

public interface OnUserResultListener {
    /**
     * 用户确认前一步骤的动作
     * @param type 暂时仅供付款方式使用,其它请先回调-1
     * @link @{@link InputManager.Style}
     * 统一入口
     */
    public void confirm(InputManager.Style type);

    /**
     * 用户取消前一步骤动作
     * 统一入口
     */
    public void cancel();

    public void cancel(int codeError);

    public void cancel(JSONObject jsonObject, Map<String,String> header, int statusCode);

    public void confirm(int applistselect);

    public void back();
}
