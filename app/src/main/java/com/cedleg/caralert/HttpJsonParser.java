package com.cedleg.caralert;

import android.app.ProgressDialog;
import android.content.Context;

import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpJsonParser extends AsyncTask<Void, Void, String> {

    Context ctx;
    static JSONObject jObj = null;
    private String adr = "";
    private String method = "GET";
    private String body = "";
    private String response = "";
    ProgressDialog pd;

    private PostTaskListener<String> postTaskListener;

    public interface PostTaskListener<K> {
        // K is the type of the result object of the async task
        void onPostTask(K result);
    }


    public HttpJsonParser(Context c, String adr, PostTaskListener<String> postTaskListener)
    {
        if(this.postTaskListener==null) {
            this.postTaskListener = postTaskListener;
        }
        this.ctx = c;
        setAdr(adr);
    }

    public HttpJsonParser(Context c, String adr, String method) {
        this.ctx = c;
        setAdr(adr);
        setMethod(method);
    }

    public HttpJsonParser(Context c, String adr, String method, String body) {
        this.ctx = c;
        setAdr(adr);
        setMethod(method);
        setBody(body);
    }

    // Propriétés :
    public String getAdr() {return adr;}
    public void setAdr(String value) {adr = value;}

    public String getMethod() {return method;}
    public void setMethod(String value) {method = value;}

    public String getBody() {return body;}
    public void setBody(String value) {body = value;}

    public String getResponse() {return response;}

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(ctx);
        pd.setTitle("Send");
        pd.setMessage("Sending..Please wait");
        pd.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        return this.callAPI();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pd.dismiss();
        if(response != null)
        {
            //SUCCESS

            /* You can parse json here*/
            /*
            try {
                JSONArray jsonArray = new JSONArray(response);
                //jObj = new JSONObject(response);
                jObj = jsonArray.toJSONObject(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            */

            //Toast.makeText(ctx,response,Toast.LENGTH_LONG).show();
            if (postTaskListener != null)
                postTaskListener.onPostTask(response);
        }else
        {
            //NO SUCCESS
            Toast.makeText(ctx,"Unsuccessful request", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


    public String callAPI() {
        URL url;
        HttpURLConnection cnt = null;
        try {
            url = new URL(adr);

            // Établir la connexion :
            cnt = (HttpURLConnection) url.openConnection();
            cnt.setUseCaches(false);
            cnt.setRequestMethod(method);
            cnt.setConnectTimeout(20000);
            cnt.setReadTimeout(20000);
            cnt.setDoInput(true);
            cnt.setRequestProperty("Content-Type", "application/json");
            cnt.setRequestProperty("Accept", "application/json");

            // Envoyer les données :
            if(method.equals("POST")) {
                cnt.setDoOutput(true);
                OutputStream out = cnt.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        out, "UTF-8"));
                writer.write(body);
                writer.flush();
                writer.close();
                out.close();
            }

            // Lire la réponse :
            InputStream in = cnt.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            response = "";
            StringBuilder sb = new StringBuilder();
            String line = null;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                sb.append(line);
            }
            response = builder.toString();
            reader.close();
            in.close();
        }
        catch (Exception ex) {
            response= "";
        }
        finally {
            if(cnt != null)
            cnt.disconnect();

            return response;
        }


    }

}
