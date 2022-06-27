package com.example.billiardstrainer;

import android.content.Context;

public class ShotDataHandler {

    public static int colourSmoothness(int smoothness, Context context) {
        int perfectColor = context.getResources().getColor(R.color.pink);
        int gooodColor = context.getResources().getColor(R.color.green);
        int okColor = context.getResources().getColor(R.color.yellow);
        int badColor = context.getResources().getColor(R.color.orange);
        int veryBadColor = context.getResources().getColor(R.color.red);

        if (smoothness >= 90)
            return perfectColor;
        else if (smoothness >= 80 && smoothness < 90)
            return gooodColor;
        else if (smoothness >= 70 && smoothness < 80)
            return okColor;
        else if (smoothness >= 55 && smoothness < 70)
            return badColor;
        else
            return veryBadColor;
    }

    public static int colourDeviation(int deviation, Context context) {
        int perfectColor = context.getResources().getColor(R.color.pink);
        int gooodColor = context.getResources().getColor(R.color.green);
        int okColor = context.getResources().getColor(R.color.yellow);
        int badColor = context.getResources().getColor(R.color.orange);
        int veryBadColor = context.getResources().getColor(R.color.red);

        if (deviation < 5)
            return perfectColor;
        else if (deviation >= 5 && deviation < 10)
            return gooodColor;
        else if (deviation >= 10 && deviation < 25)
            return okColor;
        else if (deviation >= 25 && deviation < 45)
            return badColor;
        else
            return veryBadColor;
    }
}
