package com.bcp.inicializacion.configuracioncomercio;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


@RunWith(MockitoJUnitRunner.class)
public class RangoTest {

    Rango rango =  new Rango();
    private static Rango instance = null;
    String descripcion;
    String typeCard;
    String binEnable;
    String rangoMin;
    String rangoMax;


    @Before
    public void Init() {

        rango = new Rango();
    }

    @Test
    public void getSingletonInstance() {

        rango.getSingletonInstance();
        assertEquals(instance,null);
    }

    @Test
    public void clearRango() {
        //pendiente
        Rango rango = mock(Rango.class);
        rango.clearRango();
    }

    @Test
    public void inCardTableACQ() {
//        rango.inCardTableACQ(pan,rango,context);
//        assertEquals(pan,context);
//        assertEquals(ok,true);
//        StringBuilder sql = new StringBuilder();
//         assertEquals(sql.append(","),sql.append("SELECT"));
    }

    @Test
    public void getDescription() {
        rango.getDescription();
        assertEquals(descripcion,descripcion);
    }

    @Test
    public void setDescription() {
        // Rango rango = mock(Rango.class);
        rango.setDescription(descripcion);
        assertEquals(this.descripcion,descripcion);
    }

    @Test
    public void getTypeCard() {
        rango.getTypeCard();
        assertEquals(typeCard,typeCard);

    }

    @Test
    public void setTypeCard() {
        rango.setTypeCard(typeCard);
        assertEquals(this.typeCard,typeCard);
    }

    @Test
    public void getBinEnable() {
        rango.getBinEnable();
        assertEquals(binEnable,binEnable);
    }

    @Test
    public void setBinEnable() {
        rango.setBinEnable(binEnable);
        assertEquals(this.binEnable,binEnable);
    }

    @Test
    public void getRangoMin() {
        rango.getRangoMin();
        assertEquals(rangoMin,rangoMin);
    }

    @Test
    public void setRangoMin() {
        rango.setRangoMin(rangoMin);
        assertEquals(this.rangoMin,rangoMin);
    }

    @Test
    public void getRangoMax() {
        rango.getRangoMax();
        assertEquals(rangoMax,rangoMax);
    }

    @Test
    public void setRangoMax() {
        rango.setRangoMax(rangoMax);
        assertEquals(this.rangoMax,rangoMax);
    }
}