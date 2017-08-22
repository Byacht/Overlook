package com.byacht.overlook.douyutv.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.byacht.overlook.R;
import com.byacht.overlook.util.DensityUtil;
import com.byacht.overlook.util.ScreenUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

public class DouyuTvRoomActivity extends AppCompatActivity {

    @BindView(R.id.vv_douyu_live)
    VideoView mVideoView;
    @BindView(R.id.pb_douyu_live)
    ProgressBar mPbDouyuLive;
    @BindView(R.id.tv_download_rate)
    TextView mTvDownloadRate;
    @BindView(R.id.tv_load_rate)
    TextView mTvLoadRate;
    @BindView(R.id.tv_danmu)
    TextView mTvDanmu;
    @BindView(R.id.rl_video)
    RelativeLayout mRlVideo;
    @BindView(R.id.ll_send_danmu)
    LinearLayout mLlSendDanmu;
    @BindView(R.id.btn_full_screen)
    Button mBtnFullScreen;
    @BindView(R.id.et_danmu)
    EditText mEtDanmu;
    @BindView(R.id.btn_send_danmu)
    Button mBtnSendDanmu;
    @BindView(R.id.btn_pause_button)
    Button mBtnPause;
    @BindView(R.id.danmu_view)
    DanmakuView mDanmaView;
    @BindView(R.id.et_danmu_view)
    EditText mEtDanmuView;
    @BindView(R.id.btn_send_danmu_view)
    Button mBtnSendDanmuView;
    @BindView(R.id.ll_danmu_view)
    LinearLayout mLlDanmuView;

    private DanmakuContext mDanmakuContext;

    private boolean isShow = true;
    private boolean isCrossScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //检查vitamio框架是否可用
        if (!LibsChecker.checkVitamioLibs(this)) {
            Log.d("htout", "cannot use");
            return;
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_douyu_tv_room);
        ButterKnife.bind(this);

//        String roomId = getIntent().getStringExtra("roomId");

        // vitamio 初始化成功，返回 true
        if (Vitamio.initialize(this)) {
            String url = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
            mVideoView.setVideoURI(Uri.parse(url));
            mVideoView.setBufferSize(1024 * 500);
//            MediaController controller = new MediaController(this);
//            mVideoView.setMediaController(controller);
            mVideoView.start();
            setupVideoViewListener();

            mBtnFullScreen.getBackground().setAlpha(150);
            mBtnPause.getBackground().setAlpha(150);
        }

