package com.avoma.rssfeed.main;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.avoma.rssfeed.model.Item;
import com.avoma.rssfeed.utils.Resource;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private final MainRepository repository;

    private WeakReference<Context> weakReference;
    public MainViewModel(Application application) {
        super(application);

        weakReference = new WeakReference<>(application.getApplicationContext());
        repository = new MainRepository();
    }

    LiveData<Resource<List<Item>>> getLiveData() {
        return repository.getFeedLiveData();
    }

    void getData(){
        repository.getDetails(weakReference.get());
    }

    void editData(Boolean isBookmarked, int id){
        repository.editDetails(weakReference.get(), isBookmarked, id);
    }
}