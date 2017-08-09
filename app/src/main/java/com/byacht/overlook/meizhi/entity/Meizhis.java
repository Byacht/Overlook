package com.byacht.overlook.meizhi.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dn on 2017/8/6.
 */

public class Meizhis {
    public boolean error;
    @SerializedName("results")
    public ArrayList<Meizhi> meizhis;

    public ArrayList<Meizhi> getMeizhis() {
        return meizhis;
    }

    public void setMeizhis(ArrayList<Meizhi> meizhis) {
        this.meizhis = meizhis;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
