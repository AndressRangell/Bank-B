package com.newpos.libpay.presenter;

import com.android.desert.keyboard.InputManager;
import com.bcp.amount.MsgScreenAmount;
import com.bcp.document.InputData;
import com.bcp.document.InputInfo;
import com.bcp.document.MsgScreenDocument;
import com.bcp.menus.seleccion_cuenta.InputSelectAccount;
import com.bcp.menus.seleccion_cuenta.SelectedAccountItem;
import com.bcp.printer.MsgScreenPrinter;
import com.bcp.rest.JSONInfo;
import com.newpos.libpay.device.card.CardInfo;
import com.newpos.libpay.device.pinpad.OfflineRSA;
import com.newpos.libpay.device.pinpad.PinInfo;
import com.newpos.libpay.device.scanner.QRCInfo;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouqiang on 2017/3/15.
 *
 * @author zhouqiang
 * 交易UI接口类
 */

public interface TransUI {
    /**
     * 获取外界输入UI接口(提示用户输入信息)
     *
     * @return return
     */
    InputInfo getOutsideInput(MsgScreenAmount msgScreenAmount);

    /**
     * 获取外界卡片UI接口(提示用户用卡)
     *
     * @return return
     */
    CardInfo getCardUse(String title, String msg, int timeout, int mode);

    /**
     *
     * @param msg
     * @param timeout
     * @param mode
     * @param title
     * @return
     */
    CardInfo getCardFallback(String msg, int timeout, int mode, String title);

    /**
     * 获取密码键盘输入联机PIN
     *
     * @param timeout Timeout
     * @param amount  Monto
     * @param cardNo  Numero Tarjeta
     */
    PinInfo getPinpadOnlinePin(int timeout, String amount, String cardNo, String tittle);

    /**
     * @param timeout       Timeout
     * @param i             Listener
     * @param key           Key
     * @param offlinecounts Contador
     * @return return
     */
    PinInfo getPinpadOfflinePin(int timeout, int i, OfflineRSA key, int offlinecounts);

    /**
     * 人机交互显示UI接口(卡号确认)
     *
     * @param cn 卡号
     */
    int showCardConfirm(int timeout, String cn);

    /**
     * @param msg        Mensaje
     * @param btnCancel  Boton cancel
     * @param btnConfirm boton confirmar
     * @return return
     */
    InputInfo showMessageInfo(String title, String msg, String btnCancel, String btnConfirm, int timeout, boolean isQR);

    /**
     * @return return
     */
    InputInfo showMessageImpresion(MsgScreenPrinter msgScreenPrinter);

    /**
     * 人机交互显示UI接口(多应用卡片选择)
     *
     * @param timeout Timeout
     * @param list    Lista
     * @return return
     */
    int showCardApplist(int timeout,String nameTrans, String[] list);

    /**
     * 人机交互显示UI接口（多语言选择接口）
     *
     * @param timeout Timeout
     * @param langs   Lenguajes
     * @return return
     */
    int showMultiLangs(int timeout, String[] langs);

    void handlingBCP(int tittle, int status, boolean imgProgress);

    void setHeader(Map<String, String> header, String url);

    InputInfo alertView(String transEname, String msg1, String msg2,String msg3);

    void verifyCard(String tittle, boolean imgProgress);

    /**
     * 人机交互显示UI接口(耗时处理操作)
     *
     * @param status  TransStatus 状态标志以获取详细错误信息
     */
    void handlingBCP(String title, String status, boolean imgProgress);

    /**
     * 交易成功处理结果
     *
     * @param code Codigo
     */
    void trannSuccess(int code, String... args);

    /**
     * 人机交互显示UI接口(显示交易出错错误信息)
     *
     * @param errcode 实际代码错误返回码
     */
    void showError(String tittle, int errcode);

    void showError(String tittle, String cod,  String errcode);

    /**
     * @param timeout Timeout
     * @param title   Titulo
     * @return return
     */
    InputInfo showInputUser(int timeout, final String title, final String label2, int min, int max);

    /**
     * @return return
     */
    InputInfo showInputDoc(MsgScreenDocument msgScreenDocument);

    InputData showInputTypeTrans(MsgScreenDocument msgScreenDocument);

    InputData showseleccionServicio(MsgScreenDocument msgScreenDocument);

    InputInfo showInputclavSecre(MsgScreenDocument msgScreenDocument);

    InputData showInputbeneficiarycobros(MsgScreenDocument msgScreenDocument);

    InputData showInputdatosoBeneficiary(MsgScreenDocument msgScreenDocument);

    InputData showInputImpresionUltimas(String title, String msg, int timeout);

    InputData showInputdetalleServicio(MsgScreenDocument msgScreenDocument);

    InputData showInputdetailPayment(MsgScreenDocument msgScreenDocument);


    /**
     * @param errcode Error code
     */
    void toasTrans(int errcode, boolean sound, boolean isErr);

    void toasTrans(String errcode, boolean sound, boolean isErr);

    /**
     *
     * @param timeout
     * @param title
     * @param label
     * @param amnt
     * @param isHTML
     * @param textSize
     * @return
     */
    InputInfo showConfirmAmount(int timeout, final String title, final String[] label, String amnt, boolean isHTML, float textSize);

    /**
     * @param timeout   Timeout
     * @param title     Titulo
     * @param transType Tipo Transaccion
     * @return return
     */
    InputInfo showSignature(int timeout, String title, String transType);

    /**
     * @param timeout   Timeout
     * @param title     Titulo
     * @param transType Tipo Transaccion
     * @return return
     */
    InputInfo showList(int timeout, String title, String transType, final ArrayList<String> listMenu, int id);

    /**
     * @param timeout Timeout
     * @param data Data transaccion
     * @param items Lista items
     * @param items2 Lista items 2
     * @return return
     */
    InputSelectAccount showSelectAccount(int timeout, final String[] data, final List<SelectedAccountItem> items, final List<SelectedAccountItem> items2);

    /**
     * 获取外界扫码支付方式接口(提示用户扫码操作)
     *
     * @param timeout Timeout
     * @param mode    @{@link InputManager.Style}
     * @return return
     */
    QRCInfo getQRCInfo(int timeout, InputManager.Style mode);

    /**
     * @param timeout Timeout
     * @param title   Titulo
     * @return return
     */
    InputInfo showChangePwd(int timeout, final String title, int min, int max);

    /**
     *
     * @param timeout
     * @param jsonObject
     * @param headers
     * @param url
     * @return
     */
    JSONInfo processApiRest(int timeout, JSONObject jsonObject, Map<String, String> headers, String url);

    /**
     *
     * @param timeout
     * @param params
     * @param url
     * @return
     */
    JSONInfo processApiRestStringPost(int timeout, Map<String, String> params, String url);

    /**
     *
     * @param timeout
     * @param url
     * @return
     */
    JSONInfo processApiRestStringGet(int timeout, Map<String, String> headers, String data, String url);

    /**
     *
     * @param timeout
     * @param url
     * @return
     */
    JSONInfo httpRequetsStringPostWithOutBody(int timeout, Map<String, String> headers, String url);

    /**
     *
     * @param category
     * @param msg
     */
    void senEndSession(String category,String msg);

    InputInfo showLastOperations(String contentLast, String contentThree);

}
