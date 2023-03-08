package com.example.dldemo;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dldemo.Tests.PictureQuestion;
import com.example.dldemo.Tests.Question;
import com.example.dldemo.Tests.SentenceQuestion;
import com.example.dldemo.Tests.TextQuestion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Get a reference to the Firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Call the method to load and display the questions
        loadQuestions();
    }

    private void loadQuestions() {
        // Get a reference to the "tests" node in your Firebase database
        DatabaseReference reference = mDatabase.child("tests");
        System.out.println(reference);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Parse the JSON data into Java objects
                // Assume that you have three lists for the three types of questions
                List<SentenceQuestion> sentenceQuestions = new ArrayList<>();
                List<PictureQuestion> pictureQuestions = new ArrayList<>();
                List<TextQuestion> textQuestions = new ArrayList<>();

                for (DataSnapshot childSnapshot : snapshot.child("sentences").getChildren()) {
                    SentenceQuestion question = childSnapshot.getValue(SentenceQuestion.class);
                    sentenceQuestions.add(question);
                }

                for (DataSnapshot childSnapshot : snapshot.child("pictures").getChildren()) {
                    PictureQuestion question = childSnapshot.getValue(PictureQuestion.class);
                    pictureQuestions.add(question);
                }

                for (DataSnapshot childSnapshot : snapshot.child("texts").getChildren()) {
                    TextQuestion question = childSnapshot.getValue(TextQuestion.class);
                    textQuestions.add(question);
                }

                // Shuffle the questions and select 10 of them randomly
                List<Question> questions = new ArrayList<>();
                questions.addAll(sentenceQuestions);
                questions.addAll(pictureQuestions);
                questions.addAll(textQuestions);
                Collections.shuffle(questions);

                System.out.println("sentenceQuestions"+sentenceQuestions.size());
                System.out.println("pictureQuestions"+pictureQuestions.size());
                System.out.println("textQuestions"+textQuestions.size());

                System.out.println("qqqq"+questions.size());

                for (int kk = 0 ; kk<questions.size();kk++){
                    System.out.println("questions"+questions.get(kk));
                }

                List<Question> selectedQuestions = new ArrayList<>();
                if (questions.size() >= 10) {
                    selectedQuestions = questions.subList(0, 10);
                }
                System.out.println("ssss"+selectedQuestions);
                System.out.println("ssssize"+selectedQuestions.size());
                System.out.println("qqqq"+questions.size());

                // Display the questions to the user
                for (int i = 0; i < selectedQuestions.size(); i++) {
                    Question question = selectedQuestions.get(i);
                    if (question instanceof SentenceQuestion) {
                        // Display the sentence and ask the user to translate it
                        SentenceQuestion sentenceQuestion = (SentenceQuestion) question;
                       displaySentenceQuestion(sentenceQuestion);
                    } else if (question instanceof PictureQuestion) {
                        // Display the picture and the word options and ask the user to drag and drop the right word
                        PictureQuestion pictureQuestion = (PictureQuestion) question;
                      displayPictureQuestion(pictureQuestion);
                    } else if (question instanceof TextQuestion) {
                        // Display the text with errors and ask the user to correct it
                        TextQuestion textQuestion = (TextQuestion) question;
                      displayTextQuestion(textQuestion);
                    }
                }
            }



            private void displaySentenceQuestion(SentenceQuestion sentenceQuestion) {
                // Get the sentence TextView from your activity layout
                TextView sentenceTextView = findViewById(R.id.sentence_textview);
                // Set the sentence text
                sentenceTextView.setText(sentenceQuestion.getSentence());
            }

            private void displayPictureQuestion(PictureQuestion pictureQuestion) {
                // Get the ImageView and the three TextViews for the word options from your activity layout
                ImageView pictureImageView = findViewById(R.id.picture_imageview);
                TextView option1TextView = findViewById(R.id.word1_textview);
                TextView option2TextView = findViewById(R.id.word2_textview);
                TextView option3TextView = findViewById(R.id.word3_textview);
                TextView option4TextView = findViewById(R.id.word4_textview);

                // Load the picture from the URL and display it in the ImageView
                if (pictureQuestion.getImageUrl()!=null){
                    Picasso.get().load(pictureQuestion.getImageUrl()).into(pictureImageView);

                }

                // Shuffle the word options and set the text for each TextView

                System.out.println("sssssssuuuuuuuuuuuu "+pictureQuestion.getOptions().size());
                List<String> options = new ArrayList<>(pictureQuestion.getOptions());
                Collections.shuffle(options);
                option1TextView.setText(options.get(0));
                option2TextView.setText(options.get(1));
                option3TextView.setText(options.get(2));
                option3TextView.setText(options.get(4));
            }

            private void displayTextQuestion(TextQuestion textQuestion) {
                // Get the TextView for the text with errors and the EditText for the user's answer from your activity layout
                TextView textTextView = findViewById(R.id.textview_with_errors);
                EditText answerEditText = findViewById(R.id.correction_edittext);

                // Set the text with errors
                StringBuilder errs = null;
                for (int x=0 ; x< textQuestion.getErrors().size(); x++){
                    assert errs != null;
                    errs.append(textQuestion.getErrors().get(x));
                }

                assert errs != null;
                textTextView.setText(errs.toString());

                // Set a TextWatcher on the EditText to check the user's answer
                answerEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // Check if the user's answer is correct
                        String answer = s.toString().trim();
                        if (answer.equalsIgnoreCase(textQuestion.getText())) {
                            // Disable the EditText and show a toast with a message
                            answerEditText.setEnabled(false);
                            Toast.makeText(TestActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });
            }




            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
