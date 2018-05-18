package com.cedleg.caralert;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cedleg.caralert.tools.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CompteActivity extends AppCompatActivity {

    public static final String URL_GET = "http://www.declique.net/cours/caralert/index.php/load/"; // + phone number
    public static final String URL_GET_ALL = "http://www.declique.net/cours/caralert/index.php/list/all";
    public static final String URL_SAVE = "http://www.declique.net/cours/caralert/index.php/save/"; // + phone number
    public static final String URL_PARK = "http://www.declique.net/cours/caralert/index.php/park/"; // + matricul number
    public static final String URL_UNPARK = "http://www.declique.net/cours/caralert/index.php/unpark/"; // + matricul number

    private AppCompatEditText edPhone;
    private List<Vehicule> vehicules = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte);
        edPhone = findViewById(R.id.edPhone);
    }

    public void btnCompteSearch(View v){

        Tools tool = new Tools();
        tool.hideKeyboard(CompteActivity.this);

        final String url = URL_GET + edPhone.getText().toString();

        HttpJsonParser.PostTaskListener<String> postTaskListener = new HttpJsonParser.PostTaskListener<String>() {
            @Override
            public void onPostTask(String result) {

                if (result != null && !result.isEmpty()) {
                    JSONArray jsonArray = null;

                    try {
                        jsonArray = new JSONArray(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for(int i=0; i<jsonArray.length(); i++) {
                        try {
                            createEntryVehicule(jsonArray.getJSONObject(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
                else{
                    Toast.makeText(CompteActivity.this, "Failed result",Toast.LENGTH_LONG).show();
                }
            }
        };

        if(Tools.isCorrectPhoneNumber(edPhone.getText().toString())) {
            HttpJsonParser client = new HttpJsonParser(this, url, postTaskListener);
            client.execute();
        } else {
            Toast.makeText(this, "incorrect phone number format\n0600000000 or 0700000000", Toast.LENGTH_SHORT).show();
        }

    }

    public void btnClose(View v){
        finish();
    }

    public void btnCheck(View v){

        JSONArray jsonArray = new JSONArray();

        for (int i=0; i<vehicules.size(); i++) {
            JSONObject jsonPost = new JSONObject();
            try {
                jsonPost.put("numero", edPhone.getText().toString());
                jsonPost.put("plaque", vehicules.get(0).getImmatriculation());
                jsonPost.put("latitude", vehicules.get(0).getLatitude());
                jsonPost.put("longitude", vehicules.get(0).getLongitude());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            jsonArray.put(jsonPost);
        }

        //Log.e("TAG POST JSON", jsonArray.toString());
        //String jsonBody = jsonArray.toString();

        /*
        JSONObject jsonPost2 = new JSONObject();
        try {
            jsonPost2.put("numero", "06456789");
            jsonPost2.put("plaque", "XX6754TT");
            jsonPost2.put("latitude", "");
            jsonPost2.put("longitude", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonBody = jsonPost2.toString();
        */

        String jsonBody = jsonArray.toString();
        String url = URL_SAVE + edPhone.getText().toString();
        //String url = URL_SAVE + "0";
        if(Tools.isCorrectPhoneNumber(edPhone.getText().toString())){
            HttpJsonParser client = new HttpJsonParser(this, url, "POST", jsonBody);
            client.execute();
        }else{
            Toast.makeText(this, "incorrect phone number format\n0600000000 or 0700000000", Toast.LENGTH_SHORT).show();
        }

    }

    public void createEntryVehicule(JSONObject json){

        LinearLayout parentLayout = findViewById(R.id.layoutAllPI);

        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.imat_item, parentLayout, false);
        final AppCompatImageButton button = view.findViewById(R.id.btn_imat);

        String pi = null, latitude = "0", longitude = "0";

        try {
            if(json.has("plaque")) pi = json.getString("plaque");

            if(json.has("latitude") && json.has("longitude")) {
                if (json.getString("latitude").equals("0") && json.getString("longitude").equals("0")) {
                    latitude = json.getString("latitude");
                    longitude = json.getString("longitude");
                    button.setImageDrawable(getResources().getDrawable(R.mipmap.ic_park));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Vehicule v = new Vehicule(pi, latitude, longitude);

        button.setTag(v);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton im = (AppCompatImageButton)v;

                Vehicule vehicule = (Vehicule) v.getTag();
                //TODO get real GPS values
                if(vehicule.isParked()){
                    vehicule.setLatitude("0");
                    vehicule.setLongitude("0");
                    im.setImageDrawable(getResources().getDrawable(R.mipmap.ic_unpark));
                } else {
                    vehicule.setLatitude("45.555689"); //Recupérer les valeurs du GPS
                    vehicule.setLongitude("25.348976"); //Recupérer les valeurs du GPS
                    im.setImageDrawable(getResources().getDrawable(R.mipmap.ic_park));
                }
                postParkedPoints(vehicule.getImmatriculation(), vehicule.getLatitude(), vehicule.getLongitude());
                Toast.makeText(CompteActivity.this, button.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        AppCompatEditText editPI = view.findViewById(R.id.ed_imat);
        editPI.setText(v.getImmatriculation());
        editPI.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                v.setImmatriculation(s.toString());
            }
        });

        vehicules.add(v);
        parentLayout.addView(view);

    }

    private void postParkedPoints(String immatriculation, String latitude, String longitude){

        if(!isCorrectData(immatriculation, latitude, longitude)){
            Toast.makeText(this, "incorrect format data", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonPost = new JSONObject();
        try {
            jsonPost.put("latitude", latitude);
            jsonPost.put("longitude", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.e("TAG POST PARK", jsonPost.toString());
        String jsonBody = jsonPost.toString();

        String url = URL_PARK + immatriculation;
        //Log.e("URL PARK", url);
        HttpJsonParser client = new HttpJsonParser(this, url, "POST", jsonBody);
        client.execute();
    }

    public boolean isCorrectData(String immat, String latitude, String longitude){
            return Tools.isCorrectImmatriculation(immat) && Tools.isCorrectGpsCoordinates(latitude, longitude);
    }
}
