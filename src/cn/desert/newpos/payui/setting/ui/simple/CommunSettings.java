package cn.desert.newpos.payui.setting.ui.simple;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bcp.definesbcp.Definesbcp;
import com.android.newpos.pay.R;
import com.bcp.teclado_alfanumerico.ClickKeyboard;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.utils.PAYUtils;

import java.util.Timer;

import cn.desert.newpos.payui.UIUtils;
import cn.desert.newpos.payui.base.BaseActivity;
import cn.desert.newpos.payui.setting.ui.SettingsFrags;

/**
 * Created by zhouqiang on 2017/11/15.
 */
public class CommunSettings extends BaseActivity implements View.OnClickListener {
    EditText communTimeout;

    EditText communPubPort;
    EditText merchantTid;
    RelativeLayout layoutTermIDComm;
    TextView tvLabelTermID;
    TextView tvLabelNii;
    EditText etNii;

    TextView txtTimeout;
    TextView txtPort;
    TextView txtTerminal;
    TextView txtNii;
    TextView tvIp1;
    TextView tvIp2;
    TextView tvIp3;
    TextView tvIp4;
    EditText etIp1;
    EditText etIp2;
    EditText etIp3;
    EditText etIp4;

    Button btnSave;
    Button btnContinuar;
    ImageButton btnFinalizar;
    Button cancelar;

    RelativeLayout relativeTeclado;
    RelativeLayout relativeTecladoAlfa;
    LinearLayout linearConfirmacion;

