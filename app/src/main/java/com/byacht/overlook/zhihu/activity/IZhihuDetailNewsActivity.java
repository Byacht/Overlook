package com.byacht.overlook.zhihu.activity;

import com.byacht.overlook.zhihu.entity.ZhihuDetailNews;

/**
 * Created by dn on 2017/8/7.
 */

public interface IZhihuDetailNewsActivity {
    void showZhihuDetailNews(ZhihuDetailNews zhihuDetailNews);

    void showError(String error);
}
