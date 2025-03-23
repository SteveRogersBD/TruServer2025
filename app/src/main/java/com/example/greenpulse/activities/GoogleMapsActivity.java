package com.example.greenpulse.activities;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.greenpulse.R;
import com.example.greenpulse.databinding.ActivityGoogleMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "GoogleMapsActivity";
    private GoogleMap mMap;
    private ActivityGoogleMapsBinding binding;
    private LocationManager locationManager;
    private LatLng currentLocation;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGoogleMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        getCurrentLocation();
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        mMap.setMyLocationEnabled(true);
        Log.d(TAG, "Location permission granted, MyLocation enabled");

        // Check provider status
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.d(TAG, "GPS Enabled: " + gpsEnabled + ", Network Enabled: " + networkEnabled);

        if (!gpsEnabled && !networkEnabled) {
            Toast.makeText(this, "Location services are disabled", Toast.LENGTH_SHORT).show();
            promptEnableLocation();
            return;
        }

        // Try last known location
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation == null) {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (lastKnownLocation != null) {
            currentLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            Log.d(TAG, "Last known location: " + currentLocation.latitude + ", " + currentLocation.longitude);
        } else {
            Toast.makeText(this, "Waiting for location...", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Last known location is null");
        }

        // Request location updates
        if (gpsEnabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
            Log.d(TAG, "Requested updates from GPS_PROVIDER");
        }
        if (networkEnabled) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
            Log.d(TAG, "Requested updates from NETWORK_PROVIDER");
        }
    }

    private void promptEnableLocation() {
        Toast.makeText(this, "Please enable location services", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.clear(); // Optional: Remove if you want multiple markers
                mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Current Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                Log.d(TAG, "Location updated: " + location.getLatitude() + ", " + location.getLongitude());
            } else {
                Log.d(TAG, "onLocationChanged received null location");
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "Provider " + provider + " status changed: " + status);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(GoogleMapsActivity.this, provider + " enabled", Toast.LENGTH_SHORT).show();
            getCurrentLocation();
            Log.d(TAG, "Provider enabled: " + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "Provider disabled: " + provider);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                    !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                promptEnableLocation();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
            Log.d(TAG, "Location updates removed");
        }
    }
}