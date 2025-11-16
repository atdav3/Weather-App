package com.example.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {
    
    private static final String PREF_NAME = "theme_preferences";
    private static final String KEY_THEME_MODE = "theme_mode";
    
    public static final int THEME_AUTO = 0;
    public static final int THEME_LIGHT = 1;
    public static final int THEME_DARK = 2;
    
    private static ThemeManager instance;
    private SharedPreferences preferences;
    
    private ThemeManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    public static ThemeManager getInstance(Context context) {
        if (instance == null) {
            instance = new ThemeManager(context.getApplicationContext());
        }
        return instance;
    }
    
    public void setThemeMode(int themeMode) {
        preferences.edit().putInt(KEY_THEME_MODE, themeMode).apply();
        applyTheme(themeMode);
    }
    
    public int getThemeMode() {
        return preferences.getInt(KEY_THEME_MODE, THEME_AUTO);
    }
    
    public void applyTheme(int themeMode) {
        switch (themeMode) {
            case THEME_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case THEME_AUTO:
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                }
                break;
        }
    }
    
    public boolean isDarkTheme(Context context) {
        int currentTheme = getCurrentTheme(context);
        return currentTheme == THEME_DARK || 
               (currentTheme == THEME_AUTO && isSystemDarkMode(context));
    }
    
    public boolean isLightTheme(Context context) {
        int currentTheme = getCurrentTheme(context);
        return currentTheme == THEME_LIGHT || 
               (currentTheme == THEME_AUTO && !isSystemDarkMode(context));
    }
    
    private int getCurrentTheme(Context context) {
        return preferences.getInt(KEY_THEME_MODE, THEME_AUTO);
    }
    
    private boolean isSystemDarkMode(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & 
                           Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
    
    public String getThemeModeName(Context context) {
        switch (getThemeMode()) {
            case THEME_LIGHT:
                return "Light";
            case THEME_DARK:
                return "Dark";
            case THEME_AUTO:
            default:
                return "Auto";
        }
    }
    
    public void toggleTheme(Context context) {
        int currentMode = getThemeMode();
        int newMode;
        
        switch (currentMode) {
            case THEME_LIGHT:
                newMode = THEME_DARK;
                break;
            case THEME_DARK:
                newMode = THEME_AUTO;
                break;
            case THEME_AUTO:
            default:
                newMode = THEME_LIGHT;
                break;
        }
        
        setThemeMode(newMode);
    }
}
