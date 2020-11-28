package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.treasurehunt.MainActivity;
import com.example.treasurehunt.R;

public class ScoreActivity extends AppCompatActivity {

    Cache currentCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context thisContext = this;
        currentCache = (Cache)getIntent().getSerializableExtra("cache");
        int hintsUsed = getIntent().getIntExtra("hintsUsed", -1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        currentCache.refresh(this, new Runnable() {
            @Override
            public void run() {
            }
        });
        displayScore(hintsUsed);
    }

    public void displayScore(int hintsUsed) {
        double headingScore = currentCache.bearing;
        double paceScore = currentCache.distance;
        int paces = (int) (paceScore/.75);
        String finalScore = "You were off by " + Integer.toString(paces) + " paces\nat a heading of " + Integer.toString((int) headingScore) + " degrees\nYou used " + Integer.toString(hintsUsed) + " hint(s)";
        TextView textView = (TextView) findViewById(R.id.textView10);
        textView.setText(finalScore);
    }

    public void againPressed(View view) {
        Intent intent = new Intent(this, HuntingActivity.class);
        startActivity(intent);
    }

    public void scoreboardPressed(View view){
        Intent intent = new Intent(this, ScoreboardActivity.class);
        startActivity(intent);
    }
}