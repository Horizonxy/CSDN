package com.horizon.csdn.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.horizon.csdn.R;
import com.horizon.csdn.adapter.CsdnTabAdapter;
import com.horizon.csdn.widget.RectViewPagerIndicator;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

public class CsdnMainActivity extends BaseActivity {

    @Bind(R.id.csdn_news)
    ViewPager mNewsPager;
    @Bind(R.id.pager_indicator)
    RectViewPagerIndicator mPagerIndicator;

    private CsdnTabAdapter mAdapter;
    private static final List<String> TITLES = Arrays.asList(new String[]{ "业界", "移动", "研发", "程序员杂志", "云计算" });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csdn_main);
        setTitle("SCDN资讯");
        setBtnLeft(MaterialDesignIconic.Icon.gmi_fire);

        mNewsPager.setAdapter(mAdapter = new CsdnTabAdapter(TITLES, getSupportFragmentManager()));

        mPagerIndicator.setViewPager(mNewsPager, 0);
        mPagerIndicator.setTabItemTitles(TITLES);
    }
}
