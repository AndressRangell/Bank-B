package com.bcp.menus;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.newpos.pay.ProcessingCertificate;
import com.android.newpos.pay.R;
import com.bcp.cambio_clave.CambioClave;
import com.bcp.definesbcp.Definesbcp;
import com.bcp.login.Login;
import com.bcp.reportes.DetailReport;
import com.bcp.tools.WifiSettings;
import com.newpos.libpay.Logger;
import com.newpos.libpay.device.printer.PrintRes;
import com.newpos.libpay.utils.PAYUtils;

import java.util.ArrayList;

import cn.desert.newpos.payui.UIUtils;
import cn.desert.newpos.payui.master.MasterControl;
import cn.desert.newpos.payui.setting.ui.simple.CommunSettings;

import static com.android.newpos.pay.ProcessingCertificate.polarisUtil;
import static com.android.newpos.pay.StartAppBCP.agente;
import static com.android.newpos.pay.StartAppBCP.variables;

public class MenuBCP extends AppCompatActivity {

    ArrayList<BotonMenuBCP> list;
    RecyclerView contenedor;
    ImageButton moreOptions;
    ImageButton close;
    RelativeLayout bgMenu;
    LinearLayout layout;
    Button cierre;
    Button reporteDetallado;
    Button cierretotal;
    Button ultimasoperaciones;
    Button cambioclaveactual;
    String[] items;
    TextView msg;
    TextView title;
    RelativeLayout clickMoreOptions;
    RelativeLayout clickClose;
    private CountDownTimer countDownTimerMenus;
    boolean time = true;

