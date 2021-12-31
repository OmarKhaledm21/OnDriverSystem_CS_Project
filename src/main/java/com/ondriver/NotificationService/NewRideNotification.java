package com.ondriver.NotificationService;

import com.ondriver.Model.Ride;

public class NewRideNotification extends Notification {

    public NewRideNotification(Ride ride) {
        super(ride);
    }

    public NewRideNotification(Ride ride, int id) {
        super(ride, id);
    }

    @Override
    public String toString() {
        return "New ride notification\n" + this.getRide().toString();
    }
}
