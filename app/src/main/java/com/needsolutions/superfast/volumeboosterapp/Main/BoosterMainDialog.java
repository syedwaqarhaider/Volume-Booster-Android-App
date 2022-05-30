package com.needsolutions.superfast.volumeboosterapp.Main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;

import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bullhead.equalizer.AnalogController;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.kobakei.ratethisapp.RateThisApp;
import com.needsolutions.superfast.volumeboosterapp.BoosterService;
import com.needsolutions.superfast.volumeboosterapp.CustomListeners.AcceptHelpClickListener;
import com.needsolutions.superfast.volumeboosterapp.CustomListeners.AcceptWarningClickListener;
import com.needsolutions.superfast.volumeboosterapp.CustomListeners.BoostSeekBarListener;
import com.needsolutions.superfast.volumeboosterapp.CustomListeners.CancelWarningClickListener;
import com.needsolutions.superfast.volumeboosterapp.CustomListeners.VolumeSeekBarListener;
import com.needsolutions.superfast.volumeboosterapp.CustomListeners.onCancelHelpListener;
import com.needsolutions.superfast.volumeboosterapp.CustomListeners.onCancelWarningListener;
import com.needsolutions.superfast.volumeboosterapp.Options.Options;
import com.needsolutions.superfast.volumeboosterapp.Options.OptionsActivity;
import com.needsolutions.superfast.volumeboosterapp.R;
import com.needsolutions.superfast.volumeboosterapp.Settings;
import com.needsolutions.superfast.volumeboosterapp.Utils.NotificationChannelManager;
import com.needsolutions.superfast.volumeboosterapp.web_view;
import com.sdsmdg.harjot.crollerTest.Croller;
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener;

import java.lang.annotation.Native;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.gsottbauer.equalizerview.EqualizerView;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.media.AudioManager.ADJUST_LOWER;
import static android.media.AudioManager.ADJUST_RAISE;
import static android.media.AudioManager.STREAM_MUSIC;
import static android.media.AudioManager.STREAM_SYSTEM;
import static com.needsolutions.superfast.volumeboosterapp.R.drawable.*;

public class BoosterMainDialog extends AppCompatActivity implements ServiceConnection {
    private static boolean loggingBoolean = true;
    public SharedPreferences sharedPreferences;
    private Messenger messenger;
    private int boostConstant = 10000;
    public Settings settings;
    public AudioManager audioManager;
    private boolean volumeVisibility = true;
    public boolean volumeWasSet;
    public int currentVersionCode;
    private ImageButton btnMute;
    private Button btnThirty, btnSixty, btnHundred;
    private MediaPlayer mediaPlayer;

    private SeekBar sb_volume;
    private Croller sb_boost;
    @BindView(R.id.layout_volume)
    LinearLayout layout_volume;
    @BindView(R.id.tv_volume)
    TextView tv_volume;
    @BindView(R.id.tv_boost)
    TextView tv_boost;
    @BindView(R.id.equalizer_view)
    EqualizerView equalizer_view;
    Button Sharesb;
    private boolean isAdLoaded = false;
    private final String TAG = BoosterMainDialog.class.getSimpleName();
    private Button refreshed;
    //private UnifiedNativeAd nativeAd;
    private com.facebook.ads.InterstitialAd interstitialAd;
    private NativeAd nativeAd;
    private boolean isPaused=true;
    private ImageView play;