        initDanmaView();
        mBtnSendDanmuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String danmu = mEtDanmuView.getText().toString();
                if (danmu != null) {
                    addDanmaku(danmu, false);
                    mEtDanmuView.setText("");
                }
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        hideStatuBar();
    }

    @OnClick(R.id.btn_full_screen)
    public void turnToFullScreen() {
        if (isCrossScreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @OnClick(R.id.btn_pause_button)
    public void pauseOrStartVideo() {
        if (mVideoView.isPlaying()) {
            mBtnPause.setBackgroundResource(R.drawable.start);
            mVideoView.pause();
        } else {
            mBtnPause.setBackgroundResource(R.drawable.pause);
            mVideoView.start();
        }
        mBtnPause.getBackground().setAlpha(150);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("htout", "configchange");
        int width = ScreenUtil.getScreenWidth(this);
        int height = ScreenUtil.getScreenHeight(this);
        //是否为横屏
        isCrossScreen = width < height ? false : true;
        if (isCrossScreen) {
            hideDanmu();
            setVideoSize(true);
            mLlDanmuView.setVisibility(View.VISIBLE);
            mBtnFullScreen.setBackgroundResource(R.drawable.quit_full_screen);
        } else {
            showDanmu();
            setVideoSize(false);
            mLlDanmuView.setVisibility(View.GONE);
            mBtnFullScreen.setBackgroundResource(R.drawable.full_screen);
        }
        mBtnFullScreen.getBackground().setAlpha(150);
        super.onConfigurationChanged(newConfig);
    }

    private void setupVideoViewListener() {
        mVideoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                mTvLoadRate.setText(percent + "%");
            }
        });

        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    //开始缓冲
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        if (mVideoView.isPlaying()) {
                            mVideoView.pause();
                            mPbDouyuLive.setVisibility(View.VISIBLE);
                            mTvDownloadRate.setVisibility(View.VISIBLE);
                            mTvLoadRate.setVisibility(View.VISIBLE);
                        }
                        break;
                    //缓冲结束
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        mVideoView.start();
                        mPbDouyuLive.setVisibility(View.GONE);
                        mTvDownloadRate.setVisibility(View.GONE);
                        mTvLoadRate.setVisibility(View.GONE);
                        break;
                    //正在缓冲
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        mTvDownloadRate.setText("" + extra + "kb/s" + "  ");
                        break;
                }
                return true;
            }
        });

        mDanmaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow) {
                    if (isCrossScreen) {
                        mBtnFullScreen.setVisibility(View.GONE);
                        mBtnPause.setVisibility(View.GONE);
                        mLlDanmuView.setVisibility(View.GONE);
                    } else {
                        mBtnFullScreen.setVisibility(View.GONE);
                        mBtnPause.setVisibility(View.GONE);
                    }
                    isShow = false;
                } else {
                    if (isCrossScreen) {
                        mBtnFullScreen.setVisibility(View.VISIBLE);
                        mBtnPause.setVisibility(View.VISIBLE);
                        mLlDanmuView.setVisibility(View.VISIBLE);
                    } else {
                        mBtnFullScreen.setVisibility(View.VISIBLE);
                        mBtnPause.setVisibility(View.VISIBLE);
                    }
                    isShow = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            mHandler.sendEmptyMessage(0);
                        }
                    }).start();
                }
            }
        });
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (isCrossScreen) {
                mBtnFullScreen.setVisibility(View.GONE);
                mBtnPause.setVisibility(View.GONE);
                mLlDanmuView.setVisibility(View.GONE);
            } else {
                mBtnFullScreen.setVisibility(View.GONE);
                mBtnPause.setVisibility(View.GONE);
            }
            isShow = false;
            super.handleMessage(msg);
        }
    };

    private void initDanmaView() {
        mDanmaView.enableDanmakuDrawingCache(true);
        mDanmaView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                mDanmaView.start();
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void drawingFinished() {

            }
        });
        mDanmakuContext = DanmakuContext.create();
        mDanmaView.prepare(mParser, mDanmakuContext);
    }

    //添加弹幕
    private void addDanmaku(String content, boolean withBorder) {
        BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        danmaku.text = content;
        danmaku.padding = 5;
        danmaku.textSize = DensityUtil.sp2px(this, 20);
        danmaku.textColor = Color.WHITE;
        danmaku.setTime(mDanmaView.getCurrentTime());
        if (withBorder) {
            danmaku.borderColor = Color.GREEN;
        }
        mDanmaView.addDanmaku(danmaku);
    }

    private BaseDanmakuParser mParser = new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };

    private void setVideoSize(boolean isFullScreen) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mRlVideo.getLayoutParams();
        RelativeLayout.LayoutParams videoLayoutParams = (RelativeLayout.LayoutParams) mVideoView.getLayoutParams();
        if (isFullScreen) {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            videoLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = DensityUtil.dp2px(this, 200);
            videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            videoLayoutParams.height = DensityUtil.dp2px(this, 200);
        }
        mRlVideo.setLayoutParams(layoutParams);
        mVideoView.setLayoutParams(videoLayoutParams);
    }

    private void hideStatuBar() {
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

    }

    private void showStatuBar() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    private void hideDanmu() {
        mTvDanmu.setVisibility(View.GONE);
        mLlSendDanmu.setVisibility(View.GONE);
    }

    private void showDanmu() {
        mTvDanmu.setVisibility(View.VISIBLE);
        mLlSendDanmu.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (isCrossScreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDanmaView != null && mDanmaView.isPrepared()) {
            mDanmaView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDanmaView != null && mDanmaView.isPrepared() && mDanmaView.isPaused()) {
            mDanmaView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
        if (mDanmaView != null) {
            mDanmaView.release();
            mDanmaView = null;
        }
    }
}
