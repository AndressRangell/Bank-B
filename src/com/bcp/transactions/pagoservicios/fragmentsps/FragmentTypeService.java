package com.bcp.transactions.pagoservicios.fragmentsps;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.airbnb.lottie.LottieAnimationView;
import com.android.desert.keyboard.InputManager;
import com.android.newpos.pay.R;
import com.bcp.document.ClassArguments;
import com.bcp.rest.httpclient.RequestWs;
import com.bcp.rest.jsonerror.JsonError;
import com.bcp.rest.pagoservicios.request.ReqListClientDebts;
import com.bcp.rest.pagoservicios.request.ReqObtainAmountLimits;
import com.bcp.rest.pagoservicios.request.ReqVerifyPayment;
import com.bcp.rest.pagoservicios.response.ListsDebt;
import com.bcp.rest.pagoservicios.response.ListsServices;
import com.bcp.rest.pagoservicios.response.RspListClientDebts;
import com.bcp.rest.pagoservicios.response.RspListServiceDocuments;
import com.bcp.rest.pagoservicios.response.RspListServiceImports;
import com.bcp.rest.pagoservicios.response.RspListServices;
import com.bcp.rest.pagoservicios.response.RspObtainAmountLimits;
import com.bcp.rest.pagoservicios.response.RspVerifyPayment;
import com.bcp.teclado_alfanumerico.ClickSpinner;
import com.bcp.transactions.callbacks.WaitConsumosApi;
import com.bcp.transactions.callbacks.WaitPaymentServices;
import com.bcp.transactions.callbacks.WaitTransFragment;
import com.newpos.libpay.Logger;
import com.newpos.libpay.presenter.TransUIImpl;
import com.newpos.libpay.presenter.TransView;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.Trans;
import org.json.JSONException;
import org.json.JSONObject;

import cn.desert.newpos.payui.UIUtils;

import static com.bcp.rest.JsonUtil.ROOT;
import static com.bcp.rest.JsonUtil.TIMEOUT;


public class FragmentTypeService extends Fragment {

    ImageView close;
    TextView titleTipoServicio;
    TextView title;
    TextView tvDescripServiceTitle;
    TextView selectTypeService;
    TextView selectDeudaPendiente;
    TextView txDeudasPendientes;
    TextView selectlistdocument;
    EditText spinnerTypeService;
    EditText txtDescriptionService;
    EditText spinnDeudasPendientes;
    EditText spinnerlistDocument;
    ImageButton btnlistDocument;
    ImageButton btnTypeService;
    ImageButton btnDeudaPend;
    View vista;
    ImageView volver;
    LinearLayout linDescriptionService;
    LinearLayout linDeudasPendientes;
    Button btnContinuar;
    Button btnConti;
    RadioGroup radioGroup;
    RadioButton montoTotal;
    RadioButton montoMinimo;
    LinearLayout linFormPago;
    LinearLayout linlistDocument;
    LinearLayout linScrollViewPS;
    LinearLayout linProgress;
    ScrollView scrollViewPS;
    RelativeLayout msgRelative;
    LottieAnimationView progressPS;
    RelativeLayout relativeFondo;
    Button btnCambiarCod;


    ClickSpinner clickSpinner;
    String[] arrayTipoServicio;
    String[] arrayDeudas;
    String[] arrayImport;
    String[] arrayDocument;
    Context thiscontext;
    ClassArguments classArguments;
    Activity activity;
    int minEtInputuser;
    String serviceType = "";
    String minAmount = "";
    RspListServices rspListServices;
    RspListClientDebts rspListClientDebts;
    RspListServiceImports rspListServiceImports;
    RspListServiceDocuments rspListServiceDocuments;
    RspVerifyPayment rspVerifyPayment;
    RspObtainAmountLimits rspObtainAmountLimits;


    ListsServices listsServices;
    ListsDebt listsDebt;
    WaitPaymentServices callbackPaymetServices;
    WaitConsumosApi waitConsumosApi;
    WaitTransFragment waitTransFragment;
    public static final String  TYPESERVICE = "tipo servicio";
    public static final String  DEBTSTOPAY  = "deudas pendientes";
    public static final String  AMOUNTTOPAY = "importe a pagar";
    public static final String TYPEDOCUMENT = "tipo documento";
    private static final String MSGSCREENTIMER = "FragmentTypeService";
    boolean clicks = false;
    private int conteoClicks = 0;



    private RelativeLayout relativeKeyboardNume;
    private RelativeLayout relativeKeyboardAlfa;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.thiscontext = context;
        this.activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       vista = inflater.inflate(R.layout.fragment_type_service, container, false);

