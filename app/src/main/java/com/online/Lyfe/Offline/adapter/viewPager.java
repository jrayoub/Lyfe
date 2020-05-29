package com.online.Lyfe.Offline.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.online.Lyfe.Offline.Model.fragment;

import java.util.ArrayList;

public class viewPager extends FragmentPagerAdapter {
    private ArrayList<fragment> fragments = new ArrayList<>();

    public viewPager(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position).getFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addfragment(fragment fragment) {
        this.fragments.add(fragment);
    }
}
