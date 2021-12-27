public class Offer {
    private Captain captain;
    private double offeredPrice;
    private Ride ride;

    public Offer(Captain captain, double offeredPrice, Ride ride){
        this.captain = captain;
        this.offeredPrice = offeredPrice;
        this.setRide(ride);
        if(this.ride.getDestination().getDiscount()!=0.0){
            this.offeredPrice = offeredPrice * ( 1 - this.ride.getDestination().getDiscount() );
        }
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public Captain getDriver() {
        return captain;
    }

    public void setDriver(Captain captain) {
        this.captain = captain;
    }

    public double getOfferedPrice() {
        return offeredPrice;
    }

    public void setOfferedPrice(double offeredPrice) {
        this.offeredPrice = offeredPrice;
    }

    @Override
    public String toString() {
        return "Driver name: " + this.captain.getUsername() + "\nOffer : "+ this.offeredPrice;
    }
}
