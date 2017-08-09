package com.byacht.overlook.douyutv.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.byacht.overlook.R;
import com.byacht.overlook.douyutv.activity.DouyuTvMoreActivity;
import com.byacht.overlook.douyutv.adapter.DouyuTvAdapter;
import com.byacht.overlook.douyutv.entity.TvRoom;

import java.util.ArrayList;

/**
 * Created by dn on 2017/8/9.
 */

public class DouyuTvView extends LinearLayout {
    private ImageView mImgGameIcon;
    private TextView mTvGameName;
    private RecyclerView mRvDouyuRoom;
    private TextView mTvGetMore;
    private Context mContext;
    private String mGameShortName;
    private String mGameIconUrl;
    private String mCategoryName;

    public DouyuTvView(Context context) {
        this(context, null, 0);
    }

    public DouyuTvView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DouyuTvView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.douyu_tv_view, this);
        mImgGameIcon = (ImageView) findViewById(R.id.img_game);
        mTvGameName = (TextView) findViewById(R.id.tv_game_name);
        mRvDouyuRoom = (RecyclerView) findViewById(R.id.rv_douyu_tv);
        mTvGetMore = (TextView) findViewById(R.id.tv_get_more);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRvDouyuRoom.setLayoutManager(layoutManager);

        mTvGetMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DouyuTvMoreActivity.class);
                intent.putExtra("gameName", mGameShortName);
                intent.putExtra("categoryName", mCategoryName);
                intent.putExtra("gameIcon", mGameIconUrl);
                mContext.startActivity(intent);
            }
        });
    }

    public void setGameIcon(String url) {
        Glide.with(mContext)
                .load(url)
                .into(mImgGameIcon);
        mGameIconUrl = url;
    }

    public void setTvGameName(String gameName) {
        mTvGameName.setText(gameName);
        mCategoryName = gameName;
    }

    public void setGameShortName(String shortName) {
        mGameShortName = shortName;
    }

    public void setRvDouyuRoom(ArrayList<TvRoom> tvRooms) {
        DouyuTvAdapter adapter = new DouyuTvAdapter(mContext, tvRooms);
        mRvDouyuRoom.setAdapter(adapter);
    }


}
