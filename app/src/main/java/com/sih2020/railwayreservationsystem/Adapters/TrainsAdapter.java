package com.sih2020.railwayreservationsystem.Adapters;

import android.content.Context;
import android.util.Pair;
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

public class TrainsAdapter extends RecyclerView.Adapter<TrainsAdapter.MyViewHolder> {
    private Context mContext;
    private List<Pair<String, String>> mTrainsList;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.station_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Pair<String, String> pair = mTrainsList.get(position);
        holder.trainName.setText(pair.second.trim());
        holder.trainCode.setText(pair.first.trim());
        holder.trainImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_train_black_24dp));
    }

    @Override
    public int getItemCount() {
        return mTrainsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView trainName, trainCode;
        public ImageView trainImg;

        public MyViewHolder(View view) {
            super(view);
            trainName = view.findViewById(R.id.station_name);
            trainCode = view.findViewById(R.id.station_code);
            trainImg = view.findViewById(R.id.iv_to_show);
        }
    }

    public TrainsAdapter(@NonNull Context context, @NonNull List<Pair<String, String>> objects) {
        mContext = context;
        mTrainsList = objects;
    }
}
