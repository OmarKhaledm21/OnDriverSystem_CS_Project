package com.ondriver.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ondriver.EventLogService.CustomerAcceptedEvent;
import com.ondriver.EventLogService.RideEndedEvent;
import com.ondriver.NotificationService.CustomerAcceptedRideNotification;
import com.ondriver.NotificationService.FinishedRideNotification;
import com.ondriver.Controllers.OnDriverSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Scanner;

@Component
public class Customer extends User {
    private Ride ride;
    private boolean isFirstRide;

    @Autowired
    public Customer(@JsonProperty("username")String username,
                    @JsonProperty("password")String password,
                    @JsonProperty("email")String email,
                    @JsonProperty("mobileNumber")String mobileNumber,
                    @JsonProperty("status") int status,
                    @JsonProperty("date")String date) {
        super(username, password, email, mobileNumber, status, date);
        this.status = status;
        ride = null;
        this.isFirstRide = false;
    }

    public boolean isFirstRide() {
        return OnDriverSystem.getSystem().isNewUser(this.getUsername());
    }

    public void setFirstRide(boolean firstRide) {
        isFirstRide = firstRide;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    @Override
    public String toString() {
        return "Customer name: " + this.getUsername() + " Mobile number: " + this.getMobileNumber() + " E-mail: " + this.getEmail() + " Status: " + this.getStatus();
    }

    public void requestRide() {
        if (this.ride != null) {
            System.out.println("You are already in a ride!");
        } else {
            OnDriverSystem system = OnDriverSystem.getSystem();
            ArrayList<Area> areas = system.getAreaList();
            Scanner input = new Scanner(System.in);

            System.out.println("Enter number of passengers: ");
            int passenger_number = input.nextInt();

            System.out.println("Enter source and destination areas: ");
            String src = input.next(), dest = input.next();
            Area source = new Area(src), destination = new Area(dest);
            OnDriverSystem.getSystem().addArea(source);
            OnDriverSystem.getSystem().addArea(destination);

            if (areas != null) {
                for (Area area : areas) {
                    if (area.getLocation().equals(src)) {
                        source = area;
                    }
                    if(area.getLocation().equals(dest)){
                        destination = area;
                    }
                }
            }

            if(destination.getDiscount() != 0.0){
                System.out.println("This area has 10% discount applied!");
            }

            this.ride = Ride.createRide(this, source, destination,passenger_number);

            system.newRideNotify(this.ride);
            System.out.println("Ride is requested, waiting for offers from drivers!");

        }
    }

    public void rateRide() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your rating 1-5: ");
        int rating = input.nextInt();
        while (true) {
            if (rating > 0 && rating <= 5) {
                this.ride.setRating(rating);
                System.out.println("You rate this ride " + rating + " out of 5!");
                break;
            } else {
                System.out.println("Please rate the ride from 1 to 5!");
                rating = input.nextInt();
            }
        }

    }

    public void checkAverageRating() {
        System.out.println("Average rating: " + this.ride.getDriver().getAverageRating());
    }

    public void checkOffers() {
        System.out.println("Checking for available offers....");
        Scanner input = new Scanner(System.in);
        ArrayList<Offer> offers = this.ride.getPriceOffers();

        if (offers.size() > 0 && offers!= null) {
            for (int i = 0; i < offers.size(); i++) {
                System.out.println(i + 1 + ". " + offers.get(i).toString());
                //TODO GET RATING FROM DB
                System.out.println("Average Rating: " + offers.get(i).getDriver().getAverageRating());
            }
            System.out.println("Type the number of offer you would like to accept or -1 for exit : ");
            int choice = input.nextInt();
            if (choice >= 0 && choice <= offers.size()) {

                Offer acceptedOffer = offers.get(choice - 1);
                this.ride.acceptOffer(acceptedOffer);
                acceptedOffer.getDriver().notify(new CustomerAcceptedRideNotification(this.ride));
                CustomerAcceptedEvent customerAcceptedEvent = new CustomerAcceptedEvent(this.ride);

                this.ride.addToEventLog(customerAcceptedEvent);
                //this.ride.setRideStatus(RideStatus.IN_PROGRESS);



                OnDriverSystem.getSystem().changeRideStatus(this.ride);
                OnDriverSystem.getSystem().addRide(this.ride);

                //this.ride.setRideStatus(RideStatus.IN_PROGRESS);
            } else if (choice == -1) {
                System.out.println("Back to menu!");
            } else {
                System.out.println("Invalid number!");
            }

        } else {
            System.out.println("No offers for this ride yet!");
        }
    }

    public void endRide() {
        if (this.ride != null) {
            if (this.ride.getRideStatus().equals(RideStatus.PENDING)) {
                System.out.println("Ride cancelled");
            } else {
                this.ride.setRideStatus(RideStatus.FINISHED);

                System.out.println("Rate the ride: ");
                rateRide();

                this.ride.getDriver().notify(new FinishedRideNotification(this.ride));
                RideEndedEvent rideEndedEvent = new RideEndedEvent(this.ride);

                this.ride.addToEventLog(rideEndedEvent);
                OnDriverSystem.getSystem().changeRideStatus(this.ride);
            }
        } else {
            System.out.println("You are not in a ride currently!");
        }
        ride = null;
        OnDriverSystem.getSystem().updateCustomerRides(this.getUsername());
    }

    public void getCurrentRideStatus() {
        if (this.ride == null) {
            System.out.println("You are not in a ride currently!");
        } else {
            System.out.println("The current ride is: " + this.ride.getRideStatus());
        }
    }

    @Override
    public void displayMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome " + this.getUsername() + " \n1- Request ride\n2- Check offers on my ride\n3- End ride\n4- Get current ride status\n5- Logout");
        int choice = input.nextInt();
        while (choice != 5) {
            switch (choice) {
                case 1:
                    requestRide();
                    break;
                case 2:
                    if (this.getRide() != null) {
                        checkOffers();
                    } else {
                        System.out.println("You are not in a ride currently!");
                    }
                    break;
                case 3:
                    endRide();
                    break;
                case 4:
                    getCurrentRideStatus();
                    break;
                default:
                    break;
            }
            System.out.println("Welcome " + this.getUsername() + " \n1- Request ride\n2- Check offers on my ride\n3- End ride\n4- Get current ride status\n5- Logout");
            choice = input.nextInt();
        }
    }
}
