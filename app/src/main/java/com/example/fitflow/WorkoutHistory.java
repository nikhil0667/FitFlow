package com.example.fitflow;

public class WorkoutHistory {
    private String date;
    private int minutes;
    private int steps;
    private double distance;
    private int calories;

    public WorkoutHistory(String date,  int steps, double distance, int calories) {
        this.date = date;
        this.steps = steps;
        this.distance = distance;
        this.calories = calories;
    }

    public String getDate() { return date; }

    public int getSteps() { return steps; }
    public double getDistance() { return distance; }
    public int getCalories() { return calories; }
}
