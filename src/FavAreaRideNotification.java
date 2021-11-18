public class FavAreaRideNotification extends Notification{
    public FavAreaRideNotification(Ride ride) {
        super(ride);
    }

    @Override
    public String toString() {
        return "New Fav Area Notification\n" + this.getRide().toString();
    }
}
