package com.bcp.reportes;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.newpos.pay.R;
import com.bcp.definesbcp.Definesbcp;
import com.bcp.login.Login;
import com.bcp.printer.PrintParameter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.newpos.libpay.Logger;
import com.newpos.libpay.presenter.TransUIImpl;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.Trans;
import com.newpos.libpay.trans.translog.TransLogDataWs;
import com.newpos.libpay.trans.translog.TransLogWs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.desert.newpos.payui.UIUtils;
import cn.desert.newpos.payui.master.ResultControl;

import static com.android.newpos.pay.ProcessingCertificate.polarisUtil;

public class DetailReport extends AppCompatActivity {

    TextView titulo;
    TextView ingefectivo;
    TextView deposito;
    TextView totsoles;
    TextView totoper;

    //total ingreso efectivo
    TextView totingefectivo;
    TextView valTotIngEfectivo;
    TextView totOprIngreso;
    TextView valTotOprIngreso;
    //total salida efectivo
    TextView totSalidafectivo;
    TextView valTotSalidaEfectivo;
    TextView totOprSalida;
    TextView valTotOprSalida;

    TextView salefectivo;
    TextView retiro;
    TextView totsolesfin;
    TextView totaloperfin;
    TextView valTotalOperFin;
    TextView valTotalSolesFin;
    TextView valTotalSoles;
    TextView valTotalOper;

    View view1;
    View view2;
    TextView titOperEfectivo;

    //giros Emision
    TextView tituloTransGirosEmision;
    ListView historyGiroEmision;
    AdapterDetailReport adapterGiroEmision;
    TextView totsolesGirosEmision;
    TextView valTotalSolesGirosEmision;
    TextView totoperGirosEmision;
    TextView valTotalOperGirosEmision;

    //giros Cobros
    TextView tituloTransGirosCobros;
    ListView historyGiroCobros;
    AdapterDetailReport adapterGiroCobros;
    TextView totsolesGirosCobros;
    TextView valTotalSolesGirosCobros;
    TextView totoperGirosCobros;
    TextView valTotalOperGirosCobros;

    //pago de servicios
    TextView tituloTransPagoServicios;
    ListView historyPagoServicios;
    TextView totsolesPagoServicios;
    TextView valTotalSolesPagoServicios;
    TextView totoperPagoServicios;
    TextView valTotalOperPagoServicios;
    AdapterDetailReport adapterPagoServicios;

    TextView operWithCard;
    //giros emision with card
    TextView girosEmisionWithCard;
    ListView historyGiroEmisionCard;
    AdapterDetailReport adapterGiroEmisionCard;
    TextView totoperGirosEmisionCard;
    TextView valTotalOperGirosEmisCard;

    //giros Cobros with card
    TextView girosCobrosWithCard;
    ListView historyGiroCobrosCard;
    AdapterDetailReport adapterGiroCobrosCard;
    TextView totoperGirosCobrosCard;
    TextView valTotalOperGirosCobrosCard;

    //giros Cobros with card
    TextView pagoServiciosWithCard;
    ListView historyPagoServiciosCard;
    TextView totoperPagoServiciosCard;
    TextView valTotalOperPagoServiciosCard;
    AdapterDetailReport adapterPagoServiciosCard;

    TextView operNoMonetary;
    TextView consultaSaldos;
    TextView totOperSaldos;
    TextView valTotOperSaldos;
    TextView consultaMovimientos;
    TextView totOperMovimientos;
    TextView valTotOperMovimientos;


    LinearLayout linearImprimirCombrobante;
    RelativeLayout relativeMsgEmpty;
    ImageButton btnExpand;
    Button btnCnfirm;
    Button btnCancel;

    //Toolbar
    ImageView back;
    TextView textViewTitleToolbar;
    NestedScrollView nestedScrollView;

    LottieAnimationView gifReportePaper;

    ListView historyDep;
    ListView historyRet;
    AdapterDetailReport adapterDeposito;
    AdapterDetailReport adapterRetiro;
    LinearLayout linearDetailDeposito;
    LinearLayout linearNoMonetary;
    LinearLayout linearDetailRetiro;
    LinearLayout linearOperWithCard;

