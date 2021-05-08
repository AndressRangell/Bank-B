package com.bcp.transactions.pagoservicios.fragmentsps;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.desert.keyboard.InputManager;
import com.android.newpos.pay.R;
import com.bcp.document.ClassArguments;
import com.newpos.libpay.presenter.TransView;
import com.newpos.libpay.trans.TcodeError;


public class FragmentDocumetClient extends Fragment {


    Button saltar;
    TextView title;
    TextView titleNumDoc;
    EditText numDocument;
    ClassArguments arguments;
    RelativeLayout relativeKeyboardNume;
    Button btnContinuar;
    Activity activity;
    ImageView close;
    Context thiscontext;
    View vista;
    private static final String MSGSCREENTIMER = "FragmentDocumetClient";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.thiscontext = context;
        this.activity = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_documet_client, container, false);


        title = vista.findViewById(R.id.title_toolbar);
        numDocument = vista.findViewById(R.id.numDoc);
        titleNumDoc = vista.findViewById(R.id.doc_num);
        relativeKeyboardNume = vista.findViewById(R.id.keyboardNumerico);
        btnContinuar = vista.findViewById(R.id.continuar);
        close = vista.findViewById(R.id.iv_close);
        saltar = vista.findViewById(R.id.iv_saltar);


        close.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.titleDoc));
        titleNumDoc.setText(getResources().getString(R.string.titleNumDoc));
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TransView) activity).deleteTimer();
        ((TransView) activity).counterDownTimer(((TransView) activity).getMsgScreendocument().getTimeOut(), MSGSCREENTIMER);

        arguments = ((TransView)activity).getArguments();

        ((TransView) activity).setKeyboard(numDocument ,8,false,true ,((TransView)activity).getMsgScreendocument().isAlfa(),8,relativeKeyboardNume,null);
        numDocument.setHint(R.string.digit_8);

        close.setOnClickListener(view14 -> {
            ((TransView)activity).deleteTimer();
            ((TransView) activity).getListener().cancel(TcodeError.T_USER_CANCEL);
        });

        saltar.setOnClickListener(view12 -> {
            ((TransView)activity).deleteTimer();
            ((TransView)activity).getListener().confirm(InputManager.Style.COMMONINPUT);


        });

        btnContinuar.setOnClickListener(view1 -> {
            ((TransView)activity).deleteTimer();
            ((TransView)activity).getListener().confirm(InputManager.Style.COMMONINPUT);
        });
    }
}