package com.pefi.Busse;

/**
 * Created by pererikfinstad on 23/11/14.
 */
public class Favourite {
    public static final String TAG = "Favourite";

    String stopiId, lineNo, destination, lineName, firstArrival, secondArrival, thirdArrival;

    public Favourite(String stopiId, String lineNo, String destination) {
        this.stopiId = stopiId;
        this.lineNo = lineNo;
        this.destination = destination;
    }

    public Favourite(String lineName, String firstArrival, String secondArrival, String thirdArrival) {
        this.lineName = lineName;
        this.thirdArrival = thirdArrival;
        this.secondArrival = secondArrival;
        this.firstArrival = firstArrival;
    }

    public String getStopiId() {
        return stopiId;
    }

    public void setStopiId(String stopiId) {
        this.stopiId = stopiId;
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
} //end class Favourite
