package com.example.applicationtest.first;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationtest.R;

public class Forget extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private EditText users;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        button = findViewById(R.id.find_yes);

        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                    button.setBackgroundColor(R.color.jiemian);
                }
        });
        users =findViewById(R.id.find_users);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.find_yes:
                SharedPreferences sharedPreferences = getSharedPreferences("info",MODE_PRIVATE);//获取输入到的数据
                String result = sharedPreferences.getString(users.getText().toString(),null);
                if (result==null)
                {
                    Toast.makeText(Forget.this, "查无此人", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent();
                    intent.setClass(Forget.this, forget_question.class);
                    intent.putExtra("Users", result);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_dight,R.anim.out_to_left);
                }
                    break;

                default:
        }
    }
}
