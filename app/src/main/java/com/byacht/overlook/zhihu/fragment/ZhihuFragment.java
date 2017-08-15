package com.byacht.overlook.zhihu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.byacht.overlook.R;
import com.byacht.overlook.util.MyUtils;
import com.byacht.overlook.zhihu.GlideLoader;
import com.byacht.overlook.zhihu.ItemDivider;
import com.byacht.overlook.zhihu.activity.IZhihuDetailNewsActivity;
import com.byacht.overlook.zhihu.adapter.ZhihuDailyAdapter;
import com.byacht.overlook.zhihu.entity.ZhihuDailies;
import com.byacht.overlook.zhihu.entity.ZhihuDaily;
import com.byacht.overlook.zhihu.entity.ZhihuDetailNews;
import com.byacht.overlook.zhihu.entity.ZhihuHot;
import com.byacht.overlook.zhihu.entity.ZhihuHots;
import com.byacht.overlook.zhihu.presenter.ZhihuDetailNewsPresenter;
import com.byacht.overlook.zhihu.presenter.ZhihuPresenter;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dn on 2017/8/5.
 */

public class ZhihuFragment extends Fragment implements IZhihuFragment, IZhihuDetailNewsActivity {

    @BindView(R.id.rv_zhihu_daily)
    RecyclerView mRvZhihuDaily;
    @BindView(R.id.refresh_zhihu)
    SwipeRefreshLayout mRefreshLayout;

    private ZhihuDailyAdapter mZhihuDailyAdapter;
    private ZhihuPresenter mZhihuPresenter;
    private ZhihuDetailNewsPresenter mDetailNewsPresenter;

    private ArrayList<ZhihuDaily> mZhihuDailies;
    private ArrayList<ZhihuDetailNews> mZhihuDetailNews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zhihu, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        getData();

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mZhihuDailies.clear();
                mZhihuDetailNews.clear();
                mZhihuDailyAdapter.notifyDataSetChanged();
                mZhihuPresenter.getZhihuHot();
                mZhihuPresenter.getZhihuDailies();
            }
        });
    }

    private void init() {
        mZhihuDailies = new ArrayList<ZhihuDaily>();
        mZhihuDetailNews = new ArrayList<ZhihuDetailNews>();
        mZhihuDailyAdapter = new ZhihuDailyAdapter(getContext(), mZhihuDailies, mZhihuDetailNews);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRvZhihuDaily.setLayoutManager(layoutManager);
        ItemDivider itemDivider = new ItemDivider(this.getContext());
        mRvZhihuDaily.addItemDecoration(itemDivider);
        mRvZhihuDaily.setAdapter(mZhihuDailyAdapter);
    }

    private void getData() {
        mDetailNewsPresenter = new ZhihuDetailNewsPresenter(this);
        mZhihuPresenter = new ZhihuPresenter(this);
        mZhihuPresenter.getZhihuHot();
        mZhihuPresenter.getZhihuDailies();
    }

    @Override
    public void showZhihuHot(ZhihuHots zhihuHots) {
        //获取前五条知乎热点
        for (int i = 0; i < 5; i++) {
            mDetailNewsPresenter.getZhihuDetailNews(zhihuHots.getZhihuHots().get(i).getNews_id());
        }
    }

    @Override
    public void showZhihuDailies(ZhihuDailies zhihuDailies) {
        mZhihuDailies.addAll(zhihuDailies.getZhihuDailies());
        mZhihuDailyAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showZhihuDetailNews(ZhihuDetailNews zhihuDetailNews) {
        mZhihuDetailNews.add(zhihuDetailNews);
        if (mZhihuDetailNews.size() == 5) {
            mZhihuDailyAdapter.notifyDataSetChanged();
        }
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String error) {
        MyUtils.Snackbar(mRvZhihuDaily, "网络请求出错,请检查网络状态", "重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mZhihuPresenter.getZhihuHot();
                mZhihuPresenter.getZhihuDailies();
            }
        });
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
