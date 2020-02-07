package com.sih2020.railwayreservationsystem.Models;

public class LiveStationModel {
    public String trainNo, schArrival, expArrival, trainName, delay, source, destination, pf, ddept;

    public LiveStationModel(String trainNo, String schArrival, String expArrival, String trainName, String delay, String source, String destination, String pf, String ddept) {
        this.trainNo = trainNo;
        this.schArrival = schArrival;
        this.expArrival = expArrival;
        this.trainName = trainName;
        this.delay = delay;
        this.source = source;
        this.destination = destination;
        this.pf = pf;
        this.ddept = ddept;
    }

    public String getDdept() {
        return ddept;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public void setSchArrival(String schArrival) {
        this.schArrival = schArrival;
    }

    public void setExpArrival(String expArrival) {
        this.expArrival = expArrival;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setPf(String pf) {
        this.pf = pf;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public String getSchArrival() {
        return schArrival;
    }

    public String getExpArrival() {
        return expArrival;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getDelay() {
        return delay;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getPf() {
        return pf;
    }
}
