package cn.desert.newpos.payui.master;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.android.desert.keyboard.InputManager;
import com.android.newpos.pay.R;
import com.bcp.amount.MsgScreenAmount;
import com.bcp.definesbcp.Definesbcp;
import com.bcp.document.ClassArguments;
import com.bcp.document.MsgScreenDocument;
import com.bcp.inicializacion.configuracioncomercio.Rango;
import com.bcp.inicializacion.init_emv.CapkRow;
import com.bcp.inicializacion.init_emv.EmvAppRow;
import com.bcp.inicializacion.tools.PolarisUtil;
import com.bcp.menus.seleccion_cuenta.SeleccionCuentaAdaptadorItem;
import com.bcp.menus.seleccion_cuenta.SeleccionCuentaAdaptadorItem2;
import com.bcp.menus.seleccion_cuenta.SelectedAccountItem;
import com.bcp.printer.MsgScreenPrinter;
import com.bcp.teclado_alfanumerico.ClickKeyboard;
import com.bcp.teclado_alfanumerico.ClickSpinner;
import com.bcp.tools.MenuApplicationsList;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.newpos.libpay.Logger;
import com.newpos.libpay.PaySdk;
import com.newpos.libpay.PaySdkException;
import com.newpos.libpay.device.user.OnUserResultListener;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.presenter.TransView;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.Trans;
import com.newpos.libpay.utils.PAYUtils;
import com.pos.device.printer.Printer;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import cn.desert.newpos.payui.UIUtils;
import cn.desert.newpos.payui.base.PayApplication;
import static com.android.newpos.pay.ProcessingCertificate.polarisUtil;
import static com.android.newpos.pay.StartAppBCP.batteryStatus;
import static com.android.newpos.pay.StartAppBCP.paperStatus;
import static com.android.newpos.pay.StartAppBCP.rango;
import static com.android.newpos.pay.StartAppBCP.variables;
import static com.bcp.definesbcp.Variables.FALLBACK;
import static com.newpos.libpay.trans.Trans.ID_LOTE;
import static com.newpos.libpay.trans.Trans.PAGOSERVICIOS;
import static com.newpos.libpay.trans.Trans.RETIRO;


/**
 * Created by zhouqiang on 2017/7/3.
 */

public class MasterControl extends AppCompatActivity implements TransView, View.OnClickListener {

    private static final String MSG_ERROR = "error";
    Button btnConfirm;
    Button btnCancel;
    EditText editCardNO;
    Button btn00;
    ImageButton btnConti;

    //Toolbar
    TextView clickClose;
    TextView clickBack;
    ImageView close;
    ImageView volver;
    ImageView menu;
    TextView etTitle;

    //screen amount
    TextView tvMaxAmount;
    TextView tvAccount;
    TextView tvSymbol;


    //Type Account
    RadioButton rbMon1;
    RadioButton rbMon2;
    String typeCoin = "1";

    //Input user
    TextView btnCancelInputUser;
    TextView btnAcceptInputUser;
    EditText etInputuser;
    TextView tvInputuser;
    TextView tvClavesecreta;
    int minEtInputuser;
    int maxEtInputuser;

    //Show message info
    Button btnCancelMsg;
    Button btnConfirmMsg;
    TextView etMsgInfo;
    LinearLayout linearLogoMsg;

    //Show message impresion
    TextView msgImpresion;
    TextView msgDetUno;
    TextView msgDetDos;
    TextView msgInfo;
    TextView signoSol;
    TextView selectDocument;

    LottieAnimationView gifProgress;
    OnUserResultListener listener;
    RelativeLayout keyboardNum;
    RelativeLayout keyboardAlfa;

    LinearLayout procesandoTrans;
    LottieAnimationView gifImageView;

    String inputContent;
    Toast toast;

    private SelectedAccountItem selectedItem;
    private ClassArguments arguments;
    private MsgScreenDocument objDatosDocument;

    private CountDownTimer countDownTimerSignature;
    private CountDownTimer countDownTimer;

    public static final String TRANS_KEY = "TRANS_KEY";

    private boolean activeDebugLocal = false;

    private static final String MSG_TIMEOUT = "Master Control";

    //Firma
    boolean isSignature;
    boolean isOnSignature;
    private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;
    private Button mCancelSignature;

    ClickKeyboard keyboards;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PayApplication.getInstance().addActivity(this);

        variables.setCtlSign(false);
        variables.setHolderName("");
        String type = getIntent().getStringExtra(TRANS_KEY);
        toast = new Toast(MasterControl.this);

        if (variables.getContFallback() != FALLBACK) {

            switch (type) {
                case Trans.SETTLE:
                case Trans.ECHO_TEST:
                case Trans.LOGIN:
                case Trans.ULT_OPERACIONES:
                    break;
                default:
                    //Sino esta inicializado debe leer los AID y Capks por default para procesar la tarjeta del agente
                    if (type.equals(Trans.INIT_T) && !polarisUtil.isInit()) {
                        polarisUtil.searchfile(MasterControl.this, PolarisUtil.NAME_DB, PolarisUtil.NAME_DB);
                    }

                    runOnUiThread(() -> {
                        EmvAppRow emvappRow;
                        emvappRow = EmvAppRow.getSingletonInstance();
                        emvappRow.selectEMVAPPROW(MasterControl.this);

                        CapkRow capkRow;
                        capkRow = CapkRow.getSingletonInstance();
                        capkRow.selectCAPKROW(MasterControl.this);
                    });
                    break;
            }

            variables.setIdAcquirer(ID_LOTE);
            startTrans(type);
        } else {
            if (variables.getIdAcquirer() != null)
                startTrans(type);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PaySdk.getInstance().releaseCard();
        clearVar();
    }


