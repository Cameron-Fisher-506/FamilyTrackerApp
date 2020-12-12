package za.co.trackmybravo.services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import org.json.JSONObject;
import java.util.Timer;
import java.util.TimerTask;
import za.co.trackmybravo.utils.ConstantUtils;
import za.co.trackmybravo.utils.DTUtils;
import za.co.trackmybravo.utils.DeviceUtils;
import za.co.trackmybravo.utils.LocationUtils;
import za.co.trackmybravo.utils.SharedPreferencesUtils;
import za.co.trackmybravo.utils.StringUtils;
import za.co.trackmybravo.utils.WSCallsUtils;
import za.co.trackmybravo.utils.WSCallsUtilsTaskCaller;

public class DeviceService extends Service implements WSCallsUtilsTaskCaller
{
    private final int REQ_CODE_UPDATE_DEVICE_COORDINATE_HEALTH = 101;

    private Timer timer;
    private TimerTask timerTask;
    private LocationUtils locationUtils;

    @Override
    public void onCreate() {
        super.onCreate();

        this.locationUtils = new LocationUtils(getApplicationContext());

        this.timerTask = new TimerTask() {
            @Override
            public void run()
            {
                updateDeviceCoordinateHealth();
            }
        };

        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(this.timerTask, 0, ConstantUtils.DEVICE_SERVICE_TIME);
    }

    private void updateDeviceCoordinateHealth()
    {
        try
        {
            JSONObject body = new JSONObject();

            String code = SharedPreferencesUtils.getString(getApplicationContext(), SharedPreferencesUtils.MY_CODE);
            if(code != null && locationUtils.getCurrentLocation() != null)
            {
                body.put("code", code);

                JSONObject coordinate = new JSONObject();
                coordinate.put("latitude", Double.toString(locationUtils.getCurrentLocation().getLatitude()));
                coordinate.put("longitude", Double.toString(locationUtils.getCurrentLocation().getLongitude()));
                coordinate.put("speed", Float.toString(locationUtils.getCurrentLocation().getSpeed()));
                coordinate.put("bearing", Float.toString(locationUtils.getCurrentLocation().getBearing()));
                coordinate.put("accuracy", Float.toString(locationUtils.getCurrentLocation().getAccuracy()));
                body.put("coordinate", coordinate);

                JSONObject health = new JSONObject();
                health.put("batteryLife", DeviceUtils.getBatteryLevel(getApplicationContext()));
                health.put("signalStrength", DeviceUtils.getNetworkType(getApplicationContext()));
                health.put("isRoaming", DeviceUtils.isRoaming(getApplicationContext()));
                body.put("health", health);

                WSCallsUtils.post(this, StringUtils.FAMILY_TRACKER_URL + "/rest/device/updateDeviceCoordinateHealth", body.toString(), REQ_CODE_UPDATE_DEVICE_COORDINATE_HEALTH);
            }
        }catch(Exception e)
        {
            Log.d(ConstantUtils.TAG, "\n\nClass: DeviceService" +
                    "\nMethod: onCreate" +
                    "\nError: " + e.getMessage() +
                    "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(timer != null)
        {
            this.timer.cancel();
            this.timer.purge();
        }
    }

    @Override
    public Activity getActivity() {
        return null;
    }

    @Override
    public void taskCompleted(String response, int reqCode)
    {
        if(response != null)
        {
            if(reqCode == REQ_CODE_UPDATE_DEVICE_COORDINATE_HEALTH)
            {

            }
        }

    }
}
