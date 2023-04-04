package com.example.dldemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreView extends View {
    private int maxScore;
    static int currentScore;

    private Paint backgroundPaint;
    private Paint borderPaint;
    private Paint scorePaint;
    static int tries = 0;
    static int protries = 0;

    public ScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        maxScore = 100;
        currentScore = 0;

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.GRAY);
        backgroundPaint.setStyle(Paint.Style.FILL);

        borderPaint = new Paint();
        borderPaint.setColor(Color.WHITE);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(8);

        scorePaint = new Paint();
        scorePaint.setColor(Color.GREEN);
        scorePaint.setStyle(Paint.Style.FILL);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onDraw(Canvas canvas) {
        if (currentScore == 0) {

            View parentView = (View) getParent();
            TextView scoreTextView = parentView.findViewById(R.id.scoreper);
            scoreTextView.setText("  ");

            TextView test_title_textview = parentView.findViewById(R.id.test_title_textview);
            test_title_textview.setText(" ");

        } else if (currentScore > 0 && currentScore < 60) {

            super.onDraw(canvas);

            // Calculate score percentage
            float scorePercentage = (float) currentScore / maxScore;

            // Get dimensions of the view
            int width = getWidth();
            int height = getHeight();

            // Draw background
            canvas.drawCircle(width / 2f, height / 2f, width / 2f, backgroundPaint);

            // Draw border
            canvas.drawCircle(width / 2f, height / 2f, width / 2f - 4, borderPaint);

            // Draw score
            float sweepAngle = scorePercentage * 360;
            canvas.drawArc(new RectF(4, 4, width - 4, height - 4), -90, sweepAngle, true, scorePaint);


            View parentView = (View) getParent();
            Button tryAgainButton = parentView.findViewById(R.id.try_again_button);
            tryAgainButton.setVisibility(View.VISIBLE);
            TextView scoreTextView = parentView.findViewById(R.id.scoreper);
            scoreTextView.setText(currentScore + " %");
            scoreTextView.setText("Try again, you have " + currentScore + " %");
        } else {

            super.onDraw(canvas);

            // Calculate score percentage
            float scorePercentage = (float) currentScore / maxScore;

            // Get dimensions of the view
            int width = getWidth();
            int height = getHeight();

            // Draw background
            canvas.drawCircle(width / 2f, height / 2f, width / 2f, backgroundPaint);

            // Draw border
            canvas.drawCircle(width / 2f, height / 2f, width / 2f - 4, borderPaint);

            // Draw score
            float sweepAngle = scorePercentage * 360;
            canvas.drawArc(new RectF(4, 4, width - 4, height - 4), -90, sweepAngle, true, scorePaint);


            View parentView = (View) getParent();
            TextView scoreTextView = parentView.findViewById(R.id.scoreper);
            scoreTextView.setText(" good job !!! " + currentScore + " %");
            scoreTextView.setText("good job! you get " + currentScore + " %");

            System.out.println("dlkflksdfknllfkkslnfklnsd" + ScoreView.currentScore);


// Get the ID of the current user
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

// Create a Firebase reference to the user's scores node
            DatabaseReference userScoresRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("scores");

// Get the current time
            LocalDateTime currentTime = LocalDateTime.now();

            // Define the desired date-time format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd--HH:mm:ss");

            // Format the current time using the defined format
            String formattedTime = currentTime.format(formatter);

            // Print the formatted time
            System.out.println(formattedTime);

// Create a map to hold the new score data
            Map<String, Object> newScoreData = new HashMap<>();
            newScoreData.put("score", currentScore);
            newScoreData.put("time", formattedTime);
            newScoreData.put("level", LevelsActivity.lev);
            newScoreData.put("language", LevelsActivity.lang);

// Retrieve the existing score data from the database
            userScoresRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Map<String, Object>> existingScoreData = new ArrayList<>();

                    // Add the existing score data to the list
                    for (DataSnapshot scoreSnapshot : dataSnapshot.getChildren()) {
                        GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>() {};
                        existingScoreData.add(scoreSnapshot.getValue(t));
                    }

                    // Add the new score data to the list
                    existingScoreData.add(newScoreData);

                    // Save the updated score data to the database
                    userScoresRef.setValue(existingScoreData);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });





        }
    }

    public static void incrementScore() {
        currentScore += 10;

    }

    public static void resetScore() {
        currentScore = 0;
    }

    public static void resettries() {
        tries = 0;
        protries = 0;
    }
}
