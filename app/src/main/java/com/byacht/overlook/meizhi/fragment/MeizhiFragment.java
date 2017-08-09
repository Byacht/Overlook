package com.byacht.overlook.meizhi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.byacht.overlook.R;
import com.byacht.overlook.meizhi.adapter.MeizhiAdapter;
import com.byacht.overlook.meizhi.entity.Meizhis;
import com.byacht.overlook.meizhi.presenter.MeizhiPresenter;

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
    Unbinder unbinder;
    private MeizhiPresenter mMeizhiPresenter;
    private MeizhiAdapter mMeizhiAdapter;
    private ArrayList<Integer> mImageHeight;

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

        mImageHeight = new ArrayList<Integer>();
        mMeizhiPresenter = new MeizhiPresenter(this);
        mMeizhiPresenter.getMeizhis();
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRvMeizhi.setLayoutManager(layoutManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showMeizhis(Meizhis meizhis) {
        if (meizhis != null) {
            for (int i = 0; i < meizhis.getMeizhis().size(); i++){
                mImageHeight.add((int) (350 + Math.random() * 300));
            }
            mMeizhiAdapter = new MeizhiAdapter(getContext(), meizhis.getMeizhis(), mImageHeight);
            mRvMeizhi.setAdapter(mMeizhiAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
