package com.byacht.overlook;

import com.byacht.overlook.douyutv.IDouyuTv;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dn on 2017/8/16.
 */

public class ApiManager {

    private static ApiManager mApiManager;

    private IDouyuTv douyuTv;

    private ApiManager() {

    }

    public static ApiManager getInstance() {
        if (mApiManager == null) {
            synchronized (ApiManager.class) {
                if (mApiManager == null) {
                    mApiManager = new ApiManager();
                }
            }
        }
        return mApiManager;
    }

    public IDouyuTv getIDouyuTv() {
        if (douyuTv == null) {
            douyuTv = new Retrofit.Builder()
                    .baseUrl("http://open.douyucdn.cn")
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(IDouyuTv.class);
        }
        return douyuTv;
    }
}
