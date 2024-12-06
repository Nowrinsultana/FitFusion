package com.example.fitfusion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WorkoutFragment extends Fragment {

    private RecyclerView workoutRecyclerView;
    private WorkoutAdapter workoutAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout, container, false);

        // Initialize RecyclerView
        workoutRecyclerView = view.findViewById(R.id.workoutRecyclerView);
        workoutRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Initialize adapter with workout data
        workoutAdapter = new WorkoutAdapter(getWorkoutOptions());
        workoutRecyclerView.setAdapter(workoutAdapter);

        return view;
    }

    private List<WorkoutOption> getWorkoutOptions() {
        List<WorkoutOption> workoutOptions = new ArrayList<>();
        workoutOptions.add(new WorkoutOption("Yoga", R.drawable.yoga));
        workoutOptions.add(new WorkoutOption("Cardio", R.drawable.cardio));
        workoutOptions.add(new WorkoutOption("Strength", R.drawable.strength));
        workoutOptions.add(new WorkoutOption("Pilates", R.drawable.pilates));
        workoutOptions.add(new WorkoutOption("Stretching", R.drawable.stretching));
        workoutOptions.add(new WorkoutOption("HIIT", R.drawable.hiit));

        return workoutOptions;
    }
}
