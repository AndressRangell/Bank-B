package com.newpos.libpay.presenter;

import android.os.CountDownTimer;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.desert.keyboard.InputManager;
import com.bcp.amount.MsgScreenAmount;
import com.bcp.document.ClassArguments;
import com.bcp.document.MsgScreenDocument;
import com.bcp.menus.seleccion_cuenta.SelectedAccountItem;
import com.bcp.printer.MsgScreenPrinter;
import com.bcp.rest.JSONInfo;
import com.bcp.teclado_alfanumerico.ClickKeyboard;
import com.newpos.libpay.device.user.OnUserResultListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouqiang on 2017/4/25.
 * 交易用户显示接口
 *
 * @author zhouqiang
 * 面向用户的接口
 */

public interface TransView {
    /**
     * 通知上层UI显示刷卡样式
     *
     * @param timeout 超时时间 单位 秒
     * @param mode    输入模式，详见@{@link com.newpos.libpay.device.card.CardManager}
     */
    void showCardView(String title, String msg, int timeout, int mode, OnUserResultListener l);



    /**
     * 通知UI显示当前交易读取的卡号供用户确认
     *
     * @param timeout 超时时间
     * @param pan     当前卡号
     * @param l       需要上层通过此接口给底层回调通知 详见@{@link OnUserResultListener}
     */
    void showCardNo(int timeout, String pan, OnUserResultListener l);

    /**
     * @param title      Titulo
     * @param msg        Mensaje
     * @param btnCancel  Boton cancelar
     * @param btnConfirm Boton confirmar
     * @param l          Listener
     */
    void showMessageInfo(String title, String msg, String btnCancel, String btnConfirm, int timeout, boolean isQR, OnUserResultListener l);

    /**
     * @param l          Listener
     */
    void showMessageImpresion(MsgScreenPrinter msgScreenPrinter, OnUserResultListener l);

    /**
     * 通知UI显示输入样式
     *
     * @param l       需要上层通过此接口给底层回调用户行为 详见@{@link OnUserResultListener}
     */
    void showInputView(MsgScreenAmount msgScreenAmount, OnUserResultListener l);

    /**
     * 获取输入信息
     *
     * @param type 输入类型 @{@link InputManager.Mode}
     * @return 输入结果
     */
    String getInput(InputManager.Mode type);

    /**
     *
     * @return
     */
    SelectedAccountItem getSelectedAccountItem();

    /**
     *
     * @return
     */
    ClassArguments getArguments();

    void setArgumentsClass(ClassArguments classArguments);

    /**
     *
     * @param etTxt
     * @param lenMax
     * @param activityAmount
     * @param activityPwd
     * @param isAlfa
     */
    void setKeyboard(EditText etTxt, int lenMax, boolean activityAmount, boolean activityPwd, boolean isAlfa, int minLen, RelativeLayout keyboardNumeric ,RelativeLayout keyboardAlfa);

    ClickKeyboard getKeyboards();
    /**
     * 通知UI显示卡片多应用
     *
     * @param timeout 超时时间
     * @param apps    应用列表
     * @param l       需要上层通过此接口给底层回调用户行为 详见@{@link OnUserResultListener}
     */
    void showCardAppListView(int timeout,String nameTrans, String[] apps, OnUserResultListener l);

    /**
     * 通知UI显示卡片多语言选择
     *
     * @param timeout 超时时间
     * @param langs   语言列表
     * @param l       需要上层通过此接口给底层回调用户行为 详见@{@link OnUserResultListener}
     */
    void showMultiLangView(int timeout, String[] langs, OnUserResultListener l);

    /**
     * 通知UI交易结束成功后续处理
     *
     * @param info    交易结果详情
     */
    void showSuccess(String info, String... msg);

    /**
     * 通过UI交易结束失败后续处理
     *
     * @param err     错误详情信息
     */
    void showError(String tittle, String cod, String err);

    void showMsgInfoBCP(String tittle, String status, boolean transaccion);

    void showAlertBCP(String transEname, String msg1, String msg2,String msg3, OnUserResultListener l);

    void showVerifyCard(String tittle, boolean transaccion);

    /**
     * @param timeout Timeout
     * @param title   Titulo
     * @param label   Mensaje
     * @param l       Listener
     */
    void showInputUser(int timeout, final String title, final String label, int min, int max, OnUserResultListener l);


    /**
     * @param timeout Timeout
     * @param title   Titulo
     * @param label   Mensaje
     * @param l       Listener
     */
    void showInputDoc(MsgScreenDocument msgScreenDocument, OnUserResultListener l);

    void showInputTypeTrans(MsgScreenDocument msgScreenDocument, OnUserResultListener l);

    void showSeleccionServicio(MsgScreenDocument msgScreenDocument, OnUserResultListener l);

    void showInputclavSecre(MsgScreenDocument msgScreenDocument, OnUserResultListener l);

    void showInputbeneficiarycobros(MsgScreenDocument msgScreenDocument, OnUserResultListener l);

    void showInputdatosoBeneficiary(MsgScreenDocument msgScreenDocument, OnUserResultListener l);

    void showInputImpresionUltimas(String title, String msg, int timeout,OnUserResultListener l);

    void showInputdetalleServicio(MsgScreenDocument msgScreenDocument, OnUserResultListener l);

    void showInputdetailPayment(MsgScreenDocument msgScreenDocument, OnUserResultListener l);

    /**
     * @param errcode Codigo de error
     */
    void toasTransView(String errcode, boolean sound);

    /**
     * @param timeout Timeout
     * @param title   Titulo
     * @param label   Mensaje
     */
    void showConfirmAmountView(int timeout, final String title, final String[] label, String amnt, boolean isHTML, float textSize, OnUserResultListener l);

    /**
     * @param timeout   Timeout
     * @param l         Listener
     * @param title     Titulo
     * @param transType Tipo Transaccion
     */
    void showSignatureView(int timeout, OnUserResultListener l, String title, String transType);

    /**
     * @param timeout   Timeout
     * @param l         Listener
     * @param title     Titulo
     * @param transType Tipo Transaccion
     */
    void showListView(int timeout, OnUserResultListener l, String title, String transType, final ArrayList<String> listMenu, int id);

    /**
     *
     * @param l
     */
    void getResultListener(OnUserResultListener l);

    /**
     *
     * @return OnUserResultListener
     */
    OnUserResultListener getListener();

    /**
     *
     * @return MsgScreenDocument
     */
    MsgScreenDocument getMsgScreendocument();

    /**
     *
     * @param timeout
     * @param mensaje
     */
    void counterDownTimer(int timeout, final String mensaje);

    CountDownTimer getTimer();

    /**
     *
     */
    void deleteTimer();

    /**
     * funcion para calcular la longitud maxima y minima de los editText
     * @return
     */
    boolean longMin(EditText etInputuser);

    /**
     *
     * @param timeout
     * @param mode
     */
    void showQRCView(int timeout, InputManager.Style mode);

    void showSelectAccountView(int timeout, String[] data, List<SelectedAccountItem> items, List<SelectedAccountItem> items2, OnUserResultListener l);

    /**
     * @param timeout Timeout
     * @param title   Titulo
     * @param l       Listener
     */
    void showChangePwdView(int timeout, final String title, int min, int max, OnUserResultListener l);

    void showLastOperations(String contentLast, String contentThree, OnUserResultListener l);
}
