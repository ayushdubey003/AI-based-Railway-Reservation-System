package com.sih2020.railwayreservationsystem.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sih2020.railwayreservationsystem.Fragments.HomeFragment;
import com.sih2020.railwayreservationsystem.Fragments.ServiceFragment;
import com.sih2020.railwayreservationsystem.Fragments.TripsFragment;
import com.sih2020.railwayreservationsystem.Models.SpinnerModel;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;
import com.sih2020.railwayreservationsystem.Utils.NetworkRequests;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener {

    private GradientDrawable mGradientDrawable;
    private String mIP, mPort;
    private SharedPreferences mSharedPreferences;
    private CoordinatorLayout mCL;
    private static final String LOG_TAG = "Main";
    private ArrayList<String> mAllPermissions;
    private NetworkRequests networkRequests;
    private LinearLayout mAppBar;
    private TextView mAppBarTv;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ProgressBar mProgressBar;
    private RelativeLayout mMain, mProgressRl;
    private ImageView loginButton;

    private FirebaseAuth mauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mauth=FirebaseAuth.getInstance();
        init();
        receiveClicks();

        if (mSharedPreferences.getBoolean(AppConstants.mDataGiven, false)) {
            AppConstants.mUrl = mSharedPreferences.getString(AppConstants.mUrlSaved, "");
            askPermissions();
        } else {
            setupAlertDialog();
            setRecentPnrSharedPreference();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mauth.getCurrentUser()==null){
            loginButton.setImageResource(R.drawable.loginicon);
        }
        else{
            loginButton.setImageResource(R.drawable.logout2icon);
        }
    }

    private void receiveClicks() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mauth.getCurrentUser()==null){
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                else {
                    FirebaseAuth.getInstance().signOut();
                    loginButton.setImageResource(R.drawable.loginicon);
                    Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setRecentPnrSharedPreference() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("RecentPnrNo","1,2,3,4,5");
        editor.putString("RecentPnrFrom","1,2,3,4,5");
        editor.putString("RecentPnrTo","1,2,3,4,5");
        editor.putString("RecentPnrDoj","1,2,3,4,5");
        editor.commit();
    }

    private void setupAlertDialog() {
        Rect displayRectangle = new Rect();
        Window window = MainActivity.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.ip_dialog, viewGroup, false);

        dialogView.findViewById(R.id.app_bar).setBackground(mGradientDrawable);
        Button submit = dialogView.findViewById(R.id.submit);
        final EditText ip = dialogView.findViewById(R.id.ip_et);
        final EditText port = dialogView.findViewById(R.id.port_et);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.CustomAlertDialog);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.setCancelable(false);
        alertDialog.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIP = ip.getText().toString();
                mPort = port.getText().toString();
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putBoolean(AppConstants.mDataGiven, true);
                AppConstants.mUrl = "http://" + mIP + ":" + mPort;
                editor.putString(AppConstants.mUrlSaved, AppConstants.mUrl);
                editor.apply();
                Log.e(LOG_TAG, AppConstants.mUrl);
                alertDialog.hide();
                askPermissions();
            }
        });
    }

    private void init() {
        mGradientDrawable = GenerateBackground.generateBackground();
        mSharedPreferences = getSharedPreferences(AppConstants.mPrefsName, MODE_PRIVATE);
        mCL = findViewById(R.id.coordinator);
        mAppBar = findViewById(R.id.app_bar);
        mAppBarTv = findViewById(R.id.app_bar_tv);
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        mAllPermissions = new ArrayList<>();
        mAllPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        networkRequests = new NetworkRequests(mCL, this, MainActivity.this);
        mProgressBar = findViewById(R.id.progress_bar);
        mMain = findViewById(R.id.main_layout);
        mProgressRl = findViewById(R.id.progress_bar_rl);

        loginButton=findViewById(R.id.home_login_button);
        loginButton.setVisibility(View.VISIBLE);
        if(mauth.getCurrentUser()!=null){
            loginButton.setImageResource(R.drawable.logouticon);
        }

        mTabLayout.setBackground(mGradientDrawable);
        mAppBar.setBackground(mGradientDrawable);
        mAppBarTv.setText("Ticket Reservation System");
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setOnTabSelectedListener(MainActivity.this);
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(3);
        mProgressRl.setVisibility(View.VISIBLE);
        mMain.setAlpha(0.2f);

    }

    private void askPermissions() {
        Dexter.withActivity(MainActivity.this).
                withPermissions(mAllPermissions).
                withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                        } else
                            Snackbar.make(mCL, "App needs access to storage for smooth functioning", Snackbar.LENGTH_LONG).show();
                        // check for permanent denial of any permission show alert dialog
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // open Settings activity
                            showSettingsDialog();
                        }
                        if (!AppConstants.mDataFetchCalled) {
                            networkRequests.fetchVersionNumber();
                            AppConstants.mDataFetchCalled = true;
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                   PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(
                        new PermissionRequestErrorListener() {
                            @Override
                            public void onError(DexterError error) {
                                Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                            }
                        })
                .onSameThread()
                .check();
    }


    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Permissions");
        builder.setMessage("App requires certain permissions to function");
        builder.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public void dataFetchComplete() {
        mProgressRl.setVisibility(View.GONE);
        mMain.setAlpha(1.0f);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        LinearLayout tabLayout = (LinearLayout) ((ViewGroup) mTabLayout.getChildAt(0)).getChildAt(tab.getPosition());
        TextView tabTextView = (TextView) tabLayout.getChildAt(1);
        tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.BOLD);
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HomeFragment.newInstance();
                case 1:
                    return TripsFragment.newInstance();
                case 2:
                    return ServiceFragment.newInstance();
            }
            return HomeFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Home";
                case 1:
                    return "Trips";
                case 2:
                    return "Services";
            }
            return "Home";
        }
    }
}
