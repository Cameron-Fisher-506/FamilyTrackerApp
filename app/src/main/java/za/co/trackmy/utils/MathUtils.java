package za.co.trackmy.utils;

import java.text.DecimalFormat;

public class MathUtils
{
    public static Double precision(Double value)
    {
        Double toReturn = null;

        if(value != null)
        {
            DecimalFormat decimalFormat = new DecimalFormat("#,##");
            toReturn = Double.valueOf(decimalFormat.format(value));
        }

        return toReturn;
    }
}
