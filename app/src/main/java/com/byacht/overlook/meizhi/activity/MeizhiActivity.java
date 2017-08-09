package com.byacht.overlook.meizhi.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.byacht.overlook.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeizhiActivity extends Activity {

    @BindView(R.id.photo_view)
    PhotoView mPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meizhi);
        ButterKnife.bind(this);

        final String url = getIntent().getStringExtra("url");
        new Thread(new Runnable(){
            @Override
            public void run() {
                final Drawable drawable = loadImage(url);
                mPhotoView.post(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mPhotoView.setImageDrawable(drawable) ;
                    }}) ;
            }

        }).start()  ;

    }

    private Drawable loadImage(String url)
    {
        Drawable drawable = null;
        try {
            drawable = Drawable.createFromStream(new URL(url).openStream(), "image.jpg");
        } catch (IOException e) {
        }
        return drawable ;
    }
}
