package za.co.familytracker;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import com.example.familytracker.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        LatLng myLocation = new LatLng(-26.127733, 27.982846);
        this.map.addMarker(new MarkerOptions()
                .position(myLocation)
                .title("Me"));
        this.map.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
    }
}
