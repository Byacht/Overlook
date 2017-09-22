package com.byacht.overlook;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;

import com.byacht.overlook.activity.SettingActivity;
import com.byacht.overlook.adapter.MyFragmentPagerAdapter;
import com.byacht.overlook.douyutv.activity.DouyuTvActivity;
import com.byacht.overlook.meizhi.fragment.MeizhiFragment;
import com.byacht.overlook.util.MyUtils;
import com.byacht.overlook.zhihu.fragment.ZhihuFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_main)
    Toolbar mToolbarMain;
    @BindView(R.id.navigation)
    NavigationView mNavigation;
    @BindView(R.id.activity_main)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.tab_main)
    TabLayout mTab;
    @BindView(R.id.vp_main)
    ViewPager mViewPager;

    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupToolbar();
        setupDrawer();
        initView();
        setupTab();
    }

    private void setupToolbar() {
        mToolbarMain.setTitle("首页");
        mToolbarMain.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbarMain);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, mToolbarMain, R.string.open, R.string.close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.douyu_tv_menu:
                        Intent intent = new Intent(MainActivity.this, DouyuTvActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.setting_menu:
                        Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(settingIntent);
                        finish();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void initView() {
        fragments = new ArrayList<Fragment>();
        ZhihuFragment zhihuFragment = new ZhihuFragment();
        MeizhiFragment meizhiFragment = new MeizhiFragment();
        fragments.add(zhihuFragment);
        fragments.add(meizhiFragment);
    }

    private void setupTab(){
        if (fragments != null) {
            MyFragmentPagerAdapter fmPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
            mViewPager.setAdapter(fmPagerAdapter);
            mTab.setupWithViewPager(mViewPager);
        }

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

