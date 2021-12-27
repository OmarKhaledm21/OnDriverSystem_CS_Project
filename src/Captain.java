import java.util.ArrayList;
import java.util.Scanner;

public class Captain extends User {
    private String nationalID;
    private String licenseNumber;
    private double averageRating;
    private ArrayList<Notification> notificationList;
    private Ride ride;

    private ArrayList<Ride> ridesHistory;
    private Area currentLocation;

    public Captain() {
        this.averageRating = 0.0;
        this.notificationList = new ArrayList<>();
        this.ride = null;
        this.ridesHistory = new ArrayList<>();
        nationalID = "";
        licenseNumber = "";
        this.status = 0;
        currentLocation = null;
    }

    public Captain(String username, String password, String email, String mobileNumber, String nationalID, String licenseNumber, Area currentLocation, int status) {
        super(username, password, email, mobileNumber, status);
        this.nationalID = nationalID;
        this.licenseNumber = licenseNumber;
        this.averageRating = 0.0;
        this.notificationList = new ArrayList<>();
        this.ride = null;
        this.ridesHistory = new ArrayList<>();
        this.currentLocation = new Area("default");
    }
    ///////////////////////////////////// Getters and Setters /////////////////////////////////////
    @Override
    public String toString() {
        return "Captain Name: " + this.getUsername() +
                ", National ID: " + this.getNationalID() +
                ", License Number: " + this.getLicenseNumber() +
                ", Average Rating: " + this.getAverageRating() +
                ", Mobile Number: " + this.getMobileNumber() +
                ", Email: " + this.getEmail() +
                ", Current Location: " + this.getCurrentLocation();
    }

    public void setRidesHistory(ArrayList<Ride>ridesHistory){
        this.ridesHistory = ridesHistory;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public Ride getRide() {
        return this.ride;
    }

    public void getCurrentRideStatus() {
        if (this.ride == null) {
            System.out.println("You are not in a ride currently!");
        } else {
            System.out.println("The current ride is: " + this.ride.getRideStatus());
        }
    }

    public void setAverageRating() {
        int sumRating = 0;
        for (Ride ride : ridesHistory) {
            sumRating += ride.getRating();
        }
        if (ridesHistory.size() == 0) {
            this.averageRating = 0;
        } else {
            this.averageRating = (double) sumRating / ridesHistory.size();
        }
        OnDriverSystem.getSystem().updateCaptain(this);
    }

    public Area getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Area currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void offerPrice(Ride ride) {
        System.out.println("Enter your offer: ");
        Scanner input = new Scanner(System.in);
        double price = input.nextDouble();
        Offer offeredPrice = new Offer(this, price, ride);
        ride.addOffer(offeredPrice);
        OfferPriceEvent offerPriceEvent = new OfferPriceEvent(ride, offeredPrice);
        ride.addToEventLog(offerPriceEvent);
    }

    public void notify(Notification notification){
        if (notification instanceof CustomerAcceptedRideNotification){
                this.ride = notification.getRide();
                this.ride.setDriver(this);
        }else {
                if (notification instanceof FinishedRideNotification) {
                    this.currentLocation = ride.getDestination();
                    OnDriverSystem.getSystem().driverMoved(this);
                    this.ridesHistory.add(ride);
                    setAverageRating();
                    this.ride = null;
                } else {
                    this.notificationList.add(notification);
                }
            }
    }
    

    public void manageNotification() {
        int notificationIndex = 0;
        int choice = 0;

        while (notificationIndex != -1) {
            for (int i = 0; i < notificationList.size(); i++) {
                if (!(notificationList.get(i).getRide().getRideStatus().equals(RideStatus.PENDING))) {
                    notificationList.remove(i);
                    i--;
                }
            }
            if (notificationList.isEmpty()) {
                System.out.println("No new notifications");
                break;
            } else {
                for (int i = 0; i < this.notificationList.size(); i++) {
                    System.out.println(i + 1 + ". " + this.notificationList.get(i).toString());
                }
                Scanner input = new Scanner(System.in);
                System.out.println("Please enter ride number \nEnter -1 to exit");
                notificationIndex = input.nextInt();
                if (notificationIndex == -1) {
                    continue;
                }
                if (notificationIndex <= notificationList.size()) {
                    System.out.println("1- Offer Price 2- Reject Ride");
                    choice = input.nextInt();
                    if (choice == 1) {
                        offerPrice(notificationList.get(notificationIndex - 1).getRide());
                        notificationList.remove(notificationIndex - 1);
                    } else if (choice == 2) {
                        notificationList.remove(notificationIndex - 1);
                    } else {
                        System.out.println("Invalid input, returning to menu!");
                    }
                }
            }
        }
    }

//TODO DBBBB
    public void addFavouriteArea() {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter area name: ");
        String location = input.nextLine();
        Area favouriteArea = new Area(location);
        boolean found = false;
        for (Area area : OnDriverSystem.getSystem().getAreaList()) {
            if (location.equals(area.getLocation())) {
                area.addToPinnedDrivers(this);
                found = true;
            }
        }
        if (!found) {
            favouriteArea.addToPinnedDrivers(this);
            OnDriverSystem.getSystem().addArea(favouriteArea);
        }
    }

    public void listRatings() {
        if (ridesHistory.isEmpty()) {
            System.out.println("No rides");
        } else {
            for (Ride ride : ridesHistory) {
                System.out.println(ride.toString() + "\nRating: " + ride.getRating());
            }
        }
    }

    public void moveToClient() {
        if (this.ride == null) {
            System.out.println("you are not in a ride at the moment");
        } else {
            if (this.currentLocation != ride.getSource()) {
                this.currentLocation = ride.getSource();
                OnDriverSystem.getSystem().driverMoved(this);
                RideEvent rideEvent = new DriverArrivedEvent(this.ride);
                OnDriverSystem.getSystem().saveEvent(rideEvent);
            } else {
                System.out.println("Your are Currently at the Pick up");
                RideEvent rideEvent = new DriverArrivedEvent(this.ride);
                OnDriverSystem.getSystem().saveEvent(rideEvent);
            }
        }
    }


    public void RideStatus() {
        System.out.println(this.ride.getRideStatus());
    }

    @Override
    public void displayMenu() {
        Scanner input = new Scanner(System.in);
        int choice = 0;
        while (choice != 6) {
            System.out.println("1. View and manage notifications\n2. View rides history\n3. Add a favourite area\n4. Move To Client Location\n5. Check Current Location\n6. logout");
            choice = input.nextInt();
            input.nextLine();
            switch (choice) {
                case 1:
                    manageNotification();
                    break;
                case 2:
                    listRatings();
                    break;
                case 3:
                    addFavouriteArea();
                    break;
                case 4:
                    moveToClient();
                    break;
                case 5:
                    System.out.println(getCurrentLocation().toString());
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }
}
