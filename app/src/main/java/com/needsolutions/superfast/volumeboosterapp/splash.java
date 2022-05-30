package com.needsolutions.superfast.volumeboosterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import com.needsolutions.superfast.volumeboosterapp.Main.BoosterMainDialog;


public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        hideSystemUI(splash.this);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(findViewById(R.id.logo_image), "translationY", -1000, 0),
                ObjectAnimator.ofFloat(findViewById(R.id.logo_image), "alpha", 0, 1),
                ObjectAnimator.ofFloat(findViewById(R.id.logo_text), "translationX", -2000, 0)
        );
        // animatorSet.setInterpolator(new DescelerateInterpolator());
        animatorSet.setDuration(2000);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                AnimatorSet animatorSet2 = new AnimatorSet();
                animatorSet2.playTogether(
                        ObjectAnimator.ofFloat(findViewById(R.id.logo_image), "scaleX", 1f, 0.5f, 1f),
                        ObjectAnimator.ofFloat(findViewById(R.id.logo_image), "scaleY", 1f, 0.5f, 1f)
                );
                animatorSet2.setInterpolator(new AccelerateInterpolator());
                animatorSet2.setDuration(1000);
                animatorSet2.start();

            }
        });
        animatorSet.start();
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            startActivity(new Intent(splash.this, BoosterMainDialog.class));
            finish();
        }, 6000);
    }

    public static void hideSystemUI(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