    private void clearVar() {
        btnConfirm = null;
        btnCancel = null;
        editCardNO = null;
        btn00 = null;
        btnConti = null;

        //Toolbar
        clickClose= null;
        clickBack= null;
        close= null;
        volver= null;
        menu= null;
        etTitle= null;

        //screen amount
        tvMaxAmount= null;
        tvAccount= null;
        tvSymbol= null;


        //Type Account
        rbMon1= null;
        rbMon2= null;
        typeCoin= null;

        //Input user
        btnCancelInputUser= null;
        btnAcceptInputUser= null;
        etInputuser= null;
        tvInputuser= null;
        tvClavesecreta= null;
        minEtInputuser= 0;
        maxEtInputuser= 0;

        //Show message info
        btnCancelMsg= null;
        btnConfirmMsg= null;
        etMsgInfo= null;
        linearLogoMsg= null;

        //Show message impresion
        msgImpresion= null;
        msgDetUno= null;
        msgDetDos= null;
        msgInfo= null;
        signoSol= null;
        selectDocument= null;

        gifProgress= null;
        listener= null;
        keyboardNum= null;
        keyboardAlfa= null;

        procesandoTrans= null;
        gifImageView= null;

        inputContent= null;
        toast= null;

        selectedItem= null;
        arguments= null;
        objDatosDocument= null;

        if (countDownTimer!= null){
            countDownTimer.cancel();
            countDownTimer= null;
        }
        if (countDownTimerSignature != null){
            countDownTimerSignature.cancel();
            countDownTimerSignature= null;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.clickclose:
            case R.id.clickback:
            case R.id.iv_close:
            case R.id.back:
            case R.id.btn_cancel_mon:
            case R.id.cardno_cancel:
            case R.id.last4_cancel:
            case R.id.result_cancel:
            case R.id.exitMsg:
                listener.cancel();
                deleteTimer();
                break;
            case R.id.btn_conf_mon:
            case R.id.cardno_confirm:
            case R.id.confirm:
            case R.id.result_confirm:
            case R.id.Btncontinuar:
                listener.confirm(InputManager.Style.COMMONINPUT);
                deleteTimer();
                break;
            case R.id.rb_moneda1:
                rbMon1.setChecked(true);
                rbMon2.setChecked(false);
                typeCoin = "1";
                break;
            case R.id.rb_moneda2:
                rbMon1.setChecked(false);
                rbMon2.setChecked(true);
                typeCoin = "2";
                break;
            case R.id.last4_confirm:
                if (etInputuser.getText().toString().equals(""))
                    UIUtils.toast(MasterControl.this, R.drawable.ic_lg_light, getString(R.string.ingrese_dato), Toast.LENGTH_SHORT, new int[]{Gravity.CENTER, 0, 0});
                else {
                    if (etInputuser.length() < minEtInputuser) {
                        UIUtils.toast(MasterControl.this, R.drawable.ic_lg_light, getString(R.string.longitud_invalida), Toast.LENGTH_SHORT, new int[]{Gravity.CENTER, 0, 0});
                    } else {
                        hideKeyBoard(etInputuser.getWindowToken());
                        inputContent = etInputuser.getText().toString();
                        listener.confirm(InputManager.Style.COMMONINPUT);
                        deleteTimer();
                    }
                }
                break;
            case R.id.continuar:
            case R.id.continuarAlfa:
                if (!longMin(etInputuser)){
                    listener.confirm(InputManager.Style.COMMONINPUT);
                }
                break;
            default:
                // Do nothing
                break;
        }
    }

    @Override
    public void showCardView(final String title, final String msg, final int timeout, final int mode, OnUserResultListener l) {
        this.listener = l;
        runOnUiThread(() -> {
            setContentView(R.layout.activity_card);

            deleteTimer();

            LinearLayout toolbar = findViewById(R.id.toolbar_id);
            toolbar.setBackgroundColor(getResources().getColor(R.color.screenCard));

            LottieAnimationView gifImageView;
            gifImageView = findViewById(R.id.gifFlechas);
            gifImageView.setAnimation("flechas_arriba.json");
            gifImageView.playAnimation();

            LinearLayout linearLayout;
            linearLayout = findViewById(R.id.linearPrueba);

            Animation animation = AnimationUtils.loadAnimation(MasterControl.this, R.anim.tarjeta_arriba);
            linearLayout.startAnimation(animation);

            RelativeLayout relativeLayout = findViewById(R.id.RelativeTimeout);
            relativeLayout.setVisibility(View.VISIBLE);

            final TextView txtTimeout = findViewById(R.id.TxtTimeout);

            timerCard(timeout, txtTimeout);

            TextView msg1 = findViewById(R.id.title);
            TextView msg2 = findViewById(R.id.title2);
            msg1.setText(title);
            msg2.setText(msg);

            close = findViewById(R.id.iv_close);
            close.setVisibility(View.VISIBLE);
            clickClose = findViewById(R.id.clickclose);
            clickClose.setVisibility(View.VISIBLE);
            clickClose.setOnClickListener(MasterControl.this);

        });
    }


    @Override
    public void showCardNo(final int timeout, final String pan, OnUserResultListener l) {
        this.listener = l;
        runOnUiThread(() -> {
            setContentView(R.layout.trans_show_cardno);
            showConfirmCardNO(pan);
            deleteTimer();
        });
    }

    @Override
    public void showMessageInfo(final String title, final String msg, final String btnCancel, final String btnConfirm, final int timeout, final boolean isQR, OnUserResultListener l) {
        this.listener = l;

        runOnUiThread(() -> {
            setContentView(R.layout.mensaje_impresion);

            deleteTimer();

            LinearLayout linearLayout;
            TextView tvtMsg = findViewById(R.id.msgImpresion);
            btnCancelMsg = findViewById(R.id.result_cancel);
            btnConfirmMsg =  findViewById(R.id.result_confirm);
            if (isQR){
                linearLogoMsg = findViewById(R.id.linearLogoMsg);
                findViewById(R.id.imgAvatar).setBackgroundResource(R.drawable.ic_avatar_supervisor);
                linearLogoMsg.setVisibility(View.VISIBLE);
                linearLayout = findViewById(R.id.linearContenido);
                linearLayout.setVisibility(View.GONE);

                etTitle = findViewById(R.id.tvMsg1);
                etMsgInfo = findViewById(R.id.tvMsg2);
                tvtMsg.setText(R.string.obtenercodigo);
            }else {
                etTitle = findViewById(R.id.msgInfo);
                etMsgInfo = findViewById(R.id.msgDetalleDos);
                linearLayout = findViewById(R.id.linearMonto);
                linearLayout.setVisibility(View.GONE);
                tvtMsg.setText(R.string.continuar);
            }

            etTitle.setText(title);
            etMsgInfo.setText(msg);
            btnCancelMsg.setText(btnCancel);
            btnConfirmMsg.setText(btnConfirm);

            btnCancelMsg.setOnClickListener(MasterControl.this);
            btnConfirmMsg.setOnClickListener(MasterControl.this);

            counterDownTimer(timeout, MSG_TIMEOUT);
        });
    }

    @Override
    public void showMessageImpresion(final MsgScreenPrinter msgScreenPrinter, OnUserResultListener l) {
        this.listener = l;

        runOnUiThread(() -> {
            try {
                setContentView(R.layout.mensaje_impresion);

                btnCancelMsg = findViewById(R.id.result_cancel);
                btnConfirmMsg = findViewById(R.id.result_confirm);
                msgImpresion = findViewById(R.id.msgImpresion);
                signoSol = findViewById(R.id.signoSol);
                msgInfo = findViewById(R.id.msgInfo);
                msgDetUno = findViewById(R.id.msgDetalleUno);
                msgDetDos = findViewById(R.id.msgDetalleDos);

                msgInfo.setText(msgScreenPrinter.getTittle());

                if (!msgScreenPrinter.getMsgAmount().equals("")) {
                    msgDetUno.setText(UIUtils.formatMiles(msgScreenPrinter.getMsgAmount()));
                }else {
                    msgDetUno.setText("");
                }

                signoSol.setText(msgScreenPrinter.getTransEname().equals(Trans.RETIRO) ? "S/" : msgScreenPrinter.getSymbol());
                if (msgScreenPrinter.getMsgAmount().equals("") && msgScreenPrinter.getSymbol().equals("")){
                    findViewById(R.id.linearMonto).setVisibility(View.GONE);
                }
                msgDetDos.setText(msgScreenPrinter.getMsgAccount());
                msgImpresion.setText(msgScreenPrinter.getMsgBanner());
                btnCancelMsg.setText(msgScreenPrinter.getMsgButtonCancel());
                btnConfirmMsg.setText(msgScreenPrinter.getMsgButtonConfirm());

                btnCancelMsg.setOnClickListener(MasterControl.this);
                btnConfirmMsg.setOnClickListener(MasterControl.this);
            }catch (Exception e){
                listener.cancel(TcodeError.T_ERR_DATA_NULL);
                Logger.logLine(Logger.LOG_EXECPTION, "showMessageImpresion");
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            }
        });
    }

