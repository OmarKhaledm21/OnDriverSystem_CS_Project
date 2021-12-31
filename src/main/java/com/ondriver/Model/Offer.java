package com.ondriver.Model;

import com.ondriver.Controllers.OnDriverSystem;

public class Offer {
    private Captain captain;
    private double offeredPrice;
    private Ride ride;

    public Offer(Captain captain, double offeredPrice, Ride ride){
        this.captain = captain;
        this.offeredPrice = offeredPrice;
        this.setRide(ride);

        double discounts = 0.0 ;

        if(this.ride.getDestination().getDiscount()!=0.0){
            discounts+= this.ride.getDestination().getDiscount();
        }

        if(this.getRide().getCustomer().isFirstRide()){
            discounts+= 0.1 ;
        }

        if(this.getRide().getPassenger_number() >= 2){
            discounts+= 0.05 ;
        }

        OnDriverSystem system = OnDriverSystem.getSystem();
        
        discounts += system.checkHoliday(system.getCurrentDate());

        if(this.getRide().getCustomer().getBirthDay().equals(system.getCurrentDate())){
            discounts += 0.1;
        }

        this.offeredPrice = offeredPrice* ( 1-discounts );
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
