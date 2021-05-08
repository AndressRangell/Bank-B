package com.bcp.inicializacion.configuracioncomercio;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bcp.inicializacion.sqlite.frompackage.DBHelper;
import com.bcp.inicializacion.tools.PolarisUtil;
import com.newpos.libpay.Logger;

import java.util.ArrayList;
import java.util.List;

public class CheckCategory {

    private static  CheckCategory instance = null;

    protected CheckCategory(){}

    public static CheckCategory getSingletonInstance(){
        if (instance == null)
            instance = new CheckCategory();
        else
            Log.d("Category","No se puede crear objeto category, ya exite");

        return instance;
    }

    public List<Category> selectCategories(Context context){
        DBHelper databaseAccess = new DBHelper(context, PolarisUtil.NAME_DB, null, 1);
        databaseAccess.openDb(PolarisUtil.NAME_DB);

        List<Category> alp = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Category category;
        boolean categoryNull = false;

        sql.append("SELECT ");
        sql.append("*");
        sql.append(" FROM CATEGORIA");

        try{
            Cursor cursor = databaseAccess.rawQuery(sql.toString());
            cursor.moveToFirst();
            int indexColumn;
            while (!cursor.isAfterLast()) {
                category = new Category();
                category.clearCategory();
                indexColumn = 0;
                for (String s : category.getFields()) {
                    category.setCategory(s, cursor.getString(indexColumn++).trim());
                }
                categoryNull = true;
                alp.add(category);

                cursor.moveToNext();
            }
            cursor.close();

        }catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }

        databaseAccess.closeDb();

        if (!categoryNull)
            alp = null;

        return alp;

    }
}
