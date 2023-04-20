package edu.northeastern.numad23sp_group39project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WorkoutPlanActivity extends AppCompatActivity {
    private String API_KEY = "8oIz1o63I1d35JvmZiFSMA==BEKRGnpyWmu9JaGP";
    private String API_URL_CALORIES = "https://api.api-ninjas.com/v1/caloriesburned?activity=";
    private EditText exerciseName;
    private TextView showResult;
    private Button testBtn;
    private TextView userView;

    // TODO: change the following into list to parse into ListView
    private int calories;
    private ValueEventListener mValueEventListener;
    private DatabaseReference mDatabase;
    private List<WorkoutItem> cardItems;
    private RecyclerView recyclerView;
    private WorkoutItemAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_plan);

        cardItems = new ArrayList<>();

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
        loadWorkoutsDate();

    }

    private void loadWorkoutsDate() {
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
        cardAdapter = new WorkoutItemAdapter(cardItems);
        recyclerView.setAdapter(cardAdapter);
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
        JSONArray responseArray = new JSONArray(response.toString());
        JSONObject responseObject = responseArray.getJSONObject(0);
        int curCalories = responseObject.getInt("total_calories");
        return curCalories;
    }


}