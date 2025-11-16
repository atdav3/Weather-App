package com.example.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app.R;

import com.example.app.adapters.WeatherEventAdapter;
import com.example.app.models.WeatherEvent;
import com.example.app.models.WeatherData;
import com.example.app.models.ForecastDay;
import com.example.app.network.ApiClient;
import com.example.app.network.WeatherApiService;
import com.example.app.utils.WeatherAnimationUtils;
import com.example.app.utils.WeatherUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherCalendarActivity extends AppCompatActivity {

    // UI Components
    private ImageView btnBack;
    private ImageView btnAddEvent;
    private CalendarView calendarView;
    private TextView tvSelectedDate;
    private TextView tvDateWeather;
    private RecyclerView rvWeatherEvents;
    private TextView tvNoEvents;
    private ImageView ivForecastIcon;
    private TextView tvForecastTemp;
    private TextView tvForecastDesc;
    private TextView tvPrecipitationChance;

    // Services and Adapters
    private WeatherApiService weatherApiService;
    private WeatherEventAdapter eventAdapter;

    // Data
    private Date selectedDate;
    private List<WeatherEvent> weatherEvents;
    private WeatherData currentWeatherData;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault());
    private SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_calendar);

        initializeViews();
        initializeServices();
        setupRecyclerViews();
        setupClickListeners();
        setupCalendarView();

        // Ngày mặc định là hôm nay
        selectedDate = new Date();
        updateSelectedDateUI();
        loadWeatherEventsForDate(selectedDate);
        fetchWeatherForecastForDate(selectedDate);
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        btnAddEvent = findViewById(R.id.btnAddEvent);
        calendarView = findViewById(R.id.calendarView);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvDateWeather = findViewById(R.id.tvDateWeather);
        rvWeatherEvents = findViewById(R.id.rvWeatherEvents);
        tvNoEvents = findViewById(R.id.tvNoEvents);
        ivForecastIcon = findViewById(R.id.ivForecastIcon);
        tvForecastTemp = findViewById(R.id.tvForecastTemp);
        tvForecastDesc = findViewById(R.id.tvForecastDesc);
        tvPrecipitationChance = findViewById(R.id.tvPrecipitationChance);
    }

    private void initializeServices() {
        weatherApiService = ApiClient.getWeatherApiService();
        weatherEvents = new ArrayList<>();
    }

    private void setupRecyclerViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvWeatherEvents.setLayoutManager(layoutManager);
        eventAdapter = new WeatherEventAdapter(this, weatherEvents);
        rvWeatherEvents.setAdapter(eventAdapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> {
            WeatherAnimationUtils.bounceView(btnBack);
            finish();
        });

        btnAddEvent.setOnClickListener(v -> {
            WeatherAnimationUtils.bounceView(btnAddEvent);
            showAddEventDialog();
        });
    }

    private void setupCalendarView() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            selectedDate = calendar.getTime();
            
            updateSelectedDateUI();
            loadWeatherEventsForDate(selectedDate);
            fetchWeatherForecastForDate(selectedDate);
        });
    }

    private void updateSelectedDateUI() {
        Calendar today = Calendar.getInstance();
        Calendar selected = Calendar.getInstance();
        selected.setTime(selectedDate);

        if (selected.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            selected.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            tvSelectedDate.setText(getString(R.string.today));
        } else if (selected.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                   selected.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) + 1) {
            tvSelectedDate.setText(getString(R.string.tomorrow));
        } else {
            tvSelectedDate.setText(dateFormat.format(selectedDate));
        }
    }

    private void loadWeatherEventsForDate(Date date) {
        // TODO: Có thể thay bằng dữ liệu DB nếu cần
        weatherEvents.clear();
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        
        // Add sample events based on the day
        if (dayOfWeek == Calendar.MONDAY) {
            weatherEvents.add(new WeatherEvent("Weekly Weather Review", "Review weather patterns for the week", date, "09:00"));
        } else if (dayOfWeek == Calendar.WEDNESDAY) {
            weatherEvents.add(new WeatherEvent("Mid-week Forecast Check", "Check mid-week weather updates", date, "14:00"));
        } else if (dayOfWeek == Calendar.FRIDAY) {
            weatherEvents.add(new WeatherEvent("Weekend Weather Planning", "Plan activities based on weekend forecast", date, "16:00"));
        }
        
        eventAdapter.notifyDataSetChanged();
        updateEventsVisibility();
    }

    private void updateEventsVisibility() {
        if (weatherEvents.isEmpty()) {
            rvWeatherEvents.setVisibility(View.GONE);
            tvNoEvents.setVisibility(View.VISIBLE);
        } else {
            rvWeatherEvents.setVisibility(View.VISIBLE);
            tvNoEvents.setVisibility(View.GONE);
        }
    }

    private void fetchWeatherForecastForDate(Date date) {
        String dateString = apiDateFormat.format(date);
        
        // Tạm thời dùng địa điểm mặc định; có thể truyền từ MainActivity qua Intent
        String location = "Ho Chi Minh City";
        
        Call<WeatherData> call = weatherApiService.getWeatherForecast(
                ApiClient.API_KEY,
                location,
                7,
                "no",
                "no"
        );

        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(@NonNull Call<WeatherData> call, @NonNull Response<WeatherData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentWeatherData = response.body();
                    updateWeatherForecastUI(dateString);
                } else {
                    showDefaultWeatherInfo();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherData> call, @NonNull Throwable t) {
                showDefaultWeatherInfo();
            }
        });
    }

    private void updateWeatherForecastUI(String dateString) {
        try {
            if (currentWeatherData != null && currentWeatherData.getForecast() != null &&
                currentWeatherData.getForecast().getForecastday() != null) {
                List<ForecastDay> forecastDays = currentWeatherData.getForecast().getForecastday();

                for (ForecastDay day : forecastDays) {
                    if (day != null && dateString.equals(day.getDate()) && day.getDay() != null) {
                        ForecastDay.Day dayData = day.getDay();
                        WeatherData.Condition condition = dayData.getCondition();

                        double maxC = dayData.getMaxtempC();
                        double minC = dayData.getMintempC();
                        double avgC = dayData.getAvgtempC();
                        double chance = dayData.getDailyChanceOfRain();

                        tvForecastTemp.setText(String.format(Locale.getDefault(),
                                "%.0f°C / %.0f°C", maxC, minC));
                        tvForecastDesc.setText(condition != null ? condition.getText() : "");
                        tvPrecipitationChance.setText(String.format(Locale.getDefault(),
                                "%.0f%%", chance));

                        // Set weather icon
                        int iconCode = condition != null ? condition.getCode() : 1003; // partly cloudy default
                        int iconResId = WeatherUtils.getWeatherIcon(iconCode, true);
                        ivForecastIcon.setImageResource(iconResId);

                        // Update date weather summary
                        String summary = String.format(Locale.getDefault(),
                                "%.0f°C, %s", avgC, condition != null ? condition.getText() : "");
                        tvDateWeather.setText(summary);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            // Fallback to default if anything unexpected happens
        }

        showDefaultWeatherInfo();
    }

    private void showDefaultWeatherInfo() {
        tvForecastTemp.setText("25°C / 18°C");
        tvForecastDesc.setText(getString(R.string.partly_cloudy));
        tvPrecipitationChance.setText("20%");
        ivForecastIcon.setImageResource(R.drawable.ic_cloudy);
        tvDateWeather.setText("25°C, " + getString(R.string.partly_cloudy));
    }

    private void showAddEventDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Weather Event")
               .setMessage("This feature will allow you to add custom weather-related events to your calendar.")
               .setPositiveButton("OK", (dialog, which) -> {
                   Toast.makeText(this, "Event feature coming soon!", Toast.LENGTH_SHORT).show();
               })
               .setNegativeButton("Cancel", null)
               .show();
    }
}
