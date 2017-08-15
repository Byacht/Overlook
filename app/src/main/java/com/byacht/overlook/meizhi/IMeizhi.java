package com.byacht.overlook.meizhi;

import com.byacht.overlook.meizhi.entity.Meizhis;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by dn on 2017/8/6.
 */

public interface IMeizhi {

    @HTTP(method = "GET", path = "/api/data/福利/10/{page}", hasBody = false)
    Observable<Meizhis> getMeizhis(@Path("page") int page);
}
