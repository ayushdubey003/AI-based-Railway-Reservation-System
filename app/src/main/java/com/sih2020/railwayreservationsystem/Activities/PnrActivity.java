package com.sih2020.railwayreservationsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PnrActivity extends AppCompatActivity {

    private LinearLayout upper_linear_layout;
    private GradientDrawable gradientDrawable;
    private RecyclerView pnr_passenger_list;
    private PnrPassengerListAdapter mAdapter;
    private ArrayList<PnrPassengerModel> mdata;
    private String mpnr;

    private TextView train_no, train_name, from_date, to_date, from_time, duration,
                to_time, from_code_pf, to_code_pf, from_name, to_name, pnr_show_no, resv_class;

    private ImageView refresh_status_button;
    private CardView share_pnr_status_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnr);

        init();

        fetchPnrData();
        setPassengerRecyclerView();
    }
    private void init() {
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

        mdata = new ArrayList<>();
        gradientDrawable = GenerateBackground.generateBackground();
        upper_linear_layout = findViewById(R.id.upper_linear_layout);
        upper_linear_layout.setBackground(gradientDrawable);

        pnr_passenger_list = findViewById(R.id.pnr_passenger_list);

        mpnr = getIntent().getStringExtra("PNR_No");
    }

    private void fetchPnrData() {
        String url = AppConstants.mUrl+"/pnrstatus/"+mpnr;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jobj = response.getJSONObject("status");
                            Log.e("onResponse: ",jobj.toString());
                            setPnrData(jobj);
                        } catch (JSONException e) {
                            Log.e("PnrData: ",e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PnrActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
            from_code_pf.setText(jobj.getString("fromCode")+" PF "+jobj.getString("platform"));
            //TO-DO : change to platform
            to_code_pf.setText(jobj.getString("fromCode")+" PF "+jobj.getString("platform"));
            from_name.setText(jobj.getString("fromName"));
            to_name.setText(jobj.getString("toName"));
            pnr_show_no.setText(jobj.getString("pnr"));
            resv_class.setText(jobj.getString("class"));
        } catch (JSONException e) {
            Log.e("PnrData: ",e.getMessage());
            e.printStackTrace();
        }
    }

    private void setPassengerRecyclerView() {
        mdata.add(new PnrPassengerModel("Passenger 1","S4 61"));
        mdata.add(new PnrPassengerModel("Passenger 2","S4 61"));
        mdata.add(new PnrPassengerModel("Passenger 3","S4 61"));
        mdata.add(new PnrPassengerModel("Passenger 4","S4 61"));
        mAdapter = new PnrPassengerListAdapter(PnrActivity.this, mdata);
        pnr_passenger_list.setLayoutManager(new LinearLayoutManager(this));
        pnr_passenger_list.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        pnr_passenger_list.setAdapter(mAdapter);
    }
}
