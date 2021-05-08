package com.bcp.printer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.desert.keyboard.InputManager;
import com.android.newpos.pay.R;
import com.bcp.amount.MsgScreenAmount;
import com.bcp.document.InputData;
import com.bcp.document.InputInfo;
import com.bcp.document.MsgScreenDocument;
import com.bcp.menus.seleccion_cuenta.InputSelectAccount;
import com.bcp.menus.seleccion_cuenta.SelectedAccountItem;
import com.bcp.rest.JSONInfo;
import com.bcp.rest.ultimasoperaciones.response.RspViewLastOperations;
import com.newpos.libpay.device.card.CardInfo;
import com.newpos.libpay.device.pinpad.OfflineRSA;
import com.newpos.libpay.device.pinpad.PinInfo;
import com.newpos.libpay.device.printer.PrintManager;
import com.newpos.libpay.device.scanner.QRCInfo;
import com.newpos.libpay.presenter.TransUI;
import com.newpos.libpay.trans.Trans;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.bcp.definesbcp.Definesbcp.ITEM_BORRAR_LOTE;
import static com.bcp.definesbcp.Definesbcp.ITEM_REPORTE_DETALLADO;
import static com.bcp.definesbcp.Definesbcp.ITEM_TEST;

public class PrintParameter extends AppCompatActivity implements TransUI {

