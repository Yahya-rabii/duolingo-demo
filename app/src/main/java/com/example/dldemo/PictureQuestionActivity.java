package com.example.dldemo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dldemo.Tests.PictureQuestion;
import com.squareup.picasso.Picasso;

public class PictureQuestionActivity extends AppCompatActivity {

    private PictureQuestion mQuestion;
    private int mNumAttempts = 0;
    private boolean mAnsweredCorrectly = false;
    private TextView selectedOptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_test);

        // Get the question data from the intent
        mQuestion = (PictureQuestion) getIntent().getSerializableExtra("pictureQuestion");

        // Set the image
        ImageView imageView = findViewById(R.id.picture_imageview);
        System.out.println("hahouwa"+mQuestion.getImage_Url());
        Picasso.get().load(mQuestion.getImage_Url()).into(imageView);

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


        Button opt1 = findViewById(R.id.option1_textview);
        Button opt2 = findViewById(R.id.option2_textview);
        Button opt3 = findViewById(R.id.option3_textview);
        Button opt4 = findViewById(R.id.option4_textview);


        opt1.setOnClickListener(view -> {
            this.selectedOptionTextView = option1TextView;
        });
        opt2.setOnClickListener(view -> {
            this.selectedOptionTextView = option2TextView;
        });
        opt3.setOnClickListener(view -> {
            this.selectedOptionTextView = option3TextView;
        });
        opt4.setOnClickListener(view -> {
            this.selectedOptionTextView = option4TextView;
        });


        // Set up the logic to check the answer
        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(view -> {
            if (!mAnsweredCorrectly) {
                // Check if the selected answer is correct
                if (selectedOptionTextView != null) {
                    String selectedOption = selectedOptionTextView.getText().toString();
                }

                String selectedOption = "rien";



                System.out.println(selectedOptionTextView);
                TextView feedbackTextView = findViewById(R.id.selected_option_textview);
                feedbackTextView.setText(selectedOption);


                if (selectedOption.equals(mQuestion.getName())) {
                    // If the selected answer is correct, set the flag and show a message
                    mAnsweredCorrectly = true;
                    feedbackTextView = findViewById(R.id.selected_option_textview);
                    feedbackTextView.setText("Correct!");
                    finish();

                } else {
                    feedbackTextView = findViewById(R.id.selected_option_textview);
                    feedbackTextView.setText("Incorrect. The correct answer is: " + mQuestion.getName());
                    finish();
                }

            }
        });
    }
}
