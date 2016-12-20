package com.arghami.exchangeratenotifier;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(52005);
        context.stopService(new Intent(context, ExchangeService.class));
        if (intent.getAction().equals("android.intent.action.STOP")) {
            System.exit(0);
        } else {
            context.startService(new Intent(context, ExchangeService.class));
        }
    }

}
