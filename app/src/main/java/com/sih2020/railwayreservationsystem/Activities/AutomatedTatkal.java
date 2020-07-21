package com.sih2020.railwayreservationsystem.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.sih2020.railwayreservationsystem.Adapters.AddPassengerListAdapter;
import com.sih2020.railwayreservationsystem.Fragments.AddPassengerFragment;
import com.sih2020.railwayreservationsystem.Models.AddPassengerModal;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Services.TatkalService;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AutomatedTatkal extends AppCompatActivity {

    private Button mService;
    private ScrollView layerToHide;
    private RelativeLayout appBar;
    private Button reviewButton;
    private TextView addNewPassenger, appBarLowerText, trainNo, trainName, boardTime, reachTime,
            durationText, fromName, fromCode, toCode, toName, classText, availability, fare;
    private ImageView backButton, routeIcon;
    private EditText phoneNo;
    private Spinner mBoardingSpinner;
    private ArrayList<String> mBoardingList;
    private ArrayAdapter<String> mBoardingAdapter;
    private RecyclerView passengerList;
    public AddPassengerListAdapter addPassengerListAdapter;
    public ArrayList<AddPassengerModal> mPassengers;
    ProgressDialog progressDialog;
    private int currentWalletAmount,mTotalFare;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automated_tatkal);
        AppConstants.mAddPassengerList.clear();
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
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        routeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AutomatedTatkal.this, RouteActivity.class);
                intent.putExtra("position", getIntent().getExtras().getString("position"));
                startActivity(intent);
            }
        });

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                    String lmsg = trainNo.getText().toString() + "  " + trainName.getText().toString() + "\n" +
                            "DOJ: " + getIntent().getExtras().getString("doj") + "\n" +
                            getIntent().getExtras().getString("fromCode") + " to " +
                            getIntent().getExtras().getString("toCode") + "\n" +
                            "Boarding at- " + mBoardingList.get(mBoardingSpinner.getSelectedItemPosition()) + "\n" +
                            "Passenger Mobile No: " + phoneNo.getText().toString() + "\n" +
                            "Adults: " + mPassengers.size();

                    new AlertDialog.Builder(AutomatedTatkal.this)
                            .setTitle("Review Booking Details")
                            .setMessage(lmsg)

                            .setPositiveButton("Set automation", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Toast.makeText(AutomatedTatkal.this, "Set the service", Toast.LENGTH_SHORT).show();
                                    progressDialog.show();
                                    fetchWalletBalanceFromBlockChain();

                                }
                            })

                            .setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        });
    }

    private void fetchWalletBalanceFromBlockChain() {
        String url = AppConstants.mUrl + "/calculateWalletAmount/" + FirebaseAuth.getInstance().getCurrentUser().getUid();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int jobj = response.getInt("walletAmount");

                            currentWalletAmount=jobj;
                            progressDialog.dismiss();
                            finish();

                            mTotalFare=mPassengers.size()*Integer.parseInt(fare.getText().toString().split(" ")[1]);

                            if(currentWalletAmount<mTotalFare){
                                Toast.makeText(AutomatedTatkal.this, "Wallet balance is less than total fare", Toast.LENGTH_SHORT).show();
                            }
                            Intent intent=new Intent(AutomatedTatkal.this, TatkalService.class);
                            intent.putExtra("walletbalance",currentWalletAmount);
                            intent.putExtra("totalfare",mTotalFare);

                            startService(intent);



                            Log.e("onResponse: ", response.getString("Success"));
                        } catch (JSONException e) {
                            Log.e("onResponse: ", e.getMessage());
                            e.printStackTrace();
//                            Toast.makeText(PnrActivity.this, "PNR Not Available", Toast.LENGTH_SHORT).show();
//                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(PnrActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                finish();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private boolean validate() {
        return true;
    }

    public void init() {

        mBoardingSpinner = findViewById(R.id.boarding_stations_at);
        progressDialog = new ProgressDialog(AutomatedTatkal.this);
        progressDialog.setMessage("Please wait...");

        mBoardingList = new ArrayList<>();
        mBoardingList = AppConstants.mTrainList.get(getIntent().getExtras().getInt("position")).getmNamedRoutes();

        for (int i = 0; i < mBoardingList.size(); i++){
            
        }

//        boolean flag=false;
//        for(int i=0;i<mBoardingList.size();i++){
//            if(!flag){
//                if (mBoardingList.get(i).equals(getIntent().getExtras().getString("toName"))){
//                    break;
//                }
//                else {
//                    mBoardingList.remove(i);
//                }
//            }
//        }
        mBoardingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mBoardingList);
        mBoardingSpinner.setAdapter(mBoardingAdapter);
        mPassengers = new ArrayList<>();

        //mService=findViewById(R.id.service);
        layerToHide = findViewById(R.id.zeroth_layer);
        addNewPassenger = findViewById(R.id.add_new_passenger);
        appBar = findViewById(R.id.app_bar_at);
        appBar.setBackground(GenerateBackground.generateBackground());
        reviewButton = findViewById(R.id.review_button_at);
        reviewButton.setBackground(GenerateBackground.generateBackground());

        backButton = findViewById(R.id.bacK_button_at);
        phoneNo = findViewById(R.id.phone_no_at);

        String tempNo = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        phoneNo.setText(tempNo.substring(3, 12));

        routeIcon = findViewById(R.id.route_at);

        classText = findViewById(R.id.travel_class_at);
        classText.setText(AppConstants.mClass.getmAbbreviation());

        availability = findViewById(R.id.availability_at);
        availability.setText(getIntent().getExtras().getString("availability"));

        appBarLowerText = findViewById(R.id.app_bar_lower_text);
        appBarLowerText.setText(getIntent().getExtras().getString("fromCode") + " TO " +
                getIntent().getExtras().getString("toCode") + " | " +
                getIntent().getExtras().getString("doj"));

        trainNo = findViewById(R.id.train_no_at);
        trainNo.setText(getIntent().getExtras().getString("trainNo"));

        trainName = findViewById(R.id.train_name_at);
        trainName.setText(getIntent().getExtras().getString("trainName"));

        boardTime = findViewById(R.id.start_time_at);
        boardTime.setText(getIntent().getExtras().getString("reachTime"));

        reachTime = findViewById(R.id.end_time_at);
        reachTime.setText(getIntent().getExtras().getString("boardTime"));

        fromCode = findViewById(R.id.board_station_code);
        fromCode.setText(getIntent().getExtras().getString("toCode"));

        fromName = findViewById(R.id.board_station_name);
        fromName.setText(getIntent().getExtras().getString("toName"));

        toCode = findViewById(R.id.reach_code);
        toCode.setText(getIntent().getExtras().getString("fromName"));

        toName = findViewById(R.id.reach_name);
        toName.setText(getIntent().getExtras().getString("fromCode"));

        fare = findViewById(R.id.fare_at);
        fare.setText(getIntent().getExtras().getString("fare"));

        passengerList = findViewById(R.id.passenger_list_at);
        addPassengerListAdapter = new AddPassengerListAdapter(this, mPassengers, AutomatedTatkal.this);
        passengerList.setLayoutManager(new LinearLayoutManager(this));
        passengerList.setAdapter(addPassengerListAdapter);
    }

    @Override
    public void onBackPressed() {
        Log.e("onBackPressed: ", "" + AppConstants.mBottomSheetOpen);
        if (AppConstants.mBottomSheetOpen) {
            AppConstants.hideBottomSheet(AutomatedTatkal.this);
        } else {
            super.onBackPressed();
        }
    }
}
