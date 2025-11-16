package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.app.R;
import com.example.app.services.HistoryService;
import com.example.app.models.UserPreferences;
import com.example.app.utils.UnitManager;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Switch switchNotifications;
    private Switch switchLocationServices;
    private Switch switchAutoRefresh;
    private TextView tvTemperatureUnit;
    private TextView tvWindSpeedUnit;
    private TextView tvLanguage;
    private TextView tvClearHistory;
    private TextView tvAbout;
    
    private HistoryService historyService;
    private UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        initViews();
        initToolbar();
        initHistoryService();
        loadUserPreferences();
        setupClickListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        switchNotifications = findViewById(R.id.switch_notifications);
        switchLocationServices = findViewById(R.id.switch_location_services);
        switchAutoRefresh = findViewById(R.id.switch_auto_refresh);
        tvTemperatureUnit = findViewById(R.id.tv_temperature_unit);
        tvWindSpeedUnit = findViewById(R.id.tv_wind_speed_unit);
        tvLanguage = findViewById(R.id.tv_language);
        tvClearHistory = findViewById(R.id.tv_clear_history);
        tvAbout = findViewById(R.id.tv_about);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Cài đặt");
        }
    }

    private void initHistoryService() {
        historyService = new HistoryService(this);
    }

    private void loadUserPreferences() {
        userPreferences = historyService.getUserPreferences();
        if (userPreferences == null) {
            userPreferences = new UserPreferences();
        }
        
        // Cập nhật UI với preferences hiện tại
        switchNotifications.setChecked(userPreferences.isNotificationsEnabled());
        switchLocationServices.setChecked(userPreferences.isAutoLocationEnabled());
        switchAutoRefresh.setChecked(userPreferences.isNotificationsEnabled());
        
        tvTemperatureUnit.setText("Đơn vị nhiệt độ: " + (userPreferences.isCelsius() ? "Celsius" : "Fahrenheit"));
        tvWindSpeedUnit.setText("Đơn vị tốc độ gió: " + (userPreferences.isMetric() ? "km/h" : "mph"));
        tvLanguage.setText("Ngôn ngữ: " + ("vi".equals(userPreferences.getLanguage()) ? "Tiếng Việt" : "English"));
    }

    private void setupClickListeners() {
        // Switch listeners
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            userPreferences.setNotificationsEnabled(isChecked);
            historyService.updateNotificationSettings(isChecked);
            Toast.makeText(this, isChecked ? "Đã bật thông báo" : "Đã tắt thông báo", Toast.LENGTH_SHORT).show();
        });

        switchLocationServices.setOnCheckedChangeListener((buttonView, isChecked) -> {
            userPreferences.setAutoLocationEnabled(isChecked);
            historyService.updateUserPreferences(userPreferences);
            Toast.makeText(this, isChecked ? "Đã bật dịch vụ vị trí" : "Đã tắt dịch vụ vị trí", Toast.LENGTH_SHORT).show();
        });

        switchAutoRefresh.setOnCheckedChangeListener((buttonView, isChecked) -> {
            userPreferences.setNotificationsEnabled(isChecked);
            historyService.updateUserPreferences(userPreferences);
            Toast.makeText(this, isChecked ? "Đã bật làm mới tự động" : "Đã tắt làm mới tự động", Toast.LENGTH_SHORT).show();
        });

        // Click listeners
        tvTemperatureUnit.setOnClickListener(v -> {
            // Chuyển đổi đơn vị nhiệt độ
            String newUnit = userPreferences.isCelsius() ? "fahrenheit" : "celsius";
            userPreferences.setTemperatureUnit(newUnit);
            historyService.updateTemperatureUnit(newUnit);
            tvTemperatureUnit.setText("Đơn vị nhiệt độ: " + (userPreferences.isCelsius() ? "Celsius" : "Fahrenheit"));
            Toast.makeText(this, "Đã chuyển sang " + (userPreferences.isCelsius() ? "Celsius" : "Fahrenheit"), Toast.LENGTH_SHORT).show();
        });

        tvWindSpeedUnit.setOnClickListener(v -> {
            // Chuyển đổi đơn vị tốc độ gió
            String newUnit = userPreferences.isMetric() ? "mph" : "kmh";
            userPreferences.setWindSpeedUnit(newUnit);
            historyService.updateWindSpeedUnit(newUnit);
            tvWindSpeedUnit.setText("Đơn vị tốc độ gió: " + (userPreferences.isMetric() ? "km/h" : "mph"));
            Toast.makeText(this, "Đã chuyển sang " + (userPreferences.isMetric() ? "km/h" : "mph"), Toast.LENGTH_SHORT).show();
        });

        tvLanguage.setOnClickListener(v -> {
            // Mở màn hình chọn ngôn ngữ
            Intent intent = new Intent(this, LanguageActivity.class);
            startActivity(intent);
        });

        tvClearHistory.setOnClickListener(v -> {
            // Xóa lịch sử tìm kiếm
            if (historyService.clearSearchHistory()) {
                Toast.makeText(this, "Đã xóa lịch sử tìm kiếm", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Có lỗi xảy ra khi xóa lịch sử", Toast.LENGTH_SHORT).show();
            }
        });

        tvAbout.setOnClickListener(v -> {
            // Mở thông tin ứng dụng
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("show_about", true);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (historyService != null) {
            historyService.close();
        }
    }
}
