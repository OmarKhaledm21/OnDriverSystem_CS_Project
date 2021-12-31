package com.ondriver.EventLogService;

import com.ondriver.Model.Ride;

public class DriverArrivedEvent extends RideEvent {


    public DriverArrivedEvent(Ride ride) {
        super(ride);
    }

    @Override
    public String toString() {
        return "the Driver: "+ride.getDriver().toString()+" Arrived to the Client's location "+ride.getCustomer().toString();
    }
}
