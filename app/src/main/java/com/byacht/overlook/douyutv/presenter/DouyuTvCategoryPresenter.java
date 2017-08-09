package com.byacht.overlook.douyutv.presenter;

import android.util.Log;

import com.byacht.overlook.douyutv.IDouyuTv;
import com.byacht.overlook.douyutv.activity.DouyuTvActivity;
import com.byacht.overlook.douyutv.entity.TvCategory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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
        IDouyuTv douyuTv = new Retrofit.Builder()
                .baseUrl("http://open.douyucdn.cn")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IDouyuTv.class);
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
