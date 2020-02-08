package com.sih2020.railwayreservationsystem.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.sih2020.railwayreservationsystem.Activities.MainActivity;
import com.sih2020.railwayreservationsystem.Activities.SeatAvailabilityActivity;
import com.sih2020.railwayreservationsystem.Activities.TrainLiveStatus;
import com.sih2020.railwayreservationsystem.Models.Station;
import com.sih2020.railwayreservationsystem.Models.Status;
import com.sih2020.railwayreservationsystem.Models.Train;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import eu.amirs.JSON;

public class NetworkRequests {
    private CoordinatorLayout mCL;
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private MainActivity mainActivity;
    private SeatAvailabilityActivity seatAvailabilityActivity;
    private TrainLiveStatus trainLiveStatus;
    private String version;
    private ArrayList<String> temp;
    String arrOfSplits[];
    public RequestQueue requestQueue;

    public NetworkRequests(CoordinatorLayout coordinatorLayout, Context context, MainActivity mainActivity) {
        mCL = coordinatorLayout;
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(AppConstants.mPrefsName, Context.MODE_PRIVATE);
        this.mainActivity = mainActivity;
        requestQueue = Volley.newRequestQueue(mContext);
    }

    public NetworkRequests(CoordinatorLayout coordinatorLayout, Context context, SeatAvailabilityActivity seatAvailabilityActivity) {
        mCL = coordinatorLayout;
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(AppConstants.mPrefsName, Context.MODE_PRIVATE);
        this.seatAvailabilityActivity = seatAvailabilityActivity;
        requestQueue = Volley.newRequestQueue(mContext);
        temp = new ArrayList<>();
        for (int i = 0; i < 6; i++)
            temp.add("unavailable".toUpperCase());
    }

    public NetworkRequests(CoordinatorLayout coordinatorLayout, Context context, TrainLiveStatus trainLiveStatus) {
        mCL = coordinatorLayout;
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(AppConstants.mPrefsName, Context.MODE_PRIVATE);
        this.trainLiveStatus = trainLiveStatus;
        requestQueue = Volley.newRequestQueue(mContext);
    }

    public void fetchVersionNumber() {

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
                                fetchTrainsList();
                            } else if (res != PackageManager.PERMISSION_GRANTED) {
                                fetchStationsList();
                                fetchTrainsList();
                            } else
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


