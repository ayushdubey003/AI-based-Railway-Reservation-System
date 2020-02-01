package com.sih2020.railwayreservationsystem.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sih2020.railwayreservationsystem.Activities.SearchTrains;
import com.sih2020.railwayreservationsystem.Models.Station;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ImageView mSourceCircle, mDestinationCircle, mClearSource, mClearDestination, mReverseIv;
    private CardView mSourceCodeCard, mDestinationCodeCard;
    private TextView mSourceCodeTv, mDestinationCodeTv;
    private EditText mSourceEt, mDestinationEt;
    private String mSource, mDestination;
    private View mVerticalLine;
    private RelativeLayout mRl;
    private static final String LOG_TAG = "Home Fragment";


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
                Intent intent = new Intent(getActivity(), SearchTrains.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });

        mDestinationEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchTrains.class);
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
    }

    private void init(final View view) {
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

        mSourceCircle.setColorFilter(Color.parseColor("#43A047"));
        mDestinationCircle.setColorFilter(Color.parseColor("#ff0000"));
        animateViewRecursiveCall(mVerticalLine, 700);
    }

    private ValueAnimator animateView(final View view, long duration, int initialHeight, int finalHeight) {
        FrameLayout.LayoutParams lP = (FrameLayout.LayoutParams) view.getLayoutParams();
        ValueAnimator anim = ValueAnimator.ofInt(initialHeight, finalHeight);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                    layoutParams.height = val;
                    try {
                        layoutParams.width = (int) AppConstants.convertDpToPixel(1.0f, getContext());
                    }
                    catch (Exception e)
                    {
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
        FrameLayout.LayoutParams lP = (FrameLayout.LayoutParams) view.getLayoutParams();
        int height = lP.height;
        ValueAnimator valueAnimator;
        if (height == 0)
            try {
                valueAnimator = animateView(view, 700, 0, (int) AppConstants.convertDpToPixel(55, getContext()));
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getLocalizedMessage());
                return;
            }
        else {
            try {
                valueAnimator = animateView(view, 700, (int) AppConstants.convertDpToPixel(55, getContext()), 0);
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
    }
}
