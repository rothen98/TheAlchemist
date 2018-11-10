package toblindr.student.chalmers.se.thealchemist;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;


import java.util.List;

import toblindr.student.chalmers.se.thealchemist.model.Facade;

public class WorkerFragment extends Fragment {
    private List<ItemView> itemViews;
    private Facade facade;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setData(List<ItemView> itemViews,Facade facade){
        this.itemViews=itemViews;
        this.facade = facade;
    }



    public List<ItemView> getItemViews() {
        return itemViews;
    }

    public Facade getFacade() {
        return facade;
    }
}
