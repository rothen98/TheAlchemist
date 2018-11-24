package toblindr.student.chalmers.se.thealchemist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatImageView;

import toblindr.student.chalmers.se.thealchemist.model.Item;

public class SmallImageView extends AppCompatImageView {
    public static final int SIZE=120;
    public SmallImageView(Context context, Item item) {
        super(context);
        Bitmap aBitmap = BitmapFactory.decodeResource(getResources(),
                getResources().getIdentifier( item.getImagePath() ,
                        "drawable", getContext().getPackageName()));
        if(aBitmap!=null){
            aBitmap = Bitmap.createScaledBitmap(aBitmap,SIZE,SIZE,false);
            setImageBitmap(aBitmap);
        }else{
            setImageBitmap(Util.getDefaultItemBitmap(getResources(),getContext(),SIZE,SIZE));
        }
    }
}
