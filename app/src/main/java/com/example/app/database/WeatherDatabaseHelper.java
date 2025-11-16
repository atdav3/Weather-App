package com.example.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.app.models.SearchHistory;
import com.example.app.models.UserPreferences;
import com.example.app.models.WeatherViewHistory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherDatabaseHelper extends SQLiteOpenHelper {
    private static volatile WeatherDatabaseHelper instance;
    
    private static final String DATABASE_NAME = "weather_app.db";
    private static final int DATABASE_VERSION = 2; // bump for constraints, indices, FKs, triggers

    // Table names
    private static final String TABLE_SEARCH_HISTORY = "search_history";
    private static final String TABLE_USER_PREFERENCES = "user_preferences";
    private static final String TABLE_WEATHER_VIEW_HISTORY = "weather_view_history";
    private static final String TABLE_FAVORITE_LOCATIONS = "favorite_locations";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // Search History Table columns
    private static final String KEY_LOCATION_NAME = "location_name";
    private static final String KEY_REGION = "region";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_SEARCH_DATE = "search_date";
    private static final String KEY_SEARCH_COUNT = "search_count";
    private static final String KEY_IS_FAVORITE = "is_favorite";

    // User Preferences Table columns
    private static final String KEY_TEMPERATURE_UNIT = "temperature_unit";
    private static final String KEY_WIND_SPEED_UNIT = "wind_speed_unit";
    private static final String KEY_PRESSURE_UNIT = "pressure_unit";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_NOTIFICATIONS_ENABLED = "notifications_enabled";
    private static final String KEY_AUTO_LOCATION_ENABLED = "auto_location_enabled";
    private static final String KEY_REFRESH_INTERVAL = "refresh_interval";
    private static final String KEY_THEME = "theme";
    private static final String KEY_SHOW_HUMIDITY = "show_humidity";
    private static final String KEY_SHOW_WIND = "show_wind";
    private static final String KEY_SHOW_PRESSURE = "show_pressure";
    private static final String KEY_SHOW_UV = "show_uv";

    // Weather View History Table columns
    private static final String KEY_LOCATION_ID = "location_id";
    private static final String KEY_VIEW_DATE = "view_date";
    private static final String KEY_WEATHER_CONDITION = "weather_condition";
    private static final String KEY_TEMPERATURE = "temperature";
    private static final String KEY_TEMPERATURE_UNIT_HISTORY = "temperature_unit";
    private static final String KEY_HUMIDITY = "humidity";
    private static final String KEY_WIND_SPEED = "wind_speed";
    private static final String KEY_WIND_SPEED_UNIT_HISTORY = "wind_speed_unit";
    private static final String KEY_VIEW_TYPE = "view_type";

    // Create table statements
    private static final String CREATE_TABLE_SEARCH_HISTORY = "CREATE TABLE IF NOT EXISTS " + TABLE_SEARCH_HISTORY + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_LOCATION_NAME + " TEXT NOT NULL,"
            + KEY_REGION + " TEXT,"
            + KEY_COUNTRY + " TEXT,"
            + KEY_LATITUDE + " REAL NOT NULL,"
            + KEY_LONGITUDE + " REAL NOT NULL,"
            + KEY_SEARCH_DATE + " INTEGER NOT NULL,"
            + KEY_SEARCH_COUNT + " INTEGER NOT NULL DEFAULT 1,"
            + KEY_IS_FAVORITE + " INTEGER NOT NULL DEFAULT 0,"
            + KEY_CREATED_AT + " INTEGER NOT NULL DEFAULT (strftime('%s','now')*1000),"
            + "UNIQUE(" + KEY_LOCATION_NAME + ", " + KEY_REGION + ", " + KEY_COUNTRY + ") ON CONFLICT IGNORE"
            + ")";

    private static final String CREATE_TABLE_USER_PREFERENCES = "CREATE TABLE IF NOT EXISTS " + TABLE_USER_PREFERENCES + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TEMPERATURE_UNIT + " TEXT NOT NULL DEFAULT 'celsius',"
            + KEY_WIND_SPEED_UNIT + " TEXT NOT NULL DEFAULT 'kmh',"
            + KEY_PRESSURE_UNIT + " TEXT NOT NULL DEFAULT 'mb',"
            + KEY_LANGUAGE + " TEXT NOT NULL DEFAULT 'vi',"
            + KEY_NOTIFICATIONS_ENABLED + " INTEGER NOT NULL DEFAULT 1,"
            + KEY_AUTO_LOCATION_ENABLED + " INTEGER NOT NULL DEFAULT 1,"
            + KEY_REFRESH_INTERVAL + " INTEGER NOT NULL DEFAULT 30,"
            + KEY_THEME + " TEXT NOT NULL DEFAULT 'auto',"
            + KEY_SHOW_HUMIDITY + " INTEGER NOT NULL DEFAULT 1,"
            + KEY_SHOW_WIND + " INTEGER NOT NULL DEFAULT 1,"
            + KEY_SHOW_PRESSURE + " INTEGER NOT NULL DEFAULT 1,"
            + KEY_SHOW_UV + " INTEGER NOT NULL DEFAULT 1,"
            + KEY_CREATED_AT + " INTEGER NOT NULL DEFAULT (strftime('%s','now')*1000)"
            + ")";

    private static final String CREATE_TABLE_WEATHER_VIEW_HISTORY = "CREATE TABLE IF NOT EXISTS " + TABLE_WEATHER_VIEW_HISTORY + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_LOCATION_ID + " INTEGER,"
            + KEY_LOCATION_NAME + " TEXT NOT NULL,"
            + KEY_VIEW_DATE + " INTEGER NOT NULL,"
            + KEY_WEATHER_CONDITION + " TEXT,"
            + KEY_TEMPERATURE + " REAL,"
            + KEY_TEMPERATURE_UNIT_HISTORY + " TEXT,"
            + KEY_HUMIDITY + " INTEGER,"
            + KEY_WIND_SPEED + " REAL,"
            + KEY_WIND_SPEED_UNIT_HISTORY + " TEXT,"
            + KEY_VIEW_TYPE + " TEXT,"
            + KEY_CREATED_AT + " INTEGER NOT NULL DEFAULT (strftime('%s','now')*1000)"
            + ")";

    private static final String CREATE_INDEXES =
            "CREATE INDEX IF NOT EXISTS idx_search_history_date ON " + TABLE_SEARCH_HISTORY + "(" + KEY_SEARCH_DATE + ");" +
            "CREATE INDEX IF NOT EXISTS idx_search_history_name ON " + TABLE_SEARCH_HISTORY + "(" + KEY_LOCATION_NAME + ");" +
            "CREATE INDEX IF NOT EXISTS idx_weather_view_date ON " + TABLE_WEATHER_VIEW_HISTORY + "(" + KEY_VIEW_DATE + ");" +
            "CREATE INDEX IF NOT EXISTS idx_weather_view_location ON " + TABLE_WEATHER_VIEW_HISTORY + "(" + KEY_LOCATION_NAME + ");";

    private WeatherDatabaseHelper(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static WeatherDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (WeatherDatabaseHelper.class) {
                if (instance == null) {
                    instance = new WeatherDatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SEARCH_HISTORY);
        db.execSQL(CREATE_TABLE_USER_PREFERENCES);
        db.execSQL(CREATE_TABLE_WEATHER_VIEW_HISTORY);
        db.execSQL(CREATE_INDEXES);
        
        // Insert default user preferences
        insertDefaultUserPreferences(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Ensure NOT NULL/defaults via adding columns where needed
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_search_history_date ON " + TABLE_SEARCH_HISTORY + "(" + KEY_SEARCH_DATE + ")");
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_search_history_name ON " + TABLE_SEARCH_HISTORY + "(" + KEY_LOCATION_NAME + ")");
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_weather_view_date ON " + TABLE_WEATHER_VIEW_HISTORY + "(" + KEY_VIEW_DATE + ")");
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_weather_view_location ON " + TABLE_WEATHER_VIEW_HISTORY + "(" + KEY_LOCATION_NAME + ")");
            // backfill created_at if missing
            db.execSQL("UPDATE " + TABLE_SEARCH_HISTORY + " SET " + KEY_CREATED_AT + " = COALESCE(" + KEY_CREATED_AT + ", strftime('%s','now')*1000)");
            db.execSQL("UPDATE " + TABLE_USER_PREFERENCES + " SET " + KEY_CREATED_AT + " = COALESCE(" + KEY_CREATED_AT + ", strftime('%s','now')*1000)");
            db.execSQL("UPDATE " + TABLE_WEATHER_VIEW_HISTORY + " SET " + KEY_CREATED_AT + " = COALESCE(" + KEY_CREATED_AT + ", strftime('%s','now')*1000)");
        }
    }

    private void insertDefaultUserPreferences(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(KEY_TEMPERATURE_UNIT, "celsius");
        values.put(KEY_WIND_SPEED_UNIT, "kmh");
        values.put(KEY_PRESSURE_UNIT, "mb");
        values.put(KEY_LANGUAGE, "vi");
        values.put(KEY_NOTIFICATIONS_ENABLED, 1);
        values.put(KEY_AUTO_LOCATION_ENABLED, 1);
        values.put(KEY_REFRESH_INTERVAL, 30);
        values.put(KEY_THEME, "auto");
        values.put(KEY_SHOW_HUMIDITY, 1);
        values.put(KEY_SHOW_WIND, 1);
        values.put(KEY_SHOW_PRESSURE, 1);
        values.put(KEY_SHOW_UV, 1);
        values.put(KEY_CREATED_AT, System.currentTimeMillis());
        
        db.insert(TABLE_USER_PREFERENCES, null, values);
    }

    // ==================== SEARCH HISTORY METHODS ====================

    public long addSearchHistory(SearchHistory searchHistory) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_LOCATION_NAME, searchHistory.getLocationName());
        values.put(KEY_REGION, searchHistory.getRegion());
        values.put(KEY_COUNTRY, searchHistory.getCountry());
        values.put(KEY_LATITUDE, searchHistory.getLatitude());
        values.put(KEY_LONGITUDE, searchHistory.getLongitude());
        values.put(KEY_SEARCH_DATE, searchHistory.getSearchDate().getTime());
        values.put(KEY_SEARCH_COUNT, searchHistory.getSearchCount());
        values.put(KEY_IS_FAVORITE, searchHistory.isFavorite() ? 1 : 0);
        values.put(KEY_CREATED_AT, System.currentTimeMillis());

        long id = db.insert(TABLE_SEARCH_HISTORY, null, values);
        db.close();
        return id;
    }

    public SearchHistory getSearchHistory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(TABLE_SEARCH_HISTORY, null, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            SearchHistory searchHistory = cursorToSearchHistory(cursor);
            cursor.close();
            db.close();
            return searchHistory;
        }
        
        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    public List<SearchHistory> getAllSearchHistory() {
        List<SearchHistory> searchHistoryList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SEARCH_HISTORY + " ORDER BY " + KEY_SEARCH_DATE + " DESC";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                SearchHistory searchHistory = cursorToSearchHistory(cursor);
                searchHistoryList.add(searchHistory);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return searchHistoryList;
    }

    public List<SearchHistory> getFavoriteLocations() {
        List<SearchHistory> favoriteList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SEARCH_HISTORY + 
                           " WHERE " + KEY_IS_FAVORITE + "=1 ORDER BY " + KEY_LOCATION_NAME;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                SearchHistory searchHistory = cursorToSearchHistory(cursor);
                favoriteList.add(searchHistory);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return favoriteList;
    }

    public int updateSearchHistory(SearchHistory searchHistory) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_LOCATION_NAME, searchHistory.getLocationName());
        values.put(KEY_REGION, searchHistory.getRegion());
        values.put(KEY_COUNTRY, searchHistory.getCountry());
        values.put(KEY_LATITUDE, searchHistory.getLatitude());
        values.put(KEY_LONGITUDE, searchHistory.getLongitude());
        values.put(KEY_SEARCH_DATE, searchHistory.getSearchDate().getTime());
        values.put(KEY_SEARCH_COUNT, searchHistory.getSearchCount());
        values.put(KEY_IS_FAVORITE, searchHistory.isFavorite() ? 1 : 0);

        int result = db.update(TABLE_SEARCH_HISTORY, values, KEY_ID + "=?",
                new String[]{String.valueOf(searchHistory.getId())});
        db.close();
        return result;
    }

    public void deleteSearchHistory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SEARCH_HISTORY, KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void clearSearchHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SEARCH_HISTORY, null, null);
        db.close();
    }

    private SearchHistory cursorToSearchHistory(Cursor cursor) {
        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        searchHistory.setLocationName(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_NAME)));
        searchHistory.setRegion(cursor.getString(cursor.getColumnIndex(KEY_REGION)));
        searchHistory.setCountry(cursor.getString(cursor.getColumnIndex(KEY_COUNTRY)));
        searchHistory.setLatitude(cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)));
        searchHistory.setLongitude(cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE)));
        searchHistory.setSearchDate(new Date(cursor.getLong(cursor.getColumnIndex(KEY_SEARCH_DATE))));
        searchHistory.setSearchCount(cursor.getInt(cursor.getColumnIndex(KEY_SEARCH_COUNT)));
        searchHistory.setFavorite(cursor.getInt(cursor.getColumnIndex(KEY_IS_FAVORITE)) == 1);
        return searchHistory;
    }

    // ==================== USER PREFERENCES METHODS ====================

    public long addUserPreferences(UserPreferences preferences) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_TEMPERATURE_UNIT, preferences.getTemperatureUnit());
        values.put(KEY_WIND_SPEED_UNIT, preferences.getWindSpeedUnit());
        values.put(KEY_PRESSURE_UNIT, preferences.getPressureUnit());
        values.put(KEY_LANGUAGE, preferences.getLanguage());
        values.put(KEY_NOTIFICATIONS_ENABLED, preferences.isNotificationsEnabled() ? 1 : 0);
        values.put(KEY_AUTO_LOCATION_ENABLED, preferences.isAutoLocationEnabled() ? 1 : 0);
        values.put(KEY_REFRESH_INTERVAL, preferences.getRefreshInterval());
        values.put(KEY_THEME, preferences.getTheme());
        values.put(KEY_SHOW_HUMIDITY, preferences.isShowHumidity() ? 1 : 0);
        values.put(KEY_SHOW_WIND, preferences.isShowWind() ? 1 : 0);
        values.put(KEY_SHOW_PRESSURE, preferences.isShowPressure() ? 1 : 0);
        values.put(KEY_SHOW_UV, preferences.isShowUV() ? 1 : 0);
        values.put(KEY_CREATED_AT, System.currentTimeMillis());

        long id = db.insert(TABLE_USER_PREFERENCES, null, values);
        db.close();
        return id;
    }

    public UserPreferences getUserPreferences() {
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(TABLE_USER_PREFERENCES, null, null, null, null, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            UserPreferences preferences = cursorToUserPreferences(cursor);
            cursor.close();
            db.close();
            return preferences;
        }
        
        if (cursor != null) cursor.close();
        db.close();
        return new UserPreferences(); // Return default preferences
    }

    public int updateUserPreferences(UserPreferences preferences) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_TEMPERATURE_UNIT, preferences.getTemperatureUnit());
        values.put(KEY_WIND_SPEED_UNIT, preferences.getWindSpeedUnit());
        values.put(KEY_PRESSURE_UNIT, preferences.getPressureUnit());
        values.put(KEY_LANGUAGE, preferences.getLanguage());
        values.put(KEY_NOTIFICATIONS_ENABLED, preferences.isNotificationsEnabled() ? 1 : 0);
        values.put(KEY_AUTO_LOCATION_ENABLED, preferences.isAutoLocationEnabled() ? 1 : 0);
        values.put(KEY_REFRESH_INTERVAL, preferences.getRefreshInterval());
        values.put(KEY_THEME, preferences.getTheme());
        values.put(KEY_SHOW_HUMIDITY, preferences.isShowHumidity() ? 1 : 0);
        values.put(KEY_SHOW_WIND, preferences.isShowWind() ? 1 : 0);
        values.put(KEY_SHOW_PRESSURE, preferences.isShowPressure() ? 1 : 0);
        values.put(KEY_SHOW_UV, preferences.isShowUV() ? 1 : 0);

        int result = db.update(TABLE_USER_PREFERENCES, values, KEY_ID + "=?",
                new String[]{String.valueOf(preferences.getId())});
        db.close();
        return result;
    }

    private UserPreferences cursorToUserPreferences(Cursor cursor) {
        UserPreferences preferences = new UserPreferences();
        preferences.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        preferences.setTemperatureUnit(cursor.getString(cursor.getColumnIndex(KEY_TEMPERATURE_UNIT)));
        preferences.setWindSpeedUnit(cursor.getString(cursor.getColumnIndex(KEY_WIND_SPEED_UNIT)));
        preferences.setPressureUnit(cursor.getString(cursor.getColumnIndex(KEY_PRESSURE_UNIT)));
        preferences.setLanguage(cursor.getString(cursor.getColumnIndex(KEY_LANGUAGE)));
        preferences.setNotificationsEnabled(cursor.getInt(cursor.getColumnIndex(KEY_NOTIFICATIONS_ENABLED)) == 1);
        preferences.setAutoLocationEnabled(cursor.getInt(cursor.getColumnIndex(KEY_AUTO_LOCATION_ENABLED)) == 1);
        preferences.setRefreshInterval(cursor.getInt(cursor.getColumnIndex(KEY_REFRESH_INTERVAL)));
        preferences.setTheme(cursor.getString(cursor.getColumnIndex(KEY_THEME)));
        preferences.setShowHumidity(cursor.getInt(cursor.getColumnIndex(KEY_SHOW_HUMIDITY)) == 1);
        preferences.setShowWind(cursor.getInt(cursor.getColumnIndex(KEY_SHOW_WIND)) == 1);
        preferences.setShowPressure(cursor.getInt(cursor.getColumnIndex(KEY_SHOW_PRESSURE)) == 1);
        preferences.setShowUV(cursor.getInt(cursor.getColumnIndex(KEY_SHOW_UV)) == 1);
        return preferences;
    }

    // ==================== WEATHER VIEW HISTORY METHODS ====================

    public long addWeatherViewHistory(WeatherViewHistory viewHistory) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_LOCATION_ID, viewHistory.getLocationId());
        values.put(KEY_LOCATION_NAME, viewHistory.getLocationName());
        values.put(KEY_VIEW_DATE, viewHistory.getViewDate().getTime());
        values.put(KEY_WEATHER_CONDITION, viewHistory.getWeatherCondition());
        values.put(KEY_TEMPERATURE, viewHistory.getTemperature());
        values.put(KEY_TEMPERATURE_UNIT_HISTORY, viewHistory.getTemperatureUnit());
        values.put(KEY_HUMIDITY, viewHistory.getHumidity());
        values.put(KEY_WIND_SPEED, viewHistory.getWindSpeed());
        values.put(KEY_WIND_SPEED_UNIT_HISTORY, viewHistory.getWindSpeedUnit());
        values.put(KEY_VIEW_TYPE, viewHistory.getViewType());
        values.put(KEY_CREATED_AT, System.currentTimeMillis());

        long id = db.insert(TABLE_WEATHER_VIEW_HISTORY, null, values);
        db.close();
        return id;
    }

    public List<WeatherViewHistory> getWeatherViewHistory(int limit) {
        List<WeatherViewHistory> viewHistoryList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_WEATHER_VIEW_HISTORY + 
                           " ORDER BY " + KEY_VIEW_DATE + " DESC LIMIT " + limit;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                WeatherViewHistory viewHistory = cursorToWeatherViewHistory(cursor);
                viewHistoryList.add(viewHistory);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return viewHistoryList;
    }

    public void clearWeatherViewHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WEATHER_VIEW_HISTORY, null, null);
        db.close();
    }

    private WeatherViewHistory cursorToWeatherViewHistory(Cursor cursor) {
        WeatherViewHistory viewHistory = new WeatherViewHistory();
        viewHistory.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        viewHistory.setLocationId(cursor.getInt(cursor.getColumnIndex(KEY_LOCATION_ID)));
        viewHistory.setLocationName(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_NAME)));
        viewHistory.setViewDate(new Date(cursor.getLong(cursor.getColumnIndex(KEY_VIEW_DATE))));
        viewHistory.setWeatherCondition(cursor.getString(cursor.getColumnIndex(KEY_WEATHER_CONDITION)));
        viewHistory.setTemperature(cursor.getDouble(cursor.getColumnIndex(KEY_TEMPERATURE)));
        viewHistory.setTemperatureUnit(cursor.getString(cursor.getColumnIndex(KEY_TEMPERATURE_UNIT_HISTORY)));
        viewHistory.setHumidity(cursor.getInt(cursor.getColumnIndex(KEY_HUMIDITY)));
        viewHistory.setWindSpeed(cursor.getDouble(cursor.getColumnIndex(KEY_WIND_SPEED)));
        viewHistory.setWindSpeedUnit(cursor.getString(cursor.getColumnIndex(KEY_WIND_SPEED_UNIT_HISTORY)));
        viewHistory.setViewType(cursor.getString(cursor.getColumnIndex(KEY_VIEW_TYPE)));
        return viewHistory;
    }
}
