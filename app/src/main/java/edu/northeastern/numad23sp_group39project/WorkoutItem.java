package edu.northeastern.numad23sp_group39project;

public class WorkoutItem {
    private String name;
    private int caloriesPerHour;
    private int durationMinutes;
    private int totalCalories;
    private String user;

    public WorkoutItem(String name, int caloriesPerHour, int durationMinutes, int totalCalories, String user) {
        this.name = name;
        this.caloriesPerHour = caloriesPerHour;
        this.durationMinutes = durationMinutes;
        this.totalCalories = totalCalories;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCaloriesPerHour() {
        return caloriesPerHour;
    }

    public void setCaloriesPerHour(int caloriesPerHour) {
        this.caloriesPerHour = caloriesPerHour;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public int getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}




