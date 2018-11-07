package toblindr.student.chalmers.se.thealchemist;

import android.content.ClipData;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;


import toblindr.student.chalmers.se.thealchemist.model.Item;

public class ItemListView extends ItemHolder {

    private IItemParentController parent;
    public ItemListView(Context context, Item item, IItemParentController parent) {
        super(context,item);
        this.parent = parent;

        setOnTouchListener(new ListItemTouchListener());

    }

    @Override
    public boolean canBeRemoved() {
        return false;
    }


    private final class ListItemTouchListener implements View.OnTouchListener {
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
                //view.setVisibility(View.INVISIBLE);

                return true;
            } else {
                return false;
            }
        }
    }
}
