package com.cedleg.caralert;


import org.junit.Test;

import java.util.regex.Pattern;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class CharEntriesTest {

    @Test
    public void isCorrectNumberPhone(){
        String str = "0600000001";
        assertEquals(true, Pattern.matches("^[0][6-7][0-9]{8}$", str));
        assertTrue(str.length() == 10);

        String str1 = "0700000001";
        assertEquals(true, Pattern.matches("^[0]{1}[6-7]{1}[0-9]{8}$", str1));

        String str2 = "0800000001";
        assertEquals(false, Pattern.matches("^[0][6-7][0-9]{8}$", str2));

        String str3 = "800000001";
        assertEquals(false, Pattern.matches("^[0][6-7][0-9]{8}$", str3));

        String str4 = "6600000001";
        assertEquals(false, Pattern.matches("^[0][6-7][0-9]{8}$", str4));

        String str5 = "060000";
        assertEquals(false, Pattern.matches("^[0][6-7][0-9]{8}$", str5));

        String str6 = "0600ABC001";
        assertEquals(false, Pattern.matches("^[0][6-7][0-9]{8}$", str6));

        String str7 = "000000000";
        assertEquals(false, Pattern.matches("^[0][6-7][0-9]{8}$", str7));
    }

    @Test
    public void isCorrectImmatriculation(){
        String str = "XX000FF";
        assertEquals(true, Pattern.matches("^[A-Z]{2}[0-9]{3}[A-Z]{2}$", str));

        String str1 = "0000000";
        assertEquals(false, Pattern.matches("^[A-Z]{2}[0-9]{3}[A-Z]{2}$", str1));

        String str2 = "XXXXXXX";
        assertEquals(false, Pattern.matches("^[A-Z]{2}[0-9]{3}[A-Z]{2}$", str2));

        String str3 = "XX000";
        assertEquals(false, Pattern.matches("^[A-Z]{2}[0-9]{3}[A-Z]{2}$", str3));
    }

    @Test
    public void isCorrectGpsCoordinates(){
        String str = "25.432421";
        assertEquals(true, Pattern.matches("0|(^[0-9]{1,2}.[0-9]{6})$", str));

        String str1 = "0";
        assertEquals(true, Pattern.matches("0|(^[0-9]{1,2}.[0-9]{6})$", str1));

        String str2 = "12.432421";
        assertEquals(true, Pattern.matches("0|(^[0-9]{1,2}.[0-9]{6})$", str2));

        String str3 = "123.432421";
        assertEquals(false, Pattern.matches("0|^[0-9]{1,2}.[0-9]{6}$", str3));

        String str4 = ".432421";
        assertEquals(false, Pattern.matches("0|^[0-9]{1,2}.[0-9]{6}$", str4));

        String str5 = "AA.AAAAAA";
        assertEquals(false, Pattern.matches("0|^[0-9]{1,2}.[0-9]{6}$", str5));
    }
}
