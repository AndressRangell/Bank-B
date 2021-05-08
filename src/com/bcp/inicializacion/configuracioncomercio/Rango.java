package com.bcp.inicializacion.configuracioncomercio;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.bcp.inicializacion.sqlite.frompackage.DBHelper;
import com.bcp.inicializacion.tools.PolarisUtil;
import com.newpos.libpay.Logger;

public class Rango {
    private String description;
    private String typeCard;
    private String binEnable;
    private String rangoMin;
    private String rangoMax;

    private static Rango instance = null;

    private static final String[] FIELDS = new String[]{
            "LIMITE_INFERIOR",
            "LIMITE_SUPERIOR",
            "ESTADO",
            "DESCRIPCION",
            "TIPO"
    };

    protected Rango(){

    }

    public static Rango getSingletonInstance(){
        if (instance == null){
            instance = new Rango();
        }else{
            Log.d("Rango", "No se puede crear otro objeto, ya existe");
        }
        return instance;
    }

    private void setRango(String column, String value){
        switch (column){
            case "LIMITE_INFERIOR":
                setRangoMin(value);
                break;
            case "LIMITE_SUPERIOR":
                setRangoMax(value);
                break;
            case "ESTADO":
                setBinEnable(value);
                break;
            case "DESCRIPCION":
                setDescription(value);
                break;
            case "TIPO":
                setTypeCard(value);
                break;
            default:
                break;
        }
    }

    public void clearRango() {
        for (String s : Rango.FIELDS) {
            setRango(s, "");
        }
    }

    public static boolean inCardTableACQ(String pan,Rango cardRow, Context context) {
        boolean ok = false;
        try {
            if (cardRow.selectcardRow(pan, context)) {
                ok = true;
            }
        } catch (Exception ex) {
            Logger.logLine(Logger.LOG_EXECPTION, ex.getStackTrace(), " Rango " +  ex.getMessage());
            return false;
        }
        return ok;
    }

    private boolean selectcardRow(String pan, Context context) {
       boolean ok = false;
        DBHelper databaseAccess = new DBHelper(context, PolarisUtil.NAME_DB, null, 1);
        databaseAccess.openDb(PolarisUtil.NAME_DB);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        int counter = 1;
        for (String s : Rango.FIELDS) {
            sql.append(s);
            if (counter++ < Rango.FIELDS.length) {
                sql.append(",");
            }
        }

        sql.append(" from CARDS where ");
        sql.append("(cast(LIMITE_INFERIOR as integer) <= cast(substr('");
        sql.append(pan);
        sql.append("',0,LENGTH(TRIM(LIMITE_INFERIOR))+1) as integer) and cast(LIMITE_SUPERIOR as integer) >= cast(substr('");  // se valida solo el bin de la tarjeta MO
        sql.append(pan);
        sql.append("',0,LENGTH(TRIM(LIMITE_SUPERIOR))+1) as integer)) ");
        sql.append("order by cast(LIMITE_SUPERIOR as integer) - cast(LIMITE_INFERIOR as integer) asc limit 1;");

        try {

            Cursor cursor = databaseAccess.rawQuery(sql.toString());

            cursor.moveToFirst();
            int indexColumn;

            while (!cursor.isAfterLast()){
                clearRango();
                indexColumn = 0;
                for (String s : Rango.FIELDS) {
                    setRango(s, cursor.getString(indexColumn++).trim());
                }
                ok = true;
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "error " + e.getMessage());
        }
        databaseAccess.closeDb();
        return ok;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeCard() {
        return typeCard;
    }

    public void setTypeCard(String typeCard) {
        this.typeCard = typeCard;
    }

    public String getBinEnable() {
        return binEnable;
    }

    public void setBinEnable(String binEnable) {
        this.binEnable = binEnable;
    }

    public String getRangoMin() {
        return rangoMin;
    }

    public void setRangoMin(String rangoMin) {
        this.rangoMin = rangoMin;
    }

    public String getRangoMax() {
        return rangoMax;
    }

    public void setRangoMax(String rangoMax) {
        this.rangoMax = rangoMax;
    }
}
