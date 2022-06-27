package com.example.billiardstrainer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ShotsDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "ShotsDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "shots_table";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_2 = "power";
    private static final String COLUMN_3 = "max_acceleration";
    private static final String COLUMN_4 = "max_deceleration";
    private static final String COLUMN_5 = "smoothness";
    private static final String COLUMN_6 = "deviation_left";
    private static final String COLUMN_7 = "deviation_right";
    private static final String COLUMN_8 = "backswing_length";
    private static final String COLUMN_9 = "backswing_pause";
    private static final String COLUMN_10 = "follow_through";
    private static final String COLUMN_11 = "end_pause";

    public ShotsDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String querry =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_2 + " REAL, " +
                        COLUMN_3 + " REAL, " +
                        COLUMN_4 + " REAL, " +
                        COLUMN_5 + " INTEGER, " +
                        COLUMN_6 + " INTEGER, " +
                        COLUMN_7 + " INTEGER, " +
                        COLUMN_8 + " INTEGER, " +
                        COLUMN_9 + " REAL, " +
                        COLUMN_10 + " INTEGER, " +
                        COLUMN_11 + " REAL);";

        sqLiteDatabase.execSQL(querry);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean addData(Shot shot) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_2, shot.getPower());
        contentValues.put(COLUMN_3, shot.getMaxAcceleration());
        contentValues.put(COLUMN_4, shot.getMaxDeceleration());
        contentValues.put(COLUMN_5, shot.getSmoothness());
        contentValues.put(COLUMN_6, shot.getDeviationLeft());
        contentValues.put(COLUMN_7, shot.getDeviationRight());
        contentValues.put(COLUMN_8, shot.getBackswingLength());
        contentValues.put(COLUMN_9, shot.getBackswingPause());
        contentValues.put(COLUMN_10, shot.getFollowThrough());
        contentValues.put(COLUMN_11, shot.getEndPause());

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) // -1 if data inserted incorrectly
            return false;
        else return true;
    }

    public Cursor readAllData() {
        String querry = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(querry, null);
        }
        return cursor;
    }

    public void deleteOneRow(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[] {row_id});
        if (result == -1) {
            Toast.makeText(context, "Niepowodzenie", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Usunięto", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
