package com.example.app.utils;

import android.util.Log;
import com.example.app.models.WeatherData;
import com.example.app.network.ApiClient;
import com.example.app.network.WeatherApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiTester {
    private static final String TAG = "ApiTester";
    
    public static void testApiKey() {
        Log.d(TAG, "Testing API key: " + ApiClient.API_KEY);
        Log.d(TAG, "API key configured: " + ApiClient.isApiKeyConfigured());
        
        WeatherApiService service = ApiClient.getWeatherApiService();
        Call<WeatherData> call = service.getCurrentWeather(
            ApiClient.API_KEY,
            "London",
            "no"
        );
        
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                Log.d(TAG, "API Test Response Code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "API Test SUCCESS!");
                    Log.d(TAG, "Location: " + response.body().getLocation().getName());
                    Log.d(TAG, "Temperature: " + response.body().getCurrent().getTempC() + "°C");
                } else {
                    Log.e(TAG, "API Test FAILED - Response not successful");
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                        } catch (Exception e) {
                            Log.e(TAG, "Error reading error body", e);
                        }
                    }
                }
            }
            
            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Log.e(TAG, "API Test NETWORK FAILURE", t);
            }
        });
    }
}
