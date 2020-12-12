package za.co.trackmybravo.objs;

import android.util.Log;

import org.json.JSONObject;

import za.co.trackmybravo.utils.ConstantUtils;
import za.co.trackmybravo.utils.DTUtils;

public class Device
{
    private String name;
    private String code;
    private String createdTime;
    private Coordinate coordinate;
    private Health health;

    public Device()
    {

    }

    public Device(String name, String code, String createdTime, Coordinate coordinate, Health health) {
        this.name = name;
        this.code = code;
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
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
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
                this.code = jsonObject.has("code") ? jsonObject.getString("code") : null;
                this.createdTime = jsonObject.has("createdTime") ? jsonObject.getString("createdTime") : null;

                if(jsonObject.has("coordinate"))
                {
                    JSONObject jsonObjectCoordinate = jsonObject.getJSONObject("coordinate");
                    String latitude = jsonObjectCoordinate.has("latitude") ? jsonObjectCoordinate.getString("latitude") : null;
                    String longitude = jsonObjectCoordinate.has("longitude") ? jsonObjectCoordinate.getString("longitude") : null;
                    String speed = jsonObjectCoordinate.has("speed") ? jsonObjectCoordinate.getString("speed") : null;
                    String bearing = jsonObjectCoordinate.has("bearing") ? jsonObjectCoordinate.getString("bearing") : null;
                    String accuracy = jsonObjectCoordinate.has("accuracy") ? jsonObjectCoordinate.getString("accuracy") : null;
                    String createdTime = jsonObjectCoordinate.has("createdTime") ? jsonObjectCoordinate.getString("createdTime") : null;

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

    public JSONObject toJSON()
    {
        JSONObject toReturn = new JSONObject();
        try
        {
            toReturn.put("name", this.name);
            toReturn.put("code", this.code);
            toReturn.put("createdTime", this.createdTime);

            if(this.coordinate != null)
            {
                JSONObject jsonObjectCoordinate  = new JSONObject();
                jsonObjectCoordinate.put("latitude", this.coordinate.getLatitude());
                jsonObjectCoordinate.put("longitude", this.coordinate.getLongitude());
                jsonObjectCoordinate.put("speed", this.coordinate.getSpeed());
                jsonObjectCoordinate.put("accuracy", this.coordinate.getAccuracy());
                jsonObjectCoordinate.put("bearing", this.coordinate.getBearing());
                jsonObjectCoordinate.put("createdTime", this.coordinate.getCreatedTime());

                toReturn.put("coordinate", jsonObjectCoordinate);
            }

            if(this.health != null)
            {
                JSONObject jsonObjectHealth = new JSONObject();
                jsonObjectHealth.put("batteryLife", this.health.getBatteryLife());
                jsonObjectHealth.put("signalStrength", this.health.getSignalStrength());
                jsonObjectHealth.put("createdTime", this.health.getCreatedTime());

                toReturn.put("health", jsonObjectHealth);
            }

        }catch (Exception e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: Device - toJSON"
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }


        return toReturn;
    }
}
