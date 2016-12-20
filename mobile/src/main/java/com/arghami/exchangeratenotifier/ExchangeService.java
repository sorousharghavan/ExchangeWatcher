package com.arghami.exchangeratenotifier;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ExchangeService extends Service {
    int isEquity;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            final Handler handler = new Handler();
            final SQLManager sqlManager = new SQLManager(getApplicationContext());
            String symbl="USD";
            try{symbl = sqlManager.readSym().toUpperCase();}catch (Exception e){e.printStackTrace();}
            final String smbbl = symbl;
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    try {
                        rateChecker rt = new rateChecker();

                        NotificationManager notificationManager = (NotificationManager)
                                getSystemService(NOTIFICATION_SERVICE);
                        Intent yesReceive = new Intent();
                        yesReceive.setAction("android.intent.action.STOP");
                        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(getApplicationContext(), 0, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);

                        Intent update = new Intent();
                        update.setAction("android.intent.action.UPDATE");
                        PendingIntent pupdate = PendingIntent.getBroadcast(getApplicationContext(), 0, update, PendingIntent.FLAG_UPDATE_CURRENT);
                        String reply = rt.execute(smbbl).get();
                        if (reply.equals("Invalid Symbol")){
                            Notification n = new Notification.Builder(ExchangeService.this)
                                    .setContentTitle("Current Exchange Rate")
                                    .setContentText("Could not find "+smbbl)
                                    .setContentIntent(pupdate)
                                    .setAutoCancel(true)
                                    .addAction(R.drawable.ic_update_black_18dp, "Update", pupdate)
                                    .addAction(R.drawable.ic_clear_black_18dp, "Exit", pendingIntentYes)
                                    .setSmallIcon(R.drawable.dsign)
                                    .build();
                            n.flags = Notification.FLAG_AUTO_CANCEL;
                            notificationManager.notify(52005, n);
                        }else {
                            if (isEquity==0) {
                                Notification n = new Notification.Builder(ExchangeService.this)
                                        .setContentTitle("Current Exchange Rate")
                                        .setContentText("1 USD = " + reply + " " + smbbl)
                                        .setContentIntent(pupdate)
                                        .setAutoCancel(true)
                                        .addAction(R.drawable.ic_update_black_18dp, "Update", pupdate)
                                        .addAction(R.drawable.ic_clear_black_18dp, "Exit", pendingIntentYes)
                                        .setSmallIcon(R.drawable.dsign)
                                        .build();
                                n.flags = Notification.FLAG_AUTO_CANCEL;
                                notificationManager.notify(52005, n);
                            } else {
                                Notification n = new Notification.Builder(ExchangeService.this)
                                        .setContentTitle("Current Exchange Rate")
                                        .setContentText("1 " + smbbl + " = " + reply + " USD")
                                        .setContentIntent(pupdate)
                                        .setAutoCancel(true)
                                        .addAction(R.drawable.ic_update_black_18dp, "Update", pupdate)
                                        .addAction(R.drawable.ic_clear_black_18dp, "Exit", pendingIntentYes)
                                        .setSmallIcon(R.drawable.dsign)
                                        .build();
                                n.flags = Notification.FLAG_AUTO_CANCEL;
                                notificationManager.notify(52005, n);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            };
            handler.postDelayed(runnable, 1000);
        }
        return START_STICKY;
    }

    public class rateChecker extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            JSONObject data;
            String rate = "Invalid Symbol";
            try {
                URL url;
                if (params[0].length()==3){
                    url = new URL("http://finance.yahoo.com/webservice/v1/symbols/"+params[0]+"=X/quote?format=json");
                    HttpURLConnection connection =
                            (HttpURLConnection)url.openConnection();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    StringBuffer json = new StringBuffer(1024);
                    String tmp;
                    while((tmp=reader.readLine())!=null)
                        json.append(tmp).append("\n");
                    reader.close();
                    data = new JSONObject(json.toString());
                    JSONObject trate = (JSONObject)data.getJSONObject("list").getJSONArray("resources").get(0);
                    JSONObject jtmp = (JSONObject)trate.get("resource");
                    jtmp= (JSONObject)jtmp.get("fields");
                    rate = jtmp.getString("price");
                }else{
                    url = new URL("http://finance.yahoo.com/webservice/v1/symbols/"+params[0]+"/quote?format=json");
                    HttpURLConnection connection =
                            (HttpURLConnection)url.openConnection();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    StringBuffer json = new StringBuffer(1024);
                    String tmp;
                    while((tmp=reader.readLine())!=null)
                        json.append(tmp).append("\n");
                    reader.close();
                    data = new JSONObject(json.toString());
                    JSONObject trate = (JSONObject)data.getJSONObject("list").getJSONArray("resources").get(0);
                    JSONObject jtmp = (JSONObject)trate.get("resource");
                    jtmp= (JSONObject)jtmp.get("fields");
                    rate = jtmp.getString("price");
                    isEquity=1;
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                if (params[0].length()==3){
                try{
                    URL url;
                    url = new URL("http://finance.yahoo.com/webservice/v1/symbols/"+params[0]+"/quote?format=json");
                    HttpURLConnection connection =
                            (HttpURLConnection)url.openConnection();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    StringBuffer json = new StringBuffer(1024);
                    String tmp;
                    while((tmp=reader.readLine())!=null)
                        json.append(tmp).append("\n");
                    reader.close();
                    data = new JSONObject(json.toString());
                    JSONObject trate = (JSONObject)data.getJSONObject("list").getJSONArray("resources").get(0);
                    JSONObject jtmp = (JSONObject)trate.get("resource");
                    jtmp= (JSONObject)jtmp.get("fields");
                    rate = jtmp.getString("price");
                    isEquity=1;
                }catch (JSONException | IOException f){
                    e.printStackTrace();
                }}
            }

            return rate;
        }
    }

}
