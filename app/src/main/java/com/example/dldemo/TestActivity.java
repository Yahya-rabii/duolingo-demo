package com.example.dldemo;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ScoreView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Get a reference to the Firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get a reference to the ScoreView
        scoreView = findViewById(R.id.scoreView);

        // Call the method to load and display the questions
        loadQuestions();

        // Set the OnClickListener for the "Try Again" button
        Button tryAgainButton = findViewById(R.id.try_again_button);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset the score
                ScoreView.resetScore();

                // Start the MainActivity
                Intent intent = new Intent(TestActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadQuestions() {
        String language = getIntent().getStringExtra("Language");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Parse the JSON data into Java objects
                // Assume that you have three lists for the three types of questions
                List<SentenceQuestion> sentenceQuestions = new ArrayList<>();
                List<PictureQuestion> pictureQuestions = new ArrayList<>();
                List<TextQuestion> textQuestions = new ArrayList<>();

                if (language.toLowerCase().equals("english")) {

                    for (DataSnapshot childSnapshot : snapshot.child("ensentences").getChildren()) {
                        SentenceQuestion question = childSnapshot.getValue(SentenceQuestion.class);
                        sentenceQuestions.add(question);
                    }

                    for (DataSnapshot childSnapshot : snapshot.child("enpictures").getChildren()) {
                        PictureQuestion question = childSnapshot.getValue(PictureQuestion.class);
                        pictureQuestions.add(question);
                    }

                    for (DataSnapshot childSnapshot : snapshot.child("entexts").getChildren()) {
                        TextQuestion question = childSnapshot.getValue(TextQuestion.class);
                        textQuestions.add(question);
                    }
                }     if (language.toLowerCase().equals("arabic")) {

                    for (DataSnapshot childSnapshot : snapshot.child("arsentences").getChildren()) {
                        SentenceQuestion question = childSnapshot.getValue(SentenceQuestion.class);
                        sentenceQuestions.add(question);
                    }

                    for (DataSnapshot childSnapshot : snapshot.child("arpictures").getChildren()) {
                        PictureQuestion question = childSnapshot.getValue(PictureQuestion.class);
                        pictureQuestions.add(question);
                    }

                    for (DataSnapshot childSnapshot : snapshot.child("artexts").getChildren()) {
                        TextQuestion question = childSnapshot.getValue(TextQuestion.class);
                        textQuestions.add(question);
                    }
                }     if (language.toLowerCase().equals("french")) {

                    for (DataSnapshot childSnapshot : snapshot.child("frsentences").getChildren()) {
                        SentenceQuestion question = childSnapshot.getValue(SentenceQuestion.class);
                        sentenceQuestions.add(question);
                    }

                    for (DataSnapshot childSnapshot : snapshot.child("frpictures").getChildren()) {
                        PictureQuestion question = childSnapshot.getValue(PictureQuestion.class);
                        pictureQuestions.add(question);
                    }

                    for (DataSnapshot childSnapshot : snapshot.child("frtexts").getChildren()) {
                        TextQuestion question = childSnapshot.getValue(TextQuestion.class);
                        textQuestions.add(question);
                    }
                }
                // Shuffle the questions and select 10 of them randomly
                List<Question> questions = new ArrayList<>();
                questions.addAll(sentenceQuestions);
                questions.addAll(pictureQuestions);
                questions.addAll(textQuestions);
                Collections.shuffle(questions);
                for (PictureQuestion question : pictureQuestions) {
                    System.out.println("img url: " + question.getImage_Url());
                    System.out.println("name: " + question.getName());
                    for (int i = 0; i < question.getOptions().size(); i++) {
                        System.out.println("opt: " + i + question.getOptions().get(i));
                    }
                }

              /*  for (SentenceQuestion question : sentenceQuestions) {
                    System.out.println("Sentence: " + question.getText());
                    System.out.println("Translation: " + question.getTranslation());
                }
                for (TextQuestion question : textQuestions) {
                    System.out.println("text: " + question.getText());
                    System.out.println("errors: " + question.getErrors());
                }


                System.out.println("sentenceQuestions" + sentenceQuestions.size());
                System.out.println("pictureQuestions" + pictureQuestions.size());
                System.out.println("textQuestions" + textQuestions.size());

                System.out.println("qqqq" + questions.size());

                for (int kk = 0; kk < questions.size(); kk++) {
                    System.out.println("questions" + questions.get(kk));
                }*/

                List<Question> selectedQuestions = new ArrayList<>();
                if (questions.size() >= 10) {
                    selectedQuestions = questions.subList(0, 10);
                }
               // System.out.println("ssss" + selectedQuestions);
                //System.out.println("ssssize" + selectedQuestions.size());
                //System.out.println("qqqq" + questions.size());


                //System.out.println("sentence" + selectedQuestions.get(2));//null



                // Display the questions to the user
                for (int i = 0; i < selectedQuestions.size(); i++) {
                    Question question = selectedQuestions.get(i);
                    if (question instanceof PictureQuestion) {
                        //todo Redirect the user to the PictureQuestionActivity and pass the data to display it
                        PictureQuestion pictureQuestion = (PictureQuestion) question;
                        Intent intent = new Intent(TestActivity.this, PictureQuestionActivity.class);
                        intent.putExtra("pictureQuestion", pictureQuestion);
                        startActivity(intent);

                    } else if (question instanceof TextQuestion) {
                        //todo Redirect the user to the TextQuestionActivity and pass the data to display it
                        TextQuestion textQuestion = (TextQuestion) question;
                        Intent intent = new Intent(TestActivity.this, TextQuestionActivity.class);
                        intent.putExtra("textQuestion", textQuestion);
                        startActivity(intent);
                    } else if (question instanceof SentenceQuestion) {
                        //todo Redirect the user to the PictureQuestionActivity and pass the data to display it
                        SentenceQuestion sentenceQuestion = (SentenceQuestion) question;
                        Intent intent = new Intent(TestActivity.this, SentenceQuestionActivity.class);
                        intent.putExtra("sentenceQuestion", sentenceQuestion);
                        startActivity(intent); }

                }




            }

// todo i want to implement the functions in order  to display every  type of question to the user (every type of qquestion in a different layout)

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
