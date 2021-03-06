package com.pefi.Busse;

/**
 * Created by pererikfinstad on 12/11/14.
 */
public class Stop {
    String name;
    String district;

    int id;
    int zone;

    public Stop(String name,  String district, int id, int zone) {
        this.name = name;
        this.district = district;
        this.id = id;
        this.zone = zone;
    }

    public Stop(String name,  String district, int id) {
        this.name = name;
        this.district = district;
        this.id = id;
    }

    public Stop(String name){
        this.name = name;
    }

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
