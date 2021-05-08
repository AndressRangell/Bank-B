package com.bcp.login;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.android.newpos.pay.R;
import com.bcp.cambio_clave.CambioClave;
import com.bcp.cambio_clave.UtilChangePwd;
import com.bcp.definesbcp.Definesbcp;
import com.bcp.menus.MenuBCP;
import com.bcp.settings.BCPConfig;
import com.bcp.splash.Splash;
import com.bcp.transactions.common.CommonFunctionalities;
import com.newpos.libpay.Logger;
import com.newpos.libpay.device.printer.PrintRes;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.utils.ISOUtil;
import com.newpos.libpay.utils.PAYUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import cn.desert.newpos.payui.UIUtils;
import cn.desert.newpos.payui.master.MasterControl;

import static com.android.newpos.pay.ProcessingCertificate.polarisUtil;
import static com.android.newpos.pay.StartAppBCP.agente;

public class Login extends AppCompatActivity {

    StringBuilder builder;
    ImageButton ibDigito1;
    ImageButton ibDigito2;
    ImageButton ibDigito3;
    ImageButton ibDigito4;
    ImageButton ibDigito5;
    ImageButton ibDigito6;
    ImageButton numberDelete;

    ImageView imgLogo;
    TextView tvClave;
    TextView tvSocio;
    ImageView menuSupervisor;
    TextView clickMenuSupervisor;
    ImageView imgBack;
    TextView clickBack;
    boolean isSupervisor;
    boolean isChecked;//Permite solo validar con el primer digito

    LottieAnimationView gifVerificando;
    LottieAnimationView gifCheck;

    LinearLayout linearDigitos;
    LinearLayout linearVerificando;
    LinearLayout linearError;
    LinearLayout linearConfirmacion;

    //caducidad de clave
    RelativeLayout relativeCahngePwd;
    Button btnIgnore;
    Button btnChangePwd;
    TextView msgTittle;
    TextView msgCaducidad;
    TextView msgErr;
    TextView tvVersion;

    Handler handler = new Handler();

    private String loginPassword;

    int intento = 0;
    private CountDownTimer countDownTimerLogin;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Logger.debug("Inicia Login");

        validateRam();

        gifVerificando = findViewById(R.id.gifVerificando);
        gifCheck = findViewById(R.id.gifCheck);

        ibDigito1 = findViewById(R.id.ibDigito1);
        ibDigito2 = findViewById(R.id.ibDigito2);
        ibDigito3 = findViewById(R.id.ibDigito3);
        ibDigito4 = findViewById(R.id.ibDigito4);
        ibDigito5 = findViewById(R.id.ibDigito5);
        ibDigito6 = findViewById(R.id.ibDigito6);

        numberDelete = findViewById(R.id.numberDelete);

        imgLogo = findViewById(R.id.imgAvatar);
        tvClave = findViewById(R.id.tvMsg1);
        tvSocio = findViewById(R.id.tvMsg2);
        menuSupervisor = findViewById(R.id.menuSupervisor);
        clickMenuSupervisor = findViewById(R.id.clickMenuSupervisor);
        imgBack = findViewById(R.id.imgBack);
        clickBack = findViewById(R.id.clickBack);

        linearDigitos = findViewById(R.id.linearDigitos);
        linearVerificando = findViewById(R.id.linearVerificando);
        linearError = findViewById(R.id.linearError);
        linearConfirmacion = findViewById(R.id.linearConfirmacion);
        tvVersion = findViewById(R.id.version);

        msgErr = findViewById(R.id.tvErr);

        builder = new StringBuilder();

