package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

public class DirectionsActivity extends AppCompatActivity implements SensorEventListener {

    // device sensor manager
    private SensorManager mSensorManager;

    // Current cache
    Cache currentCache;

    // variable for heading text
    TextView heading;

    // variable for holding angle of animation
    private float currentDegree = 0f;

    //For the eventual image
    ImageView image;

    //To keep track of score
    int hintsUsed = 0;
    double startDistance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        // Here is how we get heading and bearing
        currentCache = (Cache)getIntent().getSerializableExtra("cache");
        assert currentCache != null;
        double bearing = currentCache.bearing;
        double distance = currentCache.distance;
        startDistance = currentCache.distance;
        int paces = (int)(distance / StepService.getStepLength());

        String directions = "Directions:\n";
        directions += "Walk " + paces + " paces at a heading of " + bearing + "°";

        TextView textView4 = findViewById(R.id.textView7);
        textView4.setText(directions);

        TextView title = findViewById(R.id.titleTextView);
        title.setText(currentCache.name);

        // TextView that will tell the user what their heading is
        heading = findViewById(R.id.textView8);

        image = findViewById(R.id.imageView3);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    public Float lpf = null;
    public final float alpha = 0.9f;
    @Override
    public void onSensorChanged(SensorEvent event) {
        // get the angle around the z-axis rotated
        float currentReading = -event.values[0];
        if (lpf == null) {
            lpf = currentReading;
        } else {
            lpf = lpf * alpha + currentReading * (1 - alpha);
        }

        heading.setText(String.format(getResources().getConfiguration().locale, "Heading: %.1f degrees", lpf));

        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                lpf,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = lpf;
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

    public ProgressDialog showProgress (String text) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(true);
        dialog.setMessage(text);
        dialog.show();
        return dialog;
    }

    public void foundPressed(View v) {
        score(this,true);
    }

    public void forfeitPressed(View v) {
        final Context context = this;
        final ProgressDialog dialog = showProgress("Calculating your score...");
        currentCache.refresh(this, new Runnable() {
            @Override
            public void run() {
                dialog.cancel();
                score(context,false);
            }
        });
    }



    public void score(Context context, boolean found) {
        Score score = new Score(found, startDistance, currentCache, hintsUsed);
        Intent intent = new Intent(context, ScoreActivity.class);
        intent.putExtra("score", score);
        startActivity(intent);
    }

    public void hintPressed(View view) {

        final Context context = this;
        final ProgressDialog dialog = showProgress("Processing hint...");
        currentCache.refresh(this, new Runnable() {
            @Override
            public void run() {
                dialog.cancel();
                double bearing = currentCache.bearing;
                double distance = currentCache.distance;
                int paces = (int) (distance/.75);

                String directions = "Directions:\n";
                directions += "Walk " + paces + " paces at a heading of " + bearing + "°";

                TextView textView = findViewById(R.id.textView7);
                textView.setText(directions);
            }
        });
        hintsUsed++;
    }
}