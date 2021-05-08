package com.bcp.cambio_clave;

import android.content.Context;
import android.content.SharedPreferences;
import com.bcp.transactions.common.CommonFunctionalities;
import com.newpos.libpay.Logger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static com.android.newpos.pay.StartAppBCP.agente;

public class UtilChangePwd {

    protected UtilChangePwd() { }

    private static int remainigDays = -1;
    private static final String PATTERN_DATE_FORMAT = "dd/MM/yyyy HH:mm";
    private static final String MSG_CHANGE_PWD = "fecha-cambio-clave";
    private static final String MSG_REPORT = "informativo";

    public static boolean checkLastPassword(Context context) {
        DateFormat hourDateFormat = new SimpleDateFormat(PATTERN_DATE_FORMAT);
        Date currentDate = new Date();
        String dateFormat = hourDateFormat.format(currentDate);
        SharedPreferences prefs = context.getSharedPreferences(MSG_CHANGE_PWD, MODE_PRIVATE);
        String dateChange = prefs.getString("fecha-sig-cambio", null);
        if (dateChange != null) {
            Date changeDate;
            Date dateCurrent;
            try {
                changeDate = hourDateFormat.parse(dateChange);
                dateCurrent = hourDateFormat.parse(dateFormat);
                assert dateCurrent != null;
                int rta = dateCurrent.compareTo(changeDate);
                if (rta >= 0) {
                    return false;
                }else {
                    if (changeDate != null){
                        remainigDays =(int) ((changeDate.getTime()-dateCurrent.getTime())/86400000);
                    }
                }
            } catch (ParseException e) {
                Logger.debug("error" + e);
            }
        } else {
            saveDateChange(context);
        }
        return true;
    }

    public static void saveDateChange(Context context) {
        DateFormat hourDateFormat = new SimpleDateFormat(PATTERN_DATE_FORMAT);
        Date fechaActual = new Date();
        String daysChangePwd = !agente.getKeyExpiration().equals("") ? agente.getKeyExpiration() : "5";
        Date dateChangePwd = CommonFunctionalities.sumarRestarDiasFecha(fechaActual, Integer.parseInt(daysChangePwd));
        SharedPreferences.Editor editor = context.getSharedPreferences(MSG_CHANGE_PWD, MODE_PRIVATE).edit();
        editor.putString("fecha-sig-cambio", hourDateFormat.format(dateChangePwd));
        saveReportAgent(context,true);
        editor.apply();
    }

    public static void saveReportAgent(Context context, boolean flag) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MSG_CHANGE_PWD, MODE_PRIVATE).edit();
        editor.putBoolean(MSG_REPORT, flag);
        editor.apply();
    }

    public static boolean getReportAgent(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MSG_CHANGE_PWD, MODE_PRIVATE);
        return prefs.getBoolean(MSG_REPORT, false);
    }

    public static int getRemainigDays() {
        return remainigDays;
    }
}
