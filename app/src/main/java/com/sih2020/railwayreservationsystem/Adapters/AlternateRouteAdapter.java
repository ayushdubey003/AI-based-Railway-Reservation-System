package com.sih2020.railwayreservationsystem.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sih2020.railwayreservationsystem.Models.AlternateModel;
import com.sih2020.railwayreservationsystem.Models.AlternateRoutesModel;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;

import java.util.ArrayList;

public class AlternateRouteAdapter extends RecyclerView.Adapter<AlternateRouteAdapter.MyViewHolder> {

    ArrayList<AlternateModel> routes;
    Context context;

    public AlternateRouteAdapter(Context context, ArrayList<AlternateModel> routes) {
        this.routes = routes;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alternate_routes_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (routes.get(position).getN() == 1) {
            try {
                holder.two.setVisibility(View.GONE);
                holder.one.setVisibility(View.VISIBLE);
                holder.img1.setImageResource(R.drawable.ic_train_black_24dp);
                holder.img2.setImageResource(R.drawable.ic_train_black_24dp);
                holder.img3.setImageResource(R.drawable.ic_train_black_24dp);
                holder.t1s.setText(routes.get(position).getmStations().get(0).replace('"', ' ').trim());
                holder.t1d.setText(routes.get(position).getmStations().get(1).replace('"', ' ').trim());
                holder.t1n.setText(routes.get(position).getmTrains().get(0).replace('"', ' ').trim());
                holder.t2s.setText(routes.get(position).getmStations().get(1).replace('"', ' ').trim());
                holder.t2d.setText(routes.get(position).getmStations().get(2).replace('"', ' ').trim());
                holder.t2n.setText(routes.get(position).getmTrains().get(1).replace('"', ' ').trim());
                holder.s1nm.setText(routes.get(position).getmStations().get(0).replace('"', ' ').trim());
                holder.s2nm.setText(routes.get(position).getmStations().get(1).replace('"', ' ').trim());
                holder.s3nm.setText(routes.get(position).getmStations().get(2).replace('"', ' ').trim());

            } catch (Exception e) {

            }
            try {
                int time = Integer.parseInt(routes.get(position).getmTime());
                int hr = time / 60;
                int min = time % 60;
                holder.otime.setText(hr + " hr " + (min == 0 ? "" : min + " min "));
                if (routes.get(position).getmFares().get(routes.get(position).getmTrains().get(0)).length() == 0)
                    holder.ofare_tv1.setText("UNAVAILABLE");
                else {
                    holder.ofare_tv1.setText(routes.get(position).getmFares().get(routes.get(position).getmTrains().get(0)));
                }
                holder.ofare_tv1.setVisibility(View.VISIBLE);
                holder.ofare_progress1.setVisibility(View.GONE);
            } catch (Exception e) {
            }
            try {
                if (routes.get(position).getmConfirmation().get(routes.get(position).getmTrains().get(0)).length() == 0)
                    holder.oconfirmation_tv1.setText("UNAVAILABLE");
                else {
                    String s = routes.get(position).getmConfirmation().get(routes.get(position).getmTrains().get(1));
                    s = s.substring(0, 5);
                    s = s + "%";
                    holder.oconfirmation_tv1.setText(s);
                }
                holder.oconfirmation_tv1.setVisibility(View.VISIBLE);
                holder.oconfirmation_progress1.setVisibility(View.GONE);
            } catch (Exception e) {
            }

            try {

//                Log.e("Hey", routes.get(position).getmSeatstatus().get(routes.get(position).getmTrains().get(0)) + "xx");
                if(routes.get(position).getmSeatstatus().get(routes.get(position).getmTrains().get(0)).contains("AVBL"))
                    holder.oConfirmationRl1.setVisibility(View.GONE);
                if (routes.get(position).getmSeatstatus().get(routes.get(position).getmTrains().get(0)).length() == 0)
                    holder.oavailability_tv1.setText("UNAVAILABLE");
                else {
                    holder.oavailability_tv1.setText(routes.get(position).getmSeatstatus().get(routes.get(position).getmTrains().get(0)));
                }

                holder.oavailability_tv1.setVisibility(View.VISIBLE);
                holder.oavailability_progress1.setVisibility(View.GONE);
            } catch (Exception e) {
            }

            try {
                if (routes.get(position).getmFares().get(routes.get(position).getmTrains().get(1)).length() == 0)
                    holder.ofare_tv2.setText("N/A");
                else
                    holder.ofare_tv2.setText(routes.get(position).getmFares().get(routes.get(position).getmTrains().get(1)));
                holder.ofare_tv2.setVisibility(View.VISIBLE);
                holder.ofare_progress2.setVisibility(View.GONE);
            } catch (Exception e) {
            }

            try {
//                Log.e("Hey", routes.get(position).getmConfirmation().get(routes.get(position).getmTrains().get(1)) + "xx");
                if(routes.get(position).getmSeatstatus().get(routes.get(position).getmTrains().get(1)).contains("AVBL"))
                    holder.oConfirmationRl2.setVisibility(View.GONE);
                if (routes.get(position).getmConfirmation().get(routes.get(position).getmTrains().get(1)).length() == 0)
                    holder.oconfirmation_tv2.setText("N/A");
                else {
                    String s = routes.get(position).getmConfirmation().get(routes.get(position).getmTrains().get(1));
                    s = s.substring(0, 5);
                    s = s + "%";
                    holder.oconfirmation_tv2.setText(s);
                }
                holder.oconfirmation_tv2.setVisibility(View.VISIBLE);
                holder.oconfirmation_progress2.setVisibility(View.GONE);
            } catch (Exception e) {
            }

            try {
//                Log.e("Hey", routes.get(position).getmSeatstatus().get(routes.get(position).getmTrains().get(1)) + "xx");
                if (routes.get(position).getmSeatstatus().get(routes.get(position).getmTrains().get(1)).length() == 0)
                    holder.oavailability_tv2.setText("UNAVAILABLE");
                else {
                    Log.e("Hey", routes.get(position).getmSeatstatus().get(routes.get(position).getmTrains().get(1)));
                    holder.oavailability_tv2.setText(routes.get(position).getmSeatstatus().get(routes.get(position).getmTrains().get(1)));
                }
                holder.oavailability_tv2.setVisibility(View.VISIBLE);
                holder.oavailability_progress2.setVisibility(View.GONE);
            } catch (Exception e) {

            }
            holder.otravel_class_tv1.setText(AppConstants.mClass.getmAbbreviation());
            holder.otravel_class_tv2.setText(AppConstants.mClass.getmAbbreviation());

        } else {
            try {
                holder.one.setVisibility(View.GONE);
                holder.two.setVisibility(View.VISIBLE);
                holder.timg1.setImageResource(R.drawable.ic_train_black_24dp);
                holder.timg2.setImageResource(android.R.drawable.arrow_up_float);
                holder.timg3.setImageResource(android.R.drawable.arrow_up_float);
                holder.timg4.setImageResource(android.R.drawable.arrow_up_float);
                holder.tt1s.setText(routes.get(position).getmStations().get(0));
                holder.tt1d.setText(routes.get(position).getmStations().get(1));
                holder.tt1n.setText(routes.get(position).getmTrains().get(0));
                holder.tt2s.setText(routes.get(position).getmStations().get(1));
                holder.tt2d.setText(routes.get(position).getmStations().get(2));
                holder.tt2n.setText(routes.get(position).getmTrains().get(1));
                holder.tt3s.setText(routes.get(position).getmStations().get(2));
                holder.tt3d.setText(routes.get(position).getmStations().get(3));
                holder.tt3n.setText(routes.get(position).getmTrains().get(2));
            } catch (Exception e) {

            }
            try {
                int time = Integer.parseInt(routes.get(position).getmTime());
                int hr = time / 60;
                int min = time % 60;
                holder.otime.setText(hr + " hr " + (min == 0 ? "" : min + " min "));
                Log.e("Hey", routes.get(position).getmTrains().get(0) + "  " + routes.get(position).getmFares().get(Integer.parseInt(routes.get(position).getmTrains().get(0))));
                holder.ofare_tv1.setVisibility(View.VISIBLE);
                holder.ofare_progress1.setVisibility(View.GONE);
                Log.e("Hey", routes.get(position).getmFares().get(routes.get(position).getmTrains().get(0)) + "xx");
                if (routes.get(position).getmFares().get(routes.get(position).getmTrains().get(0)).length() == 0)
                    holder.ofare_tv1.setText("UNAVAILABLE");
                else {
                    Log.e("Hey", routes.get(position).getmFares().get(routes.get(position).getmTrains().get(0)));
                    holder.ofare_tv1.setText(routes.get(position).getmFares().get(routes.get(position).getmTrains().get(0)));
                }
                holder.ofare_tv2.setVisibility(View.VISIBLE);
                holder.ofare_progress2.setVisibility(View.GONE);
                if (routes.get(position).getmFares().get(routes.get(position).getmTrains().get(1)).length() == 0)
                    holder.ofare_tv2.setText("UNAVAILABLE");
                else
                    holder.ofare_tv2.setText(routes.get(position).getmFares().get(routes.get(position).getmTrains().get(1)));
                holder.ofare_tv3.setVisibility(View.VISIBLE);
                holder.ofare_progress3.setVisibility(View.GONE);
                if (routes.get(position).getmFares().get(routes.get(position).getmTrains().get(2)).length() == 0)
                    holder.ofare_tv2.setText("UNAVAILABLE");
                else
                    holder.ofare_tv2.setText(routes.get(position).getmFares().get(routes.get(position).getmTrains().get(2)));
            } catch (Exception e) {
                Log.e("Route", "Exception was generated " + e.getMessage());
            }

        }

    }

