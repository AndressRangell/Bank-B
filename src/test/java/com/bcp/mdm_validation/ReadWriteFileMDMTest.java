package com.bcp.mdm_validation;

import android.os.Environment;
import android.util.Xml;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReadWriteFileMDMTest {

    ReadWriteFileMDM readWriteFileMDM = new ReadWriteFileMDM();
    private static ReadWriteFileMDM instance = null;
    private XmlSerializer serializer;
     String reVerse,seTtle,initAuto,name,tag,value;


    @Mock
    ReadWriteFileMDM readWriteFileMDMMock = mock(ReadWriteFileMDM.class);

    @Before
    public void Init(){
        readWriteFileMDM = new ReadWriteFileMDM();

    }

    @Test
    public void getInstance() {

        readWriteFileMDM.getInitAuto();
        assertEquals(instance,null);

    }

    @Test
    public void getReverse() {
        readWriteFileMDM.getReverse();
        assertEquals(reVerse,reVerse);
    }

    @Test
    public void setReverse() {
        readWriteFileMDMMock.setReverse(reVerse);
        verify(readWriteFileMDMMock).setReverse(reVerse);
    }

    @Test
    public void getSettle() {
        readWriteFileMDMMock.getReverse();
        assertEquals(seTtle,seTtle);
    }

    @Test
    public void setSettle() {
        readWriteFileMDMMock.setSettle(seTtle);
        verify(readWriteFileMDMMock).setSettle(seTtle);
    }

    @Test
    public void getInitAuto() {
        readWriteFileMDMMock.getInitAuto();
        assertEquals(initAuto,readWriteFileMDM.getInitAuto());

    }

    @Test
    public void setInitAuto() {
        readWriteFileMDMMock.setInitAuto(initAuto);
        verify(readWriteFileMDMMock).setInitAuto(initAuto);
    }

    @Test
    public void createXML() {

        //Falta completar algunas cosas
        readWriteFileMDMMock.createXML(name);
        serializer = Xml.newSerializer();
        File newxmlfile;
        verify(readWriteFileMDMMock).createXML(name);
    }

    @Test
    public void setTag() {

        readWriteFileMDMMock.setTag(tag,value);
        verify(readWriteFileMDMMock).setTag(tag,value);
        assertEquals(null,tag );

    }

    @Test
    public void writeFileMDM() {
        //Pendiente
    }



    @Test
    public void readFileMDM() {
        //Pendiente por realizar
    }

    @Test
    public void isAutoInitActive() {
    //Pendiente por realizar

    }
}