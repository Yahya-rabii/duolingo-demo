package com.example.dldemo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dldemo.Tests.SentenceQuestion;

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

        // Set up the answer EditText
        mCorrectionEditText = findViewById(R.id.translation_edittext);

        // Set up the logic to check the answer
        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(view -> {
            String userAnswer = mCorrectionEditText.getText().toString().toLowerCase();
            boolean isCorrect = true;

            System.out.println(userAnswer +"  ===  "+mQuestion.getTranslation().toLowerCase());

            if (userAnswer.equals(mQuestion.getTranslation())) {
                // If the answer is correct, show a message to the user
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                finish();

            } else {
                // If the answer is incorrect, show a message to the user and allow them to try again
                Toast.makeText(this, "Incorrect. Try again.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
