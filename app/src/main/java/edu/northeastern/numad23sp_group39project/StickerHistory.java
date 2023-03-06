package edu.northeastern.numad23sp_group39project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class StickerHistory extends AppCompatActivity {

    private ArrayList<StickerHistoryList> hisResList;

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_history);

        //initialize lists
        hisResList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);

        //test lists
        StickerHistoryList list1 = new StickerHistoryList("111str111", "111str222", "111str333");
        StickerHistoryList list2 = new StickerHistoryList("222str111", "222str222", "222str333");
        StickerHistoryList list3 = new StickerHistoryList("333str111", "333str222", "333str333");
        StickerHistoryList list4 = new StickerHistoryList("444str111", "444str222", "444str333");
        StickerHistoryList list5 = new StickerHistoryList("555str111", "555str222", "555str333");
        hisResList.add(list1);
        hisResList.add(list2);
        hisResList.add(list3);
        hisResList.add(list4);
        hisResList.add(list5);

        StickerHistoryAdapter stickerHistoryAdapter = new StickerHistoryAdapter(hisResList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(stickerHistoryAdapter);


    }
}