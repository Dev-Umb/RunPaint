package com.example.applicationtest.first;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.applicationtest.R;
import com.example.applicationtest.instor.Adapter;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


public class SplashActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private ViewPager viewPager;
    private List<View> viewList;
    private View view1,view2,view3,view4;//切换页面
    private int currentItem;//默认首页
    private Button startButton;//进入按键
    private boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        setContentView(R.layout.activity_splash);
        initViewPager();
        SharedPreferences sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
        int flag = sharedPreferences.getInt("Flag",0);
        if(flag==1)
        {
            flag=2;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("Flag",flag);
            editor.commit();
        }

    }

    private void initViewPager(){
        viewPager  = findViewById(R.id.viewPager);
        viewList  = new ArrayList<View>();
        view1 = View.inflate(SplashActivity.this,R.layout.view1,null);
        view2 = View.inflate(SplashActivity.this,R.layout.view2,null);
        view3 = View.inflate(SplashActivity.this,R.layout.view3,null);
        view4 = View.inflate(SplashActivity.this,R.layout.activity_sing_in,null);
        viewList.add(view1);
        viewList.add(view2);
        //viewList.add(view3);
        viewList.add(view4);
        Adapter adapter = new Adapter(viewList);
        viewPager.setAdapter(adapter);
        //绑定适配器
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                if (currentItem == viewList.size() - 1) {
                    startButton = findViewById(R.id.start);
                    startButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SplashActivity.this, FirstActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        CircleIndicator indicator =findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        indicator.createIndicators(3,0);
        indicator.animatePageSelected(0);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());

    }
    @Override
    public void onClick(View v) {
        viewPager.setCurrentItem((Integer) v.getTag());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
