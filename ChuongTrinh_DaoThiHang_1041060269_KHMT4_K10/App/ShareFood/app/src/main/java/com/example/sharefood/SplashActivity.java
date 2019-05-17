package com.example.sharefood;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.example.sharefood.constant.Key;
import com.example.sharefood.model.Favorite;
import com.example.sharefood.util.Preferences;
import com.example.sharefood.util.Utils;
import com.google.gson.Gson;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String fav = Preferences.getData(Key.FAV, this);
        if (fav.equals("")){
            Favorite favorite = new Favorite();
            Preferences.saveData(Key.FAV, new Gson().toJson(favorite), this);
        }

        if (!Utils.checkPermission(this) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startActivity(new Intent(this, PermissionActivity.class));
            finish();
            return;
        }

        CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashActivity.this, Main2Activity.class));
            }
        }.start();
    }
}
