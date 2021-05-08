package cn.desert.newpos.payui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.newpos.pay.R;
import com.bcp.amount.MsgScreenAmount;
import com.bcp.settings.BCPConfig;
import com.newpos.libpay.Logger;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.Trans;
import com.newpos.libpay.utils.ISOUtil;
import java.text.DecimalFormat;

import static com.android.newpos.pay.StartAppBCP.agente;
import static java.lang.Thread.sleep;

/**
 * @author zhouqiang
 * @email wy1376359644@163.com
 */
public class UIUtils {

    private UIUtils() {
    }

    /**
     * 显示交易结果
     *
     * @param activity
     * @param flag
     * @param info
     */
    public static void startResult(Activity activity, Class<?> cls, boolean indInfo, boolean flag, String[] info, boolean boton) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (indInfo) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("flag", flag);
            bundle.putStringArray("info", info);
            bundle.putBoolean("isIntent", boton);
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
        activity.finish();
    }

    public static void toast(Activity activity, int ico, String str, int duration, int[] dirToast) {
        LayoutInflater inflater_3 = activity.getLayoutInflater();
        View view3 = inflater_3.inflate(R.layout.toast_transaparente,
                (ViewGroup) activity.findViewById(R.id.toast_layoutTransaparent));
        Toast toast = new Toast(activity);
        toast.setGravity(dirToast[0], dirToast[1], dirToast[2]);
        toast.setDuration(duration);
        toast.setView(view3);
        ((TextView) view3.findViewById(R.id.toastMessageDoc)).setText(str);
        toast.show();
    }

    public static Dialog centerDialog(Context mContext, int resID, int root) {
        final Dialog pd = new Dialog(mContext, R.style.Translucent_Dialog);
        pd.setContentView(resID);
        LinearLayout layout = pd.findViewById(root);
        layout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.up_down));
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(true);
        pd.show();
        return pd;
    }

    /**
     * 根据资源ID获取资源字符串
     *
     * @param context 上下文对象
     * @param resid   资源ID
     * @return
     */
    public static String getStringByInt(Context context, int resid) {

        return context.getResources().getString(resid);
    }

    public static void beep(int typeTone) {

        int timeOut = 2;
        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);

        long start = SystemClock.uptimeMillis();
        while (true) {
            toneG.startTone(typeTone, 2000);
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "Exception" + e.toString());
                Thread.currentThread().interrupt();
            }

            if (SystemClock.uptimeMillis() - start > timeOut) {
                toneG.stopTone();
                break;
            }
        }
    }

    public static String labelHTML(final String label, final String value) {
        String varB = "<b>";
        return varB + label + varB + " " + value;
    }

    /**
     * Crea un dialogo de alerta
     *
     * @return Nuevo dialogo
     */
    public static void showAlertDialog(String title, String msg, Context context) {
        final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);

        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.drawable.bcp);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        android.app.AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    public static String formatMiles(String valor) {
        String formatOut = "";
        if (valor.length() > 2) {
            formatOut = valor.substring(0, valor.length() - 2) + "." + valor.substring(valor.length() - 2);
        } else if (valor.length() == 2){
            formatOut = "0." + valor;
        } else {
            formatOut = "0.0" + valor;
        }
        DecimalFormat formatMinimoRetiro = new DecimalFormat("###,##0.00");
        return (formatMinimoRetiro.format(Double.parseDouble(formatOut)));
    }

    public static MsgScreenAmount fillScreenAmount(String titleTrans, String maxAmount, String account, int mtimeOut, String symbol, String tittle, String minAmount) {
        MsgScreenAmount msgScreenAmount = new MsgScreenAmount();

        msgScreenAmount.setTransEname(titleTrans);
        msgScreenAmount.setMaxAmount(maxAmount);
        msgScreenAmount.setMinAmount(minAmount);
        msgScreenAmount.setAccount(account);
        msgScreenAmount.setTimeOut(mtimeOut);
        msgScreenAmount.setTittle(tittle);
        msgScreenAmount.setTypeCoin(symbol);

        return msgScreenAmount;
    }

    public static void intentTrans(Class<?> cls, String key, String value, boolean boolKey, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, cls);
        if (boolKey) {
            intent.putExtra(key, ISOUtil.stringToBoolean(value));
        } else {
            if (key != null && !key.equals("")) {
                intent.putExtra(key, value);
            }
        }
        context.startActivity(intent);
    }

    public static boolean validateMaxAmount(long amount, String transEname, boolean isValidate, Context context){
        long amountBase = BCPConfig.getInstance(context).getAmntMaxAgent(BCPConfig.AMOUNTMAXAGENTKEY) != null ? BCPConfig.getInstance(context).getAmntMaxAgent(BCPConfig.AMOUNTMAXAGENTKEY) : 0;
        switch (transEname){
            case Trans.DEPOSITO:
            case Trans.GIROS:
                amountBase += amount;
                break;
            case Trans.RETIRO:
                amountBase -= amount;
                break;
            default:
                break;
        }
        if (isValidate){
            return amountBase >= -Integer.parseInt(agente.getMaximumTransactionAmount()) && amountBase <= Integer.parseInt(agente.getMaximumTransactionAmount());
        }else {
            BCPConfig.getInstance(context).setAmntMaxAgent(BCPConfig.AMOUNTMAXAGENTKEY,amountBase);
        }
        return true;
    }

    public static String checkNull(String strText) {
        if (strText == null) {
            strText = "---";
        }
        return strText;
    }

    public static int validateResponseError(int error){
        int ret;
        switch (error){
            case TcodeError.T_INTERNAL_SERVER_ERR:
            case TcodeError.T_ERR_BAD_REQUEST:
            case TcodeError.T_ERR_NO_FOUND:
                ret = TcodeError.T_RECEIVE_REFUSE;
                break;
            default:
                ret = error;
                break;
        }
        return ret;
    }
}
