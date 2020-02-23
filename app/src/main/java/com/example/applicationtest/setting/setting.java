package com.example.applicationtest.setting;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationtest.Bottom_tabActivity;
import com.example.applicationtest.R;

import java.util.ArrayList;
import java.util.List;

public class setting extends AppCompatActivity {
    private FrameLayout frameLayout;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        List<String> data=intData();
        List<Integer> red=initRed(),blue=initBlue(),
                green=initGreen();
        frameLayout =findViewById( R.id.side_title2);
        textView=findViewById(R.id.主题设置);
        frameLayout.setBackgroundColor(Color.rgb( Bottom_tabActivity.red_colors,Bottom_tabActivity.Green_colors,Bottom_tabActivity.blue_colors));

        RecyclerView recyclerView = findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new NomalAdapter(data,red,blue,green));
        RadioButton radioButton = findViewById(R.id.returned_theme);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (Bottom_tabActivity.red_colors==0&&Bottom_tabActivity.Green_colors==0&&Bottom_tabActivity.blue_colors==0)
        {
            textView.setTextColor(Color.rgb(255,255,255));
            radioButton.setButtonTintList(ColorStateList.valueOf(Color.rgb(255,255,255)));
        }else {
            textView.setTextColor(Color.rgb(0,0,0));
        }
    }
    private List<String> intData()
    {
        List<String> data = new ArrayList<>();
        data.add("  天依蓝");
        data.add("   天依蓝");
        data.add("  阳光黄");
        data.add("  乐正红");
        data.add("  极简白");
        data.add("  酷炫黑");
        return data;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private List<Integer> initRed()
    {
        List<Integer> data = new ArrayList<>();
        data.add(102);
        data.add(255);
        data.add(255);
        data.add(255);
        data.add(255);
        data.add(0);
        return data;
    }
    private List<Integer> initGreen()
    {
        List<Integer> data = new ArrayList<>();
        data.add(204);
        data.add(152);
        data.add(235);
        data.add(0);
        data.add(255);
        data.add(0);
        return data;
    }
    private List<Integer> initBlue()
    {
        List<Integer> data = new ArrayList<>();
        data.add(255);
        data.add(0);
        data.add(59);
        data.add(0);
        data.add(255);
        data.add(0);
        return data;
    }
}
