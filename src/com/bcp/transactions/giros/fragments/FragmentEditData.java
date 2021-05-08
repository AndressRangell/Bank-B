package com.bcp.transactions.giros.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.bcp.transactions.callbacks.WaitTypeDocument;
import com.newpos.libpay.Logger;
import com.newpos.libpay.presenter.TransView;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.Trans;

import org.json.JSONException;
import org.json.JSONObject;

import cn.desert.newpos.payui.UIUtils;

import static com.bcp.rest.JsonUtil.ROOT;
import static com.bcp.rest.JsonUtil.TIMEOUT;

public class FragmentEditData extends Fragment {

    private Context thiscontext;
    private Activity activity;
    private ClassArguments classArguments;
    private RspObtainPersonEmision rspObtainPersonEmision;
    private RspVerifyBankEmision rspVerifyBank;
    private WaitConsumosApi waitVerifyBank;
    private View vista;

    //Toolbar
    private Button btnContinuar;
    private TextView tvInputDocument;
    private TextView btnAcceptInputUser;
    private TextView nombreBenef;
    private EditText nameRegister;
    private ImageButton btnConti;
    private ImageButton btnTipOrden;
    private ImageButton btnTipoDoc;
    private LinearLayout linearLayoutProgress;
    private LinearLayout linnombreBenef;
    private LottieAnimationView gifProgressBen;
    LinearLayout linearBotones;
    private LinearLayout linearDoc;
    RelativeLayout relativeTipOrden;
    TextView etTitle;
    TextView titleOrdenante;
    TextView tittleDoc;
    private TextView selectDni;
    private TextView numeroDni;
    private RelativeLayout relativeKeyboardNume;
    private RelativeLayout relativeKeyboardAlfa;
    int minEtInputuser;
    int maxEtInputuser;
    private EditText tipoDoc;
    private EditText numberDoc;
    ImageView close;
    private String[] arrayTipoDoc;
    private Boolean nombre = false;
    private String document;
    private String dateName;
    ClickSpinner clickSpinner;
    private boolean isClient;
    private String name = "";

    private WaitTransFragment waitTransFragment;
    private static final String MSGSCREENTIMER = "FragmentBeneficiaryDataEmision";
    protected WaitTypeDocument callbackTypeDocument;

    TransView transView;
    MsgScreenDocument msgScreenDocument;

    public FragmentEditData(){
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
         thiscontext = null;
         activity = null;
         classArguments = null;
         rspObtainPersonEmision = null;
         rspVerifyBank = null;
         waitVerifyBank = null;
         vista = null;

        //Toolbar
          btnContinuar = null;
          tvInputDocument = null;
          btnAcceptInputUser = null;
          nombreBenef = null;
          nameRegister = null;
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
          tittleDoc = null;
          selectDni = null;
          numeroDni = null;
          relativeKeyboardNume = null;
          relativeKeyboardAlfa = null;
          minEtInputuser = 0;
          maxEtInputuser = 0;
          tipoDoc = null;
          numberDoc = null;
          close = null;
          arrayTipoDoc = null;
          nombre = false;
          document = null;
          dateName = null;
          clickSpinner = null;
          isClient = false;
          name = null;

          waitTransFragment = null;
          callbackTypeDocument = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_edit_data, container, false);

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
        nameRegister          = vista.findViewById(R.id.TxtSpinnerBen);
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
        numeroDni             = vista.findViewById(R.id.numeroDni);
        tittleDoc             = vista.findViewById(R.id.tip_doc);

        numberDoc.setBackgroundResource(R.drawable.btn_spinner_first);

        close.setVisibility(View.VISIBLE);
        minEtInputuser = ((TransView)activity).getMsgScreendocument().getMinLength();

        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        transView.deleteTimer();

        transView.counterDownTimer(msgScreenDocument.getTimeOut(), MSGSCREENTIMER);

        classArguments = transView.getArguments();

        //se instancia el callback para obtener el sesionId y el header
        waitTransFragment = msgScreenDocument.getCallbackTransFragment();

