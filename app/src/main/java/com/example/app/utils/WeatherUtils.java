package com.example.app.utils;

import android.content.Context;
import com.example.app.R;
import com.example.app.models.WeatherData;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WeatherUtils {

    /**
     * Get weather icon resource based on condition code and day/night
     */
    public static int getWeatherIcon(int conditionCode, boolean isDay) {
        // Weather condition codes from WeatherAPI.com
        switch (conditionCode) {
            case 1000: // Sunny/Clear
                return R.drawable.ic_sunny;
            case 1003: // Partly cloudy
            case 1006: // Cloudy
            case 1009: // Overcast
                return R.drawable.ic_cloudy;
            case 1063: // Patchy rain possible
            case 1150: // Patchy light drizzle
            case 1153: // Light drizzle
            case 1168: // Freezing drizzle
            case 1171: // Heavy freezing drizzle
            case 1180: // Patchy light rain
            case 1183: // Light rain
            case 1186: // Moderate rain at times
            case 1189: // Moderate rain
            case 1192: // Heavy rain at times
            case 1195: // Heavy rain
            case 1198: // Light freezing rain
            case 1201: // Moderate or heavy freezing rain
            case 1240: // Light rain shower
            case 1243: // Moderate or heavy rain shower
            case 1246: // Torrential rain shower
                return R.drawable.ic_rainy;
            case 1066: // Patchy snow possible
            case 1069: // Patchy sleet possible
            case 1072: // Patchy freezing drizzle possible
            case 1114: // Blowing snow
            case 1117: // Blizzard
            case 1204: // Light sleet
            case 1207: // Moderate or heavy sleet
            case 1210: // Patchy light snow
            case 1213: // Light snow
            case 1216: // Patchy moderate snow
            case 1219: // Moderate snow
            case 1222: // Patchy heavy snow
            case 1225: // Heavy snow
            case 1237: // Ice pellets
            case 1249: // Light sleet showers
            case 1252: // Moderate or heavy sleet showers
            case 1255: // Light snow showers
            case 1258: // Moderate or heavy snow showers
            case 1261: // Light showers of ice pellets
            case 1264: // Moderate or heavy showers of ice pellets
                return R.drawable.ic_rainy; // Using rainy icon for snow as well
            case 1087: // Thundery outbreaks possible
            case 1273: // Patchy light rain with thunder
            case 1276: // Moderate or heavy rain with thunder
            case 1279: // Patchy light snow with thunder
            case 1282: // Moderate or heavy snow with thunder
                return R.drawable.ic_rainy; // Using rainy icon for thunderstorms
            case 1030: // Mist
            case 1135: // Fog
            case 1147: // Freezing fog
                return R.drawable.ic_cloudy;
            default:
                return isDay ? R.drawable.ic_sunny : R.drawable.ic_cloudy;
        }
    }

    /**
     * Format temperature with degree symbol
     */
    public static String formatTemperature(double temperature) {
        return Math.round(temperature) + "°";
    }

    /**
     * Format temperature with unit
     */
    public static String formatTemperatureWithUnit(double temperature, boolean isCelsius) {
        if (isCelsius) {
            return Math.round(temperature) + "°C";
        } else {
            return Math.round(temperature) + "°F";
        }
    }

    /**
     * Format humidity as percentage
     */
    public static String formatHumidity(int humidity) {
        return humidity + "%";
    }

    /**
     * Format wind speed
     */
    public static String formatWindSpeed(double windSpeed, boolean isKmh) {
        if (isKmh) {
            return Math.round(windSpeed) + " km/h";
        } else {
            return Math.round(windSpeed) + " mph";
        }
    }

    /**
     * Format pressure
     */
    public static String formatPressure(double pressure) {
        return Math.round(pressure) + " hPa";
    }

    /**
     * Format visibility
     */
    public static String formatVisibility(double visibility, boolean isKm) {
        if (isKm) {
            return String.format(Locale.getDefault(), "%.1f km", visibility);
        } else {
            return String.format(Locale.getDefault(), "%.1f miles", visibility);
        }
    }

    /**
     * Format UV index
     */
    public static String formatUvIndex(double uvIndex) {
        return String.valueOf(Math.round(uvIndex));
    }

    /**
     * Get UV index description
     */
    public static String getUvIndexDescription(double uvIndex, Context context) {
        if (uvIndex <= 2) {
            return "Low";
        } else if (uvIndex <= 5) {
            return "Moderate";
        } else if (uvIndex <= 7) {
            return "High";
        } else if (uvIndex <= 10) {
            return "Very High";
        } else {
            return "Extreme";
        }
    }

    /**
     * Format time from API time string (yyyy-MM-dd HH:mm)
     */
    public static String formatTime(String timeString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date date = inputFormat.parse(timeString);
            return outputFormat.format(date);
        } catch (Exception e) {
            return timeString;
        }
    }

    /**
     * Format time from API time string to display format (HH:mm)
     */
    public static String formatHourlyTime(String timeString) {
        try {
            if (timeString != null && timeString.length() >= 16) {
                return timeString.substring(11, 16); // Extract "HH:mm" from "yyyy-MM-dd HH:mm"
            }
            return timeString;
        } catch (Exception e) {
            return timeString;
        }
    }

    /**
     * Get day name from date string
     */
    public static String getDayName(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inputFormat.parse(dateString);
            
            Calendar calendar = Calendar.getInstance();
            Calendar today = Calendar.getInstance();
            calendar.setTime(date);
            
            if (calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) && 
                calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
                return "Today";
            }
            
            today.add(Calendar.DAY_OF_YEAR, 1);
            if (calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) && 
                calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
                return "Tomorrow";
            }
            
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
            return outputFormat.format(date);
        } catch (Exception e) {
            return dateString;
        }
    }

    /**
     * Format feels like temperature
     */
    public static String formatFeelsLike(double feelsLike, Context context) {
        return context.getString(R.string.feels_like, formatTemperature(feelsLike));
    }

    /**
     * Format last updated time
     */
    public static String formatLastUpdated(String lastUpdated, Context context) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date date = inputFormat.parse(lastUpdated);
            String time = outputFormat.format(date);
            return context.getString(R.string.last_updated, time);
        } catch (Exception e) {
            return context.getString(R.string.last_updated, lastUpdated);
        }
    }

    /**
     * Check if it's currently day time based on sunrise and sunset
     */
    public static boolean isDayTime(String sunrise, String sunset) {
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            Date sunriseTime = timeFormat.parse(sunrise);
            Date sunsetTime = timeFormat.parse(sunset);
            Date currentTime = new Date();
            
            Calendar sunriseCal = Calendar.getInstance();
            Calendar sunsetCal = Calendar.getInstance();
            Calendar currentCal = Calendar.getInstance();
            
            sunriseCal.setTime(sunriseTime);
            sunsetCal.setTime(sunsetTime);
            currentCal.setTime(currentTime);
            
            int currentHour = currentCal.get(Calendar.HOUR_OF_DAY);
            int currentMinute = currentCal.get(Calendar.MINUTE);
            int sunriseHour = sunriseCal.get(Calendar.HOUR_OF_DAY);
            int sunriseMinute = sunriseCal.get(Calendar.MINUTE);
            int sunsetHour = sunsetCal.get(Calendar.HOUR_OF_DAY);
            int sunsetMinute = sunsetCal.get(Calendar.MINUTE);
            
            int currentTotalMinutes = currentHour * 60 + currentMinute;
            int sunriseTotalMinutes = sunriseHour * 60 + sunriseMinute;
            int sunsetTotalMinutes = sunsetHour * 60 + sunsetMinute;
            
            return currentTotalMinutes >= sunriseTotalMinutes && currentTotalMinutes <= sunsetTotalMinutes;
        } catch (Exception e) {
            // Default to day time if parsing fails
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            return hour >= 6 && hour < 18; // Assume day time is 6 AM to 6 PM
        }
    }
}
