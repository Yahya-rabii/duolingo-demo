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

public class ScoreView extends View {
    private int maxScore;
    static int currentScore;
    private Paint backgroundPaint;
    private Paint borderPaint;
    private Paint scorePaint;

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



        if (currentScore >0 && currentScore< 60) {
            View parentView = (View) getParent();
            Button tryAgainButton = parentView.findViewById(R.id.try_again_button);
            tryAgainButton.setVisibility(View.VISIBLE);
            TextView scoreTextView = parentView.findViewById(R.id.scoreper);
            scoreTextView.setText(" you get only  ="+currentScore + " %");
        }
        else {
            View parentView = (View) getParent();
            TextView scoreTextView = parentView.findViewById(R.id.scoreper);
            scoreTextView.setText(" good !!! you get  ="+currentScore + " %");
        }
    }

    public static void incrementScore() {
        currentScore += 10;

    }

    public static void resetScore() {
        currentScore = 0;
    }
}