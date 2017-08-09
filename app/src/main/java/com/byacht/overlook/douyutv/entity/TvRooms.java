package com.byacht.overlook.douyutv.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dn on 2017/8/8.
 */

public class TvRooms {
    private int error;
    @SerializedName("data")
    private ArrayList<TvRoom> tvRooms;

    public ArrayList<TvRoom> getTvRooms() {
        return tvRooms;
    }

    public void setTvRooms(ArrayList<TvRoom> tvRooms) {
        this.tvRooms = tvRooms;
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
        for (int i = 0; i < tvRooms.size(); i ++) {
            sb.append("roomName:" + tvRooms.get(i).getRoom_name() + "\n");
            sb.append("roomName:" + tvRooms.get(i).getRoom_src() + "\n");
        }
        return sb.toString();
    }
}
