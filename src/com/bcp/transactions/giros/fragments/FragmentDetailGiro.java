package com.bcp.transactions.giros.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.android.newpos.pay.R;
import com.bcp.document.ClassArguments;
import com.bcp.document.MsgScreenDocument;
import com.newpos.libpay.Logger;
import com.newpos.libpay.presenter.TransView;
import com.newpos.libpay.trans.TcodeError;

import cn.desert.newpos.payui.UIUtils;

import static com.bcp.inicializacion.tools.PolarisUtil.IGUALORDENANTESI;


public class FragmentDetailGiro extends Fragment {

    private ImageView close;
    private ImageButton editarMonto;
    private ImageButton editarBenGiro;
    private ImageButton editarOrdenGiro;
    private ImageButton editarSolicitante;
    private Button btnContinuar;
    private TextView nameBenfGiro;
    private TextView dniBenf;
    private TextView nameOrdGiro;
    private TextView dniOrde;
    private TextView nameSender;
    private TextView dniSender;
    private TextView monto;
    private TextView comisionGiro;
    private TextView total;
    private TextView tipoCambio;
    private TextView totalPagar;
    private TextView simbolMonto;
    private TextView simbolTotal;
    private TextView simbcomision;
    private TextView simboltotalPagar;
    private TextView titOrdenante;
    private LinearLayout linComisionGiro;
    private RelativeLayout solicitanteGiro;
    private Activity activity;
    private LinearLayout linTotal;
    private LinearLayout linTipoCambio;

    RelativeLayout ordenanteGiro;
    View vista;
    TextView etTitle;
    Context thiscontext;
    ClassArguments classArguments;
    NestedScrollView nestedScrollView;
    String editable;

    private static final String MSGSCREENTIMER = "FragmentDetailGiro";
    private static final String BENEFICIARY = "0";
    private static final String SENDER = "1";
    private static final String REMITTER = "2";

    TransView transView;
    MsgScreenDocument msgScreenDocument;

    public FragmentDetailGiro(){
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
        close = null;
        editarMonto = null;
        editarBenGiro = null;
        editarOrdenGiro = null;
        editarSolicitante = null;
        btnContinuar = null;
        nameBenfGiro = null;
        dniBenf = null;
        nameOrdGiro = null;
        dniOrde = null;
        nameSender = null;
        dniSender = null;
        monto = null;
        comisionGiro = null;
        total = null;
        tipoCambio = null;
        totalPagar = null;
        simbolMonto = null;
        simbolTotal = null;
        simbcomision = null;
        simboltotalPagar = null;
        titOrdenante = null;
        linComisionGiro = null;
        solicitanteGiro = null;
        activity = null;
        linTotal = null;
        linTipoCambio = null;

        ordenanteGiro = null;
        vista = null;
        etTitle = null;
        thiscontext = null;
        classArguments = null;
        nestedScrollView = null;
        editable = null;

        transView = null;
        msgScreenDocument = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_detalle_giro, container, false);

        nestedScrollView = vista.findViewById(R.id.scrollDG);

        etTitle           = vista.findViewById(R.id.title_toolbar);
        close             = vista.findViewById(R.id.iv_close);
        editarMonto       = vista.findViewById(R.id.EditarMonto);
        editarBenGiro     = vista.findViewById(R.id.EditarBenGiro);
        editarOrdenGiro   = vista.findViewById(R.id.EditarOrdGiro);
        editarSolicitante = vista.findViewById(R.id.EditarSolicGiro);
        btnContinuar      = vista.findViewById(R.id.confirm);
        linTotal          = vista.findViewById(R.id.linearTotal);
        linTipoCambio     = vista.findViewById(R.id.linearTipCambio);
        nameBenfGiro      = vista.findViewById(R.id.NombrebenefGiro);
        dniBenf           = vista.findViewById(R.id.DniBene);
        nameOrdGiro       = vista.findViewById(R.id.ordenGiro);
        dniOrde           = vista.findViewById(R.id.DniordenGiro);
        nameSender        = vista.findViewById(R.id.soliGiro);
        dniSender         = vista.findViewById(R.id.DniSoli);
        monto             = vista.findViewById(R.id.monGiro);
        comisionGiro      = vista.findViewById(R.id.comision);
        total             = vista.findViewById(R.id.TotalTxt);
        tipoCambio        = vista.findViewById(R.id.tipeCambio);
        totalPagar        = vista.findViewById(R.id.totalPagar);
        simbolMonto       = vista.findViewById(R.id.simbolMonto);
        simbolTotal       = vista.findViewById(R.id.simbolTotal);
        simbcomision      = vista.findViewById(R.id.simbolComision);
        simboltotalPagar  = vista.findViewById(R.id.simbolTotPagar);
        linComisionGiro   = vista.findViewById(R.id.linearComision);
        ordenanteGiro     = vista.findViewById(R.id.linearOrdenGiro);
        solicitanteGiro   = vista.findViewById(R.id.linearSolciGiro);
        titOrdenante      = vista.findViewById(R.id.msg1);

        close.setVisibility(View.VISIBLE);
        etTitle.setText(getResources().getString(R.string.titleDetallGiro));

        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transView.deleteTimer();

        (transView).counterDownTimer(msgScreenDocument.getTimeOut(), MSGSCREENTIMER);

        classArguments = (transView).getArguments();

