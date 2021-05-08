package com.newpos.libpay.presenter;
import android.app.Activity;
import android.media.ToneGenerator;
import com.android.desert.keyboard.InputManager;
import com.bcp.amount.MsgScreenAmount;
import com.bcp.document.InputData;
import com.bcp.document.InputInfo;
import com.bcp.document.MsgScreenDocument;
import com.bcp.menus.seleccion_cuenta.InputSelectAccount;
import com.bcp.menus.seleccion_cuenta.SelectedAccountItem;
import com.bcp.printer.MsgScreenPrinter;
import com.bcp.rest.JSONInfo;
import com.bcp.rest.JsonUtil;
import com.bcp.rest.giros.cobro_nacional.request.ReqEndSession;
import com.bcp.rest.httpclient.RequestWs;
import com.newpos.libpay.Logger;
import com.newpos.libpay.PaySdk;
import com.newpos.libpay.PaySdkException;
import com.newpos.libpay.device.scanner.QRCInfo;
import com.newpos.libpay.device.scanner.ScannerManager;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.device.card.CardInfo;
import com.newpos.libpay.device.card.CardManager;
import com.newpos.libpay.device.pinpad.OfflineRSA;
import com.newpos.libpay.device.pinpad.PinInfo;
import com.newpos.libpay.device.pinpad.PinpadManager;
import com.newpos.libpay.device.user.OnUserResultListener;
import com.newpos.libpay.global.TMConstants;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.utils.PAYUtils;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import cn.desert.newpos.payui.UIUtils;
import static com.bcp.rest.JsonUtil.TIMEOUT;

/**
 * Created by zhouqiang on 2017/4/25.
 *
 * @author zhouqiang
 * 交易UI接口实现类
 * MVP架构中的P层 ，处理复杂的逻辑及数据
 */

public class TransUIImpl implements TransUI {

    private TransView transView;
    private Activity mActivity;
    private static final String RESPONSE = "Response: ";
    private static final int CANCELTRANS = 1;
    private static final int BACKTRANS = 2;
    private static final int ERRORAPI = 3;
    private Map<String,String> header;
    private String url;
    JSONInfo jsonInfoError = new JSONInfo();

    public TransUIImpl(Activity activity, TransView tv) {
        this.transView = tv;
        this.mActivity = activity;
    }

    /**
     * object lock
     */
    private Object o = new byte[0] ;

    /**
     * Notify
     */
    private void listenNotify(){
        synchronized (o){
            o.notifyAll();
        }
    }

