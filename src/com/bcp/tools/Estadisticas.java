package com.bcp.tools;

import android.os.Environment;
import com.newpos.libpay.Logger;
import com.newpos.libpay.trans.Trans;
import com.newpos.libpay.utils.PAYUtils;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.jose4j.json.internal.json_simple.parser.ParseException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.json.JSONException;


public class Estadisticas {

    public static final String TRANSSUCCESS = "APROBADAS";
    public static final String TRANSFAIL = "RECHAZADAS";
    public static final String TRANSTIMEOUT = "TIMEOUT";
    public static final String TRANSIMPRE = "ERROR_IMPRESION";

    private static final String FICHERO = "estadisticas";
    private static final String MSG_ERROR = "error";


    public void countsTransaction(String contador, String transEName, File file) {

        JSONParser jsonParser = new JSONParser();
        int value;
        org.json.JSONObject jObject = null;

        try {
            //se toma el archivo y se parseo
            Object object = jsonParser.parse(new FileReader(file));
            // se crea una variable de tipo JSONbject y se le pasa el objeto
            JSONObject json = (JSONObject) object;
            // se recorre el objeto en donde se obtiene el objeto de transaccion y se obtiene el contador del onjeto de transaccion
            jObject = new org.json.JSONObject(json.toString());
            value = jObject.getJSONObject(transEName).getInt(contador);
            // se le aumenta el valor a value que es el contador
            value++;
            // se obtiene el objeto transaccion y se actualiza el contador
            jObject.getJSONObject(transEName).put(contador, value);

        } catch (IOException | ParseException | JSONException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "ERROR " + e.getMessage());
        }

        //  se lee el archivo Json
        try (OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(file))) {
            //se escribe el objeto  Json
            if (jObject != null)
                fout.write(jObject.toString());
        } catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_ERROR + e);
        }
    }

    public void writeEstadisticas(String contador, String transEName) {

        File archivoJson = new File(Environment.getExternalStorageDirectory(), FICHERO + PAYUtils.getLocalDateAAAAMMDD() + ".json");

        if (!archivoJson.exists()) {

            JSONObject jsonObject = new JSONObject();
            JSONObject contadorJson = new JSONObject();
            //PBJETO CONTADORES
            contadorJson.put(TRANSSUCCESS, 0);
            contadorJson.put(TRANSFAIL, 0);
            contadorJson.put(TRANSTIMEOUT, 0);
            contadorJson.put(TRANSIMPRE, 0);

            jsonObject.put(Trans.RETIRO, contadorJson);
            jsonObject.put("DEPOSITO", contadorJson);
            jsonObject.put(Trans.CONSULTAS, contadorJson);
            jsonObject.put(Trans.GIROS, contadorJson);

            //  se lee el archivo Json
            try (OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(archivoJson))) {
                //se escribe el objeto  Json
                fout.write(jsonObject.toJSONString());
            } catch (Exception e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_ERROR + e);
            }
        }
        countsTransaction(contador, transEName, archivoJson);

    }
}
