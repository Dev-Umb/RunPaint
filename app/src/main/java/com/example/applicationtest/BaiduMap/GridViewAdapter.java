package com.example.applicationtest.BaiduMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.applicationtest.R;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    Context contextq;
    List<String> DIY_list;
    public GridViewAdapter(Context context,List<String> list)
    {
        this.contextq=context;
        this.DIY_list=list;
    }
    @Override
    public int getCount() {
        return DIY_list.size();
    }

    @Override
    public Object getItem(int position) {
        return DIY_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(contextq);
        convertView = layoutInflater.inflate(R.layout.grid_item, null);
        TextView tv = (TextView) convertView.findViewById(R.id.grid_text);
        String str=DIY_list.get(position);
        tv.setText(str);
        return convertView;
    }
}
