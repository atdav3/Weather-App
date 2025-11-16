package com.example.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.view.inputmethod.EditorInfo;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.adapters.DailyForecastAdapter;
import com.example.app.adapters.HourlyForecastAdapter;
import com.example.app.adapters.LocationSearchAdapter;
import com.example.app.models.ForecastDay;
import com.example.app.models.WeatherData;
import com.example.app.models.LocationSearchResult;
import com.example.app.network.ApiClient;
import com.example.app.network.WeatherApiService;
import com.example.app.services.LocationService;
import com.example.app.services.HistoryService;
import com.example.app.utils.WeatherUtils;
import com.example.app.utils.WeatherAnimationUtils;
import com.example.app.utils.NetworkUtils;
import com.example.app.utils.UnitManager;
import com.example.app.utils.AuthManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationService.LocationListener {

    // UI Components
    private AutoCompleteTextView etLocationSearch;
    private TextView tvLocation;
    private ImageView ivWeatherIcon;
    private TextView tvTemperature;
    private TextView tvWeatherDescription;
    private TextView tvHumidity;
    private TextView tvStatAvgTemp;
    private TextView tvStatMaxTemp;
    private TextView tvStatMinTemp;
    private TextView tvStatAvgHumidity;
    private RecyclerView rvHourlyForecast;
    private RecyclerView rvDailyForecast;
    private Button btnRefresh;
    private Button btnSearch;
    private Button btnWeatherMap;
    private Button btnWeatherCalendar;
    private Button btnWeatherCharts;
    private ImageButton btnMenuPopup;
    private FrameLayout loadingOverlay;
    private PieChart pieChartStats;

    // Services and Adapters
    private LocationService locationService;
    private WeatherApiService weatherApiService;
    private HistoryService historyService;
    private HourlyForecastAdapter hourlyAdapter;
    private DailyForecastAdapter dailyAdapter;
    private LocationSearchAdapter searchAdapter;
    private UnitManager unitManager;
    private AuthManager authManager;

    // Data
    private String currentLocationQuery = "Ho Chi Minh City"; // Default location (Vietnam)
    private WeatherData currentWeatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        initializeServices();
        ensureLoggedIn();
        setupRecyclerViews();
        setupClickListeners();

        // Start getting location and weather data
        getLocationAndWeather();
    }

    private void initializeViews() {
        etLocationSearch = findViewById(R.id.etLocationSearch);
        tvLocation = findViewById(R.id.tvLocation);
        ivWeatherIcon = findViewById(R.id.ivWeatherIcon);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvWeatherDescription = findViewById(R.id.tvWeatherDescription);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvStatAvgTemp = findViewById(R.id.tvStatAvgTemp);
        tvStatMaxTemp = findViewById(R.id.tvStatMaxTemp);
        tvStatMinTemp = findViewById(R.id.tvStatMinTemp);
        tvStatAvgHumidity = findViewById(R.id.tvStatAvgHumidity);
        rvHourlyForecast = findViewById(R.id.rvHourlyForecast);
        rvDailyForecast = findViewById(R.id.rvDailyForecast);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnSearch = findViewById(R.id.btnSearch);
        btnWeatherMap = findViewById(R.id.btnWeatherMap);
        btnWeatherCalendar = findViewById(R.id.btnWeatherCalendar);
        btnWeatherCharts = findViewById(R.id.btnWeatherCharts);
        btnMenuPopup = findViewById(R.id.btnMenuPopup);
        loadingOverlay = findViewById(R.id.loadingOverlay);
        pieChartStats = findViewById(R.id.pieChartStats);
    }

    private void initializeServices() {
        locationService = new LocationService(this);
        locationService.setLocationListener(this);
        weatherApiService = ApiClient.getWeatherApiService();
        historyService = new HistoryService(this);
        authManager = new AuthManager(this);
        authManager.seedAdminIfMissing();
        unitManager = UnitManager.getInstance(this);
        
        // Initialize search adapter
        searchAdapter = new LocationSearchAdapter(this, new ArrayList<>());
        etLocationSearch.setAdapter(searchAdapter);
        etLocationSearch.setThreshold(2); // Start searching after 2 characters
    }

    private void ensureLoggedIn() {
        if (authManager != null && !authManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void setupRecyclerViews() {
        // Setup hourly forecast RecyclerView
        LinearLayoutManager hourlyLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvHourlyForecast.setLayoutManager(hourlyLayoutManager);
        hourlyAdapter = new HourlyForecastAdapter(this, new ArrayList<>());
        rvHourlyForecast.setAdapter(hourlyAdapter);

        // Setup daily forecast RecyclerView
        LinearLayoutManager dailyLayoutManager = new LinearLayoutManager(this);
        dailyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDailyForecast.setLayoutManager(dailyLayoutManager);
        rvDailyForecast.setNestedScrollingEnabled(false);
        dailyAdapter = new DailyForecastAdapter(this, new ArrayList<>());
        rvDailyForecast.setAdapter(dailyAdapter);
        
    }
    

    private void setupClickListeners() {
        btnRefresh.setOnClickListener(v -> {
            WeatherAnimationUtils.animateRefreshButton(btnRefresh);
            WeatherAnimationUtils.bounceView(btnRefresh);
            getLocationAndWeather();
        });
        if (btnSearch != null) {
            btnSearch.setOnClickListener(v -> {
                String searchText = etLocationSearch.getText().toString().trim();
                if (!searchText.isEmpty()) {
                    currentLocationQuery = searchText;
                    saveManualSearchToHistory(searchText);
                    fetchWeatherData(currentLocationQuery);
                    etLocationSearch.clearFocus();
                } else {
                    showError("Vui lòng nhập địa điểm");
                }
            });
        }
        btnRefresh.setOnLongClickListener(v -> { showError(getString(R.string.refresh)); return true; });
        
        btnWeatherMap.setOnClickListener(v -> {
            WeatherAnimationUtils.bounceView(btnWeatherMap);
            Intent intent = new Intent(this, WeatherMapActivity.class);
            startActivity(intent);
        });
        btnWeatherMap.setOnLongClickListener(v -> { showError(getString(R.string.weather_map)); return true; });
        
        btnWeatherCalendar.setOnClickListener(v -> {
            WeatherAnimationUtils.bounceView(btnWeatherCalendar);
            Intent intent = new Intent(this, WeatherCalendarActivity.class);
            startActivity(intent);
        });
        btnWeatherCalendar.setOnLongClickListener(v -> { showError(getString(R.string.weather_calendar)); return true; });
        
        btnWeatherCharts.setOnClickListener(v -> {
            WeatherAnimationUtils.bounceView(btnWeatherCharts);
            startActivity(new Intent(this, WeatherChartsActivity.class));
        });
        btnWeatherCharts.setOnLongClickListener(v -> { showError(getString(R.string.weather_charts)); return true; });
        
        // Popup menu button
        btnMenuPopup.setOnClickListener(v -> {
            WeatherAnimationUtils.bounceView(btnMenuPopup);
            showPopupMenu(v);
        });
        btnMenuPopup.setOnLongClickListener(v -> { showError("Menu chính"); return true; });
        
        // Search functionality
        etLocationSearch.setOnItemClickListener((parent, view, position, id) -> {
            LocationSearchResult selectedLocation = searchAdapter.getItem(position);
            if (selectedLocation != null) {
                etLocationSearch.setText(selectedLocation.getFullLocationName());
                currentLocationQuery = selectedLocation.getCoordinateString();
                
                // Save to search history
                saveSearchToHistory(selectedLocation);
                
                fetchWeatherData(currentLocationQuery);
                etLocationSearch.clearFocus();
            }
        });
        
        etLocationSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String searchText = etLocationSearch.getText().toString().trim();
                if (!searchText.isEmpty()) {
                    currentLocationQuery = searchText;
                    
                    // Save manual search to history
                    saveManualSearchToHistory(searchText);
                    
                    fetchWeatherData(currentLocationQuery);
                    etLocationSearch.clearFocus();
                }
                return true;
            }
            return false;
        });
    }

    private void getLocationAndWeather() {
        showLoading(true);
        
        if (locationService.hasLocationPermission()) {
            locationService.getCurrentLocation();
        } else {
            locationService.requestLocationPermission(this);
        }
    }

    private void fetchWeatherData(String location) {
        // Check network connectivity first
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showLoading(false);
            showError("No internet connection. Please check your network settings.");
            return;
        }
        
        Call<WeatherData> call = weatherApiService.getWeatherForecast(
                ApiClient.API_KEY,
                location,
                7, // 7 days forecast
                "no", // No air quality data
                "no"  // No weather alerts
        );

        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(@NonNull Call<WeatherData> call, @NonNull Response<WeatherData> response) {
                showLoading(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    currentWeatherData = response.body();
                    updateUI(currentWeatherData);
                } else {
                    // Enhanced error logging
                    String errorMessage = "Failed to load weather data.";
                    if (response.code() == 401) {
                        errorMessage = "Invalid API key. Please check your API configuration.";
                    } else if (response.code() == 400) {
                        errorMessage = "Invalid location. Please try a different city.";
                    } else if (response.code() == 403) {
                        errorMessage = "API quota exceeded. Please try again later.";
                    } else if (response.code() >= 500) {
                        errorMessage = "Server error. Please try again later.";
                    }
                    showError(errorMessage + " (Code: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherData> call, @NonNull Throwable t) {
                showLoading(false);
                showError("Network error: " + t.getMessage());
            }
        });
    }

    private void updateUI(WeatherData weatherData) {
        try {
            // Update location with animation
            String locationText = weatherData.getLocation().getName() + ", " + 
                                weatherData.getLocation().getCountry();
            tvLocation.setText(locationText);
            WeatherAnimationUtils.fadeIn(tvLocation);

            // Update current weather
            WeatherData.Current current = weatherData.getCurrent();
            
            // Set weather icon with animation
            int iconResource = WeatherUtils.getWeatherIcon(
                    current.getCondition().getCode(), 
                    current.getIsDay() == 1
            );
            ivWeatherIcon.setImageResource(iconResource);
            WeatherAnimationUtils.animateWeatherIcon(ivWeatherIcon);

            // Set temperature and description with animation
            tvTemperature.setText(unitManager.formatTemperature(current.getTempC()));
            tvWeatherDescription.setText(current.getCondition().getText());
            WeatherAnimationUtils.animateTemperatureChange(tvTemperature, unitManager.formatTemperature(current.getTempC()));
            WeatherAnimationUtils.fadeIn(tvWeatherDescription);

            // Update weather details with staggered animation
            tvHumidity.setText(WeatherUtils.formatHumidity(current.getHumidity()));

            // Update sunrise (from first forecast day)
            if (weatherData.getForecast() != null && 
                !weatherData.getForecast().getForecastday().isEmpty()) {
                ForecastDay today = weatherData.getForecast().getForecastday().get(0);

                // Update hourly forecast (next 24 hours)
                updateHourlyForecast(today);

            // Update daily forecast
            List<ForecastDay> forecastDays = weatherData.getForecast().getForecastday();
            Log.d("MainActivity", "Total forecast days received: " + (forecastDays != null ? forecastDays.size() : 0));
            updateDailyForecast(forecastDays);

            // Update stats table
            updateStats(forecastDays);
            }


        } catch (Exception e) {
            showError("Error updating UI: " + e.getMessage());
        }
    }

    private void updateHourlyForecast(ForecastDay today) {
        try {
            List<ForecastDay.Hour> hourlyData = new ArrayList<>();
            // Add all hours from today (simplified)
            for (ForecastDay.Hour hour : today.getHour()) {
                hourlyData.add(hour);
                // Limit to 24 hours
                if (hourlyData.size() >= 24) break;
            }

            
            hourlyAdapter.updateData(hourlyData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDailyForecast(List<ForecastDay> forecastDays) {
        // Debug: Log số ngày nhận được
        if (forecastDays != null) {
            Log.d("DailyForecast", "Received " + forecastDays.size() + " forecast days");
            for (int i = 0; i < forecastDays.size(); i++) {
                ForecastDay day = forecastDays.get(i);
                if (day != null && day.getDate() != null) {
                    Log.d("DailyForecast", "Day " + i + ": " + day.getDate());
                }
            }
        } else {
            Log.w("DailyForecast", "Forecast days list is null");
        }
        
        dailyAdapter.updateData(forecastDays);
    }

    private void updateStats(List<ForecastDay> forecastDays) {
        if (forecastDays == null || forecastDays.isEmpty()) return;
        double sumAvg = 0.0;
        double maxTemp = Double.NEGATIVE_INFINITY;
        double minTemp = Double.POSITIVE_INFINITY;
        double sumHumidity = 0.0;
        int countHours = 0;

        try {
            for (ForecastDay day : forecastDays) {
                if (day == null) continue;
                if (day.getDay() != null) {
                    double avg = day.getDay().getAvgtempC();
                    double mx = day.getDay().getMaxtempC();
                    double mn = day.getDay().getMintempC();
                    sumAvg += avg;
                    if (mx > maxTemp) maxTemp = mx;
                    if (mn < minTemp) minTemp = mn;
                }
                if (day.getHour() != null) {
                    for (ForecastDay.Hour h : day.getHour()) {
                        sumHumidity += h.getHumidity();
                        countHours++;
                    }
                }
            }
            int days = forecastDays.size();
            double avgTemp = days > 0 ? sumAvg / days : 0.0;
            double avgHumidity = countHours > 0 ? sumHumidity / countHours : 0.0;

            tvStatAvgTemp.setText(unitManager.formatTemperature(avgTemp));
            tvStatMaxTemp.setText(unitManager.formatTemperature(maxTemp));
            tvStatMinTemp.setText(unitManager.formatTemperature(minTemp));
            tvStatAvgHumidity.setText(WeatherUtils.formatHumidity((int) Math.round(avgHumidity)));

            // Render pie chart: Avg vs Max vs Min temp contributions
            List<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry((float) Math.max(0, avgTemp), "Avg"));
            entries.add(new PieEntry((float) Math.max(0, maxTemp), "Max"));
            entries.add(new PieEntry((float) Math.max(0, (minTemp < 0 ? -minTemp : minTemp)), "Min|abs"));

            PieDataSet dataSet = new PieDataSet(entries, "Thống kê nhiệt độ");
            dataSet.setSliceSpace(2f);
            dataSet.setColors(new int[]{
                    android.graphics.Color.parseColor("#FFA726"),
                    android.graphics.Color.parseColor("#29B6F6"),
                    android.graphics.Color.parseColor("#66BB6A")
            });
            dataSet.setValueTextSize(12f);
            dataSet.setValueTextColor(android.graphics.Color.WHITE);

            PieData data = new PieData(dataSet);
            pieChartStats.setData(data);
            pieChartStats.getDescription().setEnabled(false);
            Legend legend = pieChartStats.getLegend();
            legend.setEnabled(true);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            pieChartStats.setUsePercentValues(false);
            pieChartStats.setDrawEntryLabels(false);
            pieChartStats.invalidate();
        } catch (Exception ignored) {}
    }

    private void showLoading(boolean show) {
        loadingOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    // LocationService.LocationListener implementation
    @Override
    public void onLocationReceived(Location location) {
        currentLocationQuery = LocationService.locationToCoordinateString(location);
        fetchWeatherData(currentLocationQuery);
    }

    @Override
    public void onLocationError(String error) {
        showError("Location error: " + error);
        // Fallback to default location
        fetchWeatherData(currentLocationQuery);
    }

    @Override
    public void onPermissionDenied() {
        showError("Location permission denied. Using default location.");
        // Fallback to default location
        fetchWeatherData(currentLocationQuery);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == LocationService.getLocationPermissionRequestCode()) {
            locationService.onPermissionResult(requestCode, permissions, grantResults);
        }
    }



    private void showPopupMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
        
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            
            if (id == R.id.action_history) {
                // Mở lịch sử tìm kiếm và yêu thích
                Intent intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.action_charts) {
                // Mở biểu đồ thời tiết
                Intent intent = new Intent(this, WeatherChartsActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.action_settings) {
                // Mở cài đặt
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.action_language) {
                // Chuyển đổi ngôn ngữ
                Intent intent = new Intent(this, LanguageActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.action_about) {
                // Hiển thị thông tin ứng dụng
                showAboutDialog();
                return true;
            } else if (id == R.id.action_admin) {
                String username = (authManager != null) ? authManager.getLoggedInUsername() : null;
                if ("admin".equalsIgnoreCase(username)) {
                    startActivity(new Intent(this, AdminActivity.class));
                } else {
                    Toast.makeText(this, "Chỉ dành cho admin", Toast.LENGTH_SHORT).show();
                }
                return true;
            } else if (id == R.id.action_logout) {
                if (authManager != null) {
                    authManager.logout();
                    Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
            
            return false;
        });
        
        popupMenu.show();
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Giới thiệu ứng dụng")
                .setMessage("Ứng dụng dự báo thời tiết\nPhiên bản 1.0\n\nCung cấp thông tin thời tiết chính xác và đầy đủ cho người dùng Việt Nam.")
                .setPositiveButton("Đóng", null)
                .show();
    }

    private void saveSearchToHistory(LocationSearchResult location) {
        new Thread(() -> {
            try {
                historyService.addOrUpdateSearchHistory(
                    location.getName(),
                    location.getRegion(),
                    location.getCountry(),
                    location.getLat(),
                    location.getLon()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    private void saveManualSearchToHistory(String searchText) {
        new Thread(() -> {
            try {
                historyService.addOrUpdateSearchHistory(
                    searchText,
                    "",
                    "",
                    0.0,
                    0.0
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationService != null) {
            locationService.cleanup();
        }
        if (historyService != null) {
            historyService.close();
        }
    }
}