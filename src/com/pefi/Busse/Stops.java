package com.pefi.Busse;

/**
 * Created by pererikfinstad on 12/11/14.
 */
public class Stops {
    public String name, shortName, district;
    public int id, zone;

    public Stops(String name, String shortName, String district, int id, int zone) {
        this.name = name;
        this.shortName = shortName;
        this.district = district;
        this.id = id;
        this.zone = zone;
    }

    public Stops(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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
