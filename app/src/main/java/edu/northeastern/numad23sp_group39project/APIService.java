package edu.northeastern.numad23sp_group39project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class APIService extends AppCompatActivity {
    private static final String TAG = "APIService";
    private LinearLayout moviesLayout;
    private Button addMovieButton;
    private Button SubmitButton;
    private ArrayList<Integer> idList = new ArrayList<>();
    private static ArrayList<EditText> textList = new ArrayList<>();
    private ArrayList<String[]> searchRes = new ArrayList<>();
    private final String lock = "LOCK";
    ProgressBar progressBar;

    private static Integer totalTask = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = findViewById(R.id.progress_bar);
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
                // setTotal task number
                totalTask = movieList.size();
                // launch http call here and parse result
                for (String title : movieList) {
                    searchMovieViaHTTP(title);
                }
                new Thread(new ResultCollector()).start();
            }
        });
        // add back button in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent backToMainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(backToMainIntent);
        return true;
    }

    public void searchMovieViaHTTP(String title) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String[] result = runHTTPCall(title);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (lock){
                            //store fetched result
                            searchRes.add(new String[]{result[0],result[1]});
                            lock.notifyAll();
                        }
                    }
                });
            }
        });
    }

    public String[] runHTTPCall(String title) {
        String[] results = new String[2];
        URL url = null;
        try {
            // update UI
            progressBar = findViewById(R.id.progress_bar);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
            url = new URL(getResources().getString(R.string.imdb_api) + title);
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
        results[0] = "Movie Not Found";
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                progressBar.setVisibility(View.GONE);
//            }
//        });
        return(results);
    }

//    collect results from all PingWebServiceTask and present them in a new activity
    private class ResultCollector implements Runnable{
        @Override
        public void run() {
            synchronized (lock) {
//                wait for task finish
                while (searchRes.size()<totalTask){
                    try {
                        lock.wait();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
//                all task finished
                Log.e(TAG,searchRes.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
//                show data in another activity using RecyclerView
                Intent intent = new Intent(APIService.this, ShowResult.class);
                intent.putExtra("search result", searchRes);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        moviesLayout.removeViews(1, moviesLayout.getChildCount() - 1);
        idList.clear();
        textList.clear();
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