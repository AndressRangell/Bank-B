package com.bcp.transactions.giros.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.desert.keyboard.InputManager;
import com.android.newpos.pay.R;
import com.bcp.document.ClassArguments;
import com.bcp.document.MsgScreenDocument;
import com.bcp.rest.giros.emision.request.ReqVerifyBankEmision;
import com.bcp.rest.giros.emision.response.RspVerifyBankEmision;
import com.bcp.rest.httpclient.RequestWs;
import com.bcp.rest.jsonerror.JsonError;
import com.bcp.rest.pagoservicios.request.ReqVerifyPayment;
import com.bcp.rest.pagoservicios.response.RspVerifyPayment;
import com.bcp.settings.BCPConfig;
import com.bcp.teclado_alfanumerico.ClickKeyboardFragment;
import com.bcp.transactions.callbacks.WaitTransFragment;
import com.newpos.libpay.Logger;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.presenter.TransUIImpl;
import com.bcp.transactions.callbacks.WaitConsumosApi;
import com.newpos.libpay.presenter.TransView;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.Trans;
import org.json.JSONException;
import org.json.JSONObject;
import cn.desert.newpos.payui.UIUtils;
import static com.bcp.rest.JsonUtil.ROOT;
import static com.bcp.rest.JsonUtil.TIMEOUT;


public class FragmentTypeCoin extends Fragment {

    RadioButton btnSoles;
    RadioButton btnDolares;
    TextView  signSol;
    LinearLayout typecoin;
    TextView etTitle;
    Button btn00;
    TextView btnAcceptInputUser;
    TextView montoMinimo;
    TextView montoMaximo;
    TextView tituloMonto;
    RelativeLayout amount;
    EditText valor;
    ImageView volver;
    ImageView close;

    //screen amount
    TextView tvMaxAmount;
    TextView tvAccount;
    
    Context thiscontext;
    int minEtInputuser;
    Activity activity;
    private ClassArguments classArguments;
    View viewFragment;
    private RspVerifyBankEmision rspVerifyBank;
    private WaitTransFragment waitTransFragment;
    private String maxAmount;
    private String minAmount;
    private RspVerifyPayment rspVerifyPayment;
    private WaitConsumosApi waitConsumosApi;


    private static final String COLOR_WHITE = "#FFFFFF";
    private static final String COLOR_GRAY = "#767c9b";
    private  ClickKeyboardFragment keyboards;
    private static final String MSGSCREENTIMER = "FragmentTypeCoin";

    TransView transView;
    MsgScreenDocument msgScreenDocument;

    public FragmentTypeCoin(){
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

        btnSoles = null;
        btnDolares = null;
        signSol = null;
        typecoin = null;
        etTitle = null;
        btn00 = null;
        btnAcceptInputUser = null;
        montoMinimo = null;
        montoMaximo = null;
        tituloMonto = null;
        amount = null;
        valor = null;
        volver = null;
        close = null;

        //screen amount
        tvMaxAmount = null;
        tvAccount = null;

        thiscontext = null;
        minEtInputuser = 0;
        activity = null;
        classArguments = null;
        viewFragment = null;
        rspVerifyBank = null;
        waitTransFragment = null;
        maxAmount = null;
        minAmount = null;
        rspVerifyPayment = null;
        waitConsumosApi = null;



        keyboards = null;

        transView = null;
        msgScreenDocument = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewFragment = inflater.inflate(R.layout.fragment_type_coin, container, false);

        btnSoles              = viewFragment.findViewById(R.id.btnSoles);
        btnDolares            = viewFragment.findViewById(R.id.btnDolares);
        signSol               = viewFragment.findViewById(R.id.signSol);
        typecoin              = viewFragment.findViewById(R.id.layout_typecoin);
        etTitle               = viewFragment.findViewById(R.id.title_toolbar);
        btn00                 = viewFragment.findViewById(R.id.numb00);
        btnAcceptInputUser    = viewFragment.findViewById(R.id.continuar);
        valor                 = viewFragment.findViewById(R.id.monto);
        tvMaxAmount           = viewFragment.findViewById(R.id.layoutMontoTittle1);
        tvAccount             = viewFragment.findViewById(R.id.account);
        volver                = viewFragment.findViewById(R.id.back);
        close                 = viewFragment.findViewById(R.id.iv_close);
        amount                = viewFragment.findViewById(R.id.relativeMonto);
        montoMaximo           = viewFragment.findViewById(R.id.amountmaximo);
        montoMinimo           = viewFragment.findViewById(R.id.amountMinimo);
        tituloMonto        = viewFragment.findViewById(R.id.titleMonto);


        etTitle.setText(getResources().getString(R.string.titleEmision));
        volver.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);

