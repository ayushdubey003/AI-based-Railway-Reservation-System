package com.sih2020.railwayreservationsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sih2020.railwayreservationsystem.Adapters.PnrPassengerListAdapter;
import com.sih2020.railwayreservationsystem.Models.PnrPassengerModel;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PnrActivity extends AppCompatActivity {

    private LinearLayout upper_linear_layout;
    private GradientDrawable gradientDrawable;
    private RecyclerView pnr_passenger_list;
    private PnrPassengerListAdapter mAdapter;
    private ArrayList<PnrPassengerModel> mdata;
    private String mpnr;

    private TextView train_no, train_name, from_date, to_date, from_time, duration,
            to_time, from_code_pf, to_code_pf, from_name, to_name, pnr_show_no, resv_class;

    private ImageView refresh_status_button, pnr_back_button;
    private CardView share_pnr_status_button;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnr);

        setProgressDialog();
        init();

        setPassengerRecyclerView();
        fetchPnrData();

        setOnClicks();
    }

    private void setOnClicks() {
        pnr_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refresh_status_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                fetchPnrData();
                setPassengerRecyclerView();
            }
        });

        share_pnr_status_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PnrActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setProgressDialog() {
        progressDialog = new ProgressDialog(PnrActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching PNR Status...");
        progressDialog.show();
    }

    private void init() {
        pnr_back_button = findViewById(R.id.pnr_back_button);
        share_pnr_status_button = findViewById(R.id.pnr_share_status);
        refresh_status_button = findViewById(R.id.pnr_refresh_button);
        train_no = findViewById(R.id.pnr_train_no);
        train_name = findViewById(R.id.pnr_train_name);
        from_date = findViewById(R.id.pnr_from_date);
        to_date = findViewById(R.id.pnr_to_date);
        from_time = findViewById(R.id.pnr_from_time);
        to_time = findViewById(R.id.pnr_to_time);
        duration = findViewById(R.id.pnr_duration);
        from_code_pf = findViewById(R.id.pnr_from_code_pf);
        to_code_pf = findViewById(R.id.pnr_to_code_pf);
        from_name = findViewById(R.id.pnr_from_name);
        to_name = findViewById(R.id.pnr_to_name);
        pnr_show_no = findViewById(R.id.pnr_show_no);
        resv_class = findViewById(R.id.pnr_class);

        mdata = new ArrayList<>();
        gradientDrawable = GenerateBackground.generateBackground();
        upper_linear_layout = findViewById(R.id.upper_linear_layout);
        upper_linear_layout.setBackground(gradientDrawable);

        pnr_passenger_list = findViewById(R.id.pnr_passenger_list);

        mpnr = getIntent().getStringExtra("PNR_No");
    }

    private void fetchPnrData() {
        String url = "http://192.168.43.128:5000" + "/pnrstatus/" + mpnr;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jobj = response.getJSONObject("status");
                            Log.e("onResponse: ", jobj.toString());
                            setPnrData(jobj);
                        } catch (JSONException e) {
                            Log.e("PnrData: ", e.getMessage());
                            e.printStackTrace();
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(PnrActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void setPnrData(JSONObject jobj) {
        try {
            train_no.setText(jobj.getString("trainNo"));
            train_name.setText(jobj.getString("trainName"));
            from_date.setText(jobj.getString("doj"));
            //TO-DO : change to_date
            to_date.setText(jobj.getString("doj"));
            from_time.setText(jobj.getString("fromTime"));
            to_time.setText(jobj.getString("toTime"));
            duration.setText(jobj.getString("duration"));
            from_code_pf.setText(jobj.getString("fromCode") + " PF " + jobj.getString("platform"));
            //TO-DO : change to platform
            to_code_pf.setText(jobj.getString("toCode"));
            from_name.setText(jobj.getString("fromName"));
            to_name.setText(jobj.getString("toName"));
            pnr_show_no.setText("PNR: " + jobj.getString("pnr"));
            resv_class.setText(jobj.getString("class") + " class");


            JSONArray bs = jobj.getJSONArray("bookingStatus");
            JSONArray cs = jobj.getJSONArray("currentStatus");
            Log.i("setPnrData: ", bs.toString());
            int no_of_passengers = bs.length();
            mdata.clear();
            for (int j=0;j<no_of_passengers;j++)
            {
                Log.d("setPnrData: ",""+bs.get(j));
                mdata.add(new PnrPassengerModel("Passenger "+Integer.toString(j+1),cs.get(j).toString()));
            }

            mAdapter.notifyDataSetChanged();

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }


        } catch (JSONException e) {
            Log.e("PnrData: ", e.getMessage());
            e.printStackTrace();
            finish();
        }
    }

    private void setPassengerRecyclerView() {
//        mdata.add(new PnrPassengerModel("Passenger 1", "S4 61"));
//        mdata.add(new PnrPassengerModel("Passenger 2", "S4 61"));
//        mdata.add(new PnrPassengerModel("Passenger 3", "S4 61"));
//        mdata.add(new PnrPassengerModel("Passenger 4", "S4 61"));
        mAdapter = new PnrPassengerListAdapter(PnrActivity.this, mdata);
        pnr_passenger_list.setLayoutManager(new LinearLayoutManager(this));
        pnr_passenger_list.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        pnr_passenger_list.setAdapter(mAdapter);
    }

    private String findTimeDifference(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa", Locale.getDefault());
        String mTime = sdf.format(new Date());

        try {
            Date end = sdf.parse(mTime);
            Date start = sdf.parse(time);
            long different = end.getTime() - start.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            if (elapsedDays > 0) {
                return Long.toString(elapsedDays) + " days";
            } else if (elapsedHours > 0) {
                return Long.toString(elapsedHours) + " hrs";
            } else if (elapsedMinutes > 0) {
                return Long.toString(elapsedMinutes) + " min";
            } else
                return Long.toString(elapsedSeconds) + " sec";

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "24 min";
    }

}
