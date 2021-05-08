package com.bcp.transactions.giros.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.android.desert.keyboard.InputManager;
import com.android.newpos.pay.R;
import com.bcp.document.ClassArguments;
import com.bcp.document.MsgScreenDocument;
import com.bcp.rest.giros.cobro_nacional.request.ReqVerifyBank;
import com.bcp.rest.giros.cobro_nacional.response.PaymentOrders;
import com.bcp.rest.giros.cobro_nacional.response.RspVerifyBank;
import com.bcp.rest.httpclient.RequestWs;
import com.bcp.teclado_alfanumerico.ClickKeyboardFragment;
import com.bcp.teclado_alfanumerico.ClickSpinner;
import com.bcp.transactions.callbacks.WaitTransFragment;
import com.newpos.libpay.Logger;
import com.newpos.libpay.presenter.TransView;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.Trans;

import org.json.JSONException;
import org.json.JSONObject;

import cn.desert.newpos.payui.UIUtils;

import static com.bcp.rest.JsonUtil.ROOT;
import static com.bcp.rest.JsonUtil.TIMEOUT;


public class FragmentBeneficiaryData extends Fragment {

    //Toolbar
    Button btnContinuar;
    TextView tvInputDocument;
    TextView btnAcceptInputUser;
    EditText spinnerOrdenBen;
    ImageButton btnConti;
    ImageButton btnTipOrden;
    ImageButton btnTipoDoc;
    LinearLayout linearLayoutProgress;
    LinearLayout ordenBenef;
    LottieAnimationView gifProgressBen;
    LinearLayout linearBotones;
    LinearLayout linearDoc;
    RelativeLayout relativeTipOrden;
    TextView etTitle;
    TextView selectBenDocument;
    TextView selectOrden;

    EditText tipoDoc;
    EditText numberDoc;
    ImageView volver;
    private ClickKeyboardFragment keyboards;
    Toast toast;

    //Toolbar
    ImageView close;

    private RelativeLayout relativeKeyboardNume;
    private RelativeLayout relativeKeyboardAlfa;
    ClassArguments classArguments;
    Activity activity;
    View vista;
    boolean clicks = false;
    private int conteoClicks = 0;
    int minEtInputuser;

    String[] arrayOrden;
    String[] arrayTipoDoc;
    ClickSpinner clickSpinner;
    Context thiscontext;
    PaymentOrders[] paymentOrders;
    FrameLayout frameLayout;

    WaitTransFragment waitTransFragment;

    TransView transView;
    MsgScreenDocument msgScreenDocument;

    //jsonObject
    RspVerifyBank rspVerifyBank;
    private static final String MSGSCREENTIMER = "FragmentBeneficiaryData";

    public FragmentBeneficiaryData(){
        //Constructor de la clase
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = getActivity();
        this.thiscontext = context;
        this.activity = getActivity();
        this.transView = (TransView)getActivity();
        this.msgScreenDocument = transView.getMsgScreendocument();
    }

    @Override
    public void onDetach() {
        clearVar();
        super.onDetach();
    }

