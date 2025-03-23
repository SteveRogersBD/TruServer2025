package com.example.greenpulse.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.example.greenpulse.OtherActivity;
import com.example.greenpulse.R;
import com.example.greenpulse.databinding.ActivityMapBinding;
import com.example.greenpulse.models.Event;
import com.example.greenpulse.models.Field;
import com.example.greenpulse.models.MyLatLng;
import com.example.greenpulse.models.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapActivity extends OtherActivity implements OnMapReadyCallback {


    ActivityMapBinding binding;
    GoogleMap mMap;
    SharedPreferences sp;
    private final int LOCATION_CODE = 2000;
    FusedLocationProviderClient locationProviderClient;
    List<LatLng>pointList;
    List<MyLatLng>myPointList;
    List<Circle> circles = new ArrayList<>();
    Polygon polygon;
    DatabaseReference fieldDb,eventDb;
    List<Field>myFields;
    List<Event>events;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pointList = new ArrayList<>();
        myPointList = new ArrayList<>();
        myFields = new ArrayList<>();
        events = new ArrayList<>();
        fieldDb = FirebaseDatabase.getInstance().getReference().child("fields");
        eventDb = FirebaseDatabase.getInstance().getReference().child("events");
//        retrieveFields(fields -> {
//            // This block will be executed once the data is successfully retrieved
//            addFieldsOnMap(fields); // Add fields to map after data retrieval
//        });

        // Obtain the SupportMapFragment and get notified when the map is ready to use.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        binding.drawButton.setOnClickListener((v)->{
            drawPolygon();
            callDialogue();
        });
        binding.deleteButton.setOnClickListener((v)->{
            clearPolygons();
        });
        binding.searchView.setBackground(getDrawable(R.drawable.search_view_bg));
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        askLocationPermission();
        // Add a marker and move the camera
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                pointList.add(latLng);
                //Toast.makeText(MapActivity.this, latLng.toString(), Toast.LENGTH_SHORT).show();
                Circle circle = mMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(3) // radius in meters
                        .strokeColor(Color.BLUE) // border color
                        .fillColor(Color.argb(128, 0, 0, 255))); // fill color with transparency
                circles.add(circle); // Add the circle to the list
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                callDialogue2Event(latLng);
            }
        });

    }

    private void retrieveFields(DataRetrievalCallback callback) {
        fieldDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myFields.clear(); // Clear existing list
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Field aField = dataSnapshot.getValue(Field.class);
                    if (aField != null) {
                        myFields.add(aField);
                    }
                }
                // Trigger the callback when data is retrieved
                callback.onDataRetrieved(myFields);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to retrieve fields: " + error.getMessage());
                Toast.makeText(MapActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addFieldsOnMap(List<Field> fields) {
        for (Field field : myFields) {
            List<MyLatLng> fieldPointsWrapper = field.points;
            List<LatLng> fieldPoints = new ArrayList<>();
            for (MyLatLng point : fieldPointsWrapper) {
                fieldPoints.add(point.latLng());
            }
            PolygonOptions polygonOptions = new PolygonOptions()
                    .addAll(fieldPoints) // Add the points to the polygon
                    .strokeColor(Color.BLACK) // Set the outline color
                    .fillColor(0x220FF000) // Set the fill color with transparency
                    .strokeWidth(5); // Set the stroke width
            // Add the polygon to the map
            polygon = mMap.addPolygon(polygonOptions);
        }
    }


    private void retrieveMyLocation(String[] location)
    {
        sp = getSharedPreferences("userInfo",MODE_PRIVATE);
        location[0] = sp.getString("lat", null);
        location[1] = sp.getString("lon", null);
        if(location[0]==null || location[1]==null)
        {
            askLocationPermission();
        }
    }
    private void askLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_CODE);
        }
        else{
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==LOCATION_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                //permission granted so call getCurrentLocation()
                getCurrentLocation();
            }
            else {
                //permission denied. so do nothing
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation()
    {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationProviderClient.getLastLocation().addOnSuccessListener(this,new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null)
                {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    // Create a LatLng object for the current location
                    LatLng userLocation = new LatLng(latitude, longitude);
                    Toast.makeText(MapActivity.this, userLocation.toString(),
                            Toast.LENGTH_LONG).show();
                    // Move the camera to the user's location with a closer zoom
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17)); // Adjusted zoom level to 17

                    // Add a marker at the user's location
                    mMap.addMarker(new MarkerOptions().position(userLocation).title("My Location"));
                }
            }
        });
    }
    private void getAddress(LatLng location) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address>addressList;
        addressList = geocoder.getFromLocation(location.latitude,location.longitude,1);
        if(addressList!=null)
        {
            String address = addressList.get(0).toString();
            Toast.makeText(this, address, Toast.LENGTH_LONG).show();

        }

    }

    private void drawPolygon() {
        // Clear any existing polygons
//        if (polygon != null) {
//            polygon.remove();
//        }
        // Create a polygon on the map using the points
        PolygonOptions polygonOptions = new PolygonOptions()
                .addAll(pointList) // Add the points to the polygon
                .strokeColor(Color.RED) // Set the outline color
                .fillColor(0x220FF000) // Set the fill color with transparency
                .strokeWidth(5); // Set the stroke width
        // Add the polygon to the map
        polygon = mMap.addPolygon(polygonOptions);
        //pointList.clear();

    }
    private LatLng calculateCenterPoint(List<LatLng> points) {
        if (points.isEmpty()) return null;

        double latSum = 0;
        double lngSum = 0;

        for (LatLng point : points) {
            latSum += point.latitude;
            lngSum += point.longitude;
        }

        int totalPoints = points.size();
        return new LatLng(latSum / totalPoints, lngSum / totalPoints);
    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addressList;
        String address = "Address not found"; // Default value

        try {
            addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addressList != null && !addressList.isEmpty()) {
                address = addressList.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("GetAddress", "Geocoder service not available");
        }

        return address;
    }

    private void clearPolygons() {
        // Remove the polygon if it exists
        if (polygon != null) {
            polygon.remove();
            polygon = null; // Clear the reference
        }
        // Remove all circles
        for (com.google.android.gms.maps.model.Circle circle : circles) {
            circle.remove();
        }
        circles.clear(); // Clear the list of circles
    }
    // Method to search for a location
    private void searchLocation(String location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocationName(location, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                // Move the camera to the new location
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                // Add a marker at the new location
                mMap.addMarker(new MarkerOptions().position(latLng).title(address.getAddressLine(0)));
                mMap.addCircle(new CircleOptions()
                        .center(latLng) // Set the center to the searched location
                        .radius(100) // Radius in meters (adjust as needed)
                        .strokeColor(0xFF0000FF) // Blue outline
                        .fillColor(0x220000FF) // Light blue fill
                        .strokeWidth(5)); // Width of the circle outline
            } else {
                Log.d("SearchLocation", "Location not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("SearchLocation", "Geocoder service not available");
        }
    }

    private void callDialogue(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setTitle("Create Entry");

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        TextInputEditText titleEditText = dialog.findViewById(R.id.edit_text_title);
        TextInputEditText descriptionEditText = dialog.findViewById(R.id.edit_text_description);
        AppCompatButton button = dialog.findViewById(R.id.done_btn);


        button.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();

            // Validate inputs
            if (title.isEmpty()) {
                titleEditText.setError("Enter a title");
                return;
            }
            if (description.isEmpty()) {
                descriptionEditText.setError("Enter a description");
                return;
            }

            // Populate myPointList with LatLng values
            for (LatLng point : pointList) {
                MyLatLng myPoint = new MyLatLng(point.latitude, point.longitude);
                myPointList.add(myPoint);
            }

            // Create Field object and save to database
            Field myField = new Field(title, description, myPointList, new ArrayList<Task>());
            fieldDb.child(myField.title).setValue(myField);
            pointList.clear();
            // Display success message and dismiss the dialog
            Toast.makeText(this, "Field Added!!!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();


    }
    private void callDialogue2Event(LatLng latLng){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setTitle("Create Entry");

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        TextInputEditText titleEditText = dialog.findViewById(R.id.edit_text_title);
        TextInputEditText descriptionEditText = dialog.findViewById(R.id.edit_text_description);
        AppCompatButton button = dialog.findViewById(R.id.done_btn);


        button.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();

            // Validate inputs
            if (title.isEmpty()) {
                titleEditText.setError("Enter a title");
                return;
            }
            if (description.isEmpty()) {
                descriptionEditText.setError("Enter a description");
                return;
            }

            // Populate myPointList with LatLng values


            // Create Field object and save to database
            Event myEvent = new Event(title, description,latLng.latitude,latLng.longitude);
            eventDb.child(myEvent.title).setValue(myEvent);
            events.add(myEvent);
            // Display success message and dismiss the dialog
            Toast.makeText(this, "Event Added!!!", Toast.LENGTH_SHORT).show();
            drawCircle(myEvent);
            dialog.dismiss();
        });

        dialog.show();


    }

    private void drawCircle(Event event) {
        // Define the circle's properties
        LatLng latLng = new LatLng(event.lat,event.lon);
        CircleOptions circleOptions = new CircleOptions()
                .center(latLng) // The center of the circle
                .radius(100) // Radius of the circle in meters (you can adjust this)
                .strokeColor(Color.RED) // Color of the circle's border
                .strokeWidth(3) // Stroke width
                .fillColor(Color.argb(50, 255, 0, 0)); // Fill color with transparency (red in this case)

        // Add the circle to the map
        mMap.addCircle(circleOptions);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng) // Set the position (LatLng)
                .title(event.title) // Optional: Set a title for the marker (appears when you tap the marker)
                .snippet(event.description) // Optional: Set a snippet (description) for the marker
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)); // Optional: Set marker icon

        // Add the marker to the map
        mMap.addMarker(markerOptions);
    }


    public interface DataRetrievalCallback {
        void onDataRetrieved(List<Field> fields);
    }





}