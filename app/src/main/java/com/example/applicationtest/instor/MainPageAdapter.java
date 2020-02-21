package com.example.applicationtest.instor;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class MainPageAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragmentsList = null;
    public Fragment currentFragment;

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        this.currentFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }

    public MainPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public MainPageAdapter(FragmentManager fm,List<Fragment> list){
        super(fm);
        this.fragmentsList =list;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }
}
