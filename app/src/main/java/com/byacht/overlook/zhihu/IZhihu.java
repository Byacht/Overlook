package com.byacht.overlook.zhihu;

import com.byacht.overlook.zhihu.entity.ZhihuDailies;
import com.byacht.overlook.zhihu.entity.ZhihuDetailNews;

import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by dn on 2017/8/6.
 */

public interface IZhihu {
    @HTTP(method = "GET", path = "/api/4/news/latest", hasBody = false)
    Observable<ZhihuDailies> getZhihuDailies();

    @GET("/api/4/news/{id}")
    Observable<ZhihuDetailNews> getZhihuDetailNews(@Path("id") String id);
}
