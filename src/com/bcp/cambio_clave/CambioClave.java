package com.bcp.cambio_clave;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.newpos.pay.R;
import com.bcp.login.Login;
import com.bcp.settings.BCPConfig;
import cn.desert.newpos.payui.UIUtils;
import cn.desert.newpos.payui.master.ResultControl;

public class CambioClave extends AppCompatActivity {

    TextView toolBarTitle;

    TextView msg;
    TextView msg2;
    TextView msg3;
    TextView clickClose;

    TextView msgError1;
    TextView msgError2;
    TextView msgError3;
    TextView msgError4;

    EditText contra;
    EditText nuevaContra;
    EditText nuevaContra2;

    LinearLayout linearClaveActual;

    NestedScrollView scrollView;

    String typeChange = "";

    StringBuilder builder;
    private EditText editTextActual;
    private String passwordActual;

    Button continuar;
    ImageButton numbDelete;
    ImageView close;

    private static final String COLOR_NORMAL = "#002473";
    private static final String COLOR_TEXT = "#034AFF";
    private static final String COLOR_MSG = "#202E44";

    private CountDownTimer countDownTimer;
    boolean time = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambio_clave);

        toolBarTitle = findViewById(R.id.title_toolbar);
        close = findViewById(R.id.iv_close);
        clickClose = findViewById(R.id.clickclose);
        close.setVisibility(View.VISIBLE);
        clickClose.setVisibility(View.VISIBLE);
        clickClose.setOnClickListener(v -> {
            deleteTimerMenus();
            Intent intent = new Intent();
            intent.setClass(CambioClave.this, Login.class);
            startActivity(intent);
            finish();
        });
        findViewById(R.id.back).setVisibility(View.GONE);

        msgError1 = findViewById(R.id.msgError);
        msgError2 = findViewById(R.id.msgError2);
        msgError3 = findViewById(R.id.msgError3);
        msgError4 = findViewById(R.id.msgError4);

        msg = findViewById(R.id.msg);
        msg2 = findViewById(R.id.msg2);
        msg3 = findViewById(R.id.msg3);

        scrollView = findViewById(R.id.scrollClave);

        contra = findViewById(R.id.contra);
        contra.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        contra.setShowSoftInputOnFocus(false);
        contra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                int tam = s.length();
                switch (tam) {
                    case 0:
                        contra.setCursorVisible(true);
                        break;
                    case 1:
                        contra.setCursorVisible(false);
                        break;
                    default:
                        break;
                }
            }
        });

        nuevaContra = findViewById(R.id.nuevaContra);
        nuevaContra.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        nuevaContra.setShowSoftInputOnFocus(false);
        nuevaContra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                int tam = s.length();
                switch (tam) {
                    case 0:
                        nuevaContra.setCursorVisible(true);
                        break;
                    case 1:
                        nuevaContra.setCursorVisible(false);
                        break;
                    default:
                        break;
                }
            }
        });

        nuevaContra2 = findViewById(R.id.nuevaContra2);
        nuevaContra2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        nuevaContra2.setShowSoftInputOnFocus(false);
        nuevaContra2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                int tam = s.length();
                switch (tam) {
                    case 0:
                        nuevaContra2.setCursorVisible(true);
                        break;
                    case 1:
                        nuevaContra2.setCursorVisible(false);
                        break;
                    default:
                        break;
                }
            }
        });

        linearClaveActual = findViewById(R.id.linearClaveActual);

        builder = new StringBuilder();

        continuar = findViewById(R.id.continuar);
        continuar.setEnabled(false);
        numbDelete = findViewById(R.id.numbDelete);

        final Intent intent = getIntent();
        Bundle data = intent.getExtras();
        if (data != null){
            typeChange = data.getString("CAMBIO CLAVE");
        }

        if (typeChange != null && typeChange.equals("CAMBIO CLAVE SUPERVISOR")) {
            linearClaveActual.setVisibility(View.GONE);
            editTextActual = nuevaContra;
            editTextActual.setBackgroundResource(R.drawable.btn_spinner_first);
            editTextActual.setTextColor(Color.parseColor(COLOR_TEXT));
            msg2.setTextColor(Color.parseColor(COLOR_TEXT));
            nuevaContra.setEnabled(true);
            toolBarTitle.setText(getString(R.string.crearClave));
        }else {
            linearClaveActual.setVisibility(View.VISIBLE);
            editTextActual = contra;
            editTextActual.setBackgroundResource(R.drawable.btn_spinner_first);
            editTextActual.setTextColor(Color.parseColor(COLOR_TEXT));
            msg.setTextColor(Color.parseColor(COLOR_TEXT));
            contra.setEnabled(true);
            toolBarTitle.setText(getString(R.string.cambioClave));
        }

        findViewById(R.id.numb1).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.numb2).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.numb3).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.numb4).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.numb5).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.numb6).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.numb7).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.numb8).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.numb9).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.numb0).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.numbDelete).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.continuar).setOnClickListener(keyBoardClickListener);

        counterDownTimerMenus();
    }

    private final View.OnClickListener keyBoardClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.numb0:
                    fillData(getString(R.string._0));
                    break;
                case R.id.numb1:
                    fillData(getString(R.string._1));
                    break;
                case R.id.numb2:
                    fillData(getString(R.string._2));
                    break;
                case R.id.numb3:
                    fillData(getString(R.string._3));
                    break;
                case R.id.numb4:
                    fillData(getString(R.string._4));
                    break;
                case R.id.numb5:
                    fillData(getString(R.string._5));
                    break;
                case R.id.numb6:
                    fillData(getString(R.string._6));
                    break;
                case R.id.numb7:
                    fillData(getString(R.string._7));
                    break;
                case R.id.numb8:
                    fillData(getString(R.string._8));
                    break;
                case R.id.numb9:
                    fillData(getString(R.string._9));
                    break;
                case R.id.numbDelete:
                    int len = builder.length();
                    continuar.setEnabled(false);
                    if (len != 0) {
                        builder.deleteCharAt(builder.length() - 1);
                        editTextActual.setText(builder);
                        continuar.setText(getString(R.string.continuar));
                    } else {
                        if (linearClaveActual.getVisibility() == View.VISIBLE) {
                            if (editTextActual.equals(nuevaContra2)) {
                                scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_UP));
                                nuevaContra2.setEnabled(false);
                                editTextActual.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(COLOR_NORMAL)));
                                editTextActual.setTextColor(Color.parseColor(COLOR_NORMAL));
                                msg3.setTextColor(Color.parseColor(COLOR_MSG));
                                nuevaContra.setEnabled(true);
                                borrarDatosEditText(nuevaContra, false);
                                editTextActual.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(COLOR_TEXT)));
                                editTextActual.setTextColor(Color.parseColor(COLOR_TEXT));
                                msg2.setTextColor(Color.parseColor(COLOR_TEXT));
                            } else if (editTextActual.equals(nuevaContra)) {
                                nuevaContra.setEnabled(false);
                                editTextActual.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(COLOR_NORMAL)));
                                editTextActual.setTextColor(Color.parseColor(COLOR_NORMAL));
                                msg2.setTextColor(Color.parseColor(COLOR_MSG));
                                contra.setEnabled(true);
                                borrarDatosEditText(contra, false);
                                editTextActual.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(COLOR_TEXT)));
                                editTextActual.setTextColor(Color.parseColor(COLOR_TEXT));
                                msg.setTextColor(Color.parseColor(COLOR_TEXT));
                                continuar.setEnabled(false);
                            }
                        } else {
                            if (editTextActual.equals(nuevaContra2)) {
                                nuevaContra2.setEnabled(false);
                                editTextActual.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(COLOR_NORMAL)));
                                editTextActual.setTextColor(Color.parseColor(COLOR_NORMAL));
                                msg3.setTextColor(Color.parseColor(COLOR_MSG));
                                nuevaContra.setEnabled(true);
                                borrarDatosEditText(nuevaContra, false);
                                editTextActual.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(COLOR_TEXT)));
                                editTextActual.setTextColor(Color.parseColor(COLOR_TEXT));
                                msg2.setTextColor(Color.parseColor(COLOR_TEXT));
                                continuar.setEnabled(false);
                            }
                        }
                    }
                    break;
                case R.id.continuar:
                    if (continuar.getText().equals("Aceptar")) {
                        if (linearClaveActual.getVisibility() == View.VISIBLE) {
                            //Aqui es cambio de contraseña actual
                            if (BCPConfig.getInstance(CambioClave.this).getLoginPassword(BCPConfig.LOGINPASSWORDKEY).equals(contra.getText().toString())) {
                                if (nuevaContra.getText().toString().equals(nuevaContra2.getText().toString())) {
                                    BCPConfig.getInstance(CambioClave.this).setLoginPassword(BCPConfig.LOGINPASSWORDKEY,nuevaContra.getText().toString());
                                    UtilChangePwd.saveDateChange(CambioClave.this);
                                    //Toast.makeText(CambioClave.this, getString(R.string.camContrExitosa), Toast.LENGTH_SHORT).show();
                                    clear();
                                    deleteTimerMenus();
                                    UIUtils.startResult(CambioClave.this, ResultControl.class, true, true, new String[] {"Cambio de clave","2","exitoso"}, false);
                                } else {
                                    Toast.makeText(CambioClave.this, getString(R.string.contraNoCoincide), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(CambioClave.this, getString(R.string.msgContraIncorrecta), Toast.LENGTH_SHORT).show();
                            }
                        } else if (linearClaveActual.getVisibility() == View.GONE) {
                            //Aqui es creacion de nueva contraseña, vista supervisor
                            if (nuevaContra.getText().toString().equals(nuevaContra2.getText().toString())) {
                                BCPConfig.getInstance(CambioClave.this).setLoginPassword(BCPConfig.LOGINPASSWORDKEY,nuevaContra.getText().toString());
                                UtilChangePwd.saveDateChange(CambioClave.this);
                                //Toast.makeText(CambioClave.this, getString(R.string.contraCreada), Toast.LENGTH_SHORT).show();
                                clear();
                                deleteTimerMenus();
                                UIUtils.startResult(CambioClave.this, ResultControl.class, true, true, new String[] {"Clave\nconfigurada","2",""}, false);
                            } else {
                                Toast.makeText(CambioClave.this, getString(R.string.msgNoconinciden), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if (linearClaveActual.getVisibility() == View.VISIBLE) {
                            if (contra.getText().toString().length() == 6) {
                                builder.setLength(0);
                                contra.setEnabled(false);
                                contra.setBackgroundResource(R.drawable.btn_spinner_select);
                                contra.setTextColor(Color.parseColor(COLOR_NORMAL));
                                msg.setTextColor(Color.parseColor(COLOR_MSG));
                                nuevaContra.setEnabled(true);
                                nuevaContra.requestFocus();
                                editTextActual = nuevaContra;
                                editTextActual.setBackgroundResource(R.drawable.btn_spinner_first);
                                editTextActual.setTextColor(Color.parseColor(COLOR_TEXT));
                                msg2.setTextColor(Color.parseColor(COLOR_TEXT));
                                continuar.setEnabled(false);
                                if (nuevaContra.getText().toString().length() == 6) {
                                    scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
                                    builder.setLength(0);
                                    nuevaContra.setEnabled(false);
                                    nuevaContra.setBackgroundResource(R.drawable.btn_spinner_select);
                                    nuevaContra.setTextColor(Color.parseColor(COLOR_NORMAL));
                                    msg2.setTextColor(Color.parseColor(COLOR_MSG));
                                    nuevaContra2.setEnabled(true);
                                    nuevaContra2.requestFocus();
                                    editTextActual = nuevaContra2;
                                    editTextActual.setBackgroundResource(R.drawable.btn_spinner_first);
                                    editTextActual.setTextColor(Color.parseColor(COLOR_TEXT));
                                    msg3.setTextColor(Color.parseColor(COLOR_TEXT));
                                    continuar.setEnabled(false);
                                }
                            }
                        } else {
                            nuevaContra.requestFocus();
                            nuevaContra.setBackgroundResource(R.drawable.btn_spinner_select);
                            nuevaContra.setTextColor(Color.parseColor(COLOR_NORMAL));
                            msg2.setTextColor(Color.parseColor(COLOR_MSG));
                            if (nuevaContra.getText().toString().length() == 6) {
                                builder.setLength(0);
                                nuevaContra.setEnabled(false);
                                nuevaContra2.setEnabled(true);
                                nuevaContra2.requestFocus();
                                editTextActual = nuevaContra2;
                                editTextActual.setBackgroundResource(R.drawable.btn_spinner_first);
                                editTextActual.setTextColor(Color.parseColor(COLOR_TEXT));
                                msg3.setTextColor(Color.parseColor(COLOR_TEXT));
                                continuar.setEnabled(false);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        private void clear() {
            continuar.setText(getString(R.string.continuar));
            continuar.setEnabled(false);
            builder.setLength(0);
            contra.setText("");
            nuevaContra.setText("");
            nuevaContra2.setText("");
            if (linearClaveActual.getVisibility() == View.VISIBLE) {
                editTextActual = contra;
                msg3.setTextColor(Color.parseColor(COLOR_MSG));
                msg.setTextColor(Color.parseColor(COLOR_TEXT));
                contra.setTextColor(Color.parseColor(COLOR_TEXT));
                contra.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(COLOR_TEXT)));
                contra.setEnabled(true);
                nuevaContra.setEnabled(false);
                nuevaContra2.setEnabled(false);
            } else if (linearClaveActual.getVisibility() == View.GONE){
                editTextActual = nuevaContra;
                msg3.setTextColor(Color.parseColor(COLOR_MSG));
                msg2.setTextColor(Color.parseColor(COLOR_TEXT));
                nuevaContra.setTextColor(Color.parseColor(COLOR_TEXT));
                nuevaContra.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(COLOR_TEXT)));
                nuevaContra.setEnabled(true);
                nuevaContra2.setEnabled(false);
            }
        }

        private void fillData(String val) {
            restartTimeout();
            if (builder.length() < 6) {
                builder.append(val);
                numbDelete.setEnabled(true);
                editTextActual.setText(builder);
                if (builder.length() == 6) {
                    if (editTextActual.equals(contra)){
                        passwordActual = BCPConfig.getInstance(CambioClave.this).getLoginPassword(BCPConfig.LOGINPASSWORDKEY);
                        if (builder.toString().equals(passwordActual)){
                            continuar.setEnabled(true);
                        } else {
                            msgError1.setVisibility(View.VISIBLE);
                            contra.setTextColor(Color.RED);
                            contra.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                            msg.setTextColor(Color.RED);
                            new Handler().postDelayed(() -> {
                                borrarDatosEditText(contra, true);
                                contra.setTextColor(Color.parseColor(COLOR_TEXT));
                                contra.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(COLOR_TEXT)));
                                msg.setTextColor(Color.parseColor(COLOR_TEXT));
                                msgError1.setVisibility(View.GONE);
                            }, 2000);
                        }
                    } else {
                        continuar.setEnabled(true);
                    }
                }
                if (!contra.getText().toString().equals(nuevaContra.getText().toString())) {
                    if (nuevaContra2.getText().toString().length() == 6) {
                        if (nuevaContra.getText().toString().equals(nuevaContra2.getText().toString())) {
                            continuar.setText(getString(R.string.aceptar));
                        } else {
                            msgError3.setVisibility(View.VISIBLE);
                            msgError4.setVisibility(View.VISIBLE);
                            nuevaContra.setTextColor(Color.RED);
                            nuevaContra.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                            msg2.setTextColor(Color.RED);
                            nuevaContra2.setTextColor(Color.RED);
                            nuevaContra2.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                            msg3.setTextColor(Color.RED);
                            new Handler().postDelayed(() -> {
                                borrarDatosEditText(nuevaContra2, true);
                                nuevaContra.setTextColor(Color.parseColor(COLOR_NORMAL));
                                nuevaContra.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(COLOR_NORMAL)));
                                msg2.setTextColor(Color.parseColor(COLOR_MSG));
                                nuevaContra2.setTextColor(Color.parseColor(COLOR_TEXT));
                                nuevaContra2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(COLOR_TEXT)));
                                msg3.setTextColor(Color.parseColor(COLOR_TEXT));
                                msgError3.setVisibility(View.GONE);
                                msgError4.setVisibility(View.GONE);
                            }, 2000);
                        }
                    }
                } else {
                    msgError2.setVisibility(View.VISIBLE);
                    contra.setTextColor(Color.RED);
                    contra.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    msg.setTextColor(Color.RED);
                    nuevaContra.setTextColor(Color.RED);
                    nuevaContra.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    msg2.setTextColor(Color.RED);
                    new Handler().postDelayed(() -> {
                        borrarDatosEditText(nuevaContra, true);
                        contra.setTextColor(Color.parseColor(COLOR_NORMAL));
                        contra.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(COLOR_NORMAL)));
                        msg.setTextColor(Color.parseColor(COLOR_MSG));
                        nuevaContra.setTextColor(Color.parseColor(COLOR_TEXT));
                        nuevaContra.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(COLOR_TEXT)));
                        msg2.setTextColor(Color.parseColor(COLOR_TEXT));
                        msgError2.setVisibility(View.GONE);
                    }, 2000);
                }
            } else {
                Toast.makeText(CambioClave.this, getString(R.string.msg_len), Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void borrarDatosEditText(EditText editText, boolean all) {
        this.editTextActual = editText;
        this.editTextActual.requestFocus();
        builder.append(editText.getText().toString());
        if (!all) {
            builder.deleteCharAt(builder.length() - 1);
        } else {
            builder.setLength(0);
        }
        this.editTextActual.setText(builder);
    }

    @Override
    public void onBackPressed() {
        //nothing
    }

    private void restartTimeout(){
        if(countDownTimer != null){
            countDownTimer.cancel();
            countDownTimer.start();
        }
    }

    private void deleteTimerMenus() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void counterDownTimerMenus() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                Log.d("onTick", getResources().getString(R.string.msgInitTimer) + millisUntilFinished/1000);
            }
            public void onFinish() {
                Log.d("onTick", getResources().getString(R.string.msgfinishTimer));
                if(time){
                    counterDownTimerMenus();
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
            counterDownTimerMenus();
            relativeLayout.setVisibility(View.INVISIBLE);
        });
    }

    private void finishActivity(){
        UIUtils.intentTrans(Login.class,"","",false, CambioClave.this);
        deleteTimerMenus();
        finish();
    }
}
