package com.sih2020.railwayreservationsystem.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sih2020.railwayreservationsystem.Activities.LiveStation;
import com.sih2020.railwayreservationsystem.Activities.PassingByTrainsActivity;
import com.sih2020.railwayreservationsystem.Activities.SearchStations;
import com.sih2020.railwayreservationsystem.Activities.SearchTrains;
import com.sih2020.railwayreservationsystem.Activities.TrainLiveStatus;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;

import static com.android.volley.VolleyLog.e;

public class ServiceFragment extends Fragment {
    private LinearLayout open_passingBy, liveStationOpen, mSpotLL;
    private EditText passing_by_edit_text, liveStationEditText, mSpotTrain;

    private ImageView clear_passingbyText, liveStationClear, spotTrainClear, mSpotIv, mLiveIv, mPassIv;
    private TextView mSpotTv, mLiveTv, mPassTv;
    private CardView mSpotCv, mLiveCv, mPassCv;

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
        AppConstants.mDestinationStation = null;

        init(view);
        receiveClicks();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (AppConstants.mLiveStation != null) {
            mLiveCv.setVisibility(View.VISIBLE);
            mLiveIv.setVisibility(View.GONE);
            mLiveTv.setText(AppConstants.mLiveStation.getmStationCode());
            liveStationEditText.setText(AppConstants.mLiveStation.getmStationName());
            liveStationClear.setVisibility(View.VISIBLE);
        }

        if (!AppConstants.mFlag && AppConstants.mSourceStation != null) {
            mPassCv.setVisibility(View.VISIBLE);
            mPassIv.setVisibility(View.GONE);
            mPassTv.setText(AppConstants.mSourceStation.getmStationCode());
            passing_by_edit_text.setText(AppConstants.mSourceStation.getmStationName());
            clear_passingbyText.setVisibility(View.VISIBLE);
        } else {
            AppConstants.mFlag = false;
        }
        try {
            int x;
            if (AppConstants.mSpotTrainPair.first.equals(null))
                x = 1 / 0;
            mSpotTrain.setText(AppConstants.mSpotTrainPair.second);
            mSpotTv.setText(AppConstants.mSpotTrainPair.first);
            mSpotCv.setVisibility(View.VISIBLE);
            mSpotIv.setVisibility(View.GONE);
            spotTrainClear.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            mSpotTrain.setText("");
            mSpotCv.setVisibility(View.GONE);
            mSpotIv.setVisibility(View.VISIBLE);
            spotTrainClear.setVisibility(View.GONE);
        }
    }

    private void receiveClicks() {

        liveStationOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liveStationEditText.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Select a Station", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), LiveStation.class);
                    intent.putExtra("station", AppConstants.mLiveStation.getmStationCode());
                    startActivity(intent);
                }
            }
        });

        liveStationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchStations.class);
                intent.putExtra("type", 4);
                startActivity(intent);
            }
        });

        liveStationClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liveStationEditText.setText("");
                liveStationClear.setVisibility(View.GONE);
                mLiveCv.setVisibility(View.GONE);
                mLiveIv.setVisibility(View.VISIBLE);
                AppConstants.mLiveStation = null;
            }
        });

        open_passingBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passing_by_edit_text.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Select a Station", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), PassingByTrainsActivity.class);
                    intent.putExtra("station", AppConstants.mSourceStation.getmStationCode());
                    startActivity(intent);
                }
            }
        });

        passing_by_edit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchStations.class);
                intent.putExtra("type", 3);
                startActivity(intent);
            }
        });

        clear_passingbyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passing_by_edit_text.setText("");
                clear_passingbyText.setVisibility(View.GONE);
                mPassCv.setVisibility(View.GONE);
                mPassIv.setVisibility(View.VISIBLE);
                AppConstants.mSourceStation = null;
                AppConstants.mFlag = true;
            }
        });

        mSpotTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchTrains.class));
            }
        });

        spotTrainClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpotTrain.setText("");
                mSpotCv.setVisibility(View.GONE);
                mSpotIv.setVisibility(View.VISIBLE);
                spotTrainClear.setVisibility(View.GONE);
                Pair<String, String> p = new Pair<>(null, null);
                AppConstants.mSpotTrainPair = p;
            }
        });

        mSpotLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TrainLiveStatus.class));
            }
        });
    }

    private void init(View view) {
        open_passingBy = view.findViewById(R.id.open_passing_by);
        passing_by_edit_text = view.findViewById(R.id.passing_by_edit_text);
        clear_passingbyText = view.findViewById(R.id.clear_passing_by_text);

        liveStationOpen = view.findViewById(R.id.live_station_open);
        liveStationEditText = view.findViewById(R.id.live_station_edit_text);
        liveStationClear = view.findViewById(R.id.live_station_clear);

        spotTrainClear = view.findViewById(R.id.clear_train_no_st);

        mSpotTrain = view.findViewById(R.id.spot_train);
        mSpotLL = view.findViewById(R.id.spot_train_ll);

        clear_passingbyText.setVisibility(View.GONE);
        liveStationClear.setVisibility(View.GONE);
        spotTrainClear.setVisibility(View.GONE);

        mLiveCv = view.findViewById(R.id.live_cv);
        mLiveIv = view.findViewById(R.id.live_iv);
        mLiveTv = view.findViewById(R.id.live_tv);

        mSpotCv = view.findViewById(R.id.spot_cv);
        mSpotIv = view.findViewById(R.id.spot_iv);
        mSpotTv = view.findViewById(R.id.spot_tv);

        mPassCv = view.findViewById(R.id.pass_cv);
        mPassIv = view.findViewById(R.id.pass_iv);
        mPassTv = view.findViewById(R.id.pass_tv);
    }

}
