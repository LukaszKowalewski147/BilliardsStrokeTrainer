package com.example.billiardstrainer;

import java.util.LinkedList;

public class ShotAnalyzer {

    private final LinkedList<DisplacementEntry> displacementEntries;
    private final int startingPoint;
    private final int contactPoint;
    private int endingPoint;
    private double maxAcceleration;

    public static int calculateFullLength(Shot shot) {
        return shot.getBackswingLength() + shot.getFollowThrough();
    }

    public static int calculateFullDeviation(Shot shot) {
        return Math.abs(shot.getDeviationLeft() - shot.getDeviationRight());
    }

    public ShotAnalyzer(LinkedList<DisplacementEntry> displacementEntries) {
        this.displacementEntries = displacementEntries;

        contactPoint = findContactPoint();
        startingPoint = findStartingPoint();
        endingPoint = findEndingPoint();
    }

    public Shot analyze() {
        Shot shot = new Shot();

        shot.setPower(calculatePower());
        shot.setMaxAcceleration(findMaxAcceleration());
        shot.setMaxDeceleration(findMaxDeceleration());

        int[] deviation = calculateDeviation();
        shot.setDeviationRight(deviation[0]);
        shot.setDeviationLeft(deviation[1]);

        int[] shotLength = calculateShotLength();
        shot.setBackswingLength(shotLength[0]);
        shot.setFollowThrough(shotLength[1]);

        shot.setBackswingPause(findBackPauseLength());
        shot.setEndPause(findEndPauseLength());

        shot.setSmoothness(calculateSmoothness());

        return shot;
    }

    private int findContactPoint() {
        double acceleration = 0.0d;
        double minAcceleration = 0.0d;
        int index = 0;

        for (int i = 0; i < displacementEntries.size(); i++) {
            acceleration = displacementEntries.get(i).getYAxis();
            if(minAcceleration > acceleration) {
                minAcceleration = acceleration;
                index = i;
            }
        }
        return index;
    }

    private int findStartingPoint() {
        double yCord = 0.0d;
        int pauseCounter = 0;
        int index = 0;

        for (int i = contactPoint - 40; i >= 0; i--) {  // przyjmuję że przyspieszanie trwa minimum 0.2s
            yCord = displacementEntries.get(i).getYAxis();
            if (yCord <= 0.4)    // 0.4 = pause threshold acceleration
            {
                ++pauseCounter;
                if (pauseCounter >= 3) // 0.005s * 3 = 0.015s of recognizable pause
                {
                    index = i + 3;
                    break;
                }
            } else {
                pauseCounter = 0;
            }
        }
        return index;
    }

    private int findEndingPoint() {
        double yCord = 0.0d;
        int pauseCounter = 0;
        int pauseIndex = 0;

        for (int i = contactPoint; i < displacementEntries.size(); i++) {
            yCord = displacementEntries.get(i).getYAxis();
            if (yCord <= 0.6d && yCord >= -0.6d)    // 0.6 = pause threshold acceleration
            {
                ++pauseCounter;
                if (pauseCounter >= 3) // 0.005s * 3 = 0.015s of recognizable pause
                {
                    pauseIndex = i - 2;
                    break;
                }
            } else {
                pauseCounter = 0;
            }
        }
        return pauseIndex;
    }

    private double calculatePower() {
        double power = 0.0d;
        double mass = 0.82d; // 820 gramów = 0.82kg - masa układu
        double acceleration = displacementEntries.get(contactPoint).getYAxis() * -1;
        power = mass * acceleration;
        return Helper.changePrecision(power, 1);
    }

    private double findMaxAcceleration() {
        double acceleration = 0.0d;
        double maxAcceleration = 0.0d;

        for (int i = startingPoint; i < contactPoint; i++) {
            acceleration = displacementEntries.get(i).getYAxis();
            if(maxAcceleration < acceleration)
                maxAcceleration = acceleration;
        }

        this.maxAcceleration = maxAcceleration;
        return maxAcceleration;
    }

    private double findMaxDeceleration() {
        return displacementEntries.get(contactPoint).getYAxis();
    }

    private int[] calculateDeviation() {
        double s0 = 0.0d;
        double v0 = 0.0d;
        double xCord = 0.0d;
        double deviationRight = 0.0d;
        double deviationLeft = 0.0d;
        long time = 0L;

        for (int i = startingPoint; i < contactPoint; i++) {
            xCord = displacementEntries.get(i).getXAxis();
            time = displacementEntries.get(i).getTime();
            s0 = calculateDisplacement(v0, xCord, time);

            if(s0 > 0.0d)
                deviationRight += s0;
            else
                deviationLeft += (-s0);

            v0 = calculateVelocity(v0, xCord, time);
        }

        deviationLeft *= 1000;
        deviationRight *= 1000;

        return new int[] {(int) deviationLeft, (int) deviationRight};
    }

