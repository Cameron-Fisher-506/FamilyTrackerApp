package za.co.trackmy.utils;

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

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            {
                toReturn = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                toReturn = telephonyManager.getImei();
            }else{
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
        String toReturn = null;

        try
        {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if(telephonyManager != null)
            {
                int networkType = telephonyManager.getNetworkType();
                switch (networkType)
                {
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                        toReturn = "1xRTT";
                        break;
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        toReturn = "CDMA";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        toReturn = "EDGE";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                        toReturn = "eHRPD";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        toReturn = "EVDO rev. 0";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        toReturn = "EVDO rev. A";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        toReturn = "EVDO rev. B";
                        break;
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        toReturn = "GPRS";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        toReturn = "HSDPA";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        toReturn = "HSPA";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        toReturn = "HSPA+";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                        toReturn = "HSUPA";
                        break;
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        toReturn = "iDen";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        toReturn = "LTE";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        toReturn = "UMTS";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                        toReturn = "Unknown";
                        break;
                }
            }
        }catch (Exception e)
        {
            Log.d(ConstantUtils.TAG, "Method: DeviceUtils - getNetworkType"
                    + "\nMessage: " + e.getMessage()
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }

        return toReturn;
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
