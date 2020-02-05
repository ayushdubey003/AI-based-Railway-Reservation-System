package com.sih2020.railwayreservationsystem.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.sih2020.railwayreservationsystem.Activities.PnrActivity;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;

public class TripsFragment extends Fragment {

    private LinearLayout find_pnr_button;
    private EditText pnr_no;

    private TextView pnrno1, code1, doj1, pnrno2, code2, doj2, pnrno3, code3, doj3, pnrno4, code4, doj4,
            pnrno5, code5, doj5;

    private CardView pnr1, pnr2, pnr3, pnr4, pnr5;

    public TripsFragment() {
        // Required empty public constructor
    }

    public static TripsFragment newInstance() {
        TripsFragment fragment = new TripsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setRecentPnrData();
        setClicks();
    }

    private void setRecentPnrData() {
        SharedPreferences lsharedpreferences = this.getActivity().getSharedPreferences(AppConstants.mPrefsName,Context.MODE_PRIVATE);
        String[] lpnr = lsharedpreferences.getString("RecentPnrNo","").split(",");

        String[] lfromcode = lsharedpreferences.getString("RecentPnrFrom","").split(",");
        String[] ltocode = lsharedpreferences.getString("RecentPnrTo","").split(",");
        String[] ldoj = lsharedpreferences.getString("RecentPnrDoj","").split(",");

        if(lpnr[0].length()==10){
            pnrno1.setText(lpnr[0]);
            code1.setText(lfromcode[0]+"-"+ltocode[0]);
            doj1.setText("DOJ-"+ldoj[0]);
        }
        else {
            pnr1.setVisibility(View.GONE);
        }
        if(lpnr[1].length()==10){
            pnrno2.setText(lpnr[1]);
            code2.setText(lfromcode[1]+"-"+ltocode[1]);
            doj2.setText("DOJ-"+ldoj[1]);
        }
        else {
            pnr2.setVisibility(View.GONE);
        }
        if(lpnr[2].length()==10){
            pnrno3.setText(lpnr[2]);
            code3.setText(lfromcode[2]+"-"+ltocode[2]);
            doj3.setText("DOJ-"+ldoj[2]);
        }
        else {
            pnr3.setVisibility(View.GONE);
        }
        if(lpnr[3].length()==10){
            pnrno4.setText(lpnr[3]);
            code4.setText(lfromcode[3]+"-"+ltocode[3]);
            doj4.setText("DOJ-"+ldoj[3]);
        }
        else {
            pnr4.setVisibility(View.GONE);
        }
        if(lpnr[4].length()==10){
            pnrno5.setText(lpnr[4]);
            code5.setText(lfromcode[4]+"-"+ltocode[4]);
            doj5.setText("DOJ-"+ldoj[4]);
        }
        else {
            pnr5.setVisibility(View.GONE);
        }
    }

    private void setClicks() {
        Log.e("onClick: ", "level 0");
        find_pnr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    Intent intent = new Intent(getActivity(), PnrActivity.class);
                    intent.putExtra("PNR_No", pnr_no.getText().toString().trim());
                    startActivity(intent);
                    Log.e("onClick: ", "okay");
                } else {
                    Snackbar.make(getView(), "Invalid PNR Number", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        pnr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PnrActivity.class);
                intent.putExtra("PNR_No", pnrno1.getText().toString().trim());
                startActivity(intent);
            }
        });

        pnr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PnrActivity.class);
                intent.putExtra("PNR_No", pnrno2.getText().toString().trim());
                startActivity(intent);
            }
        });

        pnr3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PnrActivity.class);
                intent.putExtra("PNR_No", pnrno3.getText().toString().trim());
                startActivity(intent);
            }
        });

        pnr4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PnrActivity.class);
                intent.putExtra("PNR_No", pnrno4.getText().toString().trim());
                startActivity(intent);
            }
        });

        pnr5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PnrActivity.class);
                intent.putExtra("PNR_No", pnrno5.getText().toString().trim());
                startActivity(intent);
            }
        });
    }

    private boolean validate() {
        String tpnr = pnr_no.getText().toString().trim();
        if (tpnr.length() != 10) {
            return false;
        }
        return true;
    }

    private void init(View view) {
        find_pnr_button = view.findViewById(R.id.find_pnr_button);
        pnr_no = view.findViewById(R.id.pnr_no);

        pnrno1 = view.findViewById(R.id.recent_pnrno1);
        code1 = view.findViewById(R.id.recent_code1);
        doj1 = view.findViewById(R.id.recent_doj1);

        pnrno2 = view.findViewById(R.id.recent_pnrno2);
        code2 = view.findViewById(R.id.recent_code2);
        doj2 = view.findViewById(R.id.recent_doj2);

        pnrno3 = view.findViewById(R.id.recent_pnrno3);
        code3 = view.findViewById(R.id.recent_code3);
        doj3 = view.findViewById(R.id.recent_doj3);

        pnrno4 = view.findViewById(R.id.recent_pnrno4);
        code4 = view.findViewById(R.id.recent_code4);
        doj4 = view.findViewById(R.id.recent_doj4);

        pnrno5 = view.findViewById(R.id.recent_pnrno5);
        code5 = view.findViewById(R.id.recent_code5);
        doj5 = view.findViewById(R.id.recent_doj5);

        pnr1 = view.findViewById(R.id.recent_pnr1);
        pnr2 = view.findViewById(R.id.recent_pnr2);
        pnr3 = view.findViewById(R.id.recent_pnr3);
        pnr4 = view.findViewById(R.id.recent_pnr4);
        pnr5 = view.findViewById(R.id.recent_pnr5);
    }
}
