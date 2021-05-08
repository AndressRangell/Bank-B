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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.newpos.pay.R;
import com.bcp.document.ClassArguments;
import com.bcp.document.MsgScreenDocument;
import com.bcp.menus.seleccion_cuenta.SeleccionCuentaAdaptadorItem;
import com.bcp.menus.seleccion_cuenta.SelectedAccountItem;
import com.bcp.transactions.callbacks.WaitTransFragment;
import com.newpos.libpay.Logger;
import com.newpos.libpay.presenter.TransView;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.Trans;

import java.util.ArrayList;
import java.util.List;

public class FragmentTypeTransaction extends Fragment {

    View vista;
    TextView msg2;
    TextView msg3;
    TextView title;
    SelectedAccountItem selectedItem;
    ImageView volver;
    Context thiscontext;
    Activity activity;
    ClassArguments classArguments;
    WaitTransFragment waitTransFragment;
    private String MSGSCREENTIMER = "FragmentTypeTransaction";

    TransView transView;
    MsgScreenDocument msgScreenDocument;

    public FragmentTypeTransaction(){
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
        vista = null;
        msg2 = null;
        msg3 = null;
        title = null;
        selectedItem = null;
        volver = null;
        thiscontext = null;
        activity = null;
        classArguments = null;
        waitTransFragment = null;
        transView = null;
        msgScreenDocument = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_type_transaction, container, false);

        msg2 = vista.findViewById(R.id.msg2);
        msg3 = vista.findViewById(R.id.msg3);
        title = vista.findViewById(R.id.title_toolbar);
        volver = vista.findViewById(R.id.back);
        volver.setVisibility(View.VISIBLE);

        title.setText(getResources().getString(R.string.titleTypeTrnas));

        msg2.setText("");
        msg3.setText("");

        drawMenuHardcode(new String[]{"Emisión\nnacional", "Cobro\nnacional"},vista);

        return vista;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transView.deleteTimer();

        (transView).counterDownTimer(msgScreenDocument.getTimeOut(),MSGSCREENTIMER);

        classArguments = (transView).getArguments();

        volver.setOnClickListener(view1 -> {
            transView.deleteTimer();
            transView.getListener().back();
        });

        waitTransFragment = msgScreenDocument.getCallbackTransFragment();
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

            classArguments.setTypetransaction(position + 1);

           try {
              transView.setArgumentsClass(classArguments);
               if(selectedItem.getProductDescription().equals("Emisión\nnacional")){
                   transView.getMsgScreendocument().setListUrl(waitTransFragment.getListUrl(Trans.GIROS_EMISION));
                   setAmount();
                   Navigation.findNavController(view).navigate(R.id.action_fragmentTypeTransaction_to_navigation1);
               }else{
                   transView.getMsgScreendocument().setListUrl(waitTransFragment.getListUrl(Trans.GIROS_COBROS));
                   setAmount();
                   classArguments.setArgument1(null);
                   Navigation.findNavController(view).navigate(R.id.action_fragmentTypeTransaction_to_navigation);
               }
           }catch (Exception e){
               transView.getListener().cancel(TcodeError.T_ERR_NAVI_FRAGMENT);
               Logger.logLine(Logger.LOG_EXECPTION, "FragmentTypeTransaction");
               Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
           }
        });

    }

    private void setAmount(){
        try {
            String[] amounts = waitTransFragment.getAmount();
            transView.getMsgScreendocument().setMaxAmount(amounts[0]);
            transView.getMsgScreendocument().setMinAmount(amounts[1]);
            transView.getMsgScreendocument().setMaxAmountUsd(amounts[2]);
            transView.getMsgScreendocument().setMinAmountUsd(amounts[3]);
        }catch (Exception e){
            ((TransView)activity).getListener().cancel(TcodeError.T_ERR_DATA_NULL);
            Logger.logLine(Logger.LOG_EXECPTION, "FragmentTypeTransaction");
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
    }
}