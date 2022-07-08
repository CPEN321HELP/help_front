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
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.help_m5.ui.database.DatabaseConnection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class FacilityActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DatabaseConnection DBconnection;

    private String facilityId;
    private String title;
    private double rate;
    private int numReviews;
    private String description;
    private int type;
    private String image;
    private double latitude;
    private double longitude;

    private Button rateButton;
    private MapView mapView;
    private GoogleMap mMap;

    private int id = 1;
    private final int UPVOTE_BASE_ID = 10000000;
    private final int DOWNVOTE_BASE_ID = 20000000;
    private final int UPVOTE_TEXTVIEW_BASE_ID = 30000000;
    private final int DOWNVOTE_TEXTVIEW_BASE_ID = 40000000;
    private final int REPORT_BUTTON_BASE_ID = 50000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);

        Bundle bundle = getIntent().getExtras();
        facilityId = bundle.getString("facility_id");
        type = Integer.parseInt(bundle.getString("facility_type"));

        // Get data from database
        DBconnection = new DatabaseConnection();
        DBconnection.getSpecificFacility(type, facilityId, FacilityActivity.this);
        String facilityInfo = DBconnection.readFromJson(FacilityActivity.this, "specific_facility.json");

        try {
            JSONObject facility = new JSONObject(facilityInfo);
            title = (String) facility.getJSONObject("facility").getString("facilityTitle");
            description = (String) facility.getJSONObject("facility").getString("facilityDescription");
            System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
            image = (String) facility.getJSONObject("facility").getString("facilityImageLink");
            System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD"+image);
            rate = Double.parseDouble((String) facility.getJSONObject("facility").getString("facilityOverallRate"));
            numReviews = Integer.parseInt((String) facility.getJSONObject("facility").getString("numberOfRates"));
            latitude = Double.parseDouble((String) facility.getJSONObject("facility").getString("latitude"));
            longitude = Double.parseDouble((String) facility.getJSONObject("facility").getString("longtitude"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Facility Title
        TextView facilityTitle = findViewById(R.id.facilityTitle);
        facilityTitle.setText(title);

        // Facility Description
        TextView facilityDescription = findViewById(R.id.facilityDescription);
        facilityDescription.setText(description);

        // Facility Image
        image = "https://www.lunenburgregion.ca/themes/user/site/default/asset/img/gallery/Tim_Hortons_2.jpg";
        Uri uriImage = Uri.parse(image);
        Picasso.get().load(uriImage).into((ImageView)findViewById(R.id.imageView2));

        // Facility Rate
        TextView facilityRate = findViewById(R.id.facilityRatingText);
        facilityRate.setText("★" + String.valueOf((float) rate));

        // Rating bar
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        ratingBar.setRating((float) rate);

        // Facility Number of Reviews/Rates
        TextView facilityNumReviews = findViewById(R.id.facilityNumberOfRates);
        facilityNumReviews.setText(String.valueOf(numReviews) + " Reviews");

        // Google Maps Location
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
        String userDate = "22/24/2022";
        String userEmail = "frist@gmail.com";
        String userEmail2 = "second@gmail.com";
        String userEmail3 = "third@gmail.com";
        String userEmail4 = "fourth@gmail.com";
        String userEmail5 = "fifth@gmail.com";

        createUserReview(userRate, userName, userEmail, userDescription, userDate);
        createUserReview(userRate, userName, userEmail2, userDescription, userDate);
        createUserReview(userRate, userName, userEmail3, userDescription, userDate);
        createUserReview(userRate, userName, userEmail4, userDescription, userDate);
        createUserReview(userRate, userName, userEmail5, userDescription, userDate);

        for (int i = 1; i < id; i++) {
            CheckBox checkUpvote = (CheckBox) findViewById(UPVOTE_BASE_ID + i);
            boolean checkedUp = PreferenceManager.getDefaultSharedPreferences(this)
                    .getBoolean("upVote"+String.valueOf(UPVOTE_BASE_ID + i), false);
            checkUpvote.setChecked(checkedUp);

            CheckBox checkDownvote = (CheckBox) findViewById(DOWNVOTE_BASE_ID + i);
            boolean checkedDown = PreferenceManager.getDefaultSharedPreferences(this)
                    .getBoolean("downVote"+String.valueOf(DOWNVOTE_BASE_ID + i), false);
            checkDownvote.setChecked(checkedDown);
        }

    }

    public void createUserReview(float userRate, String userName, String userEmail, String userDescription, String userDate) {
        // Linear Layouts
        LinearLayout review = new LinearLayout(this);
        review.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        review.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(dpToPx(15f), dpToPx(5f), dpToPx(15f), dpToPx(10f));
        review.setElevation(30);
        review.setBackgroundColor(Color.parseColor("#FFFFFF"));
        review.setLayoutParams(layoutParams);

        LinearLayout usernameAndDate = new LinearLayout(this);
        usernameAndDate.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        usernameAndDate.setOrientation(LinearLayout.HORIZONTAL);
        usernameAndDate.setBackgroundColor(Color.parseColor("#FFFFFF"));

        LinearLayout descriptionAndReport = new LinearLayout(this);
        descriptionAndReport.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        descriptionAndReport.setOrientation(LinearLayout.HORIZONTAL);
        descriptionAndReport.setBackgroundColor(Color.parseColor("#FFFFFF"));

        LinearLayout votingSystem = new LinearLayout(this);
        votingSystem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        votingSystem.setOrientation(LinearLayout.HORIZONTAL);
        votingSystem.setBackgroundColor(Color.parseColor("#FFFFFF"));

        // Specific Element Views
        TextView userNameView = new TextView(this);
        userNameView.setText(userName);
        userNameView.setTextSize(15f);
        userNameView.setTextColor(Color.parseColor("#000000"));
        LinearLayout.LayoutParams layoutMargin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutMargin.setMargins(dpToPx(5f), dpToPx(5f), dpToPx(5f), dpToPx(0f));
        userNameView.setLayoutParams(layoutMargin);

        TextView userDateView = new TextView(this);
        userDateView.setText(userDate);
        userDateView.setTextSize(15f);
        userDateView.setTextColor(Color.parseColor("#626062"));
        LinearLayout.LayoutParams layoutParamsDate = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsDate.setMargins(dpToPx(210f), dpToPx(3f), dpToPx(5f), dpToPx(0f));
        layoutParamsDate.gravity = Gravity.CENTER_VERTICAL;
        userDateView.setLayoutParams(layoutParamsDate);

        RatingBar userRateView = new RatingBar(new ContextThemeWrapper(this, R.style.RatingBar), null, android.R.attr.ratingBarStyleSmall);
        userRateView.setRating(userRate);
        userRateView.setNumStars(5);
        LinearLayout.LayoutParams layoutParamsRate = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsRate.setMargins(dpToPx(4f), dpToPx(0f), dpToPx(5f), dpToPx(0f));
        layoutParamsRate.gravity = Gravity.CENTER_VERTICAL;
        userRateView.setLayoutParams(layoutParamsRate);

        TextView userDescriptionView = new TextView(this);
        userDescriptionView.setText(userDescription);
        userDescriptionView.setTextSize(15f);
        userDescriptionView.setTextColor(Color.parseColor("#000000"));
        userDescriptionView.setWidth(dpToPx(300f));
        LinearLayout.LayoutParams layoutParamsDescription = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsDescription.setMargins(dpToPx(5f), dpToPx(7f), dpToPx(5f), dpToPx(3f));
        layoutParamsDescription.gravity = Gravity.CENTER;
        userDescriptionView.setLayoutParams(layoutParamsDescription);

        Button reportButton = new Button(this, null, androidx.appcompat.R.attr.borderlessButtonStyle);
        reportButton.setId(REPORT_BUTTON_BASE_ID + id);
        reportButton.setTag(userEmail);
        reportButton.setText("Report");
        reportButton.setTextSize(dpToPx(5f));
        reportButton.setTextColor(Color.parseColor("#626062"));
        reportButton.setAllCaps(false);
        LinearLayout.LayoutParams layoutParamsReport = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsReport.setMargins(dpToPx(170f), dpToPx(0f), dpToPx(0f), dpToPx(0f));
        reportButton.setLayoutParams(layoutParamsReport);
        reportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent reportIntent = new Intent(FacilityActivity.this, ReportActivity.class);
                Bundle bundle = new Bundle();
                Button button = (Button) v;
                bundle.putString("user_email", (String) v.getTag());
                reportIntent.putExtras(bundle);
                startActivity(reportIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        TextView upVoteCount = new TextView(this);
        upVoteCount.setText("231");
        upVoteCount.setId(UPVOTE_TEXTVIEW_BASE_ID + id);
        LinearLayout.LayoutParams layoutParamsVoteCount = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsVoteCount.setMargins(dpToPx(2f), dpToPx(0f), dpToPx(0f), dpToPx(0f));
        upVoteCount.setLayoutParams(layoutParamsVoteCount);

        CheckBox upVote = new CheckBox(this);
        upVote.setButtonDrawable(R.drawable.upvote);
        upVote.setId(UPVOTE_BASE_ID + id);
        LinearLayout.LayoutParams layoutParamsupUpvote = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsupUpvote.setMargins(dpToPx(5f), dpToPx(0f), dpToPx(0f), dpToPx(0f));
        upVote.setLayoutParams(layoutParamsupUpvote);
        upVote.setOnCheckedChangeListener((buttonView, isChecked) -> {
            LinearLayout linearLayout = (LinearLayout) buttonView.getParent();
            TextView textView = (TextView) linearLayout.getChildAt(1);
            if (isChecked) {
                textView.setText(String.valueOf(Integer.parseInt(textView.getText().toString()) + 1));
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("upVote"+String.valueOf(buttonView.getId()), true).commit();
            } else {
                textView.setText(String.valueOf(Integer.parseInt(textView.getText().toString()) - 1));
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("upVote"+String.valueOf(buttonView.getId()), false).commit();
            }
        });

        CheckBox downVote = new CheckBox(this);
        downVote.setButtonDrawable(R.drawable.downvote);
        downVote.setId(DOWNVOTE_BASE_ID + id);
        LinearLayout.LayoutParams layoutParamsDownvote = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsDownvote.setMargins(dpToPx(10f), dpToPx(0f), dpToPx(0f), dpToPx(0f));
        downVote.setLayoutParams(layoutParamsDownvote);
        downVote.setOnCheckedChangeListener((buttonView, isChecked) -> {
            LinearLayout linearLayout = (LinearLayout) buttonView.getParent();
            TextView textView = (TextView) linearLayout.getChildAt(3);
            if (isChecked) {
                textView.setText(String.valueOf(Integer.parseInt(textView.getText().toString()) + 1));
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("downVote"+String.valueOf(buttonView.getId()), true).commit();
            } else {
                textView.setText(String.valueOf(Integer.parseInt(textView.getText().toString()) - 1));
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("downVote"+String.valueOf(buttonView.getId()), false).commit();
            }
        });

        TextView downVoteCount = new TextView(this);
        downVoteCount.setText("22");
        downVoteCount.setId(DOWNVOTE_TEXTVIEW_BASE_ID + id);
        downVoteCount.setLayoutParams(layoutParamsVoteCount);

        // Define Parent-Child relationships
        usernameAndDate.addView(userNameView);
        usernameAndDate.addView(userDateView);
        descriptionAndReport.addView(userDescriptionView);
        votingSystem.addView(upVote);
        votingSystem.addView(upVoteCount);
        votingSystem.addView(downVote);
        votingSystem.addView(downVoteCount);
        votingSystem.addView(reportButton);
        review.addView(usernameAndDate);
        review.addView(userRateView);
        review.addView(descriptionAndReport);
        review.addView(votingSystem);

        LinearLayout linearLayout = findViewById(R.id.facilityReviews);
        linearLayout.addView(review);
        id++;
    }

    private int dpToPx(float dp) {
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