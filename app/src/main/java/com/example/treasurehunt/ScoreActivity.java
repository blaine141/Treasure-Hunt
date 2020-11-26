package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.treasurehunt.MainActivity;
import com.example.treasurehunt.R;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int hintsUsed = getIntent().getIntExtra("hintsUsed", -1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        //CALL REFRESH FUNCTION TO GET THE DISTANCE AND HEADING AWAY FROM THE CACHE

        float paceScore = 15;
        float headingScore = 15;

        String finalScore = "You were off by " + Integer.toString((int) paceScore) + " paces\nat a heading of " + Integer.toString((int) headingScore) + " degrees\nYou used " + Integer.toString(hintsUsed) + " hints";

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
