package com.bcp.transactions.giros.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.android.newpos.pay.R;
import com.bcp.document.ClassArguments;
import com.bcp.document.MsgScreenDocument;
import com.bcp.rest.giros.emision.request.ReqObtainPersonEmision;
import com.bcp.rest.giros.emision.request.ReqVerifyBankEmision;
import com.bcp.rest.giros.emision.response.RspObtainPersonEmision;
import com.bcp.rest.giros.emision.response.RspVerifyBankEmision;
import com.bcp.rest.httpclient.RequestWs;
import com.bcp.settings.BCPConfig;
import com.bcp.teclado_alfanumerico.ClickSpinner;
import com.bcp.transactions.callbacks.WaitConsumosApi;
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


public class FragmentBeneficiaryDataEmision extends Fragment {

    //Toolbar
    Button btnContinuar;
    TextView tvInputDocument;
    TextView btnAcceptInputUser;
    TextView nombreBenef;
    EditText nameBeneficiario;
    ImageButton btnConti;
    ImageButton btnTipOrden;
    ImageButton btnTipoDoc;
    LinearLayout linearLayoutProgress;
    LinearLayout linnombreBenef;
    LottieAnimationView gifProgressBen;
    LinearLayout linearBotones;
    LinearLayout linearDoc;
    RelativeLayout relativeTipOrden;
    TextView etTitle;
    TextView titleOrdenante;
    TextView selectDni;

    EditText tipoDoc;
    EditText numberDoc;
    ImageView close;
    String[] arrayTipoDoc;
    String name;
    String typeCoin;

    TransView transView;
    MsgScreenDocument msgScreenDocument;

    private RelativeLayout relativeKeyboardNume;
    private RelativeLayout relativeKeyboardAlfa;
    int minEtInputuser;
    RspObtainPersonEmision rspObtainPersonEmision;
    RspVerifyBankEmision rspVerifyBank;
    ClassArguments classArguments;

    View vista;
    Context thiscontext;
    Activity activity;
    ClickSpinner clickSpinner;
    WaitConsumosApi waitConsumosApi;
    private boolean isClient;
    WaitTransFragment waitTransFragment;
    FrameLayout frameLayout;
    private static final  String MSGSCREENTIMER = "FragmentBeneficiaryDataEmision";

    public FragmentBeneficiaryDataEmision(){
        //Constructor de la clase
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        nombreBenef = null;
        nameBeneficiario = null;
        btnConti = null;
        btnTipOrden = null;
        btnTipoDoc = null;
        linearLayoutProgress = null;
        linnombreBenef = null;
        gifProgressBen = null;
        linearBotones = null;
        linearDoc = null;
        relativeTipOrden = null;
        etTitle = null;
        titleOrdenante = null;
        selectDni = null;

        tipoDoc = null;
        numberDoc = null;
        close = null;
        arrayTipoDoc = null;
        name = null;
        typeCoin = null;

        transView = null;
        msgScreenDocument = null;

        relativeKeyboardNume = null;
        relativeKeyboardAlfa = null;
        minEtInputuser = 0;
        rspObtainPersonEmision = null;
        rspVerifyBank = null;
        classArguments = null;

        vista = null;
        thiscontext = null;
        activity = null;
        clickSpinner = null;
        waitConsumosApi = null;
        isClient = false;
        waitTransFragment = null;
        frameLayout = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_beneficiary_data_emision, container, false);

        frameLayout = vista.findViewById(R.id.fragmentDBE);

        btnContinuar          = vista.findViewById(R.id.Btncontinuar);
        numberDoc             = vista.findViewById(R.id.NumeroDocument);
        tvInputDocument       = vista.findViewById(R.id.doc_numero);
        btnAcceptInputUser    = vista.findViewById(R.id.continuar);
        btnConti              = vista.findViewById(R.id.continuarAlfa);
        relativeKeyboardNume  = vista.findViewById(R.id.keyboardNumerico);
        relativeKeyboardAlfa  = vista.findViewById(R.id.keyboardAlfa);
        gifProgressBen        = vista.findViewById(R.id.gifProcesandoBen);
        linearLayoutProgress  = vista.findViewById(R.id.procesandoTransBen);
        linnombreBenef        =  vista.findViewById(R.id.linearOrdenPago);
        nameBeneficiario      = vista.findViewById(R.id.TxtSpinnerBen);
        btnTipOrden           = vista.findViewById(R.id.TipoOrdenGiro);
        linearBotones         = vista.findViewById(R.id.linearBotonesGiros);
        relativeTipOrden      = vista.findViewById(R.id.relativeTipOrden);
        tipoDoc               = vista.findViewById(R.id.textSpinnerdoc);
        etTitle               = vista.findViewById(R.id.title_toolbar);
        btnTipoDoc            =  vista.findViewById(R.id.tipDocumt);
        close                 = vista.findViewById(R.id.iv_close);
        linearDoc             = vista.findViewById(R.id.linearDocument);
        nombreBenef           = vista.findViewById(R.id.txtNomApellBenef);
        titleOrdenante        = vista.findViewById(R.id.ordenPagoGiro);
        selectDni             = vista.findViewById(R.id.selectBeneficiary);
        arrayTipoDoc          = getResources().getStringArray(R.array.typeDocumen);

