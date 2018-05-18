package com.cedleg.caralert.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.cedleg.caralert.BuildConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PermissionRunTime implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 52;
    private Activity activity;
    private Context context;

    public PermissionRunTime(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    /*PERMISSIONS REQUEST AT RUN TIME*/
    public boolean checkAndRequestPermissions() {

        //boolean isValide = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int camera = ContextCompat.checkSelfPermission(this.context, Manifest.permission.CAMERA);

            ArrayList<String> listPermissionsNeeded = new ArrayList<>();

            if (camera != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity, Manifest.permission.CAMERA)) {
                    Toast.makeText(this.context, "Accept permission Camera fonctionality." + "\n" +
                            "Go to Setting > Application > Stocker App > Authorisation", Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions(this.activity, listPermissionsNeeded.toArray
                            (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                }
                return false;
            }

        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                for(int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    if (BuildConfig.DEBUG) {
                        Log.e("value", "Permission storage Granted, success.");
                    }
                    //Snackbar.make(findViewById(R.id.main_layout), "Permission storage granted.", Snackbar.LENGTH_LONG).show();
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.e("value", "Permission Storage Denied, You cannot use Export fonctionality.");
                    }
                    //Snackbar.make(findViewById(R.id.main_layout), "Permission Storage Denied, You cannot use Export fonctionality.", Snackbar.LENGTH_LONG).show();
                }

                break;
            }
        }
    }
}
