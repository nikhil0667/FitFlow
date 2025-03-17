package com.example.fitflow;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class StepCounterService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private SharedPreferences sharedPreferences;
    private int totalSteps = 0;
    private int previousTotalSteps = 0;
    private static final String TAG = "StepCounterService";

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "StepCounterService Created", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Service Created");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // ✅ Fix: Add this flag
            startActivity(intent);
        }


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sharedPreferences = getSharedPreferences("StepPref", MODE_PRIVATE);
        previousTotalSteps = sharedPreferences.getInt("previousSteps", 0);

        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
            Toast.makeText(this, "Step Sensor Registered", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No Step Sensor Found!", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Step sensor not available!");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "StepCounterService Started", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Service Started");
        startForeground(1, createNotification()); // Keep service alive in the background
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            totalSteps = (int) event.values[0];
            int stepsSinceLastReset = totalSteps - previousTotalSteps;

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("currentSteps", stepsSinceLastReset);
            editor.apply();

            Toast.makeText(this, "Steps Updated: " + stepsSinceLastReset, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Steps Updated: " + stepsSinceLastReset);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Toast.makeText(this, "Sensor Accuracy Changed: " + accuracy, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Sensor Accuracy Changed: " + accuracy);
    }

    private Notification createNotification() {
        String channelId = "stepChannel";
        String channelName = "Step Tracker";

        NotificationManager manager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        return new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Step Counter Running")
                .setContentText("Tracking your steps in the background.")
                .setSmallIcon(R.drawable.logo)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "StepCounterService Destroyed", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Service Destroyed");

        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
