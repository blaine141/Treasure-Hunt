package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class HuntingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunting);

        //Place code that will read from cache list and display 5 nearby caches
        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText("CACHE1");

        TextView textView1 = (TextView) findViewById(R.id.textView3);
        textView1.setText("CACHE2");

        TextView textView2 = (TextView) findViewById(R.id.textView4);
        textView2.setText("CACHE3");

        TextView textView3 = (TextView) findViewById(R.id.textView5);
        textView3.setText("CACHE4");

        TextView textView4 = (TextView) findViewById(R.id.textView6);
        textView4.setText("CACHE5");
    }

    public void huntOneSelect(View view) {
        Intent intent = new Intent(this, DirectionsActivity.class);
        intent.putExtra("arrayNumber", 1);
        startActivity(intent);
    }

    public void huntTwoSelect(View view) {
        Intent intent = new Intent(this, DirectionsActivity.class);
        intent.putExtra("arrayNumber", 2);
        startActivity(intent);
    }

    public void huntThreeSelect(View view) {
        Intent intent = new Intent(this, DirectionsActivity.class);
        intent.putExtra("arrayNumber", 3);
        startActivity(intent);
    }

    public void huntFourSelect(View view) {
        Intent intent = new Intent(this, DirectionsActivity.class);
        intent.putExtra("arrayNumber", 4);
        startActivity(intent);
    }

    public void huntFiveSelect(View view){
        Intent intent = new Intent(this, DirectionsActivity.class);
        intent.putExtra("arrayNumber", 5);
        startActivity(intent);
    }
}