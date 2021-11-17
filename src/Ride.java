import java.util.ArrayList;
import java.util.Scanner;

public class Ride {
    private Area source;
    private Area destination;
    private RideStatus rideStatus;
    private int rating;
    private double price;
    private Driver driver;
    private ArrayList<Offer> priceOffers;

    public Ride(Area source, Area destination) {
        this.source = source;
        this.destination = destination;

        this.rideStatus = RideStatus.PENDING;
        this.driver = null;

        this.priceOffers = new ArrayList<>();
    }

    ///////////////////////////////////// Getters and Setters /////////////////////////////////////
    public Area getSource() {
        return source;
    }

    public Area getDestination() {
        return destination;
    }

    public double getPrice() {
        return price;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public ArrayList<Offer> getPriceOffers() {
        return priceOffers;
    }

    public void setPriceOffers(ArrayList<Offer> priceOffers) {
        this.priceOffers = priceOffers;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void addOffer(Offer offer) {
        this.priceOffers.add(offer);
    }

    public String getRideStatus(){
        return this.rideStatus.toString();
    }

    public void viewOffers() {
        for (int i = 0; i < this.priceOffers.size(); i++) {
            System.out.println(i + ". " + priceOffers.get(i).toString());
        }
        Scanner in = new Scanner(System.in);
        int selectedOffer = -1;
        do {
            System.out.println("Please choose an offer number\nOr enter -1 to exit");
            selectedOffer = in.nextInt();

            if (selectedOffer == -1)
                break;

            if (selectedOffer > priceOffers.size() - 1) {
                System.out.println("invalid choice");
                continue;
            }

            int choice = -1;
            System.out.println("you want to accept?\n1: Accept 2: Reject");
            choice = in.nextInt();
            switch (choice) {
                case 1 -> acceptOffer(this.priceOffers.get(selectedOffer));
                case 2 -> this.priceOffers.remove(selectedOffer);
                default -> System.out.println("please give me an answer.....");
            }

        } while (selectedOffer != -1);

    }

    public void acceptOffer(Offer offer) {
        this.driver = offer.getDriver();
        this.price = offer.getOfferedPrice();
        this.priceOffers = null;
        this.rideStatus=RideStatus.IN_PROGRESS;
    }

    @Override
    public String toString() {
        return "Source Area: " + this.source + "\nDestination Area: " + this.destination + "\nStatus: " + this.rideStatus.toString();
    }
}

