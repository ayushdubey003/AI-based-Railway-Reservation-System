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
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sih2020.railwayreservationsystem.Adapters.StationAdapter;
import com.sih2020.railwayreservationsystem.Adapters.TrainsAdapter;
import com.sih2020.railwayreservationsystem.Interfaces.RecyclerTouchListener;
import com.sih2020.railwayreservationsystem.Models.Station;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;
import com.sih2020.railwayreservationsystem.Utils.KMPStringMatching;

import java.util.ArrayList;
import java.util.HashSet;

public class SearchTrains extends AppCompatActivity {

    private LinearLayout mAppBar;
    private EditText mSearchEt;
    private ImageView mMicIv, mCloseIv, mBackIv;
    private ArrayList<Pair<String, String>> mSearchTrains;
    private HashSet<Pair<String, String>> mSearchSet;
    private RecyclerView mSearchList;
    private TrainsAdapter mAdapter;
    private KMPStringMatching kmpStringMatching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_trains);
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
                mSearchTrains.clear();
                mSearchSet.clear();
                if (s.toString().trim().length() == 0 || s.toString() == null) {
                    mSearchList.setVisibility(View.GONE);
                } else {
                    mSearchList.setVisibility(View.VISIBLE);
                    for (int i = 0; i < AppConstants.mTrainListAtStartTime.size(); i++) {
                        Pair<String, String> pair = AppConstants.mTrainListAtStartTime.get(i);
                        String tnum = pair.first.trim().toLowerCase();
                        String tname = pair.second.trim().toLowerCase();
                        if (kmpStringMatching.KMPSearch(s.toString().toLowerCase(), tnum)) {
                            if (!mSearchSet.contains(pair)) {
                                mSearchTrains.add(pair);
                                mSearchSet.add(pair);
                            }
                        }
                        if (kmpStringMatching.KMPSearch(s.toString().toLowerCase(), tname)) {
                            if (!mSearchSet.contains(pair)) {
                                mSearchTrains.add(pair);
                                mSearchSet.add(pair);
                            }
                        }
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
                mSearchList.setVisibility(View.GONE);
            }
        });

        mSearchList.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mSearchList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                AppConstants.mSpotTrainPair = mSearchTrains.get(position);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void init() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mAppBar = findViewById(R.id.app_bar);
        mSearchEt = findViewById(R.id.search_train_et);
        mMicIv = findViewById(R.id.mic_iv);
        mCloseIv = findViewById(R.id.close_iv);
        mSearchList = findViewById(R.id.search_list);
        mBackIv = findViewById(R.id.back_iv);
        mSearchTrains = new ArrayList<>();
        mSearchSet = new HashSet<>();
        mAdapter = new TrainsAdapter(getApplicationContext(), mSearchTrains);
        kmpStringMatching = new KMPStringMatching();
        mSearchList.setLayoutManager(mLayoutManager);
        mSearchList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mSearchList.setItemAnimator(new DefaultItemAnimator());

        mAppBar.setBackground(GenerateBackground.generateBackground());
        mCloseIv.setColorFilter(Color.parseColor("#a9a9a9"));
    }
}
