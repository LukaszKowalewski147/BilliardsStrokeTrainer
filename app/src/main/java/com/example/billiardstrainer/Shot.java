package com.example.billiardstrainer;

public class Shot {

    private int id;
    private double power;
    private int smoothness;
    private double maxAcceleration;
    private double maxDeceleration;
    private int deviationLeft;
    private int deviationRight;
    private int backswingLength;
    private double backswingPause;
    private int followThrough;
    private double endPause;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public int getSmoothness() {
        return smoothness;
    }

    public void setSmoothness(int smoothness) {
        this.smoothness = smoothness;
    }

    public double getMaxAcceleration() {
        return maxAcceleration;
    }

    public void setMaxAcceleration(double maxAcceleration) {
        this.maxAcceleration = maxAcceleration;
    }

    public double getMaxDeceleration() {
        return maxDeceleration;
    }

    public void setMaxDeceleration(double maxDeceleration) {
        this.maxDeceleration = maxDeceleration;
    }

    public int getDeviationLeft() {
        return deviationLeft;
    }

    public void setDeviationLeft(int deviationLeft) {
        this.deviationLeft = deviationLeft;
    }

    public int getDeviationRight() {
        return deviationRight;
    }

    public void setDeviationRight(int deviationRight) {
        this.deviationRight = deviationRight;
    }

    public int getBackswingLength() {
        return backswingLength;
    }

    public void setBackswingLength(int backswingLength) {
        this.backswingLength = backswingLength;
    }

    public double getBackswingPause() {
        return backswingPause;
    }

    public void setBackswingPause(double backswingPause) {
        this.backswingPause = backswingPause;
    }

    public int getFollowThrough() {
        return followThrough;
    }

    public void setFollowThrough(int followThrough) {
        this.followThrough = followThrough;
    }

    public double getEndPause() {
        return endPause;
    }

    public void setEndPause(double endPause) {
        this.endPause = endPause;
    }
}
