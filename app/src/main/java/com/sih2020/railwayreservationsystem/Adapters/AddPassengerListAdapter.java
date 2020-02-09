package com.sih2020.railwayreservationsystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sih2020.railwayreservationsystem.Activities.AutomatedTatkal;
import com.sih2020.railwayreservationsystem.Models.AddPassengerModal;
import com.sih2020.railwayreservationsystem.R;

import java.util.ArrayList;

public class AddPassengerListAdapter extends RecyclerView.Adapter<AddPassengerListAdapter.Viewholder> {

    Context mContext;
    ArrayList<AddPassengerModal> mList=new ArrayList<>();

    public AddPassengerListAdapter(Context context,ArrayList<AddPassengerModal> data) {
        this.mContext=context;
        this.mList=data;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.add_passenger_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.name.setText(mList.get(position).getmName());
        holder.restInfo.setText(mList.get(position).getmAge()+" "+
                mList.get(position).getmGender()+" | "+
                mList.get(position).getmBerth()+" | "+
                mList.get(position).getmNationality());
        holder.deletePassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Ruk bhai thoda...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView name,restInfo;
        ImageView deletePassenger;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.passenger_name_api);
            restInfo=itemView.findViewById(R.id.rest_info_api);
            deletePassenger=itemView.findViewById(R.id.delete_passenger_api);
        }
    }
}
