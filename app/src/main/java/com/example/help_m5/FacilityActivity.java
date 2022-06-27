package com.example.help_m5;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

public class FacilityActivity extends AppCompatActivity {

    double rate;
    int numReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);

        // Facility Title
        TextView facilityTitle = findViewById(R.id.facilityTitle);
        facilityTitle.setText("Tim Hortons");

        // Facility Rate
        rate = 4.3;
        TextView facilityRate = findViewById(R.id.facilityRatingText);
        facilityRate.setText(String.valueOf(rate));

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        // Facility Number of Reviews/Rates
        numReviews = 312;
        TextView facilityNumReviews = findViewById(R.id.facilityNumberOfRates);
        facilityNumReviews.setText(String.valueOf(numReviews) + " Reviews");



    }
}