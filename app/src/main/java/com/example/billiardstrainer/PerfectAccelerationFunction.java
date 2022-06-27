package com.example.billiardstrainer;

public class PerfectAccelerationFunction {

    private final int startIndex;
    private final int stopIndex;
    private final double minAcceleration;
    private final double maxAcceleration;
    private final double step;

    public PerfectAccelerationFunction (int startIndex, int stopIndex, double minAcceleration, double maxAcceleration) {
        this.startIndex = startIndex;
        this.stopIndex = stopIndex;
        this.minAcceleration = minAcceleration;
        this.maxAcceleration = maxAcceleration;
        step = calculateStep();
    }

    public double calculateDeviationPercent(double acceleration, int index) {
        double deviationPercent = 0.0d;
        double compensation = 1.0d; // mała kompensacja w stronę ludzkiego błędu
        double perfectAcceleration = getCurrentPerfectAcceleration(index);
        double error = Math.abs(perfectAcceleration - acceleration);

        if (error <= compensation)
            return 0.0d;

        error -= compensation;

        deviationPercent = (error * 100) / perfectAcceleration;
        if (error > 100.0d)
            return 100.0d;

        return deviationPercent;
    }

    private double getCurrentPerfectAcceleration(int index) {
        double accelerationIncrease = (double)(index - startIndex) * step;
        accelerationIncrease += minAcceleration;
        return Helper.changePrecision(accelerationIncrease, 1);
    }

    private double calculateStep() {
        double stepCount = (double) stopIndex - (double) startIndex;
        return (maxAcceleration - minAcceleration) / stepCount;
    }
}
