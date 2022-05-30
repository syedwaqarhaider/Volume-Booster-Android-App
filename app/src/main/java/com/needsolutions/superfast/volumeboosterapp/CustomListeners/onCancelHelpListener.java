package com.needsolutions.superfast.volumeboosterapp.CustomListeners;

import android.content.DialogInterface;

import com.needsolutions.superfast.volumeboosterapp.Main.BoosterMainDialog;


/**
 * Created by User on 1/12/2018.
 */

public class onCancelHelpListener implements DialogInterface.OnCancelListener {
    private BoosterMainDialog boosterMainDialog;

    public onCancelHelpListener(BoosterMainDialog boosterMainDialog) {
        this.boosterMainDialog = boosterMainDialog;
    }

    public void onCancel(DialogInterface dialogInterface) {
    }
}