package com.bcp.startboot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.bcp.splash.Splash;

public class StartBoot extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, Splash.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
