package com.sih2020.railwayreservationsystem.Models;

public class Status {
    private String arrDelay, delayDept, expArr, expDept, schArr, schDept, station;

    public Status(String arrDelay, String delayDept, String expArr, String expDept, String schArr, String schDept, String station) {
        this.arrDelay = arrDelay;
        this.delayDept = delayDept;
        this.expArr = expArr;
        this.expDept = expDept;
        this.schArr = schArr;
        this.schDept = schDept;
        this.station = station;
    }

    public String getArrDelay() {
        return arrDelay;
    }

    public void setArrDelay(String arrDelay) {
        this.arrDelay = arrDelay;
    }

    public String getDelayDept() {
        return delayDept;
    }

    public void setDelayDept(String delayDept) {
        this.delayDept = delayDept;
    }

    public String getExpArr() {
        return expArr;
    }

    public void setExpArr(String expArr) {
        this.expArr = expArr;
    }

    public String getExpDept() {
        return expDept;
    }

    public void setExpDept(String expDept) {
        this.expDept = expDept;
    }

    public String getSchArr() {
        return schArr;
    }

    public void setSchArr(String schArr) {
        this.schArr = schArr;
    }

    public String getSchDept() {
        return schDept;
    }

    public void setSchDept(String schDept) {
        this.schDept = schDept;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
}
