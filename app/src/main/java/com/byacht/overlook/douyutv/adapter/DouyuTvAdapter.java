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
import com.byacht.overlook.douyutv.entity.TvRoom;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dn on 2017/8/8.
 */

public class DouyuTvAdapter extends RecyclerView.Adapter<DouyuTvAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<TvRoom> mTvRooms;

    public DouyuTvAdapter(Context context, ArrayList<TvRoom> tvRooms) {
        mContext = context;
        mTvRooms = tvRooms;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.douyu_tv_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TvRoom tvRoom = mTvRooms.get(position);
        Glide.with(mContext)
                .load(tvRoom.getRoom_src())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.mImgDouyuTvRoom);
        holder.mTvDouyuTvOnline.setText(tvRoom.getOnline() + "");
        holder.mTvRoomName.setText(tvRoom.getRoom_name());
    }

    @Override
    public int getItemCount() {
        return mTvRooms.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_douyu_tv_room)
        ImageView mImgDouyuTvRoom;
        @BindView(R.id.tv_douyu_tv_online)
        TextView mTvDouyuTvOnline;
        @BindView(R.id.tv_douyu_tv_room_name)
        TextView mTvRoomName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
