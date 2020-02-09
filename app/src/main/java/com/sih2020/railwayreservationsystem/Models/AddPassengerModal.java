package com.sih2020.railwayreservationsystem.Models;

public class AddPassengerModal {
    public String mName,mAge,mGender,mBerth,mNationality;

    public AddPassengerModal(String mName, String mAge, String mGender, String mBerth, String mNationality) {
        this.mName = mName;
        this.mAge = mAge;
        this.mGender = mGender;
        this.mBerth = mBerth;
        this.mNationality = mNationality;
    }

    public void setmAge(String mAge) {
        this.mAge = mAge;
    }

    public void setmGender(String mGender) {
        this.mGender = mGender;
    }

    public void setmBerth(String mBerth) {
        this.mBerth = mBerth;
    }

    public void setmNationality(String mNationality) {
        this.mNationality = mNationality;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmName() {
        return mName;
    }

    public String getmAge() {
        return mAge;
    }

    public String getmGender() {
        return mGender;
    }

    public String getmBerth() {
        return mBerth;
    }

    public String getmNationality() {
        return mNationality;
    }
}
