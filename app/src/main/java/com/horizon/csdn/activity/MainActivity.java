package com.horizon.csdn.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.horizon.csdn.R;
import com.horizon.csdn.adapter.CsdnTabAdapter;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AutoLayoutActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tabs)
    TabLayout mTab;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    private CsdnTabAdapter mAdapter;
    private static final List<String> TITLES = Arrays.asList(new String[]{ "业界", "移动", "研发", "程序员", "云计算" });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolBar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(new IconicsDrawable(this).color(Color.WHITE).icon(MaterialDesignIconic.Icon.gmi_fire).sizeDp(32));
        ab.setDisplayHomeAsUpEnabled(true);

        mViewPager.setAdapter(mAdapter = new CsdnTabAdapter(TITLES, getSupportFragmentManager()));
        mTab.setupWithViewPager(mViewPager);
    }
}
