package za.co.familytracker.objs;

import android.util.Log;

import org.json.JSONObject;

import za.co.familytracker.utils.ConstantUtils;
import za.co.familytracker.utils.DTUtils;

public class Device
{
    private String name;
    private String imei;
    private String createdTime;
    private Coordinate coordinate;
    private Health health;

    public Device()
    {

    }

    public Device(String name, String imei, String createdTime, Coordinate coordinate, Health health) {
        this.name = name;
        this.imei = imei;
        this.createdTime = createdTime;
        this.coordinate = coordinate;
        this.health = health;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImei() {
        return imei;
    }
    public void setImei(String imei) {
        this.imei = imei;
    }
    public String getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
    public Coordinate getCoordinate() {
        return coordinate;
    }
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
    public Health getHealth() {
        return health;
    }
    public void setHealth(Health health) {
        this.health = health;
    }

    public void populate(JSONObject jsonObject)
    {
        if(jsonObject != null)
        {
            try
            {
                this.name = jsonObject.has("name") ? jsonObject.getString("name") : null;
                this.imei = jsonObject.has("imei") ? jsonObject.getString("imei") : null;
                this.createdTime = jsonObject.has("createdTime") ? jsonObject.getString("createdTime") : null;

                if(jsonObject.has("coordinate"))
                {
                    JSONObject jsonObjectCoordinate = jsonObject.getJSONObject("coordinate");
                    String latitude = jsonObjectCoordinate.has("latitude") ? jsonObjectCoordinate.getString("latitude") : null;
                    String longitude = jsonObjectCoordinate.has("latitude") ? jsonObjectCoordinate.getString("latitude") : null;
                    String speed = jsonObjectCoordinate.has("speed") ? jsonObjectCoordinate.getString("speed") : null;
                    String bearing = jsonObjectCoordinate.has("bearing") ? jsonObjectCoordinate.getString("bearing") : null;
                    String accuracy = jsonObjectCoordinate.has("accuracy") ? jsonObjectCoordinate.getString("accuracy") : null;
                    String createdTime = jsonObjectCoordinate.has("createdTimer") ? jsonObjectCoordinate.getString("createdTime") : null;

                    this.coordinate = new Coordinate(latitude, longitude, bearing, speed, accuracy, createdTime);
                }

                if(jsonObject.has("health"))
                {
                    JSONObject jsonObjectHealth = jsonObject.getJSONObject("health");
                    String batteryLife = jsonObjectHealth.has("batteryLife") ? jsonObjectHealth.getString("batteryLife") : null;
                    String signalStrength = jsonObjectHealth.has("signalStrength") ? jsonObjectHealth.getString("signalStrength") : null;
                    String createdTime = jsonObjectHealth.has("createdTime") ? jsonObjectHealth.getString("createdTime") : null;

                    this.health = new Health(batteryLife, signalStrength, createdTime);
                }
            }catch(Exception e)
            {
                Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                        + "\nMethod: Device - populate"
                        + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
            }

        }
    }
}
