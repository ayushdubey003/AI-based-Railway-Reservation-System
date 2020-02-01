package com.sih2020.railwayreservationsystem.Utils;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.sih2020.railwayreservationsystem.Activities.MainActivity;
import com.sih2020.railwayreservationsystem.Models.Station;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import eu.amirs.JSON;

public class NetworkRequests {
    private CoordinatorLayout mCL;
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private MainActivity mainActivity;
    private String version;

    public NetworkRequests(CoordinatorLayout coordinatorLayout, Context context, MainActivity mainActivity) {
        mCL = coordinatorLayout;
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(AppConstants.mPrefsName, Context.MODE_PRIVATE);
        this.mainActivity = mainActivity;
    }

    public void fetchVersionNumber() {
        RequestQueue queue = Volley.newRequestQueue(mContext);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppConstants.mUrl + "/list/version", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            version = response.getString("version");

                            String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
                            int res = mContext.checkCallingOrSelfPermission(permission);
                            if (!mSharedPreferences.getString(AppConstants.mStationsListVersionNo, "").equals(version)) {
                                fetchStationsList();
                            } else if (res != PackageManager.PERMISSION_GRANTED)
                                fetchStationsList();
                            else
                                readFromFile();
                        } catch (JSONException e) {
                            readFromFile();
                            Snackbar.make(mCL, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                readFromFile();
                Snackbar.make(mCL, error.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });


        queue.add(jsonObjectRequest);
    }

    private void readFromFile() {
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Android/data/" + mContext.getPackageName());
        File file = new File(dir, "stationList.txt");

        StringBuilder data = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                data.append(line);
            }

            br.close();
        } catch (IOException e) {
            Snackbar.make(mCL, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            mainActivity.dataFetchComplete();
        }
        parseData(data.toString());
    }

    private void fetchStationsList() {
        RequestQueue queue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.mUrl + "/list",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        writeToFile(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(mCL, error.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                mainActivity.dataFetchComplete();
            }
        });

        queue.add(stringRequest);
    }


    private void writeToFile(String data) {
        String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = mContext.checkCallingOrSelfPermission(permission);
        if (res == PackageManager.PERMISSION_GRANTED) {
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/Android/data/" + mContext.getPackageName());
            dir.mkdirs();
            File file = new File(dir, "stationList.txt");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data.getBytes());
                fileOutputStream.close();
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(AppConstants.mStationsListVersionNo, version);
                editor.apply();
            } catch (Exception e) {
                Snackbar.make(mCL, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                mainActivity.dataFetchComplete();
            }
        }
        parseData(data);
    }

    private void parseData(String data) {
        JSON parser = new JSON(data);
        JSONObject jsonObject = parser.getJsonObject();
        try {
            JSONArray stations = jsonObject.getJSONArray("stations");
            for (int i = 0; i < stations.length(); i++) {
                JSONObject station = stations.getJSONObject(i);
                AppConstants.mStationsName.add(new Station(station.getString("name"),
                        station.getString("code"),
                        new Pair<String, String>(station.getJSONArray("coordinates").getString(0), station.getJSONArray("coordinates").getString(1))));
            }
        } catch (Exception e) {
            Snackbar.make(mCL, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
        }
        mainActivity.dataFetchComplete();
    }
}