        return viewFragment;
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transView.deleteTimer();

        (transView).counterDownTimer(msgScreenDocument.getTimeOut(), MSGSCREENTIMER);

        classArguments = (transView).getArguments();
        //se instancia el callback para obtener el sesionId y el header
        waitTransFragment = msgScreenDocument.getCallbackTransFragment();
        minEtInputuser = 0;

        if(classArguments.getTipoPagoServicio() != null && (classArguments.getTipoPagoServicio().equals(Trans.PAGO_PARCIAL) || classArguments.getTipoPagoServicio().equals(Trans.PAGO_CONRANGO))){
            etTitle.setText("Ingrese el monto a pagar");
            amount.setVisibility(View.VISIBLE);
            tvMaxAmount.setVisibility(View.GONE);
            tituloMonto.setText("Monto Permitido entre:");
            if(classArguments.getTipoPagoServicio().equals(Trans.PAGO_CONRANGO)){
                montoMaximo.setText("S/"+  classArguments.getTotal());
            }else {
                montoMaximo.setText("S/"+  classArguments.getMaxAmount());
            }
            montoMinimo.setText("S/"+ classArguments.getMinAmount());
        }else
            typecoin.setVisibility(View.VISIBLE);

        if (classArguments.isEditar())
            volver.setVisibility(View.GONE);

        close.setOnClickListener(v -> {
            (transView).deleteTimer();
            (transView).getListener().cancel();
        });

        btnSoles.setOnClickListener(v -> {
            btnSoles.setTextColor(Color.parseColor(COLOR_WHITE));
            btnDolares.setTextColor(Color.parseColor(COLOR_GRAY));
            signSol.setText("S/");
            tvMaxAmount.setText("S/ " + UIUtils.formatMiles(!msgScreenDocument.getMaxAmount().equals("-1") ? msgScreenDocument.getMaxAmount() : "0"));
        });
        btnDolares.setOnClickListener(v -> {
            btnDolares.setTextColor(Color.parseColor(COLOR_WHITE));
            btnSoles.setTextColor(Color.parseColor(COLOR_GRAY));
            signSol.setText("US$");
            tvMaxAmount.setText("US$ " + UIUtils.formatMiles(!msgScreenDocument.getMaxAmountUsd().equals("-1") ? msgScreenDocument.getMaxAmountUsd() : "0"));
        });

        btnAcceptInputUser.setOnClickListener(v -> {

            initAmountMinMax();

            String amount = valor.getText().toString();
            long amt = Long.parseLong(amount.replaceAll("[,.]",""));
            if (amt < Long.parseLong(minAmount)) {
                msgAccountSingle(getResources().getString(R.string.minPermitido));
            }else if (amt > Long.parseLong(maxAmount)){
                msgAccountSingle(getResources().getString(R.string.maxPermitido));
            }else {
                actionAmopuntOk();
            }
            (transView).setArgumentsClass(classArguments);
        });

        volver.setOnClickListener(v -> NavHostFragment.findNavController(FragmentTypeCoin.this)
                .navigate(R.id.action_fragmentTypeCoin_to_fragmentTypePayment));

        setKeyboard(valor, 9,true,false, false, minEtInputuser, viewFragment.findViewById(R.id.keyboardNumerico),null);

