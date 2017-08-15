package com.byacht.overlook.zhihu.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.byacht.overlook.R;
import com.byacht.overlook.util.MyUtils;
import com.byacht.overlook.util.WebUtil;
import com.byacht.overlook.zhihu.entity.ZhihuDetailNews;
import com.byacht.overlook.zhihu.presenter.ZhihuDetailNewsPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZhihuDetailNewsActivity extends AppCompatActivity implements IZhihuDetailNewsActivity {

    @BindView(R.id.wv_zhihu_detail_news)
    WebView mWvZhihuDetailNews;
    @BindView(R.id.toolbar_zhihu_detail)
    Toolbar mToolbarZhihuDetail;
    @BindView(R.id.img_zhihu_detail)
    ImageView mImgZhihuDetail;
    @BindView(R.id.toolbar_layout_zhihu)
    CollapsingToolbarLayout mToolbarLayoutZhihu;

    private ZhihuDetailNewsPresenter mZhihuPresenter;
    private String mId;
    private String mImgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_zhihu_detail_news);
        ButterKnife.bind(this);
        setupTransition();
        getData();
    }

    private void setupTransition() {
        if (Build.VERSION.SDK_INT >= 21) {
            Explode explode = new Explode();
            explode.setDuration(800);
            getWindow().setEnterTransition(explode);

            Slide slide = new Slide();
            slide.setDuration(800);
            slide.setSlideEdge(Gravity.RIGHT);
            getWindow().setReturnTransition(slide);
        }
    }

    private void getData() {
        mId = getIntent().getStringExtra("id");
        mZhihuPresenter = new ZhihuDetailNewsPresenter(this);
        mZhihuPresenter.getZhihuDetailNews(mId);
    }

    private void setupToolbar() {
        String title = getIntent().getStringExtra("title");
        mToolbarLayoutZhihu.setTitle(title);
        //设置 toolbar 拉伸和收缩时标题的颜色
        mToolbarLayoutZhihu.setExpandedTitleColor(Color.WHITE);
        mToolbarLayoutZhihu.setCollapsedTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbarZhihuDetail);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void showZhihuDetailNews(ZhihuDetailNews zhihuDetailNews) {
        //加载 toolbar 的图片
        mImgUrl = zhihuDetailNews.getImage();
        Glide.with(this)
                .load(mImgUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mImgZhihuDetail);

        String data = WebUtil.buildHtmlWithCss(zhihuDetailNews.getBody(), zhihuDetailNews.getCss(), false);
        mWvZhihuDetailNews.loadDataWithBaseURL(WebUtil.BASE_URL, data, WebUtil.MIME_TYPE, WebUtil.ENCODING, WebUtil.FAIL_URL);

        setupToolbar();
    }

    @Override
    public void showError(String error) {
        MyUtils.Snackbar(mWvZhihuDetailNews, "网络请求出错,请检查网络状态", "重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mZhihuPresenter.getZhihuDetailNews(mId);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toolbar 返回键
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
