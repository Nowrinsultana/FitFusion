package com.example.fitfusion;

public class WorkoutOption {

    private final String name;
    private final int iconResId;

    public WorkoutOption(String name, int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
    }

    public String getName() {
        return name;
    }

    public int getIconResId() {
        return iconResId;
    }
}
