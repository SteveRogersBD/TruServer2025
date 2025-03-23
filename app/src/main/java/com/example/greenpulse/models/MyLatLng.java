package com.example.greenpulse.models;

import com.google.android.gms.maps.model.LatLng;

public class MyLatLng {
    private double latitude;
    private double longitude;

    // No-argument constructor required by Firebase
    public MyLatLng() {
    }

    // Constructor
    public MyLatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and setters
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public LatLng latLng()
    {
        return new LatLng(latitude,longitude);
    }
}
