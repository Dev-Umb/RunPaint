package com.example.applicationtest.SideBar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationtest.R;

public class MyInformantion extends AppCompatActivity {
    private String UserName;
    private SharedPreferences.Editor editor;
    private EditText sex_Text,birthday_Text,sport_Text,position_Text,interesting_Text,work_Text;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_informantion);
        FrameLayout frameLayout = findViewById(R.id.side_title);
        Intent intent = getIntent();

        UserName = intent.getStringExtra("UserName");


        TextView textView = new TextView(this);
        textView.setText("个人信息");
        textView.setTextColor(R.color.colorPrimaryDark);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        frameLayout.addView(textView);

        sex_Text = findViewById(R.id.sex);
        birthday_Text = findViewById(R.id.birthday);
        work_Text = findViewById(R.id.work);
        sport_Text = findViewById(R.id.sports);
        position_Text = findViewById(R.id.position);
        interesting_Text = findViewById(R.id.interesting);

        final Button button = findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sex= sex_Text.getText().toString();
                String birthday = birthday_Text.getText().toString();
                String work =work_Text.getText().toString();
                String sport = sport_Text.getText().toString();
                String position = position_Text.getText().toString();
                String interesting = interesting_Text.getText().toString();

                SharedPreferences sharedPreferences = getSharedPreferences(UserName, MODE_PRIVATE);
                editor = sharedPreferences.edit();

                editor.putString("性别",sex);
                editor.putString("生日",birthday);
                editor.putString("职业",work);
                editor.putString("运动",sport);
                editor.putString("所在地",position);
                editor.putString("兴趣", interesting);
                editor.commit();
                Toast.makeText(MyInformantion.this, "保存成功！", Toast.LENGTH_SHORT).show();
            }
        });
        RadioButton back = findViewById(R.id.returned);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
