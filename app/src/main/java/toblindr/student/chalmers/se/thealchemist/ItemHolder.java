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

    public ItemHolder(Context context,Item item) {
        super(context);
        this.item = item;
        this.bitmap = bitmap;
        this.bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                getResources().getIdentifier( item.getImagePath() ,
                        "drawable", getContext().getPackageName())),150,150,false);
        setImageBitmap(bitmap);
    }

    public Item getItem(){
        return item;
    }

    public abstract boolean canBeRemoved();
}
