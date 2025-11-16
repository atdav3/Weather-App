package com.example.app.models;

import java.util.Date;

public class SearchHistory {
    private int id;
    private String locationName;
    private String region;
    private String country;
    private double latitude;
    private double longitude;
    private Date searchDate;
    private int searchCount;
    private boolean isFavorite;

    public SearchHistory() {}

    public SearchHistory(String locationName, String region, String country, 
                        double latitude, double longitude) {
        this.locationName = locationName;
        this.region = region;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.searchDate = new Date();
        this.searchCount = 1;
        this.isFavorite = false;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(Date searchDate) {
        this.searchDate = searchDate;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getFullLocationName() {
        StringBuilder locationBuilder = new StringBuilder();
        
        if (locationName != null && !locationName.isEmpty()) {
            locationBuilder.append(locationName);
        }
        
        if (region != null && !region.isEmpty() && !region.equals(locationName)) {
            if (locationBuilder.length() > 0) {
                locationBuilder.append(", ");
            }
            locationBuilder.append(region);
        }
        
        if (country != null && !country.isEmpty()) {
            if (locationBuilder.length() > 0) {
                locationBuilder.append(", ");
            }
            locationBuilder.append(country);
        }
        
        return locationBuilder.toString();
    }

    public void incrementSearchCount() {
        this.searchCount++;
        this.searchDate = new Date();
    }
}
