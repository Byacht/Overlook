package com.byacht.overlook.douyutv.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dn on 2017/8/8.
 */

public class TvCategory {
    private int error;
    @SerializedName("data")
    private ArrayList<TvGame> tvGames;

    public ArrayList<TvGame> getTvGames() {
        return tvGames;
    }

    public void setTvGames(ArrayList<TvGame> tvGames) {
        this.tvGames = tvGames;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("error:" + error + "\n");
        for (int i = 0; i < tvGames.size(); i++) {
            sb.append("game name:" + tvGames.get(i).getGame_name() + "\n");
        }
        return sb.toString();
    }
}
