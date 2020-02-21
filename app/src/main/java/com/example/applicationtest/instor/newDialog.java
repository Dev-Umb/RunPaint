package com.example.applicationtest.instor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationtest.R;

import java.util.ArrayList;
import java.util.List;

public class newDialog extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    private List<String> list;
    private String Data_Name,Table_name="name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_new_dialog);
        Intent intent = getIntent();
        String User = intent.getStringExtra("UserName");
        Data_Name = User + ".db";
        list = new ArrayList<>();
        ListView listView = findViewById(R.id.list_sign);
        adapter = new ArrayAdapter<>(this, R.layout.list_item2, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), list.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
