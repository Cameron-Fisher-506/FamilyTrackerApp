package za.co.trackmybravo.nav;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import za.co.trackmybravo.MainActivity;
import za.co.trackmybravo.R;
import za.co.trackmybravo.utils.ConstantUtils;
import za.co.trackmybravo.utils.DTUtils;
import za.co.trackmybravo.utils.DeviceUtils;
import za.co.trackmybravo.utils.LocationUtils;


public class MeFrag extends Fragment implements OnMapReadyCallback, LocationListener
{
    private GoogleMap map;
    private Marker marker;

    private LocationManager locationManager;

    private TextView txtAddress;
    private TextView txtSpeed;
    private TextView txtBattery;
    private TextView txtSignal;
    private TextView txtLastSeen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_me, container, false);

        setLocationManager();

        wireUI(view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void wireUI(View view)
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment != null)
        {
            mapFragment.getMapAsync(this);
        }

        //txtAddress = (TextView) view.findViewById(R.id.txtAddress);
        this.txtLastSeen = (TextView) view.findViewById(R.id.txtLastSeen);
        this.txtSpeed = (TextView) view.findViewById(R.id.txtSpeed);
        this.txtBattery = (TextView) view.findViewById(R.id.txtBattery);
        this.txtSignal = (TextView) view.findViewById(R.id.txtSignal);
    }

    private void setLocationManager()
    {
        try
        {
            locationManager = (LocationManager) ((MainActivity)getActivity()).getSystemService(Context.LOCATION_SERVICE);
            if(locationManager != null)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5,this);
            }

        } catch (SecurityException e)
        {
            Log.e(ConstantUtils.TAG, "Error: " + e.getMessage() +
                    "\nMethod: LocationUtils - startLocationManager" +
                    "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.map = googleMap;
        this.map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity().getApplicationContext(), R.raw.map_in_night));

    }

    @Override
    public void onLocationChanged(Location location) {
        if(this.map != null && location != null)
        {
            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(myLocation)
                    .title("Me")
                    .snippet(LocationUtils.getAddress(getContext(), myLocation));


            if(this.marker == null)
            {
                this.marker = this.map.addMarker(markerOptions);
            }else
            {
                marker.setPosition(myLocation);
            }

            this.marker.showInfoWindow();

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(myLocation)      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            this.txtSpeed.setText(LocationUtils.msToKmh((double)location.getSpeed()) + " km/h");

            String batteryLife = DeviceUtils.getBatteryLevel(getContext());
            if(batteryLife != null)
            {
                this.txtBattery.setText(batteryLife);
            }else
            {
                this.txtBattery.setText("N/A");
            }

            String signalStrength = DeviceUtils.getNetworkType(getContext());
            if(signalStrength != null)
            {
                this.txtSignal.setText(signalStrength);
            }else
            {
                this.txtSignal.setText("N/A");
            }

            String lastSeen = DTUtils.getCurrentDateTime();
            if(lastSeen != null)
            {
                this.txtLastSeen.setText(lastSeen);
            }else
            {
                this.txtLastSeen.setText("N/A");
            }
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
