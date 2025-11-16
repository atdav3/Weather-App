package com.example.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.app.models.LocationSearchResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocationManager {
    
    private static final String PREF_NAME = "location_preferences";
    private static final String KEY_SAVED_LOCATIONS = "saved_locations";
    private static final String KEY_CURRENT_LOCATION = "current_location";
    private static final String KEY_DEFAULT_LOCATION = "default_location";
    
    private static LocationManager instance;
    private SharedPreferences preferences;
    private Gson gson;
    
    private LocationManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }
    
    public static LocationManager getInstance(Context context) {
        if (instance == null) {
            instance = new LocationManager(context.getApplicationContext());
        }
        return instance;
    }
    
    // Save location
    public void saveLocation(LocationSearchResult location) {
        List<LocationSearchResult> locations = getSavedLocations();
        
        // Check if location already exists
        boolean exists = false;
        for (LocationSearchResult savedLocation : locations) {
            if (savedLocation.getCoordinateString().equals(location.getCoordinateString())) {
                exists = true;
                break;
            }
        }
        
        if (!exists) {
            locations.add(location);
            saveLocationsList(locations);
        }
    }
    
    // Remove location
    public void removeLocation(LocationSearchResult location) {
        List<LocationSearchResult> locations = getSavedLocations();
        locations.removeIf(savedLocation -> 
            savedLocation.getCoordinateString().equals(location.getCoordinateString()));
        saveLocationsList(locations);
    }
    
    // Get all saved locations
    public List<LocationSearchResult> getSavedLocations() {
        String json = preferences.getString(KEY_SAVED_LOCATIONS, "[]");
        Type type = new TypeToken<ArrayList<LocationSearchResult>>(){}.getType();
        List<LocationSearchResult> locations = gson.fromJson(json, type);
        return locations != null ? locations : new ArrayList<>();
    }
    
    // Save locations list
    private void saveLocationsList(List<LocationSearchResult> locations) {
        String json = gson.toJson(locations);
        preferences.edit().putString(KEY_SAVED_LOCATIONS, json).apply();
    }
    
    // Set current location
    public void setCurrentLocation(String locationQuery) {
        preferences.edit().putString(KEY_CURRENT_LOCATION, locationQuery).apply();
    }
    
    // Get current location
    public String getCurrentLocation() {
        return preferences.getString(KEY_CURRENT_LOCATION, "Ho Chi Minh City");
    }
    
    // Set default location
    public void setDefaultLocation(String locationQuery) {
        preferences.edit().putString(KEY_DEFAULT_LOCATION, locationQuery).apply();
    }
    
    // Get default location
    public String getDefaultLocation() {
        return preferences.getString(KEY_DEFAULT_LOCATION, "Ho Chi Minh City");
    }
    
    // Check if location is saved
    public boolean isLocationSaved(LocationSearchResult location) {
        List<LocationSearchResult> locations = getSavedLocations();
        for (LocationSearchResult savedLocation : locations) {
            if (savedLocation.getCoordinateString().equals(location.getCoordinateString())) {
                return true;
            }
        }
        return false;
    }
    
    // Get location by coordinates
    public LocationSearchResult getLocationByCoordinates(String coordinates) {
        List<LocationSearchResult> locations = getSavedLocations();
        for (LocationSearchResult location : locations) {
            if (location.getCoordinateString().equals(coordinates)) {
                return location;
            }
        }
        return null;
    }
    
    // Clear all saved locations
    public void clearAllLocations() {
        preferences.edit().remove(KEY_SAVED_LOCATIONS).apply();
    }
    
    // Get locations count
    public int getLocationsCount() {
        return getSavedLocations().size();
    }
    
    // Check if has saved locations
    public boolean hasSavedLocations() {
        return getLocationsCount() > 0;
    }
    
    // Get location at index
    public LocationSearchResult getLocationAt(int index) {
        List<LocationSearchResult> locations = getSavedLocations();
        if (index >= 0 && index < locations.size()) {
            return locations.get(index);
        }
        return null;
    }
    
    // Move location to top (make it current)
    public void moveLocationToTop(LocationSearchResult location) {
        List<LocationSearchResult> locations = getSavedLocations();
        
        // Remove if exists
        locations.removeIf(savedLocation -> 
            savedLocation.getCoordinateString().equals(location.getCoordinateString()));
        
        // Add to top
        locations.add(0, location);
        
        // Keep only first 10 locations
        if (locations.size() > 10) {
            locations = locations.subList(0, 10);
        }
        
        saveLocationsList(locations);
        setCurrentLocation(location.getCoordinateString());
    }
    
    // Get recent locations (last 5)
    public List<LocationSearchResult> getRecentLocations() {
        List<LocationSearchResult> locations = getSavedLocations();
        if (locations.size() <= 5) {
            return locations;
        }
        return locations.subList(0, 5);
    }
    
    // Search in saved locations
    public List<LocationSearchResult> searchInSavedLocations(String query) {
        List<LocationSearchResult> locations = getSavedLocations();
        List<LocationSearchResult> results = new ArrayList<>();
        
        String lowerQuery = query.toLowerCase();
        for (LocationSearchResult location : locations) {
            if (location.getFullLocationName().toLowerCase().contains(lowerQuery)) {
                results.add(location);
            }
        }
        
        return results;
    }
}
