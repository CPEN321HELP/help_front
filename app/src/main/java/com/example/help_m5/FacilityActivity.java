package com.example.help_m5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FacilityActivity extends AppCompatActivity implements OnMapReadyCallback {

    double rate;
    int numReviews;
    MapView mapView;
    GoogleMap mMap;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);

        // Rating bar
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        // Facility Title
        TextView facilityTitle = findViewById(R.id.facilityTitle);
        facilityTitle.setText("Tim Hortons");

        // Facility Rate
        rate = 4.3;
        TextView facilityRate = findViewById(R.id.facilityRatingText);
        facilityRate.setText(String.valueOf(rate));

        // Facility Number of Reviews/Rates
        numReviews = 312;
        TextView facilityNumReviews = findViewById(R.id.facilityNumberOfRates);
        facilityNumReviews.setText(String.valueOf(numReviews) + " Reviews");

        // Google Maps Location
        latitude = 49.2602;
        longitude = -123.2484;
        mapView = findViewById(R.id.mapView);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng marker = new LatLng(latitude, longitude);
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions()
                .position(marker)
                .title("Marker"));
        float zoomLevel = 17.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, zoomLevel));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}