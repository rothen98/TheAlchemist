package toblindr.student.chalmers.se.thealchemist;


import android.animation.Animator;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import toblindr.student.chalmers.se.thealchemist.model.Item;
import toblindr.student.chalmers.se.thealchemist.model.Reaction;

import static android.view.View.VISIBLE;

public class AlchemistActivity extends AppCompatActivity implements IItemParentController, NewItemFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener {
    private static final String ITEM_ID = "item_id";
    private static final String REACTION_ID = "reaction_id";
    //Graphic
    private LinearLayout itemLayout;
    private ConstraintLayout reactionView;
    private Button settingsButton;
    private Button clearButton;
    private Button encyclopediaButton;
    private Button hintButton;
    //Music
    private MediaPlayer errorSound;
    private MediaPlayer succesSound;
    private MediaPlayer blopSound;
    private MediaPlayer popSound;
    //Model

    //Services
    private DataHandler dataHandler;
   //Constants
    private final static String FILENAME = "alchemist.txt";

    // initialize SharedPreferences var
    private SharedPreferences sharedPref;

    private AlchemistViewModel viewModel;

    //fragments
    private EncyclopediaFragment encyclopediaFragment;


    public AlchemistActivity() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        // todo saveAllData();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alchemist);
        dataHandler = new DataHandler();
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        viewModel = ViewModelProviders.of(this,
                new MyViewModelFactory(this.getApplication(),
                        sharedPref.getString(ITEM_ID,"EMPTY"),
                        sharedPref.getString(REACTION_ID,"EMPTY"))).get(AlchemistViewModel.class);

        encyclopediaFragment = EncyclopediaFragment.newInstance();

        this.errorSound = MediaPlayer.create(this, R.raw.wrong);
        this.succesSound = MediaPlayer.create(this, R.raw.succes);
        this.blopSound = MediaPlayer.create(this,R.raw.blop);
        this.popSound = MediaPlayer.create(this,R.raw.blop);

        this.reactionView = findViewById(R.id.reaction_view);
        this.itemLayout = findViewById(R.id.itemLayout);
        this.settingsButton = findViewById(R.id.settingsButton);
        this.clearButton = findViewById(R.id.clearButton);
        this.hintButton=findViewById(R.id.hintButton);
        this.encyclopediaButton=findViewById(R.id.encyclopediaButton);


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





    //--------------------Handle graphical components--------------------------------

    private void initItemLayout() {
        for (Item item : viewModel.getKnownItems()) {
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
        initEncyclopediaButton();
        initHintButton();
    }

    private void initHintButton() {
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item hint = viewModel.getHint();
                Context context = getApplicationContext();
                CharSequence text = "";

                if(hint!=null){
                    text = "The hint is: "+ hint.toString();
                }else{
                    text = "Couldn't find any hints";
                }

                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

    }

    private void initEncyclopediaButton() {
        encyclopediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentByTag("ENCYCLOPEDIA_FRAGMENT");
                if(fragment==null){
                    openEncyclopediaFragment();
                }else if(!fragment.isVisible()){
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.show(fragment);
                    transaction.commit();
                }
                else{
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.hide(fragment);
                    transaction.commit();
                }

            }
        });
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
                }
            }
        });

    }

    private void openSettingsFragment() {
        Fragment fragment = SettingsFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.add(R.id.centerPane,fragment,"SETTINGS_FRAGMENT");
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

    private void openEncyclopediaFragment(){
        EncyclopediaFragment fragment = encyclopediaFragment;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_right, R.anim.enter_from_right, R.anim.exit_from_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.centerPane, fragment, "ENCYCLOPEDIA_FRAGMENT");
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
    public void reaction(ItemHolder item, float itemX, float itemY, ItemHolder itemTwo, float itemTwoX, float itemTwoY){
        List<Item> reactants= new ArrayList<>();
        reactants.add(item.getItem());
        reactants.add(itemTwo.getItem());
        Map<Item,ItemStatus> items  = viewModel.tryReaction(reactants);
        if(!items.isEmpty()){
            for(Map.Entry<Item,ItemStatus> entry: items.entrySet()){
                if(entry.getValue()==ItemStatus.NEW){
                    unknownItem(entry.getKey(),itemX,itemY);
                }else if(entry.getValue()==ItemStatus.NEW_REACTION){
                    knownItemFromUnknownReaction(entry.getKey(),itemX,itemY);
                }else{
                    knownItemFromKnownReaction(entry.getKey(),itemTwoX,itemTwo.getWidth(),itemTwoY);

                }
            }
            if(items.values().contains(ItemStatus.NEW)){
                tryToRemoveItemViews(item,itemTwo);
                succesSound.start();
            }else if(items.values().contains(ItemStatus.NEW_REACTION)){
                tryToRemoveItemViews(item,itemTwo);
                popSound.start();
            }else{
                handleItemOnTop(itemTwo,itemTwoX,itemTwoY,false);
                blopSound.start();
            }
        }else{
            handleItemOnTop(itemTwo,itemTwoX,itemTwoY,true);
            errorSound.start();
        }

        saveData();

    }

    private void saveData(){
        sharedPref.edit().putString(ITEM_ID, viewModel.getSavedItems()).apply();
        sharedPref.edit().putString(REACTION_ID, viewModel.getSavedReactions()).apply();
    }

    private void knownItemFromKnownReaction(Item i, float itemX, float itemWidth, float itemY) {
        final SmallImageView smallview= new SmallImageView(getApplicationContext(),i);
        smallview.setX(itemX+(itemWidth-SmallImageView.SIZE)/2);
        smallview.setY(itemY);
        smallview.setAlpha(0.0f);
        reactionView.addView(smallview);
        // Start the animation
        smallview.animate()
                .translationY(itemY-SmallImageView.SIZE-20)
                .alpha(1.0f)
                .setDuration(1000)
                .setStartDelay(200)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        smallview.animate().alpha(0.0f).setDuration(200).setStartDelay(200).setListener(new Animator.AnimatorListener() {
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

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    private void knownItemFromUnknownReaction(Item i, float itemX, float itemY) {
        ItemView newView = createItemView(i);
        newView.setX(itemX - (newView.getWidth() / 2));
        newView.setY(itemY - (newView.getHeight() / 2));
        reactionView.addView(newView);
    }

    private void unknownItem(Item i, float itemX, float itemY) {
        openNewItemFragment(i,i.getName().toUpperCase());
        itemLayout.addView(new ListCompleteItemView(this, i.getName(),
                    new ItemListView(this, i, this)));
        sortItemLayout();

        ItemView newView = createItemView(i);
        newView.setX(itemX - (newView.getWidth() / 2));
        newView.setY(itemY - (newView.getHeight() / 2));
        reactionView.addView(newView);

    }

    private void handleItemOnTop(ItemHolder itemTwo, float itemTwoX, float itemTwoY,boolean shake) {
        if (itemTwo.canBeRemoved()) {
            reactionView.removeView(itemTwo);
            reactionView.addView(itemTwo);
            itemTwo.setX(itemTwoX);
            itemTwo.setY(itemTwoY);
            itemTwo.setVisibility(VISIBLE);
            if(shake) {
                shake(itemTwo);
            }
        } else {
            ItemView newView = createItemView(itemTwo.getItem());
            reactionView.addView(newView);
            newView.setX(itemTwoX);
            newView.setY(itemTwoY);
            if(shake) {
                shake(newView);
            }
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
        viewModel.resetGame();
        itemLayout.removeAllViews();
        initItemLayout();
        reactionView.removeAllViews();
        saveData();
    }



}
