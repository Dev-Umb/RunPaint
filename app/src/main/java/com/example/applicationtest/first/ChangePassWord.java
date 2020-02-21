package com.example.applicationtest.first;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationtest.R;

public class ChangePassWord extends AppCompatActivity {
   private SharedPreferences sharedPreferences;
   private Intent intent;
   private EditText editText;
   private SharedPreferences.Editor editor;
   private String newPassWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_word);
        intent = getIntent();
        String result = intent.getStringExtra("Users");
        sharedPreferences = getSharedPreferences(result,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editText = findViewById(R.id.newPassWord);

        Button button = findViewById(R.id.ChangeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPassWord = editText.getText().toString();
                editor.putString("密码",newPassWord);
                editor.commit();
                String test = sharedPreferences.getString("密码",null);
                if (test.equals(newPassWord)) {
                    Toast.makeText(ChangePassWord.this, "密码修改成功！", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ChangePassWord.this, FirstActivity.class);
                    startActivity(i);
                }else {
                    Toast.makeText(ChangePassWord.this, "密码修改失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
