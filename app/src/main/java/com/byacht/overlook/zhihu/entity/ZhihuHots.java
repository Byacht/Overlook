package com.byacht.overlook.zhihu.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dn on 2017/8/10.
 */

public class ZhihuHots {
    @SerializedName("recent")
    private ArrayList<ZhihuHot> zhihuHots;

    public ArrayList<ZhihuHot> getZhihuHots() {
        return zhihuHots;
    }

    public void setZhihuHots(ArrayList<ZhihuHot> zhihuHots) {
        this.zhihuHots = zhihuHots;
    }


}
