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

public class TrainAdapter extends ArrayAdapter<Train> {
    private Context mContext;
    public List<Train> mTrains;
    private View view;

    public TrainAdapter(@NonNull Context context, @NonNull List<Train> objects) {
        super(context, 0, objects);
        mContext = context;
        mTrains = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.train_list_item, parent, false);

        view = convertView;

        int deptIndex = 0;
        for (int i = 0; i < mTrains.get(position).getmCodedRoutes().size(); i++) {
            if (mTrains.get(position).getmCodedRoutes().get(i).trim().equalsIgnoreCase(AppConstants.mSourceStation.getmStationCode())) {
                deptIndex = i;
                break;
            }
        }

        int arrIndex = 0;
        for (int i = 0; i < mTrains.get(position).getmCodedRoutes().size(); i++) {
            if (mTrains.get(position).getmCodedRoutes().get(i).trim().equalsIgnoreCase(AppConstants.mDestinationStation.getmStationCode())) {
                arrIndex = i;
                break;
            }
        }

        TextView trainNo = convertView.findViewById(R.id.train_no_tv);
        trainNo.setText(mTrains.get(position).getmTrainNo());
        TextView trainName = convertView.findViewById(R.id.train_name_tv);
        trainName.setText(mTrains.get(position).getmTrainName());
        TextView startTime = convertView.findViewById(R.id.start_time_tv);
        startTime.setText(convertTime(mTrains.get(position).getmDepartureTime().get(deptIndex)));
        TextView endTime = convertView.findViewById(R.id.end_time_tv);
        endTime.setText(convertTime(mTrains.get(position).getmArrivalTimes().get(arrIndex)));
        TextView duration = convertView.findViewById(R.id.duration_tv);
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

        duration.setText("06hr");
        TextView travelClass = convertView.findViewById(R.id.travel_class_tv);
        travelClass.setText(AppConstants.mClass.getmAbbreviation());

        TextView seatAvltv = convertView.findViewById(R.id.availability_tv);
        final ProgressBar avlProgress = convertView.findViewById(R.id.availability_progress);
        final ProgressBar fareProgress = convertView.findViewById(R.id.fare_progress);
        try {
            seatAvltv.setText(mTrains.get(position).getmSeats().get(0));
            seatAvltv.setVisibility(View.VISIBLE);
            avlProgress.setVisibility(View.GONE);
        } catch (Exception e) {
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
