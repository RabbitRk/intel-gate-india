package com.example.pavinaveen.slidingtab;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class ConnectionReceiver extends BroadcastReceiver {
    //Declaration goes here

    public static final String LOG_TAG = "com.slidingtab.conrec";

    @Override
    public void onReceive(Context context, Intent intent) {

//        String ssid = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        String ssid = null;

       ssid = wifiInfo.getSSID();


        if (((null != wifi)&&(wifi.isConnected()))) {
            Intent uplIntent = new Intent(context, toll_registration.class);
            uplIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.i(LOG_TAG,ssid);
//            if (ssid.equals("Rk.D.Rabbitt")){
                Toast.makeText(context, "Wifi connected..."+ssid, Toast.LENGTH_SHORT).show();
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_announcement_black_24dp)
                            .setContentTitle("IntelGate")
                            .setContentText("Your are nearby a toll gate");

            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);
            // Add as notification
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());

            //Start the activity
            context.startActivity(uplIntent);
//        }


        }
    }
}