    @Override
    public void showInputView(final MsgScreenAmount msgScreenAmount, OnUserResultListener l) {
        this.listener = l;
        runOnUiThread(() -> {

            setContentView(R.layout.activity_monto);

            keyboardNum = findViewById(R.id.keyboardNumerico);
            btn00 = findViewById(R.id.numb00);
            close = findViewById(R.id.iv_close);
            clickClose = findViewById(R.id.clickclose);
            etTitle = findViewById(R.id.title_toolbar);
            btnAcceptInputUser = findViewById(R.id.continuar);
            etInputuser = findViewById(R.id.monto);
            minEtInputuser = 0;

            tvMaxAmount = findViewById(R.id.layoutMontoTittle1);
            tvAccount = findViewById(R.id.account);
            tvSymbol = findViewById(R.id.signSol);

            deleteTimer();

            btn00.setEnabled(true);
            btn00.setText(getResources().getString(R.string._00));
            btn00.setTextColor(Color.BLACK);
            close.setVisibility(View.VISIBLE);
            clickClose.setVisibility(View.VISIBLE);

            etTitle.setText(msgScreenAmount.getTransEname().equals(Trans.DEPOSITO) ? getResources().getString(R.string.depAmount) : msgScreenAmount.getTittle());
            tvMaxAmount.setText(String.format("%s %s", msgScreenAmount.getTransEname().equals(Trans.DEPOSITO) ? "S/" : msgScreenAmount.getTypeCoin(), UIUtils.formatMiles(msgScreenAmount.getMaxAmount())));

            if (!msgScreenAmount.getAccount().equals("") && !msgScreenAmount.getTypeCoin().equals("")){
                tvAccount.setText(String.format("%s %s", (msgScreenAmount.getTransEname().equals(Trans.DEPOSITO) ? "a " : "de ") + "cuenta " + msgScreenAmount.getAccount(), msgScreenAmount.getTypeCoin()));
            }else {
                tvAccount.setText("");
            }

            tvSymbol.setText(msgScreenAmount.getTransEname().equals(Trans.DEPOSITO) ? "S/" : msgScreenAmount.getTypeCoin());

            btnAcceptInputUser.setOnClickListener(v -> {
                String amount = etInputuser.getText().toString();
                long amt = Long.parseLong(amount.replaceAll("[,.]",""));
                if (amt < Long.parseLong(msgScreenAmount.getMinAmount())) {
                    msgAccountSingle(getResources().getString(R.string.minPermitido));
                }else if (amt > Long.parseLong(msgScreenAmount.getMaxAmount())){
                    msgAccountSingle(getResources().getString(R.string.maxPermitido));
                }else {
                    inputContent = amount;
                    listener.confirm(InputManager.Style.COMMONINPUT);
                    deleteTimer();
                }
            });


            clickClose.setOnClickListener(MasterControl.this);

            counterDownTimer(msgScreenAmount.getTimeOut(), MSG_TIMEOUT);
            setKeyboard(etInputuser, 9,true,false, false, minEtInputuser,keyboardNum,null);

        });
    }

    private void msgAccountSingle(String msg){
        final RelativeLayout relativeLayout = findViewById(R.id.msgAccount);
        relativeLayout.setVisibility(View.VISIBLE);
        TextView tvAccountSymbol = findViewById(R.id.msg2Screen);
        tvAccountSymbol.setText(msg);
        Button btnEntentido = findViewById(R.id.entendidoMsg);

        relativeLayout.setOnClickListener(MasterControl.this);

        btnEntentido.setOnClickListener(v -> relativeLayout.setVisibility(View.INVISIBLE));
    }

    @Override
    public String getInput(InputManager.Mode type) {
        return inputContent;
    }

    @Override
    public SelectedAccountItem getSelectedAccountItem() {
        return selectedItem;
    }

    @Override
    public ClassArguments getArguments() {
        return arguments;
    }

    @Override
    public void setArgumentsClass(ClassArguments classArguments) {
        this.arguments = classArguments;
    }

    @Override
    public MsgScreenDocument getMsgScreendocument() {
        return objDatosDocument;
    }

    @Override
    public void showCardAppListView(int timeout, final String nameTrans, final String[] apps, OnUserResultListener l) {
        this.listener = l;

        runOnUiThread(() -> {

            MenuApplicationsList applicationsList = new MenuApplicationsList(MasterControl.this,nameTrans);
            applicationsList.menuApplicationsList(apps, idApp -> listener.confirm(idApp));
        });
    }

    @Override
    public void showMultiLangView(int timeout, String[] langs, OnUserResultListener l) {
        this.listener = l;
    }

    @Override
    public void showSuccess(final String info, final String... msg) {
        runOnUiThread(() -> {
            UIUtils.startResult(MasterControl.this, ResultControl.class, true, true, new String[] {info, msg[0],msg[1]}, false);
            deleteTimer();
        });
    }

    @Override
    public void showError(final String tittle, final String cod, final String err) {
        runOnUiThread(() -> {
            String[] msgError = new String[3];
            msgError[0] = tittle.replace("_", " ");
            msgError[1] = cod;
            msgError[2] = err;
            UIUtils.startResult(MasterControl.this, ResultControl.class, true, false, msgError,false);
            deleteTimer();
        });
    }

