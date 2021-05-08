package com.android.newpos.pay;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.bcp.inicializacion.configuracioncomercio.Certificate;
import com.bcp.inicializacion.tools.PolarisUtil;
import com.newpos.libpay.PaySdk;
import cn.desert.newpos.payui.base.PayApplication;

public class ProcessingCertificate extends AppCompatActivity {

    public static final Certificate certificate = new Certificate();
    public static final PolarisUtil polarisUtil = new PolarisUtil();

    LottieAnimationView gifProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handling_bcp);

        gifProgress = findViewById(R.id.gifProgres);

        TextView tvtTittle = findViewById(R.id.msgTipoOpe);
        TextView tvtmsg = findViewById(R.id.msgTituloOpe);

        gifProgress.setAnimation("loader.json");
        gifProgress.playAnimation();

        tvtTittle.setText(getResources().getString(R.string.init));
        tvtmsg.setText(getResources().getString(R.string.certificate));

        initSDK();

        readCertificate();
    }

    private void readCertificate() {
        Thread t = new Thread() {
            @Override
            public void run() {
                int i = 0;
                String[] nameFile = certificate.getNameKeystore(i, ProcessingCertificate.this);
                int retVal = 0;
                while (nameFile.length > 0) {
                    if ((retVal = certificate.readCertificate(ProcessingCertificate.this, nameFile[0], nameFile[1])) != 0) {
                        break;
                    }
                    i++;
                    nameFile = certificate.getNameKeystore(i,ProcessingCertificate.this);
                }
                polarisUtil.setCertificate(retVal == 0);

                ProcessingCertificate.this.startActivity(new Intent(ProcessingCertificate.this, StartAppBCP.class));
                ProcessingCertificate.this.finish();

            }
        };
        t.start();
    }

    /**
     * inicializa el sdk
     */
    private void initSDK() {
        PaySdk.getInstance().setActivity(this);
        PayApplication.getInstance().addActivity(this);
        PayApplication.getInstance().setRunned();
    }
}