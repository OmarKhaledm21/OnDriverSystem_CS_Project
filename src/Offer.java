public class Offer {
    private Driver driver;
    private double offeredPrice;

    public Offer(Driver driver,double offeredPrice){
        this.driver = driver;
        this.offeredPrice = offeredPrice;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public double getOfferedPrice() {
        return offeredPrice;
    }

    public void setOfferedPrice(double offeredPrice) {
        this.offeredPrice = offeredPrice;
    }

    @Override
    public String toString() {

        return "Driver name: " + this.driver.getUsername() + "\nOffer : "+ this.offeredPrice;
    }
}
