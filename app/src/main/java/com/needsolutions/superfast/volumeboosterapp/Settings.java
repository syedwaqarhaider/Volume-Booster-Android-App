package com.needsolutions.superfast.volumeboosterapp;

import android.content.SharedPreferences;
import android.media.audiofx.Equalizer;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Build;
import android.util.Log;

import com.needsolutions.superfast.volumeboosterapp.Main.BoosterMainDialog;
import com.needsolutions.superfast.volumeboosterapp.Options.Options;


/**
 * Created by User on 1/12/2018.
 */

public class Settings {
    public static int boost2;
    public boolean isShape = true;
    private short numberBands;
    private short lowerBandLimit;
    private short upperBandLimit;
    private boolean f2189f = true;
    private Equalizer equalizer = null;
    private LoudnessEnhancer loudnessEnhancer = null;



    public Settings(boolean z) {

      // adset = (AdView).findViewById(R.id.adView1);
        //this.adset=ad;
        //adset.loadAd(new AdRequest.Builder().build());
        this.equalizer = null;
        this.loudnessEnhancer = null;
        if (z) {
            if (19 <= Build.VERSION.SDK_INT) {
                try {
                    BoosterMainDialog.verboseLog("Trying LoudnessEnhancer");
                    this.loudnessEnhancer = new LoudnessEnhancer(0);
                    if (z) {
                        this.f2189f = false;
                    } else {
                        this.loudnessEnhancer.release();
                        this.f2189f = true;
                    }
                    BoosterMainDialog.verboseLog("LE set");
                    return;
                } catch (Exception e) {
                    BoosterMainDialog.verboseLog("Error " + e);
                    this.loudnessEnhancer = null;
                }
            }
            if (9 <= Build.VERSION.SDK_INT) {
                try {
                    this.equalizer = new Equalizer(87654325, 0);
                    this.numberBands = this.equalizer.getNumberOfBands();
                    BoosterMainDialog.verboseLog("Set up equalizer, have " + this.numberBands + " bands");
                    this.lowerBandLimit = this.equalizer.getBandLevelRange()[0];
                    this.upperBandLimit = this.equalizer.getBandLevelRange()[1];
                    BoosterMainDialog.verboseLog("range " + this.lowerBandLimit + " " + this.upperBandLimit);
                    if (z) {
                        this.f2189f = false;
                        return;
                    }
                    this.equalizer.release();
                    this.f2189f = true;
                } catch (Exception e2) {
                    BoosterMainDialog.verboseLog("Exception " + e2);
                    this.equalizer = null;
                }
            }
        }
    }

    private void setEqualizer(Equalizer equalizer) {
        BoosterMainDialog.verboseLog("setEqualizer " + boost2);
        if (equalizer != null) {
            short s = (short) (((boost2 * this.upperBandLimit) + 750) / 1500);
            if (s < (short) 0) {
                s = (short) 0;
            }
            short s2 = s > this.upperBandLimit ? this.upperBandLimit : s;
            if (s2 != (short) 0) {
                equalizer.setEnabled(true);
                for (s = (short) 0; s < this.numberBands; s = (short) (s + 1)) {
                    short s3 = (short) 0;
                    if (this.isShape) {
                        int centerFreq = equalizer.getCenterFreq(s) / 1000;
                        if (centerFreq < 150) {
                            s3 = (short) 0;
                        } else if (centerFreq < 250) {
                            s3 = (short) (s2 / 2);
                        } else if (centerFreq > 8000) {
                            s3 = (short) ((s2 * 3) / 4);
                        }
                        BoosterMainDialog.verboseLog("boost " + s + " (" + (this.equalizer.getCenterFreq(s) / 1000) + "hz) to " + s3);
                        BoosterMainDialog.verboseLog("previous value " + this.equalizer.getBandLevel(s));
                        equalizer.setBandLevel(s, s3);
                        //todo MAYBE return?? probar!
                    }
                    s3 = s2;
                    BoosterMainDialog.verboseLog("boost " + s + " (" + (this.equalizer.getCenterFreq(s) / 1000) + "hz) to " + s3);
                    BoosterMainDialog.verboseLog("previous value " + this.equalizer.getBandLevel(s));
                    try {
                        equalizer.setBandLevel(s, s3);
                    } catch (Exception e) {
                        BoosterMainDialog.verboseLog("Error " + e);
                    }
                }
                return;
            }
            equalizer.setEnabled(false);
        }
    }

    public void setBoost() {
        boolean z = true;
        if (this.loudnessEnhancer != null) {
            int i = (boost2 * 750) / 100;
            BoosterMainDialog.verboseLog("setting loudness boost to " + i + " in state " + this.loudnessEnhancer.getEnabled() + " " + this.loudnessEnhancer.hasControl());
            try {
                if (this.loudnessEnhancer.getEnabled() != (i > 0)) {
                    LoudnessEnhancer loudnessEnhancer = this.loudnessEnhancer;
                    if (i <= 0) {
                        z = false;
                    }
                    loudnessEnhancer.setEnabled(z);
                }
                this.loudnessEnhancer.setTargetGain(i);
                return;
            } catch (Exception e) {
                Log.e("VolumeBooster", "le " + e);
                return;
            }
        }
        setEqualizer(this.equalizer);
    }

    public void setupBoost2(SharedPreferences sharedPreferences) {
        boost2 = sharedPreferences.getInt("boost2", 0);
        int b = (Options.getMaximumBoost(sharedPreferences) * 1500) / 100;
        if (boost2 > b) {
            boost2 = b;
        }
        this.isShape = sharedPreferences.getBoolean("shape", true);
    }

    public void saveBoost2(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("boost2", boost2);
        edit.apply();
    }

    public boolean eqAndLeAvailability() {
        return !(this.loudnessEnhancer == null && this.equalizer == null);
    }

    public void destroyEqAndLe() {
        closeEqAndLe();
        if (this.loudnessEnhancer != null) {
            BoosterMainDialog.verboseLog("Destroying le");
            this.loudnessEnhancer.release();
            this.f2189f = true;
            this.loudnessEnhancer = null;
        }
        if (this.equalizer != null) {
            BoosterMainDialog.verboseLog("Destroying equalizer");
            this.equalizer.release();
            this.f2189f = true;
            this.equalizer = null;
        }
    }

    public void closeEqAndLe() {
        if (this.loudnessEnhancer != null && !this.f2189f) {
            BoosterMainDialog.verboseLog("Closing loudnessenhancer");
            this.loudnessEnhancer.setEnabled(false);
        } else if (this.equalizer != null && !this.f2189f) {
            BoosterMainDialog.verboseLog("Closing equalizer");
            this.equalizer.setEnabled(false);
        }
    }

    public static boolean isBoosting() {
        return boost2 > 0;
    }

}
