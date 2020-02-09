package com.sih2020.railwayreservationsystem.Models;

import android.util.Pair;

public class Station {
    private String mStationName, mStationCode;
    private Pair<String, String> mCoordinates;
    private String mPinCode;

    public Station(String mStationName, String mStationCode, Pair<String, String> mCoordinates, String mPinCode) {
        this.mStationName = mStationName;
        this.mStationCode = mStationCode;
        this.mCoordinates = mCoordinates;
        this.mPinCode = mPinCode;
    }

    public String getmStationName() {
        return mStationName;
    }

    public String getmStationCode() {
        return mStationCode;
    }

    public Pair<String, String> getmCoordinates() {
        return mCoordinates;
    }

    public String getmPinCode() {
        return mPinCode;
    }

    public void setmPinCode(String mPinCode) {
        this.mPinCode = mPinCode;
    }
}
