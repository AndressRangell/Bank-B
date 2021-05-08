package com.android.newpos.pay;

import android.app.AlarmManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.bcp.definesbcp.Variables;
import com.bcp.inicializacion.configuracioncomercio.AGENTE;
import com.bcp.inicializacion.configuracioncomercio.Category;
import com.bcp.inicializacion.configuracioncomercio.CheckCategory;
import com.bcp.inicializacion.configuracioncomercio.CheckCompanny;
import com.bcp.inicializacion.configuracioncomercio.Rango;
import com.bcp.inicializacion.configuracioncomercio.CheckTransactions;
import com.bcp.inicializacion.configuracioncomercio.Transaction;
import com.bcp.login.Login;
import com.bcp.mdm_validation.ReadWriteFileMDM;
import com.bcp.tools.BatteryStatus;
import com.bcp.tools.Estadisticas;
import com.bcp.tools.PaperStatus;
import com.bcp.transactions.common.CommonFunctionalities;
import com.bcp.updateapp.UpdateApk;
import com.newpos.libpay.Logger;
import com.newpos.libpay.device.printer.PrintRes;
import com.pos.device.ped.KeySystem;
import com.pos.device.ped.KeyType;
import com.pos.device.ped.Ped;
import com.pos.device.sys.SystemManager;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import cn.desert.newpos.payui.UIUtils;
import cn.desert.newpos.payui.master.MasterControl;
import cn.desert.newpos.payui.master.ResultControl;
import static com.android.newpos.pay.ProcessingCertificate.polarisUtil;
import static com.bcp.definesbcp.Variables.MASTERKEYIDX;


public class StartAppBCP extends AppCompatActivity {

    public static final BatteryStatus batteryStatus = new BatteryStatus();
    public static final PaperStatus paperStatus = new PaperStatus();
    public static final AGENTE agente = AGENTE.getSingletonInstance();
    public static final Rango rango = Rango.getSingletonInstance();
    public static final Variables variables =  new Variables();
    public static final Estadisticas estadisticas = new Estadisticas();
    public static final CheckTransactions transaction = CheckTransactions.getSingletonInstance();
    public static final ReadWriteFileMDM readWriteFileMDM = ReadWriteFileMDM.getInstance();
    public static final CheckCategory checkCategory = CheckCategory.getSingletonInstance();
    public static final CheckCompanny checkCompanny = CheckCompanny.getSingletonInstance();

