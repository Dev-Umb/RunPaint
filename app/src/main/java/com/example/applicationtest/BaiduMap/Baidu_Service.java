package com.example.applicationtest.BaiduMap;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.api.entity.AddEntityResponse;
import com.baidu.trace.api.entity.EntityListResponse;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.model.LocationMode;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.TraceLocation;

public class Baidu_Service extends Service {
    public static final long serviceId = 219103;
    //设备标识
    private static final String entityName = "myTrace";
    //是否需要对象存储服务
    private static boolean isNeedObjectStorage = false;
    //初始化
    private static final com.baidu.trace.Trace myTrace=new com.baidu.trace.Trace(serviceId, entityName, isNeedObjectStorage);;
    //初始化轨迹客户端
    private LBSTraceClient traceClient;
    private OnTraceListener onTraceListener;
    private OnEntityListener entityListener = null;
    private int gatherInterval = 5;
    private int packInterval  =10;
    private boolean isStart=false;
    //开启轨迹服务器

    //手机轨迹标识
    private String imei;
    private void Init()
    {
        onTraceListener = new OnTraceListener() {
            @Override
            public void onBindServiceCallback(int i, String s) {

            }

            @Override
            public void onStartTraceCallback(int i, String s) {

            }

            @Override
            public void onStopTraceCallback(int i, String s) {

            }

            @Override
            public void onStartGatherCallback(int i, String s) {

            }

            @Override
            public void onStopGatherCallback(int i, String s) {

            }

            @Override
            public void onPushCallback(byte b, PushMessage pushMessage) {

            }

            @Override
            public void onInitBOSCallback(int i, String s) {

            }
        };
        traceClient  = new LBSTraceClient(getApplicationContext());
        //设置上传和回调周期
        traceClient.setInterval(gatherInterval,packInterval);
        //s设置定位模式
        traceClient.setLocationMode(LocationMode.High_Accuracy);
        initListener();
        startTrace();
    }
    private void startTrace()
    {
        traceClient.startTrace(myTrace,onTraceListener);
        traceClient.startGather(onTraceListener);
    }
    private void stopTrace()
    {
        traceClient.stopTrace(myTrace,onTraceListener);
    }
    private void initListener()
    {
        entityListener = new OnEntityListener() {
            @Override
            public void onAddEntityCallback(AddEntityResponse addEntityResponse) {
                super.onAddEntityCallback(addEntityResponse);
            }

            @Override
            public void onEntityListCallback(EntityListResponse entityListResponse) {
                super.onEntityListCallback(entityListResponse);
            }

            @Override
            public void onReceiveLocation(TraceLocation traceLocation) {
                super.onReceiveLocation(traceLocation);
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null && intent.getExtras()!=null)
        //从上个活动获取标识
        {
            imei = intent.getStringExtra("imei");
        }
        Init();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
