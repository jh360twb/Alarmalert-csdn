package com.example.dell.alarmalert_csdn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class CallAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context,AlarmAlert.class);
        Bundle bundle = new Bundle();
        //String content = intent.getStringExtra("content");
        //Log.e("content===sadsad",content);
        bundle.putString("STR_CALLER","");
        intent1.putExtras(bundle);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
