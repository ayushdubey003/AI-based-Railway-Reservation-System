package com.sih2020.railwayreservationsystem.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sih2020.railwayreservationsystem.Models.Train;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PassingByTrainAdapter extends ArrayAdapter<Train> {
    private Context mContext;
    public ArrayList<Train> mTrains;
    private View view;
    private String mstation;

    public PassingByTrainAdapter(@NonNull Context context, @NonNull ArrayList<Train> objects, String station) {
        super(context, 0, objects);
        mContext = context;
        mTrains = objects;
        mstation = station;
        Log.e("PassingByTrainAdapter: ", "" + mTrains.size());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.passing_by_train_item, parent, false);

        view = convertView;
        int arrIndex = 0;
        for (int i = 0; i < mTrains.get(position).getmCodedRoutes().size(); i++) {
            if (mTrains.get(position).getmCodedRoutes().get(i).trim().equalsIgnoreCase(mstation)) {
                arrIndex = i;
                break;
            }
        }

        TextView trainNo = convertView.findViewById(R.id.train_no_pb);
        trainNo.setText(mTrains.get(position).getmTrainNo());
        TextView trainName = convertView.findViewById(R.id.train_name_pb);
        trainName.setText(mTrains.get(position).getmTrainName());


        String arrt = mTrains.get(position).getmDepartureTime().get(arrIndex);
        TextView startTime = convertView.findViewById(R.id.start_time_pb);
        startTime.setText(arrt);

        String dtime = mTrains.get(position).getmArrivalTimes().get(arrIndex);
        TextView endTime = convertView.findViewById(R.id.end_time_pb);
        endTime.setText(dtime);


        TextView sun = convertView.findViewById(R.id.sun);
        TextView mon = convertView.findViewById(R.id.mon);
        TextView tue = convertView.findViewById(R.id.tue);
        TextView wed = convertView.findViewById(R.id.wed);
        TextView thurs = convertView.findViewById(R.id.thurs);
        TextView fri = convertView.findViewById(R.id.fri);
        TextView sat = convertView.findViewById(R.id.sat);

        sun.setTextColor(Color.parseColor("#FF0000"));
        mon.setTextColor(Color.parseColor("#FF0000"));
        tue.setTextColor(Color.parseColor("#FF0000"));
        wed.setTextColor(Color.parseColor("#FF0000"));
        thurs.setTextColor(Color.parseColor("#FF0000"));
        fri.setTextColor(Color.parseColor("#FF0000"));
        sat.setTextColor(Color.parseColor("#FF0000"));

        ArrayList<String> runningDays = mTrains.get(position).getmRunningDays();

        if (runningDays.size() == 1) {
            if (runningDays.get(0).equalsIgnoreCase("daily")) {
                sun.setTextColor(Color.parseColor("#00FF00"));
                mon.setTextColor(Color.parseColor("#00FF00"));
                tue.setTextColor(Color.parseColor("#00FF00"));
                wed.setTextColor(Color.parseColor("#00FF00"));
                thurs.setTextColor(Color.parseColor("#00FF00"));
                fri.setTextColor(Color.parseColor("#00FF00"));
                sat.setTextColor(Color.parseColor("#00FF00"));
            }
        }
        for (int i = 0; i < runningDays.size(); i++) {
            if (runningDays.get(i).equalsIgnoreCase("sun"))
                sun.setTextColor(Color.parseColor("#00FF00"));
            if (runningDays.get(i).equalsIgnoreCase("mon"))
                mon.setTextColor(Color.parseColor("#00FF00"));
            if (runningDays.get(i).equalsIgnoreCase("tue"))
                tue.setTextColor(Color.parseColor("#00FF00"));
            if (runningDays.get(i).equalsIgnoreCase("wed"))
                wed.setTextColor(Color.parseColor("#00FF00"));
            if (runningDays.get(i).equalsIgnoreCase("thu"))
                thurs.setTextColor(Color.parseColor("#00FF00"));
            if (runningDays.get(i).equalsIgnoreCase("fri"))
                fri.setTextColor(Color.parseColor("#00FF00"));
            if (runningDays.get(i).equalsIgnoreCase("sat"))
                sat.setTextColor(Color.parseColor("#00FF00"));
        }
        return convertView;
    }

    private static String convertTime(String time) {
        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
        Date _24HourDt = null;
        try {
            _24HourDt = _24HourSDF.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return _12HourSDF.format(_24HourDt);
    }
}
