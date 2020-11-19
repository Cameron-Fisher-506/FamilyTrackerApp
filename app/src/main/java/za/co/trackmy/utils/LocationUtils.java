package za.co.trackmy.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class LocationUtils {

    private static final String TAG = "LocationUtils";

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

    public static String getAddress(Context context, LatLng latLng) {
        String toReturn = null;

        try
        {
            if(context != null && latLng != null)
            {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(context, Locale.getDefault());

                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                toReturn  = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            }
        }catch(Exception e)
        {
            Log.e(TAG, "Error: " + e.getMessage() +
                    "\nMethod: LocationUtils - getAddress" +
                    "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }



        return toReturn;
    }

    public static Double msToKmh(Double ms)
    {
        Double toReturn = null;

        if(ms != null)
        {
           toReturn = MathUtils.precision(ms * 3.6);
        }

        return toReturn;
    }

}

