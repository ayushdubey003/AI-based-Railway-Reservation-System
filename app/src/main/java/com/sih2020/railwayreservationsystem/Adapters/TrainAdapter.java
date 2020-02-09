package com.sih2020.railwayreservationsystem.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sih2020.railwayreservationsystem.Activities.RouteActivity;
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

    private static String LOG_TAG = "TrainAdapter";

    public TrainAdapter(@NonNull Context context, @NonNull List<Train> objects) {
        super(context, 0, objects);
        mContext = context;
        mTrains = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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

        duration.setText("06hr");

//        try {
//            duration.setText("" + findTrainArrivalDepartureTimeDifference(mTrains.get(position), arrIndex, deptIndex) + "hr");
//        }
//        catch (Exception e){
//            Log.d(LOG_TAG, e.getLocalizedMessage());
//        }

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

        TextView travelClass = convertView.findViewById(R.id.travel_class_tv);
        travelClass.setText(AppConstants.mClass.getmAbbreviation());

        TextView seatAvltv = convertView.findViewById(R.id.availability_tv);
        final ProgressBar avlProgress = convertView.findViewById(R.id.availability_progress);

        ImageView routeIv = convertView.findViewById(R.id.route_iv);
        routeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RouteActivity.class);
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });

        try {
            seatAvltv.setText(mTrains.get(position).getmSeats().get(0));
            seatAvltv.setVisibility(View.VISIBLE);
            avlProgress.setVisibility(View.GONE);
        } catch (Exception e) {
        }

        final ProgressBar fareProgress = convertView.findViewById(R.id.fare_progress);
        TextView fareTv = convertView.findViewById(R.id.fare_tv);
        try {
            int x;
            if (mTrains.get(position).getmFare() == null)
                x = 1 / 0;
            fareTv.setText(mTrains.get(position).getmFare());
            fareTv.setVisibility(View.VISIBLE);
            fareProgress.setVisibility(View.GONE);
        } catch (Exception e) {
        }

        final ProgressBar confirmationProgress = convertView.findViewById(R.id.confirmation_progress);
        TextView confirmationTv = convertView.findViewById(R.id.confirmation_tv);
        RelativeLayout confirmationRl = convertView.findViewById(R.id.confirmation_rl);
        try {
            int x;
            if (mTrains.get(position).getmConfirmationProbability() == null)
                x = 1 / 0;
            if (mTrains.get(position).getmConfirmationProbability().equalsIgnoreCase("unavailable")) {
                confirmationProgress.setVisibility(View.GONE);
                String s = mTrains.get(position).getmConfirmationProbability().substring(0, 5) + "%";
                confirmationTv.setText("UNAVAILABLE");
                confirmationTv.setVisibility(View.VISIBLE);
            } else {
                confirmationProgress.setVisibility(View.GONE);
                String s = mTrains.get(position).getmConfirmationProbability().substring(0, 5) + "%";
                confirmationTv.setText(s);
                confirmationTv.setVisibility(View.VISIBLE);
            }
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

    private static int findTrainArrivalDepartureTimeDifference(Train t, int arrIndex, int depIndex){
        ArrayList<String> times = t.getmDepartureTime();
        Log.d(LOG_TAG, times.get(arrIndex).substring(0, 5));
        int startTime = getMinutes(times.get(arrIndex).substring(0, 5));
        int startDay = times.get(arrIndex).charAt(times.get(arrIndex).length() - 2) - '0';
        int endTime = getMinutes(times.get(depIndex).substring(0, 5));
        int endDay = times.get(depIndex).charAt(times.get(depIndex).length() - 2) - '0';

        return ((startTime - endTime) + (startDay - endDay) * 24 * 60) / 60;
    }

    private static int getMinutes(String time){
        time = time.trim();
        int hour = 10 * (time.charAt(0) - '0') + (time.charAt(1) - '0');
        int min = 10 * (time.charAt(3) - '0') + (time.charAt(4) - '0');
        return hour * 60 + min;
    }

}
