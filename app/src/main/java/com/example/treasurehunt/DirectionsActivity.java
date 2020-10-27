package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DirectionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        // Here is how we get heading and bearing
        Cache currentCache = (Cache)getIntent().getSerializableExtra("cache");
        double bearing = currentCache.bearing;
        double distance = currentCache.distance;
        int paces = (int)(distance / 0.75);

        String directions = "Directions:\n";
        directions += "Walk " + paces + " paces at a heading of " + bearing + "°";

        TextView textView4 = (TextView) findViewById(R.id.textView7);
        textView4.setText(directions);
    }
}