package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class DirectionsActivity extends AppCompatActivity implements SensorEventListener {

    // device sensor manager
    private SensorManager mSensorManager;

    // variable for heading text
    TextView heading;

    // variable for holding angle of animation
    private float currentDegree = 0f;

    //For the eventual image
    ImageView image;

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

        // TextView that will tell the user what their heading is
        heading = (TextView) findViewById(R.id.textView8);

        image = (ImageView) findViewById(R.id.imageView3);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);
        heading.setText("Heading: " + Float.toString(degree) + " degrees");

        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void scorePressed(View view) {
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
    }
//
//    public void hintPressed(View view) {
//
//        //double bearing = currentCache.bearing;
//        //double distance = currentCache.distance;
//        //int paces = (int)(distance / 0.75);
//
//        String directions = "Directions:\n";
//        //directions += "Walk " + paces + " paces at a heading of " + bearing + "°";
//
//        //TextView textView4 = (TextView) findViewById(R.id.textView7);
//        //textView4.setText(directions);
//    }
}