    @Override
    public int getItemCount() {
        return routes.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        FrameLayout one, two;
        ImageView img1, img2, img3, timg1, timg2, timg3, timg4;
        TextView t1s, t1d, t1n, t2s, t2d, t2n, tt1s, tt1d, tt1n, tt2s, tt2d, tt2n, tt3s, tt3d, tt3n, ttime, otime, s1nm, s2nm, s3nm;
        TextView otravel_class_tv1, ofare_tv1, oavailability_tv1, oconfirmation_tv1, otravel_class_tv2, ofare_tv2, oavailability_tv2, oconfirmation_tv2, ofare_tv3, oconfirmation_tv3, oavailability_tv3;
        ProgressBar ofare_progress1, ofare_progress2, ofare_progress3, oconfirmation_progress1, oconfirmation_progress2, oavailability_progress1, oavailability_progress2, oavailability_progress3;
        ProgressBar fare_progress1, confirmation_progress1, availability_progress1, fare_progress2, confirmation_progress2, availability_progress2,
                fare_progress3, confirmation_progress3, availability_progress3;
        TextView fare_tv1, fare_tv2, fare_tv3, confirmation_tv1, confirmation_tv2, confirmation_tv3, availability_tv1, availability_tv2, availability_tv3, travel_class_tv1, travel_class_tv2, travel_class_tv3;
        RelativeLayout oConfirmationRl1, oConfirmationRl2;
        RelativeLayout predictionRl;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            one = itemView.findViewById(R.id.one);
            two = itemView.findViewById(R.id.two);
            otime = itemView.findViewById(R.id.otime);
            ttime = itemView.findViewById(R.id.ttime);
            img1 = itemView.findViewById(R.id.oimg1);
            img2 = itemView.findViewById(R.id.oimg2);
            img3 = itemView.findViewById(R.id.oimg3);
            timg1 = itemView.findViewById(R.id.timg1);
            timg2 = itemView.findViewById(R.id.timg2);
            timg3 = itemView.findViewById(R.id.timg3);
            timg4 = itemView.findViewById(R.id.timg4);
            t1s = itemView.findViewById(R.id.ott1s);
            t1d = itemView.findViewById(R.id.ott1d);
            t1n = itemView.findViewById(R.id.ott1n);
            t2s = itemView.findViewById(R.id.ott2s);
            t2d = itemView.findViewById(R.id.ott2d);
            t2n = itemView.findViewById(R.id.ott2n);
            tt1s = itemView.findViewById(R.id.tt1s);
            tt1d = itemView.findViewById(R.id.tt1d);
            tt1n = itemView.findViewById(R.id.tt1n);
            tt2s = itemView.findViewById(R.id.tt2s);
            tt2d = itemView.findViewById(R.id.tt2d);
            tt2n = itemView.findViewById(R.id.tt2n);
            tt3s = itemView.findViewById(R.id.tt3s);
            tt3d = itemView.findViewById(R.id.tt3d);
            tt3n = itemView.findViewById(R.id.tt3n);
            s1nm = itemView.findViewById(R.id.s1nm);
            s2nm = itemView.findViewById(R.id.s2nm);
            s3nm = itemView.findViewById(R.id.s3nm);
            otravel_class_tv1 = itemView.findViewById(R.id.otravel_class_tv1);
            oavailability_tv1 = itemView.findViewById(R.id.oavailability_tv1);
            oconfirmation_tv1 = itemView.findViewById(R.id.oconfirmation_tv1);
            ofare_tv1 = itemView.findViewById(R.id.ofare_tv1);
            otravel_class_tv2 = itemView.findViewById(R.id.otravel_class_tv2);
            oavailability_tv2 = itemView.findViewById(R.id.oavailability_tv2);
            oconfirmation_tv2 = itemView.findViewById(R.id.oconfirmation_tv2);
            ofare_tv2 = itemView.findViewById(R.id.ofare_tv2);
            ofare_progress1 = itemView.findViewById(R.id.ofare_progress1);
            ofare_progress2 = itemView.findViewById(R.id.ofare_progress2);
            oconfirmation_progress1 = itemView.findViewById(R.id.oconfirmation_progress1);
            oconfirmation_progress2 = itemView.findViewById(R.id.oconfirmation_progress2);
            oavailability_progress1 = itemView.findViewById(R.id.oavailability_progress1);
            oavailability_progress2 = itemView.findViewById(R.id.oavailability_progress2);
            fare_progress1 = itemView.findViewById(R.id.fare_progress1);
            fare_progress2 = itemView.findViewById(R.id.fare_progress2);
            fare_progress3 = itemView.findViewById(R.id.fare_progress3);
            confirmation_progress1 = itemView.findViewById(R.id.confirmation_progress1);
            confirmation_progress2 = itemView.findViewById(R.id.confirmation_progress2);
            confirmation_progress3 = itemView.findViewById(R.id.confirmation_progress3);
            fare_tv1 = itemView.findViewById(R.id.fare_tv1);
            fare_tv2 = itemView.findViewById(R.id.fare_tv2);
            fare_tv3 = itemView.findViewById(R.id.fare_tv3);
            confirmation_tv1 = itemView.findViewById(R.id.confirmation_tv1);
            confirmation_tv2 = itemView.findViewById(R.id.confirmation_tv2);
            confirmation_tv3 = itemView.findViewById(R.id.confirmation_tv3);
            availability_tv1 = itemView.findViewById(R.id.availability_tv1);
            availability_tv2 = itemView.findViewById(R.id.availability_tv2);
            availability_tv3 = itemView.findViewById(R.id.availability_tv3);
            travel_class_tv1 = itemView.findViewById(R.id.travel_class_tv1);
            travel_class_tv2 = itemView.findViewById(R.id.travel_class_tv2);
            travel_class_tv3 = itemView.findViewById(R.id.travel_class_tv3);
            oConfirmationRl1 = itemView.findViewById(R.id.oconfirmation_rl1);
            oConfirmationRl2 = itemView.findViewById(R.id.oconfirmation_rl2);
        }
    }
}
