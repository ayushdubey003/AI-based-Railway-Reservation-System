package com.sih2020.railwayreservationsystem.Models;

public class PnrPassengerModel {
    private String pass_no,seat_no;

    public PnrPassengerModel(String pass_no, String seat_no) {
        this.pass_no = pass_no;
        this.seat_no = seat_no;
    }

    public void setPass_no(String pass_no) {
        this.pass_no = pass_no;
    }

    public void setSeat_no(String seat_no) {
        this.seat_no = seat_no;
    }

    public String getPass_no() {
        return pass_no;
    }

    public String getSeat_no() {
        return seat_no;
    }
}
