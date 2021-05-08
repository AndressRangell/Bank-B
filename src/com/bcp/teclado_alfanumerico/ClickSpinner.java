package com.bcp.teclado_alfanumerico;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.android.newpos.pay.R;
import com.bcp.document.ClassArguments;
import com.bcp.transactions.callbacks.WaitPaymentServices;
import com.bcp.transactions.callbacks.WaitTypeDocument;
import com.newpos.libpay.presenter.TransView;
import com.newpos.libpay.trans.Trans;

import static com.newpos.libpay.trans.Trans.DEPOSITO;
import static com.newpos.libpay.trans.Trans.GIROS_COBROS;
import static com.newpos.libpay.trans.Trans.GIROS_EMISION;
import static com.newpos.libpay.trans.Trans.PAGOSERVICIOS;
import static com.newpos.libpay.trans.Trans.PAGO_CONRANGO;
import static com.newpos.libpay.trans.Trans.PAGO_IMPORTE;

public class ClickSpinner {

    private EditText txtData;
    private String[] spinners;
    private Activity activity;
    private LinearLayout linBotones;
    private ImageButton tipoSelectid;
    private RelativeLayout spinnerRelat;
    private static final String COLOR_BLUE = "#0a47f0";
    private Typeface typeface2;
    private Typeface typeface3;
    private Button[] button;
    private Button btnContinuar;
    private TextView titleDoc;
    private LinearLayout layoutDocument;
    private LinearLayout layoutNombre;
    LinearLayout  linScrollView;
    LinearLayout linFormPago;
    private int positionSelected;
    private WaitTypeDocument callbackTypeDocument;
    private WaitPaymentServices callbackPaymentService;
    private TextView tvtSelect;

    //RELATIVE TECLADO
    RelativeLayout relativeKeyboardNum;
    RelativeLayout relativeKeyboardAlfa;
    private  static final String SOLCITANTE = "Solicitante";
    private  static final String BENEFICIARIO = "Beneficiario";
    ClickKeyboard clickKeyboard;
    ClickKeyboardFragment keyboards;
    private ClassArguments classArguments;
    ScrollView scrollView;

    public ClickSpinner(EditText txtCapture , TextView tvSelect, ImageButton tipoSelect,LinearLayout linearBotones , RelativeLayout spinnerrelative, String[] spinner , Activity activity2) {
        this.txtData = txtCapture;
        this.tipoSelectid = tipoSelect;
        this.spinners = spinner;
        this.activity = activity2;
        this.spinnerRelat = spinnerrelative;
        this.linBotones = linearBotones;
        this.tvtSelect = tvSelect;

        typeface3 = ResourcesCompat.getFont(activity2.getApplicationContext(), R.font.flexo_medium);
        typeface2 = ResourcesCompat.getFont(activity2.getApplicationContext(), R.font.flexo_bold);
        tipoSelectid.setOnClickListener(onClickListener);
        if (tvtSelect != null)
            tvtSelect.setOnClickListener(onClickListener);
    }

    private final View.OnClickListener onClickListener = (View v) -> {
            if (v.equals(tipoSelectid) || v.equals(tvtSelect)){
                spinnerRelat.setVisibility(View.VISIBLE);
                activity.findViewById(R.id.keyboardNumerico).setVisibility(View.GONE);
                activity.findViewById(R.id.keyboardAlfa).setVisibility(View.GONE);
                button[0].setTypeface(typeface3);
                tipoSelectid.setBackgroundResource(R.drawable.ic_icn_dropdown_close);
                txtData.setBackgroundResource(R.drawable.btn_spinner_first);
                classArguments = ((TransView)activity).getArguments();
                if((classArguments != null && classArguments.isEditar()) || txtData == activity.findViewById(R.id.textSpinnerdoc)){
                    layoutNombre = activity.findViewById(R.id.linearOrdenPago);
                    layoutNombre.setVisibility(View.GONE);
                }
                if ((classArguments != null && !classArguments.isEditar()) && txtData == activity.findViewById(R.id.textSpinnerdoc)){
                    EditText editText = activity.findViewById(R.id.TxtSpinnerBen);
                    editText.setText("");
                }
            }
    };

    @SuppressLint("SetTextI18n")
    public void spinnerBotones(final String transEname, String typeclient) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        button = new Button[spinners.length];
        button[0] = new Button(activity);
        button[0].setBackgroundResource(R.drawable.btn_spinner_first);
        button[0].setLayoutParams(lp);
        button[0].setText(activity.getResources().getString(R.string.spinner));
        button[0].setTextColor(Color.parseColor(COLOR_BLUE));
        button[0].setTypeface(typeface3);
        button[0].setTextSize(17);
        button[0].setOnClickListener(onClickListener);
        button[0].setAllCaps(false);
        button[0].setGravity(0);
        button[0].setCompoundDrawablePadding(5);
        linBotones.addView(button[0]);

