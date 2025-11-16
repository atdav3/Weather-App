package com.example.app.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherData {
    @SerializedName("location")
    private Location location;

    @SerializedName("current")
    private Current current;

    @SerializedName("forecast")
    private Forecast forecast;

    // Constructors
    public WeatherData() {}

    public WeatherData(Location location, Current current, Forecast forecast) {
        this.location = location;
        this.current = current;
        this.forecast = forecast;
    }

    // Getters and Setters
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    // Inner Classes
    public static class Location {
        @SerializedName("name")
        private String name;

        @SerializedName("region")
        private String region;

        @SerializedName("country")
        private String country;

        @SerializedName("lat")
        private double lat;

        @SerializedName("lon")
        private double lon;

        @SerializedName("localtime")
        private String localtime;

        // Constructors
        public Location() {}

        public Location(String name, String region, String country, double lat, double lon, String localtime) {
            this.name = name;
            this.region = region;
            this.country = country;
            this.lat = lat;
            this.lon = lon;
            this.localtime = localtime;
        }

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public String getLocaltime() {
            return localtime;
        }

        public void setLocaltime(String localtime) {
            this.localtime = localtime;
        }

        public String getFullLocationName() {
            return name + ", " + region + ", " + country;
        }
    }

    public static class Current {
        @SerializedName("last_updated")
        private String lastUpdated;

        @SerializedName("temp_c")
        private double tempC;

        @SerializedName("temp_f")
        private double tempF;

        @SerializedName("is_day")
        private int isDay;

        @SerializedName("condition")
        private Condition condition;

        @SerializedName("wind_mph")
        private double windMph;

        @SerializedName("wind_kph")
        private double windKph;

        @SerializedName("wind_degree")
        private int windDegree;

        @SerializedName("wind_dir")
        private String windDir;

        @SerializedName("pressure_mb")
        private double pressureMb;

        @SerializedName("pressure_in")
        private double pressureIn;

        @SerializedName("precip_mm")
        private double precipMm;

        @SerializedName("humidity")
        private int humidity;

        @SerializedName("cloud")
        private int cloud;

        @SerializedName("feelslike_c")
        private double feelslikeC;

        @SerializedName("feelslike_f")
        private double feelslikeF;

        @SerializedName("vis_km")
        private double visKm;

        @SerializedName("vis_miles")
        private double visMiles;

        @SerializedName("uv")
        private double uv;

        // Constructors
        public Current() {}

        // Getters and Setters
        public String getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(String lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        public double getTempC() {
            return tempC;
        }

        public void setTempC(double tempC) {
            this.tempC = tempC;
        }

        public double getTempF() {
            return tempF;
        }

        public void setTempF(double tempF) {
            this.tempF = tempF;
        }

        public int getIsDay() {
            return isDay;
        }

        public void setIsDay(int isDay) {
            this.isDay = isDay;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }

        public double getWindMph() {
            return windMph;
        }

        public void setWindMph(double windMph) {
            this.windMph = windMph;
        }

        public double getWindKph() {
            return windKph;
        }

        public void setWindKph(double windKph) {
            this.windKph = windKph;
        }

        public int getWindDegree() {
            return windDegree;
        }

        public void setWindDegree(int windDegree) {
            this.windDegree = windDegree;
        }

        public String getWindDir() {
            return windDir;
        }

        public void setWindDir(String windDir) {
            this.windDir = windDir;
        }

        public double getPressureMb() {
            return pressureMb;
        }

        public void setPressureMb(double pressureMb) {
            this.pressureMb = pressureMb;
        }

        public double getPressureIn() {
            return pressureIn;
        }

        public void setPressureIn(double pressureIn) {
            this.pressureIn = pressureIn;
        }

        public double getPrecipMm() {
            return precipMm;
        }

        public void setPrecipMm(double precipMm) {
            this.precipMm = precipMm;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public int getCloud() {
            return cloud;
        }

        public void setCloud(int cloud) {
            this.cloud = cloud;
        }

        public double getFeelslikeC() {
            return feelslikeC;
        }

        public void setFeelslikeC(double feelslikeC) {
            this.feelslikeC = feelslikeC;
        }

        public double getFeelslikeF() {
            return feelslikeF;
        }

        public void setFeelslikeF(double feelslikeF) {
            this.feelslikeF = feelslikeF;
        }

        public double getVisKm() {
            return visKm;
        }

        public void setVisKm(double visKm) {
            this.visKm = visKm;
        }

        public double getVisMiles() {
            return visMiles;
        }

        public void setVisMiles(double visMiles) {
            this.visMiles = visMiles;
        }

        public double getUv() {
            return uv;
        }

        public void setUv(double uv) {
            this.uv = uv;
        }
    }

    public static class Condition {
        @SerializedName("text")
        private String text;

        @SerializedName("icon")
        private String icon;

        @SerializedName("code")
        private int code;

        // Constructors
        public Condition() {}

        public Condition(String text, String icon, int code) {
            this.text = text;
            this.icon = icon;
            this.code = code;
        }

        // Getters and Setters
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    public static class Forecast {
        @SerializedName("forecastday")
        private List<ForecastDay> forecastday;

        // Constructors
        public Forecast() {}

        public Forecast(List<ForecastDay> forecastday) {
            this.forecastday = forecastday;
        }

        // Getters and Setters
        public List<ForecastDay> getForecastday() {
            return forecastday;
        }

        public void setForecastday(List<ForecastDay> forecastday) {
            this.forecastday = forecastday;
        }
    }
}
