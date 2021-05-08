package com.bcp.inicializacion.configuracioncomercio;

import android.content.Context;
import android.database.Cursor;

import com.bcp.inicializacion.sqlite.frompackage.DBHelper;
import com.bcp.inicializacion.tools.PolarisUtil;
import com.newpos.libpay.Logger;
import com.newpos.libpay.trans.Trans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckURL {

    private static CheckURL instance = null;
    private String urlToken;
    private static final String DEPOSITO = "DEPOSITO";

    protected CheckURL(){}

    public static CheckURL getSingletonInstance(){
        if (instance == null){
            instance = new CheckURL();
        }
        return instance;
    }

    public List<URL> selectURL(Context context, String idTransaction) {
        DBHelper databaseAccess = new DBHelper(context, PolarisUtil.NAME_DB, null, 1);
        databaseAccess.openDb(PolarisUtil.NAME_DB);

        List<URL> aLp = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        URL url = new URL();
        boolean urlNull = false;

        sql.append("SELECT ");
        int counter = 1;
        for (String s: url.getFields()) {
            sql.append(s);
            if (counter++ < url.getFields().length){
                sql.append(",");
            }
        }
        sql.append(" FROM URL");
        sql.append(" WHERE TRIM(ID_TRANSACCION) = ? ");
        sql.append(" OR ID_TRANSACCION = 'TOKEN' ");
        sql.append(" ORDER BY ");
        sql.append(" CAST(ORDEN AS INTEGER) ASC;");

        try {

            Cursor cursor = databaseAccess.rawQuery(sql.toString(), new String[]{idTransaction.replaceAll("[_]"," ")});

            cursor.moveToFirst();
            int indexColumn;
            while (!cursor.isAfterLast()) {
                url = new URL();
                url.clearURL();
                indexColumn = 0;
                for (String s : url.getFields()) {
                    if (cursor.getString(0).trim().equals(Trans.TOKEN)){
                        urlToken = cursor.getString(1).trim();
                    }else {
                        url.setURL(s, cursor.getString(indexColumn++).trim());
                    }
                }
                urlNull = true;
                if (!cursor.getString(0).trim().equals(Trans.TOKEN))
                    aLp.add(url);

                cursor.moveToNext();
            }
            cursor.close();

        } catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " checkUrl " + e.getMessage());
        }
        databaseAccess.closeDb();

        if (!urlNull)
            aLp = null;

        return aLp;
    }

    public List<URL> selectUrlTest(String transEname){

        URL[] url = new URL[6];
        switch (transEname){
            case Trans.RETIRO:
                url[0] = new URL();
                url[0].setUrlTest(Trans.RETIRO,"obtain_document","1");
                url[1] = new URL();
                url[1].setUrlTest(Trans.RETIRO,"validate_document","2");
                url[2] = new URL();
                url[2].setUrlTest(Trans.RETIRO,"list_product","3");
                url[3] = new URL();
                url[3].setUrlTest(Trans.RETIRO,"execute_transaction","4");
                url[4] = new URL();
                url[4].setUrlTest(Trans.RETIRO,"confirmVoucherPrint","5");
                url[5] = new URL();
                url[5].setUrlTest(Trans.RETIRO,"end-session","6");
                break;
            case DEPOSITO:
                url[0] = new URL();
                url[0].setUrlTest(DEPOSITO,"list_product_dep","1");
                url[1] = new URL();
                url[1].setUrlTest(DEPOSITO,"validate_account","2");
                url[2] = new URL();
                url[2].setUrlTest(DEPOSITO,"verify_deposit","3");
                url[3] = new URL();
                url[3].setUrlTest(DEPOSITO,"execute_transaction_dep","4");
                url[4] = new URL();
                url[4].setUrlTest(DEPOSITO,"confirmVoucherPrint_dep","5");
                url[5] = new URL();
                url[5].setUrlTest(DEPOSITO,"end-session","6");
                break;
            case Trans.CONSULTAS:
                url = new URL[4];
                url[0] = new URL();
                url[0].setUrlTest(Trans.CONSULTAS,"list_products_cons","1");
                url[1] = new URL();
                url[1].setUrlTest(Trans.CONSULTAS,"execute_transaction_cons","2");
                url[2] = new URL();
                url[2].setUrlTest(Trans.CONSULTAS,"confirm_voucher_print_cons","3");
                url[3] = new URL();
                url[3].setUrlTest(Trans.CONSULTAS,"end-session","4");
                break;
            case Trans.GIROS_COBROS:
                url = new URL[5];
                url[0] = new URL();
                url[0].setUrlTest(Trans.GIROS,"verify-bank-draft-withdrawal","1");
                url[1] = new URL();
                url[1].setUrlTest(Trans.GIROS,"list-products","2");
                url[2] = new URL();
                url[2].setUrlTest(Trans.GIROS,"execute-transaction","3");
                url[3] = new URL();
                url[3].setUrlTest(Trans.GIROS,"confirm-voucher-print","4");
                url[4] = new URL();
                url[4].setUrlTest(Trans.GIROS,"end-session","5");
                break;
            case Trans.GIROS_EMISION:
                url[0] = new URL();
                url[0].setUrlTest(Trans.GIROS,"list-products-emision","1");
                url[1] = new URL();
                url[1].setUrlTest(Trans.GIROS,"obtain-person-emision","2");
                url[2] = new URL();
                url[2].setUrlTest(Trans.GIROS,"verify-bank-draft-issue-emision","3");
                url[3] = new URL();
                url[3].setUrlTest(Trans.GIROS,"execute-transaction-emision","4");
                url[4] = new URL();
                url[4].setUrlTest(Trans.GIROS,"confirm-voucher-print-emision","5");
                url[5] = new URL();
                url[5].setUrlTest(Trans.GIROS,"end-session","6");
                break;
            case "SETTLE":
                url = new URL[2];
                url[0] = new URL();
                url[0].setUrlTest("SETTLE","resumen_trans","1");
                url[1] = new URL();
                url[1].setUrlTest("SETTLE","end-session","2");
                break;
            case Trans.PAGOSERVICIOS:
                url = new URL[12];
                url[0] = new URL();
                url[0].setUrlTest(Trans.PAGOSERVICIOS,"list-services","1");
                url[1] = new URL();
                url[1].setUrlTest(Trans.PAGOSERVICIOS,"list-client-debts","2");
                url[2] = new URL();
                url[2].setUrlTest(Trans.PAGOSERVICIOS,"list-service-documents","3");
                url[3] = new URL();
                url[3].setUrlTest(Trans.PAGOSERVICIOS,"list-service-imports","4");
                url[4] = new URL();
                url[4].setUrlTest(Trans.PAGOSERVICIOS,"obtain-amount-limits","5");
                url[5] = new URL();
                url[5].setUrlTest(Trans.PAGOSERVICIOS,"obtain-document-type","6");
                url[6] = new URL();
                url[6].setUrlTest(Trans.PAGOSERVICIOS,"validate-document-number","7");
                url[7] = new URL();
                url[7].setUrlTest(Trans.PAGOSERVICIOS,"list-products","8");
                url[8] = new URL();
                url[8].setUrlTest(Trans.PAGOSERVICIOS,"verify-payment","9");
                url[9] = new URL();
                url[9].setUrlTest(Trans.PAGOSERVICIOS,"execute-transaction","10");
                url[10] = new URL();
                url[10].setUrlTest(Trans.PAGOSERVICIOS,"confirm-voucher-print","11");
                url[11] = new URL();
                url[11].setUrlTest(Trans.PAGOSERVICIOS,"end-session","12");


                break;
            case Trans.ULT_OPERACIONES:
                url = new URL[3];
                url[0] = new URL();
                url[0].setUrlTest(Trans.ULT_OPERACIONES,"view_last_operations", "1");
                url[1] = new URL();
                url[1].setUrlTest(Trans.ULT_OPERACIONES,"confirmVoucherPrint_ultimas","2");
                url[2] = new URL();
                url[2].setUrlTest(Trans.ULT_OPERACIONES, "end-session", "3");
                break;
            default:
                url = new URL[0];
                break;
        }

        urlToken = "token";

        return new ArrayList<>(Arrays.asList(url));
    }

    public String getUrlToken() {
        return urlToken;
    }
}
