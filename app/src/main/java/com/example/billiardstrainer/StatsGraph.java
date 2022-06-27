package com.example.billiardstrainer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class StatsGraph extends AppCompatActivity {

    private GraphView graph;
    private ArrayList<Shot> shots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_graph);

        graph = findViewById(R.id.graph);

        shots = new ArrayList<>();
        ShotsDatabaseHelper shotsDatabaseHelper = new ShotsDatabaseHelper(this);
        shots = Helper.storeDataInArrays(shotsDatabaseHelper, this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ShotDetailCategory category = (ShotDetailCategory) extras.get("category");
            takeActionsFor(category);
        } else {
        }
    }

    private void manageActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
    }

    private void takeActionsFor(ShotDetailCategory category) {
        switch (category) {
            case POWER:
                manageActionBar("Wykres siły");
                preparePowerGraph();
                break;
            case SMOOTHNESS:
                manageActionBar("Wykres płynności");
                prepareSmoothnessGraph();
                break;
            case ACCURACY:
                manageActionBar("Wykres celności");
                prepareAccuracyGraph();
                break;
            case LENGTH:
                manageActionBar("Wykres długości");
                prepareLengthGraph();
                break;
            case PAUSE:
                manageActionBar("Wykres pauz");
                preparePauseGraph();
                break;
        }
    }

    private void preparePowerGraph() {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        double x, y;
        x = 0.0d;

        for (int i = 0; i < shots.size(); i++) {
            x += 1.0d;
            y = shots.get(i).getPower();
            series.appendData(new DataPoint(x, y), true, 500);
        }
        setStandardGraphParameters();

        series.setTitle("Siła");
        series.setColor(Color.RED);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10.0f);

        graph.getViewport().setMaxY(series.getHighestValueY() + 20.0d);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    if (value % 1 == 0) {
                        return super.formatLabel(value, isValueX);
                    } else
                        return null;
                }
                return super.formatLabel(value, isValueX) + "N";
            }
        });
        graph.addSeries(series);
    }

    private void prepareSmoothnessGraph() {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        double x, y;
        x = 0.0d;

        for (int i = 0; i < shots.size(); i++) {
            x += 1.0d;
            y = shots.get(i).getSmoothness();
            series.appendData(new DataPoint(x, y), true, 500);
        }
        setStandardGraphParameters();

        series.setTitle("Płynność");
        series.setColor(Color.RED);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10.0f);

        graph.getViewport().setMaxY(110.0d);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    if (value % 1 == 0) {
                        return super.formatLabel(value, isValueX);
                    } else
                        return null;
                }
                return super.formatLabel(value, isValueX) + "%";
            }
        });
        graph.addSeries(series);
    }

    private void prepareAccuracyGraph() {
        LineGraphSeries<DataPoint> seriesFull = new LineGraphSeries<>();
        LineGraphSeries<DataPoint> seriesLeft = new LineGraphSeries<>();
        LineGraphSeries<DataPoint> seriesRight = new LineGraphSeries<>();
        double x, yFull, yLeft, yRight;
        x = 0.0d;

        for (int i = 0; i < shots.size(); i++) {
            x += 1.0d;
            yLeft = shots.get(i).getDeviationLeft();
            yRight = shots.get(i).getDeviationRight();
            yFull = Math.abs(yLeft - yRight);

            seriesFull.appendData(new DataPoint(x, yFull), true, 500);
            seriesLeft.appendData(new DataPoint(x, yLeft), true, 500);
            seriesRight.appendData(new DataPoint(x, yRight), true, 500);
        }
        setStandardGraphParameters();

        seriesFull.setTitle("Błąd całkowity");
        seriesLeft.setTitle("Błąd lewy");
        seriesRight.setTitle("Błąd prawy");

        seriesFull.setColor(Color.RED);
        seriesLeft.setColor(Color.rgb(0, 190, 0));
        seriesRight.setColor(Color.BLUE);

        seriesFull.setDrawDataPoints(true);
        seriesLeft.setDrawDataPoints(true);
        seriesRight.setDrawDataPoints(true);
        seriesFull.setDataPointsRadius(12.0f);
        seriesLeft.setDataPointsRadius(10.0f);
        seriesRight.setDataPointsRadius(10.0f);

        seriesFull.setThickness(10);

        double maxValue = Math.max(Math.max(seriesLeft.getHighestValueY(), seriesRight.getHighestValueY()), seriesFull.getHighestValueY());
        graph.getViewport().setMaxY(maxValue + 20.0d);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    if (value % 1 == 0) {
                        return super.formatLabel(value, isValueX);
                    } else
                        return null;
                }
                return super.formatLabel(value, isValueX) + "mm";
            }
        });
        graph.addSeries(seriesFull);
        graph.addSeries(seriesLeft);
        graph.addSeries(seriesRight);
    }

    private void prepareLengthGraph() {
        LineGraphSeries<DataPoint> seriesFull = new LineGraphSeries<>();
        LineGraphSeries<DataPoint> seriesBackswing = new LineGraphSeries<>();
        LineGraphSeries<DataPoint> seriesFollowThrough = new LineGraphSeries<>();
        double x, yFull, yBackswing, yFollowThrough;
        x = 0.0d;

        for (int i = 0; i < shots.size(); i++) {
            x += 1.0d;
            yBackswing = shots.get(i).getBackswingLength();
            yFollowThrough = shots.get(i).getFollowThrough();
            yFull = yBackswing + yFollowThrough;

            seriesFull.appendData(new DataPoint(x, yFull), true, 500);
            seriesBackswing.appendData(new DataPoint(x, yBackswing), true, 500);
            seriesFollowThrough.appendData(new DataPoint(x, yFollowThrough), true, 500);
        }
        setStandardGraphParameters();

        seriesFull.setTitle("Całkowita");
        seriesBackswing.setTitle("Backswing");
        seriesFollowThrough.setTitle("Follow through");

        seriesFull.setColor(Color.RED);
        seriesBackswing.setColor(Color.rgb(0, 190, 0));
        seriesFollowThrough.setColor(Color.BLUE);

        seriesFull.setDrawDataPoints(true);
        seriesBackswing.setDrawDataPoints(true);
        seriesFollowThrough.setDrawDataPoints(true);
        seriesFull.setDataPointsRadius(12.0f);
        seriesBackswing.setDataPointsRadius(10.0f);
        seriesFollowThrough.setDataPointsRadius(10.0f);

        seriesFull.setThickness(10);

        graph.getViewport().setMaxY(seriesFull.getHighestValueY() + 20.0d);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    if (value % 1 == 0) {
                        return super.formatLabel(value, isValueX);
                    } else
                        return null;
                }
                return super.formatLabel(value, isValueX) + "cm";
            }
        });
        graph.addSeries(seriesFull);
        graph.addSeries(seriesBackswing);
        graph.addSeries(seriesFollowThrough);
    }

    private void preparePauseGraph() {
        LineGraphSeries<DataPoint> seriesBack = new LineGraphSeries<>();
        LineGraphSeries<DataPoint> seriesEnd = new LineGraphSeries<>();
        double x, yBack, yEnd;
        x = 0.0d;

        for (int i = 0; i < shots.size(); i++) {
            x += 1.0d;
            yBack = shots.get(i).getBackswingPause();
            yEnd = shots.get(i).getEndPause();
            seriesBack.appendData(new DataPoint(x, yBack), true, 500);
            seriesEnd.appendData(new DataPoint(x, yEnd), true, 500);
        }
        setStandardGraphParameters();

        seriesBack.setTitle("Przed");
        seriesEnd.setTitle("Po");

        seriesBack.setColor(Color.RED);
        seriesEnd.setColor(Color.BLUE);

        seriesBack.setDrawDataPoints(true);
        seriesEnd.setDrawDataPoints(true);
        seriesBack.setDataPointsRadius(10.0f);
        seriesEnd.setDataPointsRadius(10.0f);

        double maxValue = Math.max(seriesBack.getHighestValueY(), seriesEnd.getHighestValueY());
        graph.getViewport().setMaxY(maxValue + 1.0d);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    if (value % 1 == 0) {
                        return super.formatLabel(value, isValueX);
                    } else
                        return null;
                }
                return super.formatLabel(value, isValueX) + "s";
            }
        });

        graph.addSeries(seriesBack);
        graph.addSeries(seriesEnd);
    }

    private void setStandardGraphParameters() {
        int maxXLabels = 10;
        if (shots.size() < 10)
            maxXLabels = shots.size() + 1;
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        graph.getGridLabelRenderer().setHorizontalAxisTitle("Numer zagrania");
        graph.getGridLabelRenderer().setNumHorizontalLabels(maxXLabels);
        graph.getGridLabelRenderer().setNumVerticalLabels(12);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0.0d);
        graph.getViewport().setMaxX(shots.size());
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0.0d);
        graph.getViewport().setScalable(true);
    }
}