    @Override
    public void showMsgInfoBCP(final String tittle, final String status, final boolean imgProgress) {
        runOnUiThread(() -> {
            setContentView(R.layout.handling_bcp);

            gifProgress = findViewById(R.id.gifProgres);

            TextView tvtTittle = findViewById(R.id.msgTipoOpe);
            TextView tvtmsg = findViewById(R.id.msgTituloOpe);

            if (imgProgress){
                gifProgress.setAnimation("check.json");
            }else {
                gifProgress.setAnimation("loader.json");
            }
            gifProgress.playAnimation();

            if (tittle != null && !tittle.equals("")){
                tvtTittle.setText(tittle);
            }

            if (status != null && !status.equals("")){
                tvtmsg.setText(status);
            }
            deleteTimer();
        });
    }
    @Override
    public void showVerifyCard(final String tittle, final boolean imgProgress) {
        runOnUiThread(() -> {
            setContentView(R.layout.verify_card);

            TextView tvtTittle = findViewById(R.id.verify);

            if (tittle != null && !tittle.equals("")){
                tvtTittle.setText(tittle);
            }

            final LottieAnimationView gifProgressCard;
            gifProgressCard = findViewById(R.id.gifProgresverify);

            final LottieAnimationView gifImageView;
            gifImageView = findViewById(R.id.gifFlechasverify);

            if (imgProgress){
                gifProgressCard.setAnimation("check.json");
                gifProgressCard.playAnimation();
                gifImageView.setVisibility(View.GONE);
            }else {
                gifProgressCard.setAnimation("loader.json");
                gifProgressCard.playAnimation();
                gifImageView.playAnimation();
            }
            deleteTimer();
        });
    }

    @Override
    public void showInputUser(final int timeout, final String title, final String label, final int min, final int max, OnUserResultListener l) {
        this.listener = l;

        runOnUiThread(() -> {
            setContentView(R.layout.activity_input_user);

            close = findViewById(R.id.iv_close);
            clickClose = findViewById(R.id.clickclose);
            etTitle = findViewById(R.id.title_toolbar);
            close.setVisibility(View.VISIBLE);
            clickClose.setVisibility(View.VISIBLE);
            try {
                etTitle.setText(title.replace("_", " "));
            } catch (Exception e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_ERROR + e);
                etTitle.setText("");
            }

            minEtInputuser = min;
            maxEtInputuser = max;

            etInputuser = findViewById(R.id.editText_input);
            etInputuser.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});
            tvInputuser = findViewById(R.id.textView_title);
            btnCancelInputUser = findViewById(R.id.last4_cancel);
            btnAcceptInputUser = findViewById(R.id.last4_confirm);

            clickClose.setOnClickListener(MasterControl.this);
            btnAcceptInputUser.setOnClickListener(MasterControl.this);
            btnCancelInputUser.setOnClickListener(MasterControl.this);

            tvInputuser.setText(label);

