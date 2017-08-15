package com.byacht.overlook.zhihu.fragment;

import com.byacht.overlook.zhihu.entity.ZhihuDailies;
import com.byacht.overlook.zhihu.entity.ZhihuHots;

/**
 * Created by dn on 2017/8/6.
 */

public interface IZhihuFragment {
    void showZhihuDailies(ZhihuDailies zhihuDailies);

    void showZhihuHot(ZhihuHots zhihuHots);

    void showError(String error);
}
