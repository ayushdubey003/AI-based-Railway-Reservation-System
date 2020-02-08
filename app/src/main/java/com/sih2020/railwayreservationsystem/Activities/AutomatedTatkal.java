package com.sih2020.railwayreservationsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sih2020.railwayreservationsystem.Fragments.AddPassengerFragment;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Services.TatkalService;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;

public class AutomatedTatkal extends AppCompatActivity {

    private Button mService;
    private ScrollView layerToHide;
    private RelativeLayout appBar;
    private Button reviewButton;
    private TextView addNewPassenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automated_tatkal);
        init();
        receiveClicks();

//        mService.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startService(new Intent(AutomatedTatkal.this, TatkalService.class));
//            }
//        });
    }

    private void receiveClicks() {
        addNewPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.showBottomSheet(AutomatedTatkal.this);
                AppConstants.mScrollView = findViewById(R.id.zeroth_layer);
                layerToHide.setAlpha(0.1f);
//                AddPassengerFragment bottomSheet = new AddPassengerFragment();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.no_anim);
//                transaction.add(R.id.fragment_layout, bottomSheet);
//                transaction.commit();
            }
        });
    }

    public void init() {
        //mService=findViewById(R.id.service);
        layerToHide = findViewById(R.id.zeroth_layer);
        addNewPassenger = findViewById(R.id.add_new_passenger);
        appBar = findViewById(R.id.app_bar_at);
        appBar.setBackground(GenerateBackground.generateBackground());
        reviewButton = findViewById(R.id.review_button_at);
        reviewButton.setBackground(GenerateBackground.generateBackground());

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(!AppConstants.mBottomSheetOpen){
//                    layerToHide.setAlpha(1.0f);
//                }
//            }
//        },500);
    }
}
