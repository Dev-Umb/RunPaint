package com.example.applicationtest.SideBar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationtest.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class InformationCard extends AppCompatActivity {
    private LinearLayout linearLayout;
    private ImageView imageView;
    private static final int OPEN_GALLERY = 7;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_card);
        imageView = findViewById(R.id.SIDE_头像);
        //改头像
        DisplayImageOptions op = new DisplayImageOptions.Builder().build();
        ImageLoaderConfiguration con = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(op).build();
        ImageLoader.getInstance().init(con);

        Intent intent = getIntent();
        linearLayout = findViewById(R.id.information_LinearLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView textView[] = new TextView[7];
        for (int i = 0; i < textView.length; i++) {
            textView[i] = new TextView(this);
        }
            String UserName = intent.getStringExtra("UserName");

            SharedPreferences sp = getSharedPreferences(UserName, MODE_PRIVATE);
            String information[] = new String[6];
            information[0] = sp.getString("性别", null);
            information[1] = sp.getString("生日", null);
            information[2] = sp.getString("职业", null);
            information[3] = sp.getString("运动", null);
            information[4] = sp.getString("兴趣", null);
            information[5] = sp.getString("所在地", null);
            String headUrl =sp.getString("头像",null);
            ImageLoader.getInstance().displayImage(headUrl,imageView);

            textView[0].setText("性别:" +"    " +information[0]);
            textView[1].setText("生日:" +"    "+ information[1]);
            textView[2].setText("职业:" +"    "+ information[2]);
            textView[3].setText("运动:" +"    "+ information[3]);
            textView[4].setText("兴趣:" +"    " +information[4]);
            textView[5].setText("所在地:" +"   "+ information[5]);
            textView[6].setText(UserName);
            {
                textView[6].setTextSize(30);
                textView[6].setTextColor(Color.BLACK);
                textView[6].setGravity(Gravity.CENTER);
                textView[6].setBackgroundColor(Color.WHITE);
                textView[6].setTop(20);
                textView[6].setTextColor(R.color.fuYuan);
            }
            linearLayout.addView(textView[6]);
            for (int i = 0; i < textView.length-1; i++) {
                textView[i].setTextSize(30);
                textView[i].setTextColor(Color.BLACK);
                textView[i].setGravity(Gravity.CENTER);
                textView[i].setBackgroundColor(Color.WHITE);
                textView[i].setTop(20);
                linearLayout.addView(textView[i]);
            }

        RadioButton back = findViewById(R.id.returned);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void openAlbum()
    {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,OPEN_GALLERY);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(this, "未授权读取相册", Toast.LENGTH_SHORT).show();
                }
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_OK)
        {
            handleIMageOnkitKat(data);
        }
    }
    //低版本api兼容
    @TargetApi(19)
    private void handleIMageOnkitKat(Intent data)
    {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri))
        {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority()))
            {
                String id = docId.split(":")[1];//解析id
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority()))
            {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme()))
        {
            imagePath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme()))
        {
            imagePath=uri.getPath();

        }
        displayImage(imagePath);
    }
    private void displayImage(String imagePath)
    {
        if (imagePath!=null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Toast.makeText(this,"成功",Toast.LENGTH_SHORT).show();
            imageView.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this,"失败",Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri uri,String selection)
    {
        String path=null;
                Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
                if (cursor!=null)
                {
                    if (cursor.moveToFirst())
                    {
                        path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

}
