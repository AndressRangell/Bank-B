package com.bcp.inicializacion.configuracioncomercio;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bcp.inicializacion.sqlite.frompackage.DBHelper;
import com.bcp.inicializacion.tools.PolarisUtil;
import com.newpos.libpay.Logger;
import java.util.ArrayList;
import java.util.List;

public class CheckCredentials {

    private static CheckCredentials instance = null;

    public static CheckCredentials getSingletonInstance(){
        if (instance == null){
            instance = new CheckCredentials();
        }else {
            Log.d("CREDENTIALS","No se puede crear este objeto, ya existe");
        }
        return instance;
    }

    public List<Credentials> selectCredentials(Context context){
        DBHelper databaseAccess = new DBHelper(context, PolarisUtil.NAME_DB, null, 1);
        databaseAccess.openDb(PolarisUtil.NAME_DB);

        List<Credentials> aLp = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Credentials credentials = new Credentials();
        boolean credentialNull = false;

        sql.append("select ");
        int counter = 1;
        for (String s : credentials.getFields()) {
            sql.append(s);
            if (counter++ < credentials.getFields().length) {
                sql.append(",");
            }
        }
        sql.append(" from CREDENCIALS");

        try {

            Cursor cursor = databaseAccess.rawQuery(sql.toString());

            cursor.moveToFirst();
            int indexColumn;

            while (!cursor.isAfterLast()) {

                credentials = new Credentials();
                credentials.clearCredentials();
                indexColumn = 0;

                for (String s : credentials.getFields()) {
                    credentials.setCredentials(s, cursor.getString(indexColumn++).trim());
                }

                credentialNull = true;
                aLp.add(credentials);

                cursor.moveToNext();
            }
            cursor.close();

        } catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " configComercio " + e.getMessage());
        }
        databaseAccess.closeDb();

        if (!credentialNull)
            aLp = null;

        return aLp;
    }
}
