package com.newpos.libpay;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.android.newpos.pay.StartAppBCP;
import com.bcp.mdm_validation.ReadWriteFileMDM;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.utils.PAYUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by zhouqiang on 2017/3/8.
 * @author zhouqiang
 * sdk全局日主输出
 */

public class Logger extends AsyncTask<String, String, Void> {
    public static  final String nombreCliente = "BCP";
    public static final String TAG = "PAYSDK";
    public static Logger claseLogger;
    /**
     * Para BCP se plantea el manejo de 2 tipos de LOGS
     * 0 - LOG GENERAL
     * 1 - LOG_EXECPTION
     */
    public static final int LOG_GENERAL = 0;
    public static final int LOG_EXECPTION = 1;
    /*
    public static final int LOG_ERROR = 2;
    public static final int LOG_FLUJO = 3;
    public static final int LOG_COMUNICACION = 4;
    public static final int LOG_TIMER = 5;
    public static final int LOG_IMPRESION = 6;
     */

    public static String logGeneralEnable;
    public static String logExceptionEnable;

    private static String version = "";
    private static final String ERROR_MAX_PESO = "Logs no se puede seguir modificando, maximo tamaño excedido";
    private File rutaSd = Environment.getExternalStorageDirectory();
    private int opc = 0;
    FileWriter fileWriter;
    BufferedWriter bufferedWriter;
    static String claseActual = "Logger.java";
    File file;
    String general = ""; //0
    String exception = "EXCEPTION :";//2

    int tp;
    String msj;

    public Logger(int tipo, String mensaje) {
        this.tp = tipo;
        this.msj = mensaje;
    }

    public Logger() {
    }

    public static void initLogger(Context context) {
        getInstanceClaseLogger();
        NewConfigMDM newConfigMdm = NewConfigMDM.getInstanceNewConfigMDM();
        logGeneralEnable = newConfigMdm.getLogGeneral();
        logExceptionEnable = newConfigMdm.getLogException();
        version = PAYUtils.getVersion(context);
    }

    public static void debug(String msg) {
        if (TMConfig.getInstance().isDebug()) {
            Log.i(TAG, msg);
        }
    }

/*
    public static void error(String msg) {
        if (TMConfig.getInstance().isError()) {
            Log.e(TAG, msg);
        }
    }
    */

    @Override
    protected Void doInBackground(String... args) {

        try{
            String mensaje = args[1];
            int tipo = Integer.parseInt(args[0]);
            switch (tipo) {
                case LOG_GENERAL:
                    if(logGeneralEnable.equals("1")){
                        writeLog(version, general + " " + mensaje);
                    }
                    break;

                case LOG_EXECPTION:
                    if(logExceptionEnable.equals("1")){
                        writeLog(version, exception + " " + mensaje);
                    }
                    break;

                default:
                    break;
            }
        }catch(Exception e){
            Log.e(version, exception + "No se pudo almacenar registro de Logs :" + e.getMessage());
        }


        return null;

    }

    private File crearCarpeta(String nombreDirectorio) {
        File carpeta = new File(rutaSd.getAbsolutePath() + "/LogsWposs/" + nombreCliente + "/" , nombreDirectorio);
        carpeta.mkdirs();
        if (!carpeta.exists()) {
            logLine(LOG_GENERAL,claseActual, "Error: No se creo la carpeta Log");
        }
        return carpeta;
    }

    private void validarPeso(File fichero) {
        float longitud = fichero.length();
        if (longitud > 5120000) {
            opc = 1;
        } else {
            opc = 0;
        }
    }

    private void writeLog(String carpeta, String msg) {
        crearCarpeta(carpeta);
        String date = PAYUtils.getLocalDate();
        String time = PAYUtils.getLocalTime("HH:mm:ss S");

        String nomArchivo = "logsdata" + date + ".txt";
        file = new File(rutaSd.getAbsolutePath() + "/LogsWposs/" + nombreCliente + "/" + carpeta + '/' + nomArchivo);
        validarPeso(file);

        if (file.exists() && opc == 0) {
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(file);
            } catch (FileNotFoundException e) {
                logLine(LOG_EXECPTION,claseActual, e.getStackTrace());
            }
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                StringBuilder archivo = new StringBuilder();
                for (String leer; (leer = bufferedReader.readLine()) != null; ) {
                    archivo.append(leer);
                    archivo.append("\n");
                }
                fileWriter = new FileWriter(file);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(archivo.toString());
                bufferedWriter.append(time).append(" - ").append(msg);
                bufferedWriter.newLine();
                bufferedWriter.close();
            } catch (IOException e) {
                logLine(LOG_EXECPTION,claseActual,  e.getStackTrace());
            }
        } else if (file.exists() && opc == 1) {
            logLine(LOG_GENERAL,claseActual, ERROR_MAX_PESO);
        } else {
            try {
                fileWriter = new FileWriter(file);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.newLine();
                bufferedWriter.append(time).append(" - ").append(msg);
                bufferedWriter.newLine();
                bufferedWriter.close();
            } catch (IOException e) {
                logLine(LOG_EXECPTION,claseActual, e.getStackTrace());
            }
        }
    }


    private static void getInstanceClaseLogger() {
        claseLogger = new Logger();
    }


    public static void getInstanceClaseLogger(int tipo, String msg) {
        try{
            Logger claseLogger = new Logger();
            claseLogger.execute(String.valueOf(tipo), msg);
        }catch (Exception e){
            Log.e("Exception", "No se pudo ejecutar registro de Logs "  + e);
        }
    }

    public static void logLine(int tipo, String clase, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(msg);
        getInstanceClaseLogger(tipo, clase+": "+ sb.toString());
    }
    public static void logLine(int tipo, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(msg);
        getInstanceClaseLogger(tipo,  sb.toString());
    }

    public static void logLine(int tipo, String clase, StackTraceElement[] msg) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : msg) {
            sb.append("Nom del archivo: " + element.getFileName() + "\n");
            sb.append("Num de la linea: " + element.getLineNumber() + "\n");
            sb.append("Nomb del metodo: " + element.getMethodName() + "\n");
        }
        getInstanceClaseLogger(tipo, clase +": "+sb.toString());
    }
    public static void logLine(int tipo,  StackTraceElement[] msg) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : msg) {
            sb.append("Nom del archivo: " + element.getFileName() + "\n");
            sb.append("Num de la linea: " + element.getLineNumber() + "\n");
            sb.append("Nomb del metodo: " + element.getMethodName() + "\n");
        }
        getInstanceClaseLogger(tipo, sb.toString());
    }

    public static void logLine(int tipo, StackTraceElement[] msg, String msgError) {
        StringBuilder sb = new StringBuilder();
        sb.append("Error: " + msgError + "\n");
        for (StackTraceElement element : msg) {
            sb.append("Archivo: " + element.getFileName() + " - " + element.getLineNumber() + "\n");
            sb.append("Metodo: " + element.getMethodName() + "\n");
        }
        getInstanceClaseLogger(tipo, sb.toString());
    }
}
