package suptracking.supinfo.com.suptracking;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static android.location.LocationManager.PASSIVE_PROVIDER;


public class Connected extends FragmentActivity implements OnMapReadyCallback{
    Marker mark;
    Marker car;
    int incr=0;
    Location locCar = new Location(PASSIVE_PROVIDER);
    Location myPos = new Location(PASSIVE_PROVIDER);
    GoogleMap map;
    int i=0;
    TextView txtv;
    RelativeLayout rl1;
    Timer t;
    Timer t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected);

        txtv = (TextView) findViewById(R.id.txtViewDist);
        rl1 = (RelativeLayout) findViewById(R.id.rl1);
        final ImageView car_footer = (ImageView) findViewById(R.id.car_footer);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        TranslateAnimation animation = new TranslateAnimation(-100.0f, 1100.0f,0.0f, 0.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(5200);  // animation duration
        animation.setRepeatCount(Animation.INFINITE); // animation repeat count
        animation.setRepeatMode(1);   // repeat animation (left to right, right to left )
        //animation.setFillAfter(true);
        car_footer.startAnimation(animation);

    }

    @Override
    protected void onDestroy() {
        if(t!=null){
            t.cancel();
            t.purge();
        }
        if(t1!=null){
            t1.cancel();
            t1.purge();
        }
        super.onDestroy();
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.d("Loc changed","");
                if (mark != null) {
                    mark.remove();
                }

                if(car != null){
                    car.remove();
                }
                myPos=location;
                mark = map.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .title("Moi"));

                Location l=getCarPosition();
                car = map.addMarker(new MarkerOptions()
                        .position(new LatLng(l.getLatitude(),l.getLongitude()))
                        .title("Voiture"));

                double distance = location.distanceTo(l);
                distance=Math.round(distance);
                txtv.setText(distance+"m");
                if(distance>100){
                    carOutLimit(distance, rl1);
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        Location loc=locationManager.getLastKnownLocation(PASSIVE_PROVIDER);

        // Called when a new location is found by the network location provider.
        mark=map.addMarker(new MarkerOptions()
                .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                .title("Moi"));
        myPos=loc;
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(loc.getLatitude(), loc.getLongitude())));
        map.animateCamera(CameraUpdateFactory.zoomTo(10),5000,null);

        Location l=getCarPosition();
        car=map.addMarker(new MarkerOptions()
                .position(new LatLng(l.getLatitude(),l.getLongitude()))
                .title("Vehicle"));

        t = new Timer();

        t.scheduleAtFixedRate( new TimerTask() {
            @Override
            public void run() {
                Log.d("Car : "," Moving");
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        if(car != null){
                            car.remove();
                        }

                        Location l=getCarPosition();
                        car = map.addMarker(new MarkerOptions()
                                .position(new LatLng(l.getLatitude(),l.getLongitude()))
                                .title("Vehicle"));

                        double distance = myPos.distanceTo(l);
                        distance=Math.round(distance);
                        txtv.setText(distance+"m");
                        if(distance>100){
                            carOutLimit(distance, rl1);
                        }
                    }
                });

            }
        }, 2000, 2000 );



    }

    private void carOutLimit(double distance, final RelativeLayout rl1){
        if(i==0){
           t1 = new Timer();

            t1.scheduleAtFixedRate( new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            if ( (i % 2) == 0) {
                                //rl1.setBackgroundColor(Color.RED);
                            } else {
                                //rl1.setBackgroundColor(Color.YELLOW);
                            }
                            i++;
                        }
                    });

                }
            }, 500, 500 );

            notifyUser();
        }
        Toast.makeText(this, "ALERT : Someone steal your vehicle ! \n Distance : "+distance+"m", Toast.LENGTH_SHORT).show();
    }

    public void notifyUser(){

        NotificationManager notificationManager = (NotificationManager)this.getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, Connected.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //use the flag FLAG_UPDATE_CURRENT to override any notification already there
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification(R.drawable.ic_launcher, "SupTracking", System.currentTimeMillis());
        notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND;

        notification.setLatestEventInfo(this, "SupTracking", " Someone steal your vehicle !", contentIntent);
        //10 is a random number I chose to act as the id for this notification
        notificationManager.notify(10, notification);

    }

    private Location getCarPosition(){

        if(incr == 0){
            locCar.setLatitude(myPos.getLatitude());
            locCar.setLongitude(myPos.getLongitude());
        }else{

            locCar.setLatitude(locCar.getLatitude()+0.000100);
            locCar.setLongitude(locCar.getLongitude() + 0.000100);

            Log.d("Move Car","Lat : "+locCar.getLatitude()+" - Long : "+locCar.getLongitude());
        }
        incr++;
        return locCar;
    }

    @Override
    public void onBackPressed() {
        if(t!=null){
            t.cancel();
            t.purge();
        }
        if(t1!=null){
            t1.cancel();
            t1.purge();
        }
        //Log.e("Timer : ",t.toString());
        Intent i = new Intent(Connected.this, Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(i);
        finish();
    }
}
