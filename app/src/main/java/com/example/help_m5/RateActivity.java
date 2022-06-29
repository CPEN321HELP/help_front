package com.example.help_m5;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;

public class RateActivity extends AppCompatActivity {

    private double rate;
    private String comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar2);
        rate = ratingBar.getRating();

        EditText editText = findViewById(R.id.editTextTextMultiLine);
        comment = editText.getText().toString();
    }
}