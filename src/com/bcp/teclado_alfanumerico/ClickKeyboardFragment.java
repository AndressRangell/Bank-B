package com.bcp.teclado_alfanumerico;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.newpos.pay.R;

import java.text.DecimalFormat;

public class ClickKeyboardFragment {
    private StringBuilder builderKeyboard = new StringBuilder();
    private EditText txtData;
    private EditText monto;
    private TextView signo;
    private View viewKeyboard;
    private boolean keyboardAlfa;
    private Boolean activityMonto = false;
    private Boolean activityPwd = false;
    private int lengthMax = 0;
    private int lengthMin = 0;
    private ImageView imageView;

    private RelativeLayout relativeKeyboardNum;
    private RelativeLayout realtiveKeyboardAlfa;

    private CountDownTimer countDownTimer;

    private static final String COLOR_BLUE = "#0a47f0";


    public ClickKeyboardFragment(EditText txtCapture, View activity, boolean isAlfa, CountDownTimer countDownTimer, RelativeLayout keyboardNum , RelativeLayout keyboardAlfaNum) {
        this.txtData = txtCapture;
        this.viewKeyboard = activity;
        this.keyboardAlfa = isAlfa;
        this.countDownTimer = countDownTimer;
        this.relativeKeyboardNum = keyboardNum;
        this.realtiveKeyboardAlfa = keyboardAlfaNum;



        if (keyboardAlfa){
            activeKeyboardAlfa();
        }else {
            activeKeyboardNumeric();
        }
    }

    private void loadsViews() {
        if (keyboardAlfa) {
            keyboardAlfaNumerico();
            imageView = viewKeyboard.findViewById(R.id.numberDelete);
        } else {
            keyboardNumerico();
            imageView = viewKeyboard.findViewById(R.id.numbDelete);
            imageView.setEnabled(false);
        }
    }

