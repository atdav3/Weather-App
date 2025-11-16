package com.example.app;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class WeatherChartsActivity extends AppCompatActivity {
    private TextView tvChartTitle;
    private TextView tvAverage;
    private TextView tvHighest;
    private TextView tvLowest;
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_charts);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Bind views
        tvChartTitle = findViewById(R.id.tvChartTitle);
        tvAverage = findViewById(R.id.tvAverage);
        tvHighest = findViewById(R.id.tvHighest);
        tvLowest = findViewById(R.id.tvLowest);
        lineChart = findViewById(R.id.lineChart);
        
        // Setup chart
        setupChart();

        Button btnTemperatureChart = findViewById(R.id.btnTemperatureChart);
        Button btnHumidityChart = findViewById(R.id.btnHumidityChart);
        Button btnWindChart = findViewById(R.id.btnWindChart);
        Button btnPressureChart = findViewById(R.id.btnPressureChart);

        btnTemperatureChart.setOnClickListener(v -> {
            tvChartTitle.setText(getString(R.string.temperature_chart));
            applySampleStats("25°C", "32°C", "18°C");
            showTemperatureChart();
        });

        btnHumidityChart.setOnClickListener(v -> {
            tvChartTitle.setText(getString(R.string.humidity));
            applySampleStats("68%", "92%", "40%");
            showHumidityChart();
        });

        btnWindChart.setOnClickListener(v -> {
            tvChartTitle.setText(getString(R.string.wind_speed));
            applySampleStats("12 km/h", "25 km/h", "3 km/h");
            showWindChart();
        });

        btnPressureChart.setOnClickListener(v -> {
            tvChartTitle.setText(getString(R.string.pressure));
            applySampleStats("1012 hPa", "1023 hPa", "1001 hPa");
            showPressureChart();
        });
        
        // Show temperature chart by default
        showTemperatureChart();
    }

    private void applySampleStats(String avg, String hi, String lo) {
        if (tvAverage != null) tvAverage.setText(avg);
        if (tvHighest != null) tvHighest.setText(hi);
        if (tvLowest != null) tvLowest.setText(lo);
    }
    
    private void setupChart() {
        // Configure chart appearance
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setDrawGridBackground(false);
        lineChart.setDrawBorders(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        
        // Configure X axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12f);
        
        // Configure Y axis
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setTextSize(12f);
        
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
        
        // Configure legend
        lineChart.getLegend().setEnabled(false);
        
        // Configure description
        lineChart.getDescription().setEnabled(false);
    }
    
    private void showTemperatureChart() {
        List<Entry> entries = new ArrayList<>();
        String[] labels = {"00:00", "04:00", "08:00", "12:00", "16:00", "20:00", "24:00"};
        float[] temperatures = {18f, 16f, 20f, 28f, 32f, 26f, 22f};
        
        for (int i = 0; i < temperatures.length; i++) {
            entries.add(new Entry(i, temperatures[i]));
        }
        
        LineDataSet dataSet = new LineDataSet(entries, "Nhiệt độ");
        dataSet.setColor(Color.parseColor("#FF6B35"));
        dataSet.setLineWidth(3f);
        dataSet.setCircleColor(Color.parseColor("#FF6B35"));
        dataSet.setCircleRadius(6f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);
        
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        
        // Set X axis labels
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        lineChart.getXAxis().setLabelCount(labels.length);
        
        lineChart.invalidate();
    }
    
    private void showHumidityChart() {
        List<Entry> entries = new ArrayList<>();
        String[] labels = {"00:00", "04:00", "08:00", "12:00", "16:00", "20:00", "24:00"};
        float[] humidity = {85f, 90f, 75f, 60f, 55f, 70f, 80f};
        
        for (int i = 0; i < humidity.length; i++) {
            entries.add(new Entry(i, humidity[i]));
        }
        
        LineDataSet dataSet = new LineDataSet(entries, "Độ ẩm");
        dataSet.setColor(Color.parseColor("#4ECDC4"));
        dataSet.setLineWidth(3f);
        dataSet.setCircleColor(Color.parseColor("#4ECDC4"));
        dataSet.setCircleRadius(6f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);
        
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        lineChart.getXAxis().setLabelCount(labels.length);
        
        lineChart.invalidate();
    }
    
    private void showWindChart() {
        List<Entry> entries = new ArrayList<>();
        String[] labels = {"00:00", "04:00", "08:00", "12:00", "16:00", "20:00", "24:00"};
        float[] windSpeed = {5f, 8f, 12f, 18f, 25f, 15f, 10f};
        
        for (int i = 0; i < windSpeed.length; i++) {
            entries.add(new Entry(i, windSpeed[i]));
        }
        
        LineDataSet dataSet = new LineDataSet(entries, "Tốc độ gió");
        dataSet.setColor(Color.parseColor("#45B7D1"));
        dataSet.setLineWidth(3f);
        dataSet.setCircleColor(Color.parseColor("#45B7D1"));
        dataSet.setCircleRadius(6f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);
        
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        lineChart.getXAxis().setLabelCount(labels.length);
        
        lineChart.invalidate();
    }
    
    private void showPressureChart() {
        List<Entry> entries = new ArrayList<>();
        String[] labels = {"00:00", "04:00", "08:00", "12:00", "16:00", "20:00", "24:00"};
        float[] pressure = {1010f, 1012f, 1015f, 1018f, 1020f, 1017f, 1013f};
        
        for (int i = 0; i < pressure.length; i++) {
            entries.add(new Entry(i, pressure[i]));
        }
        
        LineDataSet dataSet = new LineDataSet(entries, "Áp suất");
        dataSet.setColor(Color.parseColor("#96CEB4"));
        dataSet.setLineWidth(3f);
        dataSet.setCircleColor(Color.parseColor("#96CEB4"));
        dataSet.setCircleRadius(6f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);
        
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        lineChart.getXAxis().setLabelCount(labels.length);
        
        lineChart.invalidate();
    }
}


