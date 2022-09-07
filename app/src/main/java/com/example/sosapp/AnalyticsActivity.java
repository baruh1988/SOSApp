package com.example.sosapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AnalyticsActivity extends AppCompatActivity {

    int lastMonthCount = 0;
    int currentCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        HashMap<String, String> monthData = new HashMap<>();

        ValueLineChart mCubicValueLineChart = (ValueLineChart) findViewById(R.id.cubiclinechart);

        ValueLineSeries series = new ValueLineSeries();
        series.setColor(0xFF56B7F1);

        DBHelper db = new DBHelper(this);
        List<LocalDateTime> data = db.getCallDates();
        if (data.size() != 0) {
            series.addPoint(new ValueLinePoint(Month.of((data.get(0).getMonthValue()) % 12 - 1).getDisplayName(TextStyle.SHORT, Locale.ENGLISH), 0));
            monthData.put(Month.of((data.get(0).getMonthValue()) % 12 - 1).getDisplayName(TextStyle.SHORT, Locale.ENGLISH), "" + 0);
            int c = 1;
            for (int i = 1; i < data.size(); i++) {
                if (data.get(i).getMonthValue() == data.get(i - 1).getMonthValue()) {
                    c++;
                } else {
                    series.addPoint(new ValueLinePoint(Month.of(data.get(i - 1).getMonthValue()).getDisplayName(TextStyle.SHORT, Locale.ENGLISH), c));
                    monthData.put(Month.of(data.get(i - 1).getMonthValue()).getDisplayName(TextStyle.SHORT, Locale.ENGLISH), "" + c);
                    c = 1;
                }
            }
            series.addPoint(new ValueLinePoint(Month.of(data.get(data.size() - 1).getMonthValue()).getDisplayName(TextStyle.SHORT, Locale.ENGLISH), c));
            monthData.put(Month.of(data.get(data.size() - 1).getMonthValue()).getDisplayName(TextStyle.SHORT, Locale.ENGLISH), "" + c);
            series.addPoint(new ValueLinePoint(Month.of((data.get(data.size() - 1).getMonthValue()) % 12 + 1).getDisplayName(TextStyle.SHORT, Locale.ENGLISH), 0));
            monthData.put(Month.of(data.get(data.size() - 1).getMonthValue() % 12 + 1).getDisplayName(TextStyle.SHORT, Locale.ENGLISH), "" + 0);

            mCubicValueLineChart.addSeries(series);
            mCubicValueLineChart.startAnimation();
        }

        if (Settings.getSettings().isNotifyEnabled()) {
            int i = LocalDateTime.now().getMonthValue();
            currentCount = Integer.valueOf(monthData.get(Month.of(i).getDisplayName(TextStyle.SHORT, Locale.ENGLISH)));
            lastMonthCount = Integer.valueOf(monthData.get(Month.of((i % 12) - 1).getDisplayName(TextStyle.SHORT, Locale.ENGLISH)));
            if (currentCount - lastMonthCount >= Settings.getSettings().getAmount()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert!").setMessage(String.format("There has been an increase of SOS calls compared to last month by %d!\n You should notify the appropriate authorities!", currentCount - lastMonthCount));
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }
}