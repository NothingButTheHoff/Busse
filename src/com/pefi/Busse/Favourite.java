package com.pefi.Busse;

/**
 * Created by pererikfinstad on 23/11/14.
 */
public class Favourite {

    String stopId;
    String lineNo;
    String destination;
    String lineName;
    String firstArrival;
    String secondArrival;
    String thirdArrival;
    String lineColor;
    String fromStopName;

    int id;

    public Favourite(String stopId, String lineNo, String destination, String fromStopName) {
        this.stopId     = stopId;
        this.lineNo      = lineNo;
        this.destination = destination;
        this.fromStopName   = fromStopName;

    }

    public Favourite(int id, String lineName, String firstArrival, String secondArrival, String thirdArrival, String lineColor, String stopName) {
        this.lineName      = lineName;
        this.thirdArrival  = thirdArrival;
        this.secondArrival = secondArrival;
        this.firstArrival  = firstArrival;
        this.id            = id;
        this.lineColor     = lineColor;
        this.fromStopName  = stopName;
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

    public String getFromStopName() {
        return fromStopName;
    }

    public void setFromStopName(String fromStopName) {
        this.fromStopName = fromStopName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLineColor() {
        return lineColor;
    }

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }



} //end class Favourite
