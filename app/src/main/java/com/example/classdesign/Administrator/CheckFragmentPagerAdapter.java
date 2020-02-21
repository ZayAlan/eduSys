package com.example.classdesign.Administrator;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckFragmentPagerAdapter extends FragmentPagerAdapter {
    private int num;
    private List<String> tabIndicators = new ArrayList<>();

    private HashMap<Integer, Fragment> mFragmentHashMap = new HashMap<>();

    public CheckFragmentPagerAdapter(FragmentManager fm,int num){
        super(fm);
        this.num = num;
        tabIndicators.add("个人教师申请");
        tabIndicators.add("教育机构申请");
    }

    @Override
    public Fragment getItem(int position) {
        return createFragment(position);
    }

    @Override
    public int getCount() {
        return num;
    }

    private Fragment createFragment(int pos) {
        Fragment fragment = mFragmentHashMap.get(pos);

        if (fragment == null) {
            switch (pos) {
                case 0:
                    fragment = new CheckTeacherFragment();
                    break;
                case 1:
                    fragment = new CheckInstitutionFragment();
                    break;
            }
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabIndicators.get(position);
    }
}
