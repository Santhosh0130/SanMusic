package com.example.sanmusic.AdapterClasses;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class StateFragmentAdapter extends FragmentStateAdapter {

    public List<Fragment> mItems;
    public FragmentManager mFragmentManager;
    public StateFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        this.mItems = new ArrayList<>();
        this.mFragmentManager = fragmentManager;
    }

    public void addFragment(Fragment fragment){
        mItems.add(fragment);
    }

    public Fragment getFragment(int position){
        return this.mItems.get(position);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
