package com.byacht.overlook.douyutv.presenter;

import com.byacht.overlook.douyutv.entity.TvCategory;
import com.byacht.overlook.douyutv.entity.TvGame;
import com.byacht.overlook.douyutv.entity.TvRoom;
import com.byacht.overlook.douyutv.entity.TvRooms;
import com.byacht.overlook.douyutv.entity.TvView;


import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.functions.Func2;

/**
 * Created by dn on 2017/9/6.
 */

public class DouyuTvViewPresenter implements IDouyuTvViewPresenter {
    @Override
    public void getTvView(Observable<TvCategory> tvCategoryObservable, Observable<List<TvRooms>> tvRoomObservable) {
        Observable.zip(tvCategoryObservable, tvRoomObservable, new Func2<TvCategory, List<TvRooms>, List<TvView>>() {
            @Override
            public List<TvView> call(TvCategory tvCategory, List<TvRooms> tvRoomsList) {
                List<TvView> tvViews = new ArrayList<TvView>();
                for(int i = 0; i < tvCategory.getTvGames().size(); i++) {
                    TvGame tvGame = tvCategory.getTvGames().get(i);
                    TvRooms tvRooms = tvRoomsList.get(i);
                    ArrayList<TvRoom> newTvRooms = new ArrayList<TvRoom>();
                    if (tvRooms.getTvRooms().size() >= 4){
                        for (int j = 0; j < 4; j++) {
                            newTvRooms.add(tvRooms.getTvRooms().get(j));
                        }
                    } else {
                        for (int j = 0; j < tvRooms.getTvRooms().size(); j++) {
                            newTvRooms.add(tvRooms.getTvRooms().get(j));
                        }
                    }
                    TvView tvView = new TvView();
                    tvView.setTvGame(tvGame);
                    tvView.setTvRooms(newTvRooms);
                    tvViews.add(tvView);
                }
                return tvViews;
            }
        }).subscribe(new Observer<List<TvView>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<TvView> tvViews) {

            }
        });
    }
}
