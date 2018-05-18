package com.cedleg.caralert;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import com.cedleg.caralert.permission.PermissionRunTime;
import com.cedleg.caralert.tools.Tools;

public class MainActivity extends AppCompatActivity {


    AppCompatEditText editPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionRunTime perms = new PermissionRunTime(this);
        perms.checkAndRequestPermissions();

        editPI = findViewById(R.id.editPI);
    }

    public void btnCompte(View v){
        startActivity(new Intent(this, CompteActivity.class));
    }

    public void btnPhotoPI(View v){

    }

    public void btnAlert(View v){


        Tools.hideKeyboard(MainActivity.this);

        final String url = "http://www.declique.net/cours/caralert/index.php/get/" + editPI.getText().toString();
        HttpJsonParser.PostTaskListener<String> postTaskListener = new HttpJsonParser.PostTaskListener<String>() {
            @Override
            public void onPostTask(String result) {

                if (result != null && !result.isEmpty()) {
                    Toast.makeText(MainActivity.this, result,Toast.LENGTH_LONG).show();
                }
                else{
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

}
