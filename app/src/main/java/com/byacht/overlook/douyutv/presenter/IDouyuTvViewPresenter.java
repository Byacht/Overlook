package com.byacht.overlook.douyutv.presenter;

import com.byacht.overlook.douyutv.entity.TvCategory;
import com.byacht.overlook.douyutv.entity.TvRooms;

import java.util.List;

import rx.Observable;

/**
 * Created by dn on 2017/9/6.
 */

public interface IDouyuTvViewPresenter {
    void getTvView(Observable<TvCategory> tvCategoryObservable, Observable<List<TvRooms>> tvRoomObservable);
}
