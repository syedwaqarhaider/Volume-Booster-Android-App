package com.needsolutions.superfast.volumeboosterapp.CustomListeners;

import android.content.DialogInterface;

import com.needsolutions.superfast.volumeboosterapp.Main.BoosterMainDialog;

/**
 * Created by User on 1/12/2018.
 */

public class AcceptWarningClickListener implements DialogInterface.OnClickListener{
    private BoosterMainDialog boosterMainDialog;

    public AcceptWarningClickListener(BoosterMainDialog boosterMainDialog) {
        this.boosterMainDialog = boosterMainDialog;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.boosterMainDialog.sharedPreferences.edit().putInt("warnedLastVersion", this.boosterMainDialog.currentVersionCode).apply();
    }
    
}