        requestQueue.add(jsonObjectRequest);
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
            try {
                mainActivity.dataFetchComplete();
            } catch (Exception u) {
                return;
            }
        }
        parseData(data.toString(), 1);
        file = new File(dir, "trainsList.txt");

        data = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                data.append(line);
            }

            br.close();
        } catch (IOException e) {
            Snackbar.make(mCL, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            try {
                mainActivity.dataFetchComplete();
            } catch (Exception u) {
                return;
            }
        }
        parseData(data.toString(), 2);

    }

    private void fetchStationsList() {
        RequestQueue queue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.mUrl + "/list",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        writeToFile(response, 1);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(mCL, error.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
//                mainActivity.dataFetchComplete();
            }
        });

        queue.add(stringRequest);
    }

    private void fetchTrainsList() {
        RequestQueue queue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.mUrl + "/trains",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        writeToFile(response, 2);
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

    private void writeToFile(String data, int x) {
        String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = mContext.checkCallingOrSelfPermission(permission);
        if (res == PackageManager.PERMISSION_GRANTED) {
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/Android/data/" + mContext.getPackageName());
            dir.mkdirs();
            File file = null;
            if (x == 1)
                file = new File(dir, "stationList.txt");
            else
                file = new File(dir, "trainsList.txt");

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
        parseData(data, x);
    }

    private void parseData(String data, int x) {
        JSON parser = new JSON(data);
        JSONObject jsonObject = parser.getJsonObject();
        try {
            if (x == 1) {
                JSONArray stations = jsonObject.getJSONArray("stations");
                for (int i = 0; i < stations.length(); i++) {
                    JSONObject station = stations.getJSONObject(i);
                    AppConstants.mStationsName.add(new Station(station.getString("name"),
                            station.getString("code"),
                            new Pair<String, String>(station.getJSONArray("coordinates").getString(0), station.getJSONArray("coordinates").getString(1))));
                }
            } else {
                JSONArray trains = jsonObject.getJSONArray("trains");
                for (int i = 0; i < trains.length(); i++) {
                    JSONObject train = trains.getJSONObject(i);
                    AppConstants.mTrainListAtStartTime.add(new Pair<String, String>(train.getString("trainNo"), train.getString("trainName")));
                }
            }
        } catch (Exception e) {
            Snackbar.make(mCL, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
        }
        try {
            mainActivity.dataFetchComplete();
        } catch (Exception e) {
            return;
        }
    }

    public void fetchTrainsFromUrl(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        AppConstants.mTrainList.clear();
                        try {
                            JSONArray trainsArray = response.getJSONArray("trains");
                            for (int j = 0; j < trainsArray.length(); j++) {
                                JSONObject train = trainsArray.getJSONObject(j);
                                String trainName = train.getString("train name");
                                String trainNo = train.getString("train no");
                                String type = train.getString("type");
                                String zone = train.getString("zone");
                                ArrayList<String> arrTime = new ArrayList<>();
                                for (int i = 0; i < train.getJSONArray("arrival times").length(); i++)
                                    arrTime.add(train.getJSONArray("arrival times").getString(i));
                                ArrayList<String> avClasses = new ArrayList<>();
                                for (int i = 0; i < train.getJSONArray("available classes").length(); i++)
                                    avClasses.add(train.getJSONArray("available classes").getString(i));
                                ArrayList<String> canc = new ArrayList<>();
                                for (int i = 0; i < train.getJSONArray("cancelled/unknown").length(); i++)
                                    canc.add(train.getJSONArray("cancelled/unknown").getString(i));
                                ArrayList<String> deptTime = new ArrayList<>();
                                for (int i = 0; i < train.getJSONArray("departure times").length(); i++)
                                    deptTime.add(train.getJSONArray("departure times").getString(i));
                                ArrayList<String> rightTime = new ArrayList<>();
                                for (int i = 0; i < train.getJSONArray("right time").length(); i++)
                                    rightTime.add(train.getJSONArray("right time").getString(i));
                                ArrayList<String> codedRoutes = new ArrayList<>();
                                for (int i = 0; i < train.getJSONArray("route with codes").length(); i++)
                                    codedRoutes.add(train.getJSONArray("route with codes").getString(i));
                                ArrayList<String> namedRoutes = new ArrayList<>();
                                for (int i = 0; i < train.getJSONArray("route with names").length(); i++)
                                    namedRoutes.add(train.getJSONArray("route with names").getString(i));
                                ArrayList<String> days = new ArrayList<>();
                                for (int i = 0; i < train.getJSONArray("running days").length(); i++)
                                    days.add(train.getJSONArray("running days").getString(i));
                                ArrayList<String> sigDelay = new ArrayList<>();
                                for (int i = 0; i < train.getJSONArray("significant delay").length(); i++)
                                    sigDelay.add(train.getJSONArray("significant delay").getString(i));
                                ArrayList<String> sliDelay = new ArrayList<>();
                                for (int i = 0; i < train.getJSONArray("slight delay").length(); i++)
                                    sliDelay.add(train.getJSONArray("slight delay").getString(i));

                                AppConstants.mTrainList.add(new Train(trainName,
                                        trainNo,
                                        type,
                                        zone,
                                        arrTime,
                                        avClasses,
                                        canc,
                                        deptTime,
                                        rightTime,
                                        codedRoutes,
                                        namedRoutes,
                                        days,
                                        sigDelay,
                                        sliDelay,
                                        null,
                                        null));
                            }
                        } catch (JSONException e) {
                            readFromFile();
                            Snackbar.make(mCL, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                        } finally {
                            try {
                                seatAvailabilityActivity.trainListFetchComplete();
                            } catch (Exception u) {
                                return;
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(mCL, error.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });


        requestQueue.add(jsonObjectRequest);
    }

    private ArrayList<String> cleanList(ArrayList<String> arrayList) {
        ArrayList<String> arr = new ArrayList<>();
        for (int j = 0; j < arrayList.size(); j++) {
            String u = "";
            String s = arrayList.get(j);
            s = s.toUpperCase();
            boolean ws = true;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == ' ') {
                    if (ws) {
                        ws = false;
                        u = u + s.charAt(i);
                    }
                } else {
                    ws = true;
                    if (s.charAt(i) == '.')
                        continue;
                    u = u + s.charAt(i);
                }
            }
            arr.add(u);
        }
        return arr;
    }

    public void fetchSeatsData(final String trainNo, final String url, final int requestNo) {
        if (requestNo >= 3) {
            int pos = 0;
            try {
                seatAvailabilityActivity.mAdapter.mTrains.get(pos).setmSeats(temp);
                seatAvailabilityActivity.mAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        Ion.with(mContext)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null)
                            fetchSeatsData(trainNo, url, requestNo + 1);
                        else {
                            try {
                                JsonArray jsonArray = result.getAsJsonArray("seatavailability");
                                ArrayList<String> arrayList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.size(); i++)
                                    arrayList.add(jsonArray.get(i).getAsString());
                                arrayList = cleanList(arrayList);
                                int pos = 0;
                                for (int i = 0; i < AppConstants.mTrainList.size(); i++) {
                                    if (trainNo.equalsIgnoreCase(AppConstants.mTrainList.get(i).getmTrainNo().trim())) {
                                        pos = i;
                                        break;
                                    }
                                }
                                try {
                                    AppConstants.mSeatFetch.add(pos);
                                    AppConstants.mTrainList.get(pos).setmSeats(arrayList);
                                    seatAvailabilityActivity.mAdapter.mTrains.get(pos).setmSeats(arrayList);
                                    seatAvailabilityActivity.mAdapter.notifyDataSetChanged();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } catch (Exception ex) {
                                int pos = 0;
                                for (int i = 0; i < AppConstants.mTrainList.size(); i++) {
                                    if (trainNo.equalsIgnoreCase(AppConstants.mTrainList.get(i).getmTrainNo().trim())) {
                                        pos = i;
                                        break;
                                    }
                                }
                                try {
                                    AppConstants.mTrainList.get(pos).setmSeats(temp);
                                    seatAvailabilityActivity.mAdapter.notifyDataSetChanged();
                                } catch (Exception exx) {
                                    exx.printStackTrace();
                                }
                            }
                        }
                    }
                });
    }

    public void fetchFareData(final String trainNo, final String url, final int requestNo) {
        if (requestNo >= 3) {
            int pos = 0;
            try {
                seatAvailabilityActivity.mAdapter.mTrains.get(pos).setmSeats(temp);
                seatAvailabilityActivity.mAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        Ion.with(mContext)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null)
                            fetchFareData(trainNo, url, requestNo + 1);
                        else {
                            try {
                                JsonObject jsonObject = result.getAsJsonObject("fare");
                                JsonArray results;
                                if (AppConstants.mQuota.getmAbbreviation().equalsIgnoreCase("tq"))
                                    results = jsonObject.getAsJsonArray("adultTatkal");
                                else
                                    results = jsonObject.getAsJsonArray("adult");

                                String fare = "N/A";
                                for (int i = 0; i < results.size(); i++) {
                                    JsonObject resultsJSONObject = results.get(i).getAsJsonObject();
                                    try {
                                        fare = resultsJSONObject.get(AppConstants.mClass.getmAbbreviation()).getAsString();
                                    } catch (Exception eee) {
                                        continue;
                                    }
                                }
                                int pos = 0;
                                for (int i = 0; i < AppConstants.mTrainList.size(); i++) {
                                    if (trainNo.equalsIgnoreCase(AppConstants.mTrainList.get(i).getmTrainNo().trim())) {
                                        pos = i;
                                        break;
                                    }
                                }
                                try {
                                    AppConstants.mFareFetch.add(pos);
                                    AppConstants.mTrainList.get(pos).setmFare(fare);
                                    seatAvailabilityActivity.mAdapter.mTrains.get(pos).setmFare(fare);
                                    seatAvailabilityActivity.mAdapter.notifyDataSetChanged();
                                } catch (Exception ee) {
                                    ee.printStackTrace();
                                }
                                return;
                            } catch (Exception ee) {
                                int pos = 0;
                                for (int i = 0; i < AppConstants.mTrainList.size(); i++) {
                                    if (trainNo.equalsIgnoreCase(AppConstants.mTrainList.get(i).getmTrainNo().trim())) {
                                        pos = i;
                                        break;
                                    }
                                }
                                try {
                                    AppConstants.mTrainList.get(pos).setmFare("N/A");
                                    seatAvailabilityActivity.mAdapter.notifyDataSetChanged();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                            return;
                        }
                    }
                });
    }

    public void fetchTrainStatus(String url, final ArrayList<Status> route) {
        Ion.with(mContext)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Snackbar.make(mCL, "Error getting train status", Snackbar.LENGTH_LONG).show();
                        } else {
                            JsonArray jsonArray = result.get("status").getAsJsonArray();
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                route.add(new Status(jsonObject.get("arrDelay").getAsString(),
                                        jsonObject.get("delayDept").getAsString(),
                                        jsonObject.get("expArr").getAsString(),
                                        jsonObject.get("expDept").getAsString(),
                                        jsonObject.get("schArr").getAsString(),
                                        jsonObject.get("schDept").getAsString(),
                                        jsonObject.get("station").getAsString()));
                            }
                            trainLiveStatus.dataFetchComplete(route);
                        }
                    }
                });
    }
}