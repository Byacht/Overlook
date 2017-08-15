package com.byacht.overlook.meizhi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.byacht.overlook.R;
import com.byacht.overlook.meizhi.adapter.MeizhiAdapter;
import com.byacht.overlook.meizhi.entity.Meizhi;
import com.byacht.overlook.meizhi.entity.Meizhis;
import com.byacht.overlook.meizhi.presenter.MeizhiPresenter;
import com.byacht.overlook.util.MyUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dn on 2017/8/5.
 */

public class MeizhiFragment extends Fragment implements IMeizhiFragment {

    @BindView(R.id.rv_meizhi)
    RecyclerView mRvMeizhi;
    @BindView(R.id.refresh_meizhi)
    SwipeRefreshLayout mRefreshLayout;
    Unbinder unbinder;

    private MeizhiPresenter mMeizhiPresenter;
    private MeizhiAdapter mMeizhiAdapter;

    private ArrayList<Meizhi> mMeizhis;
    private ArrayList<Integer> mImageHeight;  //保存妹纸图片的随机高度
    private int mPage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meizhi, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        getData();
        //下拉刷新时清除数据，从网络上获取最新的数据
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                mMeizhis.clear();
                mImageHeight.clear();
                mMeizhiAdapter.notifyDataSetChanged();
                mMeizhiPresenter.getMeizhis(mPage);
            }
        });
    }

    private void init() {
        mMeizhis = new ArrayList<Meizhi>();
        mImageHeight = new ArrayList<Integer>();
        mMeizhiAdapter = new MeizhiAdapter(getContext(), mMeizhis, mImageHeight);

        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRvMeizhi.setLayoutManager(layoutManager);
        mRvMeizhi.setAdapter(mMeizhiAdapter);

        mRvMeizhi.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //mMeizhis.size() > 0 此条件是为了防止 Rv 一开始没有数据时列表为空，误判为已滑到底端
                if (isSlideToBottom(recyclerView) && mMeizhis.size() > 0){
                    mPage++;
                    mMeizhiPresenter.getMeizhis(mPage);    //滑动到底端，从网络上获取新的数据
                }
                return;
            }
        });
    }

    private void getData() {
        mMeizhiPresenter = new MeizhiPresenter(this);
        mMeizhiPresenter.getMeizhis(mPage);
    }

    //判断 Rv 是否滑动到底端
    private boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    @Override
    public void showMeizhis(Meizhis meizhis) {
        //设置图片随机高度
        for (int i = 0; i < meizhis.getMeizhis().size(); i++){
            mImageHeight.add((int) (350 + Math.random() * 300));
        }
        mMeizhis.addAll(meizhis.getMeizhis());
        mMeizhiAdapter.notifyItemRangeChanged((mPage - 1) * 10, 10);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String error) {
        MyUtils.Snackbar(mRvMeizhi, "网络请求出错,请检查网络状态", "重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMeizhiPresenter.getMeizhis(mPage);
            }
        });
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