    LottieAnimationView gifProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handling_bcp);
        Logger.initLogger(StartAppBCP.this);
        SystemManager.setNoSimDialogVisibility(false);
        batteryStatus();
        validarZonaHoraria();

        if (idioma()){
            Logger.debug("Inicia StartAppBCP");
            initObjetPSTIS();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            }
            readDBInit();
        }
    }

    private void readDBInit(){
        polarisUtil.setCallbackReadDB(null);
        polarisUtil.setCallbackReadDB(status -> {
            if (status) {
                if (threreIsKey(MASTERKEYIDX)){
                    try {
                        if ( UpdateApk.isPackageExisted("com.wposs.injectmk",this))
                            SystemManager.uninstallApp("com.wposs.injectmk");
                    }catch (Exception e){
                        Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                    }
                }
                polarisUtil.setInit(true);
            }
        });
        polarisUtil.isInitPolaris(StartAppBCP.this);
    }

    private boolean setZonaHoraria(String zonaHoraria){
        try {
            AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            if (am != null) {
                am.setTimeZone(zonaHoraria);
                return true;
            }
        }catch (Exception e){
            Logger.logLine(Logger.LOG_EXECPTION, e.getMessage());
        }
        return false;
    }

    private void validarZonaHoraria(){
        boolean ret = true;
        String zonaHorariaLima = "America/Bogota";
        String zonaHoraria = TimeZone.getDefault().getID();

        if (!zonaHoraria.equals(zonaHorariaLima)){
            ret = setZonaHoraria(zonaHorariaLima);
            if(!ret){
                Logger.logLine(Logger.LOG_EXECPTION, "ZONA HORARIA No configurada");
            }
        }
    }

    private void batteryStatus(){
        try {
            this.registerReceiver(batteryStatus, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        }catch (Exception e){
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
    }

    private boolean idioma() {
        boolean ret = false;
        String idiomaLocal = Locale.getDefault().toString();
        if (!idiomaLocal.equals("es_US")){
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_AppCompat_Light_Dialog));

            builder.setIcon(R.drawable.bcp);
            builder.setTitle("Advertencia");
            builder.setMessage("Por favor cambia el idioma del dispositivo.\nPreferencia: Español - Estados Unidos");
            builder.setCancelable(false);

            builder.setPositiveButton("Aceptar", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS)));
            Dialog dialog = builder.create();
            dialog.show();
        } else {
            ret = true;
        }
        return ret;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.initLogger(StartAppBCP.this);
        if (idioma()){
            if (threreIsKey(MASTERKEYIDX)){
                if (polarisUtil.isInit()) {
                    if (!polarisUtil.mustInitialize(StartAppBCP.this)){
                        if(readDBInit(StartAppBCP.this)){
                            if (rWFileMDM())
                                checkedIsMakeInitParcial();
                            else
                                initApp();
                        }else
                            initApp();
                    }else
                        makeInitParcial();
                } else
                    initApp();
            }else{
                UIUtils.startResult(StartAppBCP.this, ResultControl.class,true, false, new String[]  {"Error Master Key","Wposs-205","Debe cargar Master Key"}, true);
            }
        }
    }

    public static boolean threreIsKey(int indexKey){
        int retTmp = Ped.getInstance().checkKey(KeySystem.MS_DES, KeyType.KEY_TYPE_MASTK, indexKey, 0);
        return retTmp == 0;
    }

    /**
     * Instancia todos los objetos necesarios para el manejo de la
     * inicializacion del PSTIS
     */
    private void initObjetPSTIS(){
        //--------- limpiar datos----------
        if (agente != null){
            agente.clearAGENTE();
        }
        if (rango != null){
            rango.clearRango();
        }
    }

    private void makeInitParcial(){
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    long start = SystemClock.uptimeMillis() ;
                    boolean isConnect;
                    boolean justOne = false;
                    //check if connected!
                    while (!(isConnect=isConnected())) {

                        if (!justOne){
                            showMessage("",false);
                            justOne = true;
                        }
                        //Wait to connect
                        Thread.sleep(1000);
                        if (SystemClock.uptimeMillis() - start > 15000) {
                            break;
                        }
                    }
                    if (isConnect) {
                        if (agente.selectAgent(StartAppBCP.this))//Necesita llenar la tabla agente para obtener el ROOT
                            UIUtils.intentTrans(MasterControl.class, MasterControl.TRANS_KEY, PrintRes.runnerTransEn(6), false, StartAppBCP.this);
                        else {
                            showMessage(getResources().getString(R.string.err_read_db), false);
                            initAppWithOutCheckSettle();
                        }
                    }else{
                        showMessage(getResources().getString(R.string.err_connected),true);
                        initAppWithOutCheckSettle();
                    }
                } catch (Exception e) {
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "error " + e);
                    initApp();
                }
            }
        };
        t.start();
    }

    private void checkedIsMakeInitParcial(){
        polarisUtil.setMakeinitcallback(null);
        polarisUtil.setMakeinitcallback(status -> {
            if (status) {
                initApp();
            }
        });
        makeInitParcial();
    }

    private boolean readDBInit(final Context context){
        try {
            List<Transaction> listTrans = transaction.selectTransEnable(context,"1");
            if (listTrans!=null){
                polarisUtil.setListTrans(listTrans);
                //llenado de categorias para pago de servicios
                List<Category> listCategory = checkCategory.selectCategories(context);
                if (listCategory != null)
                    polarisUtil.setListCategory(listCategory);
                if (agente.selectAgent(context)) {
                    return true;
                }
            }
        } catch (Exception e){
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "error " + e);
        }
        return false;
    }

    private boolean rWFileMDM(){
        try {
            if(readWriteFileMDM.readFileMDM()) {
                return readWriteFileMDM.isAutoInitActive();
            } else {
                return false;
            }
        } catch (Exception e){
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "error " + e);
        }
        return false;
    }

    /**
     * Inicio de la app
     */
    private void initApp(){
        if (polarisUtil.isInit() && CommonFunctionalities.mustSettle(StartAppBCP.this)) {
            msgMakeSettle();
        }else {
            Intent intent = new Intent();
            intent.setClass(StartAppBCP.this, Login.class);
            startActivity(intent);
        }
    }

    private void initAppWithOutCheckSettle(){
        Intent intent = new Intent();
        intent.setClass(StartAppBCP.this, Login.class);
        startActivity(intent);
    }

    private boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void msgMakeSettle(){
        final RelativeLayout relativeLayout = findViewById(R.id.msgAccount);
        relativeLayout.setVisibility(View.VISIBLE);
        TextView tvTittle = findViewById(R.id.tittleMsgScreen);
        TextView tvMsg1 = findViewById(R.id.msg1Screen);
        TextView tvMsg2 = findViewById(R.id.msg2Screen);
        TextView tvMsg3 = findViewById(R.id.msg3Screen);

        Button btnContinue = findViewById(R.id.entendidoMsg);

        tvTittle.setText(getResources().getString(R.string.msgSettle1));
        tvMsg1.setVisibility(View.GONE);
        tvMsg2.setText(getResources().getString(R.string.msgSettle2));
        tvMsg3.setText(getResources().getString(R.string.msgSettle3));

        btnContinue.setText(getResources().getString(R.string.continuar));

        relativeLayout.setOnClickListener(v -> {
            //nothing
        });

        btnContinue.setOnClickListener(v ->{
            polarisUtil.setIntentSettle(polarisUtil.getIntentSettle()+1);
            relativeLayout.setVisibility(View.INVISIBLE);
            if(agente.selectAgent(StartAppBCP.this))
                UIUtils.intentTrans(MasterControl.class, MasterControl.TRANS_KEY, PrintRes.runnerTransEn(10), false,StartAppBCP.this);
            else {
                UIUtils.toast(StartAppBCP.this, R.drawable.ic_lg_light, getResources().getString(R.string.err_read_db), Toast.LENGTH_SHORT, new int[]{Gravity.CENTER, 0, 0});
                initAppWithOutCheckSettle();
            }
        });
    }

    private void showMessage(String msg, boolean isToast){
        runOnUiThread(() -> {
            if (isToast)
                UIUtils.toast(StartAppBCP.this, R.drawable.ic_lg_light, msg, Toast.LENGTH_LONG, new int[]{Gravity.CENTER, 0, 0});
            else {
                gifProgress = findViewById(R.id.gifProgres);

                TextView tvtTittle = findViewById(R.id.msgTipoOpe);
                TextView tvtmsg = findViewById(R.id.msgTituloOpe);

                gifProgress.setAnimation("loader.json");
                gifProgress.playAnimation();

                tvtTittle.setText(getResources().getString(R.string.init));
                tvtmsg.setText(getResources().getString(R.string.without_internet));
            }
        });
    }
}
