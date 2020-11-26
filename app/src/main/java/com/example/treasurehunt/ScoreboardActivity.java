package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ScoreboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        //Place the function in that remembers past scores
        TextView textView = (TextView) findViewById(R.id.textView4);
        textView.setText("Placeholder for Scoreboard");
    }

    public void againPressed(View view) {
        Intent intent = new Intent(this, HuntingActivity.class);
        startActivity(intent);
    }
}