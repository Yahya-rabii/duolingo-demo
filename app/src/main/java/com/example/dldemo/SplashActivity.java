package com.example.dldemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreen";
    final String ON_BOARDING_SCREEN_VIEWED = "ON_BOARDING_SCREEN_VIEWED";
    Animation mAnimation;
    ImageView splashScreenAppLogo;
    RelativeLayout passRelativeLayout;
    TextView splashScreenAppName, splashScreenMadeInMorocco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        passRelativeLayout = findViewById(R.id.passRelativeLayout); // Added this line to initialize the RelativeLayout containing the 'pass' button

        passRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        });


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        overridePendingTransition(0, R.anim.slide_out_left);

        mAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        splashScreenAppLogo = findViewById(R.id.splashScreenAppLogo);
        splashScreenAppName = findViewById(R.id.splashScreenAppName);
        splashScreenMadeInMorocco = findViewById(R.id.splashScreenMadeInMorocco);

        TextPaint paint = splashScreenMadeInMorocco.getPaint();
        float width = paint.measureText("Made in Morocco");
        Shader textShader = new LinearGradient(0, 0, width, splashScreenMadeInMorocco.getTextSize(),
                new int[]{
                        Color.parseColor("#FF8000"),
                        Color.parseColor("#BBBBBB"),
                        Color.parseColor("#008000"),
                }, null, Shader.TileMode.CLAMP);
        splashScreenMadeInMorocco.getPaint().setShader(textShader);

        splashScreenAppLogo.setAnimation(mAnimation);
        splashScreenAppName.setAnimation(mAnimation);
        splashScreenMadeInMorocco.setAnimation(mAnimation);
        MobileAds.initialize(this);

        splashScreenAppName.postDelayed(() -> {
            Log.d(TAG, "run: Splash Screen Finished");
            SharedPreferences sharedPreferences = getSharedPreferences("OnBoarding", MODE_PRIVATE);
            if (sharedPreferences.getBoolean(ON_BOARDING_SCREEN_VIEWED, false)) {
                Log.d(TAG, "run: starting Login Activity");
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            } else {
                Log.d(TAG, "run: starting On Boarding Activity");
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }, 5000);


        Log.d(TAG, "onCreate: ends");
    }

}
