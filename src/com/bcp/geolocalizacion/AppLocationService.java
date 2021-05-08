package com.bcp.geolocalizacion;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

class AppLocationService extends Service implements LocationListener {

    protected LocationManager locationManager;
    Location location;
    Context ctx;
    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 120000;

    public AppLocationService(Context context) {
        ctx = context;
        locationManager = (LocationManager) context
                .getSystemService(LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    public Location getLocation(String provider) {

        if (locationManager.isProviderEnabled(provider)) {

            locationManager.requestLocationUpdates(provider,
                    MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(provider);
                return location;
            }
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        //nothing
    }

    @Override
    public void onProviderDisabled(String provider) {
        //nothing
    }

    @Override
    public void onProviderEnabled(String provider) {
        //nothing
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //nothing
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
