package com.byacht.overlook.douyutv.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byacht.overlook.R;
import com.byacht.overlook.douyutv.ui.MediaControllerView;
import com.byacht.overlook.douyutv.TimeThread;
import com.byacht.overlook.util.DensityUtil;
import com.byacht.overlook.util.ScreenUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;
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
    //弹幕显示的view
    @BindView(R.id.danmu_view)
    DanmakuView mDanmaView;
    //调节音量和亮度
    @BindView(R.id.media_controller_view)
    MediaControllerView mControllerView;

    //横屏时底部控制条
    @BindView(R.id.ll_control_box)
    LinearLayout mLlController;
    @BindView(R.id.et_danmu_full_screen)
    EditText mEtDanmuFull;
    @BindView(R.id.btn_send_danmu_full_screen)
    Button mBtnSendDanmuFull;
    @BindView(R.id.btn_pause_full_screen)
    Button mBtnPauseFull;
    @BindView(R.id.btn_quit_full_screen)
    Button mBtnQuitFullScreen;

    private DanmakuContext mDanmakuContext;

    //全屏按钮和暂停按钮是否显示
    private boolean isShow = true;
    //是否为横屏
    private boolean isCrossScreen = false;
    //记时线程
    private TimeThread mTimeThread;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mBtnFullScreen.setVisibility(View.GONE);
            mBtnPause.setVisibility(View.GONE);
            isShow = false;
            super.handleMessage(msg);
        }
    };

    private BaseDanmakuParser mParser = new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //检查vitamio框架是否可用
        if (!LibsChecker.checkVitamioLibs(this)) {
            return;
        }
        //全屏显示，隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_douyu_tv_room);
        ButterKnife.bind(this);

        // vitamio 初始化成功，返回 true
        if (Vitamio.initialize(this)) {
            String url = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
            mVideoView.setVideoURI(Uri.parse(url));
            //缓存大小，单位 byte
            mVideoView.setBufferSize(1024 * 200);
            mVideoView.start();
            setupVideoViewListener();
        }

        initDanmaView();
        setupDanmuViewListener();
        //设置按钮透明度
        mBtnFullScreen.getBackground().setAlpha(150);
        mBtnPause.getBackground().setAlpha(150);

        //定时，屏幕无操作 8 秒时隐藏按钮
        mTimeThread = new TimeThread(8 * 1000, mHandler);
        mTimeThread.start();

        mControllerView.setLlController(mLlController);
        mControllerView.setActivity(this);

    }

    //初始化弹幕view
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //获取屏幕宽高
        int width = ScreenUtil.getScreenWidth(this);
        int height = ScreenUtil.getScreenHeight(this);
        //是否为横屏
        isCrossScreen = width < height ? false : true;
        if (isCrossScreen) {
            setVisibility(true);
            setVideoSize(true);
        } else {
            setVisibility(false);
            setVideoSize(false);
        }
        super.onConfigurationChanged(newConfig);
    }

    //显示或隐藏部分控件
    private void setVisibility(boolean isCrossScreen) {
        if (isCrossScreen) {
            mControllerView.setVisibility(View.VISIBLE);
            mLlController.setVisibility(View.VISIBLE);
            mTvDanmu.setVisibility(View.GONE);
            mLlSendDanmu.setVisibility(View.GONE);
            mBtnFullScreen.setVisibility(View.GONE);
            mBtnPause.setVisibility(View.GONE);
        } else {
            mControllerView.setVisibility(View.GONE);
            mLlController.setVisibility(View.GONE);
            mTvDanmu.setVisibility(View.VISIBLE);
            mLlSendDanmu.setVisibility(View.VISIBLE);
            mBtnFullScreen.setVisibility(View.VISIBLE);
            mBtnPause.setVisibility(View.VISIBLE);
        }
    }

    //设置 videoView 宽高
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

    private void setupVideoViewListener() {
        //显示加载百分比
        mVideoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                mTvLoadRate.setText(percent + "%");
            }
        });

        //监听缓冲事件
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
                        //显示缓冲速度
                        mTvDownloadRate.setText("" + extra + "kb/s" + "  ");
                        break;
                }
                return true;
            }
        });
    }

    private void setupDanmuViewListener() {
        //点击屏幕，显示或隐藏部分控件
        mDanmaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow) {
                    mBtnFullScreen.setVisibility(View.GONE);
                    mBtnPause.setVisibility(View.GONE);
                    isShow = false;
                } else {
                    if (!isCrossScreen) {
                        mBtnFullScreen.setVisibility(View.VISIBLE);
                        mBtnPause.setVisibility(View.VISIBLE);
                        isShow = true;
                    }
                    //重置计时起始时间
                    mTimeThread.setStartTime(System.currentTimeMillis());
                }
            }
        });
    }

    //添加弹幕
    public void addDanmaku(String content, boolean withBorder) {
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

    @OnTouch(R.id.et_danmu_full_screen)
    public boolean inputDanmu() {
        //输入弹幕期间停止计时，不隐藏 controllerView
        mControllerView.getTimeThread().setIsTiming(false);
        return false;
    }

    //全屏按钮
    @OnClick(R.id.btn_full_screen)
    public void turnToFullScreen() {
        if (isCrossScreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    //暂停按钮
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

    //发送弹幕
    @OnClick(R.id.btn_send_danmu_full_screen)
    public void sendDanmu() {
        String danmu = mEtDanmuFull.getText().toString();
        if (danmu != null) {
            addDanmaku(danmu, false);
            mEtDanmuFull.setText("");
        }
        //发送弹幕后重新开始计时，一定时间后隐藏 controllerView
        mControllerView.getTimeThread().setIsTiming(true);
        mControllerView.getTimeThread().setStartTime(System.currentTimeMillis());
    }

    //退出全屏按钮
    @OnClick(R.id.btn_quit_full_screen)
    public void quitFullScreen() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    //横屏时的暂停按钮
    @OnClick(R.id.btn_pause_full_screen)
    public void pauseOrStartVideoFull() {
        if (mVideoView.isPlaying()) {
            mBtnPauseFull.setBackgroundResource(R.drawable.start);
            mVideoView.pause();
        } else {
            mBtnPauseFull.setBackgroundResource(R.drawable.pause);
            mVideoView.start();
        }
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
