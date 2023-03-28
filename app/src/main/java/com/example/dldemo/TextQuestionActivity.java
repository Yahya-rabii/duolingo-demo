package com.example.dldemo;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dldemo.Tests.TextQuestion;

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
            if (!mAnsweredCorrectly) {
                // Check if the selected answer is correct
                if (selectedOptionTextView != null) {

                    this.selectedOption = selectedOptionTextView.getText().toString();

                    TextView feedbackTextView = findViewById(R.id.selected_option_texttview);
                    feedbackTextView.setText(this.selectedOption);
                }

                else this.selectedOption = "rien";



                System.out.println(selectedOptionTextView);
                TextView feedbackTextView = findViewById(R.id.selected_option_texttview);
                feedbackTextView.setText(selectedOption);


                if (selectedOption.equals(mQuestion.getName())) {
                    // If the selected answer is correct, set the flag and show a message
                    mAnsweredCorrectly = true;
                    feedbackTextView = findViewById(R.id.selected_option_texttview);
                    feedbackTextView.setText("Correct!");
                    ScoreView scoreView = findViewById(R.id.scoreView);
                    ScoreView.incrementScore();
                    finish();

                } else {
                    feedbackTextView = findViewById(R.id.selected_option_texttview);
                    feedbackTextView.setText("Incorrect. The correct answer is: " + mQuestion.getName());
                    finish();
                }

            }
        });
    }
}

