package com.bcp.transactions.giros.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.newpos.pay.R;
import com.bcp.document.ClassArguments;
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
import com.pandingdebt.AdapterPandingdebt;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cn.desert.newpos.payui.UIUtils;

import static com.bcp.rest.JsonUtil.ROOT;
import static com.bcp.rest.JsonUtil.TIMEOUT;


public class FragmentingPadding extends Fragment {

    ImageView close;
    TextView toolbarTittle;
    TextView textMontoPanding;
    RecyclerView recyclerViewPanding;
    LinearLayout linearLayoutMonto;
    Button buttonContinuar;
    boolean position0 = false;
    boolean position1 = false;
    boolean position2 = false;
    boolean position3 = false;
    List<String> list = new ArrayList<>();
    List<String> amount = new ArrayList<>();
    AdapterPandingdebt adapterPandingdebt = new AdapterPandingdebt();
    SparseBooleanArray itemStatateArray = new SparseBooleanArray();
    String[] arrayDebts;
    WaitTransFragment waitTransFragment;
    RspVerifyPayment rspVerifyPayment;
    WaitConsumosApi waitConsumosApi;
    ClassArguments classArguments;

    private static final String MSGSCREENTIMER = "FragmentingPadding";
    private LinearLayoutManager linearLayoutManager;

    Activity activity;

    Context thiscontext;
    View viewvista;
    View view;
    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.thiscontext = context;
        this.activity = getActivity();
    }

    //CONSUMO DE API LISTA DEUDAS
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewvista = inflater.inflate(R.layout.fragment_pandding, container, false);

        close = viewvista.findViewById(R.id.iv_close);
        toolbarTittle = viewvista.findViewById(R.id.title_toolbar);
        textMontoPanding = viewvista.findViewById(R.id.textmontopanding);
        recyclerViewPanding = viewvista.findViewById(R.id.recyclerpending);
        view = viewvista.findViewById(R.id.view);
        linearLayoutManager = new LinearLayoutManager(thiscontext, LinearLayoutManager.VERTICAL,false);
        recyclerViewPanding.setLayoutManager(linearLayoutManager);
        recyclerViewPanding.setAdapter(adapterPandingdebt);
        buttonContinuar = viewvista.findViewById(R.id.btncontinuar);
        linearLayoutMonto = viewvista.findViewById(R.id.linearmonto);

        drawMenuPadding();

        close.setVisibility(View.VISIBLE);
        toolbarTittle.setVisibility(View.VISIBLE);
        toolbarTittle.setText(R.string.titlePse);


        classArguments = ((TransView)activity).getArguments();

        arrayDebts = new String[classArguments.getListDebts().length];
        for(int i = 0 ; i < classArguments.getListDebts().length; i++){
            arrayList.add(classArguments.getListDebts()[i].getAmountCurrencySymbol() + " " + classArguments.getListDebts()[i].getAmount() + " (vence " + classArguments.getListDebts()[i].getDueDate() + " )");
        }

        return viewvista;
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TransView)activity).deleteTimer();

        ((TransView)activity).counterDownTimer(((TransView)activity).getMsgScreendocument().getTimeOut(), MSGSCREENTIMER);

        classArguments = ((TransView)activity).getArguments();

        waitTransFragment = ((TransView)activity).getMsgScreendocument().getCallbackTransFragment();


        drawMenuPadding();
        adapterPandingdebt.loadItems(list);
        clickAdater();

        buttonContinuar.setOnClickListener(view1 -> {
            waitConsumosApi = null;
            waitConsumosApi = retval -> {
                if(retval == 200){
                    NavHostFragment.findNavController(FragmentingPadding.this)
                            .navigate(R.id.action_fragmentingPadding_to_fragmentDetailPaymentService);
                }
            };
            consumoVeryfyPayment();
        });

        close.setOnClickListener(view1 -> {
            ((TransView)activity).deleteTimer();
            ((TransView)activity).getListener().cancel();
        });
    }

    public void drawMenuPadding() {

        for (int i = 0; i <arrayList.size(); i++){
            list.add(arrayList.get(i).replace("[","").replace("]","").replace(",",""));
        }
    }

    private void clickAdater(){
        adapterPandingdebt.setOnClickListener((position, view, isCheckbox, text) -> {
            if (isCheckbox){
                int pos = text.getText().toString().indexOf("(");
                amount.add(text.getText().toString().substring(2,pos).replace(" ","").replace(",",""));
                buttonContinuar.setEnabled(true);
                text.setTypeface(Typeface.create("font", Typeface.BOLD));
                linearLayoutMonto.setVisibility(View.VISIBLE);
                checkbox(position, true);
            }else {
                int pos = text.getText().toString().indexOf("(");
                amount.remove(text.getText().toString().substring(2, pos).replace(" ", "").replace(",", ""));
                text.setTypeface(Typeface.create("font", Typeface.NORMAL));
                checkbox(position, false);
                if(amount.size()>=1){
                    linearLayoutMonto.setVisibility(View.VISIBLE);
                    buttonContinuar.setEnabled(true);
                }else{
                    linearLayoutMonto.setVisibility(View.INVISIBLE);
                    buttonContinuar.setEnabled(false);
                }
            }
            totalAmount(amount);
        });
    }

    public void totalAmount(List amount){
        double suma = 0;
        String result;
        if (amount.size() >= 2) {
            for (int i = 0; i < amount.size(); i++) {
                suma += Double.parseDouble(amount.get(i).toString());
            }
            System.out.println("suma " + suma);
            result = "Monto: " + "S/ "+ UIUtils.formatMiles(String.valueOf(suma).replaceAll("[,.]","0"));

        }else {
            result = "Monto: " + "S/ "+ amount.toString().replace("[", "").replace("]", "");
        }
        textMontoPanding.setText(result);
    }

    public void checkbox(int position, boolean flag){
        switch (position){
            case 0:
                position0 = flag;
                break;
            case 1:
                position1 = flag;
                break;
            case 2:
                position2 = flag;
                break;
            case 3:
                position3 = flag;
                break;
            default:
                break;
        }
    }

    public String idCod(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(position0 ? "1":"0");
        stringBuilder.append(position1 ? "1":"0");
        stringBuilder.append(position2 ? "1":"0");
        stringBuilder.append(position3 ? "1":"0");

        return stringBuilder.toString();
    }


    private void consumoVeryfyPayment(){

        RequestWs requestWs = new RequestWs(getActivity(), ROOT + ((TransView)activity).getMsgScreendocument().getListUrl().get(8).getMethod(), TIMEOUT, true);
        requestWs.httpRequets(((TransView)activity).getMsgScreendocument().getHeader(), setVerifyPayment(), (result, statusCode,header) -> {
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

                            waitTransFragment.getSessionId(rspVerifyPayment.getSesionId()+"");

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

    //SE USA PARA PAGO SERVICIOS
    private JSONObject setVerifyPayment(){
        final ReqVerifyPayment reqVerifyPayment = new ReqVerifyPayment();

        reqVerifyPayment.setDebtcode(idCod());
        if (classArguments.getTypepayment().equals("2")){
            reqVerifyPayment.setCtnFamilyCode(classArguments.getSelectedAccountItem().getFamilyCode());
            reqVerifyPayment.setCtnCurrencyCode(classArguments.getSelectedAccountItem().getCurrencyCode());
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
}