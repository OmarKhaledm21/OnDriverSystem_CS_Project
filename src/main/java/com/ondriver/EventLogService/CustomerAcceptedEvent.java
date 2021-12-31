package com.ondriver.EventLogService;

import com.ondriver.Model.Ride;

public class CustomerAcceptedEvent extends RideEvent {

    public CustomerAcceptedEvent(Ride ride) {
        super(ride);
    }
    

    @Override
    public String toString() {
        return "Customer Accepted EVENT!!!! Customer: " + this.ride.getCustomer().toString() + " For this ride";

    }
}
