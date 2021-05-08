package com.bcp.inicializacion.configuracioncomercio;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.bcp.inicializacion.sqlite.frompackage.DBHelper;
import com.bcp.inicializacion.tools.PolarisUtil;
import com.newpos.libpay.Logger;

import java.util.ArrayList;
import java.util.List;

public class CheckTransactions {

    private static CheckTransactions instance = null;

    protected CheckTransactions(){}

    public static CheckTransactions getSingletonInstance(){
        if (instance == null){
            instance = new CheckTransactions();
        }else{
            Log.d("Transaction", "No se puede crear otro objeto, ya existe");
        }
        return instance;
    }

    public Transaction selectTransaction(Context context, String idTransaction){
        DBHelper databaseAccess = new DBHelper(context, PolarisUtil.NAME_DB, null, 1);
        databaseAccess.openDb(PolarisUtil.NAME_DB);
        Transaction transaction = new Transaction();

        StringBuilder sql = new StringBuilder();

        sql.append("select ");
        int counter = 1;
        for (String s : transaction.fields) {
            sql.append(s);
            if (counter++ < transaction.fields.length) {
                sql.append(",");
            }
        }
        sql.append(" from TRANSACCIONES ");
        sql.append(" WHERE instr(trim(ID_TRANSACCION), ?)");
        sql.append(" >0;");

        try {

            Cursor cursor = databaseAccess.rawQuery(sql.toString(), new String[]{idTransaction.replaceAll("[_]"," ")});

            cursor.moveToFirst();
            int indexColumn;
            transaction.clearTransaction();
            while (!cursor.isAfterLast()) {
                indexColumn = 0;
                for (String s : transaction.fields) {
                    transaction.setTransaction(s, cursor.getString(indexColumn++).trim());
                }
                cursor.moveToNext();
            }
            cursor.close();

        } catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " checkTransaction " + e.getMessage());
        }
        databaseAccess.closeDb();
        return transaction;
    }

    public List<Transaction> selectTransEnable(Context context, String isEnable) {
        DBHelper databaseAccess = new DBHelper(context, PolarisUtil.NAME_DB, null, 1);
        databaseAccess.openDb(PolarisUtil.NAME_DB);

        List<Transaction> aLp = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Transaction trans;
        boolean transNull = false;

        sql.append("SELECT ");
        sql.append("*");
        sql.append(" FROM TRANSACCIONES");
        sql.append(" WHERE TRIM(HABILITAR) = ? ");

        try {

            Cursor cursor = databaseAccess.rawQuery(sql.toString(), new String[]{isEnable});

            cursor.moveToFirst();
            int indexColumn;
            while (!cursor.isAfterLast()) {
                trans = new Transaction();
                trans.clearTransaction();
                indexColumn = 0;
                for (String s : trans.getFields()) {
                    trans.setTransaction(s, cursor.getString(indexColumn++).trim());
                }
                transNull = true;
                aLp.add(trans);

                cursor.moveToNext();
            }
            cursor.close();

        } catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " checkTransaction 2 " + e.getMessage());
        }
        databaseAccess.closeDb();

        if (!transNull)
            aLp = null;

        return aLp;
    }
}
