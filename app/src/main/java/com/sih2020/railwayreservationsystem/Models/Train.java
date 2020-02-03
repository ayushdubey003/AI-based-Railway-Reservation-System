package com.sih2020.railwayreservationsystem.Models;

import java.util.ArrayList;

public class Train {
    private String mTrainName, mTrainNo, mType, mZone;
    private ArrayList<String> mArrivalTimes, mAvailableClasses, mCancelled, mDepartureTime, mRightTime, mCodedRoutes, mNamedRoutes, mRunningDays, mSignificantDelay, mSlightDelay, mSeats;


    public Train(String mTrainName, String mTrainNo, String mType, String mZone, ArrayList<String> mArrivalTimes, ArrayList<String> mAvailableClasses, ArrayList<String> mCancelled, ArrayList<String> mDepartureTime, ArrayList<String> mRightTime, ArrayList<String> mCodedRoutes, ArrayList<String> mNamedRoutes, ArrayList<String> mRunningDays, ArrayList<String> mSignificantDelay, ArrayList<String> mSlightDelay, ArrayList<String> mSeats) {
        this.mTrainName = mTrainName;
        this.mTrainNo = mTrainNo;
        this.mType = mType;
        this.mZone = mZone;
        this.mArrivalTimes = mArrivalTimes;
        this.mAvailableClasses = mAvailableClasses;
        this.mCancelled = mCancelled;
        this.mDepartureTime = mDepartureTime;
        this.mRightTime = mRightTime;
        this.mCodedRoutes = mCodedRoutes;
        this.mNamedRoutes = mNamedRoutes;
        this.mRunningDays = mRunningDays;
        this.mSignificantDelay = mSignificantDelay;
        this.mSlightDelay = mSlightDelay;
        this.mSeats = mSeats;
    }

    public String getmTrainName() {
        return mTrainName;
    }

    public String getmTrainNo() {
        return mTrainNo;
    }

    public String getmType() {
        return mType;
    }

    public String getmZone() {
        return mZone;
    }

    public ArrayList<String> getmArrivalTimes() {
        return mArrivalTimes;
    }

    public ArrayList<String> getmAvailableClasses() {
        return mAvailableClasses;
    }

    public ArrayList<String> getmCancelled() {
        return mCancelled;
    }

    public ArrayList<String> getmDepartureTime() {
        return mDepartureTime;
    }

    public ArrayList<String> getmRightTime() {
        return mRightTime;
    }

    public ArrayList<String> getmCodedRoutes() {
        return mCodedRoutes;
    }

    public ArrayList<String> getmNamedRoutes() {
        return mNamedRoutes;
    }

    public ArrayList<String> getmRunningDays() {
        return mRunningDays;
    }

    public ArrayList<String> getmSignificantDelay() {
        return mSignificantDelay;
    }

    public ArrayList<String> getmSlightDelay() {
        return mSlightDelay;
    }

    public ArrayList<String> getmSeats() {
        return mSeats;
    }

    public void setmSeats(ArrayList<String> mSeats) {
        this.mSeats = mSeats;
    }
}
