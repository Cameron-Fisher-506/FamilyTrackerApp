package za.co.familytracker;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.familytracker.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import za.co.familytracker.za.co.familytracker.utils.DTUtils;
import za.co.familytracker.za.co.familytracker.utils.GeneralUtils;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener
{
    private final String TAG = "MainActivity";

    private GoogleMap map;
    private Marker marker;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLocationManager();

        SupportMapFragment  mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if(mapFragment != null)
        {
            mapFragment.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.map = googleMap;

    }

    private void setLocationManager()
    {
        try
        {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5, MainActivity.this);
        } catch (SecurityException e)
        {
            Log.e(TAG, "Error: " + e.getMessage() +
                    "\nMethod: LocationUtils - startLocationManager" +
                    "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null)
        {
            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(myLocation)
                    .title("Me");

            if(this.marker == null)
            {
                this.marker = this.map.addMarker(markerOptions);
            }else
            {
                marker.setPosition(myLocation);
            }

            this.map.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
