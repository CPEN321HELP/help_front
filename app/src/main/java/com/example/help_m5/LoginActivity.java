package com.example.help_m5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.help_m5.ui.database.DatabaseConnection;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private int RC_SIGN_IN = 1;
    final static String TAG = "LoginActivity";
    private static int userid = 1;
    private final String vm_ip = "http://20.213.243.141:8000/";

    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;

    private int userType;

    private static final String ONESIGNAL_APP_ID = "f38cdc86-9fb7-40a5-8176-68b4115411da";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        OneSignal.setNotificationOpenedHandler(
                new OneSignal.OSNotificationOpenedHandler() {
                    @Override
                    public void notificationOpened(OSNotificationOpenedResult result) {
                        String actionId = result.getAction().getActionId();
                        OSNotificationAction.ActionType type = result.getAction().getType(); // "ActionTaken" | "Opened"
                        String title = result.getNotification().getTitle();
                        String message = result.getNotification().getBody();
                        Intent intent = new Intent(LoginActivity.this, FacilityActivity.class);
                        startActivity(intent);
                    }
                });


        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                    return;
                }

                // Get new FCM registration token
                String token = task.getResult();

                // Log and toast
                //String msg = getString(R.string.msg_token_fmt, token);
                //Log.d(TAG, msg);
                //Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

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
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //try {
        //    updateUI(account);
        //} catch (JSONException e) {
        //    e.printStackTrace();
        //}
    }

    private void updateUI(GoogleSignInAccount account) throws JSONException {
        if (account == null) {
            Log.d(TAG, "There is no user signed in");
        } else {
            // Send token to back-end
            RequestQueue queue = Volley.newRequestQueue(this);
            HashMap<String, String> params = new HashMap<String, String>();
            queue.start();
            params.put("_id", account.getEmail());
            params.put("username", account.getDisplayName());
            if (account.getPhotoUrl() != null) {
                params.put("user_logo", account.getPhotoUrl().toString());
            } else {
                params.put("user_logo", "none");
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, vm_ip+"google_sign_up", new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("response is: "+response.toString());
                        DatabaseConnection DBconnection = new DatabaseConnection();
                        String userInfo = DBconnection.readFromJson(LoginActivity.this, "specific_facility.json");

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error: " + error.getMessage());
                    }
                });
            queue.add(request);

            // Move to another activity
            Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("user_name", account.getDisplayName());
            bundle.putString("user_email", account.getEmail());
            bundle.putInt("user_type", 0);
            if (account.getPhotoUrl() != null) {
                bundle.putString("user_icon", account.getPhotoUrl().toString());
            } else {
                bundle.putString("user_icon", "none");
            }
            MainIntent.putExtras(bundle);
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