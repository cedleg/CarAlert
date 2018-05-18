package com.cedleg.caralert;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Arrays;
import java.util.Objects;

public class Vehicule {

    private String immatriculation;
    private String latitude;
    private String longitude;

    public Vehicule() {
        latitude = "0";
        longitude = "0";
    }

    public Vehicule(String immatriculation, String latitude, String longitude) {
        this();
        this.immatriculation = immatriculation;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public boolean isParked() {
        if(latitude != "0" && longitude != "O"){
            return true;
        } else {
            return false;
        }
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Vehicule{" +
                "immatriculation='" + immatriculation + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
