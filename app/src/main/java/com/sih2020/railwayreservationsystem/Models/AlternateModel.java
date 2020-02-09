package com.sih2020.railwayreservationsystem.Models;

import java.util.ArrayList;
import java.util.HashMap;

public class AlternateModel {

    int n;
    ArrayList<String> mStations;
    ArrayList<String> mTrains;
    ArrayList<String> mDepartureTime,mDepartureDate;
    String mTime;
    HashMap<String, String> mConfirmation, mSeatstatus, mFares;
    String mTicketclass;
    String dayofjourney;
    String dateofjourney;

    public AlternateModel() {
        mStations = new ArrayList<>();
        mConfirmation = new HashMap<>();
        mFares = new HashMap<>();
        mSeatstatus = new HashMap<>();
        mTrains = new ArrayList<>();
        mDepartureTime = new ArrayList<>();
        mDepartureDate=new ArrayList<>();
    }

    public void setmDepartureTime(ArrayList<String> mDepartureTime) {
        this.mDepartureTime = mDepartureTime;
    }

    public void setmDepartureDate(ArrayList<String> mDepartureDate) {
        this.mDepartureDate = mDepartureDate;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setmStations(ArrayList<String> mStations) {
        this.mStations = mStations;
    }

    public void setmTrains(ArrayList<String> mTrains) {
        this.mTrains = mTrains;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }


    public void setmTicketclass(String mTicketclass) {
        this.mTicketclass = mTicketclass;
    }

    public void setDayofjourney(String dayofjourney) {
        this.dayofjourney = dayofjourney;
    }

    public void setDateofjourney(String dateofjourney) {
        this.dateofjourney = dateofjourney;
    }

    public int getN() {
        return n;
    }

    public ArrayList<String> getmStations() {
        return mStations;
    }

    public ArrayList<String> getmTrains() {
        return mTrains;
    }

    public String getmTime() {
        return mTime;
    }

    public HashMap<String, String> getmConfirmation() {
        return mConfirmation;
    }

    public void setmConfirmation(HashMap<String, String> mConfirmation) {
        this.mConfirmation = mConfirmation;
    }

    public HashMap<String, String> getmSeatstatus() {
        return mSeatstatus;
    }

    public void setmSeatstatus(HashMap<String, String> mSeatstatus) {
        this.mSeatstatus = mSeatstatus;
    }

    public HashMap<String, String> getmFares() {
        return mFares;
    }

    public void setmFares(HashMap<String, String> mFares) {
        this.mFares = mFares;
    }

    public String getmTicketclass() {
        return mTicketclass;
    }

    public ArrayList<String> getmDepartureTime() {
        return mDepartureTime;
    }

    public ArrayList<String> getmDepartureDate() {
        return mDepartureDate;
    }

    public String getDayofjourney() {
        return dayofjourney;
    }

    public String getDateofjourney() {
        return dateofjourney;
    }
}
