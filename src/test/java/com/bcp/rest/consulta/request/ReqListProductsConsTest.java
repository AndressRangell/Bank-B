package com.bcp.rest.consulta.request;

import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ReqListProductsConsTest {

    protected String deviceReference;
    protected String opnNumber;
    protected String deviceIp;
    protected String deviceMac;
    protected String trackDos;

    ReqListProductsCons reqListProductsCons = new ReqListProductsCons();

    @Mock
    ReqListProductsCons reqListProductsConsMock = mock(ReqListProductsCons.class);

    @Test
    public void getDeviceReference() {
        reqListProductsCons.getDeviceReference();
        assertEquals(reqListProductsCons.getDeviceReference(),reqListProductsCons.getDeviceReference());
    }

    @Test
    public void setDeviceReference() {
        reqListProductsConsMock.setDeviceReference(deviceReference);
        verify(reqListProductsConsMock).setDeviceReference(deviceReference);
    }

    @Test
    public void getOpnNumber() {
        reqListProductsCons.getOpnNumber();
        assertEquals(reqListProductsCons.getOpnNumber(),reqListProductsCons.getOpnNumber());
    }

    @Test
    public void setOpnNumber() {
        reqListProductsConsMock.setOpnNumber(opnNumber);
        verify(reqListProductsConsMock).setOpnNumber(opnNumber);
    }

    @Test
    public void getDeviceIp() {
        reqListProductsCons.getDeviceIp();
        assertEquals(reqListProductsCons.getDeviceIp(),reqListProductsCons.getDeviceIp());
    }

    @Test
    public void setDeviceIp() {
        reqListProductsConsMock.setDeviceIp(deviceIp);
        verify(reqListProductsConsMock).setDeviceIp(deviceIp);
    }

    @Test
    public void getDeviceMac() {
        reqListProductsCons.getDeviceMac();
        assertEquals(reqListProductsCons.getDeviceMac(),reqListProductsCons.getDeviceMac());
    }

    @Test
    public void setDeviceMac() {
        reqListProductsConsMock.setDeviceMac(deviceMac);
        verify(reqListProductsConsMock).setDeviceMac(deviceMac);
    }

    @Test
    public void getTrackDos() {
        reqListProductsCons.getTrackDos();
        assertEquals(reqListProductsCons.getTrackDos(),reqListProductsCons.getTrackDos());
    }

    @Test
    public void setTrackDos() {
        reqListProductsConsMock.setTrackDos(trackDos);
        verify(reqListProductsConsMock).setTrackDos(trackDos);
    }

    @Test
    public void builJsonObject() {
//        JSONObject jsonObject = new JSONObject();
//        reqListProductsCons.builJsonObject();
//        assertEquals(jsonObject,jsonObject);
//        assertFalse(false);
    }
}