package edu.northeastern.numad23sp_group39project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class APIService extends AppCompatActivity {
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
                // TODO: launch http call here and parse result
                /*
                Refer to https://www.omdbapi.com/. Get Movie info by querying a movie title
                sample request: (GET METHOD)
                Request: http://www.omdbapi.com/?t=avengers
                Response:
                {"Title":"The Avengers","Year":"2012","Rated":"PG-13","Released":"04 May 2012",
                "Runtime":"143 min","Genre":"Action, Sci-Fi","Director":"Joss Whedon","Writer":"Joss Whedon,
                Zak Penn","Actors":"Robert Downey Jr., Chris Evans, Scarlett Johansson",
                "Plot":"Earth's mightiest heroes must come together and learn to fight as a team
                if they are going to stop the mischievous Loki and his alien army from enslaving humanity.",
                "Language":"English, Russian, Hindi","Country":"United States","Awards":"Nominated for 1 Oscar.
                38 wins & 80 nominations total","Poster":"https://m.media-amazon.com/images/M/MV5BNDYxNjQyMjAtNTdiOS00NGYwLW
                FmNTAtNThmYjU5ZGI2YTI1XkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg","Ratings":[{"Source":"Internet Movie Database",
                "Value":"8.0/10"},{"Source":"Rotten Tomatoes","Value":"91%"},{"Source":"Metacritic","Value":"69/100"}],"Metascore":
                "69","imdbRating":"8.0","imdbVotes":"1,397,515","imdbID":"tt0848228","Type":"movie","DVD":"25 Sep 2012","BoxOffice":"$623,357,910",
                "Production":"N/A","Website":"N/A","Response":"True"}
                 */

                // TODO: present result
                /*
                1. consider using RecyclerView
                2. present movie title + imdbRating in a single line
                3. Consider displaying icons instead of text for categorical variables in the response
                such as Good for rating > 85, Normal for rating > 60, Bad for rating < 60
                 */
            }
        });

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
            View movieInputLayout = scrollView.findViewById(id);
            movieTitleEditText = movieInputLayout.findViewById(R.id.movieTitleEditText);
            res.add(String.valueOf(movieTitleEditText.getText()));
        }
        return res;
    }


}