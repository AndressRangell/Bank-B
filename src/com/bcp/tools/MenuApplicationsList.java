package com.bcp.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.android.newpos.pay.R;
import com.newpos.libpay.Logger;

import cn.desert.newpos.payui.UIUtils;
import cn.desert.newpos.payui.master.ResultControl;

public class MenuApplicationsList {

    private Context mContext;
    private boolean showMsgCancel;
    private String transEname;

    public MenuApplicationsList(Context mContext, String nameTrans) {
        this.mContext = mContext;
        this.showMsgCancel = false;
        this.transEname = nameTrans;
    }

    public void menuApplicationsList(String[] apps, final WaitSelectApplicationsList callback){
        final String[] listApps = new String[apps.length];
        int i = 0;
        final int[] selapp = new int[1];
        for(String  str : apps)
        {
            listApps[i++] = str.trim();
            Logger.logLine(Logger.LOG_GENERAL, str.trim());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.dialog_title_listapp);
        builder.setCancelable(false);

        //list of items
        builder.setSingleChoiceItems(listApps, 0,
                (dialog, select) -> {
                    // item selected logic
                    selapp[0] = select;
                });


        builder.setPositiveButton("Aceptar",
                (dialog, which) -> {
                    // positive button logic
                    callback.getAppListSelect(selapp[0]);
                    dialog.dismiss();
                });

        builder.setNegativeButton("Cancelar", (dialogInterface, i1) -> {
            callback.getAppListSelect(-1);
            if (showMsgCancel)
                UIUtils.startResult((Activity) mContext, ResultControl.class,true, false, new String[] {transEname , "104", "TRANSACCION CANCELADA"},false);
        });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }
}
