package com.sih2020.railwayreservationsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.sih2020.railwayreservationsystem.Adapters.PnrPassengerListAdapter;
import com.sih2020.railwayreservationsystem.Models.PnrPassengerModel;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;

import java.util.ArrayList;

public class PnrActivity extends AppCompatActivity {

    private LinearLayout upper_linear_layout;
    private GradientDrawable gradientDrawable;
    private RecyclerView pnr_passenger_list;
    private PnrPassengerListAdapter mAdapter;
    private ArrayList<PnrPassengerModel> mdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnr);

        init();

        setPassRecyclerView();
    }

    private void setPassRecyclerView() {
        mdata.add(new PnrPassengerModel("Passenger 1","S4 61"));
        mdata.add(new PnrPassengerModel("Passenger 2","S4 61"));
        mdata.add(new PnrPassengerModel("Passenger 3","S4 61"));
        mdata.add(new PnrPassengerModel("Passenger 4","S4 61"));
        mAdapter = new PnrPassengerListAdapter(PnrActivity.this, mdata);
        pnr_passenger_list.setLayoutManager(new LinearLayoutManager(this));
        pnr_passenger_list.setAdapter(mAdapter);
    }

    private void init() {
        mdata = new ArrayList<>();
        gradientDrawable = GenerateBackground.generateBackground();
        upper_linear_layout = findViewById(R.id.upper_linear_layout);
        upper_linear_layout.setBackground(gradientDrawable);

        pnr_passenger_list = findViewById(R.id.pnr_passenger_list);
    }
}
