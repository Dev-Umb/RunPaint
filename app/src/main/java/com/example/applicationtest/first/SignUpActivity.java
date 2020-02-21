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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button;
    private EditText users,password,protect_Password,daAn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        button= findViewById(R.id.yes_signUp);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                button.setBackgroundColor(R.color.jiemian);

            }
        });
        users =findViewById(R.id.users);
        password = findViewById(R.id.password);
        protect_Password = findViewById(R.id.protect);
        daAn = findViewById(R.id.答案_1);
        button.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes_signUp:
                String inputText = users.getText().toString();
                String inputMiMa = password.getText().toString();
                String password_protect = protect_Password.getText().toString();
                String answer = daAn.getText().toString();
                //把获取的数据存储到数据库中
                if(inputText.length()>=4 && inputMiMa.length() >= 6 && password_protect.length()>0 ) {
                    Intent intent = new Intent(SignUpActivity.this, FirstActivity.class);

                    SharedPreferences sp = getSharedPreferences("info",MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sp.edit();
                    SharedPreferences sharedPreferences = getSharedPreferences(inputText,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    String result = sp.getString(inputText,null);
                    if(result!=null){
                        Toast.makeText(SignUpActivity.this, "该用户已经存在", Toast.LENGTH_SHORT).show();
                    }else if (inputText.length()<4)
                    {
                        Toast.makeText(SignUpActivity.this, "用户名需要大于四位", Toast.LENGTH_SHORT).show();
                    }else if (inputMiMa.length()<6){
                        Toast.makeText(SignUpActivity.this, "密码需要大于6位", Toast.LENGTH_SHORT).show();
                    } else if(password_protect.length()<=0) {
                        Toast.makeText(SignUpActivity.this, "密保问题不能为空", Toast.LENGTH_SHORT).show();
                    }else {
                        editor1.putString(inputText,inputText);
                        editor.putString("用户名",inputText);
                        editor.putString("密码",inputMiMa);
                        editor.putString("密保问题",password_protect);
                        editor.putString("答案",answer);
                        editor.commit();
                        editor1.commit();
                        Toast.makeText(SignUpActivity.this, "注册成功！欢迎您"+inputText, Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }
                }else {
                    Toast.makeText(SignUpActivity.this, "注册失败！请确认信息是否全部正确填写" , Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