    private TMConfig config;
    private boolean isOpen;
    private String key;
    private Timer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_home_commun);

        txtTimeout = findViewById(R.id.txtTimeout);
        txtPort = findViewById(R.id.txtPuerto);
        txtTerminal = findViewById(R.id.txtTerminal);
        txtNii = findViewById(R.id.txtNii);

        tvIp1 = findViewById(R.id.tvIp1);
        tvIp2 = findViewById(R.id.tvIp2);
        tvIp3 = findViewById(R.id.tvIp3);
        tvIp4 = findViewById(R.id.tvIp4);

        etIp1 = findViewById(R.id.etIp1);
        etIp2 = findViewById(R.id.etIp2);
        etIp3 = findViewById(R.id.etIp3);
        etIp4 = findViewById(R.id.etIp4);

        btnSave = findViewById(R.id.saveSettings);
        cancelar = findViewById(R.id.cancelSettings);

        btnContinuar = findViewById(R.id.continuar);

        relativeTeclado = findViewById(R.id.keyboardNumerico);
        relativeTecladoAlfa = findViewById(R.id.keyboardAlfa);
        linearConfirmacion = findViewById(R.id.linearConfirmacion);
        btnFinalizar = findViewById(R.id.continuarAlfa);

        key = getIntent().getExtras().getString(SettingsFrags.JUMP_KEY);
        setNaviTitle(key);
        config = TMConfig.getInstance();
        initData();

        operatingEditText();

        txtTimeout.setOnClickListener(CommunSettings.this);
        txtPort.setOnClickListener(CommunSettings.this);
        txtTerminal.setOnClickListener(CommunSettings.this);
        txtNii.setOnClickListener(CommunSettings.this);

        tvIp1.setOnClickListener(CommunSettings.this);
        tvIp2.setOnClickListener(CommunSettings.this);
        tvIp3.setOnClickListener(CommunSettings.this);
        tvIp4.setOnClickListener(CommunSettings.this);
        btnContinuar.setText("Finalizar");
        btnContinuar.setVisibility(View.GONE);
        relativeTeclado.setVisibility(View.GONE);
        setRightVisiblity(View.GONE);

        communTimeout.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                communTimeout.setOnClickListener(null);
            }
            return false;
        });

        communPubPort.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                communPubPort.clearFocus();
                merchantTid.requestFocus();
            }
            return false;
        });

        merchantTid.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                merchantTid.clearFocus();
                etNii.requestFocus();
            }
            return false;
        });

        etNii.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                //
            }
            return false;
        });

        btnSave.setOnClickListener(v -> {
            if (!key.equals(Definesbcp.ITEM_TIPO_COMUNICACION)) {
                save();
            }
        });
        cancelar.setOnClickListener(v -> {
            if (!key.equals(Definesbcp.ITEM_TIPO_COMUNICACION)) {
                finish();
            }
        });


        communTimeout.setShowSoftInputOnFocus(false);
        communPubPort.setShowSoftInputOnFocus(false);
        merchantTid.setShowSoftInputOnFocus(false);
        etNii.setShowSoftInputOnFocus(false);
        etIp1.setShowSoftInputOnFocus(false);
        etIp2.setShowSoftInputOnFocus(false);
        etIp3.setShowSoftInputOnFocus(false);
        etIp4.setShowSoftInputOnFocus(false);

    }

    @Override
    protected void back() {
        if (relativeTeclado.getVisibility() == View.VISIBLE) {
            relativeTeclado.setVisibility(View.GONE);
            linearConfirmacion.setVisibility(View.VISIBLE);
        } else if (relativeTecladoAlfa.getVisibility() == View.VISIBLE) {
            relativeTecladoAlfa.setVisibility(View.GONE);
            linearConfirmacion.setVisibility(View.VISIBLE);
        } else {
            super.back();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    private void initData() {

        if (key.equals(Definesbcp.ITEM_TIPO_COMUNICACION)) {

            layoutTermIDComm = findViewById(R.id.layout_termID_comm);

            etNii = findViewById(R.id.setting_com_timeout);
            communPubPort = findViewById(R.id.setting_com_public_port);
            merchantTid = findViewById(R.id.merchant_tid);
            tvLabelTermID = findViewById(R.id.tv_ter_id);
            tvLabelNii = findViewById(R.id.tv_timeout);

            tvLabelNii.setText("NII");
            merchantTid.setVisibility(View.INVISIBLE);
            tvLabelTermID.setVisibility(View.INVISIBLE);
            layoutTermIDComm.setVisibility(View.INVISIBLE);

        } else {

            layoutTermIDComm = findViewById(R.id.layout_termID_comm);
            communTimeout = findViewById(R.id.setting_com_timeout);
            communPubPort = findViewById(R.id.setting_com_public_port);
            merchantTid = findViewById(R.id.merchant_tid);
            etNii = findViewById(R.id.setting_com_nii);

            communTimeout.setText(String.valueOf(config.getTimeout() / 1000));
            isOpen = true;
            setIPText(config.getIp().split("\\."));
            communPubPort.setText(config.getPort());
            communPubPort.setSelection(config.getPort().length());
            merchantTid.setText(config.getTermID());
            merchantTid.setSelection(config.getTermID().length());
            etNii.setText(config.getNii());
            etNii.setSelection(config.getNii().length());

        }

    }


    boolean borrado = false;
    int lenTxt = 0;
    private String mText2;
    private String mText3;
    private String mText4;
    private SharedPreferences mPreferences;

    private void operatingEditText() {

        mPreferences = getApplicationContext().getSharedPreferences("config_IP", Context.MODE_PRIVATE);

        et1Change();

        et2Change();

        et3Change();

        et4Change();
    }

    private void saveShared(String tittle, int lenContent) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(tittle, lenContent);
        editor.apply();
    }

    private void focusFocusable(CharSequence s, EditText edtIp) {
        if (s.length() > 2) {
            edtIp.setFocusable(true);
            edtIp.requestFocus();
        }
    }


    private void et1Change() {
        etIp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lenTxt = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && Integer.parseInt(String.valueOf(s)) > 255) {
                    etIp1.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 2 && Integer.parseInt(String.valueOf(s)) <= 255) {
                    etIp2.setFocusable(true);
                    etIp2.requestFocus();
                }
            }
        });
    }

    private void et2Change() {

        etIp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0 && (s.length() > 1)) {
                    if (s.toString().trim().contains(".")) {
                        mText2 = s.toString().substring(0, s.length() - 1);
                        etIp2.setText(mText2);
                    } else {
                        mText2 = s.toString().trim();
                    }
                    if (Integer.parseInt(mText2) > 255) {
                        return;
                    }

                    saveShared("IP_SECOND", mText2.length());

                    focusFocusable(s, etIp3);
                }

                if (start == 0 && s != null && s.length() == 0
                        && !PAYUtils.isNullWithTrim(etIp1.getText().toString())
                        && etIp1.length() > 1 && borrado) {
                    borrado = false;
                    etIp1.setFocusable(true);
                    etIp1.requestFocus();
                    etIp1.setSelection(etIp1.getText().length());
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lenTxt = s.length();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < lenTxt) {
                    borrado = true;
                }
                if (etIp2.getText().length() > 0 && (Integer.parseInt(etIp2.getText().toString()) > 255)) {
                    borrado = false;
                    etIp2.setText("");
                }
            }
        });
    }

    private void et3Change() {

        etIp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0 && (s.length() > 1)) {
                    if (s.toString().trim().contains(".")) {
                        mText3 = s.toString().substring(0, s.length() - 1);
                        etIp3.setText(mText3);
                    } else {
                        mText3 = s.toString().trim();
                    }

                    if (Integer.parseInt(mText3) > 255) {
                        return;
                    }

                    saveShared("IP_THIRD", mText3.length());

                    focusFocusable(s, etIp4);
                }

                if (start == 0 && s != null && s.length() == 0
                        && !PAYUtils.isNullWithTrim(etIp2.getText().toString())
                        && etIp2.length() > 1 && borrado) {
                    borrado = false;
                    etIp2.setFocusable(true);
                    etIp2.requestFocus();
                    etIp2.setSelection(etIp2.getText().length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lenTxt = s.length();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < lenTxt) {
                    borrado = true;
                }
                if (etIp3.getText().length() > 0 && (Integer.parseInt(etIp3.getText().toString()) > 255)) {
                    borrado = false;
                    etIp3.setText("");
                }
            }
        });
    }

    private void et4Change() {
        etIp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s != null && s.length() > 0) {
                    mText4 = s.toString().trim();

                    if (Integer.parseInt(mText4) > 255) {
                        return;
                    }
                    saveShared("IP_FOURTH", mText4.length());
                }

                if (start == 0 && s != null && s.length() == 0
                        && !PAYUtils.isNullWithTrim(etIp3.getText().toString())
                        && etIp3.length() > 1 && borrado) {
                    borrado = false;
                    etIp3.setFocusable(true);
                    etIp3.requestFocus();
                    etIp3.setSelection(etIp3.getText().length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lenTxt = s.length();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < lenTxt) {
                    borrado = true;
                }
                if (etIp4.getText().length() > 0 && Integer.parseInt(etIp4.getText().toString()) > 255) {
                    borrado = false;
                    etIp4.setText("");
                }
            }
        });
    }

    public void setIPText(String[] ip) {
        etIp1.setText(ip[0]);
        etIp2.setText(ip[1]);
        etIp3.setText(ip[2]);
        etIp4.setText(ip[3]);
        etIp4.requestFocus();
    }

    private void save() {
        String ip = concatIP();
        String port = communPubPort.getText().toString();
        String timeout = communTimeout.getText().toString();
        String tid = merchantTid.getText().toString();
        String nii = etNii.getText().toString();
        if (PAYUtils.isNullWithTrim(ip) ||
                PAYUtils.isNullWithTrim(tid) ||
                PAYUtils.isNullWithTrim(port) ||
                PAYUtils.isNullWithTrim(timeout) ||
                PAYUtils.isNullWithTrim(nii)) {
            Toast.makeText(this, getString(R.string.data_null), Toast.LENGTH_SHORT).show();
            return;
        }
        config.setIp(ip)
                .setPort(port)
                .setTimeout(Integer.parseInt(timeout) * 1000)
                .setPubCommun(isOpen)
                .setTermID(tid)
                .setNii(nii)
                .save();
        UIUtils.toast(this, R.drawable.ic_lg_light, getString(R.string.save_success), Toast.LENGTH_SHORT, new int[]{Gravity.CENTER, 0, 0});
        finish();
    }

    public String concatIP() {
        String ret = "";
        if (etIp1.getText().toString().equals("") || etIp2.getText().toString().equals("") ||
                etIp3.getText().toString().equals("") || etIp4.getText().toString().equals("")) {
            return "";
        } else {
            ret = etIp1.getText().toString() + "." + etIp2.getText().toString() + "." + etIp3.getText().toString() + "." + etIp4.getText().toString();
            return ret;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.txtTimeout:
                communTimeout.requestFocus();
                communTimeout.setSelection(communTimeout.getText().length());
                etTeclado(communTimeout, 2, false);
                btnContinuar.setOnClickListener(v -> {
                    relativeTeclado.setVisibility(View.GONE);
                    linearConfirmacion.setVisibility(View.VISIBLE);
                });
                if (relativeTecladoAlfa.getVisibility() == View.VISIBLE) {
                    relativeTecladoAlfa.setVisibility(View.GONE);
                }
                relativeTeclado.setVisibility(View.VISIBLE);
                linearConfirmacion.setVisibility(View.GONE);
                btnContinuar.setVisibility(View.VISIBLE);
                break;

            case R.id.txtPuerto:
                communPubPort.requestFocus();
                communPubPort.setSelection(communPubPort.getText().length());
                etTeclado(communPubPort, 6, false);
                btnContinuar.setOnClickListener(v -> {
                    relativeTeclado.setVisibility(View.GONE);
                    linearConfirmacion.setVisibility(View.VISIBLE);
                });
                if (relativeTecladoAlfa.getVisibility() == View.VISIBLE) {
                    relativeTecladoAlfa.setVisibility(View.GONE);
                }
                relativeTeclado.setVisibility(View.VISIBLE);
                linearConfirmacion.setVisibility(View.GONE);
                btnContinuar.setVisibility(View.VISIBLE);
                break;

            case R.id.txtTerminal:
                merchantTid.requestFocus();
                merchantTid.setSelection(merchantTid.getText().length());
                etTeclado(merchantTid, 8, true);
                btnFinalizar.setOnClickListener(v -> {
                    relativeTecladoAlfa.setVisibility(View.GONE);
                    linearConfirmacion.setVisibility(View.VISIBLE);
                });
                if (relativeTeclado.getVisibility() == View.VISIBLE) {
                    relativeTeclado.setVisibility(View.GONE);
                }
                relativeTecladoAlfa.setVisibility(View.VISIBLE);
                linearConfirmacion.setVisibility(View.GONE);
                btnFinalizar.setVisibility(View.VISIBLE);
                break;

            case R.id.txtNii:
                etNii.requestFocus();
                etNii.setSelection(etNii.getText().length());
                etTeclado(etNii, 3, false);
                btnContinuar.setOnClickListener(v -> {
                    relativeTeclado.setVisibility(View.GONE);
                    linearConfirmacion.setVisibility(View.VISIBLE);
                });
                if (relativeTecladoAlfa.getVisibility() == View.VISIBLE) {
                    relativeTecladoAlfa.setVisibility(View.GONE);
                }
                relativeTeclado.setVisibility(View.VISIBLE);
                linearConfirmacion.setVisibility(View.GONE);
                btnContinuar.setVisibility(View.VISIBLE);
                break;

            case R.id.tvIp1:
                etIp1.requestFocus();
                etIp1.setSelection(etIp1.getText().length());
                etTeclado(etIp1, 3, false);
                if (relativeTecladoAlfa.getVisibility() == View.VISIBLE) {
                    relativeTecladoAlfa.setVisibility(View.GONE);
                }
                relativeTeclado.setVisibility(View.VISIBLE);
                linearConfirmacion.setVisibility(View.GONE);
                break;

            case R.id.tvIp2:
                etIp2.requestFocus();
                etIp2.setSelection(etIp2.getText().length());
                etTeclado(etIp2, 3, false);
                if (relativeTecladoAlfa.getVisibility() == View.VISIBLE) {
                    relativeTecladoAlfa.setVisibility(View.GONE);
                }
                relativeTeclado.setVisibility(View.VISIBLE);
                linearConfirmacion.setVisibility(View.GONE);
                break;

            case R.id.tvIp3:
                etIp3.requestFocus();
                etIp3.setSelection(etIp3.getText().length());
                etTeclado(etIp3, 3, false);
                if (relativeTecladoAlfa.getVisibility() == View.VISIBLE) {
                    relativeTecladoAlfa.setVisibility(View.GONE);
                }
                relativeTeclado.setVisibility(View.VISIBLE);
                linearConfirmacion.setVisibility(View.GONE);
                break;

            case R.id.tvIp4:
                etIp4.requestFocus();
                etIp4.setSelection(etIp4.getText().length());
                etTeclado(etIp4, 3, false);
               btnContinuar.setOnClickListener(v -> {
                   relativeTeclado.setVisibility(View.GONE);
                   linearConfirmacion.setVisibility(View.VISIBLE);
               });
                if (relativeTecladoAlfa.getVisibility() == View.VISIBLE) {
                    relativeTecladoAlfa.setVisibility(View.GONE);
                }
                relativeTeclado.setVisibility(View.VISIBLE);
                linearConfirmacion.setVisibility(View.GONE);
                btnContinuar.setVisibility(View.VISIBLE);
                break;

            default:
                break;

        }

    }

    private void etTeclado(EditText editText, int maxCarac, boolean isAlfa) {
        ClickKeyboard keyboard = new ClickKeyboard(editText, CommunSettings.this, isAlfa,null,relativeTeclado,relativeTecladoAlfa);
        keyboard.setLengthMax(maxCarac,0);
        keyboard.setConfiPolaris(editText);
    }

}
