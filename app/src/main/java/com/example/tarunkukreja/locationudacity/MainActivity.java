package com.example.tarunkukreja.locationudacity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final static String LOG_TAG = "MainActivity";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private TextView mLatitudeText;
    private TextView mLongText;
    private Location mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLatitudeText = (TextView)findViewById(R.id.latitude_text);
        mLongText = (TextView)findViewById(R.id.long_text);

        builtGoogleApiClient();

    }

    protected synchronized void builtGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(LOG_TAG, "onConnected -- Google Client");

        mLocationRequest = LocationRequest.create() ;
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY) ;
        mLocationRequest.setInterval(1000) ; //update the location every second


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 0){
            if(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                Log.d(LOG_TAG, "Permission was denied or request was cancelled");
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.d(LOG_TAG, "onConnectionSuspended -- Google Client") ;

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.d(LOG_TAG, "onConnectionFailed -- Google Client") ;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart()") ;

        // connecting the client
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "onStop()") ;
        super.onStop();
        // disconnecting the client
        if(mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }


    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "onPause()") ;
        super.onPause();

       // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this) ;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onLocationChanged(Location location) {

      Log.d(LOG_TAG, "Current Loc: " + location.toString());
      mLatitudeText.setText(Double.toString(location.getLatitude()));
    }
}
