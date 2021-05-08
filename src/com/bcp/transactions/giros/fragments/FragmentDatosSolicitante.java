package com.bcp.transactions.giros.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.android.newpos.pay.R;
import com.bcp.document.ClassArguments;
import com.bcp.document.MsgScreenDocument;
import com.bcp.rest.giros.emision.request.ReqObtainPersonEmision;
import com.bcp.rest.giros.emision.response.RspObtainPersonEmision;
import com.bcp.rest.httpclient.RequestWs;
import com.bcp.teclado_alfanumerico.ClickSpinner;
import com.bcp.transactions.callbacks.WaitTransFragment;
import com.bcp.transactions.callbacks.WaitTypeDocument;
import com.newpos.libpay.Logger;
import com.newpos.libpay.presenter.TransView;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.Trans;

import org.json.JSONException;
import org.json.JSONObject;

import cn.desert.newpos.payui.UIUtils;

import static com.bcp.inicializacion.tools.PolarisUtil.IGUALORDENANTENO;
import static com.bcp.inicializacion.tools.PolarisUtil.IGUALORDENANTESI;
import static com.bcp.rest.JsonUtil.ROOT;
import static com.bcp.rest.JsonUtil.TIMEOUT;


public class FragmentDatosSolicitante extends Fragment  {

    View vista;

    //DATOS SOLICITANTE
    EditText tipoDocSolici;
    EditText numDocSolicitante;
    EditText tipoDatoOrdenante;
    EditText txtNumDniOrdenante;
    EditText txtNombreApeOrdenan;
    TextView numDniOrde;
    EditText nombreApellido;
    TextView nomApellOrdenante;
    TextView txtNombreApellido;
    TextView txtNumDoc;
    TextView txtDniSolci;
    TextView txtselectOrd;
    ImageButton btnTipDoc;
    ImageButton tipoDocOrdenante;
    TextView btnAcceptInputUser;
    ImageButton btnConti;
    Button btnContinuar;
    RadioGroup radTipoSeleccion;
    String type;

    //LINEAR
    LinearLayout linNumDocSolci;
    LinearLayout linProgress;
    LinearLayout linNombApellSolic;
    LinearLayout linDocumentType;
    LinearLayout linDatosOrdenante;
    LinearLayout linTypeClient;
    LinearLayout linDNIOrdenante;
    LinearLayout linNomOrdenante;
    LinearLayout linBtnContinuar;
    LinearLayout linProgressOrde;
    RelativeLayout relativNumDoc;

    //TOOLBAR
    TextView tittle;
    ImageView close;
    ImageView volver;
    LottieAnimationView imgprogress;
    LottieAnimationView imgprogressOrde;
    RadioButton radSi;
    RadioButton radNo;
    LinearLayout  linScrollView;

    //ARRAY
    String[] arrayDocumt;
    String name;

    int minNumDocument;
    Boolean typeClient = true;
    private Typeface typeface3;
    private boolean clientRemitter;
    private boolean clientSender;
    private boolean spinnerDocumetn = false;
    public static Toast toast;

    //ACTIVITY Y CONTEX
    Activity activity;
    Context thisContext;
    ClickSpinner clickSpinner;
    ClassArguments classArguments;
    RspObtainPersonEmision rspObtainPersonEmision;
    protected WaitTypeDocument callbackTypeDocument;

    //RELATIVE TECLADO
    private RelativeLayout relativeKeyboardNum;
    private RelativeLayout relativeKeyboardAlfa;
    WaitTransFragment waitTransFragment;
    int minEtInputuser;
    int maxEtInputuser;
    private String msgScreenTimer = "FragmentDatosSolicitante";

    TransView transView;
    MsgScreenDocument msgScreenDocument;

