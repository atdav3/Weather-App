package com.example.app.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    // WeatherAPI.com
    private static final String BASE_URL = "https://api.weatherapi.com/v1/";
    //  https://www.weatherapi.com/
    public static final String API_KEY = "e138943e6b464e0996a152759250908";
    //  https://www.weatherbit.io/
    public static final String WEATHERBIT_API_KEY = "7086192585214de8ab5bb31307dd8d3b";
    private static Retrofit retrofit = null;
    private static WeatherApiService weatherApiService = null;

    /**
     * Get Retrofit instance
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            
            // Create logging interceptor for debugging
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            
            // Create OkHttp client with interceptors
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();
            
            // Create Retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /**
     * Get WeatherApiService instance
     */
    public static WeatherApiService getWeatherApiService() {
        if (weatherApiService == null) {
            weatherApiService = getRetrofitInstance().create(WeatherApiService.class);
        }
        return weatherApiService;
    }

    /**
     * Check if API key is configured
     */
    public static boolean isApiKeyConfigured() {
        return !API_KEY.equals("e138943e6b464e0996a152759250908") && !API_KEY.isEmpty();
    }
}
