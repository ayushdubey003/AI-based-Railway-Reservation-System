package com.sih2020.railwayreservationsystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sih2020.railwayreservationsystem.Models.PnrPassengerModel;
import com.sih2020.railwayreservationsystem.R;

import java.util.ArrayList;

public class PnrPassengerListAdapter extends RecyclerView.Adapter<PnrPassengerListAdapter.ViewHolder> {

    ArrayList<PnrPassengerModel> data;
    Context mcontext;
    public PnrPassengerListAdapter(Context context, ArrayList<PnrPassengerModel> list) {
        this.data = list;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.pnr_passenger_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.pass_no.setText(data.get(position).getPass_no());
        holder.seat_no.setText(data.get(position).getSeat_no());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView pass_no, seat_no;
        public ViewHolder(@NonNull View view) {
            super(view);
            pass_no = view.findViewById(R.id.pass_no);
            seat_no = view.findViewById(R.id.pnr_seat_no);
        }
    }
}
