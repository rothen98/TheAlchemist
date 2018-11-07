package toblindr.student.chalmers.se.thealchemist;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatImageView;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;


import toblindr.student.chalmers.se.thealchemist.model.Item;


public class ItemView extends ItemHolder {
    private IItemParentController parent;
    public ItemView(Context context, Item item, IItemParentController parent) {
        super(context,item);

        this.parent = parent;

        setOnTouchListener(new ItemTouchListener());
        setOnDragListener(new ItemDragListener());

    }

    @Override
    public boolean canBeRemoved() {
        return true;
    }

    private final class ItemTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                // Get view object tag value.
                String tag = (String)view.getTag();

                // Create clip data.
                ClipData clipData = ClipData.newPlainText("", tag);

                // Create drag shadow builder object.
                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(view);

                /* Invoke view object's startDrag method to start the drag action.
                clipData : to be dragged data.
                dragShadowBuilder : the shadow of the dragged view.*/
                view.startDrag(clipData, dragShadowBuilder, view, 0);


                // Hide the view object because we are dragging it.
                view.setVisibility(View.INVISIBLE);

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
                    break;
                case DragEvent.ACTION_DROP:
                    ItemHolder view = (ItemHolder) dragView;
                    tryReaction(view,getX(),getY());
                case DragEvent.ACTION_DRAG_ENDED:

                default:
                    break;
            }
            return true;
        }

    }

    private void tryReaction(ItemHolder view, float x, float y) {
        parent.reaction(this,view,x,y);
    }
}



