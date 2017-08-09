package com.byacht.overlook.douyutv.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.byacht.overlook.R;
import com.byacht.overlook.douyutv.adapter.DouyuTvAdapter;
import com.byacht.overlook.douyutv.entity.TvCategory;
import com.byacht.overlook.douyutv.entity.TvRooms;
import com.byacht.overlook.douyutv.presenter.DouyuTvRoomPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DouyuTvMoreActivity extends AppCompatActivity implements IDouyuTvActivity {

    @BindView(R.id.img_game)
    ImageView mImgGame;
    @BindView(R.id.tv_game_name)
    TextView mTvGameName;
    @BindView(R.id.tv_get_more)
    TextView mTvGetMore;
    @BindView(R.id.rv_douyu_tv)
    RecyclerView mRvDouyuTv;

    private DouyuTvRoomPresenter mDouyuTvRoomPresenter;
    private DouyuTvAdapter mDouyuTvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_douyu_tv_more);
        ButterKnife.bind(this);

        mDouyuTvRoomPresenter = new DouyuTvRoomPresenter(this);
        mDouyuTvRoomPresenter.getTvRooms(getIntent().getStringExtra("gameName"));
        mTvGetMore.setVisibility(View.GONE);
        mTvGameName.setText(getIntent().getStringExtra("categoryName"));
        Glide.with(this)
                .load(getIntent().getStringExtra("gameIcon"))
                .into(mImgGame);
        Log.d("htout", "gameName:" + getIntent().getStringExtra("gameName") + " url:" + getIntent().getStringExtra("gameIcon"));
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRvDouyuTv.setLayoutManager(layoutManager);
    }

    @Override
    public void showAllCategories(TvCategory tvCategory) {

    }

    @Override
    public void showRooms(TvRooms tvRooms) {
        mDouyuTvAdapter = new DouyuTvAdapter(this, tvRooms.getTvRooms());
        mRvDouyuTv.setAdapter(mDouyuTvAdapter);
    }
}