            counterDownTimer(timeout, MSG_TIMEOUT);
        });
    }


    @Override
    public void showInputDoc(MsgScreenDocument msgScreenDocument, OnUserResultListener l) {
        this.listener = l;

        runOnUiThread(() -> {
            setContentView(R.layout.ingreso_documento);

            keyboardNum = findViewById(R.id.keyboardNumerico);
            keyboardAlfa = findViewById(R.id.keyboardAlfa);
            close = findViewById(R.id.iv_close);
            clickClose = findViewById(R.id.clickclose);
            etTitle = findViewById(R.id.title_toolbar);
            close.setVisibility(View.VISIBLE);
            clickClose.setVisibility(View.VISIBLE);
            findViewById(R.id.linearTipDoc).setVisibility(msgScreenDocument.isBanner() ? View.VISIBLE : View.GONE);

            try {
                etTitle.setText(msgScreenDocument.getTittle());
            } catch (Exception e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_ERROR + e);
                etTitle.setText("");
            }

            deleteTimer();

            minEtInputuser = msgScreenDocument.getMinLength();
            maxEtInputuser = msgScreenDocument.getMaxLength();

            etInputuser = findViewById(R.id.numDoc);
            if(msgScreenDocument.getTransEname().equals(RETIRO) || msgScreenDocument.getTransEname().equals(PAGOSERVICIOS)) {
                etInputuser.setInputType(129);
            }else {
                etInputuser.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }

            etInputuser.setBackgroundResource(R.drawable.btn_spinner_first);

            tvInputuser = findViewById(R.id.doc_num);
            selectDocument = findViewById(R.id.selectDocument);
            btnAcceptInputUser = findViewById(R.id.continuar);

            btnConti = findViewById(R.id.continuarAlfa);
            btnConti.setOnClickListener(MasterControl.this);

            clickClose.setOnClickListener(MasterControl.this);
            btnAcceptInputUser.setOnClickListener(MasterControl.this);

            tvInputuser.setText(msgScreenDocument.getTittleDoc());

            counterDownTimer(msgScreenDocument.getTimeOut(), MSG_TIMEOUT);
            setKeyboard(etInputuser, msgScreenDocument.getMaxLength(),false,false, msgScreenDocument.isAlfa(), minEtInputuser,keyboardNum,keyboardAlfa);
            if (msgScreenDocument.isBanner()){
                ClickSpinner clickSpinner =  new ClickSpinner(findViewById(R.id.textSpinner),selectDocument,findViewById(R.id.tipDoc),findViewById(R.id.linearBotones),findViewById(R.id.relativeTipDoc),getResources().getStringArray(R.array.typeDocumen),MasterControl.this);
                clickSpinner.setDocument(tvInputuser,findViewById(R.id.linearDoc),keyboardNum,keyboardAlfa);
                clickSpinner.setClickKeyboard(keyboards);
                clickSpinner.spinnerBotones(msgScreenDocument.getTransEname(),"Beneficiario");
            }
        });
    }

    @Override
    public void showInputTypeTrans(MsgScreenDocument msgScreenDocument, OnUserResultListener l) {
        this.listener = l;
        this.arguments = new ClassArguments();
        this.objDatosDocument = msgScreenDocument;

        runOnUiThread(() -> setContentView(R.layout.container_fragment));
    }

    @Override
    public void showSeleccionServicio(MsgScreenDocument msgScreenDocument, OnUserResultListener l) {
        this.listener = l;
        this.arguments = new ClassArguments();
        this.objDatosDocument = msgScreenDocument;

        runOnUiThread(() -> setContentView(R.layout.container_pagoservicios));
    }

    @Override
    public void showInputdatosoBeneficiary(MsgScreenDocument msgScreenDocument, OnUserResultListener l) {
        this.listener = l;
        this.arguments = msgScreenDocument.getArguments();
        this.objDatosDocument = msgScreenDocument;
        this.arguments.setSelectedAccountItem(msgScreenDocument.getSelectedAccountItem());

        runOnUiThread(() -> setContentView(R.layout.container_tarjeta));
    }

    @Override
    public void showInputImpresionUltimas(String title, String msg, int timeout,OnUserResultListener l) {
        this.listener = l;
        runOnUiThread(() -> {
            setContentView(R.layout.activity_printer);

            LinearLayout linearLayout;
            TextView titulo1;
            TextView titulo2;

            linearLayout = findViewById(R.id.procesandoTrans);
            titulo1 = findViewById(R.id.msgTipoOpe);
            titulo2 = findViewById(R.id.msgTituloOpe);

            titulo1.setText(title);
            titulo2.setText(msg);

            loading();

            titulo2.setGravity(-1);

            new Handler().postDelayed(() -> {
                final LottieAnimationView gifProgress;
                gifProgress = findViewById(R.id.gifProgres);
                linearLayout.setVisibility(View.VISIBLE);
                gifImageView.pauseAnimation();
                gifProgress.setAnimation("check.json");
                gifProgress.playAnimation();
                listener.confirm(InputManager.Style.COMMONINPUT);
            }, timeout);

            deleteTimer();
        });
    }

    private void loading() {
        gifImageView = findViewById(R.id.gifImpresion);
        gifImageView.playAnimation();
    }

    @Override
    public void showInputdetailPayment(MsgScreenDocument msgScreenDocument, OnUserResultListener l) {
        this.listener = l;
        this.arguments = msgScreenDocument.getArguments();
        this.objDatosDocument = msgScreenDocument;
        this.arguments.setSelectedAccountItem(msgScreenDocument.getSelectedAccountItem());

        runOnUiThread(() -> setContentView(R.layout.container_tarjeta_ps));
    }

    @Override
    public void showInputdetalleServicio(MsgScreenDocument msgScreenDocument, OnUserResultListener l) {
        this.listener = l;
        this.arguments = msgScreenDocument.getArguments();
        this.objDatosDocument = msgScreenDocument;
        this.arguments.setSelectedAccountItem(msgScreenDocument.getSelectedAccountItem());

        runOnUiThread(() -> setContentView(R.layout.container_pase));
    }

    @Override
    public void showInputclavSecre(MsgScreenDocument msgScreenDocument, OnUserResultListener l) {
        this.listener = l;

        runOnUiThread(() -> {
            setContentView(R.layout.activity_clave);

            keyboardNum = findViewById(R.id.keyboardNumerico);
            close = findViewById(R.id.iv_close);
            clickClose = findViewById(R.id.clickclose);
            etTitle = findViewById(R.id.title_toolbar);
            close.setVisibility(View.VISIBLE);
            clickClose.setVisibility(View.VISIBLE);
            volver = findViewById(R.id.back);
            clickBack = findViewById(R.id.clickback);
            clickBack.setVisibility(View.VISIBLE);
            volver.setVisibility(View.GONE);

            String account = msgScreenDocument.getSelectedAccountItem().getProductDescription() + " " + msgScreenDocument.getSelectedAccountItem().getCurrencyDescription() + " " + msgScreenDocument.getSelectedAccountItem().getCurrencySymbol();
            boolean isSol = msgScreenDocument.getSelectedAccountItem().getCurrencyCode().equals("PEN");

            if (msgScreenDocument.getPaymentOrders() != null)
                isSol = msgScreenDocument.getSelectedAccountItem().getCurrencySymbol().equals(msgScreenDocument.getPaymentOrders().getRspCurrencySymbol());

            infoAccountClient( getResources().getString((isSol) ? R.string.msg1GiroSol : R.string.msg1GiroDol ) , account , isSol ? "" : getResources().getString(R.string.msg3GiroDolar));

            try {
                etTitle.setText(msgScreenDocument.getTittle());
            } catch (Exception e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_ERROR + e);
                etTitle.setText("");
            }

            deleteTimer();

            minEtInputuser = msgScreenDocument.getMinLength();
            maxEtInputuser = msgScreenDocument.getMaxLength();

            etInputuser = findViewById(R.id.clavesecreta);
            etInputuser.setBackgroundResource(R.drawable.btn_spinner_first);

            tvClavesecreta = findViewById(R.id.clave_secreta);
            tvClavesecreta.setTextColor(getResources().getColor(R.color.colorbutNumCuent));
            btnAcceptInputUser = findViewById(R.id.continuar);
            btnAcceptInputUser.setText(getResources().getString(R.string.confirmPasword));


            clickClose.setOnClickListener(MasterControl.this);
            btnAcceptInputUser.setOnClickListener(MasterControl.this);

            tvClavesecreta.setText(msgScreenDocument.getTittleDoc());

            volver.setOnClickListener(v -> listener.back());

            counterDownTimer(msgScreenDocument.getTimeOut(), MSG_TIMEOUT);
            setKeyboard(etInputuser, msgScreenDocument.getMaxLength(),false,true , msgScreenDocument.isAlfa(), minEtInputuser,keyboardNum,null);
        });
    }

    @Override
    public void showInputbeneficiarycobros(MsgScreenDocument msgScreenDocument, OnUserResultListener l) {
        this.listener = l;
        this.arguments = msgScreenDocument.getArguments();
        this.objDatosDocument = msgScreenDocument;
        this.arguments.setSelectedAccountItem(msgScreenDocument.getSelectedAccountItem());

        runOnUiThread(() -> setContentView(R.layout.container_tarjeta_cobros));
    }

    @Override
    public void toasTransView(final String errcode, final boolean sound) {
        runOnUiThread(() -> {
            if (sound) {
                ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2, 2000);
                toneG.stopTone();
            }
            UIUtils.toast(MasterControl.this, R.drawable.ic_lg_light, errcode, Toast.LENGTH_SHORT, new int[]{Gravity.CENTER, 0, 0});
        });

    }

    @Override
    public void showConfirmAmountView(final int timeout, final String title, final String[] label, final String amnt, final boolean isHTML, final float textSize, OnUserResultListener l) {
        this.listener = l;

        runOnUiThread(() -> {
            setContentView(R.layout.detalle_transaccion);

            TextView fullName = findViewById(R.id.fullName);
            TextView total = findViewById(R.id.nroMonto);
            TextView typeAccount = findViewById(R.id.typeAcc);
            TextView accountNumber = findViewById(R.id.AccountNumber);


            etTitle = findViewById(R.id.title_toolbar);
            btnConfirm = findViewById(R.id.confirm);
            close = findViewById(R.id.iv_close);
            clickClose = findViewById(R.id.clickclose);


            close.setVisibility(View.VISIBLE);
            clickClose.setVisibility(View.VISIBLE);
            try {
                etTitle.setText(title);
                fullName.setText(label[0]);
                typeAccount.setText("Cuenta " + label[1] + ":");
                accountNumber.setText(label[2]);
            } catch (Exception e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_ERROR + e);
                etTitle.setText("");
            }

            total.setText("S/ " + amnt);

            btnConfirm.setOnClickListener(MasterControl.this);
            clickClose.setOnClickListener(MasterControl.this);

            counterDownTimer(timeout, MSG_TIMEOUT);
        });
    }

    @Override
    public void showSignatureView(final int timeout, OnUserResultListener l, final String title, final String transType) {
        this.listener = l;
        isSignature = true;
        runOnUiThread(() -> {
            setContentView(R.layout.activity_signature);
            final TextView textViewTitle = findViewById(R.id.textView_cont);
            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
            }
            runTime(textViewTitle);
            mSignaturePad = findViewById(R.id.signature_pad);
            mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
                @Override
                public void onStartSigning() {
                    isOnSignature = true;
                }

                @Override
                public void onSigned() {
                    mClearButton.setEnabled(true);
                }

                @Override
                public void onClear() {
                    runTime(textViewTitle);
                    mClearButton.setEnabled(false);
                    isOnSignature = false;
                }
            });
            mClearButton = findViewById(R.id.clear_button);
            mSaveButton = findViewById(R.id.save_button);
            mCancelSignature = findViewById(R.id.cancel_signature);
            mCancelSignature.setEnabled(true);
            mSaveButton.setEnabled(true);
            mClearButton.setOnClickListener(view -> mSignaturePad.clear());

            mCancelSignature.setOnClickListener(view -> {
                if (isOnSignature) {
                    Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                    saveImage(signatureBitmap);
                }
                countDownTimerSignature.cancel();
                listener.cancel();
            });

            mSaveButton.setOnClickListener(view -> {
                if (isOnSignature) {
                    Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                    saveImage(signatureBitmap);
                    countDownTimerSignature.cancel();
                    listener.confirm(InputManager.Style.COMMONINPUT);
                } else {
                    UIUtils.showAlertDialog(getResources().getString(R.string.informacionmjs), getResources().getString(R.string.ingFirma), MasterControl.this);
                }
            });
        });
    }

    private void runTime(final TextView textViewTitle) {
        if (countDownTimerSignature != null) {
            countDownTimerSignature.cancel();
            countDownTimerSignature = null;
        }
        final int[] i = {120};
        countDownTimerSignature = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                Logger.debug(Logger.TAG + millisUntilFinished);
                textViewTitle.setText(i[0]-- + "");
            }

            public void onFinish() {
                countDownTimerSignature.cancel();
                inputContent = "false";
                listener.cancel();
            }
        }.start();
    }

    private void timerCard(final int timeout, final TextView txtTimer) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        final int[] i = {timeout/1000};
        countDownTimer = new CountDownTimer(timeout, 1000) {

            public void onTick(long millisUntilFinished) {
                Logger.debug(Logger.TAG + millisUntilFinished);
                txtTimer.setText(i[0]--+" "+ getResources().getString(R.string.segundos));
            }

            public void onFinish() {
                countDownTimer.cancel();
                inputContent = "false";
                listener.cancel();
            }
        }.start();
    }

    final void saveImage(Bitmap signature) {

        String root = Environment.getExternalStorageDirectory().toString();

        // the directory where the signature will be saved
        File myDir = new File(root + "/saved_signature");

        // make the directory if it does not exist yet
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        // set the file name of your choice
        String fname = "signature.jpeg";

        // in our case, we delete the previous file, you can remove this
        File file = new File(myDir, fname);
        if (file.exists()) {
            Logger.debug("Archivo eliminado - saveImage" + file.delete());
        }

        try {

            // save the signature
            FileOutputStream out = new FileOutputStream(file);
            signature.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_ERROR + e);
        }
    }

    @Override
    public void showListView(final int timeout, OnUserResultListener l, final String title, final String transType, final ArrayList<String> listMenu, final int id) {
        this.listener = l;
        runOnUiThread(() -> {
            setContentView(R.layout.frag_show_list);
            close = findViewById(R.id.iv_close);
            clickClose = findViewById(R.id.clickclose);
            etTitle = findViewById(R.id.title_toolbar);
            menu = findViewById(R.id.iv_menus);

            close.setVisibility(View.VISIBLE);
            clickClose.setVisibility(View.VISIBLE);
            menu.setImageResource(id);

            try {
                etTitle.setText(title.replace("_", " "));
            } catch (Exception e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_ERROR + e);
                etTitle.setText("");
            }

            initList(listMenu);

            clickClose.setOnClickListener(MasterControl.this);

            counterDownTimer(timeout, MSG_TIMEOUT);
        });

    }

    @Override
    public void getResultListener(OnUserResultListener l) {
        this.listener = l;
    }

    @Override
    public OnUserResultListener getListener() {
        return listener;
    }

    @Override
    public void showSelectAccountView(final int timeout, final String[] data, final List<SelectedAccountItem> items, final List<SelectedAccountItem> items2, OnUserResultListener l) {
        this.listener = l;
        runOnUiThread(() -> {
            setContentView(R.layout.menu_seleccion_cuenta);

            deleteTimer();
            TextView title = findViewById(R.id.title_toolbar);
            TextView label1 = findViewById(R.id.msg2);
            TextView label2 = findViewById(R.id.msg3);
            close = findViewById(R.id.iv_close);
            clickClose = findViewById(R.id.clickclose);

            close.setVisibility(View.VISIBLE);
            clickClose.setVisibility(View.VISIBLE);

            title.setText(data[0]);
            label1.setText(data[1]);
            label2.setText(data[2]);

            clickClose.setOnClickListener(MasterControl.this);

            if (items!=null) {
                RecyclerView recyclerView = findViewById(R.id.recyclerUno);
                recyclerView.setLayoutManager(new GridLayoutManager(MasterControl.this, 2));

                SeleccionCuentaAdaptadorItem cuentaAdaptadorItem = new SeleccionCuentaAdaptadorItem(items, MasterControl.this);
                recyclerView.setAdapter(cuentaAdaptadorItem);
                cuentaAdaptadorItem.setOnClickListener((view, obj, position) -> {
                    selectedItem = obj;
                    listener.confirm(InputManager.Style.COMMONINPUT);
                    deleteTimer();
                    //Aqui va lo que se realiza al presionar los botones del primer recycler
                });
            }

            if (items2!=null) {
                RecyclerView recyclerView2 = findViewById(R.id.recyclerDos);
                recyclerView2.setLayoutManager(new GridLayoutManager(MasterControl.this, 2));

                SeleccionCuentaAdaptadorItem2 cuentaAdaptadorItem2 = new SeleccionCuentaAdaptadorItem2(items2, MasterControl.this);
                recyclerView2.setAdapter(cuentaAdaptadorItem2);
                cuentaAdaptadorItem2.setOnClickListener((view, obj, position) -> {
                    selectedItem = obj;
                    listener.confirm(InputManager.Style.COMMONINPUT);
                    deleteTimer();
                    //Aqui va lo que se realiza al presionar los botones del segundo recycler
                });
            }
            counterDownTimer(timeout, MSG_TIMEOUT);
        });
    }

    @Override
    public void showChangePwdView(final int timeout, final String title, final int min, final int max, OnUserResultListener l) {
        this.listener = l;

        runOnUiThread(() -> {
            setContentView(R.layout.cambio_clave);

            keyboardNum = findViewById(R.id.keyboardNumerico);
            LinearLayout linearClaveActual = findViewById(R.id.linearClaveActual);
            etTitle = findViewById(R.id.title_toolbar);
            etInputuser = findViewById(R.id.nuevaContra);
            EditText txtPwd2 = findViewById(R.id.nuevaContra2);
            btnAcceptInputUser = findViewById(R.id.continuar);

            linearClaveActual.setVisibility(View.GONE);

            deleteTimer();

            try {
                etTitle.setText(title);
            } catch (Exception e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_ERROR + e);
                etTitle.setText("");
            }

            minEtInputuser = min;
            maxEtInputuser = max;

            etInputuser.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});
            txtPwd2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});

            counterDownTimer(timeout, MSG_TIMEOUT);
            setKeyboard(etInputuser,max,false,true, false, minEtInputuser,keyboardNum,null);

            etInputuser.setBackgroundResource(R.drawable.btn_spinner_first);
            txtPwd2.setBackgroundResource(R.drawable.btn_spinner_select);

            btnAcceptInputUser.setOnClickListener(v -> {
                if (btnAcceptInputUser.getText().equals("Aceptar")){
                    if (etInputuser.getText() !=null && txtPwd2.getText() !=null && etInputuser.getText().toString().equals(txtPwd2.getText().toString())) {
                            inputContent = txtPwd2.getText().toString();
                            listener.confirm(InputManager.Style.COMMONINPUT);
                    }else {
                        Toast.makeText(MasterControl.this, getString(R.string.msgNoconinciden), Toast.LENGTH_SHORT).show();
                        etInputuser.requestFocus();
                        setKeyboard(etInputuser,max,false,true,false,minEtInputuser,keyboardNum,null);
                        etInputuser.setBackgroundResource(R.drawable.btn_spinner_first);
                        txtPwd2.setBackgroundResource(R.drawable.btn_spinner_select);
                        btnAcceptInputUser.setText(getString(R.string.continuar));
                        btnAcceptInputUser.setEnabled(false);
                        etInputuser.setText("");
                        txtPwd2.setText("");
                    }
                }else {
                    if(etInputuser.getText() != null){
                        if(etInputuser.getText().length()==6){
                            txtPwd2.requestFocus();
                            setKeyboard(txtPwd2, max,false,true, false, minEtInputuser,keyboardNum,null);
                            txtPwd2.setBackgroundResource(R.drawable.btn_spinner_first);
                            etInputuser.setBackgroundResource(R.drawable.btn_spinner_select);
                            btnAcceptInputUser.setText(getString(R.string.aceptar));
                        }
                    }else {
                            Toast.makeText(MasterControl.this, getResources().getString(R.string.camposRequeridos), Toast.LENGTH_LONG).show();
                    }
                }
            });

        });
    }

    @Override
    public void showAlertBCP(final String transEname, final String msg1, final String msg2,String msg3, OnUserResultListener l) {
        this.listener = l;
        runOnUiThread(() -> {
            setContentView(R.layout.handling_bcp);

            deleteTimer();

            LinearLayout linearLayout = findViewById(R.id.procesandoTrans);
            linearLayout.setVisibility(View.GONE);

            final RelativeLayout relativeLayout = findViewById(R.id.msgAccount);
            relativeLayout.setVisibility(View.VISIBLE);

            TextView tvMsg1 = findViewById(R.id.msg1Screen);
            TextView tvMsg2 = findViewById(R.id.msg2Screen);
            TextView tvMsg3 = findViewById(R.id.msg3Screen);

            Typeface typeface2;
            Typeface typeface3;

            typeface2 = ResourcesCompat.getFont(getApplicationContext(), R.font.flexo_bold);
            typeface3 = ResourcesCompat.getFont(getApplicationContext(), R.font.flexo_medium);

            tvMsg1.setText(msg1);
            tvMsg2.setText(msg2);
            tvMsg3.setText(msg3);

            Button btnEntentido = findViewById(R.id.entendidoMsg);
            Button btnSalir = findViewById(R.id.exitMsg);

            if (transEname != null && transEname.equals(Trans.ULT_OPERACIONES)){
                TextView tittle = findViewById(R.id.tittleMsgScreen);
                tittle.setText(R.string.titUltiOperaciones);
                btnEntentido.setText(R.string.continuar);
                tvMsg1.setTypeface(typeface2);
                tvMsg2.setTypeface(typeface3);
                btnSalir.setVisibility(View.VISIBLE);

                if (tvMsg1.getText().equals("No realizaste operaciones")){
                    btnEntentido.setVisibility(View.GONE);
                }else {
                    btnSalir.setVisibility(View.VISIBLE);
                }
            }

            relativeLayout.setOnClickListener(MasterControl.this);

            btnEntentido.setOnClickListener(v -> {
                listener.confirm(InputManager.Style.COMMONINPUT);
                deleteTimer();
            });

            btnSalir.setOnClickListener(v -> {
                listener.cancel();
                deleteTimer();
            });

            counterDownTimer(30000, MSG_TIMEOUT);

        });
    }

    @Override
    public void setKeyboard(EditText etTxt, int lenMax, boolean activityAmount, boolean activityPwd,boolean isAlfa, int minLen, RelativeLayout keyboardNumeric ,RelativeLayout keyboardAlfa){
        keyboards = new ClickKeyboard(etTxt, MasterControl.this, isAlfa,countDownTimer,keyboardNumeric,keyboardAlfa );
        keyboards.setActivityPwd(activityPwd);
        keyboards.setActivityMonto(activityAmount);
        keyboards.setLengthMax(lenMax,minLen);
    }

    @Override
    public ClickKeyboard getKeyboards() {
        return keyboards;
    }

    @Override
    public void showQRCView(int timeout, InputManager.Style mode) {
        runOnUiThread(() -> setContentView(R.layout.layout_scan_qrc));
    }

    private void initList(final ArrayList<String> listMenu) {
        final ListView listview = findViewById(R.id.simpleListView);
        ArrayList<String> list;
        list = listMenu;
        list.add("");

        final StableArrayAdapter adapter = new StableArrayAdapter(MasterControl.this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener((parent, view, position, id) -> {
            final String item = String.valueOf(parent.getItemAtPosition(position));
            view.animate().setDuration(500).alpha(0)
                    .withEndAction(() -> {

                        if (!item.equals("")) {
                            inputContent = item;
                            listener.confirm(InputManager.Style.COMMONINPUT);
                        }

                    });
        });
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<>();

        StableArrayAdapter(Context context, int textViewResourceId,
                           List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }


    private void startTrans(String type) {
        try {
            if ((batteryStatus.getLevelBattery() <= 8) && (!batteryStatus.isCharging()) && (!type.equals(Trans.INIT_P))) {
                UIUtils.toast(MasterControl.this, R.drawable.ic_lg_light, Definesbcp.MSG_BATTERY, Toast.LENGTH_SHORT, new int[]{Gravity.CENTER, 0, 0});
                finish();
            } else if (paperStatus.getRet() == Printer.PRINTER_STATUS_PAPER_LACK && (!type.equals(Trans.INIT_P))) {
                UIUtils.toast(MasterControl.this, R.drawable.ic_lg_light, Definesbcp.MSG_PAPER, Toast.LENGTH_SHORT, new int[]{Gravity.CENTER, 0, 0});
                finish();
            } else {
                PaySdk.getInstance().startTrans(type, this);
            }
        } catch (PaySdkException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "Exception" + e.toString());
        }
    }

    private void showConfirmCardNO(String pan) {
        btnConfirm = findViewById(R.id.cardno_confirm);
        btnCancel = findViewById(R.id.cardno_cancel);
        editCardNO = findViewById(R.id.cardno_display_area);
        ImageView iv = findViewById(R.id.trans_cardno_iv);
        iv.setImageBitmap(PAYUtils.getLogoByBankId(this, TMConfig.getInstance().getBankid()));
        btnCancel.setOnClickListener(MasterControl.this);
        btnConfirm.setOnClickListener(MasterControl.this);
        editCardNO.setText(pan);
    }

    private void hideKeyBoard(IBinder windowToken) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(windowToken, 0);
    }

    public static void hideKeyBoard(IBinder windowToken, Context ctx) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(windowToken, 0);
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }

    /**
     * Check card exist in table.
     *
     * @param cardNum Numero Tarjeta
     * @return return
     */
    public static boolean incardTable(String cardNum) {

        if (cardNum == null)
            return false;

        if (cardNum.length() < 10)
            return false;

        if (!Rango.inCardTableACQ(cardNum, rango, variables.getMcontext())) {
            Logger.debug("No se encontraron parametros");
            return false;
        }

        return true;
    }

    /**
     * se agrega booleano para la pantalla de imprimir copia no utilice
     * el Resultcontrol y finalice la actividad
     */
    @Override
    public void counterDownTimer(int timeout, final String mensaje) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        countDownTimer = new CountDownTimer(timeout, 1000) {
            public void onTick(long millisUntilFinished) {
                if (activeDebugLocal)
                    Log.d("onTick", "init onTick countDownTimer " + mensaje + " " + millisUntilFinished);
            }

            public void onFinish() {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                if (listener != null)
                    listener.cancel(TcodeError.T_ERR_TIMEOUT_IN_DATA);
            }
        }.start();
    }

    @Override
    public CountDownTimer getTimer() {
        return countDownTimer;
    }

    @Override
    public void deleteTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    public boolean longMin(EditText etInputuser){

        if (keyboards.getLengthMin() > 0 )
            minEtInputuser = keyboards.getLengthMin();
        if (!etInputuser.getText().toString().equals("")){
            if (etInputuser.length() >= minEtInputuser) {
                toast.cancel();
                inputContent = etInputuser.getText().toString();
                deleteTimer();
                return false;
            } else {
                toast(MasterControl.this, R.drawable.ic_lg_light, getString(R.string.minCaracteres), Toast.LENGTH_SHORT, new int[]{Gravity.CENTER, 0, 0});
            }
        }else {
            UIUtils.toast(MasterControl.this, R.drawable.ic_lg_light, getString(R.string.ingrese_dato), Toast.LENGTH_SHORT, new int[]{Gravity.CENTER, 0, 0});
        }
        return true;
    }

    private void infoAccountClient(String msg1, String msg2, String msg3){
        final RelativeLayout relativeLayout = findViewById(R.id.msgAccount);
        relativeLayout.setVisibility(View.VISIBLE);

        Typeface typeFaceBold = ResourcesCompat.getFont(getApplicationContext(),R.font.flexo_bold);
        Typeface typeFace = ResourcesCompat.getFont(getApplicationContext(),R.font.flexo_medium);

        TextView tvMsg1 = findViewById(R.id.msg1Screen);
        TextView tvMsg2 = findViewById(R.id.msg2Screen);
        TextView tvMsg3 = findViewById(R.id.msg3Screen);

        //CENTRAR CONTENIDO
        tvMsg1.setGravity(Gravity.CENTER);
        tvMsg2.setGravity(Gravity.CENTER);
        tvMsg3.setGravity(Gravity.CENTER);

        //contenido de pantalla
        tvMsg1.setText(msg1);//primer mensaje sin negrilla
        tvMsg2.setText(msg2);//segundo mensaje con negrilla
        tvMsg3.setText(msg3);//tercer mensaje sin negrilla

        //ajuste de fuente
        tvMsg1.setTypeface(typeFaceBold);
        tvMsg2.setTypeface(typeFace);

        //botones
        Button btnEntentido = findViewById(R.id.entendidoMsg);

        btnEntentido.setText(getResources().getString(R.string.msgAccountClientBtn));

        relativeLayout.setOnClickListener(MasterControl.this);

        btnEntentido.setOnClickListener(view -> {
            relativeLayout.setVisibility(View.GONE);
            volver.setVisibility(View.VISIBLE);
        });
    }

    public void toast(Activity activity, int ico, String str, int duration, int[] dirToast) {
        LayoutInflater inflater_3 = activity.getLayoutInflater();
        View view3 = inflater_3.inflate(R.layout.toast_transaparente,
                activity.findViewById(R.id.toast_layoutTransaparent));
        toast.setGravity(dirToast[0], dirToast[1], dirToast[2]);
        toast.setDuration(duration);
        toast.setView(view3);
        ((TextView) view3.findViewById(R.id.toastMessageDoc)).setText(str);
        toast.show();
    }

    @Override
    public void showLastOperations(String contentLast, String contentThree, OnUserResultListener l) {

        runOnUiThread(() -> {
            setContentView(R.layout.reporteultimasoperaciones);

            deleteTimer();

            TextView lastop = findViewById(R.id.lastOp);
            Button btn3Ultimas = findViewById(R.id.btnUltimas);
            Button btnUltima = findViewById(R.id.btnUltima);
            volver = findViewById(R.id.back);
            volver.setVisibility(View.VISIBLE);
            TextView tittle = findViewById(R.id.title_toolbar);


            tittle.setText(R.string.titUltiOperaciones);

            int offset = 0;
            int length = 40;
            String temp = "";

            if (contentLast == null || contentThree != null){

                for(int i =contentThree.length()-1 ; i>0 ; i = i-length){
                    if(i > length){
                        temp = temp + contentThree.substring(offset,offset+length)+ "\n";
                    }else {
                        temp = temp + contentThree.substring(offset)+ "\n";
                    }
                    offset+=length;
                    lastop.setText(temp);
                }

            }else {

                for(int i =contentLast.length()-1 ; i>0 ; i = i-length){
                    if(i > length){
                        temp = temp + contentLast.substring(offset,offset+length)+ "\n";
                    }else {
                        temp = temp + contentLast.substring(offset)+ "\n";
                    }
                    offset+=length;
                    lastop.setText(temp);
                }
            }

            lastop.setText(temp);

            volver.setOnClickListener(v -> listener.back());

            btn3Ultimas.setOnClickListener(v -> {
                listener.confirm(InputManager.Style.COMMONINPUT);
                countDownTimer.cancel();
            });

            btnUltima.setOnClickListener(v -> {
                listener.cancel();
                countDownTimer.cancel();
            });

            counterDownTimer(30000, MSG_TIMEOUT);

        });
    }
}
