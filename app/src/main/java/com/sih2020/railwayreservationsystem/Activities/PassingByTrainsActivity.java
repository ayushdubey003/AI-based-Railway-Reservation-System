package com.sih2020.railwayreservationsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sih2020.railwayreservationsystem.Adapters.PassingByTrainAdapter;
import com.sih2020.railwayreservationsystem.Models.Train;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PassingByTrainsActivity extends AppCompatActivity {

    private ImageView back_button;
    private LinearLayout date_bar, appBar;
    private ListView passing_train_list;
    private RelativeLayout progressBar;
    private TextView date_text;

    private PassingByTrainAdapter madapter;

    private DatePickerDialog.OnDateSetListener mDate;
    private Calendar mCalendar;

    private String mstation = "TATA";
    private ArrayList<Train> mlist;
    private ArrayList<Train> required_list;
    private String selected_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passing_by_trains);

        mstation = getIntent().getExtras().getString("station");
        mlist = new ArrayList<>();
        required_list = new ArrayList<>();
        fetchData();
        init();
        setClicks();
    }


    private void init() {
        appBar = findViewById(R.id.app_bar_pb);
        appBar.setBackground(GenerateBackground.generateBackground());

        date_text = findViewById(R.id.date_text);
        back_button = findViewById(R.id.back_iv_passing_by);
        date_bar = findViewById(R.id.date_tv_passing_by);
        passing_train_list = findViewById(R.id.passing_by_trains_list);
        progressBar = findViewById(R.id.progress_passingby);

        passing_train_list = findViewById(R.id.passing_by_trains_list);

        selected_day = (String) DateFormat.format("EEEE", AppConstants.mDate);
        Log.e("PassingByTrainAdapter: ", selected_day);

        mCalendar = Calendar.getInstance();
        mDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateJourneyDate();
            }
        };

        String dateToShow = (String) DateFormat.format("MMM", AppConstants.mDate) + " " + (String) DateFormat.format("dd", AppConstants.mDate) + "," + (String) DateFormat.format("yyyy", AppConstants.mDate);
        date_text.setText(dateToShow);

        madapter = new PassingByTrainAdapter(this, required_list, mstation);
        passing_train_list.setAdapter(madapter);
    }

    private void fetchData() {
        String url = AppConstants.mUrl + "/trains/" + mstation;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray listarray = response.getJSONArray("trains");
                            Log.e("onResponse: 55", listarray.toString());
                            for (int k = 0; k < listarray.length(); k++) {
                                JSONObject jobj = listarray.getJSONObject(k);
                                Log.e("onResponse: ", jobj.toString());

                                String dtrainno = jobj.getString("train no");
                                String dtrainname = jobj.getString("train name");
                                JSONArray drunningdays = jobj.getJSONArray("running days");
                                JSONArray droutes_with_names = jobj.getJSONArray("route with names");
                                JSONArray droutes_with_code = jobj.getJSONArray("route with codes");
                                JSONArray darrival_times = jobj.getJSONArray("arrival times");
                                JSONArray ddept_times = jobj.getJSONArray("departure times");

                                ArrayList<String> rnames = new ArrayList<>();
                                ArrayList<String> rcodes = new ArrayList<>();
                                ArrayList<String> arrtime = new ArrayList<>();
                                ArrayList<String> depttime = new ArrayList<>();
                                ArrayList<String> rundays = new ArrayList<>();
                                for (int i = 0; i < drunningdays.length(); i++) {
                                    rundays.add(drunningdays.get(i).toString());
                                }
                                for (int i = 0; i < droutes_with_names.length(); i++) {

                                    rnames.add(droutes_with_names.get(i).toString());
                                    rcodes.add(droutes_with_code.get(i).toString());
                                    arrtime.add(darrival_times.get(i).toString());
                                    depttime.add(ddept_times.get(i).toString());

                                }

                                mlist.add(new Train(dtrainname, dtrainno, "1", "1", arrtime, new ArrayList<String>(), new ArrayList<String>(), depttime,
                                        new ArrayList<String>(), rcodes, rnames, rundays, new ArrayList<String>(), new ArrayList<String>(),
                                        new ArrayList<String>(), "1", "1"));
                            }

                            Log.e("onResponse: ", "" + mlist.size());
                            filterCurrentList();

                        } catch (JSONException e) {
                            Log.e("Trainsdata: ", e.getMessage());
                            e.printStackTrace();
                            Toast.makeText(PassingByTrainsActivity.this, "Trains Not Found", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PassingByTrainsActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                try {
                    Log.e("onErrorResponse: ", error.getLocalizedMessage());
                } catch (Exception e) {
                    //do nothing
                }
                finish();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void filterCurrentList() {
        Log.e("filterCurrentList: ", "called " + selected_day);
        if (required_list.size() > 0)
            required_list.clear();
        String ldate;
        if (selected_day.equalsIgnoreCase("monday"))
            ldate = "MON";
        else if (selected_day.equalsIgnoreCase("tuesday"))
            ldate = "TUE";
        else if (selected_day.equalsIgnoreCase("wednesday"))
            ldate = "WED";
        else if (selected_day.equalsIgnoreCase("thursday"))
            ldate = "THU";
        else if (selected_day.equalsIgnoreCase("friday"))
            ldate = "FRI";
        else if (selected_day.equalsIgnoreCase("saturday"))
            ldate = "SAT";
        else
            ldate = "SUN";

        for (int i = 0; i < mlist.size(); i++) {
            if (mlist.get(i).getmRunningDays().get(0).equalsIgnoreCase("Daily")) {
                required_list.add(mlist.get(i));
            } else {
                for (int j = 0; j < mlist.get(i).getmRunningDays().size(); j++) {
                    if (mlist.get(i).getmRunningDays().get(j).equalsIgnoreCase(ldate)) {
                        required_list.add(mlist.get(i));
                    }
                }
            }
        }

        progressBar.setVisibility(View.GONE);
        madapter.notifyDataSetChanged();
    }

    private void setClicks() {
        date_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpDateAlert();
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpDateAlert() {
        mCalendar.setTime(AppConstants.mDate);
        DatePickerDialog dialog = new DatePickerDialog(this, mDate,
                mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void updateJourneyDate() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date date = null;
        try {
            date = sdf.parse(sdf.format(mCalendar.getTime()));
        } catch (Exception e) {
            Log.e("Level", e.getLocalizedMessage());
        }
        setDate(date);
    }

    private void setDate(Date date) {
        boolean z = true;
        if (!date.equals(AppConstants.mDate))
            z = false;
        AppConstants.mDate = date;
        selected_day = (String) DateFormat.format("EEEE", AppConstants.mDate);
        ;
        String dateToShow = (String) DateFormat.format("MMM", AppConstants.mDate) + " " + (String) DateFormat.format("dd", AppConstants.mDate) + "," + (String) DateFormat.format("yyyy", AppConstants.mDate);
        date_text.setText(dateToShow);
        if (!z) {
            String day = (String) DateFormat.format("EE", date);
            progressBar.setVisibility(View.VISIBLE);
            filterCurrentList();
        }
    }
}
