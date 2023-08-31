package com.flitzen.pinnacle.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.utils.SharePref;

public class SplashActivity_2 extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_2);

        sharedPreferences = SharePref.getSharePref(SplashActivity_2.this);

        Handler handler;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedPreferences.getBoolean(SharePref.isLoggedIn,false)){
                    //startActivity(new Intent(SplashActivity_2.this, DrawerActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    startActivity(new Intent(SplashActivity_2.this, PinActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashActivity_2.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                overridePendingTransition(0, 0);

            }
        }, 2000);
    }
}