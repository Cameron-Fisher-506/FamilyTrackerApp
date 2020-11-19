package za.co.trackmy.nav;

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
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import za.co.trackmy.R;
import za.co.trackmy.objs.Device;
import za.co.trackmy.utils.ConstantUtils;
import za.co.trackmy.utils.DTUtils;
import za.co.trackmy.utils.GeneralUtils;
import za.co.trackmy.utils.LocationUtils;
import za.co.trackmy.utils.StringUtils;
import za.co.trackmy.utils.WSCallsUtils;
import za.co.trackmy.utils.WSCallsUtilsTaskCaller;

public class MapFrag extends Fragment implements OnMapReadyCallback, WSCallsUtilsTaskCaller
{
    private final int REQ_CODE_GET_DEVICE_COORDINATE_HEALTH = 101;

    private GoogleMap map;
    private Marker marker;

    private TextView txtAddress;
    private TextView txtSpeed;
    private TextView txtBattery;
    private TextView txtSignal;
    private TextView txtLastSeen;

    private Device device;

    private Timer timer;
    private TimerTask timerTask;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_map, container, false);

        wireUI(view);

        String strDevice = getArguments().getString("device");

        if(strDevice != null)
        {
            try
            {
                JSONObject jsonObjectDevice = new JSONObject(strDevice);
                this.device = new Device();
                this.device.populate(jsonObjectDevice);

            }catch(Exception e)
            {
                Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                        + "\nMethod: MapFrag - onCreateView"
                        + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
            }
        }

        updateLocation();

        return view;
    }

    private void updateLocation()
    {
        this.timerTask = new TimerTask() {
            @Override
            public void run()
            {
                getDeviceUpdatedLocation();
            }
        };

        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(this.timerTask, 0, ConstantUtils.MAP_REFRESH_TIME);
    }

    private void getDeviceUpdatedLocation()
    {

        if(this.device.getImei() != null)
        {
            WSCallsUtils.get(this, StringUtils.FAMILY_TRACKER_URL + "/rest/device/getDeviceCoordinateHealth/" + this.device.getImei(), REQ_CODE_GET_DEVICE_COORDINATE_HEALTH);
        }else
        {
            GeneralUtils.createAlertDialog(getContext(), this.device.getName() + "IMEI not found!", "Unable to track location!", false, null).show();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if(this.timer != null)
        {
            this.timer.cancel();
            this.timer.purge();
        }
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

    private void updateMap()
    {
        if(this.map != null && this.device != null && this.device.getCoordinate() != null)
        {
            LatLng location = new LatLng(Double.parseDouble(this.device.getCoordinate().getLatitude()), Double.parseDouble(this.device.getCoordinate().getLongitude()));
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(location)
                    .title(this.device.getName())
                    .snippet(LocationUtils.getAddress(getContext(), location));

            if(this.marker == null)
            {
                this.marker = this.map.addMarker(markerOptions);
            }else
            {
                marker.setPosition(location);
            }
            this.marker.showInfoWindow();

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(location)      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            txtSpeed.setText(LocationUtils.msToKmh(Double.parseDouble(this.device.getCoordinate().getSpeed())) + " km/h");

            String batteryLife = this.device.getHealth().getBatteryLife();
            if(batteryLife != null)
            {
                this.txtBattery.setText(batteryLife);
            }else
            {
                this.txtBattery.setText("N/A");
            }

            String signalStrength = this.device.getHealth().getSignalStrength();
            if(signalStrength != null)
            {
                this.txtSignal.setText(signalStrength);
            }else
            {
                this.txtSignal.setText("N/A");
            }

            String lastSeen = this.device.getCoordinate().getCreatedTime();
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
    public void onMapReady(GoogleMap googleMap)
    {
        this.map = googleMap;
        this.map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity().getApplicationContext(), R.raw.map_in_night));

        updateMap();

    }

    @Override
    public void taskCompleted(String response, int reqCode) {
        if(response != null)
        {
            if(reqCode == REQ_CODE_GET_DEVICE_COORDINATE_HEALTH)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    this.device.populate(jsonObject);

                    updateMap();

                }catch(Exception e)
                {
                    Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                            + "\nMethod: WSCallsUtils - taskCompleted"
                            + "\nresponse: " + response
                            + "\nreqCode: " + reqCode
                            + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
                }
            }
        }
    }
}