    private void loadNativeAd() {
        ;
        nativeAd = new NativeAd(this, getString(R.string.fb_native));
        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed

                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                //inflateAd(nativeAd);

            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
            }
        };

        // Request an ad
        nativeAd.loadAd(nativeAd.buildLoadAdConfig()
                .withAdListener(nativeAdListener)
                .build());
    }

   /* private void inflateAd(NativeAd nativeAd) {
        Log.e("NativeAds", "" + nativeAd);
        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        NativeAdLayout nativeAdLayout = findViewById(R.id.native_ad_container);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(getApplicationContext(), nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView, nativeAdMedia, nativeAdIcon, clickableViews);
    }

    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            this.currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            this.currentVersionCode = 0;
        }
        this.audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        setContentView(R.layout.activity_main_nad);
        loadNativeAd();
        loadInterstitialfb();
        this.setFinishOnTouchOutside(false);
        ButterKnife.bind(this);
        this.settings = new Settings(false);
        // this.settings = new Settings(false,adView);
        this.sharedPreferences.edit().putBoolean("volumeControl", this.sharedPreferences.getBoolean("volumeControl", Options.isKindleFire())).apply();
        RateThisApp.Config config = new RateThisApp.Config(2, 5);
        config.setTitle(R.string.rate_app_title);
        config.setMessage(R.string.rate_app_message);
        config.setYesButtonText(R.string.rate_app_yes);
        config.setNoButtonText(R.string.rate_app_no);
        config.setCancelButtonText(R.string.rate_app_later);
        RateThisApp.init(config);
        RateThisApp.onCreate(this);

       /* Sharesb = (Button) findViewById(R.id.sharesb);
        // If the condition is satisfied, "Rate this app" dialog will be shown
        RateThisApp.showRateDialogIfNeeded(this);
        Sharesb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String shareBody = "https://play.google.com/store/apps/details?id=" + getPackageName();
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Share app");
                    intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(intent, getString(R.string.app_name)));
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });
*/

        this.btnMute=(ImageButton) findViewById(R.id.btnMute);
        this.btnThirty=(Button) findViewById(R.id.btnThirty);
        this.btnSixty=(Button) findViewById(R.id.btnSixty);
        this.btnHundred=(Button) findViewById(R.id.btnHundred);
        sb_volume = findViewById(R.id.sb_volume);
        sb_boost = findViewById(R.id.sb_boost);
        play= (ImageView) findViewById(R.id.play);
        mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.music);
        sb_volume.setProgress(audioManager.getStreamVolume(STREAM_MUSIC));
        sb_volume.setMax(audioManager.getStreamMaxVolume(STREAM_MUSIC));
        sb_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setupEq();

        NotificationChannelManager.createNotificationChannel(this);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkIfWarned();
        volumeWasSet = false;
        this.settings.setupBoost2(this.sharedPreferences);
        verboseLog("loaded boost = " + Settings.boost2);
        int b = Options.getMaximumBoost(this.sharedPreferences);
        this.sb_boost.setProgress((this.boostConstant * b) / 100);
        //  this.sb_boost.getThumb().setColorFilter(ContextCompat.getColor(this,R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        // this.sb_boost.getProgressDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        if (Settings.boost2 > (b * 1500) / 100) {
            Settings.boost2 = (b * 1500) / 100;
            this.settings.saveBoost2(this.sharedPreferences);
        }
        setupEq();
        needService();
        doNotify(this, this.sharedPreferences, (NotificationManager) getSystemService(NOTIFICATION_SERVICE), this.settings);
        setupVolumeBar();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        verboseLog("connected");
        this.messenger = new Messenger(iBinder);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        verboseLog("disconnected");
        this.messenger = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        int i2 = 0;
        AudioManager audioManager = this.audioManager;
        if (!this.volumeVisibility) {
            i2 = 1;
        }
        verboseLog("Max vol: " + audioManager.getStreamMaxVolume(STREAM_MUSIC));
        verboseLog("Previous vol: " + audioManager.getStreamVolume(STREAM_MUSIC));
        switch (keyCode) {
            case 24: {
                audioManager.adjustStreamVolume(STREAM_MUSIC, ADJUST_RAISE, i2);
                volumeWasSet = true;
                break;
            }
            case 25: {
                audioManager.adjustStreamVolume(STREAM_MUSIC, ADJUST_LOWER, i2);
                volumeWasSet = true;
                break;
            }
            default: {
                return super.onKeyDown(keyCode, event);
            }
        }
        verboseLog("Actual vol: " + audioManager.getStreamVolume(STREAM_MUSIC));
        setCurrentVolume();
        return true;
    }

