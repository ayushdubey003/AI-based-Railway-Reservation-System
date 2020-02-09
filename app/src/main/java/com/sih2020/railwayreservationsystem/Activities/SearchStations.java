package com.sih2020.railwayreservationsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sih2020.railwayreservationsystem.Adapters.StationAdapter;
import com.sih2020.railwayreservationsystem.Interfaces.RecyclerTouchListener;
import com.sih2020.railwayreservationsystem.Models.Station;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;
import com.sih2020.railwayreservationsystem.Utils.KMPStringMatching;

import java.util.ArrayList;

public class SearchStations extends AppCompatActivity {

    private LinearLayout mAppBar, mLocationLl;
    private EditText mSearchEt;
    private ImageView mMicIv, mCloseIv, mBackIv;
    private ArrayList<Station> mSearchStations;
    private RecyclerView mSearchList;
    private StationAdapter mAdapter;
    private KMPStringMatching kmpStringMatching;
    private int mIntentCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_stations);
        init();
        receiveClicks();
        mSearchList.setAdapter(mAdapter);
    }

    private void receiveClicks() {
        mSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSearchStations.clear();
                if (s.toString().trim().length() == 0 || s.toString() == null) {
                    if (mIntentCode == 1)
                        mLocationLl.setVisibility(View.VISIBLE);
                    mSearchList.setVisibility(View.GONE);
                } else {
                    mLocationLl.setVisibility(View.GONE);
                    mSearchList.setVisibility(View.VISIBLE);
                    for (int i = 0; i < AppConstants.mStationsName.size(); i++) {
                        Station station = AppConstants.mStationsName.get(i);
                        String sn = station.getmStationName().trim().toLowerCase();
                        String sc = station.getmStationCode().trim().toLowerCase();
                        if (kmpStringMatching.KMPSearch(s.toString().toLowerCase(), sn))
                            mSearchStations.add(station);
                        else if (kmpStringMatching.KMPSearch(s.toString().toLowerCase(), sc))
                            mSearchStations.add(station);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchEt.setText("");
                if (mIntentCode == 1)
                    mLocationLl.setVisibility(View.VISIBLE);
                mSearchList.setVisibility(View.GONE);
            }
        });

        mSearchList.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mSearchList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                try {
                    if (mIntentCode == 1)
                        AppConstants.mSourceStation = mSearchStations.get(position);
                    else if (mIntentCode == 2)
                        AppConstants.mDestinationStation = mSearchStations.get(position);
                    else if (mIntentCode == 3) {
                        AppConstants.mSourceStation = mSearchStations.get(position);
                        AppConstants.mFlag = false;
                    } else if (mIntentCode == 4) {
                        AppConstants.mLiveStation = mSearchStations.get(position);
                    } else {
                        AppConstants.mLiveStationOptional = mSearchStations.get(position);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void init() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mIntentCode = getIntent().getIntExtra("type", 1);
        mAppBar = findViewById(R.id.app_bar);
        mSearchEt = findViewById(R.id.search_station_et);
        mMicIv = findViewById(R.id.mic_iv);
        mCloseIv = findViewById(R.id.close_iv);
        mLocationLl = findViewById(R.id.location_ll);
        mSearchList = findViewById(R.id.search_list);
        mBackIv = findViewById(R.id.back_iv);
        mSearchStations = new ArrayList<>();
        mAdapter = new StationAdapter(getApplicationContext(), mSearchStations);
        kmpStringMatching = new KMPStringMatching();
        mSearchList.setLayoutManager(mLayoutManager);
        mSearchList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mSearchList.setItemAnimator(new DefaultItemAnimator());

        mAppBar.setBackground(GenerateBackground.generateBackground());
        mCloseIv.setColorFilter(Color.parseColor("#a9a9a9"));

        if (mIntentCode > 1)
            mLocationLl.setVisibility(View.GONE);
    }
}
