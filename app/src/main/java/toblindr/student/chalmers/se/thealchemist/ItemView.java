package toblindr.student.chalmers.se.thealchemist;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import toblindr.student.chalmers.se.thealchemist.model.Item;


public class ItemView extends View {
    private Item item;
    private Bitmap bitmap;
    private IItemParentController parent;
    public ItemView(Context context, Item item, IItemParentController parent) {
        super(context);
        this.item = item;
        this.parent = parent;
        this.bitmap = BitmapFactory.decodeResource(getResources(),
                getResources().getIdentifier( item.getImagePath() ,
                        "drawable", getContext().getPackageName()));
        setOnTouchListener(new ItemTouchListener());
        setOnDragListener(new ItemDragListener());
    }

    @Override
    public void onDraw(Canvas canvas){
        //canvas.drawBitmap(bitmap);

    }

    public Item getItem() {
        return item;
    }

    private final class ItemTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                DragShadowBuilder shadowBuilder = new DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    private final class ItemDragListener implements OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            View dragView = (View) event.getLocalState();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:

                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    dragView.setVisibility(VISIBLE);
                    break;
                case DragEvent.ACTION_DROP:
                    ItemView view = (ItemView) dragView;
                    parent.reaction(item, view.getItem());
                case DragEvent.ACTION_DRAG_ENDED:

                default:
                    break;
            }
            return true;
        }

    }
}



