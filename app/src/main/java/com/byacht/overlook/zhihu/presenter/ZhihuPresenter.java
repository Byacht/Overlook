package com.byacht.overlook.zhihu.presenter;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.byacht.overlook.zhihu.IZhihu;
import com.byacht.overlook.zhihu.entity.ZhihuDailies;
import com.byacht.overlook.zhihu.entity.ZhihuDaily;
import com.byacht.overlook.zhihu.entity.ZhihuDetailNews;
import com.byacht.overlook.zhihu.entity.ZhihuHots;
import com.byacht.overlook.zhihu.fragment.IZhihuFragment;
import com.byacht.overlook.zhihu.fragment.ZhihuFragment;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dn on 2017/8/6.
 */

public class ZhihuPresenter implements IZhihuPresenter{
    private IZhihuFragment zhihuDailyFragment;

    public ZhihuPresenter(IZhihuFragment fragment) {
        zhihuDailyFragment = fragment;
    }

    @Override
    public void getZhihuDailies() {
        IZhihu zhihu = new Retrofit.Builder()
                .baseUrl("http://news-at.zhihu.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IZhihu.class);

        zhihu.getZhihuDailies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ZhihuDailies>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        zhihuDailyFragment.showError(e.toString());
                    }

                    @Override
                    public void onNext(ZhihuDailies zhihuDailies) {
                        zhihuDailyFragment.showZhihuDailies(zhihuDailies);
                    }
                });

    }

    @Override
    public void getZhihuHot() {
        IZhihu zhihu  = new Retrofit.Builder()
                .baseUrl("http://news-at.zhihu.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IZhihu.class);

        zhihu.getZhihuHot()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ZhihuHots>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ZhihuHots zhihuHots) {
                        zhihuDailyFragment.showZhihuHot(zhihuHots);
                    }
                });
    }

}
