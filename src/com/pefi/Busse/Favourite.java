package com.pefi.Busse;

/**
 * Created by pererikfinstad on 23/11/14.
 */
public class Favourite {
    public static final String TAG = "Favourite";

    String stopId, lineNo, destination, lineName, firstArrival, secondArrival, thirdArrival;
    int direction, id;

    public Favourite(String stopId, String lineNo, String destination, int direction) {
        this.stopId     = stopId;
        this.lineNo      = lineNo;
        this.destination = destination;
        this.direction   = direction;

    }

    public Favourite(int id,String lineName, String firstArrival, String secondArrival, String thirdArrival) {
        this.lineName      = lineName;
        this.thirdArrival  = thirdArrival;
        this.secondArrival = secondArrival;
        this.firstArrival  = firstArrival;
        this.id            = id;
    }

    public Favourite(){

    }


    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getFirstArrival() {
        return firstArrival;
    }

    public void setFirstArrival(String firstArrival) {
        this.firstArrival = firstArrival;
    }

    public String getSecondArrival() {
        return secondArrival;
    }

    public void setSecondArrival(String secondArrival) {
        this.secondArrival = secondArrival;
    }

    public String getThirdArrival() {
        return thirdArrival;
    }

    public void setThirdArrival(String thirdArrival) {
        this.thirdArrival = thirdArrival;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

} //end class Favourite
