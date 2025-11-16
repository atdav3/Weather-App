package com.example.app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.app.models.WeatherData;
import com.example.app.network.ApiClient;
import com.example.app.network.WeatherApiService;
import com.example.app.services.LocationService;
import com.example.app.utils.WeatherAnimationUtils;
import com.example.app.utils.WeatherUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;
import com.google.android.material.appbar.MaterialToolbar;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherMapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationService.LocationListener {

    private GoogleMap mMap;
    private LocationService locationService;
    private WeatherApiService weatherApiService;
    
    // UI Components
    private TextView tvMapLocation;
    private TextView tvMapTemperature;
    private TextView tvMapHumidity;
    private TextView tvMapWind;
    private Button btnTemperatureLayer;
    private Button btnPrecipitationLayer;
    private Button btnWindLayer;
    
    // Map state
    private Marker weatherMarker;
    private TileOverlay currentOverlay;
    private String currentLayer = "temperature";
    private LatLng currentLocation;
    
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final LatLng DEFAULT_LOCATION = new LatLng(10.8231, 106.6297); // Ho Chi Minh City

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_map);
        
        // Initialize services
        locationService = new LocationService(this);
        locationService.setLocationListener(this);
        weatherApiService = ApiClient.getWeatherApiService();
        
        // Initialize UI
        initializeViews();
        setupClickListeners();
        
        // Setup map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            showError("Không thể khởi tạo bản đồ. Vui lòng kiểm tra Google Play Services.");
        }
    }

    private void initializeViews() {
        tvMapLocation = findViewById(R.id.tvMapLocation);
        tvMapTemperature = findViewById(R.id.tvMapTemperature);
        tvMapHumidity = findViewById(R.id.tvMapHumidity);
        tvMapWind = findViewById(R.id.tvMapWind);
        btnTemperatureLayer = findViewById(R.id.btnTemperatureLayer);
        btnPrecipitationLayer = findViewById(R.id.btnPrecipitationLayer);
        btnWindLayer = findViewById(R.id.btnWindLayer);
    }

    private void setupClickListeners() {
        // Toolbar navigation & menu
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
            toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_map_type) {
                    toggleMapType();
                    return true;
                }
                return false;
            });
        }
        
        // Current location button
        findViewById(R.id.btnCurrentLocation).setOnClickListener(v -> {
            WeatherAnimationUtils.bounceView(v);
            getCurrentLocation();
        });
        
        // Weather layer buttons
        btnTemperatureLayer.setOnClickListener(v -> {
            WeatherAnimationUtils.bounceView(v);
            setWeatherLayer("temperature");
        });
        
        btnPrecipitationLayer.setOnClickListener(v -> {
            WeatherAnimationUtils.bounceView(v);
            setWeatherLayer("precipitation");
        });
        
        btnWindLayer.setOnClickListener(v -> {
            WeatherAnimationUtils.bounceView(v);
            setWeatherLayer("wind");
        });
        // Long-click to show tips
        btnTemperatureLayer.setOnLongClickListener(v -> { showError(getString(R.string.temperature)); return true; });
        btnPrecipitationLayer.setOnLongClickListener(v -> { showError(getString(R.string.precipitation)); return true; });
        btnWindLayer.setOnLongClickListener(v -> { showError(getString(R.string.wind_speed)); return true; });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        
        try {
            // Enable user location
            if (checkLocationPermission()) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false); // Hide default button
            }
            
            // Set default location
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 10));
            
            // Add weather marker
            weatherMarker = mMap.addMarker(new MarkerOptions()
                    .position(DEFAULT_LOCATION)
                    .title("Weather Info"));
            
            // Set map click listener
            mMap.setOnMapClickListener(latLng -> {
                currentLocation = latLng;
                weatherMarker.setPosition(latLng);
                fetchWeatherForLocation(latLng);
            });
            
            // Add temperature overlay by default
            setWeatherLayer("temperature");
            
            // Get current location if permission granted
            if (checkLocationPermission()) {
                getCurrentLocation();
            }
            
            // Show success message
            Toast.makeText(this, "Bản đồ đã sẵn sàng", Toast.LENGTH_SHORT).show();
            
        } catch (SecurityException e) {
            showError("Lỗi quyền truy cập vị trí: " + e.getMessage());
        } catch (Exception e) {
            showError("Lỗi khởi tạo bản đồ: " + e.getMessage());
        }
    }

    private void getCurrentLocation() {
        if (checkLocationPermission()) {
            locationService.getCurrentLocation();
        } else {
            requestLocationPermission();
        }
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void toggleMapType() {
        if (mMap != null) {
            int currentType = mMap.getMapType();
            if (currentType == GoogleMap.MAP_TYPE_NORMAL) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            } else {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        }
    }

    private void setWeatherLayer(String layer) {
        currentLayer = layer;
        
        // Remove current overlay
        if (currentOverlay != null) {
            currentOverlay.remove();
        }
        
        // Add new overlay based on layer type
        switch (layer) {
            case "temperature":
                addTemperatureOverlay();
                break;
            case "precipitation":
                addPrecipitationOverlay();
                break;
            case "wind":
                addWindOverlay();
                break;
        }
        
        // Update button states
        updateLayerButtonStates();
    }

    private void addTemperatureOverlay() {
        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {
                try {
                    // Weatherbit nhiệt độ lớp temp2m
                    return new URL(String.format(Locale.US,
                            "https://maps.weatherbit.io/v2.0/singleband/temp2m/latest/%d/%d/%d.png?key=%s",
                            zoom, x, y, ApiClient.WEATHERBIT_API_KEY));
                } catch (MalformedURLException e) {
                    return null;
                }
            }
        };
        currentOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
    }

    private void addPrecipitationOverlay() {
        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {
                try {
                    // Weatherbit precipitation lớp catprecipdbz
                    return new URL(String.format(Locale.US,
                            "https://maps.weatherbit.io/v2.0/singleband/catprecipdbz/latest/%d/%d/%d.png?key=%s",
                            zoom, x, y, ApiClient.WEATHERBIT_API_KEY));
                } catch (MalformedURLException e) {
                    return null;
                }
            }
        };
        currentOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
    }

    private void addWindOverlay() {
        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {
                try {

                    // Weatherbit wind lớp windspd10m
                    return new URL(String.format(Locale.US,
                            "https://maps.weatherbit.io/v2.0/singleband/windspd10m/latest/%d/%d/%d.png?key=%s",
                            zoom, x, y, ApiClient.WEATHERBIT_API_KEY));
                } catch (MalformedURLException e) {
                    return null;
                }
            }
        };
        currentOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
    }

    private void updateLayerButtonStates() {
        // Reset all buttons to default state
        btnTemperatureLayer.setSelected(false);
        btnPrecipitationLayer.setSelected(false);
        btnWindLayer.setSelected(false);
        
        // Highlight active button
        switch (currentLayer) {
            case "temperature":
                btnTemperatureLayer.setSelected(true);
                break;
            case "precipitation":
                btnPrecipitationLayer.setSelected(true);
                break;
            case "wind":
                btnWindLayer.setSelected(true);
                break;
        }
    }

    private void fetchWeatherForLocation(LatLng location) {
        String coordinates = location.latitude + "," + location.longitude;
        
        Call<WeatherData> call = weatherApiService.getWeatherForecast(
                ApiClient.API_KEY,
                coordinates,
                1, // 1 day forecast
                "no", // No air quality data
                "no"  // No weather alerts
        );

        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(@NonNull Call<WeatherData> call, @NonNull Response<WeatherData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateWeatherInfo(response.body());
                } else {
                    showError("Failed to load weather data");
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherData> call, @NonNull Throwable t) {
                showError("Network error: " + t.getMessage());
            }
        });
    }

    private void updateWeatherInfo(WeatherData weatherData) {
        WeatherData.Current current = weatherData.getCurrent();
        WeatherData.Location location = weatherData.getLocation();
        
        // Update location name
        String locationText = location.getName() + ", " + location.getCountry();
        tvMapLocation.setText(locationText);
        
        // Update weather info
        tvMapTemperature.setText(WeatherUtils.formatTemperature(current.getTempC()));
        tvMapHumidity.setText(WeatherUtils.formatHumidity(current.getHumidity()));
        tvMapWind.setText(WeatherUtils.formatWindSpeed(current.getWindKph(), true));
        
        // Animate updates
        WeatherAnimationUtils.fadeIn(tvMapLocation);
        WeatherAnimationUtils.fadeIn(tvMapTemperature);
        WeatherAnimationUtils.fadeIn(tvMapHumidity);
        WeatherAnimationUtils.fadeIn(tvMapWind);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // LocationService.LocationListener implementation
    @Override
    public void onLocationReceived(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        currentLocation = latLng;
        
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            weatherMarker.setPosition(latLng);
            fetchWeatherForLocation(latLng);
        }
    }

    @Override
    public void onLocationError(String error) {
        showError("Location error: " + error);
    }

    @Override
    public void onPermissionDenied() {
        showError("Location permission denied");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                showError("Location permission required for map features");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationService != null) {
            locationService.cleanup();
        }
    }
}
