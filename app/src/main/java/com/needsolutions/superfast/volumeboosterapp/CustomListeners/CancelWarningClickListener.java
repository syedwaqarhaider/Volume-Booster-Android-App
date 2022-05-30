package com.needsolutions.superfast.volumeboosterapp.CustomListeners;

import android.content.DialogInterface;

import com.needsolutions.superfast.volumeboosterapp.Main.BoosterMainDialog;


/**
 * Created by User on 1/12/2018.
 */

public class CancelWarningClickListener implements DialogInterface.OnClickListener {
    private BoosterMainDialog boosterMainDialog;

    public CancelWarningClickListener(BoosterMainDialog boosterMainDialog) {
        this.boosterMainDialog = boosterMainDialog;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        this.boosterMainDialog.finish();
    }
}