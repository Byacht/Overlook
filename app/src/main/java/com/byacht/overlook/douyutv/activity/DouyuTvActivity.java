package com.byacht.overlook.douyutv.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.byacht.overlook.R;
import com.byacht.overlook.douyutv.adapter.DouyuTvAdapter;
import com.byacht.overlook.douyutv.adapter.DouyuTvAllGamesAdapter;
import com.byacht.overlook.douyutv.entity.TvCategory;
import com.byacht.overlook.douyutv.entity.TvGame;
import com.byacht.overlook.douyutv.entity.TvRoom;
import com.byacht.overlook.douyutv.entity.TvRooms;
import com.byacht.overlook.douyutv.entity.TvView;
import com.byacht.overlook.douyutv.presenter.DouyuTvCategoryPresenter;
import com.byacht.overlook.douyutv.presenter.DouyuTvRoomPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DouyuTvActivity extends AppCompatActivity implements IDouyuTvActivity {

    @BindView(R.id.rv_douyu_tv_all_games)
    RecyclerView mRvDouyuTv;
    @BindView(R.id.pgb_douyu_tv)
    ProgressBar mProgressBar;

    private DouyuTvCategoryPresenter mDouyuTvPresenter;
    private DouyuTvRoomPresenter mDouyuTvRoomPresenter;

    private ArrayList<String> mTvCategories;
    private DouyuTvAllGamesAdapter mAdapter;

    private ArrayList<TvGame> mTvGames;
    private ArrayList<TvView> mTvViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_douyu_tv);
        ButterKnife.bind(this);

        mProgressBar.setVisibility(View.VISIBLE);
        mTvViews = new ArrayList<TvView>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvDouyuTv.setLayoutManager(layoutManager);

        mDouyuTvPresenter = new DouyuTvCategoryPresenter(this);
        mDouyuTvRoomPresenter = new DouyuTvRoomPresenter(this);
        mDouyuTvPresenter.getTvCategory();
    }

    @Override
    public void showAllCategories(TvCategory tvCategory) {
        Log.d("htout", tvCategory.toString());
        mTvGames = tvCategory.getTvGames();
        for (int i = 0; i < mTvGames.size(); i++) {
            mDouyuTvRoomPresenter.getTvRooms(mTvGames.get(i).getShort_name());
        }
        mAdapter = new DouyuTvAllGamesAdapter(this, mTvViews);
        mRvDouyuTv.setAdapter(mAdapter);
    }

    @Override
    public void showRooms(TvRooms tvRooms) {
        Log.d("htout", "succeed!");
        hideProgressBar();
        ArrayList<TvRoom> newTvRooms = new ArrayList<TvRoom>();
        if (tvRooms.getTvRooms().size() >= 4){
            for (int i = 0; i < 4; i++) {
                newTvRooms.add(tvRooms.getTvRooms().get(i));
            }
        } else {
            for (int i = 0; i < tvRooms.getTvRooms().size(); i++) {
                newTvRooms.add(tvRooms.getTvRooms().get(i));
            }
        }
        if (newTvRooms.size() > 0) {
            for (int i = 0; i < mTvGames.size(); i++) {
                if (newTvRooms.get(0).getGame_name().equals(mTvGames.get(i).getGame_name())){
                    TvView tvView = new TvView();
                    tvView.setTvGame(mTvGames.get(i));
                    tvView.setTvRooms(newTvRooms);
                    mTvViews.add(tvView);
                }
            }
        }

    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}
