package com.bassamworks.rsr.activities.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.bassamworks.rsr.R;
import com.bassamworks.rsr.activities.main.MainActivity;

public class SplashActivity
        extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIMEOUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //To start the main activity after the timeout specified to show the splash screen
        new Handler().postDelayed(getDelayedRunnable(), SPLASH_SCREEN_TIMEOUT);

    }

    private Runnable getDelayedRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        };
    }
}