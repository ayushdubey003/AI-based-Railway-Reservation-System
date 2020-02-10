package com.sih2020.railwayreservationsystem.BroadcastRecievers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.sih2020.railwayreservationsystem.Activities.MainActivity;
import com.sih2020.railwayreservationsystem.Activities.ProfileAcitvity;
import com.sih2020.railwayreservationsystem.R;

public class TatkalReciever extends BroadcastReceiver {

    Bundle extras;
    String action;
    Vibrator vibrator;
    Notification notification;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving

        action=intent.getAction();
        vibrator=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);



        if(action.equals("wallet_notify"))
        {
            Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
            Log.i( "onReceive: ","Paise");

            _vibrate();
            createNotification("Please recharge your wallet",context,"Alert",1);
        }
        else if(action.equals("booking_start"))
        {
            _vibrate();
            createNotification("Get ready to enter captcha",context,"Booked",0);
        }





    }


    public void _vibrate()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));}
            else
                vibrator.vibrate(200);

    }


    public void createNotification(String message,Context context,String title,int mode)
    {



        int notifyID = 1;
        String CHANNEL_ID = "tatkal_channel_01";// The id of the channel.
        CharSequence name = "Tatkal";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mNotificationManager.createNotificationChannel(mChannel);
            if(mode==1) {
                Intent intent=new Intent(context,ProfileAcitvity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingintent=PendingIntent.getActivity(context,0,intent,0);

                notification = new Notification.Builder(context)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pendingintent)
                        .setChannelId(CHANNEL_ID)
                        .build();
            }
            else
            {
                notification = new Notification.Builder(context)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setChannelId(CHANNEL_ID)
                        .build();

            }
        }
         else {

             if(mode==1) {
                 Intent intent=new Intent(context,ProfileAcitvity.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                 PendingIntent pendingintent=PendingIntent.getActivity(context,0,intent,0);
                 notification = new Notification.Builder(context)
                         .setContentTitle(title)
                         .setContentText(message)
                         .setSmallIcon(R.mipmap.ic_launcher)
                         .setContentIntent(pendingintent)
                         .build();
             }
             else
             {
                 notification = new Notification.Builder(context)
                         .setContentTitle(title)
                         .setContentText(message)
                         .setSmallIcon(R.mipmap.ic_launcher)
                         .build();
             }
        }



        mNotificationManager.notify(notifyID , notification);
    }
}
