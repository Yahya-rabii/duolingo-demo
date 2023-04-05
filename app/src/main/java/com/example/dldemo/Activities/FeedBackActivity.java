package com.example.dldemo.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dldemo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedBackActivity extends AppCompatActivity{

    private EditText feedbackEditText;
    private Button submitButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        //Firebase will be initialized here
        feedbackEditText = findViewById(R.id.feedback_message);
        submitButton = findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedbacktext = feedbackEditText.getText().toString().trim();

                if (TextUtils.isEmpty(feedbacktext)){
                    Toast.makeText(FeedBackActivity.this, "Please enter feedback", Toast.LENGTH_SHORT).show();
                }else {
                    saveFeedbackToFirebase(feedbacktext);
                    Toast.makeText(FeedBackActivity.this, "Feedback submitted!", Toast.LENGTH_SHORT).show();
                    feedbackEditText.setText("");

                }
            }
        });
    }
    private void saveFeedbackToFirebase(String feedbackText) {
        DatabaseReference feedbackRef = FirebaseDatabase.getInstance().getReference("feedback");
        String feedbackId = feedbackRef.push().getKey();
        Feedback feedback = new Feedback(feedbackId, feedbackText);
        feedbackRef.child(feedbackId).setValue(feedback);
    }

    public class Feedback {
        private String feedbackId;
        private String feedbackText;

        public Feedback() {
            // Default constructor required for calls to DataSnapshot.getValue(Feedback.class)
            this.feedbackId = null;
            this.feedbackText = null;
        }

        public Feedback(String feedbackId, String feedbackText) {
            this.feedbackId = feedbackId;
            this.feedbackText = feedbackText;
        }

        public String getFeedbackId() {
            return feedbackId;
        }

        public void setFeedbackId(String feedbackId) {
            this.feedbackId = feedbackId;
        }

        public String getFeedbackText() {
            return feedbackText;
        }

        public void setFeedbackText(String feedbackText) {
            this.feedbackText = feedbackText;
        }
    }
}
