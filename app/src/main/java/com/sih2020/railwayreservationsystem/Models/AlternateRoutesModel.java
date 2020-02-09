package com.sih2020.railwayreservationsystem.Models;

import java.util.ArrayList;

public class AlternateRoutesModel {

    int n;
    ArrayList<String> stations,trainnos;
    String time;

    public AlternateRoutesModel()
    {
                 stations=new ArrayList<>();
                 trainnos=new ArrayList<>();
    }
    public AlternateRoutesModel(int n,ArrayList<String> stations,ArrayList<String> trainnos,String time)
    {
        stations=new ArrayList<>();
        trainnos=new ArrayList<>();
        this.stations=stations;
        this.trainnos=trainnos;
        this.n=n;
        this.time=time;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getN() {
        return n;
    }

    public ArrayList<String> getStations() {
        return stations;
    }

    public ArrayList<String> getTrainnos() {
        return trainnos;
    }

    public String getTime() {
        return time;
    }
}
