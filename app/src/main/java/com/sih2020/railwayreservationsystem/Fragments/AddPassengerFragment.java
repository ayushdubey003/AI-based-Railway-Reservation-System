package com.sih2020.railwayreservationsystem.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPassengerFragment extends Fragment {

    private Button addPassengerAp;
    private TextView layer1Ap;

    public AddPassengerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_passenger, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        layer1Ap=view.findViewById(R.id.layer1_ap);
        layer1Ap.setBackground(GenerateBackground.generateBackground());

        addPassengerAp=view.findViewById(R.id.add_passenger_ap);
        addPassengerAp.setBackground(GenerateBackground.generateBackground());
        addPassengerAp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.hideBottomSheet(getActivity());
            }
        });
    }

}
