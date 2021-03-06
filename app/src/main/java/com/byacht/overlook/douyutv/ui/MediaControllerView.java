package com.byacht.overlook.douyutv.ui;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.byacht.overlook.R;
import com.byacht.overlook.douyutv.TimeThread;
import com.byacht.overlook.util.ScreenUtil;

/**
 * Created by dn on 2017/8/22.
 */

public class MediaControllerView extends RelativeLayout {

    private Context mContext;
    private GestureDetector mGestureDetector;
    private AudioManager mAudioManager;
    private int mScreenWidth;
    private int mScreenHeight;
    private float mMaxVolume;
    private float mCurrentVolume;
    private int mCurrentBrightness;
    //底部控制条是否显示
    private boolean isShow = true;
    private TimeThread mTimeThread;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mLlController.setVisibility(GONE);
            isShow = false;
            mSeekBar.setVisibility(GONE);
            mLlAdjust.setVisibility(GONE);
            super.handleMessage(msg);
        }
    };

    private LinearLayout mLlAdjust;
    private ImageView mImgAdjust;
    private SeekBar mSeekBar;

    private LinearLayout mLlController;
    private Activity mActivity;

    public void setLlController(LinearLayout llController) {
        mLlController = llController;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public TimeThread getTimeThread() {
        return mTimeThread;
    }

    public MediaControllerView(Context context) {
        this(context, null, 0);
    }

    public MediaControllerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaControllerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.mediacontroller, this);
        mLlAdjust = (LinearLayout) view.findViewById(R.id.ll_adjust_bg);
        mImgAdjust = (ImageView) view.findViewById(R.id.img_adjust);
        mSeekBar = (SeekBar) view.findViewById(R.id.seekbar);
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mCurrentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mScreenWidth = ScreenUtil.getScreenWidth(context);
        mScreenHeight = ScreenUtil.getScreenHeight(context);
        mGestureDetector = new GestureDetector(context, new MyGestureDetectorListener());
        mTimeThread = new TimeThread(8 * 1000, mHandler);
        mTimeThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    class MyGestureDetectorListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (isShow) {
                mLlController.setVisibility(GONE);
                isShow = false;
            } else {
                mTimeThread.setStartTime(System.currentTimeMillis());
                mLlController.setVisibility(VISIBLE);
                isShow = true;
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float startY = e1.getY();
            float endY = e2.getY();
            float percent = (startY - endY) / mScreenHeight;
            float startX = e1.getX();
            Log.d("htout", "startX:" + startX + " percent:" + percent);
            if (startX < mScreenWidth / 2) {
                changeVolume(percent);
            } else {
                changeBrightness(percent);
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    private void changeVolume(float percent) {
        mCurrentVolume += (mMaxVolume * percent);
        if (mCurrentVolume > mMaxVolume) {
            mCurrentVolume = mMaxVolume;
        } else if (mCurrentVolume < 0) {
            mCurrentVolume = 0;
        }
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) mCurrentVolume, 0);
        mLlAdjust.setVisibility(VISIBLE);
        mImgAdjust.setImageResource(R.drawable.volume);
        mSeekBar.setVisibility(VISIBLE);
        mSeekBar.setProgress((int)((mCurrentVolume / mMaxVolume) * 100));

    }

    private void changeBrightness(float percent) {
        if (mCurrentBrightness == 0){
            try {
                mCurrentBrightness = Settings.System.getInt(mActivity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            mCurrentBrightness = (int) (mCurrentBrightness + percent * 255);
            if (mCurrentBrightness <= 0){
                mCurrentBrightness = 1;
            } else if (mCurrentBrightness > 255){
                mCurrentBrightness = 255;
            }
        }
        WindowManager.LayoutParams lpa = mActivity.getWindow().getAttributes();
        lpa.screenBrightness = (float)mCurrentBrightness / 255;
        if (lpa.screenBrightness > 1.0f) {
            lpa.screenBrightness = 1.0f;
        } else if (lpa.screenBrightness < 0.01f) {
            lpa.screenBrightness = 0.01f;
        }
        mActivity.getWindow().setAttributes(lpa);

        mLlAdjust.setVisibility(VISIBLE);
        mImgAdjust.setImageResource(R.drawable.brightness);
        mSeekBar.setVisibility(VISIBLE);
        Log.d("htout", "bright:" + mCurrentBrightness);
        mSeekBar.setProgress((int)((float)mCurrentBrightness / 255 * 100));
        Log.d("htout", "bright/255:" + (float)mCurrentBrightness / 255);
    }

}