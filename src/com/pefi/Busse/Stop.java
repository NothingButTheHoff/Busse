package com.pefi.Busse;

/**
 * Created by pererikfinstad on 12/11/14.
 */
public class Stop {
    public String name, district;
    public int id, zone;

    public Stop(String name,  String district, int id, int zone) {
        this.name = name;
        this.district = district;
        this.id = id;
        this.zone = zone;
    }

    public Stop(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }
} //end Stops
