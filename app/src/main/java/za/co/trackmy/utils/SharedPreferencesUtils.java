package za.co.trackmy.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import org.json.JSONObject;

public class SharedPreferencesUtils {

    public static final String PRIVACY_POLICY_ACCEPTANCE = "PRIVACY_POLICY_ACCEPTANCE";
    public static final String MY_CODE = "MY_CODE";


    public static void save(Context context, String sharedPrefName, JSONObject jsonObject){

        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefName, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(sharedPrefName, jsonObject.toString());
        editor.apply();
    }

    public static void save(Context context, String sharedPrefName, String value){

        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefName, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(sharedPrefName, value);
        editor.apply();
    }

    public static String getString(Context context, String sharedPrefName)
    {
        String toReturn = null;

        try
        {
            SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefName, 0);

            if(sharedPreferences != null && sharedPreferences.contains(sharedPrefName))
            {
                String value = sharedPreferences.getString(sharedPrefName, "DEFAULT");

                if(value != null && !value.equals(""))
                {
                    toReturn = value;
                }
            }

        }catch(Exception e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: SharedPreferencesUtils - get"
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }

        return toReturn;
    }

    public static JSONObject get(Context context, String sharedPrefName)
    {
        JSONObject toReturn = null;

        try
        {
            SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefName, 0);

            if(sharedPreferences != null && sharedPreferences.contains(sharedPrefName))
            {
                String value = sharedPreferences.getString(sharedPrefName, "DEFAULT");

                if(value != null && !value.equals(""))
                {
                    toReturn = new JSONObject(value);
                }
            }

        }catch(Exception e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: SharedPreferencesUtils - get"
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }

        return toReturn;
    }
}
