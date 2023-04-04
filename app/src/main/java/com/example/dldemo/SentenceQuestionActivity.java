package com.example.dldemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dldemo.Tests.SentenceQuestion;

import java.util.Objects;

public class SentenceQuestionActivity extends AppCompatActivity {

    private SentenceQuestion mQuestion;
    EditText mCorrectionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sentance_test);

        // Get the question data from the intent
        mQuestion = (SentenceQuestion) getIntent().getSerializableExtra("sentenceQuestion");

        // Set the question text
        TextView questionTextView = findViewById(R.id.sentence_textview);
        questionTextView.setText(mQuestion.getText());
        SeekBar seekBar = findViewById(R.id.seekBar);
        // Set up the answer EditText
        mCorrectionEditText = findViewById(R.id.translation_edittext);
        seekBar.setProgress(InstentScore.score);

        // Set up the logic to check the answer
        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(view -> {
            String userAnswer = mCorrectionEditText.getText().toString().toLowerCase();
            boolean isCorrect = true;

            System.out.println(userAnswer.toLowerCase() + "  ===  " + mQuestion.getTranslation().toLowerCase());

            TextView feedback;


            String level = getIntent().getStringExtra("level");
            String language = getIntent().getStringExtra("Language");

            if (ScoreView.protries >= 1 || ScoreView.tries >= 3) {
                Toast.makeText(this, "you did not passed please try later", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SentenceQuestionActivity.this, MainActivity.class);
                ScoreView.tries=0;
                ScoreView.protries=0;
                startActivity(intent);
                finish();
            } else {





                if (Objects.equals(level, "beginner")) {


                    if (userAnswer.toLowerCase().equals(mQuestion.getTranslation().toLowerCase())) {
                        // If the answer is correct, show a message to the user
                        feedback = findViewById(R.id.selected_option_textview);
                        feedback.setText("Correct!");
                        ScoreView scoreView = findViewById(R.id.scoreView);
                        ScoreView.incrementScore();
                        tracking.currentIntent += 1;

                        // Update the SeekBar and text view
                        InstentScore.incrementsc();

                        if (tracking.currentIntent < tracking.intents.size()){
                            startActivity(tracking.intents.get(tracking.currentIntent));
                        }
                        finish();

                    } else {
                        // If the answer is incorrect, show a message to the user and allow them to try again
                        feedback = findViewById(R.id.selected_option_textview);
                        feedback.setText("Incorrect!");
                        tracking.currentIntent += 1;

                        if (tracking.currentIntent < tracking.intents.size()){
                            startActivity(tracking.intents.get(tracking.currentIntent));
                        }
                        finish();
                    }

                } else if (Objects.equals(level, "intermediate")) {


                    if (userAnswer.toLowerCase().equals(mQuestion.getTranslation().toLowerCase())) {
                        // If the answer is correct, show a message to the user
                        feedback = findViewById(R.id.selected_option_textview);
                        feedback.setText("Correct!");
                        ScoreView scoreView = findViewById(R.id.scoreView);
                        ScoreView.incrementScore();
                        tracking.currentIntent += 1;


                        // Update the SeekBar and text view
                        InstentScore.incrementsc();
                        if (tracking.currentIntent < tracking.intents.size()){
                            startActivity(tracking.intents.get(tracking.currentIntent));
                        }
                        finish();

                    } else {
                        // If the answer is incorrect, show a message to the user and allow them to try again
                        feedback = findViewById(R.id.selected_option_textview);
                        feedback.setText("Incorrect!");
                        ScoreView.tries++;
                        tracking.currentIntent += 1;

                        if (tracking.currentIntent < tracking.intents.size()){
                            startActivity(tracking.intents.get(tracking.currentIntent));
                        }
                        finish();
                    }

                } else if (Objects.equals(level, "advanced")) {


                    if (userAnswer.toLowerCase().equals(mQuestion.getTranslation().toLowerCase())) {
                        // If the answer is correct, show a message to the user
                        feedback = findViewById(R.id.selected_option_textview);
                        feedback.setText("Correct!");
                        ScoreView scoreView = findViewById(R.id.scoreView);
                        ScoreView.incrementScore();
                        tracking.currentIntent += 1;

                        // Update the SeekBar and text view
                        InstentScore.incrementsc();


                        if (tracking.currentIntent < tracking.intents.size()){
                            startActivity(tracking.intents.get(tracking.currentIntent));
                        }
                        finish();

                    } else {
                        // If the answer is incorrect, show a message to the user and allow them to try again
                        feedback = findViewById(R.id.selected_option_textview);
                        feedback.setText("Incorrect!");
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
        InstentScore.resetsc();
        finish(); // Optional, if you want to close the current activity
    }
}
