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
import android.widget.TextView;

public class StatsFragment extends Fragment {

    private AlchemistViewModel mViewModel;
    private TextView knownItemsRatio;


    public static StatsFragment newInstance() {
        return new StatsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.stats_fragment, container, false);
        knownItemsRatio = v.findViewById(R.id.known_items_ratio);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(AlchemistViewModel.class);
        initKnownItemsRatio();


    }

    @Override
    public void onResume() {
        super.onResume();
        initKnownItemsRatio();
    }

    private void initKnownItemsRatio() {
        String ratio = String.valueOf(mViewModel.getNumberOfDiscoveredItems()) + "/" +
                String.valueOf(mViewModel.getTotalNumberOfItems())+ "\n" + "Items Found";
        knownItemsRatio.setText(ratio);

        final int total = mViewModel.getTotalNumberOfItems();
        // Create the observer which updates the UI.
        final Observer<Integer> numberOfItems = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer number) {
                // Update the UI, in this case, a TextView.
                String toShow = number+"/"+total+"\n"+"Items Found";
                knownItemsRatio.setText(toShow);
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mViewModel.getCurrentAmountOfKnownItems().observe(this, numberOfItems);
    }

}