    private void clearVar() {
        //Toolbar
         btnContinuar = null;
         tvInputDocument = null;
         btnAcceptInputUser = null;
         spinnerOrdenBen = null;
         btnConti = null;
         btnTipOrden = null;
         btnTipoDoc = null;
         linearLayoutProgress = null;
         ordenBenef = null;
         gifProgressBen = null;
         linearBotones = null;
         linearDoc = null;
         relativeTipOrden = null;
         etTitle = null;
         selectBenDocument = null;
         selectOrden = null;

         tipoDoc = null;
         numberDoc = null;
         volver = null;
         keyboards = null;
         toast = null;

        //Toolbar
         close = null;

          relativeKeyboardNume = null;
          relativeKeyboardAlfa = null;
         classArguments = null;
         activity = null;
         vista = null;
         clicks = false;
         conteoClicks = 0;
         minEtInputuser = 0;

         arrayOrden = null;
         arrayTipoDoc = null;
         clickSpinner = null;
         thiscontext = null;
         paymentOrders = null;
         frameLayout = null;

         waitTransFragment = null;

         transView = null;
         msgScreenDocument = null;

        //jsonObject
         rspVerifyBank = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista =  inflater.inflate(R.layout.fragment_datos_beneficiario, container, false);

        frameLayout = vista.findViewById(R.id.fragmentDB);

        btnContinuar          = vista.findViewById(R.id.Btncontinuar);
        numberDoc             = vista.findViewById(R.id.NumeroDocument);
        tvInputDocument       = vista.findViewById(R.id.doc_numero);
        btnAcceptInputUser    = vista.findViewById(R.id.continuar);
        btnConti              = vista.findViewById(R.id.continuarAlfa);
        relativeKeyboardNume  = vista.findViewById(R.id.keyboardNumerico);
        relativeKeyboardAlfa  = vista.findViewById(R.id.keyboardAlfa);
        gifProgressBen        = vista.findViewById(R.id.gifProcesandoBen);
        linearLayoutProgress  = vista.findViewById(R.id.procesandoTransBen);
        ordenBenef            = vista.findViewById(R.id.linearOrdenPago);
        spinnerOrdenBen       = vista.findViewById(R.id.TxtSpinnerBen);
        btnTipOrden           = vista.findViewById(R.id.TipoOrdenGiro);
        linearBotones         = vista.findViewById(R.id.linearBotonesGiros);
        relativeTipOrden      = vista.findViewById(R.id.relativeTipOrden);
        tipoDoc               = vista.findViewById(R.id.textSpinnerdoc);
        volver                = vista.findViewById(R.id.back);
        etTitle               = vista.findViewById(R.id.title_toolbar);
        btnTipoDoc            = vista.findViewById(R.id.tipDocumt);
        close                 = vista.findViewById(R.id.iv_close);
        linearDoc             = vista.findViewById(R.id.linearDocument);
        selectBenDocument     = vista.findViewById(R.id.selectBeneficiary);
        selectOrden           = vista.findViewById(R.id.txtNomApellBenef);
        arrayTipoDoc          = getResources().getStringArray(R.array.typeDocumen);

        numberDoc.setBackgroundResource(R.drawable.btn_spinner_first);


        close.setVisibility(View.VISIBLE);
        volver.setVisibility(View.VISIBLE);

        etTitle.setText(getResources().getString(R.string.datosBeneficiario));
        minEtInputuser = ((TransView)activity).getMsgScreendocument().getMinLength();

        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transView.deleteTimer();

        transView.counterDownTimer(msgScreenDocument.getTimeOut(), MSGSCREENTIMER);

        classArguments = transView.getArguments();

        toast = new Toast(getContext());

        waitTransFragment = msgScreenDocument.getCallbackTransFragment();

        btnAcceptInputUser.setOnClickListener(v -> {
            if(classArguments.getTypetransaction() == 2 && classArguments.getTypepayment().equals("2")){
                if(!longMin(numberDoc)){
                    relativeKeyboardNume.setVisibility(View.GONE);
                    numberDoc.setBackgroundResource(R.drawable.btn_spinner_estados);
                    consumoApi();
                }
            }else {
                if(!transView.longMin(numberDoc)){
                    relativeKeyboardNume.setVisibility(View.GONE);
                    numberDoc.setBackgroundResource(R.drawable.btn_spinner_estados);
                    consumoApi();
                }
            }
        });
        btnConti.setOnClickListener(v -> {
            if(classArguments.getTypetransaction() == 2 && classArguments.getTypepayment().equals("2")){
                if(!longMin(numberDoc)){
                    relativeKeyboardAlfa.setVisibility(View.GONE);
                    numberDoc.setBackgroundResource(R.drawable.btn_spinner_estados);
                    consumoApi();
                }
            }else {
                if(!transView.longMin(numberDoc)){
                    relativeKeyboardAlfa.setVisibility(View.GONE);
                    numberDoc.setBackgroundResource(R.drawable.btn_spinner_estados);
                    consumoApi();
                }
            }
        });
        btnContinuar.setOnClickListener(v ->{
            try {
                classArguments.setArgument1(tipoDoc.getText().toString());
                classArguments.setArgument2(numberDoc.getText().toString());
                classArguments.setArgument3(String.valueOf(clickSpinner.getPositionSelected()));
                classArguments.setArgument6(arrayOrden);
                if (rspVerifyBank != null)
                    classArguments.setSessionId(rspVerifyBank.getSesionId());
                if (paymentOrders != null)
                    classArguments.setPaymentOrders(paymentOrders);

                transView.setArgumentsClass(classArguments);

                if(classArguments.getTypetransaction() == 2 && classArguments.getTypepayment().equals("2")){
                    transView.deleteTimer();
                    transView.getListener().confirm(InputManager.Style.COMMONINPUT);

                }else{
                    NavHostFragment.findNavController(FragmentBeneficiaryData.this)
                            .navigate(R.id.action_fragment_datos_beneficiario_to_fragmentPassword);
                }
            }catch (Exception e){
                transView.getListener().cancel(TcodeError.T_ERR_NAVI_FRAGMENT);
                Logger.logLine(Logger.LOG_EXECPTION, "FragmentBeneficiaryData");
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            }


        });

        if(classArguments.getTypetransaction() == 2 && classArguments.getTypepayment().equals("1")){
            volver.setOnClickListener(view1 -> {
                try {
                    transView.deleteTimer();
                    NavHostFragment.findNavController(FragmentBeneficiaryData.this)
                            .navigate(R.id.action_fragment_datos_beneficiario_to_fragmentTypePayment);
                }catch (Exception e){
                    transView.getListener().cancel(TcodeError.T_ERR_RETURN_FRAGMENT);
                    Logger.logLine(Logger.LOG_EXECPTION, "FragmentBeneficiaryData");
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                }

            });
        }else {
            volver.setVisibility(View.GONE);
        }

        close.setOnClickListener(view12 -> {
            transView.deleteTimer();
            transView.getListener().cancel(TcodeError.T_USER_CANCEL);
        });

        if(classArguments.getArgument1() != null){

            tipoDoc.setText(classArguments.getArgument1());
            linearDoc.setVisibility(View.VISIBLE);
            numberDoc.setText(classArguments.getArgument2());
            numberDoc.setBackgroundResource(R.drawable.btn_spinner_estados);
            tipoDoc.setBackgroundResource(R.drawable.btn_spinner_estados);
            btnTipoDoc.setBackgroundResource(R.drawable.ic_flecha_abajo);
            ordenBenef.setVisibility(View.GONE);
            arrayOrden = classArguments.getArgument6();
            spinnerOrdenBen.setText(arrayOrden[Integer.parseInt(classArguments.getArgument3())]);
            btnContinuar.setEnabled(true);
            selectSpinner();

        }else{
            if(classArguments.getTypetransaction() == 2 && classArguments.getTypepayment().equals("2")){
                setKeyboard(numberDoc, transView.getMsgScreendocument().getMaxLength(),false,false, msgScreenDocument.isAlfa(), minEtInputuser,relativeKeyboardNume,relativeKeyboardAlfa);
            }else {
                transView.setKeyboard(numberDoc, msgScreenDocument.getMaxLength(),false,false, msgScreenDocument.isAlfa(), minEtInputuser,relativeKeyboardNume,relativeKeyboardAlfa);
            }

            if (msgScreenDocument.isBanner()){
                clickSpinner = new ClickSpinner(tipoDoc,selectBenDocument,btnTipoDoc,vista.findViewById(R.id.linearSpinnerDoc),vista.findViewById(R.id.relativeTipoDocumet),arrayTipoDoc,(Activity) thiscontext);
                clickSpinner.setDocument(tvInputDocument,linearDoc,relativeKeyboardNume,relativeKeyboardAlfa);
                if(classArguments.getTypetransaction() == 2 && classArguments.getTypepayment().equals("2")){
                    clickSpinner.setKeyboards(getKeyboards());
                }else {
                    clickSpinner.setClickKeyboard(transView.getKeyboards());
                }
                clickSpinner.spinnerBotones(Trans.GIROS_COBROS,"Beneficiario");
            }
        }
    }

