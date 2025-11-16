package com.example.app.models;

public class UserPreferences {
    private int id;
    private String temperatureUnit; // "celsius" or "fahrenheit"
    private String windSpeedUnit;   // "kmh" or "mph"
    private String pressureUnit;    // "mb" or "in"
    private String language;        // "vi", "en", etc.
    private boolean notificationsEnabled;
    private boolean autoLocationEnabled;
    private int refreshInterval;    // minutes
    private String theme;           // "light", "dark", "auto"
    private boolean showHumidity;
    private boolean showWind;
    private boolean showPressure;
    private boolean showUV;

    public UserPreferences() {
        // Default values
        this.temperatureUnit = "celsius";
        this.windSpeedUnit = "kmh";
        this.pressureUnit = "mb";
        this.language = "vi";
        this.notificationsEnabled = true;
        this.autoLocationEnabled = true;
        this.refreshInterval = 30;
        this.theme = "auto";
        this.showHumidity = true;
        this.showWind = true;
        this.showPressure = true;
        this.showUV = true;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public String getWindSpeedUnit() {
        return windSpeedUnit;
    }

    public void setWindSpeedUnit(String windSpeedUnit) {
        this.windSpeedUnit = windSpeedUnit;
    }

    public String getPressureUnit() {
        return pressureUnit;
    }

    public void setPressureUnit(String pressureUnit) {
        this.pressureUnit = pressureUnit;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public boolean isAutoLocationEnabled() {
        return autoLocationEnabled;
    }

    public void setAutoLocationEnabled(boolean autoLocationEnabled) {
        this.autoLocationEnabled = autoLocationEnabled;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean isShowHumidity() {
        return showHumidity;
    }

    public void setShowHumidity(boolean showHumidity) {
        this.showHumidity = showHumidity;
    }

    public boolean isShowWind() {
        return showWind;
    }

    public void setShowWind(boolean showWind) {
        this.showWind = showWind;
    }

    public boolean isShowPressure() {
        return showPressure;
    }

    public void setShowPressure(boolean showPressure) {
        this.showPressure = showPressure;
    }

    public boolean isShowUV() {
        return showUV;
    }

    public void setShowUV(boolean showUV) {
        this.showUV = showUV;
    }

    // Helper methods
    public boolean isCelsius() {
        return "celsius".equals(temperatureUnit);
    }

    public boolean isFahrenheit() {
        return "fahrenheit".equals(temperatureUnit);
    }

    public boolean isMetric() {
        return "kmh".equals(windSpeedUnit) && "mb".equals(pressureUnit);
    }

    public boolean isImperial() {
        return "mph".equals(windSpeedUnit) && "in".equals(pressureUnit);
    }
}
