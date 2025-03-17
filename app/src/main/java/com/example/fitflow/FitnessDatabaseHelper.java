package com.example.fitflow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FitnessDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "fitness_tracker.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "fitness_data";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_STEPS = "steps";
    private static final String COLUMN_DISTANCE = "distance";
    private static final String COLUMN_CALORIES = "calories";
    private static final String COLUMN_TIME = "time";

    public FitnessDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STEPS + " INTEGER, " +
                COLUMN_DISTANCE + " REAL, " +
                COLUMN_CALORIES + " REAL, " +
                COLUMN_TIME + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertFitnessData(int steps, double distance, double calories, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STEPS, steps);
        values.put(COLUMN_DISTANCE, distance);
        values.put(COLUMN_CALORIES, calories);
        values.put(COLUMN_TIME, time);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    public Cursor getAllFitnessData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT SUBSTR(" + COLUMN_TIME + ", 1, 10) AS date, " +
                COLUMN_STEPS +
                " FROM " + TABLE_NAME +
                " ORDER BY " + COLUMN_TIME + " DESC LIMIT 7", null);
    }

}
