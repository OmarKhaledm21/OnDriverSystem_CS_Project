public class CustomerAcceptedRideNotification extends Notification{

    public CustomerAcceptedRideNotification(Ride ride) {
        super(ride);
    }

    public CustomerAcceptedRideNotification(Ride ride, int id) {
        super(ride, id);
    }
}