    private int[] calculateShotLength() {
        double s0 = 0.0d;
        double v0 = 0.0d;
        double yCord = 0.0d;
        double backswing = 0.0d;
        double followThrough = 0.0d;
        long time = 0L;

        for (int i = startingPoint; i < contactPoint; i++) {
            yCord = displacementEntries.get(i).getYAxis();
            time = displacementEntries.get(i).getTime();
            s0 = calculateDisplacement(v0, yCord, time);
            backswing += s0;
            v0 = calculateVelocity(v0, yCord, time);
        }

        for (int i = contactPoint; i < endingPoint; i++) {
            yCord = displacementEntries.get(i).getYAxis();
            time = displacementEntries.get(i).getTime();
            s0 = calculateDisplacement(v0, yCord, time);
            followThrough += s0;
            v0 = calculateVelocity(v0, yCord, time);
            if (v0 < 0.0d)
                break;
        }

        backswing *= 100;
        followThrough *= 100;

        return new int[] {(int) backswing, (int) followThrough};
    }

    private double findBackPauseLength() {
        double yCord = 0.0d;
        double seconds = 0.0d;
        long time = 0L;

        for (int i = startingPoint; i >= 0; i--) {
            yCord = displacementEntries.get(i).getYAxis();
            if (yCord <= 1.0d && yCord >= -1.0d) {   // 0.5 = pause threshold acceleration
                time += displacementEntries.get(i).getTime();
            } else
                break;
        }

        seconds = getTimeInSeconds(time); //uzyskuje czas w sekundach z dokladnoscia do 3 miejsc po przecinku
        return Helper.changePrecision(seconds, 1); //zaokragla do 1 miejsca po przecinku
    }

    private double findEndPauseLength() {
        double yCord = 0.0d;
        double seconds = 0.0d;
        long time = 0L;

        for (int i = endingPoint; i < displacementEntries.size(); i++) {
            yCord = displacementEntries.get(i).getYAxis();
            if (yCord <= 1.4d && yCord >= -1.4d) {   // 1.4 = pause threshold acceleration
                time += displacementEntries.get(i).getTime();
            } else
                break;
        }

        seconds = getTimeInSeconds(time); //uzyskuje czas w sekundach z dokladnoscia do 3 miejsc po przecinku
        return Helper.changePrecision(seconds, 1); //zaokragla do 1 miejsca po przecinku
    }

    private int calculateSmoothness() {
        int smoothness = 0;
        double acceleration = 0.0d;
        double perfectAcceleration = 0.0d;
        double deviationCurrent = 0.0d;
        double deviationSum = 0.0d;
        double avgDeviation = 0.0d;
        double deviationPercent = 0.0d;
        double devider = (double) contactPoint - (double) startingPoint;
        double minAcceleration = displacementEntries.get(startingPoint).getYAxis();

        PerfectAccelerationFunction perfectAccelFunc =
                new PerfectAccelerationFunction(startingPoint, contactPoint, minAcceleration, maxAcceleration);

        for (int i = startingPoint; i < contactPoint; i++) {
            acceleration = displacementEntries.get(i).getYAxis();

            deviationCurrent = perfectAccelFunc.calculateDeviationPercent(acceleration, i);
            deviationSum += deviationCurrent;

            /*
            perfectAcceleration = perfectAccelFunc.getCurrentPerfectAcceleration(i);
            deviationCurrent = Math.abs(perfectAcceleration - acceleration);
            if (deviationCurrent > 0.3d) { // mała kompensacja w stronę ludzkiego błędu
                deviationSum += deviationCurrent - 0.3d;
            }*/
        }

        avgDeviation = deviationSum / devider;
        /*  // z proporcji
        deviationPercent = (avgDeviation * 100) / maxAcceleration; // % odchylen
        smoothness = (int) (100 - Math.round(deviationPercent)); // % plynnosci
        */
        smoothness = (int) (100 - Math.round(avgDeviation)); // % plynnosci
        return smoothness;
    }

    private int calculateSmoothnessOnFlatFunction() {
        int smoothness = 0;
        double acceleration = 0.0d;
        double accelerationSum = 0.0d;
        double avgAcceleration = 0.0d;
        double deviationSum = 0.0d;
        double avgDeviation = 0.0d;
        double devider = (double) contactPoint - (double) startingPoint - 1;

        for (int i = startingPoint; i < contactPoint; i++) {
            accelerationSum += displacementEntries.get(i).getYAxis();
        }

        avgAcceleration = accelerationSum / devider;

        for (int i = startingPoint; i < contactPoint; i++) {
            acceleration = displacementEntries.get(i).getYAxis();
            deviationSum += Math.abs(avgAcceleration - acceleration);
        }

        // z proporcji
        avgDeviation = deviationSum / devider;
        avgDeviation *= 100;
        smoothness = (int) Math.round(avgDeviation / avgAcceleration); // % odchylenia
        smoothness = 100 - smoothness; // % plynnosci

        return smoothness;
    }

    private double calculateDisplacement(double v0, double a, long time) {
        double s = 0.0d;
        double two = 2.0d;
        double t = getTimeInSeconds(time);
        s = (v0 * t) + ((a * t * t) / two);
        return Helper.changePrecision(s, 8);
    }

    private double calculateVelocity(double v0, double a, long time) {
        double v = 0.0d;
        double t = getTimeInSeconds(time);
        v = (a * t) + v0;
        return Helper.changePrecision(v, 5);
    }

    private double getTimeInSeconds(long time) {
        double divider = 1000000000.0d; // przelicznik z nanosekund na sekundy
        double t = time / divider;
        return Helper.changePrecision(t, 4);
    }
}