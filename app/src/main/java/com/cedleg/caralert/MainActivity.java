package com.cedleg.caralert;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cedleg.caralert.permission.PermissionRunTime;
import com.cedleg.caralert.tools.Tools;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private AppCompatEditText editPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionRunTime perms = new PermissionRunTime(this);
        perms.checkAndRequestPermissions();
        editPI = findViewById(R.id.editPI);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("ocr_value"))
                editPI.setText(savedInstanceState.getString("ocr_value"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("ocr_value"))
                editPI.setText(getIntent().getExtras().getString("ocr_value"));
        }

    }

    public void btnCompte(View v){
        startActivity(new Intent(this, CompteActivity.class));
    }

    public void btnPhotoPI(View v){
        startActivity(new Intent(this, OcrActivity.class));
    }

    public void btnAlert(View v){


        Tools.hideKeyboard(MainActivity.this);

        final String url = CompteActivity.URL_GET + editPI.getText().toString();
        HttpJsonParser.PostTaskListener<String> postTaskListener = new HttpJsonParser.PostTaskListener<String>() {
            @Override
            public void onPostTask(String result) {

                if (result != null && !result.isEmpty()) {
                    Toast.makeText(MainActivity.this, result,Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(MainActivity.this, R.string.wrong_format, Toast.LENGTH_SHORT).show();
                }
            }
        };

        if (Tools.isCorrectImmatriculation(editPI.getText().toString())){
            HttpJsonParser client = new HttpJsonParser(this, url, postTaskListener);
            client.execute();
        } else {
            Toast.makeText(MainActivity.this, R.string.wrong_immatriculation, Toast.LENGTH_SHORT).show();
        }

    }

    public void btnAlertAll(View v){

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PermissionRunTime.REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    if (BuildConfig.DEBUG) {
                        Log.e("value", "Permission camera Granted, success.");
                    }
                    //Snackbar.make(findViewById(R.id.main_layout), "Permission storage granted.", Snackbar.LENGTH_LONG).show();
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.e("value", "Permission camera Denied");
                    }
                    //Snackbar.make(findViewById(R.id.main_layout), "Permission Storage Denied, You cannot use Export fonctionality.", Snackbar.LENGTH_LONG).show();
                }

                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (BuildConfig.DEBUG) {
                        Log.e("value", "Permission location Granted, success.");
                    }
                    //Snackbar.make(findViewById(R.id.main_layout), "Permission storage granted.", Snackbar.LENGTH_LONG).show();
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.e("value", "Permission location Denied");
                    }
                    //Snackbar.make(findViewById(R.id.main_layout), "Permission Storage Denied, You cannot use Export fonctionality.", Snackbar.LENGTH_LONG).show();
                }

                break;
            }
        }
    }

}
