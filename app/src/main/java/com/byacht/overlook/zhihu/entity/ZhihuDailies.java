package com.byacht.overlook.zhihu.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dn on 2017/8/6.
 */

public class ZhihuDailies {
    private String date;
    @SerializedName("stories")
    private ArrayList<ZhihuDaily> zhihuDailies;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<ZhihuDaily> getZhihuDailies() {
        return zhihuDailies;
    }

    public void setZhihuDailies(ArrayList<ZhihuDaily> stories) {
        this.zhihuDailies = zhihuDailies;
    }
}
