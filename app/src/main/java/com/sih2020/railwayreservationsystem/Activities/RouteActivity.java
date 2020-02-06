package com.sih2020.railwayreservationsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sih2020.railwayreservationsystem.Adapters.RouteAdapter;
import com.sih2020.railwayreservationsystem.Models.Train;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;

import java.util.List;

public class RouteActivity extends AppCompatActivity {

    private TextView mAppBarTv;
    private LinearLayout mAppBar, mLegends;
    private int mPosition;
    private ListView mRouteList;
    private ImageView mBack;
    private RouteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        init();
    }

    private void init() {
        mPosition = getIntent().getIntExtra("position", 0);
        mAppBar = findViewById(R.id.app_bar);
        mLegends = findViewById(R.id.legends_ll);
        mAppBarTv = findViewById(R.id.app_bar_tv);
        mRouteList = findViewById(R.id.route_list);
        mBack = findViewById(R.id.back_iv);
        mAdapter = new RouteAdapter(this, AppConstants.mTrainList, mPosition);
        mRouteList.setAdapter(mAdapter);

        mAppBar.setBackground(GenerateBackground.generateBackground());
        mLegends.setBackground(GenerateBackground.generateBackground());

        String text = AppConstants.mTrainList.get(mPosition).getmTrainNo() + " - " + AppConstants.mTrainList.get(mPosition).getmTrainName();
        mAppBarTv.setText(text);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
