package com.bcp.transactions.deposito;

import android.content.Context;

import com.newpos.libpay.helper.iso8583.ISO8583;
import com.newpos.libpay.trans.Trans;
import com.newpos.libpay.trans.TransInputPara;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DepositoTest {

  Deposito deposito;
   ISO8583 iso8583;
  @Mock
    Deposito depositomock = mock(Deposito.class);
    Context ctx;
    String transEname;
    TransInputPara p;

    @Before
    public void  Init(){
     //deposito = new Deposito(ctx,transEname,p);
    }

    @Test
    public void start() {
       // depositomock.start();
       // verify(depositomock).start();
    }

    @Test
    public void getISO8583() {
     // deposito.getISO8583();
     // assertEquals(deposito.getISO8583(),deposito.getISO8583());

    }
}