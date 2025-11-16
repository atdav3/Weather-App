package com.example.app.models;

import com.google.gson.annotations.SerializedName;

public class LocationSearchResult {
    @SerializedName("id")
    private int id;

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

    @SerializedName("url")
    private String url;

    // Constructors
    public LocationSearchResult() {}

    public LocationSearchResult(int id, String name, String region, String country, double lat, double lon, String url) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.country = country;
        this.lat = lat;
        this.lon = lon;
        this.url = url;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // Utility methods
    public String getFullLocationName() {
        StringBuilder locationBuilder = new StringBuilder();
        
        if (name != null && !name.isEmpty()) {
            locationBuilder.append(name);
        }
        
        if (region != null && !region.isEmpty() && !region.equals(name)) {
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

    public String getCoordinateString() {
        return lat + "," + lon;
    }

    @Override
    public String toString() {
        return getFullLocationName();
    }
}
