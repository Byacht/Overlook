package com.byacht.overlook.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.byacht.overlook.MainActivity;
import com.byacht.overlook.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_setting)
    Toolbar mToolbar;
    @BindView(R.id.switch_night)
    Switch mSwitchNight;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        setupToolbar();

        mSharedPreferences = getSharedPreferences("night", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        mSwitchNight.setChecked(mSharedPreferences.getBoolean("isNightMode", false));
        Log.d("htout", "isChecked:" + mSharedPreferences.getBoolean("isNightMode", false));

        mSwitchNight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(SettingActivity.this, "open", Toast.LENGTH_SHORT).show();
                setNightMode();
            }
        });
    }

    private void setupToolbar() {
        mToolbar.setTitle("设置");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setNightMode() {
        //  获取当前模式
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        //  将是否为夜间模式保存到SharedPreferences
//        UserInfoTools.setNightMode(this, currentNightMode == Configuration.UI_MODE_NIGHT_NO);
        //  切换模式
        getDelegate().setDefaultNightMode(currentNightMode == Configuration.UI_MODE_NIGHT_NO ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        mEditor.putBoolean("isNightMode", currentNightMode == Configuration.UI_MODE_NIGHT_NO);
        mEditor.commit();
//        UserInfoTools.setChangeNightMode(this,true);
        //  重启Activity
        startActivity(new Intent(this,SettingActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
