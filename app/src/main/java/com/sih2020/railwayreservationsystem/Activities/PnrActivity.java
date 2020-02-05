package com.sih2020.railwayreservationsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import static java.lang.Math.min;

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
    private Button share_pnr_status_button;

    ProgressDialog progressDialog;

    private String currentStatus;

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
                Log.e("onClick: ","heyy!!");
                Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_SEND);
                intent2.setType("text/plain");
                intent2.putExtra(Intent.EXTRA_TEXT, "Heyy there! this is the Current Status for my PNR No "+
                        mpnr+" : DOJ: "+from_date.getText().toString()+" "+from_time.getText().toString()+"\n"+
                        from_code_pf.getText().toString()+" to "+to_code_pf.getText().toString()+"\n"+
                        "Current Status: "+"\n"+currentStatus);
                startActivity(Intent.createChooser(intent2, "Share via"));
                //Toast.makeText(PnrActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(PnrActivity.this, "PNR Not Available", Toast.LENGTH_SHORT).show();
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
            currentStatus="";
            for (int j=0;j<no_of_passengers;j++)
            {
                Log.d("setPnrData: ",""+bs.get(j));
                if (cs.get(j).toString().equals("CNF")){
                    mdata.add(new PnrPassengerModel("Passenger "+Integer.toString(j+1),bs.get(j).toString()));
                    currentStatus+="Passenger "+Integer.toString(j+1)+" : "+bs.get(j).toString()+"\n";
                }
                else {
                    mdata.add(new PnrPassengerModel("Passenger " + Integer.toString(j + 1), cs.get(j).toString()));
                    currentStatus += "Passenger " + Integer.toString(j + 1) + " : " + cs.get(j).toString() + "\n";
                }
            }

            mAdapter.notifyDataSetChanged();

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            saveToRecents(jobj.getString("fromCode"), jobj.getString("toCode"),
                    jobj.getString("doj"));


        } catch (JSONException e) {
            Log.e("PnrData: ", e.getMessage());
            e.printStackTrace();
            Toast.makeText(PnrActivity.this, "PNR Not Available", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void saveToRecents(String fromCode, String toCode, String doj) {

        SharedPreferences lsharedpreferences=getSharedPreferences(AppConstants.mPrefsName, MODE_PRIVATE);
        SharedPreferences.Editor editor = lsharedpreferences.edit();
        String[] lpnr = lsharedpreferences.getString("RecentPnrNo","").split(",");

        String[] lfromcode = lsharedpreferences.getString("RecentPnrFrom","").split(",");
        String[] ltocode = lsharedpreferences.getString("RecentPnrTo","").split(",");
        String[] ldoj = lsharedpreferences.getString("RecentPnrDoj","").split(",");

        Log.e("saveToRecents: ",lpnr[0]+" "+lpnr[1]);

        boolean flag=false;
        for (int i=0;i<min(5,lpnr.length);i++){
            if(lpnr[i].equals(mpnr)){
                flag=true;
                if(i!=0){
                    for(int j=0;j<i;j++){
                        lpnr[j+1]=lpnr[j];
                        lfromcode[j+1]=lfromcode[j];
                        ltocode[j+1]=ltocode[j];
                        ldoj[j+1]=ldoj[j];
                    }
                }
                else return;
            }
        }
        if(!flag){
            for(int j=0;j<min(4,lpnr.length);j++){
                lpnr[j+1]=lpnr[j];
                lfromcode[j+1]=lfromcode[j];
                ltocode[j+1]=ltocode[j];
                ldoj[j+1]=ldoj[j];
            }
        }
        lpnr[0]=mpnr;
        lfromcode[0]=fromCode;
        ltocode[0]=toCode;
        ldoj[0]=doj;

        String dpnr, dfrom, dto,ddoj;
        dpnr=lpnr[0]+","+lpnr[1]+","+lpnr[2]+","+lpnr[3]+","+lpnr[4];
        dfrom=lfromcode[0]+","+lfromcode[1]+","+lfromcode[2]+","+lfromcode[3]+","+lfromcode[4];
        dto=ltocode[0]+","+ltocode[1]+","+ltocode[2]+","+ltocode[3]+","+ltocode[4];
        ddoj=ldoj[0]+","+ldoj[1]+","+ldoj[2]+","+ldoj[3]+","+ldoj[4];

        editor.putString("RecentPnrNo",dpnr);
        editor.putString("RecentPnrFrom",dfrom);
        editor.putString("RecentPnrTo",dto);
        editor.putString("RecentPnrDoj",ddoj);

        editor.apply();
        Log.e("saveToRecents: ",lpnr[0]+" "+lpnr[1]);
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