        if(spinners.length > 1){
            for(int i = 0 ; i < spinners.length; i++){
                button[i] = new Button(activity);
                button[i].setLayoutParams(lp);
                button[i].setText(spinners[i]);
                button[i].setBackgroundResource(R.drawable.btn_spinner_estados);
                button[i].setTypeface(typeface3);
                button[i].setTextColor(activity.getResources().getColorStateList(R.color.btn_spinner_color));
                button[i].setGravity(Gravity.CENTER_VERTICAL);
                button[i].setAllCaps(false);
                button[i].setTextSize(15);
                final int cont = i;
                int finalI = i;
                button[i].setOnClickListener(v -> {
                    spinnerRelat.setVisibility(View.GONE);
                    button[cont].setTypeface(typeface3);
                    txtData.setTypeface(typeface2);
                    txtData.setText(button[cont].getText());
                    txtData.setBackgroundResource(R.drawable.btn_spinner_estados);
                    tipoSelectid.setBackgroundResource(R.drawable.ic_flecha_abajo);
                    spinerDocument(transEname,typeclient);
                    if(txtData == activity.findViewById(R.id.TxtSpinnerBen)){
                        btnContinuar = activity.findViewById(R.id.Btncontinuar);
                        btnContinuar.setEnabled(true);
                    }
                    if(transEname.equals(PAGO_CONRANGO)){
                        linFormPago = activity.findViewById(R.id.linFormaPago);
                        btnContinuar = activity.findViewById(R.id.Btncontinuar);
                        linFormPago.setVisibility(View.VISIBLE);
                        btnContinuar.setEnabled(true);

                    }
                     positionSelected = finalI;
                });
                linBotones.addView(button[i]);
            }
        }else {
            txtData.setText(spinners[0]);
            tipoSelectid.setVisibility(View.GONE);
            txtData.setBackgroundResource(R.drawable.btn_spinner_estados);
            txtData.setTypeface(typeface2);
            txtData.setClickable(false);
            tipoSelectid.setClickable(false);
            tvtSelect.setClickable(false);
            spinerDocument(transEname,typeclient);
            if(txtData == activity.findViewById(R.id.TxtSpinnerBen)){
                btnContinuar = activity.findViewById(R.id.Btncontinuar);
                btnContinuar.setEnabled(true);
            }
            if(transEname.equals(PAGO_CONRANGO)){
                linFormPago = activity.findViewById(R.id.linFormaPago);
                btnContinuar = activity.findViewById(R.id.Btncontinuar);
                linFormPago.setVisibility(View.VISIBLE);
                btnContinuar.setEnabled(true);

            }
        }
    }

    private void spinerDocument(String transEname,String typeclient){
        int size = 0;
        switch (transEname) {
            case DEPOSITO:
                switch (txtData.getText().toString()) {
                    case "DNI":
                        titleDoc.setText(activity.getResources().getString(R.string.numDniDepo));
                        clickKeyboard.clearInpuDoc(8, 8);
                        clickKeyboard.activeKeyboardNumeric();
                        break;
                    case "Carnet de extranjería":
                        titleDoc.setText(activity.getResources().getString(R.string.numCarnetDepo));
                        clickKeyboard.clearInpuDoc(12, 5);
                        clickKeyboard.activeKeyboardAlfa();
                        break;
                    case "Pasaporte":
                        titleDoc.setText(activity.getResources().getString(R.string.numPasaportDepo));
                        clickKeyboard.clearInpuDoc(12, 8);
                        clickKeyboard.activeKeyboardAlfa();
                        break;
                    default:
                        break;
                }
                layoutDocument.setVisibility(View.VISIBLE);
                break;
            case GIROS_COBROS:
            case GIROS_EMISION:
                switch (txtData.getText().toString()) {
                    case "DNI":
                        if(transEname.equals(GIROS_COBROS) || typeclient.equals(BENEFICIARIO)){
                            titleDoc.setText(activity.getResources().getString(R.string.numDniBenef));
                        }else if(typeclient.equals(SOLCITANTE)){
                            titleDoc.setText(activity.getResources().getString(R.string.numDNISolici));
                        }else {
                            titleDoc.setText(activity.getResources().getString(R.string.numDNIOrdenate));
                        }
                        if(classArguments.getTypepayment().equals("2") && transEname.equals(GIROS_COBROS)){
                            keyboards.clearInpuDoc(8, 8);
                            keyboards.activeKeyboardNumeric();
                        }else {
                            clickKeyboard.clearInpuDoc(8, 8);
                            clickKeyboard.activeKeyboardNumeric();
                        }
                        size = 380;
                        break;
                    case "Carnet de extranjería":
                        if(transEname.equals(GIROS_COBROS) || typeclient.equals(BENEFICIARIO)){
                            titleDoc.setText(activity.getResources().getString(R.string.numCarnetBenef));
                        }else if(typeclient.equals(SOLCITANTE)){
                            titleDoc.setText(activity.getResources().getString(R.string.numEXTSolici));
                        }else {
                            titleDoc.setText(activity.getResources().getString(R.string.numEXTOrdenate));
                        }
                        if(classArguments.getTypepayment().equals("2") && transEname.equals(GIROS_COBROS)){
                            keyboards.clearInpuDoc(24, 5);
                            keyboards.activeKeyboardAlfa();
                        }else {
                            clickKeyboard.clearInpuDoc(24, 5);
                            clickKeyboard.activeKeyboardAlfa();
                        }
                        size = 515;
                        break;
                    case "Pasaporte":
                        if(transEname.equals(GIROS_COBROS) || typeclient.equals(BENEFICIARIO)){
                            titleDoc.setText(activity.getResources().getString(R.string.numPasaportBenef));
                        }else if(typeclient.equals(SOLCITANTE)){
                            titleDoc.setText(activity.getResources().getString(R.string.numPASSolici));
                        }else {
                            titleDoc.setText(activity.getResources().getString(R.string.numPASOrdenate));
                        }
                        if(classArguments.getTypepayment().equals("2") && transEname.equals(GIROS_COBROS)){
                            keyboards.clearInpuDoc(24, 5);
                            keyboards.activeKeyboardAlfa();
                        }else {
                            clickKeyboard.clearInpuDoc(24, 5);
                            clickKeyboard.activeKeyboardAlfa();
                        }
                        size = 515;
                        break;
                    case "RUC":
                        if(transEname.equals(GIROS_COBROS) || typeclient.equals(BENEFICIARIO)){
                            titleDoc.setText(activity.getResources().getString(R.string.numRucBenef));
                        }else if(typeclient.equals(SOLCITANTE)){
                            titleDoc.setText(activity.getResources().getString(R.string.numRUC));
                        }else {
                            titleDoc.setText(activity.getResources().getString(R.string.numRUCOrdenate));
                        }
                        callbackTypeDocument.getTypeDocument();

                        if(classArguments.getTypepayment().equals("2") && transEname.equals(GIROS_COBROS)){
                            keyboards.clearInpuDoc(11, 11);
                            keyboards.activeKeyboardNumeric();
                        }else {
                            clickKeyboard.clearInpuDoc(11, 11);
                            clickKeyboard.activeKeyboardNumeric();
                        }
                        size = 380;
                    break;
                    default:
                        break;
                }
                if (callbackTypeDocument != null)
                    callbackTypeDocument.cleanData(typeclient);

               break;
            case PAGO_CONRANGO:
            case PAGOSERVICIOS:
            case PAGO_IMPORTE:
                callbackPaymentService.getTypeService(typeclient);
                if(typeclient.equals("tipo servicio") || typeclient.equals("tipo documento")){
                    clickKeyboard.clearInpuDoc(11, 10);
                    clickKeyboard.activeKeyboardNumeric();
                    if(txtData == activity.findViewById(R.id.SpinnerDocumentList)){
                        linScrollView = activity.findViewById(R.id.linearScrollPs);
                        linScrollView.getLayoutParams().height = 350;
                    }
                }
                break;
            default:
                break;
        }
        if (layoutDocument != null)
            layoutDocument.setVisibility(View.VISIBLE);

        if(txtData == activity.findViewById(R.id.TxtTipoDocOrd)){
            linScrollView = activity.findViewById(R.id.linearScroll);
            linScrollView.getLayoutParams().height = size;

            if (activity.findViewById(R.id.scrollViewDS) != null) {
                scrollView = activity.findViewById(R.id.scrollViewDS);

                scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
            }
        }
    }

    public void setDocument(TextView title, LinearLayout tipDoc , RelativeLayout keyboardNumer , RelativeLayout keyboardAlfanumer){
        this.titleDoc = title;
        this.layoutDocument = tipDoc;
        this.relativeKeyboardNum = keyboardNumer;
        this.relativeKeyboardAlfa = keyboardAlfanumer;

        layoutDocument.setVisibility(View.GONE);
        relativeKeyboardNum.setVisibility(View.GONE);
        relativeKeyboardAlfa.setVisibility(View.GONE);

    }

    public void setClickKeyboard(ClickKeyboard clickKeyboard){
        this.clickKeyboard = clickKeyboard;
    }

    public void setKeyboards(ClickKeyboardFragment keyboards) {
        this.keyboards = keyboards;
    }

    public int getPositionSelected() {
        return positionSelected;
    }

    public void setCallbackTypeDocument(WaitTypeDocument callbackTypeDocument) {
        this.callbackTypeDocument = callbackTypeDocument;
    }

    public void setCallbackPaymentService(WaitPaymentServices callbackPaymentService) {
        this.callbackPaymentService = callbackPaymentService;
    }
}