        callbackTypeDocument = new WaitTypeDocument() {
            @Override
            public void getTypeDocument() {
                fillScreenOrders();
            }

            @Override
            public void cleanData(String typeSpinner) {
                //nothing
            }
        };

        close.setOnClickListener(view14 -> {
            transView.deleteTimer();
            transView.getListener().cancel(TcodeError.T_USER_CANCEL);
        });
        try {
            btnContinuar.setOnClickListener(v -> {

                btnContinuar.setEnabled(false);

                waitVerifyBank = null;
                waitVerifyBank = retval -> {
                    if(retval == 200){
                        if(classArguments.getTypepayment().equals("2")){
                            NavHostFragment.findNavController(FragmentEditData.this)
                                    .navigate(R.id.action_fragmentEditData2_to_fragmentDetailGiro);
                        }else {
                            NavHostFragment.findNavController(FragmentEditData.this)
                                    .navigate(R.id.action_fragmentEditData_to_fragmentDetalleGiro);
                        }
                    }
                };
                consumeApiVerifyBank();
            });
        }catch (Exception e){
            transView.getListener().cancel(TcodeError.T_ERR_DATA_NULL);
            Logger.logLine(Logger.LOG_EXECPTION, "FragmentEditData");
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }


        if (classArguments.getEditableGiros().equals("0"))
            arrayTipoDoc = getResources().getStringArray(R.array.typeDocumen);
        else
            arrayTipoDoc = getResources().getStringArray(R.array.typeDocumenEmision);

        spinner();

        switch (classArguments.getEditableGiros()){
            case "0":
                //datos beneficiario
                tipoDoc.setText(classArguments.getTypeDocumentBeneficiary());
                linearDoc.setVisibility(View.VISIBLE);
                btnTipOrden.setVisibility(View.GONE);
                numberDoc.setText(classArguments.getDniBeneficiary());
                linnombreBenef.setVisibility(View.VISIBLE);
                nameRegister.setText(classArguments.getNameBeneficiary());
                numberDoc.setBackgroundResource(R.drawable.btn_spinner_estados);
                tipoDoc.setBackgroundResource(R.drawable.btn_spinner_estados);
                btnTipoDoc.setBackgroundResource(R.drawable.ic_flecha_abajo);
                document = classArguments.getDniBeneficiary();
                dateName = classArguments.getNameBeneficiary();
                btnContinuar.setEnabled(true);
                selectDni.setEnabled(true);
                numeroDni.setEnabled(true);
                etTitle.setText(getResources().getString(R.string.datosBeneficiario));
                titleOrdenante.setText(getResources().getString(R.string.nomApellBene));
                tittleDoc.setText(getResources().getString(R.string.tipoDocBenf));
                break;
            case "1":
                //datos solicitante
                tipoDoc.setText(classArguments.getTypeDocumentSender());
                numberDoc.setText(classArguments.getDniSender());
                linnombreBenef.setVisibility(View.VISIBLE);
                btnTipOrden.setVisibility(View.GONE);
                nameRegister.setText(classArguments.getNameSender());
                document = classArguments.getDniSender();
                dateName = classArguments.getNameSender();
                nombreBenef.setEnabled(true);
                numeroDni.setEnabled(true);
                btnContinuar.setEnabled(true);
                selectDni.setEnabled(true);
                btnTipOrden.setVisibility(View.GONE);
                etTitle.setText(getResources().getString(R.string.tittleSender));
                titleOrdenante.setText(getResources().getString(R.string.nomApell));
                tittleDoc.setText(getResources().getString(R.string.tipoDocSoli));
                break;
            case "2":
                //datos ordenante
                tipoDoc.setText(classArguments.getTypeDocumentRemitter());
                linearDoc.setVisibility(View.VISIBLE);
                btnTipOrden.setVisibility(View.GONE);
                numberDoc.setText(classArguments.getDniRemitter());
                numberDoc.setBackgroundResource(R.drawable.btn_spinner_estados);
                tipoDoc.setBackgroundResource(R.drawable.btn_spinner_estados);
                btnConti.setEnabled(false);
                selectDni.setEnabled(true);
                numeroDni.setEnabled(true);
                linnombreBenef.setVisibility(View.VISIBLE);
                nameRegister.setText(classArguments.getNameRemitter());
                document = classArguments.getDniRemitter();
                dateName = classArguments.getNameRemitter();
                btnContinuar.setEnabled(true);
                etTitle.setText(getResources().getString(R.string.tittleRemitter));
                titleOrdenante.setText(getResources().getString(R.string.NomOrdenante));
                tittleDoc.setText(getResources().getString(R.string.TipoDocOrdenant));
                break;
            default:
                break;
        }

