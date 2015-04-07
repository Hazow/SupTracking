package suptracking.supinfo.com.suptracking;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.supinfo.suptracking.model.User;
import com.supinfo.suptracking.request.ProtocoleHTTPTask;
import com.supinfo.suptracking.sqlite.DAO;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Login extends ActionBarActivity {

    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button butSignIn = (Button) findViewById(R.id.btnSingIn);
        final EditText editName = (EditText) findViewById(R.id.etUserName);
        final EditText editPass = (EditText) findViewById(R.id.etPass);
        final ImageView car_footer = (ImageView) findViewById(R.id.car_footer);
        dao = new DAO(this);

        butSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(editName.getText().toString(),editPass.getText().toString());
            }
        });

        car_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateAnimation animation = new TranslateAnimation(0.0f, 600.0f,0.0f, 0.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
                animation.setDuration(1200);  // animation duration
                animation.setRepeatCount(1);  // animation repeat count
                animation.setRepeatMode(2);   // repeat animation (left to right, right to left )
                //animation.setFillAfter(true);
                car_footer.startAnimation(animation);
            }
        });

        if(dao.checkTableIsEmpty()){
            User user = dao.getLastUser();
            editName.setText(user.getUsername());
            editPass.setText(user.getPassword());
        }else{

        }
    }


    private void Login(String login,String password){// On ajoute nos données dans une liste
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        // On ajoute nos valeurs ici un identifiant et un message
        nameValuePairs.add(new BasicNameValuePair("action", "login"));
        nameValuePairs.add(new BasicNameValuePair("login", login));
        nameValuePairs.add(new BasicNameValuePair("password", password));

        new ProtocoleHTTPTask(this).execute(nameValuePairs);
    }
}
