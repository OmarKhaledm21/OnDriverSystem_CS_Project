package com.ondriver;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RideEndedEvent extends RideEvent {


    RideEndedEvent(@JsonProperty("ride") Ride ride) {
        super(ride);
    }

    @Override
    public String toString() {
        return "Ride Ended EVENT!!!!! Driver: " + ride.getDriver().toString() + " Completed Client's " + ride.getCustomer().toString() + " ride.";
    }
}
