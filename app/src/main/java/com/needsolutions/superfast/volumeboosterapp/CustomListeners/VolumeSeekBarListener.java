package com.needsolutions.superfast.volumeboosterapp.CustomListeners;

import android.util.Log;
import android.widget.SeekBar;

import com.bullhead.equalizer.AnalogController;
import com.needsolutions.superfast.volumeboosterapp.Main.BoosterMainDialog;
import com.sdsmdg.harjot.crollerTest.Croller;

import static android.media.AudioManager.STREAM_MUSIC;

/**
 * Created by User on 1/12/2018.
 */

public class VolumeSeekBarListener implements Croller.onProgressChangedListener {
    private BoosterMainDialog boosterMainDialog;


    public VolumeSeekBarListener(BoosterMainDialog boosterMainDialog) {
        this.boosterMainDialog = boosterMainDialog;
    }

    @Override
    public void onProgressChanged(int progress) {

        Log.e("Degreeprogress", "" + progress * 5);
        if (!this.boosterMainDialog.volumeWasSet) {
            this.boosterMainDialog.audioManager.setStreamVolume(STREAM_MUSIC, this.boosterMainDialog.percentageToVolume((int) progress * 5), 0);
        }
        this.boosterMainDialog.setVolumeString(progress * 5);


    }
//
//    @Override
//    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//        if(!this.boosterMainDialog.volumeWasSet){
//            this.boosterMainDialog.audioManager.setStreamVolume(STREAM_MUSIC, this.boosterMainDialog.percentageToVolume(i), 0);
//        }
//        this.boosterMainDialog.setVolumeString(i);
//    }
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//
//    }
}
