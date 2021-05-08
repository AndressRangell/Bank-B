package com.bcp.inicializacion.configuracioncomercio;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bcp.inicializacion.sqlite.frompackage.DBHelper;
import com.bcp.inicializacion.tools.PolarisUtil;
import com.newpos.libpay.Logger;

import java.util.ArrayList;
import java.util.List;

public class CheckCompanny {

    private static CheckCompanny instance = null;

    protected CheckCompanny(){}

    public static CheckCompanny getSingletonInstance(){
        if (instance == null)
            instance = new CheckCompanny();
        else
            Log.d("Companny", "No se puede crear otro objeto checkCompanny, ya existe");

        return instance;
    }

    public List<Companny> selectCompannyForCategory(Context context, String category) {
        DBHelper databaseAccess = new DBHelper(context, PolarisUtil.NAME_DB, null, 1);
        databaseAccess.openDb(PolarisUtil.NAME_DB);

        List<Companny> aLp = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Companny companny;
        boolean compannyNull = false;

        sql.append("SELECT ");
        sql.append("*");
        sql.append(" FROM EMPRESA");
        if (category != null)
            sql.append(" WHERE TRIM(ID_CATEGORIA) = ? ");

        try {
            Cursor cursor;

            if (category != null)
                cursor = databaseAccess.rawQuery(sql.toString(), new String[]{category});
            else
                cursor = databaseAccess.rawQuery(sql.toString());

            cursor.moveToFirst();
            int indexColumn;
            while (!cursor.isAfterLast()) {
                companny = new Companny();
                companny.clearCompany();
                indexColumn = 0;
                for (String s : companny.getFields()) {
                    companny.setCompanny(s, cursor.getString(indexColumn++).trim());
                }
                compannyNull = true;
                aLp.add(companny);

                cursor.moveToNext();
            }
            cursor.close();

        } catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
        databaseAccess.closeDb();

        if (!compannyNull)
            aLp = null;

        return aLp;
    }
}