    LottieAnimationView gifImageView;
    private PrintManager manager = null;
    private static boolean printTotals;
    private static final String MSG_ERROR = "error";
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer);

        gifImageView = findViewById(R.id.gifImpresion);
        linearLayout = findViewById(R.id.procesandoTrans);

        loading();

        manager = PrintManager.getmInstance(this, this);

        String typeReport;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                typeReport = null;
            } else {
                typeReport = extras.getString("typeReport");
            }
        } else {
            typeReport = (String) savedInstanceState.getSerializable("typeReport");
        }
        new Handler().postDelayed(() -> {
            printAll(typeReport);
            linearLayout.setVisibility(View.VISIBLE);
            gifImageView.pauseAnimation();
            final LottieAnimationView gifProgress;
            gifProgress = findViewById(R.id.gifProgres);
            gifProgress.setAnimation("loader.json");
            gifProgress.playAnimation();
        }, 4000);
    }

    private void printAll(final String typeReport) {
        new Thread() {
            @Override
            public void run() {
                switch (typeReport) {
                    case ITEM_TEST:
                        manager.printParamInit();
                        break;
                    case ITEM_REPORTE_DETALLADO:
                    case ITEM_BORRAR_LOTE:
                        manager.printDetailReport();
                        break;
                    default:
                        break;
                }
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            finish();
        }
    };

    private void loading() {
        gifImageView.playAnimation();
    }

    @Override
    public InputInfo getOutsideInput(MsgScreenAmount msgScreenAmount) {
        return null;
    }

    @Override
    public CardInfo getCardUse(String title, String msg, int timeout, int mode) {
        return null;
    }

    @Override
    public CardInfo getCardFallback(String msg, int timeout, int mode, String title) {
        return null;
    }

    @Override
    public PinInfo getPinpadOnlinePin(int timeout, String amount, String cardNo, String tittle) {
        return null;
    }

    @Override
    public PinInfo getPinpadOfflinePin(int timeout, int i, OfflineRSA key, int offlinecounts) {
        return null;
    }

    @Override
    public int showCardConfirm(int timeout, String cn) {
        return 0;
    }

    @Override
    public InputInfo showMessageInfo(String title, String msg, String btnCancel, String btnConfirm, int timeout, boolean isQR) {
        return null;
    }

    @Override
    public InputInfo showMessageImpresion(MsgScreenPrinter msgScreenPrinter) {
        return null;
    }

    @Override
    public int showCardApplist(int timeout, String nameTrans, String[] list) {
        return 0;
    }

    @Override
    public int showMultiLangs(int timeout, String[] langs) {
        return 0;
    }

    @Override
    public void handlingBCP(int tittle, int status, boolean imgProgress) {
        //
    }

    @Override
    public void setHeader(Map<String, String> header, String url) {

    }

    @Override
    public InputInfo alertView(String transEname, String msg1, String msg2,String msg3) {
        return null;
    }

    @Override
    public void verifyCard(String tittle, boolean imgProgress) {
        //
    }

    @Override
    public void handlingBCP(String title, String status, boolean imgProgress ) {
        //El metodo heredado de TransUI.class
    }

    @Override
    public void trannSuccess(int code, String... args) {
        //
    }

    @Override
    public void showError( String tittle, int errcode) {
        //
    }

    @Override
    public void showError(String tittle, String cod, String errcode) {
        //
    }

    @Override
    public InputInfo showInputUser(int timeout, String title, String label, int min, int max) {
        return null;
    }

    @Override
    public InputInfo showInputDoc(MsgScreenDocument msgScreenDocument) {
        return null;
    }

    @Override
    public InputData showInputTypeTrans(MsgScreenDocument msgScreenDocument) { return null; }

    @Override
    public InputData showseleccionServicio(MsgScreenDocument msgScreenDocument) { return null; }

    @Override
    public InputInfo showInputclavSecre(MsgScreenDocument msgScreenDocument) {
        return null;
    }

    @Override
    public InputData showInputbeneficiarycobros(MsgScreenDocument msgScreenDocument) {
        return null;
    }

    @Override
    public InputData showInputdatosoBeneficiary(MsgScreenDocument msgScreenDocument) {
        return null;
    }

    @Override
    public InputData showInputImpresionUltimas(String title, String msg, int timeout) {
        return null;
    }

    @Override
    public InputData showInputdetailPayment(MsgScreenDocument msgScreenDocument) {
        return null;
    }

    @Override
    public InputData showInputdetalleServicio(MsgScreenDocument msgScreenDocument) {
        return null;
    }

    @Override
    public void toasTrans(int errcode, boolean sound, boolean isErr) {
        //
    }

    @Override
    public void toasTrans(String errcode, boolean sound, boolean isErr) {
       //
    }

    @Override
    public InputInfo showConfirmAmount(int timeout, String title, String[] label, String amnt, boolean isHTML, float textSize) {
        return null;
    }

    @Override
    public InputInfo showSignature(int timeout, String title, String transType) {
        return null;
    }

    @Override
    public InputInfo showList(int timeout, String title, String transType, final ArrayList<String> listMenu, int id) {
        return null;
    }

    @Override
    public InputSelectAccount showSelectAccount(int timeout, String[] data, List<SelectedAccountItem> items, List<SelectedAccountItem> items2) {
        return null;
    }

    public static boolean isPrintTotals() {
        return printTotals;
    }

    public static void setPrintTotals(boolean printTotal) {
        printTotals = printTotal;
    }

    @Override
    public QRCInfo getQRCInfo(int timeout, InputManager.Style mode) {
        return null;
    }

    @Override
    public InputInfo showChangePwd(int timeout, String title, int min, int max) {
        return null;
    }

    @Override
    public JSONInfo processApiRest(int timeout, JSONObject jsonObject, Map<String, String> headers, String url) {
        return null;
    }

    @Override
    public JSONInfo processApiRestStringPost(int timeout, Map<String, String> params, String url) {
        return null;
    }

    @Override
    public JSONInfo processApiRestStringGet(int timeout, Map<String, String> headers, String data, String url) {
        return null;
    }

    @Override
    public JSONInfo httpRequetsStringPostWithOutBody(int timeout, Map<String, String> headers, String url) {
        return null;
    }

    @Override
    public void senEndSession(String category, String msg) {

    }

    @Override
    public InputInfo showLastOperations(String row, String content) {
        return null;
    }

}