//-------------------------------------Custom Methods-----------------------------------------------

    private void checkIfWarned() {
        verboseLog("version " + this.currentVersionCode);
        if (this.sharedPreferences.getInt("warnedLastVersion", 0) != this.currentVersionCode) {
            createWarningDialog();
        }
    }

    private void loadInterstitialfb() {
        interstitialAd = new com.facebook.ads.InterstitialAd(BoosterMainDialog.this, getString(R.string.fb_intersitial));
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                startActivity(new Intent(BoosterMainDialog.this, OptionsActivity.class));
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                isAdLoaded = false;
            }

            @Override
            public void onAdLoaded(Ad ad) {
                isAdLoaded = true;
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };

        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());

    }

    private void createWarningDialog() {
        AlertDialog create = new AlertDialog.Builder(this).create();
        Settings.boost2 = 0;
        this.settings.saveBoost2(this.sharedPreferences);
        this.sb_boost.setProgress(0);
        sendMessage(2, 0, 0);
        create.setTitle(getString(R.string.warning_title));
        create.setMessage(getString(R.string.warningText));
        create.setButton(BUTTON_POSITIVE, getString(R.string.accept), new AcceptWarningClickListener(this));
        create.setButton(BUTTON_NEGATIVE, getString(R.string.cancel), new CancelWarningClickListener(this));
        create.setOnCancelListener(new onCancelWarningListener(this));
        create.show();
    }

    private void createHelpDialog(String title, String content) {
        AlertDialog create = new AlertDialog.Builder(this).create();
        create.setTitle(title);
        create.setMessage(content);
        create.setButton(BUTTON_POSITIVE, getString(R.string.accept), new AcceptHelpClickListener(this));
        create.setOnCancelListener(new onCancelHelpListener(this));
        create.show();
    }

    private void createOptionDialog(final BoosterMainDialog parent) {
        CharSequence[] charSequenceArr = new String[]{
                getString(R.string.help),
                getString(R.string.settings),
                getString(R.string.more_apps),
                getString(R.string.privacytext),
                "Games",
                getString(R.string.close)};
        final AlertDialog create = new AlertDialog.Builder(this).setItems(charSequenceArr, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        parent.createHelpDialog(getString(R.string.help_title), getString(R.string.helpText));
                        break;
                    case 1:
                        parent.startActivity(new Intent(parent, OptionsActivity.class));
                        if (isAdLoaded)
                            interstitialAd.show();
                        else
                            parent.startActivity(new Intent(parent, OptionsActivity.class));
                        break;
                    case 2:
                        parent.showMoreApps(true);
                        break;
                    case 3:
                        //Privacy url here
                        try {
                            Uri uri = Uri.parse(getString(R.string.privacyLink));// // missing 'http://' will cause crashed
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        } catch (Exception ignored) {
                        }
                        break;
                    case 4:
                        startActivity(new Intent(getApplicationContext(), web_view.class));
                        break;
                    case 5:
                        finish();
                        break;
                }
            }
        }).create();
        create.setTitle(getString(R.string.option_title));
        create.show();
    }

    private void setupVolumeBar() {
        if (this.sharedPreferences.getBoolean("volumeControl", Options.isKindleFire())) {
            this.sb_volume.setProgress(1);
            this.volumeVisibility = true;
            this.layout_volume.setVisibility(View.VISIBLE);
            this.sb_volume.setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener) new VolumeSeekBarListener(this));
            // this.sb_volume.getThumb().setColorFilter(ContextCompat.getColor(this,R.color.colorAccent), PorterDuff.Mode.SRC_IN);
            // this.sb_volume.getProgressDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
            setCurrentVolume();
            return;
        }
        this.layout_volume.setVisibility(View.VISIBLE);
        this.volumeVisibility = true;
    }

    void setupEq() {
        verboseLog("setupEqualizer");
        this.sb_boost.setOnProgressChangedListener(new BoostSeekBarListener(this));
        //int i = this.sharedPreferences.getInt("boost2", 0);
        //Settings settings = this.settings;
        //i = conversion1(i, 0, 1500);
        this.sb_boost.setProgress(0);
        setBoostString(10);
    }

    private void setCurrentVolume() {
        int progress = volumeToPercentage(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        this.sb_volume.setProgress(progress);
        setVolumeString(progress);
    }

    public void setVolumeString(int i) {
        tv_volume.setText("Vol.: " + i + "%");
        this.volumeWasSet = false;
    }

    public void setBoostString(int i) {
        int boost = (((i * 1000) + (this.boostConstant / 2)) / this.boostConstant);

        if (boost == 10) {
            equalizer_view.stopBars();
        } else {
            equalizer_view.animateBars();
        }

        float scale = (1.0f + (boost / 100.0f));

        Animation anim = new ScaleAnimation(
                1f, 1f, // Start and end values for the X axis scaling
                1f, scale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(100);
        equalizer_view.startAnimation(anim);

        tv_boost.setText("Boost: " + i * 10 / 190 + "%");
        Log.e("Boostis", "" + boost);
        Log.e("Boostis", "" + scale);
    }

    public int volumeToPercentage(int value) {
        return (int) Math.ceil((value * 100.0) / this.audioManager.getStreamMaxVolume(STREAM_MUSIC));
    }

    public int percentageToVolume(int percentage) {
        return (int) ((percentage * this.audioManager.getStreamMaxVolume(STREAM_MUSIC)) / 100.0);
    }

    private int conversion1(int i, int i2, int i3) {//volumen actual, var, vol maximo del stream
        return (((i - i2) * this.boostConstant) + ((i3 - i2) / 2)) / (i3 - i2);
    }

    public int conversion2(int i, int i2, int i3) {
        return ((((this.boostConstant - i) * i2) + (i3 * i)) + (this.boostConstant / 2)) / this.boostConstant;
    }

    public static void verboseLog(String str) {
        if (loggingBoolean) {
            Log.v("VolumeBooster", str);
        }
    }

    public void sendMessage(int i, int i2, int i3) {
        if (this.messenger != null) {
            try {
                verboseLog("message " + i + " " + i2 + " " + i3);
                this.messenger.send(Message.obtain(null, i, i2, i3));
            } catch (RemoteException e) {
                Log.e("VolumeBooster", "sendMessage: ", e);
            }
        }
    }

    public static void doNotify(Context context, SharedPreferences sharedPreferences, NotificationManager notificationManager, Settings settings) {
        verboseLog("doNotify " + Options.getNotification(sharedPreferences));
        switch (Options.getNotification(sharedPreferences)) {
            case 0:
                notificationManager.cancelAll();
                break;
            case 1:
                if (Settings.isBoosting()) {
                    sendNotification(context, notificationManager);
                    break;
                }
                verboseLog("trying to cancel notification");
                notificationManager.cancelAll();
                break;
            case 2:
                sendNotification(context, notificationManager);
                break;
        }
    }

    public static void sendNotification(Context context, NotificationManager notificationManager) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, context.getResources().getString(R.string.app_name));
        Intent intent = new Intent(context, BoosterMainDialog.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);

        Notification a = mBuilder.setContentIntent(PendingIntent.getActivity(context, 0, intent, 0))
                .setSmallIcon(Settings.isBoosting() ? ic_volume_up_black_24dp : ic_volume_mute_black_24dp)
                .setContentTitle(context.getString(R.string.app_name))
                .setWhen(System.currentTimeMillis())
                .setTicker(context.getString(R.string.app_name))
                .setChannelId("1234")
                .setContentText(Settings.isBoosting() ? context.getString(R.string.notification_boost_on) : context.getString(R.string.notification_boost_off))
                .build();

        a.flags |= Notification.FLAG_ONGOING_EVENT;
        //a.flags |= Notification.FLAG_ONGOING_EVENT;

        notificationManager.notify(1, a);
    }

    public void needService() {
        verboseLog("needService = " + Settings.isBoosting());
        restartOrStop(Settings.isBoosting());
    }

    void serviceStart(boolean z) {
        serviceStop();
        //emptyMethod();
        verboseLog("starting service");
        Intent intent = new Intent(this, BoosterService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
        if (z) {
            serviceBind();
        }
    }

    void serviceStop() {
        verboseLog("stop service");
        if (this.messenger != null) {
            unbindService(this);
            this.messenger = null;
        }
        stopService(new Intent(this, BoosterService.class));
    }

    void restartOrStop(boolean z) {
        if (z) {
            verboseLog("restartService");
            serviceStart(true);
            return;
        }
        verboseLog("stopService");
        serviceStop();
        doNotify(this, this.sharedPreferences, (NotificationManager) getSystemService(NOTIFICATION_SERVICE), this.settings);
    }

    void serviceBind() {
        verboseLog("bind");
        bindService(new Intent(this, BoosterService.class), this, 0);
    }

    void showMoreApps(boolean z) {
        //todo MarketDetector.showMoreApps(this, z);
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Need+Solutions+Pvt+Ltd")));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Need+Solutions+Pvt+Ltd")));
        }
    }

    public void muteBoster(View view) {
        btnMute.setBackground(getDrawable(button_green));
        btnThirty.setBackground(getDrawable(button_gray));
        btnSixty.setBackground(getDrawable(button_gray));
        btnHundred.setBackground(getDrawable(button_gray));
        sb_boost.setProgress(0);
    }

    public void boostThirty(View view) {
        btnMute.setBackground(getDrawable(button_gray));
        btnThirty.setBackground(getDrawable(button_green));
        btnSixty.setBackground(getDrawable(button_gray));
        btnHundred.setBackground(getDrawable(button_gray));
        sb_boost.setProgress(30);
    }

    public void boostSixty(View view) {
        btnMute.setBackground(getDrawable(button_gray));
        btnThirty.setBackground(getDrawable(button_gray));
        btnSixty.setBackground(getDrawable(button_green));
        btnHundred.setBackground(getDrawable(button_gray));
        sb_boost.setProgress(60);
    }

    public void boostHundred(View view) {
        btnMute.setBackground(getDrawable(button_gray));
        btnThirty.setBackground(getDrawable(button_gray));
        btnSixty.setBackground(getDrawable(button_gray));
        btnHundred.setBackground(getDrawable(button_green));
        sb_boost.setProgress(100);
    }



   // --------------------------------Butterknife injected methods--------------------------------------
    @OnClick(R.id.stop)
    public void stopApp(View view) {
        //displayInterstitial();
        // showinterstitialfb();
        this.serviceStop();
        try {
            mediaPlayer.release();
            mediaPlayer.stop();
            ((NotificationManager) this.getSystemService(NOTIFICATION_SERVICE)).cancelAll();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        this.onBackPressed();
    }

    public void play_PauseMusic(View view) {
        if(isPaused)
        {
            play.setImageResource(ic_baseline_pause_circle_24);
            mediaPlayer.start();
            isPaused=false;
        }
        else
        {
            play.setImageResource(ic_baseline_play_circle_24);
            mediaPlayer.pause();
            isPaused=true;
        }

    }

    public void showMenu(View view) {
        Toast.makeText(getApplicationContext(),"Menu Will Appear", Toast.LENGTH_LONG).show();
    }

   /* @OnClick(R.id.config)
    public void showConfig() {
        createOptionDialog(this);
    }

     */

}
