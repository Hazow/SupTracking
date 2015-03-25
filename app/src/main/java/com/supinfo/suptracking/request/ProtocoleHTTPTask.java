package com.supinfo.suptracking.request;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import suptracking.supinfo.com.suptracking.Connected;
import suptracking.supinfo.com.suptracking.Login;
import suptracking.supinfo.com.suptracking.SplashScreen;

/**
 * Created by Anthony on 25/03/2015.
 */
public class ProtocoleHTTPTask extends AsyncTask<List<NameValuePair>, Void, String>
{
    private Context mContext;
    public ProtocoleHTTPTask (Context context){
        mContext = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("Debut", "Traitement async");
    }


    @Override
    protected String doInBackground(List<NameValuePair>... params) {


        // On créé un client http
        HttpClient httpclient = new DefaultHttpClient();
        // On créé notre entête
        HttpPost httppost = new HttpPost("http://91.121.105.200/SUPTracking/");

        try {

            // Ajoute la liste à notre entête
            httppost.setEntity(new UrlEncodedFormEntity(params[0]));

            // On exécute la requête tout en récupérant la réponse
            HttpResponse response = httpclient.execute(httppost);

            String responseText = null;
            try {
                responseText = EntityUtils.toString(response.getEntity());
            }catch (ParseException e) {
                e.printStackTrace();
                Log.i("Parse Exception", e + "");


            }
            JSONObject json = new JSONObject(responseText);
            return json.toString();


        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            return "Error";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return "Error";
        } catch (JSONException e) {
            e.printStackTrace();
            return e.getMessage();
        }


    }

    @Override
    protected void onPostExecute(String result) {

        JSONObject json;

        try {
            json = new JSONObject(result);
            String val= String.valueOf(json.get("success"));

            if(val=="true") {
                Log.e("Login : ******** ", "Bad login !");
                Toast.makeText(mContext,"Utilisateur identifié !",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(mContext, Connected.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }else{
                Log.e("Login : ******** ", "Good logins !");
                Toast.makeText(mContext,"Utilisateur incorrect !",Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("http réponse", result);
    }
}