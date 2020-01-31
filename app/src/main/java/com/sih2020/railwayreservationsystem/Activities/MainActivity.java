package com.sih2020.railwayreservationsystem.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
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
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;
import com.sih2020.railwayreservationsystem.Utils.NetworkRequests;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GradientDrawable mGradientDrawable;
    private String mIP, mPort;
    private SharedPreferences mSharedPreferences;
    private CoordinatorLayout mCL;
    private static final String LOG_TAG = "Main";
    private ArrayList<String> mAllPermissions;
    private NetworkRequests networkRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        if (mSharedPreferences.getBoolean(AppConstants.mDataGiven, false)) {
            AppConstants.mUrl = mSharedPreferences.getString(AppConstants.mUrlSaved, "");
            askPermissions();
        } else
            setupAlertDialog();
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
        mAllPermissions = new ArrayList<>();
        mAllPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        networkRequests = new NetworkRequests(mCL, this);
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
                        networkRequests.fetchVersionNumber();
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

    // navigating settings app
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}
