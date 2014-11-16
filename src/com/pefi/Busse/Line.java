package com.pefi.Busse;

/**
 * Created by pererikfinstad on 12/11/14.
 */
public class Line {
    public String name,transportation;
    public int id;

    public Line(String name, int id, String transportation) {
        this.name = name;
        this.id = id;
        this.transportation = transportation;
    }

    public Line(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransportation(){
        return transportation;
    }

    public void setTransportation(String transportation){
        this.transportation = transportation;
    }
}
