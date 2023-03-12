package com.example.dldemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dldemo.Tests.PictureQuestion;

public class PictureQuestionActivity extends AppCompatActivity {

    private PictureQuestion mQuestion;
    private int mNumAttempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_test);

        // Get the question data from the intent
        mQuestion = getIntent().getParcelableExtra("pictureQuestion");

        // Set the image
        ImageView imageView = findViewById(R.id.picture_imageview);
        //todo! here  Picasso.get().load(mQuestion.getImage_Url()).into(imageView);

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

        // Set up the logic to check the answer
        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the selected answer is correct
                TextView selectedOptionTextView = findViewById(R.id.selected_option_textview);
                String selectedOption = selectedOptionTextView.getText().toString();
                if (selectedOption.equals(mQuestion.getName())) {
                    // If the selected answer is correct, redirect the user to the next question
                    Intent intent = new Intent(PictureQuestionActivity.this, TextQuestionActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // If the selected answer is incorrect, show a message and give the user up to 3 attempts
                    mNumAttempts++;
                    if (mNumAttempts < 3) {
                        TextView feedbackTextView = findViewById(R.id.selected_option_textview);
                        feedbackTextView.setText("Incorrect answer. Please try again.");
                    } else {
                        // If the user has used up all 3 attempts, redirect the user to the next question
                        Intent intent = new Intent(PictureQuestionActivity.this, TextQuestionActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
}
