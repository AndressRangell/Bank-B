package com.newpos.libpay.utils;

import org.junit.Before;
import static org.junit.Assert.*;

public class ISOUtilTest {

     ISOUtil isoUtil;

    @Before
    public void Init()  {
        isoUtil = new ISOUtil();
    }

    @org.junit.Test
    public void padleft() {

        assertEquals("x00012",isoUtil.padleft("00012",6,'x'));
        assertEquals("xxxxxx",isoUtil.padleft("",6,'x'));
        assertEquals(null,isoUtil.padleft("",-6,'x'));
        assertNotEquals("x00012",isoUtil.padleft("00012",3,'x'));

    }

    @org.junit.Test
    public void padright() {

        assertEquals("00012x",isoUtil.padright("00012",6,'x'));
        assertEquals("txxxxx",isoUtil.padright("t",6,'x'));
        assertNotEquals("x12",isoUtil.padright("00012",3,'x'));
    }

    @org.junit.Test
    public void trim() {

        assertEquals("tarjeta_arriba",isoUtil.trim("tarjeta_arriba    "));
        assertEquals("tarjeta_arriba",isoUtil.trim("    tarjeta_arriba"));
        assertEquals("",isoUtil.trim("    "));
        assertEquals(null,isoUtil.trim(null));
    }

    @org.junit.Test
    public void zeropad() {

        assertEquals("000pruebas",isoUtil.zeropad("pruebas",10));
        assertNotEquals("pruebas00",isoUtil.zeropad("pruebas",9));
        assertEquals(null,isoUtil.zeropad("pruebas",4));
        assertEquals("000",isoUtil.zeropad("",3));

        assertEquals("000",isoUtil.zeropad(0,3));
        assertEquals("030",isoUtil.zeropad(30,3));
        assertEquals("134",isoUtil.zeropad(134,3));
    }

    @org.junit.Test
    public void testZeropad() {

    }


    @org.junit.Test
    public void strpad() {
        assertEquals("pruebas   ",isoUtil.strpad("pruebas",10));
        assertNotEquals("pruebas",isoUtil.strpad("pruebas",10));
        assertEquals("pruebas   ",isoUtil.strpad("pruebas",10));
        assertEquals("   ",isoUtil.strpad("",3));
    }

    @org.junit.Test
    public void zeropadRight() {

        assertEquals("0000",isoUtil.zeropadRight("",4));
        assertEquals("prueba000",isoUtil.zeropadRight("prueba000",9));
        assertEquals("pruebas",isoUtil.zeropadRight("pruebas",4));
        assertEquals("0000",isoUtil.zeropadRight("",4));
    }

    @org.junit.Test
    public void str2bcd() {
    }

    @org.junit.Test
    public void testStr2bcd() {
    }

    @org.junit.Test
    public void testStr2bcd1() {
    }

    @org.junit.Test
    public void bcd2str() {
    }

    @org.junit.Test
    public void hexString() {
    }

    @org.junit.Test
    public void testHexString() {
    }

    @org.junit.Test
    public void bitSet2String() {
    }

    @org.junit.Test
    public void bitSet2byte() {
    }

    @org.junit.Test
    public void testBitSet2byte() {
    }

    @org.junit.Test
    public void bitSet2Int() {
    }

    @org.junit.Test
    public void int2BitSet() {
    }

    @org.junit.Test
    public void testInt2BitSet() {
    }

    @org.junit.Test
    public void byte2BitSet() {
    }

    @org.junit.Test
    public void testByte2BitSet() {
    }

    @org.junit.Test
    public void testByte2BitSet1() {
    }

    @org.junit.Test
    public void hex2BitSet() {
    }

    @org.junit.Test
    public void testHex2BitSet() {
    }

    @org.junit.Test
    public void testHex2BitSet1() {
    }

    @org.junit.Test
    public void hex2byte() {
    }

    @org.junit.Test
    public void testHex2byte() {
    }

    @org.junit.Test
    public void byte2hex() {
    }

    @org.junit.Test
    public void int2byte() {
    }

    @org.junit.Test
    public void byte2int() {
    }

    @org.junit.Test
    public void testByte2hex() {
    }

    @org.junit.Test
    public void formatAmount() {
    }

    @org.junit.Test
    public void toIntArray() {
    }

    @org.junit.Test
    public void toStringArray() {
    }

    @org.junit.Test
    public void xor() {
    }

    @org.junit.Test
    public void hexor() {
    }

    @org.junit.Test
    public void testTrim() {
    }

    @org.junit.Test
    public void concat() {
    }

    @org.junit.Test
    public void testConcat() {
    }

    @org.junit.Test
    public void zeroUnPad() {
    }

    @org.junit.Test
    public void blankUnPad() {
    }

    @org.junit.Test
    public void unPadRight() {
    }

    @org.junit.Test
    public void unPadLeft() {
    }

    @org.junit.Test
    public void isZero() {
    }

    @org.junit.Test
    public void isBlank() {
    }

    @org.junit.Test
    public void isAlphaNumeric() {
    }

    @org.junit.Test
    public void isNumeric() {
    }

    @org.junit.Test
    public void bitSet2extendedByte() {
    }

    @org.junit.Test
    public void hexdump() {
    }

    @org.junit.Test
    public void testHexdump() {
    }

    @org.junit.Test
    public void testHexdump1() {
    }

    @org.junit.Test
    public void strpadf() {
    }

    @org.junit.Test
    public void trimf() {
    }

    @org.junit.Test
    public void takeLastN() {
    }

    @org.junit.Test
    public void takeFirstN() {
    }

    @org.junit.Test
    public void millisToString() {
    }

    @org.junit.Test
    public void int2bcd() {
    }

    @org.junit.Test
    public void testByte2int() {
    }

    @org.junit.Test
    public void bcd2int() {
    }

    @org.junit.Test
    public void testBcd2int() {
    }

    @org.junit.Test
    public void memcmp() {
    }

    @org.junit.Test
    public void subStrByLen() {
    }

    @org.junit.Test
    public void hex2AsciiStr() {
    }

    @org.junit.Test
    public void testHex2AsciiStr() {
    }

    @org.junit.Test
    public void intToHex() {
    }

    @org.junit.Test
    public void convertStringToHex() {
    }

    @org.junit.Test
    public void testBcd2str() {
    }

    @org.junit.Test
    public void stringToAscii() {
    }

    @org.junit.Test
    public void decimalFormat() {
    }

    @org.junit.Test
    public void checkNull() {
    }

    @org.junit.Test
    public void stringToBoolean() {
    }

    @org.junit.Test
    public void asciiToHex() {
    }

    @org.junit.Test
    public void toHex() {
    }
}