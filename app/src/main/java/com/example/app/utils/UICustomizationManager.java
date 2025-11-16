package com.example.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

public class UICustomizationManager {
    
    private static final String PREF_NAME = "ui_customization_preferences";
    private static final String KEY_ACCENT_COLOR = "accent_color";
    private static final String KEY_BACKGROUND_STYLE = "background_style";
    private static final String KEY_CARD_STYLE = "card_style";
    private static final String KEY_ANIMATION_SPEED = "animation_speed";
    private static final String KEY_FONT_SIZE = "font_size";
    private static final String KEY_LAYOUT_DENSITY = "layout_density";
    
    public static final String BACKGROUND_GRADIENT = "gradient";
    public static final String BACKGROUND_SOLID = "solid";
    public static final String BACKGROUND_IMAGE = "image";
    
    public static final String CARD_ROUNDED = "rounded";
    public static final String CARD_SQUARE = "square";
    public static final String CARD_GLASS = "glass";
    
    public static final String ANIMATION_FAST = "fast";
    public static final String ANIMATION_NORMAL = "normal";
    public static final String ANIMATION_SLOW = "slow";
    
    public static final String FONT_SMALL = "small";
    public static final String FONT_MEDIUM = "medium";
    public static final String FONT_LARGE = "large";
    
    public static final String DENSITY_COMPACT = "compact";
    public static final String DENSITY_NORMAL = "normal";
    public static final String DENSITY_SPACIOUS = "spacious";
    
    private static UICustomizationManager instance;
    private SharedPreferences preferences;
    
    private UICustomizationManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    public static UICustomizationManager getInstance(Context context) {
        if (instance == null) {
            instance = new UICustomizationManager(context);
        }
        return instance;
    }
    
    // Accent Color
    public void setAccentColor(int color) {
        preferences.edit().putInt(KEY_ACCENT_COLOR, color).apply();
    }
    
    public int getAccentColor() {
        return preferences.getInt(KEY_ACCENT_COLOR, Color.parseColor("#4A90E2"));
    }
    
    public String getAccentColorHex() {
        return String.format("#%06X", (0xFFFFFF & getAccentColor()));
    }
    
    // Background Style
    public void setBackgroundStyle(String style) {
        preferences.edit().putString(KEY_BACKGROUND_STYLE, style).apply();
    }
    
    public String getBackgroundStyle() {
        return preferences.getString(KEY_BACKGROUND_STYLE, BACKGROUND_GRADIENT);
    }
    
    // Card Style
    public void setCardStyle(String style) {
        preferences.edit().putString(KEY_CARD_STYLE, style).apply();
    }
    
    public String getCardStyle() {
        return preferences.getString(KEY_CARD_STYLE, CARD_ROUNDED);
    }
    
    // Animation Speed
    public void setAnimationSpeed(String speed) {
        preferences.edit().putString(KEY_ANIMATION_SPEED, speed).apply();
    }
    
    public String getAnimationSpeed() {
        return preferences.getString(KEY_ANIMATION_SPEED, ANIMATION_NORMAL);
    }
    
    public long getAnimationDuration() {
        switch (getAnimationSpeed()) {
            case ANIMATION_FAST:
                return 200;
            case ANIMATION_SLOW:
                return 800;
            case ANIMATION_NORMAL:
            default:
                return 400;
        }
    }
    
    // Font Size
    public void setFontSize(String size) {
        preferences.edit().putString(KEY_FONT_SIZE, size).apply();
    }
    
    public String getFontSize() {
        return preferences.getString(KEY_FONT_SIZE, FONT_MEDIUM);
    }
    
    public float getFontSizeMultiplier() {
        switch (getFontSize()) {
            case FONT_SMALL:
                return 0.8f;
            case FONT_LARGE:
                return 1.2f;
            case FONT_MEDIUM:
            default:
                return 1.0f;
        }
    }
    
    // Layout Density
    public void setLayoutDensity(String density) {
        preferences.edit().putString(KEY_LAYOUT_DENSITY, density).apply();
    }
    
    public String getLayoutDensity() {
        return preferences.getString(KEY_LAYOUT_DENSITY, DENSITY_NORMAL);
    }
    
    public float getDensityMultiplier() {
        switch (getLayoutDensity()) {
            case DENSITY_COMPACT:
                return 0.8f;
            case DENSITY_SPACIOUS:
                return 1.2f;
            case DENSITY_NORMAL:
            default:
                return 1.0f;
        }
    }
    
    // Preset Themes
    public void applyPresetTheme(String themeName) {
        switch (themeName.toLowerCase()) {
            case "ocean":
                setAccentColor(Color.parseColor("#2196F3"));
                setBackgroundStyle(BACKGROUND_GRADIENT);
                setCardStyle(CARD_ROUNDED);
                break;
            case "sunset":
                setAccentColor(Color.parseColor("#FF5722"));
                setBackgroundStyle(BACKGROUND_GRADIENT);
                setCardStyle(CARD_GLASS);
                break;
            case "forest":
                setAccentColor(Color.parseColor("#4CAF50"));
                setBackgroundStyle(BACKGROUND_SOLID);
                setCardStyle(CARD_ROUNDED);
                break;
            case "minimal":
                setAccentColor(Color.parseColor("#9E9E9E"));
                setBackgroundStyle(BACKGROUND_SOLID);
                setCardStyle(CARD_SQUARE);
                break;
            case "vibrant":
                setAccentColor(Color.parseColor("#E91E63"));
                setBackgroundStyle(BACKGROUND_GRADIENT);
                setCardStyle(CARD_GLASS);
                break;
        }
    }
    
    // Reset to Default
    public void resetToDefault() {
        setAccentColor(Color.parseColor("#4A90E2"));
        setBackgroundStyle(BACKGROUND_GRADIENT);
        setCardStyle(CARD_ROUNDED);
        setAnimationSpeed(ANIMATION_NORMAL);
        setFontSize(FONT_MEDIUM);
        setLayoutDensity(DENSITY_NORMAL);
    }
    
    // Get Theme Summary
    public String getThemeSummary() {
        return String.format("Accent: %s, Background: %s, Cards: %s", 
                getAccentColorHex(), 
                getBackgroundStyle(), 
                getCardStyle());
    }
    
    // Check if using custom theme
    public boolean isUsingCustomTheme() {
        return !getAccentColorHex().equals("#4A90E2") || 
               !getBackgroundStyle().equals(BACKGROUND_GRADIENT) ||
               !getCardStyle().equals(CARD_ROUNDED);
    }
}
