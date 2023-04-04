package com.example.dldemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    private TextView scoreTextView;
    private TextView dash;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize views
        scoreTextView = findViewById(R.id.scoreTextView);
        dash = findViewById(R.id.dash);

        // Get the ID of the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        if (currentUser == null) {
            // User is not logged in
            // Handle this case as appropriate
            return;
        }
        String userId = currentUser.getUid();
        dash.setText(currentUser.getEmail());
        // Create a Firebase reference to the user's scores node
        DatabaseReference userScoresRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("scores");

        // Retrieve the score data from the database
        userScoresRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Map<String, Object>> scoreDataList = new ArrayList<>();

                // Add the score data to the list
                for (DataSnapshot scoreSnapshot : dataSnapshot.getChildren()) {
                    GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>() {};
                    scoreDataList.add(scoreSnapshot.getValue(t));
                }

                // Display the score data in the UI
                StringBuilder scoreTextBuilder = new StringBuilder();
                for (Map<String, Object> scoreData : scoreDataList) {
                    String language = (String) scoreData.get("language");
                    String level = (String) scoreData.get("level");
                    int score = ((Long) scoreData.get("score")).intValue();
                    String time = (String) scoreData.get("time");

                    scoreTextBuilder.append("Language: ").append(language).append("\n")
                            .append("Level: ").append(level).append("\n")
                            .append("Score: ").append(score).append("\n")
                            .append("Time: ").append(time).append("\n\n");
                }
                scoreTextView.setText(scoreTextBuilder.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
