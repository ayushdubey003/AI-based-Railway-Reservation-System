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
import androidx.recyclerview.widget.RecyclerView;

import com.sih2020.railwayreservationsystem.Models.Station;
import com.sih2020.railwayreservationsystem.R;

import java.util.List;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.MyViewHolder> {
    private Context mContext;
    private List<Station> mStationsList;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.station_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Station station = mStationsList.get(position);
        holder.stationName.setText(station.getmStationName().trim());
        holder.stationCode.setText(station.getmStationCode().trim());
        holder.trainImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_location_on_black_24dp));
    }

    @Override
    public int getItemCount() {
        return mStationsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView stationName, stationCode;
        public ImageView trainImg;

        public MyViewHolder(View view) {
            super(view);
            stationName = view.findViewById(R.id.station_name);
            stationCode = view.findViewById(R.id.station_code);
            trainImg = view.findViewById(R.id.iv_to_show);
        }
    }

    public StationAdapter(@NonNull Context context, @NonNull List<Station> objects) {
        mContext = context;
        mStationsList = objects;
    }
}
