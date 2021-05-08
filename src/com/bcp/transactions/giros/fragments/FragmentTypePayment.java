package com.bcp.transactions.giros.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.desert.keyboard.InputManager;
import com.android.newpos.pay.R;
import com.bcp.document.ClassArguments;
import com.bcp.document.MsgScreenDocument;
import com.bcp.menus.seleccion_cuenta.SeleccionCuentaAdaptadorItem;
import com.bcp.menus.seleccion_cuenta.SelectedAccountItem;
import com.newpos.libpay.Logger;
import com.newpos.libpay.presenter.TransView;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.Trans;

import java.util.ArrayList;
import java.util.List;


public class FragmentTypePayment extends Fragment {

    TextView msg2;
    TextView msg3;
    TextView title;
    SelectedAccountItem selectedItem;
    ImageView volver;
    ImageView close;
    Context thiscontext;
    Activity activity;
    ClassArguments classArguments;
    private String MSGSCREENTIMER = "FragmentTypePayment";

    TransView transView;
    MsgScreenDocument msgScreenDocument;

    public FragmentTypePayment(){
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
        msg2 = null;
        msg3 = null;
        title = null;
        selectedItem = null;
        volver = null;
        close = null;
        thiscontext = null;
        activity = null;
        classArguments = null;

        transView = null;
        msgScreenDocument = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_tipo_pago, container, false);

        msg2 = vista.findViewById(R.id.msg2);
        msg3 = vista.findViewById(R.id.msg3);
        title = vista.findViewById(R.id.title_toolbar);
        volver = vista.findViewById(R.id.back);
        close = vista.findViewById(R.id.iv_close);

        volver.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);

        if(msgScreenDocument.getTransEname().equals(Trans.GIROS)){
            title.setText(getResources().getString(R.string.shapeOperation));
        }else {
            title.setText(getResources().getString(R.string.selecPago));
        }

        msg2.setText("");
        msg3.setText("");

        drawMenuHardcode(new String[]{"En efectivo","Con tarjeta"},vista);

        return vista;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transView.deleteTimer();

        (transView).counterDownTimer(msgScreenDocument.getTimeOut(), MSGSCREENTIMER);

        classArguments = (transView).getArguments();

        volver.setOnClickListener(view1 -> backTypeTransaction());

        close.setOnClickListener(v -> {
            (transView).deleteTimer();
            (transView).getListener().cancel();
        });
    }

    public void drawMenuHardcode(String[] args, View vista) {
        List<SelectedAccountItem> itemsList = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            SelectedAccountItem selectedItem = new SelectedAccountItem();
            selectedItem.setCurrencyCode(String.valueOf(i));
            selectedItem.setCurrencyDescription("");
            selectedItem.setFamilyCode("");
            selectedItem.setProductDescription(args[i]);

            itemsList.add(selectedItem);
        }
        RecyclerView recyclerView = vista.findViewById(R.id.recyclerUno);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        SeleccionCuentaAdaptadorItem cuentaAdaptadorItem = new SeleccionCuentaAdaptadorItem(itemsList, getContext());
        recyclerView.setAdapter(cuentaAdaptadorItem);
        cuentaAdaptadorItem.setOnClickListener((view, obj, position) ->{
            selectedItem = obj;

            classArguments.setTypepayment(String.valueOf(position + 1));
            (transView).setArgumentsClass(classArguments);
            typeTransaction();


        });
    }

    private void typeTransaction(){
        try {
            if(msgScreenDocument.getTransEname().equals(Trans.GIROS)){

                if(classArguments.getTypetransaction() == 1){

                    NavHostFragment.findNavController(FragmentTypePayment.this)
                            .navigate(R.id.action_fragmentTypePayment_to_fragmentTypeCoin);

                }else {
                    if(selectedItem.getProductDescription().equals("En efectivo")){

                        NavHostFragment.findNavController(FragmentTypePayment.this)
                                .navigate(R.id.action_fragmentTypePayment_to_fragment_datos_beneficiario2);
                    }else {
                        (transView).deleteTimer();
                        (transView).getListener().confirm(InputManager.Style.COMMONINPUT);
                    }
                }
            }else {
                //PAGO DE SERVICIOS
                NavHostFragment.findNavController(FragmentTypePayment.this)
                        .navigate(R.id.action_fragmentTypePayment2_to_fragmentServiceSelector);
            }
        }catch (Exception e){
            ((TransView)activity).getListener().cancel(TcodeError.T_ERR_NAVI_FRAGMENT);
            Logger.logLine(Logger.LOG_EXECPTION, "FragmentTypePayment");
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }

    }

    private void backTypeTransaction(){
        try {
            if(msgScreenDocument.getTransEname().equals(Trans.GIROS)){

                if(classArguments.getTypetransaction() == 1){
                    NavHostFragment.findNavController(FragmentTypePayment.this)
                            .navigate(R.id.action_global_fragmentTypeTransaction);
                }else {
                    NavHostFragment.findNavController(FragmentTypePayment.this)
                            .navigate(R.id.action_global_fragmentTypeTransaction2);
                }

            }else {
                //PAGO DE SERVICIOS
                (transView).getListener().back();
            }
        }catch (Exception e){
            ((TransView)activity).getListener().cancel(TcodeError.T_ERR_RETURN_FRAGMENT);
            Logger.logLine(Logger.LOG_EXECPTION, "FragmentTypePayment");
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
    }
}