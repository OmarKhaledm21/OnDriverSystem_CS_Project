package com.ondriver.NotificationService;

import com.ondriver.Model.Ride;

public class FinishedRideNotification extends Notification {
    public FinishedRideNotification(Ride ride) {
        super(ride);
    }

    public FinishedRideNotification(Ride ride, int id) {
        super(ride, id);
    }
}
