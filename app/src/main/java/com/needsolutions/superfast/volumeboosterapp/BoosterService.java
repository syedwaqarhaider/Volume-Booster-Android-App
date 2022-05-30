package com.needsolutions.superfast.volumeboosterapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import androidx.core.app.NotificationCompat;
import android.widget.Toast;

import com.needsolutions.superfast.volumeboosterapp.Main.BoosterMainDialog;
import com.needsolutions.superfast.volumeboosterapp.Options.Options;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class BoosterService extends Service {
    private final Messenger messenger = new Messenger(new myHandler(this));
    private SharedPreferences sharedPreferences;
    private Settings settings;
    private long currentTimeMillis;

    public class myHandler extends Handler {
        final BoosterService service;

        public myHandler(BoosterService boosterService) {
            this.service = boosterService;
        }

        public void handleMessage(Message message) {
            BoosterMainDialog.verboseLog("Message: " + message.what);
            switch (message.what) {
                case 2:
                    this.service.settings.setupBoost2(this.service.sharedPreferences);
                    this.service.settings.setBoost();
                    return;
                default:
                    super.handleMessage(message);
                    return;
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return this.messenger.getBinder();
    }

    public void onCreate() {
        this.currentTimeMillis = System.currentTimeMillis();
        BoosterMainDialog.verboseLog("Creating service at " + this.currentTimeMillis);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.settings = new Settings(true);
        this.settings.setupBoost2(this.sharedPreferences);
        if (this.settings.eqAndLeAvailability()) {
            BoosterMainDialog.verboseLog("Success setting up equalizer");
        } else {
            Settings.boost2 = 0;
            this.settings.saveBoost2(this.sharedPreferences);
            if (19 <= Build.VERSION.SDK_INT) {
                Boolean valueOf = false;
                for (AudioEffect.Descriptor descriptor : AudioEffect.queryEffects()) {
                    if (descriptor.uuid.equals(LoudnessEnhancer.EFFECT_TYPE_LOUDNESS_ENHANCER)) {
                        valueOf = true;
                        break;
                    }
                }
                if (valueOf) {
                    Toast.makeText(this, getString(R.string.try_later), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.incompatible), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, getString(R.string.try_later), Toast.LENGTH_SHORT).show();
            }
            BoosterMainDialog.verboseLog("Error setting up equalizer");
        }
        if (Options.getNotification(this.sharedPreferences) != 0) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1234");
            Intent intent = new Intent(this, BoosterMainDialog.class);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);

            Notification a = mBuilder.setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))
                    .setSmallIcon(R.drawable.ic_volume_up_black_24dp)
                    .setContentTitle(getString(R.string.app_name))
                    .setWhen(System.currentTimeMillis())
                    .setTicker(getString(R.string.app_name))
                    .setChannelId("1234")
                    .setContentText(Settings.isBoosting() ? getString(R.string.notification_boost_on) : getString(R.string.notification_boost_off))
                    .build();

            a.flags |= Notification.FLAG_ONGOING_EVENT;
            BoosterMainDialog.verboseLog("notify from service " + a.toString());
            startForeground(1, a);
        }
        if (Settings.isBoosting()) {
            this.settings.setBoost();
        } else {
            this.settings.closeEqAndLe();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.settings.setupBoost2(this.sharedPreferences);
        BoosterMainDialog.verboseLog("disabling equalizer");
        this.settings.destroyEqAndLe();
        BoosterMainDialog.verboseLog("Destroying service");
        if (Options.getNotification(this.sharedPreferences) != 0) {
            stopForeground(true);
        }
    }

    public void onStart(Intent intent, int i) {
        BoosterMainDialog.verboseLog("Starting service");
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        onStart(intent, i);
        return START_STICKY;
    }
}
