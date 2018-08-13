package com.jp.projetoanimes.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jp.projetoanimes.service.NotifyService;
import com.jp.projetoanimes.types.FirebaseManager;

public class StartUpBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            if (FirebaseManager.getAuth().getUid() != null){
                context.startService(new Intent(context, NotifyService.class));
            }
        }
    }
}