package com.sih2020.railwayreservationsystem.Models;

public class SpinnerModel {
    private String mAbbreviation, mFullForm;

    public SpinnerModel(String mAbbreviation, String mFullForm) {
        this.mAbbreviation = mAbbreviation;
        this.mFullForm = mFullForm;
    }

    public String getmAbbreviation() {
        return mAbbreviation;
    }

    public String getmFullForm() {
        return mFullForm;
    }
}