    public FragmentDatosSolicitante(){
        //Constructor de la clase
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.thisContext = context;
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
        vista = null;

        //DATOS SOLICITANTE
        tipoDocSolici = null;;
        numDocSolicitante = null;;
        tipoDatoOrdenante = null;
        txtNumDniOrdenante = null;
        txtNombreApeOrdenan = null;
        numDniOrde = null;
        nombreApellido = null;
        nomApellOrdenante = null;
        txtNombreApellido = null;
        txtNumDoc = null;
        txtDniSolci = null;
        txtselectOrd = null;
        btnTipDoc = null;
        tipoDocOrdenante = null;
        btnAcceptInputUser = null;
        btnConti = null;
        btnContinuar = null;
        radTipoSeleccion = null;
        type = null;

        //LINEAR
        linNumDocSolci = null;
        linProgress = null;
        linNombApellSolic = null;
        linDocumentType = null;
        linDatosOrdenante = null;
        linTypeClient = null;
        linDNIOrdenante = null;
        linNomOrdenante = null;
        linBtnContinuar = null;
        linProgressOrde = null;
        relativNumDoc = null;

        //TOOLBAR
        tittle = null;
        close = null;
        volver = null;
        imgprogress = null;
        imgprogressOrde = null;
        radSi = null;
        radNo = null;
        linScrollView = null;

        //ARRAY
        arrayDocumt = null;
        name = null;

        minNumDocument = 0;
        typeClient = null;
        typeface3 = null;
        clientRemitter = false;
        clientSender = false;
        spinnerDocumetn = false;
         toast = null;

        //ACTIVITY Y CONTEX
        activity = null;
        thisContext = null;
        clickSpinner = null;
        classArguments = null;
        rspObtainPersonEmision = null;
        callbackTypeDocument = null;

        //RELATIVE TECLADO
        relativeKeyboardNum = null;
        relativeKeyboardAlfa = null;
        waitTransFragment = null;
        minEtInputuser = 0;
        maxEtInputuser = 0;

        transView = null;
        msgScreenDocument = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_datos_solicitante, container, false);

        tipoDocSolici         = vista.findViewById(R.id.TxtTipoDoc);
        numDocSolicitante     = vista.findViewById(R.id.TxtNumDoc);
        linNumDocSolci        = vista.findViewById(R.id.linNumDocSolic);
        tittle                = vista.findViewById(R.id.title_toolbar);
        close                 = vista.findViewById(R.id.iv_close);
        volver                = vista.findViewById(R.id.back);
        linProgress           = vista.findViewById(R.id.processTransSolic);
        relativeKeyboardNum   = vista.findViewById(R.id.keyboardNumerico);
        relativeKeyboardAlfa  = vista.findViewById(R.id.keyboardAlfa);
        linNombApellSolic     = vista.findViewById(R.id.linNomApellSolic);
        arrayDocumt           = getResources().getStringArray(R.array.typeDocumenEmision);
        btnTipDoc             = vista.findViewById(R.id.TipoDocum);
        relativNumDoc         = vista.findViewById(R.id.relativeTipoDocumet);
        txtNumDoc             = vista.findViewById(R.id.NumDni);
        linDocumentType       = vista.findViewById(R.id.linSpinnerDoc);
        btnAcceptInputUser    = vista.findViewById(R.id.continuar);
        btnConti              = vista.findViewById(R.id.continuarAlfa);
        btnContinuar          = vista.findViewById(R.id.BtncontinuarSolic);
        imgprogress           = vista.findViewById(R.id.gifProcesandoSolic);
        radSi                 = vista.findViewById(R.id.SelectSi);
        radNo                 = vista.findViewById(R.id.SelectNo);
        txtNombreApellido     = vista.findViewById(R.id.txtNomApell);
        nombreApellido        = vista.findViewById(R.id.TxtNombAp);//SOLICITANTE
        linDatosOrdenante     = vista.findViewById(R.id.lindatosOrdenante);
        radTipoSeleccion      = vista.findViewById(R.id.radioGroupTipo);
        tipoDatoOrdenante     = vista.findViewById(R.id.TxtTipoDocOrd);
        tipoDocOrdenante      = vista.findViewById(R.id.TipoDocumOrd);
        txtNumDniOrdenante    = vista.findViewById(R.id.NumDNIOdenante);
        nomApellOrdenante     = vista.findViewById(R.id.txtNomApellOdenante);
        txtNombreApeOrdenan   = vista.findViewById(R.id.NomApellOrde);//ORDENANTE
        numDniOrde            = vista.findViewById(R.id.NumDINOrden);
        linDNIOrdenante       = vista.findViewById(R.id.linDNIOrden);
        linBtnContinuar       = vista.findViewById(R.id.btnSpinner);
        linNomOrdenante       = vista.findViewById(R.id.linearNombOrdenante);
        linTypeClient         = vista.findViewById(R.id.linearTipClient);
        linProgressOrde       = vista.findViewById(R.id.processTransOrden);
        imgprogressOrde       = vista.findViewById(R.id.gifProcesandoSOrden);
        linScrollView         = vista.findViewById(R.id.linearScroll);
        txtDniSolci           = vista.findViewById(R.id.txtDniSolci);
        txtselectOrd           = vista.findViewById(R.id.selectDniOrd);

