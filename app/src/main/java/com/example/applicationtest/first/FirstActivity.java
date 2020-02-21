package com.example.applicationtest.first;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationtest.Bottom_tabActivity;
import com.example.applicationtest.QQLoginManager;
import com.example.applicationtest.R;
import com.example.applicationtest.instor.Dialog;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener, QQLoginManager.QQLoginListener {
    private SharedPreferences sharedPreferences,User;
    private ProgressBar progressBar;
    private EditText editText_zhangHao,editText_miMa;
    private ImageView tencent;
    private QQLoginManager qqLoginManager;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        qqLoginManager.onActivityResultData(requestCode,resultCode,data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        StatConfig.setDebugEnable(true);
        // 基础统计API
        StatService.registerActivityLifecycleCallbacks(this.getApplication());
        //mTencent = Tencent.createInstance("101851786",getApplicationContext());
        qqLoginManager = new QQLoginManager("101851786",this);
        setContentView(R.layout.first_layout);//加载布局
        progressBar = findViewById(R.id.Load);
        if(progressBar.getVisibility()==View.VISIBLE)
        {
            progressBar.setVisibility(View.INVISIBLE);
        }
        final Button signIn =findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                signIn.setBackgroundColor(R.color.jiemian);
            }
        });
        editText_zhangHao = findViewById(R.id.zhangHao);
        editText_miMa = findViewById(R.id.miMa);
        Buttons();
        signIn.setOnClickListener(this);

    }
    private void Buttons()
    {
        tencent = findViewById(R.id.tencent);
        tencent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qqLoginManager.launchQQLogin();
            }
        });
        Button SignUp = findViewById(R.id.signUp);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FirstActivity.this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_dight,R.anim.out_to_left);
            }
        });
        Button forget = findViewById(R.id.forget_miMa);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FirstActivity.this, Forget.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_dight,R.anim.out_to_left);

            }
        });
    }
    private void SignIn (String zhangHao,String miMa) {
        String userName;
        String Pasword;
        Intent intent;
        User = getSharedPreferences("info",MODE_PRIVATE);
        sharedPreferences= getSharedPreferences(zhangHao,MODE_PRIVATE);
        userName = User.getString(zhangHao, null);
        Pasword= sharedPreferences.getString("密码", null);

        if (userName == null) {
            Toast.makeText(FirstActivity.this, "该账号不存在！", Toast.LENGTH_SHORT).show();
        } else if (miMa.equals(Pasword)) {
            intent = new Intent();
            intent.setClass(FirstActivity.this, Bottom_tabActivity.class);
            SharedPreferences.Editor editor  = User.edit();
            editor.putString("当前登录账号",userName);
            intent.putExtra("UserName", zhangHao);
            intent.putExtra("QQflag",false);
            if (progressBar.getVisibility() == View.INVISIBLE) {
                progressBar.setVisibility(View.VISIBLE);
            }
            editor.commit();
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_dight,R.anim.out_to_left);
            finish();
        } else if (zhangHao.length() == 0) {
            Toast.makeText(FirstActivity.this, "请输入账号！", Toast.LENGTH_SHORT).show();
        } else if (miMa.length() == 0) {
            Toast.makeText(FirstActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent1 = new Intent(FirstActivity.this, Dialog.class);
            startActivity(intent1);
            //设置窗体为没有标题的模式

        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.signIn:
                String inputText = editText_zhangHao.getText().toString();
                String inputMiMa = editText_miMa.getText().toString();
                SignIn(inputText,inputMiMa);
                break;
                default:
        }
    }

    @Override
    public void onQQLoginSuccess(JSONObject jsonObject, QQLoginManager.UserAuthInfo authInfo) {
        String qqlogin = jsonObject.toString();
        try {
            Toast.makeText(FirstActivity.this,"正在登陆，请稍后",Toast.LENGTH_SHORT).show();
            if (progressBar.getVisibility() == View.INVISIBLE) {
                progressBar.setVisibility(View.VISIBLE);
            }
            String userName =jsonObject.getString("nickname");
            Intent intent = new Intent(FirstActivity.this,Bottom_tabActivity.class);
            String QQJSON = jsonObject.toString();
            intent.putExtra("QQJSON",QQJSON);
            intent.putExtra("UserName",userName);
            User = getSharedPreferences("info",MODE_PRIVATE);
            SharedPreferences.Editor editor  = User.edit();
            editor.putString("当前登录账号",userName);
            editor.commit();
            intent.putExtra("QQflag",true);
            startActivity(intent);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onQQLoginCancel() {

    }

    @Override
    public void onQQLoginError(UiError uiError) {

    }
}
