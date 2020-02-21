package com.example.applicationtest.instor;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class Adapter extends PagerAdapter {
    private List<View> myViewList;
    public Adapter(List<View> myViewList)
    {
        this.myViewList=myViewList;
    }
    @Override
    public int getCount() {
        if(myViewList.size()!=0) {
            return myViewList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(myViewList.get(position));
        return myViewList.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(myViewList.get(position));
    }
}