    /**
     * block
     */
    private void funWait(){
        synchronized (o){
            try {
                o.wait();
            } catch (InterruptedException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    private int mRet = 0;
    private int errorCode = -1;
    private InputManager.Style payStyle;

    private final OnUserResultListener listener = new OnUserResultListener() {
        @Override
        public void confirm(InputManager.Style style) {
            mRet = 0;
            payStyle = style;
            listenNotify();
        }

        @Override
        public void cancel() {
            mRet = 1;
            listenNotify();
        }

        @Override
        public void cancel(int codeError) {
            mRet = 1;
            errorCode = codeError;
            listenNotify();
        }

        @Override
        public void cancel(JSONObject jsonObject, Map<String, String> header, int statusCode) {
            mRet = 3;
            jsonInfoError.setJsonObject(jsonObject);
            jsonInfoError.setHeader(header);
            jsonInfoError.setStatusCode(statusCode);
            listenNotify();
        }

        @Override
        public void confirm(int applistselect) {
            mRet = applistselect;
            listenNotify();
        }

        @Override
        public void back() {
            mRet = 2;
            listenNotify();
        }
    };

    @Override
    public PinInfo getPinpadOfflinePin(int timeout, int i, OfflineRSA key, int counts) {
        final PinInfo pinInfo = new PinInfo();
        PinpadManager pinpadManager = PinpadManager.getInstance();
        pinpadManager.getOfflinePin(i, key, counts, info -> {
            pinInfo.setResultFlag(info.isResultFlag());
            pinInfo.setErrno(info.getErrno());
            pinInfo.setNoPin(info.isNoPin());
            pinInfo.setPinblock(info.getPinblock());
            listenNotify();
        });
        funWait();
        transView.showMsgInfoBCP(getStatusInfo(String.valueOf(TcodeSucces.HANDLING)),"PIN",false);
        return pinInfo;
    }

    @Override
    public InputInfo getOutsideInput(MsgScreenAmount msgScreenAmount) {
        transView.showInputView(msgScreenAmount, listener);
        funWait();
        InputInfo info = new InputInfo();
        if (mRet == 1) {
            info.setResultFlag(false);
            info.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL : errorCode);
        } else {
            info.setResultFlag(true);
            info.setResult(transView.getInput(InputManager.Mode.REFERENCE));
            info.setNextStyle(payStyle);
        }
        return info;
    }

    @Override
    public CardInfo getCardUse(String title, String msg, int timeout, int mode) {
        transView.showCardView(title, msg, timeout, mode,listener);
        final CardInfo cInfo = new CardInfo() ;
        CardManager cardManager = CardManager.getInstance(mode);
        cardManager.getCard(timeout,transView, cardInfo -> {
            cInfo.setResultFalg(cardInfo.isResultFalg());
            cInfo.setNfcType(cardInfo.getNfcType());
            cInfo.setCardType(cardInfo.getCardType());
            cInfo.setTrackNo(cardInfo.getTrackNo());
            cInfo.setCardAtr(cardInfo.getCardAtr());
            cInfo.setErrno(cardInfo.getErrno());
            listenNotify();
        });
        funWait();

        if (mRet == 1) {
            cInfo.setResultFalg(false);
            cInfo.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL : errorCode);
        }

        return cInfo;
    }

    @Override
    public CardInfo getCardFallback(String msg, int timeout, int mode, String title) {
        transView.showCardView(title, msg, timeout, mode, listener);
        final CardInfo cInfo = new CardInfo() ;
        CardManager cardManager = CardManager.getInstance(mode);
        cardManager.getCard(timeout, cardInfo -> {
            cInfo.setResultFalg(cardInfo.isResultFalg());
            cInfo.setNfcType(cardInfo.getNfcType());
            cInfo.setCardType(cardInfo.getCardType());
            cInfo.setTrackNo(cardInfo.getTrackNo());
            cInfo.setCardAtr(cardInfo.getCardAtr());
            cInfo.setErrno(cardInfo.getErrno());
            listenNotify();
        });
        funWait();

        return cInfo;
    }

    @Override
    public PinInfo getPinpadOnlinePin(int timeout, String amount, String cardNo, String tittle) {
        final PinInfo pinInfo = new PinInfo();
        PinpadManager pinpadManager = PinpadManager.getInstance();
        pinpadManager.getPin(amount, cardNo,tittle, info -> {
            pinInfo.setResultFlag(info.isResultFlag());
            pinInfo.setErrno(info.getErrno());
            pinInfo.setNoPin(info.isNoPin());
            pinInfo.setPinblock(info.getPinblock());
            listenNotify();
        });
        funWait();
        transView.showMsgInfoBCP(getStatusInfo(String.valueOf(TcodeSucces.HANDLING)),"PIN",false);
        return pinInfo;
    }

    @Override
    public int showCardConfirm(int timeout, String cn) {
        transView.showCardNo(timeout, cn, listener);
        funWait();
        return mRet;
    }

    @Override
    public InputInfo showMessageInfo(String title, String msg, String btnCancel, String btnConfirm, int timeout, boolean isQR) {
        transView.showMessageInfo(title, msg, btnCancel, btnConfirm, timeout, isQR , listener);
        funWait();
        InputInfo info = new InputInfo();
        if (mRet == 1) {
            info.setResultFlag(false);
            info.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL : errorCode);
        } else {
            info.setResultFlag(true);
            info.setResult(transView.getInput(InputManager.Mode.REFERENCE));
        }
        return info;
    }

