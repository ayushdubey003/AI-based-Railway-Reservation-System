package com.sih2020.railwayreservationsystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sih2020.railwayreservationsystem.Models.Train;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;

import java.util.ArrayList;
import java.util.List;

public class RouteAdapter extends ArrayAdapter<Train> {

    private Context mContext;
    private List<Train> mList;
    private int mainPosition;

    public RouteAdapter(@NonNull Context context, @NonNull List<Train> objects, int mainPosition) {
        super(context, 0, objects);
        mContext = context;
        mList = objects;
        this.mainPosition = mainPosition;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.route_item, parent, false);
        TextView stationName = convertView.findViewById(R.id.station_tv);
        stationName.setText(mList.get(mainPosition).getmNamedRoutes().get(position));
        TextView arr = convertView.findViewById(R.id.arrival_tv);
        arr.setText(mList.get(mainPosition).getmArrivalTimes().get(position));
        TextView dept = convertView.findViewById(R.id.departure_tv);
        dept.setText(mList.get(mainPosition).getmDepartureTime().get(position));
        TextView correctTime = convertView.findViewById(R.id.correct_time_tv);
        TextView slightDelay = convertView.findViewById(R.id.slight_delay_tv);
        TextView signDelay = convertView.findViewById(R.id.significant_delay_tv);
        TextView cancelled = convertView.findViewById(R.id.cancelled_tv);
        View line = convertView.findViewById(R.id.line);
        line.setBackground(GenerateBackground.generateBackground());

        try {
            double d = Double.parseDouble(mList.get(mainPosition).getmRightTime().get(position).trim());
            d = d * 100;
            String s = "Correct Time: " + String.format(".2f", d) + "%";
            correctTime.setText(s);

            d = Double.parseDouble(mList.get(mainPosition).getmSlightDelay().get(position).trim());
            d = d * 100;
            s = "Slight Delay: " + String.format(".2f", d) + "%";
            slightDelay.setText(s);

            d = Double.parseDouble(mList.get(mainPosition).getmSignificantDelay().get(position).trim());
            d = d * 100;
            s = "Signinficant Delay: " + String.format(".2f", d) + "%";
            signDelay.setText(s);

            d = Double.parseDouble(mList.get(mainPosition).getmCancelled().get(position).trim());
            d = d * 100;
            s = "Cancelled: " + String.format(".2f", d) + "%";
            cancelled.setText(s);
        } catch (Exception e) {
            correctTime.setText("UNAVAILABLE");
        }
        return convertView;
    }
}
