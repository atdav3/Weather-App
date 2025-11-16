package com.example.app.network;

import com.example.app.models.WeatherData;
import com.example.app.models.LocationSearchResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    
 // API key at https://www.weatherapi.com/
    @GET("forecast.json")
    Call<WeatherData> getWeatherForecast(
            @Query("key") String apiKey,
            @Query("q") String location,
            @Query("days") int days,
            @Query("aqi") String aqi,
            @Query("alerts") String alerts
    );

    /**
     * Get current weather data only
     */
    @GET("current.json")
    Call<WeatherData> getCurrentWeather(
            @Query("key") String apiKey,
            @Query("q") String location,
            @Query("aqi") String aqi
    );

    /**
     * Search for locations
     */
    @GET("search.json")
    Call<LocationSearchResult[]> searchLocations(
            @Query("key") String apiKey,
            @Query("q") String query
    );
}
