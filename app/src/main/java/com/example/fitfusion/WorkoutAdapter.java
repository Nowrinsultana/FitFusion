package com.example.fitfusion;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private final List<WorkoutOption> workoutOptions;

    public WorkoutAdapter(List<WorkoutOption> workoutOptions) {
        this.workoutOptions = workoutOptions;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout_card, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        WorkoutOption workoutOption = workoutOptions.get(position);
        holder.workoutName.setText(workoutOption.getName());
        holder.workoutIcon.setImageResource(workoutOption.getIconResId());

//        holder.itemView.setOnClickListener(v -> {
//                Context context = holder.itemView.getContext();
//                Intent intent = new Intent(context, WorkoutDetailActivity.class);
//                intent.putExtra("workoutName", workoutOption.getName());
//                context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return workoutOptions.size();
    }

    static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        ImageView workoutIcon;
        TextView workoutName;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutIcon = itemView.findViewById(R.id.workoutIcon);
            workoutName = itemView.findViewById(R.id.workoutName);
        }
    }
}