    @Override
    public InputInfo showMessageImpresion(MsgScreenPrinter msgScreenPrinter) {
        transView.showMessageImpresion(msgScreenPrinter, listener);
        funWait();
        InputInfo info = new InputInfo();
        if (mRet == 1) {
            info.setResultFlag(false);
            info.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL : errorCode);
        } else {
            info.setResultFlag(true);
            info.setResult(transView.getInput(InputManager.Mode.REFERENCE));
        }
        return info;
    }

    @Override
    public int showCardApplist(int timeout, String nametrans, String[] list) {
        transView.showCardAppListView(timeout,nametrans, list, listener);
        funWait();
        return mRet;
    }

    @Override
    public int showMultiLangs(int timeout, String[] langs) {
        transView.showMultiLangView(timeout, langs, listener);
        funWait();
        return mRet;
    }

    @Override
    public void handlingBCP(int tittle, int status, boolean imgProgress) {
        transView.showMsgInfoBCP(getStatusInfo(String.valueOf(tittle)), getStatusInfo(String.valueOf(status)).toLowerCase(),imgProgress);
    }

    @Override
    public void setHeader(Map<String, String> header , String url) {
        this.header = header;
        this.url = url;
    }

    @Override
    public InputInfo alertView(String transEname, String msg1, String msg2,String msg3) {
        transView.showAlertBCP(transEname, msg1, msg2,msg3,listener);
        funWait();
        InputInfo info = new InputInfo();
        if (mRet == 1) {
            info.setResultFlag(false);
            info.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL : errorCode);
        } else {
            info.setResultFlag(true);
            info.setResult(transView.getInput(InputManager.Mode.AMOUNT));
            info.setNextStyle(payStyle);
        }
        return info;
    }

    @Override
    public void verifyCard(String tittle, boolean imgProgress) {
        transView.showVerifyCard(tittle,imgProgress);
    }

    @Override
    public void handlingBCP(String tittle, String status, boolean imgProgress ) {
        transView.showMsgInfoBCP(tittle, status.toLowerCase(), imgProgress);
    }

    @Override
    public void trannSuccess(int code, String... args) {
        String info = getStatusInfo(String.valueOf(code));
        transView.showSuccess( info, args[0] + "",args[1] + "");
        senEndSession("","");
    }

    @Override
    public void showError(String tittle, int errcode) {
        String msg = getErrInfo(String.valueOf(errcode));
        transView.showError(tittle, String.valueOf(errcode), msg);
        UIUtils.beep(ToneGenerator.TONE_PROP_BEEP2);
        senEndSession("Technical",msg);
    }

    @Override
    public void showError(String tittle, String cod, String errcode) {
        transView.showError(tittle, cod, errcode);
        senEndSession("Technical",errcode);
    }

    @Override
    public InputInfo showInputUser(int timeout, String title, String label, int min, int max) {
        transView.showInputUser(timeout, title, label, min, max, listener);
        funWait();
        InputInfo info = new InputInfo();
        if (mRet == 1) {
            info.setResultFlag(false);
            info.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL : errorCode);
        } else {
            info.setResultFlag(true);
            info.setResult(transView.getInput(InputManager.Mode.REFERENCE));
            info.setNextStyle(payStyle);
        }
        return info;
    }

    @Override
    public InputInfo showInputDoc(MsgScreenDocument msgScreenDocument) {
        transView.showInputDoc(msgScreenDocument, listener);
        funWait();
        InputInfo info = new InputInfo();
        if (mRet == 1) {
            info.setResultFlag(false);
            info.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL : errorCode);
        } else {
            info.setResultFlag(true);
            info.setResult(transView.getInput(InputManager.Mode.REFERENCE));
            info.setNextStyle(payStyle);
        }
        return info;
    }
    @Override
    public InputData showInputTypeTrans(MsgScreenDocument msgScreenDocument) {
        transView.showInputTypeTrans(msgScreenDocument, listener);
        funWait();
        InputData info = new InputData();
        if (mRet == CANCELTRANS) {
            info.setResultFlag(false);
            info.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL : errorCode);
        } else if (mRet == BACKTRANS){
            info.setResultFlag(false);
            info.setBack(true);
        }else if (mRet == ERRORAPI){
            info.setResultFlag(false);
            info.setJsonInfo(jsonInfoError);
        }else {
            info.setResultFlag(true);
            info.setArguments(transView.getArguments());
        }
        return info;
    }

    @Override
    public InputData showseleccionServicio(MsgScreenDocument msgScreenDocument) {
        transView.showSeleccionServicio(msgScreenDocument, listener);
        funWait();
        InputData info = new InputData();
        if (mRet == 1) {
            info.setResultFlag(false);
            info.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL : errorCode);
        } else if (mRet == 2){
            info.setResultFlag(false);
            info.setBack(true);
        }else {
            info.setResultFlag(true);
            info.setArguments(transView.getArguments());
        }
        return info;
    }

    @Override
    public InputInfo showInputclavSecre(MsgScreenDocument msgScreenDocument) {
        transView.showInputclavSecre(msgScreenDocument, listener);
        funWait();
        InputInfo info = new InputInfo();
        if (mRet == 1) {
            info.setResultFlag(false);
            info.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL : errorCode);
        } else if (mRet == 2){
            info.setResultFlag(false);
            info.setBack(true);
        }else {
            info.setResultFlag(true);
            info.setResult(transView.getInput(InputManager.Mode.REFERENCE));
            info.setNextStyle(payStyle);
        }
        return info;
    }
    @Override
    public InputData showInputbeneficiarycobros(MsgScreenDocument msgScreenDocument) {
        transView.showInputbeneficiarycobros(msgScreenDocument, listener);
        funWait();
        InputData info = new InputData();
        if (mRet == CANCELTRANS) {
            info.setResultFlag(false);
            info.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL : errorCode);
        } else if (mRet == BACKTRANS){
            info.setResultFlag(false);
            info.setBack(true);
        }else if (mRet == ERRORAPI){
            info.setResultFlag(false);
            info.setJsonInfo(jsonInfoError);
        }else {
            info.setResultFlag(true);
            info.setArguments(transView.getArguments());
        }
        return info;
    }

    @Override
    public InputData showInputdatosoBeneficiary(MsgScreenDocument msgScreenDocument) {
        transView.showInputdatosoBeneficiary(msgScreenDocument, listener);
        funWait();
        InputData info = new InputData();
        if (mRet == CANCELTRANS) {
            info.setResultFlag(false);
            info.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL : errorCode);
        } else if (mRet == BACKTRANS){
            info.setResultFlag(false);
            info.setBack(true);
        }else if (mRet == ERRORAPI){
            info.setResultFlag(false);
            info.setJsonInfo(jsonInfoError);
        }else {
            info.setResultFlag(true);
            info.setArguments(transView.getArguments());
        }
        return info;
    }

    @Override
    public InputData showInputImpresionUltimas(String title, String msg, int timeout) {
        transView.showInputImpresionUltimas(title,msg,timeout,listener);
        funWait();
        InputData info = new InputData();
        if (mRet == CANCELTRANS) {
            info.setResultFlag(false);
            info.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL_INPUT : errorCode);
        } else if (mRet == BACKTRANS){
            info.setResultFlag(false);
            info.setBack(true);
        }else if (mRet == ERRORAPI){
            info.setResultFlag(false);
            info.setJsonInfo(jsonInfoError);
        }else {
            info.setResultFlag(true);
            info.setArguments(transView.getArguments());
        }
        return info;
    }

    @Override
    public InputData showInputdetailPayment(MsgScreenDocument msgScreenDocument) {
        transView.showInputdetailPayment(msgScreenDocument, listener);
        funWait();
        InputData info = new InputData();
        if (mRet == CANCELTRANS) {
            info.setResultFlag(false);
            info.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL : errorCode);
        } else if (mRet == BACKTRANS){
            info.setResultFlag(false);
            info.setBack(true);
        }else if (mRet == ERRORAPI){
            info.setResultFlag(false);
            info.setJsonInfo(jsonInfoError);
        }else {
            info.setResultFlag(true);
            info.setArguments(transView.getArguments());
        }
        return info;
    }

    @Override
    public InputData showInputdetalleServicio(MsgScreenDocument msgScreenDocument) {
        transView.showInputdetalleServicio(msgScreenDocument, listener);
        funWait();
        InputData info = new InputData();
        if (mRet == 1) {
            info.setResultFlag(false);
            info.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL : errorCode);
        } else if (mRet == 2){
            info.setResultFlag(false);
            info.setBack(true);
        }else {
            info.setResultFlag(true);
            info.setArguments(transView.getArguments());
        }
        return info;
    }

    @Override
    public void toasTrans(int errcode, boolean sound, boolean isErr) {
        if (isErr)
            transView.toasTransView(getErrInfo(String.valueOf(errcode)), sound);
        else
            transView.toasTransView(getStatusInfo(String.valueOf(errcode)), sound);
    }

    public void toasTrans(String errcode, boolean sound, boolean isErr) {

        transView.toasTransView(errcode, sound);

    }

    @Override
    public InputInfo showConfirmAmount(int timeout, String title, String[] label, String amnt, boolean isHTML, float textSize) {
        transView.showConfirmAmountView(timeout, title, label, amnt, isHTML, textSize, listener);
        funWait();
        InputInfo info = new InputInfo();
        if (mRet == 1) {
            info.setResultFlag(false);
            info.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL : errorCode);
        } else {
            info.setResultFlag(true);
            info.setResult(transView.getInput(InputManager.Mode.REFERENCE));
            info.setNextStyle(payStyle);
        }
        return info;
    }

    /**
     * =============================================
     */

    private String getStatusInfo(String status) {
        try {
            String[] infos = Locale.getDefault().getLanguage().equals("ZH") ?
                    PAYUtils.getProps(PaySdk.getInstance().getContext(), TMConstants.STATUS, status) :
                    PAYUtils.getProps(PaySdk.getInstance().getContext(), TMConstants.STATUS_EN, status);
            if (infos != null) {
                return infos[0];
            }
        } catch (PaySdkException pse) {
            Logger.logLine(Logger.LOG_EXECPTION, pse.getStackTrace(), pse.getMessage());
            Thread.currentThread().interrupt();
        }
        if (Locale.getDefault().getLanguage().equals("ZH")) {
            return "未知信息";
        } else {
            return "Error Desconocido";
        }
    }

    public static String getErrInfo(String status) {
        try {
            if (Integer.parseInt(status)<0)
                return "Codigo de Error Desconocido";

            String[] errs = Locale.getDefault().getLanguage().equals("ZH") ?
                    PAYUtils.getProps(PaySdk.getInstance().getContext(), TMConstants.ERRNO, status) :
                    PAYUtils.getProps(PaySdk.getInstance().getContext(), TMConstants.ERRNO_EN, status);
            if (errs != null) {
                return errs[0];
            }
        } catch (Exception pse) {
            Logger.logLine(Logger.LOG_EXECPTION, pse.getStackTrace(), pse.getMessage());
            Thread.currentThread().interrupt();
        }
        if (Locale.getDefault().getLanguage().equals("ZH")) {
            return "未知错误";
        } else {
            return "Codigo de Error Desconocido";
        }
    }

    @Override
    public InputInfo showSignature(int timeout, String title, String transType) {
        transView.showSignatureView(timeout, listener, title, transType);
        funWait();
        InputInfo info = new InputInfo();
        if (mRet == 1) {
            info.setResultFlag(false);
            info.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL : errorCode);
        } else {
            info.setResultFlag(true);
            info.setResult(transView.getInput(InputManager.Mode.AMOUNT));
            info.setNextStyle(payStyle);
        }
        return info;
    }

    @Override
    public InputInfo showList(int timeout, String title, String transType, final ArrayList<String> listMenu, int id) {
        transView.showListView(timeout, listener, title, transType, listMenu, id);
        funWait();
        InputInfo info = new InputInfo();
        if (mRet == 1) {
            info.setResultFlag(false);
            info.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL : errorCode);
        } else {
            info.setResultFlag(true);
            info.setResult(transView.getInput(InputManager.Mode.AMOUNT));
            info.setNextStyle(payStyle);
        }
        return info;
    }

    public InputSelectAccount showSelectAccount(int timeout, String[] data, List<SelectedAccountItem> items, List<SelectedAccountItem> items2) {
        transView.showSelectAccountView(timeout, data, items, items2, listener);
        funWait();
        InputSelectAccount info = new InputSelectAccount();
        if(mRet == 1){
            info.setResultFlag(false);
            info.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL : errorCode);
        }else {
            info.setResultFlag(true);
            info.setResult(transView.getSelectedAccountItem());
            info.setNextStyle(payStyle);
        }
        return info;
    }

    @Override
    public QRCInfo getQRCInfo(final int timeout, final InputManager.Style mode) {
        transView.showQRCView(timeout, mode);
        final QRCInfo qinfo = new QRCInfo();
        mActivity.runOnUiThread(() -> {
            ScannerManager manager = ScannerManager.getInstance(mActivity, mode);
            manager.getQRCode(timeout, info -> {
                qinfo.setResultFalg(info.isResultFalg());
                qinfo.setErrno(info.getErrno());
                qinfo.setQrc(info.getQrc());
                listenNotify();
            });
        });
        funWait();
        return qinfo;
    }

    @Override
    public InputInfo showChangePwd(int timeout, String title, int min, int max) {
        transView.showChangePwdView(timeout, title, min, max, listener);
        funWait();
        InputInfo info = new InputInfo();
        if (mRet == 1) {
            info.setResultFlag(false);
            info.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL : errorCode);
        } else {
            info.setResultFlag(true);
            info.setResult(transView.getInput(InputManager.Mode.REFERENCE));
            info.setNextStyle(payStyle);
        }
        return info;
    }

    @Override
    public JSONInfo processApiRest(final int timeout, final JSONObject jsonObject, final Map<String, String> headers, final String url) {
        final JSONInfo jsonInfo = new JSONInfo();

        mActivity.runOnUiThread(() -> {
            RequestWs requestWs = new RequestWs(mActivity, url, timeout, true);
            requestWs.httpRequets(headers, jsonObject, (result, statusCode,rspHeader) -> {
                if (result == null) {
                    jsonInfo.setResultFlag(false);
                    if (statusCode==200 || statusCode==204){
                        jsonInfo.setErr(0);
                        jsonInfo.setHeader(rspHeader);
                    }
                    else
                        jsonInfo.setErr(statusCode);

                    jsonInfo.setStatusCode(statusCode);
                    jsonInfo.setJsonObject(null);
                }else{
                    if (statusCode!=200){
                        jsonInfo.setResultFlag(false);
                        jsonInfo.setErr(statusCode);
                    }else {
                        jsonInfo.setResultFlag(true);
                        jsonInfo.setErr(0);
                    }
                    jsonInfo.setStatusCode(statusCode);
                    jsonInfo.setJsonObject(result);
                    jsonInfo.setHeader(rspHeader);
                    Logger.debug(RESPONSE + result);
                }
                listenNotify();
            });
        });

        funWait();

        return jsonInfo;
    }

    @Override
    public JSONInfo processApiRestStringPost(int timeout, Map<String, String> params, String url) {
        final JSONInfo jsonInfo = new JSONInfo();

        mActivity.runOnUiThread(() -> {
            RequestWs requestWs = new RequestWs(mActivity, url, timeout,true);
            requestWs.httpRequetsStringPost(params, (result, statusCode,rspHeader) -> {
                if (result == null) {
                    jsonInfo.setResultFlag(false);
                    if (statusCode==200 || statusCode==204)
                        jsonInfo.setErr(0);
                    else
                        jsonInfo.setErr(statusCode);

                    jsonInfo.setStatusCode(statusCode);
                    jsonInfo.setJsonObject(null);
                }else{
                    if (statusCode!=200){
                        jsonInfo.setResultFlag(false);
                        jsonInfo.setErr(statusCode);
                    }else {
                        jsonInfo.setResultFlag(true);
                        jsonInfo.setErr(0);
                    }
                    jsonInfo.setStatusCode(statusCode);
                    jsonInfo.setJsonObject(result);
                    jsonInfo.setHeader(rspHeader);
                    Logger.debug(RESPONSE + result);
                }
                listenNotify();
            });
        });

        funWait();

        return jsonInfo;
    }

    @Override
    public JSONInfo processApiRestStringGet(int timeout, Map<String, String> headers, String data, String url) {
        final JSONInfo jsonInfo = new JSONInfo();

        mActivity.runOnUiThread(() -> {
            RequestWs requestWs = new RequestWs(mActivity, url, timeout,true);
            requestWs.httpRequetsStringGet(headers, data, (result, statusCode,rspHeader) -> {
                if (result == null) {
                    jsonInfo.setResultFlag(false);
                    if (statusCode==200 || statusCode==204)
                        jsonInfo.setErr(0);
                    else
                        jsonInfo.setErr(statusCode);

                    jsonInfo.setStatusCode(statusCode);
                    jsonInfo.setJsonObject(null);
                }else{
                    if (statusCode!=200){
                        jsonInfo.setResultFlag(false);
                        jsonInfo.setErr(statusCode);
                    }else {
                        jsonInfo.setResultFlag(true);
                        jsonInfo.setErr(0);
                    }
                    jsonInfo.setStatusCode(statusCode);
                    jsonInfo.setJsonObject(result);
                    jsonInfo.setHeader(rspHeader);
                    Logger.debug(RESPONSE + result);
                }
                listenNotify();
            });
        });

        funWait();

        return jsonInfo;
    }

    @Override
    public JSONInfo httpRequetsStringPostWithOutBody(int timeout, Map<String, String> headers, String url) {
        final JSONInfo jsonInfo = new JSONInfo();

        mActivity.runOnUiThread(() -> {
            RequestWs requestWs = new RequestWs(mActivity, url, timeout,true);
            requestWs.httpRequetsStringPostWithOutBody(headers, (result, statusCode,rspHeader) -> {
                if (result == null) {
                    jsonInfo.setResultFlag(false);
                    if (statusCode==200 || statusCode==204)
                        jsonInfo.setErr(0);
                    else
                        jsonInfo.setErr(statusCode);

                    jsonInfo.setStatusCode(statusCode);
                    jsonInfo.setJsonObject(null);
                }else{
                    if (statusCode!=200){
                        jsonInfo.setResultFlag(false);
                        jsonInfo.setErr(statusCode);
                    }else {
                        jsonInfo.setResultFlag(true);
                        jsonInfo.setErr(0);
                    }
                    jsonInfo.setStatusCode(statusCode);
                    jsonInfo.setJsonObject(result);
                    jsonInfo.setHeader(rspHeader);
                    Logger.debug(RESPONSE + result);
                }
                listenNotify();
            });
        });

        funWait();

        return jsonInfo;
    }

    private JSONObject setEndSession(String category, String detail){
        final ReqEndSession reqEndSession = new ReqEndSession();
        reqEndSession.setReqCategory(category);
        reqEndSession.setReqReasonDetail(detail);
        return reqEndSession.buildsObjectJSON();
    }

    @Override
    public void senEndSession(String category,String msg){
        try{
            if (url != null){
                processApiRest(TIMEOUT,setEndSession(category,msg),this.header,this.url);
                TMConfig.getInstance().incOpnNumber();
            }
        }catch (Exception e){
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
    }

    @Override
    public InputInfo showLastOperations(String row, String content) {
        transView.showLastOperations(row, content,listener);
        funWait();
        InputInfo info = new InputInfo();
        if (mRet == CANCELTRANS) {
            info.setResultFlag(false);
            info.setErrno(errorCode == -1 ? TcodeError.T_USER_CANCEL_INPUT : errorCode);
        } else if (mRet == BACKTRANS){
            info.setResultFlag(false);
            info.setBack(true);
        }else {
            info.setResultFlag(true);
        }
        return info;
    }

}
