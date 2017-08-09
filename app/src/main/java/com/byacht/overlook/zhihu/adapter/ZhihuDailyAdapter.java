package com.byacht.overlook.zhihu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.byacht.overlook.R;
import com.byacht.overlook.zhihu.activity.ZhihuDetailNewsActivity;
import com.byacht.overlook.zhihu.entity.ZhihuDaily;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dn on 2017/8/6.
 */

public class ZhihuDailyAdapter extends RecyclerView.Adapter<ZhihuDailyAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ZhihuDaily> mZhihuDailies;

    public ZhihuDailyAdapter(Context context, ArrayList<ZhihuDaily> zhihuDailies) {
        this.mContext = context;
        this.mZhihuDailies = zhihuDailies;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.zhihu_daily_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Glide.with(mContext)
                .load(mZhihuDailies.get(position).getImages()[0])
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.imgZhihuDaily);
        holder.tvZhihuDaily.setText(mZhihuDailies.get(position).getTitle());
        holder.llZhihuDailyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mZhihuDailies.get(position).getId();
                Intent intent = new Intent(mContext, ZhihuDetailNewsActivity.class);
                intent.putExtra("id", id);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mZhihuDailies.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_zhihu_daily_item)
        LinearLayout llZhihuDailyItem;
        @BindView(R.id.img_zhihu_daily)
        ImageView imgZhihuDaily;
        @BindView(R.id.tv_zhihu_daily)
        TextView tvZhihuDaily;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