        numberDoc.setBackgroundResource(R.drawable.btn_spinner_first);

        close.setVisibility(View.VISIBLE);

        etTitle.setText(getResources().getString(R.string.datosBeneficiario));
        titleOrdenante.setText(getResources().getString(R.string.nomApellBene));
        minEtInputuser = msgScreenDocument.getMinLength();


        return vista;
    }

    @SuppressLint("ClickableViewAccessibility")
    public  void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transView.deleteTimer();

        (transView).counterDownTimer(msgScreenDocument.getTimeOut(), MSGSCREENTIMER);

        classArguments = (transView).getArguments();

        //se instancia el callback para obtener el sesionId y el header
        waitTransFragment = msgScreenDocument.getCallbackTransFragment();

        btnAcceptInputUser.setOnClickListener(v -> {
            if(!nameBeneficiario.getText().toString().equals("")){
                relativeKeyboardAlfa.setVisibility(View.GONE);
                btnContinuar.setEnabled(true);
            }else {
                if (!(transView).longMin(numberDoc)) {
                    relativeKeyboardNume.setVisibility(View.GONE);
                    consumoApi();
                    numberDoc.setEnabled(false);
                    numberDoc.setBackgroundResource(R.drawable.btn_spinner_estados);
                    btnContinuar.setEnabled(true);
                }
            }

        });
        btnConti.setOnClickListener(v -> {
            if(!nameBeneficiario.getText().toString().equals("")){
                relativeKeyboardAlfa.setVisibility(View.GONE);
                btnContinuar.setEnabled(true);
            }else {
                if (!(transView).longMin(numberDoc)) {
                    relativeKeyboardAlfa.setVisibility(View.GONE);
                    consumoApi();
                    numberDoc.setEnabled(false);
                    numberDoc.setBackgroundResource(R.drawable.btn_spinner_estados);
                    btnContinuar.setEnabled(true);
                }
            }
        });
        btnContinuar.setOnClickListener(v -> {
            try {
                btnContinuar.setEnabled(false);
                classArguments.setTypeDocumentBeneficiary(tipoDoc.getText().toString());
                classArguments.setDniBeneficiary(numberDoc.getText().toString());
                classArguments.setNameBeneficiary(nameBeneficiario.getText().toString());

                waitConsumosApi = null;
                waitConsumosApi = retval -> {
                    if(retval == 200){
                        classArguments.setTypetransaction(1);
                        typeCoin =classArguments.getTypeCoin();
                        classArguments = transView.getArguments();
                        if(classArguments.getTypepayment().equals("2")){

                            NavHostFragment.findNavController(FragmentBeneficiaryDataEmision.this)
                                    .navigate(R.id.action_fragmentBeneficiaryDataEmision2_to_fragmentDetailGiro);

                        }else{
                            NavHostFragment.findNavController(FragmentBeneficiaryDataEmision.this)
                                    .navigate(R.id.action_fragmentBeneficiaryDataEmision_to_fragmentDetalleGiro);
                        }
                    }
                };
                consumeApiVerifyBank();
            }catch (Exception e){
                ((TransView)activity).getListener().cancel(TcodeError.T_ERR_DATA_NULL);
                Logger.logLine(Logger.LOG_EXECPTION, "FragmentBeneficiaryDataEmision");
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            }

        });

        nombreBenef.setOnClickListener(v -> {
            nombreBenef.setText("");
            (transView).setKeyboard(nameBeneficiario,50,false,false,true, 1,null,relativeKeyboardAlfa);
            nameBeneficiario.setHint(R.string.nombreOrdenante);
        });

        close.setOnClickListener(view12 -> {
            (transView).deleteTimer();
            (transView).getListener().cancel(TcodeError.T_USER_CANCEL);
        });

        if(classArguments.getDniBeneficiary() != null) {
            tipoDoc.setText(classArguments.getTypeDocumentBeneficiary());
            linearDoc.setVisibility(View.VISIBLE);
            numberDoc.setText(classArguments.getDniBeneficiary());
            numberDoc.setBackgroundResource(R.drawable.btn_spinner_estados);
            tipoDoc.setBackgroundResource(R.drawable.btn_spinner_estados);
            linnombreBenef.setVisibility(View.VISIBLE);
            btnTipOrden.setVisibility(View.GONE);
            nameBeneficiario.setText(classArguments.getNameBeneficiary());
            btnContinuar.setEnabled(true);

        }else {
            classArguments = (transView).getArguments();
            (transView).setKeyboard(numberDoc, msgScreenDocument.getMaxLength(),false,false, msgScreenDocument.isAlfa(), minEtInputuser,relativeKeyboardNume,relativeKeyboardAlfa);

            if (msgScreenDocument.isBanner()){
                clickSpinner = new ClickSpinner(tipoDoc,selectDni,btnTipoDoc,vista.findViewById(R.id.linearSpinnerDoc),vista.findViewById(R.id.relativeTipoDocumet),arrayTipoDoc,(Activity) thiscontext);
                clickSpinner.setDocument(tvInputDocument,linearDoc,relativeKeyboardNume,relativeKeyboardAlfa);
                clickSpinner.setClickKeyboard((transView).getKeyboards());
                clickSpinner.spinnerBotones(Trans.GIROS_EMISION,"Beneficiario");
            }
        }
    }

    private void consumoApi(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en reqObtainPerson ");
        btnTipOrden.setVisibility(View.GONE);
        linearLayoutProgress.setVisibility(View.VISIBLE);
        gifProgressBen.playAnimation();

        RequestWs requestWs = new RequestWs(getActivity(), ROOT + msgScreenDocument.getListUrl().get(1).getMethod(), TIMEOUT, true);
        requestWs.httpRequets(waitTransFragment.headerTrans(true), setObtainPerson(), (result, statusCode, header) -> {
            waitTransFragment.setOpnNumber();
            Logger.logLine(Logger.LOG_GENERAL, " Saliendo de reqObtainPerson ");
            if (result != null){
                if (statusCode == 200){
                    try {
                        (transView).counterDownTimer(msgScreenDocument.getTimeOut(), MSGSCREENTIMER);
                        rspObtainPersonEmision = new RspObtainPersonEmision();
                        if (rspObtainPersonEmision.getRspobtainPerson(result,header)){

                            name = rspObtainPersonEmision.getRspname();

                            if(name != null && !name.equals("")) {
                                isClient = true;
                                linearLayoutProgress.setVisibility(View.GONE);
                                linnombreBenef.setVisibility(View.VISIBLE);
                                nameBeneficiario.setText(name);
                                nombreBenef.setEnabled(false);
                                nameBeneficiario.setEnabled(false);
                            }else {
                                isClient = false;
                                nameBeneficiario.setEnabled(true);
                                nombreBenef.setEnabled(true);
                                linearLayoutProgress.setVisibility(View.GONE);
                                gifProgressBen.pauseAnimation();
                                btnContinuar.setEnabled(false);
                                linnombreBenef.setVisibility(View.VISIBLE);
                                nameBeneficiario.setHint(R.string.nombreOrdenante);
                            }
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
    }

    private void consumeApiVerifyBank(){
        Logger.logLine(Logger.LOG_GENERAL, " Entrando en consumeApiVerifyBank ");
        RequestWs requestWs = new RequestWs(getActivity(), ROOT + msgScreenDocument.getListUrl().get(2).getMethod(), TIMEOUT, true);
        requestWs.httpRequets(waitTransFragment.headerTrans(true), setVerifyBankDraft(), (result, statusCode, header) -> {
            waitTransFragment.setOpnNumber();
            Logger.logLine(Logger.LOG_GENERAL, " Saliendo de consumeApiVerifyBank ");
            if (result != null){
                if (statusCode == 200){
                    try {
                        (transView).counterDownTimer(msgScreenDocument.getTimeOut(), MSGSCREENTIMER);
                        rspVerifyBank = new RspVerifyBankEmision();
                        if (rspVerifyBank.getRspVerifyBank(result)){

                            classArguments.setNameBeneficiary(rspVerifyBank.getRspNameBenef());
                            classArguments.setNameRemitter(rspVerifyBank.getRspNameRemitter());
                            classArguments.setNameSender(rspVerifyBank.getRspNameSender());
                            classArguments.setDniBeneficiary(rspVerifyBank.getRspDocumentNumberBenef());
                            classArguments.setDniRemitter(rspVerifyBank.getRspDocumentNumber_Remitter());
                            classArguments.setDniSender(rspVerifyBank.getRspDocumentNumberSender());
                            classArguments.setEditBenef(rspVerifyBank.getRspEditableBenef());
                            classArguments.setEditRemit(rspVerifyBank.getRspEditableRemitter());
                            classArguments.setEditSender(rspVerifyBank.getRspEditableSender());
                            classArguments.setEditEditable(rspVerifyBank.getRspEditable());
                            classArguments.setTotal(rspVerifyBank.getRspTotal());
                            classArguments.setComissionAmount(rspVerifyBank.getRsCommissionAmount());
                            classArguments.setSimbcomision(rspVerifyBank.getRspCommissionCurrencySymbol());
                            classArguments.setSimbolTotal(rspVerifyBank.getRspTotalCurrencySymbol());
                            classArguments.setTotalPagar(rspVerifyBank.getRspTotalAmount());
                            classArguments.setSimboltotalPagar(rspVerifyBank.getRspTotalAmountCurrencySymbol());
                            classArguments.setTipoCambio(rspVerifyBank.getRspExchangeRate());
                            classArguments.setTypeDocumentBeneficiary(rspVerifyBank.getRspDocumentTypeDesBenef());
                            classArguments.setTypeDocumentRemitter(rspVerifyBank.getRspDocumentTypeDesRemitter());
                            classArguments.setTypeDocumentSender(rspVerifyBank.getRspDocumentTypeDes_Sender());
                            (transView).setArgumentsClass(classArguments);

                            btnTipoDoc.setEnabled(false);
                        }else {
                            (transView).deleteTimer();
                            (transView).getListener().cancel(TcodeError.T_ERR_UNPACK_JSON);
                            return;
                        }
                    } catch (JSONException e) {
                        Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                    }
                }else {
                    (transView).getListener().cancel(result,header,statusCode);
                    return;
                }
                waitConsumosApi.getVeryBank(statusCode);
            }else {
                (transView).deleteTimer();
                (transView).getListener().cancel(UIUtils.validateResponseError(statusCode));
            }
        });
    }

    private JSONObject setObtainPerson(){
        final ReqObtainPersonEmision reqObtainPersonEmision = new ReqObtainPersonEmision();

        reqObtainPersonEmision.setReqactor("3");
        reqObtainPersonEmision.setReqdocumentType(documentType(tipoDoc.getText().toString()));
        reqObtainPersonEmision.setReqdocumentNumber(numberDoc.getText().toString());

        return reqObtainPersonEmision.buildsObjectJSON();
    }

    private JSONObject setVerifyBankDraft(){
        final ReqVerifyBankEmision reqVerifyBankEmision = new ReqVerifyBankEmision();

        reqVerifyBankEmision.setReqCurrencyCode(classArguments.getTypeCoin());
        reqVerifyBankEmision.setReqAmount(classArguments.getMonto());
        reqVerifyBankEmision.setReqName(classArguments.getNameRemitter());
        reqVerifyBankEmision.setReqNameBeneficiary(nameBeneficiario.getText().toString());
        reqVerifyBankEmision.setReqNameSender(classArguments.getNameSender());
        if (classArguments.getTypepayment().equals("1"))
            reqVerifyBankEmision.setReqTrack2(BCPConfig.getInstance(getContext()).getTrack2Agente(BCPConfig.TRACK2AGENTEKEY) + "");
        else {
            reqVerifyBankEmision.setReqFamilyCode(classArguments.getSelectedAccountItem().getFamilyCode());
            reqVerifyBankEmision.setReqCurrencyCodeAccount(classArguments.getSelectedAccountItem().getCurrencyCode());
        }
        //flags para saber si son clientes de bcp
        reqVerifyBankEmision.setBeneficiary(isClient);
        reqVerifyBankEmision.setRemitter(classArguments.isFlag1());
        reqVerifyBankEmision.setSender(classArguments.isFlag2());

        return reqVerifyBankEmision.buildsObjectJSON();
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
}