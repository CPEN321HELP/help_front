package com.example.help_m5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.common.SignInButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;
    final static String TAG = "LoginActivity";
    private static int userid = 1;
    final String Hizan_alibaba_url = "http://47.251.34.10:3000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Google Sign In Button
        signInButton = findViewById(R.id.sign_in_button);
        setButtonText(signInButton, "Sign in with Google");
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN); // If the sign in works, return RC_SIGN_IN
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                handleSignInResult(task);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) throws JSONException {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
            Log.d(TAG, "Trying to load device phone model information");
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        try {
            updateUI(account);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateUI(GoogleSignInAccount account) throws JSONException {
        if (account == null) {
            Log.d(TAG, "There is no user signed in");
        } else {
            Log.d(TAG, "Pref Name: " + account.getDisplayName());
            Log.d(TAG, "Family Name: " + account.getFamilyName());
            Log.d(TAG, "Email: " + account.getEmail());

            // Send token to back-end
            JSONObject userJSON = new JSONObject();
            userJSON.put("user_id", String.valueOf(userid));
            userJSON.put("user_name",account.getDisplayName() + " " + account.getFamilyName());
            userJSON.put("user_email", account.getEmail());
            userJSON.put("user_logo", account.getPhotoUrl());
            userJSON.put("account_type","0");
            userJSON.put("account_status","0");
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Hizan_alibaba_url+"/user/"+String.valueOf(userid), userJSON,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "response is: "+response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse" + "Error: " + error.getMessage());
                    }
                });
            queue.add(request);

            // Move to another activity
            Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(MainIntent);
            Log.d(TAG, "start next activity");
        }
    }

    protected void setButtonText(SignInButton signInButton, String text) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View view = signInButton.getChildAt(i);

            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setText(text);
                return;
            }
        }
    }

}