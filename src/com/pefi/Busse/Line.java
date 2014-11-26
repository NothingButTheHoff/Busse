package com.pefi.Busse;

/**
 * Created by pererikfinstad on 12/11/14.
 */
public class Line {
    public static final String TAG = "LineClass";

    public String lineNo,lineRef, destination,color;
    public int direction;

    public Line(String lineNo, String lineRef, String destination, String color) {
        this.lineNo      = lineNo;
        this.lineRef     = lineRef;
        this.destination = destination;
        this.color       = color;
    }

    public Line(String lineNo, String destination, String color) {
        this.lineNo      = lineNo;
        this.destination = destination;
        this.color       = color;
    }

    public Line(String lineNo, String destination){
        this.lineNo = lineNo;
        this.destination = destination;
    }

    public Line(String lineNo){
        this.lineNo = lineNo;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getLineRef() {
        return lineRef;
    }

    public void setLineRef(String lineRef) {
        this.lineRef = lineRef;
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

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (obj instanceof Line) {
            Line line = (Line) obj;
            if ((line.getLineNo() == null && lineNo == null) || (line.getLineNo().equals(lineNo)
                    &&
                ((line.getDestination() == null && destination == null) || line.getDestination().equals(destination)))) {
                return true;
            }
        }
        return false;

    }


} // end of class Line
