package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.app.R;
import com.example.app.services.HistoryService;
import com.example.app.models.UserPreferences;

public class LanguageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RadioGroup radioGroupLanguage;
    private RadioButton radioVietnamese;
    private RadioButton radioEnglish;
    
    private HistoryService historyService;
    private UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        
        initViews();
        initToolbar();
        initHistoryService();
        loadCurrentLanguage();
        setupClickListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        radioGroupLanguage = findViewById(R.id.radio_group_language);
        radioVietnamese = findViewById(R.id.radio_vietnamese);
        radioEnglish = findViewById(R.id.radio_english);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chọn ngôn ngữ");
        }
    }

    private void initHistoryService() {
        historyService = new HistoryService(this);
    }

    private void loadCurrentLanguage() {
        userPreferences = historyService.getUserPreferences();
        if (userPreferences == null) {
            userPreferences = new UserPreferences();
        }
        
        String currentLanguage = userPreferences.getLanguage();
        if ("en".equals(currentLanguage)) {
            radioEnglish.setChecked(true);
        } else {
            radioVietnamese.setChecked(true); // Default to Vietnamese
        }
    }

    private void setupClickListeners() {
        radioGroupLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            String selectedLanguage;
            if (checkedId == R.id.radio_english) {
                selectedLanguage = "en";
            } else {
                selectedLanguage = "vi";
            }
            
            // Cập nhật ngôn ngữ trong preferences
            userPreferences.setLanguage(selectedLanguage);
            historyService.updateLanguage(selectedLanguage);
            
            String displayName = "en".equals(selectedLanguage) ? "English" : "Tiếng Việt";
            Toast.makeText(this, "Đã chọn ngôn ngữ: " + displayName, Toast.LENGTH_SHORT).show();
            
            // Lưu ý: Để thay đổi ngôn ngữ thực sự, cần restart app hoặc reload resources
            // Hiện tại chỉ lưu preference, cần implement LocaleManager để thay đổi ngôn ngữ runtime
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
