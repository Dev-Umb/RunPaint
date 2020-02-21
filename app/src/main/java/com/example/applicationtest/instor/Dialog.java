package com.example.applicationtest.instor;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationtest.R;

public class Dialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_dialog);
    }
}
