package com.sih2020.railwayreservationsystem.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;

import android.text.format.DateUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.sih2020.railwayreservationsystem.Adapters.AlternateRouteAdapter;
import com.sih2020.railwayreservationsystem.Models.AlternateModel;
import com.sih2020.railwayreservationsystem.Models.AlternateRoutesModel;
import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;
import com.sih2020.railwayreservationsystem.Utils.NetworkRequests;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AlternateRoutesActivity extends AppCompatActivity {

    ArrayList<AlternateModel> routes;
    RecyclerView recycler;
   public  AlternateRouteAdapter alternateRouteAdapter;
    ProgressDialog progressDialog;
    LinearLayout toolBar;
    SimpleDateFormat timeformat,dateformat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternate_routes);

        toolBar = findViewById(R.id.tool_bar_ar);
        toolBar.setBackground(GenerateBackground.generateBackground());
         dateformat=new SimpleDateFormat("yyyy-MM-dd");
         timeformat=new SimpleDateFormat("hh:mm");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Asd");
        progressDialog.setCancelable(false);
        progressDialog.show();
        routes = new ArrayList<>();
        fetchAlternateRoutes(this);
        recycler = findViewById(R.id.alternaterouterecycler);
        alternateRouteAdapter = new AlternateRouteAdapter(this, routes);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(alternateRouteAdapter);
        Log.i("onCreate: ", "hollllla");

    }


    public void fetchAlternateRoutes(final Context context) {
        Log.i("fetchAlternateRoutes: ", "jhollaa");
        SimpleDateFormat mformatter = new SimpleDateFormat("EEEE");
        String day = mformatter.format(AppConstants.mDate);
        Date date = new Date();
        String temp = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date);
        String current[] = temp.split(" ");
        Log.i("fetchAlternateRoutes: ", day);
        String uri1 = AppConstants.mUrl + "/alternates/" + AppConstants.mSourceStation.getmStationCode().toUpperCase() + "/" + AppConstants.mDestinationStation.getmStationCode().toUpperCase() + "/1/" + AppConstants.getDay(day) + "/120/360";///";//+current[0]+"/"+current[1]+"/"+AppConstants.mDate;
        final String uri2 = AppConstants.mUrl + "/alternates/" + AppConstants.mSourceStation.getmStationCode().toUpperCase() + "/" + AppConstants.mDestinationStation.getmStationCode().toUpperCase() + "/2/" + AppConstants.getDay(day) + "/120/360";///"+current[0]+"/"+current[1]+"/"+AppConstants.mDate;
        Log.i("fetchAlternateRoutes: ", uri1);

        final NetworkRequests networkRequests = new NetworkRequests(null, AlternateRoutesActivity.this, AlternateRoutesActivity.this);
        Ion.with(context)
                .load(uri1)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            AlternateModel route;
                            JsonArray array = result.getAsJsonArray("alternates");
                            Log.i("onCompleted:", array.toString());
                            for (int i = 0; i < array.size(); i++) {
                                route = new AlternateModel();
                                JsonObject singleroute = array.get(i).getAsJsonObject();
                                Log.i("Completed: ", singleroute.toString());
                                for (int j = 0; j < singleroute.getAsJsonArray("stations").size(); j++) {
                                    route.getmStations().add(singleroute.getAsJsonArray("stations").get(j).toString());
                                }
                                Date target;
                                int min=0;
                                for(int j=0;j<singleroute.getAsJsonArray("departureTime").size();j++){
                                    min+=Integer.parseInt(singleroute.getAsJsonArray("departureTime").get(j).getAsString());
                                    target=new Date(AppConstants.mDate.getTime()+min*60000);
                                    String time=timeformat.format(target);
                                    String date=dateformat.format(target);
                                    route.getmDepartureTime().add(time);
                                    route.getmDepartureDate().add(date);

                                }

                                for (int j = 0; j < singleroute.getAsJsonArray("trains").size(); j++) {
                                    route.getmTrains().add(singleroute.getAsJsonArray("trains").get(j).getAsString());
                                    Date date = AppConstants.mDate;
                                    String doj = (String) DateFormat.format("yyyy", date) + "-" + (String) DateFormat.format("MM", date) + "-" + (String) DateFormat.format("dd", date);
                                    String temp = AppConstants.mUrl + "/seats/" + singleroute.getAsJsonArray("trains").get(j).getAsString().trim() + "/" + singleroute.getAsJsonArray("stations").get(j).getAsString().trim() + "/" + singleroute.getAsJsonArray("stations").get(j + 1).getAsString().trim() + "/" + AppConstants.mClass.getmAbbreviation().trim() + "/" + doj + "/" + AppConstants.mQuota.getmAbbreviation().trim();
                                    networkRequests.fetchAlternateSeatsData(singleroute.getAsJsonArray("trains").get(j).getAsString().trim(), temp, 0, route, j);
                                    String temp1 = AppConstants.mUrl + "/fareenquiry" + "/" + singleroute.getAsJsonArray("trains").get(j).getAsString().trim() + "/" + singleroute.getAsJsonArray("stations").get(j).getAsString().trim() + "/" + singleroute.getAsJsonArray("stations").get(j + 1).getAsString().trim();
                                    networkRequests.fetchAlternateFares(singleroute.getAsJsonArray("trains").get(j).getAsString().trim(), temp1, 0, route, j);
                                }
                                route.setN(1);
                                route.setmTime(singleroute.get("time").getAsString());
                                routes.add(route);
                                alternateRouteAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();

//                                networkRequests.fetchAlternateFareDate()
                            }
