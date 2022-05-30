package com.needsolutions.superfast.volumeboosterapp.CustomListeners;

import android.util.Log;
import android.widget.SeekBar;

import com.bullhead.equalizer.AnalogController;
import com.needsolutions.superfast.volumeboosterapp.Main.BoosterMainDialog;
import com.sdsmdg.harjot.crollerTest.Croller;

import static com.needsolutions.superfast.volumeboosterapp.Settings.boost2;


/**
 * Created by User on 1/12/2018.
 */

public class BoostSeekBarListener implements Croller.onProgressChangedListener {
    private BoosterMainDialog boosterMainDialog;

    public BoostSeekBarListener(BoosterMainDialog boosterMainDialog) {
        this.boosterMainDialog = boosterMainDialog;
    }

    @Override
    public void onProgressChanged(int progress) {
        Log.d("Waqar"," "+progress);
        Log.e("BoostSeakbar__", "" + progress);
        int i2 = 1;
        BoosterMainDialog.verboseLog("progress changed");
        //if (fromUser) {
        int i3 = boost2;
        boost2 = this.boosterMainDialog.conversion2(progress * 100, 0, 1500);
        BoosterMainDialog.verboseLog("setting " + boost2);
        this.boosterMainDialog.settings.saveBoost2(this.boosterMainDialog.sharedPreferences);
        int i4 = boost2 == 0 ? 1 : 0;
        if (i3 != 0) {
            i2 = 0;
        }
        if (i4 != i2) {
            this.boosterMainDialog.needService();
        } else {
            this.boosterMainDialog.sendMessage(2, 0, 0);
        }
        //}
        this.boosterMainDialog.setBoostString(progress*100);
    }

//    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
//        int i2 = 1;
//        BoosterMainDialog.verboseLog("progress changed");
//        if (z) {
//            int i3 = boost2;
//            boost2 = this.boosterMainDialog.conversion2(i, 0, 1500);
//            BoosterMainDialog.verboseLog("setting " + boost2);
//            this.boosterMainDialog.settings.saveBoost2(this.boosterMainDialog.sharedPreferences);
//            int i4 = boost2 == 0 ? 1 : 0;
//            if (i3 != 0) {
//                i2 = 0;
//            }
//            if (i4 != i2) {
//                this.boosterMainDialog.needService();
//            } else {
//                this.boosterMainDialog.sendMessage(2, 0, 0);
//            }
//        }
//        this.boosterMainDialog.setBoostString(i);
//    }
//
//    public void onStartTrackingTouch(SeekBar seekBar) {
//    }
//
//    public void onStopTrackingTouch(SeekBar seekBar) {
//    }
}
