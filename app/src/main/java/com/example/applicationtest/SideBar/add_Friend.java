package com.example.applicationtest.SideBar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationtest.R;

public class add_Friend extends AppCompatActivity {
    private String UserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__friend);
        final EditText Name = findViewById(R.id.friend_name);
        final EditText number = findViewById(R.id.Phone_number);
        Button button = findViewById(R.id.save_friend);
        Intent intent1 = getIntent();
        UserName = intent1.getStringExtra("UserName");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name =  Name.getText().toString();
                String  Phone = number.getText().toString();
                if (name!=null && Phone!=null) {
                    boolean flag = true;
                    for (int i = 0; i < Phone.length(); i++) {
                        if (Phone.charAt(i) < '0' || Phone.charAt(i) > '9') {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        String str = name + '\n' + Phone;
                        Intent intent = new Intent(add_Friend.this,MyFriendList.class);
                        intent.putExtra("str",str);
                        intent.putExtra("UserName",UserName);
                        startActivity(intent);
                        overridePendingTransition(R.anim.in_from_dight,R.anim.out_to_left);
                        finish();
                    }else {
                        Toast.makeText(add_Friend.this, "电话号码格式有误！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(add_Friend.this, "请填写", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button close = findViewById(R.id.close_add);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(add_Friend.this,MyFriendList.class);
                intent.putExtra("UserName",UserName);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_dight,R.anim.out_to_left);
                finish();
            }
        });
    }
}
