package com.example.app.models;

import com.google.gson.annotations.SerializedName;

public class WeatherAlert {
    
    @SerializedName("alert")
    private Alert[] alerts;
    
    public static class Alert {
        @SerializedName("headline")
        private String headline;
        
        @SerializedName("msgtype")
        private String msgType;
        
        @SerializedName("severity")
        private String severity;
        
        @SerializedName("urgency")
        private String urgency;
        
        @SerializedName("areas")
        private String areas;
        
        @SerializedName("category")
        private String category;
        
        @SerializedName("certainty")
        private String certainty;
        
        @SerializedName("event")
        private String event;
        
        @SerializedName("note")
        private String note;
        
        @SerializedName("effective")
        private String effective;
        
        @SerializedName("expires")
        private String expires;
        
        @SerializedName("desc")
        private String description;
        
        @SerializedName("instruction")
        private String instruction;
        
        // Getters
        public String getHeadline() { return headline; }
        public String getMsgType() { return msgType; }
        public String getSeverity() { return severity; }
        public String getUrgency() { return urgency; }
        public String getAreas() { return areas; }
        public String getCategory() { return category; }
        public String getCertainty() { return certainty; }
        public String getEvent() { return event; }
        public String getNote() { return note; }
        public String getEffective() { return effective; }
        public String getExpires() { return expires; }
        public String getDescription() { return description; }
        public String getInstruction() { return instruction; }
        
        // Helper methods
        public boolean isActive() {
            // Check if alert is currently active
            long currentTime = System.currentTimeMillis() / 1000;
            long effectiveTime = parseTime(effective);
            long expiresTime = parseTime(expires);
            
            return currentTime >= effectiveTime && currentTime <= expiresTime;
        }
        
        public int getSeverityLevel() {
            switch (severity.toLowerCase()) {
                case "extreme": return 4;
                case "severe": return 3;
                case "moderate": return 2;
                case "minor": return 1;
                default: return 0;
            }
        }
        
        public String getSeverityColor() {
            switch (severity.toLowerCase()) {
                case "extreme": return "#FF0000"; // Red
                case "severe": return "#FF6600";  // Orange
                case "moderate": return "#FFCC00"; // Yellow
                case "minor": return "#00CC00";   // Green
                default: return "#CCCCCC";        // Gray
            }
        }
        
        public String getSeverityIcon() {
            switch (event.toLowerCase()) {
                case "tornado":
                case "severe thunderstorm":
                    return "ic_stormy";
                case "flood":
                case "flash flood":
                    return "ic_rainy";
                case "winter storm":
                case "blizzard":
                    return "ic_snowy";
                case "heat":
                case "excessive heat":
                    return "ic_sunny";
                case "cold":
                case "freeze":
                    return "ic_cloudy";
                default:
                    return "ic_cloudy";
            }
        }
        
        private long parseTime(String timeString) {
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
                    
                    return cal.getTimeInMillis() / 1000; // Convert to seconds
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    }
    
    // Getters
    public Alert[] getAlerts() { return alerts; }
    
    // Helper methods
    public boolean hasAlerts() {
        return alerts != null && alerts.length > 0;
    }
    
    public int getActiveAlertsCount() {
        if (!hasAlerts()) return 0;
        
        int count = 0;
        for (Alert alert : alerts) {
            if (alert.isActive()) {
                count++;
            }
        }
        return count;
    }
    
    public Alert[] getActiveAlerts() {
        if (!hasAlerts()) return new Alert[0];
        
        java.util.List<Alert> activeAlerts = new java.util.ArrayList<>();
        for (Alert alert : alerts) {
            if (alert.isActive()) {
                activeAlerts.add(alert);
            }
        }
        return activeAlerts.toArray(new Alert[0]);
    }
    
    public Alert getMostSevereAlert() {
        if (!hasAlerts()) return null;
        
        Alert mostSevere = null;
        int highestSeverity = -1;
        
        for (Alert alert : alerts) {
            if (alert.isActive() && alert.getSeverityLevel() > highestSeverity) {
                mostSevere = alert;
                highestSeverity = alert.getSeverityLevel();
            }
        }
        
        return mostSevere;
    }
}
