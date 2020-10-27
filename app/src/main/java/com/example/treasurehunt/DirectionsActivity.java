package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DirectionsActivity extends AppCompatActivity implements SensorEventListener {

    // device sensor manager
    private SensorManager mSensorManager;

    // variable for heading text
    TextView heading;

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
}