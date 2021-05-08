package com.bcp.teclado_alfanumerico;

import android.app.Activity;
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

public class ClickKeyboard {
    private StringBuilder builderKeyboard = new StringBuilder();
    private EditText txtData;
    private EditText monto;
    private TextView signo;
    private Activity activity2;
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

    //banner


    public ClickKeyboard(EditText txtCapture, Activity activity, boolean isAlfa,CountDownTimer countDownTimer,RelativeLayout keyboardNum , RelativeLayout keyboardAlfaNum) {
        this.txtData = txtCapture;
        this.activity2 = activity;
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
            imageView = activity2.findViewById(R.id.numberDelete);
        } else {
            keyboardNumerico();
            imageView = activity2.findViewById(R.id.numbDelete);
            imageView.setEnabled(false);
        }
    }

    private void keyboardAlfaNumerico() {
        activity2.findViewById(R.id.letraQ).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraW).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraE).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraR).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraT).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraY).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraU).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraI).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraO).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraP).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraA).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraS).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraD).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraF).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraG).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraH).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraJ).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraK).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraL).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraZ).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraX).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraC).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraV).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraB).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraN).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraÑ).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraM).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.charAt).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraScrip).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraAmper).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraUnder).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.letraPunto).setOnClickListener(keyClickListener);

        activity2.findViewById(R.id.number1).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.number2).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.number3).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.number4).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.number5).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.number6).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.number7).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.number8).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.number9).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.number0).setOnClickListener(keyClickListener);

        activity2.findViewById(R.id.numberDelete).setOnClickListener(keyClickListener);

        activity2.findViewById(R.id.btnEspacio).setOnClickListener(keyClickListener);

        activity2.findViewById(R.id.continuarAlfa).setEnabled(false);
    }

    private void keyboardNumerico() {
        activity2.findViewById(R.id.numb1).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.numb2).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.numb3).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.numb4).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.numb5).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.numb6).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.numb7).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.numb8).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.numb9).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.numb0).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.numb00).setOnClickListener(keyClickListener);
        activity2.findViewById(R.id.numbDelete).setOnClickListener(keyClickListener);
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
                        activity2.findViewById(R.id.continuarAlfa).setEnabled(false);
                    } else {
                        activity2.findViewById(R.id.continuar).setEnabled(false);
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
                        fillData(activity2.getString(R.string._00));
                    break;
                case R.id.numb0:
                case R.id.number0:
                    fillData(activity2.getString(R.string._0));
                    break;
                case R.id.numb1:
                case R.id.number1:
                    fillData(activity2.getString(R.string._1));
                    break;
                case R.id.numb2:
                case R.id.number2:
                    fillData(activity2.getString(R.string._2));
                    break;
                case R.id.numb3:
                case R.id.number3:
                    fillData(activity2.getString(R.string._3));
                    break;
                case R.id.numb4:
                case R.id.number4:
                    fillData(activity2.getString(R.string._4));
                    break;
                case R.id.numb5:
                case R.id.number5:
                    fillData(activity2.getString(R.string._5));
                    break;
                case R.id.numb6:
                case R.id.number6:
                    fillData(activity2.getString(R.string._6));
                    break;
                case R.id.numb7:
                case R.id.number7:
                    fillData(activity2.getString(R.string._7));
                    break;
                case R.id.numb8:
                case R.id.number8:
                    fillData(activity2.getString(R.string._8));
                    break;
                case R.id.numb9:
                case R.id.number9:
                    fillData(activity2.getString(R.string._9));
                    break;
                default:
                    break;
            }
        }

        private void symbol(int viewNumber) {
            switch (viewNumber) {
                case R.id.charAt:
                    fillData(activity2.getString(R.string.at));
                    break;
                case R.id.letraScrip:
                    fillData(activity2.getString(R.string.script));
                    break;
                case R.id.letraAmper:
                    fillData(activity2.getString(R.string.ampersand));
                    break;
                case R.id.letraUnder:
                    fillData(activity2.getString(R.string.underscore));
                    break;
                case R.id.letraPunto:
                    fillData(activity2.getString(R.string._punto));
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
                    fillData(activity2.getString(R.string._q));
                    break;
                case R.id.letraW:
                    fillData(activity2.getString(R.string._w));
                    break;
                case R.id.letraE:
                    fillData(activity2.getString(R.string._e));
                    break;
                case R.id.letraR:
                    fillData(activity2.getString(R.string._r));
                    break;
                case R.id.letraT:
                    fillData(activity2.getString(R.string._t));
                    break;
                case R.id.letraY:
                    fillData(activity2.getString(R.string._y));
                    break;
                case R.id.letraU:
                    fillData(activity2.getString(R.string._u));
                    break;
                case R.id.letraI:
                    fillData(activity2.getString(R.string._i));
                    break;
                case R.id.letraO:
                    fillData(activity2.getString(R.string._o));
                    break;
                case R.id.letraP:
                    fillData(activity2.getString(R.string._p));
                    break;
                case R.id.letraA:
                    fillData(activity2.getString(R.string._a));
                    break;
                case R.id.letraS:
                    fillData(activity2.getString(R.string._s));
                    break;
                case R.id.letraD:
                    fillData(activity2.getString(R.string._d));
                    break;
                case R.id.letraF:
                    fillData(activity2.getString(R.string._f));
                    break;
                case R.id.letraG:
                    fillData(activity2.getString(R.string._g));
                    break;
                case R.id.letraH:
                    fillData(activity2.getString(R.string._h));
                    break;
                case R.id.letraJ:
                    fillData(activity2.getString(R.string._j));
                    break;
                case R.id.letraK:
                    fillData(activity2.getString(R.string._k));
                    break;
                case R.id.letraL:
                    fillData(activity2.getString(R.string._l));
                    break;
                case R.id.letraZ:
                    fillData(activity2.getString(R.string._z));
                    break;
                case R.id.letraX:
                    fillData(activity2.getString(R.string._x));
                    break;
                case R.id.letraC:
                    fillData(activity2.getString(R.string._c));
                    break;
                case R.id.letraV:
                    fillData(activity2.getString(R.string._v));
                    break;
                case R.id.letraB:
                    fillData(activity2.getString(R.string._b));
                    break;
                case R.id.letraN:
                    fillData(activity2.getString(R.string._n));
                    break;
                case R.id.letraÑ:
                    fillData(activity2.getString(R.string._ñ));
                    break;
                case R.id.letraM:
                    fillData(activity2.getString(R.string._m));
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
                    activity2.findViewById(R.id.continuarAlfa).setEnabled(true);
                } else {
                    if (Boolean.TRUE.equals(activityMonto)) {
                        monto = activity2.findViewById(R.id.monto);
                        monto.setTextColor(Color.parseColor(COLOR_BLUE));
                        signo = activity2.findViewById(R.id.signSol);
                        signo.setTextColor(Color.parseColor(COLOR_BLUE));
                    }
                    activity2.findViewById(R.id.continuar).setEnabled(true);
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
                msgHint = R.string.digitMax12;
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
        conti = activity2.findViewById(R.id.continuar);
        if (isMonto) {
            conti.setText(activity2.getString(R.string.confirmAmount));
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
        activity2.findViewById(R.id.continuar).setVisibility(View.GONE);
        imageView.setEnabled(!editText.getText().toString().isEmpty());
    }

    private void restartTimeout(){
        if(countDownTimer != null){
            countDownTimer.cancel();
            countDownTimer.start();
        }
    }
}
