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
                //ItemView it = new ItemView(getContext(),getItem(),parent);
                //String tag = (String)it.getTag();
                //ClipData clipData = ClipData.newPlainText("", tag);
                //View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(it);
                //view.startDrag(clipData, dragShadowBuilder, it, 0);
                //Todo above possible instead?
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
                //TODO start drag should be drag and drop?


                // Hide the view object because we are dragging it.
                //view.setVisibility(View.INVISIBLE);

                return true;
            } else {
                return false;
            }
        }
    }
}
