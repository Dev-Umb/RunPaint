package com.example.applicationtest.instor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {
    private boolean isCanScroll;
    public CustomViewPager(@NonNull Context context) {
        super(context);
    }

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public void setCanScroll(boolean isCanScroll)
    {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCanScroll  && super.onInterceptTouchEvent(ev);
    }
}
