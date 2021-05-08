package com.bcp.updateapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.bcp.mdm_validation.ReadWriteFileMDM;
import com.newpos.libpay.Logger;

import java.io.File;
import java.util.List;

import static com.android.newpos.pay.StartAppBCP.readWriteFileMDM;

/**
 * Clase para verificar si debe instalar actualizacion
 */
public class UpdateApk {

    public static final String DEFAULT_DOWNLOAD_PATH = Environment.getExternalStorageDirectory() + File.separator + "download";

    private Context mCtx;
    public static final String S ="%S/%S";

    public UpdateApk(Context mCtx) {
        this.mCtx = mCtx;
    }

    public String[] instalarApp(Context c) {
        String[] listOfFiles = Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS).list();

        String[] ret = new String[listOfFiles.length];

        String packageNameDisco = null;
        String versionNameDisco = null;
        String versionNameAppInstalada = null;

        for (int i = 0; i < listOfFiles.length; i++) {

            String apkEnDisco = listOfFiles[i];


            if (apkEnDisco.endsWith(".apk")) {

                final PackageManager pm = c.getPackageManager();
                String fullPath = String.format(S, DEFAULT_DOWNLOAD_PATH, apkEnDisco);
                PackageInfo info = pm.getPackageArchiveInfo(fullPath, 0);

                try {
                    packageNameDisco = info.packageName;
                    versionNameDisco = info.versionName;
                } catch (Exception e) {
                    ret[i] = apkEnDisco;
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                }

                if (packageNameDisco == null){
                    packageNameDisco = "";
                }

                if (!estaInstaladaAplicacion(packageNameDisco.trim(), c)) {

                    File file = new File(String.format(S, DEFAULT_DOWNLOAD_PATH, apkEnDisco));
                    if (file.exists()) {
                        updateFlagAutoInit();
                        checkUpdate();
                    }
                } else {
                    PackageInfo pinfo = null;
                    try {

                        pinfo = c.getPackageManager().getPackageInfo(packageNameDisco, 0);
                        versionNameAppInstalada = pinfo.versionName;

                        if (!versionNameDisco.equals(versionNameAppInstalada)) {

                            File file = new File(String.format(S, DEFAULT_DOWNLOAD_PATH, apkEnDisco));

                            if (file.exists()) {
                                updateFlagAutoInit();
                                checkUpdate();
                            }
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), Logger.TAG + e.getMessage());
                    }

                }
            }
        }

        return ret;

    }

    private void updateFlagAutoInit(){
        readWriteFileMDM.writeFileMDM(readWriteFileMDM.getReverse(), readWriteFileMDM.getSettle(), ReadWriteFileMDM.INITAUTOACTIVE,readWriteFileMDM.getTrans());
    }

    private boolean estaInstaladaAplicacion(String nombrePaquete, Context context) {

        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(nombrePaquete, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "UpdateApk  " +  e.getMessage());
            return false;
        }
    }

    private void checkUpdate(){
        //indica al mdm si tiene apps por instalar
        try {
            Intent launchIntent = new Intent("NOTIFYDOWNLOAD");
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mCtx.startActivity(launchIntent);
        } catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "No se puede abrir intent null" + e.getMessage());
        }
    }

    public static boolean isPackageExisted(String targetPackage, Context context){
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = context.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }
}
