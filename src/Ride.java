import java.util.LinkedList;

public class Ride {
    private Area source;
    private Area destination;
    private boolean rideStatus;
    private double price;
    private Driver driver;
    private LinkedList<Offer> priceOffers;

    public Ride(String source, String destination){
        this.source = new Area();
        this.source.setLocation(source);
        this.destination = new Area();
        this.destination.setLocation(destination);

        this.rideStatus = false;
        this.driver = null;

        this.priceOffers = new LinkedList<>();
    }

    ///////////////////////////////////// Getters and Setters /////////////////////////////////////
    public Area getSource() {
        return source;
    }

    public void setSource(Area source) {
        this.source = source;
    }

    public Area getDestination() {
        return destination;
    }

    public void setDestination(Area destination) {
        this.destination = destination;
    }

    public boolean isRideStatus() {
        return rideStatus;
    }

    public void setRideStatus(boolean rideStatus) {
        this.rideStatus = rideStatus;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public LinkedList<Offer> getPriceOffers() {
        return priceOffers;
    }

    public void setPriceOffers(LinkedList<Offer> priceOffers) {
        this.priceOffers = priceOffers;
    }
}