    private void keyboardAlfaNumerico() {
        viewKeyboard.findViewById(R.id.letraQ).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraW).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraE).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraR).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraT).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraY).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraU).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraI).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraO).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraP).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraA).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraS).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraD).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraF).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraG).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraH).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraJ).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraK).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraL).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraZ).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraX).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraC).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraV).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraB).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraN).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraÑ).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraM).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.charAt).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraScrip).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraAmper).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraUnder).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.letraPunto).setOnClickListener(keyClickListener);

        viewKeyboard.findViewById(R.id.number1).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.number2).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.number3).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.number4).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.number5).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.number6).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.number7).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.number8).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.number9).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.number0).setOnClickListener(keyClickListener);

        viewKeyboard.findViewById(R.id.numberDelete).setOnClickListener(keyClickListener);

        viewKeyboard.findViewById(R.id.btnEspacio).setOnClickListener(keyClickListener);

        viewKeyboard.findViewById(R.id.continuarAlfa).setEnabled(false);
    }

    private void keyboardNumerico() {
        viewKeyboard.findViewById(R.id.numb1).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.numb2).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.numb3).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.numb4).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.numb5).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.numb6).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.numb7).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.numb8).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.numb9).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.numb0).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.numb00).setOnClickListener(keyClickListener);
        viewKeyboard.findViewById(R.id.numbDelete).setOnClickListener(keyClickListener);
    }

    private final View.OnClickListener keyClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int viewClick = v.getId();

            if (viewClick == R.id.numbDelete || viewClick == R.id.numberDelete) {
                if (!activityMonto && txtData.getText() != null && !txtData.getText().toString().equals("")) {
                    builderKeyboard = new StringBuilder();
                    builderKeyboard.append(txtData.getText());
                }
                int len = builderKeyboard.length();
                if (len != 0) {
                    builderKeyboard.deleteCharAt(builderKeyboard.length() - 1);
                    txtData.setText(Boolean.TRUE.equals(activityMonto) ? formatMiles(builderKeyboard.toString()) : builderKeyboard);
                }

                if (builderKeyboard.length() == 0) {
                    if (viewClick == R.id.numberDelete) {
                        viewKeyboard.findViewById(R.id.continuarAlfa).setEnabled(false);
                    } else {
                        viewKeyboard.findViewById(R.id.continuar).setEnabled(false);
                        if (Boolean.TRUE.equals(activityMonto)) {
                            monto.setTextColor(Color.parseColor(COLOR_BLUE));
                            signo.setTextColor(Color.parseColor(COLOR_BLUE));
                        }
                    }
                    imageView.setEnabled(false);
                    imageView.setColorFilter(Color.WHITE);
                }
            }
            numbers(viewClick);

            if (keyboardAlfa){
                letters(viewClick);
                symbol(viewClick);
            }
        }

        private void numbers(int viewNumber) {
            switch (viewNumber) {
                case R.id.numb00:
                    if (activityMonto)
                        fillData(viewKeyboard.getContext().getString(R.string._00));
                    break;
                case R.id.numb0:
                case R.id.number0:
                    fillData(viewKeyboard.getContext().getString(R.string._0));
                    break;
                case R.id.numb1:
                case R.id.number1:
                    fillData(viewKeyboard.getContext().getString(R.string._1));
                    break;
                case R.id.numb2:
                case R.id.number2:
                    fillData(viewKeyboard.getContext().getString(R.string._2));
                    break;
                case R.id.numb3:
                case R.id.number3:
                    fillData(viewKeyboard.getContext().getString(R.string._3));
                    break;
                case R.id.numb4:
                case R.id.number4:
                    fillData(viewKeyboard.getContext().getString(R.string._4));
                    break;
                case R.id.numb5:
                case R.id.number5:
                    fillData(viewKeyboard.getContext().getString(R.string._5));
                    break;
                case R.id.numb6:
                case R.id.number6:
                    fillData(viewKeyboard.getContext().getString(R.string._6));
                    break;
                case R.id.numb7:
                case R.id.number7:
                    fillData(viewKeyboard.getContext().getString(R.string._7));
                    break;
                case R.id.numb8:
                case R.id.number8:
                    fillData(viewKeyboard.getContext().getString(R.string._8));
                    break;
                case R.id.numb9:
                case R.id.number9:
                    fillData(viewKeyboard.getContext().getString(R.string._9));
                    break;
                default:
                    break;
            }
        }

        private void symbol(int viewNumber) {
            switch (viewNumber) {
                case R.id.charAt:
                    fillData(viewKeyboard.getContext().getString(R.string.at));
                    break;
                case R.id.letraScrip:
                    fillData(viewKeyboard.getContext().getString(R.string.script));
                    break;
                case R.id.letraAmper:
                    fillData(viewKeyboard.getContext().getString(R.string.ampersand));
                    break;
                case R.id.letraUnder:
                    fillData(viewKeyboard.getContext().getString(R.string.underscore));
                    break;
                case R.id.letraPunto:
                    fillData(viewKeyboard.getContext().getString(R.string._punto));
                    break;
                case R.id.btnEspacio:
                    fillData(" ");
                    break;
                default:
                    break;
            }
        }

        private void letters(int viewLett) {
            switch (viewLett) {
                case R.id.letraQ:
                    fillData(viewKeyboard.getContext().getString(R.string._q));
                    break;
                case R.id.letraW:
                    fillData(viewKeyboard.getContext().getString(R.string._w));
                    break;
                case R.id.letraE:
                    fillData(viewKeyboard.getContext().getString(R.string._e));
                    break;
                case R.id.letraR:
                    fillData(viewKeyboard.getContext().getString(R.string._r));
                    break;
                case R.id.letraT:
                    fillData(viewKeyboard.getContext().getString(R.string._t));
                    break;
                case R.id.letraY:
                    fillData(viewKeyboard.getContext().getString(R.string._y));
                    break;
                case R.id.letraU:
                    fillData(viewKeyboard.getContext().getString(R.string._u));
                    break;
                case R.id.letraI:
                    fillData(viewKeyboard.getContext().getString(R.string._i));
                    break;
                case R.id.letraO:
                    fillData(viewKeyboard.getContext().getString(R.string._o));
                    break;
                case R.id.letraP:
                    fillData(viewKeyboard.getContext().getString(R.string._p));
                    break;
                case R.id.letraA:
                    fillData(viewKeyboard.getContext().getString(R.string._a));
                    break;
                case R.id.letraS:
                    fillData(viewKeyboard.getContext().getString(R.string._s));
                    break;
                case R.id.letraD:
                    fillData(viewKeyboard.getContext().getString(R.string._d));
                    break;
                case R.id.letraF:
                    fillData(viewKeyboard.getContext().getString(R.string._f));
                    break;
                case R.id.letraG:
                    fillData(viewKeyboard.getContext().getString(R.string._g));
                    break;
                case R.id.letraH:
                    fillData(viewKeyboard.getContext().getString(R.string._h));
                    break;
                case R.id.letraJ:
                    fillData(viewKeyboard.getContext().getString(R.string._j));
                    break;
                case R.id.letraK:
                    fillData(viewKeyboard.getContext().getString(R.string._k));
                    break;
                case R.id.letraL:
                    fillData(viewKeyboard.getContext().getString(R.string._l));
                    break;
                case R.id.letraZ:
                    fillData(viewKeyboard.getContext().getString(R.string._z));
                    break;
                case R.id.letraX:
                    fillData(viewKeyboard.getContext().getString(R.string._x));
                    break;
                case R.id.letraC:
                    fillData(viewKeyboard.getContext().getString(R.string._c));
                    break;
                case R.id.letraV:
                    fillData(viewKeyboard.getContext().getString(R.string._v));
                    break;
                case R.id.letraB:
                    fillData(viewKeyboard.getContext().getString(R.string._b));
                    break;
                case R.id.letraN:
                    fillData(viewKeyboard.getContext().getString(R.string._n));
                    break;
                case R.id.letraÑ:
                    fillData(viewKeyboard.getContext().getString(R.string._ñ));
                    break;
                case R.id.letraM:
                    fillData(viewKeyboard.getContext().getString(R.string._m));
                    break;
                default:
                    break;
            }
        }

        private void fillData(String val) {
            restartTimeout();
            if (!activityMonto && txtData.getText() != null && !txtData.getText().toString().equals("")) {
                builderKeyboard = new StringBuilder();
                builderKeyboard.append(txtData.getText());
            }
            if (builderKeyboard.length() < lengthMax) {
                builderKeyboard.append(val);
            }
            if (!txtData.toString().equals("0.00")) {
                if (keyboardAlfa) {
                    viewKeyboard.findViewById(R.id.continuarAlfa).setEnabled(true);
                } else {
                    if (Boolean.TRUE.equals(activityMonto)) {
                        monto = viewKeyboard.findViewById(R.id.monto);
                        monto.setTextColor(Color.parseColor(COLOR_BLUE));
                        signo = viewKeyboard.findViewById(R.id.signSol);
                        signo.setTextColor(Color.parseColor(COLOR_BLUE));
                    }
                    viewKeyboard.findViewById(R.id.continuar).setEnabled(true);
                }
                imageView.setEnabled(true);
            }
            txtData.setText(Boolean.TRUE.equals(activityMonto) ? formatMiles(builderKeyboard.toString()) : builderKeyboard);
        }

        private String formatMiles(String valor) {
            String formatOut = "";
            if (valor.length() > 2) {
                formatOut = valor.substring(0, valor.length() - 2) + "." + valor.substring(valor.length() - 2);
            } else if (valor.length() == 2){
                formatOut = "0." + valor;
            } else {
                formatOut = "0.0" + valor;
            }
            DecimalFormat formatMinimoRetiro = new DecimalFormat("###,##0.00");
            return (formatMinimoRetiro.format(Double.parseDouble(formatOut)));
        }
    };

    public void activeKeyboardAlfa() {
        if (realtiveKeyboardAlfa != null)
            realtiveKeyboardAlfa.setVisibility(View.VISIBLE);

        if (relativeKeyboardNum != null)
            relativeKeyboardNum.setVisibility(View.GONE);

        keyboardAlfa = true;
        loadsViews();
    }

    public void activeKeyboardNumeric() {
        if (relativeKeyboardNum != null)
            relativeKeyboardNum.setVisibility(View.VISIBLE);

        if (realtiveKeyboardAlfa != null)
            realtiveKeyboardAlfa.setVisibility(View.GONE);

        keyboardAlfa = false;
        loadsViews();
    }

    public void clearInpuDoc(int hint, int minLength) {
        int msgHint = 0;
        switch (hint){
            case 8:
                msgHint = R.string.digit_8;
                break;
            case 12:
                msgHint = R.string.digit_12;
                break;
            case 11:
                msgHint = R.string.digit_11;
                break;
            case 13:
            case 14:
                msgHint = R.string._14_digitos;
                break;
            case 24:
                msgHint =R.string.digitMax12;
                hint = 12;
                break;
            default:
                msgHint = R.string.digit_default;
                break;
        }
        lengthMax = hint;
        lengthMin = minLength;
        if (!activityPwd && !activityMonto){
            txtData.setText("");
            txtData.setHint(msgHint);
            builderKeyboard = new StringBuilder();
        }
    }

    public void setActivityMonto(boolean isMonto) {
        this.activityMonto = isMonto;
        Button conti;
        conti = viewKeyboard.findViewById(R.id.continuar);
        if (isMonto) {
            conti.setText(viewKeyboard.getContext().getString(R.string.confirmAmount));
        }
    }

    public void setLengthMax(int len,int minLength ) {
        clearInpuDoc(len,minLength);
    }

    public int getLengthMin() {
        return lengthMin;
    }

    public void setActivityPwd(boolean activityPwd){
        this.activityPwd = activityPwd;
    }

    public void setConfiPolaris(EditText editText) {
        viewKeyboard.findViewById(R.id.continuar).setVisibility(View.GONE);
        imageView.setEnabled(!editText.getText().toString().isEmpty());
    }

    private void restartTimeout(){
        if(countDownTimer != null){
            countDownTimer.cancel();
            countDownTimer.start();
        }
    }
}
