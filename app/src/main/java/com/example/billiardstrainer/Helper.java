package com.example.billiardstrainer;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Helper {

    public static ArrayList<Shot> storeDataInArrays(ShotsDatabaseHelper shotsDatabaseHelper, Context context) {
        ArrayList<Shot> shots = new ArrayList<>();
        Cursor cursor = shotsDatabaseHelper.readAllData();

        if (cursor.getCount() == 0) {
            Toast.makeText(context, "Brak zapisanych uderzeń", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                Shot shot = new Shot();

                shot.setId(Integer.valueOf(cursor.getString(0)));
                shot.setPower(Double.valueOf(cursor.getString(1)));
                shot.setMaxAcceleration(Double.valueOf(cursor.getString(2)));
                shot.setMaxDeceleration(Double.valueOf(cursor.getString(3)));
                shot.setSmoothness(Integer.valueOf(cursor.getString(4)));
                shot.setDeviationLeft(Integer.valueOf(cursor.getString(5)));
                shot.setDeviationRight(Integer.valueOf(cursor.getString(6)));
                shot.setBackswingLength(Integer.valueOf(cursor.getString(7)));
                shot.setBackswingPause(Double.valueOf(cursor.getString(8)));
                shot.setFollowThrough(Integer.valueOf(cursor.getString(9)));
                shot.setEndPause(Double.valueOf(cursor.getString(10)));

                shots.add(shot);
            }
        }
        return shots;
    }

    public static double[] convertToDoubleArray(Queue<DisplacementEntry> displacementEntries, boolean x) {
        Queue<DisplacementEntry> entries = new LinkedList<>(displacementEntries);

        double value = 0.0d;
        int size = entries.size();

        double[] array = new double[size];

        if (x) {
            for (int i = 0; i < size; i++) {
                value = entries.poll().getXAxis();
                array[i] = Helper.changePrecision(value, 1);
            }
        } else {
            for (int i = 0; i < size; i++) {
                value = entries.poll().getYAxis();
                array[i] = Helper.changePrecision(value, 1);
            }
        }
        return array;
    }

    public static long[] convertTimeToLongArray(Queue<DisplacementEntry> displacementEntries) {

        Queue<DisplacementEntry> entries = new LinkedList<>(displacementEntries);
        int size = entries.size();
        long[] array = new long[size];

        for (int i = 0; i < size; i++) {
            array[i] = entries.poll().getTime();
        }
        return array;
    }

    public static LinkedList<DisplacementEntry> convertToLinkedList(double[] xArray, double[] yArray, long[] timeArray) {
        LinkedList<DisplacementEntry> displacementEntries = new LinkedList<>();
        int size = xArray.length;

        for (int i = 0; i < size; i++) {
            DisplacementEntry entry = new DisplacementEntry(xArray[i], yArray[i], timeArray[i]);
            displacementEntries.add(entry);
        }
        return displacementEntries;
    }

    public static double changePrecision(double value, int precision) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(precision, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
