package com.byacht.overlook.zhihu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.byacht.overlook.R;
import com.byacht.overlook.zhihu.adapter.ZhihuDailyAdapter;
import com.byacht.overlook.zhihu.entity.ZhihuDailies;
import com.byacht.overlook.zhihu.presenter.ZhihuPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dn on 2017/8/5.
 */

public class ZhihuFragment extends Fragment implements IZhihuFragment {

    @BindView(R.id.rv_zhihu_daily)
    RecyclerView mRvZhihuDaily;

    private ZhihuDailyAdapter mZhihuDailyAdapter;
    private ZhihuPresenter mZhihuPresenter;

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRvZhihuDaily.setLayoutManager(layoutManager);
        mZhihuPresenter = new ZhihuPresenter(this);
        mZhihuPresenter.getZhihuDailies();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showZhihuDailies(ZhihuDailies zhihuDailies) {
        if (zhihuDailies != null) {
            mZhihuDailyAdapter = new ZhihuDailyAdapter(getContext(), zhihuDailies.getZhihuDailies());
            mRvZhihuDaily.setAdapter(mZhihuDailyAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
