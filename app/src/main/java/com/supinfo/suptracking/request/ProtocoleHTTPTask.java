package com.supinfo.suptracking.request;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.supinfo.suptracking.model.User;
import com.supinfo.suptracking.sqlite.DAO;

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
import org.json.JSONArray;
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
    private DAO dao;
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
                Log.e("Login : ******** ", "Good logins !");

                JSONObject uniObject = json.getJSONObject("user");
                final String username = String.valueOf(uniObject.get("username"));
                final String password = String.valueOf(uniObject.get("password"));
                dao = new DAO(mContext);
                final User user = new User(username,password);

                if(dao.checkIfExist(user.getUsername(),user.getPassword())){
                    Intent i = new Intent(mContext, Connected.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                    builder.setTitle("Suptracking");
                    builder.setMessage("Do you want to save your password ?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            dao.insertUser(user);
                            Intent i = new Intent(mContext, Connected.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(i);
                          }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent i = new Intent(mContext, Connected.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(i);
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }else{
                Log.e("Login : ******** ", "Bad login !");
                Toast.makeText(mContext,"Invalid login",Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("http réponse", result);
    }
}