public class CustomerAcceptedEvent extends RideEvent {

    CustomerAcceptedEvent(Ride ride) {
        super(ride);
    }
    

    @Override
    public String toString() {
        return "Customer Accepted EVENT!!!! Customer: " + this.ride.getCustomer().toString() + " For this ride";

    }
}
