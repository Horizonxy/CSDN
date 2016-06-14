package com.horizon.csdn.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.horizon.csdn.fragment.CsdnTypeListFragment;

import java.util.List;

public class CsdnTabAdapter extends FragmentPagerAdapter {

    private List<String> mTitles;

    public CsdnTabAdapter(List<String> mTitles, FragmentManager fm) {
        super(fm);
        this.mTitles = mTitles;
    }

    @Override
    public int getCount() {
        return mTitles == null ? 0 : mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return new CsdnTypeListFragment().newInstance(position + 1);
    }
}
