public class RideEndedEvent extends RideEvent {


    RideEndedEvent(Ride ride) {
        super(ride);
    }

    @Override
    public String toString() {
        return "Ride Ended EVENT!!!!! Driver: " + ride.getDriver().toString() + " Completed Client's " + ride.getCustomer().toString() + " ride.";
    }
}
