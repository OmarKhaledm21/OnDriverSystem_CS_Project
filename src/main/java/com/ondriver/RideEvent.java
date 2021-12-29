package com.ondriver;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class RideEvent {
    protected Ride ride;

    RideEvent(@JsonProperty("ride") Ride ride){
        this.ride=ride;
    }

    public Ride getRide(){
        return this.ride;
    }
}
