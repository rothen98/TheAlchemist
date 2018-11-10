package toblindr.student.chalmers.se.thealchemist;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListCompleteItemView extends GridLayout {
    private String itemName;
    public ListCompleteItemView(Context context, String name, ImageView imageView) {
        super(context);
        itemName = name;
        addView(imageView);
        TextView textView = new TextView(getContext());
        textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
        param.setGravity(Gravity.CENTER_VERTICAL |Gravity.START);
        param.setMarginStart(20);
        textView.setLayoutParams(param);
        textView.setText(name);
        textView.setTypeface(Typeface.DEFAULT_BOLD);

        addView(textView);



    }

    public String getItemName() {
        return itemName;
    }
}