        fillTextView();

        validateLongMaxMin(tipoDoc.getText().toString());

        btnAcceptInputUser.setOnClickListener(view12 -> {
            if (transView.longMin(numberDoc)) {
                return;
            } else if(!numberDoc.getText().toString().equals(document) && nameRegister.getText().toString().equals(dateName)) {
                nameRegister.setText("");
                consumoApi();
                nombre = false;
            }
            relativeKeyboardNume.setVisibility(View.GONE);
        });

        btnConti.setOnClickListener(view13 -> {
            if (transView.longMin(numberDoc)) {
                return;
            } else if(!numberDoc.getText().toString().equals(document) && nameRegister.getText().toString().equals(dateName)) {
                nameRegister.setText("");
                consumoApi();
                nombre = false;
            }
            relativeKeyboardAlfa.setVisibility(View.GONE);
        });

        numeroDni.setOnClickListener(view15 -> {
            validateLongMaxMin(tipoDoc.getText().toString());
            nombre = false;
            transView.setKeyboard(numberDoc, maxEtInputuser,false,false, (!tipoDoc.getText().toString().equals("DNI") && !tipoDoc.getText().toString().equals("RUC")), minEtInputuser,relativeKeyboardNume,relativeKeyboardAlfa);
            btnContinuar.setEnabled(true);
            linnombreBenef.setVisibility(View.GONE);
        });

