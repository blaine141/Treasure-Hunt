package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;

public class StepCounter extends AppCompatActivity implements SensorEventListener {
    private Sensor sensor;
    private SensorManager sm;
    private double MagnitudePrev = 0;
    private double sumM = 0;
    private double maxMagnitude;
    private double minMagnitude;
    private Integer stepcounter = 0;

    private Integer SensorSwitch = 0; //0:step_counter; 1:accelerometer
    private boolean running = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        running = true;

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (sensor != null) {
            sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            SensorSwitch = 1;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        //stop detecting
        //sm.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (SensorSwitch == 0) {
            if (running == true) {
                stepcounter = Math.round(event.values[0]);
            }
        } else {
            if (event != null) {
                float x_acc = event.values[0];
                float y_acc = event.values[1];
                float z_acc = event.values[2];

                double Magnitude = Math.sqrt(x_acc * x_acc + y_acc * y_acc + z_acc * z_acc);
                if (MagnitudePrev == 0) {
                    MagnitudePrev = Magnitude;
                    maxMagnitude = Magnitude;
                    minMagnitude = Magnitude;
                }

                if (Magnitude > maxMagnitude) {
                    maxMagnitude = Magnitude;
                }
                if (Magnitude < minMagnitude) {
                    minMagnitude = Magnitude;
                }

                double MagnitudeDelta = Magnitude - MagnitudePrev;
                MagnitudePrev = Magnitude;

                if (MagnitudeDelta > 2) {
                    stepcounter++;
                }
                double delta = maxMagnitude - minMagnitude;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
