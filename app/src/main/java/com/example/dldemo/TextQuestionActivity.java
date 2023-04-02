package com.example.dldemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dldemo.Tests.TextQuestion;

import java.util.Objects;

public class TextQuestionActivity extends AppCompatActivity {


    private TextQuestion mQuestion;
    private boolean mAnsweredCorrectly = false;
    private TextView selectedOptionTextView;
    private String selectedOption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_test);

        // Get the question data from the intent
        mQuestion = (TextQuestion) getIntent().getSerializableExtra("textQuestion");

        // Set the question text
        TextView questionTextView = findViewById(R.id.question_texttview);
        questionTextView.setText("complete the sentence?");

        // Set the answer options text
        TextView option1TextView = findViewById(R.id.option1_texttview);
        option1TextView.setText(mQuestion.getOptions().get(0));
        TextView option2TextView = findViewById(R.id.option2_texttview);
        option2TextView.setText(mQuestion.getOptions().get(1));
        TextView option3TextView = findViewById(R.id.option3_texttview);
        option3TextView.setText(mQuestion.getOptions().get(2));
        TextView option4TextView = findViewById(R.id.option4_texttview);
        option4TextView.setText(mQuestion.getOptions().get(3));


        TextView questionTexttView = findViewById(R.id.question_texttview);
        questionTexttView.setText(mQuestion.getQuote());

        TextView opt1 = findViewById(R.id.option1_texttview);
        TextView opt2 = findViewById(R.id.option2_texttview);
        TextView opt3 = findViewById(R.id.option3_texttview);
        TextView opt4 = findViewById(R.id.option4_texttview);


        opt1.setOnClickListener(view -> {
            this.selectedOptionTextView = option1TextView;
            selectedOptionTextView.setBackgroundColor(Color.BLUE); // set your desired color here

        });
        opt2.setOnClickListener(view -> {
            this.selectedOptionTextView = option2TextView;
            selectedOptionTextView.setBackgroundColor(Color.BLUE); // set your desired color here

        });
        opt3.setOnClickListener(view -> {
            this.selectedOptionTextView = option3TextView;
            selectedOptionTextView.setBackgroundColor(Color.BLUE); // set your desired color here

        });
        opt4.setOnClickListener(view -> {
            this.selectedOptionTextView = option4TextView;
            selectedOptionTextView.setBackgroundColor(Color.BLUE); // set your desired color here

        });

        // Set up the logic to check the answer
        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(view -> {
            // Check if the selected answer is correct
            String level = getIntent().getStringExtra("level");


            if (selectedOptionTextView != null) {

                this.selectedOption = selectedOptionTextView.getText().toString();

                TextView feedbackTextView = findViewById(R.id.selected_option_texttview);
                feedbackTextView.setText(this.selectedOption);

            } else this.selectedOption = "rien";


            TextView feedbackTextView;
            System.out.println("heeeeeeeeeeeeeeeeeeeeeeeeyyyy");

            if (ScoreView.protries >= 1 || ScoreView.tries >= 3) {
                Toast.makeText(this, "you did not passed please try later", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TextQuestionActivity.this, MainActivity.class);
                ScoreView.tries = 0;
                ScoreView.protries = 0;
                startActivity(intent);
                finish();
            } else {



                System.out.println(level);
                if (Objects.equals(level, "beginner")) {

                    System.out.println(selectedOption + "===" + mQuestion.getName().toLowerCase());
                    if (selectedOption.equals(mQuestion.getName().toLowerCase())) {
                        // If the answer is correct, show a message to the user
                        feedbackTextView = findViewById(R.id.selected_option_texttview);
                        feedbackTextView.setText("Correct!");
                        ScoreView scoreView = findViewById(R.id.scoreView);
                        ScoreView.incrementScore();
                        tracking.currentIntent += 1;

                        if (tracking.currentIntent < tracking.intents.size()){
                            startActivity(tracking.intents.get(tracking.currentIntent));
                        }
                        finish();

                    } else {
                        // If the answer is incorrect, show a message to the user and allow them to try again
                        feedbackTextView = findViewById(R.id.selected_option_texttview);
                        feedbackTextView.setText("Incorrect!");
                        tracking.currentIntent += 1;

                        if (tracking.currentIntent < tracking.intents.size()){
                            startActivity(tracking.intents.get(tracking.currentIntent));
                        }
                        finish();
                    }

                } else if (Objects.equals(level, "intermediate")) {


                    if (selectedOption.equals(mQuestion.getName())) {
                        // If the selected answer is correct, set the flag and show a message
                        mAnsweredCorrectly = true;
                        feedbackTextView = findViewById(R.id.selected_option_texttview);
                        feedbackTextView.setText("Correct!");
                        ScoreView scoreView = findViewById(R.id.scoreView);
                        ScoreView.incrementScore();
                        tracking.currentIntent += 1;

                        if (tracking.currentIntent < tracking.intents.size()){
                            startActivity(tracking.intents.get(tracking.currentIntent));
                        }
                        finish();

                    } else {
                        feedbackTextView = findViewById(R.id.selected_option_texttview);
                        feedbackTextView.setText("Incorrect. The correct answer is: " + mQuestion.getName());
                        ScoreView.tries++;
                        tracking.currentIntent += 1;

                        if (tracking.currentIntent < tracking.intents.size()){
                            startActivity(tracking.intents.get(tracking.currentIntent));
                        }
                        finish();
                    }

                } else if (Objects.equals(level, "advanced")) {


                    if (selectedOption.equals(mQuestion.getName())) {
                        // If the selected answer is correct, set the flag and show a message
                        mAnsweredCorrectly = true;
                        feedbackTextView = findViewById(R.id.selected_option_texttview);
                        feedbackTextView.setText("Correct!");
                        ScoreView scoreView = findViewById(R.id.scoreView);
                        ScoreView.incrementScore();
                        tracking.currentIntent += 1;

                        if (tracking.currentIntent < tracking.intents.size()){
                            startActivity(tracking.intents.get(tracking.currentIntent));
                        }
                        finish();

                    } else {
                        feedbackTextView = findViewById(R.id.selected_option_texttview);
                        feedbackTextView.setText("Incorrect. The correct answer is: " + mQuestion.getName());
                        ScoreView.protries++;
                        tracking.currentIntent += 1;

                        if (tracking.currentIntent < tracking.intents.size()){
                            startActivity(tracking.intents.get(tracking.currentIntent));
                        }
                        finish();
                    }

                }

            }


        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Optional, if you want to close the current activity
    }
}