        numDocSolicitante.setBackgroundResource(R.drawable.btn_spinner_first);

        close.setVisibility(View.VISIBLE);
        volver.setVisibility(View.VISIBLE);

        tittle.setText(getResources().getString(R.string.tittleEmision));
        minNumDocument = msgScreenDocument.getMinLength();

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        typeface3 = ResourcesCompat.getFont(activity.getApplicationContext(), R.font.flexo_medium);

       toast = new Toast(getActivity());

        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transView.deleteTimer();

        (transView).counterDownTimer(msgScreenDocument.getTimeOut(), msgScreenTimer);

         classArguments = (transView).getArguments();

        //se instancia el callback para obtener el sesionId y el header
        waitTransFragment = msgScreenDocument.getCallbackTransFragment();

        callbackTypeDocument = new WaitTypeDocument() {
            @Override
            public void getTypeDocument() {
                fillScreenOrders();
            }

            @Override
            public void cleanData(String typeSpinner) {
                if (typeSpinner.equals("Solicitante")){
                    //SOLICITANTE
                    nombreApellido.setText("");
                    linNombApellSolic.setVisibility(View.GONE);
                    radTipoSeleccion.check(R.id.SelectSi);
                    linTypeClient.setVisibility(View.GONE);
                    //ORDENANTE
                    linDatosOrdenante.setVisibility(View.GONE);
                    linDNIOrdenante.setVisibility(View.GONE);
                    linNomOrdenante.setVisibility(View.GONE);
                    tipoDatoOrdenante.setText("");
                    txtNombreApeOrdenan.setText("");
                    txtNumDniOrdenante.setText("");
                    txtNombreApellido.setEnabled(true);
                    validateLongMaxMin(tipoDocSolici.getText().toString());
                    (transView).setKeyboard(numDocSolicitante, maxEtInputuser,false,false, (!tipoDocSolici.getText().toString().equals("DNI") && !tipoDocSolici.getText().toString().equals("RUC")), minEtInputuser,relativeKeyboardNum,relativeKeyboardAlfa);
                    typeClient = true;
                    clientSender = false;
                }else {
                    linNomOrdenante.setVisibility(View.GONE);
                    txtNombreApeOrdenan.setText("");
                    clientRemitter = false;
                }
                btnContinuar.setEnabled(false);
            }
        };

        datosSolicitante();

        btnAcceptInputUser.setOnClickListener(v -> {
            if(!nombreApellido.getText().toString().equals("")){
                if((classArguments.getTypeclient() == IGUALORDENANTESI)){
                    relativeKeyboardNum.setVisibility(View.GONE);
                    typeClient= false;
                    consumoApi();
                }else {
                    if (!(transView).longMin(txtNumDniOrdenante)) {
                        if (!numDocSolicitante.getText().toString().equals(txtNumDniOrdenante.getText().toString())){
                            toast.cancel();
                            type = "2";
                            relativeKeyboardNum.setVisibility(View.GONE);
                            consumoApi();
                            volver.setVisibility(View.VISIBLE);
                            tipoDocOrdenante.setEnabled(false);
                            volver.setVisibility(View.VISIBLE);
                        }else
                            Toast.makeText(getContext(), getResources().getString(R.string.msgDocIgual), Toast.LENGTH_SHORT).show();                    }
                }
            }else {
                if (!(transView).longMin(numDocSolicitante)) {
                    type = "1";
                    relativeKeyboardNum.setVisibility(View.GONE);
                    consumoApi();
                    volver.setVisibility(View.VISIBLE);
                    btnTipDoc.setEnabled(false);
                    btnContinuar.setEnabled(false);
                    linScrollView.getLayoutParams().height = 850;
                    numDocSolicitante.setBackgroundResource(R.drawable.btn_spinner_estados);
                    linBtnContinuar.setVisibility(View.VISIBLE);                    }
            }
        });

