package com.example.lab4_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);
            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            startListening();
        }
        else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    updateLocationInfo(location);
                }
            }
        }
    }

    public void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,  @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    public void updateLocationInfo(Location location){
        Log.i("Locationinfo", location.toString());
        TextView latitude = findViewById(R.id.lattitude);
        TextView longitude = findViewById(R.id.longitude);
        TextView altitude = findViewById(R.id.altitude);
        TextView accuracy = findViewById(R.id.accuracy);
        TextView addr = findViewById(R.id.address);

        latitude.setText("Latitude: " + location.getLatitude());
        longitude.setText("Longitude: " + location.getLongitude());
        altitude.setText("Altitude: " + location.getAltitude());
        accuracy.setText("Accuracy: " + location.getAccuracy());

        // Address:
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            String address = "Could Not Find Address";
            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            Log.i("PlaceInfo", listAddresses.get(0).toString());
            address = "Address: \n";
            if (listAddresses.get(0).getSubThoroughfare() != null) {
                address += listAddresses.get(0).getSubThoroughfare() + " ";
            }
            if (listAddresses.get(0).getThoroughfare() != null) {
                address += listAddresses.get(0).getThoroughfare() + "\n";
            }
            if (listAddresses.get(0).getLocality() != null) {
                address += listAddresses.get(0).getLocality() + "\n";
            }
            if (listAddresses.get(0).getPostalCode() != null) {
                address += listAddresses.get(0).getPostalCode() + "\n";
            }
            if (listAddresses.get(0).getCountryCode() != null) {
                address += listAddresses.get(0).getCountryCode() + "\n";
            }

            addr.setText(address);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