        title                  = vista.findViewById(R.id.title_toolbar);
        close                  = vista.findViewById(R.id.iv_close);
        btnContinuar           = vista.findViewById(R.id.continuar);
        titleTipoServicio      = vista.findViewById(R.id.typeService);
        spinnerTypeService     = vista.findViewById(R.id.SpinnerTypeService);
        relativeKeyboardNume   = vista.findViewById(R.id.keyboardNumerico);
        relativeKeyboardAlfa   = vista.findViewById(R.id.keyboardAlfa);
        volver                 = vista.findViewById(R.id.back);
        btnTypeService         = vista.findViewById(R.id.btnTypeService);
        tvDescripServiceTitle  = vista.findViewById(R.id.titleDescripService);
        linDescriptionService  = vista.findViewById(R.id.linDescriptionService);
        txtDescriptionService  = vista.findViewById(R.id.txtDescriptionService);
        selectTypeService      = vista.findViewById(R.id.selectTypeService);
        linDeudasPendientes    = vista.findViewById(R.id.DeudasPendientes);
        spinnDeudasPendientes  = vista.findViewById(R.id.SpinnerDeudaPendient);
        selectDeudaPendiente   = vista.findViewById(R.id.selectDeudasPend);
        btnDeudaPend           = vista.findViewById(R.id.btnDeudaPend);
        txDeudasPendientes     = vista.findViewById(R.id.txtDeudaPendient);
        btnConti               = vista.findViewById(R.id.Btncontinuar);
        radioGroup             = vista.findViewById(R.id.radioGroupPago);
        montoTotal             = vista.findViewById(R.id.SelectPagoTotal);
        linFormPago            = vista.findViewById(R.id.linFormaPago);
        montoMinimo            = vista.findViewById(R.id.SelectPagoMinimo);
        selectlistdocument     = vista.findViewById(R.id.selectDocumentList);
        spinnerlistDocument    = vista.findViewById(R.id.SpinnerDocumentList);
        btnlistDocument        = vista.findViewById(R.id.btnDocumentList);
        linlistDocument        = vista.findViewById(R.id.listDocument);
        scrollViewPS           = vista.findViewById(R.id.scrollViewPs);
        linScrollViewPS        = vista.findViewById(R.id.linearScrollPs);
        msgRelative            = vista.findViewById(R.id.msgRelativePS);
        progressPS             = vista.findViewById(R.id.gifProcesandoPS);
        linProgress            = vista.findViewById(R.id.processPS);
        relativeFondo         = vista.findViewById(R.id.msgAccount);
        btnCambiarCod          = vista.findViewById(R.id.continuarMsg);

