package com.byacht.overlook.douyutv.presenter;

import android.database.Observable;
import android.util.Log;

import com.byacht.overlook.ApiManager;
import com.byacht.overlook.douyutv.IDouyuTv;
import com.byacht.overlook.douyutv.activity.DouyuTvActivity;
import com.byacht.overlook.douyutv.entity.TvCategory;
import com.byacht.overlook.douyutv.entity.TvRooms;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by dn on 2017/8/8.
 */

public class DouyuTvCategoryPresenter implements IDouyuTvCategoryPresenter {
    private DouyuTvActivity mDouyuTvActivity;

    public DouyuTvCategoryPresenter(DouyuTvActivity activity) {
        mDouyuTvActivity = activity;
    }
    @Override
    public void getTvCategory() {
        IDouyuTv douyuTv = ApiManager.getInstance().getIDouyuTv();
        douyuTv.getTvGames()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TvCategory>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("htout", "222:" + e.toString());
                    }

                    @Override
                    public void onNext(TvCategory tvCategory) {
                        mDouyuTvActivity.showAllCategories(tvCategory);
                    }
                });

    }
}
