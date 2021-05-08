package cn.desert.newpos.payui.master;

import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.newpos.pay.R;
import com.android.newpos.pay.StartAppBCP;
import com.bcp.mdm_validation.ReadWriteFileMDM;
import com.newpos.libpay.Logger;
import com.pos.device.icc.IccReader;
import com.pos.device.icc.SlotType;

import static com.android.newpos.pay.ProcessingCertificate.polarisUtil;
import static com.android.newpos.pay.StartAppBCP.readWriteFileMDM;
import static java.lang.Thread.sleep;

/**
 * Created by zhouqiang on 2016/11/12.
 */
public class ResultControl extends AppCompatActivity {
    ImageView imgClose ;
    TextView tittle ;
    TextView contCod ;
    TextView textTransResult;
    TextView msg ;
    RelativeLayout relativeFail;
    LinearLayout linearOkUno;
    LinearLayout linearOkDos;
    LinearLayout linearfail;
    Boolean intentMk = false;
    ImageView face ;
    IccReader iccReader0;
    Thread proceso = null;
    private static final int TIMEOUT_REMOVE_CARD = 60 * 2000;//2 min
    protected String info = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_result);
        iccReader0 = IccReader.getInstance(SlotType.USER_CARD);
        imgClose = findViewById(R.id.iv_close);
        textTransResult = findViewById(R.id.clickclose_transResult);


        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            displayDetails(bundle.getBoolean("flag"), bundle.getStringArray("info"), bundle.getBoolean("isIntent"));
        } else {
            removeCard();
        }

        textTransResult.setOnClickListener(v -> {
            goMenus();
            removeCard();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //nothing
    }

    private void displayDetails(boolean flag , String[] info, boolean isIntent){
        this.intentMk = isIntent;
        linearOkUno = findViewById(R.id.linearTransOKUno);
        relativeFail = findViewById(R.id.relativeTransFail);
        linearOkUno.setVisibility(flag ? View.VISIBLE : View.GONE);
        relativeFail.setVisibility( !flag ? View.VISIBLE : View.GONE);
        if(flag){
            if (info[1].equals("1")){
                transOkUno();
            }else if (info[1].equals("2")){
                transOkDos();
            }else {
                removeCard();
                return;
            }
        }else {
            tittle = findViewById(R.id.titleFail);
            contCod = findViewById(R.id.codFail);
            msg = findViewById(R.id.msgFail);

            if ("xxx00".equals(info[1])) {
                linearfail = findViewById(R.id.linearfail);
                linearfail.setVisibility(View.GONE);
            } else
                contCod.setText(info[1]);
        }
        tittle.setText(info[0]);
        msg.setText(info[2]);
    }

    /**
     * Pantalla ok para deposito
     */
    private void transOkUno(){
        tittle = findViewById(R.id.msg);
        face = findViewById(R.id.imgCheck);
        face.setImageResource(R.drawable.ic_dark);
        msg=findViewById(R.id.msg_bienvenida);
    }

    /**
     * Pantalla ok para demas transacciones
     */
    private  void  transOkDos(){
        linearOkDos= findViewById(R.id.linearTransOKDos);
        linearOkDos.setVisibility(View.VISIBLE);
        linearOkUno.setVisibility(View.INVISIBLE);
        tittle = findViewById(R.id.msgDos);
        face = findViewById(R.id.imgCheckDos);
        face.setImageResource(R.drawable.ic_check_icon);
        msg=findViewById(R.id.msg_bienvenidaDos);
    }

    private void removeCard(){
        if (proceso == null) {
            proceso = new Thread(() -> {

                runOnUiThread(() -> {
                    if (iccReader0.isCardPresent()) {
                        setContentView(R.layout.activity_remove_card);

                        LottieAnimationView gifRemoveCard;
                        gifRemoveCard = findViewById(R.id.gifRemoveCard);
                        gifRemoveCard.playAnimation();

                        ImageView linearLayout;
                        linearLayout = findViewById(R.id.imgCardestadicaremoved);

                        Animation animation;
                        animation = AnimationUtils.loadAnimation(ResultControl.this, R.anim.tarjeta_abajo);
                        linearLayout.startAnimation(animation);
                    }
                });

                if (!checkCard()){

                    //se desactiva el flag cuando se finaliza una transaccion
                    try {
                        readWriteFileMDM.writeFileMDM(readWriteFileMDM.getReverse(), readWriteFileMDM.getSettle(), readWriteFileMDM.getInitAuto(), ReadWriteFileMDM.TRANS_DEACTIVE);
                    }catch (Exception e){
                        Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                    }

                    if (Boolean.TRUE.equals(intentMk)){
                        try {
                            startActivity(new Intent( getPackageManager().getLaunchIntentForPackage("com.wposs.injectmk")));
                        }catch (Exception e){
                            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                            startActivity(new Intent(ResultControl.this, StartAppBCP.class));
                        }
                    }else {
                        startActivity(new Intent(ResultControl.this, StartAppBCP.class));
                    }
                    finish();
                }
            });
            proceso.start();
        }
    }

    private boolean checkCard(){
        boolean ret;
        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        long start = SystemClock.uptimeMillis() ;
        try {
            while (ret = iccReader0.isCardPresent()) {
                toneG.startTone(ToneGenerator.TONE_PROP_BEEP2, 2000);
                sleep(2000);
                if (SystemClock.uptimeMillis() - start > TIMEOUT_REMOVE_CARD) {
                    toneG.stopTone();
                    ret = false;
                    break;
                }
            }
        }catch (Exception e){
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "error " + e);
            ret = false;
        }
        proceso = null;
        return ret;
    }

    private void goMenus() {
        if (polarisUtil.getMakeinitcallback() != null) {
            polarisUtil.getMakeinitcallback().getMakeInitCallback(true);
        }
    }
}
