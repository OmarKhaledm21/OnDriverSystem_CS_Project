package com.ondriver;
public abstract class Notification {
    private Ride ride;
    private int _id;

    public Notification(Ride ride){
        this.ride = ride;
    }

    public Notification(Ride ride, int id){
        this._id = id;
        this.ride = ride;
    }
    public Ride getRide() {
        return ride;
    }
}
