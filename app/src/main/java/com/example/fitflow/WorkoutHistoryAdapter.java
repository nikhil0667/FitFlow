package com.example.fitflow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WorkoutHistoryAdapter extends RecyclerView.Adapter<WorkoutHistoryAdapter.ViewHolder> {

    private List<WorkoutHistory> workoutHistoryList;

    public WorkoutHistoryAdapter(List<WorkoutHistory> workoutHistoryList) {
        this.workoutHistoryList = workoutHistoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WorkoutHistory history = workoutHistoryList.get(position);
        holder.dateText.setText(history.getDate());
        holder.stepsText.setText("" + history.getSteps());
        holder.distanceText.setText("" + history.getDistance() + " km");
        holder.caloriesText.setText("" + history.getCalories() + "kcal");
    }

    @Override
    public int getItemCount() {
        return workoutHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateText, minutesText, stepsText, distanceText, caloriesText;

        public ViewHolder(View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.dateText);
            stepsText = itemView.findViewById(R.id.stepsText);
            distanceText = itemView.findViewById(R.id.distanceText);
            caloriesText = itemView.findViewById(R.id.caloriesText);
        }
    }
}
