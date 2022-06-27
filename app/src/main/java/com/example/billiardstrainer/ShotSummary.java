package com.example.billiardstrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

public class ShotSummary extends AppCompatActivity {

    private ShotsDatabaseHelper shotsDatabaseHelper;
    private LinkedList<DisplacementEntry> displacementEntries;

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

    private Button saveShotBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shot_summary);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            double[] xArray = (double[]) extras.get("xArrayKey");
            double[] yArray = (double[]) extras.get("yArrayKey");
            long[] timeArray = (long[]) extras.get("timeArrayKey");
            displacementEntries = Helper.convertToLinkedList(xArray, yArray, timeArray);
        } else {
        }

        shotsDatabaseHelper = new ShotsDatabaseHelper(this);

        powerTxt = findViewById(R.id.summary_power_txt);
        smoothnessTxt = findViewById(R.id.summary_smoothness_txt);
        deviationTxt = findViewById(R.id.summary_deviation_txt);
        deviationLeftTxt = findViewById(R.id.summary_deviation_left_txt);
        deviationRightTxt = findViewById(R.id.summary_deviation_right_txt);
        backswingLengthTxt = findViewById(R.id.summary_backswingsummary_length_txt);
        followThroughTxt = findViewById(R.id.summary_follow_through_length_txt);
        fullLengthTxt = findViewById(R.id.summary_full_length_txt);
        backswingPauseTxt = findViewById(R.id.summary_pause_backswing_txt);
        endPauseTxt = findViewById(R.id.summary_end_pause_txt);

        saveShotBtn = findViewById(R.id.summary_save_btn);

        Shot shot = prepareSummary();

        saveShotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveShot(shot);
            }
        });
    }

    private Shot prepareSummary() {
        ShotAnalyzer analyzer = new ShotAnalyzer(displacementEntries);
        Shot shot = analyzer.analyze();

        int smoothness = shot.getSmoothness();
        int deviation = ShotAnalyzer.calculateFullDeviation(shot);
        int deviationLeft = shot.getDeviationLeft();
        int deviationRight = shot.getDeviationRight();
        int length = ShotAnalyzer.calculateFullLength(shot);

        powerTxt.setText(shot.getPower() + "N");
        smoothnessTxt.setText(smoothness + "%");
        deviationTxt.setText("Całkowity: " + deviation + "mm");
        deviationLeftTxt.setText("Błąd lewy: " + deviationLeft + "mm");
        deviationRightTxt.setText("Błąd prawy: " + deviationRight + "mm");
        backswingLengthTxt.setText("Backswing: " + shot.getBackswingLength() + "cm");
        followThroughTxt.setText("Follow through: " + shot.getFollowThrough() + "cm");
        fullLengthTxt.setText("Całkowita: " + length + "cm");
        backswingPauseTxt.setText("Przed: " + shot.getBackswingPause() + "s");
        endPauseTxt.setText("Po: " + shot.getEndPause() + "s");

        colourText(smoothness, deviation, deviationLeft, deviationRight);

        return shot;
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

    private void saveShot(Shot shot) {
        boolean saveSuccessful = shotsDatabaseHelper.addData(shot);
        if (saveSuccessful) {
            Toast.makeText(this, "Zapisano uderzenie", Toast.LENGTH_SHORT).show();
            saveShotBtn.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, "Błąd zapisu", Toast.LENGTH_SHORT).show();
        }
    }
}