    public void selectSpinner(){

        linearLayoutProgress.setVisibility(View.GONE);
        ordenBenef.setVisibility(View.VISIBLE);
        if (classArguments.getTypepayment().equals("1"))
            volver.setVisibility(View.VISIBLE);

        clickSpinner = new ClickSpinner(spinnerOrdenBen,selectOrden,btnTipOrden,linearBotones,relativeTipOrden,arrayOrden,(Activity) thiscontext);
        clickSpinner.spinnerBotones(Trans.GIROS_COBROS,"Beneficiario");

    }

    private void consumoApi(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqObtainPerson ");
        volver.setVisibility(View.GONE);
        linearLayoutProgress.setVisibility(View.VISIBLE);
        gifProgressBen.playAnimation();

        RequestWs requestWs = new RequestWs(getActivity(), ROOT + msgScreenDocument.getListUrl().get(0).getMethod(), TIMEOUT, true);
        requestWs.httpRequets(msgScreenDocument.getHeader(), setBankDraft(), (result, statusCode,header) -> {
            waitTransFragment.setOpnNumber();
            Logger.logLine(Logger.LOG_GENERAL, " Saliendo de reqObtainPerson ");
            if (result != null){
                if (statusCode == 200){
                    try {
                        transView.counterDownTimer(msgScreenDocument.getTimeOut(), MSGSCREENTIMER);
                        rspVerifyBank = new RspVerifyBank();
                        if (rspVerifyBank.getRspObtainOrders(result,header)){

                            waitTransFragment.getSessionId(rspVerifyBank.getSesionId()+"");

                            arrayOrden = new String[rspVerifyBank.getRspOrders().length];

                            paymentOrders = rspVerifyBank.getRspOrders();

                            for(int i = 0 ; i < rspVerifyBank.getRspOrders().length; i++){
                                arrayOrden[i] = rspVerifyBank.getRspOrders()[i].getRspCurrencySymbol() + "  " +rspVerifyBank.getRspOrders()[i].getRspAmount() + " - " + rspVerifyBank.getRspOrders()[i].getRspBeneficiaryName();
                            }
                            selectSpinner();
                            btnTipoDoc.setEnabled(false);
                            selectBenDocument.setEnabled(false);
                        }else {
                            fillScreenOrders();
                        }
                    } catch (JSONException e) {
                        Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                    }
                }else {
                    transView.getListener().cancel(result,header,statusCode);
                }
            }else {
                transView.deleteTimer();
                transView.getListener().cancel(UIUtils.validateResponseError(statusCode));
            }
        });
    }

