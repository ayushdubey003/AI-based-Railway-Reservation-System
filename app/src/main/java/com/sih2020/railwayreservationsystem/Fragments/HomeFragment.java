package com.sih2020.railwayreservationsystem.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;

public class HomeFragment extends Fragment {

    private ImageView mSourceCircle, mDestinationCircle, mClearSource, mClearDestination;
    private CardView mSourceCodeCard, mDestinationCodeCard;
    private TextView mSourceCodeTv, mDestinationCodeTv;
    private EditText mSourceEt, mDestinationEt;
    private String mSource, mDestination;
    private View mVerticalLine;

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
    }

    private void init(View view) {
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
                layoutParams.width = (int) AppConstants.convertDpToPixel(1.0f, getContext());
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
        if(height == 0)
            valueAnimator = animateView(view,700,0,(int)AppConstants.convertDpToPixel(53,getContext()));
        else
            valueAnimator = animateView(view,700,(int)AppConstants.convertDpToPixel(53,getContext()),0);
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
                        animateViewRecursiveCall(view,700);
                    }
                },500);
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
}
