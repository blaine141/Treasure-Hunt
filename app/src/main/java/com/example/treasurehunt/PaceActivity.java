package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;

import android.os.Bundle;
import android.view.View;

public class PaceActivity extends AppCompatActivity{
    private TextView text;
    private double steplength;
    DecimalFormat df = new DecimalFormat("##.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pace);
    }

    public void calibratePress(View view) {
        //Calibrate approximate step length with height, because calculating the walking distance with accelerometer is hard to implement.
        TextView text = findViewById(R.id.editTextNumberDecimal);
        if("".equals(text.getText().toString().trim())){
            Toast.makeText(this,"Blank is invalid!", Toast.LENGTH_SHORT).show();
        }else{
            double height=Double.valueOf(text.getText().toString());

            if(height>280.0||height<60.0){
                Toast.makeText(this,"Invalid number!", Toast.LENGTH_SHORT).show();
            }else {
                steplength=Double.parseDouble(df.format(0.45*height/100));
                Step.setStepLength(steplength);
                Toast.makeText(this,"Calibration succeeded!", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void finishedPress(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}