package com.sih2020.railwayreservationsystem.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.sih2020.railwayreservationsystem.Activities.SearchStations;
import com.sih2020.railwayreservationsystem.Activities.LoginActivity;
import com.sih2020.railwayreservationsystem.Activities.SearchTrains;
import com.sih2020.railwayreservationsystem.Activities.SeatAvailabilityActivity;
import com.sih2020.railwayreservationsystem.Adapters.SpinnerAdapter;
import com.sih2020.railwayreservationsystem.Models.SpinnerModel;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private ImageView mSourceCircle, mDestinationCircle, mClearSource, mClearDestination, mReverseIv;
    private CardView mSourceCodeCard, mDestinationCodeCard;
    private TextView mSourceCodeTv, mDestinationCodeTv, mDateTv, mMonthTv, mDayTvAbbreviated, mDayTv;
    private EditText mSourceEt, mDestinationEt;
    private String mSource, mDestination;
    private View mVerticalLine;
    private FrameLayout mRl;
    private RelativeLayout mDateRl;
    private static final String LOG_TAG = "Home Fragment";
    private DatePickerDialog.OnDateSetListener mDate;
    private Calendar mCalendar;
    private CheckBox mDateFlexible;
    private String mDayOfTheWeek, mDay, mMonth, mDateNum;
    private Spinner mClassSpinner, mQuotaSpinner;
    private SpinnerAdapter mClassAdapter, mQuotaAdapter;
    private LinearLayout mSearchTrains;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        receiveClicks();
    }

    private void receiveClicks() {
        mRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(500);
                rotate.setInterpolator(new LinearInterpolator());
                mReverseIv.startAnimation(rotate);
                rotate.setFillAfter(true);
                AppConstants.mTempStation = AppConstants.mSourceStation;
                AppConstants.mSourceStation = AppConstants.mDestinationStation;
                AppConstants.mDestinationStation = AppConstants.mTempStation;

                try {
                    mSourceEt.setText(AppConstants.mSourceStation.getmStationName());
                    mSourceCodeTv.setText(AppConstants.mSourceStation.getmStationCode());
                    mSourceCodeCard.setVisibility(View.VISIBLE);
                    mClearSource.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    mSourceEt.setText("");
                    mSourceCodeCard.setVisibility(View.GONE);
                    mClearSource.setVisibility(View.GONE);
                    Log.e(LOG_TAG, e.getLocalizedMessage());
                }

                try {
                    mDestinationEt.setText(AppConstants.mDestinationStation.getmStationName());
                    mDestinationCodeTv.setText(AppConstants.mDestinationStation.getmStationCode());
                    mDestinationCodeCard.setVisibility(View.VISIBLE);
                    mClearDestination.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    mDestinationEt.setText("");
                    mDestinationCodeCard.setVisibility(View.GONE);
                    mClearDestination.setVisibility(View.GONE);
                    Log.e(LOG_TAG, e.getLocalizedMessage());
                }
            }
        });

        mSourceEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchStations.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });

        mDestinationEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchStations.class);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });

        mClearSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.mSourceStation = null;
                mSourceEt.setText("");
                mClearSource.setVisibility(View.GONE);
                mSourceCodeCard.setVisibility(View.GONE);
            }
        });

        mClearDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.mDestinationStation = null;
                mDestinationEt.setText("");
                mClearDestination.setVisibility(View.GONE);
                mDestinationCodeCard.setVisibility(View.GONE);
            }
        });

        mDateRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(getContext(), mDate,
                        mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        mDateFlexible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mDateRl.setAlpha(0.2f);
                    mDateRl.setClickable(false);
                    AppConstants.mUseDate = false;
                } else {
                    mDateRl.setAlpha(1.0f);
                    mDateRl.setClickable(true);
                    AppConstants.mUseDate = true;
                }
            }
        });

        mClassSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppConstants.mClass = AppConstants.mTravelClasses.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mQuotaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppConstants.mQuota = AppConstants.mTravelQuotas.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSearchTrains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url = "";
                    url = AppConstants.mUrl + "/trains/" + AppConstants.mSourceStation.getmStationCode() + "/" + AppConstants.mDestinationStation.getmStationCode() + "/" + mDay + "/" + AppConstants.mClass.getmAbbreviation().trim();
                    if (AppConstants.mSourceStation.getmStationCode().equalsIgnoreCase(AppConstants.mDestinationStation.getmStationCode())) {
                        Snackbar.make(getView(), "Source and destination station cannot be the same", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    Intent intent = new Intent(getActivity(), SeatAvailabilityActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                } catch (Exception e) {
                    Snackbar.make(getView(), "One or more fields appear to be empty", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void init(final View view) {
        AppConstants.mTravelClasses.clear();
        AppConstants.mTravelClasses.add(new SpinnerModel("1A", "First AC"));
        AppConstants.mTravelClasses.add(new SpinnerModel("EC", "AC Executive Class"));
        AppConstants.mTravelClasses.add(new SpinnerModel("2A", "Second AC"));
        AppConstants.mTravelClasses.add(new SpinnerModel("FC", "First Class"));
        AppConstants.mTravelClasses.add(new SpinnerModel("3A", "Third AC"));
        AppConstants.mTravelClasses.add(new SpinnerModel("3E", "Third AC Economy"));
        AppConstants.mTravelClasses.add(new SpinnerModel("CC", "AC Chair Car"));
        AppConstants.mTravelClasses.add(new SpinnerModel("SL", "Sleeper"));
        AppConstants.mTravelClasses.add(new SpinnerModel("2S", "Second Seating"));
        AppConstants.mTravelClasses.add(new SpinnerModel("GN", "General"));

        AppConstants.mTravelQuotas.clear();
        AppConstants.mTravelQuotas.add(new SpinnerModel("GN", "General Quota"));
        AppConstants.mTravelQuotas.add(new SpinnerModel("TQ", "Tatkal"));
        AppConstants.mTravelQuotas.add(new SpinnerModel("PT", "Premium Tatkal"));
        AppConstants.mTravelQuotas.add(new SpinnerModel("SS", "Lower Berth"));
        AppConstants.mTravelQuotas.add(new SpinnerModel("LD", "Ladies"));
        AppConstants.mTravelQuotas.add(new SpinnerModel("HP", "Physically Handicapped"));
        AppConstants.mTravelQuotas.add(new SpinnerModel("DP", "Duty Pass Quota"));
        AppConstants.mTravelQuotas.add(new SpinnerModel("DF", "Defence Quota"));

        mSourceCircle = view.findViewById(R.id.source_circle);
        mDestinationCircle = view.findViewById(R.id.destination_circle);
        mClearSource = view.findViewById(R.id.clear_iv_source);
        mClearDestination = view.findViewById(R.id.clear_iv_destination);
        mSourceCodeCard = view.findViewById(R.id.source_cv);
        mDestinationCodeCard = view.findViewById(R.id.destination_cv);
        mSourceCodeTv = view.findViewById(R.id.source_tv);
        mDestinationCodeTv = view.findViewById(R.id.destination_tv);
        mSourceEt = view.findViewById(R.id.source_et);
        mDestinationEt = view.findViewById(R.id.destination_et);
        mVerticalLine = view.findViewById(R.id.vertical_line);
        mRl = view.findViewById(R.id.reverse_rl);
        mReverseIv = view.findViewById(R.id.reverse_iv);
        mDateRl = view.findViewById(R.id.date_rl);
        mDateTv = view.findViewById(R.id.date_tv);
        mMonthTv = view.findViewById(R.id.month_tv);
        mDayTv = view.findViewById(R.id.day_big_tv);
        mDayTvAbbreviated = view.findViewById(R.id.day_tv);
        mDateFlexible = view.findViewById(R.id.date_flexible);
        mClassSpinner = view.findViewById(R.id.class_spinner);
        mQuotaSpinner = view.findViewById(R.id.quota_spinner);
        mSearchTrains = view.findViewById(R.id.search_trains);

        mClassAdapter = new SpinnerAdapter(getActivity(), getContext(), AppConstants.mTravelClasses);
        mClassSpinner.setAdapter(mClassAdapter);
        mQuotaAdapter = new SpinnerAdapter(getActivity(), getContext(), AppConstants.mTravelQuotas);
        mQuotaSpinner.setAdapter(mQuotaAdapter);
        mCalendar = Calendar.getInstance();
        AppConstants.mClass = AppConstants.mTravelClasses.get(0);
        AppConstants.mQuota = AppConstants.mTravelQuotas.get(0);
        getCurrentDate();

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

        mSourceCircle.setColorFilter(Color.parseColor("#43A047"));
        mDestinationCircle.setColorFilter(Color.parseColor("#ff0000"));
        animateViewRecursiveCall(mVerticalLine, 700);
    }

    private void getCurrentDate() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date date = mCalendar.getTime();
        try {
            date = sdf.parse(sdf.format(mCalendar.getTime()));
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getLocalizedMessage());
        }
        setDate(date);
    }

    private void setDate(Date date) {
        mDayOfTheWeek = (String) DateFormat.format("EEEE", date);
        mDateNum = (String) DateFormat.format("dd", date);
        mMonth = (String) DateFormat.format("MMMM", date);
        mDay = (String) DateFormat.format("EE", date);

        mDayTvAbbreviated.setText(mDay);
        mDayTv.setText(mDayOfTheWeek);
        mMonthTv.setText(mMonth);
        mDateTv.setText(mDateNum);
        AppConstants.mDate = date;
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

    private ValueAnimator animateView(final View view, long duration, int initialHeight, int finalHeight) {
        RelativeLayout.LayoutParams lP = (RelativeLayout.LayoutParams) view.getLayoutParams();
        ValueAnimator anim = ValueAnimator.ofInt(initialHeight, finalHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                int u = 0;
                try {
                    u = (int) AppConstants.convertDpToPixel(30, getContext());
                } catch (Exception e) {
                    return;
                }
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                layoutParams.height = val;

                if (val < u)
                    mVerticalLine.setBackgroundColor(Color.parseColor("#43A047"));
                else
                    mVerticalLine.setBackgroundColor(Color.parseColor("#ff0000"));

                try {
                    layoutParams.width = (int) AppConstants.convertDpToPixel(1.0f, getContext());
                } catch (Exception e) {
                    return;
                }
                view.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(duration);
        anim.setInterpolator(new LinearInterpolator());
        return anim;
    }

    private void animateViewRecursiveCall(final View view, long duration) {
        RelativeLayout.LayoutParams lP = (RelativeLayout.LayoutParams) view.getLayoutParams();
        int height = lP.height;
        ValueAnimator valueAnimator;
        if (height == 0)
            try {
                valueAnimator = animateView(view, 700, 0, (int) AppConstants.convertDpToPixel(45, getContext()));
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getLocalizedMessage());
                return;
            }
        else {
            try {
                valueAnimator = animateView(view, 700, (int) AppConstants.convertDpToPixel(45, getContext()), 0);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getLocalizedMessage());
                return;
            }
        }

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animateViewRecursiveCall(view, 700);
                    }
                }, 500);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationPause(Animator animation) {
                super.onAnimationPause(animation);
            }

            @Override
            public void onAnimationResume(Animator animation) {
                super.onAnimationResume(animation);
            }
        });
        valueAnimator.start();
    }

    private void animateWhileReducingMargin(View view, int i) {

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mSourceEt.setText(AppConstants.mSourceStation.getmStationName());
            mClearSource.setVisibility(View.VISIBLE);
            mSourceCodeTv.setText(AppConstants.mSourceStation.getmStationCode());
            mSourceCodeCard.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getLocalizedMessage());
        }

        try {
            mDestinationEt.setText(AppConstants.mDestinationStation.getmStationName());
            mClearDestination.setVisibility(View.VISIBLE);
            mDestinationCodeTv.setText(AppConstants.mDestinationStation.getmStationCode());
            mDestinationCodeCard.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getLocalizedMessage());
        }

        try {
            AppConstants.mTrainList.clear();
//            AppConstants.mTrainWiseSeatAvailability.clear();
        } catch (Exception e) {
            //do nothing
        }
        try {
            AppConstants.mTravelClasses.clear();
            AppConstants.mTravelClasses.add(new SpinnerModel("1A", "First AC"));
            AppConstants.mTravelClasses.add(new SpinnerModel("EC", "AC Executive Class"));
            AppConstants.mTravelClasses.add(new SpinnerModel("2A", "Second AC"));
            AppConstants.mTravelClasses.add(new SpinnerModel("FC", "First Class"));
            AppConstants.mTravelClasses.add(new SpinnerModel("3A", "Third AC"));
            AppConstants.mTravelClasses.add(new SpinnerModel("3E", "Third AC Economy"));
            AppConstants.mTravelClasses.add(new SpinnerModel("CC", "AC Chair Car"));
            AppConstants.mTravelClasses.add(new SpinnerModel("SL", "Sleeper"));
            AppConstants.mTravelClasses.add(new SpinnerModel("2S", "Second Seating"));
            AppConstants.mTravelClasses.add(new SpinnerModel("GN", "General"));
        } catch (Exception e) {
            //do nothing
        }
        try {
            AppConstants.mTravelQuotas.clear();
            AppConstants.mTravelQuotas.add(new SpinnerModel("GN", "General Quota"));
            AppConstants.mTravelQuotas.add(new SpinnerModel("TQ", "Tatkal"));
            AppConstants.mTravelQuotas.add(new SpinnerModel("PT", "Premium Tatkal"));
            AppConstants.mTravelQuotas.add(new SpinnerModel("SS", "Lower Berth"));
            AppConstants.mTravelQuotas.add(new SpinnerModel("LD", "Ladies"));
            AppConstants.mTravelQuotas.add(new SpinnerModel("HP", "Physically Handicapped"));
            AppConstants.mTravelQuotas.add(new SpinnerModel("DP", "Duty Pass Quota"));
            AppConstants.mTravelQuotas.add(new SpinnerModel("DF", "Defence Quota"));
        } catch (Exception e) {
            //do nothing
        }
    }
}
