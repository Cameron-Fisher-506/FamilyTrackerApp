package za.co.trackmybravo.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DTUtils
{
    public static String getCurrentDateTime()
    {
        String toReturn = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        now = calendar.getTime();

        toReturn = simpleDateFormat.format(now);

        return toReturn;

    }
}
