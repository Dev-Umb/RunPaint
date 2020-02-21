package com.example.applicationtest.Frament;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

public class BroadCastManager {
    private static BroadCastManager broadCastManager= new BroadCastManager();
    private BroadcastReceiver receiver;
    public static BroadCastManager getInstance()
    {
        return broadCastManager;}
    //注册接收者
    public void registerRecive(Activity activity, BroadcastReceiver receiver, IntentFilter intentFilter)
    {
        this.receiver=receiver;
        activity.registerReceiver(receiver,intentFilter);

    }
    public void sendBroadCast(Activity activity, Intent intent)
    {
        activity.sendBroadcast(intent);
    }
    public BroadcastReceiver getReceiver()
    {
        return receiver;
    }

    public void unregisterRecive(Activity activity,BroadcastReceiver receiver)

    {
        activity.unregisterReceiver(receiver);
    }

}
