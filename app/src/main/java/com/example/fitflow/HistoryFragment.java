package com.example.fitflow;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.database.Cursor;
import android.graphics.Color;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private WorkoutHistoryAdapter adapter;
    private List<WorkoutHistory> workoutHistoryList;

    private BarChart barChart;
   private MaterialButtonToggleGroup toggleGroupGraphList, toggleGroupTimeFilter, toggleGroupMetrics;
    private FitnessDatabaseHelper databaseHelper;
private TextView noDataTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        databaseHelper = new FitnessDatabaseHelper(requireContext());
        barChart = view.findViewById(R.id.barChart);
        barChart = view.findViewById(R.id.barChart);
        noDataTextView = view.findViewById(R.id.noDataTextView); // Add this in XML

        toggleGroupGraphList = view.findViewById(R.id.toggleGroupGraphList);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        barChart.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        noDataTextView.setVisibility(View.VISIBLE);

        toggleGroupGraphList.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            List<Integer> checkedIds = toggleGroupGraphList.getCheckedButtonIds();
            boolean isGraphChecked = checkedIds.contains(R.id.btnGraph);
            boolean isListChecked = checkedIds.contains(R.id.btnList);

            if (isGraphChecked && isListChecked) {
                // Show both
                barChart.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                noDataTextView.setVisibility(View.GONE);
                loadGraphData();
                loadListData();
            } else if (isGraphChecked) {
                // Show only Graph
                barChart.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                noDataTextView.setVisibility(View.GONE);
                loadGraphData();
            } else if (isListChecked) {
                // Show only List
                barChart.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                noDataTextView.setVisibility(View.GONE);
                loadListData();
            } else {
                // Hide both if neither is selected
                barChart.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                noDataTextView.setVisibility(View.VISIBLE);



            }
        });
        return view;
    }

    private void loadGraphData() {

        FitnessDatabaseHelper dbHelper = new FitnessDatabaseHelper(getContext());
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM fitness_data ORDER BY time DESC", null);
        ArrayList<BarEntry> entries = new ArrayList<>();
        int index = 0;

        while (cursor.moveToNext()) {
            int steps = cursor.getInt(1);
            entries.add(new BarEntry(index++, steps));
        }
        cursor.close();

        BarDataSet dataSet = new BarDataSet(entries, "Steps");
        dataSet.setColor(Color.parseColor("#008B8B"));
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate();
    }


    private void loadListData()  {
        workoutHistoryList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM fitness_data ORDER BY time DESC", null);

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String date = cursor.getString(4);
            int steps = cursor.getInt(1);
            double distance  = cursor.getDouble(2);

            DecimalFormat df = new DecimalFormat("0.00");
            Log.d("DatabaseCheck", "ID: " + id + ", Date: " + date + ", Steps: " + steps + ", Distance: " + distance);

            int calories = cursor.getInt(3);
            // Convert string to Date
            String formattedDate = "";

            try {
                formattedDate = new SimpleDateFormat("MMM dd").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            workoutHistoryList.add(new WorkoutHistory(formattedDate, steps, Double.parseDouble( df.format(distance)), calories));
        }
        cursor.close();

        adapter = new WorkoutHistoryAdapter(workoutHistoryList);

        recyclerView.setAdapter(adapter);
    }

}
