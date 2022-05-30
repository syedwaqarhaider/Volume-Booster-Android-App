package com.needsolutions.superfast.volumeboosterapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;

/**
 * Created by User on 1/16/2018.
 */

public class OnBootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            if (defaultSharedPreferences.getInt("warnedLastVersion", 0) == context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode) {
                Settings settings = new Settings(false);
                settings.setupBoost2(defaultSharedPreferences);
                boolean z = defaultSharedPreferences.getBoolean("autoStart", true);
                if (Settings.isBoosting() && z) {
                    Intent intent2 = new Intent(context, BoosterService.class);
                    if (Build.VERSION.SDK_INT >= 26) {
                        context.startForegroundService(intent2);
                    } else {
                        context.startService(intent2);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
    }
}
