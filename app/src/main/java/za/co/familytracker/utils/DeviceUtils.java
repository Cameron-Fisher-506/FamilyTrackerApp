package za.co.familytracker.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import static android.content.Context.BATTERY_SERVICE;

public class DeviceUtils
{
    public static String getIMEI(Context context)
    {
        String toReturn = null;

        try
        {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                toReturn = telephonyManager.getImei();
            } else {
                toReturn = telephonyManager.getDeviceId();
            }

        }catch(SecurityException e) // What exception?
        {
            Log.d(ConstantUtils.TAG, "\n\nClass: DeviceUtils" +
                    "\nMethod: getIMEI" +
                    "\nError: " + e.getMessage() +
                    "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }catch (Exception e)
        {
            Log.d(ConstantUtils.TAG, "\n\nClass: DeviceUtils" +
                    "\nMethod: getIMEI" +
                    "\nError: " + e.getMessage() +
                    "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }

        return toReturn;
    }

    public static String getBatteryLevel(Context context)
    {
        String toReturn = null;

        try
        {
            BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
            if(bm != null)
            {
                int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                toReturn = String.valueOf(batLevel).concat("%");
            }

        }catch(Exception e)
        {
            Log.d(ConstantUtils.TAG, "Method: DeviceUtils - getBatteryLevel"
                    + "\nMessage: " + e.getMessage()
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }

        return toReturn;
    }

    public static String getNetworkType(Context context)
    {
        TelephonyManager teleMan = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = teleMan.getNetworkType();
        switch (networkType)
        {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "1xRTT";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "eHRPD";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "EVDO rev. 0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "EVDO rev. A";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "EVDO rev. B";
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "HSPA+";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "iDen";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return "Unknown";
        }
        return "*";
    }

    public static Boolean isRoaming(Context context)
    {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected())
        {
            // No connection will be seen as roaming as it cannot be established reliably
            return true;
        }
        else
        {
            NetworkInfo networkType = connManager.getActiveNetworkInfo();
            return networkType.isRoaming();
        }
    }
}
