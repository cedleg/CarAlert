package com.cedleg.caralert.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;

public class PermissionRunTime {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 52;
    private Activity activity;
    private Context context;

    public PermissionRunTime(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    /*PERMISSIONS REQUEST AT RUN TIME*/
    public boolean checkAndRequestPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int camera = ContextCompat.checkSelfPermission(this.context, Manifest.permission.CAMERA);
            int location = ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION);


            ArrayList<String> listPermissionsNeeded = new ArrayList<>();

            if (camera != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }
            if (location != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if (!listPermissionsNeeded.isEmpty()) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity, Manifest.permission.CAMERA)) {
                    Toast.makeText(this.context, "Accept permission Camera." + "\n" +
                            "Go to Setting > Application > Authorisation", Toast.LENGTH_LONG).show();
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(this.context, "Accept permission locate." + "\n" +
                            "Go to Setting > Application > Authorisation", Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions(this.activity, listPermissionsNeeded.toArray
                            (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                }
                return false;
            }

        }
        return true;
    }

}
