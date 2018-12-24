package com.bassamworks.rsr.utils;

import android.app.Activity;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationFinder {

    //Constants
    private static final long DEFAULT_LOCATION_REFRESH_INTERVAL = 5000;
    private static final String LOG_TAG = LocationFinder.class.getSimpleName();

    private final Activity context;
    private final LocationChangeCallbacks listener;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    public LocationFinder(Activity context, LocationChangeCallbacks listener) {
        this.context = context;
        this.listener = listener;

        setupLocationChangeCallback();
        setupMapClients();
    }


    public void requestLocationUpdates() {
        try {
            fusedLocationClient
                    .requestLocationUpdates(
                            LocationRequest.create()
                                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                    .setInterval(DEFAULT_LOCATION_REFRESH_INTERVAL),
                            locationCallback,
                            Looper.myLooper());

        } catch (SecurityException e) {
            Log.e(LOG_TAG, e.toString());
        }
    }

    public void cancelLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void setupLocationChangeCallback() {

        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location lastLocation = locationResult.getLastLocation();

                if (lastLocation != null) {
                    listener.onLocationChanged(lastLocation);
                }
            }
        };
    }

    private void setupMapClients() {
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public interface LocationChangeCallbacks {
        void onLocationChanged(Location location);
    }
}
