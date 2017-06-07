package com.example.mukhopadhyayd0116.mymapapp;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private boolean isGPSenabled = false;
    private boolean isNetworkenabled = false;
    private boolean canGetLocation = false;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 15 * 1;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5;
    private Location myLocation;
    private static final int MY_LOC_ZOOM_FACTOR = 17;
    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void changeView(View v) {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }


    public void getLocation(View view) {
        try {

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            //get GPS status
            isGPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSenabled) Log.d("MyMaps", "GetLocation:GPS is enabled");

            isNetworkenabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isNetworkenabled) Log.d("myMaps", "GetLocation:Network is enabled");


            if (!isGPSenabled && !isNetworkenabled) {
                // if(isGPSenabled){
                Log.d("myMaps", "GetLocation: No provider is enabled");
            } else {
                this.canGetLocation = true;
                if (isNetworkenabled) {
                    Log.d("MyMap", "GetLocation: Network enabled-requesting location Updates");
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListerNetwork);


                    Log.d("MyMap", "GetLocation:NetworkLoc update request successful.");
                    Toast.makeText(this, "Using Network", Toast.LENGTH_SHORT);
                }  if (isGPSenabled) {
                    Log.d("MyGMap", "getlocation() GPS enabled, requesting loc updates");
                    try {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);

                        Log.d("MyGMap", "getLocation() gps location update successful");
                        Toast.makeText(this, "Using GPS", Toast.LENGTH_SHORT);
                    } catch (SecurityException s) {
                        Log.d("MyGMap", "se getlocation gps");
                    }
                }



            }
        } catch (Exception e) {
            Log.d("MyMap", "caught exception in getlocation");
            e.printStackTrace();
        }


    }


    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            //Output in log.d and toast that gps is enabled and working
            //drop a marker on map- create a method called dropAmarker
            //remove the network location updates. Hint see the locationManager for update removal method
try{
            color = Color.BLUE;
           Log.d("MyMap","drop a marker2");
            dropAMarker(location.getProvider());
Log.d("MyMap","Drop a marker 2");
          //  locationManager.removeUpdates(locationListerNetwork);


          //  try {
             //   locationManager.removeUpdates(locationListener);

               locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                       MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListerNetwork);


        }
catch(SecurityException s){
}
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Called when a new location is found by the network location provider.
            //Output in log.d and toast that gps is enabled and working
            //setup aa switch statement to check the status of the input parameter
            //case LocatorProvider.AVAILABLE--> output message to log.d and toast
            //case LocationProvider.Outofservice--> request updates from Network_Provider
            //case LocationProvider.TemporarilyUnavailable--> request updates from Network_Provider
            //CASE DEFAULT request updates from network provider.
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("myMap", "LocationProvider AVAILABLE");
                    try {

                        color = Color.GREEN;

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListerNetwork);


                    }
                    catch (SecurityException s){
                        Log.d("MyGMap", "se getlocation net");
                    }
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    try{
                        color = Color.GREEN;

                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListerNetwork);
                        Log.d("MyGMap", "getLocation() network location update successful");
                    }
                    catch (SecurityException s){
                        Log.d("MyGMap", "se getlocation net");
                    }
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    try{
                        color  = Color.GRAY;
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListerNetwork);
                        Log.d("MyGMap", "getLocation() network location update successful");
                    }
                    catch (SecurityException s){
                        Log.d("MyGMap", "se getlocation net");
                    }
                    break;


            }

        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    LocationListener locationListerNetwork = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //Output in log.d and toast tha gps is enabled and working
            //drop a marker ion map- create a method called drop a marker
            //relaunch a network provider request(request locationUpdates(Network_Provider))

            try {
                color = Color.GREEN;
                Log.d("MyMap","drop a marker");
                dropAMarker(location.getProvider());
                Log.d("MyMap","drop a marker");
                locationManager.removeUpdates(locationListener);

                  locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                          MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListerNetwork);
            }
            catch (SecurityException s) {

            }



        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //Output log.d message and toast
            Log.d("MyMap","Status changed");
            switch(status){
                case LocationProvider.AVAILABLE: Log.d("MyGMap", "Network available");
                    break;
                case LocationProvider.OUT_OF_SERVICE: Log.d("MyGMap", "Network out of service");
                    try {
                        color = Color.RED;
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);

                        Log.d("MyGMap", "getLocation() gps location update successful");
                    }
                    catch (SecurityException s){
                        Log.d("MyGMap", "se getlocation gps");
                    }
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE: Log.d("MyGMap", "Network temporarily out of service");
                    try {
                        color = Color.RED;
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);

                        Log.d("MyGMap", "getLocation() gps location update successful");
                    }
                    catch (SecurityException s){
                        Log.d("MyGMap", "se getlocation gps");
                    }
                    break;
            }

        }


        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void dropAMarker(String provider) {
      LatLng userLocation = null;

        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            myLocation = locationManager.getLastKnownLocation(provider);

        }
         if(myLocation == null){
             //Display a message vua log.d or toast
             Log.d("MyMap","Mylocation is null");
         }else{
             //get uiser location
             userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
             //display a message with lat/long
             CameraUpdate update = CameraUpdateFactory.newLatLngZoom(userLocation,MY_LOC_ZOOM_FACTOR);
             //drop the actual marker on the map
             //If using circle, reference Android Circle class
             Circle circle = mMap.addCircle(new CircleOptions().center(userLocation).radius(1).strokeColor(color).strokeWidth(2).fillColor(color));
             mMap.animateCamera(update);

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
        mMap.setMyLocationEnabled(true);
    }
}
