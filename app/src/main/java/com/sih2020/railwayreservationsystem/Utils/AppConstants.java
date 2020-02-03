package com.sih2020.railwayreservationsystem.Utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Pair;

import com.sih2020.railwayreservationsystem.Models.SpinnerModel;
import com.sih2020.railwayreservationsystem.Models.Station;
import com.sih2020.railwayreservationsystem.Models.Train;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AppConstants {
    public static String mUrl;
    public static String mPrefsName = "SharedPrefs";
    public static String mDataGiven = "UrlKnown";
    public static String mUrlSaved = "Url";
    public static String mStationsListVersionNo = "VersionNo";
    public static ArrayList<Station> mStationsName = new ArrayList<>();
    public static Station mSourceStation;
    public static Station mDestinationStation;
    public static Station mTempStation;
    public static boolean mDataFetchCalled = false;
    public static ArrayList<SpinnerModel> mTravelClasses = new ArrayList<>();
    public static ArrayList<SpinnerModel> mTravelQuotas = new ArrayList<>();
    public static SpinnerModel mQuota;
    public static SpinnerModel mClass;
    public static Date mDate;
    public static boolean mUseDate = true;
    public static ArrayList<Train> mTrainList = new ArrayList<>();

    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
