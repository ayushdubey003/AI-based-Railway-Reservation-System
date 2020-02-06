package com.sih2020.railwayreservationsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.sih2020.railwayreservationsystem.R;

public class LiveStation extends AppCompatActivity {

    private ImageView back_button;
    private RecyclerView live_station_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_station);

        init();
        receiveClicks();
    }

    private void receiveClicks() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        back_button = findViewById(R.id.back_ls);
        live_station_list = findViewById(R.id.live_station_list);
    }
}
