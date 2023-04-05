package com.example.dldemo.Activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dldemo.Helpers.InstentScore;
import com.example.dldemo.R;

public class LevelsActivity extends Activity {


   public static String lang;
   public static String lev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        Button beginnerBtn = findViewById(R.id.beginner_button);
        Button intermediateBtn = findViewById(R.id.intermediate_button);
        Button advancedBtn = findViewById(R.id.advanced_button);
        String language = getIntent().getStringExtra("Language");
        lang = language;

        beginnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelsActivity.this, TestActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LevelsActivity.this, beginnerBtn, "beginner_transition");
                startActivity(intent, options.toBundle());
                InstentScore.resetsc();
                intent.putExtra("Language", language);
                intent.putExtra("level", "beginner");
                lev ="beginner";
                startActivity(intent);


            }
        });

        intermediateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelsActivity.this, TestActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LevelsActivity.this, intermediateBtn, "intermediate_transition");
                InstentScore.resetsc();
                intent.putExtra("Language", language);
                intent.putExtra("level", "intermediate");
                lev ="intermediate";
                startActivity(intent, options.toBundle());
            }
        });

        advancedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelsActivity.this, TestActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LevelsActivity.this, advancedBtn, "advanced_transition");
                InstentScore.resetsc();
                intent.putExtra("Language", language);
                intent.putExtra("level", "advanced");
                lev ="advanced";
                startActivity(intent, options.toBundle());
            }
        });
    }
}
