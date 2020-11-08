package za.co.familytracker.za.co.familytracker.utils;

import android.content.Context;
import android.widget.Toast;

public class GeneralUtils
{
    public static void makeToast(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
