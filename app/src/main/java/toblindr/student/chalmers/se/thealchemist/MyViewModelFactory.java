package toblindr.student.chalmers.se.thealchemist;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class MyViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String items;
    private String reactions;


    public MyViewModelFactory(Application application, String items, String reactions) {
        mApplication = application;
        this.items = items;
        this.reactions=reactions;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AlchemistViewModel(mApplication, items,reactions);
    }
}
