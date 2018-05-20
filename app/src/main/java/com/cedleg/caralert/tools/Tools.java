package com.cedleg.caralert.tools;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Tools {

    /**
     * Constructor
     */
    public Tools() {
    }


    /**
     * Desc: Hide Soft-keyboard
     * @param activity
     */
    public static void hideKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);

        if(imm != null && imm.isAcceptingText()){
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Desc: Test correct format phone number
     * Requiered french format like 0600000000 or 0700000000
     * @param frenchPhoneNumber
     * @return
     */
    public static boolean isCorrectPhoneNumber(String frenchPhoneNumber){
        return frenchPhoneNumber.matches("^[0][6-7][0-9]{8}$");
    }

    /**
     * Desc: Test correct format immatriculation
     * Requiered french format like XX000XX
     * @param immatriculation
     * @return
     */
    public static boolean isCorrectImmatriculation(String immatriculation){
        return immatriculation.matches("^[A-Z]{2}[0-9]{3}[A-Z]{2}$");
    }

    /**
     * Desc: Test correct string format from GPS float values latitude and longitude
     * Requiered latitute + longitude format like 25.567545 + 10.653255 or 0 + 0
     * @param latitude
     * @param longitude
     * @return
     */
    public static boolean isCorrectGpsCoordinates(String latitude, String longitude){
        return latitude.matches("0|(^[0-9]{1,2}.[0-9]{6})$") && longitude.matches("0|(^[0-9]{1,2}.[0-9]{6})$");
    }
}
