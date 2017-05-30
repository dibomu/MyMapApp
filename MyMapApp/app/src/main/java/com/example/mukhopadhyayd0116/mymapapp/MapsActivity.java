package com.example.mukhopadhyayd0116.mymapapp;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private boolean isGPSenabled = false;
    private boolean isNetworkenabled = false;
    private boolean canGetLocation = false;
    private static final long MIN_TIME_BW_UPDATES= 1000*15*1;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES= 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    public void changeView(View v){
        if (mMap.getMapType()==GoogleMap.MAP_TYPE_NORMAL){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else{
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }
    public void getLocation(){
        try{
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            //get GPS status
            isGPSenabled= locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(isGPSenabled) Log.d("MyMaps","GetLocation:GPS is enabled");

            isNetworkenabled  = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(isNetworkenabled) Log.d("myMaps", "GetLocation:Network is enabled");



            if (!isGPSenabled &&!isNetworkenabled){
                Log.d("myMaps","GetLocation: No provider is enabled");
            }else{
                this.canGetLocation = true;

                if(isNetworkenabled){
                    Log.d("MyMap","GetLocation: Network enabled-requesting location Updates");
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,locationListerGPS);


                    Log.d("MyMap", "GetLocation:NetworkLoc update request successful.");
                    Toast.makeText(this, "Using Network", Toast.LENGTH_SHORT);
                }
            }
        }catch(Exception e){
            Log.d("MyMap","caught exception in getlocation");
            e.printStackTrace();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(38.6354, -90.2636);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in St.Louis"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            Log.d("myMapsApp","Failed Permission check 1");
            Log.d("myMapsApp", Integer.toString(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)));
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);

        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=  PackageManager.PERMISSION_GRANTED){
            Log.d("myMapsApp","Failed Permission check 2");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},2);

        }
    }
}
