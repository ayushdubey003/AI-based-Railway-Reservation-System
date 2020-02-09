package com.sih2020.railwayreservationsystem.Utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.sih2020.railwayreservationsystem.Activities.AutomatedTatkal;
import com.sih2020.railwayreservationsystem.Adapters.AddPassengerListAdapter;
import com.sih2020.railwayreservationsystem.Fragments.AddPassengerFragment;
import com.sih2020.railwayreservationsystem.Models.AddPassengerModal;
import com.sih2020.railwayreservationsystem.Models.SpinnerModel;
import com.sih2020.railwayreservationsystem.Models.Station;
import com.sih2020.railwayreservationsystem.Models.Train;
import com.sih2020.railwayreservationsystem.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AppConstants {
    public static boolean mFlag = true;
    public static String mUrl;
    public static String mPrefsName = "SharedPrefs";
    public static String mDataGiven = "UrlKnown";
    public static String mUrlSaved = "Url";
    public static String mStationsListVersionNo = "VersionNo";
    public static ArrayList<Station> mStationsName = new ArrayList<>();
    public static Station mSourceStation;
    public static Station mDestinationStation;
    public static Station mLiveStation;
    public static Station mLiveStationOptional = null;
    public static Station mTempStation;
    public static boolean mDataFetchCalled = false;
    public static ArrayList<SpinnerModel> mTravelClasses = new ArrayList<>();
    public static ArrayList<SpinnerModel> mTravelQuotas = new ArrayList<>();
    public static SpinnerModel mQuota;
    public static SpinnerModel mClass;
    public static Date mDate;
    public static boolean mUseDate = true;
    public static ArrayList<Train> mTrainList = new ArrayList<>();
    public static String[] mNationality = {"Indian", "Others"};
    public static String[] mOccupation = {"NA", "Govt-employee", "Farmer", "Self-employed", "Unemployed"};
    public static String[] mMaritalStatus = {"NA", "Single", "Married"};
    public static ArrayList<Pair<String, String>> mTrainListAtStartTime = new ArrayList<>();
    public static Pair<String, String> mSpotTrainPair = new Pair<String, String>(null, null);
    public static boolean mBottomSheetOpen = false;
    public static ScrollView mScrollView;
    public static ArrayList<Integer> mFareFetch = new ArrayList<>(), mSeatFetch = new ArrayList<>();
    public static ArrayList<AddPassengerModal> mAddPassengerList=new ArrayList<>();
    public static AddPassengerListAdapter mAddPassengerListGlobalAdapter;

    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static void showBottomSheet(AutomatedTatkal automatedTatkal) {
        AddPassengerFragment bottomSheet = new AddPassengerFragment(automatedTatkal);
        FragmentTransaction transaction = automatedTatkal.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.no_anim);
        transaction.add(R.id.fragment_layout, bottomSheet);
        transaction.commit();
        mBottomSheetOpen = true;
    }

    public static void hideBottomSheet(FragmentActivity activity) {
        Fragment f = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_layout);
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.no_anim, R.anim.slide_out_bottom);
        transaction.remove(f).commit();
        mBottomSheetOpen = false;
        mScrollView.setAlpha(1.0f);
    }
}
