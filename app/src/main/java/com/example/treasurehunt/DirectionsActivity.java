package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DirectionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        //Write funciton that will print instructions
        //Instructions could be stored in array for ease of printing
        int i = 0;
        String[] dummyDirections = new String[3];
        String directions = "Directions:\n";

        dummyDirections[0] = "Take 10 paces at heading 30 degrees";
        dummyDirections[1] = "Take 20 paces at heading 60 degrees";
        dummyDirections[2] = "Take 5 paces at heading 90 degrees";
        while(i < dummyDirections.length) {
            String transfer = dummyDirections[i].concat("\n");
            directions += transfer;
            i++;
        }
        TextView textView4 = (TextView) findViewById(R.id.textView7);
        textView4.setText(directions);
    }
}