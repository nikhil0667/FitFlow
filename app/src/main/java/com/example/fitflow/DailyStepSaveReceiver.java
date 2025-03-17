package com.example.fitflow;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DailyStepSaveReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("StepPref", Context.MODE_PRIVATE);
        int steps = sharedPreferences.getInt("currentSteps", 0); // Ensure this is updating correctly

        Log.d("DailyStepSaveReceiver", "Received alarm! Steps: " + steps); // Debugging Log

        double distance = steps * 0.0008;
        int calories = (int) (steps * 0.04);

        // Store in the database
        FitnessDatabaseHelper dbHelper = new FitnessDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("steps", steps);
        values.put("distance", distance);
        values.put("calories", calories);
        values.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));

        long result = db.insert("fitness_data", null, values);
        db.close();

        if (result == -1) {
            Log.e("DailyStepSaveReceiver", "Failed to insert data into database.");
        } else {
            Log.d("DailyStepSaveReceiver", "Data successfully inserted into database.");
        }

        // Reset steps in SharedPreferences for next day
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("currentSteps", 0);
        editor.apply();

        Log.d("DailyStepSaveReceiver", "Steps reset for the next day.");

//        // Correct way to reschedule the alarm
        scheduleNextAlarm(context);
//      Log.d("DailyStepSaveReceiver", "Alarm Triggered! BroadcastReceiver is working.");

    }

    private void scheduleNextAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyStepSaveReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);


        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);  // Schedule for the next day
        }

        long triggerTime = calendar.getTimeInMillis();

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
            );
            Log.d("Alarm", "Next Alarm Scheduled at: " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(calendar.getTime()));
        }
    }

}

