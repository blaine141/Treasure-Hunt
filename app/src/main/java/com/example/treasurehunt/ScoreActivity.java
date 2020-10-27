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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        //Write a method that determines the number of paces off the user is and at what heading
        float paceScore = 14;
        float headingScore = 15;
        String finalScore = Float.toString(paceScore) + " paces at a heading of " + Float.toString(headingScore) + " degrees";

        TextView textView = (TextView) findViewById(R.id.textView10);
        textView.setText(finalScore);
    }

    public void againPressed(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
