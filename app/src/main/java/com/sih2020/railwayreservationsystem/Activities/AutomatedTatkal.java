package com.sih2020.railwayreservationsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Services.TatkalService;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;

public class AutomatedTatkal extends AppCompatActivity {

    private Button mService;
    private RelativeLayout appBar;
    private Button reviewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automated_tatkal);
        init();

//        mService.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startService(new Intent(AutomatedTatkal.this, TatkalService.class));
//            }
//        });
    }

    public void init() {
        //mService=findViewById(R.id.service);
        appBar = findViewById(R.id.app_bar_at);
        appBar.setBackground(GenerateBackground.generateBackground());
        reviewButton=findViewById(R.id.review_button_at);
        reviewButton.setBackground(GenerateBackground.generateBackground());
    }
}
