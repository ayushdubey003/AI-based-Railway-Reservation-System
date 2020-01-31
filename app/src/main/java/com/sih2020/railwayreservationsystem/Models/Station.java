package com.sih2020.railwayreservationsystem.Models;

import android.util.Pair;

public class Station {
    private String mStationName, mStationCode;
    private Pair<String, String> mCoordinates;

    public Station(String mStationName, String mStationCode, Pair<String, String> mCoordinates) {
        this.mStationName = mStationName;
        this.mStationCode = mStationCode;
        this.mCoordinates = mCoordinates;
    }

    public String getmStationName() {
        return mStationName;
    }

    public String getmStationCoordinates() {
        return mStationCode;
    }

    public Pair<String, String> getmCoordinates() {
        return mCoordinates;
    }
}
