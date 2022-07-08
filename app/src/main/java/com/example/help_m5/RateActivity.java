package com.example.help_m5;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class RateActivity extends AppCompatActivity {

    private static final String TAG = "RateActivity";
    private double rate;
    private String comment;
    private GoogleSignInAccount userAccount;
    private String userEmail;
    private Button submitButton;
    private Button cancelButton;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

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
                System.out.println("send to backend");
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