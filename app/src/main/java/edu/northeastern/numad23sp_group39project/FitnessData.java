package edu.northeastern.numad23sp_group39project;

import java.util.Date;

public class FitnessData{

    private Date date;

    private int caloriesPerHour;

    private int durationMinutes;

    private String name;

    private int totalCalories;

    private String user;

    public FitnessData(){}

    public FitnessData(long date, int caloriesPerHour, int durationMinutes, String name, int totalCalories, String user) {
        this.date = new Date(date);
        this.caloriesPerHour = caloriesPerHour;
        this.durationMinutes = durationMinutes;
        this.name = name;
        this.totalCalories = totalCalories;
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = new Date(date);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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