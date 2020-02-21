package com.example.applicationtest.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationtest.Bottom_tabActivity;
import com.example.applicationtest.R;

import java.util.List;

public class NomalAdapter extends RecyclerView.Adapter<NomalAdapter.VH> {
    private List<String>mDatas;
    private List<Integer>red;
    private List<Integer>Blue;
    private List<Integer>green;
    private SharedPreferences sharedPreferences;


    NomalAdapter(List<String> mDatas,List<Integer> red,List<Integer> blue,List<Integer> green)
    {
        this.mDatas=mDatas;
        this.red=red;
        this.Blue=blue;
        this.green=green;
    }
    public class VH extends RecyclerView.ViewHolder {
       public final TextView title;
       private final Button button;
        public VH(@NonNull View itemView) {
           super(itemView);
            title=itemView.findViewById(R.id.title_color);
            button=itemView.findViewById(R.id.color_set);

        }
   }

    @NonNull
    @Override
    public NomalAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_1,parent,false);
        sharedPreferences = parent.getContext().getSharedPreferences(Bottom_tabActivity.Information, Context.MODE_PRIVATE);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final NomalAdapter.VH holder, final int position) {
        holder.title.setText(mDatas.get(position));
        holder.title.setTextColor(Color.rgb(red.get(position),green.get(position),Blue.get(position)));
        holder.button.setBackgroundColor(Color.rgb(red.get(position),green.get(position),Blue.get(position)));
        if (red.get(position)==255&&green.get(position)==255&&Blue.get(position)==255)
        {
            holder.title.setTextColor(Color.rgb(0,0,0));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("red", red.get(position));
                editor.putInt("green", green.get(position));
                editor.putInt("blue",Blue.get(position));
                editor.putBoolean("can_color",true);
                Toast.makeText(holder.itemView.getContext(),"主题已设置为"+mDatas.get(position)+"若主界面无反应请尝试重启app",Toast.LENGTH_SHORT).show();
                Bottom_tabActivity.isChange=true;
                editor.commit();

            }
        });

    }
    @Override
    public int getItemCount() {

        return mDatas.size();
    }
}
