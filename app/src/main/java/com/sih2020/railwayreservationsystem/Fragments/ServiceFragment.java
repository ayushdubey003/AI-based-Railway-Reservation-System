package com.sih2020.railwayreservationsystem.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sih2020.railwayreservationsystem.Activities.PassingByTrainsActivity;
import com.sih2020.railwayreservationsystem.Activities.SearchTrains;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;

public class ServiceFragment extends Fragment {
    private LinearLayout open_passingBy;
    private EditText passing_by_edit_text;
    private boolean first_time_flag = true;

    private ImageView clear_passingbyText;

    public ServiceFragment() {
        // Required empty public constructor
    }

    public static ServiceFragment newInstance() {
        ServiceFragment fragment = new ServiceFragment();
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
        View view = inflater.inflate(R.layout.fragment_service, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppConstants.mSourceStation = null;

        init(view);
        receiveClicks();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!first_time_flag) {
            passing_by_edit_text.setText(AppConstants.mSourceStation.getmStationCode());
        } else {
            first_time_flag = false;
        }
    }

    private void receiveClicks() {

        open_passingBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PassingByTrainsActivity.class);
                intent.putExtra("station", AppConstants.mSourceStation.getmStationCode());
                startActivity(intent);
            }
        });
        passing_by_edit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchTrains.class);
                intent.putExtra("type", 3);
                startActivity(intent);
            }
        });

        clear_passingbyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passing_by_edit_text.setText("");
            }
        });
    }

    private void init(View view) {
        open_passingBy = view.findViewById(R.id.open_passing_by);
        passing_by_edit_text = view.findViewById(R.id.passing_by_edit_text);

        clear_passingbyText=view.findViewById(R.id.clear_passing_by_text);
    }
}
