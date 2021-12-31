package com.ondriver.EventLogService;

import com.ondriver.Model.Offer;
import com.ondriver.Model.Ride;

public class OfferPriceEvent extends RideEvent {
    private Offer offer;
    public OfferPriceEvent(Ride ride, Offer offer) {
        super(ride);
        this.offer=offer;
    }

    @Override
    public String toString() {
        return "Offer Price EVENT!!! Driver: "+ this.offer.getDriver().toString()+" Offered a price for this ride "+offer.getOfferedPrice();
    }
}