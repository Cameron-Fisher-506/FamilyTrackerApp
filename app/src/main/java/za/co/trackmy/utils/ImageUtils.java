package za.co.trackmy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class ImageUtils
{
    public static Bitmap createBitmap(Context context, int width, int height, int resource)
    {
        BitmapDrawable bitmapdraw = (BitmapDrawable) context.getResources().getDrawable(resource);
        Bitmap b = bitmapdraw.getBitmap();

        return Bitmap.createScaledBitmap(b, width, height, false);
    }
}
