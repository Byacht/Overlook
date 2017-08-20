package com.byacht.overlook.douyutv.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.byacht.overlook.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DouyuTvRoomActivity extends AppCompatActivity {

    @BindView(R.id.wv_douyu_tv)
    WebView mWvDouyuTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_douyu_tv_room);
        ButterKnife.bind(this);

        final String roomId = getIntent().getStringExtra("roomId");
        Log.d("out", "room id:" + roomId);

        mWvDouyuTv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                mWvDouyuTv.loadUrl("http://www.douyu.com/" + roomId);
                return true;
            }
        });
    }

}
