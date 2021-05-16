package com.example.duan1_nhom2.BroadcastReceiverClass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.duan1_nhom2.FragmentClass.NhacDangNgheFragment;

public class Controller extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent("PerformAction").putExtra("ActionName", intent.getAction()));
    }

}
