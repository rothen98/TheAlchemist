package toblindr.student.chalmers.se.thealchemist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import toblindr.student.chalmers.se.thealchemist.model.Item;

public class ItemFragment extends Fragment {

    private AlchemistViewModel mViewModel;
    private LinearLayout itemList;


    public static ItemFragment newInstance() {
        return new ItemFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.item_fragment, container, false);
        itemList=v.findViewById(R.id.item_list);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(AlchemistViewModel.class);
        initItemList();


        // Create the observer which updates the UI.
        final Observer<Item> latestItemDiscovered = new Observer<Item>() {
            @Override
            public void onChanged(@Nullable Item item) {
                ItemEncyclopediaListView view = createItemEncyclopediaListView(item);
                itemList.addView(view);
            }

        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mViewModel.getLatestDiscoveredItem().observe(this, latestItemDiscovered);

        final Observer<Integer> numberOfItemsDiscovered = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer numberOfItems) {
                if(numberOfItems!=null){
                    if(itemList.getChildCount()>numberOfItems) {
                        itemList.removeAllViews();
                        initItemList();
                }


                }
            }
        };

        mViewModel.getCurrentAmountOfKnownItems().observe(this,numberOfItemsDiscovered);

    }

    private void initItemList() {
        for(Item i:mViewModel.getKnownItems()){
            ItemEncyclopediaListView view = createItemEncyclopediaListView(i);
            itemList.addView(view);
        }
    }

    private ItemEncyclopediaListView createItemEncyclopediaListView(Item i) {
        ItemEncyclopediaListView c = new ItemEncyclopediaListView(getContext());
        c.setItem(i);
        return c;
    }


}
