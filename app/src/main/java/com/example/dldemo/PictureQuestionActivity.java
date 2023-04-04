package com.example.dldemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dldemo.Tests.PictureQuestion;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class PictureQuestionActivity extends AppCompatActivity {

    private PictureQuestion mQuestion;
    private int mNumAttempts = 0;
    private boolean mAnsweredCorrectly = false;
    private TextView selectedOptionTextView;
    private String selectedOption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_test);

        // Get the question data from the intent
        mQuestion = (PictureQuestion) getIntent().getSerializableExtra("pictureQuestion");

        // Set the image
        ImageView imageView = findViewById(R.id.picture_imageview);
        System.out.println("hahouwa" + mQuestion.getImage_Url());
        Picasso.get().load(mQuestion.getImage_Url()).into(imageView);


        String level = getIntent().getStringExtra("level");
        String language = getIntent().getStringExtra("Language");


        // Set the question text
        TextView questionTextView = findViewById(R.id.question_textview);
        questionTextView.setText("What is the name of the object in the picture?");

        // Set the answer options text
        TextView option1TextView = findViewById(R.id.option1_textview);
        option1TextView.setText(mQuestion.getOptions().get(0));
        TextView option2TextView = findViewById(R.id.option2_textview);
        option2TextView.setText(mQuestion.getOptions().get(1));
        TextView option3TextView = findViewById(R.id.option3_textview);
        option3TextView.setText(mQuestion.getOptions().get(2));
        TextView option4TextView = findViewById(R.id.option4_textview);
        option4TextView.setText(mQuestion.getOptions().get(3));


        TextView opt1 = findViewById(R.id.option1_textview);
        TextView opt2 = findViewById(R.id.option2_textview);
        TextView opt3 = findViewById(R.id.option3_textview);
        TextView opt4 = findViewById(R.id.option4_textview);


        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setProgress(InstentScore.score);


        opt1.setOnClickListener(view -> {
            this.selectedOptionTextView = option1TextView;
            opt1.setBackgroundColor(Color.BLUE); // set your desired color here
        });
        opt2.setOnClickListener(view -> {
            this.selectedOptionTextView = option2TextView;
            opt2.setBackgroundColor(Color.BLUE); // set your desired color here

        });
        opt3.setOnClickListener(view -> {
            this.selectedOptionTextView = option3TextView;
            opt3.setBackgroundColor(Color.BLUE); // set your desired color here

        });
        opt4.setOnClickListener(view -> {
            this.selectedOptionTextView = option4TextView;
            opt4.setBackgroundColor(Color.BLUE); // set your desired color here

        });

        // Set up the logic to check the answer
        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(view -> {
            if (!mAnsweredCorrectly) {
                // Check if the selected answer is correct
                if (selectedOptionTextView != null) {

                    this.selectedOption = selectedOptionTextView.getText().toString();

                    TextView feedbackTextView = findViewById(R.id.selected_option_textview);
                    feedbackTextView.setText(this.selectedOption);
                } else this.selectedOption = "rien";


                System.out.println(selectedOptionTextView);
                TextView feedbackTextView = findViewById(R.id.selected_option_textview);
                feedbackTextView.setText(selectedOption);


                if (ScoreView.protries >= 1 || ScoreView.tries >= 3) {
                    Toast.makeText(this, "you did not passed please try later", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PictureQuestionActivity.this, MainActivity.class);
                    ScoreView.tries = 0;
                    ScoreView.protries = 0;
                    startActivity(intent);
                    finish();

                } else {


                    if (Objects.equals(level, "beginner")) {


                        if (selectedOption.equals(mQuestion.getName())) {
                            // If the selected answer is correct, set the flag and show a message
                            mAnsweredCorrectly = true;
                            feedbackTextView = findViewById(R.id.selected_option_textview);
                            feedbackTextView.setText("Correct!");
                            ScoreView scoreView = findViewById(R.id.scoreView);
                            ScoreView.incrementScore();
                            tracking.currentIntent += 1;


// Update the SeekBar and text view
                            InstentScore.incrementsc();


                            if (tracking.currentIntent < tracking.intents.size()) {
                                startActivity(tracking.intents.get(tracking.currentIntent));
                            }
                            finish();

                        } else {
                            feedbackTextView = findViewById(R.id.selected_option_textview);
                            feedbackTextView.setText("Incorrect. The correct answer is: " + mQuestion.getName());
                            tracking.currentIntent += 1;

                            if (tracking.currentIntent < tracking.intents.size()) {
                                startActivity(tracking.intents.get(tracking.currentIntent));
                            }
                            finish();
                        }

                    } else if (Objects.equals(level, "intermediate")) {


                        if (selectedOption.equals(mQuestion.getName())) {
                            // If the selected answer is correct, set the flag and show a message
                            mAnsweredCorrectly = true;
                            feedbackTextView = findViewById(R.id.selected_option_textview);
                            feedbackTextView.setText("Correct!");
                            ScoreView scoreView = findViewById(R.id.scoreView);
                            ScoreView.incrementScore();
                            tracking.currentIntent += 1;

                            // Update the SeekBar and text view
                            InstentScore.incrementsc();

                            if (tracking.currentIntent < tracking.intents.size()) {
                                startActivity(tracking.intents.get(tracking.currentIntent));
                            }
                            finish();

                        } else {
                            feedbackTextView = findViewById(R.id.selected_option_textview);
                            feedbackTextView.setText("Incorrect. The correct answer is: " + mQuestion.getName());
                            ScoreView.tries++;
                            tracking.currentIntent += 1;

                            if (tracking.currentIntent < tracking.intents.size()) {
                                startActivity(tracking.intents.get(tracking.currentIntent));
                            }

                            finish();
                        }

                    } else if (Objects.equals(level, "advanced")) {


                        if (selectedOption.equals(mQuestion.getName())) {
                            // If the selected answer is correct, set the flag and show a message
                            mAnsweredCorrectly = true;
                            feedbackTextView = findViewById(R.id.selected_option_textview);
                            feedbackTextView.setText("Correct!");
                            ScoreView scoreView = findViewById(R.id.scoreView);
                            ScoreView.incrementScore();
                            tracking.currentIntent += 1;


                            // Update the SeekBar and text view
                            InstentScore.incrementsc();

                            if (tracking.currentIntent < tracking.intents.size()) {
                                startActivity(tracking.intents.get(tracking.currentIntent));
                            }
                            finish();

                        } else {
                            feedbackTextView = findViewById(R.id.selected_option_textview);
                            feedbackTextView.setText("Incorrect. The correct answer is: " + mQuestion.getName());
                            ScoreView.protries++;
                            tracking.currentIntent += 1;

                            if (tracking.currentIntent < tracking.intents.size()) {
                                startActivity(tracking.intents.get(tracking.currentIntent));
                            }
                            finish();
                        }

                    }

                }


            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        ScoreView.protries = 0;
        ScoreView.tries = 0;
        tracking.intents.clear();
        tracking.currentIntent = 0;
        startActivity(intent);
        InstentScore.resetsc();
        finish(); // Optional, if you want to close the current activity
    }


}
