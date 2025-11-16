package com.example.app.models;

import java.util.Date;

public class WeatherViewHistory {
    private int id;
    private int locationId;
    private String locationName;
    private Date viewDate;
    private String weatherCondition;
    private double temperature;
    private String temperatureUnit;
    private int humidity;
    private double windSpeed;
    private String windSpeedUnit;
    private String viewType; // "current", "forecast", "hourly", "details"

    public WeatherViewHistory() {}

    public WeatherViewHistory(int locationId, String locationName, String weatherCondition,
                             double temperature, String temperatureUnit, int humidity,
                             double windSpeed, String windSpeedUnit, String viewType) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.weatherCondition = weatherCondition;
        this.temperature = temperature;
        this.temperatureUnit = temperatureUnit;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windSpeedUnit = windSpeedUnit;
        this.viewType = viewType;
        this.viewDate = new Date();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Date getViewDate() {
        return viewDate;
    }

    public void setViewDate(Date viewDate) {
        this.viewDate = viewDate;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindSpeedUnit() {
        return windSpeedUnit;
    }

    public void setWindSpeedUnit(String windSpeedUnit) {
        this.windSpeedUnit = windSpeedUnit;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    // Helper methods
    public String getFormattedTemperature() {
        return String.format("%.1f°%s", temperature, 
                           "celsius".equals(temperatureUnit) ? "C" : "F");
    }

    public String getFormattedWindSpeed() {
        return String.format("%.1f %s", windSpeed, 
                           "kmh".equals(windSpeedUnit) ? "km/h" : "mph");
    }

    public String getFormattedHumidity() {
        return humidity + "%";
    }

    public boolean isCurrentView() {
        return "current".equals(viewType);
    }

    public boolean isForecastView() {
        return "forecast".equals(viewType);
    }

    public boolean isHourlyView() {
        return "hourly".equals(viewType);
    }

    public boolean isDetailsView() {
        return "details".equals(viewType);
    }
}
