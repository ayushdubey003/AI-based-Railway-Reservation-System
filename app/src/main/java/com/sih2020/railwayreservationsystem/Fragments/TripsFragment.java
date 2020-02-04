package com.sih2020.railwayreservationsystem.Fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.sih2020.railwayreservationsystem.Activities.PnrActivity;
import com.sih2020.railwayreservationsystem.R;

public class TripsFragment extends Fragment {

    private LinearLayout find_pnr_button;
    private EditText pnr_no;

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
        setClicks();
    }

    private void setClicks() {
        Log.e("onClick: ", "level 0");
        find_pnr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    Intent intent = new Intent(getActivity(), PnrActivity.class);
                    intent.putExtra("PNR_No",pnr_no.getText().toString().trim());
                    startActivity(intent);
                    Log.e("onClick: ", "okay");
                } else {
                    Snackbar.make(getView(), "Invalid PNR Number", Snackbar.LENGTH_LONG).show();
                }
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
    }
}
