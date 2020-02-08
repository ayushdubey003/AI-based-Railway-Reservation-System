package com.sih2020.railwayreservationsystem.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.sih2020.railwayreservationsystem.BroadcastRecievers.TatkalReciever;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TatkalService extends Service {

    Handler mHandler;
    SimpleDateFormat mDateFormat;
    Date cur_time,ttk_time,wallet_time;
    boolean wallet=false;
    TatkalReciever reciever;
    String cur,ttk,wlt;

    public TatkalService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler=new Handler();
        mDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm");
        run_method();
        return super.onStartCommand(intent, flags, startId);


    }

    public void run_method()
    {
        cur_time=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 1, 8, 12, 47);
        wallet_time = calendar.getTime();
        calendar.set(2020,1,8,13,20);
        ttk_time=calendar.getTime();
        cur=mDateFormat.format(cur_time);
        wlt=mDateFormat.format(wallet_time);
        ttk=mDateFormat.format(ttk_time);
        Log.i("cur_time ",cur);
        Log.i("ttk_time",ttk);
        if(cur.equals(wlt))
        {
            Log.i( "run: ","wallet");

            if(!wallet)
            {
                sendBroadcast(new Intent(getApplicationContext(), TatkalReciever.class).setAction("wallet_notify"));
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        run_method();
                    }
                },60000);

            }
        }
        else if(ttk.equals(cur))
        {
            //Booking Starts
            Intent intent=new Intent(getApplicationContext(),TatkalReciever.class);
            intent.setAction("booking_start");
            sendBroadcast(intent);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    run_method();
                }
            },60000);
        }
        else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    run_method();
                }
            }, 1000);
        }
    }
}
