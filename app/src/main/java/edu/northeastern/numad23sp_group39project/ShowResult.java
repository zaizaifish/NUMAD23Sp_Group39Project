package edu.northeastern.numad23sp_group39project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;

public class ShowResult extends AppCompatActivity {
    private ArrayList<String[]> resList;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private ResAdapter resAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        resList = (ArrayList<String[]>) getIntent().getSerializableExtra("search result");
        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        resAdapter = new ResAdapter(resList);
        recyclerView.setAdapter(resAdapter);
        recyclerView.setLayoutManager(layoutManager);
        // do something here to make the result showing page more interactive
        // add back button in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent backToAPIServiceIntent = new Intent(getApplicationContext(), APIService.class);
        startActivity(backToAPIServiceIntent);
        return true;
    }
}