        btnConti.setOnClickListener(v -> {

            if(!Boolean.TRUE.equals(typeClient)){
                if(!txtNombreApeOrdenan.getText().toString().equals("")){
                    if((classArguments.getTypeclient() == IGUALORDENANTESI)) {
                        relativeKeyboardAlfa.setVisibility(View.GONE);
                        linScrollView.getLayoutParams().height = 850;
                        typeClient = false;
                        consumoApi();
                    }else {
                        if (!(transView).longMin(numDocSolicitante)) {
                            toast.cancel();
                            relativeKeyboardAlfa.setVisibility(View.GONE);
                            volver.setVisibility(View.VISIBLE);
                            btnContinuar.setEnabled(true);
                            linDatosOrdenante.setVisibility(View.VISIBLE);
                            tipoDocOrdenante.setEnabled(false);
                            linScrollView.getLayoutParams().height = 850;
                        }
                    }
                }else {
                    if (!txtNumDniOrdenante.getText().toString().equals("")) {
                        if (!(transView).longMin(txtNumDniOrdenante)) {
                            if (!numDocSolicitante.getText().toString().equals(txtNumDniOrdenante.getText().toString())) {
                                toast.cancel();
                                type = "2";
                                relativeKeyboardAlfa.setVisibility(View.GONE);
                                consumoApi();
                                volver.setVisibility(View.VISIBLE);
                                numDocSolicitante.setBackgroundResource(R.drawable.btn_spinner_estados);
                                linDatosOrdenante.setVisibility(View.VISIBLE);
                                btnContinuar.setEnabled(true);
                                linScrollView.getLayoutParams().height = 850;
                            } else
                                Toast.makeText(getContext(), getResources().getString(R.string.msgDocIgual), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        relativeKeyboardAlfa.setVisibility(View.GONE);
                        volver.setVisibility(View.VISIBLE);
                        btnTipDoc.setEnabled(true);
                        nombreApellido.setEnabled(false);
                        txtNombreApellido.setEnabled(false);
                        linTypeClient.setVisibility(View.VISIBLE);
                    }
                }
            }else {
                if(!nombreApellido.getText().toString().equals("")){
                    if (!(transView).longMin(numDocSolicitante)) {
                        toast.cancel();
                        relativeKeyboardAlfa.setVisibility(View.GONE);
                        volver.setVisibility(View.VISIBLE);
                        btnTipDoc.setEnabled(true);
                        nombreApellido.setEnabled(false);
                        txtNombreApellido.setEnabled(false);
                        linTypeClient.setVisibility(View.VISIBLE);
                        typeClient = false;
                    }
                }else {
                    if (!(transView).longMin(numDocSolicitante)) {
                        type = "1";
                        consumoApi();
                        btnTipDoc.setEnabled(false);
                        volver.setVisibility(View.VISIBLE);
                        linScrollView.getLayoutParams().height = 850;
                        relativeKeyboardAlfa.setVisibility(View.GONE);
                        numDocSolicitante.setBackgroundResource(R.drawable.btn_spinner_estados);
                        linBtnContinuar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        btnContinuar.setOnClickListener(view12 -> {
            try {
                if(classArguments.isEditar()) {
                    classArguments.setNameSender(nombreApellido.getText().toString());
                    transView.setArgumentsClass(classArguments);
                    NavHostFragment.findNavController(FragmentDatosSolicitante.this)
                            .navigate(R.id.action_fragmentDatosSolicitante_to_fragmentDetalleGiro);
                }else{
                    classArguments.setTypeDocumentSender(tipoDocSolici.getText().toString());
                    classArguments.setDniSender(numDocSolicitante.getText().toString());
                    classArguments.setNameSender(nombreApellido.getText().toString());
                    classArguments.setTypeDocumentRemitter(tipoDatoOrdenante.getText().toString());
                    classArguments.setDniRemitter(txtNumDniOrdenante.getText().toString());
                    classArguments.setNameRemitter(txtNombreApeOrdenan.getText().toString());
                    classArguments.setTypeclient(classArguments.getTypeclient());
                    classArguments.setFlag1(clientRemitter);
                    classArguments.setFlag2(clientSender);

                    transView.setArgumentsClass(classArguments);
                    NavHostFragment.findNavController(FragmentDatosSolicitante.this)
                            .navigate(R.id.action_fragmentDatosSolicitante_to_fragmentBeneficiaryDataEmision);
                }
            }catch (Exception e){
                transView.getListener().cancel(TcodeError.T_ERR_NAVI_FRAGMENT);
                Logger.logLine(Logger.LOG_EXECPTION, "FragmentDatosSolicitante");
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            }

        });

        txtNombreApellido.setOnClickListener(v -> {
            (transView).setKeyboard(nombreApellido,50,false,false,true, 1,null,relativeKeyboardAlfa);
            btnContinuar.setEnabled(true);
            nombreApellido.setHint(R.string.nombreOrdenante);
        });

        nomApellOrdenante.setOnClickListener(view1 -> {
            (transView).setKeyboard(txtNombreApeOrdenan,50,false,false,true, 1,null,relativeKeyboardAlfa);
            if (activity.findViewById(R.id.scrollViewDS) != null) {
                ScrollView scrollView;
                scrollView = activity.findViewById(R.id.scrollViewDS);
                scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
            }
            linScrollView.getLayoutParams().height = 500;
            btnContinuar.setEnabled(true);
            txtNombreApeOrdenan.setHint(R.string.nombreOrdenante);
        });

        volver.setOnClickListener(view13 -> {
            try {
                classArguments.setEditar(false);
                transView.setArgumentsClass(classArguments);
                NavHostFragment.findNavController(FragmentDatosSolicitante.this)
                        .navigate(R.id.action_fragmentDatosSolicitante_to_fragmentTypeCoin);
            }catch (Exception e){
                transView.getListener().cancel(TcodeError.T_ERR_RETURN_FRAGMENT);
                Logger.logLine(Logger.LOG_EXECPTION, "FragmentDatosSolicitante");
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            }

        });

        close.setOnClickListener(view14 -> {
            (transView).deleteTimer();
            (transView).getListener().cancel(TcodeError.T_USER_CANCEL);
        });

        if(classArguments.getTypeDocumentSender() != null) {
            //datos solciitante
            tipoDocSolici.setText(classArguments.getTypeDocumentSender());
            linNumDocSolci.setVisibility(View.VISIBLE);
            numDocSolicitante.setText(classArguments.getDniSender());
            numDocSolicitante.setBackgroundResource(R.drawable.btn_spinner_estados);
            tipoDocSolici.setBackgroundResource(R.drawable.btn_spinner_estados);
            linTypeClient.setVisibility(View.VISIBLE);
            btnTipDoc.setEnabled(false);
            linNombApellSolic.setVisibility(View.VISIBLE);
            nombreApellido.setText(classArguments.getNameSender());
            btnContinuar.setEnabled(true);
            typeClient = false;

        }
        if(classArguments.getTypeclient() == IGUALORDENANTENO && classArguments.getTypeDocumentRemitter() != null){//cuando se selecciona No
            //datos ordenante
            linDatosOrdenante.setVisibility(View.VISIBLE);
            tipoDatoOrdenante.setText(classArguments.getTypeDocumentRemitter());
            linDNIOrdenante.setVisibility(View.VISIBLE);
            txtNumDniOrdenante.setText(classArguments.getDniRemitter());
            txtNumDniOrdenante.setBackgroundResource(R.drawable.btn_spinner_estados);
            tipoDatoOrdenante.setBackgroundResource(R.drawable.btn_spinner_estados);
            btnTipDoc.setEnabled(false);
            nombreApellido.setEnabled(true);
            linNomOrdenante.setVisibility(View.VISIBLE);
            txtNombreApeOrdenan.setText(classArguments.getNameRemitter());
            radTipoSeleccion.check(R.id.SelectNo);
            btnContinuar.setEnabled(true);
        }
        radTipoSeleccion.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            try {
                switch (checkedId){
                    case R.id.SelectSi:
                        btnContinuar.setEnabled(true);
                        linBtnContinuar.setVisibility(View.VISIBLE);
                        linDatosOrdenante.setVisibility(View.GONE);
                        linDNIOrdenante.setVisibility(View.GONE);
                        linNomOrdenante.setVisibility(View.GONE);
                        relativeKeyboardAlfa.setVisibility(View.GONE);
                        relativeKeyboardNum.setVisibility(View.GONE);
                        linScrollView.getLayoutParams().height = 800;
                        tipoDatoOrdenante.setText("");
                        txtNombreApeOrdenan.setText("");
                        txtNumDniOrdenante.setText("");
                        classArguments.setTypeclient(IGUALORDENANTESI);
                        btnContinuar.setOnClickListener(v ->
                                NavHostFragment.findNavController(FragmentDatosSolicitante.this)
                                        .navigate(R.id.action_fragmentDatosSolicitante_to_fragmentBeneficiaryDataEmision));
                        break;
                    case R.id.SelectNo:
                        linDatosOrdenante.setVisibility(View.VISIBLE);
                        tipoDocOrdenante.setBackgroundResource(R.drawable.ic_arrow_down_blue);
                        tipoDatoOrdenante.setTypeface(typeface3);
                        btnContinuar.setEnabled(false);
                        relativeKeyboardAlfa.setVisibility(View.GONE);
                        relativeKeyboardNum.setVisibility(View.GONE);
                        classArguments.setTypeclient(IGUALORDENANTENO);
                        if(!spinnerDocumetn){
                            datosOrdenante();
                        }
                        break;
                    default:
                        break;
                }
            }catch (Exception e){
                ((TransView)activity).getListener().cancel(TcodeError.T_ERR_NAVI_FRAGMENT);
                Logger.logLine(Logger.LOG_EXECPTION, "FragmentDatosSolicitante");
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            }

            if( classArguments.getTypeclient() != IGUALORDENANTENO){
                classArguments.setTypeclient(IGUALORDENANTESI);//cuando se selecciona Si
            }
        });
    }

    private void consumoApi(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqObtainPerson ");
        try {
            volver.setVisibility(View.GONE);
            if(Boolean.TRUE.equals(typeClient)){
                linProgress.setVisibility(View.VISIBLE);
                imgprogress.playAnimation();
            }else {
                linProgressOrde.setVisibility(View.VISIBLE);
                imgprogressOrde.playAnimation();
            }
            RequestWs requestWs = new RequestWs(getActivity(), ROOT + msgScreenDocument.getListUrl().get(1).getMethod(), TIMEOUT, true);
            requestWs.httpRequets(waitTransFragment.headerTrans(rspObtainPersonEmision != null), setObtainPerson(), (result, statusCode, header) -> {
                waitTransFragment.setOpnNumber();
                Logger.logLine(Logger.LOG_GENERAL, " Saliendo de reqObtainPerson ");
                if (result != null){
                    if (statusCode == 200){
                        try {
                            (transView).counterDownTimer(msgScreenDocument.getTimeOut(), msgScreenTimer);
                            rspObtainPersonEmision = new RspObtainPersonEmision();
                            if (rspObtainPersonEmision.getRspobtainPerson(result,header)){

                                waitTransFragment.getSessionId(rspObtainPersonEmision.getRspsessionId()+"");

                                name = rspObtainPersonEmision.getRspname();

                                if(Boolean.TRUE.equals(typeClient)){
                                    linProgress.setVisibility(View.GONE);
                                    imgprogress.pauseAnimation();
                                }else {
                                    linProgressOrde.setVisibility(View.GONE);
                                    imgprogressOrde.pauseAnimation();
                                }

                                if(name != null && !name.equals("")){
                                    if(Boolean.TRUE.equals(typeClient)){//solicitante
                                        linNombApellSolic.setVisibility(View.VISIBLE);
                                        nombreApellido.setText(name);
                                        nombreApellido.setEnabled(false);
                                        txtNombreApellido.setEnabled(false);
                                        btnContinuar.setEnabled(true);
                                        linTypeClient.setVisibility(View.VISIBLE);
                                        typeClient = false;
                                        clientSender = true;
                                    }else {//ordenante
                                        linNomOrdenante.setVisibility(View.VISIBLE);
                                        txtNombreApeOrdenan.setText(name);
                                        txtNombreApeOrdenan.setEnabled(false);
                                        nomApellOrdenante.setEnabled(false);
                                        btnContinuar.setEnabled(true);
                                        clientRemitter = true;
                                    }
                                }else {
                                    if(Boolean.TRUE.equals(typeClient)){//solicitante
                                        linNombApellSolic.setVisibility(View.VISIBLE);
                                        volver.setVisibility(View.VISIBLE);
                                        nomApellOrdenante.setEnabled(true);
                                        nombreApellido.setEnabled(true);
                                        clientSender = false;
                                    }else {//ordenante
                                        linNomOrdenante.setVisibility(View.VISIBLE);
                                        txtNombreApellido.setEnabled(true);
                                        nomApellOrdenante.setEnabled(true);
                                        btnContinuar.setEnabled(false);
                                        clientRemitter = false;
                                    }
                                }
                                linScrollView.getLayoutParams().height = 850;
                            }else {
                                (transView).deleteTimer();
                                (transView).getListener().cancel(TcodeError.T_ERR_UNPACK_JSON);
                            }
                        } catch (JSONException e) {
                            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                        }
                    }else {
                        (transView).getListener().cancel(result,header,statusCode);
                    }
                }else {
                    (transView).deleteTimer();
                    (transView).getListener().cancel(UIUtils.validateResponseError(statusCode));
                }
            });
        }catch (Exception e){
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
    }

    private JSONObject setObtainPerson(){
        final ReqObtainPersonEmision reqObtainPersonEmision = new ReqObtainPersonEmision();

        reqObtainPersonEmision.setReqactor(type);
        reqObtainPersonEmision.setReqdocumentType(documentType(type.equals("1") ? tipoDocSolici.getText().toString() : tipoDatoOrdenante.getText().toString()));
        reqObtainPersonEmision.setReqdocumentNumber(type.equals("1") ? numDocSolicitante.getText().toString() : txtNumDniOrdenante.getText().toString());

        return reqObtainPersonEmision.buildsObjectJSON();
    }

    private void datosSolicitante(){

        (transView).setKeyboard(numDocSolicitante, msgScreenDocument.getMaxLength(),false,false, msgScreenDocument.isAlfa(), minNumDocument,relativeKeyboardNum,relativeKeyboardAlfa);
        if (msgScreenDocument.isBanner()){
            clickSpinner = new ClickSpinner(tipoDocSolici,txtDniSolci,btnTipDoc,vista.findViewById(R.id.linSpinnerDoc),vista.findViewById(R.id.relatTipoDocumet),arrayDocumt,(Activity) thisContext);
            clickSpinner.setDocument(txtNumDoc,linNumDocSolci,relativeKeyboardNum,relativeKeyboardAlfa);
            clickSpinner.setClickKeyboard((transView).getKeyboards());
            clickSpinner.spinnerBotones(Trans.GIROS_EMISION,"Solicitante");
            clickSpinner.setCallbackTypeDocument(callbackTypeDocument);
            btnContinuar.setEnabled(false);
        }
    }

    private void datosOrdenante(){

        (transView).setKeyboard(txtNumDniOrdenante, msgScreenDocument.getMaxLength(),false,false, msgScreenDocument.isAlfa(), minNumDocument,relativeKeyboardNum,relativeKeyboardAlfa);
        if (msgScreenDocument.isBanner()){
            clickSpinner = new ClickSpinner(tipoDatoOrdenante,txtselectOrd,tipoDocOrdenante,vista.findViewById(R.id.linSpinnerDocOrdenan),vista.findViewById(R.id.relatTipDocOrdenante),arrayDocumt,(Activity) thisContext);
            clickSpinner.setDocument(numDniOrde,linDNIOrdenante,relativeKeyboardNum,relativeKeyboardAlfa);
            clickSpinner.setClickKeyboard((transView).getKeyboards());
            clickSpinner.spinnerBotones(Trans.GIROS_EMISION,"Ordenante");
            clickSpinner.setCallbackTypeDocument(callbackTypeDocument);
            numDniOrde.setText(activity.getResources().getString(R.string.numDNISolici));
            spinnerDocumetn = true;
        }
    }
    private String documentType(String documentSelect){
        String typeDocument;
        switch (documentSelect){
            case "DNI":
                typeDocument = "1";
                break;
            case "Carnet de Extranjería":
            case "Carnet de extranjería":
                typeDocument = "3";
                break;
            case "Pasaporte":
                typeDocument = "4";
                break;
            case "RUC":
                typeDocument = "6";
                break;
            default:
                typeDocument = "";
                break;
        }

        return typeDocument;
    }

    private void fillScreenOrders(){
        final RelativeLayout relativeLayout = vista.findViewById(R.id.msgAccount);
        relativeLayout.setVisibility(View.VISIBLE);

        TextView tvMsg1 = vista.findViewById(R.id.msg1Screen);
        TextView tvMsg2 = vista.findViewById(R.id.msg2Screen);
        TextView tvMsg3 = vista.findViewById(R.id.msg3Screen);

        //CENTRAR CONTENIDO
        tvMsg2.setGravity(Gravity.CENTER);
        tvMsg3.setGravity(Gravity.CENTER);

        //contenido de pantalla
        tvMsg1.setVisibility(View.GONE);
        tvMsg2.setText(getResources().getString(R.string.msg3Giros));
        tvMsg3.setText(getResources().getString(R.string.msg4Giros));

        //botones
        Button btnEntentido = vista.findViewById(R.id.entendidoMsg);
        Button btnSalir = vista.findViewById(R.id.exitMsg);
        Button btnContinuarMsg = vista.findViewById(R.id.continuarMsg);

        btnSalir.setVisibility(View.VISIBLE);
        btnContinuarMsg.setVisibility(View.VISIBLE);

        btnEntentido.setText(getResources().getString(R.string.changeDocument));
        btnSalir.setText(getResources().getString(R.string.msgAccountClientBtnExit));

        relativeLayout.setOnClickListener(v -> {
            //accion al presionar el relative
        });

        btnEntentido.setOnClickListener(v -> {
            //accion al presionar el boton cambiar documento
            if(!Boolean.TRUE.equals(typeClient)){
                tipoDatoOrdenante.setText("");
                linDNIOrdenante.setVisibility(View.GONE);
                linScrollView.getLayoutParams().height = 850;
            }else{
                tipoDocSolici.setText("");
                linNumDocSolci.setVisibility(View.INVISIBLE);
            }
            relativeLayout.setVisibility(View.GONE);
            relativeKeyboardNum.setVisibility(View.GONE);

        });

        btnContinuarMsg.setOnClickListener(v ->
            //accion al presionar el boton continaur
            relativeLayout.setVisibility(View.GONE));

        btnSalir.setOnClickListener(v ->{
            (transView).deleteTimer();
            //accion al presionar el boton salir
            (transView).getListener().cancel();
        });
    }
    private void validateLongMaxMin(String documentSelect){
        int minLeng;
        int maxLeng;
        switch (documentSelect){
            case "DNI":
                minLeng = 8;
                maxLeng = 8;
                break;
            case "Carnet de Extranjería":
            case "Carnet de extranjería":
            case "Pasaporte":
                minLeng = 5;
                maxLeng = 24;
                break;
            case "RUC":
                minLeng = 11;
                maxLeng = 11;
                break;
            default:
                minLeng = 0;
                maxLeng = 0;
                break;
        }

        minEtInputuser = minLeng;
        maxEtInputuser = maxLeng;
    }

}