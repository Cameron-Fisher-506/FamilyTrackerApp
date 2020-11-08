package za.co.familytracker.za.co.familytracker.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationUtils {

    private final String TAG = "LocationUtils";

    private LocationManager locationManager;

    private LocationListener locationListener;

    private Location currentLocation;

    private Context context;

    public LocationUtils(Context context)
    {
        this.context = context;
        startLocationServices();
    }

    public void startLocationServices()
    {
        if(locationManager == null)
        {
            initializeLocationServices();

            startLocationManager();

        }
    }

    public Location getCurrentLocation()
    {
        return currentLocation;
    }

    private void initializeLocationServices() {

        if(this.context != null)
        {
            if (locationManager == null) {
                locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
            }

            if (locationListener == null)
            {
                locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        currentLocation = location;
                    }

                    public void onProviderDisabled(String provider) {
                    }

                    public void onProviderEnabled(String provider) {
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }
                };
            }
        }

    }

    private void startLocationManager()
    {
        try
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5, locationListener);
        } catch (SecurityException e)
        {
            Log.e(TAG, "Error: " + e.getMessage() +
                    "\nMethod: LocationUtils - startLocationManager" +
                    "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }
    }

}

