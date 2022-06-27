package com.example.billiardstrainer;

public class DisplacementEntry {

    private final double xAxis;
    private final double yAxis;
    private final long time;

    public DisplacementEntry(double xAxis, double yAxis, long time) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.time = time;
    }

    public double getXAxis() {
        return xAxis;
    }

    public double getYAxis() {
        return yAxis;
    }

    public long getTime() {
        return time;
    }
}
