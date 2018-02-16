package com.example.akankshasingh.listview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Akanksha.Singh on 2/16/2018.
 */

public class ShowMap extends Activity implements OnMapReadyCallback {
    Context context;
    private GoogleMap mMap;
    private LatLng location;
    Double latitude,longitude;
    private static final int MY_PERMISSIONS_ACCESSS_FINE_LOCATION = 1;
    MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        context=this;
         mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                latitude= Double.valueOf(extras.getString("Latitude"));
                longitude=Double.valueOf(extras.getString("Longitude"));
                location = new LatLng(latitude, longitude);
            }
        mapFragment.getMapAsync(this);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
            mMap.animateCamera(cameraUpdate);
            CameraUpdateFactory.zoomIn();
            CameraUpdateFactory.zoomOut();
            mMap.addMarker(new MarkerOptions()
                    .title("your location")
                    .position(location));
        }
        else if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            int currentapiVersion = Build.VERSION.SDK_INT;
            if (currentapiVersion >= 23) {
                // Do something for lollipop and above versions
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESSS_FINE_LOCATION
                );
            } else {
                // do something for phones running an SDK before lollipop
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESSS_FINE_LOCATION
                );
            }

        }
    }
}
