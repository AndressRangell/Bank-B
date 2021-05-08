package com.bcp.transactions.giros.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.newpos.pay.R;
import com.bcp.document.ClassArguments;
import com.bcp.document.MsgScreenDocument;
import com.bcp.menus.seleccion_cuenta.SeleccionCuentaAdaptadorItem;
import com.bcp.menus.seleccion_cuenta.SeleccionCuentaAdaptadorItem2;
import com.bcp.menus.seleccion_cuenta.SelectedAccountItem;
import com.bcp.rest.generic.response.ProductsAccounts;
import com.bcp.rest.httpclient.RequestWs;
import com.bcp.rest.jsonerror.JsonError;
import com.bcp.rest.pagoservicios.request.ReqVerifyPayment;
import com.bcp.rest.pagoservicios.response.RspVerifyPayment;
import com.bcp.transactions.callbacks.WaitConsumosApi;
import com.bcp.transactions.callbacks.WaitTransFragment;
import com.newpos.libpay.Logger;
import com.newpos.libpay.presenter.TransUIImpl;
import com.newpos.libpay.presenter.TransView;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.Trans;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.desert.newpos.payui.UIUtils;

import static com.android.newpos.pay.StartAppBCP.variables;
import static com.bcp.rest.JsonUtil.ROOT;
import static com.bcp.rest.JsonUtil.TIMEOUT;

public class FragmentTypeAccount extends Fragment {

    protected SelectedAccountItem selectedAccountItem;
    RelativeLayout msgRelative;
    private Context thiscontext;
    private FragmentActivity activity;
    ClassArguments classArguments;
    TextView msg2;
    TextView msg3;
    TextView title;
    ImageView close;
    View vista;
    private String MSGSCREENTIMER = "FragmentTypeAccount";
    RspVerifyPayment rspVerifyPayment;
    private WaitConsumosApi waitConsumosApi;
    private WaitTransFragment waitTransFragment;
    TransView transView;
    MsgScreenDocument msgScreenDocument;

    public FragmentTypeAccount(){
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
          selectedAccountItem = null;
          msgRelative = null;
          thiscontext = null;
          activity = null;
          classArguments = null;
          msg2 = null;
          msg3 = null;
          title = null;
          close = null;
          vista = null;
          rspVerifyPayment = null;
          waitConsumosApi = null;
          waitTransFragment = null;
          transView = null;
          msgScreenDocument = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_type_account, container, false);

        msg2 = vista.findViewById(R.id.msg2);
        msg3 = vista.findViewById(R.id.msg3);
        title = vista.findViewById(R.id.title_toolbar);
        close = vista.findViewById(R.id.iv_close);
        msgRelative = vista.findViewById(R.id.msgRelative);
        close.setVisibility(View.VISIBLE);

        return vista;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transView.deleteTimer();

        transView.counterDownTimer(msgScreenDocument.getTimeOut(), MSGSCREENTIMER);

        classArguments = transView.getArguments();

        //se instancia el callback para obtener el sesionId y el header
        waitTransFragment = msgScreenDocument.getCallbackTransFragment();

        close.setOnClickListener(v -> transView.getListener().cancel());

