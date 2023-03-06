package edu.northeastern.numad23sp_group39project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Date;

public class StickerHistory extends AppCompatActivity {

    private ArrayList<StickerHistoryList> hisResList;
    private String userName;
    private RecyclerView recyclerView;
    private DatabaseReference personalHistoryDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_history);

        //add database
        personalHistoryDatabase = FirebaseDatabase.getInstance().getReference("messages");

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        userName = sharedPref.getString(getString(R.string.saved_username_key), "");

        //initialize lists
        hisResList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);

        personalHistoryDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<StickerHistoryList> dbList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);

                    // Filter data when finding current user
                    if (message.to.equals(userName)) {
                        String msgTime = new Date(Long.parseLong(message.time)).toString();
                        StickerHistoryList stickerList = new StickerHistoryList(message.sticker, message.from, msgTime);
                        dbList.add(stickerList);
                    }

                }

                // Store filtered data
                hisResList = dbList;

                StickerHistoryAdapter stickerHistoryAdapter = new StickerHistoryAdapter(hisResList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(stickerHistoryAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

        //test lists(if needed)

    }
}