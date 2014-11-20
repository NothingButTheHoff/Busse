package com.pefi.Busse;

import android.util.Log;

/**
 * Created by pererikfinstad on 12/11/14.
 */
public class Line {
    public static final String TAG = "LineClass";

    public String lineNo,destination,color;
    public int id;

    public Line(String lineNo, int id, String destination, String color) {
        this.lineNo = lineNo;
        this.id = id;
        this.destination = destination;
        this.color = color;
    }

    public Line(String lineNo, String destination){
        this.lineNo = lineNo;
        this.destination = destination;
    }

    public Line(String lineNo){
        this.lineNo = lineNo;
    }

    public String getName() {
        return lineNo;
    }

    public void setName(String lineNo) {
        this.lineNo = lineNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDestination(){
        return destination;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDestination(String destination){
        this.destination = destination;
    }

    @Override
    public boolean equals(Object obj) {
        System.out.println("Override method called");
        // if the two objects are equal in reference, they are equal
        if (this == obj) {
            Log.d(TAG, "1");
            return true;
        } else if (obj == null) {
            Log.d(TAG, "2");
            return false;
        } else if (obj instanceof Line) {
            Line line = (Line) obj;
            if ((line.getName() == null && lineNo == null) || (line.getName().equals(lineNo)
                    &&
                ((line.getDestination() == null && destination == null) || line.getDestination().equals(destination)))) {
                Log.d(TAG, "3");
                return true;
            }
        }
        Log.d(TAG, "4");
        return false;
    }


} // end of class Line
