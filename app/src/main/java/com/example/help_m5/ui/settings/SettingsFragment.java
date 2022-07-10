package com.example.help_m5.ui.settings;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.example.help_m5.R;


import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "SettingsFragment";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);


        SwitchPreference app_theme = findPreference("app_theme_sw");
        if(app_theme != null){
            app_theme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference arg0, Object isVibrateOnObject) {
                    boolean isVibrateOn = (Boolean) isVibrateOnObject;
                    if (isVibrateOn) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                    return true;
                }
            });
        }
//        SwitchPreference sign_out = findPreference("sign_out");
//        if(sign_out != null){
//            sign_out.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//                @Override
//                public boolean onPreferenceChange(Preference arg0, Object isVibrateOnObject) {
//                    boolean isVibrateOn = (Boolean) isVibrateOnObject;
//                    if (isVibrateOn) {
//                        DatabaseConnection db = new DatabaseConnection();
//                        db.removeFile("/data/data/com.example.help_m5/files/userInfo.json");
//                        Toast.makeText(getContext(), "please exist", Toast.LENGTH_LONG).show();
//                    }
//                    return true;
//                }
//            });
//        }

    }

}
