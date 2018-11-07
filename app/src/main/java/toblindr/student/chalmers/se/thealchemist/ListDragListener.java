package toblindr.student.chalmers.se.thealchemist;

import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

public class ListDragListener implements View.OnDragListener {
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
                if(view.canBeRemoved()) {
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(dragView);
                }
            case DragEvent.ACTION_DRAG_ENDED:

            default:
                break;
        }
        return true;
    }
}
