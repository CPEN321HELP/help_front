package com.example.help_m5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
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
        String userDescription = "fwhfboifegiyoegfiofgwpifwqpfqwufvpwyfvwqpfqwpiyfvweyfvweyifwpifvywq";
        String userDate = "1/24/2022";
        float userRate1 = (float) 2.3;
        String userName1 = "Peter Na";
        String userDescription1 = "fwhfboifegiyoegfiofgwpifwqpfqwufvpwyfvwqpfqwpiyfvweyfvweyifwpifvywq";
        String userDate1 = "1/24/2022";
        float userRate2 = (float) 2.3;
        String userName2 = "Peter Na";
        String userDescription2 = "fwhfboifegiyoegfiofgwpifwqpfqwufvpwyfvwqpfqwpiyfvweyfvweyifwpifvywq";
        String userDate2 = "1/24/2022";
        float userRate3 = (float) 2.3;
        String userName3 = "Peter Na";
        String userDescription3 = "fwhfboifegiyoegfiofgwpifwqpfqwufvpwyfvwqpfqwpiyfvweyfvweyifwpifvywq";
        String userDate3 = "1/24/2022";
        float userRate4 = (float) 2.3;
        String userName4 = "Peter Na";
        String userDescription4 = "fwhfboifegiyoegfiofgwpifwqpfqwufvpwyfvwqpfqwpiyfvweyfvweyifwpifvywq";
        String userDate4 = "1/24/2022";
        createUserReview(userRate, userName, userDescription, userDate);
        createUserReview(userRate1, userName1, userDescription1, userDate1);
        createUserReview(userRate2, userName2, userDescription2, userDate2);
        createUserReview(userRate3, userName3, userDescription3, userDate3);
        createUserReview(userRate4, userName4, userDescription4, userDate4);

    }

    public void createUserReview(float userRate, String userName, String userDescription, String userDate) {
        LinearLayout review = new LinearLayout(this);
        review.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        review.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(dpToPx(15f), dpToPx(5f), dpToPx(15f), dpToPx(10f));
        review.setElevation(30);
        review.setBackgroundColor(Color.parseColor("#DDFFFFFF"));
        review.setLayoutParams(layoutParams);

        LinearLayout usernameAndRate = new LinearLayout(this);
        usernameAndRate.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        usernameAndRate.setOrientation(LinearLayout.HORIZONTAL);
        usernameAndRate.setBackgroundColor(Color.parseColor("#000B5F"));

        LinearLayout reportAndDate = new LinearLayout(this);
        reportAndDate.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        reportAndDate.setOrientation(LinearLayout.HORIZONTAL);
        reportAndDate.setBackgroundColor(Color.parseColor("#000B5F"));

        TextView userNameView = new TextView(this);
        userNameView.setText(userName);
        userNameView.setTextSize(15f);
        userNameView.setTextColor(Color.parseColor("#DDFFFFFF"));
        LinearLayout.LayoutParams layoutMargin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutMargin.setMargins(dpToPx(5f), dpToPx(5f), dpToPx(5f), dpToPx(5f));
        userNameView.setLayoutParams(layoutMargin);

        RatingBar userRateView = new RatingBar(new ContextThemeWrapper(this, R.style.RatingBar), null, android.R.attr.ratingBarStyleSmall);
        userRateView.setRating(userRate);
        LinearLayout.LayoutParams layoutParamsRate = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsRate.setMargins(dpToPx(180f), dpToPx(5f), dpToPx(5f), dpToPx(5f));
        layoutParamsRate.gravity = Gravity.CENTER | Gravity.END;
        userRateView.setLayoutParams(layoutParamsRate);

        TextView userDateView = new TextView(this);
        userDateView.setText(userDate);
        userDateView.setTextSize(15f);
        userDateView.setTextColor(Color.parseColor("#DDFFFFFF"));
        userDateView.setLayoutParams(layoutParamsRate);

        TextView userDescriptionView = new TextView(this);
        userDescriptionView.setText(userDescription);
        userDescriptionView.setTextSize(15f);
        userDescriptionView.setLayoutParams(layoutMargin);

        usernameAndRate.addView(userNameView);
        usernameAndRate.addView(userRateView);
        reportAndDate.addView(userDateView);
        review.addView(usernameAndRate);
        review.addView(reportAndDate);
        review.addView(userDescriptionView);

        LinearLayout linearLayout = findViewById(R.id.facilityReviews);
        linearLayout.addView(review);
    }

    public int dpToPx(float dp) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return (int) px;
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