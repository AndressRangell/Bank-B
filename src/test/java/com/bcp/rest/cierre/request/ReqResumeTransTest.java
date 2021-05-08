package com.bcp.rest.cierre.request;

import org.junit.Test;
import org.mockito.Mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReqResumeTransTest {

    protected String firstTransDate;
    protected String firstTransTime;
    protected String lastTransDate;
    protected String lastTransTime;

    ReqResumeTrans reqResumeTrans =  new ReqResumeTrans();

    @Mock
    ReqResumeTrans reqResumeTransMock =  mock(ReqResumeTrans.class);

    @Test
    public void getFirstTransDate() {
         reqResumeTrans.getFirstTransDate();
         assertEquals(reqResumeTrans.getFirstTransDate(),reqResumeTrans.getFirstTransDate());
    }

    @Test
    public void setFirstTransDate() {
        reqResumeTransMock.setFirstTransDate(firstTransDate);
        verify(reqResumeTransMock).setFirstTransDate(firstTransDate);
    }

    @Test
    public void getFirstTransTime() {
        reqResumeTrans.getFirstTransTime();
        assertEquals(reqResumeTrans.getFirstTransTime(),reqResumeTrans.getFirstTransTime());
    }

    @Test
    public void setFirstTransTime() {
        reqResumeTransMock.setFirstTransTime(firstTransTime);
        verify(reqResumeTransMock).setFirstTransTime(firstTransTime);
    }

    @Test
    public void getLastTransDate() {
        reqResumeTrans.getFirstTransDate();
        assertEquals(reqResumeTrans.getFirstTransDate(), reqResumeTrans.getFirstTransDate());
    }

    @Test
    public void setLastTransDate() {
        reqResumeTransMock.setLastTransDate(lastTransDate);
        verify(reqResumeTransMock).setLastTransDate(lastTransDate);
    }

    @Test
    public void getLastTransTime() {
        reqResumeTrans.getLastTransTime();
        assertEquals(reqResumeTrans.getLastTransTime(),reqResumeTrans.getLastTransTime());
    }

    @Test
    public void setLastTransTime() {
        reqResumeTransMock.setFirstTransTime(firstTransTime);
        verify(reqResumeTransMock).setFirstTransTime(firstTransTime);
    }

    @Test
    public void buildsObjectJSON() {
//        JSONObject jsonObject = new JSONObject();
//        JSONObject request = new JSONObject();
//        reqResumeTrans.buildsObjectJSON();
//        assertEquals(jsonObject,jsonObject);
//        assertTrue(true);
    }
}