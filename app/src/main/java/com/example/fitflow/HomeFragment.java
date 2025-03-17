package com.example.fitflow;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class HomeFragment extends Fragment {
    private TextView stepsText, distanceText, caloriesText;
    private AppCompatButton startButton;
    private boolean isTracking = false;
    private boolean isPermissionGranted;
    private static final String TAG = "StepTracker";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        stepsText = view.findViewById(R.id.stepsText);
        distanceText = view.findViewById(R.id.distanceText);
        caloriesText = view.findViewById(R.id.caloriesText);
        startButton = view.findViewById(R.id.startButton);

        // Get permission status from HomeScreen
        isPermissionGranted = ((HomeScreen) requireActivity()).isPermissionGranted;
        if (!isPermissionGranted) {
            Toast.makeText(requireContext(), "Permission required for step tracking!", Toast.LENGTH_SHORT).show();
        }else{
            isTracking = !isTracking;
            if (isTracking) {
                startButton.setText("Stop Tracking");
                startStepCounterService();
                scheduleDailyStepSave();
                Log.d(TAG, "Tracking started.");
            } else {
                startButton.setText("Start Tracking");
                stopStepCounterService();
                Log.d(TAG, "Tracking stopped.");
            }
        }


        startButton.setOnClickListener(v -> {
            if (!isPermissionGranted) {
                Toast.makeText(requireContext(), "Permission required for step tracking!", Toast.LENGTH_SHORT).show();
                return;
            }

            isTracking = !isTracking;
            if (isTracking) {
                startButton.setText("Stop Tracking");
                startStepCounterService();
                scheduleDailyStepSave();
                Log.d(TAG, "Tracking started.");
            } else {
                startButton.setText("Start Tracking");
                stopStepCounterService();
                Log.d(TAG, "Tracking stopped.");
            }
        });

        return view;
    }

    private void startStepCounterService() {
        Intent serviceIntent = new Intent(requireContext(), StepCounterService.class);
        requireContext().startService(serviceIntent);
    }

    private void stopStepCounterService() {
        Intent serviceIntent = new Intent(requireContext(), StepCounterService.class);
        requireContext().stopService(serviceIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("StepPref", Context.MODE_PRIVATE);
        int steps = sharedPreferences.getInt("currentSteps", 0);
        double distance = steps * 0.0008;
        int calories = (int) (steps * 0.04);

        stepsText.setText("Steps: " + steps);
        distanceText.setText(String.format("%.2f km", distance));
        caloriesText.setText(String.format("%.2f kcal", (double) calories));


        Log.d(TAG, "onResume: Loaded step data from SharedPreferences.");
    }

//    void scheduleDailyStepSave() {
//        Toast.makeText(getContext(), "Sec Started", Toast.LENGTH_SHORT).show();
//
//        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(requireContext(), DailyStepSaveReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 1);  // Midnight (00:38 AM)
//        calendar.set(Calendar.MINUTE, 19);
//        calendar.set(Calendar.SECOND, 0);
//
//        // Ensure the alarm is set for the next day if the time has already passed
//        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
//            calendar.add(Calendar.DAY_OF_YEAR, 1);
//        }
//
//        if (alarmManager == null) {
//            Log.e("AlarmManager", "AlarmManager is null!");
//            return;
//        }
//
//
//        alarmManager.setRepeating(
//                AlarmManager.RTC_WAKEUP,
//                calendar.getTimeInMillis(),
//                AlarmManager.INTERVAL_DAY,  // Repeat every 24 hours
//                pendingIntent
//        );
//        Log.d(TAG, "scheduleDailyStepSave: Scheduled daily step saving.");
//    }
void scheduleDailyStepSave() {
    Log.d("Alarm", "Scheduling Initial Alarm");

    Context context = requireContext().getApplicationContext();
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(context, DailyStepSaveReceiver.class);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
  // First trigger after 2 sec
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 11);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
        calendar.add(Calendar.DAY_OF_YEAR, 1);
    }
    if (alarmManager != null) {
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
        );   Log.d("Alarm", "Alarm scheduled for " + calendar.getTime());
    }
}
}
