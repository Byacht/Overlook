package com.byacht.overlook.zhihu.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.byacht.overlook.R;
import com.byacht.overlook.zhihu.presenter.ZhihuDetailNewsPresenter;
import com.byacht.overlook.zhihu.presenter.ZhihuPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZhihuDetailNewsActivity extends AppCompatActivity implements IZhihuDetailNewsActivity{

    @BindView(R.id.wv_zhihu_detail_news)
    WebView mWvZhihuDetailNews;

    private ZhihuDetailNewsPresenter mZhihuPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu_detail_news);
        ButterKnife.bind(this);

        mZhihuPresenter = new ZhihuDetailNewsPresenter(this);
        String id = getIntent().getStringExtra("id");
        mZhihuPresenter.getZhihuDetailNews(id);
    }

    @Override
    public void showZhihuDetailNews(String url) {
        mWvZhihuDetailNews.loadUrl(url);
    }
}
