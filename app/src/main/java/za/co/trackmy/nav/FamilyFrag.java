package za.co.trackmy.nav;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import za.co.trackmy.MainActivity;
import za.co.trackmy.R;
import za.co.trackmy.dialogs.PermissionCallback;
import za.co.trackmy.objs.Device;
import za.co.trackmy.utils.ConstantUtils;
import za.co.trackmy.utils.DTUtils;
import za.co.trackmy.utils.DeviceUtils;
import za.co.trackmy.utils.DialogUtils;
import za.co.trackmy.utils.FragmentUtils;
import za.co.trackmy.utils.GeneralUtils;
import za.co.trackmy.utils.LocationUtils;
import za.co.trackmy.utils.StringUtils;
import za.co.trackmy.utils.WSCallsUtils;
import za.co.trackmy.utils.WSCallsUtilsTaskCaller;

public class FamilyFrag extends Fragment implements WSCallsUtilsTaskCaller
{
    private final int REQ_CODE_GET_ALL_LINKED_DEVICES = 101;

    private SearchView svDevice;
    private ListView lvDevices;
    private ArrayList<Device> devices;
    private DeviceAdapter deviceAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_family, container, false);

        wireUI(view);
        setSvDevice();

        getAllLinkedDevices();

        addLvMoviesListener();
        addSvMovieListener();

        return view;
    }

    private void getAllLinkedDevices()
    {
        String imei = DeviceUtils.getIMEI(getContext());

        if(imei != null)
        {
            WSCallsUtils.get(this, StringUtils.FAMILY_TRACKER_URL + "/rest/device/getAllLinkedDevices/" + imei, REQ_CODE_GET_ALL_LINKED_DEVICES);
        }else
        {
            DialogUtils.createAlertPermission(getContext(), "Phone Permission", "Please enable phone and location permissions for Family Tracker.", true, new PermissionCallback() {
                @Override
                public void checkPermission(boolean ischeckPermission) {
                    if(ischeckPermission)
                    {
                        GeneralUtils.openAppSettingsScreen(getContext());
                    }
                }
            }).show();
        }

    }

    private void wireUI(View view)
    {
        this.svDevice = view.findViewById(R.id.svDevice);
        this.lvDevices = view.findViewById(R.id.lvDevices);
    }

    private void setSvDevice()
    {
        this.svDevice.setIconifiedByDefault(true);
        this.svDevice.setFocusable(true);
        this.svDevice.setIconified(false);

        /*int id =  this.svDevice.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) this.svDevice.findViewById(id);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.WHITE);*/

    }

    private void setLvDevices(DeviceAdapter movieAdapter)
    {
        this.lvDevices.setAdapter(movieAdapter);
    }

    private void setDevices(ArrayList<Device> devices)
    {
        this.devices = devices;
    }

    private void setDeviceAdapter(ArrayList<Device> devices)
    {
        this.deviceAdapter = new DeviceAdapter(devices);
    }

    private DeviceAdapter getDeviceAdapter()
    {
        return this.deviceAdapter;
    }

    private void addSvMovieListener()
    {
        this.svDevice.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {

                deviceAdapter.getFilter().filter(newText);

                return false;
            }
        });
    }

    private void addLvMoviesListener()
    {
        this.lvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

                Device device = (Device) adapter.getItemAtPosition(position);

                Bundle bundle = new Bundle();
                bundle.putString("device", device.toJSON().toString());

                MapFrag mapFrag = new MapFrag();
                mapFrag.setArguments(bundle);

                FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), mapFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), device.getName(), true, false, true, null);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private class DeviceAdapter extends BaseAdapter implements Filterable {

        private ArrayList<Device> devices;
        private ArrayList<Device> filteredData;
        private DeviceAdapter.ItemFilter itemFilter;


        private class ItemFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String filterString = constraint.toString().toLowerCase().trim();

                FilterResults results = new FilterResults();

                final ArrayList<Device> list = devices;

                int count = list.size();
                final ArrayList<Device> searchList = new ArrayList<>(count);

                String filterableString;

                for (int i = 0; i < count; i++) {
                    filterableString = list.get(i).getName();
                    if (filterableString.toLowerCase().contains(filterString)) {
                        searchList.add(list.get(i));
                    }
                }

                results.values = searchList;
                results.count = searchList.size();

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData = (ArrayList<Device>) results.values;
                notifyDataSetChanged();
            }

        }

        public Filter getFilter() {
            return this.itemFilter;
        }

        public DeviceAdapter(ArrayList<Device> devices)
        {
            this.devices = devices;
            this.filteredData = devices;
            this.itemFilter = new DeviceAdapter.ItemFilter();
        }

        @Override
        public int getCount() {
            return this.filteredData.size();
        }

        @Override
        public Object getItem(int position) {
            return this.filteredData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
            {
                convertView =  getLayoutInflater().inflate(R.layout.device_list_item, null);
            }

            TextView txtName = convertView.findViewById(R.id.txtName);
            txtName.setText(this.filteredData.get(position).getName());

            TextView txtImei = convertView.findViewById(R.id.txtImei);
            txtImei.setText(this.filteredData.get(position).getImei());

            if(this.filteredData.get(position).getCoordinate() != null && this.filteredData.get(position).getCoordinate().getLatitude() != null &&
                    this.filteredData.get(position).getCoordinate().getLongitude() != null)
            {
                LatLng location = new LatLng(Double.parseDouble(this.filteredData.get(position).getCoordinate().getLatitude()),
                        Double.parseDouble(this.filteredData.get(position).getCoordinate().getLongitude()));

                TextView txtAddress = convertView.findViewById(R.id.txtAddress);
                txtAddress.setText(LocationUtils.getAddress(getContext(), location));
            }

            return convertView;
        }


    }

    @Override
    public void taskCompleted(String response, int reqCode) {
        if (response != null) {
            if (reqCode == REQ_CODE_GET_ALL_LINKED_DEVICES) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null && jsonObject.has("code") && jsonObject.has("message")) {
                        int code = jsonObject.getInt("code");
                        String title = jsonObject.getString("title");
                        String message = jsonObject.getString("message");

                        if (code == 0) {
                            GeneralUtils.makeToast(getContext(), message);

                            if (jsonObject.has("devices")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("devices");
                                if (jsonArray != null && jsonArray.length() > 0) {
                                    ArrayList<Device> devices = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObjectDevice = jsonArray.getJSONObject(i);

                                        Device device = new Device();
                                        device.populate(jsonObjectDevice);

                                        devices.add(device);
                                    }

                                    setDevices(devices);
                                    setDeviceAdapter(this.devices); //list of devices
                                    setLvDevices(getDeviceAdapter());
                                }
                            }
                        } else if (code == 1) {
                            GeneralUtils.makeToast(getContext(), title + " " + message);
                        } else if (code == -1) {
                            GeneralUtils.makeToast(getContext(), title + " " + message);
                        }
                    }
                } catch (Exception e) {
                    Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                            + "\nMethod: WSCallsUtils - taskCompleted"
                            + "\nresponse: " + response
                            + "\nreqCode: " + reqCode
                            + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
                }
            }
        } else
        {
            GeneralUtils.makeToast(getContext(), "Please ensure your device is connected to the internet!");
        }
    }
}
