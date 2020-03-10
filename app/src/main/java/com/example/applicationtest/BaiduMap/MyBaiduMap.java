package com.example.applicationtest.BaiduMap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.Time;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.applicationtest.R.id.BaiduMapView;
import static com.example.applicationtest.R.id.Change_back;
import static com.example.applicationtest.R.id.Change_hot;
import static com.example.applicationtest.R.id.Change_traffic;
import static com.example.applicationtest.R.id.DIYColor;
import static com.example.applicationtest.R.id.DIY_Sign;
import static com.example.applicationtest.R.id.add_more;
import static com.example.applicationtest.R.id.saveSign;
import static com.example.applicationtest.R.id.side_title;

public class MyBaiduMap extends Activity implements View.OnClickListener{
        private MapView mapView = null;
        public com.baidu.mapapi.map.BaiduMap mbaiduMap;
        public LocationClient locationClient;
        private DrawerLayout drawerLayout;
        private BDLocationListener myListener;
        private com.baidu.mapapi.model.LatLng latLng;
        private boolean isFirstLoc =true;
        private boolean isTraffic = false;
        private boolean isHot = false;
        private boolean draw = false;
        private RadioButton radioButton;
        private Button button,exit,find_history;
        private ImageView drawing;
        private ListView listView;
        private List<String> list;
        private Handler handler;
        private Gson gson;
        private LinearLayout l;
         private ArrayAdapter<String> adapter;
        //以下是鹰眼标识
        private List<LatLng> latLngs ;
        int cnt=0;
        private AlertDialog.Builder builder;
        private BitmapDescriptor bitmapDescriptor,bitmapDescriptor1;
        private String str,user;
        private int map_sign_write;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_baidu_map);
        handler = new Handler();
        Intent intent = getIntent();
        user = intent.getStringExtra("UserName");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        latLngs = new ArrayList<>();
        mapView = findViewById(BaiduMapView);
        setTitle();
        initMap();
        initLocation();
        SharedPreferences sharedPreferences=getSharedPreferences(user+"Sign",MODE_PRIVATE);
        Map<String,?> signList=sharedPreferences.getAll();
        list=new ArrayList<>(signList.keySet());
        if (list.size()==0)
        {
            list.add("暂时没有记录哦，快去跑步吧");
        }
        adapter=new ArrayAdapter<>(MyBaiduMap.this,R.layout.list_item2,list);
        listView.setAdapter(adapter);
        listOnclick();
        if(ContextCompat.checkSelfPermission(MyBaiduMap.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MyBaiduMap.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
    }
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            latLng = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
            if (bdLocation==null||mapView==null)
            {
                return;
            }
            MyLocationData locationData  =new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .direction(bdLocation.getDirection())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();
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
                    OverlayOptions overlayOptions = new PolylineOptions().width(15).color(map_sign_write).points(latLngs);
                    Overlay overlay = mbaiduMap.addOverlay(overlayOptions);
                }
            }else if (cnt>=2)
            {
                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.stopmin);
                OverlayOptions overlayOptions2 = new MarkerOptions().position(latLngs.get(cnt-1)).icon(bitmapDescriptor).perspective(true).flat(true).draggable(true);
                mbaiduMap.addOverlay(overlayOptions2);
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
        @SuppressLint("ApplySharedPref")
        @Override
        public void run() {
            Time time = new Time();
            if (str == null || str == "") {
                str = time.year + "." + time.month + 1 + "." + time.monthDay + "." + time.hour;
            }
            int i = 0;
            if (latLngs.size() > 2) {
                SharedPreferences sharedPreferences1 = getSharedPreferences(user + "Sign", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                List<String> latitudes = new ArrayList<String>();
                List<String> longitudes = new ArrayList<String>();
                for (LatLng a : latLngs) {
                    latitudes.add(String.valueOf(a.latitude));
                    longitudes.add(String.valueOf(a.longitude));
                }

                SharedPreferences sharedPreferences = getSharedPreferences(str, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                String latit = gson.toJson(latitudes);
                String longs = gson.toJson(longitudes);
                editor.putString("latit", latit);
                editor.putString("longs", longs);


                editor1.putString(str, str);

                editor1.commit();
                editor.commit();
                SharedPreferences sharedPreferences2 = getSharedPreferences(str, MODE_PRIVATE);
                String test = sharedPreferences2.getString("latit", null);
                try {

                    if (test != "[]" && (sharedPreferences2.getString("latit", null)) != "[]") {
                        if (list.get(0) == "暂时没有记录哦，快去跑步吧") {
                            list.remove(list.get(0));
                        }
                        Toast.makeText(getApplicationContext(), "数据保存成功", Toast.LENGTH_SHORT).show();
                        list.add(str);
                    } else {
                        Toast.makeText(getApplicationContext(), "数据保存失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Looper.prepare();
                    if ((test != null && test != "[]")) {
                        if (list.size()!=0) {
                            if (list.get(0) == "暂时没有记录哦，快去跑步吧") {
                                list.remove(list.get(0));
                            }
                        }
                        Toast.makeText(getApplicationContext(), "数据保存成功", Toast.LENGTH_SHORT).show();
                        list.add(str);
                    } else {
                        Toast.makeText(getApplicationContext(), "数据保存失败", Toast.LENGTH_SHORT).show();
                    }

                    Looper.loop();
                } finally {
                    new Thread() {
                        public void run() {
                            handler.post(adapterUi);
                        }
                    }.start();
                }
                latLngs.clear();
            } else {
                Toast.makeText(getApplicationContext(), "数据保存失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private void sign_color()
    {
        Button Diy_Blue,DIY_yellow,DIY_Pink,DIY_Black,DIY_Orange,DIY_green,DIY_RED;
        Diy_Blue=findViewById(R.id.DIY_BLUE);
        Diy_Blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"已设置轨迹为淡蓝色",Toast.LENGTH_SHORT).show();
                map_sign_write=Color.rgb(102,204,255);
                l.setVisibility(View.GONE);
            }
        });
        DIY_RED=findViewById(R.id.DIY_RED);
        DIY_RED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"已设置轨迹为红色",Toast.LENGTH_SHORT).show();
                map_sign_write=Color.rgb(255,0,0);
                l.setVisibility(View.GONE);
            }
        });
        DIY_Pink = findViewById(R.id.DIY_PINK);
        DIY_Pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"已设置轨迹为粉色",Toast.LENGTH_SHORT).show();
                map_sign_write=Color.rgb(239,217,245);
                l.setVisibility(View.GONE);
            }
        });
        DIY_Black=findViewById(R.id.DIY_BLACK);
        DIY_Black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"已设置轨迹为黑色",Toast.LENGTH_SHORT).show();
                map_sign_write=Color.rgb(0,0,0);
                l.setVisibility(View.GONE);
            }
        });
        DIY_green=findViewById(R.id.DIY_GREEN);
        DIY_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"已设置轨迹为绿色",Toast.LENGTH_SHORT).show();
                map_sign_write=Color.rgb(76,175,80);
                l.setVisibility(View.GONE);
            }
        });
        DIY_yellow=findViewById(R.id.DIY_YELLOW);
        DIY_yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"已设置轨迹为黄色",Toast.LENGTH_SHORT).show();
                map_sign_write=Color.rgb(255,235,59);
                l.setVisibility(View.GONE);
            }
        });
        DIY_Orange=findViewById(R.id.DIY_ORANGE);
        DIY_Orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"已设置轨迹为橙色",Toast.LENGTH_SHORT).show();
                map_sign_write=Color.rgb(255,152,0);
            }
        });
    }
    Runnable adapterUi=new Runnable() {
        @Override
        public void run() {
            adapter.notifyDataSetChanged();
        }
    };
    private void Button_Onclick() {


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latLngs.size()>3) {
                    save_NameBuilder();
                    if (draw && cnt >= 2) {
                        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.stopmin);
                        OverlayOptions overlayOptions2 = new MarkerOptions().position(latLng).icon(bitmapDescriptor).perspective(true).flat(true).draggable(true);
                        mbaiduMap.addOverlay(overlayOptions2);
                        draw=false;
                    }
                    exit.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                    drawing.setVisibility(View.VISIBLE);
                }else {
                    try {
                        Toast.makeText(getApplicationContext(),"轨迹太短了哦，再多跑一会吧",Toast.LENGTH_SHORT).show();
                    }catch (Exception e)
                    {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(),"轨迹太短了哦，再多跑一会吧",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
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
                }
                else {
                    Toast.makeText(MyBaiduMap.this,"绘制已开启",Toast.LENGTH_SHORT).show();
                    button.setVisibility(View.VISIBLE);
                    exit.setVisibility(View.VISIBLE);
                    draw=true;
                    drawing.setVisibility(View.GONE);
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
                drawing.setVisibility(View.VISIBLE);
            }
        });
        find_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });


    }
    private void listOnclick()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
                SharedPreferences sharedPreferences = getSharedPreferences(list.get(position), MODE_PRIVATE);
                try {
                    String latit = sharedPreferences.getString("latit", null);
                    String longs = sharedPreferences.getString("longs", null);
                    if (latit != null && longs != null && latit != "[]" && longs != "[]") {
                        List<String> lat = gson.fromJson(latit, new TypeToken<List<String>>() {
                        }.getType());
                        List<String> lon = gson.fromJson(longs, new TypeToken<List<String>>() {
                        }.getType());
                        List<LatLng> latLngs = new ArrayList<>();
                        latLngs.add(latLng);
                        double templatings,tempLong;
                        for (int i = 0; i < lat.size()-1; i++) {
                           templatings=latLngs.get(i).latitude+Double.valueOf(lat.get(i+1))-Double.valueOf(lat.get(i));
                           tempLong=latLngs.get(i).longitude+Double.valueOf(lon.get(i+1))-Double.valueOf(lon.get(i));
                            LatLng latLng = new LatLng(templatings, tempLong);
                            latLngs.add(latLng);
                        }
                        bitmapDescriptor1 = BitmapDescriptorFactory.fromResource(R.drawable.startmin);
                        OverlayOptions overlayOptions_start = new MarkerOptions().position(latLngs.get(0)).icon(bitmapDescriptor1).perspective(true).flat(true).draggable(true);
                        mbaiduMap.addOverlay(overlayOptions_start);
                        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.stopmin);
                        OverlayOptions overlayOptions2 = new MarkerOptions().position(latLngs.get(latLngs.size()-1)).icon(bitmapDescriptor).perspective(true).flat(true).draggable(true);
                        mbaiduMap.addOverlay(overlayOptions2);
                        OverlayOptions overlayOptions = new PolylineOptions().width(15).color(map_sign_write).points(latLngs);
                        Overlay overlay = mbaiduMap.addOverlay(overlayOptions);
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "数据有误！", Toast.LENGTH_SHORT).show();
                }
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


    private void initMap()
    {
        map_sign_write=Color.rgb(102,204,255);
        listView=findViewById(R.id.history_list);
        gson=new Gson();
        drawerLayout=findViewById(R.id.map_draw);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,Gravity.RIGHT);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,Gravity.RIGHT);
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
                            case Change_back:
                                isFirstLoc = true;
                                MapStatusUpdate mapStatusUpdateFactory = MapStatusUpdateFactory.newLatLng(latLng);
                                mbaiduMap.animateMapStatus(mapStatusUpdateFactory);
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
                            case DIY_Sign:
                                l = findViewById(DIYColor);
                                sign_color();
                                l.setVisibility(View.VISIBLE);
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

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){


        }
    }
}