    private JSONObject setBankDraft(){
        final ReqVerifyBank reqVerifyBank = new ReqVerifyBank();

        reqVerifyBank.setCtnDocumentType(documentType(tipoDoc.getText().toString()));
        reqVerifyBank.setCtnDocumentNumber(numberDoc.getText().toString());

        return reqVerifyBank.buildsObjectJSON();
    }

    private void fillScreenOrders(){
        final RelativeLayout relativeLayout = vista.findViewById(R.id.msgAccount);
        relativeLayout.setVisibility(View.VISIBLE);

        Typeface typeFaceBold = ResourcesCompat.getFont(activity.getApplicationContext(),R.font.flexo_bold);
        Typeface typeFace = ResourcesCompat.getFont(activity.getApplicationContext(),R.font.flexo_medium);

        TextView tvMsg1 = vista.findViewById(R.id.msg1Screen);
        TextView tvMsg2 = vista.findViewById(R.id.msg2Screen);
        TextView tvMsg3 = vista.findViewById(R.id.msg3Screen);

        //CENTRAR CONTENIDO
        tvMsg1.setGravity(Gravity.CENTER);
        tvMsg2.setGravity(Gravity.CENTER);
        tvMsg3.setGravity(Gravity.CENTER);

        //contenido de pantalla
        tvMsg1.setText(getResources().getString(R.string.msg1Giros));
        tvMsg2.setText(getResources().getString(R.string.msg5Giros));
        tvMsg3.setText(getResources().getString(R.string.msg2Giros));

        //ajuste de fuente
        tvMsg1.setTypeface(typeFaceBold);
        tvMsg2.setTypeface(typeFace);

        //botones
        Button btnEntentido = vista.findViewById(R.id.entendidoMsg);
        Button btnSalir = vista.findViewById(R.id.exitMsg);

        btnSalir.setVisibility(View.VISIBLE);

        btnEntentido.setText(getResources().getString(R.string.changeDocument));
        btnSalir.setText(getResources().getString(R.string.msgAccountClientBtnExit));

        relativeLayout.setOnClickListener(v -> {
            //accion al presionar el relative
        });
        if(!clicks){
            if(conteoClicks <= 1){
                btnEntentido.setOnClickListener(v -> {
                    //accion al presionar el boton cambiar documento
                    relativeLayout.setVisibility(View.GONE);
                    linearLayoutProgress.setVisibility(View.GONE);
                    gifProgressBen.pauseAnimation();
                    relativeKeyboardNume.setVisibility(View.GONE);
                    relativeKeyboardAlfa.setVisibility(View.GONE);
                    transView.setKeyboard(numberDoc,msgScreenDocument.getMaxLength(),false,false,(!tipoDoc.getText().toString().equals("DNI")),8,relativeKeyboardNume,relativeKeyboardAlfa);
                });
                conteoClicks++;
            } else {
                relativeLayout.setVisibility(View.GONE);
                transView.showError("Sesión finalizada","xxx00","No te quedan mas intentos de ingreso de documento");
            }
        }

        btnSalir.setOnClickListener(v ->{
            transView.deleteTimer();
            //accion al presionar el boton salir
            transView.getListener().cancel();
        });
    }