    //TRANSACCIONES
    LinearLayout linearDeposito;
    LinearLayout linearGirosEmision;
    LinearLayout linearRetiro;
    LinearLayout linearGirosCobros;
    LinearLayout transGirosEmisionCard;
    LinearLayout transGirosCobrosCard;
    LinearLayout transPagoServicios;
    LinearLayout transPagoServiciosCard;

    boolean isSettle;
    String keyReport;

    private CountDownTimer countDownTimerReport;
    boolean time = true;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportedetallado);

        nestedScrollView            = findViewById(R.id.contentScroll);
        relativeMsgEmpty            = findViewById(R.id.relativeMsgEmpty);
        gifReportePaper             = findViewById(R.id.gifReportePaper);

        titulo                      = findViewById(R.id.titulo);
        ingefectivo                 = findViewById(R.id.ingefectivo);
        deposito                    = findViewById(R.id.deposito);
        totsoles                    = findViewById(R.id.totsoles);
        totoper                     = findViewById(R.id.totoper);

        totingefectivo              = findViewById(R.id.totingefectivo);
        valTotIngEfectivo           = findViewById(R.id.valTotIngEfectivo);
        totOprIngreso               = findViewById(R.id.totOprIngreso);
        valTotOprIngreso            = findViewById(R.id.valTotOprIngreso);

        totSalidafectivo            =findViewById(R.id.totSalefectivo);
        valTotSalidaEfectivo        = findViewById(R.id.valTotSalEfectivo);
        totOprSalida                = findViewById(R.id.totOprSalida);
        valTotOprSalida             = findViewById(R.id.valTotOprSalida);

        salefectivo                 = findViewById(R.id.salefectivo);
        retiro                      = findViewById(R.id.retiro);
        totsolesfin                 = findViewById(R.id.totsolesfin);
        totaloperfin                = findViewById(R.id.totaloperfin);
        valTotalOperFin             = findViewById(R.id.valTotOperFin);
        valTotalSolesFin            = findViewById(R.id.valTotSolesFin);
        valTotalSoles               = findViewById(R.id.valTotSoles);
        valTotalOper                = findViewById(R.id.valTotOper);

        view1                       = findViewById(R.id.viewsubtitulo1);
        view2                       = findViewById(R.id.viewsubtitulo2);
        titOperEfectivo             = findViewById(R.id.oprEfectivo);

        operNoMonetary              = findViewById(R.id.operNoMonetary);
        consultaSaldos              = findViewById(R.id.consultaSaldos);
        totOperSaldos               = findViewById(R.id.totOperSaldo);
        valTotOperSaldos            = findViewById(R.id.valTotOperSaldo);
        consultaMovimientos         = findViewById(R.id.consultaMovimientos);
        totOperMovimientos          = findViewById(R.id.totOperMovimientos);
        valTotOperMovimientos       = findViewById(R.id.valTotOperMovimientos);

        //giros Emision
        tituloTransGirosEmision     = findViewById(R.id.giroEmision);
        historyGiroEmision          = findViewById(R.id.history_giro_emision);
        totsolesGirosEmision        = findViewById(R.id.totsolesGemision);
        valTotalSolesGirosEmision   = findViewById(R.id.valTotSolesGemision);
        totoperGirosEmision         = findViewById(R.id.totoperGemision);
        valTotalOperGirosEmision    = findViewById(R.id.valTotOperGemision);

        //giros Cobros
        tituloTransGirosCobros      = findViewById(R.id.giroCobros);
        historyGiroCobros           = findViewById(R.id.history_giro_Cobros);
        totsolesGirosCobros         = findViewById(R.id.totsolesGCobros);
        valTotalSolesGirosCobros    = findViewById(R.id.valTotSolesGCobros);
        totoperGirosCobros          = findViewById(R.id.totoperGCobros);
        valTotalOperGirosCobros     = findViewById(R.id.valTotOperGCobros);

        //pago de servicios
        tituloTransPagoServicios    = findViewById(R.id.pagoServicios);
        historyPagoServicios        = findViewById(R.id.historyPagoServicios);
        totsolesPagoServicios       = findViewById(R.id.totsolesPagoServicios);
        valTotalSolesPagoServicios  = findViewById(R.id.valTotSolesPagoServicios);
        totoperPagoServicios        = findViewById(R.id.totoperPagoServicios);
        valTotalOperPagoServicios   = findViewById(R.id.valTotOperPagoServicios);

        back                        = findViewById(R.id.back);
        btnCnfirm                   = findViewById(R.id.btnCnfirm);
        btnCancel                   = findViewById(R.id.btnCancel);
        btnExpand                   = findViewById(R.id.btnExpand);
        linearImprimirCombrobante   = findViewById(R.id.linearImprimirCombrobante);

        historyDep                  = findViewById(R.id.history_dep);
        historyRet                  = findViewById(R.id.history_ret);

        linearDetailDeposito        = findViewById(R.id.linearDetailDeposito);
        linearDetailRetiro          = findViewById(R.id.linearDetailRetiro);
        linearNoMonetary            = findViewById(R.id.linearTransactionNoMonetary);
        linearOperWithCard          = findViewById(R.id.linearOperWithCard);
        transPagoServicios          = findViewById(R.id.transPagoServicios);
        transPagoServiciosCard      = findViewById(R.id.transPagoServiciosCard);

        linearDeposito              = findViewById(R.id.transDeposito);
        linearGirosEmision          = findViewById(R.id.transGirosEmision);
        linearRetiro                = findViewById(R.id.transRetiro);
        linearGirosCobros           = findViewById(R.id.transGirosCobros);
        transGirosEmisionCard       = findViewById(R.id.transGirosEmisionCard);
        transGirosCobrosCard        = findViewById(R.id.transGirosCobrosCard);

        textViewTitleToolbar        = findViewById(R.id.title_toolbar);
        operWithCard                = findViewById(R.id.operWithCard);

        //giros emision with card
        girosEmisionWithCard        = findViewById(R.id.giroEmisionCard);
        historyGiroEmisionCard      = findViewById(R.id.history_giro_Emision_Card);
        totoperGirosEmisionCard     = findViewById(R.id.totoperGEmisionCard);
        valTotalOperGirosEmisCard   = findViewById(R.id.valTotOperGEmisionCard);

        //giros Cobros with card
        girosCobrosWithCard         = findViewById(R.id.giroCobrosCard);
        historyGiroCobrosCard       = findViewById(R.id.history_giro_Cobros_Card);
        totoperGirosCobrosCard      = findViewById(R.id.totoperGCobrosCard);
        valTotalOperGirosCobrosCard = findViewById(R.id.valTotOperGCobrosCard);

        //pago de servicios con tarjeta
        pagoServiciosWithCard       = findViewById(R.id.pagoServiciosCard);
        historyPagoServiciosCard    = findViewById(R.id.historyPagoServiciosCard);
        totoperPagoServiciosCard    = findViewById(R.id.totoperPagoServiciosCard);
        valTotalOperPagoServiciosCard = findViewById(R.id.valTotOperPagoServiciosCard);

        back.setVisibility(View.VISIBLE);
        textViewTitleToolbar.setVisibility(View.VISIBLE);

        back.setOnClickListener(onClickListener);
        btnCnfirm.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(onClickListener);
        btnExpand.setOnClickListener(onClickListener);
        findViewById(R.id.iv_close).setOnClickListener(onClickListener);

        final BottomSheetBehavior bsb = BottomSheetBehavior.from(linearImprimirCombrobante);

        keyReport = Objects.requireNonNull(getIntent().getStringExtra(getResources().getString(R.string.msgReporte)));
        fillTittle(keyReport);

        bsb.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                String estado = "";
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        estado = "STATE_COLLAPSED";
                        btnExpand.setBackground(getDrawable(R.drawable.ic_expand_black_24dp));
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        estado = "STATE_EXPANDED1";
                        btnExpand.setBackground(getDrawable(R.drawable.ic_reduce_black_24dp));
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        estado = "STATE_HIDDEN";
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        estado = "STATE_DRAGGING";
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        estado = "STATE_SETTLING";
                        break;
                    default:
                        break;
                }
                Log.i("BottomSheets", getResources().getString(R.string.msgEstado) + estado);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i("BottomSheets", getResources().getString(R.string.msgOffset)+ slideOffset);
            }
        });

        counterDownTimerReport();

        nestedScrollView.setOnTouchListener((v, event) -> {
            counterDownTimerReport();
            return false;
        });
    }

    private void fillTittle(String reporte) {
        if (reporte.equals(getResources().getString(R.string.repDetail))) {
            nestedScrollView.setPadding(0, 0, 0, 60);
            totsolesfin.setVisibility(View.VISIBLE);
            totaloperfin.setVisibility(View.VISIBLE);
            valTotalOperFin.setVisibility(View.VISIBLE);
            valTotalSolesFin.setVisibility(View.VISIBLE);
        } else if (reporte.equals(getResources().getString(R.string.totSettle))) {
            isSettle = true;
            nestedScrollView.setPadding(0, 0, 0, 185);
            linearImprimirCombrobante.setVisibility(View.VISIBLE);
        }

        textViewTitleToolbar.setText(reporte);
        titulo.setText(reporte);
        try {
            loadData();
        }catch (Exception e){
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
    }

    private final View.OnClickListener onClickListener = (View v) -> {
            switch (v.getId()) {
                case R.id.btnCnfirm:
                    deleteTimerMenus();
                    nestedScrollView.setVisibility(View.INVISIBLE);

                    linearImprimirCombrobante.setVisibility(View.GONE);
                    findViewById(R.id.appbar).setVisibility(View.GONE);
                    gifReportePaper.setVisibility(View.VISIBLE);

                    polarisUtil.setCallbackIsPrinterSettle(null);
                    polarisUtil.setCallbackIsPrinterSettle(status -> {
                        if (status == 0) {
                            finish();
                        }
                    });

                    UIUtils.intentTrans(PrintParameter.class,"typeReport",Definesbcp.ITEM_REPORTE_DETALLADO,false, DetailReport.this);
                    break;
                case R.id.back:
                case R.id.btnCancel:
                case R.id.iv_close:
                    deleteTimerMenus();
                    if (keyReport.equals(getResources().getString(R.string.totSettle)))
                        UIUtils.startResult(DetailReport.this, ResultControl.class, true, false, new String[] {getResources().getString(R.string.msgCierreTotal),String.valueOf(TcodeError.T_USER_CANCEL), TransUIImpl.getErrInfo(String.valueOf(TcodeError.T_USER_CANCEL))}, false);
                    finish();
                    break;
                default:
                    break;
            }
    };

    private void loadData() {
        List<TransLogDataWs> list = TransLogWs.getInstance().getData();
        ArrayList<TransLogDataWs> tempDeposito = new ArrayList<>();
        ArrayList<TransLogDataWs> tempRetiro = new ArrayList<>();
        ArrayList<TransLogDataWs> tempGirosCobros = new ArrayList<>();
        ArrayList<TransLogDataWs> tempGirosEmision = new ArrayList<>();
        ArrayList<TransLogDataWs> tempGirosCobrosCard = new ArrayList<>();
        ArrayList<TransLogDataWs> tempGirosEmisionCard = new ArrayList<>();
        ArrayList<TransLogDataWs> tempPagoServicios = new ArrayList<>();
        ArrayList<TransLogDataWs> tempPagoServiciosCard = new ArrayList<>();
        long amountDep = 0;
        long amountGirEmision = 0;
        long amountPagoServicios = 0;
        long amountSolRet = 0;
        long amountGirCobro = 0;
        long amount = 0;
        int consultaSaldo = 0;
        int consultaMovimiento = 0;
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getAmount() != null) {
                    if (list.get(i).getNameTrans().equals(Trans.GIROS) || list.get(i).getNameTrans().equals(Trans.PAGOSERVICIOS))
                        amount = Integer.parseInt(list.get(i).getTotalAmount().replaceAll("[,.]",""));
                    else
                        amount = Integer.parseInt(list.get(i).getAmount().replaceAll("[,.]",""));
                }
                switch (list.get(i).getNameTrans()){
                    case Trans.DEPOSITO:
                        tempDeposito.add(list.get(i));
                        amountDep += amount;
                        break;
                    case Trans.GIROS:
                        if (list.get(i).getEntryMode().equals("5")){
                            if (list.get(i).getMoneyOut().equals("1")){
                                tempGirosEmisionCard.add(list.get(i));
                            }else{
                                tempGirosCobrosCard.add(list.get(i));
                            }
                        }else {
                            if (list.get(i).getMoneyOut().equals("1")){
                                tempGirosEmision.add(list.get(i));
                                amountGirEmision += amount;
                            }else{
                                tempGirosCobros.add(list.get(i));
                                amountGirCobro += amount;
                            }
                        }
                        break;
                    case Trans.RETIRO:
                        tempRetiro.add(list.get(i));
                        amountSolRet += amount;
                        break;
                    case Trans.CONSULTAS:
                        if (list.get(i).getTypeQuery().equals(Definesbcp.SALDO)){
                            consultaSaldo++;
                        }else if (list.get(i).getTypeQuery().equals(Definesbcp.MOVIMIENTOS)){
                            consultaMovimiento++;
                        }
                        break;
                    case Trans.PAGOSERVICIOS:
                        if (list.get(i).getEntryMode().equals("5"))
                            tempPagoServiciosCard.add(list.get(i));
                        else{
                            tempPagoServicios.add(list.get(i));
                            amountPagoServicios += amount;
                        }

                        break;
                    default:
                        break;
                }
            }
        } else {
            nestedScrollView.setVisibility(View.GONE);
            linearImprimirCombrobante.setVisibility(View.GONE);
            relativeMsgEmpty.setVisibility(View.VISIBLE);
            textViewTitleToolbar.setVisibility(View.GONE);
        }

        String varSpace = "              ";

        if (!tempDeposito.isEmpty() || !tempGirosEmision.isEmpty() || !tempPagoServicios.isEmpty()) {

            ingefectivo.setText(R.string.tittleCargCta);

            String totSoles = getResources().getString(R.string.totSoles) + varSpace;
            String totoperaci = getResources().getString(R.string.totOper) + varSpace;

            if (!tempGirosEmision.isEmpty()){
                historyGiroEmision.getLayoutParams().height = tempGirosEmision.size() * 40;

                //giros Emision
                tituloTransGirosEmision.setText(R.string.tittleGiroEmision);

                adapterGiroEmision = new AdapterDetailReport(this, tempGirosEmision);
                historyGiroEmision.setAdapter(adapterGiroEmision);

                totsolesGirosEmision.setText(totSoles);

                valTotalSolesGirosEmision.setText(UIUtils.formatMiles(String.valueOf(amountGirEmision)));
                totoperGirosEmision.setText(totoperaci);

                valTotalOperGirosEmision.setText(String.valueOf(tempGirosEmision.size()));
            }else {
                linearGirosEmision.setVisibility(View.GONE);
            }

            if (!tempDeposito.isEmpty()){
                historyDep.getLayoutParams().height = tempDeposito.size() * 40;

                deposito.setText(R.string.tittleDep);
                adapterDeposito = new AdapterDetailReport(this, tempDeposito);
                historyDep.setAdapter(adapterDeposito);

                totsoles.setText(totSoles);

                valTotalSoles.setText(UIUtils.formatMiles(String.valueOf(amountDep)));


                totoper.setText(totoperaci);

                valTotalOper.setText(String.valueOf(tempDeposito.size()));
            }else {
                linearDeposito.setVisibility(View.GONE);
            }

            if (!tempPagoServicios.isEmpty()){
                historyPagoServicios.getLayoutParams().height = tempDeposito.size() * 40;

                tituloTransPagoServicios.setText(R.string.tittlePS);
                adapterPagoServicios = new AdapterDetailReport(this, tempPagoServicios);
                historyPagoServicios.setAdapter(adapterPagoServicios);

                totsolesPagoServicios.setText(totSoles);

                valTotalSolesPagoServicios.setText(UIUtils.formatMiles(String.valueOf(amountPagoServicios)));


                totoperPagoServicios.setText(totoperaci);

                valTotalOperPagoServicios.setText(String.valueOf(tempPagoServicios.size()));
            }else {
                transPagoServicios.setVisibility(View.GONE);
            }

            String totalAmount = " S/ " + UIUtils.formatMiles(String.valueOf(amountDep + amountGirEmision + amountPagoServicios));
            totingefectivo.setText(getResources().getString(R.string.totIngEfec));
            valTotIngEfectivo.setText(totalAmount);
            totOprIngreso.setText(getResources().getString(R.string.totOprIngEfec));
            valTotOprIngreso.setText(String.valueOf(tempDeposito.size() + tempGirosEmision.size() + tempPagoServicios.size()));
        } else {
            linearDetailDeposito.setVisibility(View.GONE);
        }

        if (!tempRetiro.isEmpty() || !tempGirosCobros.isEmpty()) {

            salefectivo.setText(R.string.tittleAbnCta);

            String totalSoles = getResources().getString(R.string.totSoles) + varSpace;
            String totalOperaciones = getResources().getString(R.string.totOper) + varSpace;

            if (!tempRetiro.isEmpty()){
                historyRet.getLayoutParams().height = tempRetiro.size() * 40;

                retiro.setText(R.string.tittleRet);

                adapterRetiro = new AdapterDetailReport(this, tempRetiro);
                historyRet.setAdapter(adapterRetiro);

                totsolesfin.setText(totalSoles);

                valTotalSolesFin.setText(UIUtils.formatMiles(String.valueOf(amountSolRet)));


                totaloperfin.setText(totalOperaciones);

                valTotalOperFin.setText(String.valueOf(tempRetiro.size()));
            }else {
                linearRetiro.setVisibility(View.GONE);
            }

            if (!tempGirosCobros.isEmpty()){
                historyGiroCobros.getLayoutParams().height = tempGirosCobros.size() * 40;

                //giros Cobros
                tituloTransGirosCobros.setText(R.string.tittleGiroCobros);

                adapterGiroCobros = new AdapterDetailReport(this,tempGirosCobros);
                historyGiroCobros.setAdapter(adapterGiroCobros);

                totsolesGirosCobros.setText(totalSoles);

                valTotalSolesGirosCobros.setText(UIUtils.formatMiles(String.valueOf(amountGirCobro)));

                totoperGirosCobros.setText(totalOperaciones);
                valTotalOperGirosCobros.setText(String.valueOf(tempGirosCobros.size()));
            }else {
                linearGirosCobros.setVisibility(View.GONE);
            }

            totSalidafectivo.setText(getResources().getString(R.string.totSalEfec));
            String amountExit = " S/ " + UIUtils.formatMiles(String.valueOf(amountSolRet + amountGirCobro));
            valTotSalidaEfectivo.setText(amountExit);
            totOprSalida.setText(getResources().getString(R.string.totOprSalEfec));
            valTotOprSalida.setText(String.valueOf(tempRetiro.size() + tempGirosCobros.size()));

        } else {
            linearDetailRetiro.setVisibility(View.GONE);
        }

        if (consultaSaldo > 0 || consultaMovimiento > 0){

            if (consultaSaldo > 0){
                valTotOperSaldos.setText(String.valueOf(consultaSaldo));
            }else {
                consultaSaldos.setVisibility(View.GONE);
                totOperSaldos.setVisibility(View.GONE);
                valTotOperSaldos.setVisibility(View.GONE);
            }

            if (consultaMovimiento > 0){
                valTotOperMovimientos.setText(String.valueOf(consultaMovimiento));
            }else {
                consultaMovimientos.setVisibility(View.GONE);
                totOperMovimientos.setVisibility(View.GONE);
                valTotOperMovimientos.setVisibility(View.GONE);
            }
        }else {
            linearNoMonetary.setVisibility(View.GONE);
        }

        if (!tempGirosCobrosCard.isEmpty() || !tempGirosEmisionCard.isEmpty() || !tempPagoServiciosCard.isEmpty()){
            //titulo segmento con tarjeta
            operWithCard.setText(getResources().getString(R.string.oprWithCard));

            if (!tempPagoServiciosCard.isEmpty()){
                pagoServiciosWithCard.setText(getResources().getString(R.string.tittlePS));

                historyPagoServiciosCard.getLayoutParams().height = tempPagoServiciosCard.size() * 40;
                adapterPagoServiciosCard= new AdapterDetailReport(this, tempPagoServiciosCard);
                historyPagoServiciosCard.setAdapter(adapterPagoServiciosCard);

                totoperPagoServiciosCard.setText(getResources().getString(R.string.totOper));
                valTotalOperPagoServiciosCard.setText(String.valueOf(tempPagoServiciosCard.size()));
            }else {
                transPagoServiciosCard.setVisibility(View.GONE);
            }

            if (!tempGirosEmisionCard.isEmpty()){
                girosEmisionWithCard.setText(getResources().getString(R.string.tittleGiroEmisionCard));

                historyGiroEmisionCard.getLayoutParams().height = tempGirosEmisionCard.size() * 40;
                adapterGiroEmisionCard= new AdapterDetailReport(this, tempGirosEmisionCard);
                historyGiroEmisionCard.setAdapter(adapterGiroEmisionCard);

                totoperGirosEmisionCard.setText(getResources().getString(R.string.totOper));
                valTotalOperGirosEmisCard.setText(String.valueOf(tempGirosEmisionCard.size()));
            }else {
                transGirosEmisionCard.setVisibility(View.GONE);
            }

            if (!tempGirosCobrosCard.isEmpty()){
                girosCobrosWithCard.setText(getResources().getString(R.string.tittleGiroCobrosCard));

                historyGiroCobrosCard.getLayoutParams().height = tempGirosCobrosCard.size() * 40;
                adapterGiroCobrosCard= new AdapterDetailReport(this, tempGirosCobrosCard);
                historyGiroCobrosCard.setAdapter(adapterGiroCobrosCard);

                totoperGirosCobrosCard.setText(getResources().getString(R.string.totOper));
                valTotalOperGirosCobrosCard.setText(String.valueOf(tempGirosCobrosCard.size()));
            }else {
                transGirosCobrosCard.setVisibility(View.GONE);
            }
        }else {
            linearOperWithCard.setVisibility(View.GONE);
        }
    }

    private void deleteTimerMenus() {
        if (countDownTimerReport != null) {
            countDownTimerReport.cancel();
            countDownTimerReport = null;
        }
    }

    private void counterDownTimerReport() {
        if (countDownTimerReport != null) {
            countDownTimerReport.cancel();
            countDownTimerReport = null;
        }
        countDownTimerReport = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                Log.d("onTick", getResources().getString(R.string.msgInitTimerSettle) + millisUntilFinished/1000);
            }
            public void onFinish() {
                Log.d("onTick", getResources().getString(R.string.msgfinishTimerSettle));
                if(time){
                    counterDownTimerReport();
                    msgAccountSingle();
                }else {
                    finishActivity();
                }
            }
        }.start();
    }

    private void msgAccountSingle(){
        time = false;
        final RelativeLayout relativeLayout = findViewById(R.id.msgAccount);
        relativeLayout.setVisibility(View.VISIBLE);
        TextView tvTittle = findViewById(R.id.tittleMsgScreen);
        TextView tvMsg1 = findViewById(R.id.msg1Screen);
        TextView tvMsg2 = findViewById(R.id.msg2Screen);
        TextView tvMsg3 = findViewById(R.id.msg3Screen);

        Button btnEntentido = findViewById(R.id.entendidoMsg);
        Button btnSalir = findViewById(R.id.exitMsg);

        tvTittle.setText(getResources().getString(R.string.timeExh));
        tvMsg1.setVisibility(View.GONE);
        tvMsg2.setText(getResources().getString(R.string.msgContinuar));
        tvMsg3.setText(getResources().getString(R.string.msgOperacion));

        btnEntentido.setText(getResources().getString(R.string.continuar));
        btnSalir.setVisibility(View.VISIBLE);
        btnSalir.setText(getResources().getString(R.string.msgAccountClientBtnExit));

        relativeLayout.setOnClickListener(v -> {
            //nothing
        });

        btnSalir.setOnClickListener(v -> finishActivity());

        btnEntentido.setOnClickListener(v ->{
            time = true;
            counterDownTimerReport();
            relativeLayout.setVisibility(View.INVISIBLE);
        });
    }

    private void finishActivity(){
        UIUtils.intentTrans(Login.class,"","",false, DetailReport.this);
        deleteTimerMenus();
        finish();
    }

    @Override
    public void onBackPressed() {}
}