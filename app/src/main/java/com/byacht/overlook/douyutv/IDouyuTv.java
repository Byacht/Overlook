package com.byacht.overlook.douyutv;

import com.byacht.overlook.douyutv.entity.TvCategory;
import com.byacht.overlook.douyutv.entity.TvRoom;
import com.byacht.overlook.douyutv.entity.TvRooms;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by dn on 2017/8/8.
 */

public interface IDouyuTv {
    @GET("/api/RoomApi/game")
    Observable<TvCategory> getTvGames();

    @GET("/api/RoomApi/live/{name}")
    Observable<TvRooms> getRooms(@Path("name") String gameName);

}