    private String documentType(String documentSelect){
        String type;
        switch (documentSelect){
            case "DNI":
                type = "1";
                break;
            case "Carnet de Extranjería":
            case "Carnet de extranjería":
                type = "3";
                break;
            case "Pasaporte":
                type = "4";
                break;
            case "RUC":
                type = "6";
                break;
            default:
                type = "";
                break;
        }

        return type;
    }

    /*
     *se crea nuevo click keyboard para el manejo del teclado solo para fragments con el envio del view y no la actividad
     */
    public void setKeyboard(EditText etTxt, int lenMax, boolean activityAmount, boolean activityPwd, boolean isAlfa, int minLen, RelativeLayout keyboardNumeric , RelativeLayout keyboardAlfa){
        keyboards = new ClickKeyboardFragment(etTxt, vista, isAlfa,transView.getTimer(),keyboardNumeric,keyboardAlfa );
        keyboards.setActivityPwd(activityPwd);
        keyboards.setActivityMonto(activityAmount);
        keyboards.setLengthMax(lenMax,minLen);
    }

    public ClickKeyboardFragment getKeyboards() {
        return keyboards;
    }

    public boolean longMin(EditText etInputuser){
        if (keyboards.getLengthMin() > 0 )
            minEtInputuser = keyboards.getLengthMin();
        if (!etInputuser.getText().toString().equals("")){
            if (etInputuser.length() >= minEtInputuser) {
                toast.cancel();
                return false;
            } else {
                toast(getActivity(), R.drawable.ic_lg_light, getString(R.string.minCaracteres), Toast.LENGTH_SHORT, new int[]{Gravity.CENTER, 0, 0});
            }
        }else {
            toast(getActivity(), R.drawable.ic_lg_light, getString(R.string.ingrese_dato), Toast.LENGTH_SHORT, new int[]{Gravity.CENTER, 0, 0});
        }
        return true;
    }

    public void toast(Activity activity, int ico, String str, int duration, int[] dirToast) {
        LayoutInflater inflater_3 = activity.getLayoutInflater();
        View view3 = inflater_3.inflate(R.layout.toast_transaparente,
                (ViewGroup) activity.findViewById(R.id.toast_layoutTransaparent));
        toast.setGravity(dirToast[0], dirToast[1], dirToast[2]);
        toast.setDuration(duration);
        toast.setView(view3);
        ((TextView) view3.findViewById(R.id.toastMessageDoc)).setText(str);
        toast.show();
    }
}