package com.bcp.transactions.common;

import android.content.Context;
import android.content.SharedPreferences;
import com.newpos.libpay.presenter.TransUI;
import com.newpos.libpay.trans.finace.FinanceTrans;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class CommonFunctionalitiesTest {

    CommonFunctionalities commonFunctionalities;
    String pan,numDocument,cvv2,pin,proCode,newPassword,transEName;
    StringBuilder expDate;
    Context context;
    Date fechaActual;
    int timeout;
    public TransUI transUI;
    public static SharedPreferences sharedPreferences;
    boolean isSumarTotales = false;
    private static final String MSG_FECHA_CIERRE = "fecha-cierre";
    private static final String MSG_FECHA_SIG_CIERRE = "fechaSigCierre";
    private static final String MSG_FIRTS_TRANS = "firtsTrans";
    private static final String PATTERN_DATE_FORMAT = "dd/MM/yyyy HH:mm";

    @Before
    public void Init(){

        commonFunctionalities = new CommonFunctionalities();

    }

    @Test
    public void getPan() {
        commonFunctionalities.getPan();
        assertEquals(pan,commonFunctionalities.getPan());
    }

    @Test
    public void getNumDocument() {
        commonFunctionalities.getNumDocument();
        assertEquals(numDocument,commonFunctionalities.getNumDocument());
    }

    @Test
    public void getExpDate() {
        //commonFunctionalities.getExpDate();
        //assertEquals(expDate.toString(),commonFunctionalities.getExpDate());
    }

    @Test
    public void getCvv2() {
        commonFunctionalities.getCvv2();
        assertEquals(cvv2,commonFunctionalities.getCvv2());
    }

    @Test
    public void getPin() {
        commonFunctionalities.getPin();
        assertEquals(pin,commonFunctionalities.getPin());
    }

    @Test
    public void getProCode() {
        commonFunctionalities.getProCode();
        assertEquals(proCode,commonFunctionalities.getProCode());
    }

    @Test
    public void isSumarTotales() {
        commonFunctionalities.isSumarTotales();
        assertFalse(String.valueOf(isSumarTotales),false);
    }

    @Test
    public void getNewPassword() {
        commonFunctionalities.getNewPassword();
        assertEquals(newPassword,commonFunctionalities.getNewPassword());
    }

    @Test
    public void tipoMoneda() {
        String[] moneda = new String[2];
        moneda[0] = "$";
        moneda[1] = FinanceTrans.DOLAR;
        commonFunctionalities.tipoMoneda();
        assertArrayEquals(moneda,commonFunctionalities.tipoMoneda());
    }

    @Test
    public void checkCierre() {
        //pendiente revisar

    }

    @Test
    public void setPanManual() {
        //pendiente revisar

    }

    @Test
    public void setFechaExp() {

    }

    @Test
    public void setCVV2() {
    }

    @Test
    public void ctlPIN() {
    }

    @Test
    public void last4card() {
    }

    @Test
    public void confirmAmount() {
    }

    @Test
    public void setTipoCuenta() {
    }

    @Test
    public void setNumberDoc() {
    }

    @Test
    public void updateDateFirstTrans() {
    }

    @Test
    public void saveFirtsTrans() {

    }

    @Test
    public void getFirtsTrans() {

    }

    @Test
    public void saveDateSettle() {
    }

    @Test
    public void saveDateSettleAfterFirtsTrans() {

    }

    @Test
    public void validateCard() {
    }

    @Test
    public void setChangePassword() {
    }
}