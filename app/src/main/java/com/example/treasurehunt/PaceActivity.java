package com.example.treasurehunt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;

import android.os.Bundle;
import android.view.View;

import org.w3c.dom.Text;

public class PaceActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Location startLocation;
    private int startSteps;
    private int currentSteps;
    private boolean started = false;
    Handler handler = new Handler(Looper.getMainLooper());
    private final float LOCATION_ACCURACY = 10;

    private EditText paceCounter;
    private TextView paceLabel;
    private Button finishButton;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pace);

        paceCounter = findViewById(R.id.paceText);
        paceLabel = findViewById(R.id.pacesLabel);
        startButton = findViewById(R.id.startButton);
        finishButton = findViewById(R.id.finishButton);

        paceCounter.setVisibility(View.GONE);
        paceLabel.setVisibility(View.GONE);
        finishButton.setVisibility(View.GONE);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 101);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 36);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startStepListener();
            } else {
                System.out.println("Location permissions were denied.");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED) {
            startStepListener();
        }
    }

    public void startPressed(View view) {
        final ProgressDialog dialog = ProgressService.showProgress(this, "Getting position...");
        getLocation(this, LOCATION_ACCURACY, new Consumer<Location>() {
            @Override
            public void accept(Location location) {
                dialog.cancel();
                startLocation = location;
                startSteps = currentSteps;
                started = true;

                paceCounter.setVisibility(View.VISIBLE);
                paceLabel.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.GONE);
                finishButton.setVisibility(View.VISIBLE);
                paceCounter.setText("0");
            }
        });
    }

    private static LocationListener listener;
    public static void getLocation(Context context, final float accuracy, final Consumer<Location> consumer) {
        final LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (location.getAccuracy() < accuracy) {
                    locationManager.removeUpdates(this);
                    consumer.accept(location);
                }
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) { }
            @Override
            public void onProviderDisabled(@NonNull String provider) { }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }
        };

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        } catch (SecurityException ex) {
            System.out.println("Location permission rejected.");
        }
    }

    public void startStepListener() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        currentSteps = (int)event.values[0];
        if (started) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    EditText text = findViewById(R.id.paceText);
                    int paces = (currentSteps - startSteps) / 2;
                    text.setText("" + paces);
                }
            });
        }

    }

    public void finishPressed(View view) {
        final Context context = this;
        final ProgressDialog dialog = ProgressService.showProgress(this, "Getting position...");
        getLocation(this, LOCATION_ACCURACY, new Consumer<Location>() {
            @Override
            public void accept(Location location) {
                dialog.cancel();

                double distance = distance(startLocation, location);
                int steps = currentSteps - startSteps;
                double paceLength = 2 * distance / steps;
                PaceService.setPaceLength(context, paceLength);

                paceCounter.setVisibility(View.GONE);
                paceLabel.setVisibility(View.GONE);
                startButton.setVisibility(View.VISIBLE);
                finishButton.setVisibility(View.GONE);

                Toast toast = Toast.makeText(context, String.format(getResources().getConfiguration().locale, "Pace length is %.1f meters.", paceLength), Toast.LENGTH_LONG);
                toast.show();

                finish();
            }
        });
    }

    private static double distance(Location loc1, Location loc2) {
        double theta = loc1.getLongitude() - loc2.getLongitude();
        double dist = Math.sin(Math.toRadians(loc1.getLatitude())) * Math.sin(Math.toRadians(loc2.getLatitude())) + Math.cos(Math.toRadians(loc1.getLatitude())) * Math.cos(Math.toRadians(loc2.getLatitude())) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1609.344;
        return (dist);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }


}