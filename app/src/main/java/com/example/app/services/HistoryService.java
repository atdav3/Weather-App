package com.example.app.services;

import android.content.Context;
import android.util.Log;

import com.example.app.database.WeatherDatabaseHelper;
import com.example.app.models.SearchHistory;
import com.example.app.models.UserPreferences;
import com.example.app.models.WeatherViewHistory;

import java.util.List;

public class HistoryService {
    private static final String TAG = "HistoryService";
    private WeatherDatabaseHelper databaseHelper;
    private Context context;

    public HistoryService(Context context) {
        this.context = context.getApplicationContext();
        this.databaseHelper = WeatherDatabaseHelper.getInstance(this.context);
    }

    // Thêm hoặc cập nhật lịch sử tìm kiếm
    public long addOrUpdateSearchHistory(String locationName, String region, String country,
                                       double latitude, double longitude) {
        try {
            // Kiểm tra tồn tại
            SearchHistory existingHistory = findSearchHistoryByLocation(locationName, region, country);
            
            if (existingHistory != null) {
                // Cập nhật
                existingHistory.incrementSearchCount();
                databaseHelper.updateSearchHistory(existingHistory);
                return existingHistory.getId();
            } else {
                // Tạo mới
                SearchHistory newHistory = new SearchHistory(locationName, region, country, latitude, longitude);
                return databaseHelper.addSearchHistory(newHistory);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error adding/updating search history: " + e.getMessage());
            return -1;
        }
    }
    // Tìm theo địa điểm
    private SearchHistory findSearchHistoryByLocation(String locationName, String region, String country) {
        List<SearchHistory> allHistory = databaseHelper.getAllSearchHistory();
        for (SearchHistory history : allHistory) {
            if (history.getLocationName().equals(locationName) &&
                history.getRegion().equals(region) &&
                history.getCountry().equals(country)) {
                return history;
            }
        }
        return null;
    }

    /**
     * Lấy tất cả lịch sử tìm kiếm
     */
    public List<SearchHistory> getAllSearchHistory() {
        try {
            return databaseHelper.getAllSearchHistory();
        } catch (Exception e) {
            Log.e(TAG, "Error getting all search history: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lấy danh sách địa điểm yêu thích
     */
    public List<SearchHistory> getFavoriteLocations() {
        try {
            return databaseHelper.getFavoriteLocations();
        } catch (Exception e) {
            Log.e(TAG, "Error getting favorite locations: " + e.getMessage());
            return null;
        }
    }

    /**
     * Thêm/xóa địa điểm khỏi yêu thích
     */
    public boolean toggleFavorite(int historyId) {
        try {
            SearchHistory history = databaseHelper.getSearchHistory(historyId);
            if (history != null) {
                history.setFavorite(!history.isFavorite());
                databaseHelper.updateSearchHistory(history);
                return true;
            }
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Error toggling favorite: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cập nhật lịch sử tìm kiếm
     */
    public boolean updateSearchHistory(SearchHistory searchHistory) {
        try {
            int result = databaseHelper.updateSearchHistory(searchHistory);
            return result > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error updating search history: " + e.getMessage());
            return false;
        }
    }

    /**
     * Xóa lịch sử tìm kiếm
     */
    public boolean deleteSearchHistory(int historyId) {
        try {
            databaseHelper.deleteSearchHistory(historyId);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error deleting search history: " + e.getMessage());
            return false;
        }
    }

    /**
     * Xóa tất cả lịch sử tìm kiếm
     */
    public boolean clearSearchHistory() {
        try {
            databaseHelper.clearSearchHistory();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error clearing search history: " + e.getMessage());
            return false;
        }
    }

    // ==================== USER PREFERENCES METHODS ====================

    /**
     * Lấy cài đặt người dùng
     */
    public UserPreferences getUserPreferences() {
        try {
            return databaseHelper.getUserPreferences();
        } catch (Exception e) {
            Log.e(TAG, "Error getting user preferences: " + e.getMessage());
            return new UserPreferences(); // Trả về cài đặt mặc định
        }
    }

    /**
     * Cập nhật cài đặt người dùng
     */
    public boolean updateUserPreferences(UserPreferences preferences) {
        try {
            int result = databaseHelper.updateUserPreferences(preferences);
            return result > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error updating user preferences: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cập nhật đơn vị nhiệt độ
     */
    public boolean updateTemperatureUnit(String unit) {
        try {
            UserPreferences preferences = getUserPreferences();
            preferences.setTemperatureUnit(unit);
            return updateUserPreferences(preferences);
        } catch (Exception e) {
            Log.e(TAG, "Error updating temperature unit: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cập nhật đơn vị tốc độ gió
     */
    public boolean updateWindSpeedUnit(String unit) {
        try {
            UserPreferences preferences = getUserPreferences();
            preferences.setWindSpeedUnit(unit);
            return updateUserPreferences(preferences);
        } catch (Exception e) {
            Log.e(TAG, "Error updating wind speed unit: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cập nhật ngôn ngữ
     */
    public boolean updateLanguage(String language) {
        try {
            UserPreferences preferences = getUserPreferences();
            preferences.setLanguage(language);
            return updateUserPreferences(preferences);
        } catch (Exception e) {
            Log.e(TAG, "Error updating language: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cập nhật cài đặt thông báo
     */
    public boolean updateNotificationSettings(boolean enabled) {
        try {
            UserPreferences preferences = getUserPreferences();
            preferences.setNotificationsEnabled(enabled);
            return updateUserPreferences(preferences);
        } catch (Exception e) {
            Log.e(TAG, "Error updating notification settings: " + e.getMessage());
            return false;
        }
    }

    // ==================== WEATHER VIEW HISTORY METHODS ====================

    /**
     * Thêm lịch sử xem thời tiết
     */
    public long addWeatherViewHistory(int locationId, String locationName, String weatherCondition,
                                    double temperature, String temperatureUnit, int humidity,
                                    double windSpeed, String windSpeedUnit, String viewType) {
        try {
            WeatherViewHistory viewHistory = new WeatherViewHistory(
                locationId, locationName, weatherCondition, temperature, temperatureUnit,
                humidity, windSpeed, windSpeedUnit, viewType
            );
            return databaseHelper.addWeatherViewHistory(viewHistory);
        } catch (Exception e) {
            Log.e(TAG, "Error adding weather view history: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Lấy lịch sử xem thời tiết gần đây
     */
    public List<WeatherViewHistory> getRecentWeatherViews(int limit) {
        try {
            return databaseHelper.getWeatherViewHistory(limit);
        } catch (Exception e) {
            Log.e(TAG, "Error getting recent weather views: " + e.getMessage());
            return null;
        }
    }

    /**
     * Xóa lịch sử xem thời tiết
     */
    public boolean clearWeatherViewHistory() {
        try {
            databaseHelper.clearWeatherViewHistory();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error clearing weather view history: " + e.getMessage());
            return false;
        }
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Kiểm tra xem địa điểm có trong yêu thích không
     */
    public boolean isLocationFavorite(String locationName, String region, String country) {
        List<SearchHistory> favorites = getFavoriteLocations();
        if (favorites != null) {
            for (SearchHistory favorite : favorites) {
                if (favorite.getLocationName().equals(locationName) &&
                    favorite.getRegion().equals(region) &&
                    favorite.getCountry().equals(country)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Lấy số lượng địa điểm yêu thích
     */
    public int getFavoriteLocationsCount() {
        List<SearchHistory> favorites = getFavoriteLocations();
        return favorites != null ? favorites.size() : 0;
    }

    /**
     * Lấy số lượng lịch sử tìm kiếm
     */
    public int getSearchHistoryCount() {
        List<SearchHistory> history = getAllSearchHistory();
        return history != null ? history.size() : 0;
    }

    /**
     * Đóng database
     */
    public void close() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
