package com.sih2020.railwayreservationsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sih2020.railwayreservationsystem.Adapters.LiveStatusAdapter;
import com.sih2020.railwayreservationsystem.Adapters.RouteAdapter;
import com.sih2020.railwayreservationsystem.Models.Status;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;
import com.sih2020.railwayreservationsystem.Utils.NetworkRequests;

import java.util.ArrayList;

public class TrainLiveStatus extends AppCompatActivity {

    private TextView mAppBarTv;
    private LinearLayout mAppBar, mLegends;
    private ListView mRouteList;
    private ImageView mBack;
    private LiveStatusAdapter mAdapter;
    private RelativeLayout mMainRl, mProgressRl;
    private NetworkRequests networkRequests;
    private CoordinatorLayout mCl;
    private ArrayList<Status> route;
    private RadioButton mYesterday, mToday, mTomorrow;
    private boolean mTodayb, mYesterdayb, mTomorrowb;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_live_status);
        init();
        mToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTodayb)
                    return;
                mTodayb = true;
                mYesterdayb = false;
                mTomorrowb = false;
                mToday.setChecked(true);
                mYesterday.setChecked(false);
                mTomorrow.setChecked(false);
                route.clear();
                String url = AppConstants.mUrl + "/status/" + AppConstants.mSpotTrainPair.first + "/today";
                mToday.setClickable(false);
                mYesterday.setClickable(false);
                mTomorrow.setClickable(false);
                mProgressRl.setVisibility(View.VISIBLE);
                mMainRl.setAlpha(0.2f);
                networkRequests.fetchTrainStatus(url, route);
            }
        });

        mYesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mYesterdayb)
                    return;
                mTodayb = false;
                mYesterdayb = true;
                mTomorrowb = false;
                mYesterday.setChecked(true);
                mToday.setChecked(false);
                mTomorrow.setChecked(false);
                route.clear();
                String url = AppConstants.mUrl + "/status/" + AppConstants.mSpotTrainPair.first + "/yesterday";
                mToday.setClickable(false);
                mYesterday.setClickable(false);
                mTomorrow.setClickable(false);
                mProgressRl.setVisibility(View.VISIBLE);
                mMainRl.setAlpha(0.2f);
                networkRequests.fetchTrainStatus(url, route);
            }
        });

        mTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTomorrowb)
                    return;
                mTodayb = false;
                mYesterdayb = false;
                mTomorrowb = true;
                mToday.setChecked(false);
                mYesterday.setChecked(false);
                mTomorrow.setChecked(true);
                route.clear();
                String url = AppConstants.mUrl + "/status/" + AppConstants.mSpotTrainPair.first + "/tomorrow";
                mToday.setClickable(false);
                mYesterday.setClickable(false);
                mTomorrow.setClickable(false);
                mProgressRl.setVisibility(View.VISIBLE);
                mMainRl.setAlpha(0.2f);
                networkRequests.fetchTrainStatus(url, route);
            }
        });
    }

    private void init() {
        mMainRl = findViewById(R.id.main_rl);
        mProgressRl = findViewById(R.id.progress_rl);
        mAppBar = findViewById(R.id.app_bar);
        mLegends = findViewById(R.id.legends_ll);
        mAppBarTv = findViewById(R.id.app_bar_tv);
        mRouteList = findViewById(R.id.route_list);
        mBack = findViewById(R.id.back_iv);
        mCl = findViewById(R.id.cl);
        mYesterday = findViewById(R.id.yesterday);
        mToday = findViewById(R.id.today);
        mTomorrow = findViewById(R.id.tomorrow);
        networkRequests = new NetworkRequests(mCl, TrainLiveStatus.this, this);
        mAppBar.setBackground(GenerateBackground.generateBackground());
        mLegends.setBackground(GenerateBackground.generateBackground());
        mToday.setClickable(false);
        mYesterday.setClickable(false);
        mTomorrow.setClickable(false);
        mTodayb = true;
        mYesterdayb = false;
        mTomorrowb = false;

        String text = AppConstants.mSpotTrainPair.first + " - " + AppConstants.mSpotTrainPair.second;
        mAppBarTv.setText(text);
        mMainRl.setAlpha(0.2f);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        route = new ArrayList<>();
        String url = AppConstants.mUrl + "/status/" + AppConstants.mSpotTrainPair.first + "/today";
        networkRequests.fetchTrainStatus(url, route);
        mAdapter = new LiveStatusAdapter(this, route);
        mRouteList.setAdapter(mAdapter);
    }

    public void dataFetchComplete(ArrayList<Status> route) {
        mMainRl.setAlpha(1.0f);
        mProgressRl.setVisibility(View.GONE);
        this.route = route;
        mAdapter.notifyDataSetChanged();
        mToday.setClickable(true);
        mYesterday.setClickable(true);
        mTomorrow.setClickable(true);
    }
}
