package com.example.app.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.app.MainActivity;
import com.example.app.R;
import com.example.app.models.WeatherAlert;

import java.util.HashSet;
import java.util.Set;

public class AlertManager {
    
    private static final String PREF_NAME = "alert_preferences";
    private static final String KEY_ALERT_ENABLED = "alert_enabled";
    private static final String KEY_SEVERITY_LEVEL = "severity_level";
    private static final String KEY_NOTIFIED_ALERTS = "notified_alerts";
    private static final String KEY_ALERT_SOUND = "alert_sound";
    private static final String KEY_ALERT_VIBRATION = "alert_vibration";
    
    private static final String CHANNEL_ID = "weather_alerts";
    private static final String CHANNEL_NAME = "Weather Alerts";
    private static final String CHANNEL_DESCRIPTION = "Important weather alerts and warnings";
    
    private static AlertManager instance;
    private Context context;
    private SharedPreferences preferences;
    private NotificationManagerCompat notificationManager;
    
    private AlertManager(Context context) {
        this.context = context.getApplicationContext();
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.notificationManager = NotificationManagerCompat.from(context);
        createNotificationChannel();
    }
    
    public static AlertManager getInstance(Context context) {
        if (instance == null) {
            instance = new AlertManager(context);
        }
        return instance;
    }
    
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESCRIPTION);
            channel.enableVibration(isVibrationEnabled());
            channel.setShowBadge(true);
            
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
    
    // Settings
    public void setAlertEnabled(boolean enabled) {
        preferences.edit().putBoolean(KEY_ALERT_ENABLED, enabled).apply();
    }
    
    public boolean isAlertEnabled() {
        return preferences.getBoolean(KEY_ALERT_ENABLED, true);
    }
    
    public void setSeverityLevel(int level) {
        preferences.edit().putInt(KEY_SEVERITY_LEVEL, level).apply();
    }
    
    public int getSeverityLevel() {
        return preferences.getInt(KEY_SEVERITY_LEVEL, 1); // Default: Minor and above
    }
    
    public void setAlertSound(boolean enabled) {
        preferences.edit().putBoolean(KEY_ALERT_SOUND, enabled).apply();
    }
    
    public boolean isAlertSoundEnabled() {
        return preferences.getBoolean(KEY_ALERT_SOUND, true);
    }
    
    public void setAlertVibration(boolean enabled) {
        preferences.edit().putBoolean(KEY_ALERT_VIBRATION, enabled).apply();
    }
    
    public boolean isVibrationEnabled() {
        return preferences.getBoolean(KEY_ALERT_VIBRATION, true);
    }
    
    // Alert processing
    public void processAlerts(WeatherAlert weatherAlert) {
        if (!isAlertEnabled() || !weatherAlert.hasAlerts()) {
            return;
        }
        
        WeatherAlert.Alert[] activeAlerts = weatherAlert.getActiveAlerts();
        for (WeatherAlert.Alert alert : activeAlerts) {
            if (shouldShowNotification(alert)) {
                showAlertNotification(alert);
                markAlertAsNotified(alert);
            }
        }
    }
    
    private boolean shouldShowNotification(WeatherAlert.Alert alert) {
        // Check if alert meets severity threshold
        if (alert.getSeverityLevel() < getSeverityLevel()) {
            return false;
        }
        
        // Check if already notified
        if (isAlertAlreadyNotified(alert)) {
            return false;
        }
        
        return true;
    }
    
    private void showAlertNotification(WeatherAlert.Alert alert) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(getAlertIcon(alert))
                .setContentTitle(alert.getHeadline())
                .setContentText(alert.getDescription())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(alert.getDescription())
                        .setBigContentTitle(alert.getHeadline()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        
        // Add sound if enabled
        if (isAlertSoundEnabled()) {
            builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        }
        
        // Add vibration if enabled
        if (isVibrationEnabled()) {
            builder.setVibrate(new long[]{0, 500, 200, 500});
        }
        
        // Set color based on severity
        builder.setColor(android.graphics.Color.parseColor(alert.getSeverityColor()));
        
        // Show notification
        try {
            notificationManager.notify(alert.hashCode(), builder.build());
        } catch (SecurityException e) {
            // Handle notification permission not granted
            e.printStackTrace();
        }
    }
    
    private int getAlertIcon(WeatherAlert.Alert alert) {
        String iconName = alert.getSeverityIcon();
        return context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
    }
    
    private void markAlertAsNotified(WeatherAlert.Alert alert) {
        Set<String> notifiedAlerts = getNotifiedAlerts();
        notifiedAlerts.add(alert.getHeadline() + "_" + alert.getEffective());
        preferences.edit().putStringSet(KEY_NOTIFIED_ALERTS, notifiedAlerts).apply();
    }
    
    private boolean isAlertAlreadyNotified(WeatherAlert.Alert alert) {
        Set<String> notifiedAlerts = getNotifiedAlerts();
        String alertKey = alert.getHeadline() + "_" + alert.getEffective();
        return notifiedAlerts.contains(alertKey);
    }
    
    private Set<String> getNotifiedAlerts() {
        return preferences.getStringSet(KEY_NOTIFIED_ALERTS, new HashSet<>());
    }
    
    // Clear old notifications
    public void clearOldNotifications() {
        // Clear notifications older than 24 hours
        long currentTime = System.currentTimeMillis();
        Set<String> notifiedAlerts = getNotifiedAlerts();
        Set<String> validAlerts = new HashSet<>();
        
        for (String alertKey : notifiedAlerts) {
            try {
                String[] parts = alertKey.split("_", 2);
                if (parts.length == 2) {
                    long alertTime = parseTimeString(parts[1]);
                    if (currentTime - alertTime < 24 * 60 * 60 * 1000) { // 24 hours
                        validAlerts.add(alertKey);
                    }
                }
            } catch (Exception e) {
                // Skip invalid entries
            }
        }
        
        preferences.edit().putStringSet(KEY_NOTIFIED_ALERTS, validAlerts).apply();
    }
    
    // Clear all notifications
    public void clearAllNotifications() {
        notificationManager.cancelAll();
        preferences.edit().remove(KEY_NOTIFIED_ALERTS).apply();
    }
    
    // Get alert summary
    public String getAlertSummary(WeatherAlert weatherAlert) {
        if (!weatherAlert.hasAlerts()) {
            return "No active weather alerts";
        }
        
        int activeCount = weatherAlert.getActiveAlertsCount();
        WeatherAlert.Alert mostSevere = weatherAlert.getMostSevereAlert();
        
        if (activeCount == 1) {
            return "1 active alert: " + mostSevere.getEvent();
        } else {
            return activeCount + " active alerts. Most severe: " + mostSevere.getEvent();
        }
    }
    
    // Check if location has alerts
    public boolean hasActiveAlerts(WeatherAlert weatherAlert) {
        return weatherAlert.hasAlerts() && weatherAlert.getActiveAlertsCount() > 0;
    }
    
    // Get alert count for badge
    public int getAlertCount(WeatherAlert weatherAlert) {
        if (!isAlertEnabled()) return 0;
        
        int count = 0;
        if (weatherAlert.hasAlerts()) {
            for (WeatherAlert.Alert alert : weatherAlert.getActiveAlerts()) {
                if (alert.getSeverityLevel() >= getSeverityLevel()) {
                    count++;
                }
            }
        }
        return count;
    }
    
    // Helper method to parse time string for older API levels
    private long parseTimeString(String timeString) {
        try {
            // Simple ISO 8601 parsing for older Android versions
            if (timeString.contains("T")) {
                String[] parts = timeString.split("T");
                String datePart = parts[0];
                String timePart = parts[1].replace("Z", "");
                
                String[] dateParts = datePart.split("-");
                String[] timeParts = timePart.split(":");
                
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int day = Integer.parseInt(dateParts[2]);
                int hour = Integer.parseInt(timeParts[0]);
                int minute = Integer.parseInt(timeParts[1]);
                int second = Integer.parseInt(timeParts[2].split("\\.")[0]);
                
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.set(year, month - 1, day, hour, minute, second);
                cal.set(java.util.Calendar.MILLISECOND, 0);
                
                return cal.getTimeInMillis();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
