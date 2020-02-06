package com.sih2020.railwayreservationsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sih2020.railwayreservationsystem.Adapters.LiveStationAdapter;
import com.sih2020.railwayreservationsystem.Models.LiveStationModel;
import com.sih2020.railwayreservationsystem.Models.Train;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LiveStation extends AppCompatActivity {

    private ImageView back_button;
    private TextView selectedStationText;
    private RecyclerView live_station_list;
    private ProgressBar progressBar;

    private ArrayList<LiveStationModel> mlist;
    private String toStation = "HWH";
    private String selectedStation;
    private String mtime = "10";

    private LiveStationAdapter madapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_station);

        selectedStation = getIntent().getExtras().getString("station");
        mlist = new ArrayList<>();

        fetchData();
        init();
        receiveClicks();
    }

    private void fetchData() {
        String url = "http://192.168.43.128:5000" + "/livestation/" + selectedStation + "/" + toStation + "/" + mtime;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray listarray = response.getJSONArray("livestation");
                            Log.e("onResponse: 59", listarray.toString());
                            for (int k = 0; k < listarray.length(); k++) {
                                JSONObject jobj = listarray.getJSONObject(k);
                                Log.e("onResponse: ", jobj.toString());

                                String dtrainno = jobj.getString("trainNo");
                                String dtrainname = jobj.getString("trainName");
                                String dscharr = jobj.getString("schTimeArrival");
                                String dexparr = jobj.getString("expTimeArrival");
                                String dpf = jobj.getString("platformNo");
                                String ddelay = jobj.getString("delay");

                                mlist.add(new LiveStationModel(dtrainno, dscharr, dexparr, dtrainname, ddelay,
                                        "Source", "Destination", dpf));
                            }
                            progressBar.setVisibility(View.GONE);
                            madapter.notifyDataSetChanged();

                            Log.e("onResponse: ", "" + mlist.size());
                            //filterCurrentList();

                        } catch (JSONException e) {
                            Log.e("Trainsdata: ", e.getMessage());
                            e.printStackTrace();
                            Toast.makeText(LiveStation.this, "Trains Not Found", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LiveStation.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                //Log.e("onErrorResponse: ", error.getLocalizedMessage());
                finish();
            }
        });
        requestQueue.add(jsonObjectRequest);
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

        selectedStationText = findViewById(R.id.selected_station_ls);
        selectedStationText.setText(selectedStation);

        progressBar = findViewById(R.id.progress_bar_ls);
        back_button = findViewById(R.id.back_ls);
        live_station_list = findViewById(R.id.live_station_list);

        madapter = new LiveStationAdapter(LiveStation.this, mlist);
        live_station_list.setLayoutManager(new LinearLayoutManager(this));
        live_station_list.setAdapter(madapter);
    }
}
