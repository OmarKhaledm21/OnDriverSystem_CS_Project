package com.ondriver.EventLogService;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ondriver.Model.Ride;

public class RideEndedEvent extends RideEvent {


    public RideEndedEvent(@JsonProperty("ride") Ride ride) {
        super(ride);
    }

    @Override
    public String toString() {
        return "Ride Ended EVENT!!!!! Driver: " + ride.getDriver().toString() + " Completed Client's " + ride.getCustomer().toString() + " ride.";
    }
}
