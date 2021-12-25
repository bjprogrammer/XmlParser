package com.avoma.rssfeed.main;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.avoma.rssfeed.database.DataDao;
import com.avoma.rssfeed.database.DatabaseCreator;
import com.avoma.rssfeed.model.Feed;
import com.avoma.rssfeed.model.Item;
import com.avoma.rssfeed.network.RetrofitInterfaces;
import com.avoma.rssfeed.utils.Resource;
import com.avoma.rssfeed.utils.Status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainRepository {
    private final Retrofit retrofit = RetrofitInterfaces.newInstance();
    private final RetrofitInterfaces.AdminService adminService = retrofit.create(RetrofitInterfaces.AdminService.class);

    private final MutableLiveData<Resource<List<Item>>> liveData = new MutableLiveData<>();

    public void getDetails(Context context){
        DataDao dataDao  = DatabaseCreator.getAppDatabase(context).getDataDAO();

        dataDao.getFeeds()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MaybeObserver<List<Item>>() {
                @Override
                public void onSubscribe(Disposable d) { }
                @Override
                public void onSuccess(@NonNull List<Item> data) {
                    if(data.isEmpty()){
                        fetchData(context);
                    }else {
                        liveData.setValue(new Resource<>(Status.SUCCESS, data, null));
                    }
                }

                @Override
                public void onError(Throwable error) {
                    String errorMessage = error.getMessage();
                    liveData.setValue(new Resource<>(Status.ERROR, null,errorMessage));
                }

                @Override
                public void onComplete() { }
            });
    }

    private void fetchData(Context context){
        Observable<Feed> backChannelObservable = adminService.getBackChannelData();
        Observable<Feed> economistObservable = adminService.getEconomistData();
        Observable<Feed> matterObservable = adminService.getMatterData();

        Observable.zip(backChannelObservable, economistObservable, matterObservable, (feed, feed2, feed3) -> {
            List<Item> items = new ArrayList<>();
            items.addAll(feed.getItem());
            items.addAll(feed2.getItem());
            items.addAll(feed3.getItem());
            return items;
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<List<Item>>() {
                @Override
                public void onSubscribe(Disposable d) { }

                @Override
                public void onNext(List<Item> data) {
                    Collections.sort(data);
                    insertData(context,data);
                }

                @Override
                public void onError(Throwable error) {
                    String errorMessage = error.getMessage();
                    liveData.setValue(new Resource<>(Status.ERROR, null,errorMessage));
                }

                @Override
                public void onComplete() { }
            });
    }


    MutableLiveData<Resource<List<Item>>> getFeedLiveData() {
        return liveData;
    }

    public void insertData(Context context, List<Item> data){
        DataDao dataDao  = DatabaseCreator.getAppDatabase(context).getDataDAO();

        dataDao.insert(data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MaybeObserver<List<Long>>() {
                @Override
                public void onSubscribe(Disposable d) { }

                @Override
                public void onSuccess(@NonNull List<Long> index) {
                    liveData.setValue(new Resource<>(Status.SUCCESS,data, null));
                }

                @Override
                public void onError(Throwable error) {
                    String errorMessage = error.getMessage();
                    liveData.setValue(new Resource<>(Status.ERROR, null,errorMessage));
                }

                @Override
                public void onComplete() { }
            });
    }

    public void editDetails(Context context, Boolean isBookmarked, int id){
        DataDao dataDao  = DatabaseCreator.getAppDatabase(context).getDataDAO();

        dataDao.editNote(isBookmarked,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<Void>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onSuccess(@NonNull Void data) { }

                    @Override
                    public void onError(Throwable error) { }

                    @Override
                    public void onComplete() { }
                });
    }

}
