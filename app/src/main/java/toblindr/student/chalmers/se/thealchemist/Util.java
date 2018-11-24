package toblindr.student.chalmers.se.thealchemist;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

class Util {

    public static Bitmap getDefaultItemBitmap(Resources resources, Context context,int width,int height) {
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
                resources.getIdentifier( "qm" ,
                        "drawable", context.getPackageName())),width,height,false);
    }
}
