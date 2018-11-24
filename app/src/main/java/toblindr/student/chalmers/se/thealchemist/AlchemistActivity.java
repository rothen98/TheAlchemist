package toblindr.student.chalmers.se.thealchemist;


import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import toblindr.student.chalmers.se.thealchemist.model.Facade;
import toblindr.student.chalmers.se.thealchemist.model.Item;
import toblindr.student.chalmers.se.thealchemist.model.Reaction;

import static android.view.View.VISIBLE;

public class AlchemistActivity extends AppCompatActivity implements IItemParentController, NewItemFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener {
    //Graphic
    private LinearLayout itemLayout;
    private ConstraintLayout reactionView;
    private Button settingsButton;
    private Button clearButton;
    //Music
    private MediaPlayer errorSound;
    private MediaPlayer succesSound;
    private MediaPlayer blopSound;
    private MediaPlayer popSound;
    //Model
    private Facade facade;
    //Services
    private DataHandler dataHandler;
   //Constants
    private final static String FILENAME = "alchemist.txt";


    public AlchemistActivity() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        saveAllData();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alchemist);
        dataHandler = new DataHandler(getApplicationContext());
        facade = new Facade(initReactions(), initItems());

        this.errorSound = MediaPlayer.create(this, R.raw.wrong);
        this.succesSound = MediaPlayer.create(this, R.raw.succes);
        this.blopSound = MediaPlayer.create(this,R.raw.blop);
        this.popSound = MediaPlayer.create(this,R.raw.blop);

        this.reactionView = findViewById(R.id.reaction_view);
        this.itemLayout = findViewById(R.id.itemLayout);
        this.settingsButton = findViewById(R.id.settingsButton);
        this.clearButton = findViewById(R.id.clearButton);


        initButtons();
        initItemLayout();

        this.itemLayout.setOnDragListener(new ListDragListener());
        this.reactionView.setOnDragListener(new TableDragListener());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(this, BackgroundMusicService.class);
        intent.setAction(BackgroundMusicService.PAUSE);
        startService(intent);

    }

    @Override
    public void onResume() {
        super.onResume();
        startService(new Intent(this, BackgroundMusicService.class));
    }

    //-------Data handling--------------------------------------------------------

    private void saveAllData(){
        StringBuilder sb = new StringBuilder();
        for (Reaction r : facade.getAllUnknownReactions()) {
            for(Item item:r.getReactants()){
                String s = item.getName() + "+";
                sb.append(s);
            }
            sb.setLength(sb.length() - 1);
            sb.append("=");
            for(Item item:r.getProducts()){
                String s = item.getName() + "+";
                sb.append(s);
            }
            sb.setLength(sb.length() - 1);
            sb.append("\n");
        }

        for (Reaction r : facade.getAllKnownReactions()) {
            for(Item item:r.getReactants()){
                String s = item.getName() + "+";
                sb.append(s);
            }
            sb.setLength(sb.length() - 1);
            sb.append("=");
            for(Item item:r.getProducts()){
                String s = item.getName() + "+";
                sb.append(s);
            }
            sb.setLength(sb.length() - 1);
            sb.append(":true");
            sb.append("\n");
        }

        dataHandler.writeToPhoneFile(FILENAME,sb.toString());
        Log.i("Info", "Data saved");
    }
    private Collection<String> initItems() {

        List<String> itemsInfo = dataHandler.readRawTextFile(R.raw.items,getResources());
        return itemsInfo;
    }

    private Collection<String> initReactions() {
        //return dataHandler.readRawTextFile(R.raw.reactions,getResources());
        List<String> reactionsPhone = dataHandler.readPhoneFile(FILENAME);//readFile();
        List<String> reactionsRawFile = dataHandler.readRawTextFile(R.raw.reactions,getResources());
        return combineReactions(reactionsPhone,reactionsRawFile);
    }

    private Collection<String> combineReactions(Collection<String> phoneFiles, Collection<String> rawFiles){
        Collection<String> toReturn = new ArrayList<>();
        for(String r:phoneFiles){
            String justReaction = r.split(":")[0];
            if(rawFiles.contains(justReaction)){
                Log.i("Info","Added: " + r);
                Log.i("Info","Removed: " + justReaction);
                toReturn.add(r);
                rawFiles.remove(justReaction);
            }
        }
        toReturn.addAll(rawFiles);

        return toReturn;
    }

    //--------------------Handle graphical components--------------------------------

    private void initItemLayout() {
        for (Item item : facade.getKnownItems()) {
            ListCompleteItemView itemView = new ListCompleteItemView(this, item.getName(),
                    new ItemListView(this, item, this));
            itemLayout.addView(itemView);
        }
        sortItemLayout();
    }

    private void sortItemLayout(){
        List<ListCompleteItemView> items = new ArrayList<>();
        for(int i=0;i<itemLayout.getChildCount();i++){
            items.add((ListCompleteItemView)itemLayout.getChildAt(i));
        }
        Collections.sort(items, new Comparator<ListCompleteItemView>() {
            @Override
            public int compare(ListCompleteItemView o1, ListCompleteItemView o2) {
                return o1.getItemName().compareTo(o2.getItemName());
            }
        });
        itemLayout.removeAllViews();
        for(ListCompleteItemView lciv:items){
            itemLayout.addView(lciv);
        }
    }

    private void initButtons() {
        initSettingsButton();
        initClearButton();
    }

    private void initClearButton() {
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reactionView.removeAllViews();
                Animation animation = AnimationUtils.loadAnimation(AlchemistActivity.this, R.anim.rotate);
                v.startAnimation(animation);
            }
        });
    }

    private void initSettingsButton() {
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("SETTINGS_FRAGMENT");
                if(fragment==null){
                    openSettingsFragment();
                }else{
                    onBackPressed();
                    /*FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.commit();
                    manager.popBackStack();*/


                }
            }
        });

    }

    private void openSettingsFragment() {
        Fragment fragment = SettingsFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.add(R.id.frameContainer,fragment,"SETTINGS_FRAGMENT");
        transaction.commit();
    }

    private void openNewItemFragment(Item newItem, String tag) {
        NewItemFragment newItemFragment = NewItemFragment.newInstance(newItem.getName(), newItem.getImagePath());
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_right, R.anim.enter_from_right, R.anim.exit_from_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.backPane, newItemFragment, tag);
        transaction.commit();
    }

    private ItemView createItemView(Item item) {
        ItemView itemView = new ItemView(this, item, this);
        return itemView;
    }

    public void shake(View view) {
        final Animation animShake = AnimationUtils.loadAnimation(this, R.anim.shake);
        view.startAnimation(animShake);
    }

    @Override
    public void hideFragment() {
        onBackPressed();
    }

    //-----------------Performing reaction------------------------------------------------------

    @Override
    public void reaction(ItemHolder item, float itemX, float itemY, ItemHolder itemTwo, float itemTwoX, float itemTwoY) {
        Collection<Item> knownItems = facade.getKnownItems();
        Collection<Reaction> knownReactions = facade.getAllKnownReactions();
        List<Item> reactants= new ArrayList<>();
        reactants.add(item.getItem());
        reactants.add(itemTwo.getItem());
        Reaction reaction = facade.tryReaction(reactants);
        if (reaction != null) {
            if(!knownReactions.contains(reaction)){
                tryToRemoveItemViews(item,itemTwo);
                newReactionDiscovered(reaction.getProducts(),itemX, itemY,knownItems);
            }else{
                oldReactionPerformed(reaction.getProducts(),itemTwoX,itemTwo.getWidth(),itemTwoY);
                handleItemOnTop(itemTwo,itemTwoX,itemTwoY);


            }
        } else {
            handleItemOnTop(itemTwo,itemTwoX,itemTwoY);
            errorSound.start();
        }
    }

    private void handleItemOnTop(ItemHolder itemTwo, float itemTwoX, float itemTwoY) {
        if (itemTwo.canBeRemoved()) {
            reactionView.removeView(itemTwo);
            reactionView.addView(itemTwo);
            itemTwo.setX(itemTwoX);
            itemTwo.setY(itemTwoY);
            itemTwo.setVisibility(VISIBLE);
            shake(itemTwo);
        } else {
            ItemView newView = createItemView(itemTwo.getItem());
            reactionView.addView(newView);
            newView.setX(itemTwoX);
            newView.setY(itemTwoY);
            shake(newView);
        }
    }

    private void tryToRemoveItemViews(ItemHolder item, ItemHolder itemTwo) {
        if (item.canBeRemoved()) {
            ViewGroup owner = (ViewGroup) item.getParent();
            owner.removeView(item);
        }
        if (itemTwo.canBeRemoved()) {
            ViewGroup owner = (ViewGroup) itemTwo.getParent();
            owner.removeView(itemTwo);
        }
    }

    private void oldReactionPerformed(Set<Item> products, float itemX, float itemWidth, float itemY) {
        blopSound.start();
        for(Item i:products){
            final SmallImageView smallview= new SmallImageView(getApplicationContext(),i);
            smallview.setX(itemX+(itemWidth-SmallImageView.SIZE)/2);
            smallview.setY(itemY-100);
            reactionView.addView(smallview);
            // Start the animation
            smallview.animate()
                    .translationYBy(-100)
                    .alpha(0.0f)
                    .setDuration(1000)
                    .setStartDelay(200)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            reactionView.removeView(smallview);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
        }
    }

    private void newReactionDiscovered(Set<Item> items, float itemX, float itemY
            ,Collection<Item> knownItems) {

        boolean playSuccesSound=false;
        for(Item i:items){
            if (!knownItems.contains(i)) {
                playSuccesSound=true;
                saveAllData();
                openNewItemFragment(i,i.getName().toUpperCase());
                itemLayout.addView(new ListCompleteItemView(this, i.getName(),
                        new ItemListView(this, i, this)));
                sortItemLayout();
            }
            ItemView newView = createItemView(i);
            newView.setX(itemX - (newView.getWidth() / 2));
            newView.setY(itemY - (newView.getHeight() / 2));
            reactionView.addView(newView);
        }
        if (playSuccesSound){
            succesSound.start();
        }else{
            popSound.start();
        }
    }

    private void newItemDiscovered(Set<Item> created, ItemHolder item, float itemX, float itemY,
                                   ItemHolder itemTwo, Collection<Item> knownItems) {
        if (item.canBeRemoved()) {
            ViewGroup owner = (ViewGroup) item.getParent();
            owner.removeView(item);
        }
        if (itemTwo.canBeRemoved()) {
            ViewGroup owner = (ViewGroup) itemTwo.getParent();
            owner.removeView(itemTwo);
        }
        for(Item i:created){
            if (!knownItems.contains(i)) {

                saveAllData();
                openNewItemFragment(i,i.getName().toUpperCase());
                itemLayout.addView(new ListCompleteItemView(this, i.getName(),
                        new ItemListView(this, i, this)));
                sortItemLayout();
            }
            ItemView newView = createItemView(i);
            newView.setX(itemX - (newView.getWidth() / 2));
            newView.setY(itemY - (newView.getHeight() / 2));
            reactionView.addView(newView);
        }



    }


    //----------------------Drag Listeners------------------------------------------

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
                    if (!view.canBeRemoved()) {
                        ItemView newView = createItemView(view.getItem());
                        newView.setX(X - (view.getWidth() / 2));
                        newView.setY(Y - (view.getHeight() / 2));
                        reactionView.addView(newView);
                        System.out.println("test");
                    } else {
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

    private class ListDragListener implements View.OnDragListener {
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
                    if (view.canBeRemoved()) {
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

    //--------------------Options-----------------------------------------------------
    @Override
    public void noMusic() {

    }

    @Override
    public void resetGame() {
        facade.resetGame();
        itemLayout.removeAllViews();
        initItemLayout();
        reactionView.removeAllViews();
    }



}
