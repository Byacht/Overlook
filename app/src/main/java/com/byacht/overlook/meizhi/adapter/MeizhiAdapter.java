package com.byacht.overlook.meizhi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.byacht.overlook.R;
import com.byacht.overlook.meizhi.activity.MeizhiActivity;
import com.byacht.overlook.meizhi.entity.Meizhi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dn on 2017/8/6.
 */

public class MeizhiAdapter extends RecyclerView.Adapter<MeizhiAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Meizhi> mMeizhis;
    private ArrayList<Integer> mImageHeight;

    public MeizhiAdapter(Context context, ArrayList<Meizhi> meizhis, ArrayList<Integer> imageHeight) {
        mContext = context;
        mMeizhis = meizhis;
        mImageHeight = imageHeight;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.meizhi_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.imgMeizhi.getLayoutParams();
        lp.height = mImageHeight.get(position);
        holder.imgMeizhi.setLayoutParams(lp);
        Glide.with(mContext)
                .load(mMeizhis.get(position).getUrl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.imgMeizhi);
        holder.imgMeizhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MeizhiActivity.class);
                intent.putExtra("url", mMeizhis.get(position).getUrl());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMeizhis.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_meizhi)
        ImageView imgMeizhi;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
