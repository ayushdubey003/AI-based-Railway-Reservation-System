package com.sih2020.railwayreservationsystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sih2020.railwayreservationsystem.Models.AlternateRoutesModel;
import com.sih2020.railwayreservationsystem.R;

import java.util.ArrayList;

public class AlternateRouteAdapter extends RecyclerView.Adapter<AlternateRouteAdapter.MyViewHolder> {

    ArrayList<AlternateRoutesModel> routes;
    Context context;
    public AlternateRouteAdapter(Context context, ArrayList<AlternateRoutesModel> routes)
    {
        this.routes=routes;
        this.context=context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.alternate_routes_items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if(routes.get(position).getN()==1)
        {
            holder.two.setVisibility(View.GONE);
            holder.one.setVisibility(View.VISIBLE);
            holder.img1.setImageResource(R.drawable.ic_train_black_24dp);
            holder.img2.setImageResource(R.drawable.ic_train_black_24dp);
            holder.img3.setImageResource(R.drawable.ic_train_black_24dp);
            holder.t1s.setText(routes.get(position).getStations().get(0).replace('"',' ').trim());
            holder.t1d.setText(routes.get(position).getStations().get(1).replace('"',' ').trim());
            holder.t1n.setText(routes.get(position).getTrainnos().get(0).replace('"',' ').trim());
            holder.t2s.setText(routes.get(position).getStations().get(1).replace('"',' ').trim());
            holder.t2d.setText(routes.get(position).getStations().get(2).replace('"',' ').trim());
            holder.t2n.setText(routes.get(position).getTrainnos().get(1).replace('"',' ').trim());
            holder.s1nm.setText(routes.get(position).getStations().get(0).replace('"',' ').trim());
            holder.s2nm.setText(routes.get(position).getStations().get(1).replace('"',' ').trim());
            holder.s3nm.setText(routes.get(position).getStations().get(2).replace('"',' ').trim());
            int time=Integer.parseInt(routes.get(position).getTime());
            int hr=time/60;
            int min=time%60;
            holder.otime.setText(hr+" hr "+(min==0?"":min+" min "));


        }
        else
        {
            holder.one.setVisibility(View.GONE);
            holder.two.setVisibility(View.VISIBLE);
            holder.timg1.setImageResource(R.drawable.ic_train_black_24dp);
            holder.timg2.setImageResource(android.R.drawable.arrow_up_float);
            holder.timg3.setImageResource(android.R.drawable.arrow_up_float);
            holder.timg4.setImageResource(android.R.drawable.arrow_up_float);
            holder.tt1s.setText(routes.get(position).getStations().get(0));
            holder.tt1d.setText(routes.get(position).getStations().get(1));
            holder.tt1n.setText(routes.get(position).getTrainnos().get(0));
            holder.tt2s.setText(routes.get(position).getStations().get(1));
            holder.tt2d.setText(routes.get(position).getStations().get(2));
            holder.tt2n.setText(routes.get(position).getTrainnos().get(1));
            holder.tt3s.setText(routes.get(position).getStations().get(2));
            holder.tt3d.setText(routes.get(position).getStations().get(3));
            holder.tt3n.setText(routes.get(position).getTrainnos().get(2));
            int time=Integer.parseInt(routes.get(position).getTime());
            int hr=time/60;
            int min=time%60;
            holder.ttime.setText(hr+" hr "+min+" min");
        }

    }

    @Override
    public int getItemCount() {
        return routes.size();
    }


    class  MyViewHolder extends RecyclerView.ViewHolder
    {

        FrameLayout one,two;
        ImageView img1,img2,img3,timg1,timg2,timg3,timg4;
        TextView t1s,t1d,t1n,t2s,t2d,t2n,tt1s,tt1d,tt1n,tt2s,tt2d,tt2n,tt3s,tt3d,tt3n,ttime,otime,s1nm,s2nm,s3nm;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            one=itemView.findViewById(R.id.one);
            two=itemView.findViewById(R.id.two);
            otime=itemView.findViewById(R.id.otime);
            ttime=itemView.findViewById(R.id.ttime);
            img1=itemView.findViewById(R.id.oimg1);
            img2=itemView.findViewById(R.id.oimg2);
            img3=itemView.findViewById(R.id.oimg3);
            timg1=itemView.findViewById(R.id.timg1);
            timg2=itemView.findViewById(R.id.timg2);
            timg3=itemView.findViewById(R.id.timg3);
            timg4=itemView.findViewById(R.id.timg4);
            t1s=itemView.findViewById(R.id.ott1s);
            t1d=itemView.findViewById(R.id.ott1d);
            t1n=itemView.findViewById(R.id.ott1n);
            t2s=itemView.findViewById(R.id.ott2s);
            t2d=itemView.findViewById(R.id.ott2d);
            t2n=itemView.findViewById(R.id.ott2n);
            tt1s=itemView.findViewById(R.id.tt1s);
            tt1d=itemView.findViewById(R.id.tt1d);
            tt1n=itemView.findViewById(R.id.tt1n);
            tt2s=itemView.findViewById(R.id.tt2s);
            tt2d=itemView.findViewById(R.id.tt2d);
            tt2n=itemView.findViewById(R.id.tt2n);
            tt3s=itemView.findViewById(R.id.tt3s);
            tt3d=itemView.findViewById(R.id.tt3d);
            tt3n=itemView.findViewById(R.id.tt3n);
            s1nm=itemView.findViewById(R.id.s1nm);
            s2nm=itemView.findViewById(R.id.s2nm);
            s3nm=itemView.findViewById(R.id.s3nm);

        }
    }
}
