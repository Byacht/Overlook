package com.byacht.overlook.douyutv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.byacht.overlook.R;
import com.byacht.overlook.douyutv.entity.TvGame;
import com.byacht.overlook.douyutv.entity.TvRoom;
import com.byacht.overlook.douyutv.entity.TvView;
import com.byacht.overlook.douyutv.ui.DouyuTvView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dn on 2017/8/9.
 */

public class DouyuTvAllGamesAdapter extends RecyclerView.Adapter<DouyuTvAllGamesAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<TvView> mTvViews;

    public DouyuTvAllGamesAdapter(Context context, ArrayList<TvView> tvViews) {
        mContext = context;
        mTvViews = tvViews;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.douyu_tv_all_games_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        TvGame tvGame = mTvViews.get(position).getTvGame();
        holder.douyuTvView.setGameIcon(tvGame.getGame_icon());
        holder.douyuTvView.setTvGameName(tvGame.getGame_name());
        holder.douyuTvView.setRvDouyuRoom(mTvViews.get(position).getTvRooms());
        holder.douyuTvView.setGameShortName(tvGame.getShort_name());
    }

    @Override
    public int getItemCount() {
        return mTvViews.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.douyu_tv_view)
        DouyuTvView douyuTvView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
