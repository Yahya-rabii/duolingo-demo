package com.example.dldemo;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LevelsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        Button beginnerBtn = findViewById(R.id.beginner_button);
        Button intermediateBtn = findViewById(R.id.intermediate_button);
        Button advancedBtn = findViewById(R.id.advanced_button);


        String language = getIntent().getStringExtra("Language");
        beginnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelsActivity.this, TestActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LevelsActivity.this, beginnerBtn, "beginner_transition");
                startActivity(intent, options.toBundle());

                intent.putExtra("Language", language);
                intent.putExtra("level", "beginner");
                startActivity(intent);




            }
        });

        intermediateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelsActivity.this, TestActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LevelsActivity.this, intermediateBtn, "intermediate_transition");
                intent.putExtra("Language", language);
                intent.putExtra("level", "intermediate");
                startActivity(intent, options.toBundle());           }
        });

        advancedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LevelsActivity.this, TestActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LevelsActivity.this, advancedBtn, "advanced_transition");
                intent.putExtra("Language", language);
                intent.putExtra("level", "advanced");
                startActivity(intent, options.toBundle());         }
        });
    }
}
