package com.byacht.overlook.douyutv;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.audiofx.BassBoost;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.byacht.overlook.R;
import com.byacht.overlook.douyutv.activity.DouyuTvActivity;
import com.byacht.overlook.douyutv.activity.DouyuTvRoomActivity;
import com.byacht.overlook.util.ScreenUtil;

import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by dn on 2017/8/22.
 */

public class MediaControllerView extends FrameLayout {

    private Context mContext;
    private GestureDetector mGestureDetector;
    private AudioManager mAudioManager;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mMaxVolume;
    private int mCurrentVolume;
    private int mCurrentBrightness;
    private boolean isShow = true;
    private TimeThread mTimeThread;

    public void setLlController(LinearLayout llController) {
        mLlController = llController;
    }

    private LinearLayout mLlController;

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    private Activity mActivity;

    public MediaControllerView(Context context) {
        this(context, null, 0);
    }

    public MediaControllerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaControllerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
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
        mCurrentVolume += mMaxVolume * percent;
        if (mCurrentVolume > mMaxVolume) {
            mCurrentVolume = mMaxVolume;
        } else if (mCurrentVolume < 0) {
            mCurrentVolume = 0;
        }
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mCurrentVolume, 0);
    }

    private void changeBrightness(float percent) {
        try {
            mCurrentBrightness = Settings.System.getInt(mActivity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        WindowManager.LayoutParams lpa = mActivity.getWindow().getAttributes();
        lpa.screenBrightness = mCurrentBrightness / 255 + percent;
        if (lpa.screenBrightness > 1.0f) {
            lpa.screenBrightness = 1.0f;
        } else if (lpa.screenBrightness < 0.01f) {
            lpa.screenBrightness = 0.01f;
        }
        //变更亮度
        mActivity.getWindow().setAttributes(lpa);
    }



    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mLlController.setVisibility(GONE);
            isShow = false;
            super.handleMessage(msg);
        }
    };
}
