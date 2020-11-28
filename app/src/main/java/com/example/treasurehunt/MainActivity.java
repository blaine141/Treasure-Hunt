package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView; //for testing

import java.lang.reflect.Array;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text = findViewById(R.id.test); //for testing
    }

    public void huntingPress(View view) {
        Intent intent = new Intent(this, HuntingActivity.class);
        startActivity(intent);
    }

    public void pacePress(View view) {
        Intent intent = new Intent(this, PaceActivity.class);
        startActivity(intent);
    }

}