        nombreBenef.setOnClickListener(view14 -> {
            if(numberDoc.getText().toString().equals(document) || Boolean.FALSE.equals(nombre)){
                nameRegister.setText("");
                transView.setKeyboard(nameRegister,50,false,false,true, 1,null,relativeKeyboardAlfa);
                btnContinuar.setEnabled(true);
                nameRegister.setHint(R.string.nombreOrdenante);
            }
        });
    }

    private void fillTextView(){
        switch (tipoDoc.getText().toString()){
            case "DNI":
                switch (classArguments.getEditableGiros()){
                    case "0":
                        tvInputDocument.setText(getResources().getString(R.string.numDniBenef));
                        break;
                    case "1":
                        tvInputDocument.setText(getResources().getString(R.string.numDNISolici));
                        break;
                    case "2":
                        tvInputDocument.setText(getResources().getString(R.string.numDNIOrdenate));
                        break;
                    default:break;
                }
                break;
            case "Carnet de Extranjería":
            case "Carnet de extranjería":
                switch (classArguments.getEditableGiros()){
                    case "0":
                        tvInputDocument.setText(getResources().getString(R.string.numCarnetBenef));
                        break;
                    case "1":
                        tvInputDocument.setText(getResources().getString(R.string.numEXTSolici));
                        break;
                    case "2":
                        tvInputDocument.setText(getResources().getString(R.string.numEXTOrdenate));
                        break;
                    default:break;
                }
                break;
            case "Pasaporte":
                switch (classArguments.getEditableGiros()){
                    case "0":
                        tvInputDocument.setText(getResources().getString(R.string.numPasaportBenef));
                        break;
                    case "1":
                        tvInputDocument.setText(getResources().getString(R.string.numPASSolici));
                        break;
                    case "2":
                        tvInputDocument.setText(getResources().getString(R.string.numPASOrdenate));
                        break;
                    default:break;
                }
                break;
            case "RUC":
                switch (classArguments.getEditableGiros()){
                    case "0":
                        tvInputDocument.setText(getResources().getString(R.string.numRucBenef));
                        break;
                    case "1":
                        tvInputDocument.setText(getResources().getString(R.string.numRUC));
                        break;
                    case "2":
                        tvInputDocument.setText(getResources().getString(R.string.numRUCOrdenate));
                        break;
                    default:break;
                }
                break;
            default:
                break;
        }
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

    private void spinner() {

        if (msgScreenDocument.isBanner()) {

            transView.setKeyboard(numberDoc, msgScreenDocument.getMaxLength(), false, false, msgScreenDocument.isAlfa(), minEtInputuser, relativeKeyboardNume, relativeKeyboardAlfa);
            clickSpinner = new ClickSpinner(tipoDoc, selectDni, btnTipoDoc, vista.findViewById(R.id.linearSpinnerDoc), vista.findViewById(R.id.relativeTipoDocumet), arrayTipoDoc, (Activity) thiscontext);
            clickSpinner.setDocument(tvInputDocument, linearDoc, relativeKeyboardNume, relativeKeyboardAlfa);
            clickSpinner.setClickKeyboard(transView.getKeyboards());
            clickSpinner.setCallbackTypeDocument(callbackTypeDocument);
            if(classArguments.isEditar()){
                linearDoc.setVisibility(View.VISIBLE);
                numberDoc.setText(numberDoc.getText());
                numberDoc.setBackgroundResource(R.drawable.btn_spinner_estados);
            }
            if(classArguments.getEditableGiros().equals("0")){
                clickSpinner.spinnerBotones(Trans.GIROS_EMISION, "Beneficiario");
            }else if(classArguments.getEditableGiros().equals("1")){
                clickSpinner.spinnerBotones(Trans.GIROS_EMISION, "Solicitante");
            }else {
                clickSpinner.spinnerBotones(Trans.GIROS_EMISION, "Ordenante");
            }
            nombre = false;
        }
    }
    private void consumoApi(){

        btnTipOrden.setVisibility(View.GONE);
        linearLayoutProgress.setVisibility(View.VISIBLE);
        gifProgressBen.playAnimation();
        linnombreBenef.setVisibility(View.GONE);

        RequestWs requestWs = new RequestWs(getActivity(), ROOT + msgScreenDocument.getListUrl().get(1).getMethod(), TIMEOUT, true);
        requestWs.httpRequets(waitTransFragment.headerTrans(true), setObtainPerson(), (result, statusCode, header) -> {
            waitTransFragment.setOpnNumber();
            if (result != null){
                if (statusCode == 200){
                    try {
                        transView.counterDownTimer(msgScreenDocument.getTimeOut(), MSGSCREENTIMER);
                        rspObtainPersonEmision = new RspObtainPersonEmision();
                        if (rspObtainPersonEmision.getRspobtainPerson(result,header)){

                            name = rspObtainPersonEmision.getRspname();

                            if(name != null && !name.equals("")) {
                                isClient = true;
                                nameRegister.setText(name);
                                nombreBenef.setEnabled(false);
                                nameRegister.setEnabled(false);
                            }else {
                                nameRegister.setEnabled(true);
                                nombreBenef.setEnabled(true);
                                btnContinuar.setEnabled(false);
                                nameRegister.setHint(R.string.nombreOrdenante);
                            }
                            gifProgressBen.pauseAnimation();
                            linearLayoutProgress.setVisibility(View.GONE);
                            linnombreBenef.setVisibility(View.VISIBLE);
                        }else {
                            transView.deleteTimer();
                            transView.getListener().cancel(TcodeError.T_ERR_UNPACK_JSON);
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

    private void consumeApiVerifyBank(){

        RequestWs requestWs = new RequestWs(getActivity(), ROOT + msgScreenDocument.getListUrl().get(2).getMethod(), TIMEOUT, true);
        requestWs.httpRequets(waitTransFragment.headerTrans(true), setVerifyBankDraft(), (result, statusCode, header) -> {
            waitTransFragment.setOpnNumber();
            if (result != null){
                if (statusCode == 200){
                    try {
                        transView.counterDownTimer(msgScreenDocument.getTimeOut(), MSGSCREENTIMER);
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
                            transView.setArgumentsClass(classArguments);

                            btnTipoDoc.setEnabled(false);
                        }else {
                            transView.deleteTimer();
                            transView.getListener().cancel(TcodeError.T_ERR_UNPACK_JSON);
                            return;
                        }
                    } catch (JSONException e) {
                        Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                        return;
                    }
                }else {
                    transView.getListener().cancel(result,header,statusCode);
                    return;
                }
                waitVerifyBank.getVeryBank(statusCode);
            }else {
                transView.deleteTimer();
                transView.getListener().cancel(UIUtils.validateResponseError(statusCode));
            }
        });
    }

    private JSONObject setObtainPerson(){
        final ReqObtainPersonEmision reqObtainPersonEmision = new ReqObtainPersonEmision();

        reqObtainPersonEmision.setReqactor(classArguments.getEditableGiros().equals("0") ? "3" : classArguments.getEditableGiros());
        reqObtainPersonEmision.setReqdocumentType(documentType(tipoDoc.getText().toString()));
        reqObtainPersonEmision.setReqdocumentNumber(numberDoc.getText().toString());

        return reqObtainPersonEmision.buildsObjectJSON();
    }

    private JSONObject setVerifyBankDraft(){
        final ReqVerifyBankEmision reqVerifyBankEmision = new ReqVerifyBankEmision();

        reqVerifyBankEmision.setReqCurrencyCode(classArguments.getTypeCoin());
        reqVerifyBankEmision.setReqAmount(classArguments.getMonto());
        if (classArguments.getTypepayment().equals("1"))
            reqVerifyBankEmision.setReqTrack2(BCPConfig.getInstance(getContext()).getTrack2Agente(BCPConfig.TRACK2AGENTEKEY) + "");
        else {
            reqVerifyBankEmision.setReqFamilyCode(classArguments.getSelectedAccountItem().getFamilyCode());
            reqVerifyBankEmision.setReqCurrencyCodeAccount(classArguments.getSelectedAccountItem().getCurrencyCode());
        }

        //SOLO ENVIAR EL DATO MODIFICADO
        switch (classArguments.getEditableGiros()){
            case "0":
                if (!nameRegister.getText().toString().equals(classArguments.getNameBeneficiary())){
                    reqVerifyBankEmision.setReqNameBeneficiary(nameRegister.getText().toString());
                    //flags para saber si son clientes de bcp
                    reqVerifyBankEmision.setBeneficiary(isClient);
                }else
                    reqVerifyBankEmision.setBeneficiary(true);
                break;
            case "1":
                if (!nameRegister.getText().toString().equals(classArguments.getNameSender()))
                    reqVerifyBankEmision.setReqNameSender(nameRegister.getText().toString());
                //flags para saber si son clientes de bcp
                reqVerifyBankEmision.setSender(isClient);
                break;
            case "2":
                if (!nameRegister.getText().toString().equals(classArguments.getNameRemitter())){
                    reqVerifyBankEmision.setReqName(nameRegister.getText().toString());
                    reqVerifyBankEmision.setReqNameSender(classArguments.getNameSender());
                }
                //flags para saber si son clientes de bcp
                reqVerifyBankEmision.setRemitter(isClient);
                reqVerifyBankEmision.setSender(true);
                break;
            default:
                break;

        }

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
            tipoDoc.setText("");
            linearDoc.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.GONE);
            relativeKeyboardNume.setVisibility(View.GONE);
        });

        btnContinuarMsg.setOnClickListener(v ->
                //accion al presionar el boton continaur
                relativeLayout.setVisibility(View.GONE));

        btnSalir.setOnClickListener(v ->{
            transView.deleteTimer();
            //accion al presionar el boton salir
            transView.getListener().cancel();
        });
    }
}