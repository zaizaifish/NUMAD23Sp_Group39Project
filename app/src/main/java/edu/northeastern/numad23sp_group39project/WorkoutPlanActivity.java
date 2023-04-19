package edu.northeastern.numad23sp_group39project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class WorkoutPlanActivity extends AppCompatActivity {
    private String API_KEY = "8oIz1o63I1d35JvmZiFSMA==BEKRGnpyWmu9JaGP";
    private String API_URL_CALORIES = "https://api.api-ninjas.com/v1/caloriesburned?activity=";
    private EditText exerciseName;
    private TextView showResult;
    private Button testBtn;
    private TextView userView;

    // TODO: change the following into list to parse into ListView
    private int calories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_plan);

        exerciseName = findViewById(R.id.exerciseName);
        showResult = findViewById(R.id.showResult);
        testBtn = findViewById(R.id.button);
        userView = findViewById(R.id.userView);

        // retrieve user status
        SharedPreferences mSharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = mSharedPreferences.getString("userId", null);
        String userEmail = mSharedPreferences.getString("userEmail", null);
        String userDisplayName = mSharedPreferences.getString("userDisplayName", null);

        if (userId != null && userEmail != null) {
            userView.setText("Hello " + userEmail);
        }

        // TODO: if user have previous workout plan, read from Firebase and load into ListView

        // test API call to query exercise info
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            calories = getCalories(exerciseName.getText().toString());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Update UI
                                showResult.setText("calories: "+ calories);
                            }
                        });

                    }
                });
                thread.start();



            }
        });
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