        try {

            editable = classArguments.getEditEditable();

            nameBenfGiro.setText(classArguments.getNameBeneficiary());
            dniBenf.setText(String.format("%s %s", classArguments.getTypeDocumentBeneficiary(), classArguments.getDniBeneficiary()));

            if (classArguments.getTypeclient() == IGUALORDENANTESI){
                solicitanteGiro.setVisibility(View.GONE);
                titOrdenante.setText(getActivity().getResources().getString(R.string.ordeSolicGiro));
                nameOrdGiro.setText(classArguments.getNameSender());
                dniOrde.setText(String.format("%s %s", classArguments.getTypeDocumentSender(), classArguments.getDniSender()));
            }else {
                nameSender.setText(classArguments.getNameSender());
                dniSender.setText(String.format("%s %s", classArguments.getTypeDocumentSender(), classArguments.getDniSender()));

                nameOrdGiro.setText(classArguments.getNameRemitter());
                dniOrde.setText(String.format("%s %s", classArguments.getTypeDocumentRemitter(), classArguments.getDniRemitter()));
            }

            simbolMonto.setText(classArguments.getTypeCoin().equals("PEN") ? "S/" : "US$");
            monto.setText(classArguments.getMonto());

            if (classArguments.getSimbcomision() != null){
                simbcomision.setText(classArguments.getSimbcomision());
                comisionGiro.setText(classArguments.getComissionAmount());
            }else
                linComisionGiro.setVisibility(View.GONE);

            if(classArguments.getSimbolTotal() != null){
                simbolTotal.setText(UIUtils.checkNull(classArguments.getSimbolTotal()));
                total.setText(UIUtils.checkNull(classArguments.getTotal()));
                tipoCambio.setText(UIUtils.checkNull(classArguments.getTipoCambio()));
            }else {
                linTotal.setVisibility(View.GONE);
                linTipoCambio.setVisibility(View.GONE);
            }

            simboltotalPagar.setText(classArguments.getSimboltotalPagar());
            totalPagar.setText(classArguments.getTotalPagar());

            if(classArguments.getTypepayment().equals("2")){
                editarOrdenGiro.setVisibility(View.GONE);
                editarSolicitante.setVisibility(View.GONE);
            }

            if(classArguments.getEditEditable().equals("1")){
                editarMonto.setOnClickListener(view12 -> {

                    classArguments.setEditar(true);
                    if (classArguments.getTypepayment().equals("2")) {
                        NavHostFragment.findNavController(FragmentDetailGiro.this)
                                .navigate(R.id.action_fragmentDetailGiro_to_fragmentTypeCoin3);
                    } else {
                        NavHostFragment.findNavController(FragmentDetailGiro.this)
                                .navigate(R.id.action_fragmentDetalleGiro_to_fragmentTypeCoin);
                    }
                });
            }else {
                editarMonto.setVisibility(View.GONE);
            }

             if(classArguments.getEditBenef().equals("1")){
                 editarBenGiro.setOnClickListener(view13 -> {
                     classArguments.setEditableGiros(BENEFICIARY);
                     classArguments.setEditar(true);
                     transView.setArgumentsClass(classArguments);

                     if(classArguments.getTypepayment().equals("2")){
                         NavHostFragment.findNavController(FragmentDetailGiro.this)
                                 .navigate(R.id.action_fragmentDetailGiro_to_fragmentEditData2);
                     }else {
                         NavHostFragment.findNavController(FragmentDetailGiro.this)
                                 .navigate(R.id.action_fragmentDetalleGiro_to_fragmentEditData);
                     }
                 });
             }else {
                 editarBenGiro.setVisibility(View.GONE);
             }

             if(classArguments.getEditRemit().equals("1")){
                 editarOrdenGiro.setOnClickListener(view14 -> {
                     classArguments.setEditableGiros(classArguments.getTypeclient() == IGUALORDENANTESI ? SENDER : REMITTER);
                     classArguments.setEditar(true);
                     transView.setArgumentsClass(classArguments);
                     NavHostFragment.findNavController(FragmentDetailGiro.this)
                             .navigate(R.id.action_fragmentDetalleGiro_to_fragmentEditData);
                 });
             }else{
                 editarOrdenGiro.setVisibility(View.GONE);
             }

             if(classArguments.getEditSender().equals("1")){
                 editarSolicitante.setOnClickListener(view15 -> {
                     classArguments.setEditableGiros(SENDER);classArguments.setEditar(true);
                     transView.setArgumentsClass(classArguments);
                     NavHostFragment.findNavController(FragmentDetailGiro.this)
                             .navigate(R.id.action_fragmentDetalleGiro_to_fragmentEditData);
                 });
             }else {
                 editarSolicitante.setVisibility(View.GONE);
             }

            btnContinuar.setOnClickListener(view16 -> {
                (transView).setArgumentsClass(classArguments);
                if (classArguments.getTypepayment().equals("2")) {
                    NavHostFragment.findNavController(FragmentDetailGiro.this)
                            .navigate(R.id.action_fragmentDetailGiro_to_fragmentPassword3);
                } else {
                    NavHostFragment.findNavController(FragmentDetailGiro.this)
                            .navigate(R.id.action_fragmentDetalleGiro_to_fragmentPassword2);
                }
            });
        }catch (Exception e){
            ((TransView)activity).getListener().cancel(TcodeError.T_ERR_DATA_NULL);
            Logger.logLine(Logger.LOG_EXECPTION, "FragmentDetailGiro");
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }

        close.setOnClickListener(v -> (transView).getListener().cancel());
    }
}