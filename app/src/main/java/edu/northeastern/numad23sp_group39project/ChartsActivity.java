package edu.northeastern.numad23sp_group39project;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import edu.northeastern.numad23sp_group39project.databinding.ActivityChartsBinding;

public class ChartsActivity extends AppCompatActivity {
    private LineChart lineChart1;

    private BarChart barChart;
//    private LineChart lineChart2;
    private PieChart pieChart;

    private HashMap<String,Integer> duartionCount;

    private TreeMap<String,Integer> groupData;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        duartionCount = new HashMap<>();
        groupData = new TreeMap<>();
        // initialize LineChart and PieChart
        barChart = findViewById(R.id.Barchart);
//        lineChart2 = findViewById(R.id.lineChart2);
        pieChart = findViewById(R.id.pieChart);

        // initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 数据库中的数据发生变化，更新图表
                List<FitnessData> fitnessDataList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FitnessData fitnessData = dataSnapshot.getValue(FitnessData.class);
//                    Log.d("Charts",fitnessData.getName()+" "+fitnessData.getCaloriesPerHour());
//                    System.out.println(fitnessData.getName());
                    fitnessDataList.add(fitnessData);
                }
//                updateLineChart(fitnessDataList);
                updatePieChart(fitnessDataList);

                updateBarChart(fitnessDataList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });

        //bar chart



            // 填充 LineChart 数据
//        lineChart2.getLegend().setEnabled(false);
//        lineChart2.getDescription().setEnabled(false);
//        List<Entry> entries = new ArrayList<>();
//        entries.add(new Entry(327, 1));
//        entries.add(new Entry(312, 2));
//        entries.add(new Entry(181, 3));
//        entries.add(new Entry(181, 4));
//        entries.add(new Entry(399, 5));
//        LineDataSet dataSet = new LineDataSet(entries,"");
//        LineData lineData = new LineData(dataSet);
////        lineChart1.setData(lineData);
////        lineChart1.invalidate(); // 刷新图表
//        lineChart2.setData(lineData);
//        lineChart2.invalidate();


//        pieChart.setExtraOffsets(5, 5, 5, 5);
//        pieChart.getLayoutParams().width = (int) (getResources().getDisplayMetrics().widthPixels );
//        pieChart.getLayoutParams().height = (int) (getResources().getDisplayMetrics().widthPixels );



        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);
        //pieChart.setHoleColor(Color.TRANSPARENT);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);


    }

    private void updateBarChart(List<FitnessData> fitnessDataList){
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
//        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dateList));
//        barChart.getAxisLeft().setTextColor(Color.BLACK);
//        barChart.getAxisRight().setTextColor(Color.BLACK);
        barChart.getLegend().setEnabled(false);
        for (FitnessData data : fitnessDataList) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(data.getDate());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String key = String.format("%d-%02d-%02d", year, month, day);
            int value = data.getTotalCalories();
            if (groupData.containsKey(key)) {
                value += groupData.get(key);
            }
            groupData.put(key, value);
        }

        List<BarEntry> entries = new ArrayList<>();
        List<String> keysList = new ArrayList<>(groupData.keySet());
        for (int i = 0; i < keysList.size(); i++) {
            String key = keysList.get(i);
            float value = (float) groupData.get(key);
            entries.add(new BarEntry(i, value));
        }
        for(BarEntry e : entries){
            Log.d("Charts",e.toString());
        }
        BarDataSet dataSet = new BarDataSet(entries, "消耗");
        dataSet.setColor(Color.rgb(163,200,161));
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);
        barChart.setData(data);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setCenterAxisLabels(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(keysList));
        barChart.setDrawValueAboveBar(false);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getAxisRight().setEnabled(false);
        barChart.getBarData().setDrawValues(false);
        barChart.getDescription().setEnabled(false);
        barChart.invalidate();

    }

    // update pieChart
    private void updatePieChart(List<FitnessData> fitnessDataList) {
        // count total workout duration for each sport
        Date current = new Date();
        for (FitnessData data : fitnessDataList) {
            long diff = current.getTime() - data.getDate().getTime();
            // filter data in 7 past days
            if (diff < (1000 * 60 * 60 * 24 * 7)) {
                int d = duartionCount.getOrDefault(data.getName(), 0);
                d += data.getDurationMinutes();
                duartionCount.put(data.getName(), d);
            }
        }
        //then fill the pie chat with data
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(true);
        pieChart.setUsePercentValues(true);
        pieChart.offsetLeftAndRight(5);
        // 设置标签的字体类型
        pieChart.setEntryLabelTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));
        pieChart.animateY(1000, Easing.EaseInOutCubic);
        int total = 0;
        for (int d : duartionCount.values()) {
            total += d;
        }
        List<PieEntry> pieEntries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : duartionCount.entrySet()) {
            float percent = 100f * entry.getValue() / total;
            String label = entry.getKey() + "\n" + String.format("%.1f%%", percent);
            pieEntries.add(new PieEntry(entry.getValue(), label));
            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.setEntryLabelTextSize(12f);
            pieChart.setDrawEntryLabels(true);
            pieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD);
            pieChart.setEntryLabelColor(Color.WHITE);
            PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
            pieDataSet.setDrawIcons(false);
//            pieDataSet.setSliceSpace(3f);
            pieDataSet.setDrawValues(false);
//            pieDataSet.setIconsOffset(new MPPointF(0, 40));
//            pieDataSet.setSelectionShift(5f);
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            PieData pieData = new PieData(pieDataSet);
            pieData.setValueFormatter(new PercentFormatter());
            pieData.setValueTextSize(11f);
            pieData.setValueTextColor(Color.WHITE);
            pieData.setValueTypeface(Typeface.DEFAULT);
            pieChart.setData(pieData);
            pieChart.invalidate();
        }
    }


}