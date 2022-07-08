package com.example.help_m5;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONObject;

import java.util.HashMap;

public class RateActivity extends AppCompatActivity {

    private static final String TAG = "RateActivity";
    private final String vm_ip = "http://20.213.243.141:8000/";
    private double rate;
    private String comment;
    private GoogleSignInAccount userAccount;
    private String userEmail;
    private Button submitButton;
    private Button cancelButton;
    private RatingBar ratingBar;
    private String facilityId;
    private int facilityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        Bundle bundle = getIntent().getExtras();
        facilityId = bundle.getString("facility_id");
        facilityType = Integer.parseInt(bundle.getString("facility_type"));

        userAccount = GoogleSignIn.getLastSignedInAccount(this);
        userEmail = userAccount.getEmail();

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar2);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                submitButton.setEnabled(true);
                submitButton.setTextColor(Color.parseColor("#dbba00"));
                rate = ratingBar.getRating();
            }
        });

        EditText editText = findViewById(R.id.editTextTextMultiLine);
        comment = editText.getText().toString();

        submitButton = findViewById(R.id.submit_button);
        submitButton.setEnabled(false);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(RateActivity.this);
                queue.start();

                HashMap<String, String> paramsRate = new HashMap<String, String>();
                paramsRate.put("_id", userEmail);
                paramsRate.put("rateScore", String.valueOf(rate));
                paramsRate.put("facility_type", String.valueOf(facilityType));
                paramsRate.put("facility_id", facilityId);
                JsonObjectRequest requestRate = new JsonObjectRequest(Request.Method.POST, vm_ip+"/user/RateFacility", new JSONObject(paramsRate),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                                System.out.println("response is: "+response.toString());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("onErrorResponse" + "Error: " + error.getMessage());
                            }
                        });
                queue.add(requestRate);

                HashMap<String, String> paramsComment = new HashMap<String, String>();
                paramsRate.put("facilityType", String.valueOf(facilityType));
                paramsRate.put("facility_id", facilityId);
                paramsRate.put("user_id", userEmail);
                paramsRate.put("replyContent", String.valueOf(rate));
                JsonObjectRequest requestComment = new JsonObjectRequest(Request.Method.POST, vm_ip+"/comment/add", new JSONObject(paramsComment),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                                System.out.println("response is: "+response.toString());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("onErrorResponse" + "Error: " + error.getMessage());
                            }
                        });
                queue.add(requestComment);
                finish();
            }
        });

        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void sendRateToDatabase() {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}