package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.treasurehunt.MainActivity;
import com.example.treasurehunt.R;

import org.w3c.dom.Text;

public class ScoreActivity extends AppCompatActivity {

    Cache currentCache;
    Score score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        score = (Score)getIntent().getSerializableExtra("score");
        assert score != null;
        currentCache = score.cache;
        displayScore();
    }

    public void displayScore() {
        TextView title = findViewById(R.id.scoreTitleTextView);
        title.setText(currentCache.name);

        String finalScore = String.format(getResources().getConfiguration().locale, "You had an accuracy of %.2f%%. You used %d hint(s)", 100 * score.getAccuracy(), score.hints);
        TextView textView = findViewById(R.id.textView10);
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