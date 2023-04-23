package edu.northeastern.numad23sp_group39project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WorkoutPlanActivity extends AppCompatActivity implements WorkoutItemAdapter.OnItemClickListener {
    private String API_KEY = "8oIz1o63I1d35JvmZiFSMA==BEKRGnpyWmu9JaGP";
    private String API_URL_CALORIES = "https://api.api-ninjas.com/v1/caloriesburned?activity=";
    private FloatingActionButton fabAddExercise;
    // TODO: change the following into list to parse into ListView
    private int calories;
    private String exercise;
    private ValueEventListener mValueEventListener;
    private DatabaseReference mDatabase;
    private ArrayList<WorkoutItem> cardItems;
    private RecyclerView recyclerView;
    private WorkoutItemAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_plan);

//        cardItems = new ArrayList<>();
        if (savedInstanceState != null) {
            cardItems = savedInstanceState.getParcelableArrayList("cardItems");
        } else {
            cardItems = new ArrayList<>();
        }
        // retrieve user status
//        SharedPreferences mSharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        String userId = mSharedPreferences.getString("userId", null);
//        System.out.println(userId);
//        String userEmail = mSharedPreferences.getString("userEmail", null);
//        String userDisplayName = mSharedPreferences.getString("userDisplayName", null);
//
//        if (userId != null && userEmail != null) {
//            userView.setText("Hello " + userEmail);
//        }

        // TODO: if user have previous workout plan, read from Firebase and load into ListView
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadWorkoutsDate(null, 0);
        fabAddExercise = findViewById(R.id.floatingActionButton3);
        fabAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutPlanActivity.this);
                builder.setTitle("Add Exercise");

                final EditText input = new EditText(WorkoutPlanActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exercise = input.getText().toString();
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    calories = getCalories(exercise);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                        thread.start();
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        Toast.makeText(WorkoutPlanActivity.this, exercise + " add success " + String.valueOf(calories), Toast.LENGTH_SHORT).show();
                        loadWorkoutsDate(exercise, calories);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Display the dialog box to the user
                builder.show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        WorkoutItem clickedItem = cardItems.get(position);
        String workoutName = clickedItem.getName();

        Intent intent = new Intent(this, WorkoutTimerActivity.class);
        intent.putExtra("workout_name", workoutName);
        startActivity(intent);
    }

    private void loadWorkoutsDate(String curExercise, int curCalories) {
        if (curExercise != null && curCalories > 0){
            Log.d("add","add success");
            String name = curExercise;
            int caloriesPerHour = curCalories;
            int durationMinutes = 60;
            int totalCalories = curCalories;
            String type = "Aerobic";

            WorkoutItem cardItem = new WorkoutItem(name, caloriesPerHour, durationMinutes, totalCalories, type);
            cardItems.add(cardItem);

            cardAdapter = new WorkoutItemAdapter(cardItems, this);
            recyclerView.setAdapter(cardAdapter);
        }
        else if (cardItems.size() == 0){ // initialize cardItems
            cardItems = new ArrayList<>();
            InputStream inputStream = getResources().openRawResource(R.raw.workout_data);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int i;
            try {
                i = inputStream.read();
                while (i != -1) {
                    byteArrayOutputStream.write(i);
                    i = inputStream.read();
                }
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String json = byteArrayOutputStream.toString();

            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);

                    String name = jsonObject.getString("name");
                    int caloriesPerHour = jsonObject.getInt("calories_per_hour");
                    int durationMinutes = jsonObject.getInt("recommend_duration");
                    int totalCalories = jsonObject.getInt("total_calories");
                    String type = jsonObject.getString("type");

                    WorkoutItem cardItem = new WorkoutItem(name, caloriesPerHour, durationMinutes, totalCalories, type);
                    cardItems.add(cardItem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cardAdapter = new WorkoutItemAdapter(cardItems, this);
            recyclerView.setAdapter(cardAdapter);
        }
        else{
            cardAdapter = new WorkoutItemAdapter(cardItems, this);
            recyclerView.setAdapter(cardAdapter);
        }
    }

    private int getCalories(String exercise) throws IOException, JSONException {
        String activity = exercise;
        String apiKey = API_KEY;

        // get calories
        String api_url = API_URL_CALORIES + activity;
        URL url = new URL(api_url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("accept", "application/json");
        connection.setRequestProperty("X-Api-Key", apiKey);
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Print the response
        System.out.println(response.toString());
        try {
            JSONArray responseArray = new JSONArray(response.toString());
            if (responseArray.length() > 0) {
                JSONObject responseObject = responseArray.getJSONObject(0);
                int curCalories = responseObject.getInt("total_calories");
                return curCalories;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Random().nextInt(300); // handle empty response
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("cardItems", new ArrayList<>(cardItems));
    }
}