    AdaptadorMenuBCP adaptadorMenuBcp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_bcp);

        Logger.debug("Menu principal");

        variables.setMcontext(MenuBCP.this);

        list = new ArrayList<>();
        contenedor = findViewById(R.id.contenedor);
        contenedor.setLayoutManager(new GridLayoutManager(this, 2));
        moreOptions = findViewById(R.id.moreoptions);
        clickMoreOptions = findViewById(R.id.clickMoreOptions);
        close = findViewById(R.id.close);
        clickClose = findViewById(R.id.clickClose);
        layout = findViewById(R.id.layout);
        cierre = findViewById(R.id.cierre);
        bgMenu = findViewById(R.id.menuOculto);
        msg = findViewById(R.id.msg);

        reporteDetallado = findViewById(R.id.reporteDetallado);
        cierretotal = findViewById(R.id.cierretotal);
        ultimasoperaciones = findViewById(R.id.ultimasoperaciones);
        cambioclaveactual = findViewById(R.id.cambioclaveactual);

        title = findViewById(R.id.title);

        bgMenu.setOnClickListener(onClickListener);

        moreOptions.setOnClickListener(onClickListener);
        //elemento creado para recibir accion del boton de mas opciones
        clickMoreOptions.setOnClickListener(onClickListener);

        close.setOnClickListener(onClickListener);
        //elemento creado para recibir accion del boton de cerrar
        clickClose.setOnClickListener(onClickListener);

        cierre.setOnClickListener(onClickListener);
        reporteDetallado.setOnClickListener(onClickListener);
        cierretotal.setOnClickListener(onClickListener);
        ultimasoperaciones.setOnClickListener(onClickListener);
        cambioclaveactual.setOnClickListener(onClickListener);

        final Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            botones(extras.getInt(Definesbcp.DATO_MENU));
        }
        adaptadorMenuBcp = new AdaptadorMenuBCP(list, this);
        contenedor.setAdapter(adaptadorMenuBcp);
    }

    private void msgAccountSingle() {
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

        btnSalir.setOnClickListener(v -> {
            UIUtils.intentTrans(Login.class, "", "", false, MenuBCP.this);
            deleteTimerMenus();
            finish();
        });

        btnEntentido.setOnClickListener(v -> {
            time = true;
            counterDownTimerMenus();
            relativeLayout.setVisibility(View.INVISIBLE);
        });
    }

    private String setTittle() {
        if (PAYUtils.isEmpty(agente.getAgentName())) {
            if (agente.selectAgent(MenuBCP.this))
                return "Agente - \"" + agente.getAgentName() + "\"";
            else
                return "Agente ---";
        } else
            return "Agente - \"" + agente.getAgentName() + "\"";
    }

    private void botones(int contentMain) {
        items = getResources().getStringArray(contentMain);
        String tittleMsg = "";
        if (contentMain == (R.array.main1)) {
            tittleMsg = setTittle();
            drawMenu(false, 4);
        } else if (contentMain == (R.array.mainSupervisor)) {
            tittleMsg = "Configuración Inicial del Agente";
            moreOptions.setVisibility(View.GONE);
            clickMoreOptions.setVisibility(View.GONE);
            close.setVisibility(View.VISIBLE);
            clickClose.setVisibility(View.VISIBLE);
            drawMenu(true, 3);
        }
        msg.setText(tittleMsg);
    }

    /*
     * Permite dibujar cada n items de un color diferente
     * */
    private void drawMenu(boolean isSupervisor, int range) {
        int rangeTotal = range * 2;
        String[] itemEnable = getResources().getStringArray(R.array.mainvisual);
        int cntItems = polarisUtil.getListTrans().size();
        boolean active = false;
        boolean inGiro = false;
        int cont = 0;
        for (int i = 0; i < items.length; i++) {
            if (!isSupervisor) {
                for (int j = 0; j < cntItems; j++) {
                    if (polarisUtil.getListTrans().get(j).getIdTransaction().equals(itemEnable[i])) {
                        if (polarisUtil.getListTrans().get(j).getIdTransaction().contains("GIROS")) {
                            if (!inGiro) {
                                inGiro = true;
                                active = true;
                                break;
                            }
                        } else {
                            active = true;
                            break;
                        }
                    }
                }
                if (!active)
                    continue;
            }
            active = false;
            if (i > 0) {
                if (cont < range)
                    list.add(new BotonMenuBCP(items[i], getResources().getDrawable(R.drawable.boton_plano_menu_bcp2), getResources().getColorStateList(R.color.btn_plano_textcolor), getResources().getDrawable(R.drawable.linea_decorativa_plana)));
                else {
                    list.add(new BotonMenuBCP(items[i], getResources().getDrawable(R.drawable.boton_plano_menu_bcp), getResources().getColorStateList(R.color.btn_plano_textcolor2), getResources().getDrawable(R.drawable.linea_decorativa_plana2)));
                }
            } else {
                list.add(new BotonMenuBCP(items[i], getResources().getDrawable(R.drawable.boton_plano_menu_bcp2), getResources().getColorStateList(R.color.btn_plano_textcolor), getResources().getDrawable(R.drawable.linea_decorativa_plana)));
            }
            cont++;
            if (cont == rangeTotal)
                cont = 0;
        }
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.menuOculto:
                    bgMenu.setVisibility(View.INVISIBLE);
                    break;
                case R.id.moreoptions:
                case R.id.clickMoreOptions:
                    bgMenu.setVisibility(View.VISIBLE);
                    break;
                case R.id.cierre:
                case R.id.close:
                case R.id.clickClose:
                    deleteTimerMenus();
                    Logger.debug("Fin menu principal, se redirecciona a login");
                    UIUtils.intentTrans(Login.class, "", "", false, MenuBCP.this);
                    break;
                case R.id.reporteDetallado:
                    deleteTimerMenus();
                    Logger.debug("Fin menu principal, se ingresa a reporte de menu overflow");
                    UIUtils.intentTrans(DetailReport.class, "REPORTE", "REPORTE DETALLADO", false, MenuBCP.this);
                    break;
                case R.id.cierretotal:
                    deleteTimerMenus();
                    Logger.debug("Fin menu principal, se ingresa a cierre total de menu overflow");
                    UIUtils.intentTrans(MasterControl.class, MasterControl.TRANS_KEY, PrintRes.runnerTransEn(1), false, MenuBCP.this);
                    break;
                case R.id.cambioclaveactual:
                    deleteTimerMenus();
                    Logger.debug("Fin menu principal, se ingresa a cambio clave actual de menu overflow");
                    UIUtils.intentTrans(CambioClave.class, "CAMBIO CLAVE", "CAMBIO CLAVE ACTUAL", false, MenuBCP.this);
                    break;
                case R.id.ultimasoperaciones:
                    deleteTimerMenus();
                    Logger.debug("Fin menu principal, se ingresa a cambio clave actual de menu overflow");
                    UIUtils.intentTrans(MasterControl.class, MasterControl.TRANS_KEY, PrintRes.runnerTransEn(12), false, MenuBCP.this);
                    break;
                default:
                    break;
            }

        }
    };

    private void deleteTimerMenus() {
        if (countDownTimerMenus != null) {
            countDownTimerMenus.cancel();
            countDownTimerMenus = null;
        }
    }

    private void counterDownTimerMenus() {
        if (countDownTimerMenus != null) {
            countDownTimerMenus.cancel();
            countDownTimerMenus = null;
        }
        countDownTimerMenus = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                Log.d("onTick", getResources().getString(R.string.msgInitTimer) + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Log.d("onTick", getResources().getString(R.string.msgfinishTimer));
                if (time) {
                    counterDownTimerMenus();
                    msgAccountSingle();
                } else {
                    deleteTimerMenus();
                    finish();
                }
            }
        }.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
            case (MotionEvent.ACTION_MOVE):
            case (MotionEvent.ACTION_UP):
                counterDownTimerMenus();
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    public void onBackPressed() {
        //nothing
    }

    @Override
    protected void onResume() {
        super.onResume();
        counterDownTimerMenus();
        clickMain();
    }

    private void clickMain(){
        adaptadorMenuBcp.setOnClickListener(view -> {

            switch (list.get(contenedor.getChildAdapterPosition(view)).getTitulo()) {
                case Definesbcp.ITEM_BCP_RETIRO:
                    deleteTimerMenus();
                    if (polarisUtil.isInit()) {
                        Logger.debug("Fin menu principal, se ingresa a retiro");
                        UIUtils.intentTrans(MasterControl.class, MasterControl.TRANS_KEY, PrintRes.runnerTransEn(3), false, MenuBCP.this);
                    } else {
                        Toast.makeText(getApplicationContext(), "Operacion retiro no disponible", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Definesbcp.ITEM_BCP_DEPOSITO:
                    deleteTimerMenus();
                    Logger.debug("Fin menu principal, se ingresa a deposito");
                    if (polarisUtil.isInit()) {
                        UIUtils.intentTrans(MasterControl.class, MasterControl.TRANS_KEY, PrintRes.runnerTransEn(4), false, MenuBCP.this);
                    } else {
                        Toast.makeText(getApplicationContext(), "Operacion deposito no disponible", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Definesbcp.ITEM_BCP_GIROS:
                    deleteTimerMenus();
                    Logger.debug("Fin menu principal, se ingresa a giros");
                    if (polarisUtil.isInit()) {
                        UIUtils.intentTrans(MasterControl.class, MasterControl.TRANS_KEY, PrintRes.runnerTransEn(9), false, MenuBCP.this);
                    } else {
                        Toast.makeText(getApplicationContext(), "Operacion giro no disponible", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Definesbcp.ITEM_PAGO_SERVICIOS:
                    deleteTimerMenus();
                    Logger.debug("Fin menu principal, se ingresa a pago de servicios");
                    if (ProcessingCertificate.polarisUtil.isInit()) {
                        UIUtils.intentTrans(MasterControl.class, MasterControl.TRANS_KEY, PrintRes.runnerTransEn(11), false, MenuBCP.this);
                    } else {
                        Toast.makeText(getApplicationContext(), "Operacion de pago de servicios no disponible", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Definesbcp.ITEM_INICIALIZACION:
                    deleteTimerMenus();
                    if (polarisUtil.isCertificate()) {
                        UIUtils.intentTrans(MasterControl.class, MasterControl.TRANS_KEY, PrintRes.runnerTransEn(5), false, MenuBCP.this);
                    } else {
                        Toast.makeText(getApplicationContext(), "Realizar descarga de certificados", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Definesbcp.ITEM_CAMBIO_CLAVE:
                    deleteTimerMenus();
                    Logger.debug("Fin menu supervisor, se ingresa a cambio clave supervisor");
                    UIUtils.intentTrans(CambioClave.class, "CAMBIO CLAVE", "CAMBIO CLAVE SUPERVISOR", false, MenuBCP.this);
                    break;
                case Definesbcp.CERTIFICADO_TRANS:
                    deleteTimerMenus();
                    Logger.debug("Fin menu principal, se ingresa a obtener certificado");
                    UIUtils.intentTrans(MasterControl.class, MasterControl.TRANS_KEY, PrintRes.runnerTransEn(8), false, MenuBCP.this);
                    break;
                case Definesbcp.ITEM_CONFI_WIFI:
                    deleteTimerMenus();
                    Logger.debug("Fin menu supervisor, se ingresa a configuracion de wifi");
                    UIUtils.intentTrans(WifiSettings.class, "CONFI WIFI", "CONGIGURACION WIFI", false, MenuBCP.this);
                    break;
                case Definesbcp.ITEM_CONFIG_INICIAL:
                    deleteTimerMenus();
                    Logger.debug("Fin menu supervisor, se ingresa a configuracion polaris");
                    UIUtils.intentTrans(CommunSettings.class, "JUMP_KEY", "Configuración inicial", false, MenuBCP.this);
                    break;
                case Definesbcp.ITEM_APPMANAGER:
                    deleteTimerMenus();
                    Logger.debug("Fin menu supervisor, se abre aplicacion de AppManager");
                    try {
                        startActivity(new Intent(getPackageManager().getLaunchIntentForPackage("com.newpos.appmanager")));
                    } catch (Exception ignored) {
                    }
                    break;
                case Definesbcp.ITEM_CONSULTAS:
                    deleteTimerMenus();
                    Logger.debug("Fin menu principal, se ingresa a consultas");
                    if (polarisUtil.isInit()) {
                        UIUtils.intentTrans(MasterControl.class, MasterControl.TRANS_KEY, PrintRes.runnerTransEn(7), false, MenuBCP.this);
                    } else {
                        Toast.makeText(getApplicationContext(), "Operacion deposito no consulta", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Definesbcp.ITEM_MANUFACTURE:
                    deleteTimerMenus();
                    Logger.debug("Fin menu supervisor, se abre aplicacion de Manufacture");
                    try {
                        startActivity(new Intent(getPackageManager().getLaunchIntentForPackage("com.manufacturetest")));
                    } catch (Exception ignored) {
                    }
                    break;
                case Definesbcp.ITEM_POLARIS_CLOUD:
                    deleteTimerMenus();
                    Logger.debug("Fin menu supervisor, se abre aplicacion de Polaris");
                    try {
                        startActivity(new Intent(getPackageManager().getLaunchIntentForPackage("com.downloadmanager")));
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "No existe aplicación", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Operacion no disponible", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        deleteTimerMenus();
    }
}