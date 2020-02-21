package com.example.applicationtest.first;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationtest.Bottom_tabActivity;
import com.example.applicationtest.R;

public class OpenPicture extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_picture);
        SharedPreferences sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int flag = 1;

        if (sharedPreferences.getInt("Flag", 0)==2) {
            String nowSingIn = sharedPreferences.getString("当前登录账号",null);
            if(nowSingIn==null) {
                Intent intent = new Intent(OpenPicture.this, FirstActivity.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(OpenPicture.this, Bottom_tabActivity.class);
                intent.putExtra("UserName", nowSingIn);
                startActivity(intent);
                finish();
            }
        } else if (sharedPreferences.getInt("Flag",0)==1){
            Intent intent = new Intent(OpenPicture.this,SplashActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            editor.putInt("Flag",flag);
            Intent intent = new Intent(OpenPicture.this,SplashActivity.class);
            editor.commit();
            startActivity(intent);
            finish();
        }
    }
}