        volver.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);

        title.setText(thiscontext.getResources().getString(R.string.titleMiBanco));
        titleTipoServicio.setText(thiscontext.getResources().getString(R.string.titleTipServicio));
        spinnerTypeService.setBackgroundResource(R.drawable.btn_spinner_estados);
        minEtInputuser = ((TransView)activity).getMsgScreendocument().getMinLength();

        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TransView) activity).deleteTimer();

        ((TransView)activity).counterDownTimer(((TransView)activity).getMsgScreendocument().getTimeOut(), MSGSCREENTIMER);

        //se instancia el callback para obtener el sesionId y el header
        waitTransFragment = ((TransView)activity).getMsgScreendocument().getCallbackTransFragment();

        classArguments = ((TransView) activity).getArguments();

        callbackPaymetServices = typeSpinner -> {
            switch (typeSpinner){
                case TYPESERVICE:
                    listsServices = rspListServices.getServices()[clickSpinner.getPositionSelected()];
                    break;
                case DEBTSTOPAY:
                    listsDebt = rspListClientDebts.getListsDebts()[clickSpinner.getPositionSelected()];
                    minAmount = rspListClientDebts.getListsDebts()[clickSpinner.getPositionSelected()].getMinimumAmount();
                    break;
                case AMOUNTTOPAY:
                    listsDebt = rspListServiceImports.getListImport()[clickSpinner.getPositionSelected()];
                    break;
                case TYPEDOCUMENT:
                    listsDebt = rspListServiceDocuments.getListDocument()[clickSpinner.getPositionSelected()];
                    if (listsDebt.getDocumentCode() != null)
                        classArguments.setDocumentCode(listsDebt.getDocumentCode());
                    break;
                default:
                    break;
            }
            tipoServicio(listsServices.getServiceType(),listsServices.getServiceInputDescription().substring(0,1).toUpperCase() + listsServices.getServiceInputDescription().substring(1).toLowerCase(),typeSpinner);
        };

        waitConsumosApi = null;
        waitConsumosApi = retval -> {
            if(retval == 200){

                arrayTipoServicio = new String[rspListServices.getServices().length];
                for(int i = 0 ; i < rspListServices.getServices().length; i++){
                    arrayTipoServicio[i] = rspListServices.getServices()[i].getServiceDescription();
                }
                classArguments.setAffiliationCode(rspListServices.getServices()[0].getAffiliationCode());

                ((TransView)activity).setKeyboard(txtDescriptionService, 11,false,false, ((TransView)activity).getMsgScreendocument().isAlfa(), minEtInputuser,relativeKeyboardNume,relativeKeyboardAlfa);
                if (((TransView)activity).getMsgScreendocument().isBanner()){
                    clickSpinner = new ClickSpinner(spinnerTypeService,selectTypeService,btnTypeService,vista.findViewById(R.id.linSpinnerTypeSer),vista.findViewById(R.id.relTipeService),arrayTipoServicio,(Activity) thiscontext);
                    clickSpinner.setDocument(titleTipoServicio,linDescriptionService,relativeKeyboardNume,relativeKeyboardAlfa);
                    clickSpinner.setClickKeyboard(((TransView)activity).getKeyboards());
                    clickSpinner.setCallbackPaymentService(callbackPaymetServices);
                    clickSpinner.spinnerBotones(Trans.PAGOSERVICIOS,TYPESERVICE);
                    txtDescriptionService.setBackgroundResource(R.drawable.btn_spinner_estados);
                }
            }
        };
        consumoApi();

        btnContinuar.setOnClickListener(view1 -> {
            switch (serviceType){
                case "00"://TIPO DE PAGO PASE MOVISTAR
                    classArguments.setTipoPagoServicio(Trans.PAGO_PASEMOVISTAR);
                    relativeKeyboardNume.setVisibility(View.GONE);
                    break;
                case "04"://TIPO DE SERVICIO DE PAGO SIN VALIDACION
                    classArguments.setTipoPagoServicio(Trans.PAGO_SINVALIDACION);
                    relativeKeyboardNume.setVisibility(View.GONE);
                    btnConti.setEnabled(true);
                    break;
                case "05"://PS PARCIAL
                    classArguments.setTipoPagoServicio(Trans.PAGO_PARCIAL);
                    relativeKeyboardNume.setVisibility(View.GONE);
                    msgRelative.setBackground(getResources().getDrawable(R.drawable.ic_background_dark));
                    msgRelative.setVisibility(View.VISIBLE);
                    waitConsumosApi = null;
                    waitConsumosApi = retval -> {
                        if (retval == 200) {
                            NavHostFragment.findNavController(FragmentTypeService.this)
                                    .navigate(R.id.action_fragmentTypeService2_to_fragmentTypeCoin2);
                        }
                    };
                    consumoObtainAmount();
                    break;
                case  "06"://TIPO DE PAGO CON RANGO
                    clickSpinnerDebts("06");
                    relativeKeyboardNume.setVisibility(View.GONE);
                    linDeudasPendientes.setVisibility(View.VISIBLE);
                    break;
                case "07"://TIPO DE PAGO IMPORTE
                    classArguments.setTipoPagoServicio(Trans.PAGO_IMPORTE);
                    relativeKeyboardNume.setVisibility(View.GONE);
                    btnConti.setEnabled(true);
                    linScrollViewPS.getLayoutParams().height = 800;
                    txDeudasPendientes.setText("Importe a pagar");
                    setClickSpinnerListImport();
                    break;
                default:
                    break;
            }
            ((TransView)activity).setArgumentsClass(classArguments);
        });

        btnConti.setOnClickListener(view1 ->  {
            classArguments.setTypeservice(spinnerTypeService.getText().toString());
            switch (classArguments.getTipoPagoServicio()){
                case Trans.PAGO_PASEMOVISTAR://TIPO DE SERVICIO DE PAGO MOVISTAR
                    ((TransView)activity).deleteTimer();
                    ((TransView)activity).getListener().confirm(InputManager.Style.COMMONINPUT);
                    break;
                case Trans.PAGO_SINVALIDACION: //TIPO DE SERVICIO SIN VALIDACION
                    classArguments.setTypeservice(spinnerTypeService.getText().toString());
                    NavHostFragment.findNavController(FragmentTypeService.this)
                            .navigate(R.id.action_fragmentTypeService2_to_fragmentTypeCoin2);
                    break;
                case Trans.PAGO_CONRANGO:// TIPO DE SERVICIO DE PAGO CON RANGO
                    classArguments.setTypeservice(spinnerTypeService.getText().toString());
                    if (classArguments.isOtherAmount()){
                        NavHostFragment.findNavController(FragmentTypeService.this)
                                .navigate(R.id.action_fragmentTypeService2_to_fragmentTypeCoin2);
                    }else {
                        if(classArguments.getTypepayment().equals("1")){
                            callbackVerify("06");
                        }else {
                            ((TransView)activity).deleteTimer();
                            ((TransView)activity).getListener().confirm(InputManager.Style.COMMONINPUT);
                        }
                    }
                    break;
                case Trans.PAGO_SINRANGO://TIPO DE SERVIICO DE PAGO SIN RANGO
                    if(classArguments.getTypepayment().equals("1")){
                        callbackVerify("06");
                    }else {
                        ((TransView)activity).deleteTimer();
                        ((TransView)activity).getListener().confirm(InputManager.Style.COMMONINPUT);
                    }
                    break;
                case Trans.PAGO_IMPORTE: //TIPO DE SERVICIO DE PAGO IMPORTE
                    classArguments.setTypeservice(spinnerTypeService.getText().toString());
                    if(classArguments.getTypepayment().equals("1")){
                        callbackVerify("07");
                    }else {
                        ((TransView)activity).deleteTimer();
                        ((TransView)activity).getListener().confirm(InputManager.Style.COMMONINPUT);
                    }
                    break;
                default:
                    break;
            }
            ((TransView)activity).setArgumentsClass(classArguments);
        });

        close.setOnClickListener(view1 -> {
            ((TransView)activity).deleteTimer();
            ((TransView)activity).getListener().cancel();
        });

        volver.setOnClickListener(view1 ->{
            NavHostFragment.findNavController(FragmentTypeService.this)
                    .navigate(R.id.action_global_fragmentServiceSelector2);
        });

        radioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {

            switch (checkedId){
                case R.id.SelectOtroMonto:
                    classArguments.setOtherAmount(true);
                case R.id.SelectPagoTotal:
                case R.id.SelectPagoMinimo:
                    classArguments.setTipoPagoServicio(Trans.PAGO_CONRANGO);
                    ((TransView)activity).setArgumentsClass(classArguments);
                    btnConti.setEnabled(true);
                    break;
                default:
                    break;
            }
        });
    }

    private void callbackVerify(String typeService){
        waitConsumosApi = null;
        waitConsumosApi = retval -> {
            classArguments.setTypeservice(spinnerTypeService.getText().toString());
            if (retval == 200) {
                if(classArguments.getTypepayment().equals("1")){
                    NavHostFragment.findNavController(FragmentTypeService.this)
                            .navigate(R.id.action_fragmentTypeService2_to_fragmentDetailPaymentService2);
                }else {
                    ((TransView)activity).deleteTimer();
                    ((TransView)activity).getListener().confirm(InputManager.Style.COMMONINPUT);
                }
            }
        };
        consumoVerifypaymen(typeService);
    }

    private void clickSpinnerDebts(String tipoServicio){

        waitConsumosApi = null;
        waitConsumosApi = retval -> {
            if(retval == 200){
                if(rspListClientDebts.getListsDebts() == null){
                    msgAccount(vista,thiscontext.getResources().getString(R.string.msg1deudas),thiscontext.getResources().getString(R.string.msg2deudas));
                }else {
                    if(rspListClientDebts.getListsDebts()[0].getMinimumAmount() != null ){
                        classArguments.setTipoPagoServicio(Trans.PAGO_CONRANGO);
                    }else {
                        classArguments.setTipoPagoServicio(Trans.PAGO_SINRANGO);
                    }
                    arrayDeudas = new String[rspListClientDebts.getListsDebts().length];
                    for(int i = 0 ; i < rspListClientDebts.getListsDebts().length; i++){
                        arrayDeudas[i] = rspListClientDebts.getListsDebts()[i].getAmountCurrencySymbol() + ""+ rspListClientDebts.getListsDebts()[i].getAmount() +" (vence"+rspListClientDebts.getListsDebts()[i].getDueDate() +")";
                    }
                    if(rspListClientDebts.getListsDebts()[0].getDebtCode() != null && !rspListClientDebts.getListsDebts()[0].getDebtCode().equals("")){
                        classArguments.setDebcode(rspListClientDebts.getListsDebts()[0].getDebtCode());
                    }else if(rspListClientDebts.getListsDebts()[0].getDebtPaymentCode() != null && !rspListClientDebts.getListsDebts()[0].getDebtPaymentCode().equals("")){
                        classArguments.setDebcode(rspListClientDebts.getListsDebts()[0].getDebtPaymentCode());
                    }else if(rspListClientDebts.getListsDebts()[0].getAmount() != null && !rspListClientDebts.getListsDebts()[0].getAmount().equals("")){
                        classArguments.setTotal(rspListClientDebts.getListsDebts()[0].getAmount());
                    }

                    if (((TransView)activity).getMsgScreendocument().isBanner()){
                        clickSpinner = new ClickSpinner(spinnDeudasPendientes,selectDeudaPendiente,btnDeudaPend,vista.findViewById(R.id.linDeudasPend),vista.findViewById(R.id.relDeudasPend),arrayDeudas,(Activity) thiscontext);
                        clickSpinner.setCallbackPaymentService(callbackPaymetServices);
                        if(rspListClientDebts.getListsDebts()[0].getMinimumAmount() != null ){
                            clickSpinner.spinnerBotones(Trans.PAGO_CONRANGO,DEBTSTOPAY);
                        }else {
                            clickSpinner.spinnerBotones(Trans.PAGO_SINRANGO,DEBTSTOPAY);
                        }
                        linDeudasPendientes.setVisibility(View.VISIBLE);
                    }
                }
            }
        };
        consumoApiListDebts(tipoServicio);
    }

    //CLICK SPINNER PARA ARRAY LISTIMPORT
    private void setClickSpinnerListImport(){

        waitConsumosApi = null;
        waitConsumosApi = retval -> {
            if(retval == 200){
                arrayImport = new String[rspListServiceImports.getListImport().length];
                for(int i = 0 ; i < rspListServiceImports.getListImport().length; i++){
                    arrayImport[i] = rspListServiceImports.getListImport()[i].getAmountCurrencySymbol() + ""+ rspListServiceImports.getListImport()[i].getAmount() +" ("+rspListServiceImports.getListImport()[i].getImportName() +")";
                }
                classArguments.setImportcode(rspListServiceImports.getListImport()[0].getImportCode());

                if (((TransView)activity).getMsgScreendocument().isBanner()){
                    clickSpinner = new ClickSpinner(spinnDeudasPendientes,selectDeudaPendiente,btnDeudaPend,vista.findViewById(R.id.linDeudasPend),vista.findViewById(R.id.relDeudasPend),arrayImport,(Activity) thiscontext);
                    clickSpinner.setCallbackPaymentService(callbackPaymetServices);
                    clickSpinner.spinnerBotones(Trans.PAGO_IMPORTE,AMOUNTTOPAY);
                    linDeudasPendientes.setVisibility(View.VISIBLE);
                }
            }
        };
        consumoApiListServicesImport();
    }

    //CLICK SPINNER PARA LLENAR EL ARRAY DE TYPE DE DOCUMENT
    private void setClickSpinnerDocument(){

        waitConsumosApi = null;
        waitConsumosApi = retval -> {
            if(retval == 200){
                arrayDocument = new String[rspListServiceDocuments.getListDocument().length];

                if(rspListServiceDocuments.getListDocument()[0].getDocumentCode() != null) {
                    classArguments.setDocumentCode(rspListServiceDocuments.getListDocument()[0].getDocumentCode());
                }
                if(arrayDocument.length >=2){
                    for(int i = 0 ; i < rspListServiceDocuments.getListDocument().length; i++){
                        arrayDocument[i] = rspListServiceDocuments.getListDocument()[i].getDocumentDescription();
                    }

                   ((TransView)activity).setKeyboard(txtDescriptionService, 11,false,false, ((TransView)activity).getMsgScreendocument().isAlfa(), minEtInputuser,relativeKeyboardNume,relativeKeyboardAlfa);
                    if (((TransView)activity).getMsgScreendocument().isBanner()){
                        clickSpinner = new ClickSpinner(spinnerlistDocument,selectlistdocument,btnDeudaPend,vista.findViewById(R.id.linSpinnerlistDocument),vista.findViewById(R.id.relListDocument),arrayDocument,(Activity) thiscontext);
                        clickSpinner.setDocument(tvDescripServiceTitle,linDescriptionService,relativeKeyboardNume,relativeKeyboardAlfa);
                        clickSpinner.setClickKeyboard(((TransView)activity).getKeyboards());
                        clickSpinner.setCallbackPaymentService(callbackPaymetServices);
                        clickSpinner.spinnerBotones(Trans.PAGOSERVICIOS,TYPEDOCUMENT);
                        linlistDocument.setVisibility(View.VISIBLE);
                    }
                }
            }
        };
        consumoListServiDocuent();
    }

    public void tipoServicio(String tipoServicio , String titlePs ,String typeSpinner){

        serviceType = tipoServicio;
        switch (tipoServicio){
            case "00"://TIPO DE SERVICIO DE PAGO MOVISTAR
                spaceNumerPhone();
                btnConti.setEnabled(true);
                break;
            case "04"://TIPO DE SERVICIO DE PAGO SIN VALIDACION
            case "06":// TIPO DE SERVICIO DE PAGO CON RANGO y SIN RANGO
                classArguments.setClientDocumentNumber(txtDescriptionService.getText().toString());
                tvDescripServiceTitle.setText("Numero de DNI del cliente");
                if(typeSpinner.equals(DEBTSTOPAY)){
                    String amount = spinnDeudasPendientes.getText().toString().substring(2,8);
                    montoTotal.setText("Pago total (S/" + amount + ")");
                    montoMinimo.setText("Pago mínimo (S/" + minAmount + ")");
                    classArguments.setMinAmount(minAmount);
                    classArguments.setTotal(amount);
                }
                relativeKeyboardNume.setVisibility(View.GONE);
                btnConti.setEnabled(true);
                break;
            case "05"://TIPO DE SERVICIO DE PAGO PARCIAL
                relativeKeyboardNume.setVisibility(View.GONE);
                btnConti.setEnabled(true);
                break;
            case "07"://TIPO DE SERVIICO DE PAGO IMPORTE
                tvDescripServiceTitle.setText("Numero de DNI del cliente");
                if(typeSpinner.equals(TYPESERVICE)){
                    setClickSpinnerDocument();
                }
                relativeKeyboardNume.setVisibility(View.GONE);
                btnConti.setEnabled(true);
                break;
            default:
                break;
        }
        tvDescripServiceTitle.setText(titlePs);
    }

    private void spaceNumerPhone() {
        txtDescriptionService.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                for (int i = 3; i < s.length(); i +=4){
                    if (s.toString().charAt(i) != ' '){
                        s.insert(i," ");
                    }
                }
            }
        });
    }

    private void  consumoApi(){

        String company = "companyId=" + classArguments.getObjCompanny().getIdCompanny();

        RequestWs requestWs = new RequestWs(getActivity(), ROOT + ((TransView)activity).getMsgScreendocument().getListUrl().get(0).getMethod(), TIMEOUT, true);
        requestWs.httpRequetsStringGet(((TransView) activity).getMsgScreendocument().getHeader(), company, (result, statusCode,rspHeader) -> {
            waitTransFragment.setOpnNumber();
            if (result != null){
                if (statusCode == 200){
                    try {

                        rspListServices = new RspListServices();

                        if (!rspListServices.getRspObtain(result, rspHeader)) {
                            ((TransView) activity).showError(((TransView) activity).getMsgScreendocument().getTransEname(), String.valueOf(TcodeError.T_ERR_UNPACK_JSON), TransUIImpl.getErrInfo(String.valueOf(TcodeError.T_ERR_UNPACK_JSON)));
                            return;
                        }

                        waitTransFragment.getSessionId(rspListServices.getSesionId()+"");

                    } catch (Exception e) {
                        Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                    }
                }else {
                    ((TransView)activity).deleteTimer();
                    validateError(result,statusCode);
                }
                waitConsumosApi.getVeryBank(statusCode);
            }else {
                ((TransView)activity).deleteTimer();
                ((TransView)activity).getListener().cancel(UIUtils.validateResponseError(statusCode));
            }
        });
    }
   // CONSUMO DE API PARA PAGO DE SERVICIO CON RANGO
    private void  consumoApiListDebts(String tipoServicio ){

        RequestWs requestWs = new RequestWs(getActivity(), ROOT + ((TransView)activity).getMsgScreendocument().getListUrl().get(1).getMethod(), TIMEOUT, true);
        requestWs.httpRequets(((TransView) activity).getMsgScreendocument().getHeader(), setListClientDebts(tipoServicio), (result, statusCode, header) -> {
            waitTransFragment.setOpnNumber();
            if (result != null){
                if (statusCode == 200){
                    try {
                        rspListClientDebts = new RspListClientDebts();

                        if (!rspListClientDebts.getRspObtain(result, header,classArguments.getTipoPagoServicio(),classArguments.getTypepayment())) {
                            if(rspListClientDebts.getListsDebts() == null){
                                btnCambiarCod.setVisibility(View.VISIBLE);
                                linProgress.setVisibility(View.GONE);
                                relativeFondo.setBackground(getResources().getDrawable(R.drawable.ic_background_dark));
                                msgAccount(vista,thiscontext.getResources().getString(R.string.msg1deudas),thiscontext.getResources().getString(R.string.msg2deudas));
                            }else {
                                ((TransView) activity).showError(((TransView) activity).getMsgScreendocument().getTransEname(), String.valueOf(TcodeError.T_ERR_UNPACK_JSON), TransUIImpl.getErrInfo(String.valueOf(TcodeError.T_ERR_UNPACK_JSON)));

                            }
                        }

                    } catch (Exception e) {
                        Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                    }
                }else {
                    ((TransView)activity).deleteTimer();
                    validateError(result,statusCode);
                }
                waitConsumosApi.getVeryBank(statusCode);
            }else {
                ((TransView)activity).deleteTimer();
                ((TransView)activity).getListener().cancel(UIUtils.validateResponseError(statusCode));
            }
        });
    }

    //CONSUMO DE API DE LIST SERVICES IMPORTS

    private void  consumoApiListServicesImport(){
        String affiliationCode = "affiliationCode=" + classArguments.getAffiliationCode();

        RequestWs requestWs = new RequestWs(getActivity(), ROOT + ((TransView)activity).getMsgScreendocument().getListUrl().get(3).getMethod(), TIMEOUT, true);
        requestWs.httpRequetsStringGet(((TransView) activity).getMsgScreendocument().getHeader(), affiliationCode, (result, statusCode,rspHeader) -> {
            waitTransFragment.setOpnNumber();
            if (result != null){
                if (statusCode == 200){
                    try {

                        rspListServiceImports = new RspListServiceImports();
                        if (!rspListServiceImports.getRspListImports(result, rspHeader)) {
                            ((TransView) activity).showError(((TransView) activity).getMsgScreendocument().getTransEname(), String.valueOf(TcodeError.T_ERR_UNPACK_JSON), TransUIImpl.getErrInfo(String.valueOf(TcodeError.T_ERR_UNPACK_JSON)));
                        }

                    } catch (Exception e) {
                        Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                    }
                }else {
                    ((TransView)activity).deleteTimer();
                    validateError(result,statusCode);
                }
                waitConsumosApi.getVeryBank(statusCode);
            }else {
                ((TransView)activity).deleteTimer();
                ((TransView)activity).getListener().cancel(UIUtils.validateResponseError(statusCode));
            }
        });
    }

    //CONSUMO DE API PARA PAGO SERVICIO IMPORTE LIST SERVICES DOCUMENTS

    private void consumoListServiDocuent(){
        String affiliationCode = "affiliationCode="+ classArguments.getAffiliationCode();

        RequestWs requestWs = new RequestWs(getActivity(), ROOT + ((TransView)activity).getMsgScreendocument().getListUrl().get(2).getMethod(), TIMEOUT, true);
        requestWs.httpRequetsStringGet(((TransView) activity).getMsgScreendocument().getHeader(), affiliationCode, (result, statusCode,rspHeader) -> {
            waitTransFragment.setOpnNumber();
            if (result != null){
                if (statusCode == 200){
                    try {

                        rspListServiceDocuments = new RspListServiceDocuments();
                        if (!rspListServiceDocuments.getRspListSerDocu(result, rspHeader)) {
                            ((TransView) activity).showError(((TransView) activity).getMsgScreendocument().getTransEname(), String.valueOf(TcodeError.T_ERR_UNPACK_JSON), TransUIImpl.getErrInfo(String.valueOf(TcodeError.T_ERR_UNPACK_JSON)));
                        }

                    } catch (Exception e) {
                        Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                    }
                }else {
                    ((TransView)activity).deleteTimer();
                    validateError(result,statusCode);
                }
                waitConsumosApi.getVeryBank(statusCode);
            }
        });
    }

    //CONSUMO DE API PARA PAGO SERVICIO VERIFY PAYMENT
    private void consumoVerifypaymen(String tipoServicio ){

        RequestWs requestWs = new RequestWs(getActivity(), ROOT + ((TransView)activity).getMsgScreendocument().getListUrl().get(8).getMethod(), TIMEOUT, true);
        requestWs.httpRequets(((TransView)activity).getMsgScreendocument().getHeader(), setVerifypayment(tipoServicio), (result, statusCode,header) -> {
            waitTransFragment.setOpnNumber();
            if (result != null){
                if (statusCode == 200){
                    try {
                        ((TransView)activity).counterDownTimer(((TransView)activity).getMsgScreendocument().getTimeOut(), MSGSCREENTIMER);
                        rspVerifyPayment = new RspVerifyPayment();

                        if (rspVerifyPayment.getRspVeryfyPaymentPS(result,classArguments.getTypepayment(),classArguments.getTipoPagoServicio(),header)){

                            classArguments.setEmpresa(rspVerifyPayment.getRspcompanyName());
                            classArguments.setCodTypeService(rspVerifyPayment.getRspclientDepositCode());
                            classArguments.setSimbolTotal(rspVerifyPayment.getRspamountCurrencySymbol());
                            classArguments.setMonto(rspVerifyPayment.getRspamount());
                            classArguments.setSimOtroCargo(rspVerifyPayment.getRspextraChargeAmountCurrencySymbol());
                            classArguments.setOtroCargo(rspVerifyPayment.getRspextraChargeAmount());
                            classArguments.setSimbcomision(rspVerifyPayment.getRspcommissionCurrencySymbol());
                            classArguments.setComissionAmount(rspVerifyPayment.getRspcommissionAmount());
                            classArguments.setSimbolTotal(rspVerifyPayment.getRsptotalAmountCurrencySymbol());
                            classArguments.setTotalPagar(rspVerifyPayment.getRsptotalAmount());
                            classArguments.setStatusTipoCambio(rspVerifyPayment.getRspexchangeStatus());
                            if(rspVerifyPayment.getRspclientDepositName() != null && !rspVerifyPayment.getRspclientDepositName().equals("")){
                                classArguments.setClientDepositName(rspVerifyPayment.getRspclientDepositName());
                            }

                        }else {
                            ((TransView)activity).deleteTimer();
                            ((TransView)activity).getListener().cancel(TcodeError.T_ERR_UNPACK_JSON);
                        }
                    } catch (JSONException e) {
                        Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                    }
                }else {
                    validateError(result,statusCode);
                }
                waitConsumosApi.getVeryBank(statusCode);
            }else {
                ((TransView)activity).deleteTimer();
                ((TransView)activity).getListener().cancel(UIUtils.validateResponseError(statusCode));
            }
        });
    }

    //CONSUMO DE API PARA PAGO SERVICIO OBTAIN AMOUNT LIMITS
    private void consumoObtainAmount(){

        progressPS.playAnimation();
        RequestWs requestWs = new RequestWs(getActivity(), ROOT + ((TransView)activity).getMsgScreendocument().getListUrl().get(4).getMethod(), TIMEOUT, true);
        requestWs.httpRequets(((TransView)activity).getMsgScreendocument().getHeader(), setObtainAmount(), (result, statusCode,header) -> {
            waitTransFragment.setOpnNumber();
            if (result != null){
                if (statusCode == 200){
                    try {
                        ((TransView)activity).counterDownTimer(((TransView)activity).getMsgScreendocument().getTimeOut(), MSGSCREENTIMER);
                        rspObtainAmountLimits = new RspObtainAmountLimits();

                        if (rspObtainAmountLimits.getObtainAmountLimits(result,header)){

                            classArguments.setMinAmount(rspObtainAmountLimits.getMinimumAmount());
                            classArguments.setMaxAmount(rspObtainAmountLimits.getMaximumAmount());

                        }else {
                            ((TransView)activity).deleteTimer();
                            ((TransView)activity).getListener().cancel(TcodeError.T_ERR_UNPACK_JSON);
                        }
                    } catch (JSONException e) {
                        Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                    }
                }else {
                    validateError(result,statusCode);
                }
                waitConsumosApi.getVeryBank(statusCode);
            }else {
                ((TransView)activity).deleteTimer();
                ((TransView)activity).getListener().cancel(UIUtils.validateResponseError(statusCode));
            }
        });
    }

    private JSONObject setVerifypayment(String tipoServicio ){
        final ReqVerifyPayment reqVerifyPayment = new ReqVerifyPayment();

        if(tipoServicio.equals("06")){
            reqVerifyPayment.setDebtcode(classArguments.getDebcode());
            reqVerifyPayment.setCtnPaymentType(classArguments.getTypepayment());
        }

        if (tipoServicio.equals("07")){
            if (listsDebt != null)
                reqVerifyPayment.setCtnImportCode(listsDebt.getImportCode());
            reqVerifyPayment.setCtnClientDepositCode(classArguments.getDocumentCode());
        }
        return reqVerifyPayment.buildsJsonObject(classArguments.getTipoPagoServicio());
    }

    private JSONObject setObtainAmount(){
        final ReqObtainAmountLimits reqObtainAmountLimits = new ReqObtainAmountLimits();

        reqObtainAmountLimits.setClientDepositCode(classArguments.getCodTypeService());
        reqObtainAmountLimits.setAffiliationCode(classArguments.getAffiliationCode());

        return reqObtainAmountLimits.buildsJsonObject();
    }

    private void validateError(JSONObject jsonObject , int statuscode){

        switch (statuscode){
            case TcodeError.T_INTERNAL_SERVER_ERR:
            case TcodeError.T_ERR_BAD_REQUEST:
            case TcodeError.T_ERR_NO_FOUND:
                JsonError jsonError =  new JsonError();
                try {
                    if(!jsonError.getRspObtJson(jsonObject)) {
                        ((TransView)activity).showError(((TransView)activity).getMsgScreendocument().getTransEname(),String.valueOf(TcodeError.T_ERR_UNPACK_JSON), TransUIImpl.getErrInfo(String.valueOf(TcodeError.T_ERR_UNPACK_JSON)));
                        return;
                    }
                } catch (JSONException e) {
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                }
                ((TransView)activity).showError(((TransView)activity).getMsgScreendocument().getTransEname(),jsonError.getCode(),jsonError.getDescription());
                break;
            default:
                break;
        }
    }

    private JSONObject setListClientDebts(String tipoServicio ){

        final ReqListClientDebts reqListClientDebts = new ReqListClientDebts();

        //PARA PAGO CON RANGO SE ENVIA ESTO
        if(tipoServicio.equals("06")){
            reqListClientDebts.setClientDepositCode(txtDescriptionService.getText().toString());
            reqListClientDebts.setAffiliationCode(classArguments.getAffiliationCode());
        }

        return reqListClientDebts.builJsonObject();
    }

    //MENSAJE CUANDO LLEGUE UNA SOLA EN LA  LISTA DE DEUDAS
    private void msgAccount(View view, String msg2,String msg3){

        relativeFondo.setVisibility(View.VISIBLE);

        close.setVisibility(View.GONE);
        msgRelative.setVisibility(View.VISIBLE);

        TextView tvMsg1 = view.findViewById(R.id.msg1Screen);
        TextView tvMsg2 = view.findViewById(R.id.msg2Screen);
        TextView tvMsg3 = view.findViewById(R.id.msg3Screen);

        tvMsg1.setVisibility(View.GONE);
        tvMsg2.setText(msg2);
        tvMsg3.setText(msg3);

        Button btnEntentido = view.findViewById(R.id.entendidoMsg);
        btnCambiarCod.setText("Cambiar código");

        relativeFondo.setOnClickListener(v -> {
            //nothing
        });

        if(!clicks){
            if(conteoClicks <= 1){
                btnCambiarCod.setOnClickListener(v -> {
                    //accion al presionar el boton cambiar documento
                    relativeFondo.setVisibility(View.GONE);
                    msgRelative.setVisibility(View.GONE);
                    linDeudasPendientes.setVisibility(View.GONE);
                    relativeKeyboardNume.setVisibility(View.GONE);
                    relativeKeyboardAlfa.setVisibility(View.GONE);
                    ((TransView)activity).setKeyboard(txtDescriptionService, 11,false,false, ((TransView)activity).getMsgScreendocument().isAlfa(), minEtInputuser,relativeKeyboardNume,relativeKeyboardAlfa);
                    conteoClicks++;
                });
            } else {
                relativeFondo.setVisibility(View.GONE);
                ((TransView)activity).showError("Sesión finalizada","xxx00","no te quedan mas intentos de ingreso de documento");
            }
        }

        btnEntentido.setOnClickListener(v -> {
            msgRelative.setVisibility(View.GONE);
            ((TransView)activity).deleteTimer();
            ((TransView)activity).showError("Sesión finalizada","xxx00","no te quedan mas intentos de ingreso de código de servicio");
        });
    }
}