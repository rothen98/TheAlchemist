package toblindr.student.chalmers.se.thealchemist;


import android.content.res.Resources;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import static android.view.View.VISIBLE;

public class AlchemistActivity extends AppCompatActivity implements IItemParentController, NewItemFragment.OnFragmentInteractionListener{
    private LinearLayout itemLayout;
    private ConstraintLayout reactionView;
    private Facade facade;
    private MediaPlayer errorSound;
    private MediaPlayer succesSound;


    public AlchemistActivity() {


    }

    private void initItemLayout() {
        for(Item item:facade.getKnownItems()){
            ListCompleteItemView itemView = new ListCompleteItemView(this,item.getName(),
                    new ItemListView(this,item,this));
            itemLayout.addView(itemView);
        }
    }

    private Map<String, String> initItems() {

        List<String> itemsInfo = readRawTextFile(R.raw.items);
        Map<String,String> toReturn = new HashMap<>();
        for(String item:itemsInfo){
            String[] info = item.split(":");
            toReturn.put(info[0],info[1]);
        }
        return toReturn;
    }

    private Collection<String> initReactions() {
        return readRawTextFile(R.raw.reactions);
    }
    public List<String> readRawTextFile(int resId) {
        Resources resources = getResources();
        InputStream inputStream = resources.openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        List<String> rows = new ArrayList<>();

        try {
            while (( line = buffreader.readLine()) != null) {
                if(!line.isEmpty()) {
                    rows.add(line);
                }
            }
        } catch (IOException e) {
            return null;
        }
        return rows;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alchemist);
        facade = new Facade(initReactions(),initItems());
        this.itemLayout = findViewById(R.id.itemLayout);
        this.itemLayout.setOnDragListener(new ListDragListener());
        this.reactionView = findViewById(R.id.reaction_view);
        this.errorSound = MediaPlayer.create(this,R.raw.wrong);
        this.succesSound = MediaPlayer.create(this,R.raw.succes);
        reactionView.setOnDragListener(new TableDragListener());
        initItemLayout();
    }

    @Override
    public void reaction(ItemHolder item, float itemX, float itemY, ItemHolder itemTwo,float itemTwoX,float itemTwoY) {
        Collection<Item> knownItems = facade.getKnownItems();
        Item created = facade.tryReaction(item.getItem(),itemTwo.getItem());
        if(created != null){
            succesSound.start();
            newItemDiscovered(created,item,itemX,itemY,itemTwo,knownItems);
        } else{
            if(itemTwo.canBeRemoved()){
                reactionView.removeView(itemTwo);
                reactionView.addView(itemTwo);
                itemTwo.setX(itemTwoX);
                itemTwo.setY(itemTwoY);
                itemTwo.setVisibility(VISIBLE);
                shake(itemTwo);
            }else{
                ItemView newView = new ItemView(this,itemTwo.getItem(),this);
                reactionView.addView(newView);
                newView.setX(itemTwoX);
                newView.setY(itemTwoY);
                shake(newView);
            }
            errorSound.start();
        }
    }

    private void newItemDiscovered(Item created, ItemHolder item, float itemX,float itemY,
                                   ItemHolder itemTwo, Collection<Item> knownItems) {
        if(item.canBeRemoved()){
            ViewGroup owner = (ViewGroup) item.getParent();
            owner.removeView(item);
        }
        if(itemTwo.canBeRemoved()){
            ViewGroup owner = (ViewGroup) itemTwo.getParent();
            owner.removeView(itemTwo);
        }
        if(!knownItems.contains(created)){
            openFragment(created);
            itemLayout.addView(new ListCompleteItemView(this,created.getName(),
                    new ItemListView(this,created,this)));
        }
        ItemView newView = new ItemView(this,created,this);
        newView.setX(itemX - (newView.getWidth() / 2));
        newView.setY(itemY - (newView.getHeight() / 2));
        reactionView.addView(newView);
    }

    private void openFragment(Item newItem) {
        NewItemFragment newItemFragment = NewItemFragment.newInstance(newItem.getName(),newItem.getImagePath());
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_right,R.anim.enter_from_right,R.anim.exit_from_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.frameContainer,newItemFragment,"NEW_ITEM_FRAGMENT");
        transaction.commit();
    }

    @Override
    public void hideFragment() {
        onBackPressed();
    }

    private class TableDragListener implements View.OnDragListener {
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
                    //dragView.setVisibility(VISIBLE);
                    break;
                case DragEvent.ACTION_DROP:
                    ItemHolder view = (ItemHolder) dragView;
                    float X = event.getX();
                    float Y = event.getY();
                    if(!view.canBeRemoved()) {
                        ItemView newView = createItemView(view.getItem());
                        newView.setX(X - (view.getWidth() / 2));
                        newView.setY(Y - (view.getHeight() / 2));
                        reactionView.addView(newView);
                        System.out.println("test");
                    }else{
                        view.setX(X - (view.getWidth() / 2));
                        view.setY(Y - (view.getHeight() / 2));
                        view.setVisibility(VISIBLE);

                    }
                case DragEvent.ACTION_DRAG_ENDED:

                default:
                    break;
            }
            return true;
        }
    }

    private ItemView createItemView(Item item) {
        return new ItemView(this,item,this);
    }

    public void shake(View view){
        final Animation animShake = AnimationUtils.loadAnimation(this,R.anim.shake);
        view.startAnimation(animShake);
    }
}