        tvMaxAmount.setText("S/ " + UIUtils.formatMiles(msgScreenDocument.getMaxAmount()));
    }

    private void actionAmopuntOk(){
        classArguments.setMonto(valor.getText().toString());
        if(btnDolares.isChecked()){
            classArguments.setTypeCoin("USD");
        }else {
            classArguments.setTypeCoin("PEN");
        }
        if(classArguments.isEditar()) {
            btnAcceptInputUser.setEnabled(false);

            waitConsumosApi = null;
            waitConsumosApi = retval -> {
                if(retval == 200){
                    if (classArguments.getTypepayment().equals("2")) {
                        NavHostFragment.findNavController(FragmentTypeCoin.this)
                                .navigate(R.id.action_fragmentTypeCoin3_to_fragmentDetailGiro);
                    } else {
                        NavHostFragment.findNavController(FragmentTypeCoin.this)
                                .navigate(R.id.action_fragmentTypeCoin_to_fragmentDetalleGiro);
                    }
                }
            };
            consumeApiVerifyBank();
        }else {
            classArguments.setMaxAmount(maxAmount);
            classArguments.setMinAmount(minAmount);

            if(classArguments.getTypepayment().equals("1")){
                if(msgScreenDocument.getTransEname().equals(Trans.GIROS)){
                    NavHostFragment.findNavController(FragmentTypeCoin.this)
                            .navigate(R.id.action_fragmentTypeCoin_to_fragmentDatosSolicitante);
                }else {
                        waitConsumosApi = null;
                        waitConsumosApi = retval -> {
                            if(retval == 200){
                                NavHostFragment.findNavController(FragmentTypeCoin.this)
                                        .navigate(R.id.action_fragmentTypeCoin2_to_fragmentDetailPaymentService2);
                            }
                        };
                        consumoVerifypaymen();
                }
            }else {
                (transView).deleteTimer();
                (transView).getListener().confirm(InputManager.Style.COMMONINPUT);
            }
        }
    }

    private void initAmountMinMax(){
        if (classArguments.isEditar()){
            maxAmount = classArguments.getMaxAmount();
            minAmount = classArguments.getMinAmount();
        }else {
            if(classArguments.getTipoPagoServicio() != null && classArguments.getTipoPagoServicio().equals(Trans.PAGO_PARCIAL)){
                maxAmount = classArguments.getMaxAmount().replaceAll("[,.]","");
                minAmount = classArguments.getMinAmount().replaceAll("[,.]","");
            }else {
                if(btnDolares.isChecked()){
                    maxAmount = msgScreenDocument.getMaxAmountUsd();
                    minAmount = msgScreenDocument.getMinAmountUsd();
                }else {
                    maxAmount = msgScreenDocument.getMaxAmount();
                    minAmount = msgScreenDocument.getMinAmount();
                }
            }
        }

        if (maxAmount.equals("-1"))
            maxAmount = "0";

        if (minAmount.equals("-1"))
            minAmount = "0";
    }



    /*
    *se crea nuevo click keyboard para el manejo del teclado solo para fragments con el envio del view y no la actividad
     */
    public void setKeyboard(EditText etTxt, int lenMax, boolean activityAmount, boolean activityPwd, boolean isAlfa, int minLen, RelativeLayout keyboardNumeric , RelativeLayout keyboardAlfa){
        keyboards = new ClickKeyboardFragment(etTxt, viewFragment, isAlfa,(transView).getTimer(),keyboardNumeric,keyboardAlfa );
        keyboards.setActivityPwd(activityPwd);
        keyboards.setActivityMonto(activityAmount);
        keyboards.setLengthMax(lenMax,minLen);

        btn00.setEnabled(true);
        btn00.setText(getResources().getString(R.string._00));
        btn00.setTextColor(Color.BLACK);
    }

    private void msgAccountSingle(String msg){
        final RelativeLayout relativeLayout = viewFragment.findViewById(R.id.msgAccount);
        relativeLayout.setVisibility(View.VISIBLE);
        TextView tvAccountSymbol = viewFragment.findViewById(R.id.msg2Screen);
        tvAccountSymbol.setText(msg);
        Button btnEntentido = viewFragment.findViewById(R.id.entendidoMsg);

        btnEntentido.setOnClickListener(v -> relativeLayout.setVisibility(View.INVISIBLE));
    }

    private void consumeApiVerifyBank(){

        RequestWs requestWs = new RequestWs(getActivity(), ROOT + msgScreenDocument.getListUrl().get(2).getMethod(), TIMEOUT, true);
        requestWs.httpRequets(waitTransFragment.headerTrans(true), setVerifyBankDraft(), (result, statusCode, header) -> {
            waitTransFragment.setOpnNumber();
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
                waitConsumosApi.getVeryBank(statusCode);
            }else {
                (transView).deleteTimer();
                (transView).getListener().cancel(UIUtils.validateResponseError(statusCode));
            }
        });
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
        //flags para saber si son clientes de bcp
        reqVerifyBankEmision.setBeneficiary(true);
        reqVerifyBankEmision.setRemitter(true);
        reqVerifyBankEmision.setSender(true);

        return reqVerifyBankEmision.buildsObjectJSON();
    }

    /// PAGO DE SERVICIOS CONSUMO VERIFYPAYMENT
    private void consumoVerifypaymen(){

        RequestWs requestWs = new RequestWs(getActivity(), ROOT + msgScreenDocument.getListUrl().get(8).getMethod(), TIMEOUT, true);
        requestWs.httpRequets(msgScreenDocument.getHeader(), setVerifypayment(), (result, statusCode,header) -> {
            waitTransFragment.setOpnNumber();
            if (result != null){
                if (statusCode == 200){
                    try {
                        (transView).counterDownTimer(msgScreenDocument.getTimeOut(), MSGSCREENTIMER);
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

                            waitTransFragment.getSessionId(rspVerifyPayment.getSesionId()+"");

                        }else {
                            (transView).deleteTimer();
                            (transView).getListener().cancel(TcodeError.T_ERR_UNPACK_JSON);
                        }
                    } catch (JSONException e) {
                        Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                    }
                }else {
                    validateError(result,statusCode);
                }
                waitConsumosApi.getVeryBank(statusCode);
            }else {
                (transView).deleteTimer();
                (transView).getListener().cancel(UIUtils.validateResponseError(statusCode));
            }
        });
    }

    private JSONObject setVerifypayment(){
        final ReqVerifyPayment reqVerifyPayment = new ReqVerifyPayment();

        if(classArguments.getTipoPagoServicio().equals(Trans.PAGO_SINVALIDACION)){
            reqVerifyPayment.setCtnAffiliationCode(classArguments.getAffiliationCode());
            reqVerifyPayment.setCtnAmount(classArguments.getMonto());
            reqVerifyPayment.setCtnClientDepositCode(classArguments.getClientDocumentNumber());

        }else {
            reqVerifyPayment.setDebtcode(classArguments.getDebcode());

            if (classArguments.isOtherAmount())
                reqVerifyPayment.setCtnAmount(valor.getText().toString());

            if(classArguments.getTipoPagoServicio().equals(Trans.PAGO_PARCIAL))
                reqVerifyPayment.setCtnAmount(valor.getText().toString());

            reqVerifyPayment.setCtnPaymentType(classArguments.getTypepayment());
            reqVerifyPayment.setCtnAffiliationCode(classArguments.getAffiliationCode());

        }

        return reqVerifyPayment.buildsJsonObject(classArguments.getTipoPagoServicio());
    }

    private void validateError(JSONObject jsonObject, int status){

        switch (status){
            case TcodeError.T_INTERNAL_SERVER_ERR:
            case TcodeError.T_ERR_BAD_REQUEST:
            case TcodeError.T_ERR_NO_FOUND:
                JsonError jsonError =  new JsonError();
                try {
                    if(!jsonError.getRspObtJson(jsonObject)) {
                        (transView).showError(msgScreenDocument.getTransEname(),String.valueOf(TcodeError.T_ERR_UNPACK_JSON), TransUIImpl.getErrInfo(String.valueOf(TcodeError.T_ERR_UNPACK_JSON)));
                        return;
                    }
                } catch (JSONException e) {
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                }
                (transView).showError(msgScreenDocument.getTransEname(),jsonError.getCode(),jsonError.getDescription());
                break;
            default:
                break;
        }
    }
}