package com.example.tanay.projectscheduler.Utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.tanay.projectscheduler.Fragments.ProjectListFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    Context context;
    ArrayList<Fragment> fragList;
    ArrayList<String> fragTitleList;

    public ViewPagerAdapter(FragmentManager fm, Context context, ArrayList<Fragment> fragList,ArrayList<String> fragTitleList) {
        super(fm);
        this.context = context;
        this.fragList = fragList;
        this.fragTitleList = fragTitleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragList.get(position);
    }

    @Override
    public int getCount() {
        return fragList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragTitleList.get(position);
    }
}
