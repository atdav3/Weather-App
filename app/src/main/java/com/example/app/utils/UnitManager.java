package com.example.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UnitManager {
    
    private static final String PREF_NAME = "unit_preferences";
    private static final String KEY_TEMPERATURE_UNIT = "temperature_unit";
    private static final String KEY_WIND_UNIT = "wind_unit";
    private static final String KEY_PRESSURE_UNIT = "pressure_unit";
    private static final String KEY_VISIBILITY_UNIT = "visibility_unit";
    
    public static final String CELSIUS = "celsius";
    public static final String FAHRENHEIT = "fahrenheit";
    public static final String KMH = "kmh";
    public static final String MPH = "mph";
    public static final String HPA = "hpa";
    public static final String INHG = "inhg";
    public static final String KM = "km";
    public static final String MILES = "miles";
    
    private static UnitManager instance;
    private SharedPreferences preferences;
    
    private UnitManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    public static UnitManager getInstance(Context context) {
        if (instance == null) {
            instance = new UnitManager(context.getApplicationContext());
        }
        return instance;
    }
    
    // Temperature conversion
    public void setTemperatureUnit(String unit) {
        preferences.edit().putString(KEY_TEMPERATURE_UNIT, unit).apply();
    }
    
    public String getTemperatureUnit() {
        return preferences.getString(KEY_TEMPERATURE_UNIT, CELSIUS);
    }
    
    public double convertTemperature(double celsius) {
        if (getTemperatureUnit().equals(FAHRENHEIT)) {
            return (celsius * 9/5) + 32;
        }
        return celsius;
    }
    
    public String getTemperatureUnitSymbol() {
        return getTemperatureUnit().equals(FAHRENHEIT) ? "°F" : "°C";
    }
    
    // Wind speed conversion
    public void setWindUnit(String unit) {
        preferences.edit().putString(KEY_WIND_UNIT, unit).apply();
    }
    
    public String getWindUnit() {
        return preferences.getString(KEY_WIND_UNIT, KMH);
    }
    
    public double convertWindSpeed(double kmh) {
        if (getWindUnit().equals(MPH)) {
            return kmh * 0.621371;
        }
        return kmh;
    }
    
    public String getWindUnitSymbol() {
        return getWindUnit().equals(MPH) ? "mph" : "km/h";
    }
    
    // Pressure conversion
    public void setPressureUnit(String unit) {
        preferences.edit().putString(KEY_PRESSURE_UNIT, unit).apply();
    }
    
    public String getPressureUnit() {
        return preferences.getString(KEY_PRESSURE_UNIT, HPA);
    }
    
    public double convertPressure(double hpa) {
        if (getPressureUnit().equals(INHG)) {
            return hpa * 0.02953;
        }
        return hpa;
    }
    
    public String getPressureUnitSymbol() {
        return getPressureUnit().equals(INHG) ? "inHg" : "hPa";
    }
    
    // Visibility conversion
    public void setVisibilityUnit(String unit) {
        preferences.edit().putString(KEY_VISIBILITY_UNIT, unit).apply();
    }
    
    public String getVisibilityUnit() {
        return preferences.getString(KEY_VISIBILITY_UNIT, KM);
    }
    
    public double convertVisibility(double km) {
        if (getVisibilityUnit().equals(MILES)) {
            return km * 0.621371;
        }
        return km;
    }
    
    public String getVisibilityUnitSymbol() {
        return getVisibilityUnit().equals(MILES) ? "miles" : "km";
    }
    
    // Format methods
    public String formatTemperature(double celsius) {
        double converted = convertTemperature(celsius);
        String symbol = getTemperatureUnitSymbol();
        return Math.round(converted) + symbol;
    }
    
    public String formatWindSpeed(double kmh) {
        double converted = convertWindSpeed(kmh);
        String symbol = getWindUnitSymbol();
        return Math.round(converted) + " " + symbol;
    }
    
    public String formatPressure(double hpa) {
        double converted = convertPressure(hpa);
        String symbol = getPressureUnitSymbol();
        return Math.round(converted) + " " + symbol;
    }
    
    public String formatVisibility(double km) {
        double converted = convertVisibility(km);
        String symbol = getVisibilityUnitSymbol();
        return String.format("%.1f %s", converted, symbol);
    }
    
    // Toggle methods
    public void toggleTemperatureUnit() {
        String current = getTemperatureUnit();
        setTemperatureUnit(current.equals(CELSIUS) ? FAHRENHEIT : CELSIUS);
    }
    
    public void toggleWindUnit() {
        String current = getWindUnit();
        setWindUnit(current.equals(KMH) ? MPH : KMH);
    }
    
    public void togglePressureUnit() {
        String current = getPressureUnit();
        setPressureUnit(current.equals(HPA) ? INHG : HPA);
    }
    
    public void toggleVisibilityUnit() {
        String current = getVisibilityUnit();
        setVisibilityUnit(current.equals(KM) ? MILES : KM);
    }
    
    // Get unit names for UI
    public String getTemperatureUnitName() {
        return getTemperatureUnit().equals(FAHRENHEIT) ? "Fahrenheit" : "Celsius";
    }
    
    public String getWindUnitName() {
        return getWindUnit().equals(MPH) ? "MPH" : "KM/H";
    }
    
    public String getPressureUnitName() {
        return getPressureUnit().equals(INHG) ? "inHg" : "hPa";
    }
    
    public String getVisibilityUnitName() {
        return getVisibilityUnit().equals(MILES) ? "Miles" : "Kilometers";
    }
}
