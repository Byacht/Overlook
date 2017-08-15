package com.byacht.overlook.zhihu.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.byacht.overlook.R;
import com.byacht.overlook.zhihu.GlideLoader;
import com.byacht.overlook.zhihu.activity.ZhihuDetailNewsActivity;
import com.byacht.overlook.zhihu.entity.ZhihuDaily;
import com.byacht.overlook.zhihu.entity.ZhihuDetailNews;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dn on 2017/8/6.
 */

public class ZhihuDailyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 1;
    public static final int TYPE_SECTION = 2;

    private Context mContext;
    private ArrayList<ZhihuDaily> mZhihuDailies;
    private ArrayList<ZhihuDetailNews> mHeaders;

    public ZhihuDailyAdapter(Context context, ArrayList<ZhihuDaily> zhihuDailies, ArrayList<ZhihuDetailNews> headers) {
        mContext = context;
        mZhihuDailies = zhihuDailies;
        mHeaders = headers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View headerView = LayoutInflater.from(mContext).inflate(R.layout.zhihu_header, parent, false);
            return new ZhihuHeaderViewHolder(headerView);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.zhihu_daily_item, parent, false);
            return new ZhihuDailyViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (isHeader(position)) {
            ZhihuHeaderViewHolder headerHolder = (ZhihuHeaderViewHolder) holder;
            //轮播图数据
            if (mHeaders != null && mHeaders.size() > 0) {
                List<String> imageUrls = new ArrayList<String>();
                List<String> titles = new ArrayList<String>();
                for (int i = 0; i < mHeaders.size(); i++) {
                    imageUrls.add(mHeaders.get(i).getImage());
                    titles.add(mHeaders.get(i).getTitle());
                }
                headerHolder.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
                headerHolder.banner.setImageLoader(new GlideLoader());
                headerHolder.banner.update(imageUrls, titles);
                headerHolder.banner.start();

                headerHolder.banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        String id = mHeaders.get(position).getId();
                        String title = mHeaders.get(position).getTitle();
                        startDetailActivity(holder, id, title, true);
                    }
                });
            }
        } else {
            ZhihuDailyViewHolder sectionHolder = (ZhihuDailyViewHolder) holder;
            // position == 0 时是轮播图的视图，故这里需 position - 1
            Glide.with(mContext)
                    .load(mZhihuDailies.get(position - 1).getImages()[0])
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(sectionHolder.imgZhihuDaily);
            sectionHolder.tvZhihuDaily.setText(mZhihuDailies.get(position - 1).getTitle());
            sectionHolder.llZhihuDailyItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = mZhihuDailies.get(position - 1).getId();
                    String title = mZhihuDailies.get(position - 1).getTitle();
                    startDetailActivity(holder, id, title, false);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mZhihuDailies.size() + 1;  //多了一个轮播图视图
    }

    class ZhihuHeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.banner_zhihu)
        Banner banner;

        public ZhihuHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    class ZhihuDailyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_zhihu_daily_item)
        LinearLayout llZhihuDailyItem;
        @BindView(R.id.img_zhihu_daily)
        ImageView imgZhihuDaily;
        @BindView(R.id.tv_zhihu_daily)
        TextView tvZhihuDaily;

        public ZhihuDailyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return TYPE_HEADER;
        } else {
            return TYPE_SECTION;
        }
    }

    private void startDetailActivity(RecyclerView.ViewHolder holder, String id, String title, boolean isHeader) {
        Intent intent = new Intent(mContext, ZhihuDetailNewsActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        if (Build.VERSION.SDK_INT >= 21) {
            mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity)mContext).toBundle());
        } else {
            mContext.startActivity(intent);
        }

    }

    public boolean isHeader(int position) {
        return position == 0;
    }
}
