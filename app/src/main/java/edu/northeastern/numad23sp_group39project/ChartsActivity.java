package edu.northeastern.numad23sp_group39project;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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

import androidx.core.content.ContextCompat;
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

public class ChartsActivity extends AppCompatActivity {
    private LineChart lineChart;

    private BarChart barChart;
//    private LineChart lineChart2;
    private PieChart pieChart;

    private HashMap<String,Integer> duartionCount;

    private TreeMap<String,Integer> groupData;

    private TreeMap<String,Integer> timePerDay;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        barChart = findViewById(R.id.Barchart);
        pieChart = findViewById(R.id.pieChart);
        lineChart = findViewById(R.id.lineChart);

        // initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Date current = new Date();
                duartionCount = new HashMap<>();
                groupData = new TreeMap<>();
                timePerDay = new TreeMap<>();
                // fetch date from firebase then update the charts
                List<FitnessData> fitnessDataList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FitnessData fitnessData = dataSnapshot.getValue(FitnessData.class);
                    long diff = current.getTime() - fitnessData.getDate().getTime();
                    // filter data in 7 past days
                    if (diff < (1000 * 60 * 60 * 24 * 7)) {
                        fitnessDataList.add(fitnessData);
                    }
                }
//                updateLineChart(fitnessDataList);
                updatePieChart(fitnessDataList);

                updateBarChart(fitnessDataList);

                updateLineChart(fitnessDataList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });

        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
    }

    //update barchart
    private void updateBarChart(List<FitnessData> fitnessDataList){
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getLegend().setEnabled(false);
        // gain total work out consume of each day
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
        List<String> dateList = new ArrayList<>(groupData.keySet()
                .stream().map(datetime -> datetime.substring(5)).collect(Collectors.toList()));
        List<String> keysList = new ArrayList<>((groupData.keySet()));
        for (int i = 0; i < keysList.size(); i++) {
            String key = keysList.get(i);
            float value = (float) groupData.get(key);
            entries.add(new BarEntry(i, value));
        }

        BarDataSet dataSet = new BarDataSet(entries, "consume");
        dataSet.setColor(Color.rgb(163,200,161));
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);
        barChart.setData(data);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setCenterAxisLabels(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateList));
        barChart.setDrawValueAboveBar(false);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getAxisRight().setEnabled(false);
        barChart.getBarData().setDrawValues(false);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate();

    }

    // update pieChart
    private void updatePieChart(List<FitnessData> fitnessDataList) {
        // count total workout duration for each sport
        for (FitnessData data : fitnessDataList) {
            int d = duartionCount.getOrDefault(data.getName(), 0);
            d += data.getDurationMinutes();
            duartionCount.put(data.getName(), d);
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
            pieDataSet.setDrawValues(false);
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

    //update line chart
    private void updateLineChart(List<FitnessData> fitnessDataList){
        for (FitnessData data : fitnessDataList) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(data.getDate());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String key = String.format("%d-%02d-%02d", year, month, day);
            int value = data.getDurationMinutes();
            if (timePerDay.containsKey(key)) {
                value += timePerDay.get(key);
            }
            timePerDay.put(key, value);
        }
        List<String> dateList = new ArrayList<>(groupData.keySet()
                .stream().map(datetime -> datetime.substring(5)).collect(Collectors.toList()));
        List<String> keysList = new ArrayList<>((groupData.keySet()));
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < keysList.size(); i++) {
            String key = keysList.get(i);
            float value = (float) timePerDay.get(key);
            entries.add(new Entry(i, value));
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "date/total_duration");
        lineDataSet.setLineWidth(2f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setColor(Color.parseColor("#FF4081"));
        lineDataSet.setFillColor(Color.parseColor("#FF4081"));
        lineDataSet.setDrawFilled(true);
        lineDataSet.setValueTextSize(10f);



        int[] gradientColors = new int[]{
                Color.parseColor("#AAFF7F50"), // 渐变的起始颜色
                Color.parseColor("#00FF7F50")  // 渐变的结束颜色
        };
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, gradientColors);

        lineDataSet.setFillDrawable(gradientDrawable);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setEnabled(false);
//        lineChart.getXAxis().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.animateY(1000);
        lineChart.getLegend().setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(entries.get(0).getX() - 0.1f);
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(keysList.size());
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateList));

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setGranularityEnabled(true);
        yAxis.setGranularity(1f);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.invalidate();
    }

}