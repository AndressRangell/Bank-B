package com.bcp.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.android.newpos.pay.ProcessingCertificate;
import com.android.newpos.pay.R;

public class Splash extends AppCompatActivity {

    LottieAnimationView splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            splash = findViewById(R.id.splash);
            splash.pauseAnimation();
            Splash.this.startActivity(new Intent(Splash.this, ProcessingCertificate.class));
            Splash.this.finish();

        }, 2800);
    }
}
