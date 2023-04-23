package edu.northeastern.numad23sp_group39project;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class WorkoutItem implements Parcelable {
    private String name;
    private int caloriesPerHour;
    private int durationMinutes;
    private int totalCalories;
    private String type;

    public WorkoutItem(String name, int caloriesPerHour, int durationMinutes, int totalCalories, String type) {
        this.name = name;
        this.caloriesPerHour = caloriesPerHour;
        this.durationMinutes = durationMinutes;
        this.totalCalories = totalCalories;
        this.type = type;
    }

    protected WorkoutItem(Parcel in) {
        name = in.readString();
        caloriesPerHour = in.readInt();
        durationMinutes = in.readInt();
        totalCalories = in.readInt();
        type = in.readString();
    }

    public static final Creator<WorkoutItem> CREATOR = new Creator<WorkoutItem>() {
        @Override
        public WorkoutItem createFromParcel(Parcel in) {
            return new WorkoutItem(in);
        }

        @Override
        public WorkoutItem[] newArray(int size) {
            return new WorkoutItem[size];
        }
    };

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

    public String getType() {
        return type;
    }

    public void setUser(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(caloriesPerHour);
        parcel.writeInt(durationMinutes);
        parcel.writeInt(totalCalories);
        parcel.writeString(type);
    }
}




