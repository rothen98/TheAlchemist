package toblindr.student.chalmers.se.thealchemist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import toblindr.student.chalmers.se.thealchemist.model.Facade;
import toblindr.student.chalmers.se.thealchemist.model.Item;

public class MainActivity extends AppCompatActivity implements IItemParentController{
    private LinearLayout itemLayout;
    private Facade facade;

    public MainActivity() {
        facade = new Facade(initReactions(),initItems());
        this.itemLayout = findViewById(R.id.itemLayout);
        initItemLayout();

    }

    private void initItemLayout() {
        for(Item item:facade.getKnownItems()){
            ItemView itemView = new ItemView(this,item,this);
            itemLayout.addView(itemView);
        }
    }

    private Map<String, String> initItems() {

        List<String> itemsInfo = readRawTextFile(this,R.raw.items);
        Map<String,String> toReturn = new HashMap<>();
        for(String item:itemsInfo){
            String[] info = item.split(":");
            toReturn.put(info[0],info[1]);
        }
        return toReturn;
    }

    private Collection<String> initReactions() {
        return readRawTextFile(this,R.raw.reactions);
    }
    public List<String> readRawTextFile(Context ctx, int resId)
    {
        InputStream inputStream = ctx.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        List<String> rows = new ArrayList<>();

        try {
            while (( line = buffreader.readLine()) != null) {
                rows.add(line);
            }
        } catch (IOException e) {
            return null;
        }
        return rows;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void reaction(Item item, Item item1) {
        facade.tryReaction(item,item1);
    }
}
