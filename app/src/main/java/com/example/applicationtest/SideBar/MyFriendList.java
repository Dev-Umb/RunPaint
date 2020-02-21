package com.example.applicationtest.SideBar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.applicationtest.Bottom_tabActivity;
import com.example.applicationtest.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyFriendList extends AppCompatActivity {
    private List<String> contactList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private String str,UserName;
    private SharedPreferences sharedPreferences;
    private Map<String,?> keys;
    private Toolbar toolbar;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend_list);
        Intent intent = getIntent();
        str = intent.getStringExtra("str");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.rgb(Bottom_tabActivity.red_colors,Bottom_tabActivity.Green_colors,Bottom_tabActivity.blue_colors));
        UserName = intent.getStringExtra("UserName");
        if (UserName!=null) {
            sharedPreferences = getSharedPreferences(UserName + "list", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (str != null) {
                editor.putString(str, str);
                editor.commit();
            }
            keys = sharedPreferences.getAll();
            if (keys != null) {
                for (Map.Entry<String, ?> key : keys.entrySet()) {
                    if (key != null)
                        contactList.add(key.getValue().toString());
                }
            }
        }
        ImageView imageView = findViewById(R.id.addFriends);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MyFriendList.this, v);
                getMenuInflater().inflate(R.menu.add_friends, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add_friends:
                                Intent intent = new Intent();
                                intent.setClass(MyFriendList.this, add_Friend.class);
                                intent.putExtra("UserName",UserName);
                                startActivity(intent);
                                overridePendingTransition(R.anim.in_from_dight,R.anim.out_to_left);
                                finish();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("好友列表");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        final ListView content_View = findViewById(R.id.FriendlistView);
        adapter=new ArrayAdapter<String>(this,R.layout.list_item2,contactList);
        content_View.setAdapter(adapter);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);
        }else {
            readContacts();
        }
        content_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phone = contactList.get(position);
                boolean flag=true;
                String number =null;
                StringBuffer stringBuffer = null;
                for (int i = 0; i < phone.length(); i++) {

                 if (!flag) {
                     if (number == null) {
                         number = String.valueOf(phone.charAt(i));
                         stringBuffer = new StringBuffer(number);
                     } else {
                         stringBuffer.append(phone.charAt(i));
                     }
                 }
                 if (phone.charAt(i)=='\n')
                 {
                     flag=false;
                 }
                }
                if (!TextUtils.isEmpty(number)) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + stringBuffer));
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_dight,R.anim.out_to_left);
                }
                }
        });
    }
    private void readContacts()
    {
        Cursor cursor =null;
        try {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
            if (cursor!=null)
            {
                while(cursor.moveToNext())
                {
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String displayNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactList.add(displayName+'\n'+displayNumber);
                }
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null)
            {
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    readContacts();
                }else {
                    Toast.makeText(this, "未授权读取联系人", Toast.LENGTH_SHORT).show();
                }
                default:
        }
    }
}
