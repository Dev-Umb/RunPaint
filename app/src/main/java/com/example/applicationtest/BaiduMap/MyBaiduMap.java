package com.example.applicationtest.BaiduMap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.text.format.Time;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.applicationtest.Bottom_tabActivity;
import com.example.applicationtest.R;
import com.example.applicationtest.instor.newDialog;

import java.util.ArrayList;
import java.util.List;

import static com.example.applicationtest.R.id.BaiduMapView;
import static com.example.applicationtest.R.id.Change_back;
import static com.example.applicationtest.R.id.Change_hot;
import static com.example.applicationtest.R.id.Change_putong;
import static com.example.applicationtest.R.id.Change_start;
import static com.example.applicationtest.R.id.Change_traffic;
import static com.example.applicationtest.R.id.add_more;
import static com.example.applicationtest.R.id.openDraw;
import static com.example.applicationtest.R.id.saveSign;
import static com.example.applicationtest.R.id.side_title;

public class MyBaiduMap extends Activity implements View.OnClickListener{
        private MapView mapView = null;
        public com.baidu.mapapi.map.BaiduMap mbaiduMap;
        public LocationClient locationClient;
        private BDLocationListener myListener;
        private com.baidu.mapapi.model.LatLng latLng;
        private boolean isFirstLoc =true;
        private boolean isTraffic = false;
        private boolean isHot = false;
        private boolean draw = false;
        private RadioButton radioButton;
        private Button button,exit,find_history;
        private ImageView drawing;
        //以下是鹰眼标识
        private List<LatLng> latLngs ;
        int cnt=0;
        private AlertDialog.Builder builder;
        private BitmapDescriptor bitmapDescriptor,bitmapDescriptor1;
        private String str,user;
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            latLng = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
            if (bdLocation==null||mapView==null)
            {
                return;
            }
            MyLocationData locationData  =new MyLocationData.Builder().accuracy(bdLocation.getRadius()).direction(bdLocation.getDirection()).latitude(bdLocation.getLatitude()).longitude(bdLocation.getLongitude()).build();
            mbaiduMap.setMyLocationData(locationData);
            if(isFirstLoc)
            {
                isFirstLoc  =false;
                com.baidu.mapapi.model.LatLng ll = new com.baidu.mapapi.model.LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mbaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));//设置动画效果
                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation)//GPS定位结果，告知
                {
                    Toast.makeText(MyBaiduMap.this,"已通过GPS定位"+ bdLocation.getAddrStr(), Toast.LENGTH_SHORT).show();
                }else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation)//网络定位结果
                {
                    Toast.makeText(MyBaiduMap.this,"已通过网络定位"+ bdLocation.getAddrStr(), Toast.LENGTH_SHORT).show();
                }else if(bdLocation.getLocType() == BDLocation.TypeOffLineLocation)//离线定位结果
                {
                    Toast.makeText(MyBaiduMap.this, "离线定位"+bdLocation.getAddrStr(), Toast.LENGTH_SHORT).show();
                }else if(bdLocation.getLocType() == BDLocation.TypeServerError)
                {
                    Toast.makeText(MyBaiduMap.this, "服务器错误！", Toast.LENGTH_SHORT).show();
                }else if(bdLocation.getLocType() == BDLocation.TypeNetWorkException)
                {
                    Toast.makeText(MyBaiduMap.this, "网络好像出现了问题(⊙﹏⊙)", Toast.LENGTH_SHORT).show();
                }else if(bdLocation.getLocType() == BDLocation.TypeCriteriaException)
                {
                    Toast.makeText(MyBaiduMap.this, "您的手机可能起飞了呢，请关闭飞行模式并降落", Toast.LENGTH_SHORT).show();
                }
            }

            if (draw) {
                latLngs.add(latLng);
                bitmapDescriptor1 = BitmapDescriptorFactory.fromResource(R.drawable.startmin);
                OverlayOptions overlayOptions2 = new MarkerOptions().position(latLngs.get(0)).icon(bitmapDescriptor1).perspective(true).flat(true).draggable(true);
                mbaiduMap.addOverlay(overlayOptions2);
                cnt++;
                if (latLngs.size() >= 2) {
                    OverlayOptions overlayOptions = new PolylineOptions().width(15).color(Color.BLUE).points(latLngs);
                    Overlay overlay = mbaiduMap.addOverlay(overlayOptions);
                }
            }else if (cnt>=2)
            {
                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.stopmin);
                OverlayOptions overlayOptions2 = new MarkerOptions().position(latLngs.get(cnt-1)).icon(bitmapDescriptor).perspective(true).flat(true).draggable(true);
                mbaiduMap.addOverlay(overlayOptions2);
                latLngs.clear();
            }
            Button_Onclick();

        }
    }
    private void save_NameBuilder()
    {

        final EditText et = new EditText(this);
        new AlertDialog.Builder(this).setTitle("请输入轨迹名称")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        str= et.getText().toString();
                        new Thread(Sql).start();
                    }
                }).setNegativeButton("取消",null).show();
    }
    Runnable Sql = new Runnable() {
        @Override
        public void run() {
            Time time = new Time();
                if (str == null) {
                    str = time.year + "." + time.month + 1 + "." + time.monthDay + "." + time.hour;
                }
                int i = 0;

                if (latLngs!=null) {
                    try {
                        Toast.makeText(getApplicationContext(), "数据保存成功", Toast.LENGTH_SHORT).show();
                    }catch (Exception e)
                    {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "数据保存成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                    latLngs.clear();
                }else {
                    try {
                        Toast.makeText(getApplicationContext(), "数据保存失败", Toast.LENGTH_SHORT).show();
                    }catch (Exception e)
                    {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "数据保存失败", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }
        }
    };
    private void Button_Onclick() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latLngs.size()!=0) {
                    save_NameBuilder();
                    if (draw && cnt >= 2) {
                        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.stopmin);
                        OverlayOptions overlayOptions2 = new MarkerOptions().position(latLng).icon(bitmapDescriptor).perspective(true).flat(true).draggable(true);
                        mbaiduMap.addOverlay(overlayOptions2);
                        draw=false;
                    }
                    exit.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                }
            }
        });
        drawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (draw && cnt >= 2) {
                    Toast.makeText(MyBaiduMap.this,"绘制已关闭",Toast.LENGTH_SHORT).show();
                    bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.stopmin);
                    OverlayOptions overlayOptions2 = new MarkerOptions().position(latLng).icon(bitmapDescriptor).perspective(true).flat(true).draggable(true);
                    mbaiduMap.addOverlay(overlayOptions2);
                    exit.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                    draw=false;
                } else {
                    Toast.makeText(MyBaiduMap.this,"绘制已开启",Toast.LENGTH_SHORT).show();
                    button.setVisibility(View.VISIBLE);
                    exit.setVisibility(View.VISIBLE);
                    draw=true;
                }
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latLngs.clear();
                exit.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
                draw=false;
            }
        });
        find_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyBaiduMap.this, newDialog.class);
                intent.putExtra("UserName",user);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {

                }else {
                    Toast.makeText(this,"未授权",Toast.LENGTH_SHORT).show();
                }
        }
    }
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_baidu_map);
        Intent intent = getIntent();
        user = intent.getStringExtra("UserName");
        latLngs = new ArrayList<>();
        mapView = findViewById(BaiduMapView);
        setTitle();
        initMap();
        initLocation();
        if(ContextCompat.checkSelfPermission(MyBaiduMap.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MyBaiduMap.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
    }
    private void initMap()
    {
        drawing = findViewById(R.id.draw);
        builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("设置名称");
        button = findViewById(saveSign);
        exit = findViewById(R.id.exit);
        exit.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        locationClient=new LocationClient(getApplicationContext());
        myListener = new MyLocationListener();
        mbaiduMap = mapView.getMap();
        mbaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mbaiduMap.setMyLocationEnabled(true);
        //设置监听器
        find_history = findViewById(R.id.find_history);
        mbaiduMap.getUiSettings().setCompassEnabled(false);
        locationClient.registerLocationListener(myListener);
        locationClient.start();
        locationClient.requestLocation();
        mbaiduMap.getUiSettings().setCompassEnabled(true);
    }
    private void initLocation()
    {
        LocationClientOption option = new LocationClientOption();
        //设置定位模式为高精度
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");//返回定位结果坐标系
        option.setScanSpan(5000);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        //返回的定位结果包含手机机头方向
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setIsNeedAddress(true);
        //是否在stop时杀死定位进程
        option.SetIgnoreCacheException(true);//保持运行
        //
        option.setEnableSimulateGps(true);
        locationClient.setLocOption(option);
    }
    private void Initor_menu() {
        findViewById(add_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MyBaiduMap.this, v);
                getMenuInflater().inflate(R.menu.change_map, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case Change_putong:
                                mbaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                                return true;
                            case Change_back:
                                isFirstLoc = true;
                                MapStatusUpdate mapStatusUpdateFactory = MapStatusUpdateFactory.newLatLng(latLng);
                                mbaiduMap.animateMapStatus(mapStatusUpdateFactory);
                                return true;
                            case Change_start:
                                mbaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                                return true;
                            case Change_traffic:
                                if (isTraffic) {
                                    mbaiduMap.setTrafficEnabled(false);
                                    isTraffic = false;
                                } else {
                                    mbaiduMap.setTrafficEnabled(true);
                                    isTraffic = true;
                                }
                                break;
                            case Change_hot:
                                if (isHot) {
                                    mbaiduMap.setBaiduHeatMapEnabled(false);
                                    isHot = false;
                                } else {
                                    mbaiduMap.setBaiduHeatMapEnabled(true);
                                    isHot = true;
                                }
                                break;
                            case openDraw:
                                if (draw)
                                {
                                    Toast.makeText(MyBaiduMap.this,"绘制已关闭",Toast.LENGTH_SHORT).show();
                                    button.setVisibility(View.GONE);
                                    exit.setVisibility(View.GONE);
                                    draw=false;
                                }else {
                                    Toast.makeText(MyBaiduMap.this,"绘制已开启",Toast.LENGTH_SHORT).show();
                                    button.setVisibility(View.VISIBLE);
                                    exit.setVisibility(View.VISIBLE);
                                    draw=true;
                                }
                            default:
                        }
                    return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void setTitle()
    {
        FrameLayout frameLayout = findViewById(side_title);
        frameLayout.setBackgroundColor(Color.rgb(Bottom_tabActivity.red_colors,Bottom_tabActivity.Green_colors,Bottom_tabActivity.blue_colors));
        TextView textView  = new TextView(this);
        radioButton = findViewById(R.id.returned);
        textView.setText("地图");
        settitleClose();
        if (Bottom_tabActivity.red_colors==255 &&Bottom_tabActivity.blue_colors==255&&Bottom_tabActivity.Green_colors==255)
        {
            textView.setTextColor(Color.rgb(0, 0, 0));
        }else {
            textView.setTextColor(Color.rgb(255, 255, 255));
        }
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        frameLayout.addView(textView);

        Initor_menu();
    }
    private void settitleClose()
    {

        if (Bottom_tabActivity.red_colors==255 &&Bottom_tabActivity.blue_colors==255&&Bottom_tabActivity.Green_colors==255)
        {
            radioButton.setButtonTintList(ColorStateList.valueOf(Color.rgb(0, 0, 0)));
        }
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        locationClient.stop();
        mbaiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView=null;
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
        }
    }
}
