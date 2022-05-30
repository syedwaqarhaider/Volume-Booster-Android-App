package com.needsolutions.superfast.volumeboosterapp.Options;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.needsolutions.superfast.volumeboosterapp.R;


/**
 * Created by User on 1/07/2019.
 */

public class Options extends PreferenceFragment {
    public static int getNotification(SharedPreferences sharedPreferences) {
        return Integer.parseInt(sharedPreferences.getString("notification", "2"));
    }

    public static boolean isKindleFire() {
        return Build.MODEL.equalsIgnoreCase("Kindle Fire");
    }

    public static int getMaximumBoost(SharedPreferences sharedPreferences) {
        try {
            int parseInt = Integer.parseInt(sharedPreferences.getString("maximumBoost", "-1"));
            if (parseInt < 0) {
                return Integer.parseInt(sharedPreferences.getString("maximumBoost2", "60"));
            }
            sharedPreferences.edit().putString("maximumBoost", "-1").apply();
            return parseInt >= 60 ? 60 : parseInt;
        } catch (NumberFormatException e) {
            return 60;
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.options);
    }

    public void onResume() {
        super.onResume();
    }

    public void onStop() {
        super.onStop();
    }
}