        if (drawMenuApiRest(thiscontext.getResources().getString(R.string.accountPrimary),((TransView)activity).getMsgScreendocument().getProductsAccounts()) == 0)
            msgAccount(view,thiscontext.getResources().getString(classArguments.getTipoPagoServicio() != null ? R.string.msgAccountPS : R.string.msg1GiroEmisionSol),selectedAccountItem.getProductDescription() + " " +selectedAccountItem.getCurrencyDescription() + " " + selectedAccountItem.getCurrencySymbol());
    }

    private void msgAccount(View view, String msg1, String msg2){
        final RelativeLayout relativeLayout = view.findViewById(R.id.msgAccount);
        relativeLayout.setVisibility(View.VISIBLE);

        close.setVisibility(View.GONE);
        msgRelative.setVisibility(View.VISIBLE);

        TextView tvMsg1 = view.findViewById(R.id.msg1Screen);
        TextView tvMsg2 = view.findViewById(R.id.msg2Screen);
        TextView tvMsg3 = view.findViewById(R.id.msg3Screen);

        tvMsg1.setText(msg1);
        tvMsg2.setText(msg2);
        tvMsg3.setVisibility(View.GONE);

        Button btnEntentido = view.findViewById(R.id.entendidoMsg);

        relativeLayout.setOnClickListener(v -> {
            //nothing
        });

        btnEntentido.setOnClickListener(v -> {
                    if(classArguments.getTipoPagoServicio() != null) {
                        continuarPantalla();
                    }else {
                        NavHostFragment.findNavController(FragmentTypeAccount.this)
                                .navigate(R.id.action_fragmentTypeAccount_to_fragmentBeneficiaryDataEmision2);
                    }
            transView.deleteTimer();
        });
    }

    //Metodo para mostrar el menu seleccion cuenta y llenar los recycler's
    protected int drawMenuApiRest(String titleScreen, ProductsAccounts[] productsAccounts) {
        int ret = -1;
        String[] labels = {titleScreen, "", ""};
        List<SelectedAccountItem> itemSol = new ArrayList<>();
        List<SelectedAccountItem> itemDolar = new ArrayList<>();
        boolean sol = false;
        boolean dolar = false;

        for (ProductsAccounts productsAccount : productsAccounts) {
            SelectedAccountItem selectedItem = new SelectedAccountItem();
            selectedItem.setCurrencyCode(productsAccount.getCurrencyCode());
            selectedItem.setCurrencyDescription(productsAccount.getCurrencyDescrip());
            selectedItem.setFamilyCode(productsAccount.getFamilyCode());
            selectedItem.setProductDescription(productsAccount.getProductDescrip());
            if (productsAccount.getCurrencySymbol() != null && !productsAccount.getCurrencySymbol().equals(""))
                selectedItem.setCurrencySymbol(productsAccount.getCurrencySymbol());

            //Si solo es un tipo de cuenta no se muestra en pantalla se toma por defecto esta
            if (productsAccounts.length == 1) {
                selectedAccountItem = selectedItem;
                variables.setMultiAccount(false);
                classArguments.setSelectedAccountItem(selectedAccountItem);
                return 0;
            }

            if (thiscontext.getResources().getString(R.string.pen).equals(productsAccount.getCurrencyCode())) {
                if (!sol) {
                    sol = true;
                    labels[1] = thiscontext.getResources().getString(R.string.ctaSoles);
                }
                itemSol.add(selectedItem);
            }

            if (thiscontext.getResources().getString(R.string.usd).equals(productsAccount.getCurrencyCode())) {
                if (!dolar) {
                    dolar = true;
                    labels[2] = thiscontext.getResources().getString(R.string.ctaDolares);
                }
                itemDolar.add(selectedItem);
            }

        }

        title.setText(labels[0]);
        msg2.setText(labels[1]);
        msg3.setText(labels[2]);

        selectAccount(itemSol,itemDolar);
        variables.setMultiAccount(true);
        return ret;
    }

    private void selectAccount(List<SelectedAccountItem> items, List<SelectedAccountItem> items2){

        if (items!=null) {
            RecyclerView recyclerView = vista.findViewById(R.id.recyclerUno);
            recyclerView.setLayoutManager(new GridLayoutManager(thiscontext, 2));

            SeleccionCuentaAdaptadorItem cuentaAdaptadorItem = new SeleccionCuentaAdaptadorItem(items, thiscontext);
            recyclerView.setAdapter(cuentaAdaptadorItem);
            cuentaAdaptadorItem.setOnClickListener((view, obj, position) -> {
                selectedAccountItem = obj;
                transView.deleteTimer();

                classArguments.setTypepayment("2");
                classArguments.setSelectedAccountItem(selectedAccountItem);
                transView.setArgumentsClass(classArguments);

                if(classArguments.getTipoPagoServicio() != null){
                    continuarPantalla();
                }else {
                    NavHostFragment.findNavController(FragmentTypeAccount.this)
                            .navigate(R.id.action_fragmentTypeAccount_to_fragmentBeneficiaryDataEmision2);
                }
            });
        }

        if (items2!=null) {
            RecyclerView recyclerView2 = vista.findViewById(R.id.recyclerDos);
            recyclerView2.setLayoutManager(new GridLayoutManager(thiscontext, 2));

            SeleccionCuentaAdaptadorItem2 cuentaAdaptadorItem2 = new SeleccionCuentaAdaptadorItem2(items2, thiscontext);
            recyclerView2.setAdapter(cuentaAdaptadorItem2);
            cuentaAdaptadorItem2.setOnClickListener((view, obj, position) -> {
                selectedAccountItem = obj;
                transView.deleteTimer();
                //Aqui va lo que se realiza al presionar los botones del segundo recycler

                classArguments.setTypepayment("2");
                classArguments.setSelectedAccountItem(selectedAccountItem );
                transView.setArgumentsClass(classArguments);
                if(classArguments.getTipoPagoServicio() != null){
                    continuarPantalla();
                }else {
                    NavHostFragment.findNavController(FragmentTypeAccount.this)
                            .navigate(R.id.action_fragmentTypeAccount_to_fragmentBeneficiaryDataEmision2);
                }

            });
        }
    }

    // PAGO DE SERVICIOS CONSUMO VERIFYPAYMENT

    private void consumoVerifypaymen(){

        RequestWs requestWs = new RequestWs(getActivity(), ROOT + msgScreenDocument.getListUrl().get(8).getMethod(), TIMEOUT, true);
        requestWs.httpRequets(msgScreenDocument.getHeader(), setVerifypayment(), (result, statusCode,header) -> {
            waitTransFragment.setOpnNumber();
            if (result != null){
                if (statusCode == 200){
                    try {
                        transView.counterDownTimer(msgScreenDocument.getTimeOut(), MSGSCREENTIMER);
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
                            transView.deleteTimer();
                            transView.getListener().cancel(TcodeError.T_ERR_UNPACK_JSON);
                        }
                    } catch (JSONException e) {
                        Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                    }
                }else {
                    validateError(result,statusCode);
                }
                waitConsumosApi.getVeryBank(statusCode);
            }else {
                transView.deleteTimer();
                transView.getListener().cancel(UIUtils.validateResponseError(statusCode));
            }
        });
    }

    private JSONObject setVerifypayment(){
        final ReqVerifyPayment reqVerifyPayment = new ReqVerifyPayment();

        reqVerifyPayment.setCtnImportCode(classArguments.getImportcode());
        reqVerifyPayment.setCtnClientDepositCode(classArguments.getDocumentCode());

        if (classArguments.isOtherAmount())
                reqVerifyPayment.setCtnAmount(classArguments.getTotal());

        if(classArguments.getTypepayment().equals("2")){
            reqVerifyPayment.setCtnFamilyCode(classArguments.getSelectedAccountItem().getFamilyCode());
            reqVerifyPayment.setCtnCurrencyCode(classArguments.getSelectedAccountItem().getCurrencyCode());
        }

        if(classArguments.getTipoPagoServicio().equals(Trans.PAGO_PARCIAL))
            reqVerifyPayment.setCtnAmount(classArguments.getMonto());

        if(classArguments.getTipoPagoServicio().equals(Trans.PAGO_SINRANGO) && classArguments.getTypepayment().equals("2")){
            reqVerifyPayment.setDebtcode(classArguments.getDebcode());
        }

        if (classArguments.getTipoPagoServicio().equals(Trans.PAGO_SINVALIDACION)) {
            reqVerifyPayment.setCtnAffiliationCode(classArguments.getAffiliationCode());
            reqVerifyPayment.setCtnAmount(classArguments.getMonto());
            reqVerifyPayment.setCtnClientDepositCode(classArguments.getClientDocumentNumber());
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
                        ((TransView)activity).showError(msgScreenDocument.getTransEname(),String.valueOf(TcodeError.T_ERR_UNPACK_JSON), TransUIImpl.getErrInfo(String.valueOf(TcodeError.T_ERR_UNPACK_JSON)));
                        return;
                    }
                } catch (JSONException e) {
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                }
                ((TransView)activity).showError(msgScreenDocument.getTransEname(),jsonError.getCode(),jsonError.getDescription());
                break;
            default:
                break;
        }
    }

    private void continuarPantalla(){

        switch (classArguments.getTipoPagoServicio()){
            case Trans.PAGO_IMPORTE:
            case Trans.PAGO_CONRANGO:
            case Trans.PAGO_SINRANGO:
            case Trans.PAGO_PARCIAL:
            case Trans.PAGO_SINVALIDACION:
                waitConsumosApi = null;
                waitConsumosApi = retval -> {
                    if(retval == 200){
                        NavHostFragment.findNavController(FragmentTypeAccount.this)
                                .navigate(R.id.action_fragmentTypeAccount2_to_fragmentDetailPaymentService4);
                    }
                };
                consumoVerifypaymen();
                break;
            default:
                break;

        }
    }
}