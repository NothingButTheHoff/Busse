package com.pefi.Busse;

/**
 * Created by pererikfinstad on 12/11/14.
 */
public class Line {
    public String name,transportation,color;
    public int id;

    public Line(String name, int id, String transportation, String color) {
        this.name = name;
        this.id = id;
        this.transportation = transportation;
        this.color = color;
    }

    public Line(String name){
        this.name = name;
    }

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setTransportation(String transportation){
        this.transportation = transportation;
    }
}
