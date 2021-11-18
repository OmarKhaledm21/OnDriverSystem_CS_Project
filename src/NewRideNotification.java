public class NewRideNotification extends Notification{

    public NewRideNotification(Ride ride) {
        super(ride);
    }

    @Override
    public String toString() {
        return "New ride notification\n" + this.getRide().toString();
    }
}
