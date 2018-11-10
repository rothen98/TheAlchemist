package toblindr.student.chalmers.se.thealchemist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;

import toblindr.student.chalmers.se.thealchemist.model.Item;

public abstract class ItemHolder extends AppCompatImageView {
    private Item item;
    private Bitmap bitmap;
    public final static int SIZE = 170;

    public ItemHolder(Context context,Item item) {
        super(context);
        this.item = item;

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

    public Item getItem(){
        return item;
    }

    public abstract boolean canBeRemoved();
}
