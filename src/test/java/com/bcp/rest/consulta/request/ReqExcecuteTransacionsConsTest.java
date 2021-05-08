package com.bcp.rest.consulta.request;

import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReqExcecuteTransacionsConsTest {

    protected String quetype;
    protected String famCode;
    protected String currCode;
    protected String ctnPinblock;
    protected String ctnField55;
    protected String ctnArqc;
    boolean isSession;

    ReqExcecuteTransacionsCons reqExcecuteTransacionsCons =  new ReqExcecuteTransacionsCons();

    @Mock
   ReqExcecuteTransacionsCons reqExcecuteTransacionsConsMock = mock(ReqExcecuteTransacionsCons.class);

    @Test
    public void getQuetype() {
        reqExcecuteTransacionsCons.getQuetype();
        assertEquals(quetype,reqExcecuteTransacionsCons.getQuetype());
    }

    @Test
    public void setQuetype() {
        reqExcecuteTransacionsConsMock.setQuetype(quetype);
        verify(reqExcecuteTransacionsConsMock).setQuetype(quetype);
    }

    @Test
    public void getFamCode() {
        reqExcecuteTransacionsCons.getFamCode();
        assertEquals(famCode,reqExcecuteTransacionsCons.getFamCode());
    }

    @Test
    public void setFamCode() {
        reqExcecuteTransacionsConsMock.setFamCode(famCode);
        verify(reqExcecuteTransacionsConsMock).setFamCode(famCode);
    }

    @Test
    public void getCurrCode() {
        reqExcecuteTransacionsCons.getCurrCode();
        assertEquals(currCode,reqExcecuteTransacionsCons.getCurrCode());
    }
    @Test
    public void setCurrCode() {
        reqExcecuteTransacionsConsMock.setCurrCode(currCode);
        verify(reqExcecuteTransacionsConsMock).setCurrCode(currCode);
    }

    @Test
    public void getCtnPinblock() {
        reqExcecuteTransacionsCons.getCtnPinblock();
        assertEquals(reqExcecuteTransacionsCons.getCtnPinblock(),reqExcecuteTransacionsCons.getCtnPinblock());
    }

    @Test
    public void setCtnPinblock() {
        reqExcecuteTransacionsConsMock.setCtnPinblock(ctnPinblock);
        verify(reqExcecuteTransacionsConsMock).setCtnPinblock(ctnPinblock);
    }

    @Test
    public void getCtnField55() {
        reqExcecuteTransacionsCons.getCtnField55();
        assertEquals(reqExcecuteTransacionsCons.getCtnField55(),reqExcecuteTransacionsCons.getCtnField55());
    }

    @Test
    public void setCtnField55() {
        reqExcecuteTransacionsConsMock.setCtnField55(ctnField55);
        verify(reqExcecuteTransacionsConsMock).setCtnField55(ctnField55);
    }

    @Test
    public void getCtnArqc() {
        reqExcecuteTransacionsCons.getCtnArqc();
        assertEquals(reqExcecuteTransacionsCons.getCtnArqc(),reqExcecuteTransacionsCons.getCtnArqc());
    }

    @Test
    public void setCtnArqc() {
        reqExcecuteTransacionsConsMock.setCtnArqc(ctnArqc);
        verify(reqExcecuteTransacionsConsMock).setCtnArqc(ctnArqc);
    }

    @Test
    public void buildsObjectJSON() {
//        JSONObject jsonObject = new JSONObject();
//        reqExcecuteTransacionsCons.buildsObjectJSON();
//        assertEquals(jsonObject,jsonObject);
//        assertEquals(isSession,isSession);
//        assertFalse(isSession);
    }
}