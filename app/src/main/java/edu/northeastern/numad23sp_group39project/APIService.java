package edu.northeastern.numad23sp_group39project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class APIService extends AppCompatActivity {
    private static final String TAG = "APIService";
    private LinearLayout moviesLayout;
    private Button addMovieButton;
    private Button SubmitButton;
    private ArrayList<Integer> idList = new ArrayList<>();
    private static ArrayList<EditText> textList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apiservice);
        // Get references to the UI elements
        moviesLayout = findViewById(R.id.moviesLayout);
        addMovieButton = findViewById(R.id.addMovieButton);
        if (textList.size() > 0){ // to avoid rotate update
            ArrayList<String> temp = new ArrayList<>();
            for (EditText oldtext: textList){
                temp.add(String.valueOf(oldtext.getText()));
            }
            textList = new ArrayList<>();
            for (String oldtext: temp){
                addMovieInput(oldtext);
            }
        }
        addMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMovieInput(null);
            }
        });
        SubmitButton = findViewById(R.id.SubmitButton);
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> movieList = getMovieList();
                for (String str: movieList){
                    Log.d("movie",str);
                }
                // launch http call here and parse result
                for (String title : movieList) {
                    PingWebServiceTask task = new PingWebServiceTask();
                    task.execute(title);
                }
            }
        });

    }

    private class PingWebServiceTask extends AsyncTask<String, Integer, String[]> {

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i(TAG, "Making progress...");
        }

        @Override
        protected String[] doInBackground(String... params) {
            String[] results = new String[2];
            URL url = null;
            try {
                url = new URL(getResources().getString(R.string.imdb_api) + params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();

                InputStream inputStream = conn.getInputStream();
                final String resp = convertStreamToString(inputStream);

                // return [Title, imdbRating]
                JSONObject jObject = new JSONObject(resp);
                String jTitle = jObject.getString("Title");
                String jRating = jObject.getString("imdbRating");
                results[0] = jTitle;
                results[1] = jRating;
                return results;
            } catch (MalformedURLException e) {
                Log.e(TAG,"MalformedURLException");
                e.printStackTrace();
            } catch (ProtocolException e) {
                Log.e(TAG,"ProtocolException");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(TAG,"IOException");
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e(TAG,"JSONException");
                e.printStackTrace();
            }
            results[0] = "Something went wrong";
            return(results);
        }

        @Override
        protected void onPostExecute(String... s) {
            super.onPostExecute(s);
            // TODO: present result
            /*
                1. consider using RecyclerView
                2. present movie title + imdbRating in a single line
                3. Consider displaying icons instead of text for categorical variables in the response
                such as Good for rating > 85, Normal for rating > 60, Bad for rating < 60
             */
            /*
                s[0]: Title
                s[1]: imdbRating
             */
            Log.e(TAG, s[0]);
            Log.e(TAG, s[1]);
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

    private void addMovieInput(String inputStr) {
        // Inflate a new movie input field layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View movieInputLayout = inflater.inflate(R.layout.movie_input_layout, null);

        // Set the input field's ID to a unique value based on the number of fields in the layout
        int numFields = moviesLayout.getChildCount();
        EditText movieTitleEditText = movieInputLayout.findViewById(R.id.movieTitleEditText);
        if (inputStr != null) movieTitleEditText.setText(inputStr);
        int id = R.id.movieEditText + numFields;
        movieInputLayout.setId(id);
        idList.add(id);
        // Add the new movie input field layout to the LinearLayout
        moviesLayout.addView(movieInputLayout);
        textList.add(movieTitleEditText);
    }

    private ArrayList<String> getMovieList(){
        ArrayList<String> res = new ArrayList<String>();
        ScrollView scrollView = findViewById(R.id.scrollView2);
        EditText movieTitleEditText = findViewById(R.id.movieEditText);
        res.add(String.valueOf(movieTitleEditText.getText()));
        for (Integer id : idList) {
            ViewGroup movieInputLayout = scrollView.findViewById(id);
            movieTitleEditText = (EditText)movieInputLayout.getChildAt(0);
            res.add(String.valueOf(movieTitleEditText.getText()));
        }
        return res;
    }


}