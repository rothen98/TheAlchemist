package toblindr.student.chalmers.se.thealchemist;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import toblindr.student.chalmers.se.thealchemist.model.Item;

/**
 * TODO: document your custom view class.
 */
public class ItemEncyclopediaListView extends ConstraintLayout {

    private ImageView itemImage;
    private TextView itemName;
    public ItemEncyclopediaListView(Context context) {
        super(context);
        init();
    }

    public ItemEncyclopediaListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemEncyclopediaListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(),R.layout.encyclopedia_list_item_view,this);
        itemName = findViewById(R.id.encyclopedia_item_name);
        itemImage = findViewById(R.id.encyclopedia_image_of_item);
    }

    public void setItem(Item item){
        Bitmap aBitmap = BitmapFactory.decodeResource(getResources(),
                getResources().getIdentifier( item.getImagePath() ,
                        "drawable", getContext().getPackageName()));
        if(aBitmap!=null){
            aBitmap = Bitmap.createScaledBitmap(aBitmap,200,200,false);
            itemImage.setImageBitmap(aBitmap);
        }else{
            itemImage.setImageBitmap(Util.getDefaultItemBitmap(getResources(),getContext(),200,200));
        }

        itemName.setText(item.getName());
    }


}
