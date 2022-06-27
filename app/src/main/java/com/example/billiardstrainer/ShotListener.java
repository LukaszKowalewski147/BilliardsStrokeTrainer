package com.example.billiardstrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ShotListener extends AppCompatActivity implements SensorEventListener {

    private Displacement displacement;
    private SoundManager soundManager;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private Button startShotListening;
    private Button stopShotListening;

    private double xAxis;
    private double yAxis;
    private long lastTime;
    private long time;
    private boolean firstEvent;
    private AtomicBoolean contactOccured;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shot_listener);

        displacement = new Displacement();
        soundManager = new SoundManager(this);
        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        startShotListening = findViewById(R.id.listener_start_listening_btn);
        stopShotListening = findViewById(R.id.listener_stop_listening_btn);

        stopShotListening.setVisibility(View.GONE);

        xAxis = 0.0d;
        yAxis = 0.0d;
        lastTime = 0L;
        time = 0L;
        firstEvent = true;
        contactOccured = new AtomicBoolean(false);

        startShotListening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startListener();
            }
        });

        stopShotListening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAccelerometerStop();
            }
        });
    }

    private void startListener() {
        TextView countDownTxt = (TextView) findViewById( R.id.listener_countdown_hint_txt);
        countDownTxt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 200f);
        countDownTxt.setTextColor(Color.parseColor("#FF0000"));

        startShotListening.setVisibility(View.GONE);

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDownTxt.setText(new SimpleDateFormat("s").format(new Date( millisUntilFinished+1000)));
                soundManager.playCountdownSound();
            }

            public void onFinish() {
                soundManager.playStartShootingSound();
                countDownTxt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40f);
                countDownTxt.setText("Oczekiwanie" + System.lineSeparator() + "na" + System.lineSeparator() + "uderzenie");
                stopShotListening.setVisibility(View.VISIBLE);

                startAccelerometer();
                manageAfterContact();
            }
        }.start();
    }

    private void manageAfterContact() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while(!contactOccured.get()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                soundManager.playContactSound();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handleAccelerometerStop();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void handleAccelerometerStop() {
        stopAccelerometer();
        soundManager.playEndShootingSound();
        Queue<DisplacementEntry> displacementEntries = displacement.getEntries();
        double[] xArray = Helper.convertToDoubleArray(displacementEntries, true);
        double[] yArray = Helper.convertToDoubleArray(displacementEntries, false);
        long[] timeArray = Helper.convertTimeToLongArray(displacementEntries);

        openAnalyzer(xArray, yArray, timeArray);
    }

    private void openAnalyzer(double[] xArray, double[] yArray, long[] timeArray) {
        Intent intent = new Intent(this, ShotSummary.class);
        intent.putExtra("xArrayKey", xArray);
        intent.putExtra("yArrayKey", yArray);
        intent.putExtra("timeArrayKey", timeArray);
        startActivity(intent);
    }

    public void startAccelerometer() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void stopAccelerometer() {
        sensorManager.unregisterListener(this, accelerometer);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        xAxis = sensorEvent.values[0];
        yAxis = sensorEvent.values[1];

        if (firstEvent) {
            lastTime = sensorEvent.timestamp;
            firstEvent = false;
        }

        if (yAxis < -40.0d) {
            contactOccured.compareAndSet(false, true);
        }

        time = sensorEvent.timestamp - lastTime;
        lastTime = sensorEvent.timestamp;

        displacement.addEntry(xAxis, yAxis, time);
        //showResults(xAxis, yAxis, time);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}