        findViewById(R.id.number1).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.number2).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.number3).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.number4).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.number5).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.number6).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.number7).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.number8).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.number9).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.number0).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.numberDelete).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.menuSupervisor).setOnClickListener(keyBoardClickListener);
        findViewById(R.id.clickMenuSupervisor).setOnClickListener(keyBoardClickListener);
        imgBack.setOnClickListener(keyBoardClickListener);
        clickBack.setOnClickListener(keyBoardClickListener);

        final Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null && Objects.equals(extras.getString(Definesbcp.LOGIN_SUPERVISOR), Definesbcp.LOGIN_SUPERVISOR)) {
            isSupervisor = true;
            imgLogo.setImageDrawable(getDrawable(R.drawable.ic_avatar_supervisor));
            tvClave.setText(getResources().getString(R.string.saludosupervisor));
            menuSupervisor.setVisibility(View.INVISIBLE);
            clickMenuSupervisor.setVisibility(View.INVISIBLE);
            imgBack.setVisibility(View.VISIBLE);
            clickBack.setVisibility(View.VISIBLE);
        }

        if (isSupervisor){
            loginPassword = polarisUtil.isInit() ? agente.getAccess() : "123456";
        }else {
            loginPassword= BCPConfig.getInstance(Login.this).getLoginPassword(BCPConfig.LOGINPASSWORDKEY);
        }

        isChecked = false;

        getVersion();
    }

    private void validateRam() {
        try {
            Runtime rt = Runtime.getRuntime();
            if (rt.totalMemory() >= 314572800) {//300MB
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_AppCompat_Light_Dialog));

                alertBuilder.setTitle("Liberador de memoria");
                alertBuilder.setMessage("La aplicación será reiniciada para un mejor funcionamiento.");
                alertBuilder.setCancelable(false);

                alertBuilder.setPositiveButton("Entendido", (dialogInterface, i) -> {
                    startActivity(new Intent(Login.this, Splash.class));
                    onDestroy();
                });
                Dialog dialog = alertBuilder.create();
                dialog.show();
            }
        }catch (Exception e){
            Logger.logLine(Logger.LOG_EXECPTION,"Fallo en la liberacion de memoria : " + e.getMessage(),e.getStackTrace());
        }
    }

    private boolean changePassword(){
        boolean ret = false;
        relativeCahngePwd = findViewById(R.id.relativeChangePwd);
        btnIgnore = findViewById(R.id.ignore);
        btnChangePwd = findViewById(R.id.changePwd);
        msgTittle = findViewById(R.id.msgDaysPwd);
        msgCaducidad = findViewById(R.id.msgPwd);

        if (!UtilChangePwd.checkLastPassword(Login.this)){
            msgTittle.setText(getResources().getString(R.string.timeExp));
            msgCaducidad.setText(getResources().getString(R.string.msgPwdExp));
            btnIgnore.setText(getResources().getString(R.string.forget));
            timeExpired(true);
        }else if ((UtilChangePwd.getRemainigDays() > -1 && UtilChangePwd.getRemainigDays() <= 3) && UtilChangePwd.getReportAgent(Login.this)){
            UtilChangePwd.saveReportAgent(Login.this,false);
            msgTittle.setText(getResources().getString(R.string.daysRes));
            msgCaducidad.setText(getResources().getString(R.string.changePass));
            btnIgnore.setText(getResources().getString(R.string.ignore));
            timeExpired(false);
        }else {
            ret = true;
        }

        return ret;
    }

    private void msgAlert(boolean isFailedAttempts){
        relativeLayout = findViewById(R.id.msgSettle);
        relativeLayout.setVisibility(View.VISIBLE);
        TextView tvTittle = findViewById(R.id.tittleMsgScreen);
        TextView tvMsg1 = findViewById(R.id.msg1Screen);
        TextView tvMsg2 = findViewById(R.id.msg2Screen);
        TextView tvMsg3 = findViewById(R.id.msg3Screen);

        Button btnContinue = findViewById(R.id.entendidoMsg);

        if (isFailedAttempts){
            tvTittle.setText(getResources().getString(R.string.msgFailedAttemptTittle));
            tvMsg1.setText(getResources().getString(R.string.msgFailedAttempt1));
            tvMsg2.setText(getResources().getString(R.string.msgFailedAttempt2));
        }else {
            tvTittle.setText(getResources().getString(R.string.msgSettle1));
            tvMsg1.setVisibility(View.GONE);
            tvMsg2.setText(getResources().getString(R.string.msgSettle2));
            tvMsg3.setText(getResources().getString(R.string.msgSettle3));
        }
        btnContinue.setText(getResources().getString(R.string.continuar));

        relativeLayout.setOnClickListener(v -> {
            //nothing
        });

        btnContinue.setOnClickListener(v ->{
            relativeLayout.setVisibility(View.INVISIBLE);
            if (!isFailedAttempts) {
                if(agente.selectAgent(Login.this))
                    UIUtils.intentTrans(MasterControl.class, MasterControl.TRANS_KEY, PrintRes.runnerTransEn(10), false, Login.this);
                else {
                    UIUtils.toast(Login.this, R.drawable.ic_lg_light, getResources().getString(R.string.err_read_db), Toast.LENGTH_SHORT, new int[]{Gravity.CENTER, 0, 0});
                }
            }
        });
    }

    private void timeExpired(final boolean expired){
        relativeCahngePwd.setVisibility(View.VISIBLE);
        btnChangePwd.setOnClickListener(v -> UIUtils.startResult(Login.this, CambioClave.class, false, false, new String[] {}, false));

        btnIgnore.setOnClickListener(v -> {
            if (!expired){
                relativeCahngePwd.setVisibility(View.GONE);
            }else {
                RelativeLayout relativeLayout = findViewById(R.id.relativeSoporte);
                ImageView imgClose = findViewById(R.id.iv_close);
                relativeLayout.setVisibility(View.VISIBLE);
                imgClose.setOnClickListener(v1 -> intentLogin(false));
            }
        });
    }

    private void addCircle() {
        int len = builder.length();
        switch (len) {
            case 1:
                numberDelete.setColorFilter(Color.argb(255, 255, 255, 255));
                ibDigito1.setImageResource(R.drawable.oval_pass);
                break;
            case 2:
                ibDigito2.setImageResource(R.drawable.oval_pass);
                break;
            case 3:
                ibDigito3.setImageResource(R.drawable.oval_pass);
                break;
            case 4:
                ibDigito4.setImageResource(R.drawable.oval_pass);
                break;
            case 5:
                ibDigito5.setImageResource(R.drawable.oval_pass);
                break;
            case 6:
                ibDigito6.setImageResource(R.drawable.oval_pass);
                break;
            default:
                Toast.makeText(this, getResources().getString(R.string.msg_len), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void initApp() {
        Intent intent = new Intent();
        if (isSupervisor){
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClass(Login.this, MenuBCP.class);
            Logger.debug( getResources().getString(R.string.menSupervisor));
            intent.putExtra(Definesbcp.DATO_MENU, R.array.mainSupervisor);
        }else {
            intent.setClass(Login.this, MenuBCP.class);
            Logger.debug( getResources().getString(R.string.menuPrinci));
            intent.putExtra(Definesbcp.DATO_MENU, R.array.main1);
        }
        startActivity(intent);
        finish();
    }

    private void hide() {
        linearDigitos.setVisibility(View.GONE);
        linearVerificando.setVisibility(View.VISIBLE);
        gifVerificando.playAnimation();
    }

    public void fillData(String val) {
        try {
            if (!validateLogin()) {
                msgAlert(true);
                return;
            }

            if (!isChecked && !isSupervisor){
                isChecked = true;
                if (polarisUtil.mustInitialize(Login.this)){
                    UIUtils.intentTrans(MasterControl.class, MasterControl.TRANS_KEY, PrintRes.runnerTransEn(6), false, Login.this);
                    return;
                }

                if (CommonFunctionalities.mustSettle(Login.this)) {
                    msgAlert(false);
                    return;
                }

                if (!changePassword()){
                    return;
                }
            }

            if (builder.length() < 5) {
                builder.append(val);
                addCircle();
            } else if (builder.length() == 5) {
                builder.append(val);
                addCircle();
                if (builder.toString().equals(loginPassword)) {

                    if (!isSupervisor && (BCPConfig.getInstance(this).getTrack2Agente(BCPConfig.TRACK2AGENTEKEY).length() == 0 || !polarisUtil.isInit())) {
                        linearError.setVisibility(View.VISIBLE);
                        msgErr.setText(getResources().getString(R.string.must_init));
                        clearCamps();
                        handler.postDelayed(() -> {
                            linearError.setVisibility(View.GONE);
                            nuevoIngreso();
                        }, 3000);
                        return;
                    }

                    Logger.debug( getResources().getString(R.string.contraCorrecta));
                    hide();
                    new Handler().postDelayed(() -> {
                        unlockAcces();
                        clearCamps();
                        hide3();
                        gifCheck.playAnimation();
                        linearConfirmacion.setVisibility(View.VISIBLE);
                        handler.postDelayed(this::initApp, 500);
                    }, 500);
                } else {
                    validateIntentLogin();
                    Logger.logLine( Logger.LOG_GENERAL, getResources().getString(R.string.contraIncorrec) + intento);
                    hide();
                    new Handler().postDelayed(() -> {
                        hide2();
                        linearError.setVisibility(View.VISIBLE);
                        String msg = getResources().getString(R.string.wrongpwd) +
                                "\nLe quedan " +
                                (3 - intento) +
                                " intentos";
                        msgErr.setText(msg);
                        clearCamps();
                        ibDigito1.setBackgroundResource((R.drawable.password_line_error));
                        ibDigito2.setBackgroundResource((R.drawable.password_line_error));
                        ibDigito3.setBackgroundResource((R.drawable.password_line_error));
                        ibDigito4.setBackgroundResource((R.drawable.password_line_error));
                        ibDigito5.setBackgroundResource((R.drawable.password_line_error));
                        ibDigito6.setBackgroundResource((R.drawable.password_line_error));

                        handler.postDelayed(() -> {
                            linearError.setVisibility(View.GONE);
                            nuevoIngreso();
                        }, 1000);
                    }, 500);
                }
            }
        } catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
    }

    private void hide3() {
        linearVerificando.setVisibility(View.GONE);
        linearDigitos.setVisibility(View.GONE);
    }

    private void hide2() {
        linearVerificando.setVisibility(View.GONE);
        linearDigitos.setVisibility(View.VISIBLE);
    }

    public void nuevoIngreso() {
        ibDigito1.setBackgroundResource((R.drawable.password_line));
        ibDigito2.setBackgroundResource((R.drawable.password_line));
        ibDigito3.setBackgroundResource((R.drawable.password_line));
        ibDigito4.setBackgroundResource((R.drawable.password_line));
        ibDigito5.setBackgroundResource((R.drawable.password_line));
        ibDigito6.setBackgroundResource((R.drawable.password_line));

        builder.delete(0, 6);
    }

    private void clearCamps() {
        ibDigito1.setImageResource(R.drawable.oval_pass_null);
        ibDigito2.setImageResource(R.drawable.oval_pass_null);
        ibDigito3.setImageResource(R.drawable.oval_pass_null);
        ibDigito4.setImageResource(R.drawable.oval_pass_null);
        ibDigito5.setImageResource(R.drawable.oval_pass_null);
        ibDigito6.setImageResource(R.drawable.oval_pass_null);
    }

    private final View.OnClickListener keyBoardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.number0:
                    fillData(getString(R.string._0));
                    break;
                case R.id.number1:
                    fillData(getString(R.string._1));
                    break;
                case R.id.number2:
                    fillData(getString(R.string._2));
                    break;
                case R.id.number3:
                    fillData(getString(R.string._3));
                    break;
                case R.id.number4:
                    fillData(getString(R.string._4));
                    break;
                case R.id.number5:
                    fillData(getString(R.string._5));
                    break;
                case R.id.number6:
                    fillData(getString(R.string._6));
                    break;
                case R.id.number7:
                    fillData(getString(R.string._7));
                    break;
                case R.id.number8:
                    fillData(getString(R.string._8));
                    break;
                case R.id.number9:
                    fillData(getString(R.string._9));
                    break;
                case R.id.numberDelete:
                    int len = builder.length();
                    if (len != 0) {
                        builder.deleteCharAt(len - 1);
                    }
                    len = builder.length();
                    switch (len) {
                        case 0:
                            isChecked = false;
                            numberDelete.setColorFilter(Color.GRAY);
                            ibDigito1.setImageResource(R.drawable.oval_pass_null);
                            ibDigito2.setImageResource(R.drawable.oval_pass_null);
                            ibDigito3.setImageResource(R.drawable.oval_pass_null);
                            ibDigito4.setImageResource(R.drawable.oval_pass_null);
                            ibDigito5.setImageResource(R.drawable.oval_pass_null);
                            ibDigito6.setImageResource(R.drawable.oval_pass_null);
                            break;
                        case 1:
                            ibDigito1.setImageResource(R.drawable.oval_pass);
                            ibDigito2.setImageResource(R.drawable.oval_pass_null);
                            ibDigito3.setImageResource(R.drawable.oval_pass_null);
                            ibDigito4.setImageResource(R.drawable.oval_pass_null);
                            ibDigito5.setImageResource(R.drawable.oval_pass_null);
                            ibDigito6.setImageResource(R.drawable.oval_pass_null);
                            break;
                        case 2:
                            ibDigito1.setImageResource(R.drawable.oval_pass);
                            ibDigito2.setImageResource(R.drawable.oval_pass);
                            ibDigito3.setImageResource(R.drawable.oval_pass_null);
                            ibDigito4.setImageResource(R.drawable.oval_pass_null);
                            ibDigito5.setImageResource(R.drawable.oval_pass_null);
                            ibDigito6.setImageResource(R.drawable.oval_pass_null);
                            break;
                        case 3:
                            ibDigito1.setImageResource(R.drawable.oval_pass);
                            ibDigito2.setImageResource(R.drawable.oval_pass);
                            ibDigito3.setImageResource(R.drawable.oval_pass);
                            ibDigito4.setImageResource(R.drawable.oval_pass_null);
                            ibDigito5.setImageResource(R.drawable.oval_pass_null);
                            ibDigito6.setImageResource(R.drawable.oval_pass_null);
                            break;
                        case 4:
                            ibDigito1.setImageResource(R.drawable.oval_pass);
                            ibDigito2.setImageResource(R.drawable.oval_pass);
                            ibDigito3.setImageResource(R.drawable.oval_pass);
                            ibDigito4.setImageResource(R.drawable.oval_pass);
                            ibDigito5.setImageResource(R.drawable.oval_pass_null);
                            ibDigito6.setImageResource(R.drawable.oval_pass_null);
                            break;
                        case 5:
                            ibDigito1.setImageResource(R.drawable.oval_pass);
                            ibDigito2.setImageResource(R.drawable.oval_pass);
                            ibDigito3.setImageResource(R.drawable.oval_pass);
                            ibDigito4.setImageResource(R.drawable.oval_pass);
                            ibDigito5.setImageResource(R.drawable.oval_pass);
                            ibDigito6.setImageResource(R.drawable.oval_pass_null);
                            break;
                        default:
                            UIUtils.toast(Login.this, R.drawable.ic_lg_light, getString(R.string.msg_len), Toast.LENGTH_LONG, new int[]{Gravity.CENTER, 0, 0});
                            break;
                    }
                    break;
                case R.id.menuSupervisor:
                case R.id.clickMenuSupervisor:
                    intentLogin(true);
                    break;
                case R.id.imgBack:
                case R.id.clickBack:
                    intentLogin(false);
                    break;
                default:
                    break;
            }
        }
    };

    protected void intentLogin(boolean isDefine){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(Login.this, Login.class);
        if (isDefine)
            intent.putExtra(Definesbcp.LOGIN_SUPERVISOR, Definesbcp.LOGIN_SUPERVISOR);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //Se elimina la funcion del boton back
    }

    private void getVersion() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
        assert packageInfo != null;
        String versionApp = getResources().getString(R.string.version) + " " + packageInfo.versionName;

        tvVersion.setText(versionApp);
    }

    private boolean validateLogin(){
        boolean ret = false;

        if ((isSupervisor ? TMConfig.getInstance().getIntentLoginTecnico() : TMConfig.getInstance().getIntentLoginAgent()) >= 3){
            long timer = getDifferenceBetwenDates(Calendar.getInstance().getTime(),isSupervisor ? TMConfig.getInstance().getDateUnlockTecnico() : TMConfig.getInstance().getDateUnlockAgent());

            if (timer < 0){
                counterDownTimerAccesLogin(timer * -1);
            }else{
                unlockAcces();
                ret = true;
            }

        }else
            ret = true;

        return ret;
    }

    private void validateIntentLogin(){
        intento = isSupervisor ? TMConfig.getInstance().getIntentLoginTecnico() : TMConfig.getInstance().getIntentLoginAgent();
        intento++;

        if (isSupervisor)
            TMConfig.getInstance().setIntentLoginTecnico(String.valueOf(intento)).save();
        else
            TMConfig.getInstance().setIntentLoginAgent(String.valueOf(intento)).save();

        if (intento >= 3){
            if (isSupervisor)
                TMConfig.getInstance().setDateUnlockTecnico(PAYUtils.dateToStr(addMinutes(),"yyyy-MM-dd HH:mm:ss")).save();
            else
                TMConfig.getInstance().setDateUnlockAgent(PAYUtils.dateToStr(addMinutes(),"yyyy-MM-dd HH:mm:ss")).save();
        }
    }

    private Date addMinutes(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);
        return calendar.getTime();
    }

    private long getDifferenceBetwenDates(Date dateInicio, Date dateFinal) {
        return dateInicio.getTime()-dateFinal.getTime();
    }

    private void counterDownTimerAccesLogin(long timer) {
        TextView textView = findViewById(R.id.msg3Screen);
        if (countDownTimerLogin != null) {
            countDownTimerLogin.cancel();
            countDownTimerLogin = null;
        }

        countDownTimerLogin = new CountDownTimer(timer, 1000) {
            public void onTick(long millisUntilFinished) {
                Log.d("onTick", getResources().getString(R.string.msgInitTimer) + millisUntilFinished/1000);
                if (relativeLayout.getVisibility() == View.VISIBLE){
                    String timeRemaining = ISOUtil.padleft(String.valueOf(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)),2,'0') + ":" + ISOUtil.padleft(String.valueOf((int) (millisUntilFinished / 1000) % 60), 2,'0');
                    textView.setText(timeRemaining);
                }
            }
            public void onFinish() {
                Log.d("onTick", getResources().getString(R.string.msgfinishTimer));
                unlockAcces();
                intentLogin(isSupervisor);
            }
        }.start();
    }

    private void unlockAcces(){
        if (isSupervisor)
            TMConfig.getInstance().setIntentLoginTecnico(String.valueOf(0)).save();
        else
            TMConfig.getInstance().setIntentLoginAgent(String.valueOf(0)).save();
    }
}
