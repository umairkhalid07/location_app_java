package com.example.locationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button B = findViewById(R.id.location_button);
        TextView txtLat, txtLng, txtSpeed, txtAltitude, txtAddress, txtLocality, txtSubLocality;
        txtLat = findViewById(R.id.latitude);
        txtLng = findViewById(R.id.longitude);
        txtAltitude = findViewById(R.id.altitude);
        txtSpeed = findViewById(R.id.speed);
        txtLocality = findViewById(R.id.sublocality);
        txtSubLocality = findViewById(R.id.locality);

        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (p != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    LocationManager lm = (LocationManager)getSystemService(LOCATION_SERVICE);
                    Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (loc == null)
                    {
                        Toast.makeText(getApplicationContext(), "Location not Loaded", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        txtLat.setText(""+loc.getLatitude());
                        Log.d("******", String.valueOf(loc.getLatitude()));
                        txtLng.setText(""+loc.getLongitude());
                        Log.d("******", String.valueOf(loc.getLongitude()));
                        txtSpeed.setText(""+String.valueOf(loc.getSpeed()));
                        Log.d("******", String.valueOf(loc.getSpeed()));
                        txtAltitude.setText(""+String.valueOf(loc.getAltitude()));
                        Log.d("******", String.valueOf(loc.getAltitude()));

                        Geocoder g = new Geocoder(getApplicationContext(), Locale.getDefault());
                        try {
                            List<Address> addresses = g.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                            Address aa = addresses.get(0);
                            //String wholeaddress = aa.getAddressLine(0);
                            //txtAddress.setText(wholeaddress);
                            txtLocality.setText(String.valueOf(aa.getLocality()));
                            Log.d("******", String.valueOf(aa.getLocality()));
                            txtSubLocality.setText(String.valueOf(aa.getSubLocality()));
                            Log.d("******", String.valueOf(aa.getSubLocality()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

//                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                            2000,
//                            1,
//                            new LocationListener() {
//                                @Override
//                                public void onLocationChanged(@NonNull Location location) {
//                                    txtLat.setText("" + location.getLatitude());
//                                    txtLng.setText("" + location.getLongitude());
//                                    txtSpeed.setText("" + location.getSpeed());
//                                    Geocoder g = new Geocoder(getApplicationContext(), Locale.getDefault());
//                                    List<Address> addresses = null;
//                                    try {
//                                        addresses = g.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    Address aa = addresses.get(0);
//                                    txtLocality.setText("" + aa.getLocality());
//                                    txtSubLocality.setText("" + aa.getSubLocality());
//                                }
//                            });

                }
                Button map = findViewById(R.id.map_button);
                map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int p = ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION);
                        if (p != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        } else {
                            LocationManager lm = (LocationManager)getSystemService(LOCATION_SERVICE);
                            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            Uri u = Uri.parse("geo:" + location.getLatitude() + "," + location.getLongitude());
                            Intent i = new Intent(Intent.ACTION_VIEW, u);
                            i.setPackage("com.google.android.apps.maps");
                            try {
                                startActivity(i);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });

    }
}