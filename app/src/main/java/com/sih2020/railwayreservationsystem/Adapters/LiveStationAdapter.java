package com.sih2020.railwayreservationsystem.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sih2020.railwayreservationsystem.Models.LiveStationModel;
import com.sih2020.railwayreservationsystem.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class LiveStationAdapter extends RecyclerView.Adapter<LiveStationAdapter.ViewHolder> {

    ArrayList<LiveStationModel> mlist;
    Context context;

    public LiveStationAdapter(Context context, ArrayList<LiveStationModel> mlist) {
        this.mlist = mlist;
        this.context = context;

        Log.e("LiveStationAdapter: ", "" + mlist.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.live_station_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.trainNo.setText(mlist.get(position).getTrainNo());
        holder.trainName.setText(mlist.get(position).getTrainName());

        if (mlist.get(position).getSchArrival().equalsIgnoreCase("src")){
            holder.schArr.setText(mlist.get(position).getDdept());
            holder.expArr.setText(mlist.get(position).getDdept());
        }
        else {
            holder.schArr.setText(mlist.get(position).getSchArrival());
            holder.expArr.setText(mlist.get(position).getExpArrival());
        }


        if (mlist.get(position).getPf().equals(null)) {
            holder.pf.setText("--");
        }
        holder.pf.setText("Platform " + mlist.get(position).getPf());
        holder.source.setText(mlist.get(position).getSource());
        holder.destination.setText(mlist.get(position).getDestination());
        holder.delay.setText(mlist.get(position).getDelay());

        String ldelay = mlist.get(position).getDelay();
        if (Integer.parseInt(ldelay.substring(0, 1)) == 0 && Integer.parseInt(ldelay.substring(3, 4)) == 0) {
            holder.expArr.setTextColor(Color.parseColor("#43A047"));
            holder.delay.setTextColor(Color.parseColor("#43A047"));
        }

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView trainNo, trainName, schArr, expArr, pf, source, destination, delay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trainNo = itemView.findViewById(R.id.train_no_ls);
            trainName = itemView.findViewById(R.id.train_name_ls);
            schArr = itemView.findViewById(R.id.sceduled_arr_ls);
            expArr = itemView.findViewById(R.id.expected_arr_ls);
            pf = itemView.findViewById(R.id.pf_ls);
            source = itemView.findViewById(R.id.source_ls);
            destination = itemView.findViewById(R.id.destination_ls);
            delay = itemView.findViewById(R.id.delay_ls);
        }
    }
}
