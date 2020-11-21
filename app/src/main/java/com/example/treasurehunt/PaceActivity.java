package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pace);
    }

    public void calibratePress(View view) {
        //INSERT PACE COUNTER AND MEASUREMENT FUNCTIONS TO GET PACE SIZE
    }

    public void finishedPress(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}