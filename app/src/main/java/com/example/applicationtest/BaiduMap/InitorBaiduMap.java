package com.example.applicationtest.BaiduMap;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

public class InitorBaiduMap extends Application {
    @Override
    public void onCreate() {
        SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.BD09LL);

        super.onCreate();
    }
}
