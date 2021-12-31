package com.ondriver.EventLogService;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ondriver.Model.Ride;

public abstract class RideEvent {
    protected Ride ride;

    public RideEvent(@JsonProperty("ride") Ride ride){
        this.ride=ride;
    }

    public Ride getRide(){
        return this.ride;
    }
}
