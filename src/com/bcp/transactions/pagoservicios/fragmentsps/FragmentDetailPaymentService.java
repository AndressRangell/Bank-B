package com.bcp.transactions.pagoservicios.fragmentsps;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.desert.keyboard.InputManager;
import com.android.newpos.pay.R;
import com.bcp.document.ClassArguments;
import com.newpos.libpay.presenter.TransView;
import com.newpos.libpay.trans.Trans;

public class FragmentDetailPaymentService extends Fragment {


    View vista;
    ImageView close;
    ImageView volver;
    Button confirmar;
    TextView title;
    TextView titleTypeService;
    TextView codtypeservice;
    TextView nombreEmpresa;
    TextView monto;
    TextView otrosCargos;
    TextView comision;
    TextView totalPagar;
    TextView simbolMonto;
    TextView simbolComision;
    TextView simbolOtrosCarg;
    TextView simbolTotal;
    LinearLayout tipoCuenta;
    LinearLayout tipoCambio;
    LinearLayout linPropietario;
    TextView namepropietario;

    Context thiscontext;
    Activity activity;
    private ClassArguments classArguments;
    private static final String MSGSCREENTIMER = "FragmentDetailPaymentService";



    @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            this.thiscontext = context;
            this.activity = getActivity();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment

            vista = inflater.inflate(R.layout.fragment_detail_payment_service, container, false);

            close            = vista.findViewById(R.id.iv_close);
            title            = vista.findViewById(R.id.title_toolbar);
            volver           = vista.findViewById(R.id.back);
            confirmar        = vista.findViewById(R.id.confirm);
            titleTypeService = vista.findViewById(R.id.typeservice);
            codtypeservice   = vista.findViewById(R.id.Codigo);
            nombreEmpresa    = vista.findViewById(R.id.NombEmpresa);
            tipoCuenta       = vista.findViewById(R.id.linTipoCuenta);
            monto            = vista.findViewById(R.id.montoPS);
            otrosCargos      = vista.findViewById(R.id.OtroCargoPS);
            comision         = vista.findViewById(R.id.ComisionPago);
            totalPagar       = vista.findViewById(R.id.TxtotalPagarPS);
            simbolMonto      = vista.findViewById(R.id.simbolMontoPS);
            simbolOtrosCarg  = vista.findViewById(R.id.simbolOtroCargoPS);
            simbolComision   = vista.findViewById(R.id.simbComisionPS);
            simbolTotal      = vista.findViewById(R.id.simbolTotPagarPS);
            tipoCambio       = vista.findViewById(R.id.msgPagoServicios);
            linPropietario   = vista.findViewById(R.id.linPropietario);
            namepropietario  = vista.findViewById(R.id.NamePropietario);


            volver.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            close.setVisibility(View.VISIBLE);
            tipoCuenta.setVisibility(View.VISIBLE);
            title.setText(getResources().getString(R.string.titleDetPagoServ));

            return vista;
        }
        @Override
        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            ((TransView)activity).deleteTimer();
            ((TransView)activity).counterDownTimer(((TransView)activity).getMsgScreendocument().getTimeOut(), MSGSCREENTIMER);
            classArguments = ((TransView)activity).getArguments();

            nombreEmpresa.setText(classArguments.getEmpresa());
            titleTypeService.setText(classArguments.getTypeservice());
            codtypeservice.setText(classArguments.getCodTypeService());
            simbolMonto.setText(classArguments.getSimbolTotal());
            monto.setText(classArguments.getMonto());
            simbolOtrosCarg.setText(classArguments.getSimOtroCargo());
            otrosCargos.setText(classArguments.getOtroCargo());
            simbolComision.setText(classArguments.getSimbcomision());
            comision.setText(classArguments.getComissionAmount());
            simbolTotal.setText(classArguments.getSimboltotalPagar());
            totalPagar.setText(classArguments.getTotalPagar());
            if(classArguments.getClientDepositName() != null && !classArguments.getClientDepositName() .equals("")){
                linPropietario.setVisibility(View.VISIBLE);
                namepropietario.setText(classArguments.getClientDepositName());
            }

            if(classArguments.getStatusTipoCambio() != null && classArguments.getStatusTipoCambio().equals("true")){
                tipoCambio.setVisibility(View.VISIBLE);
            }

            confirmar.setOnClickListener(view1 -> {
                classArguments.getTipoPagoServicio();
                if(classArguments.getTypepayment().equals("1")){
                        botoncontinuar();
                }else {
                    ((TransView)activity).deleteTimer();
                    ((TransView)activity).getListener().confirm(InputManager.Style.COMMONINPUT);
                }
            });

            close.setOnClickListener(view1 -> {
                ((TransView)activity).deleteTimer();
                ((TransView)activity).getListener().cancel();
            });
        }

        private void botoncontinuar(){

        switch (classArguments.getTipoPagoServicio()){
            case Trans.PAGO_PASEMOVISTAR:
                NavHostFragment.findNavController(FragmentDetailPaymentService.this)
                        .navigate(R.id.action_fragmentDetailPaymentService_to_fragmentDocumetClient);
                break;
            case Trans.PAGO_CONRANGO:
            case Trans.PAGO_SINVALIDACION:
            case Trans.PAGO_SINRANGO:
            case Trans.PAGO_IMPORTE:
            case Trans.PAGO_PARCIAL:
                NavHostFragment.findNavController(FragmentDetailPaymentService.this)
                        .navigate(R.id.action_fragmentDetailPaymentService2_to_fragmentDocumetClient2);
                break;
            default:
                break;
        }
    }
}