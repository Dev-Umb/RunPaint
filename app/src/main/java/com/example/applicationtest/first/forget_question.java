package com.example.applicationtest.first;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationtest.R;

public class forget_question extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private Intent intent;
    private LinearLayout linearLayout;
    private TextView tv;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_question);

        linearLayout  =findViewById(R.id.password_question);
        tv = new TextView(this);
        intent = getIntent();
        sharedPreferences = getSharedPreferences(intent.getStringExtra("Users"),MODE_PRIVATE);
        String textQuestion = sharedPreferences.getString("密保问题",null);
        msetText(textQuestion);

        final String answer1 = sharedPreferences.getString("答案",null);
        final EditText editText = findViewById(R.id.answer);
        Button button = findViewById(R.id.find_yes);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer1.equals(editText.getText().toString()))
                {
                    Intent intent1 = new Intent(forget_question.this,ChangePassWord.class);
                    intent1.putExtra("Users",intent.getStringExtra("Users"));
                    startActivity(intent1);
                }else {
                    Toast.makeText(forget_question.this, "错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void msetText(String textQuestion)
    {
        tv.setText(textQuestion);
        tv.setTextSize(30);
        tv.setTextColor(Color.BLACK);
        linearLayout.addView(tv);
    }
}
