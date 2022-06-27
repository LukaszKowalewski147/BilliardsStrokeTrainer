package com.example.billiardstrainer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ShotDetails extends AppCompatActivity {

    private TextView powerTxt;
    private TextView smoothnessTxt;
    private TextView deviationTxt;
    private TextView deviationLeftTxt;
    private TextView deviationRightTxt;
    private TextView backswingLengthTxt;
    private TextView followThroughTxt;
    private TextView fullLengthTxt;
    private TextView backswingPauseTxt;
    private TextView endPauseTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shot_details);

        powerTxt = findViewById(R.id.details_power_txt);
        smoothnessTxt = findViewById(R.id.details_smoothness_txt);
        deviationTxt = findViewById(R.id.details_deviation_txt);
        deviationLeftTxt = findViewById(R.id.details_deviation_left_txt);
        deviationRightTxt = findViewById(R.id.details_deviation_right_txt);
        backswingLengthTxt = findViewById(R.id.details_backswingsummary_length_txt);
        followThroughTxt = findViewById(R.id.details_follow_through_length_txt);
        fullLengthTxt = findViewById(R.id.details_full_length_txt);
        backswingPauseTxt = findViewById(R.id.details_pause_backswing_txt);
        endPauseTxt = findViewById(R.id.details_end_pause_txt);

        int shotNumber = 0;
        int smoothness = 0;
        int deviation = 0;
        int deviationLeft = 0;
        int deviationRight = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            shotNumber = (int) extras.get("shotNumber");
            smoothness = (int) extras.get("smoothness");
            deviation = (int) extras.get("deviation");
            deviationLeft = (int) extras.get("deviationLeft");
            deviationRight = (int) extras.get("deviationRight");

            powerTxt.setText(extras.get("power") + "N");
            smoothnessTxt.setText(smoothness + "%");
            deviationTxt.setText("Całkowity: " + deviation + "mm");
            deviationLeftTxt.setText("Błąd lewy: " + deviationLeft + "mm");
            deviationRightTxt.setText("Błąd prawy: " + deviationRight + "mm");
            backswingLengthTxt.setText("Backswing: " + extras.get("backswingLength") + "cm");
            followThroughTxt.setText("Follow through: " + extras.get("followThrough") + "cm");
            fullLengthTxt.setText("Całkowita: " + extras.get("fullLength") + "cm");
            backswingPauseTxt.setText("Przed: " + extras.get("backswingPause") + "s");
            endPauseTxt.setText("Po: " + extras.get("endPause") + "s");
        } else {
        }
        manageActionBar(shotNumber);
        colourText(smoothness, deviation, deviationLeft, deviationRight);
    }

    private void colourText(int smoothness, int deviation, int deviationLeft, int deviationRight) {
        int color;

        color = ShotDataHandler.colourSmoothness(smoothness, this);
        smoothnessTxt.setTextColor(color);

        color = ShotDataHandler.colourDeviation(deviation, this);
        deviationTxt.setTextColor(color);

        color = ShotDataHandler.colourDeviation(deviationLeft, this);
        deviationLeftTxt.setTextColor(color);

        color = ShotDataHandler.colourDeviation(deviationRight, this);
        deviationRightTxt.setTextColor(color);
    }

    private void manageActionBar(int shotNumber) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Uderzenie " + shotNumber);
    }
}