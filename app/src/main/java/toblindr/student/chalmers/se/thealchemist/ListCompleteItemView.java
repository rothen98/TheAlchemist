package toblindr.student.chalmers.se.thealchemist;

import android.content.Context;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListCompleteItemView extends GridLayout {
    public ListCompleteItemView(Context context, String name, ImageView imageView) {
        super(context);
        addView(imageView);
        TextView textView = new TextView(getContext());
        textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
        param.setGravity(Gravity.CENTER);
        textView.setLayoutParams(param);
        textView.setText(name);
        addView(textView);



    }
}
