package com.byacht.overlook.douyutv.entity;

import java.util.ArrayList;

/**
 * Created by dn on 2017/8/9.
 */

public class TvView {
    private TvGame mTvGame;
    private ArrayList<TvRoom> mTvRooms;

    public TvGame getTvGame() {
        return mTvGame;
    }

    public void setTvGame(TvGame tvGame) {
        mTvGame = tvGame;
    }

    public ArrayList<TvRoom> getTvRooms() {
        return mTvRooms;
    }

    public void setTvRooms(ArrayList<TvRoom> tvRooms) {
        mTvRooms = tvRooms;
    }

}
