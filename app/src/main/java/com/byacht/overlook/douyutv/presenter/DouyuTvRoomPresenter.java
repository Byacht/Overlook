package com.byacht.overlook.douyutv.presenter;

import android.util.Log;

import com.byacht.overlook.ApiManager;
import com.byacht.overlook.douyutv.IDouyuTv;
import com.byacht.overlook.douyutv.activity.DouyuTvActivity;
import com.byacht.overlook.douyutv.activity.IDouyuTvActivity;
import com.byacht.overlook.douyutv.entity.TvRoom;
import com.byacht.overlook.douyutv.entity.TvRooms;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dn on 2017/8/8.
 */

public class DouyuTvRoomPresenter implements IDouyuTvRoomPresenter {
    private IDouyuTvActivity mDouyuTvActivity;
    private int categorySize = 0;
    private int actualSize = 0;

    public void setCategorySize(int size) {
        categorySize = size;
    }

    public void setActualSize(int size) {
        actualSize = size;
    }

    public DouyuTvRoomPresenter(IDouyuTvActivity activity) {
        mDouyuTvActivity = activity;
    }
    @Override
    public void getTvRooms(String gameName) {
        IDouyuTv douyuTv = ApiManager.getInstance().getIDouyuTv();
        douyuTv.getRooms(gameName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TvRooms>() {
                    @Override
                    public void onCompleted() {
                        actualSize++;
                        if (actualSize == categorySize) {
                            mDouyuTvActivity.show();
                        }
                        Log.d("htout", "actualSize:" + actualSize);
                    }

                    @Override
                    public void onError(Throwable e) {
                        actualSize++;
                        if (actualSize == categorySize) {
                            mDouyuTvActivity.show();
                        }
                    }

                    @Override
                    public void onNext(TvRooms tvRooms) {
                        mDouyuTvActivity.showRooms(tvRooms);
                    }
                });
    }
}
