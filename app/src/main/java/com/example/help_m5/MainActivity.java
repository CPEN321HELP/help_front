package com.example.help_m5;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import com.example.help_m5.databinding.ActivityMainBinding;
import com.example.help_m5.ui.database.DatabaseConnection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DatabaseConnection db;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

//        db = new DatabaseConnection();
//        db.cleanCaches(getApplicationContext());
        //db.getFacilities(binding, 0, 1, getApplicationContext(), false, false, "");
        //db.getFacilities(binding, 1, 1, getApplicationContext(), false, false, "");
        //db.getFacilities(binding, 2, 1, getApplicationContext(), false, false, "");
        //db.getFacilities(binding, 3, 1, getApplicationContext(), false, false, "");
        //db.getFacilities(binding, 5, 1, getApplicationContext(), false, true, "");
        //db.getFacilities(binding, 6, 1, getApplicationContext(), false, true, "");

        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
//        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_report, R.id.nav_add_facility)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_report).setVisible(false);

        NotificationChannel channel = new NotificationChannel("notification_channel", "notification_channel", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                    return;
                }

                // Get new FCM registration token
                String token = task.getResult();
                DatabaseConnection DBconnection = new DatabaseConnection();
                DBconnection.sendToken(getApplicationContext(),token);
                // Log and toast
                Log.d(TAG, token);
                Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
            }
        });

//        Bundle bundle = getIntent().getExtras();
//        String userName = bundle.getString("user_name");
//        String userEmail = bundle.getString("user_email");
//        TextView userNameView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userName);
//        userNameView.setText(userName);
//        TextView userEmailView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userEmail);
//        userEmailView.setText(userEmail);
//        if (!bundle.getString("user_icon").equals("none")) {
//            Uri userIcon = Uri.parse(bundle.getString("user_icon"));
//            Picasso.get().load(userIcon).into((ImageView) navigationView.getHeaderView(0).findViewById(R.id.userIcon));
//        }
    }

    private void displaySharedPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}