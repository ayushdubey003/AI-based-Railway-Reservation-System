package com.sih2020.railwayreservationsystem.Utils;

import android.content.Context;
import android.util.DisplayMetrics;

import com.sih2020.railwayreservationsystem.Models.Station;

import java.util.ArrayList;

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

    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
