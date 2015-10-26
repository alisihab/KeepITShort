package com.example.bahadir.myapplicationn;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class PushReceiver extends BroadcastReceiver
{
    boolean notificationver = true;
    private static final int uniqueID = 31331;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        notificationSharedAl(context);
        Log.i("tago", "notification oluşturuldu ");
        //Toast.makeText(context, "Action: " + intent.getAction(), Toast.LENGTH_SHORT).show();

        String notificationTitle = "Shappy";
        String notificationDesc = "Test notification";


        if ( intent.getStringExtra("message") != null )
        {
            notificationDesc = intent.getStringExtra("message");
        }

        Bundle bundle = intent.getExtras();
        Intent inte = new Intent("broadCastName");
        inte.putExtra("mesaj" , bundle.getString("message"));
        context.sendBroadcast(inte);
        Log.i("tago" , "broadcast gonderildi");

        //-----------------------------
        // Create a test notification
        //-----------------------------
        if(notificationver) {
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
            notification.setContentText(notificationDesc);
            notification.setContentTitle(notificationTitle);
            notification.setSmallIcon(R.mipmap.ozerprof);
            notification.setWhen(System.currentTimeMillis());
            Intent i = new Intent(context, PushReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pendingIntent);

            NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            nm.notify(uniqueID, notification.build());
            Log.i("tago", "notification oluşturuldu " + notificationDesc + " " + notificationTitle);
        }
        //-----------------------------
        // Sound + vibrate + light
        //-----------------------------

    }

    private void notificationSharedAl(Context context) {
        SharedPreferences sP = context.getSharedPreferences("notification",Context.MODE_PRIVATE);
        notificationver = sP.getBoolean("notificationver" , true);
    }
}