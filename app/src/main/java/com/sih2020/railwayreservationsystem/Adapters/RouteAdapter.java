package com.sih2020.railwayreservationsystem.Adapters;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.sih2020.railwayreservationsystem.Models.Train;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RouteAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<Train> mList;
    private List<String> mRoutes;
    private int mainPosition;

    public RouteAdapter(@NonNull Context context, @NonNull List<String> objects, List<Train> trains, int mainPosition) {
        super(context, 0, objects);
        mContext = context;
        mRoutes = objects;
        mList = trains;
        this.mainPosition = mainPosition;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.route_item, parent, false);
        TextView stationName = convertView.findViewById(R.id.station_tv);
        try {
            stationName.setText(mList.get(mainPosition).getmNamedRoutes().get(position));
            TextView arr = convertView.findViewById(R.id.arrival_tv);
            if (mList.get(mainPosition).getmArrivalTimes().get(position).equalsIgnoreCase("source"))
                arr.setText("Source");
            else {
                try {
                    arr.setText(convertTime(mList.get(mainPosition).getmArrivalTimes().get(position)));
                } catch (Exception e) {
                    arr.setText("N/A");
                }
            }
            TextView dept = convertView.findViewById(R.id.departure_tv);
            if (mList.get(mainPosition).getmDepartureTime().get(position).equalsIgnoreCase("destination"))
                dept.setText("Destination");
            else {
                try {
                    dept.setText(convertTime(mList.get(mainPosition).getmDepartureTime().get(position)));
                } catch (Exception e) {
                    dept.setText("N/A");
                }
            }
            View line = convertView.findViewById(R.id.line);
            line.setBackground(GenerateBackground.generateBackground());
            ValueAnimator valueAnimator;
            try {
                valueAnimator = animateView(line, 700, 0, (int) AppConstants.convertDpToPixel(75, getContext()));
                valueAnimator.start();
            } catch (Exception ee) {
                Log.e("RouteAdapter", ee.getLocalizedMessage());
            }
            if (position == mRoutes.size() - 2)
                line.setVisibility(View.GONE);
            if (position < mRoutes.size())
                line.setVisibility(View.VISIBLE);
            if (position == mRoutes.size() - 1)
                line.setVisibility(View.GONE);
        } catch (Exception ee) {
            ee.printStackTrace();
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

    private ValueAnimator animateView(final View view, long duration, int initialHeight, int finalHeight) {
        RelativeLayout.LayoutParams lP = (RelativeLayout.LayoutParams) view.getLayoutParams();
        ValueAnimator anim = ValueAnimator.ofInt(initialHeight, finalHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                layoutParams.height = val;
                view.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(duration);
        anim.setInterpolator(new LinearInterpolator());
        return anim;
    }
}
