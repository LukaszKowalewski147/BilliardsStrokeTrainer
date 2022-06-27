package com.example.billiardstrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View;

import java.util.ArrayList;

public class Stats extends AppCompatActivity {

    private ArrayList<Shot> shots;

    private RelativeLayout powerLayout;
    private RelativeLayout smoothnessLayout;
    private RelativeLayout accuracyLayout;
    private RelativeLayout lengthLayout;
    private RelativeLayout pauseLayout;

    private TextView avgPowerTxt;
    private TextView avgSmoothnessTxt;
    private TextView avgFullDeviationTxt;
    private TextView avgLeftDeviationTxt;
    private TextView avgRightDeviationTxt;
    private TextView avgLengthTxt;
    private TextView avgBackswingTxt;
    private TextView avgFollowThroughTxt;
    private TextView avgBackPauseTxt;
    private TextView avgEndPauseTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        powerLayout = findViewById(R.id.stats_power_layout);
        smoothnessLayout = findViewById(R.id.stats_smoothness_layout);
        accuracyLayout = findViewById(R.id.stats_deviation_layout);
        lengthLayout = findViewById(R.id.stats_shot_length_layout);
        pauseLayout = findViewById(R.id.stats_pause_layout);

        avgPowerTxt = findViewById(R.id.stats_power_txt);
        avgSmoothnessTxt = findViewById(R.id.stats_smoothness_txt);
        avgFullDeviationTxt = findViewById(R.id.stats_deviation_txt);
        avgLeftDeviationTxt = findViewById(R.id.stats_deviation_left_txt);
        avgRightDeviationTxt = findViewById(R.id.stats_deviation_right_txt);
        avgLengthTxt = findViewById(R.id.stats_full_length_txt);
        avgBackswingTxt = findViewById(R.id.stats_backswing_length_txt);
        avgFollowThroughTxt = findViewById(R.id.stats_follow_through_length_txt);
        avgBackPauseTxt = findViewById(R.id.stats_pause_backswing_txt);
        avgEndPauseTxt = findViewById(R.id.stats_end_pause_txt);

        shots = new ArrayList<>();
        ShotsDatabaseHelper shotsDatabaseHelper = new ShotsDatabaseHelper(this);
        shots = Helper.storeDataInArrays(shotsDatabaseHelper, this);

        if (shots.size() > 0) {
            calculateAndShowAverages();
            setOnClickListeners();
        }
    }

    private void setOnClickListeners() {
        powerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGraph(ShotDetailCategory.POWER);
            }
        });

        smoothnessLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGraph(ShotDetailCategory.SMOOTHNESS);
            }
        });

        accuracyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGraph(ShotDetailCategory.ACCURACY);
            }
        });

        lengthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGraph(ShotDetailCategory.LENGTH);
            }
        });

        pauseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGraph(ShotDetailCategory.PAUSE);
            }
        });
    }

    private void openGraph(ShotDetailCategory category) {
        Intent intent = new Intent(this, StatsGraph.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    private void calculateAndShowAverages() {
        int[] avgDeviation = calculateAvgDeviation();
        int[] avgLength = calculateAvgLength();
        double[] avgPause = calculateAvgPause();
        int avgSmoothness = calculateAvgSmoothness();

        avgPowerTxt.setText("Średnio: " + calculateAvgPower() + "N");
        avgSmoothnessTxt.setText("Średnio: " + avgSmoothness + "%");
        avgFullDeviationTxt.setText("Błąd całkowity: " + avgDeviation[0] + "mm");
        avgLeftDeviationTxt.setText("Lewy: " + avgDeviation[1] + "mm");
        avgRightDeviationTxt.setText("Prawy: " + avgDeviation[2] + "mm");
        avgLengthTxt.setText("Całkowita: " + avgLength[0] + "cm");
        avgBackswingTxt.setText("Backswing: " + avgLength[1] + "cm");
        avgFollowThroughTxt.setText("Follow through: " + avgLength[2] + "cm");
        avgBackPauseTxt.setText("Przed: " + avgPause[0] + "s");
        avgEndPauseTxt.setText("Po: " + avgPause[1] + "s");

        colourText(avgSmoothness, avgDeviation[0], avgDeviation[1], avgDeviation[2]);
    }

    private void colourText(int avgSmoothness, int avgDeviation, int avgDeviationLeft, int avgDeviationRight) {
        int color;

        color = ShotDataHandler.colourSmoothness(avgSmoothness, this);
        avgSmoothnessTxt.setTextColor(color);

        color = ShotDataHandler.colourDeviation(avgDeviation, this);
        avgFullDeviationTxt.setTextColor(color);

        color = ShotDataHandler.colourDeviation(avgDeviationLeft, this);
        avgLeftDeviationTxt.setTextColor(color);

        color = ShotDataHandler.colourDeviation(avgDeviationRight, this);
        avgRightDeviationTxt.setTextColor(color);
    }

    private double calculateAvgPower() {
        double avgPower = 0.0d;
        double powerSum = 0.0d;

        for (Shot shot : shots) {
            powerSum += shot.getPower();
        }

        avgPower = powerSum / shots.size();
        return Helper.changePrecision(avgPower, 1);
    }

    private int calculateAvgSmoothness() {
        int avgSmoothness = 0;
        int smoothnessSum = 0;

        for (Shot shot : shots) {
            smoothnessSum += shot.getSmoothness();
        }

        avgSmoothness = (int) Math.round((double)smoothnessSum / shots.size());

        return avgSmoothness;
    }

    private int[] calculateAvgDeviation() {
        int avgFullDeviation = 0;
        int avgLeftDeviation = 0;
        int avgRightDeviation = 0;
        int deviationLeftSum = 0;
        int deviationRightSum = 0;
        int deviationFullSum = 0;

        for (Shot shot : shots) {
            deviationLeftSum += shot.getDeviationLeft();
            deviationRightSum += shot.getDeviationRight();
            deviationFullSum += Math.abs(shot.getDeviationLeft() - shot.getDeviationRight());
        }

        avgLeftDeviation = (int) Math.round((double)deviationLeftSum / shots.size());
        avgRightDeviation = (int) Math.round((double)deviationRightSum / shots.size());
        avgFullDeviation = (int) Math.round((double)deviationFullSum / shots.size());

        return new int[]{avgFullDeviation, avgLeftDeviation, avgRightDeviation};
    }

    private int[] calculateAvgLength() {
        int avgFullLength = 0;
        int avgBackswing = 0;
        int avgFollowThrough = 0;
        int backswingSum = 0;
        int followThroughSum = 0;

        for (Shot shot : shots) {
            backswingSum += shot.getBackswingLength();
            followThroughSum += shot.getFollowThrough();
        }

        avgBackswing = (int) Math.round((double)backswingSum / shots.size());
        avgFollowThrough = (int) Math.round((double)followThroughSum / shots.size());
        avgFullLength = avgBackswing + avgFollowThrough;

        return new int[]{avgFullLength, avgBackswing, avgFollowThrough};
    }

    private double[] calculateAvgPause() {
        double avgBackPause = 0.0d;
        double avgEndPause = 0.0d;
        double backPauseSum = 0.0d;
        double endPauseSum = 0.0d;

        for (Shot shot : shots) {
            backPauseSum += shot.getBackswingPause();
            endPauseSum += shot.getEndPause();
        }

        avgBackPause = backPauseSum / shots.size();
        avgEndPause = endPauseSum / shots.size();

        avgBackPause = Helper.changePrecision(avgBackPause, 1);
        avgEndPause = Helper.changePrecision(avgEndPause, 1);

        return new double[]{avgBackPause, avgEndPause};
    }
}