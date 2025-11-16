package com.example.app.services;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class LocationService {
    
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final long UPDATE_INTERVAL = 10000; // 10 seconds
    private static final long FASTEST_INTERVAL = 5000; // 5 seconds
    
    private Context context;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationListener locationListener;
    
    public interface LocationListener {
        void onLocationReceived(Location location);
        void onLocationError(String error);
        void onPermissionDenied();
    }

    public LocationService(Context context) {
        this.context = context;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    /**
     * Set location listener
     */
    public void setLocationListener(LocationListener listener) {
        this.locationListener = listener;
    }

    /**
     * Check if location permissions are granted
     */
    public boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) 
                == PackageManager.PERMISSION_GRANTED ||
               ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) 
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Request location permissions
     */
    public void requestLocationPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    /**
     * Check if location services are enabled
     */
    public boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && 
               (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    /**
     * Get current location (one-time)
     */
    public void getCurrentLocation() {
        if (!hasLocationPermission()) {
            if (locationListener != null) {
                locationListener.onPermissionDenied();
            }
            return;
        }

        if (!isLocationEnabled()) {
            if (locationListener != null) {
                locationListener.onLocationError("Location services are disabled");
            }
            return;
        }

        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            if (locationListener != null) {
                                locationListener.onLocationReceived(location);
                            }
                        } else {
                            // Last location is null, request fresh location
                            requestLocationUpdates();
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (locationListener != null) {
                            locationListener.onLocationError("Failed to get location: " + e.getMessage());
                        }
                    });
        } catch (SecurityException e) {
            if (locationListener != null) {
                locationListener.onLocationError("Location permission not granted");
            }
        }
    }

    /**
     * Start location updates
     */
    public void requestLocationUpdates() {
        if (!hasLocationPermission()) {
            if (locationListener != null) {
                locationListener.onPermissionDenied();
            }
            return;
        }

        if (!isLocationEnabled()) {
            if (locationListener != null) {
                locationListener.onLocationError("Location services are disabled");
            }
            return;
        }

        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(FASTEST_INTERVAL)
                .setMaxUpdateDelayMillis(UPDATE_INTERVAL)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null && locationListener != null) {
                    locationListener.onLocationReceived(location);
                    // Stop updates after getting first location
                    stopLocationUpdates();
                }
            }
        };

        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } catch (SecurityException e) {
            if (locationListener != null) {
                locationListener.onLocationError("Location permission not granted");
            }
        }
    }

    /**
     * Stop location updates
     */
    public void stopLocationUpdates() {
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
            locationCallback = null;
        }
    }

    /**
     * Convert location to coordinate string for API
     */
    public static String locationToCoordinateString(Location location) {
        return location.getLatitude() + "," + location.getLongitude();
    }

    /**
     * Handle permission result
     */
    public void onPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get location
                getCurrentLocation();
            } else {
                // Permission denied
                if (locationListener != null) {
                    locationListener.onPermissionDenied();
                }
            }
        }
    }

    /**
     * Clean up resources
     */
    public void cleanup() {
        stopLocationUpdates();
        locationListener = null;
    }

    /**
     * Get permission request code
     */
    public static int getLocationPermissionRequestCode() {
        return LOCATION_PERMISSION_REQUEST_CODE;
    }
}
