package com.example.applicationtest.Frament;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.example.applicationtest.BaiduMap.MyBaiduMap;
import com.example.applicationtest.Bottom_tabActivity;
import com.example.applicationtest.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.search.Search;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class Frament1 extends Fragment {
    private TextView city,Coud,everyDayText;
    private View view;
    private Boolean flag;
    private String cityName,temp,text;
    private ImageView imageView,everyImage,run;
    private String address;
    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private SensorManager sensorManager;
    private String uri;
    private String User;
    private Activity activity;
    private SharedPreferences sharedPreferences;
    private Sensor sensor;
    private void receiveAction()
    {
        Activity activity = getActivity();
        if (activity!=null)
        {
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        localReceiver = new LocalReceiver();
        intentFilter.addAction("加载完成");
        BroadCastManager.getInstance().registerRecive(getActivity(),localReceiver,intentFilter);
        }
    }
    class LocalReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            new Thread(runnable).start();
        }
    }
    @Override
    public void onStart() {

        if (!flag) {
            everyDayText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("SignIn",true);
                        editor.commit();
                        Toast.makeText(getContext(), "签到成功！", Toast.LENGTH_SHORT).show();
                        flag=true;
                        new Thread(runnableEveryDay).start();
                    }
            });
        }else {
            new Thread(runnableEveryDay).start();
        }
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyBaiduMap.class);
                intent.putExtra("UserName",User);
                startActivity(intent);
                Activity activity = getActivity();
                if (activity!=null)
                getActivity().overridePendingTransition(R.anim.in_from_dight,R.anim.out_to_left);
            }
        });
        super.onStart();
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        User = ((Bottom_tabActivity)activity).getTitles();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_frament1,container,false);
        DisplayImageOptions op = new DisplayImageOptions.Builder().build();
        activity=getActivity();

        if(activity!=null) {
            ImageLoaderConfiguration con = new ImageLoaderConfiguration.Builder(getActivity()).defaultDisplayImageOptions(op).build();
            ImageLoader.getInstance().init(con);
            findID();
            HeConfig.init("HE2002071631541633", "f41c046df41041068e74c21343b53a33");
            HeConfig.switchToFreeServerNode();
            sharedPreferences = getActivity().getSharedPreferences(User, MODE_PRIVATE);
            flag = sharedPreferences.getBoolean("SignIn",false);
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if (hour == 12) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("SignIn", false);
                editor.commit();
            }
            receiveAction();
        }
        return view;
    }
     private   Runnable runnableEveryDay = new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("https://api.ooopn.com/ciba/api.php").build();
                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    if (response.isSuccessful()&&response.body()!=null) {
                        String responseData = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);
                        text = jsonObject.getString("ciba");
                        uri = jsonObject.getString("imgurl");
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            everyDayText.setText(text);
                            everyDayText.setTextColor(Color.WHITE);
                            if (Bottom_tabActivity.red_colors == 255 && Bottom_tabActivity.blue_colors == 255 && Bottom_tabActivity.Green_colors == 255) {
                                everyDayText.setBackgroundColor(Color.rgb(0,0,0));
                            }else {
                                everyDayText.setBackgroundColor(Color.rgb(Bottom_tabActivity.red_colors, Bottom_tabActivity.Green_colors, Bottom_tabActivity.blue_colors));
                            }
                            everyDayText.setTextSize(15);
                            everyDayText.setGravity(Gravity.CENTER_HORIZONTAL);
                            //ImageLoader.getInstance().displayImage(uri, everyImage);
                            Glide.with(getContext())
                                    .load(uri)
                                    .into(everyImage);
                        }
                    });
                }
            }
        };


    private void findID()
    {

        run = view.findViewById(R.id.run);
        everyDayText = view.findViewById(R.id.everyDayText);
        everyImage = view.findViewById(R.id.everyDayPicture);
        if (activity!=null) {
            sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        }
        if (sensorManager!=null){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }
        linearLayout = view.findViewById(R.id.tianqi);
        linearLayout.setVisibility(View.GONE);
        progressBar = view.findViewById(R.id.jiazai);
        progressBar.setVisibility(View.VISIBLE);
        city = view.findViewById(R.id.city);
        imageView = view.findViewById(R.id.weather);
        Coud = view.findViewById(R.id.Cond);
    }

    @Override
    public void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

    private void fetchDataByPost()
    {

        HeWeather.getSearch(getActivity(),address,"world",1,Lang.CHINESE_SIMPLIFIED, new HeWeather.OnResultSearchBeansListener() {

            @Override
            public void onError(Throwable throwable) {}

            @Override
            public void onSuccess(Search search) {
                if (search.getBasic() != null) {
                        Basic basic = search.getBasic().get(0);
                        final String BasicCid = basic.getCid();
                        cityName = basic.getLocation();
                        city.setText(cityName);
                        city.setTextColor(Color.BLACK);
                        city.setGravity(Gravity.CENTER_HORIZONTAL);
                        city.setTextSize(20);
                        HeWeather.getWeatherNow(getActivity(), BasicCid, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherNowBeanListener() {
                            @Override
                            public void onError(Throwable throwable) {
                                Log.i(TAG, "onError!", throwable);
                            }
                            @Override
                            public void onSuccess(Now now) {
                                if (Code.OK.getCode().equalsIgnoreCase(now.getStatus())) {
                                    NowBase nowBase = now.getNow();
                                    Coud.setText("天气：" + nowBase.getCond_txt() + "    " + "当前温度： " + nowBase.getFl());
                                    Coud.setTextColor(Color.BLACK);
                                    Coud.setTextSize(20);
                                    String weather_ic = nowBase.getCond_code();
                                    int i = search_ic(weather_ic);
                                    imageView.setImageResource(i);
                                    imageView.setMaxWidth(20);
                                    imageView.setMaxWidth(20);
                                    temp = now.getStatus();
                                } else {
                                    String status = now.getStatus();
                                    Code code = Code.toEnum(status);
                                    Log.i(TAG, "failed" + code);
                                }
                            }
                        });
                }else {
                    Toast.makeText(getContext(),"请求失败！",Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

   private Runnable runnable  = new Runnable() {
        @Override
        public void run() {

                Bundle bundle = getArguments();
                if (bundle.getString("address")!=null ){
                    address = bundle.getString("address");
                    fetchDataByPost();
                }else {
                    Toast.makeText(getContext(),"请求失败！",Toast.LENGTH_SHORT).show();
            }
        }
    };
    private int search_ic(String weather)
    {
        switch (weather){
            case "100":
               return R.drawable.weather_100;
            case "101":
                return R.drawable.weather_101;
            case "102":
                return R.drawable.w_102;
            case "103":
                return R.drawable.w_103;
            case "104":
                return R.drawable.w_104;
            case "200":
                return R.drawable.w_200;
            case "201":
                return R.drawable.w_201;
            case "202":
                return R.drawable.w_202;
            case "203":
                return R.drawable.w_203;
            case "204":
                return R.drawable.w_204;
            case "205":
                return R.drawable.w_205;
            case "206":
                return R.drawable.w_206;
            case "207":
                return R.drawable.w_207;
            case "208":
                return R.drawable.w_208;
            case "209":
                return R.drawable.w_209;
            case "210":
                return R.drawable.w_210;
            case "211":
                return R.drawable.w_211;
            case "212":
                return R.drawable.w_212;
            case "213":
                return R.drawable.w_213;
            case "300":
                return R.drawable.w_300;
            case "301":
                return R.drawable.w_301;
            case "302":
                return R.drawable.w_302;
            case "303":
                return R.drawable.w_303;
            case "304":
                return R.drawable.w_304;
            case "305":
                return R.drawable.w_305;
            case "306":
                return R.drawable.w_306;
            case "307":
                return R.drawable.w_307;
            case "308":
                return R.drawable.w_308;
            case "309":
                return R.drawable.w_309;
            case "310":
                return R.drawable.w_310;
            case "311":
                return R.drawable.w_311;
            case "312":
                return R.drawable.w_312;
            case "313":
                return R.drawable.w_313;
            case "314":
                return R.drawable.w_314;
            case "315":
                return R.drawable.w_315;
            case "316":
                return R.drawable.w_316;
            case "317":
                return R.drawable.w_317;
            case "318":
                return R.drawable.w_318;
            case "399":
                return R.drawable.w_399;
            case "400":
                return R.drawable.w_400;
            case "401":
                return R.drawable.w_401;
            case "402":
                return R.drawable.w_402;
            case "403":
                return R.drawable.w_403;
            case "404":
                return R.drawable.w_404;
            case "405":
                return R.drawable.w_405;
            case "406":
                return R.drawable.w_406;
            case "407":
                return R.drawable.w_407;
            case "408":
                return R.drawable.w_408;
            case "409":
                return R.drawable.w_409;
            case "410":
                return R.drawable.w_410;
            case "499":
                return R.drawable.w_499;
            case "500":
                return R.drawable.w_500;
            case "501":
                return R.drawable.w_501;
            case "502":
                return R.drawable.w_502;
            case "503":
                return R.drawable.w_503;
            case "504":
                return R.drawable.w_504;
            case "507":
                return R.drawable.w_507;
            case "508":
                return R.drawable.w_508;
            case "509":
                return R.drawable.w_509;
            case "511":
                return R.drawable.w_511;
            case "512":
                return R.drawable.w_512;
            case "513":
                return R.drawable.w_513;
            case "514":
                return R.drawable.w_514;
            case "515":
                return R.drawable.w_515;
            case "900":
                return R.drawable.w_900;
            case "901":
                return R.drawable.w_901;
            case "999":
                return R.drawable.w_999;

        }
        return R.drawable.w_999;
    }


}

