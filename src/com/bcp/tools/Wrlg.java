package com.bcp.tools;

import android.os.Environment;

import com.newpos.libpay.Logger;
import com.newpos.libpay.utils.PAYUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Wrlg {

    FileWriter fileWriter = null;
    BufferedWriter bufferedWriter = null;
    File rutaSd = Environment.getExternalStorageDirectory();
    String nomArchivo;
    File file;
    int opc = 0;

    public void wrDataTxt(String data) {
        String date = PAYUtils.getLocalDate();
        String time = PAYUtils.getLocalTime(PAYUtils.TIMEWITHOUTPOINT);
        try {
            String carpeta = "/logsBCP/debug";
            crearCarpeta(carpeta);

            nomArchivo = "debugdata" + date + ".txt";
            file = new File(rutaSd.getAbsolutePath() + carpeta + '/' + nomArchivo);

            validarPeso(file);

            if (file.exists()) {
                if (opc == 0) {
                    FileReader fileReader = new FileReader(file);
                    try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                        StringBuilder archivo = new StringBuilder();
                        for (String leer; (leer = bufferedReader.readLine()) != null; ) {
                            archivo.append(leer);
                            archivo.append("\n");
                        }
                        fileWriter = new FileWriter(file);
                        bufferedWriter = new BufferedWriter(fileWriter);

                        bufferedWriter.write(archivo.toString());
                        bufferedWriter.append(time).append(" - ").append(data);
                        bufferedWriter.newLine();
                        bufferedWriter.close();
                    }
                } else {
                    Logger.logLine(Logger.LOG_GENERAL, "Error: El archivo de logs no se puede seguir modificando, maximo tamaño excedido");
                }

            } else {
                fileWriter = new FileWriter(file);
                bufferedWriter = new BufferedWriter(fileWriter);

                bufferedWriter.write("Archivo de Log-Debug para el dia " + date);
                bufferedWriter.newLine();
                bufferedWriter.append(time).append(" - ").append(data);
                bufferedWriter.newLine();
                bufferedWriter.close();
            }
        } catch (IOException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
    }

    private File crearCarpeta(String nombreDirectorio) {
        File carpeta = new File(rutaSd.getAbsolutePath(), nombreDirectorio);
        carpeta.mkdirs();

        if (!carpeta.exists()) {
            Logger.logLine(Logger.LOG_GENERAL, "Error: No se creo la carpeta");
        }

        return carpeta;
    }

    private void validarPeso(File fichero) {
        float longitud = fichero.length();
        if (longitud > 5120000){
            opc = 1;
        } else {
            opc = 0;
        }
    }

}
