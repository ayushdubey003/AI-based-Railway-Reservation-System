package com.sih2020.railwayreservationsystem.Adapters;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sih2020.railwayreservationsystem.Models.Status;
import com.sih2020.railwayreservationsystem.Models.Train;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LiveStatusAdapter extends ArrayAdapter<Status> {
    private Context mContext;
    private List<Status> mList;

    public LiveStatusAdapter(@NonNull Context context, @NonNull List<Status> objects) {
        super(context, 0, objects);
        mContext = context;
        mList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.live_status_item, parent, false);
        TextView stationName = convertView.findViewById(R.id.station_tv);
        try {
            stationName.setText(mList.get(position).getStation());
            TextView arr = convertView.findViewById(R.id.arrival_tv);
            arr.setText(mList.get(position).getSchArr());
            TextView dept = convertView.findViewById(R.id.departure_tv);
            dept.setText(mList.get(position).getSchDept());
            arr = convertView.findViewById(R.id.arrival_tv_act);
            arr.setText(mList.get(position).getExpArr());
            dept = convertView.findViewById(R.id.departure_tv_exp);
            dept.setText(mList.get(position).getExpDept());
            arr = convertView.findViewById(R.id.arrival_delay_tv);
            arr.setText(mList.get(position).getArrDelay());
            dept = convertView.findViewById(R.id.departure_delay_tv);
            dept.setText(mList.get(position).getDelayDept());
            View line = convertView.findViewById(R.id.line);
            line.setBackground(GenerateBackground.generateBackground());
            ValueAnimator valueAnimator;
            try {
                valueAnimator = animateView(line, 700, 0, (int) AppConstants.convertDpToPixel(80, getContext()));
                valueAnimator.start();
            } catch (Exception ee) {
                Log.e("LiveStatusAdapter", ee.getLocalizedMessage());
            }
            if (position < mList.size())
                line.setVisibility(View.VISIBLE);
            if (position == mList.size() - 1)
                line.setVisibility(View.GONE);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return convertView;
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
