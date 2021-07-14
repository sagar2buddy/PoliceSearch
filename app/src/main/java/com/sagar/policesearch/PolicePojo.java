package com.sagar.policesearch;

import java.util.Objects;

public class PolicePojo {

    private String sno;
    private String policeStation;
    private String district;
    private String state;
    private String phoneNumber;

    public PolicePojo(String sno, String policeStation, String district, String state, String phoneNumber) {
        this.sno = sno;
        this.policeStation = policeStation;
        this.district = district;
        this.state = state;
        this.phoneNumber = phoneNumber;
    }

    public PolicePojo(){

    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getPoliceStation() {
        return policeStation;
    }

    public void setPoliceStation(String policeStation) {
        this.policeStation = policeStation;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolicePojo that = (PolicePojo) o;
        return Objects.equals(sno, that.sno) &&
                Objects.equals(policeStation, that.policeStation) &&
                Objects.equals(district, that.district) &&
                Objects.equals(state, that.state) &&
                Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sno, policeStation, district, state, phoneNumber);
    }

    @Override
    public String toString() {
        return "PolicePojo{" +
                "sno='" + sno + '\'' +
                ", policeStation='" + policeStation + '\'' +
                ", district='" + district + '\'' +
                ", state='" + state + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
