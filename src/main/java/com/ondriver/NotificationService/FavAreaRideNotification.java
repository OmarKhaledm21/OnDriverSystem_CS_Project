package com.ondriver.NotificationService;

import com.ondriver.Model.Ride;

public class FavAreaRideNotification extends Notification {
    public FavAreaRideNotification(Ride ride) {
        super(ride);
    }

    public FavAreaRideNotification(Ride ride, int id) {
        super(ride, id);
    }

    @Override
    public String toString() {
        return "New Fav Area Notification\n" + this.getRide().toString();
    }
}
