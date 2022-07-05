package com.example.help_m5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URISyntaxException;

public class FacilityActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String title;
    private float rate;
    private int numReviews;
    private MapView mapView;
    private GoogleMap mMap;
    private double latitude;
    private double longitude;
    private Button rateButton;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);

        // Facility Title
        title = "Tim Hortons";
        TextView facilityTitle = findViewById(R.id.facilityTitle);
        facilityTitle.setText(title);

        // Facility Image
        image = "https://www.lunenburgregion.ca/themes/user/site/default/asset/img/gallery/Tim_Hortons_2.jpg";
        Uri uriImage = Uri.parse(image);
        Picasso.get().load(uriImage).into((ImageView)findViewById(R.id.imageView2));

        // Facility Rate
        rate = (float) 4.3;
        TextView facilityRate = findViewById(R.id.facilityRatingText);
        facilityRate.setText("★" + String.valueOf(rate));

        // Rating bar
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        ratingBar.setRating(rate);

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

        // Rate Button
        rateButton = findViewById(R.id.rate_button);
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent modelIntent = new Intent(FacilityActivity.this, RateActivity.class);
                startActivity(modelIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        float userRate = (float) 2.3;
        String userName = "Peter Na";
        String userDescription = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    }

    public void createUserReview(float userRate, String userName, String userDescription) {
        LinearLayout review = new LinearLayout(this);
        review.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        review.setOrientation(LinearLayout.VERTICAL);

        LinearLayout usernameAndRate = new LinearLayout(this);
        usernameAndRate.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        usernameAndRate.setOrientation(LinearLayout.HORIZONTAL);

        TextView userNameView = new TextView(this);
        userNameView.setText(userName);
        userNameView.setTextSize(12);

        TextView userDescriptionView = new TextView(this);
        userNameView.setText(userDescription);
        userNameView.setTextSize(12);

        RatingBar userRateView = new RatingBar(this);
        userRateView.setRating(userRate);
        userRateView.setScaleX((float)0.5);
        userRateView.setScaleY((float)0.5);

        usernameAndRate.addView(userNameView);
        usernameAndRate.addView(userRateView);
        review.addView(usernameAndRate);
        review.addView(userDescriptionView);
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