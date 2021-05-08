package com.bcp.inicializacion.trans_init.trans;

import android.content.Context;
import android.os.AsyncTask;
import com.android.newpos.pay.R;
import com.newpos.libpay.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static java.lang.Thread.sleep;

/**
 * Created by Technology&Solutions on 31/05/2017.
 */

public class UnpackFile extends AsyncTask<String, Integer,Boolean> {

    private String ubicacionZIP;
    private String destinoDescompresion;
    private boolean mantenerZIP;
    private FileCallback callback;
    private boolean ponerLaT;

    /**
     * Descomprime un archivo .ZIP
     * @param ctx Contexto de la Aplicación Android
     * @param ubicacion Ruta ABSOLUTA de un archivo .zip
     * @param destino Ruta ABSOLUTA del destino de la descompresión. Finalizar con /
     * @param mantener Indica si se debe mantener el archivo ZIP despues de descomprimir
     */

    public UnpackFile(Context ctx, String ubicacion, String destino, boolean ponerLaT, boolean mantener, final FileCallback callback)

    {
        this.ubicacionZIP = ubicacion;
        this.destinoDescompresion = destino;
        this.mantenerZIP = mantener;
        this.callback = callback;
        this.ponerLaT = ponerLaT;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        int size;
        byte[] buffer = new byte[2048];

        new File(destinoDescompresion).mkdirs(); //Crea la ruta de descompresion si no existe
        ZipInputStream lectorZip = null;
        ZipEntry itemZip = null;
        FileOutputStream outStream = null;
        BufferedOutputStream bufferOut = null;

        try (FileInputStream lectorArchivo = new FileInputStream( destinoDescompresion + ubicacionZIP)) {

            lectorZip = new ZipInputStream(lectorArchivo);

            while ((itemZip = lectorZip.getNextEntry()) != null) {
                Logger.debug("Descomprimiendo " + itemZip.getName());

                if (itemZip.isDirectory()) { //Si el elemento es un directorio, crearlo
                    creaCarpetas(itemZip.getName(), destinoDescompresion);
                } else {
                    if (ponerLaT){
                        if (itemZip.getName().endsWith(".bin") || itemZip.getName().endsWith(".BIN")){
                            outStream = new FileOutputStream(destinoDescompresion + itemZip.getName());
                        }else{
                            outStream = new FileOutputStream(destinoDescompresion + itemZip.getName()+"T");
                        }
                    }else{
                        outStream = new FileOutputStream(destinoDescompresion + itemZip.getName());
                    }
                    bufferOut = new BufferedOutputStream(outStream, buffer.length);

                    while ((size = lectorZip.read(buffer, 0, buffer.length)) != -1) {
                        bufferOut.write(buffer, 0, size);
                    }

                    bufferOut.flush();
                    bufferOut.close();
                }
            }
            lectorZip.close();

            //Conservar archvi .zip
            if(!mantenerZIP) {
                Logger.debug("archivo eliminado " + new File(ubicacionZIP).delete());
            }

            //Espera para poder realizar las validaciones por cada ciclo
            sleep(5000);

            return true;

        } catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "Descomprimir: " + e);
        } finally {
            String msgUnpack = "UnpackFile";
            if (outStream != null){
                try {
                    outStream.close();
                } catch (IOException e) {
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), msgUnpack + e);
                }
            }

            if (bufferOut != null) {
                try {
                    bufferOut.close();
                } catch (IOException e) {
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), msgUnpack + e);
                }
            }

            if (lectorZip != null){
                try {
                    lectorZip.close();
                } catch (IOException e) {
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), msgUnpack + e);
                }
            }
        }

        return false;

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        //callback
        callback.rspUnpack(aBoolean);
    }

    /**
     * Crea la carpeta donde seran almacenados los archivos del .zip
     * @param dir
     * @param location
     */
    private void creaCarpetas(String dir, String location) {
        File f = new File(location + dir);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }

    /**
     *
     */
    public interface FileCallback{
        void rspUnpack(boolean okUnpack);
    }

}
