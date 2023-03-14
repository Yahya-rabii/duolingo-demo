package com.example.dldemo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dldemo.Tests.TextQuestion;

import java.util.Arrays;

public class TextQuestionActivity extends AppCompatActivity {

    private TextQuestion mQuestion;
    private EditText mCorrectionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_test);

        // Get the question data from the intent
        mQuestion = (TextQuestion) getIntent().getSerializableExtra("textQuestion");

        // Set the question text
        TextView questionTextView = findViewById(R.id.textview_with_errors);
        questionTextView.setText(mQuestion.getText());

        // Set up the answer EditText
        mCorrectionEditText = findViewById(R.id.correction_edittext);

        // Set up the logic to check the answer
        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(view -> {
            String userAnswer = mCorrectionEditText.getText().toString();
            String[] userErrors = userAnswer.split("-");
            String[] actualErrors = mQuestion.getText().split(" ");
            boolean isCorrect = true;


            System.out.println( mCorrectionEditText.getText().toString()+"==="+ actualErrors);


            for (String error : userErrors) {
                if (!Arrays.asList(actualErrors).contains(error)) {
                    isCorrect = false;
                    break;
                }
            }

            TextView feedback;
            if (isCorrect) {

                feedback = findViewById(R.id.selected_option_textview);
                feedback.setText("Correct!");
                ScoreView scoreView = findViewById(R.id.scoreView);
                ScoreView.incrementScore();
                finish();


            } else {
                // If the answer is incorrect, show a message to the user and allow them to try again
                feedback = findViewById(R.id.selected_option_textview);
                feedback.setText("Incorrect!");
                finish();
            }
        });

    }
}

