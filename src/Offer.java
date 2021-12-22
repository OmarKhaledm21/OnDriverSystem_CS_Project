public class Offer {
    private Captain captain;
    private double offeredPrice;

    public Offer(Captain captain, double offeredPrice){
        this.captain = captain;
        this.offeredPrice = offeredPrice;
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
