package com.example.applicationtest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.example.applicationtest.BaiduMap.MyBaiduMap;
import com.example.applicationtest.Frament.BroadCastManager;
import com.example.applicationtest.Frament.Frament1;
import com.example.applicationtest.Frament.Frament2;
import com.example.applicationtest.SideBar.InformationCard;
import com.example.applicationtest.SideBar.MyFriendList;
import com.example.applicationtest.SideBar.MyInformantion;
import com.example.applicationtest.first.FirstActivity;
import com.example.applicationtest.instor.CustomViewPager;
import com.example.applicationtest.instor.MainPageAdapter;
import com.example.applicationtest.setting.setting;
import com.google.android.material.tabs.TabLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Bottom_tabActivity extends AppCompatActivity {
    private IntentFilter intenttFilter;
    private NetWorkChange netWorkChange;
    @BindView(R.id.tabLayout)
    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private int ivTabs[];
    private String TvTabs[];
    private Intent intent;
    private boolean flag=false;
    private LinearLayout linearLayout1;
    private TextView textView,tv;
    private MainPageAdapter mainPageAdapter;
    public String getTitles() {
        return Information;
    }
    public static String Information;
    private Frament1 frament1 = new Frament1();
    private Frament2 frament2  = new Frament2();
    private DrawerLayout drawerLayout;
    private   List<String> permissionList;
    private LocationClient locationClient;
    private  int cnt = 0;
    private SharedPreferences sharedPreferences;
    private ImageView imageView,ImageViewSide,iv;
    private LinearLayout title;
    private boolean can_color= false;
    private ImageView settings;
    public static int red_colors=102,blue_colors=255,Green_colors=204;
//重写位置监听器
    public static boolean isChange = false;

    @Override
    protected void onStart() {
        super.onStart();
        if (isChange)
        {
            changedTheme();
        }
    }

    private class MyLocationListener extends BDAbstractLocationListener
    {

        @Override
        public void onReceiveLocation(final BDLocation bdLocation) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String address = bdLocation.getDistrict();
                    if (cnt == 1) {
                        if (address != null) {
                            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                                Toast.makeText(Bottom_tabActivity.this, "已通过GPS定位" + address, Toast.LENGTH_SHORT).show();
                            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                                Toast.makeText(Bottom_tabActivity.this, "已通过网络定位" + address, Toast.LENGTH_SHORT).show();
                            }
                            Intent intent = new Intent();
                            intent.setAction("加载完成");
                            Bundle bundle = new Bundle();
                            bundle.putString("address", address);
                            BroadCastManager.getInstance().sendBroadCast(Bottom_tabActivity.this,intent);

                            frament1.setArguments(bundle);
                        } else {
                            cnt = 0;
                        }
                    }
                    cnt++;
                }
            });
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        locationClient.stop();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 1:
                if (grantResults.length>0)
                {
                    for (int result:grantResults) {
                        if (result!=PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(Bottom_tabActivity.this, "请给予权限"+permissions.toString(), Toast.LENGTH_SHORT).show();

                            return;
                        }
                    }
                    requestionLocation();
                }else {
                    Toast.makeText(Bottom_tabActivity.this,"发生未知错误，请联系开发者",Toast.LENGTH_SHORT).show();
                        finish();
                }
                break;
            default:
        }
    }

    private void requestionLocation()
    {
        InitLocation();
        locationClient.start();
    }

    private void InitLocation()
    {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(1000);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll");
        locationClient.setLocOption(option);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void QQsignIn(JSONObject json){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            String figureurl = json.getString("figureurl_qq_2");
            String sex = json.getString("gender");
            String city = json.getString("city");
            String birthday = json.getString("year");
            String province = json.getString("province");
            editor.putString("性别",sex);
            editor.putString("所在地",city+province);
            editor.putString("生日",birthday);
            editor.putString("头像",figureurl);
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK && frament2.mwebView.canGoBack())
        {
            frament2.mwebView.goBack();
            return true;
        }else {
            return super.onKeyDown(keyCode,event);
        }
    }

    private void requestPermission()
    {
        locationClient = new LocationClient(Bottom_tabActivity.this.getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());
        permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(Bottom_tabActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(Bottom_tabActivity.this, Manifest.permission.ACTIVITY_RECOGNITION)!= PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.ACTIVITY_RECOGNITION);
        }
        if(ContextCompat.checkSelfPermission(Bottom_tabActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(Bottom_tabActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(Bottom_tabActivity.this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (permissionList!=null&&permissionList.size()!=0)
        {
            String[]permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(Bottom_tabActivity.this,permissions,1);
        }else {
            requestionLocation();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_tab);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        intent = getIntent();
        DisplayImageOptions op = new DisplayImageOptions.Builder().build();
        ImageLoaderConfiguration con = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(op).build();
        ImageLoader.getInstance().init(con);
        findID();
        requestPermission();
        boolean QQflag = intent.getBooleanExtra("QQflag",false);
        Information = intent.getStringExtra("UserName");
        sharedPreferences = getSharedPreferences(Information,MODE_PRIVATE);
        can_color=sharedPreferences.getBoolean("can_color",false);
        if (can_color) {
            red_colors = sharedPreferences.getInt("red", 0);
            blue_colors = sharedPreferences.getInt("blue", 0);
            Green_colors=sharedPreferences.getInt("green",0);
        }
        if (Bottom_tabActivity.red_colors == 255 && Bottom_tabActivity.blue_colors == 255 && Bottom_tabActivity.Green_colors == 255) {
            textView.setTextColor(Color.rgb(0,0,0));
        }
        if (QQflag)
        {
            String QQJSON = intent.getStringExtra("QQJSON");
            try {
                JSONObject jsonObject = new JSONObject(QQJSON);
                QQsignIn(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String url = sharedPreferences.getString("头像",null);
        if (url!=null)
        {
            Glide.with(getApplicationContext())
                .load(url)
                .into(imageView);
            Glide.with(getApplicationContext())
                    .load(url)
                    .into(ImageViewSide);
        }
        Initor_Broadcast();
        ButterKnife.bind(this);//减少findViewById和setOnClickListener的使用，一键绑定
        InitBottom();
        //上部菜单栏
        Initor_menu();

        //侧边栏
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        Button_Click();
        if (ContextCompat.checkSelfPermission(Bottom_tabActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Bottom_tabActivity.this,new String[]{Manifest.permission.INTERNET},1);
        }

    }
    private void changedTheme()
    {
        red_colors = sharedPreferences.getInt("red", 0);
        blue_colors = sharedPreferences.getInt("blue", 0);
        Green_colors=sharedPreferences.getInt("green",0);
        tabLayout.setBackgroundColor(Color.rgb(red_colors,Green_colors,blue_colors));
        title.setBackgroundColor(Color.rgb(red_colors,Green_colors,blue_colors));
        if (red_colors == 255 && blue_colors == 255 && Green_colors == 255) {
            iv.setColorFilter(Color.rgb(0,0,0));
            tv.setTextColor(Color.rgb(0,0,0));
            textView.setTextColor(Color.rgb(0,0,0));
        }
    }
    class NetWorkChange extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);//获取网络状态
            if (connectivityManager!=null) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {
                    Toast.makeText(context, "欢迎回来！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "呐，好像没网络了呢＞﹏＜", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
    @SuppressLint("ResourceAsColor")
    private void findID()
    {
        settings = findViewById(R.id.setting);
        title = findViewById(R.id.titles);
        ImageViewSide = findViewById(R.id.DrawHeadPic);
        imageView = findViewById(R.id.DrawImagButton);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setCanScroll(false);
        tabLayout = findViewById(R.id.tabLayout);
        drawerLayout = findViewById(R.id.drawLayout);
        linearLayout1 = findViewById(R.id.userName_Draw);
        textView = findViewById(R.id.绘跑);
    }
    private void Button_Click()
    {
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bottom_tabActivity.this, setting.class);
                startActivity(intent);
            }
        });
        Button button = findViewById(R.id.close);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("info",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("当前登录账号",null);
                editor.commit();
                Intent intent = new Intent(Bottom_tabActivity.this, FirstActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_dight,R.anim.out_to_left);
                finish();
            }
        });
        final Button information = findViewById(R.id.information_button);
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bottom_tabActivity.this, MyInformantion.class);
                intent.putExtra("UserName", Information);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_dight,R.anim.out_to_left);
            }
        });
        Button friendList = findViewById(R.id.friend_button);
        friendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bottom_tabActivity.this, MyFriendList.class);
                intent.putExtra("UserName",Information);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_dight,R.anim.out_to_left);
            }
        });
        ImageViewSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bottom_tabActivity.this, InformationCard.class);
                intent.putExtra("UserName", Information);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_dight,R.anim.out_to_left);
            }
        });
    }

    @Override
    protected void onPause() {
        if (flag){
            flag=false;
            unregisterReceiver(netWorkChange);
        }
        super.onPause();
    }

    private void Initor_Broadcast() {
        //把网络状态加载进activity
        intenttFilter = new IntentFilter();
        intenttFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netWorkChange = new NetWorkChange();
        if (!flag) {
            registerReceiver(netWorkChange, intenttFilter);
            flag=true;
        }
    }

    private void Initor_menu() {
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(Bottom_tabActivity.this, v);
                getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add_item:
                                Intent intent = new Intent();
                                intent.putExtra("User",Information);
                                intent.setClass(Bottom_tabActivity.this, MyBaiduMap.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.in_from_dight,R.anim.out_to_left);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }


    //初始化底部栏

    @SuppressWarnings("deprecation")
    @SuppressLint({"ClickableViewAccessibility", "ResourceAsColor"})
    private void InitBottom() {
        title.setBackgroundColor(Color.rgb(red_colors,Green_colors,blue_colors));
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(frament1);
        fragments.add(frament2);
        mainPageAdapter = new MainPageAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mainPageAdapter);//绑定监视器
        tabLayout.setupWithViewPager(viewPager);//底部栏绑定
        tabLayout.setTabMode(TabLayout.MODE_FIXED);//设置触摸模式
        tabLayout.setBackgroundColor(Color.rgb(red_colors,Green_colors,blue_colors));
        ivTabs = new int[]{R.drawable.ic_home_black_24dp, R.drawable.ic_dashboard_black_24dp};
        TvTabs = new String[]{"首页", "发现"};
        for (int i = 0; i < fragments.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.main_tab_item, null, false);
            iv = view.findViewById(R.id.ivTab);
            tv = view.findViewById(R.id.tvTab);
            if (red_colors == 255 && blue_colors == 255 && Green_colors == 255) {
                iv.setColorFilter(Color.rgb(0,0,0));
                tv.setTextColor(Color.rgb(0,0,0));
            }
            iv.setImageResource(ivTabs[i]);
            tv.setText(TvTabs[i]);
            if (i == 0) {
                iv.setColorFilter(Color.BLUE);
            }
            tabLayout.getTabAt(i).setCustomView(view);
        }
        tabLayout.setSelectedTabIndicatorHeight(0);
        //绑定触摸监视
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("ResourceAsColor")
            //点击更改
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                iv = customView.findViewById(R.id.ivTab);
                tv = customView.findViewById(R.id.tvTab);
                iv.setColorFilter(Color.BLUE);
                tv.setTextColor(Color.BLUE);
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                ImageView iv = customView.findViewById(R.id.ivTab);
                TextView tv = customView.findViewById(R.id.tvTab);
                if (Bottom_tabActivity.red_colors == 255 && Bottom_tabActivity.blue_colors == 255 && Bottom_tabActivity.Green_colors == 255) {
                    iv.setColorFilter(Color.rgb(0,0,0));
                    tv.setTextColor(Color.rgb(0,0,0));

                } else {
                    iv.setColorFilter(Color.WHITE);
                    tv.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }
}
