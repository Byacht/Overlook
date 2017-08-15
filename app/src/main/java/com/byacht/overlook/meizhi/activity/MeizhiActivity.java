package com.byacht.overlook.meizhi.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.byacht.overlook.R;
import com.github.chrisbanes.photoview.OnViewTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeizhiActivity extends Activity {

    @BindView(R.id.photo_view)
    ImageView mPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_meizhi);
        ButterKnife.bind(this);

        getData();
//        new Thread(new Runnable(){
//            @Override
//            public void run() {
//                final Drawable drawable = loadImage(url);
//                mPhotoView.post(new Runnable(){
//                    @Override
//                    public void run() {
//                        mPhotoView.setImageDrawable(drawable) ;
//                    }}) ;
//            }
//
//        }).start()  ;

    }

    private void getData() {
        String url = getIntent().getStringExtra("url");
        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mPhotoView);
    }

//    private Drawable loadImage(String url)
//    {
//        Drawable drawable = null;
//        try {
//            drawable = Drawable.createFromStream(new URL(url).openStream(), "image.jpg");
//        } catch (IOException e) {
//        }
//        return drawable ;
//    }

}
