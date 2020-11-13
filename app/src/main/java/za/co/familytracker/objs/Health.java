package za.co.familytracker.objs;

import za.co.familytracker.utils.DTUtils;

public class Health
{
    private String batteryLife;
    private String signalStrength;
    private String createdTime;

    public Health(String batteryLife, String signalStrength, String createdTime)
    {
        this.batteryLife = batteryLife;
        this.signalStrength = signalStrength;
        this.createdTime = createdTime;
    }
    public String getBatteryLife() {
        return batteryLife;
    }
    public void setBatteryLife(String batteryLife) {
        this.batteryLife = batteryLife;
    }
    public String getSignalStrength() {
        return signalStrength;
    }
    public void setSignalStrength(String signalStrength) {
        this.signalStrength = signalStrength;
    }
    public String getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }


}
