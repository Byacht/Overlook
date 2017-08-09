package com.byacht.overlook.meizhi.presenter;

import android.util.Log;
import android.view.View;

import com.byacht.overlook.meizhi.IMeizhi;
import com.byacht.overlook.meizhi.entity.Meizhis;
import com.byacht.overlook.meizhi.fragment.MeizhiFragment;

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
 * Created by dn on 2017/8/6.
 */

public class MeizhiPresenter implements IMeizhiPresenter {

    private MeizhiFragment mMeizhiFragment;

    public MeizhiPresenter(MeizhiFragment fragment) {
        mMeizhiFragment = fragment;
    }

    @Override
    public void getMeizhis() {
        Log.d("out", "111");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        IMeizhi meizhi = retrofit.create(IMeizhi.class);
//        meizhi.getMeizhis(1)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Meizhis>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d("out", "111:" + e.toString());
//                    }
//                    @Override
//                    public void onNext(Meizhis meizhis) {
//                        mMeizhiFragment.showMeizhis(meizhis);
//                        Log.d("out", meizhis.toString());
//            }
//        });
        Call<Meizhis> call = meizhi.getMeiZhi(1);
        call.enqueue(new Callback<Meizhis>() {
            @Override
            public void onResponse(Call<Meizhis> call, Response<Meizhis> response) {
                mMeizhiFragment.showMeizhis(response.body());
            }

            @Override
            public void onFailure(Call<Meizhis> call, Throwable t) {
                Log.d("out", "fail:" + t.toString());
            }
        });
    }
}
