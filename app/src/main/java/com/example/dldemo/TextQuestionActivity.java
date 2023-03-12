package com.example.dldemo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dldemo.Tests.TextQuestion;

public class TextQuestionActivity extends AppCompatActivity {

    private TextQuestion mQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_test);

        // Get the question data from the intent
        mQuestion = getIntent().getParcelableExtra("question");

        // Set the question text
        TextView questionTextView = findViewById(R.id.question_textview);
        questionTextView.setText(mQuestion.getText());

        // TODO: Set up the answer options and logic to check the answer
    }
}
