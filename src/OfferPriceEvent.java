public class OfferPriceEvent extends  RideEvent{
    private Offer offer;
    OfferPriceEvent(Ride ride,Offer offer) {
        super(ride);
        this.offer=offer;

    }

    @Override
    public String toString() {
        return "Offer Price EVENT!!! Driver: "+ this.offer.getDriver().toString()+" Offered a price for this ride "+offer.getOfferedPrice();

    }
}


