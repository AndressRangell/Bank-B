package com.bcp.tools;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.newpos.pay.R;
import com.github.angads25.toggle.LabeledSwitch;
import com.newpos.libpay.Logger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import cn.desert.newpos.payui.UIUtils;

public class WifiSettings extends AppCompatActivity {
    private LabeledSwitch switchWifi;
    private WifiManager wifiManager;
    private LottieAnimationView webView;
    private ListView listaWifi;
    private TextView txtToolbarText;
    private TextView txtBuscandoRedes;
    private LinearLayout linearLayout;
    private int count;
    Context context = this;
    CountDownTimer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_wifi);

        switchWifi = findViewById(R.id.swt);
        webView = findViewById(R.id.webViewProgress);
        listaWifi = findViewById(R.id.listWifi);
        txtToolbarText = findViewById(R.id.txtToolbarText);
        linearLayout = findViewById(R.id.linearActualizar);
        txtBuscandoRedes = findViewById(R.id.txtBuscandoRedes);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        inicializarTimer();

        if ( !wifiManager.isWifiEnabled() ){
            wifiManager.setWifiEnabled(true);
        }
        txtToolbarText.setText(R.string.ofWifi);
        switchWifi.setOn(true);
        obtenerLista();

        switchWifi.setOnClickListener(v -> estadoWifi(switchWifi.isOn()));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(r);
        timer.cancel();
        finish();
    }

    private void inicializarTimer(){
        timer = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                //here you can have your logic to set text to edittext
            }
            public void onFinish() {
                wifiManager.reassociate();
                mostrarLista();
                timer.cancel();
                timer.start();
            }

        };
    }

    private void estadoWifi(boolean estado) {
        if (!estado){
            if ( !wifiManager.isWifiEnabled() ) {
                wifiManager.setWifiEnabled(true);
            }
            txtToolbarText.setText(R.string.ofWifi);
            obtenerLista();

        } else {
            if ( wifiManager.isWifiEnabled() ){
                wifiManager.setWifiEnabled(false);
                timer.cancel();
            }
            webView.setVisibility(View.GONE);
            listaWifi.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            txtToolbarText.setText(R.string.onWifi);
            txtBuscandoRedes.setVisibility(View.GONE);
            handler.removeCallbacks(r);
        }
    }

    Handler handler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            switchWifi.setEnabled(true);
            txtBuscandoRedes.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
            timer.start();
            mostrarLista();
        }
    };

    private void obtenerLista() {
        webView.playAnimation();
        webView.setVisibility(View.VISIBLE);
        txtBuscandoRedes.setVisibility(View.VISIBLE);
        txtBuscandoRedes.setText("BUSCANDO REDES WIFI...");

        handler.postDelayed(r,5000);
    }

    private void mostrarLista() {
        final String redConectada = wifiManager.getConnectionInfo().getSSID();

        final List<String> redFormateada = eliminarVacios(wifiManager.getScanResults());

        ArrayAdapter arrayAdapter = new ArrayAdapter(context,R.layout.setting_list_item);
        listaWifi.setAdapter(arrayAdapter);

        if (!redFormateada.isEmpty()) {
            if ( redFormateada.get(0).equals("NULL") ){
                redesError();
            } else {
                listaWifi.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);

                WifiAdapter wifiAdapter = new WifiAdapter(context,redFormateada, redConectada );

                listaWifi.setAdapter(wifiAdapter);

                listaWifi.setOnItemClickListener((parent, view, position, id) -> {
                    String redConectadaActual =  wifiManager.getConnectionInfo().getSSID();
                    String red = redFormateada.get(position);
                    if (redConectadaActual != null) {
                        if (red.equals(redConectadaActual.replace("\"", ""))) {
                            modificarRed(red);
                        } else {
                            ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                            NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                            if (wifi.isConnected()){
                                modificarRed(redConectadaActual.replace("\"", ""));
                                final String finalRed = red;

                                new Handler().postDelayed(() -> ingresarContrasena(finalRed),3000);
                            } else {
                                ingresarContrasena(red);
                            }
                        }
                    }
                });
            }
        } else {
            redesError();
        }
    }

    private void redesError() {
        UIUtils.toast((Activity) context, R.drawable.ic_lg_light, "No hay redes disponibles", Toast.LENGTH_SHORT, new int[]{Gravity.CENTER, 0, 0});
        switchWifi.setOn(false);
        estadoWifi(true);
        handler.removeCallbacks(r);
        timer.cancel();
    }

    private List<String> eliminarVacios(List<ScanResult> scanResults) {
        ArrayList<String> resultados = new ArrayList<>();

        if (scanResults != null) {
            for (int i = 0; i < scanResults.size(); i++) {
                if (scanResults.get(i).SSID.length() > 0) {
                    resultados.add(scanResults.get(i).SSID);
                }
            }
            if (resultados.isEmpty()) {
                eliminarRepetidos(resultados);
            }
        } else {
            resultados.add("NULL");
        }

        return resultados;
    }

    private ArrayList<String> eliminarRepetidos(ArrayList<String> resultados) {

        Set<String> hashSet = new HashSet<>(resultados);
        resultados.clear();
        resultados.addAll(hashSet);

        return resultados;
    }

    private void ingresarContrasena(final String titulo) {
        final int typeNet = typeNetwork(titulo);
        if (typeNet > 0) {
            final Dialog dialog = UIUtils.centerDialog(context, R.layout.setting_home_pass, R.id.setting_pass_layout);
            final EditText newEdit = dialog.findViewById(R.id.setting_pass_new);
            final TextView titlePass = dialog.findViewById(R.id.title_pass);
            Button confirm = dialog.findViewById(R.id.setting_pass_confirm);
            newEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            newEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
            newEdit.requestFocus();
            titlePass.setText(titulo);

            dialog.findViewById(R.id.setting_pass_close).setOnClickListener(view -> dialog.dismiss());

            confirm.setOnClickListener(view -> {
                validarRed( titulo, newEdit.getText().toString(), typeNet);
                dialog.dismiss();
            });
            dialog.show();
        } else {
            validarRed( titulo, "", typeNet);
        }
    }

    private int typeNetwork(String titulo) {
        List<ScanResult> lista = wifiManager.getScanResults();

        Set<ScanResult> hashSet = new HashSet<>(lista);
        lista.clear();
        lista.addAll(hashSet);

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).SSID.equals(titulo)) {
                String capabilities = lista.get(i).capabilities;
                if (!capabilities.contains("-")) {
                    return WifiConfiguration.KeyMgmt.NONE;
                } else {
                    String type = capabilities.substring(capabilities.indexOf('-') + 1, capabilities.indexOf('-') + 4);

                    if (type.contains("IEEE802.1X"))
                        return WifiConfiguration.KeyMgmt.IEEE8021X;
                    else if (type.contains("WPA"))
                        return WifiConfiguration.KeyMgmt.WPA_EAP;
                    else if (type.contains("PSK"))
                        return WifiConfiguration.KeyMgmt.WPA_PSK;

                }
            }
        }
        return -1;
    }

    public void onClickBack(View view) {
        Logger.debug("" + view);
        handler.removeCallbacks(r);
        timer.cancel();
        finish();
    }

    public void actualizar(View view) {
        Logger.debug("click" + view.getId());
        cambiarColores();
        wifiManager.reassociate();
        mostrarLista();
    }

    private void cambiarColores() {
        final ImageView imageView = findViewById(R.id.imgId);
        count = 0;

        CountDownTimer timerRefresh = new CountDownTimer(3000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                switch (count){
                    case 0:
                    case 3:
                        imageView.setColorFilter(context.getResources().getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
                        break;

                    case 1:
                    case 4:
                        imageView.setColorFilter(context.getResources().getColor(R.color.base_blue), PorterDuff.Mode.SRC_IN);
                        break;

                    case 2:
                        imageView.setColorFilter(context.getResources().getColor(R.color.dull_blue), PorterDuff.Mode.SRC_IN);
                        break;

                    default:
                        break;
                }
                count ++;
            }

            @Override
            public void onFinish() {
                count = 0;
                imageView.setColorFilter(context.getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
            }
        };
        timerRefresh.start();
    }

    private void validarRed(String red, String contrasena, int typeKey){
        boolean conexionExitosa;
        // setup a ic_wifi configuration
        WifiConfiguration wc = new WifiConfiguration();
        wc.SSID = "\"" + red + "\"";
        if (typeKey != WifiConfiguration.KeyMgmt.NONE) {
            wc.preSharedKey = "\""+ contrasena + "\"";
        }
        wc.status = WifiConfiguration.Status.ENABLED;
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wc.allowedKeyManagement.set(typeKey);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        // connect to and enable the connection
        int netId = wifiManager.addNetwork(wc);
        conexionExitosa = wifiManager.enableNetwork(netId, false);

        if(conexionExitosa) {
            conectar(red);
        } else {
            UIUtils.toast((Activity) context, R.drawable.ic_lg_light, "Longitud inválida", Toast.LENGTH_SHORT, new int[]{Gravity.CENTER, 0, 0});
        }
    }

    CountDownTimer timer2;
    private void conectar(final String ssid ) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        timer.cancel();
        progressDialog.setMessage("Conectando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        timer2 = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (wifi.isConnected() && wifiManager.getConnectionInfo().getSSID().replace("\"", "").equals(ssid)) {

                        UIUtils.toast((Activity) context, R.drawable.ic_lg_light, "Conexión establecida", Toast.LENGTH_SHORT, new int[]{Gravity.CENTER, 0, 0});
                        progressDialog.cancel();
                        wifiManager.reassociate();
                        mostrarLista();
                        timer.start();
                        timer2.cancel();

                }
            }

            @Override
            public void onFinish() {
                UIUtils.toast((Activity) context, R.drawable.ic_lg_light, "Contraseña incorrecta", Toast.LENGTH_SHORT, new int[]{Gravity.CENTER, 0, 0});
                progressDialog.cancel();
                wifiManager.reassociate();
                mostrarLista();
                timer.start();
                timer2.cancel();
            }
        };
        timer2.start();
    }

    private void modificarRed(String titulo) {
        boolean desconexionExitosa;

        final int typeKey = typeNetwork(titulo);

        // setup a ic_wifi configuration
        WifiConfiguration wc = new WifiConfiguration();
        wc.SSID = "\"" + titulo + "\"";
        wc.status = WifiConfiguration.Status.ENABLED;
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wc.allowedKeyManagement.set(typeKey);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        // connect to and enable the connection
        int netId = wifiManager.addNetwork(wc);
        desconexionExitosa = wifiManager.disableNetwork(netId);        // desconectar

        if (desconexionExitosa){
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Desconectando de " + titulo + "...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            new Handler().postDelayed(progressDialog::cancel, 2000);
        } else {
            UIUtils.toast((Activity) context, R.drawable.ic_lg_light, "No fué posible desconectar", Toast.LENGTH_SHORT, new int[]{Gravity.CENTER, 0, 0});
        }

        wifiManager.reassociate();
        mostrarLista();
        timer.start();

    }
}
