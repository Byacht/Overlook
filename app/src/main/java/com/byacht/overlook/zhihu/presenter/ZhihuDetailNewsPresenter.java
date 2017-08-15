package com.byacht.overlook.zhihu.presenter;

import com.byacht.overlook.zhihu.IZhihu;
import com.byacht.overlook.zhihu.activity.IZhihuDetailNewsActivity;
import com.byacht.overlook.zhihu.activity.ZhihuDetailNewsActivity;
import com.byacht.overlook.zhihu.entity.ZhihuDetailNews;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dn on 2017/8/7.
 */

public class ZhihuDetailNewsPresenter implements IZhihuDetailNewsPresenter {
    private IZhihuDetailNewsActivity mZhihuDetailNewsActivity;

    public ZhihuDetailNewsPresenter(IZhihuDetailNewsActivity activity) {
        mZhihuDetailNewsActivity = activity;
    }

    @Override
    public void getZhihuDetailNews(String id) {
        IZhihu zhihu = new Retrofit.Builder()
                .baseUrl("http://news-at.zhihu.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IZhihu.class);
        zhihu.getZhihuDetailNews(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ZhihuDetailNews>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mZhihuDetailNewsActivity.showError(e.toString());
                    }

                    @Override
                    public void onNext(ZhihuDetailNews zhihuDetailNews) {
                        mZhihuDetailNewsActivity.showZhihuDetailNews(zhihuDetailNews);
                    }
                });
    }
}