//                            alternateRouteAdapter.notifyDataSetChanged();


//                            if(routes.size()==0)
//                            {
//                                Ion.with(context).load(uri2).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
//                                    @Override
//                                    public void onCompleted(Exception e, JsonObject result) {
//                                       if(e!=null)
//                                       {   progressDialog.dismiss();
//                                           Log.i( "onCompleted0:",result.getAsJsonArray().toString());
//                                           AlternateRoutesModel route=new AlternateRoutesModel();
//                                           JsonArray array=result.getAsJsonArray("alternates");
//                                           for(int i=0;i<array.size();i++)
//                                           {
//                                               JsonObject object=array.get(i).getAsJsonObject();
//                                               for(int j=0;j<object.getAsJsonArray("stations").size();j++)
//                                               {
//                                                   route.getStations().add(object.getAsJsonArray("stations").get(j).toString());
//                                               }
//
//                                               for(int j=0;j<object.getAsJsonArray("trainnos").size();j++)
//                                               {
//                                                   route.getTrainnos().add(object.getAsJsonArray("trainnos").get(j).toString());
//                                               }
//                                               route.setN(2);
//                                               route.setTime(object.get("time").toString());
//                                               routes.add(route);
//
//                                           }
//
//                                           alternateRouteAdapter.notifyDataSetChanged();
//
//                                       }
//                                    }
//                                });
//                            }

                        } else {
//                            Ion.with(context).load(uri2).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
//                                @Override
//                                public void onCompleted(Exception e, JsonObject result) {
//                                    if(e!=null)
//                                    {   progressDialog.dismiss();
//                                        Log.i( "onCompleted1:",result.getAsJsonArray().toString());
//                                        AlternateRoutesModel route=new AlternateRoutesModel();
//                                        JsonArray array=result.getAsJsonArray("alternates");
//                                        for(int i=0;i<array.size();i++)
//                                        {
//                                            JsonObject object=array.get(i).getAsJsonObject();
//                                            for(int j=0;j<object.getAsJsonArray("stations").size();j++)
//                                            {
//                                                route.getStations().add(object.getAsJsonArray("stations").get(j).toString());
//                                            }
//
//                                            for(int j=0;j<object.getAsJsonArray("trainnos").size();j++)
//                                            {
//                                                route.getTrainnos().add(object.getAsJsonArray("trainnos").get(j).toString());
//                                            }
//                                            route.setN(2);
//                                            route.setTime(object.get("time").toString());
//                                            routes.add(route);
//
//                                        }
//
//                                        alternateRouteAdapter.notifyDataSetChanged();
//
//
//
//                                    }
//                                    else
//                                    {
//                                        //Unable to fetch
//                                        progressDialog.dismiss();
//                                        Log.i("onCompleted: ",e.getMessage());
//                                    }
//                                }
//
//                            });

                            progressDialog.dismiss();
                            Log.i("onCompleted:kj ", e.getMessage());
                        }
                    }
                });


    }
}
