package com.sih2020.railwayreservationsystem.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sih2020.railwayreservationsystem.Adapters.TrainAdapter;
import com.sih2020.railwayreservationsystem.Models.SpinnerModel;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;
import com.sih2020.railwayreservationsystem.Utils.NetworkRequests;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Stack;

public class SeatAvailabilityActivity extends AppCompatActivity {

    private String mUrl;
    private NetworkRequests networkRequests;
    private CoordinatorLayout mCL;
    private static final String LOG_TAG = "SeatAvailability";
    private LinearLayout mToolbar;
    private RelativeLayout mMain, mProgress;
    public TrainAdapter mAdapter;
    private ListView mList;
    private TextView mDateTv, mTravelClass;
    private DatePickerDialog.OnDateSetListener mDate;
    private Calendar mCalendar;
    private ImageView mBack;
    private RadioButton lastChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_availability);
        init();
        receiveClicks();
    }

    private void receiveClicks() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpDateAlert();
            }
        });
        mTravelClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpClassAlert();
            }
        });
    }

    private void init() {
        mUrl = getIntent().getStringExtra("url");
        mCL = findViewById(R.id.cl);
        mToolbar = findViewById(R.id.app_bar);
        mMain = findViewById(R.id.main_rl);
        mProgress = findViewById(R.id.progress_rl);
        mList = findViewById(R.id.trains_list);
        mDateTv = findViewById(R.id.date_tv);
        mTravelClass = findViewById(R.id.travel_class_tv);
        mBack = findViewById(R.id.back_iv);
        mAdapter = new TrainAdapter(SeatAvailabilityActivity.this, AppConstants.mTrainList);
        mCalendar = Calendar.getInstance();
        mList.setAdapter(mAdapter);
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

        networkRequests = new NetworkRequests(mCL, this, SeatAvailabilityActivity.this);
        networkRequests.fetchTrainsFromUrl(mUrl);
        mToolbar.setBackground(GenerateBackground.generateBackground());
        mMain.setAlpha(0.2f);
        String dateToShow = (String) DateFormat.format("MMM", AppConstants.mDate) + " " + (String) DateFormat.format("dd", AppConstants.mDate) + "," + (String) DateFormat.format("yyyy", AppConstants.mDate);
        mDateTv.setText(dateToShow);
        String tc = AppConstants.mClass.getmAbbreviation() + " - " + AppConstants.mClass.getmFullForm();
        mTravelClass.setText(tc);
    }

    public void trainListFetchComplete() {
        mMain.setAlpha(1.0f);
        mProgress.setVisibility(View.GONE);
        mAdapter.notifyDataSetChanged();
        for (int i = 0; i < AppConstants.mTrainList.size(); i++) {
            ArrayList<String> route = AppConstants.mTrainList.get(i).getmCodedRoutes();
            int pos = 0;
            for (int j = 0; j < route.size(); j++) {
                if (route.get(i).trim().equalsIgnoreCase(AppConstants.mSourceStation.getmStationCode().toString())) {
                    pos = j;
                    break;
                }
            }
            String dayNo = AppConstants.mTrainList.get(i).getmDepartureTime().get(pos);
            int x = 0;
            for (int j = 0; j < dayNo.length(); j++) {
                try {
                    x = (int) dayNo.charAt(j) - 48;
                    break;
                } catch (Exception e) {
                    continue;
                }
            }
            x = x - 1;
            Date date = AppConstants.mDate;
            String doj = (String) DateFormat.format("yyyy", date) + "-" + (String) DateFormat.format("MM", date) + "-" + (String) DateFormat.format("dd", date);
            String temp = AppConstants.mUrl + "/seats/" + AppConstants.mTrainList.get(i).getmTrainNo().trim() + "/" + AppConstants.mSourceStation.getmStationCode().trim() + "/" + AppConstants.mDestinationStation.getmStationCode() + "/" + AppConstants.mClass.getmAbbreviation().trim() + "/" + doj + "/" + AppConstants.mQuota.getmAbbreviation().trim();
            networkRequests.fetchSeatsData(AppConstants.mTrainList.get(i).getmTrainNo().trim(), temp);
            String temp1 = AppConstants.mUrl + "/fareenquiry" + "/" + AppConstants.mTrainList.get(i).getmTrainNo().trim() + "/" + AppConstants.mSourceStation + "/" + AppConstants.mDestinationStation;
            networkRequests.fetchFareData(AppConstants.mTrainList.get(i).getmTrainNo().trim(), temp1);
        }
    }

    private void setUpDateAlert() {
        mCalendar.setTime(AppConstants.mDate);
        DatePickerDialog dialog = new DatePickerDialog(this, mDate,
                mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void setUpClassAlert() {
        final SpinnerModel spinnerModel = AppConstants.mClass;
        Rect displayRectangle = new Rect();
        Window window = SeatAvailabilityActivity.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.class_dialog, viewGroup, false);

        dialogView.findViewById(R.id.heading).setBackground(GenerateBackground.generateBackground());

        AlertDialog.Builder builder = new AlertDialog.Builder(SeatAvailabilityActivity.this, R.style.CustomAlertDialog);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.setCancelable(true);
        alertDialog.show();

        final RadioButton a1 = dialogView.findViewById(R.id.ac1);
        final RadioButton ec = dialogView.findViewById(R.id.ec);
        final RadioButton a2 = dialogView.findViewById(R.id.ac2);
        final RadioButton fc = dialogView.findViewById(R.id.fc);
        final RadioButton a3 = dialogView.findViewById(R.id.ac3);
        final RadioButton e3 = dialogView.findViewById(R.id.e3);
        final RadioButton cc = dialogView.findViewById(R.id.cc);
        final RadioButton sl = dialogView.findViewById(R.id.sl);
        final RadioButton s2 = dialogView.findViewById(R.id.s2);
        final RadioButton gn = dialogView.findViewById(R.id.gen);

        TextView cancel = dialogView.findViewById(R.id.cancel_tv);
        cancel.setBackground(GenerateBackground.generateBackground());

        switch (AppConstants.mClass.getmAbbreviation()) {
            case "1A":
                a1.setChecked(true);
                lastChecked = a1;
                break;
            case "EC":
                ec.setChecked(true);
                lastChecked = ec;
                break;
            case "2A":
                a2.setChecked(true);
                lastChecked = a2;
                break;
            case "FC":
                fc.setChecked(true);
                lastChecked = fc;
                break;
            case "3A":
                a3.setChecked(true);
                lastChecked = a3;
                break;
            case "3E":
                e3.setChecked(true);
                lastChecked = e3;
                break;
            case "CC":
                cc.setChecked(true);
                lastChecked = cc;
                break;
            case "SL":
                sl.setChecked(true);
                lastChecked = sl;
                break;
            case "2S":
                s2.setChecked(true);
                lastChecked = s2;
                break;
            case "GN":
                gn.setChecked(true);
                lastChecked = gn;
                break;
        }

        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.mClass.setmAbbreviation("1A");
                AppConstants.mClass.setmFullForm("First AC");
                lastChecked.setChecked(false);
                a1.setChecked(true);
                if (lastChecked != a1)
                    update();
                lastChecked = a1;
                alertDialog.hide();
            }
        });

        ec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.mClass.setmAbbreviation("EC");
                AppConstants.mClass.setmFullForm("AC Executive Class");
                lastChecked.setChecked(false);
                ec.setChecked(true);
                if (lastChecked != ec)
                    update();
                lastChecked = ec;
                alertDialog.hide();
            }
        });

        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.mClass.setmAbbreviation("2A");
                AppConstants.mClass.setmFullForm("Second AC");
                lastChecked.setChecked(false);
                a2.setChecked(true);
                update();
                if (lastChecked != a2)
                    lastChecked = a2;
                alertDialog.hide();
            }
        });

        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.mClass.setmAbbreviation("FC");
                AppConstants.mClass.setmFullForm("First Class");
                lastChecked.setChecked(false);
                fc.setChecked(true);
                update();
                if (lastChecked != fc)
                    lastChecked = fc;
                alertDialog.hide();
            }
        });

        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.mClass.setmAbbreviation("3A");
                AppConstants.mClass.setmFullForm("Third AC");
                lastChecked.setChecked(false);
                a3.setChecked(true);
                if (lastChecked != a3)
                    update();
                lastChecked = a3;
                alertDialog.hide();
            }
        });

        e3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.mClass.setmAbbreviation("3E");
                AppConstants.mClass.setmFullForm("Third AC Economy");
                lastChecked.setChecked(false);
                e3.setChecked(true);
                if (lastChecked != e3)
                    update();
                lastChecked = e3;
                alertDialog.hide();
            }
        });

        cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.mClass.setmAbbreviation("CC");
                AppConstants.mClass.setmFullForm("AC Chair Car");
                lastChecked.setChecked(false);
                cc.setChecked(true);
                if (lastChecked != cc)
                    update();
                lastChecked = cc;
                alertDialog.hide();
            }
        });

        sl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.mClass.setmAbbreviation("SL");
                AppConstants.mClass.setmFullForm("Sleeper");
                lastChecked.setChecked(false);
                sl.setChecked(true);
                if (lastChecked != sl)
                    update();
                lastChecked = sl;
                alertDialog.hide();
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.mClass.setmAbbreviation("2S");
                AppConstants.mClass.setmFullForm("Second Seating");
                lastChecked.setChecked(false);
                s2.setChecked(true);
                if (lastChecked != s2)
                    update();
                lastChecked = s2;
                alertDialog.hide();
            }
        });

        gn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.mClass.setmAbbreviation("GN");
                AppConstants.mClass.setmFullForm("General");
                lastChecked.setChecked(false);
                gn.setChecked(true);
                if (lastChecked != gn)
                    update();
                lastChecked = gn;
                alertDialog.hide();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.hide();
            }
        });
    }

    private void update() {
        String day = (String) DateFormat.format("EE", AppConstants.mDate);
        String tc = AppConstants.mClass.getmAbbreviation() + " - " + AppConstants.mClass.getmFullForm();
        mTravelClass.setText(tc);
        String url = AppConstants.mUrl + "/trains/" + AppConstants.mSourceStation.getmStationCode() + "/" + AppConstants.mDestinationStation.getmStationCode() + "/" + day + "/" + AppConstants.mClass.getmAbbreviation().trim();
        mProgress.setVisibility(View.VISIBLE);
        mMain.setAlpha(0.2f);
        AppConstants.mTrainList.clear();
        mList = null;
        mList = findViewById(R.id.trains_list);
        mAdapter = null;
        mAdapter = new TrainAdapter(SeatAvailabilityActivity.this, AppConstants.mTrainList);
        mList.setAdapter(mAdapter);
        networkRequests.fetchTrainsFromUrl(url);
    }

    private void setDate(Date date) {
        boolean z = true;
        if (!date.equals(AppConstants.mDate))
            z = false;
        AppConstants.mDate = date;
        String dateToShow = (String) DateFormat.format("MMM", AppConstants.mDate) + " " + (String) DateFormat.format("dd", AppConstants.mDate) + "," + (String) DateFormat.format("yyyy", AppConstants.mDate);
        mDateTv.setText(dateToShow);
        if (!z) {
            String day = (String) DateFormat.format("EE", date);
            String url = AppConstants.mUrl + "/trains/" + AppConstants.mSourceStation.getmStationCode() + "/" + AppConstants.mDestinationStation.getmStationCode() + "/" + day + "/" + AppConstants.mClass.getmAbbreviation().trim();
            mProgress.setVisibility(View.VISIBLE);
            mMain.setAlpha(0.2f);
            AppConstants.mTrainList.clear();
            mList = null;
            mList = findViewById(R.id.trains_list);
            mAdapter = null;
            mAdapter = new TrainAdapter(SeatAvailabilityActivity.this, AppConstants.mTrainList);
            mList.setAdapter(mAdapter);
            networkRequests.fetchTrainsFromUrl(url);
        }
    }

    private void updateJourneyDate() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date date = null;
        try {
            date = sdf.parse(sdf.format(mCalendar.getTime()));
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getLocalizedMessage());
        }
        setDate(date);
    }
}
